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
 *
 * @author matti
 */
public class Level {
    
    private int[][] lvlData;
    private EnemyManager enemies;
    private ProjectileManager proj;
    private Player player; 
    private BufferedImage background;

    public Level(int[][] lvlData, EnemyManager enemies, ProjectileManager proj, Player player) {
        this.lvlData = lvlData;
        this.enemies = enemies;
        this.proj = proj;
        this.player = player;
        mapInClasses();
    }
    
    private void mapInClasses(){
        player.loadLvlData(getLvlData());
    }

    public Level(int[][] lvlData, EnemyManager enemies, ProjectileManager proj, Player player, BufferedImage background) {
        this(lvlData, enemies, proj, player);
        this.background = background;
    }

    
    
    public void update() {
        enemies.startAllThreads();
        player.update();
        flyingAmmos.updateAll(player);
    }

    
    public EnemyManager getEnemies() {
        return enemies;
    }

    public ProjectileManager getProj() {
        return proj;
    }

    public Player getPlayer() {
        return player;
    }
    
    public int getHeightInTiles() {
        return lvlData.length;
    }

    public int getWidthInTiles() {
        return (lvlData.length>0)?lvlData[0].length:0;
    }
    
    public int getSpriteIndex(int x, int y){
        return lvlData[y][x];
    }

    public int[][] getLvlData() {
        return lvlData;
    }

    public BufferedImage getBackground() {
        return background;
    }
    
    public int getLenght(){
        return getWidthInTiles()*Game.TILES_DEFAULT_SIZE;
    }
}
