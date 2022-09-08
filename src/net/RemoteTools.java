
package net;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/** 
 *
 * @author matti
 */
public class RemoteTools {
   
    public static InetAddress getPublicIP(){
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));

            return InetAddress.getByName(in.readLine());
        } catch (MalformedURLException ex) {
            Logger.getLogger(RemoteTools.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RemoteTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
