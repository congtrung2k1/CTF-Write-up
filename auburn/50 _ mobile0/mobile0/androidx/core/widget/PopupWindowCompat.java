// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.widget;

import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import android.view.View;
import android.util.Log;
import android.os.Build$VERSION;
import android.widget.PopupWindow;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class PopupWindowCompat
{
    private static final String TAG = "PopupWindowCompatApi21";
    private static Method sGetWindowLayoutTypeMethod;
    private static boolean sGetWindowLayoutTypeMethodAttempted;
    private static Field sOverlapAnchorField;
    private static boolean sOverlapAnchorFieldAttempted;
    private static Method sSetWindowLayoutTypeMethod;
    private static boolean sSetWindowLayoutTypeMethodAttempted;
    
    private PopupWindowCompat() {
    }
    
    public static boolean getOverlapAnchor(final PopupWindow obj) {
        if (Build$VERSION.SDK_INT >= 23) {
            return obj.getOverlapAnchor();
        }
        if (Build$VERSION.SDK_INT >= 21) {
            if (!PopupWindowCompat.sOverlapAnchorFieldAttempted) {
                try {
                    (PopupWindowCompat.sOverlapAnchorField = PopupWindow.class.getDeclaredField("mOverlapAnchor")).setAccessible(true);
                }
                catch (NoSuchFieldException ex) {
                    Log.i("PopupWindowCompatApi21", "Could not fetch mOverlapAnchor field from PopupWindow", (Throwable)ex);
                }
                PopupWindowCompat.sOverlapAnchorFieldAttempted = true;
            }
            final Field sOverlapAnchorField = PopupWindowCompat.sOverlapAnchorField;
            if (sOverlapAnchorField != null) {
                try {
                    return (boolean)sOverlapAnchorField.get(obj);
                }
                catch (IllegalAccessException ex2) {
                    Log.i("PopupWindowCompatApi21", "Could not get overlap anchor field in PopupWindow", (Throwable)ex2);
                }
            }
        }
        return false;
    }
    
    public static int getWindowLayoutType(final PopupWindow obj) {
        if (Build$VERSION.SDK_INT >= 23) {
            return obj.getWindowLayoutType();
        }
        if (!PopupWindowCompat.sGetWindowLayoutTypeMethodAttempted) {
            try {
                (PopupWindowCompat.sGetWindowLayoutTypeMethod = PopupWindow.class.getDeclaredMethod("getWindowLayoutType", (Class<?>[])new Class[0])).setAccessible(true);
            }
            catch (Exception ex) {}
            PopupWindowCompat.sGetWindowLayoutTypeMethodAttempted = true;
        }
        final Method sGetWindowLayoutTypeMethod = PopupWindowCompat.sGetWindowLayoutTypeMethod;
        if (sGetWindowLayoutTypeMethod != null) {
            try {
                return (int)sGetWindowLayoutTypeMethod.invoke(obj, new Object[0]);
            }
            catch (Exception ex2) {}
        }
        return 0;
    }
    
    public static void setOverlapAnchor(final PopupWindow obj, final boolean b) {
        if (Build$VERSION.SDK_INT >= 23) {
            obj.setOverlapAnchor(b);
        }
        else if (Build$VERSION.SDK_INT >= 21) {
            if (!PopupWindowCompat.sOverlapAnchorFieldAttempted) {
                try {
                    (PopupWindowCompat.sOverlapAnchorField = PopupWindow.class.getDeclaredField("mOverlapAnchor")).setAccessible(true);
                }
                catch (NoSuchFieldException ex) {
                    Log.i("PopupWindowCompatApi21", "Could not fetch mOverlapAnchor field from PopupWindow", (Throwable)ex);
                }
                PopupWindowCompat.sOverlapAnchorFieldAttempted = true;
            }
            final Field sOverlapAnchorField = PopupWindowCompat.sOverlapAnchorField;
            if (sOverlapAnchorField != null) {
                try {
                    sOverlapAnchorField.set(obj, b);
                }
                catch (IllegalAccessException ex2) {
                    Log.i("PopupWindowCompatApi21", "Could not set overlap anchor field in PopupWindow", (Throwable)ex2);
                }
            }
        }
    }
    
    public static void setWindowLayoutType(final PopupWindow obj, final int n) {
        if (Build$VERSION.SDK_INT >= 23) {
            obj.setWindowLayoutType(n);
            return;
        }
        if (!PopupWindowCompat.sSetWindowLayoutTypeMethodAttempted) {
            try {
                (PopupWindowCompat.sSetWindowLayoutTypeMethod = PopupWindow.class.getDeclaredMethod("setWindowLayoutType", Integer.TYPE)).setAccessible(true);
            }
            catch (Exception ex) {}
            PopupWindowCompat.sSetWindowLayoutTypeMethodAttempted = true;
        }
        final Method sSetWindowLayoutTypeMethod = PopupWindowCompat.sSetWindowLayoutTypeMethod;
        if (sSetWindowLayoutTypeMethod != null) {
            try {
                sSetWindowLayoutTypeMethod.invoke(obj, n);
            }
            catch (Exception ex2) {}
        }
    }
    
    public static void showAsDropDown(final PopupWindow popupWindow, final View view, final int n, final int n2, final int n3) {
        if (Build$VERSION.SDK_INT >= 19) {
            popupWindow.showAsDropDown(view, n, n2, n3);
        }
        else {
            int n4 = n;
            if ((GravityCompat.getAbsoluteGravity(n3, ViewCompat.getLayoutDirection(view)) & 0x7) == 0x5) {
                n4 = n - (popupWindow.getWidth() - view.getWidth());
            }
            popupWindow.showAsDropDown(view, n4, n2);
        }
    }
}
