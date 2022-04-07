/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progetto;
import java.awt.Color;
import java.awt.Graphics;
import progetto.Collisions.*;

/**
 *
 * @author matti
 */
public class Collision {
    private int centerX;
    private int centerY;
    private Color fillColor;
    private Color borderColor;

    public Collision(int centerX, int centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public void setCenterPosition(int centerX, int centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }
    
    public void setPosition(int X, int Y) {
        this.centerX = X;
        this.centerY = Y;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }
    
    public Boolean isColliding(Collision c){
        return null;//NOT IMPLEMENTED FOR DEFAULT COLLISION
    }
    
    public void paint(Graphics g){
        return;
    }
    
    ///COLLISION FUNCTIONS///////////////
    public static boolean collision(Rectangle rect1, Rectangle rect2){
        if (rect1.getX() < rect2.getX() + rect2.getWidth() &&
            rect1.getX() + rect1.getWidth() > rect2.getX() &&
            rect1.getY() < rect2.getY() + rect2.getHeight() &&
            rect1.getHeight() + rect1.getY() > rect2.getY())
        {
            return true;
        }
        return false;
    }
    
    public static boolean collision(Circle circle1, Circle circle2){
        var dx = (circle1.getX() + circle1.getRadius()) - (circle2.getX() + circle2.getRadius());
        var dy = (circle1.getY() + circle1.getRadius()) - (circle2.getY() + circle2.getRadius());
        var distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < circle1.getRadius() + circle2.getRadius()) {
            return true;
        }
        return false;
    }
    
    public static boolean collision(Rectangle rect, Circle circle)
    {
        int circleDistancex = Math.abs(circle.getCenterX() - rect.getCenterX());
        int circleDistancey = Math.abs(circle.getCenterY() - rect.getCenterY());

        if (circleDistancex > (rect.getWidth()/2 + circle.getRadius())) { return false; }
        if (circleDistancey > (rect.getHeight()/2 + circle.getRadius())) { return false; }

        if (circleDistancex <= (rect.getWidth()/2)) { return true; } 
        if (circleDistancey <= (rect.getHeight()/2)) { return true; }

        double cornerDistance_sq = Math.pow(circleDistancex - rect.getWidth()/2, 2) +
                             Math.pow(circleDistancey - rect.getHeight()/2 ,2);

        return (cornerDistance_sq <= Math.pow(circle.getRadius(),2));
    }
    
    
}
