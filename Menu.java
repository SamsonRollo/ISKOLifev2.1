//package main

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class Menu extends Display{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int currentSelection = 1;
    private BufferedImage bg1 = null;

    public Menu(JFrame frame) {
        super.frame = frame;
        super.renderer = new Renderer(frame.getWidth(), frame.getHeight());//remove later
        bg1 =  loadImage("bg1.png");
    }

    @Override
    public void render(){
        BufferStrategy bs = frame.getBufferStrategy();
        Graphics g = bs.getDrawGraphics();
        super.paint(g);
        renderer.renderImage(bg1,0,0,1,1,true);
        renderer.render(g);

        updateSlection(g);

        g.dispose();
        bs.show();
        renderer.clearScreen();
    }

    public void update(){

        if(keyboard.up()) {
            if(currentSelection!=1)
                currentSelection--;
        }
        if(keyboard.down()) {
            if(currentSelection!=2)
                currentSelection++;
        }

        if(keyboard.enter() && currentSelection==1){
            Display game = new Game(frame);
            Thread gameThread = new Thread(game);
            frame.addKeyListener(game.getKeyListener());
            frame.addFocusListener(game.getKeyListener());
            frame.add((Component) game);
            gameThread.start();
            try{
                Thread.currentThread().join();
            }catch(java.lang.Exception e){};
            
            stop = true;
        }

    }

    public void updateSlection(Graphics g) {
        switch(currentSelection){
            case 1:
                g.setColor(Color.red);
                g.fillRect(400,250,100,50);
                g.setColor(Color.blue);
                g.fillRect(400,310,100,50);
                // g.fillRect(400,320,100,50);
                // g.fillRect(400,380,100,50);
                break;
            case 2:
                g.setColor(Color.red);
                g.fillRect(400,310,100,50);
                g.setColor(Color.blue);
                g.fillRect(400,250,100,50);
                // g.fillRect(400,320,100,50);
                // g.fillRect(400,380,100,50);
                break;
            // case 3:
            //     g.setColor(Color.red);
            //     g.fillRect(400,320,100,50);
            //     g.setColor(Color.blue);
            //     g.fillRect(400,200,100,50);
            //     g.fillRect(400,260,100,50);
            //     g.fillRect(400,380,100,50);
            //     break;
            // case 4:
            //     g.setColor(Color.red);
            //     g.fillRect(400,380,100,50);
            //     g.setColor(Color.blue);
            //     g.fillRect(400,200,100,50);
            //     g.fillRect(400,260,100,50);
            //     g.fillRect(400,320,100,50);
            //        break;
        }
    }
}