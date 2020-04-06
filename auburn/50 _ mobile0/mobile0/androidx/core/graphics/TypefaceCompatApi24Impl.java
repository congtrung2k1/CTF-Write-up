// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.graphics;

import android.net.Uri;
import androidx.collection.SimpleArrayMap;
import androidx.core.provider.FontsContractCompat;
import android.os.CancellationSignal;
import android.content.res.Resources;
import androidx.core.content.res.FontResourcesParserCompat;
import android.content.Context;
import java.lang.reflect.InvocationTargetException;
import android.util.Log;
import java.lang.reflect.Array;
import android.graphics.Typeface;
import java.util.List;
import java.nio.ByteBuffer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

class TypefaceCompatApi24Impl extends TypefaceCompatBaseImpl
{
    private static final String ADD_FONT_WEIGHT_STYLE_METHOD = "addFontWeightStyle";
    private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
    private static final String FONT_FAMILY_CLASS = "android.graphics.FontFamily";
    private static final String TAG = "TypefaceCompatApi24Impl";
    private static final Method sAddFontWeightStyle;
    private static final Method sCreateFromFamiliesWithDefault;
    private static final Class sFontFamily;
    private static final Constructor sFontFamilyCtor;
    
    static {
        try {
            final Class<?> forName = Class.forName("android.graphics.FontFamily");
            try {
                final Constructor<?> constructor = forName.getConstructor((Class<?>[])new Class[0]);
                try {
                    final Method method = forName.getMethod("addFontWeightStyle", ByteBuffer.class, Integer.TYPE, List.class, Integer.TYPE, Boolean.TYPE);
                    try {
                        final Method method2 = Typeface.class.getMethod("createFromFamiliesWithDefault", Array.newInstance(forName, 1).getClass());
                    }
                    catch (NoSuchMethodException ex) {}
                    catch (ClassNotFoundException ex) {}
                }
                catch (NoSuchMethodException ex) {}
                catch (ClassNotFoundException ex2) {}
            }
            catch (NoSuchMethodException ex) {}
            catch (ClassNotFoundException ex) {}
        }
        catch (NoSuchMethodException ex) {}
        catch (ClassNotFoundException ex3) {}
        final NoSuchMethodException ex;
        Log.e("TypefaceCompatApi24Impl", ex.getClass().getName(), (Throwable)ex);
        final Class<?> forName = null;
        final Constructor<?> constructor = null;
        final Method method = null;
        final Method method2 = null;
        sFontFamilyCtor = constructor;
        sFontFamily = forName;
        sAddFontWeightStyle = method;
        sCreateFromFamiliesWithDefault = method2;
    }
    
    private static boolean addFontWeightStyle(final Object ex, final ByteBuffer byteBuffer, final int i, final int j, final boolean b) {
        try {
            return (boolean)TypefaceCompatApi24Impl.sAddFontWeightStyle.invoke(ex, byteBuffer, i, null, j, b);
        }
        catch (InvocationTargetException ex) {}
        catch (IllegalAccessException ex2) {}
        throw new RuntimeException(ex);
    }
    
    private static Typeface createFromFamiliesWithDefault(Object cause) {
        try {
            final Object instance = Array.newInstance(TypefaceCompatApi24Impl.sFontFamily, 1);
            Array.set(instance, 0, cause);
            cause = (InvocationTargetException)TypefaceCompatApi24Impl.sCreateFromFamiliesWithDefault.invoke(null, instance);
            return (Typeface)cause;
        }
        catch (InvocationTargetException cause) {}
        catch (IllegalAccessException ex) {}
        throw new RuntimeException(cause);
    }
    
    public static boolean isUsable() {
        if (TypefaceCompatApi24Impl.sAddFontWeightStyle == null) {
            Log.w("TypefaceCompatApi24Impl", "Unable to collect necessary private methods.Fallback to legacy implementation.");
        }
        return TypefaceCompatApi24Impl.sAddFontWeightStyle != null;
    }
    
    private static Object newFamily() {
        InvocationTargetException instance = null;
        try {
            instance = TypefaceCompatApi24Impl.sFontFamilyCtor.newInstance(new Object[0]);
            return instance;
        }
        catch (InvocationTargetException instance) {}
        catch (InstantiationException instance) {}
        catch (IllegalAccessException ex) {}
        throw new RuntimeException(instance);
    }
    
    @Override
    public Typeface createFromFontFamilyFilesResourceEntry(final Context context, final FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, final Resources resources, int i) {
        final Object family = newFamily();
        final FontResourcesParserCompat.FontFileResourceEntry[] entries = fontFamilyFilesResourceEntry.getEntries();
        int length;
        FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry;
        ByteBuffer copyToDirectBuffer;
        for (length = entries.length, i = 0; i < length; ++i) {
            fontFileResourceEntry = entries[i];
            copyToDirectBuffer = TypefaceCompatUtil.copyToDirectBuffer(context, resources, fontFileResourceEntry.getResourceId());
            if (copyToDirectBuffer == null) {
                return null;
            }
            if (!addFontWeightStyle(family, copyToDirectBuffer, fontFileResourceEntry.getTtcIndex(), fontFileResourceEntry.getWeight(), fontFileResourceEntry.isItalic())) {
                return null;
            }
        }
        return createFromFamiliesWithDefault(family);
    }
    
    @Override
    public Typeface createFromFontInfo(final Context context, final CancellationSignal cancellationSignal, final FontsContractCompat.FontInfo[] array, final int n) {
        final Object family = newFamily();
        final SimpleArrayMap<Uri, ByteBuffer> simpleArrayMap = new SimpleArrayMap<Uri, ByteBuffer>();
        for (final FontsContractCompat.FontInfo fontInfo : array) {
            final Uri uri = fontInfo.getUri();
            ByteBuffer mmap;
            if ((mmap = simpleArrayMap.get(uri)) == null) {
                mmap = TypefaceCompatUtil.mmap(context, cancellationSignal, uri);
                simpleArrayMap.put(uri, mmap);
            }
            if (!addFontWeightStyle(family, mmap, fontInfo.getTtcIndex(), fontInfo.getWeight(), fontInfo.isItalic())) {
                return null;
            }
        }
        return Typeface.create(createFromFamiliesWithDefault(family), n);
    }
}
