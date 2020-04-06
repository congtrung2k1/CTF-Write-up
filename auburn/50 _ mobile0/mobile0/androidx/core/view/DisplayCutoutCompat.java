// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.view;

import android.view.DisplayCutout;
import android.os.Build$VERSION;
import java.util.List;
import android.graphics.Rect;

public final class DisplayCutoutCompat
{
    private final Object mDisplayCutout;
    
    public DisplayCutoutCompat(final Rect rect, final List<Rect> list) {
        DisplayCutout displayCutout;
        if (Build$VERSION.SDK_INT >= 28) {
            displayCutout = new DisplayCutout(rect, (List)list);
        }
        else {
            displayCutout = null;
        }
        this(displayCutout);
    }
    
    private DisplayCutoutCompat(final Object mDisplayCutout) {
        this.mDisplayCutout = mDisplayCutout;
    }
    
    static DisplayCutoutCompat wrap(final Object o) {
        DisplayCutoutCompat displayCutoutCompat;
        if (o == null) {
            displayCutoutCompat = null;
        }
        else {
            displayCutoutCompat = new DisplayCutoutCompat(o);
        }
        return displayCutoutCompat;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean equals = true;
        if (this == o) {
            return true;
        }
        if (o != null && this.getClass() == o.getClass()) {
            final DisplayCutoutCompat displayCutoutCompat = (DisplayCutoutCompat)o;
            final Object mDisplayCutout = this.mDisplayCutout;
            if (mDisplayCutout == null) {
                if (displayCutoutCompat.mDisplayCutout != null) {
                    equals = false;
                }
            }
            else {
                equals = mDisplayCutout.equals(displayCutoutCompat.mDisplayCutout);
            }
            return equals;
        }
        return false;
    }
    
    public List<Rect> getBoundingRects() {
        if (Build$VERSION.SDK_INT >= 28) {
            return (List<Rect>)((DisplayCutout)this.mDisplayCutout).getBoundingRects();
        }
        return null;
    }
    
    public int getSafeInsetBottom() {
        if (Build$VERSION.SDK_INT >= 28) {
            return ((DisplayCutout)this.mDisplayCutout).getSafeInsetBottom();
        }
        return 0;
    }
    
    public int getSafeInsetLeft() {
        if (Build$VERSION.SDK_INT >= 28) {
            return ((DisplayCutout)this.mDisplayCutout).getSafeInsetLeft();
        }
        return 0;
    }
    
    public int getSafeInsetRight() {
        if (Build$VERSION.SDK_INT >= 28) {
            return ((DisplayCutout)this.mDisplayCutout).getSafeInsetRight();
        }
        return 0;
    }
    
    public int getSafeInsetTop() {
        if (Build$VERSION.SDK_INT >= 28) {
            return ((DisplayCutout)this.mDisplayCutout).getSafeInsetTop();
        }
        return 0;
    }
    
    @Override
    public int hashCode() {
        final Object mDisplayCutout = this.mDisplayCutout;
        int hashCode;
        if (mDisplayCutout == null) {
            hashCode = 0;
        }
        else {
            hashCode = mDisplayCutout.hashCode();
        }
        return hashCode;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DisplayCutoutCompat{");
        sb.append(this.mDisplayCutout);
        sb.append("}");
        return sb.toString();
    }
}
