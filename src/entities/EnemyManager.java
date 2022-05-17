package entities;

import gamestates.Playing;
import java.util.LinkedList;

/** Enemy manager class, contains a list of {@link entities.Enemy Enemy}, implements functions to create, remove, update enemies in a level
 * 
 * @author BossiMattia
 * @see Enemy
 */
public class EnemyManager {
    /** Enemy container */
    private LinkedList<Enemy> enemies;
    /** Playing object reference */
    private final Playing playing;

    
    /** Default constructor, 
     * create a empty {@link entities.Enemy Enemy} list
     * 
     * @param playing Playing object reference
     */
    public EnemyManager(Playing playing) {
        this.enemies = new LinkedList<>();
        this.playing = playing;
    }
    
    /** Load {@link entities.Enemy Enemy} list, replace the old one
     *@param enemies the new {@link entities.Enemy Enemy} list
     */
    public void loadEnemies(LinkedList<Enemy> enemies){
        for(Enemy el: enemies){
            el.loadPlayer(playing.getPlayer());
        }
        this.enemies = enemies;
    }

    /** Get current {@link entities.Enemy Enemy} list
     *@return the current {@link entities.Enemy Enemy} list
     */
    public LinkedList<Enemy> getEnemies() {
        return enemies;
    }
    
    /**Remove a {@link entities.Enemy Enemy} from the list
     * @param enemy the enemy to be removed
     * @return true if the enemy has been removed, false if it wasn't contained
     */
    public boolean removeEnemy(Enemy enemy){
        return enemies.remove(enemy);
    }

    /** Get the {@link gamestates.Playing Playing} reference
     * @return the loaded {@link gamestates.Playing Playing} reference
     */
    public Playing getPlaying() {
        return playing;
    }
    
    /** Calls Update on every {@link entities.Enemy Enemy} in the list
     */
    public synchronized void updateAll(){
        LinkedList<Enemy> enem = (LinkedList <Enemy>) enemies.clone();
        for(Enemy el: enem){
            el.update();
        }
    }
    
    public void startAllThreads(){
        LinkedList<Enemy> enem = (LinkedList <Enemy>) enemies.clone();
        for(Enemy el: enem){
            el.StartUpdates();
        }
    }
    
    public void stopAllThreads(){
        LinkedList<Enemy> enem = (LinkedList <Enemy>) enemies.clone();
        for(Enemy el: enem){
           el.StopUpdates();
        }
    }

    void removeAllEnemies() {
        stopAllThreads();
        enemies.clear();
    }
    
}