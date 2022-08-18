/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;


import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import static main.Game.manualFrameAdvancing;

/**
 *
 * @author matti
 */
public class Updater {
    /** function to be called at tick */
    Callable funct;
    /** ticks to call per second */
    int tps;
    /** if thread should currently run */
    private boolean running;
    /** updater thread */
    Thread upd;

    boolean asyncCalls = false;
    
    /** constructor, define function and tps
     * @param funct function to be called at tick
     * @param tps functions calls per second
     */
    public Updater(Callable funct, int tps) {
        this.funct = funct;
        this.tps = tps;
        upd = new Thread();
    }

    
    /** function run in thread, calls funct tps times a second */
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
                    if(deltaT>3){
                        //System.out.println(deltaT);
                        deltaT=(int)deltaT;
                    }
                    Tick();
                    ticks++;
                    deltaT--;
                }
            }
            sleepNano(Math.max((long) (timePerTick - (currentTime-previousTime)), 0));
            previousTime = currentTime;
        }
    } 
    
    
    /** calls the function, stops execution on Exception */
    private void Tick(){
        try {
            if(asyncCalls){
                new Thread(()->{
                    try {
                        funct.call();
                    } catch (Exception ex) {
                        Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }).start();
            }else{
                funct.call();
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Updater.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            running = false;
        }
    }
    
    /** makes the thread stop at next update */
    public void stopThread(){
        running = false;
    }
    
    /** start thread, if thread is not in ALIVE state, generate a new thread */
    public void startThread(){
        if(!running){
            running = true;
            if(!upd.isAlive()){
                upd = new Thread(()->{updateThread();});
                upd.start();
            }
        }
    }
    
    public void startThreadAsync(){
        asyncCalls = true;
        startThread();
    }
    
    /** static wrapper to sleep in nanoseconds */
    private static void sleepNano(long nano){
        try {
            Thread.sleep(nano/1000000, (int) (nano%1000000));
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Updater.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    
    /** get running state */
    public boolean isRunning() {
        return running;
    }
    
    
}
