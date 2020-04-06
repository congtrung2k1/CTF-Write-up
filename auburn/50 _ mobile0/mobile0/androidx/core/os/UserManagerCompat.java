// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.os;

import android.os.UserManager;
import android.os.Build$VERSION;
import android.content.Context;

public class UserManagerCompat
{
    private UserManagerCompat() {
    }
    
    public static boolean isUserUnlocked(final Context context) {
        return Build$VERSION.SDK_INT < 24 || ((UserManager)context.getSystemService((Class)UserManager.class)).isUserUnlocked();
    }
}
