/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.JFrame;

/**
 *
 * @author matti
 */
public class GameWindow {
    JFrame jframe;
    
    public GameWindow(GamePanel gamePanel) {
        jframe = new JFrame();
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(gamePanel);//paint gamePanel on frame
        jframe.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                gamePanel.getGame().windowLostFocus();
            }
        });
        jframe.setResizable(false);
        jframe.pack();//auto-fit gamePanel
        jframe.setLocationRelativeTo(null);//center on screen
        jframe.setVisible(true);//set visible - always at the end of constructor!
    }
}
