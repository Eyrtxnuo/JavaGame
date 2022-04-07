package progetto;

import java.util.logging.Level;
import java.util.logging.Logger;

public class disegno extends Thread {

    private mainFrame frame;
    final static int FPS= 60;
    
    
    public disegno(mainFrame frame) {
        this.frame = frame;
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(1000/FPS);
            } catch (InterruptedException ex) {
                Logger.getLogger(disegno.class.getName()).log(Level.SEVERE, null, ex);
            }
            frame.FrameMove();
            frame.repaint();
        }
    }
    /*
    double interpolation = 0;
    final int TICKS_PER_SECOND = 60;
    final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
    final int MAX_FRAMESKIP = 5;

    @Override
    public void run() {
        double next_game_tick = System.currentTimeMillis();
        int loops;

        while (true) {
            loops = 0;
            while (System.currentTimeMillis() > next_game_tick
                    && loops < MAX_FRAMESKIP) {

                frame.FrameMove();

                next_game_tick += SKIP_TICKS;
                loops++;
            }

            interpolation = (System.currentTimeMillis() + SKIP_TICKS - next_game_tick
                    / (double) SKIP_TICKS);
            frame.repaint();//interpolation
        }
    }
*/
    
    
}
