/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import main.Game;
import utils.Constants;
import static utils.Constants.EnemyConstants.GetSpriteAmount;
import static utils.Constants.EnemyConstants.*;
import static utils.HelpMethods.CanMoveHere;
import static utils.HelpMethods.GetEntityXPosNextToWall;
import static utils.HelpMethods.GetEntityYPosUnderRoofOrAboveFloor;
import static utils.HelpMethods.IsEntityOnFloor;
import utils.LoadSave;

/**
 *
 * @author Bossi_Mattia
 */
public abstract class Enemy extends Entity{
    
    protected int spriteX, spriteY;
    
    protected float xDrawOffset;
    protected float yDrawOffset;
    
    protected int TYPE;
    protected int action = RUNNING;
    protected boolean moving = false, attacking = false;
    protected boolean left, right, jump;
    protected boolean dirLeft = false;
    protected int[][] lvlData;
    protected float airSpeed = 0f;
    protected float movSpeed=0.5f, jumpSpeed=-2.25f, gravity=0.04f;
    
    protected float fallSpeedAfterCollision = 0.5f;
    protected boolean inAir = true;
    
    protected BufferedImage[][] animations;
    
    
    protected int aniTick, aniIndex, aniSpeed = 25;
    
    public Enemy(float x, float y) {
        super(x, y);
        initSprite();
        initHitbox(x, y, (int)(20f), (int)(27f));
    }
    public Enemy(float x, float y, int TYPE) {
        this(x, y);
        this.TYPE = TYPE;
    }
    
    private void initSprite(){
        spriteX = Constants.EnemyConstants.CRABBY_WIDTH_DEFAULT;
        spriteY =  Constants.EnemyConstants.CRABBY_HEIGHT_DEFAULT;
        xDrawOffset = 26;
        yDrawOffset = 7;
    }
    
    public void update(Player p) {
        updatePos(p);
        if(moving){
            action=RUNNING;
        }else{
            action=IDLE;
        }
        /*if(hitbox.intersects(p.hitbox)){
            System.out.println("DAMAGE");
            p.reset();
            teleport(x, y);
        }*/
            
    }
    
    public void render(Graphics g, float offsetX, float offsetY) {
        if((hitbox.x-xDrawOffset+spriteX)*Game.SCALE>-(offsetX) && (hitbox.x-xDrawOffset)*Game.SCALE <Game.GAME_WIDTH-(offsetX)){
            if(dirLeft){
                g.drawImage(animations[action][aniIndex % animations[action].length], (int) ((hitbox.x - xDrawOffset) * Game.SCALE + offsetX), (int) ((hitbox.y - yDrawOffset) * Game.SCALE + offsetY), (int) (spriteX * Game.SCALE), (int) (spriteY * Game.SCALE), null);
            }else{
                g.drawImage(animations[action][aniIndex % animations[action].length], (int) ((hitbox.x - xDrawOffset) * Game.SCALE + offsetX  + spriteX * Game.SCALE), (int) ((hitbox.y - yDrawOffset) * Game.SCALE + offsetY), (int) -(spriteX * Game.SCALE), (int) (spriteY * Game.SCALE), null);
            }
            if(Constants.debug){
                drawHitbox(g, offsetX, offsetY);
            }
        }
    }
    
    
       protected void LoadAnimations(String path) {
        BufferedImage img = LoadSave.GetSpriteAtlas(path);
        
        animations = new BufferedImage[5][];
        for (int j = 0; j < animations.length; j++) {
            animations[j] = new BufferedImage[Constants.EnemyConstants.GetSpriteAmount(TYPE, j)];
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * spriteX, j * spriteY, spriteX, spriteY);
            }
        }
    }
    
    protected void updatePos(Player p) {
        prePosUpdate(p);
        /**/
        moving = false;
        if(jump){
            jump();
        }
        if (!left && !right && !inAir) {
            return;
        }
        float xSpeed = 0, ySpeed = 0;

        if (left) {
            dirLeft = true;
            xSpeed -=movSpeed;
        }
        if (right) {
            dirLeft = false;
            xSpeed += movSpeed;
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
                    onRoofCealingTouch();
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
            onWallTouch();
        }
    }
    
    protected void jump() {
        if(inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }
    
    protected void resetMovements(){
        left=false;
        right=false;
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
    
    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;//= (aniIndex + 1) % GetSpriteAmount(playerAction);
            if (aniIndex >= GetSpriteAmount(TYPE, action)) {
                aniIndex = 0;
                attacking = false;
            }
        }
    }

    protected void onWallTouch(){
        
    }
    
    protected void onRoofCealingTouch(){
        
    }

    protected void prePosUpdate(Player p) {
        
    }
}
