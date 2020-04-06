// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.content.pm;

import android.os.Bundle;
import android.content.IntentSender$SendIntentException;
import android.os.Handler;
import android.content.IntentSender$OnFinished;
import android.content.BroadcastReceiver;
import android.content.IntentSender;
import java.util.Iterator;
import android.text.TextUtils;
import android.content.pm.ResolveInfo;
import androidx.core.content.ContextCompat;
import android.content.pm.ShortcutManager;
import android.os.Build$VERSION;
import android.content.Intent;
import android.content.Context;

public class ShortcutManagerCompat
{
    static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    static final String INSTALL_SHORTCUT_PERMISSION = "com.android.launcher.permission.INSTALL_SHORTCUT";
    
    private ShortcutManagerCompat() {
    }
    
    public static Intent createShortcutResultIntent(final Context context, final ShortcutInfoCompat shortcutInfoCompat) {
        Intent shortcutResultIntent = null;
        if (Build$VERSION.SDK_INT >= 26) {
            shortcutResultIntent = ((ShortcutManager)context.getSystemService((Class)ShortcutManager.class)).createShortcutResultIntent(shortcutInfoCompat.toShortcutInfo());
        }
        Intent intent;
        if ((intent = shortcutResultIntent) == null) {
            intent = new Intent();
        }
        return shortcutInfoCompat.addToIntent(intent);
    }
    
    public static boolean isRequestPinShortcutSupported(final Context context) {
        if (Build$VERSION.SDK_INT >= 26) {
            return ((ShortcutManager)context.getSystemService((Class)ShortcutManager.class)).isRequestPinShortcutSupported();
        }
        if (ContextCompat.checkSelfPermission(context, "com.android.launcher.permission.INSTALL_SHORTCUT") != 0) {
            return false;
        }
        final Iterator<ResolveInfo> iterator = context.getPackageManager().queryBroadcastReceivers(new Intent("com.android.launcher.action.INSTALL_SHORTCUT"), 0).iterator();
        while (iterator.hasNext()) {
            final String permission = iterator.next().activityInfo.permission;
            if (!TextUtils.isEmpty((CharSequence)permission) && !"com.android.launcher.permission.INSTALL_SHORTCUT".equals(permission)) {
                continue;
            }
            return true;
        }
        return false;
    }
    
    public static boolean requestPinShortcut(final Context context, final ShortcutInfoCompat shortcutInfoCompat, final IntentSender intentSender) {
        if (Build$VERSION.SDK_INT >= 26) {
            return ((ShortcutManager)context.getSystemService((Class)ShortcutManager.class)).requestPinShortcut(shortcutInfoCompat.toShortcutInfo(), intentSender);
        }
        if (!isRequestPinShortcutSupported(context)) {
            return false;
        }
        final Intent addToIntent = shortcutInfoCompat.addToIntent(new Intent("com.android.launcher.action.INSTALL_SHORTCUT"));
        if (intentSender == null) {
            context.sendBroadcast(addToIntent);
            return true;
        }
        context.sendOrderedBroadcast(addToIntent, (String)null, (BroadcastReceiver)new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                try {
                    intentSender.sendIntent(context, 0, (Intent)null, (IntentSender$OnFinished)null, (Handler)null);
                }
                catch (IntentSender$SendIntentException ex) {}
            }
        }, (Handler)null, -1, (String)null, (Bundle)null);
        return true;
    }
}
