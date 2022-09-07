/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.awt.Point;
import main.Game;
import utils.Constants;
import static utils.Constants.EnemyConstants.enemyAtlas.*;
import static utils.Constants.EnemyConstants.enemyState.*;
import utils.LoadSave;

/** enemy, flollows player if near enough
 * @author Bossi_Mattia
 */
public class FollowEnemy extends Enemy{
    
    /**
     * constructor, create enemy
     * @param x coordinate x
     * @param y coordinate y
     * @param id enemy id
     */
    public FollowEnemy(float x, float y, int id) {
        super(x, y, id);
        ATLAS_TYPE = CRABBY;
        initSprite();
        settings();
        initHitbox(x, y, (int)(22f), (int)(17f));
        LoadAnimations(LoadSave.CRABBY_ATLAS);
        jumpSpeed = -2.5f;
    }
    
    /** initialize sprite "constants" */
    private void initSprite(){
        spriteX = Constants.EnemyConstants.CRABBY_WIDTH_DEFAULT;
        spriteY =  Constants.EnemyConstants.CRABBY_HEIGHT_DEFAULT;
        xDrawOffset = 26;
        yDrawOffset = 10;
    }
    
    /**Initialize enemy options */
    private void settings(){
        action = RUNNING;
        movSpeed = 0.5f;
        gravity = 0.04f;
        jumpSpeed = -2.5f;
    }
    
    
    /** On wall touch event, jump */
    @Override
    protected void onWallTouch() {
        jump();
    }

    /** set walk direction facing player */
    @Override
    protected void prePosUpdate() {
        Player nearestPl = Game.playing.getNearestPlayer(new Point.Float(hitbox.x+hitbox.width/2,hitbox.y+hitbox.height/2));
        resetMovements();
        if(Math.abs(nearestPl.hitbox.x-hitbox.x)<Game.COORD_WIDTH/2-100){
            if(nearestPl.getHitbox().x > hitbox.x){
                right=true;
            }else if(nearestPl.getHitbox().x < hitbox.x){
                left=true;
            }
        }
    }
}
