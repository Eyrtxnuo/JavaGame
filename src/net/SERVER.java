package net;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Utils;

/**
 *
 * @author matti
 */
public class SERVER extends Thread{
    static final int SRVport = 45670;
    
    @Override
    public void run(){
        // TODO code application logic here
        DatagramSocket socket = null;
        DatagramPacket packet = null;
        DatagramPacket packetOut = null;
        try {
            socket = new DatagramSocket(SRVport);
        }catch(SocketException ex){
            System.out.println("ERROR: " + ex.getMessage());
            return;
        }
        System.out.println("PORTA: "+socket.getLocalPort());
     
        
        while(true){
            InetAddress address;
            int port;
            byte[] dataIn = new byte[1500];//1500 = MTU
            packet = new DatagramPacket(dataIn, dataIn.length);
            packet.setData(dataIn);
            
            
            try {
                socket.receive(packet);
                
                address = packet.getAddress();
                port = packet.getPort();
                dataIn = packet.getData();
                
                byte[] byteOut = dataIn;
                packetOut = new DatagramPacket(byteOut , byteOut.length, address, port);
                
                socket.send(packetOut);
                //debug
                //*
                System.out.println("[" + Utils.getHour() +"] " +address + " -> this:45670  || this -> " +address + ":" + SRVport);
                //*/
                
            } catch (IOException ex) {
                Logger.getLogger(SERVER.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            
        }
        
    }
    
}
