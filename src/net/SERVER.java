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
    public static final int SRVport = 45670;
    private boolean running;
    private DatagramSocket socket;
    
    ServerNetInterpreter interpreter; 
    

    public SERVER(ServerNetInterpreter inerpreter) {
        this.interpreter = inerpreter;
    }
    
    @Override
    public void run(){
         running = true;
        // TODO code application logic here
        DatagramPacket packet;
        try {
            socket = new DatagramSocket(SRVport);
        }catch(SocketException ex){
            System.out.println("ERROR: " + ex.getMessage());
            return;
        }
        System.out.println("PORTA: "+socket.getLocalPort());
     
       
        while(running){
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
                    
                    byte[] byteOut = Utils.deflator.compress(interpreter.packetInterpreter(data));
                    
                    var packetOut = new DatagramPacket(byteOut , byteOut.length, address, port);

                    try {
                        socket.send(packetOut);
                    } catch (IOException ex) {
                        Logger.getLogger(SERVER.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //debug
                    /*
                    System.out.println("[" + Utils.getHour() +"] " +address + " -> this:45670  || this -> " +address + ":" + port);
                    //*/
                }).start();
            } catch (SocketException ex ) {
                System.out.println("Socket closed!");
            } catch (IOException ex ) {
                Logger.getLogger(SERVER.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    void close() {
        running = false;
        socket.close();
    }

    public boolean isRunning() {
        return running;
    }
    
    
    
}
