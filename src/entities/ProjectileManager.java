
package entities;

import gamestates.Playing;
import java.util.LinkedList;

/**
 * Contains  enemy and player fired projectiles
 * @author matti
 */
public class ProjectileManager {

    private LinkedList<Projectile> projectiles;
    private LinkedList<Projectile> enemyProjectiles;
    private Playing playing;

    public ProjectileManager(Playing playing) {
        this.projectiles = new LinkedList<>();
        this.enemyProjectiles = new LinkedList<>();
        this.playing = playing;
    }

    public void loadProjectiles(LinkedList<Projectile> projectiles, LinkedList<Projectile> enemyProjectiles) {
        this.projectiles = projectiles;
        this.enemyProjectiles = enemyProjectiles;
    }

    public LinkedList<Projectile> getProjectiles() {
        return projectiles;
    }

    public LinkedList<Projectile> getEnemyProjectiles() {
        return enemyProjectiles;
    }

    public synchronized boolean removeProjectile(Projectile e) {
        return projectiles.remove(e)?true:enemyProjectiles.remove(e);
    }

    public Playing getPlaying() {
        return playing;
    }

    public synchronized void add(Projectile proj) {
        projectiles.add(proj);
    }

    public synchronized void enemyAdd(Projectile proj) {
        enemyProjectiles.add(proj);
    }

    public synchronized void updateAll() {
        LinkedList<Projectile> projects = (LinkedList< Projectile>) projectiles.clone();
        for (Projectile el : projects) {
            el.update();
        }
        projects = (LinkedList<Projectile>) enemyProjectiles.clone();
        for (Projectile el : projects) {
            el.update();
        }
    }
    
    public void clear(){
        projectiles.clear();
        enemyProjectiles.clear();
        
    }
}
