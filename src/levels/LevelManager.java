/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package levels;

import gamestates.Playing;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import main.Game;
import utils.Constants;
import utils.LoadSave;

/**
 *
 * @author matti
 */
public class LevelManager {
 
    private Game game;
    private BufferedImage[] levelSprite; 
    public static LinkedList<Integer> colorOrange = new LinkedList<Integer>();
    public static LinkedList<Integer> colorRed = new LinkedList<Integer>();

    private Level levelOne;

    public LevelManager(Game game) {
        this.game = game;
        importOutsideSprites();
        levelOne = new Level(LoadSave.GetLevelData(0), LoadSave.GetSpriteAtlas("background.png"));
    }
    
    public void update(){
        
    }
    
     public void draw(Graphics g, float offsetX, float offsetY){
        g.drawImage(levelOne.getBackground(), (int)offsetX, (int)offsetY, Playing.getLevelManager().getLevelOne().getLvlData()[0].length*game.TILES_SIZE-1 , game.GAME_HEIGHT ,null );
        for (int j = 0; j < levelOne.getLvlData().length; j++)
            for (int i = 0; i < levelOne.getLvlData()[j].length; i++) {
                int index = levelOne.getSpriteIndex(i, j);
                if((i+1)*Game.TILES_SIZE>-(offsetX) && i*Game.TILES_SIZE<Game.GAME_WIDTH-(offsetX)){
                    g.drawImage(levelSprite[index], (int)((Game.TILES_SIZE * i)+offsetX), (int)((Game.TILES_SIZE * j)+offsetY), Game.TILES_SIZE, Game.TILES_SIZE, null);
                    if(Constants.debug){
                        if(colorRed.contains(j + i*14)){
                            g.setColor(new Color(1f, 0f, 0f, 0.5f));
                            g.fillRect((int)((Game.TILES_SIZE * i)+offsetX), (int)((Game.TILES_SIZE * j)+offsetY), Game.TILES_SIZE, Game.TILES_SIZE);
                        }else if(colorOrange.contains(j + i*14)){
                            g.setColor(new Color(0.98f, 0.584f, 0.258f, 0.5f));
                            g.fillRect((int)((Game.TILES_SIZE * i)+offsetX), (int)((Game.TILES_SIZE * j)+offsetY), Game.TILES_SIZE, Game.TILES_SIZE);
                        }
                    }
                }
            }
    }

    private void importOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[48];
        for(int j = 0;j<4;j++){
            for(int i = 0;i< 12; i++){
                int index = j*12 + i;
                levelSprite[index] = img.getSubimage(i*Game.TILES_DEFAULT_SIZE, j*Game.TILES_DEFAULT_SIZE, Game.TILES_DEFAULT_SIZE, Game.TILES_DEFAULT_SIZE);
                
            }
        }
    }

    public Level getLevelOne() {
        return levelOne;
    }
    
    
}
