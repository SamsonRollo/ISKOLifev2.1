import java.util.ArrayList;

//import package.iskolife.graphics;

public class TreeThink{

	private int depth;
	private GameBoard gameBoard;
	private boolean human = false;
	public int xMove;
	public int yMove;

	public TreeThink(){}

	public TreeThink(GameBoard gameBoard, int depth){
		this.depth = depth;
		this.gameBoard = gameBoard;
	}
	//add refactor
	public void aiMove(){
		Node root = new Node(gameBoard,0,0,!human,false);
		Node maxMove = maxMove(root);
		
		if((maxMove.x==gameBoard.xAI) && (maxMove.y==gameBoard.yAI))
			maxMove = corneredValidMove(maxMove.x,maxMove.y);

		xMove = maxMove.x;
		yMove = maxMove.y;

		System.out.println("y: "+yMove+" x: "+xMove+" canMove: "+gameBoard.canMove(false,xMove,yMove)+" gametile state "+gameBoard.gameBoard[xMove][yMove]);

		gameBoard.move(human, maxMove.x, maxMove.y);
	}

	public void aiDestroy(){
		Node root = new Node(gameBoard,0,0,human,true);
		Node maxMove = maxMove(root);
		gameBoard.destroy(maxMove.x, maxMove.y);
	}

	private Node maxMove(Node root){
		double maxMoveValue = Double.NEGATIVE_INFINITY;
		double upperbound = Double.NEGATIVE_INFINITY;
		double lowerbound = Double.POSITIVE_INFINITY;
		Node maxMove = new Node(gameBoard,gameBoard.xAI,gameBoard.yAI,human,false);

		for(Node node : createMinMaxTree(root)){
			double curNodeValue = minMax(node, depth, upperbound, lowerbound);
			if(curNodeValue>maxMoveValue){
				maxMoveValue = curNodeValue;
				maxMove = node;
			}
		}
		return maxMove;
	}	

	protected ArrayList<Node> createMinMaxTree(Node node){
		ArrayList<Node> tree = new ArrayList<Node>();

		int xPos = node.gameBoard.getX(!node.human);
		int yPos = node.gameBoard.getY(!node.human);

		if(node.moved){ //destroy
			for (int i = -2; i <= 2; i++) {
				for (int j = -2; j <= 2; j++) {
					if (node.gameBoard.canDestroy(xPos + i, yPos + j)) { //add candestroy //acanmodify in general
						GameBoard gb = new GameBoard(node.gameBoard);
						gb.destroy(xPos + i, yPos + j); //add destroy method
						Node newNode = new Node(gb, xPos + i, yPos + j, node.human, false);
						tree.add(newNode);
					}
				}
			}
		}
		else { //move
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (node.gameBoard.canMove(!node.human, xPos + i, yPos + j)) { //canmove //can modify
						GameBoard gb = new GameBoard(node.gameBoard);
						gb.move(!node.human, xPos + i, yPos + j);//add move method to board
						Node newNode = new Node(gb, xPos + i, yPos + j, !node.human, true);
						tree.add(newNode);
					}
				}
			}
		}
		return tree;			
	}

	private double minMax(Node node, int depth, double upperbound, double lowerbound) {
		if(depth==0 || node.gameBoard.isGameOver()) {
			return evaluationOf(node);
		}

		ArrayList<Node> possibleMoves = createMinMaxTree(node);
		if((node.human == false && node.moved)){ //possible group debug
			for(Node moveNode : possibleMoves) {
				double currNodeValue = minMax(moveNode, depth-1, upperbound, lowerbound);
				upperbound = Math.max(currNodeValue, upperbound);
				if(lowerbound<=upperbound)
					break;
			}
			return upperbound;
		}

		else{
			for(Node moveNode : possibleMoves) {
				double currNodeValue = minMax(moveNode, depth-1, upperbound, lowerbound);
				lowerbound = Math.min(currNodeValue, lowerbound);
				if (lowerbound<=upperbound)
					break;
			}
			return lowerbound;
		}
	}

	private double evaluationOf(Node node){
		if(node.gameBoard.hasLost(human))
			return Double.NEGATIVE_INFINITY;
		if(node.gameBoard.hasLost(!human))
			return Double.POSITIVE_INFINITY;

		double playerPos = node.gameBoard.getPossibleMoveCount(human)- proximityToCenter(node.gameBoard, human);
		double otherPlayerPos = node.gameBoard.getPossibleMoveCount(!human)-proximityToCenter(node.gameBoard, !human);

		//System.out.println("moved "+((5 * playerPos / 4) - otherPlayerPos / 4) + " not "+(playerPos / 4 - (5 * otherPlayerPos / 4)));

		if(node.moved) //refactor re movement distance
			return (5 * playerPos / 4) - otherPlayerPos / 4;
		else
			return  playerPos / 4 - (5 * otherPlayerPos / 4);

	}

	private double proximityToCenter(GameBoard gameBoard, boolean human) {
		int xPos = gameBoard.getX(human);
		int yPos = gameBoard.getY(human);
		
		//possible refactor depending on the board w and h
		double centerx = (GameBoard.WIDTH - 1) / 2.;
		double centery = (GameBoard.HEIGHT - 1) / 2.;
		
		return Math.sqrt(Math.pow(centerx - xPos, 2) 
			           + Math.pow(centery - yPos, 2));
	}

	private Node corneredValidMove(int x, int y){

		for(int i = -1; i<=1; i++){
			for(int j = -1; j<=1; j++){
				if(gameBoard.inGameBounds(x+i,y+j)){
					try{
						if(gameBoard.gameBoard[x+i][y+j]==GameBoard.GameTile.FREE)
							return (new Node(gameBoard, x+i, y+j, false, true));
					}catch(ArrayIndexOutOfBoundsException e){};
				}
			}
		}
		//does not found tile
		return (new Node(gameBoard, x, y, false, true));
	}

	class Node{

		private GameBoard gameBoard;
		private boolean human, moved;
		private int x, y;

		Node(){}

		Node(GameBoard gameBoard, int x, int y, boolean human, boolean moved){
			this.gameBoard = gameBoard;
			this.x = x;
			this.y = y;
			this.human = human;
			this.moved = moved;
		}
	}

	public int getXMove(){
		return xMove;
	}

	public int getYMove(){
		return yMove;
	} 
}