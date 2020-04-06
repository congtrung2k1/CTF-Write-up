// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.view;

import android.content.res.Resources;
import android.util.TypedValue;
import android.content.Context;
import android.util.Log;
import android.view.ViewConfiguration;
import android.os.Build$VERSION;
import java.lang.reflect.Method;

public final class ViewConfigurationCompat
{
    private static final String TAG = "ViewConfigCompat";
    private static Method sGetScaledScrollFactorMethod;
    
    static {
        if (Build$VERSION.SDK_INT == 25) {
            try {
                ViewConfigurationCompat.sGetScaledScrollFactorMethod = ViewConfiguration.class.getDeclaredMethod("getScaledScrollFactor", (Class<?>[])new Class[0]);
            }
            catch (Exception ex) {
                Log.i("ViewConfigCompat", "Could not find method getScaledScrollFactor() on ViewConfiguration");
            }
        }
    }
    
    private ViewConfigurationCompat() {
    }
    
    private static float getLegacyScrollFactor(final ViewConfiguration obj, final Context context) {
        if (Build$VERSION.SDK_INT >= 25) {
            final Method sGetScaledScrollFactorMethod = ViewConfigurationCompat.sGetScaledScrollFactorMethod;
            if (sGetScaledScrollFactorMethod != null) {
                try {
                    return (float)(int)sGetScaledScrollFactorMethod.invoke(obj, new Object[0]);
                }
                catch (Exception ex) {
                    Log.i("ViewConfigCompat", "Could not find method getScaledScrollFactor() on ViewConfiguration");
                }
            }
        }
        final TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(16842829, typedValue, true)) {
            return typedValue.getDimension(context.getResources().getDisplayMetrics());
        }
        return 0.0f;
    }
    
    public static float getScaledHorizontalScrollFactor(final ViewConfiguration viewConfiguration, final Context context) {
        if (Build$VERSION.SDK_INT >= 26) {
            return viewConfiguration.getScaledHorizontalScrollFactor();
        }
        return getLegacyScrollFactor(viewConfiguration, context);
    }
    
    public static int getScaledHoverSlop(final ViewConfiguration viewConfiguration) {
        if (Build$VERSION.SDK_INT >= 28) {
            return viewConfiguration.getScaledHoverSlop();
        }
        return viewConfiguration.getScaledTouchSlop() / 2;
    }
    
    @Deprecated
    public static int getScaledPagingTouchSlop(final ViewConfiguration viewConfiguration) {
        return viewConfiguration.getScaledPagingTouchSlop();
    }
    
    public static float getScaledVerticalScrollFactor(final ViewConfiguration viewConfiguration, final Context context) {
        if (Build$VERSION.SDK_INT >= 26) {
            return viewConfiguration.getScaledVerticalScrollFactor();
        }
        return getLegacyScrollFactor(viewConfiguration, context);
    }
    
    @Deprecated
    public static boolean hasPermanentMenuKey(final ViewConfiguration viewConfiguration) {
        return viewConfiguration.hasPermanentMenuKey();
    }
    
    public static boolean shouldShowMenuShortcutsWhenKeyboardPresent(final ViewConfiguration viewConfiguration, final Context context) {
        if (Build$VERSION.SDK_INT >= 28) {
            return viewConfiguration.shouldShowMenuShortcutsWhenKeyboardPresent();
        }
        final Resources resources = context.getResources();
        final int identifier = resources.getIdentifier("config_showMenuShortcutsWhenKeyboardPresent", "bool", "android");
        return identifier != 0 && resources.getBoolean(identifier);
    }
}
