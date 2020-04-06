// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.graphics;

import android.os.ParcelFileDescriptor;
import android.content.res.AssetManager;
import android.net.Uri;
import java.util.Map;
import android.content.ContentResolver;
import java.io.IOException;
import android.graphics.Typeface$Builder;
import androidx.core.provider.FontsContractCompat;
import android.os.CancellationSignal;
import android.content.res.Resources;
import androidx.core.content.res.FontResourcesParserCompat;
import java.lang.reflect.Array;
import android.graphics.Typeface;
import java.nio.ByteBuffer;
import android.graphics.fonts.FontVariationAxis;
import android.content.Context;
import java.lang.reflect.InvocationTargetException;
import android.util.Log;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TypefaceCompatApi26Impl extends TypefaceCompatApi21Impl
{
    private static final String ABORT_CREATION_METHOD = "abortCreation";
    private static final String ADD_FONT_FROM_ASSET_MANAGER_METHOD = "addFontFromAssetManager";
    private static final String ADD_FONT_FROM_BUFFER_METHOD = "addFontFromBuffer";
    private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
    private static final String DEFAULT_FAMILY = "sans-serif";
    private static final String FONT_FAMILY_CLASS = "android.graphics.FontFamily";
    private static final String FREEZE_METHOD = "freeze";
    private static final int RESOLVE_BY_FONT_TABLE = -1;
    private static final String TAG = "TypefaceCompatApi26Impl";
    protected final Method mAbortCreation;
    protected final Method mAddFontFromAssetManager;
    protected final Method mAddFontFromBuffer;
    protected final Method mCreateFromFamiliesWithDefault;
    protected final Class mFontFamily;
    protected final Constructor mFontFamilyCtor;
    protected final Method mFreeze;
    
    public TypefaceCompatApi26Impl() {
        Class obtainFontFamily = null;
        Constructor obtainFontFamilyCtor = null;
        Method obtainAddFontFromAssetManagerMethod = null;
        Method obtainAddFontFromBufferMethod = null;
        Method obtainFreezeMethod = null;
        Method obtainAbortCreationMethod = null;
        Method obtainCreateFromFamiliesWithDefaultMethod = null;
        Label_0113: {
            try {
                obtainFontFamily = this.obtainFontFamily();
                obtainFontFamilyCtor = this.obtainFontFamilyCtor(obtainFontFamily);
                obtainAddFontFromAssetManagerMethod = this.obtainAddFontFromAssetManagerMethod(obtainFontFamily);
                obtainAddFontFromBufferMethod = this.obtainAddFontFromBufferMethod(obtainFontFamily);
                obtainFreezeMethod = this.obtainFreezeMethod(obtainFontFamily);
                obtainAbortCreationMethod = this.obtainAbortCreationMethod(obtainFontFamily);
                obtainCreateFromFamiliesWithDefaultMethod = this.obtainCreateFromFamiliesWithDefaultMethod(obtainFontFamily);
                break Label_0113;
            }
            catch (NoSuchMethodException obtainFontFamily) {}
            catch (ClassNotFoundException ex) {}
            final StringBuilder sb = new StringBuilder();
            sb.append("Unable to collect necessary methods for class ");
            sb.append(obtainFontFamily.getClass().getName());
            Log.e("TypefaceCompatApi26Impl", sb.toString(), (Throwable)obtainFontFamily);
            obtainFontFamily = null;
            obtainFontFamilyCtor = null;
            obtainAddFontFromAssetManagerMethod = null;
            obtainAddFontFromBufferMethod = null;
            obtainFreezeMethod = null;
            obtainAbortCreationMethod = null;
            obtainCreateFromFamiliesWithDefaultMethod = null;
        }
        this.mFontFamily = obtainFontFamily;
        this.mFontFamilyCtor = obtainFontFamilyCtor;
        this.mAddFontFromAssetManager = obtainAddFontFromAssetManagerMethod;
        this.mAddFontFromBuffer = obtainAddFontFromBufferMethod;
        this.mFreeze = obtainFreezeMethod;
        this.mAbortCreation = obtainAbortCreationMethod;
        this.mCreateFromFamiliesWithDefault = obtainCreateFromFamiliesWithDefaultMethod;
    }
    
    private void abortCreation(final Object ex) {
        try {
            this.mAbortCreation.invoke(ex, new Object[0]);
            return;
        }
        catch (InvocationTargetException ex) {}
        catch (IllegalAccessException ex2) {}
        throw new RuntimeException(ex);
    }
    
    private boolean addFontFromAssetManager(final Context cause, final Object obj, final String s, final int i, final int j, final int k, final FontVariationAxis[] array) {
        try {
            return (boolean)this.mAddFontFromAssetManager.invoke(obj, ((Context)cause).getAssets(), s, 0, false, i, j, k, array);
        }
        catch (InvocationTargetException cause) {}
        catch (IllegalAccessException ex) {}
        throw new RuntimeException(cause);
    }
    
    private boolean addFontFromBuffer(final Object ex, final ByteBuffer byteBuffer, final int i, final int j, final int k) {
        try {
            return (boolean)this.mAddFontFromBuffer.invoke(ex, byteBuffer, i, null, j, k);
        }
        catch (InvocationTargetException ex) {}
        catch (IllegalAccessException ex2) {}
        throw new RuntimeException(ex);
    }
    
    private boolean freeze(final Object ex) {
        try {
            return (boolean)this.mFreeze.invoke(ex, new Object[0]);
        }
        catch (InvocationTargetException ex) {}
        catch (IllegalAccessException ex2) {}
        throw new RuntimeException(ex);
    }
    
    private boolean isFontFamilyPrivateAPIAvailable() {
        if (this.mAddFontFromAssetManager == null) {
            Log.w("TypefaceCompatApi26Impl", "Unable to collect necessary private methods. Fallback to legacy implementation.");
        }
        return this.mAddFontFromAssetManager != null;
    }
    
    private Object newFamily() {
        InvocationTargetException instance = null;
        try {
            instance = this.mFontFamilyCtor.newInstance(new Object[0]);
            return instance;
        }
        catch (InvocationTargetException instance) {}
        catch (InstantiationException instance) {}
        catch (IllegalAccessException ex) {}
        throw new RuntimeException(instance);
    }
    
    protected Typeface createFromFamiliesWithDefault(Object cause) {
        try {
            final Object instance = Array.newInstance(this.mFontFamily, 1);
            Array.set(instance, 0, cause);
            cause = (InvocationTargetException)this.mCreateFromFamiliesWithDefault.invoke(null, instance, -1, -1);
            return (Typeface)cause;
        }
        catch (InvocationTargetException cause) {}
        catch (IllegalAccessException ex) {}
        throw new RuntimeException(cause);
    }
    
    @Override
    public Typeface createFromFontFamilyFilesResourceEntry(final Context context, final FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, final Resources resources, int i) {
        if (!this.isFontFamilyPrivateAPIAvailable()) {
            return super.createFromFontFamilyFilesResourceEntry(context, fontFamilyFilesResourceEntry, resources, i);
        }
        final Object family = this.newFamily();
        final FontResourcesParserCompat.FontFileResourceEntry[] entries = fontFamilyFilesResourceEntry.getEntries();
        int length;
        FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry;
        for (length = entries.length, i = 0; i < length; ++i) {
            fontFileResourceEntry = entries[i];
            if (!this.addFontFromAssetManager(context, family, fontFileResourceEntry.getFileName(), fontFileResourceEntry.getTtcIndex(), fontFileResourceEntry.getWeight(), fontFileResourceEntry.isItalic() ? 1 : 0, FontVariationAxis.fromFontVariationSettings(fontFileResourceEntry.getVariationSettings()))) {
                this.abortCreation(family);
                return null;
            }
        }
        if (!this.freeze(family)) {
            return null;
        }
        return this.createFromFamiliesWithDefault(family);
    }
    
    @Override
    public Typeface createFromFontInfo(Context openFileDescriptor, final CancellationSignal cancellationSignal, final FontsContractCompat.FontInfo[] array, final int n) {
        if (array.length < 1) {
            return null;
        }
        if (!this.isFontFamilyPrivateAPIAvailable()) {
            final FontsContractCompat.FontInfo bestInfo = this.findBestInfo(array, n);
            final ContentResolver contentResolver = openFileDescriptor.getContentResolver();
            try {
                openFileDescriptor = (Context)contentResolver.openFileDescriptor(bestInfo.getUri(), "r", cancellationSignal);
                if (openFileDescriptor == null) {
                    if (openFileDescriptor != null) {
                        ((ParcelFileDescriptor)openFileDescriptor).close();
                    }
                    return null;
                }
                try {
                    final Typeface build = new Typeface$Builder(((ParcelFileDescriptor)openFileDescriptor).getFileDescriptor()).setWeight(bestInfo.getWeight()).setItalic(bestInfo.isItalic()).build();
                    if (openFileDescriptor != null) {
                        ((ParcelFileDescriptor)openFileDescriptor).close();
                    }
                    return build;
                }
                finally {
                    try {}
                    finally {
                        if (openFileDescriptor != null) {
                            try {
                                ((ParcelFileDescriptor)openFileDescriptor).close();
                            }
                            finally {
                                final Throwable exception;
                                ((Throwable)(Object)array).addSuppressed(exception);
                            }
                        }
                    }
                }
            }
            catch (IOException ex) {
                return null;
            }
        }
        final Map<Uri, ByteBuffer> prepareFontData = FontsContractCompat.prepareFontData(openFileDescriptor, array, cancellationSignal);
        final Object family = this.newFamily();
        final int length = array.length;
        boolean b = false;
        for (final FontsContractCompat.FontInfo fontInfo : array) {
            final ByteBuffer byteBuffer = prepareFontData.get(fontInfo.getUri());
            if (byteBuffer != null) {
                if (!this.addFontFromBuffer(family, byteBuffer, fontInfo.getTtcIndex(), fontInfo.getWeight(), fontInfo.isItalic() ? 1 : 0)) {
                    this.abortCreation(family);
                    return null;
                }
                b = true;
            }
        }
        if (!b) {
            this.abortCreation(family);
            return null;
        }
        if (!this.freeze(family)) {
            return null;
        }
        return Typeface.create(this.createFromFamiliesWithDefault(family), n);
    }
    
    @Override
    public Typeface createFromResourcesFontFile(final Context context, final Resources resources, final int n, final String s, final int n2) {
        if (!this.isFontFamilyPrivateAPIAvailable()) {
            return super.createFromResourcesFontFile(context, resources, n, s, n2);
        }
        final Object family = this.newFamily();
        if (!this.addFontFromAssetManager(context, family, s, 0, -1, -1, null)) {
            this.abortCreation(family);
            return null;
        }
        if (!this.freeze(family)) {
            return null;
        }
        return this.createFromFamiliesWithDefault(family);
    }
    
    protected Method obtainAbortCreationMethod(final Class clazz) throws NoSuchMethodException {
        return clazz.getMethod("abortCreation", (Class[])new Class[0]);
    }
    
    protected Method obtainAddFontFromAssetManagerMethod(final Class clazz) throws NoSuchMethodException {
        return clazz.getMethod("addFontFromAssetManager", AssetManager.class, String.class, Integer.TYPE, Boolean.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, FontVariationAxis[].class);
    }
    
    protected Method obtainAddFontFromBufferMethod(final Class clazz) throws NoSuchMethodException {
        return clazz.getMethod("addFontFromBuffer", ByteBuffer.class, Integer.TYPE, FontVariationAxis[].class, Integer.TYPE, Integer.TYPE);
    }
    
    protected Method obtainCreateFromFamiliesWithDefaultMethod(final Class componentType) throws NoSuchMethodException {
        final Method declaredMethod = Typeface.class.getDeclaredMethod("createFromFamiliesWithDefault", Array.newInstance(componentType, 1).getClass(), Integer.TYPE, Integer.TYPE);
        declaredMethod.setAccessible(true);
        return declaredMethod;
    }
    
    protected Class obtainFontFamily() throws ClassNotFoundException {
        return Class.forName("android.graphics.FontFamily");
    }
    
    protected Constructor obtainFontFamilyCtor(final Class clazz) throws NoSuchMethodException {
        return clazz.getConstructor((Class[])new Class[0]);
    }
    
    protected Method obtainFreezeMethod(final Class clazz) throws NoSuchMethodException {
        return clazz.getMethod("freeze", (Class[])new Class[0]);
    }
}
