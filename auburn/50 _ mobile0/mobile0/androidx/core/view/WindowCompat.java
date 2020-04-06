// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.view;

import android.os.Build$VERSION;
import android.view.View;
import android.view.Window;

public final class WindowCompat
{
    public static final int FEATURE_ACTION_BAR = 8;
    public static final int FEATURE_ACTION_BAR_OVERLAY = 9;
    public static final int FEATURE_ACTION_MODE_OVERLAY = 10;
    
    private WindowCompat() {
    }
    
    public static <T extends View> T requireViewById(final Window window, final int n) {
        if (Build$VERSION.SDK_INT >= 28) {
            return (T)window.requireViewById(n);
        }
        final View viewById = window.findViewById(n);
        if (viewById != null) {
            return (T)viewById;
        }
        throw new IllegalArgumentException("ID does not reference a View inside this Window");
    }
}
