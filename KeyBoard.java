import java.awt.event.*;

public class KeyBoard implements KeyListener, FocusListener {
	
	public boolean[] keys = new boolean[120];
	private Display display;

	public KeyBoard(Display display) {
		this.display = display;
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(key<keys.length)
			keys[key] = true;
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();	
		if(key<keys.length)
			keys[key] = false;
	}

	public void keyTyped(KeyEvent e) {}

	public void focusLost(FocusEvent f) {
		for(int i = 0; i<keys.length; i++)
			keys[i] = false;
	}

	public void focusGained(FocusEvent f) {}

	public boolean up() {
		return keys[KeyEvent.VK_UP];
	}

	public boolean down() {
		return keys[KeyEvent.VK_DOWN];
	}

	public boolean left() {
		return keys[KeyEvent.VK_LEFT];
	}

	public boolean right() {
		return keys[KeyEvent.VK_RIGHT];
	}

	public boolean enter() {
		return keys[KeyEvent.VK_ENTER];
	}
}