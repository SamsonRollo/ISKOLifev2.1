public class Rectangle {
	public int x,y,w,h;
	private int[] pixels;

	Rectangle(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	Rectangle() {
		this(0,0,0,0);
	}

	public void createRectangle(int color) {
		pixels = new int[w*h];

		for(int y=0; y<h; y++) {
			for(int x=0; x<w; x++)
				pixels[x+y*w] = color;
		}
	}

	public void createRectangle(int border, int color) {
		pixels = new int[w*h];

		for(int i = 0; i < pixels.length; i++)
			pixels[i] = Display.alphaChannel;

		for(int y = 0; y < border; y++)
			for(int x = 0; x < w; x++)
				pixels[x + y * w] = color;

		for(int y = 0; y < h; y++)
			for(int x = 0; x < border; x++)
				pixels[x + y * w] = color;

		for(int y = 0; y < h; y++)
			for(int x = w - border; x < w; x++)
				pixels[x + y * w] = color;

		for(int y = h - border; y < h; y++)
			for(int x = 0; x < w; x++)
				pixels[x + y * w] = color;
	}

	public boolean intersects(Rectangle otherRectangle)
	{
		if(x > otherRectangle.x + otherRectangle.w || otherRectangle.x > x + w)
			return false;

		if(y > otherRectangle.y + otherRectangle.h || otherRectangle.y > y + h)
			return false;

		return true;
	}

	public int[] getPixels() {
		if(pixels!=null) {	
			return pixels;	
		}
		else
			System.out.println("Error! no pixels found!");
		return null;
	}
}