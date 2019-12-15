import java.lang.Thread;

public class Mob extends Character {

	protected int speed = 1;
	private int running = 0;
	public boolean created = false, visualizing=false;
	private TreeThink tt;
	private int arbx, arby;

	public Mob(Sprite sprite, int posx, int posy, int xZoom, int yZoom, int sizex, int sizey, Game game){
		setCharacter(sprite, posx, posy, xZoom, yZoom, sizex, sizey, 8, 4, game);
		currentX = 8;
		currentY = 4;
	}

	@Override
	public void update(Game game) { //copy to player in implementing the a
		//recieve dir after aiMove

		if(visualizing)
			aiUpdate();

		if(!visualizing && Game.state == Game.State.AIMOVE){

			if(playerState == PlayerState.MOVE){
				tt = new TreeThink(game.gameBoard, false, 3);
				tt.aiMove();
				arbx = currentX;
				arby = currentY;
				tempX = tt.getXMove();
				tempY = tt.getYMove();
				currentY = tt.getYMove();
				currentX = tt.getXMove();
				running = 60;
				visualizing = true;
			}

			else if(playerState == PlayerState.DESTROY){
				visualizing = true;
					tt = new TreeThink(game.gameBoard, false, 3);
					tt.aiDestroy();
					Time t = new Time();
					t.start();
			}
		}
	}

	@Override
	protected void updateDirection() {
		if(animatedSprite!=null) {
			animatedSprite.setAnimateRange(direction*8, (direction*8)+8);
		}
	}

	private void aiUpdate(){
		boolean isrunning = false;
		int newDir =0;

		if(running>0){
			
			if(arbx>tempX){//l check
				newDir = 1;
				playerBody.x -= speed;
			}
			else if(arbx<tempX){//r
				newDir = 2;
				playerBody.x += speed;
			}
			if(arby<tempY){//d check
				newDir = 0;
				playerBody.y +=speed;
			}
			else if(arby>tempY){//u
				newDir = 3;
				playerBody.y -=speed;
			}
			
			isrunning = true;
		}

		if(newDir!=direction){
			direction = newDir;
			updateDirection();
		}

		if(isrunning)
			running--;

		if(!(running>0)){
			animatedSprite.reset();
		}

		animatedSprite.update(game);

		if(isrunning && running==0) {
			Delay d = new Delay();
			d.start();
		}
	}

	class Time extends Thread{
		public void run(){
			try{
				sleep(1000);
			}catch(java.lang.Exception e){};
			game.state = Game.State.HUMANMOVE;
			playerState = PlayerState.MOVE;
			visualizing = false;
		}
	}

	//destroy delay
	class Delay extends Thread{
		public void run(){
			try{
				sleep(200);
			}catch(java.lang.Exception f){};
			visualizing = false;
			playerState = PlayerState.DESTROY;
		}
	}
}
