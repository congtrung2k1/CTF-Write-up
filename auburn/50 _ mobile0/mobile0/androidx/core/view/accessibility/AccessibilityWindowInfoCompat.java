// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.view.accessibility;

import android.graphics.Rect;
import android.view.accessibility.AccessibilityWindowInfo;
import android.os.Build$VERSION;

public class AccessibilityWindowInfoCompat
{
    public static final int TYPE_ACCESSIBILITY_OVERLAY = 4;
    public static final int TYPE_APPLICATION = 1;
    public static final int TYPE_INPUT_METHOD = 2;
    public static final int TYPE_SPLIT_SCREEN_DIVIDER = 5;
    public static final int TYPE_SYSTEM = 3;
    private static final int UNDEFINED = -1;
    private Object mInfo;
    
    private AccessibilityWindowInfoCompat(final Object mInfo) {
        this.mInfo = mInfo;
    }
    
    public static AccessibilityWindowInfoCompat obtain() {
        if (Build$VERSION.SDK_INT >= 21) {
            return wrapNonNullInstance(AccessibilityWindowInfo.obtain());
        }
        return null;
    }
    
    public static AccessibilityWindowInfoCompat obtain(AccessibilityWindowInfoCompat wrapNonNullInstance) {
        final int sdk_INT = Build$VERSION.SDK_INT;
        final AccessibilityWindowInfoCompat accessibilityWindowInfoCompat = null;
        if (sdk_INT >= 21) {
            if (wrapNonNullInstance == null) {
                wrapNonNullInstance = accessibilityWindowInfoCompat;
            }
            else {
                wrapNonNullInstance = wrapNonNullInstance(AccessibilityWindowInfo.obtain((AccessibilityWindowInfo)wrapNonNullInstance.mInfo));
            }
            return wrapNonNullInstance;
        }
        return null;
    }
    
    private static String typeToString(final int n) {
        if (n == 1) {
            return "TYPE_APPLICATION";
        }
        if (n == 2) {
            return "TYPE_INPUT_METHOD";
        }
        if (n == 3) {
            return "TYPE_SYSTEM";
        }
        if (n != 4) {
            return "<UNKNOWN>";
        }
        return "TYPE_ACCESSIBILITY_OVERLAY";
    }
    
    static AccessibilityWindowInfoCompat wrapNonNullInstance(final Object o) {
        if (o != null) {
            return new AccessibilityWindowInfoCompat(o);
        }
        return null;
    }
    
    @Override
    public boolean equals(Object mInfo) {
        if (this == mInfo) {
            return true;
        }
        if (mInfo == null) {
            return false;
        }
        if (this.getClass() != mInfo.getClass()) {
            return false;
        }
        final AccessibilityWindowInfoCompat accessibilityWindowInfoCompat = (AccessibilityWindowInfoCompat)mInfo;
        mInfo = this.mInfo;
        if (mInfo == null) {
            if (accessibilityWindowInfoCompat.mInfo != null) {
                return false;
            }
        }
        else if (!mInfo.equals(accessibilityWindowInfoCompat.mInfo)) {
            return false;
        }
        return true;
    }
    
    public AccessibilityNodeInfoCompat getAnchor() {
        if (Build$VERSION.SDK_INT >= 24) {
            return AccessibilityNodeInfoCompat.wrapNonNullInstance(((AccessibilityWindowInfo)this.mInfo).getAnchor());
        }
        return null;
    }
    
    public void getBoundsInScreen(final Rect rect) {
        if (Build$VERSION.SDK_INT >= 21) {
            ((AccessibilityWindowInfo)this.mInfo).getBoundsInScreen(rect);
        }
    }
    
    public AccessibilityWindowInfoCompat getChild(final int n) {
        if (Build$VERSION.SDK_INT >= 21) {
            return wrapNonNullInstance(((AccessibilityWindowInfo)this.mInfo).getChild(n));
        }
        return null;
    }
    
    public int getChildCount() {
        if (Build$VERSION.SDK_INT >= 21) {
            return ((AccessibilityWindowInfo)this.mInfo).getChildCount();
        }
        return 0;
    }
    
    public int getId() {
        if (Build$VERSION.SDK_INT >= 21) {
            return ((AccessibilityWindowInfo)this.mInfo).getId();
        }
        return -1;
    }
    
    public int getLayer() {
        if (Build$VERSION.SDK_INT >= 21) {
            return ((AccessibilityWindowInfo)this.mInfo).getLayer();
        }
        return -1;
    }
    
    public AccessibilityWindowInfoCompat getParent() {
        if (Build$VERSION.SDK_INT >= 21) {
            return wrapNonNullInstance(((AccessibilityWindowInfo)this.mInfo).getParent());
        }
        return null;
    }
    
    public AccessibilityNodeInfoCompat getRoot() {
        if (Build$VERSION.SDK_INT >= 21) {
            return AccessibilityNodeInfoCompat.wrapNonNullInstance(((AccessibilityWindowInfo)this.mInfo).getRoot());
        }
        return null;
    }
    
    public CharSequence getTitle() {
        if (Build$VERSION.SDK_INT >= 24) {
            return ((AccessibilityWindowInfo)this.mInfo).getTitle();
        }
        return null;
    }
    
    public int getType() {
        if (Build$VERSION.SDK_INT >= 21) {
            return ((AccessibilityWindowInfo)this.mInfo).getType();
        }
        return -1;
    }
    
    @Override
    public int hashCode() {
        final Object mInfo = this.mInfo;
        int hashCode;
        if (mInfo == null) {
            hashCode = 0;
        }
        else {
            hashCode = mInfo.hashCode();
        }
        return hashCode;
    }
    
    public boolean isAccessibilityFocused() {
        return Build$VERSION.SDK_INT < 21 || ((AccessibilityWindowInfo)this.mInfo).isAccessibilityFocused();
    }
    
    public boolean isActive() {
        return Build$VERSION.SDK_INT < 21 || ((AccessibilityWindowInfo)this.mInfo).isActive();
    }
    
    public boolean isFocused() {
        return Build$VERSION.SDK_INT < 21 || ((AccessibilityWindowInfo)this.mInfo).isFocused();
    }
    
    public void recycle() {
        if (Build$VERSION.SDK_INT >= 21) {
            ((AccessibilityWindowInfo)this.mInfo).recycle();
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        final Rect obj = new Rect();
        this.getBoundsInScreen(obj);
        sb.append("AccessibilityWindowInfo[");
        sb.append("id=");
        sb.append(this.getId());
        sb.append(", type=");
        sb.append(typeToString(this.getType()));
        sb.append(", layer=");
        sb.append(this.getLayer());
        sb.append(", bounds=");
        sb.append(obj);
        sb.append(", focused=");
        sb.append(this.isFocused());
        sb.append(", active=");
        sb.append(this.isActive());
        sb.append(", hasParent=");
        final AccessibilityWindowInfoCompat parent = this.getParent();
        final boolean b = true;
        sb.append(parent != null);
        sb.append(", hasChildren=");
        sb.append(this.getChildCount() > 0 && b);
        sb.append(']');
        return sb.toString();
    }
}
