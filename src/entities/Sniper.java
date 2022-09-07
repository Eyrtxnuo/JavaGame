/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import main.Game;
import utils.Constants;
import static utils.Constants.EnemyConstants.enemyAtlas.CRABBY;
import static utils.Constants.EnemyConstants.enemyState.RUNNING;
import utils.LoadSave;

/** enemy sniper, doesn't move, 2hp
 * @author matti
 */
public class Sniper extends Enemy {

    /** gun direction randomness, in radians  */
    private static final double GUN_RANDOMNESS = 0.02;//in radians
    /** frames between shots */
    private static final int FIRE_SPEED = 500;
    /** frame for next shot */
    private int fireTick = 100;
    
    /** hitbox offset for shooting point */
    private final static float xShootOffset = -16, yShootOffset = 3;
    
    /** Sprite image */
    private BufferedImage animation;

    /** constructor with spawn coordinates
     * @param x coordinate x
     * @param y coordinate y
     * @param id enemy id
     */
    public Sniper(float x,  float y,int id) {
        super(x, y, id);
        ATLAS_TYPE = CRABBY;
        initSprite();
        settings();
        initHitbox(x, y, (int) (24f), (int) (30f));
        //LoadAnimations(LoadSave.BOSS_ATLAS);
        animation = LoadSave.GetSpriteAtlas("zombie_atlas.png");
        resetMovements();
    }

    /** Render enemy on g
     * 
     * @param g graphics
     * @param offsetX render offset x
     * @param offsetY render offset y
     */
    @Override
    public void render(Graphics g, float offsetX, float offsetY) {
        if ((hitbox.x - xDrawOffset + spriteX) * Game.SCALE > -(offsetX) && (hitbox.x - xDrawOffset) * Game.SCALE < Game.GAME_WIDTH - (offsetX)) {
            g.drawImage(animation, (int) ((hitbox.x - xDrawOffset) * Game.SCALE + offsetX), (int) ((hitbox.y - yDrawOffset) * Game.SCALE + offsetY), (int) (spriteX * Game.SCALE), (int) (spriteY * Game.SCALE), null);
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

    /** initialize sprite "constants" */
    private void initSprite() {
        spriteX = Constants.EnemyConstants.CRABBY_WIDTH_DEFAULT;
        spriteY = Constants.EnemyConstants.CRABBY_HEIGHT_DEFAULT;
        xDrawOffset = 20;
        yDrawOffset = 26;
    }

    /**Initialize enemy options */
    private void settings() {
        MAX_LIVES = 2;
        resetLives();
        action = RUNNING;
        movSpeed = 0.5f;
        gravity = 0.04f;
        jumpSpeed = -2.5f;
        spriteX = 64;
        spriteY = 64;
    }

    /** update tick */
    @Override
    public void update() {
        super.update();
        updateAnimationTick();
        Player nearestPl = Game.playing.getNearestPlayer(new Point.Float(hitbox.x+hitbox.width/2,hitbox.y+hitbox.height/2));
        if (fireTick <= 0) {
            if (Math.abs(nearestPl.hitbox.x - hitbox.x) < Game.COORD_WIDTH / 2 - 100) {
                fire();
            }
        } else {
            fireTick--;
        }
    }


    /** Fires a projectile */
    public void fire() {
        Player nearestPl = Game.playing.getNearestPlayer(new Point.Float(hitbox.x+hitbox.width/2,hitbox.y+hitbox.height/2));
        Projectile flyingAmmo = new Projectile(getHitbox().x + xShootOffset, getHitbox().y + yShootOffset,
                (float) (Math.atan2((getHitbox().x + xShootOffset) - (nearestPl.getHitbox().x +nearestPl.getHitbox().width/2), (getHitbox().y + yShootOffset) - (nearestPl.getHitbox().y + nearestPl.getHitbox().height / 2)) + Math.PI / 2 - GUN_RANDOMNESS / 2 + Math.random() * GUN_RANDOMNESS));
        flyingAmmo.loadLvlData(Game.playing.levelManager.getLoadedLevel().getLvlData());
        Game.playing.flyingAmmos.enemyAdd(flyingAmmo);

        fireTick = FIRE_SPEED;
    }

    
    /** bar draw constants */
    private static final float BAR_BORDER = 1.5f, BAR_HEIGHT = 3, BAR_DISTANCE = 5;
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
}
