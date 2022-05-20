package ui;

import gamestates.Gamestate;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import utils.AudioPlayer;
import utils.LoadSave;
import static utils.Constants.UI.Buttons.*;

/**
 * Class to create and manage the start menu buttons
 *
 * @author m1ncio
 */
public class MenuButton {

    /**
     * Buttons position and indexes for textures
     */
    int xPos, yPos, rowIndex, index;

    /**
     * 
     */
    int xOffestCenter = B_WIDTH / 2;

    /**
     * Gamestates object
     */
    Gamestate state;

    /**
     * Array with buttons textures
     */
    private BufferedImage[] imgs;

    /**
     * Boolean for the interaction between mouse and buttons
     */
    private Boolean mouseOver = false, mousePressed = false;

    /**
     * Hitbox of the buttons
     */
    private Rectangle bounds;

    /**
     * Default constructor
     *
     * @param xPos
     * @param yPos
     * @param rowIndex
     * @param state
     */
    public MenuButton(int xPos, int yPos, int rowIndex, Gamestate state) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImgs();
        initBounds();
    }

    /**
     * Load textures in the array
     */
    private void loadImgs() {
        imgs = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * B_WIDTH_DEFAULT, rowIndex * B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
        }
    }

    /**
     * Draw the buttons
     *
     * @param g
     */
    public void draw(Graphics g) {
        g.drawImage(imgs[index], xPos - xOffestCenter, yPos, B_WIDTH, B_HEIGHT, null);
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
     * Return mouseover
     *
     * @return mouseover
     */
    public Boolean getMouseOver() {
        return mouseOver;
    }

    /**
     * Set mouseover
     *
     * @param mouseOver
     */
    public void setMouseOver(Boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    /**
     * Return mousepressed
     *
     * @return mousepressed
     */
    public Boolean getMousePressed() {
        return mousePressed;
    }

    /**
     * Set mousepressed
     *
     * @param mousePressed
     */
    public void setMousePressed(Boolean mousePressed) {
        this.mousePressed = mousePressed;

    }

    /**
     * Initialize botton's hitbox
     */
    private void initBounds() {
        bounds = new Rectangle(xPos - xOffestCenter, yPos, B_WIDTH, B_HEIGHT);
    }

    /**
     * Set the game state
     */
    public void applyGamestate() {
        Gamestate.state = state;
        AudioPlayer.playEffect(AudioPlayer.Effects.CLICK);
    }

    /**
     * Reset mouseover and mousepressed
     */
    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
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
     * Return state
     *
     * @return state
     */
    public Gamestate getGamestate() {
        return state;
    }

}
