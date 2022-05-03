/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import static utils.Constants.PlayerConstants.*;
import utils.LoadSave;
import main.Game;
import static utils.HelpMethods.*;

/**
 *
 * @author matti
 */
public class Player extends Entity {

    final int spriteX = 64, spriteY = 40;

    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 25;
    private int playerAction = IDLE;
    private int playerDir = -1;
    private boolean moving = false, attacking = false;
    private boolean left, up, right, down, jump;
    private float playerSpeed = 1f;
    private int[][] lvlData;
    private float xDrawOffset = 21;
    private float yDrawOffset = 4;
    private float airSpeed = 0f;
    private float gravity = 0.04f;
    private float jumpSpeed = -2.25f;
    private float fallSpeedAfterCollision = 0.5f;
    private boolean inAir = true;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        LoadAnimations();
        initHitbox(x, y, (int)(20f), (int)(27f));
    }

    public void update() {
        updatePos();
        updateAnimationTick();
        setAnimation();
    }

    public void render(Graphics g, float offsetX, float offsetY) {
        if (left) {
         g.drawImage(animations[playerAction][aniIndex % animations[playerAction].length], (int) ((hitbox.x - xDrawOffset) * Game.SCALE + offsetX + spriteX * Game.SCALE ) , (int) ((hitbox.y - yDrawOffset) * Game.SCALE + offsetY), -(int) (spriteX * Game.SCALE), (int) (spriteY * Game.SCALE), null);
        } else {
            g.drawImage(animations[playerAction][aniIndex % animations[playerAction].length], (int) ((hitbox.x - xDrawOffset) * Game.SCALE + offsetX), (int) ((hitbox.y - yDrawOffset) * Game.SCALE + offsetY), (int) (spriteX * Game.SCALE), (int) (spriteY * Game.SCALE), null);
        }
        //g.drawImage(animations[playerAction][aniIndex % animations[playerAction].length], (int) ((hitbox.x - xDrawOffset) * Game.SCALE + offsetX), (int) ((hitbox.y - yDrawOffset) * Game.SCALE + offsetY), (int) (spriteX * Game.SCALE), (int) (spriteY * Game.SCALE), null);
        //drawHitbox(g, offsetX, offsetY);
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    private void setAnimation() {
        int startAni = playerAction;
        if (moving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }
        if (attacking) {
            playerAction = ATTACK_1;
        }

        if (startAni != playerAction) {
            resetAniTick();
        }
    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void updatePos() {
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

//        if (up) {
//            ySpeed -= playerSpeed;
//        }
//        if (down) {
//            ySpeed += playerSpeed;
//        }

//        if(CanMoveHere(hitbox.x+xSpeed, hitbox.y+ySpeed, hitbox.width, hitbox.height, lvlData)){
//            hitbox.x += xSpeed;
//            hitbox.y += ySpeed;
//            moving = true;
//        }
    }
    
    private void updateXpos(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        }else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }
    
    private void jump() {
        if(inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;//= (aniIndex + 1) % GetSpriteAmount(playerAction);
            if (aniIndex >= GetSpriteAmount(playerAction)) {
                aniIndex = 0;
                attacking = false;
            }
        }
    }

    public void resDirBools() {
        left = false;
        right = false;
        up = false;
        down = false;
        jump = false;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isJump() {
        return jump;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }
    
    

    public boolean isAttacking() {
        return attacking;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    private void LoadAnimations() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
        animations = new BufferedImage[9][];
        for (int j = 0; j < animations.length; j++) {
            animations[j] = new BufferedImage[GetSpriteAmount(j)];
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * spriteX, j * spriteY, spriteX, spriteY);
            }
        }
    }

    public void loadLvlData(int[][] data) {
        this.lvlData = data;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }
    
    public void teleport(float x, float y){
        hitbox.x = x;
        hitbox.y = y;
        inAir=true;
    }
    
    public void reset(){
        teleport(x, y);
        //resDirBools();
        moving = false;
        attacking = false;
        aniTick = 0 ;
        aniIndex = 0;
        airSpeed=0;
    }
   
}
