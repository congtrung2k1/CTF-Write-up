// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.os;

import android.os.Message;
import android.os.Build$VERSION;
import android.os.Handler;

public final class HandlerCompat
{
    private HandlerCompat() {
    }
    
    public static boolean postDelayed(final Handler handler, final Runnable runnable, final Object obj, final long n) {
        if (Build$VERSION.SDK_INT >= 28) {
            return handler.postDelayed(runnable, obj, n);
        }
        final Message obtain = Message.obtain(handler, runnable);
        obtain.obj = obj;
        return handler.sendMessageDelayed(obtain, n);
    }
}
