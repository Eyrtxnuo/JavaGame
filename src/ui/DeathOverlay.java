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

public class DeathOverlay {

    private BufferedImage backgroundImg;
    private int bgX, bgY, bgW, bgH;
    private UrmButton replayB, menuB;
    private Playing playing;

    public DeathOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        createUrmButtons();
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.DEATH_OVERLAY);
        bgW = (int) (backgroundImg.getWidth() * Game.SCALE);
        bgH = (int) (backgroundImg.getHeight() * Game.SCALE);
        bgX = (int) (Game.GAME_WIDTH / 2 - bgW / 2);
        bgY = (int) (Game.GAME_HEIGHT / 2 - bgH / 2);
    }

    private void createUrmButtons() {
        int menuX = (int) (((bgX/Game.SCALE)+76) * Game.SCALE);
        int replayX = (int) (((bgX/Game.SCALE)+166) * Game.SCALE);
        int bY = (int) (295 * Game.SCALE);

        menuB = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE, 2);
        replayB = new UrmButton(replayX, bY, URM_SIZE, URM_SIZE, 1);
    }

    public void update() {
        menuB.update();
        replayB.update();
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);
        menuB.draw(g);
        replayB.draw(g);
    }

    private boolean isIn(MouseEvent e, PauseButton b) {
        return (b.getBounds().contains(e.getX(), e.getY()));
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuB)) {
            menuB.setMousePressed(true);
        } else if (isIn(e, replayB)) {
            replayB.setMousePressed(true);
        }
        
    }
    
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
        
        if(startMusic){
            AudioPlayer.playEffect(AudioPlayer.Effects.CLICK);
            AudioPlayer.playMusic(AudioPlayer.Musics.LEVEL_MUSIC);
        }
    }
    
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
