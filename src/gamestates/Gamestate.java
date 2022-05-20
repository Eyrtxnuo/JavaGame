package gamestates;

/**Gamestates enum, contains various states of the game
 * 
 * @author minci
 */
public enum Gamestate {
    
    /**Different possible states*/
    PLAYING, MENU, OPTIONS, QUIT;
    /**State of the game initialised as menu*/
    public static Gamestate state = MENU;
    
}
