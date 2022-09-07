/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net;

import java.util.UUID;
import org.json.JSONObject;
/**
 *
 * @author matti
 */
public interface ServerNetInterface {
    public JSONObject updateResponse(UUID Puuid, JSONObject da);
    
    public JSONObject playerConnection(UUID Puuid, JSONObject data);
    
    public JSONObject mapLoad(UUID Puuid, JSONObject data);

    public JSONObject playerDisconnection(UUID Puuid, JSONObject data);

    public JSONObject gameStartRequest(UUID Puuid,JSONObject data);
    
    public JSONObject genericRequest(JSONObject packet);
}
