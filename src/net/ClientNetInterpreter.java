/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author matti
 */
public class ClientNetInterpreter {
    CLIENT client;

    public ClientNetInterpreter() {
    }
    
    
    public JSONObject requestUpdate(){
        JSONObject packet = new JSONObject();
        packet.put("type",3);
        packet.put("uuid", connectionStatus.currentUUID.toString());
        try {
            return new JSONObject(new String(client.transmit(packet.toString().getBytes(), 100)));
        } catch (SocketTimeoutException ex) {
            return null;
        }
        
    }
    
    public void connection(String ip, int port, String Username) throws SocketException, UnknownHostException{
        connection(ip,  port,  UUID.fromString("00000000-0000-0000-0000-000000000000"),  Username);
    }
    
    public boolean connection(String ip, int port, UUID uuid, String Username) throws SocketException, UnknownHostException{
        client = new CLIENT(ip, port);
        JSONObject packet = new JSONObject();
        packet.put("type",1);
        packet.put("uuid", uuid);
        JSONObject data = new JSONObject();
        data.put("username", Username);
        packet.put("data", data);
        
        JSONObject response;
        
        try {
            String resStr = new String(client.transmit(packet.toString().getBytes(),10000));
            try{
                response = new JSONObject(resStr);
            }catch(JSONException jexc){
                Logger.getLogger(ClientNetInterpreter.class.getName()).log(Level.SEVERE, "msg("+Username+"->:" +resStr+".", jexc);
                return false;
            }
        } catch (SocketTimeoutException ex) {
            System.out.println("Packet Lost");
            return false;
        }
        //System.out.print(response);
        UUID recivedID;
        connectionStatus.currentUUID = uuid;
        try{
            recivedID = UUID.fromString((String) response.get("uuid"));
        }catch(IllegalArgumentException ex){
            System.out.println("Illegal UUID recived! Disconnectiong...");
            disconnection();
            return false;
        }
        connectionStatus.currentUUID = recivedID;
        connectionStatus.Username = Username;
        return true;
    }
    
    public void disconnection(){
        JSONObject packet = new JSONObject();
        packet.put("type",4);
        packet.put("uuid", connectionStatus.currentUUID.toString());
        client.transmit(packet.toString().getBytes());
        connectionStatus.reset();
    }
    
}
