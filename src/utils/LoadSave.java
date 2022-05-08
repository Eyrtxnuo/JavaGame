/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import entities.Enemy;
import entities.FollowEnemy;
import entities.PassiveEnemy;
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

/**
 *
 * @author matti
 */
public class LoadSave {
    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String LEVEL_ATLAS = Constants.debug?"outside_sprites_grid.png":"outside_sprites.png";
    public static final String[] LEVELS_DATA = {"level_one_data.png"};
    public static final int LEVELS_NUMBER = LEVELS_DATA.length;
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background.png";
    public static final String PAUSE_BACKGROUND = "pause_menu.png";
    public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String CRABBY_ATLAS = "crabby_sprite.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";

    public static final String URM_BUTTONS = "urm_buttons.png";

    
    public static  BufferedImage GetSpriteAtlas(String atlas){
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
                        en = new PassiveEnemy(i*Game.TILES_SIZE, j*Game.TILES_SIZE);
                    }
                    case 2 -> {
                        en = new FollowEnemy(i*Game.TILES_SIZE, j*Game.TILES_SIZE);
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
    
}
