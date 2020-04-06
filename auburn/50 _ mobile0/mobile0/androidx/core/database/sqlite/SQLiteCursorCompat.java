// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.database.sqlite;

import android.os.Build$VERSION;
import android.database.sqlite.SQLiteCursor;

public final class SQLiteCursorCompat
{
    private SQLiteCursorCompat() {
    }
    
    public static void setFillWindowForwardOnly(final SQLiteCursor sqLiteCursor, final boolean fillWindowForwardOnly) {
        if (Build$VERSION.SDK_INT >= 28) {
            sqLiteCursor.setFillWindowForwardOnly(fillWindowForwardOnly);
        }
    }
}
