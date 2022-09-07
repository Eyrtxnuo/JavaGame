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
    SERVER server;

    public ServerNetInterpreter(ServerNetInterface netInterface) {
        this.netInterface = netInterface;
        server = new SERVER(this);
        server.start();
    }
    
    public byte[] packetInterpreter(byte[] packet){
        JSONObject obj = new JSONObject(new String(packet));
        int type = obj.getInt("type");
        UUID uuid = UUID.fromString(obj.getString("uuid"));
        JSONObject data = obj.getJSONObject("data");
        JSONObject responseData;
        switch(type){
            case 1 -> {//Connection
                responseData = netInterface.playerConnection(uuid, data);
            }
            case 2 -> {//GameStart
                responseData = netInterface.gameStartRequest(uuid, data);
            }
            case 3 -> {//Update Tick
                responseData = netInterface.updateResponse(uuid, data);
            }
            case 4 ->{//Disconnection
                responseData = netInterface.playerDisconnection(uuid, data);
            }
            case 5 ->{//Map Data request
                responseData = netInterface.mapLoad(uuid, data);
            }
            default ->{
                return new byte[1500];
            }
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put("type", 0);
        responseJson.put("uuid", uuid.toString());
        responseJson.put("data", responseData);
        
        //System.out.println(responseJson);
        
        return responseJson.toString().getBytes();
    }
          
    
    public void stop(){
        if(server==null)return;
        server.close();
    }
    
    
    
}
