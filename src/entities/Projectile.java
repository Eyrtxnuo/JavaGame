/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import gamestates.Playing;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.text.PlainDocument;
import main.Game;
import utils.Constants;
import static utils.HelpMethods.CanMoveHere;

/**
 *
 * @author matti
 */
public class Projectile extends Entity{
    
    private static final float SPEED = 9f;
    private float angle;
    float xSpeed, ySpeed;
    private int[][] lvlData;
    private float gravity = 0.00f;

    public Projectile(float x, float y, float angle) {
        super(x, y);
        this.angle = -angle;
        xSpeed = (float)Math.cos(this.angle)*SPEED;
        ySpeed = (float)Math.sin(this.angle)*SPEED;
        initHitbox(x, y, 5, 5);
    }
 
    public void render(Graphics g, float offsetX, float offsetY){
        
        g.setColor(Color.black);
        g.fillRect((int)(hitbox.x*Game.SCALE + offsetX), (int)(hitbox.y*Game.SCALE + offsetY), (int)(hitbox.width*Game.SCALE), (int)(hitbox.height*Game.SCALE));
        if(Constants.debug){
            drawHitbox(g, offsetX, offsetY);
        }
    }
 
    public void update(Player p) {
        updatePos();
        /*if(hitbox.intersects(p.hitbox)){
            System.out.println("DAMAGE");
            p.reset();
        }*/
    }

    private void updatePos() {
        if(CanMoveHere(hitbox.x + xSpeed, hitbox.y + ySpeed, hitbox.width, hitbox.height, lvlData)){
            hitbox.y += ySpeed;
            hitbox.x += xSpeed;
            ySpeed += gravity;
        }else{
            //initHitbox(0, 0, 5, 5);
            Playing.flyingAmmos.removeProjectile(this);
        }
            
    }
    
    
    public void loadLvlData(int[][] data) {
        this.lvlData = data;
    }
}
