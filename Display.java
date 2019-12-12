//package graphics

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.event.KeyEvent; //RACM


public class Display extends Canvas implements Runnable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static int alphaImage = 0x00ffff;
    public static int alphaChannel = 0xff00ffff;
    public KeyBoard keyboard = new KeyBoard(this);

    protected JFrame frame;
    protected Renderer renderer;

    public Display() {}

    public Display(JFrame frame) {
        this.frame = frame;
        renderer = new Renderer(frame.getWidth(), frame.getHeight());
    }
    public void run(){
        long startTime = System.nanoTime();
		double nspPerPass = 1000000000D / 60D;
		double deltaTime = 0;

		while(true) {
			long now = System.nanoTime();

			deltaTime += (now - startTime) / nspPerPass;
			while(deltaTime >= 1) {
				update();
				deltaTime--;
			}
			render();
			startTime = now;
		}
    }

    public void render(){}

    public void update(){}

    public BufferedImage loadImage(String path) {

		try{
			BufferedImage loadedImage =  ImageIO.read(Game.class.getResource(path));
			BufferedImage argbImage = new BufferedImage(loadedImage.getWidth(), loadedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
			argbImage.getGraphics().drawImage(loadedImage,0,0,null);

			return argbImage;
		}catch(IOException c){ return null; }

	}

    public KeyBoard getKeyListener() {
        return keyboard;
    }
}