/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import main.Game;
import utils.Constants;
import static utils.Constants.PlayerConstants.GetSpriteAmount;
import static utils.Constants.PlayerConstants.IDLE;
import static utils.HelpMethods.CanMoveHere;
import static utils.HelpMethods.GetEntityXPosNextToWall;
import static utils.HelpMethods.GetEntityYPosUnderRoofOrAboveFloor;
import static utils.HelpMethods.IsEntityOnFloor;
import utils.LoadSave;

/**
 *
 * @author Bossi_Mattia
 */
public class PassiveEnemy extends Entity{
    
    final int spriteX = 64, spriteY = 40;
    
    
    private float xDrawOffset = 17;
    private float yDrawOffset = 2;
    private int playerAction = IDLE;
    private int playerDir = -1;
    private boolean moving = false, attacking = false;
    private boolean left, up, right, down, jump;
    private float playerSpeed = 0.5f;
    private int[][] lvlData;
    private float airSpeed = 0f;
    private float gravity = 0.04f;
    private float jumpSpeed = -2.5f;
    
    private float fallSpeedAfterCollision = 0.5f;
    private boolean inAir = true;
    
    private BufferedImage animations;
    private int action = IDLE;
    private int aniIndex;
    
    public PassiveEnemy(float x, float y, int width, int height) {
        super(x, y, width, height);
        initHitbox(x, y, (int)(30f), (int)(34f));
        LoadAnimations();
        resetMovements();
    }
    
    public void update(Player p) {
        updatePos(p);
        
        if(hitbox.intersects(p.hitbox)){
            System.out.println("DAMAGE");
            p.reset();
            teleport(x, y);
            resetMovements();
        }
            
    }
    
    public void render(Graphics g, float offsetX, float offsetY) {
        g.drawImage(animations/*[action][aniIndex % animations[action].length]*/, (int) ((hitbox.x - xDrawOffset) * Game.SCALE + offsetX), (int) ((hitbox.y - yDrawOffset) * Game.SCALE + offsetY), (int) (spriteX * Game.SCALE), (int) (spriteY * Game.SCALE), null);
        if(Constants.debug){
            drawHitbox(g, offsetX, offsetY);
        }
    }
    
    
    private void LoadAnimations() {
        BufferedImage img = LoadSave.GetSpriteAtlas("enemy_sprite.png");
        animations = img;
        /*animations = new BufferedImage[9][];
        for (int j = 0; j < animations.length; j++) {
            animations[j] = new BufferedImage[img.getWidth()];
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * spriteX, j * spriteY, spriteX, spriteY);
            }
        }*/
    }
    
    private void updatePos(Player p) {
        
        moving = false;
        if(jump){
            jump();
        }
        if (!left && !right && !inAir) {
            return;
        }
        float xSpeed = 0, ySpeed = 0;

        if (left) {
            xSpeed -=playerSpeed;
        }
        if (right) {
            xSpeed += playerSpeed;
        }
        
        if(!inAir){
            if(!IsEntityOnFloor(hitbox, lvlData)){
                inAir=true;
            }
        }
        
        if(inAir){
            if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height,lvlData)){
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXpos(xSpeed);
            }else{
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if(airSpeed > 0){
                    resetInAir();
                }else{
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXpos(xSpeed);
                
            }
        }else{
            updateXpos(xSpeed);
        }
        moving = true;
    }
    
    private void updateXpos(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        }else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
            left=!left;
            right=!right;
        }
    }
    
    private void jump() {
        if(inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }
    
    private void resetMovements(){
        left=true;
        right=false;
        up=false;
        down=false;
        jump=false;
    }
    
    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }
    
    public void loadLvlData(int[][] data) {
        this.lvlData = data;
    }
   
    public void teleport(float x, float y){
        hitbox.x = x;
        hitbox.y = y;
        inAir=true;
    }
}
