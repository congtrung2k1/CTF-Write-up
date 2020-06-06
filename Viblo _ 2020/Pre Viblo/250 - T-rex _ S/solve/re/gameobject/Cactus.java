// 
// Decompiled by Procyon v0.5.36
// 

package gameobject;

import java.awt.Color;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Cactus extends Enemy
{
    public static final int Y_LAND = 125;
    private int posX;
    private int width;
    private int height;
    private BufferedImage image;
    private MainCharacter mainCharacter;
    private Rectangle rectBound;
    
    public Cactus(final MainCharacter mainCharacter, final int posX, final int width, final int height, final BufferedImage image) {
        this.posX = posX;
        this.width = width;
        this.height = height;
        this.image = image;
        this.mainCharacter = mainCharacter;
        this.rectBound = new Rectangle();
    }
    
    @Override
    public void update() {
        this.posX -= (int)this.mainCharacter.getSpeedX();
    }
    
    @Override
    public void draw(final Graphics g) {
        g.drawImage(this.image, this.posX, 125 - this.image.getHeight(), null);
        g.setColor(Color.red);
    }
    
    @Override
    public Rectangle getBound() {
        this.rectBound = new Rectangle();
        this.rectBound.x = this.posX + (this.image.getWidth() - this.width) / 2;
        this.rectBound.y = 125 - this.image.getHeight() + (this.image.getHeight() - this.height) / 2;
        this.rectBound.width = this.width;
        this.rectBound.height = this.height;
        return this.rectBound;
    }
    
    @Override
    public boolean isOutOfScreen() {
        return this.posX < -this.image.getWidth();
    }
}
