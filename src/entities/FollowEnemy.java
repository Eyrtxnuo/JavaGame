/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import utils.Constants;
import static utils.Constants.PlayerConstants.*;
import utils.LoadSave;

/**
 *
 * @author Bossi_Mattia
 */
public class FollowEnemy extends Enemy{
    
    private boolean moving = false, attacking = false;

    
    private float fallSpeedAfterCollision = 0.5f;
    
    
    
    public FollowEnemy(float x, float y) {
        super(x, y);
        TYPE = Constants.EnemyConstants.CRABBY;
        initSprite();
        settings();
        initHitbox(x, y, (int)(22f), (int)(17f));
        LoadAnimations(LoadSave.CRABBY_ATLAS);
        jumpSpeed = -2.5f;
    }
    
    private void initSprite(){
        spriteX = Constants.EnemyConstants.CRABBY_WIDTH_DEFAULT;
        spriteY =  Constants.EnemyConstants.CRABBY_HEIGHT_DEFAULT;
        xDrawOffset = 26;
        yDrawOffset = 10;
    }
    private void settings(){
        action = RUNNING;
        movSpeed = 0.5f;
        gravity = 0.04f;
        jumpSpeed = -2.5f;
    }
    
    @Override
    public void update(Player p) {
        super.update(p);
        updateAnimationTick();
        if(hitbox.intersects(p.hitbox)){
            System.out.println("DAMAGE");
            p.reset();
            teleport(x, y);
        }
            
    }
    
    
    @Override
    protected void onWallTouch() {
        jump();
    }

    
    @Override
    protected void prePosUpdate(Player p) {
        resetMovements();
        if(p.getHitbox().x > hitbox.x){
            right=true;
        }else if(p.getHitbox().x < hitbox.x){
            left=true;
        }
    }
    
    
}
