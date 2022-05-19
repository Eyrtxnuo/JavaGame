/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import main.Game;
import utils.Constants;
import static utils.Constants.PlayerConstants.*;
import utils.LoadSave;

/**
 *
 * @author Bossi_Mattia
 */
public class FollowEnemy extends Enemy{
    
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
    protected void onWallTouch() {
        jump();
    }

    
    @Override
    protected void prePosUpdate(Player p) {
        resetMovements();
        if(Math.abs(p.hitbox.x-hitbox.x)<Game.COORD_WIDTH/2-100){
            if(p.getHitbox().x > hitbox.x){
                right=true;
            }else if(p.getHitbox().x < hitbox.x){
                left=true;
            }
        }
    }
    
    
}
