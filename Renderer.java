//package graphics

import java.awt.Graphics;
import java.awt.image.*;
import java.util.Arrays;

public class Renderer {
	private Rectangle screenView;
	private BufferedImage screen;
	private int[] pixels;

    public Renderer() {}

	public Renderer(int w, int h) {
		screenView = new Rectangle(-390,-330,w,h);
		screen = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) screen.getRaster().getDataBuffer()).getData();
	}

	public void render(Graphics g) {
		g.drawImage(screen, 0, 0, screen.getWidth(), screen.getHeight(), null);
	}

	public void renderImage(BufferedImage image, int xPos, int yPos, int xZoom, int yZoom, boolean fixed) { 
		int[] imagePixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		renderPixels(imagePixels, xPos, yPos, image.getWidth(), image.getHeight(), xZoom, yZoom, fixed, Display.alphaImage);
    }

    public void renderSprite(Sprite sprite, int xPos, int yPos, int xZoom, int yZoom, boolean fixed) {
        renderPixels(sprite.getPixels(), xPos, yPos, sprite.getWidth(), sprite.getHeight(), xZoom, yZoom, fixed, Display.alphaChannel);
    }
    
    public void renderRectangle(Rectangle rectangle, int xZoom, int yZoom, boolean fixed) {
		int[] rectanglePixels = rectangle.getPixels();
		if(rectanglePixels != null)
			renderPixels(rectanglePixels,rectangle.x,rectangle.y,rectangle.w,rectangle.h, xZoom, yZoom, fixed, Display.alphaChannel);
	}

	public void renderRectangle(Rectangle rectangle, Rectangle off, int xZoom, int yZoom, boolean fixed) {
		int[] rectanglePixels = rectangle.getPixels();
		if(rectanglePixels != null)
			renderPixels(rectanglePixels,rectangle.x+off.x,rectangle.y+off.y,rectangle.w,rectangle.h, xZoom, yZoom, fixed, Display.alphaChannel);
	}

	public void renderPixels(int[] array, int xPos, int yPos, int renderW, int renderH, int xZoom, int yZoom, boolean fixed, int alpha) {
		for(int y=0; y<renderH; y++)
			for(int x=0; x<renderW; x++)
				for(int yZoomPos = 0; yZoomPos<yZoom; yZoomPos++)
					for(int xZoomPos = 0; xZoomPos<xZoom; xZoomPos++)
						setPixels(array[x+y*renderW],((x*xZoom)+xPos+xZoomPos),((y*yZoom)+yPos+yZoomPos), fixed, alpha);
	}

	private void setPixels(int pixel, int x, int y, boolean fixed, int alpha) {
		int pixelIndex = 0;
		if(!fixed) {
			if(x >= screenView.x && y >= screenView.y && x <= screenView.x + screenView.w && y <= screenView.y + screenView.h)
				pixelIndex = (x - screenView.x) + (y - screenView.y) * screen.getWidth();
		}
		else {
			if(x >= 0 && y >= 0 && x <= screenView.w && y <= screenView.h)
				pixelIndex = x + y * screen.getWidth();
		}

		if(pixels.length > pixelIndex && (pixel != alpha))
			pixels[pixelIndex] = pixel;
	}

	public Rectangle getCamera(){
		return screenView;
	}

	public void clearScreen(){
		for(int i = 0; i<pixels.length; i++)
			pixels[i]=0;
	}
}