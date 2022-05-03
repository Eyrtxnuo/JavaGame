
package utils;

import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import levels.LevelManager;
import main.Game;

/**
 *
 * @author matti
 */
public class HelpMethods {

    public static boolean CanMoveHere(float x, float y,  float width, float height, int[][] lvlData) {
        /*if (!IsSolid(x, y, lvlData))
            if (!IsSolid(x + width, y + height, lvlData))
                if (!IsSolid(x + width, y, lvlData))
                    if (!IsSolid(x, y + height, lvlData))
                        return true;
        return false;*/
        return IsAllAir(x, y, x+width, y+height, lvlData);
        /*if(IsAllAir(x, y, x+width, y+height, lvlData))
            if(IsAllAir(x, y+height, x+width, y+height, lvlData))
                if(IsAllAir(x, y, x, y+height, lvlData))
                    if(IsAllAir(x, y, x+width, y, lvlData))
                        return true;
        return false;*/
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
        LevelManager.colorOrange.add((int)xIndex*14 + (int)yIndex);

        int value = lvlData[(int) yIndex][(int) xIndex];
        
        if (value >= 48 || value < 0 || value != 11) {
            
            LevelManager.colorRed.add((int)xIndex*14 + (int)yIndex);
            return true;
        }
        
        LevelManager.colorOrange.add((int)xIndex*14 + (int)yIndex);
        return false;
    }
    
    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed){
        
        if(xSpeed > 0){
            //Right
            int currentTile = (int)(( hitbox.x + hitbox.width ) / Game.TILES_DEFAULT_SIZE);
            int tileXpos = currentTile * Game.TILES_DEFAULT_SIZE;
            float xOffset = Game.TILES_DEFAULT_SIZE - hitbox.width;
            return tileXpos + xOffset -1; //
        }else{
            //Left
            int currentTile = (int)(hitbox.x / Game.TILES_DEFAULT_SIZE);
            return currentTile * Game.TILES_DEFAULT_SIZE;
        }
    }
    
    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox,float airSpeed){
        
        if(airSpeed > 0){
            //falling - touching floor
            int currentTile = (int)((hitbox.y + hitbox.height) / Game.TILES_DEFAULT_SIZE);
            int tileYpos = currentTile * Game.TILES_DEFAULT_SIZE;
            float yOffset = Game.TILES_DEFAULT_SIZE - hitbox.height;
            return tileYpos + yOffset -1;// 
        }else{
            //jumping
            int currentTile = (int)(hitbox.y / Game.TILES_DEFAULT_SIZE);
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
    
    public static boolean IsAllAir(float x1, float y1, float x2, float y2, int[][] lvlData){
        int tileX1 = (int)x1/Game.TILES_DEFAULT_SIZE;
        int tileY1 = (int)y1/Game.TILES_DEFAULT_SIZE;
        int tileX2 = (int)x2/Game.TILES_DEFAULT_SIZE;
        int tileY2 = (int)y2/Game.TILES_DEFAULT_SIZE;
        for(int x = tileX1; x <= tileX2 ; x++){
            for(int y = tileY1; y <= tileY2 ; y++){
                
                if(IsSolid(x*Game.TILES_DEFAULT_SIZE, y*Game.TILES_DEFAULT_SIZE, lvlData)){
                    return false;
                }
            }
        }
        return true;
    }
}
