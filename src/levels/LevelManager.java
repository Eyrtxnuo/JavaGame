/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package levels;

import entities.Enemy;
import entities.Projectile;
import gamestates.Playing;
import static gamestates.Playing.enemies;
import static gamestates.Playing.flyingAmmos;
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
    public static LinkedList<Integer> collisionChecked = new LinkedList<>();
    public static LinkedList<Integer> collisionFound = new LinkedList<>();

    private Level loadedLevel;

    public LevelManager(Game game) {
        this.game = game;
        importOutsideSprites();
        loadedLevel = LoadSave.getLevel(0, game.getPlaying());
    }
    
    public void loadLevel(int levelN){
        loadedLevel = LoadSave.getLevel(levelN, game.getPlaying());
    }
    
    public void update(){
        loadedLevel.update();
    }
    
     public void drawWorld(Graphics g, float offsetX, float offsetY){
        g.drawImage(loadedLevel.getBackground(), (int)offsetX, (int)offsetY, Playing.getLevelManager().getLoadedLevel().getLvlData()[0].length*game.TILES_SIZE, game.GAME_HEIGHT ,null );
        for (int j = 0; j < loadedLevel.getLvlData().length; j++)
            for (int i = 0; i < loadedLevel.getLvlData()[j].length; i++) {
                int index = loadedLevel.getSpriteIndex(i, j);
                if((i+1)*Game.TILES_SIZE>-(offsetX) && i*Game.TILES_SIZE<Game.GAME_WIDTH-(offsetX)){
                    g.drawImage(levelSprite[index], (int)((Game.TILES_SIZE * i)+offsetX), (int)((Game.TILES_SIZE * j)+offsetY), Game.TILES_SIZE, Game.TILES_SIZE, null);
                    if(Constants.DEBUG){
                        if(collisionFound.contains(j + i*14)){
                            g.setColor(new Color(1f, 0f, 0f, 0.5f));
                            g.fillRect((int)((Game.TILES_SIZE * i)+offsetX), (int)((Game.TILES_SIZE * j)+offsetY), Game.TILES_SIZE, Game.TILES_SIZE);
                        }else if(collisionChecked.contains(j + i*14)){
                            g.setColor(new Color(0.98f, 0.584f, 0.258f, 0.5f));
                            g.fillRect((int)((Game.TILES_SIZE * i)+offsetX), (int)((Game.TILES_SIZE * j)+offsetY), Game.TILES_SIZE, Game.TILES_SIZE);
                        }
                    }
                }
            }
    }
     
    public void drawEnemies(Graphics g, float offsetX, float offsetY){
        var nemic = (LinkedList<Enemy>)enemies.getEnemies().clone();
         for(Enemy en : nemic){
             en.render(g, offsetX, offsetY);
         }
    }
     
    public void drawProjs(Graphics g, float offsetX, float offsetY){
       var muniz = (LinkedList<Projectile>)flyingAmmos.getProjectiles().clone();
        muniz.forEach((el)->{
            el.render(g,  offsetX, offsetY);
        });
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

    public Level getLoadedLevel() {
        return loadedLevel;
    }
    
    
}
