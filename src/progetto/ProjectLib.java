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
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
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
            image=ImageIO.read(ProjectLib.class.getResource(posizione));
        } catch (IOException ex) {
            System.out.println("Immagine alla posizione: "+posizione+" caricata correttamente");
            Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }

    
    public static char[][] readFile(String filePath) {
        
        try {
            String result;
            result = Files.readString(new File(ProjectLib.class.getResource(filePath).toURI()).toPath());
            
       
            final int RIGHE = result.split("\n").length;
            final int COLONNE = result.split("\n")[0].length()-1;
            char[][] matrix = new char[RIGHE][COLONNE];
            int rig = 0;
            for(String str : result.split("\n")){
                int col = 0;
                for(Character val : str.substring(0, COLONNE).toCharArray()){
                    //System.out.println(val);
                    matrix[rig][col] = val;
                    col++;
                }
                rig++;
            } 
            return matrix;
        } catch (IOException ex) {
            
        }
        catch (URISyntaxException ex){
            Logger.getLogger(ProjectLib.class.getName()).log(Level.SEVERE, "URL SYNTAX ERROR", ex);
        }catch(NullPointerException ex){
            Logger.getLogger(ProjectLib.class.getName()).log(Level.SEVERE, "FILE NOT FOUND", ex);
        }
        return null;//se fallisce il caricamento
    }
        
    public static String MapArraytoString(char[][] map) {
        int RIGHE = map.length;
        int COLONNE = map[0].length;
        String str = "";
        
        str += "\n";
        for (int i = 0; i < RIGHE; i++) {

            for (int j = 0; j < COLONNE; j++) {
                str += " " + map[i][j];
            }
            str += "\n";
        }
        return str;
    }
    
}
