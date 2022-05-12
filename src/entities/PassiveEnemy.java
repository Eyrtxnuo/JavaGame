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

/**
 *
 * @author Bossi_Mattia
 */
public class PassiveEnemy extends Enemy {

    private float fallSpeedAfterCollision = 0.5f;
    private boolean inAir = true;

    private BufferedImage[][] animations;

    public PassiveEnemy(float x, float y) {
        super(x, y);
        TYPE = Constants.EnemyConstants.CRABBY;
        initSprite();
        settings();
        initHitbox(x, y, (int) (22f), (int) (17f));
        LoadAnimations(LoadSave.CRABBY_ATLAS);
        resetMovements();
    }

    @Override
    protected void resetMovements() {
        right = false;
        left = true;
        jump = false;
    }

    private void initSprite() {
        spriteX = Constants.EnemyConstants.CRABBY_WIDTH_DEFAULT;
        spriteY = Constants.EnemyConstants.CRABBY_HEIGHT_DEFAULT;
        xDrawOffset = 26;
        yDrawOffset = 10;
    }

    private void settings() {
        action = RUNNING;
        movSpeed = 0.5f;
        gravity = 0.04f;
        jumpSpeed = -2.5f;
    }

    @Override
    public void update() {
        super.update();
        updateAnimationTick();
        if (hitbox.intersects(p.hitbox)) {
            if (p.getInvincibilityFrame() == 0) {
                p.hit();
                if (p.getLives() == 0) {
                    System.out.println("DEAD");
                    p.reset();
                    teleport(x, y);
                    resetMovements();
                    p.resetLives();
                    p.resetInvincibilityFrame();
                }
            }
        }
    }

    @Override
    protected void onWallTouch() {
        left = !left;
        right = !right;
    }

}
