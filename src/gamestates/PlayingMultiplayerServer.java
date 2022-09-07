package gamestates;

import entities.Player;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Game;
import net.ServerNetInterface;
import net.ServerNetInterpreter;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.AudioPlayer;
import utils.Triplet;

/**
 * boh, non so che sto facendo, metto tutto quello che mi viene in mente.
 *
 * @author matti
 */
public class PlayingMultiplayerServer extends Playing implements ServerNetInterface {

    final Object gameStartLock = new Object();

    Map<UUID, Triplet<Player, String, Boolean>> connectedPlayers;

    ServerNetInterpreter server;

    public PlayingMultiplayerServer(Game game) {
        super(game);
        connectedPlayers = new HashMap<>();
        //connPlayers.put(UUID.fromString("cbcf1463-acce-47e4-bb91-3e3cffe6a971"), new Player(20, 50));
        server = new ServerNetInterpreter(this);
    }

    @Override
    public JSONObject playerConnection(UUID Puuid, JSONObject data) {
        while (connectedPlayers.containsKey(Puuid) || Puuid.equals(UUID.fromString("00000000-0000-0000-0000-000000000001"))) {
            Puuid = UUID.randomUUID();
        }
        
        String username = (String) data.get("username");

        if (connectedPlayers.containsValue(username)) {
            int count = 0;
            String newName;
            do {
                newName = username + "(" + count++ + ")";
            } while ((connectedPlayers.containsValue(newName)));
            username = newName;
        }
        Player pl = new Player(player.getSpawnPosition());
        pl.setUsername(username);
        pl.loadLvlData(levelManager.getLoadedLevel().getLvlData());
        connectedPlayers.put(Puuid, Triplet.of(pl, username, true));
        return new JSONObject().put("uuid", Puuid).put("username", username);
    }

    @Override
    public JSONObject updateResponse(UUID Puuid, JSONObject data) {
        //Elaborate recived data
        JSONObject RecPlayer = data.getJSONObject("player");
        connectedPlayers.get(Puuid).getFirst().updateWithJson(RecPlayer);
        //Package current data in JSON
        
        JSONArray players = new JSONArray();
        players.put(new JSONObject().put("uuid", "00000000-0000-0000-0000-000000000001").put("info",player.toJSONObject()));
        connectedPlayers.forEach((var id, var tr) -> {
            if(id.equals(Puuid))return;
            players.put(new JSONObject().put("uuid",id).put("info", tr.getFirst().toJSONObject()));
        });
        JSONObject response = new JSONObject();
        response.put("players", players);
        return response;
    }

    @Override
    public JSONObject playerDisconnection(UUID Puuid, JSONObject data) {
        if (!connectedPlayers.containsKey(Puuid)) {
            return null;
        }

        connectedPlayers.remove(Puuid);
        return new JSONObject();
    }

    @Override
    public JSONObject gameStartRequest(UUID Puuid, JSONObject data) {
        synchronized (gameStartLock) {
            try {
                gameStartLock.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(PlayingMultiplayerServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return new JSONObject().put("map", currentLevel);

    }

    @Override
    public JSONObject genericRequest(JSONObject packet) {
        return null;
    }

    public void StartGame() {
        synchronized (gameStartLock) {
            gameStartLock.notifyAll();
        }
    }

    @Override
    protected void otherPlayerDraw(Graphics g) {
        connectedPlayers.forEach((var uuid, var tr) -> {
            tr.getFirst().render(g, effXOffset, 0);
        });
    }

    @Override
    public void update() {
        super.update();
        connectedPlayers.forEach((var id, var tr) -> {
            if(tr.getThird())
                tr.getFirst().update();
        });
    }

    @Override
    public void playerDeath(Player p) {
        if (p == player) {
            super.playerDeath(p);
        }
        
        for(var es : connectedPlayers.entrySet()){
            if(es.getValue().getFirst() ==  p){
                es.getValue().setThird(false);
                AudioPlayer.playEffect(AudioPlayer.Effects.DEATH);
                break;
            }
        }
            
    }

}
