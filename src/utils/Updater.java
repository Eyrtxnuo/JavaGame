/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;


import java.util.concurrent.Callable;
import static main.Game.manualFrameAdvancing;

/**
 *
 * @author matti
 */
public class Updater {
    
    Callable funct;
    int tps;
    private boolean running;
    Thread upd;

    public Updater(Callable funct, int tps) {
        this.funct = funct;
        this.tps = tps;
        upd = new Thread();
    }

    
    
    private void updateThread(){
        long previousTime = System.nanoTime();
        long ticks = 0;
        double timePerTick = 1000000000 / tps;
        double deltaT = 0;
        while(running){
            long currentTime = System.nanoTime();
            if(!manualFrameAdvancing) {
                deltaT += (currentTime - previousTime) / timePerTick;
                if (deltaT >= 1) {
                    Tick();
                    ticks++;
                    deltaT--;
                }
            }
            sleepNano(Math.max((long) (timePerTick - (currentTime-previousTime)), 0));
            previousTime = currentTime;
        }
    } 
    
    private void Tick(){
        try {
            funct.call();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Updater.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            running = false;
        }
    }
    
    public void stopThread(){
        running = false;
    }
    
    public void startThread(){
        if(!running){
            running = true;
            if(!upd.isAlive()){
                upd = new Thread(()->{updateThread();});
                upd.start();
            }
        }
    }
    
    private static void sleepNano(long nano){
        try {
            Thread.sleep(nano/1000000, (int) (nano%1000000));
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Updater.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    public boolean isRunning() {
        return running;
    }
}
