/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import main.Game;
import utils.Constants;
import static utils.Constants.EnemyConstants.enemyState.*;
import static utils.Constants.EnemyConstants.enemyAtlas.*;

import utils.LoadSave;

/** enemy boss, flies, 20hp
 * @author matti
 */
public class Boss extends Enemy {
    
    /** gun direction randomness, in radians */
    private static final double GUN_RANDOMNESS = Math.PI/3;//in radians
    /** frames between shots */
    private static final int FIRE_SPEED = 50;
    /** frame for next shot */
    private int fireTick = 100;
    
    /** barrel to fire next*/
    private int barrel = 0;
    /** barrels offsets */
    private final static float[] xShootOffset = {21.5f, 54f}, yShootOffset = {117.5f, 117.5f};
    

    /** constructor with spawn coordinates
    * @param x coordinate x
    * @param y coordinate y 
    * @param id enemy id
    */
    public Boss(float x, float y,int id) {
        super(x, y, id);
        ATLAS_TYPE = BOSS;
        initSprite();
        settings();
        initHitbox(x, y, (int) (76f), (int) (60f));
        LoadAnimations(LoadSave.BOSS_ATLAS);
        resetMovements();
    }
    
    /** initialize sprite "constants" */
    private void initSprite() {
        spriteX = Constants.EnemyConstants.CRABBY_WIDTH_DEFAULT;
        spriteY = Constants.EnemyConstants.CRABBY_HEIGHT_DEFAULT;
        xDrawOffset = 34;
        yDrawOffset = 26;
    }
    
    /**Initialize enemy options */
    private void settings() {
        MAX_LIVES = 20;
        resetLives();
        action = Constants.EnemyConstants.enemyState.RUNNING;
        movSpeed = 0.2f;
        gravity = 0.0f;
        jumpSpeed = -2.5f;
        spriteX = 144;
        spriteY = 144;
        aniSpeed = 50;
    }
    
    /** Render boss on g if it is in the screen horizontal coordinates
     * 
     * @param g Graphics object to draw on
     * @param offsetX horizontal offset of screen
     * @param offsetY vertical offset of screen
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
                for(int i = 0; i< xShootOffset.length; i++){ 
                    g.drawLine((int) ((getHitbox().x + xShootOffset[i]) * Game.SCALE + offsetX), (int) ((getHitbox().y + yShootOffset[i]) * Game.SCALE), (int) ((p.getHitbox().x + p.getHitbox().width / 2) * Game.SCALE + offsetX), (int) ((p.getHitbox().y + p.getHitbox().height / 2) * Game.SCALE));
                }
             }
        }
    }
    
    /** update boss, calculate positions */
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

    /** set action */
    @Override
    protected void updateAction() {
        action =  IDLE;
    }
    
    
    
    /** Fires a projectile */
    public void fire() {
        Projectile flyingAmmo = new Projectile(getHitbox().x + xShootOffset[barrel], getHitbox().y + yShootOffset[barrel],
            (float) (Math.atan2((getHitbox().x + xShootOffset[barrel]) - (p.getHitbox().x+p.getHitbox().width/2), (getHitbox().y +  yShootOffset[barrel]) - (p.getHitbox().y + p.getHitbox().height / 2)) + Math.PI / 2 - GUN_RANDOMNESS / 2 + Math.random() * GUN_RANDOMNESS));
        flyingAmmo.loadLvlData(Game.playing.levelManager.getLoadedLevel().getLvlData());
        Game.playing.flyingAmmos.enemyAdd(flyingAmmo);
        barrel = ++barrel%xShootOffset.length;
        fireTick = FIRE_SPEED;
    }
    
    /** bar draw constants */
    private static final float BAR_BORDER = 1.5f, BAR_HEIGHT = 3, BAR_DISTANCE = 15;
    /** draw health bar */
    private void drawHealthBar(Graphics g, float offsetX, float offsetY) {
        Color c = g.getColor();
        g.setColor(Color.BLACK);
        g.fillRect((int)((hitbox.x-BAR_BORDER)*Game.SCALE+offsetX), (int)((hitbox.y-(BAR_HEIGHT+2*BAR_BORDER+BAR_DISTANCE))*Game.SCALE+offsetY), (int)((hitbox.width+BAR_BORDER*2)*Game.SCALE), (int) ((BAR_HEIGHT+2*BAR_BORDER)*Game.SCALE));
        g.setColor(Color.DARK_GRAY);
        g.fillRect((int)((hitbox.x)*Game.SCALE+offsetX), (int)((hitbox.y-(BAR_HEIGHT+BAR_BORDER+BAR_DISTANCE))*Game.SCALE+offsetY), (int)((hitbox.width)*Game.SCALE), (int) (BAR_HEIGHT*Game.SCALE));
        g.setColor(Color.RED);
        g.fillRect((int)((hitbox.x)*Game.SCALE+offsetX), (int)((hitbox.y-(BAR_HEIGHT+BAR_BORDER+BAR_DISTANCE))*Game.SCALE+offsetY), (int)((hitbox.width*(lives/(float)MAX_LIVES))*Game.SCALE), (int) (BAR_HEIGHT*Game.SCALE));
        g.setColor(c);
    }
    
    /** set direction to player position */
    @Override
    protected void prePosUpdate() {
        Player nearestPl = Game.playing.getNearestPlayer(new Point.Float(hitbox.x+hitbox.width/2,hitbox.y+hitbox.height/2));
        resetMovements();
        if(Math.abs(nearestPl.hitbox.x-hitbox.x)<Game.COORD_WIDTH/2){
            if(nearestPl.getHitbox().x-hitbox.width/2-5 > hitbox.x){
                right=true;
            }else if(nearestPl.getHitbox().x-hitbox.width/2+5 < hitbox.x){
                left=true;
            }
        }
    }
    
    
    @Override
    public void die() {
        super.die(); 
        Game.playing.gameWin();
        
    }
    
}
