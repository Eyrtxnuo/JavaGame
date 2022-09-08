package net;

import entities.Enemy;
import entities.EnemyManager;
import gamestates.Gamestate;
import gamestates.PlayingMultiplayerClient;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Game;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Utils;

/**
 *
 * @author matti
 */
public class ClientNetInterpreter {

    CLIENT client;
    int notResp = 0;

    public ClientNetInterpreter() {
    }

    public JSONObject requestUpdate() {
        JSONObject packet = new JSONObject();
        packet.put("type", 3);
        packet.put("uuid", ((PlayingMultiplayerClient) Game.playing).myUUID.toString());
        JSONArray enemies = new JSONArray();
        Game.playing.enemies.getEnemies().forEach((en) -> {
            if(en.getLives()<en.getMAX_LIVES()){
                enemies.put(en.JSONLives());
            }
        });
        Game.playing.enemies.getDeadEnemies().forEach((en) -> {
            enemies.put(en.JSONLives());
        });
        JSONArray projectiles = new JSONArray();
        Game.playing.flyingAmmos.getProjectiles().forEach((en) -> {
            projectiles.put(en.toJSONObject());
        });
        packet.put("data", new JSONObject()
            .put("player", Game.playing.player.toJSONObject())
            .put("enemies", enemies)
            .put("projectiles", projectiles)
        );
        
        try {
            JSONObject resp = new JSONObject(new String(client.transmit(packet.toString().getBytes(), 100))).getJSONObject("data");
            notResp=0;
            return resp;
        } catch (SocketTimeoutException ex) {
            notResp++;
            if(notResp>30){
                System.err.println("Server is not responding since 30 ticks, disconnecting");
                new Thread(()->{disconnection();}).start();
                Gamestate.state = Gamestate.MENU;
                discord.DiscordActivityManager.setMenuActivity();
            }
            return new JSONObject().put("players", new JSONArray());
        }

    }

    public UUID connection(String ip, int port, String Username) throws SocketException, UnknownHostException, SocketTimeoutException {
        return connection(ip, port, UUID.fromString("00000000-0000-0000-0000-000000000000"), Username);
    }

    public UUID connection(String ip, int port, UUID uuid, String Username) throws SocketException, UnknownHostException, SocketTimeoutException {
        client = new CLIENT(ip, port);
        JSONObject packet = new JSONObject();
        packet.put("type", 1);
        packet.put("uuid", uuid);
        JSONObject data = new JSONObject();
        data.put("username", Username);
        packet.put("data", data);

        JSONObject response;

        String resStr = new String(client.transmit(packet.toString().getBytes(), 10000));
        try {
            response = new JSONObject(resStr);
        } catch (JSONException jexc) {
            Logger.getLogger(ClientNetInterpreter.class.getName()).log(Level.SEVERE, "msg(" + Username + "->:" + resStr + ".", jexc);
            return null;
        }

        //System.out.print(response);
        UUID recivedID;
        try {
            recivedID = UUID.fromString((String) response.get("uuid"));
        } catch (IllegalArgumentException ex) {
            System.err.println("Illegal UUID recived! Disconnectiong...");
            disconnection();
            return null;
        }
        return recivedID;
    }

    public void disconnection() {
        if(client==null)return;
        JSONObject packet = new JSONObject();
        packet.put("type", 4);
        packet.put("uuid", ((PlayingMultiplayerClient) Game.playing).myUUID.toString());
        packet.put("data", new JSONObject());
        try {
            client.transmit(packet.toString().getBytes(), 5000);
        } catch (SocketTimeoutException ex) {
            System.err.println("Server did not respond, force disconnection");
        }
        ((PlayingMultiplayerClient) Game.playing).myUUID = null;
    }

    public void requestMapData(UUID myUuid) {
        JSONObject packet = new JSONObject();
        packet.put("type", 5);
        packet.put("uuid", myUuid.toString());
        packet.put("data", new JSONObject());
        JSONObject response;

        String resStr = new String(client.transmit(packet.toString().getBytes()));
        try {
            response = new JSONObject(resStr);
        } catch (JSONException jexc) {
            Logger.getLogger(ClientNetInterpreter.class.getName()).log(Level.SEVERE, jexc.getMessage());
            return;
        }
        JSONObject respData =  response.getJSONObject("data");
        JSONArray enemies = respData.getJSONArray("enemies");
        int[][] mapData = Utils.jsonMapper.JSONToLevelData(respData.getJSONObject("level").getJSONArray("mapData"));
        LinkedList<Enemy> eList = new LinkedList<>();
        enemies.forEach((enemy) -> {
            JSONObject enObj = (JSONObject) enemy;
            if(enObj.getInt("lives")<=0){
                return;
            }
            Enemy enTMP = Enemy.fromJSON(enObj);
            enTMP.loadLvlData(mapData);
            eList.add(enTMP);
        });
        EnemyManager em = new EnemyManager(Game.playing);
        em.loadEnemies(eList);
        
        Game.playing.loadCustomLevel(mapData, respData.getJSONObject("level").getInt("levelN"), em);
        Game.playing.connectLevel();
    }

    public CLIENT getClient() {
        return client;
    }
    
    
}
