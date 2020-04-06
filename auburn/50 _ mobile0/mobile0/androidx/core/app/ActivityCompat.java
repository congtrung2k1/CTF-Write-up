// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.app;

import android.app.SharedElementCallback$OnSharedElementsReadyListener;
import java.util.Map;
import java.util.List;
import android.content.Context;
import android.os.Parcelable;
import android.graphics.RectF;
import android.graphics.Matrix;
import android.content.IntentSender$SendIntentException;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import androidx.core.view.DragAndDropPermissionsCompat;
import android.view.DragEvent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build$VERSION;
import android.app.Activity;
import androidx.core.content.ContextCompat;

public class ActivityCompat extends ContextCompat
{
    private static PermissionCompatDelegate sDelegate;
    
    protected ActivityCompat() {
    }
    
    public static void finishAffinity(final Activity activity) {
        if (Build$VERSION.SDK_INT >= 16) {
            activity.finishAffinity();
        }
        else {
            activity.finish();
        }
    }
    
    public static void finishAfterTransition(final Activity activity) {
        if (Build$VERSION.SDK_INT >= 21) {
            activity.finishAfterTransition();
        }
        else {
            activity.finish();
        }
    }
    
    public static PermissionCompatDelegate getPermissionCompatDelegate() {
        return ActivityCompat.sDelegate;
    }
    
    public static Uri getReferrer(final Activity activity) {
        if (Build$VERSION.SDK_INT >= 22) {
            return activity.getReferrer();
        }
        final Intent intent = activity.getIntent();
        final Uri uri = (Uri)intent.getParcelableExtra("android.intent.extra.REFERRER");
        if (uri != null) {
            return uri;
        }
        final String stringExtra = intent.getStringExtra("android.intent.extra.REFERRER_NAME");
        if (stringExtra != null) {
            return Uri.parse(stringExtra);
        }
        return null;
    }
    
    @Deprecated
    public static boolean invalidateOptionsMenu(final Activity activity) {
        activity.invalidateOptionsMenu();
        return true;
    }
    
    public static void postponeEnterTransition(final Activity activity) {
        if (Build$VERSION.SDK_INT >= 21) {
            activity.postponeEnterTransition();
        }
    }
    
    public static DragAndDropPermissionsCompat requestDragAndDropPermissions(final Activity activity, final DragEvent dragEvent) {
        return DragAndDropPermissionsCompat.request(activity, dragEvent);
    }
    
    public static void requestPermissions(final Activity activity, final String[] array, final int n) {
        final PermissionCompatDelegate sDelegate = ActivityCompat.sDelegate;
        if (sDelegate != null && sDelegate.requestPermissions(activity, array, n)) {
            return;
        }
        if (Build$VERSION.SDK_INT >= 23) {
            if (activity instanceof RequestPermissionsRequestCodeValidator) {
                ((RequestPermissionsRequestCodeValidator)activity).validateRequestPermissionsRequestCode(n);
            }
            activity.requestPermissions(array, n);
        }
        else if (activity instanceof OnRequestPermissionsResultCallback) {
            new Handler(Looper.getMainLooper()).post((Runnable)new Runnable() {
                @Override
                public void run() {
                    final int[] array = new int[array.length];
                    final PackageManager packageManager = activity.getPackageManager();
                    final String packageName = activity.getPackageName();
                    for (int length = array.length, i = 0; i < length; ++i) {
                        array[i] = packageManager.checkPermission(array[i], packageName);
                    }
                    ((OnRequestPermissionsResultCallback)activity).onRequestPermissionsResult(n, array, array);
                }
            });
        }
    }
    
    public static <T extends View> T requireViewById(final Activity activity, final int n) {
        if (Build$VERSION.SDK_INT >= 28) {
            return (T)activity.requireViewById(n);
        }
        final View viewById = activity.findViewById(n);
        if (viewById != null) {
            return (T)viewById;
        }
        throw new IllegalArgumentException("ID does not reference a View inside this Activity");
    }
    
    public static void setEnterSharedElementCallback(final Activity activity, final SharedElementCallback sharedElementCallback) {
        if (Build$VERSION.SDK_INT >= 21) {
            SharedElementCallback21Impl enterSharedElementCallback;
            if (sharedElementCallback != null) {
                enterSharedElementCallback = new SharedElementCallback21Impl(sharedElementCallback);
            }
            else {
                enterSharedElementCallback = null;
            }
            activity.setEnterSharedElementCallback((android.app.SharedElementCallback)enterSharedElementCallback);
        }
    }
    
    public static void setExitSharedElementCallback(final Activity activity, final SharedElementCallback sharedElementCallback) {
        if (Build$VERSION.SDK_INT >= 21) {
            SharedElementCallback21Impl exitSharedElementCallback;
            if (sharedElementCallback != null) {
                exitSharedElementCallback = new SharedElementCallback21Impl(sharedElementCallback);
            }
            else {
                exitSharedElementCallback = null;
            }
            activity.setExitSharedElementCallback((android.app.SharedElementCallback)exitSharedElementCallback);
        }
    }
    
    public static void setPermissionCompatDelegate(final PermissionCompatDelegate sDelegate) {
        ActivityCompat.sDelegate = sDelegate;
    }
    
    public static boolean shouldShowRequestPermissionRationale(final Activity activity, final String s) {
        return Build$VERSION.SDK_INT >= 23 && activity.shouldShowRequestPermissionRationale(s);
    }
    
    public static void startActivityForResult(final Activity activity, final Intent intent, final int n, final Bundle bundle) {
        if (Build$VERSION.SDK_INT >= 16) {
            activity.startActivityForResult(intent, n, bundle);
        }
        else {
            activity.startActivityForResult(intent, n);
        }
    }
    
    public static void startIntentSenderForResult(final Activity activity, final IntentSender intentSender, final int n, final Intent intent, final int n2, final int n3, final int n4, final Bundle bundle) throws IntentSender$SendIntentException {
        if (Build$VERSION.SDK_INT >= 16) {
            activity.startIntentSenderForResult(intentSender, n, intent, n2, n3, n4, bundle);
        }
        else {
            activity.startIntentSenderForResult(intentSender, n, intent, n2, n3, n4);
        }
    }
    
    public static void startPostponedEnterTransition(final Activity activity) {
        if (Build$VERSION.SDK_INT >= 21) {
            activity.startPostponedEnterTransition();
        }
    }
    
    public interface OnRequestPermissionsResultCallback
    {
        void onRequestPermissionsResult(final int p0, final String[] p1, final int[] p2);
    }
    
    public interface PermissionCompatDelegate
    {
        boolean onActivityResult(final Activity p0, final int p1, final int p2, final Intent p3);
        
        boolean requestPermissions(final Activity p0, final String[] p1, final int p2);
    }
    
    public interface RequestPermissionsRequestCodeValidator
    {
        void validateRequestPermissionsRequestCode(final int p0);
    }
    
    private static class SharedElementCallback21Impl extends android.app.SharedElementCallback
    {
        private final SharedElementCallback mCallback;
        
        SharedElementCallback21Impl(final SharedElementCallback mCallback) {
            this.mCallback = mCallback;
        }
        
        public Parcelable onCaptureSharedElementSnapshot(final View view, final Matrix matrix, final RectF rectF) {
            return this.mCallback.onCaptureSharedElementSnapshot(view, matrix, rectF);
        }
        
        public View onCreateSnapshotView(final Context context, final Parcelable parcelable) {
            return this.mCallback.onCreateSnapshotView(context, parcelable);
        }
        
        public void onMapSharedElements(final List<String> list, final Map<String, View> map) {
            this.mCallback.onMapSharedElements(list, map);
        }
        
        public void onRejectSharedElements(final List<View> list) {
            this.mCallback.onRejectSharedElements(list);
        }
        
        public void onSharedElementEnd(final List<String> list, final List<View> list2, final List<View> list3) {
            this.mCallback.onSharedElementEnd(list, list2, list3);
        }
        
        public void onSharedElementStart(final List<String> list, final List<View> list2, final List<View> list3) {
            this.mCallback.onSharedElementStart(list, list2, list3);
        }
        
        public void onSharedElementsArrived(final List<String> list, final List<View> list2, final SharedElementCallback$OnSharedElementsReadyListener sharedElementCallback$OnSharedElementsReadyListener) {
            this.mCallback.onSharedElementsArrived(list, list2, (SharedElementCallback.OnSharedElementsReadyListener)new SharedElementCallback.OnSharedElementsReadyListener() {
                @Override
                public void onSharedElementsReady() {
                    sharedElementCallback$OnSharedElementsReadyListener.onSharedElementsReady();
                }
            });
        }
    }
}
