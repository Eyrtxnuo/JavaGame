package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import main.Game;
import static main.Game.*;

/** Abstract base entity class
 *
 * @author BossiMattia
 */
public abstract class Entity {
    
    protected float x, y;
    protected Rectangle2D.Float hitbox;

    public Entity(float x, float y) {
        this.x = x;
        this.y = y;
    }


    public void update() {
     if(hitbox.y < -3*Game.TILES_DEFAULT_SIZE || hitbox.y > Game.COORD_HEIGHT+3*Game.TILES_DEFAULT_SIZE){
            die();
        }
    }
    
    protected void drawHitbox(Graphics g, float offsetX, float offsetY ){
        //FOR DEBUGGING
        g.setColor(Color.red);
        g.drawRect((int)(hitbox.x*SCALE+ offsetX), (int)(hitbox.y*SCALE + offsetY), (int)(hitbox.width*SCALE ), (int)(hitbox.height*SCALE));
    }
    
    protected void initHitbox(float x, float y, int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, width, height);
    }
    
//    protected void updateHitbox(){
//        hitbox.x = (int)x;
//        hitbox.y = (int)y;
//    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }
    
    public void die(){
        
    }
}