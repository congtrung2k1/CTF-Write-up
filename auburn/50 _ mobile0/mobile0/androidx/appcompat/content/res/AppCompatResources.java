// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.content.res;

import android.content.res.Configuration;
import android.content.res.XmlResourceParser;
import android.content.res.Resources;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import androidx.core.content.res.ColorStateListInflaterCompat;
import androidx.appcompat.widget.AppCompatDrawableManager;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.os.Build$VERSION;
import android.content.res.ColorStateList;
import android.util.SparseArray;
import android.content.Context;
import java.util.WeakHashMap;
import android.util.TypedValue;

public final class AppCompatResources
{
    private static final String LOG_TAG = "AppCompatResources";
    private static final ThreadLocal<TypedValue> TL_TYPED_VALUE;
    private static final Object sColorStateCacheLock;
    private static final WeakHashMap<Context, SparseArray<ColorStateListCacheEntry>> sColorStateCaches;
    
    static {
        TL_TYPED_VALUE = new ThreadLocal<TypedValue>();
        sColorStateCaches = new WeakHashMap<Context, SparseArray<ColorStateListCacheEntry>>(0);
        sColorStateCacheLock = new Object();
    }
    
    private AppCompatResources() {
    }
    
    private static void addColorStateListToCache(final Context context, final int n, final ColorStateList list) {
        synchronized (AppCompatResources.sColorStateCacheLock) {
            SparseArray value;
            if ((value = AppCompatResources.sColorStateCaches.get(context)) == null) {
                value = new SparseArray();
                AppCompatResources.sColorStateCaches.put(context, (SparseArray<ColorStateListCacheEntry>)value);
            }
            value.append(n, (Object)new ColorStateListCacheEntry(list, context.getResources().getConfiguration()));
        }
    }
    
    private static ColorStateList getCachedColorStateList(final Context key, final int n) {
        synchronized (AppCompatResources.sColorStateCacheLock) {
            final SparseArray<ColorStateListCacheEntry> sparseArray = AppCompatResources.sColorStateCaches.get(key);
            if (sparseArray != null && sparseArray.size() > 0) {
                final ColorStateListCacheEntry colorStateListCacheEntry = (ColorStateListCacheEntry)sparseArray.get(n);
                if (colorStateListCacheEntry != null) {
                    if (colorStateListCacheEntry.configuration.equals(key.getResources().getConfiguration())) {
                        return colorStateListCacheEntry.value;
                    }
                    sparseArray.remove(n);
                }
            }
            return null;
        }
    }
    
    public static ColorStateList getColorStateList(final Context context, final int n) {
        if (Build$VERSION.SDK_INT >= 23) {
            return context.getColorStateList(n);
        }
        final ColorStateList cachedColorStateList = getCachedColorStateList(context, n);
        if (cachedColorStateList != null) {
            return cachedColorStateList;
        }
        final ColorStateList inflateColorStateList = inflateColorStateList(context, n);
        if (inflateColorStateList != null) {
            addColorStateListToCache(context, n, inflateColorStateList);
            return inflateColorStateList;
        }
        return ContextCompat.getColorStateList(context, n);
    }
    
    public static Drawable getDrawable(final Context context, final int n) {
        return AppCompatDrawableManager.get().getDrawable(context, n);
    }
    
    private static TypedValue getTypedValue() {
        TypedValue value;
        if ((value = AppCompatResources.TL_TYPED_VALUE.get()) == null) {
            value = new TypedValue();
            AppCompatResources.TL_TYPED_VALUE.set(value);
        }
        return value;
    }
    
    private static ColorStateList inflateColorStateList(final Context context, final int n) {
        if (isColorInt(context, n)) {
            return null;
        }
        final Resources resources = context.getResources();
        final XmlResourceParser xml = resources.getXml(n);
        try {
            return ColorStateListInflaterCompat.createFromXml(resources, (XmlPullParser)xml, context.getTheme());
        }
        catch (Exception ex) {
            Log.e("AppCompatResources", "Failed to inflate ColorStateList, leaving it to the framework", (Throwable)ex);
            return null;
        }
    }
    
    private static boolean isColorInt(final Context context, final int n) {
        final Resources resources = context.getResources();
        final TypedValue typedValue = getTypedValue();
        boolean b = true;
        resources.getValue(n, typedValue, true);
        if (typedValue.type < 28 || typedValue.type > 31) {
            b = false;
        }
        return b;
    }
    
    private static class ColorStateListCacheEntry
    {
        final Configuration configuration;
        final ColorStateList value;
        
        ColorStateListCacheEntry(final ColorStateList value, final Configuration configuration) {
            this.value = value;
            this.configuration = configuration;
        }
    }
}
