/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package discord;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.DiscordEventAdapter;
import de.jcm.discordgamesdk.activity.Activity;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.Game;
import main.MainClass;
import utils.Updater;
import gamestates.Gamestate;
import gamestates.PlayingMultiplayerClient;
import gamestates.PlayingMultiplayerServer;

/**
 *
 * @author matti
 */
public class DiscordActivityManager {
    public static final long ApplicationClientID  = 1017084657000525874L;
    public static Core core;

    public static String JoinedActivitySecret = "";
    
   

    public static void initializeCore() {
        try {
            Core.init(DownloadNativeLibrary.downloadDiscordLibrary());
            CreateParams params = new CreateParams();
            params.setClientID(ApplicationClientID);
            params.setFlags(CreateParams.getDefaultFlags());
            params.registerEventHandler(new DiscordEventAdapter() {
                @Override
                public void onActivityJoin(String secret) {
                    JoinedActivitySecret = secret;
                    try {
                        Gamestate.state = Gamestate.MENU;
                        Game.initPlaying(new PlayingMultiplayerClient(secret,net.SERVER.SRVport));
                        Gamestate.state = Gamestate.PLAYING;
                        setPlayingMultiplayerClientActivity();
                    } catch (SocketException | UnknownHostException | SocketTimeoutException ex) {
                        setMenuActivity();
                        Logger.getLogger(DiscordActivityManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            });
            
            core = new Core(params);
            setMenuActivity();
            
            (new Updater(()->{core.runCallbacks();return null;}, 60)).startThreadAsync();
            System.out.println("Discord Game SDK Core initialized");
        } catch (IOException ex) {
            Logger.getLogger(DiscordActivityManager.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Discord Game SDK Core initalization failed!");
        }
    }

    public static void setMenuActivity() {
        Activity activity = new Activity();
        //activity.setDetails("Playing Night Of Monsters");
        activity.setState("In the Menu");

        // Setting a start time causes an "elapsed" field to appear
        activity.timestamps().setStart(MainClass.startTime);

        /*// We are in a party with 10 out of 100 people.
        activity.party().size().setMaxSize(5);
        activity.party().size().setCurrentSize(3);*/

        // Make a "cool" image show up
        activity.assets().setLargeImage("gameicon");

        /*// Setting a join secret and a party ID causes an "Ask to Join" button to appear
        activity.party().setID("id");
        activity.secrets().setJoinSecret("Join!");*/

        core.activityManager().updateActivity(activity);
    }
    
    public static void setPlayingSingleplayerActivity() {
        Activity activity = new Activity();
        activity.setState("Playing singleplayer");
        activity.setDetails("Level " + (Game.playing.getCurrentLevel()+1));

        // Setting a start time causes an "elapsed" field to appear
        activity.timestamps().setStart(MainClass.startTime);

        /*// We are in a party with 10 out of 100 people.
        activity.party().size().setMaxSize(5);
        activity.party().size().setCurrentSize(3);*/

        // Make a "cool" image show up
        activity.assets().setLargeImage("gameicon");

        /*// Setting a join secret and a party ID causes an "Ask to Join" button to appear
        activity.party().setID("id");
        activity.secrets().setJoinSecret("Join!");*/

        core.activityManager().updateActivity(activity);
    }
    
    public static void setPlayingMultiplayerServerActivity() {
        Activity activity = new Activity();
        activity.setState("Playing Multiplayer!(sr)");
        int lvN = 1;
        if(Game.playing!=null){
            lvN += Game.playing.getCurrentLevel();
        }
        activity.setDetails("Level " + lvN);

        // Setting a start time causes an "elapsed" field to appear
        activity.timestamps().setStart(MainClass.startTime);

        // We are in a party with 10 out of 100 people.
        activity.party().size().setMaxSize(5);
        int currentSize = 1;
        if(Game.playing instanceof PlayingMultiplayerServer plSrv){
            currentSize += plSrv.getConnectedPlayersNumber();
        }
        activity.party().size().setCurrentSize(currentSize);

        // Make a "cool" image show up
        activity.assets().setLargeImage("gameicon");

        // Setting a join secret and a party ID causes an "Ask to Join" button to appear
        String ip = net.RemoteTools.getPublicIP().toString();
        activity.party().setID("id"+ip);
        activity.secrets().setJoinSecret(ip);

        core.activityManager().updateActivity(activity);
    }
    
    public static void setPlayingMultiplayerClientActivity() {
        Activity activity = new Activity();
        activity.setState("Playing Multiplayer!(cl)");
        int lvN = 1;
        if(Game.playing!=null){
            lvN += Game.playing.getCurrentLevel();
        }
        activity.setDetails("Level " + lvN);

        // Setting a start time causes an "elapsed" field to appear
        activity.timestamps().setStart(MainClass.startTime);

        // We are in a party with 10 out of 100 people.
        activity.party().size().setMaxSize(5);
        int currentSize = 1;
        if(Game.playing instanceof PlayingMultiplayerClient plSrv){
            currentSize += plSrv.getConnectedPlayersNumber();
            System.out.println(plSrv.getConnectedPlayersNumber());
        }
        activity.party().size().setCurrentSize(currentSize);

        // Make a "cool" image show up
        activity.assets().setLargeImage("gameicon");

        // Setting a join secret and a party ID causes an "Ask to Join" button to appear
        String ip = JoinedActivitySecret;
        activity.party().setID("id"+ip);
        activity.secrets().setJoinSecret(ip);

        core.activityManager().updateActivity(activity);
    }
    
}
