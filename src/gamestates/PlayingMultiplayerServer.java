package gamestates;

import entities.Enemy;
import entities.Player;
import entities.Projectile;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Game;
import net.SERVER;
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
    LinkedList<UUID> disconnectedPlayers;
    LinkedList<Projectile> clientsProjectiles;

    ServerNetInterpreter server;

    public PlayingMultiplayerServer() {
        super();
        doPauseBlock = false;
        connectedPlayers = new HashMap<>();
        disconnectedPlayers = new LinkedList<>();
        clientsProjectiles = new LinkedList<>();
        //connPlayers.put(UUID.fromString("cbcf1463-acce-47e4-bb91-3e3cffe6a971"), new Player(20, 50));
        server = new ServerNetInterpreter(this);
    }

    @Override
    public JSONObject playerConnection(UUID Puuid, JSONObject data) {
        while (connectedPlayers.containsKey(Puuid) || Puuid.equals(UUID.fromString("00000000-0000-0000-0000-000000000001"))) {
            Puuid = UUID.randomUUID();
        }
        
        String username = (String) data.get("username");

        if (connectedPlayers.containsValue(Triplet.of(null, username, null).getEqualSecond())){
            int count = 0;
            String newName;
            do {
                newName = username + "(" + count++ + ")";
            } while ((connectedPlayers.containsValue(Triplet.of(null, newName, null).getEqualSecond())));
            username = newName;
        }
        Player pl = new Player(player.getSpawnPosition());
        pl.setUsername(username);
        pl.loadLvlData(levelManager.getLoadedLevel().getLvlData());
        connectedPlayers.put(Puuid, Triplet.of(pl, username, true));
        disconnectedPlayers.remove(Puuid);
        discord.DiscordActivityManager.setPlayingMultiplayerServerActivity();
        return new JSONObject().put("uuid", Puuid).put("username", username);
    }

    @Override
    public JSONObject updateResponse(UUID Puuid, JSONObject data) {
        //Elaborate recived data
        JSONObject RecPlayer = data.getJSONObject("player");
        connectedPlayers.get(Puuid).getFirst().updateWithJson(RecPlayer);
        connectedPlayers.get(Puuid).setThird(connectedPlayers.get(Puuid).getFirst().getLives()>0);
        JSONArray upEnemies = data.getJSONArray("enemies");
        upEnemies.forEach((t) -> {
            JSONObject en = (JSONObject)t;
            Enemy sel = enemies.getFromId(en.getInt("id"));
            if(sel==null)return;
            if(sel.getLives()>en.getInt("lives")){
                sel.setLives(en.getInt("lives"));
            }
            if(sel.getLives()<=0){
                sel.die();
            }
        });
        JSONArray upProj = data.getJSONArray("projectiles");
        clientsProjectiles.clear();
        upProj.forEach((t)->{
            var pr = Projectile.fromJSON((JSONObject) t);
            pr.loadLvlData(Game.playing.levelManager.getLoadedLevel().getLvlData());
            clientsProjectiles.add(pr);
        });
        //Package current data in JSON
        return currentDataResponse(Puuid);
    }

      private JSONObject currentDataResponse(UUID filter) {
        JSONArray players = new JSONArray();
        players.put(new JSONObject().put("uuid", "00000000-0000-0000-0000-000000000001").put("info",player.toJSONObject()));
        connectedPlayers.forEach((var id, var tr) -> {
            if(id.equals(filter))return;
            players.put(new JSONObject().put("uuid",id).put("info", tr.getFirst().toJSONObject()));
        });
        disconnectedPlayers.forEach((var id)->{
            players.put(new JSONObject().put("uuid",id));
        });
        JSONObject response = new JSONObject();
        response.put("players", players);
        
        JSONArray enemiesArr = new JSONArray();
        enemies.getEnemies().forEach((enemy)->{
            enemiesArr.put(enemy.toJSONObject());
        });
        enemies.getDeadEnemies().forEach((enemy)->{
            enemiesArr.put(new JSONObject().put("id", enemy.id).put("lives", enemy.getLives()));
        });
        response.put("enemies", enemiesArr);
        
        JSONArray projectiles = new JSONArray();
        Game.playing.flyingAmmos.getProjectiles().forEach((en) -> {
            projectiles.put(en.toJSONObject());
        });
        response.put("projectiles", projectiles);
        response.put("levelNumber", currentLevel);
        return response;
    }
    
    @Override
    public JSONObject playerDisconnection(UUID Puuid, JSONObject data) {
        if (!connectedPlayers.containsKey(Puuid)) {
            return null;
        }
        connectedPlayers.remove(Puuid);
        disconnectedPlayers.add(Puuid);
        discord.DiscordActivityManager.setPlayingMultiplayerServerActivity();
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
    public void otherProjectileDraw(Graphics g) {
        clientsProjectiles.forEach((var pr) -> {
            pr.render(g, effXOffset, 0);
        });
    }

    @Override
    public void update() {
        super.update();
        connectedPlayers.forEach((var id, var tr) -> {
            if(tr.getThird())
                tr.getFirst().update();
        });
        clientsProjectiles.forEach((Projectile proj)->{
           proj.update();
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
 
    @Override
    public JSONObject mapLoad(UUID Puuid, JSONObject data) {
        JSONObject response = currentDataResponse(Puuid);
        JSONObject level = new JSONObject();
        
        level.put("levelN", getCurrentLevel());
        int[][] dataArr = levelManager.getLoadedLevel().getLvlData();
        
        level.put("mapData", new JSONArray(dataArr));
        
        response.put("level", level);
        return response;
    }
    
    @Override
    public void draw(Graphics g) {
        super.draw(g); 
        String message = "SERVER (0.0.0.0:" + SERVER.SRVport+")";
        g.drawString(message, (int) ((Game.GAME_WIDTH-g.getFontMetrics().stringWidth(message))/2), (int) (15 * Game.SCALE));
    }
    
    @Override
    public Player getNearestPlayer(Point.Float point) {
        Player nPl = player;
        double distance = point.distance(nPl.getCenterPoint());
        for(var tripl : connectedPlayers.values()){
            if(!tripl.getThird())continue;
            double connDist = point.distance(tripl.getFirst().getCenterPoint());
            if(connDist<distance){
                nPl = tripl.getFirst();
                distance = connDist;
            }
        }
        return nPl;
    }
    
    
    public void stopServer(){
        server.stop();
    }

    public int getConnectedPlayersNumber() {
        return connectedPlayers.size();
    }
    
    @Override
    protected void discordUpdate(){
        discord.DiscordActivityManager.setPlayingMultiplayerServerActivity();
    }
}
