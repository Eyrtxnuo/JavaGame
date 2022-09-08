package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import main.Game;
import org.json.JSONObject;
import utils.AudioPlayer;
import utils.Constants;
import static utils.Constants.EnemyConstants.GetSpriteAmount;
import static utils.Constants.EnemyConstants.*;
import static utils.Constants.EnemyConstants.enemyState.*;
import static utils.Constants.EnemyConstants.enemyType.*;
import static utils.HelpMethods.CanMoveHere;
import static utils.HelpMethods.GetEntityXPosNextToWall;
import static utils.HelpMethods.GetEntityYPosUnderRoofOrAboveFloor;
import static utils.HelpMethods.IsEntityOnFloor;
import utils.LoadSave;
import utils.Updater;
import utils.Utils;

/**Abstract class to declare enemies
 * 
 * @author BossiMattia
 * 
 * Enemy class, implements collision, movements, animations of enemies, 
 * a class that extends Enemy can override methods to create a
 * personalized behaviour(base functions don't do anything) 
 * 
 * @see Entity
 */
public abstract class Enemy extends Entity{
    
    /** sprite dimensons in pixel */
    protected int spriteX, spriteY;
    
    /** offset from sprite top-left angle and hitbox top-left angle */
    protected float xDrawOffset, yDrawOffset;
    
    /** enemy type */
    protected enemyAtlas ATLAS_TYPE;
    public int id;
    
    /** enemy action, for animations */
    protected enemyState action = RUNNING;
    /** enemy actions animations */
    protected boolean moving = false, attacking = false;
    /** enemy movements(inputs) */
    protected boolean left, right, jump;
    /** direction that the enemy is looking at */
    protected boolean dirLeft = false;
    /** levelData for collisions, blocks as int[][] */
    protected int[][] lvlData;
    /** current vertical speed */
    protected float airSpeed = 0f;
    /** movement constants */
    protected float movSpeed=0.5f, jumpSpeed=-2.25f, gravity=0.04f, fallSpeedAfterCollision = 0.5f;
    /** is Enemy in Air, for applying graavity and animations */
    protected boolean inAir = true;
    /** enemy max lives*/
    protected int MAX_LIVES = 1;
    /** enemy lives */
    protected int lives = MAX_LIVES;
    
    
    /** array of animation sequences */
    protected BufferedImage[][] animations;
    /** variable to track animations and update them */
    protected int aniTick, aniIndex, aniSpeed = 25;
    
    /** updater thread */
    Updater updater;
    /** Player reference */
    Player p;
    
    
    /** default enemy constructor, 
    * 
    * Initialize enemy object
    * calls initSprite to load sprite render configuration
    * @param x X coordinate of enemy
    * @param y Y coordinate of enemy
    * @param id enemy id
    */
    public Enemy(float x, float y,int id) {
        super(x, y);
        this.id = id;
        initSprite();
        initHitbox(x, y, (int)(20f), (int)(27f));
        updater = new Updater(() -> {update(); return null;}, Game.UPS_SET);
        
    }
    /** ATLAS_TYPE enemy constructor, 
 
 Initialize enemy object, declaring the type
 calls the {@link entities.Enemy#Enemy(float, float, int) default constructor}
    * @param x X coordinate of enemy
    * @param y Y coordinate of enemy
    * @param id enemy id
    * @param TYPE enemy type
    */
    public Enemy(float x, float y,int id, enemyAtlas TYPE) {
        this(x, y, id);
        this.ATLAS_TYPE = TYPE;
    }
    
    /** load sprite render configuration, 
    * 
    * Initialize spriteX, spriteY, xDrawOffset, yDrawOffset.
    * should be overwritten by a subclass to show correctly the sprite
    */
    private void initSprite(){
        spriteX = Constants.EnemyConstants.CRABBY_WIDTH_DEFAULT;
        spriteY =  Constants.EnemyConstants.CRABBY_HEIGHT_DEFAULT;
        xDrawOffset = 26;
        yDrawOffset = 7;
    }
    
    /** does an update cycle,
    * 
    * moves the hitbox(and the sprite) of the enemy
    * ¿¿maybe?? ¿¿check collision with player??
    * 
    * call updatePos(); to update positions
    * 
    */
    @Override
    public void update() {
        if(!Game.playing.enemies.getEnemies().contains(this)){
            System.err.println("ENEMY ERROR: NOT REGISTERED, STOPPING("+id+")");
            updater.stopThread();
        }
        super.update();
        updateAnimationTick();
        updatePos();
        updateAction();
        projCollision();
        if (updater.isRunning() && hitbox.intersects(p.hitbox)) {
            if (p.getInvincibilityFrame() <= 0) {
                p.hit();
            }
        }
            
    }
    
    protected void updateAction(){
        if(moving){
            action=RUNNING;
        }else{
            action=IDLE;
        }
    }
    
    /** calculates project collisions */
    public void projCollision(){
        var muniz = (LinkedList<Projectile>)Game.playing.flyingAmmos.getProjectiles().clone();
        muniz.forEach(ammo ->{
            if(hitbox.intersects(ammo.hitbox)){
                hit();
                ammo.die();
            }
        });
    }
    
    /** render the enemy on Graphics g with the offset given,
     * 
     * if it is in the screen horizontal coordinates
     * 
     * @param g Graphics object to draw on
     * @param offsetX horizontal offset of screen
     * @param offsetY vertical offset of screen
    */
    public void render(Graphics g, float offsetX, float offsetY) {
        if((hitbox.x-xDrawOffset+spriteX)*Game.SCALE>-(offsetX) && (hitbox.x-xDrawOffset)*Game.SCALE <Game.GAME_WIDTH-(offsetX)){
            if(dirLeft){
                g.drawImage(animations[action.ordinal()][aniIndex % animations[action.ordinal()].length], (int) ((hitbox.x - xDrawOffset) * Game.SCALE + offsetX), (int) ((hitbox.y - yDrawOffset) * Game.SCALE + offsetY), (int) (spriteX * Game.SCALE), (int) (spriteY * Game.SCALE), null);
            }else{
                g.drawImage(animations[action.ordinal()][aniIndex % animations[action.ordinal()].length], (int) ((hitbox.x - xDrawOffset) * Game.SCALE + offsetX  + spriteX * Game.SCALE), (int) ((hitbox.y - yDrawOffset) * Game.SCALE + offsetY), (int) -(spriteX * Game.SCALE), (int) (spriteY * Game.SCALE), null);
            }
            if(Constants.DEBUG){
                drawHitbox(g, offsetX, offsetY);
            }
        }
    }
    
    /** load the animations atlas, subdivide it to single animation frames
     * @param path local path to animations atlas
     */
    protected void LoadAnimations(String path) {
        BufferedImage img = LoadSave.GetSpriteAtlas(path);
        
        animations = new BufferedImage[enemyState.values().length][];
        for (int j = 0; j < animations.length; j++) {
            animations[j] = new BufferedImage[Constants.EnemyConstants.GetSpriteAmount(ATLAS_TYPE, enemyState.values()[j])];
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * spriteX, j * spriteY, spriteX, spriteY);
            }
        }
    }
    
    /** update enemy position
    * 
    * moves the hitbox of the enemy, based on the movements variables
    * checks vertical moements, then horizontal movement, if a movement is blocked,
    * the enemy is placed right beside the wall, roof/ceiling.
    * 
    * calls prePosUpdate(), onRoofCealingTouch(), onWallTouch() events
    * 
    * calls updateXpos() to handle horizontal movements
    */
    protected void updatePos() {
        prePosUpdate();
        /**/
        moving = false;
        if(jump){
            jump();
        }
        if (!left && !right && !inAir) {
            return;
        }
        float xSpeed = 0, ySpeed = 0;

        if (left) {
            dirLeft = true;
            xSpeed -=movSpeed;
        }
        if (right) {
            dirLeft = false;
            xSpeed += movSpeed;
        }
        
        if(!inAir){
            if(!IsEntityOnFloor(hitbox, lvlData)){
                inAir=true;
            }
        }
        
        if(inAir){
            if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height,lvlData)){
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXpos(xSpeed);
            }else{
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if(airSpeed > 0){
                    resetInAir();
                }else{
                    onRoofCealingTouch();
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXpos(xSpeed);
                
            }
        }else{
            updateXpos(xSpeed);
        }
        moving = true;
    }
    
    /** update horizontal enemy position
    * 
    * moves the hitbox of the enemy, based on the movements variables
    * checks  horizontal movement, if a movement is blocked,
    * the enemy is placed right beside the wall.
    * 
    * calls onWallTouch() events
    * 
    * @param xSpeed horizontal speed.
    */
    private void updateXpos(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        }else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
            onWallTouch();
        }
    }
    
    /** jump command
     * if not inAir, airSpeed is set to JumpSpeed, and inAir is set to true
     */
    protected void jump() {
        if(inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }
    
    /** reset commands
     * reset commands variable
     */
    protected void resetMovements(){
        left=false;
        right=false;
        jump=false;
    }
    
    /** reset air speed
     * reset inAir to false
     */
    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }
    
     /** load level data
      * needs to be called after object reations, needed to update
     * @param data the level data
     */
    public void loadLvlData(int[][] data) {
        this.lvlData = data;
    }
   
    /** load player reference
     * @param p player reference*/
    public void loadPlayer(Player p){
        this.p = p;
    }
    
    /** change enemy coordinates
     * needs to be called after object reations, needed to update
     * @param x new X coordinate of enemy
     * @param y new Y coordinate of enemy
     */
    public void teleport(float x, float y){
        hitbox.x = x;
        hitbox.y = y;
        inAir=true;
    }
    
    /** increase animation tick,
     *  every aniSpeed updates, animation frame is changed
     */
    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;//= (aniIndex + 1) % GetSpriteAmount(playerAction);
            if (aniIndex >= GetSpriteAmount(ATLAS_TYPE, action)) {
                aniIndex = 0;
                attacking = false;
            }
        }
    }
    
    /** event when a movement touches a wall,
     * should be overridden from a subclass to implement event
     */
    protected void onWallTouch(){
        
    }
    
    /** event when a movement touches a roof/ceiling,
     * should be overridden from a subclass to implement event
     */
    protected void onRoofCealingTouch(){
        
    }
    
    /** event called every time before position update,
     * should be overridden from a subclass to implement event
     */
    protected void prePosUpdate() {
        
    }
    
    /** Start updater thread */
    public void StartUpdates(){
        updater.startThread();
    }
    /** Stops updater thread */
    public void StopUpdates(){
        updater.stopThread();
    }
    
    /** Override die of Entity */
    @Override
    public void die(){
        System.out.println("EnemyDeath (" + id+ ")");
        updater.stopThread();
        Game.playing.enemies.removeEnemy(this);
    }
    
    /** get lives count
     * @return lives count*/
    public int getLives() {
        return lives;
    }
    /** reset lives to default */
    public void resetLives() {
        lives=MAX_LIVES;
    }
    /** hit function, dies if 0 */
    public void hit(){
        if(--lives<=0){
            die();
        }
        AudioPlayer.playEffect(AudioPlayer.Effects.ENEMY_DEAD);
    }
    
    /**
     * Get a Enemy representation of the enemy
     * @return a JSON object representing this
     */
    public JSONObject toJSONObject(){
        return new JSONObject()
                .put("spawnX", x)
                .put("spawnY", y)
                .put("left", left)
                .put("right",right)
                .put("jump",jump)
                .put("position", Utils.jsonMapper.pointToJSON(getPosition()))
                .put("lives", lives)
                .put("dirLeft", dirLeft)
                .put("moving", moving)
                .put("attacking", attacking)
                .put("airSpeed", airSpeed)
                .put("id", id)
                .put("type", Constants.EnemyConstants.enemyType.getFromClass(this).ordinal());
    }
    
    /**
     * Get a reduced representation of the enemy
     * should be used to send death (0 lives) updates, and in client
     * @return a reduced JSON object representing this
     */
    public JSONObject JSONLives(){
        return new JSONObject()
            .put("lives", lives)
            .put("id", id);
    }
    
    
    /**
     * Update the current enemy with the JSON data
     * @param obj the JSON object to be used to update status
     */
    public void updateWithJson(JSONObject obj){
        x = obj.getFloat("spawnX");
        y = obj.getFloat("spawnY");
        left = obj.getBoolean("left");
        right = obj.getBoolean("right");
        jump = obj.getBoolean("jump");
        setPosition(Utils.jsonMapper.JSONTOPoint(obj.getJSONArray("position")));
        lives = obj.getInt("lives");
        dirLeft = obj.getBoolean("dirLeft");
        moving = obj.getBoolean("moving");
        attacking = obj.getBoolean("attacking");
        airSpeed = obj.getFloat("airSpeed");
        id = obj.getInt("id");
    }
    
    /**
     * Create a enemy with a JSON representation
     * @param obj the JSON object used to create the enemy
     * @return the newly created enemy
     */
    public static Enemy fromJSON(JSONObject obj){
        Enemy created;
        int type = obj.getInt("type");
        if(type == PASSIVE_ENEMY.ordinal()){
            created = new PassiveEnemy(obj.getFloat("spawnX"), obj.getFloat("spawnY"),  type);
        }else if(type == FOLLOW_ENEMY.ordinal()){
            created = new FollowEnemy(obj.getFloat("spawnX"), obj.getFloat("spawnY"),  type);
        }else if(type == SNIPER.ordinal()){
            created = new Sniper(obj.getFloat("spawnX"), obj.getFloat("spawnY"),  type);
        }else if(type == BOSS.ordinal()){
            created = new Boss(obj.getFloat("spawnX"), obj.getFloat("spawnY"),  type);
        }else{
            return null;
        }
        created.id = obj.getInt("id");
        created.left = obj.getBoolean("left");
        created.right = obj.getBoolean("right");
        created.jump = obj.getBoolean("jump");
        created.setPosition(Utils.jsonMapper.JSONTOPoint(obj.getJSONArray("position")));
        created.lives = obj.getInt("lives");
        created.dirLeft = obj.getBoolean("dirLeft");
        created.moving = obj.getBoolean("moving");
        created.attacking = obj.getBoolean("attacking");
        created.airSpeed = obj.getFloat("airSpeed");
        return created;
    }
    
    /**
     * reset the position to the spawn/default
     */
    private void resetPosition() {
        hitbox.x = x;
        hitbox.y = y;
    }

    /**
     * Reset the enemy to the default status
     */
    void reset() {
        resetInAir();
        inAir = true;
        resetPosition();
        resetMovements();
        resetLives();
    }

    /**
     * Get the max lives of enemy
     * @return 
     */
    public int getMAX_LIVES() {
        return MAX_LIVES;
    }

    /**
     * Set the lives of this enemy
     * @param lives new lives value
     */
    public void setLives(int lives) {
        this.lives = lives;
    }
}
