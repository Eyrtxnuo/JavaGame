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
    ServerNetInterpreter inerpreter; 

    public SERVER(ServerNetInterpreter inerpreter) {
        this.inerpreter = inerpreter;
    }
    
    @Override
    public void run(){
        // TODO code application logic here
        final DatagramSocket socket;
        DatagramPacket packet;
        try {
            socket = new DatagramSocket(SRVport);
        }catch(SocketException ex){
            System.out.println("ERROR: " + ex.getMessage());
            return;
        }
        System.out.println("PORTA: "+socket.getLocalPort());
     
       
        while(true){
            byte[] dataIn = new byte[1500];//1500 = MTU
            packet = new DatagramPacket(dataIn, dataIn.length);
            packet.setData(dataIn);
            
            
            try {
                socket.receive(packet);
                final DatagramPacket finalPacket = packet;
                new Thread(()->{
                    InetAddress address = finalPacket.getAddress();
                    int port = finalPacket.getPort();
                    byte[] data = Utils.deflator.decompress(finalPacket.getData());
                    
                    byte[] byteOut = Utils.deflator.compress(inerpreter.packetInterpreter(data));
                    
                    var packetOut = new DatagramPacket(byteOut , byteOut.length, address, port);

                    try {
                        socket.send(packetOut);
                    } catch (IOException ex) {
                        Logger.getLogger(SERVER.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //debug
                    //*
                    System.out.println("[" + Utils.getHour() +"] " +address + " -> this:45670  || this -> " +address + ":" + port
                    );
                    //*/
                }).start();
                
                
            } catch (IOException ex) {
                Logger.getLogger(SERVER.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            
        }
        
    }
    
}
