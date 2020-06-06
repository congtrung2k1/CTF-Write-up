// 
// Decompiled by Procyon v0.5.36
// 

package util;

import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Resource
{
    public static String hiden;
    
    public static BufferedImage getResouceImage(final String path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(ClassLoader.getSystemClassLoader().getResourceAsStream(path));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }
    
    static {
        Resource.hiden = "PwPCwqR5kTKXDCKLeXr3dHgwtQseobCKciTKYJ4DaJE=";
    }
}
