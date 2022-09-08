package gamestates;

import java.awt.Rectangle;
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
    
    public Boolean isInBounds(MouseEvent e, Rectangle bo) {
        return bo.contains(e.getX(), e.getY());
    }

    

}
