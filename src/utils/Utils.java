/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author matti
 */
public class Utils {
    
    public static String getHour(){
        return (new SimpleDateFormat("HH:mm:ss")).format(new Date(System.currentTimeMillis()));
    }
    
    public static String getHour(String pattern){
        return (new SimpleDateFormat(pattern)).format(new Date(System.currentTimeMillis()));
    }
}
