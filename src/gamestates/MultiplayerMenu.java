package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import main.Game;
import ui.MultiplayerButton;
import ui.TextInput;
import static utils.Constants.UI.Buttons.*;
import static utils.Constants.UI.TextInput.TI_WIDTH;

public class MultiplayerMenu extends State implements Statemethods{

    private MultiplayerButton[] buttons = new MultiplayerButton[2];
    private TextInput textIP;
    
    private BufferedImage backgroundImg;
    
    private int menuX, menuY, menuWidth, menuHeight;
    
    public MultiplayerMenu() {
        super();
        loadButtons();
    }
    
    @Override
    public void update() {
        for (MultiplayerButton mb : buttons) {
            mb.update();
        }
        textIP.update();
    }

    @Override
    public void draw(Graphics g) {
        for (MultiplayerButton mb : buttons) {
            mb.draw(g);
        }
        textIP.draw(g);
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
        if(isInBounds(e, textIP.getBounds())){
            textIP.setSelected(true);
        }else{
            textIP.setSelected(false);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MultiplayerButton mb : buttons) {
            if (isInMultb(e, mb)) {
                if (mb.getMousePressed()) {
                    mb.clickEvent(e);
                }
                break;
            }
        }
        if(isInBounds(e, textIP.getBounds())){
            textIP.clickEvent(e);
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
        if(isInBounds(e, textIP.getBounds())){
            textIP.setMouseOver(true);
        }else{
            textIP.setMouseOver(false);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        textIP.processKeyEvent(e);
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            Gamestate.state = Gamestate.MENU;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        
    }

    private void loadButtons() {
        buttons[0] = new MultiplayerButton((int) ((Game.GAME_WIDTH-B_WIDTH)/2), (int) (Game.GAME_HEIGHT/2-(150*Game.SCALE)), 3, Gamestate.PLAYING) {
            @Override
            public boolean onClick(MouseEvent e) {
                loadPlayingServer();
                return true;
            }
        };
        buttons[1] = new MultiplayerButton((int) ((Game.GAME_WIDTH-B_WIDTH)/2), (int) (Game.GAME_HEIGHT/2+(120*Game.SCALE)), 4, Gamestate.PLAYING) {
            @Override
            public boolean onClick(MouseEvent e) {
                try {
                    loadPlayingClient();
                } catch (SocketException | UnknownHostException | SocketTimeoutException ex) {
                    //Logger.getLogger(MultiplayerMenu.class.getName()).log(Level.WARNING, "Connection error: {0}", ex.getMessage());
                    System.err.println("Connection error: "+ ex.getMessage());
                    Game.playing.pauseGame();
                    return false;
                }
                //handle wrong ip
                //handle server timeout
                return true;
                
            }
        };
        textIP = new TextInput((int) ((Game.GAME_WIDTH-TI_WIDTH)/2), (int) (Game.GAME_HEIGHT/2+(50*Game.SCALE)));
    }

    private void resetButtons() {
        for (MultiplayerButton mb : buttons) {
            mb.resetBools();
        }
    }
    
    private void loadPlayingServer(){
        Game.initPlaying(new PlayingMultiplayerServer());
        Game.playing.loadLevel(0);
        discord.DiscordActivityManager.setPlayingMultiplayerServerActivity();
        System.gc();
    }
    
    private void loadPlayingClient() throws SocketException, UnknownHostException, SocketTimeoutException{
        Game.initPlaying(new PlayingMultiplayerClient(textIP.getText(), 45670));
        discord.DiscordActivityManager.setPlayingMultiplayerClientActivity();
        System.gc();
    }
}
