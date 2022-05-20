/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package levels;

import entities.EnemyManager;
import entities.Player;
import entities.ProjectileManager;
import static gamestates.Playing.flyingAmmos;
import java.awt.image.BufferedImage;
import main.Game;

/**
 * Level class, contais tiles map, enemies, player reference, and a background
 * @author matti
 */
public class Level {
        
    /** level tiles data */
    private int[][] lvlData;
    /** enemy manager object */
    private EnemyManager enemies;
    /** projectiles manager object */
    private ProjectileManager proj;
    /** player reference object */
    private Player player; 
    /** background image */
    private BufferedImage background;

    /** 
     * Constructor, level needs tiles data, enemymanager, projectilemanager, and player
     * @param lvlData tiles data
     * @param enemies enemymanager object
     * @param proj projectilemanager object
     * @param player player object
     */
    public Level(int[][] lvlData, EnemyManager enemies, ProjectileManager proj, Player player) {
        this.lvlData = lvlData;
        this.enemies = enemies;
        this.proj = proj;
        this.player = player;
        mapInClasses();
    }
    
    /**
     * Constructor, level needs tiles data, enemymanager, projectilemanager, player and background
     * 
     * @param lvlData tiles data
     * @param enemies enemymanager object
     * @param proj projectilemanager object
     * @param player player object
     * @param background background Image
     */
    public Level(int[][] lvlData, EnemyManager enemies, ProjectileManager proj, Player player, BufferedImage background) {
        this(lvlData, enemies, proj, player);
        this.background = background;
    }

    /** loads tiles map in player object */
    private void mapInClasses(){
        player.loadLvlData(getLvlData());
    }
    
    /** update, calls update on player and enemies, starts enemy threads if they are not started */
    public void update() {
        enemies.startAllThreads();
        player.update();
        flyingAmmos.updateAll(player);
    }

    /* get enemy menager object reference */ 
    public EnemyManager getEnemies() {
        return enemies;
    }
    
    /* get projectile menager object reference */
    public ProjectileManager getProj() {
        return proj;
    }
    
    /* get player object reference */
    public Player getPlayer() {
        return player;
    }
    
     /* get level height in tiles */
    public int getHeightInTiles() {
        return lvlData.length;
    }
    
    /* get level width in tiles */
    public int getWidthInTiles() {
        return (lvlData.length>0)?lvlData[0].length:0;
    }
    
    /** get tiles of level on requested coordinates(in tiles)
     * @param x tile x coord
     * @param y tile y coord
     * @return tile type */
    public int getTilesIndex(int x, int y){
        return lvlData[y][x];
    }

    /** get loaded tiles level data
     * @return tiles level map */
    public int[][] getLvlData() {
        return lvlData;
    }

    /**
     * get level background
     * @return Background Image
     */
    public BufferedImage getBackground() {
        return background;
    }
    
    /**
     * get level width in coordinates
     * @return game lenght in coordinates
     */
    public int getLenght(){
        return getWidthInTiles()*Game.TILES_DEFAULT_SIZE;
    }
}
