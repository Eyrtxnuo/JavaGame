/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.ByteArrayOutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;
import org.json.JSONArray;

/**
 *
 * @author matti
 */
public abstract class Utils {
    
    public static String getHour(){
        return (new SimpleDateFormat("HH:mm:ss")).format(new Date(System.currentTimeMillis()));
    }
    
    public static String getHour(String pattern){
        return (new SimpleDateFormat(pattern)).format(new Date(System.currentTimeMillis()));
    }
    
    public static abstract class deflator{
        public static byte[] compress(byte[] in) {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                DeflaterOutputStream defl = new DeflaterOutputStream(out);
                defl.write(in);
                defl.flush();
                defl.close();

                return out.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(150);
                return null;
            }
        }

        public static byte[] decompress(byte[] in) {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InflaterOutputStream infl = new InflaterOutputStream(out);
                infl.write(in);
                infl.flush();
                infl.close();

                return out.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(150);
                return null;
            }
        }
    }
    
    public static abstract class jsonMapper{
        
        public static JSONArray pointToJSON(Point.Float p){
            return new JSONArray().put(p.x).put(p.y);
        } 
        
        public static Point.Float JSONTOPoint(JSONArray a){
            return new Point.Float(a.getFloat(0),a.getFloat(1));
        }
        
        public static int[][] JSONToLevelData(JSONArray arr){
            int[][] data = new int[arr.length()][];
            for (int c = 0; c < arr.length(); c++) {
                JSONArray array = arr.getJSONArray(c);
                data[c] = new int[array.length()];
                for (int i = 0; i < array.length(); ++i) {
                    data[c][i] = array.getInt(i);
                }
            }
            return data;
        }
        
    }
    
    public static boolean isPrintableChar( char c ) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of( c );
        return (!Character.isISOControl(c)) &&
            c != KeyEvent.CHAR_UNDEFINED &&
            block != null &&
            block != Character.UnicodeBlock.SPECIALS &&
            LoadSave.FONT.canDisplay(c);
    }   
    
    
}
