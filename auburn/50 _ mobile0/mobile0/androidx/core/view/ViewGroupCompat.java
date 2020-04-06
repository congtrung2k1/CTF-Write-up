// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.view;

import android.view.accessibility.AccessibilityEvent;
import android.view.View;
import androidx.core.R;
import android.os.Build$VERSION;
import android.view.ViewGroup;

public final class ViewGroupCompat
{
    public static final int LAYOUT_MODE_CLIP_BOUNDS = 0;
    public static final int LAYOUT_MODE_OPTICAL_BOUNDS = 1;
    
    private ViewGroupCompat() {
    }
    
    public static int getLayoutMode(final ViewGroup viewGroup) {
        if (Build$VERSION.SDK_INT >= 18) {
            return viewGroup.getLayoutMode();
        }
        return 0;
    }
    
    public static int getNestedScrollAxes(final ViewGroup viewGroup) {
        if (Build$VERSION.SDK_INT >= 21) {
            return viewGroup.getNestedScrollAxes();
        }
        if (viewGroup instanceof NestedScrollingParent) {
            return ((NestedScrollingParent)viewGroup).getNestedScrollAxes();
        }
        return 0;
    }
    
    public static boolean isTransitionGroup(final ViewGroup viewGroup) {
        if (Build$VERSION.SDK_INT >= 21) {
            return viewGroup.isTransitionGroup();
        }
        final Boolean b = (Boolean)viewGroup.getTag(R.id.tag_transition_group);
        return (b != null && b) || viewGroup.getBackground() != null || ViewCompat.getTransitionName((View)viewGroup) != null;
    }
    
    @Deprecated
    public static boolean onRequestSendAccessibilityEvent(final ViewGroup viewGroup, final View view, final AccessibilityEvent accessibilityEvent) {
        return viewGroup.onRequestSendAccessibilityEvent(view, accessibilityEvent);
    }
    
    public static void setLayoutMode(final ViewGroup viewGroup, final int layoutMode) {
        if (Build$VERSION.SDK_INT >= 18) {
            viewGroup.setLayoutMode(layoutMode);
        }
    }
    
    @Deprecated
    public static void setMotionEventSplittingEnabled(final ViewGroup viewGroup, final boolean motionEventSplittingEnabled) {
        viewGroup.setMotionEventSplittingEnabled(motionEventSplittingEnabled);
    }
    
    public static void setTransitionGroup(final ViewGroup viewGroup, final boolean b) {
        if (Build$VERSION.SDK_INT >= 21) {
            viewGroup.setTransitionGroup(b);
        }
        else {
            viewGroup.setTag(R.id.tag_transition_group, (Object)b);
        }
    }
}
