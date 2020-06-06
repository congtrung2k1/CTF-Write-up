// 
// Decompiled by Procyon v0.5.36
// 

package gameobject;

import java.util.Random;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.ArrayList;
import util.Resource;
import java.awt.image.BufferedImage;
import java.util.List;

public class Land
{
    public static final int LAND_POSY = 103;
    private List<ImageLand> listLand;
    private BufferedImage land1;
    private BufferedImage land2;
    private BufferedImage land3;
    private MainCharacter mainCharacter;
    
    public Land(final int width, final MainCharacter mainCharacter) {
        this.mainCharacter = mainCharacter;
        this.land1 = Resource.getResouceImage("data/land1.png");
        this.land2 = Resource.getResouceImage("data/land2.png");
        this.land3 = Resource.getResouceImage("data/land3.png");
        final int numberOfImageLand = width / this.land1.getWidth() + 2;
        this.listLand = new ArrayList<ImageLand>();
        for (int i = 0; i < numberOfImageLand; ++i) {
            final ImageLand imageLand = new ImageLand();
            imageLand.posX = (float)(i * this.land1.getWidth());
            this.setImageLand(imageLand);
            this.listLand.add(imageLand);
        }
    }
    
    public void update() {
        final Iterator<ImageLand> itr = this.listLand.iterator();
        final ImageLand imageLand;
        final ImageLand firstElement = imageLand = itr.next();
        imageLand.posX -= this.mainCharacter.getSpeedX();
        float previousPosX = firstElement.posX;
        while (itr.hasNext()) {
            final ImageLand element = itr.next();
            element.posX = previousPosX + this.land1.getWidth();
            previousPosX = element.posX;
        }
        if (firstElement.posX < -this.land1.getWidth()) {
            this.listLand.remove(firstElement);
            firstElement.posX = previousPosX + this.land1.getWidth();
            this.setImageLand(firstElement);
            this.listLand.add(firstElement);
        }
    }
    
    private void setImageLand(final ImageLand imgLand) {
        final int typeLand = this.getTypeOfLand();
        if (typeLand == 1) {
            imgLand.image = this.land1;
        }
        else if (typeLand == 3) {
            imgLand.image = this.land3;
        }
        else {
            imgLand.image = this.land2;
        }
    }
    
    public void draw(final Graphics g) {
        for (final ImageLand imgLand : this.listLand) {
            g.drawImage(imgLand.image, (int)imgLand.posX, 103, null);
        }
    }
    
    private int getTypeOfLand() {
        final Random rand = new Random();
        final int type = rand.nextInt(10);
        if (type == 1) {
            return 1;
        }
        if (type == 9) {
            return 3;
        }
        return 2;
    }
    
    private class ImageLand
    {
        float posX;
        BufferedImage image;
    }
}
