package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import main.Game;
import ui.MenuButton;
import utils.LoadSave;

public class Menu extends State implements Statemethods{

    private MenuButton[] buttons=new MenuButton[3];
    private BufferedImage backgroundImg;
    private int menuX,menuY,menuWidth,menuHeight;
    
    public Menu(Game game) {
        super(game);
        loadButtons();
        loadBackground();
    }
    
    @Override
    public void update() {
        for(MenuButton mb:buttons){
            mb.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);
        
        for(MenuButton mb:buttons){
            mb.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for(MenuButton mb:buttons){
            if(isIn(e,mb)){
                if(mb.getGamestate()==Gamestate.PLAYING){
                    Playing.loadLevel(0);
                }
                mb.setMousePressed(true);
                
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for(MenuButton mb:buttons){
            if(isIn(e,mb)){
                if(mb.getMousePressed()){
                    mb.applyGamestate();
                }
                break;
            }
        }
        resetButtons();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for(MenuButton mb:buttons){
            mb.setMouseOver(false);
        }
        for(MenuButton mb:buttons){
            if(isIn(e,mb)){
                mb.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_ENTER){
            Gamestate.state=Gamestate.PLAYING;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

    private void loadButtons() {
        buttons[0]=new MenuButton((int)(Game.GAME_WIDTH/2),(int)(150*Game.SCALE),0,Gamestate.PLAYING);
        buttons[1]=new MenuButton((int)(Game.GAME_WIDTH/2),(int)(220*Game.SCALE),1,Gamestate.OPTIONS);
        buttons[2]=new MenuButton((int)(Game.GAME_WIDTH/2),(int)(290*Game.SCALE),2,Gamestate.QUIT);
    }

    private void resetButtons() {
        for(MenuButton mb:buttons){
            mb.resetBools();
        }
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth=(int)(backgroundImg.getWidth()*Game.SCALE);
        menuHeight=(int)(backgroundImg.getHeight()*Game.SCALE);
        menuX=(int)(Game.GAME_WIDTH/2-menuWidth/2);
        menuY=(int)(45*Game.SCALE);
    }

}
