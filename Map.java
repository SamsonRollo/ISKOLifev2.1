//package graphics

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;

public class Map{

    private Tiles tiles;
    private File mapFile;
    private int fillTileID = -1;
	private ArrayList<MappedTile> mappedTiles = new ArrayList<MappedTile>();
	private Rectangle rect1,rect2,rect3,rect4;

	private Block[][] blocks;
	private int blockWidth = 6;
	private int blockHeight = 6;
	private int blockStartX, blockStartY;
	private int blockPixelWidth = blockWidth * 30;
	private int blockPixelHeight = blockHeight * 30;

    public Map() {}

    public Map(Tiles tiles, File mapFile) {
		this.tiles = tiles;
		this.mapFile = mapFile;
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;

        try{
            Scanner scanner = new Scanner(mapFile);
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(!line.startsWith("#")) {
                    if(line.contains(":")) {
						String[] lineComponent = line.split(":");
						if(lineComponent[0].equals("Fill")) {
							fillTileID = Integer.valueOf(lineComponent[1]);
							continue;
						}
					}

					String[] lineComponent = line.split(",");
					if(lineComponent.length >= 3) {
						MappedTile mapped = new MappedTile(Integer.valueOf(lineComponent[0]),Integer.valueOf(lineComponent[1]), Integer.valueOf(lineComponent[2]));
						
						if(mapped.x < minX)
							minX = mapped.x;
						if(mapped.y < minY)
							minY = mapped.y;
						if(mapped.x > maxX)
							maxX = mapped.x;
						if(mapped.x > maxY)
							maxY = mapped.y;

						mappedTiles.add(mapped);
					}
				}
			}

			if(mappedTiles.size() == 0) {
				minX = -blockWidth;
				minY = -blockHeight;
				maxX = blockWidth;
				maxY = blockHeight;
			} 

			blockStartX = minX;
			blockStartY = minY;

			int blockSizeX = (maxX + blockWidth) - minX;
			int blockSizeY = (maxY + blockHeight) - minY;
			blocks = new Block[blockSizeX][blockSizeY];

			for(int i = 0; i < mappedTiles.size(); i++) {
				MappedTile mapped = mappedTiles.get(i);
				int blockX = (mapped.x - minX)/blockWidth;
				int blockY = (mapped.y - minY)/blockHeight;
				assert(blockX >= 0 && blockX < blocks.length && blockY >= 0 && blockY < blocks[0].length);
			
				if(blocks[blockX][blockY] == null)
					blocks[blockX][blockY] = new Block();

				blocks[blockX][blockY].mappedTiles.add(mapped); 
			}

		}catch(FileNotFoundException c) {c.printStackTrace();}
	}

	public void renderMap(Renderer render, int xZoom, int yZoom) {
		int xIncrement = 30 * xZoom;
		int yIncrement = 30 * yZoom;

		if(fillTileID >=0) {
			Rectangle camera = render.getCamera();

			for(int y = camera.y - yIncrement - (camera.y % yIncrement); y < camera.y + camera.h; y+=yIncrement) {
				for(int x = camera.x - xIncrement - (camera.x % xIncrement); x < camera.x + camera.w; x+=xIncrement) {
					tiles.renderTile(fillTileID, render, x, y, xZoom, yZoom);				
				}
			}
		}	

			int topLeftX = render.getCamera().x-xIncrement/2; 
			int topLeftY = render.getCamera().y-yIncrement/2;
			int bottomRightX = render.getCamera().x + render.getCamera().w + xIncrement;
			int bottomRightY = render.getCamera().y + render.getCamera().h + yIncrement;

			int leftBlockX = (topLeftX/xIncrement - blockStartX)/blockWidth;
			int blockX = leftBlockX;
			int blockY = (topLeftY/yIncrement - blockStartY)/blockHeight;

			int pixelX = topLeftX;
			int pixelY = topLeftY;


			while(pixelX < bottomRightX && pixelY < bottomRightY) {
				
				if(blockX >= 0 && blockY >= 0 && blockX < blocks.length && blockY < blocks[0].length) 
				{
					if(blocks[blockX][blockY] != null)
						blocks[blockX][blockY].render(render, xIncrement, yIncrement, xZoom, yZoom);
				}

				blockX++;
				pixelX += blockPixelWidth;

				if(pixelX > bottomRightX) {
					pixelX = topLeftX;
					blockX = leftBlockX;
					blockY++;
					pixelY +=blockPixelHeight;
					if(pixelY > bottomRightY) 
						break;
				}
			}
	}   

	//REVISE
	public boolean checkCollision(Rectangle rect) {
		int tileWidth = 30;
		int tileHeight = 30;

		int topLeftX = (rect.x - 120)/tileWidth;
		int topLeftY = (rect.y - 120)/tileHeight;
		int bottomRightX = (rect.x + rect.w + 120)/tileWidth;
		int bottomRightY = (rect.y + rect.h + 120)/tileHeight;

		for(int x = topLeftX; x < bottomRightX; x++)
			for(int y = topLeftY; y < bottomRightY; y++) {
				MappedTile tile = getTile(x, y);
				if(tile != null) {
					int collisionType = tiles.getCollisionType(tile.id);

					switch(collisionType) {

						case 0:
								Rectangle tileRectangleWhole = new Rectangle(tile.x*tileWidth, tile.y*tileHeight, tileWidth, 30);
								if(tileRectangleWhole.intersects(rect))
									return true;
						case 1:
								Rectangle tileRectangleTop = new Rectangle(tile.x*tileWidth, tile.y*tileHeight, tileWidth, 30);
								if(tileRectangleTop.intersects(rect))
									return true;
						case 2:
								Rectangle tileRectangleLeft = new Rectangle(tile.x*tileWidth, tile.y*tileHeight, 30, tileHeight);
								if(tileRectangleLeft.intersects(rect))
									return true;
						case 3:
								Rectangle tileRectangleBottom = new Rectangle(tile.x*tileWidth, tile.y*tileHeight + tileHeight - 30, tileWidth, 30);
								if(tileRectangleBottom.intersects(rect))
									return true;
						case 4:
								Rectangle tileRectangleRight = new Rectangle(tile.x*tileWidth + tileWidth - 30, tile.y*tileHeight, 30, tileHeight);
								if(tileRectangleRight.intersects(rect))
									return true;
					}	
				}
			}

		return false;
	}

	public MappedTile getTile(int tileX, int tileY) {
		int blockX = (tileX - blockStartX)/blockWidth;
		int blockY = (tileY - blockStartY)/blockHeight;

		if(blockX < 0 || blockX >= blocks.length || blockY < 0 || blockY >= blocks[0].length)
			return null;

		Block block = blocks[blockX][blockY];

		if(block == null)
			return null;

		return block.getTile(tileX, tileY);
	}

	private class Block {
		public ArrayList<MappedTile> mappedTiles = new ArrayList<MappedTile>();

		public void render(Renderer render, int xIncrement, int yIncrement, int xZoom, int yZoom) {
			for(int tile = 0; tile< mappedTiles.size(); tile++) {
				MappedTile mapped = mappedTiles.get(tile);
				tiles.renderTile(mapped.id, render, mapped.x * xIncrement, mapped.y * yIncrement, xZoom, yZoom);
			}
		}
		public void removeTile(MappedTile tile) {
			mappedTiles.remove(tile);
		}

		public MappedTile getTile(int tileX, int tileY) 
		{
			for(int i = 0; i<mappedTiles.size(); i++) {
				MappedTile tile = mappedTiles.get(i);
				if(tile.x == tileX && tile.y == tileY)
					return tile;
			}
			return null;
		}
	}

	private class MappedTile {
		public int id, x, y;

		public MappedTile(int id, int x, int y) {
			this.id = id;
			this.x = x;
			this.y = y;
		}
	}
}
                    