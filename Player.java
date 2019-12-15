public class Player extends Character {

	protected int speed = 1;
	public boolean keyClick = false, visualizing=false;
	public boolean timeRunning = false, next;
	public int arbx, arby;
	private int moving =0;

	public Player(Sprite sprite, int posx, int posy, int xZoom, int yZoom, int sizex, int sizey, Game game){
		setCharacter(sprite, posx, posy, xZoom, yZoom, sizex, sizey, 0, 4, game);
		currentX = 0;
		currentY = 4;
	}

	@Override
	public void update(Game game) { //replace with mob probably putting it back to the parent
		KeyBoard keyboard = game.getKeyListener();

		if(Game.state == Game.State.HUMANMOVE && !visualizing){

			if(keyboard.up() && !keyClick && !timeRunning) {
				if(game.gameBoard.inGameBounds(tempX, tempY-1))
					tempY--;
				keyClick = true;
			}
			if(keyboard.down() && !keyClick  && !timeRunning) {
				if(game.gameBoard.inGameBounds(tempX, tempY+1))
					tempY++;
				keyClick = true;
			}
			if(keyboard.left() && !keyClick  && !timeRunning) {
				if(game.gameBoard.inGameBounds(tempX-1, tempY))
					tempX--;
				keyClick = true;
			}
			if(keyboard.right() && !keyClick  && !timeRunning) {
				if(game.gameBoard.inGameBounds(tempX+1, tempY))
					tempX++;
				keyClick = true;
			}

			if(keyboard.enter() && !keyClick  && !timeRunning){
				keyClick =true;
				if(playerState == PlayerState.MOVE){
					if(game.gameBoard.canMove(true, tempX,tempY)){ //getplayer y and x
						game.gameBoard.move(true, tempX, tempY); //after update iteration destroy
						moving=60;
						visualizing=true;
						arbx = currentX;
						arby =currentY;
						currentX = tempX;
						currentY = tempY;
					}
					else{
						tempX = currentX;
						tempY = currentY;
					}
				}

				else if(playerState == PlayerState.DESTROY){
					if(game.gameBoard.canDestroy(tempX,tempY)){
						game.gameBoard.destroy(tempX,tempY);
						tempY = currentY;
						tempX = currentX;
						DelayAI da = new DelayAI();
						da.start();
					}
				}
			}

			if(keyClick && !timeRunning){
				Timer t = new Timer();
				t.start();
			}
		}

		if(visualizing)
			humanUpdate();
	}

	@Override
	protected void updateDirection() {
		if(animatedSprite!=null) {
			animatedSprite.setAnimateRange(direction*3, (direction*3)+3);
		}
	}

	private void humanUpdate(){
		boolean isrunning = false;
		int newDir = 0;

			if(moving>0){
				if(tempX>arbx){
					playerBody.x+=speed;
					newDir = 2;
				}
				else if(tempX<arbx){
					playerBody.x-=speed;
					newDir = 1;
				}
				if(tempY>arby){
					playerBody.y+=speed;
					newDir = 0;
				}
				else if(tempY<arby){
					playerBody.y-=speed;
					newDir = 3;
				}
				isrunning = true;	
			}

			if(newDir!=direction) {
				direction=newDir;
				updateDirection();
			}

			if(isrunning)
				moving--;

			if(!(moving>0)) {
				animatedSprite.reset();
			}

			animatedSprite.update(game);

			if(isrunning && moving==0){
				Delay  d = new Delay();
				d.start();	
			}
	}

	//key delay
	class Timer extends Thread{
		public void run(){
			try{
				timeRunning = true;
				sleep(150);
				timeRunning = false;
			}catch(java.lang.Exception e){};
			keyClick = false;
		}
	}

	//destroy delay
	class Delay extends Thread{
		public void run(){
			try{
				sleep(100);
			}catch(java.lang.Exception f){};
			visualizing = false;
			playerState = PlayerState.DESTROY;
		}
	}

	//aimove
	class DelayAI extends Thread{
		public void run(){
			try{
				sleep(1000);
			}catch(java.lang.Exception g){};
			game.state = Game.State.AIMOVE;
			playerState = PlayerState.MOVE;
		}
	}
}
