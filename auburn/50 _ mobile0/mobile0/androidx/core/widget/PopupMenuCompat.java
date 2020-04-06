// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.widget;

import android.widget.PopupMenu;
import android.os.Build$VERSION;
import android.view.View$OnTouchListener;

public final class PopupMenuCompat
{
    private PopupMenuCompat() {
    }
    
    public static View$OnTouchListener getDragToOpenListener(final Object o) {
        if (Build$VERSION.SDK_INT >= 19) {
            return ((PopupMenu)o).getDragToOpenListener();
        }
        return null;
    }
}
