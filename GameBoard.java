
//known as map
public class GameBoard{

	public static final int WIDTH = 9;
	public static final int HEIGHT = 9;

	public int xHuman, yHuman, xAI, yAI;
	public GameTile[][] gameBoard;
	public boolean hasChange = false;
	private Tiles tiles;
	
	public GameBoard(Tiles tiles){
		this.tiles = tiles;
		gameBoard = new GameTile[WIDTH][HEIGHT];

		for(int i=0; i<WIDTH; i++)
			for(int j=0; j<HEIGHT; j++)
				gameBoard[i][j] = GameTile.FREE;

		//arbitrary positions of players
		xHuman = 0; 
		yHuman = 4;
		xAI = 8;
		yAI =4;

		gameBoard[xHuman][yHuman] = GameTile.HUMAN;
		gameBoard[xAI][yAI] = GameTile.AI;
	}

	//might have no use
	public GameBoard(GameBoard updatedGameBoard){
		gameBoard = new GameTile[WIDTH][HEIGHT];

		for(int i = 0; i<WIDTH; i++)
			for(int j = 0; j<HEIGHT; j++)
				this.gameBoard[i][j] = updatedGameBoard.gameBoard[i][j];

		this.xHuman = updatedGameBoard.xHuman;
		this.yHuman = updatedGameBoard.yHuman;
		this.xAI = updatedGameBoard.xAI;
		this.yAI = updatedGameBoard.yAI;

		hasChange = true;
	}

	public void renderBoard(Renderer renderer, int xZoom, int yZoom){
		for(int i = 0; i<WIDTH; i++)
			for(int j=0; j<HEIGHT; j++){
				if(gameBoard[i][j]==GameTile.BLOCKED)
					tiles.renderTile(2, renderer, i*60+30, j*60+30, xZoom, yZoom);
				else
					tiles.renderTile(0, renderer, i*60+30, j*60+30, xZoom, yZoom);
			}
	}

	public void move(boolean human, int x, int y){
		if(!canMove(human,x,y))
			return;

		if(human){
			gameBoard[xHuman][yHuman] = GameTile.FREE;
			gameBoard[x][y] = GameTile.HUMAN;
			xHuman = x;
			yHuman = y;
		}
		else{
			gameBoard[xAI][yAI] = GameTile.FREE;
			gameBoard[x][y] = GameTile.AI;
			xAI = x;
			yAI = y;
		}
	}

	public void destroy(int x, int y){
		if(!canDestroy(x,y))
			return;
		gameBoard[x][y] = GameTile.BLOCKED;

		// if(isGameOver())
		// 	Game.state = Game.State.OVER;
	}

	public boolean inGameBounds(int x, int y){
		return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
	}

	public int getX(boolean human){ //refactor
		if(human)
			return xHuman;
		else
			return xAI;
	}

	public int getY(boolean human){ //refactor
		if(human)
			return yHuman;
		else
			return yAI;
	}

	public boolean canDestroy(int x, int y){
		if(!inGameBounds(x,y))
			return false;
		if(!inGameBounds(x,y))
			return false;

		return gameBoard[x][y] == GameTile.FREE;
	}

	public boolean canMove(boolean human, int x, int y){
		if(!inGameBounds(x,y))
			return false;
		//System.out.println("Evaluayte"+x+" "+y+" "+xHuman+" "+yHuman);
		if(gameBoard[x][y] != GameTile.FREE)
			return false;

		if(human){
			if((x==xHuman && y==yHuman) || ((Math.abs(xHuman-x)>1) || (Math.abs(yHuman-y)>1)))
				return false;
		}
		else if(!human){
			if((x==xAI && y==yAI) || (Math.abs(xAI-x)>1) || (Math.abs(yAI-y)>1))
				return false;
		}

		return true;
	}

	public boolean isGameOver(){
		return hasLost(true) || hasLost(false);
	}

	public boolean hasLost(boolean character){
		return getPossibleMoveCount(character)==0;
	}

	public boolean whoLost(){
		return hasLost(true);
	}

	public int getPossibleMoveCount(boolean human){
		int moveCount = 0;
		int x, y;

		if(human){
			x = xHuman;
			y = yHuman;
		}
		else{
			x = xAI;
			y = yAI;
		}

		for(int i = -1; i<=1; i++){
			for(int j = -1; j<=1; j++){
				if(inGameBounds(x+i,y+j)){
					try{
						if(gameBoard[x+i][y+j]==GameTile.FREE)
							moveCount++;
					}catch(ArrayIndexOutOfBoundsException e){};
				}
			}
		}
		return moveCount;
	}

	private enum GameTile{
		FREE, HUMAN, AI, BLOCKED
	}
}