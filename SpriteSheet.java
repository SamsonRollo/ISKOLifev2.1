// package graphics

import java.awt.image.BufferedImage;

public class SpriteSheet {
	
	private final int WIDTH;
	private final int HEIGHT;
	private int[] pixels;
	private BufferedImage image;
	private boolean containedSprite = false;
	private int spriteWIDTH;
    private Sprite[] spriteList = null;
    
    public SpriteSheet() {}

	public SpriteSheet(BufferedImage sheet) {
		image = sheet;
		WIDTH = sheet.getWidth();
		HEIGHT = sheet.getHeight();

		pixels =  new int[WIDTH*HEIGHT];
		pixels = sheet.getRGB(0,0,WIDTH,HEIGHT, pixels, 0, WIDTH);
	}

	public void loadSprite(int spriteWIDTH, int spriteHEIGHT) {
		this.spriteWIDTH = spriteWIDTH;
		spriteList = new Sprite[(WIDTH/spriteWIDTH)*(HEIGHT/spriteHEIGHT)];

		int spriteIndex = 0;
		for(int y=0; y< HEIGHT; y += spriteHEIGHT) {
			for(int x=0; x<WIDTH; x += spriteWIDTH) {
				spriteList[spriteIndex] = new Sprite(this, x, y, spriteWIDTH, spriteHEIGHT);
				spriteIndex++;
			}
		}

		containedSprite = true;
	}

	public Sprite getSprite(int x, int y) {
		if(containedSprite) {
			int spriteIndex = x + y * (WIDTH/spriteWIDTH);

			if(spriteIndex< spriteList.length)
				return spriteList[spriteIndex];
			else
				System.out.println("range does not contain sprites");
		}
		else 
			System.out.println("Cannot get sprite");
		return null;
	}

	public Sprite[] getspriteLists() {
		return spriteList;
	}

	public int[] getPixels() {
		return pixels;
	}	

	public BufferedImage getImage() {
		return image;
	}
}