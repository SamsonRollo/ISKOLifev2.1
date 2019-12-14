public class Mob extends Character {

	private int cycle = 0;
	private int tempdir = 4;
	protected int speed = 1;
	public static boolean running = false;

	public Mob(Sprite sprite, int posx, int posy, int xZoom, int yZoom, int sizex, int sizey, int colx, int coly, Game game){
		setCharacter(sprite, posx, posy, xZoom, yZoom, sizex, sizey, colx, coly, game);
	}

	@Override
	public void update(Game game) { //copy to player in implementing the ai
		int newDir = 0;
		int pastX = playerBody.x;
		int pastY = playerBody.y;	

		if(running){
			if(cycle==0){//d check
				newDir = 0;
				xOffset=5;
				yOffset=-7;
				playerBody.y +=speed;
			}
			else if(cycle==1){//l check
				newDir = 1;
				yOffset=-7; 
				xOffset=5;
				playerBody.x -= speed;
			}
			else if(cycle==2){//r
				newDir = 2;
				yOffset=-7; 
				xOffset=5;
				playerBody.x += speed;
			}
			else{//u
				newDir = 3;
				xOffset=5;
				yOffset=-7;
				playerBody.y -=speed;
			}
		}

		if(newDir!=direction){
			direction = newDir;
			updateDirection();
		}

		if(!running)
			animatedSprite.reset();


		if(running){
			collisionCheckRectangle.x = playerBody.x + xOffset;
			collisionCheckRectangle.y = playerBody.y + 30 + yOffset;

			Rectangle newCollisionRect = new Rectangle(collisionCheckRectangle.x, collisionCheckRectangle.y, collisionCheckRectangle.w, collisionCheckRectangle.h);

			boolean xMove = game.getMap().checkCollision(newCollisionRect);
			boolean yMove = game.getMap().checkCollision(newCollisionRect);

			if(xMove || yMove){
				if(xMove) 
					playerBody.x = pastX;
				
				if(yMove)
					playerBody.y = pastY;
					
				 cycle = (int) Math.floor((tempdir)%4);
				 tempdir++;
			}

			animatedSprite.update(game);
		}
		running =false;
	}

	@Override
	protected void updateDirection() {
		if(animatedSprite!=null) {
			animatedSprite.setAnimateRange(direction*8, (direction*8)+8);
		}
	}
}
