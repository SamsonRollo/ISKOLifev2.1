import java.util.ArrayList;

//import package.iskolife.graphics;

public class TreeThink{

	private int depth;
	private GameBoard gameBoard;
	private boolean human;
	public int xMove;
	public int yMove;

	public TreeThink(){}

	public TreeThink(GameBoard gameBoard, boolean human, int depth){
		this.depth = depth;
		this.gameBoard = gameBoard;
		this.human= human;
	}
	//add refactor
	public void aiMove(){
		Node root = new Node(gameBoard,0,0,!human,false);
		Node maxMove = maxMove(root);
		gameBoard.move(human, maxMove.x, maxMove.y);
		xMove = maxMove.x;
		yMove = maxMove.y;
	}

	public void aiDestroy(){
		Node root = new Node(gameBoard,0,0,human,true);
		Node maxMove = maxMove(root);
		gameBoard.destroy(maxMove.x, maxMove.y);
	}

	private Node maxMove(Node root){
		double maxMoveValue = Double.NEGATIVE_INFINITY;
		Node maxMove = new Node(gameBoard,0,0,human,false);

		for(Node node : createMinMaxTree(root)){
			double curNodeValue = minMax(node, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
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
		if((node.human == this.human && node.moved) || node.human != this.human && !node.moved){ //possible group debug
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

		if(node.moved) //refactor re movement distance
			return (3 * playerPos / 2) - otherPlayerPos / 2;
		else
			return  playerPos / 2 - (3 * otherPlayerPos / 2);

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