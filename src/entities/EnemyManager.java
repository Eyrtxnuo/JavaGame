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
    private LinkedList<Enemy> defaultEnemies;
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
        stopAllThreads();
        for(Enemy el: enemies){
            el.loadPlayer(playing.getPlayer());
        }
        this.enemies = enemies;
        this.defaultEnemies = (LinkedList<Enemy>) enemies.clone();
        
         
    }

    /** Get current {@link entities.Enemy Enemy} list
     *@return the current {@link entities.Enemy Enemy} list
     */
    public LinkedList<Enemy> getEnemies() {
        return enemies;
    }

    /** Get level {@link entities.Enemy Enemy} list
     *@return the level {@link entities.Enemy Enemy} list
     */
    public LinkedList<Enemy> getDefaultEnemies() {
        return defaultEnemies;
    }
    
    /** Get the dead enemies {@link entities.Enemy Enemy} list
     *@return the list of level {@link entities.Enemy enemies} that are not currently alive 
     */
    public LinkedList<Enemy> getDeadEnemies() {
        LinkedList<Enemy> difference = ((LinkedList<Enemy>) defaultEnemies.clone());
        difference.removeAll(enemies);
        return difference;
    }
    
    /**Remove a {@link entities.Enemy Enemy} from the list
     * @param enemy the enemy to be removed
     * @return true if the enemy has been removed, false if it wasn't contained
     */
    public boolean removeEnemy(Enemy enemy){
        enemy.updater.stopThread();
        return enemies.remove(enemy);
    }

    /** *  Get the {@link gamestates.Playing Playing} reference
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
    
    /** start all enemy threads */
    public void startAllThreads(){
        LinkedList<Enemy> enem = (LinkedList <Enemy>) enemies.clone();
        for(Enemy el: enem){
            el.StartUpdates();
        }
    } 
    /** stop all enemy threads */
    public void stopAllThreads(){
        LinkedList<Enemy> enem = (LinkedList <Enemy>) enemies.clone();
        for(Enemy el: enem){
           el.StopUpdates();
        }
    }

    /** stops all threads and clear enemies */
    public void removeAllEnemies() {
        stopAllThreads();
        enemies.clear();
    }

    
    /**
     * Reload default and resets enemies
     */
    public void reset() {
        removeAllEnemies();
        defaultEnemies.forEach((en)-> {
            en.reset();
        });
        enemies.addAll(defaultEnemies);
    }
    
    /**
     * Get the enemy with the specified id if present, else return null
     * @param id the id of the enemy to be searched
     * @return the enemy found with this id, or null if not found
     */
    public Enemy getFromId(int id){
        for (Enemy enemy : enemies) {
            if(enemy.id == id){
                return enemy;
            } 
        }
        return null;
    }
}