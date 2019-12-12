//package graphics

import java.awt.*;
import java.awt.image.*;
import java.lang.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Game extends Display{

    private SpriteSheet playerSheet, mobSheet, sheet;
    private Rectangle outline, clear;
	private Tiles tiles, fontTiles;
    private Map map;
    private GameObject[] gameObjects;
    private Character player, mob;
    //private String[] charac = {"resrc/bg/player_female.png","resrc/bg/player_male.png"};
    
    private int xZoom =1;
    private int yZoom =1;

    public Game(JFrame frame){
        super.frame = frame;
        super.renderer = new Renderer(frame.getWidth(), frame.getHeight());
        initGame();
    }

   // public Game(){
    private void initGame(){
        BufferedImage sheetImage =  loadImage("sprite_sheet.png");
		sheet = new SpriteSheet(sheetImage);
        sheet.loadSprite(30,30);

  //       BufferedImage fontImage = loadImage("resrc/bg/font_sheet.png");
		// fontSheet = new SpriteSheet(fontImage);
		// fontSheet.loadSprite(8,8);
        
        tiles = new Tiles(new File("Tiles.txt"), sheet);
        map = new Map(tiles, new File("Map.txt"));	
        
        //31x48 player size sprite
		BufferedImage playerImage = loadImage("player_male.png");
		playerSheet = new SpriteSheet(playerImage);
		playerSheet.loadSprite(31,48);
        AnimatedSprite playerAnimation = new AnimatedSprite(playerSheet, 6);

        //17x29 mob size
        BufferedImage mobImage = loadImage("mob.png");
        mobSheet = new SpriteSheet(mobImage);
        mobSheet.loadSprite(17,29);
        AnimatedSprite mobAnimation = new AnimatedSprite(mobSheet, 2);
        
        gameObjects = new GameObject[2];
		player = new Player(playerAnimation, 60, -10, 1, 1, 31, 48, this);
        mob = new Mob(mobAnimation, 43, 10, 1, 1, 17, 29, this);
		gameObjects[0]=player;
        gameObjects[1]=mob;
    }

    @Override
    public void render() {
        BufferStrategy bs = frame.getBufferStrategy();
        Graphics g = bs.getDrawGraphics();
        super.paint(g);

		map.renderMap(renderer, xZoom,yZoom);
		for(int i = 0; i< gameObjects.length; i++)
		    gameObjects[i].render(renderer,xZoom,yZoom);

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

        for(int i = 0; i< gameObjects.length; i++)
			gameObjects[i].update(this);
    }

    public Map getMap(){
        return map;
    }

    public Renderer getRender() {
        return renderer;
    }
}