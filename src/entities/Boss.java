/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import static gamestates.Playing.flyingAmmos;
import static gamestates.Playing.levelManager;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import main.Game;
import utils.Constants;
import static utils.Constants.EnemyConstants.IDLE;
import static utils.Constants.PlayerConstants.RUNNING;
import utils.LoadSave;

/**
 *
 * @author matti
 */
public class Boss extends Enemy {
    
    private static final double GUN_RANDOMNESS = Math.PI/3;//in radians
    private static final int FIRE_SPEED = 50;
    private int fireTick = 100;
    
    private final static float xShootOffset = -16, yShootOffset = 3;
    


    public Boss(float x, float y) {
        super(x, y);
        TYPE = Constants.EnemyConstants.BOSS;
        initSprite();
        settings();
        initHitbox(x, y, (int) (38f), (int) (30f));
        LoadAnimations(LoadSave.BOSS_ATLAS);
        resetMovements();
    }
    
    private void initSprite() {
        spriteX = Constants.EnemyConstants.CRABBY_WIDTH_DEFAULT;
        spriteY = Constants.EnemyConstants.CRABBY_HEIGHT_DEFAULT;
        xDrawOffset = 17;
        yDrawOffset = 13;
    }
    
    private void settings() {
        MAX_LIVES = 20;
        resetLives();
        action = RUNNING;
        movSpeed = 0.5f;
        gravity = 0.0f;
        jumpSpeed = -2.5f;
        spriteX = 72;
        spriteY = 72;
        aniSpeed = 50;
    }
    
    /** Render boss on g
     * 
     * @param g graphics
     * @param offsetX render offset x
     * @param offsetY render offset y
     */
    @Override
    public void render(Graphics g, float offsetX, float offsetY) {
        if ((hitbox.x - xDrawOffset + spriteX) * Game.SCALE > -(offsetX) && (hitbox.x - xDrawOffset) * Game.SCALE < Game.GAME_WIDTH - (offsetX)) {
            if (dirLeft) {
                g.drawImage(animations[0][aniIndex], (int) ((hitbox.x - xDrawOffset) * Game.SCALE + offsetX), (int) ((hitbox.y - yDrawOffset) * Game.SCALE + offsetY), (int) (spriteX * Game.SCALE), (int) (spriteY * Game.SCALE), null);
            } else {
                g.drawImage(animations[0][aniIndex], (int) ((hitbox.x - xDrawOffset) * Game.SCALE + offsetX + spriteX * Game.SCALE), (int) ((hitbox.y - yDrawOffset) * Game.SCALE + offsetY), (int) -(spriteX * Game.SCALE), (int) (spriteY * Game.SCALE), null);
            }
            if(lives!=MAX_LIVES){
                drawHealthBar(g, offsetX,  offsetY);
            }
            if (Constants.DEBUG) {
                drawHitbox(g, offsetX, offsetY);
            }
            if (Math.abs(p.hitbox.x - hitbox.x) < Game.COORD_WIDTH / 2 - 100) {
                g.drawLine((int) ((getHitbox().x + xShootOffset) * Game.SCALE + offsetX), (int) ((getHitbox().y + yShootOffset) * Game.SCALE), (int) ((p.getHitbox().x + p.getHitbox().width / 2) * Game.SCALE + offsetX), (int) ((p.getHitbox().y + p.getHitbox().height / 2) * Game.SCALE));
            }
        }
    }
    
    @Override
    public void update() {
        super.update();
        if (fireTick <= 0) {
            if (Math.abs(p.hitbox.x - hitbox.x) < Game.COORD_WIDTH / 2 - 100) {
                fire();
            }
        } else {
            fireTick--;
        }
    }

    @Override
    protected void updateAction() {
        action = IDLE;
    }
    
    
    
    /** Fires a projectile */
    public void fire() {
        Projectile flyingAmmo = new Projectile(getHitbox().x - xDrawOffset, getHitbox().y + getHitbox().height / 2,
                (float) (Math.atan2((getHitbox().x + xShootOffset) - (p.getHitbox().x + yShootOffset), (getHitbox().y + getHitbox().height / 2) - (p.getHitbox().y + p.getHitbox().height / 2)) + Math.PI / 2 - GUN_RANDOMNESS / 2 + Math.random() * GUN_RANDOMNESS));
        flyingAmmo.loadLvlData(levelManager.getLoadedLevel().getLvlData());
        flyingAmmos.enemyAdd(flyingAmmo);

        fireTick = FIRE_SPEED;
    }
    
    
    private static final float BAR_BORDER = 1.5f;
    private static final float BAR_HEIGHT = 3;
    private void drawHealthBar(Graphics g, float offsetX, float offsetY) {
        Color c = g.getColor();
        g.setColor(Color.BLACK);
        g.fillRect((int)((hitbox.x-BAR_BORDER)*Game.SCALE+offsetX), (int)((hitbox.y-(BAR_HEIGHT+2*BAR_BORDER))*Game.SCALE+offsetY), (int)((hitbox.width+BAR_BORDER*2)*Game.SCALE), (int) ((BAR_HEIGHT+2*BAR_BORDER)*Game.SCALE));
        g.setColor(Color.DARK_GRAY);
        g.fillRect((int)((hitbox.x)*Game.SCALE+offsetX), (int)((hitbox.y-(BAR_HEIGHT+BAR_BORDER))*Game.SCALE+offsetY), (int)((hitbox.width)*Game.SCALE), (int) (BAR_HEIGHT*Game.SCALE));
        g.setColor(Color.RED);
        g.fillRect((int)((hitbox.x)*Game.SCALE+offsetX), (int)((hitbox.y-(BAR_HEIGHT+BAR_BORDER))*Game.SCALE+offsetY), (int)((hitbox.width*(lives/(float)MAX_LIVES))*Game.SCALE), (int) (BAR_HEIGHT*Game.SCALE));
        g.setColor(c);
    }
}
