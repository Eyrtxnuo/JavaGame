package net;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Utils;

/**
 *
 * @author matti
 */
public class CLIENT extends Thread{

    DatagramSocket socket = null;

    
    
    InetAddress address;
    int port;
    
    public CLIENT(String IPaddress, int port) throws SocketException, UnknownHostException{
        
            socket = new DatagramSocket();
            //System.out.println("PORTA: "+socket.getLocalPort());
            address = InetAddress.getByName(IPaddress);
            this.port = port;
        
        
    }
    
    public byte[] transmit(byte[] data){
        try {
            return transmit(data, 0);
        } catch (SocketTimeoutException ex) {
            return null;
        }
        
    }
 
    public byte[] transmit(byte[] data,int  timeout) throws SocketTimeoutException {
        try {
            socket.setSoTimeout(timeout);
        } catch (SocketException ex) {
            Logger.getLogger(CLIENT.class.getName()).log(Level.SEVERE, null, ex);
        }
        data = Utils.deflator.compress(data);
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        
         try {
             socket.send(packet);
             
             byte[] dataIn = new byte[1500];
             DatagramPacket packetIn = new DatagramPacket(dataIn, dataIn.length);
             
             socket.receive(packetIn);
             
             dataIn = Utils.deflator.decompress(packetIn.getData());
             
             return dataIn;
         }catch (SocketTimeoutException ex ){
             throw ex;
         }catch (IOException ex) {
             Logger.getLogger(CLIENT.class.getName()).log(Level.SEVERE, null, ex.getMessage());
         }
         return null;   
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
    
}
