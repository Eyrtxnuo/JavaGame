
package progetto.Collisions;
import java.awt.Color;
import java.awt.Graphics;
import progetto.Collision;


public class Rectangle extends Collision{
    private int height;
    private int width;

    public Rectangle(int height, int width, int centerX, int centerY) {
        super(centerX, centerY);
        this.height = height;
        this.width = width;
    }

    @Override
    public Boolean isColliding(Collision c) {
        if(c instanceof Rectangle rectangle){
            return Collision.collision(this, rectangle);
        }else if(c instanceof Circle circle){
             return Collision.collision(this, circle);
        } 
        return null;
    }
    
    @Override
    public boolean willCollide(int x, int y, Collision c){
        Rectangle thisNext = new Rectangle(this.getWidth(), getHeight(), 0, 0);
        thisNext.setPosition(x, y);
        return thisNext.isColliding(c);
    }

    @Override
    public void setPosition(int X, int Y) {
        setCenterPosition(X+width/2, Y+height/2);
    }
    
    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
    public int getX(){
        return this.getCenterX()-this.getWidth()/2;
    }   
    
    public int getY(){
        return this.getCenterY()-this.getHeight()/2;
    }
    
    @Override
    public void paint(Graphics g){
        Color before = g.getColor();
        g.setColor(this.getFillColor());
        g.fillRect(getX(), getY(), width, height);
        g.setColor(this.getBorderColor());
        g.drawRect(getX(), getY(), width, height);
        g.setColor(before);
    }
}
