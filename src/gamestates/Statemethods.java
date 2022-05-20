package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Interface wich implements gamestates methods
 *
 * @author minci
 */
public interface Statemethods {

    /**
     * Update event
     */
    public void update();

    /**
     * Draw event
     *
     * @param g
     */
    public void draw(Graphics g);

    /**
     * Mouse click event
     *
     * @param e
     */
    public void mouseClicked(MouseEvent e);

    /**
     * Mouse press event
     *
     * @param e
     */
    public void mousePressed(MouseEvent e);

    /**
     * Mouse realese event
     *
     * @param e
     */
    public void mouseReleased(MouseEvent e);

    /**
     * Mouse moved event
     *
     * @param e
     */
    public void mouseMoved(MouseEvent e);

    /**
     * Key press event
     *
     * @param e
     */
    public void keyPressed(KeyEvent e);

    /**
     * Key release event
     *
     * @param e
     */
    public void keyReleased(KeyEvent e);
}
