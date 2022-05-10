package main;

import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

/**
 *
 * @author matti
 */
public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    public static boolean manualFrameAdvancing;
    
    private Playing playing;
    private Menu menu;
    
    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 1f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
    public final static int COORD_WIDTH = TILES_DEFAULT_SIZE * TILES_IN_WIDTH;
    public final static int COORD_HEIGHT = TILES_DEFAULT_SIZE * TILES_IN_HEIGHT;

    public Game() {
        initClasses();
        System.out.println("Game on!");
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();
        startGameLoop();
    }

    private void initClasses() {
        menu=new Menu(this);
        playing=new Playing(this);
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        switch (Gamestate.state) {
            case PLAYING:
                playing.update();
                break;
            case MENU:
                menu.update();
                break;
            case OPTIONS:
            case QUIT:
            default:
                System.exit(0);
                break;
        }
    }

    public void render(Graphics g) {
        switch (Gamestate.state) {
            case PLAYING:
                playing.draw(g);
                if(manualFrameAdvancing){
                    g.setColor(Color.red);
                    g.fillOval(0, 0, (int)(10*SCALE), (int)(10*SCALE));
                }
                break;
            case MENU:
                menu.draw(g);
                break;
            default:
                break;
        }
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000 / FPS_SET;
        double timePerUpdate = 1000000000 / UPS_SET;
        long previousTime = System.nanoTime();
        long frames = 0;
        long updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;
        
        while(true){
            long currentTime = System.nanoTime();
            
                
            if(!manualFrameAdvancing) {
                deltaU += (currentTime - previousTime) / timePerUpdate;
                deltaF += (currentTime - previousTime) / timePerFrame;
                
                if (deltaU >= 1) {
                    update();
                    updates++;
                    deltaU--;
                }
                if (deltaF >= 1) {
                    gamePanel.repaint();
                    frames++;
                    deltaF--;
                }

                long now = System.currentTimeMillis();
                if (now - lastCheck >= 1000) {
                    lastCheck = now;
                    System.out.println("FPS: " + frames + " | UPS: " + updates);
                    frames = 0;
                    updates = 0;
                }
            }
            previousTime = currentTime;
        }

    }
    
    public void windowLostFocus() {
        if(Gamestate.state==Gamestate.PLAYING){
            playing.getPlayer().resDirBools();
        }
    }

    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }
    
    public void newFrame(){
        update();
        gamePanel.repaint();
    }
    
    
}
