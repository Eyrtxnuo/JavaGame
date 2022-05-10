package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import static main.Game.*;

/** Abstract base entity class
 *
 * @author BossiMattia
 */
public abstract class Entity {
    
    protected float x, y;
    //protected int width, height;
    protected Rectangle2D.Float hitbox;

    public Entity(float x, float y/*, int width, int height*/) {
        this.x = x;
        this.y = y;
        //this.width = width;
        //this.height = height;
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
}