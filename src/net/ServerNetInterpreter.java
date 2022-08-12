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
public class ServerNetInterpreter{
    
    ServerNetInterface netInterface;

    public ServerNetInterpreter(ServerNetInterface netInterface) {
        this.netInterface = netInterface;
    }
    
    protected byte[] packetInterpreter(byte[] packet){
        JSONObject obj = new JSONObject(new String(packet));
        int type = obj.getInt("type");
        UUID uuid = UUID.fromString(obj.getString("UUID"));
        JSONObject data = obj.getJSONObject("data");
        JSONObject responseData;
        switch(type){
            case 1 -> {
                responseData = netInterface.playerConnection(uuid, data);
            }
            case 2 -> {
                responseData = netInterface.gameStartRequest(uuid, data);
            }
            case 3 -> {
                responseData = netInterface.updateResponse(uuid, data);
            }
            case 4 ->{
                responseData = netInterface.playerDisconnection(uuid, data);
            }
            default ->{
                return new byte[1500];
            }
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put("type", 0);
        responseJson.put("uuid", uuid.toString());
        responseJson.put("data", responseData);
        
        System.out.println(responseJson);
        
        return responseJson.toString().getBytes();
    }
    
            

    
    
    
    
}
