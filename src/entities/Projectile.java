/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import java.awt.Graphics;
import utils.Constants;
import static utils.HelpMethods.CanMoveHere;
import static utils.HelpMethods.GetEntityXPosNextToWall;
import static utils.HelpMethods.GetEntityYPosUnderRoofOrAboveFloor;
import static utils.HelpMethods.IsEntityOnFloor;

/**
 *
 * @author matti
 */
public class Projectile extends Entity{
    
    private static final float SPEED = 3f;
    private float angle;
    float xSpeed, ySpeed;
    private int[][] lvlData;
    private float gravity = 0.01f;

    public Projectile(float angle) {
        super(0, 0);
        this.angle = angle;
        xSpeed = (float)Math.cos(x)*SPEED;
        ySpeed = (float)Math.sin(x)*SPEED;
        
        initHitbox(100, 100, 5, 5);
    }
 
    public void render(Graphics g, float offsetX, float offsetY){
        g.fillRect((int)(hitbox.x + offsetX), (int)(hitbox.y + offsetY), (int)hitbox.width, (int)hitbox.height);
        if(Constants.debug){
            drawHitbox(g, offsetX, offsetY);
        }
    }
 
    public void update(Player p) {
        updatePos();
        if(hitbox.intersects(p.hitbox)){
            System.out.println("DAMAGE");
            p.reset();
        }
    }

    private void updatePos() {
        
        if(CanMoveHere(hitbox.x + xSpeed, hitbox.y + ySpeed, hitbox.width, hitbox.height, lvlData)){
            hitbox.y += ySpeed;
            hitbox.x += xSpeed;
            ySpeed += gravity;
        }else{
            initHitbox(0, 0, 5, 5);
        }
            
    }
    
    
    public void loadLvlData(int[][] data) {
        this.lvlData = data;
    }
}
