// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.os;

import android.os.Trace;
import android.os.Build$VERSION;

public final class TraceCompat
{
    private TraceCompat() {
    }
    
    public static void beginSection(final String s) {
        if (Build$VERSION.SDK_INT >= 18) {
            Trace.beginSection(s);
        }
    }
    
    public static void endSection() {
        if (Build$VERSION.SDK_INT >= 18) {
            Trace.endSection();
        }
    }
}
