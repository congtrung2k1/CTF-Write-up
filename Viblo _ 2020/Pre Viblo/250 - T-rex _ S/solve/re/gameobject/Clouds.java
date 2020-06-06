// 
// Decompiled by Procyon v0.5.36
// 

package gameobject;

import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.ArrayList;
import util.Resource;
import java.awt.image.BufferedImage;
import java.util.List;

public class Clouds
{
    private List<ImageCloud> listCloud;
    private BufferedImage cloud;
    private MainCharacter mainCharacter;
    
    public Clouds(final int width, final MainCharacter mainCharacter) {
        this.mainCharacter = mainCharacter;
        this.cloud = Resource.getResouceImage("data/cloud.png");
        this.listCloud = new ArrayList<ImageCloud>();
        ImageCloud imageCloud = new ImageCloud();
        imageCloud.posX = 0.0f;
        imageCloud.posY = 30;
        this.listCloud.add(imageCloud);
        imageCloud = new ImageCloud();
        imageCloud.posX = 150.0f;
        imageCloud.posY = 40;
        this.listCloud.add(imageCloud);
        imageCloud = new ImageCloud();
        imageCloud.posX = 300.0f;
        imageCloud.posY = 50;
        this.listCloud.add(imageCloud);
        imageCloud = new ImageCloud();
        imageCloud.posX = 450.0f;
        imageCloud.posY = 20;
        this.listCloud.add(imageCloud);
        imageCloud = new ImageCloud();
        imageCloud.posX = 600.0f;
        imageCloud.posY = 60;
        this.listCloud.add(imageCloud);
    }
    
    public void update() {
        final Iterator<ImageCloud> itr = this.listCloud.iterator();
        final ImageCloud imageCloud;
        final ImageCloud firstElement = imageCloud = itr.next();
        imageCloud.posX -= this.mainCharacter.getSpeedX() / 8.0f;
        while (itr.hasNext()) {
            final ImageCloud imageCloud2;
            final ImageCloud element = imageCloud2 = itr.next();
            imageCloud2.posX -= this.mainCharacter.getSpeedX() / 8.0f;
        }
        if (firstElement.posX < -this.cloud.getWidth()) {
            this.listCloud.remove(firstElement);
            firstElement.posX = 600.0f;
            this.listCloud.add(firstElement);
        }
    }
    
    public void draw(final Graphics g) {
        for (final ImageCloud imgLand : this.listCloud) {
            g.drawImage(this.cloud, (int)imgLand.posX, imgLand.posY, null);
        }
    }
    
    private class ImageCloud
    {
        float posX;
        int posY;
    }
}
