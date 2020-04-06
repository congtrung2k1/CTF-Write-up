// 
// Decompiled by Procyon v0.5.36
// 

package androidx.legacy.content;

import android.os.PowerManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.content.Intent;
import android.os.PowerManager$WakeLock;
import android.util.SparseArray;
import android.content.BroadcastReceiver;

@Deprecated
public abstract class WakefulBroadcastReceiver extends BroadcastReceiver
{
    private static final String EXTRA_WAKE_LOCK_ID = "androidx.contentpager.content.wakelockid";
    private static int mNextId;
    private static final SparseArray<PowerManager$WakeLock> sActiveWakeLocks;
    
    static {
        sActiveWakeLocks = new SparseArray();
        WakefulBroadcastReceiver.mNextId = 1;
    }
    
    public static boolean completeWakefulIntent(final Intent intent) {
        final int intExtra = intent.getIntExtra("androidx.contentpager.content.wakelockid", 0);
        if (intExtra == 0) {
            return false;
        }
        synchronized (WakefulBroadcastReceiver.sActiveWakeLocks) {
            final PowerManager$WakeLock powerManager$WakeLock = (PowerManager$WakeLock)WakefulBroadcastReceiver.sActiveWakeLocks.get(intExtra);
            if (powerManager$WakeLock != null) {
                powerManager$WakeLock.release();
                WakefulBroadcastReceiver.sActiveWakeLocks.remove(intExtra);
                return true;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("No active wake lock id #");
            sb.append(intExtra);
            Log.w("WakefulBroadcastReceiv.", sb.toString());
            return true;
        }
    }
    
    public static ComponentName startWakefulService(final Context context, final Intent intent) {
        synchronized (WakefulBroadcastReceiver.sActiveWakeLocks) {
            final int mNextId = WakefulBroadcastReceiver.mNextId;
            if (++WakefulBroadcastReceiver.mNextId <= 0) {
                WakefulBroadcastReceiver.mNextId = 1;
            }
            intent.putExtra("androidx.contentpager.content.wakelockid", mNextId);
            final ComponentName startService = context.startService(intent);
            if (startService == null) {
                return null;
            }
            final PowerManager powerManager = (PowerManager)context.getSystemService("power");
            final StringBuilder sb = new StringBuilder();
            sb.append("androidx.core:wake:");
            sb.append(startService.flattenToShortString());
            final PowerManager$WakeLock wakeLock = powerManager.newWakeLock(1, sb.toString());
            wakeLock.setReferenceCounted(false);
            wakeLock.acquire(60000L);
            WakefulBroadcastReceiver.sActiveWakeLocks.put(mNextId, (Object)wakeLock);
            return startService;
        }
    }
}
