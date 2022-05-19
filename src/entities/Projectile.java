package entities;

import gamestates.Playing;
import java.awt.Color;
import java.awt.Graphics;
import main.Game;
import utils.Constants;
import static utils.HelpMethods.CanMoveHere;

/**
 *
 * @author matti
 */
public class Projectile extends Entity{
    
    private static final float SPEED = 3f;
    private float angle;
    private float xSpeed, ySpeed;
    private int[][] lvlData;
    private final float gravity = 0.00f;
    private int bounces = 0;
    private int despawnCounter=100;

    public Projectile(float x, float y, float angle) {
        super(x, y);
        this.angle = -angle;
        calcSpeeds();
        initHitbox(x, y, 4, 4);
    }
    
    private void calcSpeeds(){
        xSpeed = (float)Math.cos(this.angle)*SPEED;
        ySpeed = (float)Math.sin(this.angle)*SPEED;
    }
 
    public void render(Graphics g, float offsetX, float offsetY){
        
        g.setColor(Color.black);
        g.fillRect((int)(hitbox.x*Game.SCALE + offsetX), (int)(hitbox.y*Game.SCALE + offsetY), (int)(hitbox.width*Game.SCALE), (int)(hitbox.height*Game.SCALE));
        if(Constants.DEBUG){
            drawHitbox(g, offsetX, offsetY);
        }
    }
 
    public void update(Player p) {
        super.update();
        updatePos();
        /*if(hitbox.intersects(p.hitbox)){
            System.out.println("DAMAGE");
            p.reset();
        }*/
        if(despawnCounter--==0){
            die();
        }
    }

    private void updatePos() {
        if(CanMoveHere(hitbox.x, hitbox.y + ySpeed, hitbox.width, hitbox.height, lvlData)){
            hitbox.y += ySpeed;
            ySpeed += gravity;
            if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
                hitbox.x += xSpeed;
            }else {
                float angleNew = (float)(Math.PI/2+(Math.PI/2-getAngle()));
                setAngle(angleNew);
                bounces--;
            }
        }else{
            float angleNew = (float)-getAngle();
            setAngle(angleNew);
             bounces--;
            //initHitbox(0, 0, 5, 5);
            //;
        }
        if(bounces<0){
            Playing.flyingAmmos.removeProjectile(this);

        }
       
    }

    
    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
        calcSpeeds();
    }
    
    public void loadLvlData(int[][] data) {
        this.lvlData = data;
    }

    @Override
    public void die() {
        Playing.flyingAmmos.removeProjectile(this);
    }

    public int getRemaningBounces() {
        return bounces;
    }
}
