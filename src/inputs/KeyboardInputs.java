/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package inputs;

import gamestates.Gamestate;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import main.Game;
import main.GamePanel;
import utils.Constants;
/**
 * Class Implements KeyListener, calls correct gamestate keylistener 
 * @author matti
 */
public class KeyboardInputs implements KeyListener{

    /** GamePanel reference */
    private GamePanel gamePanel;

    /**
     * constructor, require gamePaner reference
     * @param gamePanel gamePanel reference
     */
    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
    
    /** KeyTyped event implementation
     * @param e KeyEvent
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    /** KeyPressed event implementation
     * @param e KeyEvent
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch(Gamestate.state){
            case PLAYING:
                gamePanel.getGame().getPlaying().keyPressed(e);
                if(Constants.DEBUG && e.getKeyChar() == 'p'){
                    Game.manualFrameAdvancing=!Game.manualFrameAdvancing;
                    System.out.println("Manual Frame Advancing: " + Game.manualFrameAdvancing);   
                }
                if(Game.manualFrameAdvancing && e.getKeyChar() == 'l'){
                    gamePanel.getGame().newFrame();
                }
                break;
            case MENU:
                gamePanel.getGame().getMenu().keyPressed(e);
                break;
            case MULTIPLAYERMENU:
                gamePanel.getGame().getMultiplayerMenu().keyPressed(e);
                break;
            default:
                
        }
    }

    /** KeyReleased event implementation
     * @param e KeyEvent
     */
    @Override
    public void keyReleased(KeyEvent e) {
        switch(Gamestate.state){
            case PLAYING:
                gamePanel.getGame().getPlaying().keyReleased(e);
                break;
            case MENU:
                gamePanel.getGame().getMenu().keyReleased(e);
                break;
            case MULTIPLAYERMENU:
                gamePanel.getGame().getMultiplayerMenu().keyReleased(e);
                break;
            default:
                
        }
    }
    
}
