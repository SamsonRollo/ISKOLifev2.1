import  java.awt.image.BufferedImage;
import java.util.Random;

public class Character implements GameObject {

	protected Rectangle playerBody;
	protected Rectangle collisionCheckRectangle;
	protected Sprite sprite;
	protected AnimatedSprite animatedSprite = null;
	protected int direction = 0; //0-DOWN 1-LEFT 2-RIGHT 3-UP
	protected Game game;
	
	protected int xOffset=0;
	protected int yOffset=0;

	public Character(){}

	public void setCharacter(Sprite sprite, int posx, int posy, int xZoom, int yZoom, int sizex, int sizey, int colx, int coly, Game game) {
		this.sprite = sprite;
		this.game = game;

		if(sprite != null && sprite instanceof AnimatedSprite)
			animatedSprite = (AnimatedSprite) sprite;

		updateDirection();
		playerBody = new Rectangle(posx,posy,sizex,sizey);
		collisionCheckRectangle = new Rectangle(0,0,colx,coly);
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

	protected void updateDirection(){}

	public void update(Game game) {}

	public Rectangle getRectangle() {return playerBody;}
}
