package ui;

import gamestates.Gamestate;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import utils.AudioPlayer;
import utils.LoadSave;
import static utils.Constants.UI.Buttons.*;

public class MenuButton {
    int xPos, yPos, rowIndex, index;
    int xOffestCenter=B_WIDTH/2;
    Gamestate state;
    private BufferedImage[] imgs;
    private Boolean mouseOver=false, mousePressed=false;
    private Rectangle bounds;

    public MenuButton(int xPos, int yPos, int rowIndex, Gamestate state) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImgs();
        initBounds();
    }

    private void loadImgs() {
        imgs = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);
        for (int i = 0; i < imgs.length; i++) {
            imgs[i]=temp.getSubimage(i*B_WIDTH_DEFAULT, rowIndex*B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
        }
    }
    
    public void draw(Graphics g){
        g.drawImage(imgs[index], xPos-xOffestCenter, yPos, B_WIDTH, B_HEIGHT, null);
    }
    
    public void update(){
        index=0;
        if(mouseOver){
            index=1;
        }
        if(mousePressed){
            index=2;
        }
    }

    public Boolean getMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(Boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public Boolean getMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(Boolean mousePressed) {
        this.mousePressed = mousePressed;
        
    }

    private void initBounds() {
        bounds=new Rectangle(xPos-xOffestCenter, yPos, B_WIDTH, B_HEIGHT);
    }
    
    public void applyGamestate(){
        Gamestate.state=state;
        AudioPlayer.playEffect(AudioPlayer.Effects.CLICK);
    }
    
    public void resetBools(){
        mouseOver=false;
        mousePressed=false;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Gamestate getGamestate() {
        return state;
    }
    
    
    
}
