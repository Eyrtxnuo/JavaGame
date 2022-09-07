/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import static utils.Constants.PlayerConstants.*;
import utils.LoadSave;
import main.Game;
import org.json.JSONObject;
import utils.AudioPlayer;
import utils.Constants;
import static utils.HelpMethods.*;
import utils.Utils;

/**
 * Player class, controlled via keyboard
 * @author matti
 */
public class Player extends Entity {

    /** sprite atlas pixels */
    final int spriteX = 64, spriteY = 40;

    /** palyer username */
    private String username;
    /** animations sprites */
    private BufferedImage[][] animations;
    /** ticks to change animations */
    private int aniTick, aniIndex, aniSpeed = 25;
    /** current action to display */
    private int playerAction = IDLE;
    /** booleans of actions */
    private boolean moving = false, attacking = false;
    /** inputs pressed variables */
    private boolean left, up, right, down, jump;
    /** facing direction */
    private boolean dirLeft = false;
    /** speed per frame */
    private float playerSpeed = 1f;
    /** level tiles data */
    private int[][] lvlData;
    /** texture offset */
    private float xDrawOffset = 21, yDrawOffset = 4;
    /** current air vertical speed */
    private float airSpeed = 0f;
    /** gravity applied every frame */
    private float gravity = 0.04f;
    /** vertical speed applied on jump */ 
    private float jumpSpeed = -2.25f;
    /** vertical speed applied on roof touch */ 
    private float fallSpeedAfterCollision = 0.5f;
    /** if player is in air */
    private boolean inAir = true;
    
    /** player max lives*/
    protected int MAX_LIVES = 3;
    /** current lives */
    protected int lives = MAX_LIVES;
    
    /** frames to be invincibile for */
    private int invincibilityFrame = 0;

    /** constructor
     * @param x coordinates x
     * @param y coordinates y
     */
    public Player(float x, float y) {
        super(x, y);
        LoadAnimations();
        initHitbox(x, y, (int) (20f), (int) (27f));
    }
    
    public Player(Point.Float position) {
        this(position.x, position.y);
    }
    
    private Player(float spawnX, float spawnY, boolean left, boolean right, boolean jump, Point.Float position, int lives, int invincibilityFrame, boolean dirLeft, boolean moving, boolean attacking){
        this(spawnX,spawnY);
        hitbox.x = position.x;
        hitbox.y = position.y;
        this.left = left;
        this.right = right;
        this.jump = jump; 
        this.lives = lives;
        this.invincibilityFrame = invincibilityFrame;
        this.dirLeft = dirLeft;
        this.moving = moving;
        this.attacking = attacking;
    }
    
    private Player(float spawnX, float spawnY, boolean left, boolean right, boolean jump, Point.Float position, int lives, int invincibilityFrame, boolean dirLeft, boolean moving, boolean attacking, String username){
        this(spawnX, spawnY, left, right, jump, position, lives, invincibilityFrame, dirLeft, moving, attacking);
        this.username = username;
    }

    /** Update tick */ 
    @Override
    public void update() {
        super.update();
        updateBlockCollision();
        updatePos();
        updateAnimationTick();
        setAnimation();
        if (invincibilityFrame > 0) {
            invincibilityFrame--;
        } else {
            projCollision();
        }
    }

    /** Checks collision with enemies projectile */
    public void projCollision() {
        var muniz = (LinkedList<Projectile>) Game.playing.flyingAmmos.getEnemyProjectiles().clone();
        muniz.forEach(ammo -> {
            if (hitbox.intersects(ammo.hitbox)) {
                hit();
                ammo.die();
            }
        });
    }

    /** Render player on g if it is in the screen horizontal coordinates
     * 
     * @param g Graphics object to draw on
     * @param offsetX horizontal offset of screen
     * @param offsetY vertical offset of screen
    */
    public void render(Graphics g, float offsetX, float offsetY) {
        if (!Game.playing.isPaused() && !Game.playing.isDeath() && !Game.playing.isEndGame() && this == Game.playing.player) {
            dirLeft = Game.playing.pointerX < (hitbox.x + hitbox.width / 2) * Game.SCALE + offsetX;
        }
        if (dirLeft) {
            g.drawImage(animations[playerAction][aniIndex % animations[playerAction].length], (int) ((hitbox.x - xDrawOffset) * Game.SCALE + offsetX + spriteX * Game.SCALE), (int) ((hitbox.y - yDrawOffset) * Game.SCALE + offsetY), -(int) (spriteX * Game.SCALE), (int) (spriteY * Game.SCALE), null);
        } else {
            g.drawImage(animations[playerAction][aniIndex % animations[playerAction].length], (int) ((hitbox.x - xDrawOffset) * Game.SCALE + offsetX), (int) ((hitbox.y - yDrawOffset) * Game.SCALE + offsetY), (int) (spriteX * Game.SCALE), (int) (spriteY * Game.SCALE), null);
        }
        //g.drawImage(animations[playerAction][aniIndex % animations[playerAction].length], (int) ((hitbox.x - xDrawOffset) * Game.SCALE + offsetX), (int) ((hitbox.y - yDrawOffset) * Game.SCALE + offsetY), (int) (spriteX * Game.SCALE), (int) (spriteY * Game.SCALE), null);
        if (Constants.DEBUG) {
            drawHitbox(g, offsetX, offsetY);
        }
    }

    
    /** set moving acion */
    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    /** set animations from actions */
    private void setAnimation() {
        int startAni = playerAction;
        if (moving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }
        if (attacking) {
            playerAction = ATTACK_1;
        }
        if (invincibilityFrame > 0) {
            playerAction = HIT;
        }

        if (startAni != playerAction) {
            resetAniTick();
        }
    }

    /** reset aniations timing */
    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }
    
    /** update positions, calculating tiles collisions */
    private void updatePos() {
        moving = false;
        if (jump) {
            jump();
        }
        if (left == right && !inAir) {
            return;
        }
        float xSpeed = 0, ySpeed = 0;

        if (left) {
            //dirLeft = true;
            xSpeed -= playerSpeed;
        }
        if (right) {
            //dirLeft = false;
            xSpeed += playerSpeed;
        }

        if (!inAir) {
            if (!IsEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
            }
        }

        if (inAir) {
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXpos(xSpeed);
            } else {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0) {
                    resetInAir();
                } else {
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXpos(xSpeed);

            }
        } else {
            updateXpos(xSpeed);
        }
        moving = true;

//        if (up) {
//            ySpeed -= playerSpeed;
//        }
//        if (down) {
//            ySpeed += playerSpeed;
//        }
//        if(CanMoveHere(hitbox.x+xSpeed, hitbox.y+ySpeed, hitbox.width, hitbox.height, lvlData)){
//            hitbox.x += xSpeed;
//            hitbox.y += ySpeed;
//            moving = true;
//        }
    }

    /** update horizontal positions, calculating tiles collisions */
    private void updateXpos(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }
    
    private void updateBlockCollision() {
        if(getStandingBlock(hitbox, lvlData)==3){
            hit();
            jump();
        }
    }

    /** make player jump */
    private void jump() {
        if (inAir) {
            return;
        }
        inAir = true;
        airSpeed = jumpSpeed;
        AudioPlayer.playEffect(AudioPlayer.Effects.JUMP);
    }

    /** advance animation timing */
    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;//= (aniIndex + 1) % GetSpriteAmount(playerAction);
            if (aniIndex >= GetSpriteAmount(playerAction)) {
                aniIndex = 0;
                attacking = false;
            }
        }
    }

    /** reset input booleans */
    public void resDirBools() {
        left = false;
        right = false;
        up = false;
        down = false;
        jump = false;
    }

    /** get if left is pressed
     * @return if left is pressed */
    public boolean isLeft() {
        return left;
    }
    
    /** set left pressed status
     * @param left new status  */
    public void setLeft(boolean left) {
        this.left = left;
    }
    
    /** get if up is pressed 
     @return if up is pressed*/
    public boolean isUp() {
        return up;
    }

    /** set up pressed status 
     * @param up new status*/
    public void setUp(boolean up) {
        this.up = up;
    }

    /** get if right is pressed 
     @return if right is pressed*/
    public boolean isRight() {
        return right;
    }

    /** set right pressed status 
     * @param right new status*/
    public void setRight(boolean right) {
        this.right = right;
    }

    /** get if down is pressed
     * @return if down is pressed */
    public boolean isDown() {
        return down;
    }

    /** set down pressed status 
     * @param down new status*/
    public void setDown(boolean down) {
        this.down = down;
    }

    /** get if jump is pressed
     * @return if jump is pressed */
    public boolean isJump() {
        return jump;
    }

    /** set jump pressed status 
     * @param jump new status */
    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public boolean isAttacking() {
        return attacking;
    }

    /** set attacking status
     * @param attacking new status*/
    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    /** load animations atlas */
    private void LoadAnimations() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
        animations = new BufferedImage[9][];
        for (int j = 0; j < animations.length; j++) {
            animations[j] = new BufferedImage[GetSpriteAmount(j)];
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * spriteX, j * spriteY, spriteX, spriteY);
            }
        }
    }

    /** load level tiles data
     * @param data level tiles data*/
    public void loadLvlData(int[][] data) {
        this.lvlData = data;
    }

    /** reset in air status */
    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    /** teleport player
     * @param x x new coordinate
     * @param y y new coordinate */
    public void teleport(float x, float y) {
        hitbox.x = x;
        hitbox.y = y;
        inAir = true;
    }

    /** reset player to initial status */
    public void reset() {
        teleport(x, y);
        //resDirBools();
        moving = false;
        attacking = false;
        aniTick = 0;
        aniIndex = 0;
        airSpeed = 0;
        inAir = true;
        resetLives();
        resetAniTick();
        resetInvincibilityFrame();
    }

    /** get current lives */
    public int getLives() {
        return lives;
    }

    /** reset lives to start value */
    public void resetLives() {
        lives = MAX_LIVES;
    }

    /** hit player event handler */
    public void hit() {
        if(invincibilityFrame>0)return;
        
        System.out.println("HIT");
        lives--;
        invincibilityFrame = 400;
        playerAction = Constants.PlayerConstants.HIT;
        if (getLives() <= 0) {
            die();
        } else {
            AudioPlayer.playEffect(AudioPlayer.Effects.DAMAGE);
        }
    }

    /** get invincibility remaning ticks */
    public int getInvincibilityFrame() {
        return invincibilityFrame;
    }

    /** remove invincibility time */
    public void resetInvincibilityFrame() {
        invincibilityFrame = 0;
    }

    /** die player event handler */
    @Override
    public void die() {
        //Playing.enemies.removeAllEnemies();
        System.out.println("DEAD");
        Game.playing.playerDeath(this);

    }

    
    
    public Point.Float getSpawnPosition(){
        return new Point.Float(x, y);
    }
    
    public JSONObject toJSONObject(){
        return new JSONObject()
                .put("spawnX", x)
                .put("spawnY", y)
                .put("left", left)
                .put("right",right)
                .put("jump",jump)
                .put("position", Utils.jsonMapper.pointToJSON(getPosition()))
                .put("lives", lives)
                .put("invincibilityFrame", invincibilityFrame)
                .put("dirLeft", dirLeft)
                .put("moving", moving)
                .put("attacking", attacking)
                .put("airSpeed", airSpeed)
                //.put("username", username)
                ;
        
    }
    
    public void updateWithJson(JSONObject obj){
        x = obj.getFloat("spawnX");
        y = obj.getFloat("spawnY");
        left = obj.getBoolean("left");
        right = obj.getBoolean("right");
        jump = obj.getBoolean("jump");
        setPosition(Utils.jsonMapper.JSONTOPoint(obj.getJSONArray("position")));
        lives = obj.getInt("lives");
        invincibilityFrame = obj.getInt("invincibilityFrame");
        dirLeft = obj.getBoolean("dirLeft");
        moving = obj.getBoolean("moving");
        attacking = obj.getBoolean("attacking");
        airSpeed = obj.getFloat("airSpeed");
        //username = obj.getString("username");
    }

    public String getUsername() {
        return username;
    }

    public Player setUsername(String username) {
        this.username = username;
        return this;
    }

    public Point.Float getCenterPoint() {
        return new Point2D.Float(hitbox.x+hitbox.width/2, hitbox.y+hitbox.height/2);
    }

}
