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
import ui.EndGameOverlay;
import ui.PauseOverlay;
import ui.VolumeButton;
import utils.AudioPlayer;
import utils.Constants;
import static utils.LoadSave.*;

public class Playing extends State implements Statemethods {

    /**
     * LevelManeger object
     */
    public static LevelManager levelManager;

    /**
     * Player object
     */
    private static Player player;

    /**
     * EnemyManager object
     */
    public static EnemyManager enemies;

    /**
     * Boolean for the pause
     */
    private static boolean paused = false;
    /**
     * Boolean for the player death
     */
    private static boolean death = false;

    
    private static boolean endGame = false;
    /**
     * PauseOverlay object
     */
    private PauseOverlay pauseOverlay;

    /**
     * DeathOverlay object
     */
    private DeathOverlay deathOverlay;
    /**
     * EndgameOverlay object
     */
    private EndGameOverlay endGameOverlay;
    /**
     * ProjectileManager object
     */
    public static ProjectileManager flyingAmmos;

    /**
     * Offset of displayed frame
     */
    private float effXOffset;

    /**
     * Pointer position
     */
    public static float pointerX, pointerY;

    /**
     * Randomness of player's gun
     */
    private static final double PLAYER_GUN_RANDOMNESS = 0.05;//in radians

    /**
     * Delay between shots
     */
    private static final int FIRE_SPEED = 100;

    /**
     * Effective delay between shots
     */
    private int fireTick = 0;

    /**
     * Current level load
     */
    private static int currentLevel = 0;

    /**
     * Default constructor
     *
     * @param game
     */
    public Playing(Game game) {
        super(game);
        initClasses();
    }

    /**
     * Create player, pauseoverlay and deathoverlay
     *
     */
    private void initClasses() {
        player = new Player(50, 200);
        pauseOverlay = new PauseOverlay(this);
        deathOverlay = new DeathOverlay(this);
        endGameOverlay = new EndGameOverlay(this);
    }

    
    /**
     * Create levelmanager
     *
     */
    public void initLevelManager() {
        levelManager = new LevelManager(game);
        connectLevel();
    }

    /**
     *
     */
    public static void connectLevel() {
        enemies = levelManager.getLoadedLevel().getEnemies();
        flyingAmmos = levelManager.getLoadedLevel().getProj();
    }

    /**
     * Reload level, player and enemies
     *
     */
    public static void reloadLevel() {
        enemies.stopAllThreads();
        levelManager.loadLevel(currentLevel);
        connectLevel();
        player.reset();
        death = false;
    }

    /**
     * Load the level
     *
     * @param levelN
     */
    public static void loadLevel(int levelN) {
        currentLevel = levelN;
        reloadLevel();
    }

    /**
     * Do an update cycle
     *
     */
    @Override
    public void update() {
        if (paused) {
            pauseOverlay.update();
            return;
        }
        if (death) {
            deathOverlay.update();
            return;
        }
        if(endGame){
            endGameOverlay.update();
            return;
        }
        if (player.getHitbox().x > (levelManager.getLoadedLevel().getWidthInTiles() - 4) * Game.TILES_DEFAULT_SIZE
                && currentLevel + 1 < LEVELS_NUMBER) {
            loadLevel(++currentLevel);
            return;
        }

        if (Constants.DEBUG && manualFrameAdvancing) {
            enemies.updateAll();
        }
        if (fireTick > 0) {
            fireTick--;
        }
        levelManager.update();
        LevelManager.collisionChecked.clear();
        LevelManager.collisionFound.clear();
        //enemies.updateAll();
    }

    /**
     * Draw the game panel
     *
     * @param g
     */
    @Override
    public void draw(Graphics g) {

        //long a = System.nanoTime();
        float xOffset = -(GAME_WIDTH / 2 - (player.getHitbox().x + player.getHitbox().width / 2) * SCALE);
        float maxOffset = (levelManager.getLoadedLevel().getLenght() * SCALE) - GAME_WIDTH;
        effXOffset = -((xOffset < 0) ? 0f : (xOffset > maxOffset ? maxOffset : xOffset));

        levelManager.drawWorld(g, effXOffset, 0);
        levelManager.drawEnemies(g, effXOffset, 0);
        Player player = levelManager.getLoadedLevel().getPlayer();
        if (!paused && !death && !endGame) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2 * Game.SCALE));
            g2.setColor(new Color(255, 0, 0, 56));
            g.drawLine((int) ((player.getHitbox().x + player.getHitbox().width / 2) * Game.SCALE + effXOffset), (int) ((player.getHitbox().y + player.getHitbox().height / 2) * Game.SCALE), (int) pointerX, (int) pointerY);
            g2.setStroke(new BasicStroke(0));
        }
        levelManager.drawProjs(g, effXOffset, 0);

        player.render(g, effXOffset, 0);
        g.setColor(Color.black);
        g.setFont(new Font("TimesRoman", Font.BOLD, (int) (15 * Game.SCALE)));
        g.drawString("LIVES: " + player.getLives(), (int) (2 * Game.SCALE), (int) (15 * Game.SCALE));

        if (paused) {
            pauseOverlay.draw(g);
        }
        if (death) {
            deathOverlay.draw(g);
        }
        if (endGame){
            endGameOverlay.draw(g);
        }
        //System.out.println(System.nanoTime() - a);

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * Take in input the pression of the mouse button
     *
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (paused) {
            pauseOverlay.mousePressed(e);
            return;
        }
        if (death) {
            deathOverlay.mousePressed(e);
            return;
        }
        if(endGame){
            endGameOverlay.mousePressed(e);
            return;
        }
        if (e.getButton() == MouseEvent.BUTTON1 && fireTick <= 0) {
            playerShoot(e.getX(), e.getY());
        }

    }

    /**
     * Take in input the release of the mouse button
     *
     * @param e
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (paused) {
            pauseOverlay.mouseReleased(e);
        }
        if (death) {
            deathOverlay.mouseReleased(e);
        }
        if(endGame){
            endGameOverlay.mouseReleased(e);
        }
    }

    /**
     * Take in input the movement of the mouse
     *
     * @param e
     */
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
        if (endGame) {
            endGameOverlay.mouseMoved(e);
            return;
        }

    }

    /**
     * Take in input the drag of the mouse
     *
     * @param e
     */
    public void mouseDragged(MouseEvent e) {
        if (paused) {
            pauseOverlay.mouseDragged(e);
            return;
        }
        if(endGame){
            endGameOverlay.mouseMoved(e);
            return;
        }
    }

    /**
     * Take in input the pression of key
     *
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (!paused && !death) {
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
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && !death && !endGame) {
            paused = !paused;
            if (paused) {
                enemies.stopAllThreads();
            }
            AudioPlayer.playEffect(AudioPlayer.Effects.PAUSE);
        }
    }

    /**
     * Take in input the release of key
     *
     * @param e
     */
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

    /**
     * Return player object
     *
     * @return player object
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Reset player's movement
     */
    public void windowLostFocus() {
        player.resDirBools();
    }

    /**
     * Return levelmanager
     *
     * @return levelmanager
     */
    static public LevelManager getLevelManager() {
        return levelManager;
    }

    /**
     * Unpause the game
     */
    public void unpauseGame() {
        paused = false;
        endGame = false;
        enemies.startAllThreads();
    }

    /**
     * Set the player as death
     */
    static public void playerDeath() {
        death = true;
        enemies.stopAllThreads();

        AudioPlayer.stopMusic();
        AudioPlayer.playEffect(AudioPlayer.Effects.DEATH);
    }

    /**
     * Return the current level
     *
     * @return the current level
     */
    public static int getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Shoot a projectile from player's gun
     *
     * @param x
     * @param y
     */
    private void playerShoot(int x, int y) {
        fireTick = FIRE_SPEED;
        player.setAttacking(true);
        Projectile flyingAmmo = new Projectile(player.getHitbox().x + player.getHitbox().width / 2, player.getHitbox().y + player.getHitbox().height / 2, (float) (Math.atan2(player.getHitbox().x + player.getHitbox().width / 2 - (x - effXOffset) / Game.SCALE, player.getHitbox().y + player.getHitbox().height / 2 - y / Game.SCALE) + Math.PI / 2 - PLAYER_GUN_RANDOMNESS / 2 + Math.random() * PLAYER_GUN_RANDOMNESS));
        flyingAmmo.loadLvlData(levelManager.getLoadedLevel().getLvlData());
        flyingAmmos.add(flyingAmmo);
        AudioPlayer.playEffect(AudioPlayer.Effects.FIRE);
    }

    /**
     * Return volume
     *
     * @return volume
     */
    public float getVolume() {
        return pauseOverlay.getVolume();
    }

    /**
     * Return if sfx is muted
     *
     * @return if sfx is muted
     */
    public boolean isSfxMuted() {
        return pauseOverlay.isSfxMuted();
    }

    /**
     * Return if music is muted
     *
     * @return if music is muted
     */
    public boolean isMusicMuted() {
        return pauseOverlay.isMusicMuted();
    }

    /**
     * Return if game is pause
     *
     * @return if game is pause
     */
    public static boolean isPaused() {
        return paused;
    }

    /**
     * Return if player is death
     *
     * @return if player is death
     */
    public static boolean isDeath() {
        return death;
    }

    public static boolean isEndGame() {
        return endGame;
    }

    public static void gameWin(){
        endGame = true;
        AudioPlayer.stopMusic();
        AudioPlayer.playEffect(AudioPlayer.Effects.WIN_GAME);
        
    }
}
