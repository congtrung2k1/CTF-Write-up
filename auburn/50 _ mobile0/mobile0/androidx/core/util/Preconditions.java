// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.util;

import android.text.TextUtils;
import java.util.Iterator;
import java.util.Collection;
import java.util.Locale;

public class Preconditions
{
    private Preconditions() {
    }
    
    public static void checkArgument(final boolean b) {
        if (b) {
            return;
        }
        throw new IllegalArgumentException();
    }
    
    public static void checkArgument(final boolean b, final Object obj) {
        if (b) {
            return;
        }
        throw new IllegalArgumentException(String.valueOf(obj));
    }
    
    public static float checkArgumentFinite(final float n, final String s) {
        if (Float.isNaN(n)) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(" must not be NaN");
            throw new IllegalArgumentException(sb.toString());
        }
        if (!Float.isInfinite(n)) {
            return n;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(s);
        sb2.append(" must not be infinite");
        throw new IllegalArgumentException(sb2.toString());
    }
    
    public static float checkArgumentInRange(final float v, final float n, final float n2, final String str) {
        if (Float.isNaN(v)) {
            final StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(" must not be NaN");
            throw new IllegalArgumentException(sb.toString());
        }
        if (v < n) {
            throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%f, %f] (too low)", str, n, n2));
        }
        if (v <= n2) {
            return v;
        }
        throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%f, %f] (too high)", str, n, n2));
    }
    
    public static int checkArgumentInRange(final int n, final int n2, final int n3, final String s) {
        if (n < n2) {
            throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too low)", s, n2, n3));
        }
        if (n <= n3) {
            return n;
        }
        throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too high)", s, n2, n3));
    }
    
    public static long checkArgumentInRange(final long n, final long n2, final long n3, final String s) {
        if (n < n2) {
            throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too low)", s, n2, n3));
        }
        if (n <= n3) {
            return n;
        }
        throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too high)", s, n2, n3));
    }
    
    public static int checkArgumentNonnegative(final int n) {
        if (n >= 0) {
            return n;
        }
        throw new IllegalArgumentException();
    }
    
    public static int checkArgumentNonnegative(final int n, final String s) {
        if (n >= 0) {
            return n;
        }
        throw new IllegalArgumentException(s);
    }
    
    public static long checkArgumentNonnegative(final long n) {
        if (n >= 0L) {
            return n;
        }
        throw new IllegalArgumentException();
    }
    
    public static long checkArgumentNonnegative(final long n, final String s) {
        if (n >= 0L) {
            return n;
        }
        throw new IllegalArgumentException(s);
    }
    
    public static int checkArgumentPositive(final int n, final String s) {
        if (n > 0) {
            return n;
        }
        throw new IllegalArgumentException(s);
    }
    
    public static float[] checkArrayElementsInRange(final float[] array, final float n, final float n2, final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(" must not be null");
        checkNotNull(array, sb.toString());
        for (int i = 0; i < array.length; ++i) {
            final float v = array[i];
            if (Float.isNaN(v)) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(s);
                sb2.append("[");
                sb2.append(i);
                sb2.append("] must not be NaN");
                throw new IllegalArgumentException(sb2.toString());
            }
            if (v < n) {
                throw new IllegalArgumentException(String.format(Locale.US, "%s[%d] is out of range of [%f, %f] (too low)", s, i, n, n2));
            }
            if (v > n2) {
                throw new IllegalArgumentException(String.format(Locale.US, "%s[%d] is out of range of [%f, %f] (too high)", s, i, n, n2));
            }
        }
        return array;
    }
    
    public static <T> T[] checkArrayElementsNotNull(final T[] array, final String str) {
        if (array != null) {
            for (int i = 0; i < array.length; ++i) {
                if (array[i] == null) {
                    throw new NullPointerException(String.format(Locale.US, "%s[%d] must not be null", str, i));
                }
            }
            return array;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" must not be null");
        throw new NullPointerException(sb.toString());
    }
    
    public static <C extends Collection<T>, T> C checkCollectionElementsNotNull(final C c, final String str) {
        if (c != null) {
            long l = 0L;
            final Iterator<T> iterator = c.iterator();
            while (iterator.hasNext()) {
                if (iterator.next() == null) {
                    throw new NullPointerException(String.format(Locale.US, "%s[%d] must not be null", str, l));
                }
                ++l;
            }
            return c;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" must not be null");
        throw new NullPointerException(sb.toString());
    }
    
    public static <T> Collection<T> checkCollectionNotEmpty(final Collection<T> collection, final String s) {
        if (collection == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(" must not be null");
            throw new NullPointerException(sb.toString());
        }
        if (!collection.isEmpty()) {
            return collection;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(s);
        sb2.append(" is empty");
        throw new IllegalArgumentException(sb2.toString());
    }
    
    public static int checkFlagsArgument(final int i, final int j) {
        if ((i & j) == i) {
            return i;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Requested flags 0x");
        sb.append(Integer.toHexString(i));
        sb.append(", but only 0x");
        sb.append(Integer.toHexString(j));
        sb.append(" are allowed");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static <T> T checkNotNull(final T t) {
        if (t != null) {
            return t;
        }
        throw null;
    }
    
    public static <T> T checkNotNull(final T t, final Object obj) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException(String.valueOf(obj));
    }
    
    public static void checkState(final boolean b) {
        checkState(b, null);
    }
    
    public static void checkState(final boolean b, final String s) {
        if (b) {
            return;
        }
        throw new IllegalStateException(s);
    }
    
    public static <T extends CharSequence> T checkStringNotEmpty(final T t) {
        if (!TextUtils.isEmpty((CharSequence)t)) {
            return t;
        }
        throw new IllegalArgumentException();
    }
    
    public static <T extends CharSequence> T checkStringNotEmpty(final T t, final Object obj) {
        if (!TextUtils.isEmpty((CharSequence)t)) {
            return t;
        }
        throw new IllegalArgumentException(String.valueOf(obj));
    }
}
