/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import main.Game;
import main.GamePanel;
import static utils.Constants.PlayerConstants.GetSpriteAmount;

/**
 *
 * @author matti
 */
public class LoadSave {
    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String LEVEL_ATLAS = Constants.debug?"outside_sprites_grid.png":"outside_sprites.png";
    public static final String LEVEL_ONE_DATA = "level_one_data.png";
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background.png";
    
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
    
    public static int[][] GetLevelData() {
        BufferedImage img = GetSpriteAtlas (LEVEL_ONE_DATA);
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
}
