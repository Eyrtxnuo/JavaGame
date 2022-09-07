package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import main.Game;
import static main.Game.*;

/** Abstract base entity class
 *
 * @author BossiMattia
 */
public abstract class Entity {
    
    /** entity default coordinates */
    protected float x, y;
    /** entity hitbox */
    protected Rectangle2D.Float hitbox;

    /** constructor, define coordinate
     * @param x coordinate x
     * @param y coordinate y
     */
    public Entity(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /** update function */
    public void update() {
     if(hitbox.y < -3*Game.TILES_DEFAULT_SIZE || hitbox.y > Game.COORD_HEIGHT+3*Game.TILES_DEFAULT_SIZE){
            die();
        }
    }
    
    /** draws on Graphics g if it is in the screen horizontal coordinates
     * 
     * @param g Graphics object to draw on
     * @param offsetX horizontal offset of screen
     * @param offsetY vertical offset of screen
    */
    protected void drawHitbox(Graphics g, float offsetX, float offsetY ){
        //FOR DEBUGGING
        g.setColor(Color.red);
        g.drawRect((int)(hitbox.x*SCALE+ offsetX), (int)(hitbox.y*SCALE + offsetY), (int)(hitbox.width*SCALE ), (int)(hitbox.height*SCALE));
    }
    
    /**
     * initalize hitbox object 
     * @param x coordinate x
     * @param y coordinate y
     * @param width hitbox width
     * @param height hitbox height
     */
    protected void initHitbox(float x, float y, int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, width, height);
    }
    
    /** get default x coordinate
     * @return default x coordinate
     */ 
    public float getX() {
        return x;
    }
    /** get default y coordinate
     * @return default y coordinate
     */
    public float getY() {
        return y;
    }

    /** get hitbox
     * @return enemy hitbox
     */ 
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }
    
    /** die funcion, override */ 
    public void die(){
        
    }
    
    public Point.Float getPosition(){
        return new Point.Float(getHitbox().x, getHitbox().y);
    }
    
    public void setPosition(Point.Float point){
        hitbox.x = point.x;
        hitbox.y = point.y;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    
}