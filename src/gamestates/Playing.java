package gamestates;

import entities.PassiveEnemy;
import entities.Enemy;
import entities.EnemyManager;
import entities.FollowEnemy;
import entities.Player;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import levels.LevelManager;
import main.Game;
import static main.Game.GAME_WIDTH;
import static main.Game.SCALE;
import main.GamePanel;
import ui.PauseOverlay;
import utils.LoadSave;

public class Playing extends State implements Statemethods {

    static private LevelManager levelManager;
    private Player player;
    private EnemyManager enemies;
    private boolean paused = false;
    private PauseOverlay pauseOverlay;

    public Playing(Game game) {
        super(game);
        initClasses();
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        player = new Player(50, 200);
        player.loadLvlData(levelManager.getLevelOne().getLvlData());
        enemies = new EnemyManager(this);
        enemies.loadEnemies(LoadSave.GetLevelEnemies(0));//new FollowEnemy(1200, 285);
        //enemies.loadLvlData(levelManager.getLevelOne().getLvlData());
        pauseOverlay = new PauseOverlay(this);
        //gamePanel = new GamePanel(game);
    }

    @Override
    public void update() {
        if (!paused) {
            levelManager.update();
            player.update();
            LevelManager.colorOrange.clear();
            LevelManager.colorRed.clear();
            for(Enemy en : enemies.getEnemies()){
                en.update(player);   
            }
        } else {
            pauseOverlay.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        long a = System.nanoTime();
        float xOffset = -(GAME_WIDTH / 2 - player.getHitbox().x * SCALE);
        float maxOffset = (levelManager.getLevelOne().getLenght()*SCALE)-GAME_WIDTH;
        float effXOffset = -((xOffset < 0) ? 0f: (xOffset > maxOffset ? maxOffset : xOffset));
        levelManager.draw(g, effXOffset, 0);

        for(Enemy en : enemies.getEnemies()){
            en.render(g, effXOffset, 0);
        }

        player.render(g, effXOffset, 0);
        if (paused) {
            pauseOverlay.draw(g);
        }
        //System.out.println(System.nanoTime() - a);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            player.setAttacking(true);
        }
        if (paused) {
            pauseOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (paused) {
            pauseOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (paused) {
            pauseOverlay.mouseMoved(e);
        }
    }
    
    public void mouseDragged(MouseEvent e) {
        if (paused) {
            pauseOverlay.mouseDragged(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!paused) {
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
            if (e.getKeyCode()==KeyEvent.VK_ESCAPE) {
                paused = !paused;
            }
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
    }
}
