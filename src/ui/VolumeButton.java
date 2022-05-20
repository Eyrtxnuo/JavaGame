package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import utils.AudioPlayer;

import utils.LoadSave;
import static utils.Constants.UI.VolumeButtons.*;

public class VolumeButton extends PauseButton {

    /**
     * Array with button textures
     */
    private BufferedImage[] imgs;

    /**
     * Slider textures
     */
    private BufferedImage slider;

    /**
     * index for slider textures
     */
    private int index = 0;

    /**
     * Boolean for the interaction between mouse and slider
     */
    private boolean mouseOver, mousePressed;

    /**
     * position and dimensions of slider
     */
    private int buttonX, minX, maxX;

    /**
     * default constructor
     *
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public VolumeButton(int x, int y, int width, int height) {
        super(x + width / 2, y, VOLUME_WIDTH, height);
        bounds.x -= VOLUME_WIDTH / 2;
        buttonX = x + width / 2;
        this.x = x;
        this.width = width;
        minX = x + VOLUME_WIDTH / 2;
        maxX = x + width - VOLUME_WIDTH / 2 + 1;
        loadImgs();
    }

    /**
     * Load textures in the array
     */
    private void loadImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.VOLUME_BUTTONS);
        imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * VOLUME_DEFAULT_WIDTH, 0, VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
        }

        slider = temp.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);

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
     * Draw the slider
     *
     * @param g
     */
    public void draw(Graphics g) {

        g.drawImage(slider, x, y, width, height, null);
        g.drawImage(imgs[index], buttonX - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, height, null);

    }

    /**
     * Prevents the slider from exiting limits
     *
     * @param x
     */
    public void changeX(int x) {
        if (x < minX) {
            buttonX = minX;
        } else if (x > maxX) {
            buttonX = maxX;
        } else {
            buttonX = x;
        }

        bounds.x = buttonX - VOLUME_WIDTH / 2;
        AudioPlayer.updateMusicVolume();
    }

    /**
     * Reset mouseover and mousepressed
     */
    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    /**
     * Return mousepressed
     *
     * @return mousepressed
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

    /**
     * Return volume
     *
     * @return volume
     */
    public float getVolume() {
        int val = buttonX - minX;
        int span = maxX - minX;
        return val / (float) span;
    }
}
