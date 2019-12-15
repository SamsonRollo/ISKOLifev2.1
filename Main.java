import java.awt.Component;

//package main

public class Main extends Thread {

    public static Frame frame;

    private Main() {
        frame = new Frame();

        frame.setVisible(true);
        frame.createBufferStrategy(3);

        Display d1 = new Menu(frame);
        Thread t1 = new Thread(d1);
        frame.add((Component) d1);
        t1.start();

        frame.addKeyListener(d1.getKeyListener());
        frame.addFocusListener(d1.getKeyListener());
    }

    public static void main(final String[] args) {
        new Main();
    }
    
}