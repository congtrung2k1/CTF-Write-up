// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.app;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.lang.reflect.Method;
import android.app.ActionBar;
import android.util.Log;
import android.os.Build$VERSION;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.app.Activity;

class ActionBarDrawerToggleHoneycomb
{
    private static final String TAG = "ActionBarDrawerToggleHC";
    private static final int[] THEME_ATTRS;
    
    static {
        THEME_ATTRS = new int[] { 16843531 };
    }
    
    private ActionBarDrawerToggleHoneycomb() {
    }
    
    public static Drawable getThemeUpIndicator(final Activity activity) {
        final TypedArray obtainStyledAttributes = activity.obtainStyledAttributes(ActionBarDrawerToggleHoneycomb.THEME_ATTRS);
        final Drawable drawable = obtainStyledAttributes.getDrawable(0);
        obtainStyledAttributes.recycle();
        return drawable;
    }
    
    public static SetIndicatorInfo setActionBarDescription(final SetIndicatorInfo setIndicatorInfo, final Activity activity, final int i) {
        SetIndicatorInfo setIndicatorInfo2 = setIndicatorInfo;
        if (setIndicatorInfo == null) {
            setIndicatorInfo2 = new SetIndicatorInfo(activity);
        }
        if (setIndicatorInfo2.setHomeAsUpIndicator != null) {
            try {
                final ActionBar actionBar = activity.getActionBar();
                setIndicatorInfo2.setHomeActionContentDescription.invoke(actionBar, i);
                if (Build$VERSION.SDK_INT <= 19) {
                    actionBar.setSubtitle(actionBar.getSubtitle());
                }
            }
            catch (Exception ex) {
                Log.w("ActionBarDrawerToggleHC", "Couldn't set content description via JB-MR2 API", (Throwable)ex);
            }
        }
        return setIndicatorInfo2;
    }
    
    public static SetIndicatorInfo setActionBarUpIndicator(SetIndicatorInfo setIndicatorInfo, final Activity activity, final Drawable imageDrawable, final int i) {
        setIndicatorInfo = new SetIndicatorInfo(activity);
        if (setIndicatorInfo.setHomeAsUpIndicator != null) {
            try {
                final ActionBar actionBar = activity.getActionBar();
                setIndicatorInfo.setHomeAsUpIndicator.invoke(actionBar, imageDrawable);
                setIndicatorInfo.setHomeActionContentDescription.invoke(actionBar, i);
            }
            catch (Exception ex) {
                Log.w("ActionBarDrawerToggleHC", "Couldn't set home-as-up indicator via JB-MR2 API", (Throwable)ex);
            }
        }
        else if (setIndicatorInfo.upIndicatorView != null) {
            setIndicatorInfo.upIndicatorView.setImageDrawable(imageDrawable);
        }
        else {
            Log.w("ActionBarDrawerToggleHC", "Couldn't set home-as-up indicator");
        }
        return setIndicatorInfo;
    }
    
    static class SetIndicatorInfo
    {
        public Method setHomeActionContentDescription;
        public Method setHomeAsUpIndicator;
        public ImageView upIndicatorView;
        
        SetIndicatorInfo(final Activity activity) {
            try {
                this.setHomeAsUpIndicator = ActionBar.class.getDeclaredMethod("setHomeAsUpIndicator", Drawable.class);
                this.setHomeActionContentDescription = ActionBar.class.getDeclaredMethod("setHomeActionContentDescription", Integer.TYPE);
            }
            catch (NoSuchMethodException ex) {
                final View viewById = activity.findViewById(16908332);
                if (viewById == null) {
                    return;
                }
                final ViewGroup viewGroup = (ViewGroup)viewById.getParent();
                if (viewGroup.getChildCount() != 2) {
                    return;
                }
                View child = viewGroup.getChildAt(0);
                final View child2 = viewGroup.getChildAt(1);
                if (child.getId() == 16908332) {
                    child = child2;
                }
                if (child instanceof ImageView) {
                    this.upIndicatorView = (ImageView)child;
                }
            }
        }
    }
}
