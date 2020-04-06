// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.database;

import android.text.TextUtils;

@Deprecated
public final class DatabaseUtilsCompat
{
    private DatabaseUtilsCompat() {
    }
    
    @Deprecated
    public static String[] appendSelectionArgs(final String[] array, final String[] array2) {
        if (array != null && array.length != 0) {
            final String[] array3 = new String[array.length + array2.length];
            System.arraycopy(array, 0, array3, 0, array.length);
            System.arraycopy(array2, 0, array3, array.length, array2.length);
            return array3;
        }
        return array2;
    }
    
    @Deprecated
    public static String concatenateWhere(final String str, final String str2) {
        if (TextUtils.isEmpty((CharSequence)str)) {
            return str2;
        }
        if (TextUtils.isEmpty((CharSequence)str2)) {
            return str;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(str);
        sb.append(") AND (");
        sb.append(str2);
        sb.append(")");
        return sb.toString();
    }
}
