package gamestates;

import entities.Enemy;
import entities.EnemyManager;
import entities.Player;
import entities.Projectile;
import entities.ProjectileManager;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import levels.LevelManager;
import main.Game;
import static main.Game.*;
import ui.DeathOverlay;
import ui.PauseOverlay;
import utils.AudioPlayer;
import utils.Constants;
import static utils.LoadSave.*;

public class Playing extends State implements Statemethods {

    public static LevelManager levelManager;
    private static Player player;
    public static EnemyManager enemies;
    private static boolean paused = false;
    private static boolean death=false;
    private PauseOverlay pauseOverlay;
    private DeathOverlay deathOverlay;
    public static ProjectileManager flyingAmmos;
    private float effXOffset;
    public static float pointerX, pointerY;
    private static final double PLAYER_GUN_RANDOMNESS=0.05;//in radians
    private static final int FIRE_SPEED = 100;
    private int fireTick = 0;
    private static int currentLevel = 0;
    

    public Playing(Game game) {
        super(game);
        initClasses();
    }

    private void initClasses() {
        player = new Player(50, 200);
        pauseOverlay = new PauseOverlay(this);
        deathOverlay=new DeathOverlay(this);
    }
    
    public void initLevelManager(){
        levelManager = new LevelManager(game);
        connectLevel();
    }
    
    public static void connectLevel() {
        enemies = levelManager.getLoadedLevel().getEnemies();
        flyingAmmos = levelManager.getLoadedLevel().getProj();
    }
    
    public static void reloadLevel(){
        enemies.stopAllThreads();
        levelManager.loadLevel(currentLevel);
        connectLevel();
        player.reset();
        death=false;
    }
    
    public static void loadLevel(int levelN){
        currentLevel = levelN;
        reloadLevel();
    }

    @Override
    public void update() {
        if (paused) {
            pauseOverlay.update();
            return;
        }
        if(death){
            deathOverlay.update();
            return;
        }
        if (player.getHitbox().x > (levelManager.getLoadedLevel().getWidthInTiles()-4)*Game.TILES_DEFAULT_SIZE
            && currentLevel+1 < LEVELS_NUMBER) {
            loadLevel(++currentLevel);
            return;
        }
        
        if(Constants.DEBUG && manualFrameAdvancing){
            enemies.updateAll();
        }
        if(fireTick>0){
            fireTick--;
        }
        levelManager.update();
        LevelManager.collisionChecked.clear();
        LevelManager.collisionFound.clear(); 
        //enemies.updateAll();
    }

    @Override
    public void draw(Graphics g) {
        
        //long a = System.nanoTime();
        
        float xOffset = -(GAME_WIDTH / 2 - (player.getHitbox().x + player.getHitbox().width/2) * SCALE);
        float maxOffset = (levelManager.getLoadedLevel().getLenght()*SCALE)-GAME_WIDTH;
        effXOffset = -((xOffset < 0) ? 0f: (xOffset > maxOffset ? maxOffset : xOffset));
        
        levelManager.drawWorld(g, effXOffset, 0);
        levelManager.drawEnemies(g, effXOffset, 0);
        Player player = levelManager.getLoadedLevel().getPlayer();
        if(!paused&&!death){
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2*Game.SCALE));
            g2.setColor(new Color(255, 0, 0, 56));
            g.drawLine((int)((player.getHitbox().x+player.getHitbox().width/2)*Game.SCALE + effXOffset), (int)((player.getHitbox().y+player.getHitbox().height/2)*Game.SCALE), (int)pointerX, (int)pointerY);
            g2.setStroke(new BasicStroke(0));
        }
        levelManager.drawProjs(g, effXOffset, 0);
        
        player.render(g, effXOffset, 0);
        g.setColor(Color.black);
        g.setFont (new Font ("TimesRoman", Font.BOLD, (int)(15*Game.SCALE)));
        g.drawString("LIVES: "+player.getLives(), (int)(2*Game.SCALE), (int)(15*Game.SCALE));
        
        if (paused) {
            pauseOverlay.draw(g);
        }
        if(death){
            deathOverlay.draw(g);
        }
        //System.out.println(System.nanoTime() - a);
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (paused) {
            pauseOverlay.mousePressed(e);
            return;
        }
        if(death){
            deathOverlay.mousePressed(e);
            return;
        }
        if (e.getButton() == MouseEvent.BUTTON1 && fireTick<=0) {
            playerShoot(e.getX(), e.getY());
        }
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (paused) {
            pauseOverlay.mouseReleased(e);
        }
        if (death) {
            deathOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (paused) {
            pauseOverlay.mouseMoved(e);
            return;
        }
        if (death) {
            deathOverlay.mouseMoved(e);
            return;
        }
       
    }
    
    public void mouseDragged(MouseEvent e) {
        if (paused) {
            pauseOverlay.mouseDragged(e);
            return;
        }
        
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!paused&&!death) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W -> {
                    player.setUp(true);
                }
                case KeyEvent.VK_A -> {
                    player.setLeft(true);
                }
                case KeyEvent.VK_S -> {
                    player.setDown(true);
                }
                case KeyEvent.VK_D -> {
                    player.setRight(true);
                }
                case KeyEvent.VK_SPACE -> {
                    player.setJump(true);
                }
            }
            
        }
        if (e.getKeyCode()==KeyEvent.VK_ESCAPE && !death) {
            paused = !paused;
            if(paused){
                enemies.stopAllThreads();
            }
            AudioPlayer.playEffect(AudioPlayer.Effects.PAUSE);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> {
                player.setUp(false);
            }
            case KeyEvent.VK_A -> {
                player.setLeft(false);
            }
            case KeyEvent.VK_S -> {
                player.setDown(false);
            }
            case KeyEvent.VK_D -> {
                player.setRight(false);
            }
            case KeyEvent.VK_SPACE -> {
                player.setJump(false);
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void windowLostFocus() {
        player.resDirBools();
    }

    static public LevelManager getLevelManager() {
        return levelManager;
    }
    
    public void unpauseGame() {
        paused = false;
        enemies.startAllThreads();
    }

    static public void playerDeath(){
        death=true;
        enemies.stopAllThreads();
        
        AudioPlayer.stopMusic();
        AudioPlayer.playEffect(AudioPlayer.Effects.DEATH);
    }

    public static int getCurrentLevel() {
        return currentLevel;
    }
    
    private void playerShoot(int x, int y){
        fireTick=FIRE_SPEED;
        player.setAttacking(true);
        Projectile flyingAmmo = new Projectile(player.getHitbox().x+player.getHitbox().width/2, player.getHitbox().y+player.getHitbox().height/2, (float)(Math.atan2(player.getHitbox().x+player.getHitbox().width/2 - (x-effXOffset)/Game.SCALE, player.getHitbox().y+player.getHitbox().height/2 - y/Game.SCALE)+Math.PI/2-PLAYER_GUN_RANDOMNESS/2+Math.random()*PLAYER_GUN_RANDOMNESS));
        flyingAmmo.loadLvlData(levelManager.getLoadedLevel().getLvlData());
        flyingAmmos.add(flyingAmmo);
        AudioPlayer.playEffect(AudioPlayer.Effects.FIRE);
    }
    
    public float getVolume(){
        return pauseOverlay.getVolume();
    }
    
    public boolean isSfxMuted(){
        return pauseOverlay.isSfxMuted();
    }
    
    public boolean isMusicMuted(){
        return pauseOverlay.isMusicMuted();
    }

    public static boolean isPaused() {
        return paused;
    }

    public static boolean isDeath() {
        return death;
    }
    
}
