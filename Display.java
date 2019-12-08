//package graphics

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferStrategy;
import java.lang.Runnable;
import javax.swing.JFrame;

public class Display extends Canvas implements Screen{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private JFrame frame;
    private Renderer renderer;

    public Display(JFrame frame) {
        this.frame = frame;
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

    public void render(){
        BufferStrategy bs = frame.getBufferStrategy();
        Graphics g = bs.getDrawGraphics();

        super.paint(g);
        g.setColor(Color.red);
        g.fillOval(50, 50, 50, 50);
        g.dispose();
        bs.show();
    }

    public void update(){}
}