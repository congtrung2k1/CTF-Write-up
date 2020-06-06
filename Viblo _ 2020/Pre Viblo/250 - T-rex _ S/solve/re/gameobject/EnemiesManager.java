// 
// Decompiled by Procyon v0.5.36
// 

package gameobject;

import java.awt.Graphics;
import java.util.Iterator;
import java.util.ArrayList;
import util.Resource;
import java.util.List;
import java.util.Random;
import java.awt.image.BufferedImage;

public class EnemiesManager
{
    private BufferedImage cactus1;
    private BufferedImage cactus2;
    private Random rand;
    private List<Enemy> enemies;
    private MainCharacter mainCharacter;
    
    public EnemiesManager(final MainCharacter mainCharacter) {
        this.rand = new Random();
        this.cactus1 = Resource.getResouceImage("data/cactus1.png");
        this.cactus2 = Resource.getResouceImage("data/cactus2.png");
        this.enemies = new ArrayList<Enemy>();
        this.mainCharacter = mainCharacter;
        this.enemies.add(this.createEnemy());
    }
    
    public void update() {
        for (final Enemy e : this.enemies) {
            e.update();
        }
        final Enemy enemy = this.enemies.get(0);
        if (enemy.isOutOfScreen()) {
            this.mainCharacter.upScore();
            this.enemies.clear();
            this.enemies.add(this.createEnemy());
        }
    }
    
    public void draw(final Graphics g) {
        for (final Enemy e : this.enemies) {
            e.draw(g);
        }
    }
    
    private Enemy createEnemy() {
        final int type = this.rand.nextInt(2);
        if (type == 0) {
            return new Cactus(this.mainCharacter, 800, this.cactus1.getWidth() - 10, this.cactus1.getHeight() - 10, this.cactus1);
        }
        return new Cactus(this.mainCharacter, 800, this.cactus2.getWidth() - 10, this.cactus2.getHeight() - 10, this.cactus2);
    }
    
    public boolean isCollision() {
        for (final Enemy e : this.enemies) {
            if (this.mainCharacter.getBound().intersects(e.getBound())) {
                return true;
            }
        }
        return false;
    }
    
    public void reset() {
        this.enemies.clear();
        this.enemies.add(this.createEnemy());
    }
}
