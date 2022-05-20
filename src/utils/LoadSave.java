/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import entities.Boss;
import entities.Sniper;
import entities.Enemy;
import entities.EnemyManager;
import entities.FollowEnemy;
import entities.PassiveEnemy;
import entities.ProjectileManager;
import gamestates.Playing;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import main.Game;
import main.GamePanel;

/** Methods to load maps, audios and textures
 *
 * @author matti
 */
public class LoadSave {
    
    /** sprites path */
    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String LEVEL_ATLAS = Constants.DEBUG?"outside_sprites_grid.png":"outside_sprites_city.png";
    public static final String[] LEVELS_DATA = {"level_one_data.png", "level_two_data.png", "level_three_data.png", "level_boss_data.png"};
    public static final String[] LEVELS_BACKGROUND = {"city_background.png","city_background.png","city_background.png", "city_background.png"};
    public static final int LEVELS_NUMBER = LEVELS_DATA.length;
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background.png";
    public static final String PAUSE_BACKGROUND = "pause_menu.png";
    public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String CRABBY_ATLAS = "crabby_sprite.png";
    public static final String SNIPER_ATLAS = "zombie_atlas.png";
    public static final String BOSS_ATLAS = "boss_sprites.png";

    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    public static final String DEATH_OVERLAY = "death_overlay.png";
    public static final String WIN_OVERLAY = "win_overlay.png";

    public static final String URM_BUTTONS = "urm_buttons.png";

    /** load sprite form local path
     * @param atlas altlas path
     * @return loaded Image*/
    public static BufferedImage GetSpriteAtlas(String atlas){
        BufferedImage img =null;
        InputStream is = LoadSave.class.getResourceAsStream("/"+atlas);
        try {
           img = ImageIO.read(is);
            
        } catch (IOException ex) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return img;
    }
    
    /** load level tiles data from level number
     * @param levelN the level to load
     * @return level tiles data */
    public static int[][] GetLevelData(int levelN) {
        if(levelN < 0 || levelN >= LEVELS_NUMBER)return null;
        
        BufferedImage img = GetSpriteAtlas (LEVELS_DATA[levelN]);
        int[][] lvlData = new int[img.getHeight()][img.getWidth()];//[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];
        for (int j = 0; j < img.getHeight(); j++)
               for (int i = 0; i < img.getWidth(); i++) {
            Color color = new Color(img.getRGB(i, j));
            int value = color.getRed();
            if(value >= 48)
                value=0;
            lvlData[j][i] = value;
            
        }
        return lvlData;
    }
    
    /** load level enemies from level number
     * @param levelN the level to load
     * @return level enemies list */
    public static LinkedList<Enemy> GetLevelEnemies(int levelN) {
        if(levelN < 0 || levelN >= LEVELS_NUMBER)return null;
        int[][] lvlData = GetLevelData(levelN);
        var enemies = new LinkedList<Enemy>();
        BufferedImage img = GetSpriteAtlas (LEVELS_DATA[levelN]);
        
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if(value == 0)
                    continue;
                Enemy en;
                switch(value){
                    case 1 -> {
                        en = new PassiveEnemy(i*Game.TILES_DEFAULT_SIZE, j*Game.TILES_DEFAULT_SIZE);
                    }
                    case 2 -> {
                        en = new FollowEnemy(i*Game.TILES_DEFAULT_SIZE, j*Game.TILES_DEFAULT_SIZE);
                    }
                    case 3 -> {
                        en = new Sniper(i*Game.TILES_DEFAULT_SIZE, j*Game.TILES_DEFAULT_SIZE);
                    }
                    case 4 -> {
                        en = new Boss(i*Game.TILES_DEFAULT_SIZE, j*Game.TILES_DEFAULT_SIZE);
                    }
                    default ->{
                        continue;
                    }
                }
                en.loadLvlData(lvlData);
                enemies.add(en);
            }
        return enemies;
    }
    
    /** load level tiles data and enemies from level number
     * @param levelN the level to load
     * @param playing current playing reference 
     * @return level tiles data */
    public static levels.Level getLevel(int levelN, Playing playing){
        EnemyManager em = new EnemyManager(playing);
        em.loadEnemies(GetLevelEnemies(levelN));
        return new levels.Level(GetLevelData(levelN),em , new ProjectileManager(playing),playing.getPlayer(), GetSpriteAtlas(LEVELS_BACKGROUND[levelN]));
    }
}
