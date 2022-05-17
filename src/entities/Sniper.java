/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import static gamestates.Playing.flyingAmmos;
import static gamestates.Playing.levelManager;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import main.Game;
import utils.Constants;
import static utils.Constants.PlayerConstants.RUNNING;
import utils.LoadSave;

/**
 *
 * @author matti
 */
public class Sniper extends Enemy{
    
    private static final double GUN_RANDOMNESS=0.02;//in radians
    private static final int FIRE_SPEED = 500;
    private int fireTick = 0;

    private BufferedImage animations;

    public Sniper(float x, float y) {
        super(x, y);
        TYPE = Constants.EnemyConstants.CRABBY;
        initSprite();
        settings();
        initHitbox(x, y, (int) (17f), (int) (43f));
        //LoadAnimations(LoadSave.BOSS_ATLAS);
        animations = LoadSave.GetSpriteAtlas("zombie_atlas.png");
        resetMovements();
    }

    @Override
    public void render(Graphics g, float offsetX, float offsetY) {
    if((hitbox.x-xDrawOffset+spriteX)*Game.SCALE>-(offsetX) && (hitbox.x-xDrawOffset)*Game.SCALE <Game.GAME_WIDTH-(offsetX)){
            if(dirLeft){
                g.drawImage(animations, (int) ((hitbox.x - xDrawOffset) * Game.SCALE + offsetX), (int) ((hitbox.y - yDrawOffset) * Game.SCALE + offsetY), (int) (spriteX * Game.SCALE), (int) (spriteY * Game.SCALE), null);
            }else{
                g.drawImage(animations, (int) ((hitbox.x - xDrawOffset) * Game.SCALE + offsetX  + spriteX * Game.SCALE), (int) ((hitbox.y - yDrawOffset) * Game.SCALE + offsetY), (int) -(spriteX * Game.SCALE), (int) (spriteY * Game.SCALE), null);
            }
            if(Constants.DEBUG){
                drawHitbox(g, offsetX, offsetY);
            }
            g.drawLine((int)((getHitbox().x-xDrawOffset)*Game.SCALE+offsetX), (int)((getHitbox().y+getHitbox().height/2)*Game.SCALE), (int)((p.getHitbox().x+p.getHitbox().width/2)*Game.SCALE+offsetX), (int)((p.getHitbox().y+p.getHitbox().height/2)*Game.SCALE));
        }
    }
    
    

    @Override
    protected void resetMovements() {
        right = false;
        left = false;
        jump = false;
        dirLeft = true;
    }

    private void initSprite() {
        spriteX = Constants.EnemyConstants.CRABBY_WIDTH_DEFAULT;
        spriteY = Constants.EnemyConstants.CRABBY_HEIGHT_DEFAULT;
        xDrawOffset = 24;
        yDrawOffset = 14;
    }

    private void settings() {
        action = RUNNING;
        movSpeed = 0.5f;
        gravity = 0.04f;
        jumpSpeed = -2.5f;
        lives =2;
        spriteX =64;
        spriteY = 64;
    }

    @Override
    public void update() {
        super.update();
        updateAnimationTick();
        if(fireTick<=0){
            fire();
        }else{
            fireTick--;
        }
    }

    @Override
    protected void onWallTouch() {
        left = !left;
        right = !right;
    }
    
    public void fire(){
        Projectile flyingAmmo = new Projectile(getHitbox().x-xDrawOffset, getHitbox().y+getHitbox().height/2,
                (float)(Math.atan2((getHitbox().x-xDrawOffset)-(p.getHitbox().x+p.getHitbox().width/2), (getHitbox().y+getHitbox().height/2)-(p.getHitbox().y+p.getHitbox().height/2)) +Math.PI/2-GUN_RANDOMNESS/2+Math.random()*GUN_RANDOMNESS));
        flyingAmmo.loadLvlData(levelManager.getLoadedLevel().getLvlData());
        flyingAmmos.enemyAdd(flyingAmmo);
        
        fireTick = FIRE_SPEED;
    }
}
