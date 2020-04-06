// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.graphics;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Array;
import android.graphics.Typeface;

public class TypefaceCompatApi28Impl extends TypefaceCompatApi26Impl
{
    private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
    private static final String DEFAULT_FAMILY = "sans-serif";
    private static final int RESOLVE_BY_FONT_TABLE = -1;
    private static final String TAG = "TypefaceCompatApi28Impl";
    
    @Override
    protected Typeface createFromFamiliesWithDefault(Object cause) {
        try {
            final Object instance = Array.newInstance(this.mFontFamily, 1);
            Array.set(instance, 0, cause);
            cause = (InvocationTargetException)this.mCreateFromFamiliesWithDefault.invoke(null, instance, "sans-serif", -1, -1);
            return (Typeface)cause;
        }
        catch (InvocationTargetException cause) {}
        catch (IllegalAccessException ex) {}
        throw new RuntimeException(cause);
    }
    
    @Override
    protected Method obtainCreateFromFamiliesWithDefaultMethod(final Class componentType) throws NoSuchMethodException {
        final Method declaredMethod = Typeface.class.getDeclaredMethod("createFromFamiliesWithDefault", Array.newInstance(componentType, 1).getClass(), String.class, Integer.TYPE, Integer.TYPE);
        declaredMethod.setAccessible(true);
        return declaredMethod;
    }
}
