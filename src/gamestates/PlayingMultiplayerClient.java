package gamestates;

import entities.Enemy;
import entities.Player;
import entities.Projectile;
import java.awt.Graphics;
import java.awt.Point;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import main.Game;
import net.ClientNetInterpreter;
import org.json.JSONObject;
import utils.AudioPlayer;
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
    LinkedList<Projectile> serverProjectiles;

    /**
     * Multiplayer Client playing 
     * @param ip Server IP to connect to
     * @param port Server Port
     * @throws SocketException Thrown when there was an error on creating the connection
     * @throws UnknownHostException Trown when the IP argument is not valid, or is not convertible in a IPv4 address 
     * @throws SocketTimeoutException Thrown when the server did not respond in Timeout time
     */
    public PlayingMultiplayerClient(String ip, int port) throws SocketException, UnknownHostException, SocketTimeoutException {
        super();
        doPauseBlock = false;
        newLevelPermitted = false;
        connectedPlayers = new HashMap<>();
        serverProjectiles = new LinkedList();
        interpreter = new ClientNetInterpreter();
        //try {
            UUID idRec = interpreter.connection(ip, port,UUID.randomUUID(), "Test1");
            if(idRec != null){
                myUUID = idRec;
            }
        /*} catch (SocketException | UnknownHostException | SocketTimeoutException ex) {
            pauseGame();
            Gamestate.state = Gamestate.MENU;
            Logger.getLogger(PlayingMultiplayerClient.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

    @Override
    public void afterCreationInit() {
        interpreter.requestMapData(myUUID);
    }

    @Override
    public void update() {
        if (updateCounter++ > 5) {
            var update = interpreter.requestUpdate();
            //connectedPlayers.clear();
            update.getJSONArray("players").forEach((var PlaObj) -> {
                UUID uuid = UUID.fromString(((JSONObject) PlaObj).getString("uuid"));
                if(uuid.equals(myUUID))return;
                if(((JSONObject) PlaObj).isNull("info")){
                    connectedPlayers.remove(uuid);
                    discord.DiscordActivityManager.setPlayingMultiplayerClientActivity();
                    return;
                }
                Player pl;
                if (connectedPlayers.containsKey(uuid)) {
                    pl = connectedPlayers.get(uuid).getFirst();
                } else {
                    pl = new Player(0, 0);
                }
                pl.loadLvlData(Game.playing.levelManager.getLoadedLevel().getLvlData());
                pl.updateWithJson(((JSONObject) PlaObj).getJSONObject("info"));
                if(connectedPlayers.put(uuid, Triplet.of(pl, "next", true))==null){
                    discord.DiscordActivityManager.setPlayingMultiplayerClientActivity();
                }
                if(update.getInt("levelNumber")!=currentLevel){
                    interpreter.requestMapData(myUUID);
                }
            });
            if(update.has("enemies")){
                update.getJSONArray("enemies").forEach((var EneObj) -> {
                    JSONObject en = (JSONObject) EneObj;
                    int id = en.getInt("id");
                    Enemy eneID = enemies.getFromId(id);
                    if (eneID==null) {
                        return;
                    }
                    if(en.getInt("lives")==0){
                        enemies.removeEnemy(eneID);
                        return;
                    }
                    eneID.updateWithJson(en);
                });
            }
            if(update.has("projectiles")){
                serverProjectiles.clear();
                update.getJSONArray("projectiles").forEach((var ProjObj) -> {
                    var pr = Projectile.fromJSON((JSONObject) ProjObj);
                    pr.loadLvlData(Game.playing.levelManager.getLoadedLevel().getLvlData());
                    serverProjectiles.add(pr);
                });
            }
            updateCounter = 0;
        }
        super.update();
        connectedPlayers.forEach((var id, var tr) -> {
            if (tr.getThird()) {
                tr.getFirst().update();
            }
        });
        serverProjectiles.forEach((Projectile proj)->{
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
    protected void otherPlayerDraw(Graphics g) {
        connectedPlayers.forEach((var uuid, var tr) -> {
            tr.getFirst().render(g, effXOffset, 0);
        });
    }

    @Override
    public void otherProjectileDraw(Graphics g) {
        serverProjectiles.forEach((var pr) -> {
            pr.render(g, effXOffset, 0);
        });
    }
    
    @Override
    public void draw(Graphics g) {
        super.draw(g);
        String message = "CLIENT ("+ interpreter.getClient().getAddress()+":"+interpreter.getClient().getPort() +")";
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
    
    /**
     * Disconnect from the server, if connection exist
     */
    public void disconnect(){
        if(interpreter==null)return;
        interpreter.disconnection();
    }
    
    public int getConnectedPlayersNumber() {
        return connectedPlayers.size();
    }
    
    @Override
    protected void discordUpdate(){
        discord.DiscordActivityManager.setPlayingMultiplayerClientActivity();
    }
}
