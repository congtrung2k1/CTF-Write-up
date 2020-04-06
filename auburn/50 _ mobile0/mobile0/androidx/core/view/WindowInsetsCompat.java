// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.view;

import android.graphics.Rect;
import android.view.WindowInsets;
import android.os.Build$VERSION;

public class WindowInsetsCompat
{
    private final Object mInsets;
    
    public WindowInsetsCompat(final WindowInsetsCompat windowInsetsCompat) {
        final int sdk_INT = Build$VERSION.SDK_INT;
        final Object o = null;
        if (sdk_INT >= 20) {
            Object mInsets;
            if (windowInsetsCompat == null) {
                mInsets = o;
            }
            else {
                mInsets = new WindowInsets((WindowInsets)windowInsetsCompat.mInsets);
            }
            this.mInsets = mInsets;
        }
        else {
            this.mInsets = null;
        }
    }
    
    private WindowInsetsCompat(final Object mInsets) {
        this.mInsets = mInsets;
    }
    
    static Object unwrap(final WindowInsetsCompat windowInsetsCompat) {
        Object mInsets;
        if (windowInsetsCompat == null) {
            mInsets = null;
        }
        else {
            mInsets = windowInsetsCompat.mInsets;
        }
        return mInsets;
    }
    
    static WindowInsetsCompat wrap(final Object o) {
        WindowInsetsCompat windowInsetsCompat;
        if (o == null) {
            windowInsetsCompat = null;
        }
        else {
            windowInsetsCompat = new WindowInsetsCompat(o);
        }
        return windowInsetsCompat;
    }
    
    public WindowInsetsCompat consumeDisplayCutout() {
        if (Build$VERSION.SDK_INT >= 28) {
            return new WindowInsetsCompat(((WindowInsets)this.mInsets).consumeDisplayCutout());
        }
        return this;
    }
    
    public WindowInsetsCompat consumeStableInsets() {
        if (Build$VERSION.SDK_INT >= 21) {
            return new WindowInsetsCompat(((WindowInsets)this.mInsets).consumeStableInsets());
        }
        return null;
    }
    
    public WindowInsetsCompat consumeSystemWindowInsets() {
        if (Build$VERSION.SDK_INT >= 20) {
            return new WindowInsetsCompat(((WindowInsets)this.mInsets).consumeSystemWindowInsets());
        }
        return null;
    }
    
    @Override
    public boolean equals(Object mInsets) {
        boolean equals = true;
        if (this == mInsets) {
            return true;
        }
        if (mInsets != null && this.getClass() == mInsets.getClass()) {
            final WindowInsetsCompat windowInsetsCompat = (WindowInsetsCompat)mInsets;
            mInsets = this.mInsets;
            if (mInsets == null) {
                if (windowInsetsCompat.mInsets != null) {
                    equals = false;
                }
            }
            else {
                equals = mInsets.equals(windowInsetsCompat.mInsets);
            }
            return equals;
        }
        return false;
    }
    
    public DisplayCutoutCompat getDisplayCutout() {
        if (Build$VERSION.SDK_INT >= 28) {
            return DisplayCutoutCompat.wrap(((WindowInsets)this.mInsets).getDisplayCutout());
        }
        return null;
    }
    
    public int getStableInsetBottom() {
        if (Build$VERSION.SDK_INT >= 21) {
            return ((WindowInsets)this.mInsets).getStableInsetBottom();
        }
        return 0;
    }
    
    public int getStableInsetLeft() {
        if (Build$VERSION.SDK_INT >= 21) {
            return ((WindowInsets)this.mInsets).getStableInsetLeft();
        }
        return 0;
    }
    
    public int getStableInsetRight() {
        if (Build$VERSION.SDK_INT >= 21) {
            return ((WindowInsets)this.mInsets).getStableInsetRight();
        }
        return 0;
    }
    
    public int getStableInsetTop() {
        if (Build$VERSION.SDK_INT >= 21) {
            return ((WindowInsets)this.mInsets).getStableInsetTop();
        }
        return 0;
    }
    
    public int getSystemWindowInsetBottom() {
        if (Build$VERSION.SDK_INT >= 20) {
            return ((WindowInsets)this.mInsets).getSystemWindowInsetBottom();
        }
        return 0;
    }
    
    public int getSystemWindowInsetLeft() {
        if (Build$VERSION.SDK_INT >= 20) {
            return ((WindowInsets)this.mInsets).getSystemWindowInsetLeft();
        }
        return 0;
    }
    
    public int getSystemWindowInsetRight() {
        if (Build$VERSION.SDK_INT >= 20) {
            return ((WindowInsets)this.mInsets).getSystemWindowInsetRight();
        }
        return 0;
    }
    
    public int getSystemWindowInsetTop() {
        if (Build$VERSION.SDK_INT >= 20) {
            return ((WindowInsets)this.mInsets).getSystemWindowInsetTop();
        }
        return 0;
    }
    
    public boolean hasInsets() {
        return Build$VERSION.SDK_INT >= 20 && ((WindowInsets)this.mInsets).hasInsets();
    }
    
    public boolean hasStableInsets() {
        return Build$VERSION.SDK_INT >= 21 && ((WindowInsets)this.mInsets).hasStableInsets();
    }
    
    public boolean hasSystemWindowInsets() {
        return Build$VERSION.SDK_INT >= 20 && ((WindowInsets)this.mInsets).hasSystemWindowInsets();
    }
    
    @Override
    public int hashCode() {
        final Object mInsets = this.mInsets;
        int hashCode;
        if (mInsets == null) {
            hashCode = 0;
        }
        else {
            hashCode = mInsets.hashCode();
        }
        return hashCode;
    }
    
    public boolean isConsumed() {
        return Build$VERSION.SDK_INT >= 21 && ((WindowInsets)this.mInsets).isConsumed();
    }
    
    public boolean isRound() {
        return Build$VERSION.SDK_INT >= 20 && ((WindowInsets)this.mInsets).isRound();
    }
    
    public WindowInsetsCompat replaceSystemWindowInsets(final int n, final int n2, final int n3, final int n4) {
        if (Build$VERSION.SDK_INT >= 20) {
            return new WindowInsetsCompat(((WindowInsets)this.mInsets).replaceSystemWindowInsets(n, n2, n3, n4));
        }
        return null;
    }
    
    public WindowInsetsCompat replaceSystemWindowInsets(final Rect rect) {
        if (Build$VERSION.SDK_INT >= 21) {
            return new WindowInsetsCompat(((WindowInsets)this.mInsets).replaceSystemWindowInsets(rect));
        }
        return null;
    }
}
