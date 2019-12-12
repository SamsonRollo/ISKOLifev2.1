public class Player extends Character {

	protected int speed = 3;

	public Player(Sprite sprite, int posx, int posy, int xZoom, int yZoom, int sizex, int sizey, int colx, int coly, Game game){
		setCharacter(sprite, posx, posy, xZoom, yZoom, sizex, sizey, colx, coly, game);
	}

	@Override
	public void update(Game game) {
		KeyBoard keyboard = game.getKeyListener();

		boolean move = false;
		int newDir = 0;
		int pastX = playerBody.x;
		int pastY = playerBody.y;

		if(keyboard.up()) {
			newDir = 3;
			move = true;
			xOffset=7;
			yOffset=5;
			playerBody.y -=speed;
		}
		if(keyboard.down()) {
			newDir = 0;
			move = true; 
			xOffset=7;
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

	@Override
	protected void updateDirection() {
		if(animatedSprite!=null) {
			animatedSprite.setAnimateRange(direction*3, (direction*3)+3);
		}
	}
}
