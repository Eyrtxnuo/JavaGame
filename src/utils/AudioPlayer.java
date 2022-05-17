/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.LineEvent.Type;
/**
 *
 * @author matti
 */
public class AudioPlayer {

    public static void playClip(String clipFile){ 
      try {
        Clip clip = AudioSystem.getClip();
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(
        AudioPlayer.class.getResourceAsStream("/" + clipFile));
        clip.open(inputStream);
        clip.start(); 
      } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
        System.err.println(e.getMessage());
      }
    }
    
    public static enum Effects{
        FIRE, JUMP, DAMAGE, ENEMY_DEAD, GAME_OVER, LEVEL_END;
    }
    
    public static enum Musics{
        LEVEL_MUSIC, MENU_MUSIC, GAME_COMPLETED_MUSIC;
    }
}