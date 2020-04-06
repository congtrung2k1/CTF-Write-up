// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.os;

import java.io.IOException;
import android.util.Log;
import android.os.Environment;
import android.os.Build$VERSION;
import java.io.File;

public final class EnvironmentCompat
{
    public static final String MEDIA_UNKNOWN = "unknown";
    private static final String TAG = "EnvironmentCompat";
    
    private EnvironmentCompat() {
    }
    
    public static String getStorageState(final File file) {
        if (Build$VERSION.SDK_INT >= 19) {
            return Environment.getStorageState(file);
        }
        try {
            if (file.getCanonicalPath().startsWith(Environment.getExternalStorageDirectory().getCanonicalPath())) {
                return Environment.getExternalStorageState();
            }
        }
        catch (IOException obj) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to resolve canonical path: ");
            sb.append(obj);
            Log.w("EnvironmentCompat", sb.toString());
        }
        return "unknown";
    }
}
