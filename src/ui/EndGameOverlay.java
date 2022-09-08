package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import gamestates.PlayingMultiplayerClient;
import gamestates.PlayingMultiplayerServer;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import main.Game;
import utils.AudioPlayer;
import static utils.Constants.UI.URMButtons.URM_SIZE;
import utils.LoadSave;

public class EndGameOverlay {

    private BufferedImage backgroundImg;
    private int bgX, bgY, bgW, bgH;
    private UrmButton menuB;
    private Playing playing;

    public EndGameOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        createUrmButtons();
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.WIN_OVERLAY);
        bgW = (int) (backgroundImg.getWidth() * Game.SCALE);
        bgH = (int) (backgroundImg.getHeight() * Game.SCALE);
        bgX = (int) (Game.GAME_WIDTH / 2 - bgW / 2);
        bgY = (int) (Game.GAME_HEIGHT / 2 - bgH / 2);
    }

    private void createUrmButtons() {
        int menuX = (int) (((bgX/Game.SCALE)) * Game.SCALE + (bgW-URM_SIZE)/2);
        int bY = (int) (295 * Game.SCALE);

        menuB = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE, 2);
    }

    public void update() {
        menuB.update();
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);
        menuB.draw(g);
    }

    private boolean isIn(MouseEvent e, PauseButton b) {
        return (b.getBounds().contains(e.getX(), e.getY()));
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuB)) {
            menuB.setMousePressed(true);
        }
        
    }
    
    public void mouseReleased(MouseEvent e) {
        
        boolean startMusic = false;
        if (isIn(e, menuB)) {
            if (menuB.isMousePressed()) {
                if(playing instanceof PlayingMultiplayerServer plCast){
                    plCast.stopServer();
                }
                if(playing instanceof PlayingMultiplayerClient plCast){
                    plCast.disconnect();
                }
                Gamestate.state = Gamestate.MENU;
                discord.DiscordActivityManager.setMenuActivity();
                //playing.unpauseGame();
                startMusic = true;
            }
        }
        menuB.resetBools();
        
        if(startMusic){
            AudioPlayer.playEffect(AudioPlayer.Effects.CLICK);
            AudioPlayer.playMusic(AudioPlayer.Musics.LEVEL_MUSIC);
        }
    }
    
    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);
        if (isIn(e, menuB)) {
            menuB.setMouseOver(true);
        }
    }
}
