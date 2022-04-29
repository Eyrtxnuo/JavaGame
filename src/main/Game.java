package main;

import entities.Player;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import levels.LevelManager;

/**
 *
 * @author matti
 */
public class Game  implements Runnable{

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET=120;
    private final int UPS_SET=200;
    private LevelManager levelManager;
    
    private Player player;
    
    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE =1.5f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
    public final static int COORD_WIDTH = TILES_DEFAULT_SIZE * TILES_IN_WIDTH;
    public final static int COORD_HEIGHT = TILES_DEFAULT_SIZE * TILES_IN_HEIGHT;
    
                                   
    public Game(){
        initClasses();
        System.out.println("Game on!");
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus(); 
        startGameLoop();
    }
    
    private void initClasses() {
        levelManager = new LevelManager(this);
        player = new Player(50, 200, 64 , 40);
        player.loadLvlData(levelManager.getLevelOne().getLvlData());
        gamePanel = new GamePanel(this); 
     }
    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    public void update(){
        player.update();
        levelManager.update();
    }
    
    public void render(Graphics g){
        //Graphics gImmagine = tempImage.getGraphics();
        long a = System.nanoTime();
        float xOffset = GAME_WIDTH/2-player.getHitbox().x*SCALE;
        levelManager.draw(g , (xOffset<=0)?xOffset:0f, 0 );
        
        player.render(g, (xOffset<=0)?xOffset:0, 0);
        System.out.println(System.nanoTime()-a);
        //g.drawImage(tempImage, (int)(GAME_WIDTH/2-player.getHitbox().x*SCALE), 0, null);
        
    }
    
    @Override
    public void run() {
        double timePerFrame = 1000000000/FPS_SET;
        double timePerUpdate = 1000000000/UPS_SET;
        long previousTime = System.nanoTime();
        long frames=0;
        long updates=0;
        long lastCheck=System.currentTimeMillis();
        
        double deltaU = 0;
        double deltaF = 0;
        
        while (true) {         
            long currentTime = System.nanoTime();
            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;
            if(deltaU>=1){
                update();
                updates++;
                deltaU--;
            }
            if(deltaF>=1){
                gamePanel.repaint();
                frames++;
                deltaF--;
            }
            
            long now = System.currentTimeMillis();
            if(now - lastCheck >= 1000){
                lastCheck=now;
                System.out.println("FPS: "+ frames+" | UPS: "+updates);
                frames=0;
                updates=0;
            }
            
        }
        
    }

    public Player getPlayer() {
        return player;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }
    
    public void windowLostFocus(){
        player.resDirBools();
    }
 
    
}
