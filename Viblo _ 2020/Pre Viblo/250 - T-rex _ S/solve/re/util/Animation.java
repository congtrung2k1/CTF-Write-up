// 
// Decompiled by Procyon v0.5.36
// 

package util;

import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.util.List;

public class Animation
{
    private List<BufferedImage> list;
    private long deltaTime;
    private int currentFrame;
    private long previousTime;
    
    public Animation(final int deltaTime) {
        this.currentFrame = 0;
        this.deltaTime = deltaTime;
        this.list = new ArrayList<BufferedImage>();
        this.previousTime = 0L;
    }
    
    public void updateFrame() {
        if (System.currentTimeMillis() - this.previousTime >= this.deltaTime) {
            ++this.currentFrame;
            if (this.currentFrame >= this.list.size()) {
                this.currentFrame = 0;
            }
            this.previousTime = System.currentTimeMillis();
        }
    }
    
    public void addFrame(final BufferedImage image) {
        this.list.add(image);
    }
    
    public BufferedImage getFrame() {
        return this.list.get(this.currentFrame);
    }
}
