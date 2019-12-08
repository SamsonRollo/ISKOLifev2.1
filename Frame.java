// package graphics

import javax.swing.JFrame;

public class Frame extends JFrame{

    public Frame(){
        setSize(900,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setUndecorated(true);
		setResizable(false);
		setLayout(null);
    }
}