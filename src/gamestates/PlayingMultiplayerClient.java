
package gamestates;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Game;
import net.ClientNetInterpreter;

/**
 *
 * @author matti
 */
public class PlayingMultiplayerClient extends Playing{

    ClientNetInterpreter interpreter;
    int updateCounter = 0;
    
    public PlayingMultiplayerClient(Game game) {
        super(game);
        interpreter = new ClientNetInterpreter();
        try {
            interpreter.connection("127.0.0.1", 45670, "Test1");
        } catch (SocketException | UnknownHostException ex) {
            pauseGame();
            Gamestate.state = Gamestate.MENU;
            Logger.getLogger(PlayingMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SocketTimeoutException ex) {
            System.out.println("ConnectionTimeout!");
            pauseGame();
            Gamestate.state = Gamestate.MENU;
            Logger.getLogger(PlayingMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update() {
        if(updateCounter++>10){
            interpreter.requestUpdate();
            updateCounter = 0;
        }
        super.update();
    }
     
    
    
}
