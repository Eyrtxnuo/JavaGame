package gamestates;

import entities.Player;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import levels.LevelManager;
import main.Game;
import static main.Game.GAME_WIDTH;
import static main.Game.SCALE;
import main.GamePanel;

public class Playing extends State implements Statemethods {

    static private LevelManager levelManager;
    private Player player;

    public Playing(Game game) {
        super(game);
        initClasses();
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        player = new Player(50, 200, 64, 40);
        player.loadLvlData(levelManager.getLevelOne().getLvlData());
        //gamePanel = new GamePanel(game);
    }

    @Override
    public void update() {
        levelManager.update();
        player.update();
    }

    @Override
    public void draw(Graphics g) {
        long a = System.nanoTime();
                float xOffset = GAME_WIDTH / 2 - player.getHitbox().x * SCALE;
                levelManager.draw(g, (xOffset <= 0) ? xOffset : 0f, 0);

                player.render(g, (xOffset <= 0) ? xOffset : 0, 0);
                System.out.println(System.nanoTime() - a);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1){
            player.setAttacking(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
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
    
}
