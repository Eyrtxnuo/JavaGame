
package utils;

import java.awt.geom.Rectangle2D;
import levels.LevelManager;
import main.Game;

/**Methods to check collisions and positions
 *
 * @author matti
 */
public class HelpMethods {

    /** checks if the hitbox passed is valid (does not touch any collision tile)
     *
     * @param x coordinate x
     * @param y coordinate y
     * @param width hitbox width
     * @param height hitbox height
     * @param lvlData level tiles data
     * @return if position is valid
     */
    public static boolean CanMoveHere(float x, float y,  float width, float height, int[][] lvlData) {
        return IsAllAir(x, y, x+width, y+height, lvlData);
    }

    /** checks if coordinates passed are in a solid tile 
     * @param x coordinate x
     * @param y coordinate y
     * @param lvlData level tiles data
     * @return if position is solid 
     */
    private static boolean IsSolid(float x, float y, int[][] lvlData) {
        if (y < 0 || y >= lvlData.length*Game.TILES_DEFAULT_SIZE) {
            return false;
        }
        if (x < 0 || x >= lvlData[0].length*Game.TILES_DEFAULT_SIZE) {
            return true;
        }
        float xIndex = x / Game.TILES_DEFAULT_SIZE;
        float yIndex = y / Game.TILES_DEFAULT_SIZE;
        LevelManager.collisionChecked.add((int)Math.floor(xIndex)*14 + (int)Math.floor(yIndex));

        int value = lvlData[(int)Math.floor(yIndex)][(int)Math.floor(xIndex)];
        
        if (value >= 48 || value < 0 || value != 11) {
            
            LevelManager.collisionFound.add((int)xIndex*14 + (int)yIndex);
            return true;
        }
        
        LevelManager.collisionChecked.add((int)xIndex*14 + (int)yIndex);
        return false;
    }
    
    /** get hitbox x position next to wall
     * @param hitbox get hitbox position and dimensions
     * @param xSpeed get walking direction from movement speed
     * @return x coordinate next to wall */
    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed){
        
        if(xSpeed > 0){
            //Right
            int currentTile = (int)Math.floor(( hitbox.x + hitbox.width ) / Game.TILES_DEFAULT_SIZE);
            int tileXpos = currentTile * Game.TILES_DEFAULT_SIZE;
            float xOffset = Game.TILES_DEFAULT_SIZE - hitbox.width;
            return tileXpos + xOffset -1; //
        }else{
            //Left
            int currentTile = (int)Math.floor(hitbox.x / Game.TILES_DEFAULT_SIZE);
            return currentTile * Game.TILES_DEFAULT_SIZE;
        }
    }
    
    /** get hitbox y position under roof or above floor
     * @param hitbox get hitbox position and dimensions
     * @param airSpeed get vertical direction from verical speed
     * @return y coordinate under roof or above floor */
    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox,float airSpeed){
        
        if(airSpeed > 0){
            //falling - touching floor
            int currentTile = (int)Math.floor((hitbox.y + hitbox.height) / Game.TILES_DEFAULT_SIZE);
            int tileYpos = currentTile * Game.TILES_DEFAULT_SIZE;
            float yOffset = Game.TILES_DEFAULT_SIZE - hitbox.height;
            return tileYpos + yOffset -1;// 
        }else{
            //jumping
            int currentTile = (int)Math.floor(hitbox.y / Game.TILES_DEFAULT_SIZE);
            return currentTile * Game.TILES_DEFAULT_SIZE;
        }
    }
    
    /** checks if hitbox is touching the floor
     * @param hitbox get hitbox position and dimensions
     * @param lvlData level tiles data
     * @return iff hitbox is touching the floor */
    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        //Check pixel bottomleft and bottomright
        if(!IsSolid(hitbox.x, hitbox.y+hitbox.height + 1 , lvlData))
            if(!IsSolid(hitbox.x+hitbox.width, hitbox.y+hitbox.height + 1, lvlData))
                return false;
        return true;
    }
    
    
    /** checks if all the tils within the two coordinates rectagle are air
     *
     * @param x1 coordinate x first point
     * @param y1 coordinate y first point
     * @param x2 coordinate x second point
     * @param y2 coordinate y second point
     * @param lvlData level tiles data
     * @return if all tiles are air
     */
    public static boolean IsAllAir(float x1, float y1, float x2, float y2, int[][] lvlData){
        int tileX1 = (int)Math.floor(x1/Game.TILES_DEFAULT_SIZE);
        int tileY1 = (int)Math.floor(y1/Game.TILES_DEFAULT_SIZE);
        int tileX2 = (int)Math.floor(x2/Game.TILES_DEFAULT_SIZE);
        int tileY2 = (int)Math.floor(y2/Game.TILES_DEFAULT_SIZE);
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
