/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.awt.image.BufferedImage;
import utils.Constants;
import static utils.Constants.EnemyConstants.*;
import static utils.Constants.PlayerConstants.RUNNING;
import utils.LoadSave;

/** enemy, bounces on walls
 * @author Bossi_Mattia
 */
public class PassiveEnemy extends Enemy {
    
    /**
     * constructor, create enemy
     * @param x coordinate x
     * @param y coordinate y
     */
    public PassiveEnemy(float x, float y) {
        super(x, y);
        TYPE = Constants.EnemyConstants.CRABBY;
        initSprite();
        settings();
        initHitbox(x, y, (int) (22f), (int) (17f));
        LoadAnimations(LoadSave.CRABBY_ATLAS);
        resetMovements();
    }

    /** reset movements override */
    @Override
    protected void resetMovements() {
        right = false;
        left = true;
        jump = false;
    }
    
    /** initialize sprite "constants" */
    private void initSprite() {
        spriteX = Constants.EnemyConstants.CRABBY_WIDTH_DEFAULT;
        spriteY = Constants.EnemyConstants.CRABBY_HEIGHT_DEFAULT;
        xDrawOffset = 26;
        yDrawOffset = 10;
    }
    
    /**Initialize enemy options */
    private void settings() {
        action = RUNNING;
        movSpeed = 0.5f;
        gravity = 0.04f;
        jumpSpeed = -2.5f;
    }

    /** On wall touch event, change walk direction */
    @Override
    protected void onWallTouch() {
        left = !left;
        right = !right;
    }

}
