package gamestates;

import entities.PassiveEnemy;
import entities.Enemy;
import entities.EnemyManager;
import entities.FollowEnemy;
import entities.Player;
import entities.Projectile;
import entities.ProjectileManager;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import levels.LevelManager;
import main.Game;
import static main.Game.GAME_WIDTH;
import static main.Game.SCALE;
import ui.PauseOverlay;
import utils.LoadSave;

public class Playing extends State implements Statemethods {

    static private LevelManager levelManager;
    private Player player;
    public static EnemyManager enemies;
    private boolean paused = false;
    private PauseOverlay pauseOverlay;
    public static ProjectileManager flyingAmmos;
    private float effXOffset;
    private float pointerX, pointerY;

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
        flyingAmmos = new ProjectileManager(this);
    }

    @Override
    public void update() {
        if (!paused) {
            levelManager.update();
            player.update();
            
            LevelManager.colorOrange.clear();
            LevelManager.colorRed.clear(); 
            enemies.updateAll(player);
            flyingAmmos.updateAll(player);
        } else {
            pauseOverlay.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        //long a = System.nanoTime();
        float xOffset = -(GAME_WIDTH / 2 - player.getHitbox().x * SCALE);
        float maxOffset = (levelManager.getLevelOne().getLenght()*SCALE)-GAME_WIDTH;
        effXOffset = -((xOffset < 0) ? 0f: (xOffset > maxOffset ? maxOffset : xOffset));
        levelManager.draw(g, effXOffset, 0);

        var nemic = (LinkedList<Enemy>)enemies.getEnemies().clone();
        for(Enemy en : nemic){
            en.render(g, effXOffset, 0);
        }
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        g2.setColor(new Color(255, 0, 0, 56));
        g.drawLine((int)((player.getHitbox().x+player.getHitbox().width/2)*Game.SCALE + effXOffset), (int)((player.getHitbox().y+player.getHitbox().height/2)*Game.SCALE), (int)pointerX, (int)pointerY);

        var muniz = (LinkedList<Projectile>)flyingAmmos.getProjectiles().clone();
        
        muniz.forEach((el)->{
            el.render(g, effXOffset, 0);
        });
        
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
        if (paused) {
            pauseOverlay.mousePressed(e);
            return;
        }
        if (e.getButton() == MouseEvent.BUTTON1) {
            //if(flyingAmmos.getProjectiles().isEmpty()){
                player.setAttacking(true);
                Projectile flyingAmmo = new Projectile(player.getHitbox().x+player.getHitbox().width/2, player.getHitbox().y+player.getHitbox().height/2, (float)(Math.atan2(player.getHitbox().x+player.getHitbox().width/2 - (e.getX()-effXOffset)/Game.SCALE, player.getHitbox().y+player.getHitbox().height/2 - e.getY()/Game.SCALE)+Math.PI/2));
                flyingAmmo.loadLvlData(levelManager.getLevelOne().getLvlData());
                flyingAmmos.add(flyingAmmo);
            //}
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
            return;
        }
        pointerX = e.getX();
        pointerY = e.getY();
    }
    
    public void mouseDragged(MouseEvent e) {
        if (paused) {
            pauseOverlay.mouseDragged(e);
            return;
        }
        pointerX = e.getX();
        pointerY = e.getY();
        
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
