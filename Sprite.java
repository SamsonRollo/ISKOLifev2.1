//package graphics

import java.awt.image.BufferedImage;

public class Sprite {
	
	protected int w,h;
    protected int[] pixels;
    
    public Sprite() {}

	public Sprite(SpriteSheet sheet, int x, int y, int w, int h) {
		this.w = w;
		this.h = h;

		pixels = new int[w*h];
		sheet.getImage().getRGB(x, y, w, h, pixels, 0, w);
	}

	public Sprite(BufferedImage image) {
		w = image.getWidth();
		h = image.getHeight();

		pixels = new int[w*h];
		image.getRGB(0,0,w,h,pixels,0,w);
	}

	public int getWidth() {
		return w;
	}

	public int getHeight() {
		return h;
	}

	public int[] getPixels() {
		return pixels;
	}
}