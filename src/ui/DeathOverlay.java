package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import main.Game;
import utils.AudioPlayer;
import static utils.Constants.UI.URMButtons.URM_SIZE;
import utils.LoadSave;

/**
 * Class with methods to create and interact with death overlay
 *
 * @author m1ncio
 */
public class DeathOverlay {

    /**
     * Image used as background of the overlay
     */
    private BufferedImage backgroundImg;

    /**
     * Position and dimensions of background
     */
    private int bgX, bgY, bgW, bgH;

    /**
     * Replay and menu buttons
     */
    private UrmButton replayB, menuB;

    /**
     * Playing object
     */
    private Playing playing;

    /**
     * Default constructor
     *
     * @param playing
     */
    public DeathOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        createUrmButtons();
    }

    /**
     * Load the background e define his position and dimentions
     */
    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.DEATH_OVERLAY);
        bgW = (int) (backgroundImg.getWidth() * Game.SCALE);
        bgH = (int) (backgroundImg.getHeight() * Game.SCALE);
        bgX = (int) (Game.GAME_WIDTH / 2 - bgW / 2);
        bgY = (int) (Game.GAME_HEIGHT / 2 - bgH / 2);
    }

    /**
     * Create the 2 buttons and set their position
     */
    private void createUrmButtons() {
        int menuX = (int) (((bgX / Game.SCALE) + 76) * Game.SCALE);
        int replayX = (int) (((bgX / Game.SCALE) + 166) * Game.SCALE);
        int bY = (int) (295 * Game.SCALE);

        menuB = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE, 2);
        replayB = new UrmButton(replayX, bY, URM_SIZE, URM_SIZE, 1);
    }

    /**
     * Do an update cycle
     */
    public void update() {
        menuB.update();
        replayB.update();
    }

    /**
     * Draw background and buttons
     *
     * @param g
     */
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);
        menuB.draw(g);
        replayB.draw(g);
    }

    /**
     * Return if the mouse is in an hitbox of a button
     *
     * @param e
     * @param b
     * @return if the mouse is in an hitbox of a button
     */
    private boolean isIn(MouseEvent e, PauseButton b) {
        return (b.getBounds().contains(e.getX(), e.getY()));
    }

    /**
     * Take in input the pression of the mouse button
     *
     * @param e
     */
    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuB)) {
            menuB.setMousePressed(true);
        } else if (isIn(e, replayB)) {
            replayB.setMousePressed(true);
        }

    }

    /**
     * Take in input the release of the mouse button
     *
     * @param e
     */
    public void mouseReleased(MouseEvent e) {

        boolean startMusic = false;
        if (isIn(e, menuB)) {
            if (menuB.isMousePressed()) {
                Gamestate.state = Gamestate.MENU;
                playing.unpauseGame();
                startMusic = true;
            }
        } else if (isIn(e, replayB)) {
            if (replayB.isMousePressed()) {
                Playing.reloadLevel();
                playing.unpauseGame();
                System.out.println("replay lvl!");
                startMusic = true;
            }
        }
        menuB.resetBools();
        replayB.resetBools();

        if (startMusic) {
            AudioPlayer.playEffect(AudioPlayer.Effects.CLICK);
            AudioPlayer.playMusic(AudioPlayer.Musics.LEVEL_MUSIC);
        }
    }

    /**
     * Take in input the movement of the mouse
     *
     * @param e
     */
    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);
        replayB.setMouseOver(false);
        if (isIn(e, menuB)) {
            menuB.setMouseOver(true);
        } else if (isIn(e, replayB)) {
            replayB.setMouseOver(true);
        }
    }
}
