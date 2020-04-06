// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.app;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.content.res.Configuration;
import android.view.MenuInflater;
import android.util.AttributeSet;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.util.Log;
import androidx.appcompat.widget.VectorEnabledTintResources;
import android.view.Window;
import android.app.Dialog;
import android.content.Context;
import android.app.Activity;

public abstract class AppCompatDelegate
{
    public static final int FEATURE_ACTION_MODE_OVERLAY = 10;
    public static final int FEATURE_SUPPORT_ACTION_BAR = 108;
    public static final int FEATURE_SUPPORT_ACTION_BAR_OVERLAY = 109;
    public static final int MODE_NIGHT_AUTO = 0;
    public static final int MODE_NIGHT_FOLLOW_SYSTEM = -1;
    public static final int MODE_NIGHT_NO = 1;
    static final int MODE_NIGHT_UNSPECIFIED = -100;
    public static final int MODE_NIGHT_YES = 2;
    static final String TAG = "AppCompatDelegate";
    private static int sDefaultNightMode;
    
    static {
        AppCompatDelegate.sDefaultNightMode = -1;
    }
    
    AppCompatDelegate() {
    }
    
    public static AppCompatDelegate create(final Activity activity, final AppCompatCallback appCompatCallback) {
        return new AppCompatDelegateImpl((Context)activity, activity.getWindow(), appCompatCallback);
    }
    
    public static AppCompatDelegate create(final Dialog dialog, final AppCompatCallback appCompatCallback) {
        return new AppCompatDelegateImpl(dialog.getContext(), dialog.getWindow(), appCompatCallback);
    }
    
    public static AppCompatDelegate create(final Context context, final Window window, final AppCompatCallback appCompatCallback) {
        return new AppCompatDelegateImpl(context, window, appCompatCallback);
    }
    
    public static int getDefaultNightMode() {
        return AppCompatDelegate.sDefaultNightMode;
    }
    
    public static boolean isCompatVectorFromResourcesEnabled() {
        return VectorEnabledTintResources.isCompatVectorFromResourcesEnabled();
    }
    
    public static void setCompatVectorFromResourcesEnabled(final boolean compatVectorFromResourcesEnabled) {
        VectorEnabledTintResources.setCompatVectorFromResourcesEnabled(compatVectorFromResourcesEnabled);
    }
    
    public static void setDefaultNightMode(final int sDefaultNightMode) {
        if (sDefaultNightMode != -1 && sDefaultNightMode != 0 && sDefaultNightMode != 1 && sDefaultNightMode != 2) {
            Log.d("AppCompatDelegate", "setDefaultNightMode() called with an unknown mode");
        }
        else {
            AppCompatDelegate.sDefaultNightMode = sDefaultNightMode;
        }
    }
    
    public abstract void addContentView(final View p0, final ViewGroup$LayoutParams p1);
    
    public abstract boolean applyDayNight();
    
    public abstract View createView(final View p0, final String p1, final Context p2, final AttributeSet p3);
    
    public abstract <T extends View> T findViewById(final int p0);
    
    public abstract ActionBarDrawerToggle.Delegate getDrawerToggleDelegate();
    
    public abstract MenuInflater getMenuInflater();
    
    public abstract ActionBar getSupportActionBar();
    
    public abstract boolean hasWindowFeature(final int p0);
    
    public abstract void installViewFactory();
    
    public abstract void invalidateOptionsMenu();
    
    public abstract boolean isHandleNativeActionModesEnabled();
    
    public abstract void onConfigurationChanged(final Configuration p0);
    
    public abstract void onCreate(final Bundle p0);
    
    public abstract void onDestroy();
    
    public abstract void onPostCreate(final Bundle p0);
    
    public abstract void onPostResume();
    
    public abstract void onSaveInstanceState(final Bundle p0);
    
    public abstract void onStart();
    
    public abstract void onStop();
    
    public abstract boolean requestWindowFeature(final int p0);
    
    public abstract void setContentView(final int p0);
    
    public abstract void setContentView(final View p0);
    
    public abstract void setContentView(final View p0, final ViewGroup$LayoutParams p1);
    
    public abstract void setHandleNativeActionModesEnabled(final boolean p0);
    
    public abstract void setLocalNightMode(final int p0);
    
    public abstract void setSupportActionBar(final Toolbar p0);
    
    public abstract void setTitle(final CharSequence p0);
    
    public abstract ActionMode startSupportActionMode(final ActionMode.Callback p0);
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface NightMode {
    }
}
