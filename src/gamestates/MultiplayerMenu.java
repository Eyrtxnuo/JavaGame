package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import main.Game;
import ui.MultiplayerButton;

public class MultiplayerMenu extends State implements Statemethods{

    private MultiplayerButton[] buttons = new MultiplayerButton[2];
    
    private BufferedImage backgroundImg;
    
    private int menuX, menuY, menuWidth, menuHeight;
    
    public MultiplayerMenu(Game game) {
        super(game);
        loadButtons();
    }
    
    @Override
    public void update() {
        for (MultiplayerButton mb : buttons) {
            mb.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        for (MultiplayerButton mb : buttons) {
            mb.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (MultiplayerButton mb : buttons) {
            if (isInMultb(e, mb)) {
                if (mb.getGamestate() == Gamestate.PLAYING) {
                    
                }
                mb.setMousePressed(true);

                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MultiplayerButton mb : buttons) {
            if (isInMultb(e, mb)) {
                if (mb.getMousePressed()) {
                    mb.clickEvent();
                }
                break;
            }
        }
        resetButtons();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MultiplayerButton mb : buttons) {
            mb.setMouseOver(false);
        }
        for (MultiplayerButton mb : buttons) {
            if (isInMultb(e, mb)) {
                mb.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            Gamestate.state = Gamestate.MENU;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

    private void loadButtons() {
        buttons[0] = new MultiplayerButton((int) (Game.GAME_WIDTH/2-(150*Game.SCALE)), (int) (Game.GAME_HEIGHT/2), 3, Gamestate.PLAYING) {
            @Override
            public void onClick() {
                game.initPlaying(new PlayingMultiplayerServer(game));
                Game.playing.loadLevel(0);
                
            }
        };
        buttons[1] = new MultiplayerButton((int) (Game.GAME_WIDTH/2+(20*Game.SCALE)), (int) (Game.GAME_HEIGHT/2), 4, Gamestate.PLAYING) {
            @Override
            public void onClick() {
                game.initPlaying(new PlayingMultiplayerClient(game));
                Game.playing.loadLevel(0);
                
            }
        };
    }

    private void resetButtons() {
        for (MultiplayerButton mb : buttons) {
            mb.resetBools();
        }
    }
}
