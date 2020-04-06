// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.util;

public class DebugUtils
{
    private DebugUtils() {
    }
    
    public static void buildShortClassTag(final Object o, final StringBuilder sb) {
        if (o == null) {
            sb.append("null");
        }
        else {
            final String simpleName = o.getClass().getSimpleName();
            String substring = null;
            Label_0067: {
                if (simpleName != null) {
                    substring = simpleName;
                    if (simpleName.length() > 0) {
                        break Label_0067;
                    }
                }
                final String name = o.getClass().getName();
                final int lastIndex = name.lastIndexOf(46);
                substring = name;
                if (lastIndex > 0) {
                    substring = name.substring(lastIndex + 1);
                }
            }
            sb.append(substring);
            sb.append('{');
            sb.append(Integer.toHexString(System.identityHashCode(o)));
        }
    }
}
