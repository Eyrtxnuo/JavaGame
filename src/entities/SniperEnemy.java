
package entities;

import utils.Constants;
import static utils.Constants.PlayerConstants.RUNNING;
import utils.LoadSave;

public class SniperEnemy extends Enemy{

    public SniperEnemy(float x, float y) {
        super(x, y);
        TYPE = Constants.EnemyConstants.CRABBY;
        initSprite();
        settings();
        initHitbox(x, y, (int) (22f), (int) (17f));
        LoadAnimations(LoadSave.CRABBY_ATLAS);
        resetMovements();
    }

    @Override
    public void update() {
        super.update();
        fire();
    }
    
    
    
    private void initSprite() {
        spriteX = Constants.EnemyConstants.CRABBY_WIDTH_DEFAULT;
        spriteY = Constants.EnemyConstants.CRABBY_HEIGHT_DEFAULT;
        xDrawOffset = 26;
        yDrawOffset = 10;
    }

    private void settings() {
        action = RUNNING;
        movSpeed = 0f;
        gravity = 0.04f;
        jumpSpeed = -2.5f;
    }

    private void fire() {
       
    }
       
}