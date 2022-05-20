package ui;

import java.awt.Rectangle;

/**
 * Super class for the buttons in the pause menu
 *
 * @author m1ncio
 */
public class PauseButton {

    /**
     * position and dimensions
     */
    protected int x, y, height, width;

    /**
     * hitbox
     */
    protected Rectangle bounds;

    /**
     * Default constructor
     *
     * @param x
     * @param y
     * @param height
     * @param width
     */
    public PauseButton(int x, int y, int height, int width) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        createBounds();
    }

    /**
     * Initialise bounds
     */
    private void createBounds() {
        bounds = new Rectangle(x, y, width, height);
    }

    /**
     * Return x
     *
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * Set x
     *
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Return y
     *
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * Set y
     *
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Return height
     *
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Set height
     *
     * @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Return width
     *
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Set width
     *
     * @param width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Return bounds
     *
     * @return bounds
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Set bounds
     *
     * @param bounds
     */
    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

}
