/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package levels;

import entities.Enemy;
import entities.EnemyManager;
import entities.Projectile;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import main.Game;
import utils.Constants;
import utils.LoadSave;
import static utils.LoadSave.LEVELS_NUMBER;

/**
 * Level manager class, load level tiles, enemies, and background
 * @author matti
 */

public class LevelManager {
 
    /** level data as image - rgb decoding */
    private BufferedImage[] levelSprite; 
    /** debug only variables, list of collision checked tiles */
    public static LinkedList<Integer> collisionChecked = new LinkedList<>(), collisionFound = new LinkedList<>();

    /** currently loaded level */
    private Level loadedLevel;

    /**
     * Constructor, needs game reference
     */
    public LevelManager() {
        importOutsideSprites();
        loadedLevel = LoadSave.getLevel(Game.playing.getCurrentLevel(), Game.getPlaying());
    }
    
    /**
     * Loads in memory the level chosen
     * @param levelN leven number to load
     * @throws NullPointerException if level does not exist
     */
    public void loadLevel(int levelN){
        loadedLevel = LoadSave.getLevel(levelN, Game.getPlaying());
        loadedLevel.reload();
    }
    
    /** update, redirect update to loadedLevel */
    public void update(){
        loadedLevel.update();
    }
    
    
    /** Render tiles on g if they are in the screen horizontal coordinates
     * 
     * @param g Graphics object to draw on
     * @param offsetX horizontal offset of screen
     * @param offsetY vertical offset of screen
    */
     public void drawWorld(Graphics g, float offsetX, float offsetY){
        
        drawBackground(g, offsetX, offsetY);
        
        if(Game.playing.newLevelPermitted && Game.playing.getCurrentLevel()+1 < LEVELS_NUMBER){
            g.setColor(new Color(255, 160, 0, 128));
            g.fillRect((int)((loadedLevel.getWidthInTiles()-4)*Game.TILES_SIZE+offsetX), (int)(0+offsetY),(int)(4*Game.TILES_SIZE), (int) Game.GAME_HEIGHT);
        }
        for (int j = 0; j < loadedLevel.getLvlData().length; j++)
            for (int i = 0; i < loadedLevel.getLvlData()[j].length; i++) {
                int index = loadedLevel.getTilesIndex(i, j);
                if((i+1)*Game.TILES_SIZE>-(offsetX) && i*Game.TILES_SIZE<Game.GAME_WIDTH-(offsetX)){
                    
                    g.drawImage(levelSprite[index], (int)((Game.TILES_SIZE * i)+offsetX), (int)((Game.TILES_SIZE * j)+offsetY), (int)Math.ceil(Game.TILES_SIZE), (int)Math.ceil(Game.TILES_SIZE), null);
                    
                    if(Constants.DEBUG){
                        if(collisionFound.contains(j + i*14)){
                            g.setColor(new Color(1f, 0f, 0f, 0.5f));
                            g.fillRect((int)((Game.TILES_SIZE * i)+offsetX), (int)((Game.TILES_SIZE * j)+offsetY), (int)(Game.TILES_SIZE), (int)(Game.TILES_SIZE));
                        }else if(collisionChecked.contains(j + i*14)){
                            g.setColor(new Color(0.98f, 0.584f, 0.258f, 0.5f));
                            g.fillRect((int)((Game.TILES_SIZE * i)+offsetX), (int)((Game.TILES_SIZE * j)+offsetY), (int)(Game.TILES_SIZE), (int)(Game.TILES_SIZE));
                        }
                    }
                }
            }
    }
     
    /** Render a repeating background
     * 
     * @param g Graphics object to draw on
     * @param offsetX horizontal offset of screen
     * @param offsetY vertical offset of screen
     */
    private void drawBackground(Graphics g, float offsetX, float offsetY){
        
        int imageWidth = (int)((loadedLevel.getBackground().getWidth()/(float)loadedLevel.getBackground().getHeight())*Game.GAME_HEIGHT);
        for(int i = 0; i< loadedLevel.getWidthInTiles()*Game.TILES_SIZE; i+=imageWidth) {
            g.drawImage(loadedLevel.getBackground(), (int)offsetX+i, (int)offsetY, imageWidth, (int)(Game.GAME_HEIGHT) ,null );
        }
    }
     
    /** Render every enemy on g, they will render only if in the screen horizontal space
     * 
     * @param g Graphics object to draw on
     * @param offsetX horizontal offset of screen
     * @param offsetY vertical offset of screen
     */
    public void drawEnemies(Graphics g, float offsetX, float offsetY){
        var nemic = (LinkedList<Enemy>)Game.playing.enemies.getEnemies().clone();
         for(Enemy en : nemic){
             en.render(g, offsetX, offsetY);
         }
    }
     
    /** Render every player projectile and enemy projectile on g
     * 
     * @param g Graphics object to draw on
     * @param offsetX horizontal offset of screen
     * @param offsetY vertical offset of screen
     */
    public void drawProjs(Graphics g, float offsetX, float offsetY){
       var muniz = (LinkedList<Projectile>)Game.playing.flyingAmmos.getProjectiles().clone();
        muniz.forEach((el)->{
            el.render(g,  offsetX, offsetY);
        });
        muniz = (LinkedList<Projectile>)Game.playing.flyingAmmos.getEnemyProjectiles().clone();
        muniz.forEach((el)->{
            el.render(g,  offsetX, offsetY);
        });
   }

    /** loads tiles textures */
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

    /**
     * Return the loaded level object reference
     * @return currently loaded level
     */
    public Level getLoadedLevel() {
        return loadedLevel;
    }
    
    public void loadLevelWIthData(int[][] data, EnemyManager em){
        loadedLevel = LoadSave.createCustomLevel(data, Game.playing, em);
        loadedLevel.reload();
    }
    
    public void reloadLevel() {
        loadedLevel.reload();
    }
    
}
