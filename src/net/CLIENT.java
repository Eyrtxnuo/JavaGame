package net;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author matti
 */
public class CLIENT extends Thread{

    DatagramSocket socket = null;

    
    DatagramPacket packetIn = null;
    
    InetAddress address;
    int port;
    
    public CLIENT(String IPaddress, int port) throws SocketException, UnknownHostException{
        
            socket = new DatagramSocket();
            System.out.println("PORTA: "+socket.getLocalPort());
            address = InetAddress.getByName(IPaddress);
            this.port = port;
        
        
    }
 
    public byte[] transmit(byte[] data){
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        
         try {
             socket.send(packet);
             
             byte[] dataIn = new byte[1500];
             packetIn = new DatagramPacket(dataIn, dataIn.length);
             
             socket.receive(packetIn);
             
             dataIn = packetIn.getData();
             return dataIn;
         } catch (IOException ex) {
             Logger.getLogger(CLIENT.class.getName()).log(Level.SEVERE, null, ex);
         }
         return null;   
    }
}
