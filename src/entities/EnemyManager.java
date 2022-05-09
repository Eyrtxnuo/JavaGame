/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import gamestates.Playing;
import java.util.LinkedList;

/**
 *
 * @author matti
 */
public class EnemyManager {
    private LinkedList<Enemy> enemies;
    private Playing playing;

    public EnemyManager(Playing playing) {
        this.enemies = new LinkedList<>();
        this.playing = playing;
    }
    
    public void loadEnemies(LinkedList<Enemy> enemies){
        this.enemies = enemies;
    }

    public LinkedList<Enemy> getEnemies() {
        return enemies;
    }
    
    public boolean removeEnemy(Enemy e){
        return enemies.remove(e);
    }

    public Playing getPlaying() {
        return playing;
    }
    
    public synchronized void updateAll(Player player){
        LinkedList<Enemy> enem = (LinkedList <Enemy>) enemies.clone();
        for(Enemy el: enem){
            el.update(player);
        }
    }    
    
    
}
