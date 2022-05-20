package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import static utils.Constants.UI.URMButtons.*;
import utils.LoadSave;

public class UrmButton extends PauseButton {

    /**
     * Array with buttons textures
     */
    private BufferedImage[] imgs;

    /**
     * indexes for textures
     */
    private int rowIndex, index;

    /**
     * Boolean for the interaction between mouse and buttons
     */
    private boolean mouseOver, mousePressed;

    /**
     * Default constructor
     *
     * @param x
     * @param y
     * @param height
     * @param width
     * @param rowIndex
     */
    public UrmButton(int x, int y, int height, int width, int rowIndex) {
        super(x, y, height, width);
        this.rowIndex = rowIndex;
        loadImgs();
    }

    /**
     * Do an update cycle
     */
    public void update() {
        index = 0;
        if (mouseOver) {
            index = 1;
        }
        if (mousePressed) {
            index = 2;
        }
    }

    /**
     * Draw the buttons
     *
     * @param g
     */
    public void draw(Graphics g) {
        g.drawImage(imgs[index], x, y, URM_SIZE, URM_SIZE, null);
    }

    /**
     * Load textures in the array
     */
    private void loadImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.URM_BUTTONS);
        imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * URM_DEFAULT_SIZE, rowIndex * URM_DEFAULT_SIZE, URM_DEFAULT_SIZE, URM_DEFAULT_SIZE);
        }
    }

    /**
     * Reset mouseover and mousepressed
     */
    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    /**
     * Return mouseover
     *
     * @return mouseover
     */
    public boolean isMouseOver() {
        return mouseOver;
    }

    /**
     * Set mouseover
     *
     * @param mouseOver
     */
    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    /**
     * Return mousepressed
     *
     * @return mousepressed
     */
    public boolean isMousePressed() {
        return mousePressed;
    }

    /**
     * Set mousepressed
     *
     * @param mousePressed
     */
    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

}
