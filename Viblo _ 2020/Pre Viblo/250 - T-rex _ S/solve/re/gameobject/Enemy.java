// 
// Decompiled by Procyon v0.5.36
// 

package gameobject;

import java.awt.Rectangle;
import java.awt.Graphics;

public abstract class Enemy
{
    public abstract void update();
    
    public abstract void draw(final Graphics p0);
    
    public abstract Rectangle getBound();
    
    public abstract boolean isOutOfScreen();
}
