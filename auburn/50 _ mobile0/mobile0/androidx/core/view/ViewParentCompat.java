// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.view;

import android.view.accessibility.AccessibilityEvent;
import android.util.Log;
import android.os.Build$VERSION;
import android.view.View;
import android.view.ViewParent;

public final class ViewParentCompat
{
    private static final String TAG = "ViewParentCompat";
    
    private ViewParentCompat() {
    }
    
    public static void notifySubtreeAccessibilityStateChanged(final ViewParent viewParent, final View view, final View view2, final int n) {
        if (Build$VERSION.SDK_INT >= 19) {
            viewParent.notifySubtreeAccessibilityStateChanged(view, view2, n);
        }
    }
    
    public static boolean onNestedFling(final ViewParent obj, final View view, final float n, final float n2, final boolean b) {
        if (Build$VERSION.SDK_INT >= 21) {
            try {
                return obj.onNestedFling(view, n, n2, b);
            }
            catch (AbstractMethodError abstractMethodError) {
                final StringBuilder sb = new StringBuilder();
                sb.append("ViewParent ");
                sb.append(obj);
                sb.append(" does not implement interface ");
                sb.append("method onNestedFling");
                Log.e("ViewParentCompat", sb.toString(), (Throwable)abstractMethodError);
                return false;
            }
        }
        if (obj instanceof NestedScrollingParent) {
            return ((NestedScrollingParent)obj).onNestedFling(view, n, n2, b);
        }
        return false;
    }
    
    public static boolean onNestedPreFling(final ViewParent obj, final View view, final float n, final float n2) {
        if (Build$VERSION.SDK_INT >= 21) {
            try {
                return obj.onNestedPreFling(view, n, n2);
            }
            catch (AbstractMethodError abstractMethodError) {
                final StringBuilder sb = new StringBuilder();
                sb.append("ViewParent ");
                sb.append(obj);
                sb.append(" does not implement interface ");
                sb.append("method onNestedPreFling");
                Log.e("ViewParentCompat", sb.toString(), (Throwable)abstractMethodError);
                return false;
            }
        }
        if (obj instanceof NestedScrollingParent) {
            return ((NestedScrollingParent)obj).onNestedPreFling(view, n, n2);
        }
        return false;
    }
    
    public static void onNestedPreScroll(final ViewParent viewParent, final View view, final int n, final int n2, final int[] array) {
        onNestedPreScroll(viewParent, view, n, n2, array, 0);
    }
    
    public static void onNestedPreScroll(final ViewParent obj, final View view, final int n, final int n2, final int[] array, final int n3) {
        if (obj instanceof NestedScrollingParent2) {
            ((NestedScrollingParent2)obj).onNestedPreScroll(view, n, n2, array, n3);
        }
        else if (n3 == 0) {
            if (Build$VERSION.SDK_INT >= 21) {
                try {
                    obj.onNestedPreScroll(view, n, n2, array);
                    return;
                }
                catch (AbstractMethodError abstractMethodError) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("ViewParent ");
                    sb.append(obj);
                    sb.append(" does not implement interface ");
                    sb.append("method onNestedPreScroll");
                    Log.e("ViewParentCompat", sb.toString(), (Throwable)abstractMethodError);
                    return;
                }
            }
            if (obj instanceof NestedScrollingParent) {
                ((NestedScrollingParent)obj).onNestedPreScroll(view, n, n2, array);
            }
        }
    }
    
    public static void onNestedScroll(final ViewParent viewParent, final View view, final int n, final int n2, final int n3, final int n4) {
        onNestedScroll(viewParent, view, n, n2, n3, n4, 0);
    }
    
    public static void onNestedScroll(final ViewParent obj, final View view, final int n, final int n2, final int n3, final int n4, final int n5) {
        if (obj instanceof NestedScrollingParent2) {
            ((NestedScrollingParent2)obj).onNestedScroll(view, n, n2, n3, n4, n5);
        }
        else if (n5 == 0) {
            if (Build$VERSION.SDK_INT >= 21) {
                try {
                    obj.onNestedScroll(view, n, n2, n3, n4);
                    return;
                }
                catch (AbstractMethodError abstractMethodError) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("ViewParent ");
                    sb.append(obj);
                    sb.append(" does not implement interface ");
                    sb.append("method onNestedScroll");
                    Log.e("ViewParentCompat", sb.toString(), (Throwable)abstractMethodError);
                    return;
                }
            }
            if (obj instanceof NestedScrollingParent) {
                ((NestedScrollingParent)obj).onNestedScroll(view, n, n2, n3, n4);
            }
        }
    }
    
    public static void onNestedScrollAccepted(final ViewParent viewParent, final View view, final View view2, final int n) {
        onNestedScrollAccepted(viewParent, view, view2, n, 0);
    }
    
    public static void onNestedScrollAccepted(final ViewParent obj, final View view, final View view2, final int n, final int n2) {
        if (obj instanceof NestedScrollingParent2) {
            ((NestedScrollingParent2)obj).onNestedScrollAccepted(view, view2, n, n2);
        }
        else if (n2 == 0) {
            if (Build$VERSION.SDK_INT >= 21) {
                try {
                    obj.onNestedScrollAccepted(view, view2, n);
                    return;
                }
                catch (AbstractMethodError abstractMethodError) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("ViewParent ");
                    sb.append(obj);
                    sb.append(" does not implement interface ");
                    sb.append("method onNestedScrollAccepted");
                    Log.e("ViewParentCompat", sb.toString(), (Throwable)abstractMethodError);
                    return;
                }
            }
            if (obj instanceof NestedScrollingParent) {
                ((NestedScrollingParent)obj).onNestedScrollAccepted(view, view2, n);
            }
        }
    }
    
    public static boolean onStartNestedScroll(final ViewParent viewParent, final View view, final View view2, final int n) {
        return onStartNestedScroll(viewParent, view, view2, n, 0);
    }
    
    public static boolean onStartNestedScroll(final ViewParent obj, final View view, final View view2, final int n, final int n2) {
        if (obj instanceof NestedScrollingParent2) {
            return ((NestedScrollingParent2)obj).onStartNestedScroll(view, view2, n, n2);
        }
        if (n2 == 0) {
            if (Build$VERSION.SDK_INT >= 21) {
                try {
                    return obj.onStartNestedScroll(view, view2, n);
                }
                catch (AbstractMethodError abstractMethodError) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("ViewParent ");
                    sb.append(obj);
                    sb.append(" does not implement interface ");
                    sb.append("method onStartNestedScroll");
                    Log.e("ViewParentCompat", sb.toString(), (Throwable)abstractMethodError);
                    return false;
                }
            }
            if (obj instanceof NestedScrollingParent) {
                return ((NestedScrollingParent)obj).onStartNestedScroll(view, view2, n);
            }
        }
        return false;
    }
    
    public static void onStopNestedScroll(final ViewParent viewParent, final View view) {
        onStopNestedScroll(viewParent, view, 0);
    }
    
    public static void onStopNestedScroll(final ViewParent obj, final View view, final int n) {
        if (obj instanceof NestedScrollingParent2) {
            ((NestedScrollingParent2)obj).onStopNestedScroll(view, n);
        }
        else if (n == 0) {
            if (Build$VERSION.SDK_INT >= 21) {
                try {
                    obj.onStopNestedScroll(view);
                    return;
                }
                catch (AbstractMethodError abstractMethodError) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("ViewParent ");
                    sb.append(obj);
                    sb.append(" does not implement interface ");
                    sb.append("method onStopNestedScroll");
                    Log.e("ViewParentCompat", sb.toString(), (Throwable)abstractMethodError);
                    return;
                }
            }
            if (obj instanceof NestedScrollingParent) {
                ((NestedScrollingParent)obj).onStopNestedScroll(view);
            }
        }
    }
    
    @Deprecated
    public static boolean requestSendAccessibilityEvent(final ViewParent viewParent, final View view, final AccessibilityEvent accessibilityEvent) {
        return viewParent.requestSendAccessibilityEvent(view, accessibilityEvent);
    }
}
