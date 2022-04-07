package progetto;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.Arrays;
import java.util.LinkedHashSet;
import progetto.Collisions.*;

public class mainFrame extends javax.swing.JFrame implements KeyListener, WindowFocusListener{

    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private final int width;
    private final int height;
    private final LinkedHashSet inputs = new LinkedHashSet<KeyEvent>();
    private final Collision rett1;
    private final Collision rett2;
    
    @SuppressWarnings("LeakingThisInConstructor")
    public mainFrame() {
        y1 = 100;
        x1 = 100;
        y2= 100;
        x2 = 300;
        width= 90;
        height=90;
        initComponents();
        
        rett1 =new Circle(width/2, 0, 0);
        rett1.setBorderColor(Color.BLACK);
        rett1.setFillColor(Color.MAGENTA);
        
        rett2 =new Rectangle(height, width, 0, 0);
        rett2.setBorderColor(Color.BLACK);
        rett2.setFillColor(Color.CYAN);
        
        addKeyListener(this);
        addWindowFocusListener(this);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void paint(Graphics g) {
        Image immagine = createImage(getActualWidth(), getActualHeight());
        Graphics gImmagine = immagine.getGraphics();
        
        
        
        //System.out.println(x1+ " " + y1+ "; " + x2+ " " + y2 + "  Inputs: " + ProjectLib.KeyCodeArrayToString(inputs.toArray()));
        gImmagine.clearRect(0, 0, this.getWidth(), this.getHeight());
        gImmagine.drawImage(ProjectLib.caricaImmagine("/resources/images/Invaders.jpg"),0 ,0, null);
        char[][] map= ProjectLib.readFile("/resources/maps/map1.txt");
        System.out.println(Arrays.deepToString(map));
        System.out.println(ProjectLib.MapArraytoString(map));
        //gImmagine.drawRect(0, 0, immagine.getWidth(this)-1, immagine.getHeight(this)-1);//Border
        /*gImmagine.setColor(Color.red);
        gImmagine.fillRect(x1, y1, 15, 15);
        gImmagine.setColor(Color.black);
        gImmagine.drawRect(x1, y1, 15, 15);
        gImmagine.setColor(Color.yellow);
        gImmagine.fillRect(x2, y2, 15, 15);
        gImmagine.setColor(Color.blue);
        gImmagine.drawRect(x2, y2, 15, 15);*/
        
        
        
        if(rett2.isColliding(rett1)==true){
            gImmagine.setColor(Color.orange);
            gImmagine.fillRect(0, 0, getActualWidth(), getActualHeight());
        }
        
        System.out.println(rett2.isColliding(rett1));
        rett1.paint(gImmagine);
        rett2.paint(gImmagine);
        
        
        
        g.drawImage(immagine, this.getInsets().left, this.getInsets().top, this);
        

    }

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                mainFrame frame = new mainFrame();
                frame.setVisible(true);
                disegno d = new disegno(frame);
                d.start();
            }
        });
    }

    @Override
    public void keyPressed(KeyEvent e) {
        this.inputs.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        this.inputs.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    
    private int speed= 2;
    public void FrameMove(){
        
        P1x: {
            if (inputs.contains(KeyEvent.VK_D)) {
                if (x1+ width+1 < getActualWidth()) {
                    x1+= speed;
                }
                else{
                    x1= getActualWidth()- width-1;
                    break P1x;
                }
            }
            if (inputs.contains(KeyEvent.VK_A)) {
                if (x1- speed > 0) {
                    x1-= speed;
                }
                else{
                    x1= 0;
                }
            }
        }
        P1y: {
            if (inputs.contains(KeyEvent.VK_W)) {
                if (y1- speed > 0) {
                    y1-= speed;
                }
                else{
                    y1= 0;
                    break P1y;
                }
            }
            if (inputs.contains(KeyEvent.VK_S)) {
                if (y1 + height+1 < getActualHeight()) {
                    y1 += speed;
                }
                else{
                    y1 = getActualHeight()-height-1;
                }
            }
        }
        //Player 2
        P2x: {
            if (inputs.contains(KeyEvent.VK_L)) {
                if (x2+ width+1 < getActualWidth()) {
                    x2+= speed;
                }
                else{
                    x2= getActualWidth()- width-1;
                    break P2x;
                }
            }
            if (inputs.contains(KeyEvent.VK_J)) {
                if (x2- speed > 0) {
                    x2-= speed;
                }
                else{
                    x2= 0;
                }
            }
        }
        P2y: {
            if (inputs.contains(KeyEvent.VK_I)) {
                if (y2- speed > 0) {
                    y2-= speed;
                }
                else{
                    y2= 0;
                    break P2y;
                }
            }
            if (inputs.contains(KeyEvent.VK_K)) {
                if (y2 + height+1 < getActualHeight()) {
                    y2 += speed;
                }
                else{
                    y2 = getActualHeight()-height-1;
                }
            }
        }
        rett1.setPosition(x1, y1);
        rett2.setPosition(x2, y2);

    }
    
    
    
    public int getActualWidth(){
        return this.getWidth()-this.getInsets().left-this.getInsets().right;
    }
    public int getActualHeight(){
        return this.getHeight()-this.getInsets().top-this.getInsets().bottom;
    }

    
    @Override
    public void windowGainedFocus(WindowEvent e) {
    }

    @Override
    public void windowLostFocus(WindowEvent e) {
        inputs.clear();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}