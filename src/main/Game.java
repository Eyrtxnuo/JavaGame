package main;

import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.MultiplayerMenu;
import gamestates.Playing;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import utils.Constants;

/**
 * Game class, contains everything, game root
 * @author matti
 */
public class Game implements Runnable {
    /** window object */
    private GameWindow gameWindow;
    /** panel object */
    private GamePanel gamePanel;
    /** game thread, updater */
    private Thread gameThread;
    /** Frame updates per second */
    public static final int FPS_SET = Math.max(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getRefreshRate(), 60);
    /** entities updates per second */
    public static final int UPS_SET = 200;

    /** debug variable, is manual frame advancing enabled*/
    public static boolean manualFrameAdvancing;
    
    /** Playing object */
    public static Playing playing;
    /** Menu object */
    private Menu menu;
    /**Multiplayer object*/
    private MultiplayerMenu multiplayermenu;
    
    /** Tiles size at scale 1 */
    public final static int TILES_DEFAULT_SIZE = 32;
    /** game scale, zoom */
    public static float SCALE;
    /** default windowed world tiles width, in fullscreen height has priority */
    public static float TILES_IN_WIDTH = 26;
    /** default  world tiles height */
    public static float TILES_IN_HEIGHT = 14;
    /** actual tiles size */
    public static float TILES_SIZE = TILES_DEFAULT_SIZE * SCALE;
    /** window calculated width */
    public static float GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    /** window calculated height */
    public static float GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
    /** game calculated cordiantes in width */
    public static float COORD_WIDTH = TILES_DEFAULT_SIZE * TILES_IN_WIDTH;
    /** game calculated cordiantes in height */
    public static float COORD_HEIGHT = TILES_DEFAULT_SIZE * TILES_IN_HEIGHT;

    
    /**
     * Constructor, need game scale and fullscreen status
     * @param scale
     * @param fullscreen 
     */
    public Game(float scale, boolean fullscreen) {
        if(fullscreen){
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            TILES_IN_WIDTH = screen.width/(screen.height/TILES_IN_HEIGHT);
        }
        changeScale(scale);
        
        initClasses();
        System.out.println("Game on!");
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel, fullscreen);
        gamePanel.requestFocus();
        startGameLoop();
    }
    
    
    /** change game scale, resize panel and recalculate sizes
     * @param scale new scale value*/
    public void changeScale(float scale){
        SCALE = scale;
        TILES_SIZE = TILES_DEFAULT_SIZE * SCALE;
        GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
        GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
        COORD_WIDTH = TILES_DEFAULT_SIZE * TILES_IN_WIDTH;
        COORD_HEIGHT = TILES_DEFAULT_SIZE * TILES_IN_HEIGHT;
        if(gamePanel != null && gameWindow != null){
            gamePanel.setPanelSize();
            gameWindow.repackPanel();  
        }
        Constants.updateScaleConsts();
    }
    
    /** initialises classes */
    private void initClasses() {
        menu=new Menu();
        multiplayermenu=new MultiplayerMenu();
        initPlaying(new Playing());
        
        /*
        playing=new PlayingMultiplayerServer(this);
        playing.initLevelManager();
        */
    }

    /** Generate a new thread, that will execute the run() function, then starts it */
    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Update method, redirect call to active Gamestate
     */
    public void update() {
        switch (Gamestate.state) {
            case PLAYING:
                playing.update();
                break;
            case MENU:
                menu.update();
                break;
            case MULTIPLAYERMENU:
                multiplayermenu.update();
                break;
            case QUIT:
            default:
                System.exit(0);
                break;
        }
    }

    /**
     * Render the current Gamestate
     * @param g Graphics the game is rendered to
     */
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
            case MULTIPLAYERMENU:
                multiplayermenu.draw(g);
                break;
            default:
                break;
        }
    }

    /** Thread methods, calls updates and repaints when needed */
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
                    gamePanel.repaint(0);
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
    
    /** Event on window lost focus, reset player movements, if playing */
    public void windowLostFocus() {
        if(Gamestate.state==Gamestate.PLAYING){
            playing.getPlayer().resDirBools();
        }
    }

    /** get menu reference
     @return menu reference */
    public Menu getMenu() {
        return menu;
    }

    /** get playing reference
     * @return playing reference */
    public static Playing getPlaying() {
        return playing;
    }
    
    /** get multiplayer menu reference
     * @return */
    public MultiplayerMenu getMultiplayerMenu(){
        return multiplayermenu;
    }

    /** debug use only, updates and paint the update instantly */
    public void newFrame() {
        update();
        gamePanel.repaint();
    }

    public static void initPlaying(Playing playing) {
        Game.playing = playing;
        Game.playing.initLevelManager();
        Game.playing.afterCreationInit();
    }

    /*public void initPlaying(Playing playing, LevelManager lm) {
        Game.playing = playing;
        Game.playing.levelManager = lm;
    }*/
    
    
    
    
}
