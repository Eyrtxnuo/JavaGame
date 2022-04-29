/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import main.Game;
import static utils.Constants.PlayerConstants.GetSpriteAmount;
import static utils.Constants.PlayerConstants.IDLE;
import utils.LoadSave;

/**
 *
 * @author Bossi_Mattia
 */
public class Enemy extends Entity{
    
    final int spriteX = 64, spriteY = 40;
    private float xDrawOffset = 21;
    private float yDrawOffset = 4;
    
    private BufferedImage[][] animations;
    private int action = IDLE;
    private int aniIndex;
    
    public Enemy(float x, float y, int width, int height) {
        super(x, y, width, height);
        initHitbox(x, y, 20f, 27f);
        LoadAnimations();
    }
    
    public void render(Graphics g, float offsetX, float offsetY) {
        g.drawImage(animations[action][aniIndex % animations[action].length], (int) ((hitbox.x - xDrawOffset) * Game.SCALE + offsetX), (int) ((hitbox.y - yDrawOffset) * Game.SCALE + offsetY), (int) (spriteX * Game.SCALE), (int) (spriteY * Game.SCALE), null);
        
    }
    
    
    private void LoadAnimations() {
        BufferedImage img = LoadSave.GetSpriteAtlas("enemy_sprite.png");
        animations = new BufferedImage[9][];
        for (int j = 0; j < animations.length; j++) {
            animations[j] = new BufferedImage[img.getWidth()];
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * spriteX, j * spriteY, spriteX, spriteY);
            }
        }
    }
}
