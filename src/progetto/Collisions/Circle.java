/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package progetto.Collisions;
import java.awt.Color;
import java.awt.Graphics;
import progetto.Collision;

/**
 *
 * @author matti
 */
public class Circle extends Collision{
    private int radius;   

    public Circle(int radius, int centerX, int centerY) {
        super(centerX, centerY);
        this.radius = radius;
    }

    @Override
    public void setPosition(int X, int Y) {
        setCenterPosition(X+getRadius(), Y+getRadius());
    }
    
    public int getRadius() {
        return radius;
    }

    public void setRaggio(int raggio) {
        this.radius = raggio;
    }
    
    public int getX(){
        return this.getCenterX()-this.getRadius();
    }   
    
    public int getY(){
        return this.getCenterY()-this.getRadius();
    }
    
    
    @Override
    public Boolean isColliding(Collision c) {
        if(c instanceof Circle circle){
            return Collision.collision(this, circle);
        }else if(c instanceof Rectangle rectangle){
            return Collision.collision(rectangle, this);
        } 
        return null;
    }
    
    @Override
    public boolean willCollide(int x, int y, Collision c){
        Circle thisNext = new Circle(this.getRadius(), 0, 0);
        thisNext.setPosition(x, y);
        return thisNext.isColliding(c);
    }
    
    @Override
    public void paint(Graphics g){
        Color before = g.getColor();
        g.setColor(this.getFillColor());
        g.fillOval(getX(), getY(), getRadius()*2, getRadius()*2);
        g.setColor(this.getBorderColor());
        g.drawOval(getX(), getY(), getRadius()*2, getRadius()*2);
        g.setColor(before);
    }
    
}
