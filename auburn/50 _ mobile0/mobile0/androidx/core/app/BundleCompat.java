// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.app;

import java.lang.reflect.InvocationTargetException;
import android.util.Log;
import java.lang.reflect.Method;
import android.os.Build$VERSION;
import android.os.IBinder;
import android.os.Bundle;

public final class BundleCompat
{
    private BundleCompat() {
    }
    
    public static IBinder getBinder(final Bundle bundle, final String s) {
        if (Build$VERSION.SDK_INT >= 18) {
            return bundle.getBinder(s);
        }
        return BundleCompatBaseImpl.getBinder(bundle, s);
    }
    
    public static void putBinder(final Bundle bundle, final String s, final IBinder binder) {
        if (Build$VERSION.SDK_INT >= 18) {
            bundle.putBinder(s, binder);
        }
        else {
            BundleCompatBaseImpl.putBinder(bundle, s, binder);
        }
    }
    
    static class BundleCompatBaseImpl
    {
        private static final String TAG = "BundleCompatBaseImpl";
        private static Method sGetIBinderMethod;
        private static boolean sGetIBinderMethodFetched;
        private static Method sPutIBinderMethod;
        private static boolean sPutIBinderMethodFetched;
        
        private BundleCompatBaseImpl() {
        }
        
        public static IBinder getBinder(Bundle obj, final String s) {
            if (!BundleCompatBaseImpl.sGetIBinderMethodFetched) {
                try {
                    (BundleCompatBaseImpl.sGetIBinderMethod = Bundle.class.getMethod("getIBinder", String.class)).setAccessible(true);
                }
                catch (NoSuchMethodException ex) {
                    Log.i("BundleCompatBaseImpl", "Failed to retrieve getIBinder method", (Throwable)ex);
                }
                BundleCompatBaseImpl.sGetIBinderMethodFetched = true;
            }
            final Method sGetIBinderMethod = BundleCompatBaseImpl.sGetIBinderMethod;
            if (sGetIBinderMethod != null) {
                try {
                    obj = (IllegalArgumentException)sGetIBinderMethod.invoke(obj, s);
                    return (IBinder)obj;
                }
                catch (IllegalArgumentException obj) {}
                catch (IllegalAccessException obj) {}
                catch (InvocationTargetException ex2) {}
                Log.i("BundleCompatBaseImpl", "Failed to invoke getIBinder via reflection", (Throwable)obj);
                BundleCompatBaseImpl.sGetIBinderMethod = null;
            }
            return null;
        }
        
        public static void putBinder(final Bundle obj, final String s, final IBinder binder) {
            if (!BundleCompatBaseImpl.sPutIBinderMethodFetched) {
                try {
                    (BundleCompatBaseImpl.sPutIBinderMethod = Bundle.class.getMethod("putIBinder", String.class, IBinder.class)).setAccessible(true);
                }
                catch (NoSuchMethodException ex) {
                    Log.i("BundleCompatBaseImpl", "Failed to retrieve putIBinder method", (Throwable)ex);
                }
                BundleCompatBaseImpl.sPutIBinderMethodFetched = true;
            }
            final Method sPutIBinderMethod = BundleCompatBaseImpl.sPutIBinderMethod;
            if (sPutIBinderMethod != null) {
                try {
                    sPutIBinderMethod.invoke(obj, s, binder);
                    return;
                }
                catch (IllegalArgumentException obj) {}
                catch (IllegalAccessException obj) {}
                catch (InvocationTargetException ex2) {}
                Log.i("BundleCompatBaseImpl", "Failed to invoke putIBinder via reflection", (Throwable)obj);
                BundleCompatBaseImpl.sPutIBinderMethod = null;
            }
        }
    }
}
