/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package levels;

import java.awt.image.BufferedImage;

/**
 *
 * @author matti
 */
public class Level {
    
    private int[][] lvlData;
    private BufferedImage background;

    public Level(int[][] lvlData) {
        this.lvlData = lvlData;
    }

    public Level(int[][] lvlData, BufferedImage background) {
        this.lvlData = lvlData;
        this.background = background;
    }
    
    
    
    public int getSpriteIndex(int x, int y){
        return lvlData[y][x];
    }

    public int[][] getLvlData() {
        return lvlData;
    }

    public BufferedImage getBackground() {
        return background;
    }
    
    
}
