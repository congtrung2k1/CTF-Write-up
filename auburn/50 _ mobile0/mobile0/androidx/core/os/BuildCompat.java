// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.os;

import android.os.Build$VERSION;

public class BuildCompat
{
    private BuildCompat() {
    }
    
    @Deprecated
    public static boolean isAtLeastN() {
        return Build$VERSION.SDK_INT >= 24;
    }
    
    @Deprecated
    public static boolean isAtLeastNMR1() {
        return Build$VERSION.SDK_INT >= 25;
    }
    
    @Deprecated
    public static boolean isAtLeastO() {
        return Build$VERSION.SDK_INT >= 26;
    }
    
    @Deprecated
    public static boolean isAtLeastOMR1() {
        return Build$VERSION.SDK_INT >= 27;
    }
    
    @Deprecated
    public static boolean isAtLeastP() {
        return Build$VERSION.SDK_INT >= 28;
    }
    
    public static boolean isAtLeastQ() {
        final int length = Build$VERSION.CODENAME.length();
        boolean b = true;
        if (length != 1 || Build$VERSION.CODENAME.charAt(0) < 'Q' || Build$VERSION.CODENAME.charAt(0) > 'Z') {
            b = false;
        }
        return b;
    }
}
