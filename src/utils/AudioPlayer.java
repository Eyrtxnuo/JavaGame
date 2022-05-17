/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import javax.sound.sampled.*;
import java.io.IOException;
import main.Game;
/**
 *
 * @author matti
 */
public class AudioPlayer {
    
    private static Game game;
    private static Clip music;
    

    public static void loadGame(Game game) {
        AudioPlayer.game = game;
    }
    
    
    public static void playEffect(Effects eff){
        if(!game.getPlaying().isSfxMuted()){
            playClip(getEffectPath(eff),game.getPlaying().getVolume());
        }
    }
    
    public static void playMusic(Musics eff){
        if(!game.getPlaying().isMusicMuted()){
            playClipMusic(getMusicPath(eff),game.getPlaying().getVolume());
        }
    }
    
    public static void playClip(String clipFile, float volume){ 
      try {
        Clip clip = AudioSystem.getClip();
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(
        AudioPlayer.class.getResourceAsStream("/audio/" + clipFile));
        clip.open(inputStream);
        setVolume(clip, volume);
        clip.start(); 
      } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
        System.err.println(e.getMessage());
      }
    }
    
    public static void playClipMusic(String musicFile, float volume){
       try {
        music = AudioSystem.getClip();
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(
        AudioPlayer.class.getResourceAsStream("/audio/" + musicFile));
        music.open(inputStream);
        setVolume(music, volume);
        music.loop(Clip.LOOP_CONTINUOUSLY);
        music.start(); 
      } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
        System.err.println(e.getMessage());
      }
    }
    
    public static void setVolume(Clip clip, float volume) {
        if (volume < 0f || volume > 1f)
            volume=0;
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);        
        gainControl.setValue(20f * (float) Math.log10(volume));
    }
    public static enum Effects{
        FIRE, JUMP, DAMAGE, ENEMY_DEAD, GAME_OVER, LEVEL_END, CLICK, PAUSE;
    }
    
    public static enum Musics{
        LEVEL_MUSIC, MENU_MUSIC, GAME_COMPLETED_MUSIC;
    }
    
    public static String getEffectPath(Effects eff){
        switch (eff) {
            case FIRE:
                return "shoot.wav";
            case JUMP:
                return "jump.wav";
            case DAMAGE:
                return "player_hit.wav";
            case ENEMY_DEAD:
                return "enemy_hit.wav";
            case CLICK:
                return "click.wav";
            case PAUSE:
                return "pause.wav";
            default:
                return null;
        }
    }
    
     public static String getMusicPath(Musics eff){
        switch (eff) {
            case LEVEL_MUSIC:
                return "mainTheme.wav";
            default:
                return null;
        }
    }
     
    public static void stopMusic(){
        if(music!=null)
            music.stop();
    }
    
    
    public static void updateMusicVolume(){
        setVolume(music, game.getPlaying().getVolume());
    }
    
    public static void toggleMusic(boolean on){
        if(!on){
            music.stop();
        }else{
            playMusic(Musics.LEVEL_MUSIC);
        }
    }
}