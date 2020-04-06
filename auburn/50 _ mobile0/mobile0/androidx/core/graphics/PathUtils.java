// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.graphics;

import android.graphics.PointF;
import java.util.ArrayList;
import java.util.Collection;
import android.graphics.Path;

public final class PathUtils
{
    private PathUtils() {
    }
    
    public static Collection<PathSegment> flatten(final Path path) {
        return flatten(path, 0.5f);
    }
    
    public static Collection<PathSegment> flatten(final Path path, float n) {
        final float[] approximate = path.approximate(n);
        final int initialCapacity = approximate.length / 3;
        final ArrayList list = new ArrayList<PathSegment>(initialCapacity);
        for (int i = 1; i < initialCapacity; ++i) {
            final int n2 = i * 3;
            final int n3 = (i - 1) * 3;
            final float n4 = approximate[n2];
            final float n5 = approximate[n2 + 1];
            final float n6 = approximate[n2 + 2];
            final float n7 = approximate[n3];
            n = approximate[n3 + 1];
            final float n8 = approximate[n3 + 2];
            if (n4 != n7 && (n5 != n || n6 != n8)) {
                list.add(new PathSegment(new PointF(n, n8), n7, new PointF(n5, n6), n4));
            }
        }
        return (Collection<PathSegment>)list;
    }
}
