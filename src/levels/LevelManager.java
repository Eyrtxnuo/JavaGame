/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import main.Game;
import utils.LoadSave;

/**
 *
 * @author matti
 */
public class LevelManager {
 
    private Game game;
    private BufferedImage[] levelSprite; 

    private Level levelOne;

    public LevelManager(Game game) {
        this.game = game;
        importOutsideSprites();
        levelOne = new Level(LoadSave.GetLevelData(), LoadSave.GetSpriteAtlas("background.png"));
    }
    
    public void update(){
        
    }
    
    public void draw(Graphics g, float offsetX, float offsetY){
        g.drawImage(levelOne.getBackground(), (int)offsetX, (int)offsetY, game.getLevelManager().getLevelOne().getLvlData()[0].length*game.TILES_SIZE-1 , game.GAME_HEIGHT ,null );
        for (int j = 0; j < levelOne.getLvlData().length; j++)
            for (int i = 0; i < levelOne.getLvlData()[j].length; i++) {
                int index = levelOne.getSpriteIndex(i, j);
                g.drawImage(levelSprite[index], (int)((Game.TILES_SIZE * i)+offsetX), (int)((Game.TILES_SIZE * j)+offsetY), Game.TILES_SIZE, Game.TILES_SIZE, null);
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
