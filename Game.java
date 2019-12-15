//package graphics

import java.awt.*;
import java.awt.image.*;
import java.lang.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Game extends Display{

    private SpriteSheet playerSheet, mobSheet, sheet, fontSheet;
    private BufferedImage lock = null, pixFrame = null;
    private Rectangle outline, clear;
	private Tiles tiles, fontTiles;
    private GameObject[] gameObjects;
    private Character player, mob;
    protected GameBoard gameBoard;
    public static State state = State.HUMANMOVE;
    //private String[] charac = {"resrc/bg/player_female.png","resrc/bg/player_male.png"};
    
    private int xZoom =1;
    private int yZoom =1;

    public Game(JFrame frame){
        super.frame = frame;
        super.renderer = new Renderer(frame.getWidth(), frame.getHeight());
        initGame();
    }

    private void initGame(){
        BufferedImage sheetImage =  loadImage("sprite_sheet.png");
		sheet = new SpriteSheet(sheetImage);
        sheet.loadSprite(60,60);

        lock = loadImage("lock.png");

        pixFrame = loadImage("Frame.png");

  //       BufferedImage fontImage = loadImage("font.png");
		// fontSheet = new SpriteSheet(fontImage);
		// fontSheet.loadSprite(14,18);
        
        //initialize the board
        tiles = new Tiles(new File("Tiles.txt"), sheet);
        gameBoard = new GameBoard(tiles);

        //31x48 player size sprite
		BufferedImage playerImage = loadImage("player_male.png");
		playerSheet = new SpriteSheet(playerImage);
		playerSheet.loadSprite(31,48);
        AnimatedSprite playerAnimation = new AnimatedSprite(playerSheet, 8);

        //17x29 mob size
        BufferedImage mobImage = loadImage("prof.png");
        mobSheet = new SpriteSheet(mobImage);
        mobSheet.loadSprite(60,60);
        AnimatedSprite mobAnimation = new AnimatedSprite(mobSheet, 8);
     
        gameObjects = new GameObject[2];
		player = new Player(playerAnimation, 31, 270, 1, 1, 31, 48,this);
        mob = new Mob(mobAnimation, 510, 270, 1, 1, 60, 60,this);
        gameObjects[1]=mob;
        gameObjects[0]=player;

        state = State.HUMANMOVE;
        Character.playerState = Character.PlayerState.MOVE;
    }

    @Override
    public void render() { //graphics mostly and check move updates
        BufferStrategy bs = frame.getBufferStrategy();
        Graphics g = bs.getDrawGraphics();
        super.paint(g);

		gameBoard.renderBoard(renderer, xZoom,yZoom);

        if(state == Game.State.HUMANMOVE)
            renderer.renderImage(lock, (player.tempX*60)+30, (player.tempY*60)+30, 1, 1, false);

		for(int i = 0; i< gameObjects.length; i++)
		    gameObjects[i].render(renderer,xZoom,yZoom);

        renderer.renderImage(pixFrame, 0, 0, 1, 1, false);

        renderer.render(g);
        g.dispose();
        bs.show();
        renderer.clearScreen();
    }

    @Override 
    public void update() {
        if(keyboard.esc() && keyboard.enter()){
            Display menu = new Menu(frame);
            Thread menuThread = new Thread(menu);
            frame.add((Component) menu);
            frame.addKeyListener(menu.getKeyListener());
            frame.addFocusListener(menu.getKeyListener());
            menuThread.start();
            try{
                Thread.currentThread().join();
            }catch(java.lang.Exception e){};
            stop=true;
        }

        if(gameBoard.isGameOver())
            System.out.println("Game is over");

        //actual game is evaluated in this part 
        for(int i = 0; i< gameObjects.length; i++)
			gameObjects[i].update(this);
    }

    public GameBoard getGameBoard(){
        return gameBoard;
    }

    public Renderer getRender() {
        return renderer;
    }

    public static enum State{
        HUMANMOVE, AIMOVE
    }
}