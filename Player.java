import  java.awt.image.BufferedImage;
import java.util.Random;

public class Player implements GameObject {

	// private ScoreRecord sr = new ScoreRecord();
	// private HighScoreCheck ch;
	private Rectangle playerBody;
	private Rectangle collisionCheckRectangle;
	//private LoadPage lp;
	private Renderer render;
	//private UPMusic mu;
	private int speed = 5;
	private Sprite sprite;
	private AnimatedSprite animatedSprite = null;
	private int direction = 0; //0-DOWN 1-LEFT 2-RIGHT 3-UP
	private Game game;
	
	private int xOffset=0;
	private int yOffset=0;
	public static boolean pressed = false, bgmPlay=false, movedM=false;

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

		if(keyboard.up() && !pressed) {
			newDir = 3;
			move = true;
			xOffset=6;
			yOffset=5;
			playerBody.y -=speed;
		}
		if(keyboard.down() && !pressed) {
			newDir = 0;
			move = true; 
			xOffset=6;
			playerBody.y +=speed;
		}
		if(keyboard.left() && !pressed) {
			newDir = 1;
			move = true; 
			xOffset=9;
			playerBody.x -= speed;
		}
		if(keyboard.right() && !pressed) {
			newDir = 2;
			move = true;
			xOffset=5;
			playerBody.x += speed;
		}

		// if(keyboard.enter() && !pressed) {
		// 	if(UPGame.type==6 || UPGame.type==7) {
		// 		pressed=true;
		// 		gameRandomizer();
		// 	}
		// 	if(UPGame.type==5 && ((sr.retrieveScore())>=50) && UPGame.sablay==false) {
		// 		pressed = true;
		// 		bgmPlay=true;
		// 		UPGame.sablay=true;
		// 		playerGlory();
		// 	}
		// 	if(UPGame.type==4) {
		// 		pressed = true;
		// 		UPGame.exit = true;
		// 	}
		// }

		if(newDir!=direction) {
			direction=newDir;
			updateDirection();
		}

		if(!move) {
			animatedSprite.reset();
		}

		if(move) {
			UPGame.start = false;

			if(bgmPlay)
				movedM = true;

			collisionCheckRectangle.x = playerBody.x + xOffset;
			collisionCheckRectangle.y = playerBody.y + 30 + yOffset;

			Rectangle newCollisionRect = new Rectangle(collisionCheckRectangle.x, collisionCheckRectangle.y, collisionCheckRectangle.w, collisionCheckRectangle.h);

			boolean xMove = game.getMap().checkCollision(newCollisionRect);
			boolean yMove = game.getMap().checkCollision(newCollisionRect);

			if(xMove) 
				playerBody.x = pastX;
			if(yMove)
				playerBody.y = pastY;

			UPGame.type = game.getMap().checkIntersection(playerBody);
		
			animatedSprite.update(game);
		}

		playerCamera(game.getRender().getCamera());
	}

	public void playerCamera(Rectangle screen) {
		screen.x = playerBody.x - (screen.w/2);
		screen.y = playerBody.y - (screen.h/2);
	}

	public Rectangle getRectangle() {return playerBody;}


	// public void playerGlory() {
    //     sr.recordScore(150);
    //     mu.clip2.stop();
	// 	UPMain.mu.playSablay();
    //     Time t3 = new Time();
    //     t3.start();
	// }

	// public void exitting() {
	// 	if(UPGame.loop<1){
	// 		RetrieveDetail rd = new RetrieveDetail();
    // 	    String nam = rd.getName();
    // 	    int scor = sr.retrieveScore();
    //     	ch = new HighScoreCheck(scor, nam);
    //     	UPGame.loop++;
    // 	}
    //     pressed=false;

    //     mu.clip2.stop();
    //     if(bgmPlay)
	// 		mu.clip.stop();
	// }

	class Time extends Thread
    {
        public void run()
        {  
            try{
                Thread.sleep(20898);
            }catch(Exception e) {e.printStackTrace();}
                mu.clip2.loop();
                bgmPlay=false;
        
    	}
    }
}
