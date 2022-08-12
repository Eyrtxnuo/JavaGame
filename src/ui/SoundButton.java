package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import utils.Constants;
import utils.LoadSave;
import static utils.Constants.UI.PauseButtons.*;

/**
 * Class to create and manage the sound buttons
 *
 * @author m1ncio
 * @see PauseButton
 */
public class SoundButton extends PauseButton {

    /**
     * Array with buttons textures
     */
    private BufferedImage[][] soundImgs;

    /**
     * Boolean for the interaction between mouse and buttons
     */
    private boolean mouseOver, mousePressed;

    /**
     * Boolean for the music muted
     */
    private boolean muted;

    /**
     * Index for textures
     */
    private int rowIndex, colIndex;

    /**
     * Default constructor
     *
     * @param x
     * @param y
     * @param height
     * @param width
     */
    public SoundButton(int x, int y, int height, int width) {
        super(x, y, height, width);
        soundImgs = new BufferedImage[2][3];
        loadSoundImgs();
    }

    /**
     * Load textures in the array
     */
    private void loadSoundImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SOUND_BUTTONS);
        for (int j = 0; j < soundImgs.length; j++) {
            for (int i = 0; i < soundImgs[j].length; i++) {
                soundImgs[j][i] = temp.getSubimage(i * SOUND_SIZE_DEFAULT, j * SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT);
            }
        }
    }

    /**
     * Do an update cycle
     */
    public void update() {
        if (muted) {
            rowIndex = 1;
        } else {
            rowIndex = 0;
        }
        colIndex = 0;
        if (mouseOver) {
            colIndex = 1;
        }
        if (mousePressed) {
            colIndex = 2;
        }
    }

    /**
     * Draw the buttons
     *
     * @param g
     */
    public void draw(Graphics g) {
        g.drawImage(soundImgs[rowIndex][colIndex], x, y, width, height, null);
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

    /**
     * Return muted
     *
     * @return muted
     */
    public boolean isMuted() {
        return muted;
    }

    /**
     * Set muted
     *
     * @param muted 
     */
    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    /**
     * Reset mouseover and mousepressed
     */
    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }
}
