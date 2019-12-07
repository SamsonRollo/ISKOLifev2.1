import java.awt.Component;

//package main

public class Main {

    public static Frame frame;

    private Main() {
        frame = new Frame();

        frame.setVisible(true);
        frame.createBufferStrategy(3);

        Screen menu = new Menu(frame);
        Thread t1 = new Thread(menu);
        frame.add((Component) menu);
        t1.start();
    }

    public static void main(final String[] args) {
        new Main();
    }
    
}