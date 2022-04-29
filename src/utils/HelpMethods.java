/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.geom.Rectangle2D;
import main.Game;

/**
 *
 * @author matti
 */
public class HelpMethods {

    public static boolean CanMoveHere(float x, float y,  float width, float height, int[][] lvlData) {
    if (!IsSolid(x, y, lvlData))
        if (!IsSolid(x + width, y + height, lvlData))
            if (!IsSolid(x + width, y, lvlData))
                if (!IsSolid(x, y + height, lvlData))
                    return true;
    return false;
}

    private static boolean IsSolid(float x, float y, int[][] lvlData) {
        /*if (x < 0 || x >= Game.COORD_WIDTH) {
            return true;
        }*/
        if (y < 0 || y >= Game.COORD_HEIGHT) {
            return true;
        }

        float xIndex = x / Game.TILES_DEFAULT_SIZE;
        float yIndex = y / Game.TILES_DEFAULT_SIZE;

        int value = lvlData[(int) yIndex][(int) xIndex];
        
        if (value >= 48 || value < 0 || value != 11) {
            return true;
        }
        return false;
    }
    
    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed){
        int currentTile = (int)(hitbox.x / Game.TILES_DEFAULT_SIZE);
        if(xSpeed > 0){
            //Right
            int tileXpos = currentTile * Game.TILES_DEFAULT_SIZE;
            float xOffset = Game.TILES_DEFAULT_SIZE - hitbox.width;
            return tileXpos + xOffset-1; //
        }else{
            //Left
            return currentTile * Game.TILES_DEFAULT_SIZE;
        }
    }
    
    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox,float airSpeed){
        int currentTile = (int)(hitbox.y / Game.TILES_DEFAULT_SIZE);
        if(airSpeed > 0){
            //falling - touching floor
            int tileYpos = currentTile * Game.TILES_DEFAULT_SIZE;
            float yOffset = Game.TILES_DEFAULT_SIZE - hitbox.height;
            return tileYpos + yOffset -1;// 
        }else{
            //jumping
            return currentTile * Game.TILES_DEFAULT_SIZE;
        }
    }
    
    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        //Check pixel bottomleft and bottomright
        if(!IsSolid(hitbox.x, hitbox.y+hitbox.height + 1 , lvlData))
            if(!IsSolid(hitbox.x+hitbox.width, hitbox.y+hitbox.height + 1, lvlData))
                return false;
        return true;
    }
}
