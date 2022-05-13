/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 *
 * @author matti
 */
public class MainClass {
    
    
    public static void main(String[] args) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("Screen: " + screen.width + "*" + screen.height );
        float vScale = (float)screen.height/(Game.TILES_DEFAULT_SIZE*Game.TILES_IN_HEIGHT);
        float hScale = (float)screen.width/(Game.TILES_DEFAULT_SIZE*Game.TILES_IN_WIDTH);
        int vIntScale = (int)Math.floor(vScale);
        int hIntScale = (int)Math.floor(hScale);
        float scale = Math.min(vScale, hScale);
        
        
        new Game(vScale, true);
    }
}
