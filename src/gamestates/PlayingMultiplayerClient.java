package gamestates;

import entities.Player;
import java.awt.Graphics;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Game;
import net.ClientNetInterpreter;
import org.json.JSONObject;
import utils.Triplet;

/**
 *
 * @author matti
 */
public class PlayingMultiplayerClient extends Playing {

    ClientNetInterpreter interpreter;
    int updateCounter = 0;
    public UUID myUUID;

    Map<UUID, Triplet<Player, String, Boolean>> connectedPlayers;

    public PlayingMultiplayerClient(Game game, String ip, int port) throws SocketException, UnknownHostException, SocketTimeoutException {
        super(game);
        connectedPlayers = new HashMap<>();
        interpreter = new ClientNetInterpreter();
        //try {
            UUID idRec = interpreter.connection(ip, port,UUID.randomUUID(), "Test1");
            if(idRec != null){
                myUUID = idRec;
            }
        /*} catch (SocketException | UnknownHostException ex) {
            pauseGame();
            Gamestate.state = Gamestate.MENU;
            Logger.getLogger(PlayingMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SocketTimeoutException ex) {
            System.out.println("ConnectionTimeout!");
            pauseGame();
            Gamestate.state = Gamestate.MENU;
            Logger.getLogger(PlayingMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

    @Override
    public void update() {
        if (updateCounter++ > 5) {
            var update = interpreter.requestUpdate();
            //connectedPlayers.clear();
            update.getJSONArray("players").forEach((var PlaObj) -> {
                UUID uuid = UUID.fromString(((JSONObject) PlaObj).getString("uuid"));
                if(uuid.equals(myUUID))return;
                Player pl;
                if (connectedPlayers.containsKey(uuid)) {
                    pl = connectedPlayers.get(uuid).getFirst();
                } else {
                    pl = new Player(0, 0);
                }
                pl.loadLvlData(Game.playing.levelManager.getLoadedLevel().getLvlData());
                pl.updateWithJson(((JSONObject) PlaObj).getJSONObject("info"));
                connectedPlayers.put(uuid, Triplet.of(pl, "next", true));
            });
            updateCounter = 0;
        }
        super.update();
        connectedPlayers.forEach((var id, var tr) -> {
            if (tr.getThird()) {
                tr.getFirst().update();
            }
        });
    }

    @Override
    protected void otherPlayerDraw(Graphics g) {
        connectedPlayers.forEach((var uuid, var tr) -> {
            tr.getFirst().render(g, effXOffset, 0);
        });
    }

}
