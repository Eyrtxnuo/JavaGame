/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net;

import java.util.UUID;

/**
 *
 * @author matti
 */
public abstract class connectionStatus {
    public static UUID currentUUID;
    public static String Username;
    
    public static void reset(){
        currentUUID = null;
        Username = null;
    }
}
