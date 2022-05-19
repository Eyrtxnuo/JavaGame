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
    
    /**load the game reference
     * @param game game reference*/
    public static void loadGame(Game game) {
        AudioPlayer.game = game;
    }
    
    /** plays the Effect passed
     * @param eff the effect enum*/
    public static void playEffect(Effects eff){
        if(!game.getPlaying().isSfxMuted()){
            playClip(getEffectPath(eff),game.getPlaying().getVolume());
        }
    }
    
    /** plays the Music passed
     * @param mus the music enum*/
    public static void playMusic(Musics mus){
        if(music!=null && music.isRunning())return;
        if(!game.getPlaying().isMusicMuted()){
            playClipMusic(getMusicPath(mus),game.getPlaying().getVolume());
        }
    }
    
    /** plays the Clip by path and volume
     * @param clipFile path to audio
     * @param volume volume level, from 0.0 to 1.0 ;
     */
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
    
    /** plays the Clip by path and volume, saves in music object
     * @param musicFile path to audio
     * @param volume volume level, from 0.0 to 1.0 ;
     */
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
    
    /** sets a clip volume
     * @param clip the clip to modify the volume to
     * @param volume the volume 0.0 to 1.0
     */ 
    public static void setVolume(Clip clip, float volume) {
        if (volume < 0f || volume > 1f)
            volume=0;
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);        
        gainControl.setValue(20f * (float) Math.log10(volume));
    }
    
    /** enum Effects list*/
    public static enum Effects{
        /**effects enums*/
        FIRE, JUMP, DAMAGE, ENEMY_DEAD, GAME_OVER, LEVEL_END, CLICK, PAUSE, DEATH;
    }
    
    /** enum Musics list*/
    public static enum Musics{
        LEVEL_MUSIC, MENU_MUSIC, GAME_COMPLETED_MUSIC;
    }
    
     /** Get the effect path
      * @param eff the Effect to get the path
      * @return the path
      */
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
            case DEATH:
                return "death.wav";
            default:
                return null;
        }
    }
    
    /** Get the music path
      * @param eff the Music to get the path
      * @return the path
      */
    public static String getMusicPath(Musics eff){
        switch (eff) {
            case LEVEL_MUSIC:
                return "mainTheme.wav";
            default:
                return null;
        }
    }
     
    /** Stops music thread */
    public static void stopMusic(){
        if(music!=null)
            music.stop();
    }
    
    /** Set volume on playing music */
    public static void updateMusicVolume(){
        setVolume(music, game.getPlaying().getVolume());
    }
    
    /** Set if music should be playing
     * @param on if the music should be on*/
    public static void toggleMusic(boolean on){
        if(!on){
            music.stop();
        }else{
            playMusic(Musics.LEVEL_MUSIC);
        }
    }
}