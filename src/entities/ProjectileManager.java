/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import gamestates.Playing;
import static gamestates.Playing.flyingAmmos;
import java.util.LinkedList;

/**
 *
 * @author matti
 */
public class ProjectileManager {
    private LinkedList<Projectile> projectiles;
    private Playing playing;

    public ProjectileManager(Playing playing) {
        this.projectiles = new LinkedList<>();
        this.playing = playing;
    }
    
    public void loadProjectiles(LinkedList<Projectile> projectile){
        this.projectiles = projectile;
    }

    public LinkedList<Projectile> getProjectiles() {
        return projectiles;
    }
    
    public synchronized boolean removeProjectile(Projectile e){
        return projectiles.remove(e);
    }

    public Playing getPlaying() {
        return playing;
    }
    
    public synchronized void add(Projectile proj){
        projectiles.add(proj);
    }
    
    public synchronized void updateAll(Player player){
        LinkedList<Projectile> projects = (LinkedList < Projectile >) projectiles.clone();
        for(Projectile el: projects){
            el.update(player);
        }
    }
}
