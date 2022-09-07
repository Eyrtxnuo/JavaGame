/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package inputs;

import gamestates.Gamestate;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import main.Game;
import main.GamePanel;

/**
 * Class Implements MouseListener, MouseMotionListener, calls correct gamestate
 * listeners
 *
 * @author matti
 */
public class MouseInputs implements MouseListener, MouseMotionListener {

    /**
     * GamePanel reference
     */
    private GamePanel gamePanel;

    /**
     * constructor, require gamePaner reference
     *
     * @param gamePanel gamePanel reference
     */
    public MouseInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    /**
     * MouseClicked event implementation
     *
     * @param e MouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        switch (Gamestate.state) {
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseClicked(e);
                break;
            default:

        }
    }

    /**
     * MousePressed event implementation
     *
     * @param e MouseEvent
     */
    @Override
    public void mousePressed(MouseEvent e) {
        switch (Gamestate.state) {
            case PLAYING:
                gamePanel.getGame().getPlaying().mousePressed(e);
                break;
            case MENU:
                gamePanel.getGame().getMenu().mousePressed(e);
                break;
            case MULTIPLAYERMENU:
                gamePanel.getGame().getMultiplayerMenu().mousePressed(e);
                break;
            default:

        }
    }

    /**
     * MouseReleased event implementation
     *
     * @param e MouseEvent
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        switch (Gamestate.state) {
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseReleased(e);
                break;
            case MENU:
                gamePanel.getGame().getMenu().mouseReleased(e);
                break;
            case MULTIPLAYERMENU:
                gamePanel.getGame().getMultiplayerMenu().mouseReleased(e);
                break;
            default:

        }
    }

    /**
     * MouseEntered event implementation
     *
     * @param e MouseEvent
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * MouseExited event implementation
     *
     * @param e MouseEvent
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
     * MouseDragged event implementation
     *
     * @param e MouseEvent
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        Game.playing.pointerX = e.getX();
        Game.playing.pointerY = e.getY();
        switch (Gamestate.state) {
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseDragged(e);
                break;
            default:
        }
    }

    /**
     * MouseMoved event implementation
     *
     * @param e MouseEvent
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        Game.playing.pointerX = e.getX();
        Game.playing.pointerY = e.getY();
        //gamePanel.setRectPosition(e.getX(), e.getY());
        switch (Gamestate.state) {
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseMoved(e);
                break;
            case MENU:
                gamePanel.getGame().getMenu().mouseMoved(e);
                break;
            case MULTIPLAYERMENU:
                gamePanel.getGame().getMultiplayerMenu().mouseMoved(e);
                break;
            default:

        }
    }
}
