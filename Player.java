import  java.awt.image.BufferedImage;
import java.util.Random;

public class Player implements GameObject {

	private Rectangle playerBody;
	private Rectangle collisionCheckRectangle;
	private Renderer render;
	private int speed = 3;
	private Sprite sprite;
	private AnimatedSprite animatedSprite = null;
	private int direction = 0; //0-DOWN 1-LEFT 2-RIGHT 3-UP
	private Game game;
	
	private int xOffset=0;
	private int yOffset=0;

	public Player(Sprite sprite, int xZoom, int yZoom, Game game) {
		this.sprite = sprite;
		this.game = game;

		if(sprite != null && sprite instanceof AnimatedSprite)
			animatedSprite = (AnimatedSprite) sprite;

		updateDirection();
		playerBody = new Rectangle(60,-10,31,48);
		collisionCheckRectangle = new Rectangle(0,0,16,10);
	}

	private void updateDirection() {
		if(animatedSprite!=null) {
			animatedSprite.setAnimateRange(direction*3, (direction*3)+3);
		}
	}

	public void render(Renderer render, int xZoom, int yZoom) {
		
		if(animatedSprite!=null) {
			render.renderSprite(animatedSprite,playerBody.x,playerBody.y,xZoom,yZoom, false);
		}
		else if(sprite!=null)
			render.renderSprite(sprite,playerBody.x,playerBody.y,xZoom,yZoom, false);
		else
			render.renderRectangle(playerBody,xZoom,yZoom, false);
	}

	public void update(Game game) {
		KeyBoard keyboard = game.getKeyListener();

		boolean move = false;
		int newDir = 0;
		int pastX = playerBody.x;
		int pastY = playerBody.y;

		if(keyboard.up()) {
			newDir = 3;
			move = true;
			xOffset=6;
			yOffset=5;
			playerBody.y -=speed;
		}
		if(keyboard.down()) {
			newDir = 0;
			move = true; 
			xOffset=6;
			playerBody.y +=speed;
		}
		if(keyboard.left()) {
			newDir = 1;
			move = true; 
			xOffset=9;
			playerBody.x -= speed;
		}
		if(keyboard.right()) {
			newDir = 2;
			move = true;
			xOffset=5;
			playerBody.x += speed;
		}

		if(newDir!=direction) {
			direction=newDir;
			updateDirection();
		}

		if(!move) {
			animatedSprite.reset();
		}

		if(move) {
			collisionCheckRectangle.x = playerBody.x + xOffset;
			collisionCheckRectangle.y = playerBody.y + 30 + yOffset;

			Rectangle newCollisionRect = new Rectangle(collisionCheckRectangle.x, collisionCheckRectangle.y, collisionCheckRectangle.w, collisionCheckRectangle.h);

			boolean xMove = game.getMap().checkCollision(newCollisionRect);
			boolean yMove = game.getMap().checkCollision(newCollisionRect);

			if(xMove) 
				playerBody.x = pastX;
			if(yMove)
				playerBody.y = pastY;
		
			animatedSprite.update(game);
		}
	}

	public Rectangle getRectangle() {return playerBody;}
}
