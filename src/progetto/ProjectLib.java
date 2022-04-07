/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progetto;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author matti
 */
public class ProjectLib {
    
    
    public static String KeyCodeArrayToString(Object[] array){
        if(array.length == 0){
            return "[]";
        }
        String str = "["+KeyEvent.getKeyText((int)array[0]);
        for(int i = 1;i < array.length;i++){
            str+=", "+KeyEvent.getKeyText((int)array[i]);
        }
        
        return str+"]";
    }
    
    
    
    public static BufferedImage caricaImmagine(String posizione) {
        BufferedImage image = null;

        try {
            image=ImageIO.read(mainFrame.class.getResource(posizione));
        } catch (IOException ex) {
            System.out.println("Immagine alla posizione: "+posizione+" caricata correttamente");
            Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }

}
