// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.util;

import java.util.Arrays;
import java.util.Objects;
import android.os.Build$VERSION;

public class ObjectsCompat
{
    private ObjectsCompat() {
    }
    
    public static boolean equals(final Object a, final Object o) {
        if (Build$VERSION.SDK_INT >= 19) {
            return Objects.equals(a, o);
        }
        return a == o || (a != null && a.equals(o));
    }
    
    public static int hash(final Object... array) {
        if (Build$VERSION.SDK_INT >= 19) {
            return Objects.hash(array);
        }
        return Arrays.hashCode(array);
    }
    
    public static int hashCode(final Object o) {
        int hashCode;
        if (o != null) {
            hashCode = o.hashCode();
        }
        else {
            hashCode = 0;
        }
        return hashCode;
    }
}
