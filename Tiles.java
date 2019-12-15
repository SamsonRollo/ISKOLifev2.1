//package graphics

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.ArrayList;

public class Tiles {
	
	private SpriteSheet sheet;
	private ArrayList<Tile> tileList = new ArrayList<Tile>();

	public Tiles(File tilesFile, SpriteSheet sheet) {
		this.sheet = sheet;

		try{
		Scanner scanner = new Scanner(tilesFile);
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if(!line.startsWith("#")) {
				String[] lineComponents = line.split("-");
				String tileName = lineComponents[0];
				int spriteTileX = Integer.valueOf(lineComponents[1]);
				int spriteTileY = Integer.valueOf(lineComponents[2]);
				Tile tile = new Tile(tileName, sheet.getSprite(spriteTileX, spriteTileY));

				if(lineComponents.length >=4) {
					tile.collision = true;
					tile.collisionCategory = Integer.valueOf(lineComponents[3]);
				}

				tileList.add(tile);
			}
		}
		}catch(FileNotFoundException cc) {cc.printStackTrace();}
	}

	//use for making map
	public void renderTile(int tileIndex, Renderer render, int xPos, int yPos, int xZoom, int yZoom) {
		if(tileIndex >= 0 && tileList.size()>tileIndex) {
			render.renderSprite(tileList.get(tileIndex).sprite, xPos, yPos, xZoom, yZoom, false);
		}
		else
			System.out.println("Tile id is out of bounds");
	}

	public int size() {
		return tileList.size();
	}

	public Sprite[] getSprites() {
		Sprite[] sprites = new Sprite[size()];

		for(int i=0; i<sprites.length; i++)
			sprites[i] = tileList.get(i).sprite;

		return sprites;
	}

	public int getCollisionType(int tileIndex) {
		if(tileIndex >= 0 && tileList.size()>tileIndex) {
			return tileList.get(tileIndex).collisionCategory;
		}
		else
			System.out.println("Tile id is out of bounds");

		return -1;
	}

	class Tile {
		public String tileName;
		public Sprite sprite;
		public boolean collision = false;
		public int collisionCategory = -1;

		public Tile(String tileName, Sprite sprite) {
			this.tileName = tileName;
			this.sprite = sprite;
		}
    }
}