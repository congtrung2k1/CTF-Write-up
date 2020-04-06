// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.widget;

import java.lang.reflect.InvocationTargetException;
import androidx.core.view.ViewCompat;
import android.util.Log;
import android.graphics.Rect;
import android.view.View;
import android.os.Build$VERSION;
import java.lang.reflect.Method;

public class ViewUtils
{
    private static final String TAG = "ViewUtils";
    private static Method sComputeFitSystemWindowsMethod;
    
    static {
        if (Build$VERSION.SDK_INT >= 18) {
            try {
                if (!(ViewUtils.sComputeFitSystemWindowsMethod = View.class.getDeclaredMethod("computeFitSystemWindows", Rect.class, Rect.class)).isAccessible()) {
                    ViewUtils.sComputeFitSystemWindowsMethod.setAccessible(true);
                }
            }
            catch (NoSuchMethodException ex) {
                Log.d("ViewUtils", "Could not find method computeFitSystemWindows. Oh well.");
            }
        }
    }
    
    private ViewUtils() {
    }
    
    public static void computeFitSystemWindows(final View obj, final Rect rect, final Rect rect2) {
        final Method sComputeFitSystemWindowsMethod = ViewUtils.sComputeFitSystemWindowsMethod;
        if (sComputeFitSystemWindowsMethod != null) {
            try {
                sComputeFitSystemWindowsMethod.invoke(obj, rect, rect2);
            }
            catch (Exception ex) {
                Log.d("ViewUtils", "Could not invoke computeFitSystemWindows", (Throwable)ex);
            }
        }
    }
    
    public static boolean isLayoutRtl(final View view) {
        final int layoutDirection = ViewCompat.getLayoutDirection(view);
        boolean b = true;
        if (layoutDirection != 1) {
            b = false;
        }
        return b;
    }
    
    public static void makeOptionalFitsSystemWindows(final View obj) {
        if (Build$VERSION.SDK_INT >= 16) {
            try {
                final Method method = obj.getClass().getMethod("makeOptionalFitsSystemWindows", (Class<?>[])new Class[0]);
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                method.invoke(obj, new Object[0]);
            }
            catch (IllegalAccessException ex) {
                Log.d("ViewUtils", "Could not invoke makeOptionalFitsSystemWindows", (Throwable)ex);
            }
            catch (InvocationTargetException ex2) {
                Log.d("ViewUtils", "Could not invoke makeOptionalFitsSystemWindows", (Throwable)ex2);
            }
            catch (NoSuchMethodException ex3) {
                Log.d("ViewUtils", "Could not find method makeOptionalFitsSystemWindows. Oh well...");
            }
        }
    }
}
