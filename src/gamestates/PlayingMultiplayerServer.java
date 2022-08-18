
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

/**
 *  boh, non so che sto facendo, metto tutto quello che mi viene in mente.
 * @author matti
 */
public class PlayingMultiplayerServer extends Playing implements ServerNetInterface{
     final Object gameStartLock=new Object();
     
    Map<UUID,String> connectedPlayers;
    Map<UUID,Player> connPlayers;
    
    ServerNetInterpreter server;
    
    public PlayingMultiplayerServer(Game game) {
        super(game);
        connectedPlayers = new HashMap<>();
        connPlayers = new HashMap<>();
        //connPlayers.put(UUID.fromString("cbcf1463-acce-47e4-bb91-3e3cffe6a971"), new Player(20, 50));
        server = new ServerNetInterpreter(this);
    }
    
    @Override
    public JSONObject playerConnection(UUID Puuid, JSONObject data) {
        if(connectedPlayers.containsKey(Puuid))return null;
        
        String username = (String)data.get("username");

        if(connectedPlayers.containsValue(username)){
            int count = 0;
            String newName;
            do{
                newName = username + "("+ count++ +")";
            }while((connectedPlayers.containsValue(newName)));
            username = newName;
        }
        connectedPlayers.put(Puuid, username);
        Player pl = new Player(player.getSpawnPosition());
        pl.setUsername(username);
        pl.loadLvlData(levelManager.getLoadedLevel().getLvlData());
        connPlayers.put(Puuid, pl);
        return new JSONObject().put("uuid", Puuid).put("username", username);
    }
    
    @Override
    public JSONObject updateResponse(UUID Puuid, JSONObject data) {
        //Elaborate recived data
        JSONObject RecPlayer = data.getJSONObject("player");
        connPlayers.get(Puuid).updateWithJson(RecPlayer);
        //Package current data in JSON
        
        JSONArray players = new JSONArray();
        players.put(player.toJSONObject());
        connPlayers.forEach((UUID id, Player p) -> {
            players.put(p.toJSONObject());
        });
        JSONObject response = new  JSONObject();
        response.put("players", players);
        return response;
    }

    @Override
    public JSONObject playerDisconnection(UUID Puuid, JSONObject data) {
        if(!connectedPlayers.containsKey(Puuid))return null;
        
        connectedPlayers.remove(Puuid);
        connPlayers.remove(Puuid);
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
    
    public void StartGame(){
        synchronized (gameStartLock) {
            gameStartLock.notifyAll();
        }
    }

    @Override
    protected void otherPlayerDraw(Graphics g) {
        connPlayers.forEach((var uuid, var p)->{
           p.render(g, effXOffset, 0);
        });
    }

    @Override
    public void update() {
        super.update();
        connPlayers.forEach((var id, var p) -> {
            p.update();
        });
    }
    
    
    
    
}
