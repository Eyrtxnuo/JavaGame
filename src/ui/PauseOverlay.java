package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import main.Game;
import utils.AudioPlayer;
import utils.LoadSave;
import static utils.Constants.UI.PauseButtons.*;
import static utils.Constants.UI.URMButtons.*;
import static utils.Constants.UI.VolumeButtons.*;

public class PauseOverlay {

    private BufferedImage backgroundImg;
    private int bgX/*287 base*/, bgY/*30 base*/, bgW, bgH;
    private SoundButton musicButton, sfxButton;
    private UrmButton unpauseB, replayB, menuB;
    private Playing playing;
    private VolumeButton volumeButton;

    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        createSoundButtons();
        createUrmButtons();
        createVolumeButton();
    }

    public void update() {
        musicButton.update();
        sfxButton.update();
        menuB.update();
        replayB.update();
        unpauseB.update();
        volumeButton.update();
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);
        musicButton.draw(g);
        sfxButton.draw(g);
        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);
        volumeButton.draw(g);
    }

    public void mouseDragged(MouseEvent e) {
        if (volumeButton.isMousePressed()) {
            volumeButton.changeX(e.getX());
        }
    }

    public void mousePressed(MouseEvent e) {
        
        if (isIn(e, musicButton)) {
            musicButton.setMousePressed(true);
        } else if (isIn(e, sfxButton)) {
            sfxButton.setMousePressed(true);
        } else if (isIn(e, menuB)) {
            menuB.setMousePressed(true);
        } else if (isIn(e, replayB)) {
            replayB.setMousePressed(true);
        } else if (isIn(e, unpauseB)) {
            unpauseB.setMousePressed(true);
        } else if (isIn(e, volumeButton)) {
            volumeButton.setMousePressed(true);
        }
        
        
    }

    public void mouseReleased(MouseEvent e) {
        boolean somethingGotCLicked=false;
        if (isIn(e, musicButton)) {
            if (musicButton.isMousePressed()) {
                musicButton.setMuted(!musicButton.isMuted());
                AudioPlayer.toggleMusic(!musicButton.isMuted());
                somethingGotCLicked=true;
            }
        } else if (isIn(e, sfxButton)) {
            if (sfxButton.isMousePressed()) {
                sfxButton.setMuted(!sfxButton.isMuted());
                somethingGotCLicked=true;
            }
        } else if (isIn(e, menuB)) {
            if (menuB.isMousePressed()) {
                Gamestate.state = Gamestate.MENU;
                playing.unpauseGame();
                somethingGotCLicked=true;
            }
        } else if (isIn(e, replayB)) {
            if (replayB.isMousePressed()) {
                playing.reloadLevel();
                playing.unpauseGame();
                System.out.println("replay lvl!");
                somethingGotCLicked=true;
            }
        } else if (isIn(e, unpauseB)) {
            if (unpauseB.isMousePressed()) {
                playing.unpauseGame();
                somethingGotCLicked=true;
            }
        }
        musicButton.resetBools();
        sfxButton.resetBools();
        menuB.resetBools();
        replayB.resetBools();
        unpauseB.resetBools();
        volumeButton.resetBools();
        if(somethingGotCLicked){
            AudioPlayer.playEffect(AudioPlayer.Effects.CLICK);
        }
    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        menuB.setMouseOver(false);
        replayB.setMouseOver(false);
        unpauseB.setMouseOver(false);
        volumeButton.setMouseOver(false);
        if (isIn(e, musicButton)) {
            musicButton.setMouseOver(true);
        } else if (isIn(e, sfxButton)) {
            sfxButton.setMouseOver(true);
        } else if (isIn(e, menuB)) {
            menuB.setMouseOver(true);
        } else if (isIn(e, replayB)) {
            replayB.setMouseOver(true);
        } else if (isIn(e, unpauseB)) {
            unpauseB.setMouseOver(true);
        } else if (isIn(e, volumeButton)) {
            volumeButton.setMouseOver(true);
        }
    }

    private boolean isIn(MouseEvent e, PauseButton b) {
        return (b.getBounds().contains(e.getX(), e.getY()));
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgW = (int) (backgroundImg.getWidth() * Game.SCALE);
        bgH = (int) (backgroundImg.getHeight() * Game.SCALE);
        bgX = (int)(Game.GAME_WIDTH / 2 - bgW / 2);
        bgY = (int)(Game.GAME_HEIGHT / 2 - bgH / 2);
    }

    private void createSoundButtons() {
        int soundX = (int) (((bgX/Game.SCALE)+163) * Game.SCALE);
        int musicY = (int) (((bgY/Game.SCALE)+117) * Game.SCALE);
        int sfxY = (int) (((bgY/Game.SCALE)+163) * Game.SCALE);
        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
    }

    private void createUrmButtons() {
        int menuX = (int) (((bgX/Game.SCALE)+28) * Game.SCALE);
        int replayX = (int) (((bgX/Game.SCALE)+102) * Game.SCALE);
        int unpauseX = (int) (((bgX/Game.SCALE)+177) * Game.SCALE);
        int bY = (int) (325 * Game.SCALE);

        menuB = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE, 2);
        replayB = new UrmButton(replayX, bY, URM_SIZE, URM_SIZE, 1);
        unpauseB = new UrmButton(unpauseX, bY, URM_SIZE, URM_SIZE, 0);
    }

    private void createVolumeButton() {
        int vX = (int) (((bgX/Game.SCALE)+22) * Game.SCALE);
        int vY = (int) (((bgY/Game.SCALE)+256) * Game.SCALE);
        volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    public float getVolume() {
        return volumeButton.getVolume();
    }
    
    public boolean isSfxMuted(){
        return sfxButton.isMuted();
    }
    public boolean isMusicMuted(){
        return musicButton.isMuted();
    }
    
}
