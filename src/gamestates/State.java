package gamestates;

import java.awt.event.MouseEvent;
import main.Game;
import ui.MenuButton;
import ui.MultiplayerButton;

/**
 * Super class of game states
 *
 * @author m1ncio
 */
public abstract class State {

    /**
     * Game object
     */
    protected Game game;

    /**
     * Default constructor
     *
     * @param game
     */
    public State(Game game) {
        this.game = game;
    }

    /**
     * Return if the mouse is in an hitbox
     *
     * @param e
     * @param mb
     * @return if the mouse is in an hitbox
     */
    public Boolean isInMb(MouseEvent e, MenuButton mb) {
        return mb.getBounds().contains(e.getX(), e.getY());
    }
    
    public Boolean isInMultb(MouseEvent e, MultiplayerButton mb) {
        return mb.getBounds().contains(e.getX(), e.getY());
    }

    /**
     * Return game object
     *
     * @return game object
     */
    public Game getGame() {
        return game;
    }

}
