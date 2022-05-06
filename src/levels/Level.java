/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package levels;

import java.awt.image.BufferedImage;
import main.Game;

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
    
    public int getHeightInTiles() {
        return lvlData.length;
    }

    public int getWidthInTiles() {
        return (lvlData.length>0)?lvlData[0].length:0;
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
    
    public int getLenght(){
        return getWidthInTiles()*Game.TILES_DEFAULT_SIZE;
    }
}
