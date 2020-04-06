// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.content;

import android.os.OperationCanceledException;
import android.os.Build$VERSION;
import android.database.Cursor;
import androidx.core.os.CancellationSignal;
import android.net.Uri;
import android.content.ContentResolver;

public final class ContentResolverCompat
{
    private ContentResolverCompat() {
    }
    
    public static Cursor query(final ContentResolver contentResolver, final Uri uri, final String[] array, final String s, final String[] array2, final String s2, final CancellationSignal cancellationSignal) {
        if (Build$VERSION.SDK_INT >= 16) {
            Label_0027: {
                if (cancellationSignal == null) {
                    break Label_0027;
                }
                try {
                    final Object cancellationSignalObject = cancellationSignal.getCancellationSignalObject();
                    return contentResolver.query(uri, array, s, array2, s2, (android.os.CancellationSignal)cancellationSignalObject);
                }
                catch (Exception ex) {
                    if (ex instanceof OperationCanceledException) {
                        throw new androidx.core.os.OperationCanceledException();
                    }
                    throw ex;
                    final Object cancellationSignalObject = null;
                    return contentResolver.query(uri, array, s, array2, s2, (android.os.CancellationSignal)cancellationSignalObject);
                }
            }
        }
        if (cancellationSignal != null) {
            cancellationSignal.throwIfCanceled();
        }
        return contentResolver.query(uri, array, s, array2, s2);
    }
}
