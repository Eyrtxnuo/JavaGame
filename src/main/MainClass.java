/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.time.Instant;
import java.util.Arrays;
import utils.AudioPlayer;

/**
 * Main class, calculates scale for screen, create game object
 *
 * @author matti
 */
public class MainClass {

    public static final Instant startTime = Instant.now();

    /**
     * program execution point
     *
     * @param args console arguments
     */
    public static void main(String[] args) {
        /*
        /*/
        discord.DiscordActivityManager.initializeCore();
        boolean fullscreen = true;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("Screen: " + screen.width + "*" + screen.height + ", " + Game.FPS_SET + " Hz");
        float vScale = (float) screen.height / (Game.TILES_DEFAULT_SIZE * Game.TILES_IN_HEIGHT);
        float hScale = (float) screen.width / (Game.TILES_DEFAULT_SIZE * Game.TILES_IN_WIDTH);
        int vIntScale = (int) Math.floor(vScale);
        int hIntScale = (int) Math.floor(hScale);
        float scale = vScale;
        if (Arrays.asList(args).contains("windowed") || fullscreen == false) {
            fullscreen = false;
            if (System.getProperty("scale") != null) {
                scale = Float.parseFloat(System.getProperty("scale"));
            } else {
                scale = Math.min(vIntScale, hIntScale);
            }
        }
        
        System.out.println("Scale: " + scale);
        AudioPlayer.loadGame(new Game(scale, fullscreen));
        AudioPlayer.playMusic(AudioPlayer.Musics.LEVEL_MUSIC);
        //*/
    }
}
