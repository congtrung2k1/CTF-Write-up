// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.content;

import java.util.ArrayList;

public final class MimeTypeFilter
{
    private MimeTypeFilter() {
    }
    
    public static String matches(String s, final String[] array) {
        if (s == null) {
            return null;
        }
        final String[] split = s.split("/");
        for (int length = array.length, i = 0; i < length; ++i) {
            s = array[i];
            if (mimeTypeAgainstFilter(split, s.split("/"))) {
                return s;
            }
        }
        return null;
    }
    
    public static String matches(final String[] array, final String s) {
        if (array == null) {
            return null;
        }
        final String[] split = s.split("/");
        for (final String s2 : array) {
            if (mimeTypeAgainstFilter(s2.split("/"), split)) {
                return s2;
            }
        }
        return null;
    }
    
    public static boolean matches(final String s, final String s2) {
        return s != null && mimeTypeAgainstFilter(s.split("/"), s2.split("/"));
    }
    
    public static String[] matchesMany(final String[] array, final String s) {
        int i = 0;
        if (array == null) {
            return new String[0];
        }
        final ArrayList<String> list = new ArrayList<String>();
        final String[] split = s.split("/");
        while (i < array.length) {
            final String e = array[i];
            if (mimeTypeAgainstFilter(e.split("/"), split)) {
                list.add(e);
            }
            ++i;
        }
        return list.toArray(new String[list.size()]);
    }
    
    private static boolean mimeTypeAgainstFilter(final String[] array, final String[] array2) {
        if (array2.length != 2) {
            throw new IllegalArgumentException("Ill-formatted MIME type filter. Must be type/subtype.");
        }
        if (!array2[0].isEmpty() && !array2[1].isEmpty()) {
            return array.length == 2 && ("*".equals(array2[0]) || array2[0].equals(array[0])) && ("*".equals(array2[1]) || array2[1].equals(array[1]));
        }
        throw new IllegalArgumentException("Ill-formatted MIME type filter. Type or subtype empty.");
    }
}
