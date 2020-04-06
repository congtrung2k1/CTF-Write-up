// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.view;

import java.util.Iterator;
import android.util.SparseArray;
import java.lang.ref.WeakReference;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.view.View$DragShadowBuilder;
import android.content.ClipData;
import android.view.PointerIcon;
import android.view.View$OnApplyWindowInsetsListener;
import android.graphics.Paint;
import java.lang.reflect.InvocationTargetException;
import android.view.ViewGroup;
import android.graphics.drawable.Drawable;
import android.view.View$AccessibilityDelegate;
import android.animation.ValueAnimator;
import android.os.Bundle;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.accessibility.AccessibilityEvent;
import android.graphics.Matrix;
import android.view.WindowManager;
import android.view.Display;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.view.accessibility.AccessibilityNodeProvider;
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat;
import android.view.KeyEvent;
import android.view.WindowInsets;
import android.view.ViewParent;
import android.util.Log;
import java.util.ArrayList;
import android.view.View$OnUnhandledKeyEventListener;
import androidx.collection.ArrayMap;
import androidx.core.R;
import java.util.Map;
import android.os.Build$VERSION;
import java.util.Collection;
import android.view.View;
import java.util.WeakHashMap;
import android.graphics.Rect;
import java.util.concurrent.atomic.AtomicInteger;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class ViewCompat
{
    public static final int ACCESSIBILITY_LIVE_REGION_ASSERTIVE = 2;
    public static final int ACCESSIBILITY_LIVE_REGION_NONE = 0;
    public static final int ACCESSIBILITY_LIVE_REGION_POLITE = 1;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_AUTO = 0;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_NO = 2;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS = 4;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_YES = 1;
    @Deprecated
    public static final int LAYER_TYPE_HARDWARE = 2;
    @Deprecated
    public static final int LAYER_TYPE_NONE = 0;
    @Deprecated
    public static final int LAYER_TYPE_SOFTWARE = 1;
    public static final int LAYOUT_DIRECTION_INHERIT = 2;
    public static final int LAYOUT_DIRECTION_LOCALE = 3;
    public static final int LAYOUT_DIRECTION_LTR = 0;
    public static final int LAYOUT_DIRECTION_RTL = 1;
    @Deprecated
    public static final int MEASURED_HEIGHT_STATE_SHIFT = 16;
    @Deprecated
    public static final int MEASURED_SIZE_MASK = 16777215;
    @Deprecated
    public static final int MEASURED_STATE_MASK = -16777216;
    @Deprecated
    public static final int MEASURED_STATE_TOO_SMALL = 16777216;
    @Deprecated
    public static final int OVER_SCROLL_ALWAYS = 0;
    @Deprecated
    public static final int OVER_SCROLL_IF_CONTENT_SCROLLS = 1;
    @Deprecated
    public static final int OVER_SCROLL_NEVER = 2;
    public static final int SCROLL_AXIS_HORIZONTAL = 1;
    public static final int SCROLL_AXIS_NONE = 0;
    public static final int SCROLL_AXIS_VERTICAL = 2;
    public static final int SCROLL_INDICATOR_BOTTOM = 2;
    public static final int SCROLL_INDICATOR_END = 32;
    public static final int SCROLL_INDICATOR_LEFT = 4;
    public static final int SCROLL_INDICATOR_RIGHT = 8;
    public static final int SCROLL_INDICATOR_START = 16;
    public static final int SCROLL_INDICATOR_TOP = 1;
    private static final String TAG = "ViewCompat";
    public static final int TYPE_NON_TOUCH = 1;
    public static final int TYPE_TOUCH = 0;
    private static boolean sAccessibilityDelegateCheckFailed;
    private static Field sAccessibilityDelegateField;
    private static Method sChildrenDrawingOrderMethod;
    private static Method sDispatchFinishTemporaryDetach;
    private static Method sDispatchStartTemporaryDetach;
    private static Field sMinHeightField;
    private static boolean sMinHeightFieldFetched;
    private static Field sMinWidthField;
    private static boolean sMinWidthFieldFetched;
    private static final AtomicInteger sNextGeneratedId;
    private static boolean sTempDetachBound;
    private static ThreadLocal<Rect> sThreadLocalRect;
    private static WeakHashMap<View, String> sTransitionNameMap;
    private static WeakHashMap<View, ViewPropertyAnimatorCompat> sViewPropertyAnimatorMap;
    
    static {
        sNextGeneratedId = new AtomicInteger(1);
        ViewCompat.sViewPropertyAnimatorMap = null;
        ViewCompat.sAccessibilityDelegateCheckFailed = false;
    }
    
    protected ViewCompat() {
    }
    
    public static void addKeyboardNavigationClusters(final View view, final Collection<View> collection, final int n) {
        if (Build$VERSION.SDK_INT >= 26) {
            view.addKeyboardNavigationClusters((Collection)collection, n);
        }
    }
    
    public static void addOnUnhandledKeyEventListener(final View view, final OnUnhandledKeyEventListenerCompat e) {
        if (Build$VERSION.SDK_INT >= 28) {
            Map<OnUnhandledKeyEventListenerCompat, View$OnUnhandledKeyEventListener> map;
            if ((map = (Map<OnUnhandledKeyEventListenerCompat, View$OnUnhandledKeyEventListener>)view.getTag(R.id.tag_unhandled_key_listeners)) == null) {
                map = new ArrayMap<OnUnhandledKeyEventListenerCompat, View$OnUnhandledKeyEventListener>();
                view.setTag(R.id.tag_unhandled_key_listeners, (Object)map);
            }
            final OnUnhandledKeyEventListenerWrapper onUnhandledKeyEventListenerWrapper = new OnUnhandledKeyEventListenerWrapper(e);
            map.put(e, (View$OnUnhandledKeyEventListener)onUnhandledKeyEventListenerWrapper);
            view.addOnUnhandledKeyEventListener((View$OnUnhandledKeyEventListener)onUnhandledKeyEventListenerWrapper);
            return;
        }
        ArrayList<OnUnhandledKeyEventListenerCompat> list;
        if ((list = (ArrayList<OnUnhandledKeyEventListenerCompat>)view.getTag(R.id.tag_unhandled_key_listeners)) == null) {
            list = new ArrayList<OnUnhandledKeyEventListenerCompat>();
            view.setTag(R.id.tag_unhandled_key_listeners, (Object)list);
        }
        list.add(e);
        if (list.size() == 1) {
            UnhandledKeyEventManager.registerListeningView(view);
        }
    }
    
    public static ViewPropertyAnimatorCompat animate(final View view) {
        if (ViewCompat.sViewPropertyAnimatorMap == null) {
            ViewCompat.sViewPropertyAnimatorMap = new WeakHashMap<View, ViewPropertyAnimatorCompat>();
        }
        ViewPropertyAnimatorCompat value;
        if ((value = ViewCompat.sViewPropertyAnimatorMap.get(view)) == null) {
            value = new ViewPropertyAnimatorCompat(view);
            ViewCompat.sViewPropertyAnimatorMap.put(view, value);
        }
        return value;
    }
    
    private static void bindTempDetach() {
        try {
            ViewCompat.sDispatchStartTemporaryDetach = View.class.getDeclaredMethod("dispatchStartTemporaryDetach", (Class<?>[])new Class[0]);
            ViewCompat.sDispatchFinishTemporaryDetach = View.class.getDeclaredMethod("dispatchFinishTemporaryDetach", (Class<?>[])new Class[0]);
        }
        catch (NoSuchMethodException ex) {
            Log.e("ViewCompat", "Couldn't find method", (Throwable)ex);
        }
        ViewCompat.sTempDetachBound = true;
    }
    
    @Deprecated
    public static boolean canScrollHorizontally(final View view, final int n) {
        return view.canScrollHorizontally(n);
    }
    
    @Deprecated
    public static boolean canScrollVertically(final View view, final int n) {
        return view.canScrollVertically(n);
    }
    
    public static void cancelDragAndDrop(final View view) {
        if (Build$VERSION.SDK_INT >= 24) {
            view.cancelDragAndDrop();
        }
    }
    
    @Deprecated
    public static int combineMeasuredStates(final int n, final int n2) {
        return View.combineMeasuredStates(n, n2);
    }
    
    private static void compatOffsetLeftAndRight(final View view, final int n) {
        view.offsetLeftAndRight(n);
        if (view.getVisibility() == 0) {
            tickleInvalidationFlag(view);
            final ViewParent parent = view.getParent();
            if (parent instanceof View) {
                tickleInvalidationFlag((View)parent);
            }
        }
    }
    
    private static void compatOffsetTopAndBottom(final View view, final int n) {
        view.offsetTopAndBottom(n);
        if (view.getVisibility() == 0) {
            tickleInvalidationFlag(view);
            final ViewParent parent = view.getParent();
            if (parent instanceof View) {
                tickleInvalidationFlag((View)parent);
            }
        }
    }
    
    public static WindowInsetsCompat dispatchApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
        if (Build$VERSION.SDK_INT >= 21) {
            final WindowInsets windowInsets = (WindowInsets)WindowInsetsCompat.unwrap(windowInsetsCompat);
            final WindowInsets dispatchApplyWindowInsets = view.dispatchApplyWindowInsets(windowInsets);
            WindowInsets windowInsets2;
            if (dispatchApplyWindowInsets != (windowInsets2 = windowInsets)) {
                windowInsets2 = new WindowInsets(dispatchApplyWindowInsets);
            }
            return WindowInsetsCompat.wrap(windowInsets2);
        }
        return windowInsetsCompat;
    }
    
    public static void dispatchFinishTemporaryDetach(final View obj) {
        if (Build$VERSION.SDK_INT >= 24) {
            obj.dispatchFinishTemporaryDetach();
        }
        else {
            if (!ViewCompat.sTempDetachBound) {
                bindTempDetach();
            }
            final Method sDispatchFinishTemporaryDetach = ViewCompat.sDispatchFinishTemporaryDetach;
            if (sDispatchFinishTemporaryDetach != null) {
                try {
                    sDispatchFinishTemporaryDetach.invoke(obj, new Object[0]);
                }
                catch (Exception ex) {
                    Log.d("ViewCompat", "Error calling dispatchFinishTemporaryDetach", (Throwable)ex);
                }
            }
            else {
                obj.onFinishTemporaryDetach();
            }
        }
    }
    
    public static boolean dispatchNestedFling(final View view, final float n, final float n2, final boolean b) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.dispatchNestedFling(n, n2, b);
        }
        return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).dispatchNestedFling(n, n2, b);
    }
    
    public static boolean dispatchNestedPreFling(final View view, final float n, final float n2) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.dispatchNestedPreFling(n, n2);
        }
        return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).dispatchNestedPreFling(n, n2);
    }
    
    public static boolean dispatchNestedPreScroll(final View view, final int n, final int n2, final int[] array, final int[] array2) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.dispatchNestedPreScroll(n, n2, array, array2);
        }
        return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).dispatchNestedPreScroll(n, n2, array, array2);
    }
    
    public static boolean dispatchNestedPreScroll(final View view, final int n, final int n2, final int[] array, final int[] array2, final int n3) {
        if (view instanceof NestedScrollingChild2) {
            return ((NestedScrollingChild2)view).dispatchNestedPreScroll(n, n2, array, array2, n3);
        }
        return n3 == 0 && dispatchNestedPreScroll(view, n, n2, array, array2);
    }
    
    public static boolean dispatchNestedScroll(final View view, final int n, final int n2, final int n3, final int n4, final int[] array) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.dispatchNestedScroll(n, n2, n3, n4, array);
        }
        return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).dispatchNestedScroll(n, n2, n3, n4, array);
    }
    
    public static boolean dispatchNestedScroll(final View view, final int n, final int n2, final int n3, final int n4, final int[] array, final int n5) {
        if (view instanceof NestedScrollingChild2) {
            return ((NestedScrollingChild2)view).dispatchNestedScroll(n, n2, n3, n4, array, n5);
        }
        return n5 == 0 && dispatchNestedScroll(view, n, n2, n3, n4, array);
    }
    
    public static void dispatchStartTemporaryDetach(final View obj) {
        if (Build$VERSION.SDK_INT >= 24) {
            obj.dispatchStartTemporaryDetach();
        }
        else {
            if (!ViewCompat.sTempDetachBound) {
                bindTempDetach();
            }
            final Method sDispatchStartTemporaryDetach = ViewCompat.sDispatchStartTemporaryDetach;
            if (sDispatchStartTemporaryDetach != null) {
                try {
                    sDispatchStartTemporaryDetach.invoke(obj, new Object[0]);
                }
                catch (Exception ex) {
                    Log.d("ViewCompat", "Error calling dispatchStartTemporaryDetach", (Throwable)ex);
                }
            }
            else {
                obj.onStartTemporaryDetach();
            }
        }
    }
    
    static boolean dispatchUnhandledKeyEventBeforeCallback(final View view, final KeyEvent keyEvent) {
        return Build$VERSION.SDK_INT < 28 && UnhandledKeyEventManager.at(view).dispatch(view, keyEvent);
    }
    
    static boolean dispatchUnhandledKeyEventBeforeHierarchy(final View view, final KeyEvent keyEvent) {
        return Build$VERSION.SDK_INT < 28 && UnhandledKeyEventManager.at(view).preDispatch(keyEvent);
    }
    
    public static int generateViewId() {
        if (Build$VERSION.SDK_INT >= 17) {
            return View.generateViewId();
        }
        int value;
        int newValue;
        do {
            value = ViewCompat.sNextGeneratedId.get();
            if ((newValue = value + 1) > 16777215) {
                newValue = 1;
            }
        } while (!ViewCompat.sNextGeneratedId.compareAndSet(value, newValue));
        return value;
    }
    
    public static int getAccessibilityLiveRegion(final View view) {
        if (Build$VERSION.SDK_INT >= 19) {
            return view.getAccessibilityLiveRegion();
        }
        return 0;
    }
    
    public static AccessibilityNodeProviderCompat getAccessibilityNodeProvider(final View view) {
        if (Build$VERSION.SDK_INT >= 16) {
            final AccessibilityNodeProvider accessibilityNodeProvider = view.getAccessibilityNodeProvider();
            if (accessibilityNodeProvider != null) {
                return new AccessibilityNodeProviderCompat(accessibilityNodeProvider);
            }
        }
        return null;
    }
    
    @Deprecated
    public static float getAlpha(final View view) {
        return view.getAlpha();
    }
    
    public static ColorStateList getBackgroundTintList(final View view) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.getBackgroundTintList();
        }
        ColorStateList supportBackgroundTintList;
        if (view instanceof TintableBackgroundView) {
            supportBackgroundTintList = ((TintableBackgroundView)view).getSupportBackgroundTintList();
        }
        else {
            supportBackgroundTintList = null;
        }
        return supportBackgroundTintList;
    }
    
    public static PorterDuff$Mode getBackgroundTintMode(final View view) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.getBackgroundTintMode();
        }
        PorterDuff$Mode supportBackgroundTintMode;
        if (view instanceof TintableBackgroundView) {
            supportBackgroundTintMode = ((TintableBackgroundView)view).getSupportBackgroundTintMode();
        }
        else {
            supportBackgroundTintMode = null;
        }
        return supportBackgroundTintMode;
    }
    
    public static Rect getClipBounds(final View view) {
        if (Build$VERSION.SDK_INT >= 18) {
            return view.getClipBounds();
        }
        return null;
    }
    
    public static Display getDisplay(final View view) {
        if (Build$VERSION.SDK_INT >= 17) {
            return view.getDisplay();
        }
        if (isAttachedToWindow(view)) {
            return ((WindowManager)view.getContext().getSystemService("window")).getDefaultDisplay();
        }
        return null;
    }
    
    public static float getElevation(final View view) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.getElevation();
        }
        return 0.0f;
    }
    
    private static Rect getEmptyTempRect() {
        if (ViewCompat.sThreadLocalRect == null) {
            ViewCompat.sThreadLocalRect = new ThreadLocal<Rect>();
        }
        Rect value;
        if ((value = ViewCompat.sThreadLocalRect.get()) == null) {
            value = new Rect();
            ViewCompat.sThreadLocalRect.set(value);
        }
        value.setEmpty();
        return value;
    }
    
    public static boolean getFitsSystemWindows(final View view) {
        return Build$VERSION.SDK_INT >= 16 && view.getFitsSystemWindows();
    }
    
    public static int getImportantForAccessibility(final View view) {
        if (Build$VERSION.SDK_INT >= 16) {
            return view.getImportantForAccessibility();
        }
        return 0;
    }
    
    public static int getImportantForAutofill(final View view) {
        if (Build$VERSION.SDK_INT >= 26) {
            return view.getImportantForAutofill();
        }
        return 0;
    }
    
    public static int getLabelFor(final View view) {
        if (Build$VERSION.SDK_INT >= 17) {
            return view.getLabelFor();
        }
        return 0;
    }
    
    @Deprecated
    public static int getLayerType(final View view) {
        return view.getLayerType();
    }
    
    public static int getLayoutDirection(final View view) {
        if (Build$VERSION.SDK_INT >= 17) {
            return view.getLayoutDirection();
        }
        return 0;
    }
    
    @Deprecated
    public static Matrix getMatrix(final View view) {
        return view.getMatrix();
    }
    
    @Deprecated
    public static int getMeasuredHeightAndState(final View view) {
        return view.getMeasuredHeightAndState();
    }
    
    @Deprecated
    public static int getMeasuredState(final View view) {
        return view.getMeasuredState();
    }
    
    @Deprecated
    public static int getMeasuredWidthAndState(final View view) {
        return view.getMeasuredWidthAndState();
    }
    
    public static int getMinimumHeight(final View obj) {
        if (Build$VERSION.SDK_INT >= 16) {
            return obj.getMinimumHeight();
        }
        if (!ViewCompat.sMinHeightFieldFetched) {
            try {
                (ViewCompat.sMinHeightField = View.class.getDeclaredField("mMinHeight")).setAccessible(true);
            }
            catch (NoSuchFieldException ex) {}
            ViewCompat.sMinHeightFieldFetched = true;
        }
        final Field sMinHeightField = ViewCompat.sMinHeightField;
        if (sMinHeightField != null) {
            try {
                return (int)sMinHeightField.get(obj);
            }
            catch (Exception ex2) {}
        }
        return 0;
    }
    
    public static int getMinimumWidth(final View obj) {
        if (Build$VERSION.SDK_INT >= 16) {
            return obj.getMinimumWidth();
        }
        if (!ViewCompat.sMinWidthFieldFetched) {
            try {
                (ViewCompat.sMinWidthField = View.class.getDeclaredField("mMinWidth")).setAccessible(true);
            }
            catch (NoSuchFieldException ex) {}
            ViewCompat.sMinWidthFieldFetched = true;
        }
        final Field sMinWidthField = ViewCompat.sMinWidthField;
        if (sMinWidthField != null) {
            try {
                return (int)sMinWidthField.get(obj);
            }
            catch (Exception ex2) {}
        }
        return 0;
    }
    
    public static int getNextClusterForwardId(final View view) {
        if (Build$VERSION.SDK_INT >= 26) {
            return view.getNextClusterForwardId();
        }
        return -1;
    }
    
    @Deprecated
    public static int getOverScrollMode(final View view) {
        return view.getOverScrollMode();
    }
    
    public static int getPaddingEnd(final View view) {
        if (Build$VERSION.SDK_INT >= 17) {
            return view.getPaddingEnd();
        }
        return view.getPaddingRight();
    }
    
    public static int getPaddingStart(final View view) {
        if (Build$VERSION.SDK_INT >= 17) {
            return view.getPaddingStart();
        }
        return view.getPaddingLeft();
    }
    
    public static ViewParent getParentForAccessibility(final View view) {
        if (Build$VERSION.SDK_INT >= 16) {
            return view.getParentForAccessibility();
        }
        return view.getParent();
    }
    
    @Deprecated
    public static float getPivotX(final View view) {
        return view.getPivotX();
    }
    
    @Deprecated
    public static float getPivotY(final View view) {
        return view.getPivotY();
    }
    
    @Deprecated
    public static float getRotation(final View view) {
        return view.getRotation();
    }
    
    @Deprecated
    public static float getRotationX(final View view) {
        return view.getRotationX();
    }
    
    @Deprecated
    public static float getRotationY(final View view) {
        return view.getRotationY();
    }
    
    @Deprecated
    public static float getScaleX(final View view) {
        return view.getScaleX();
    }
    
    @Deprecated
    public static float getScaleY(final View view) {
        return view.getScaleY();
    }
    
    public static int getScrollIndicators(final View view) {
        if (Build$VERSION.SDK_INT >= 23) {
            return view.getScrollIndicators();
        }
        return 0;
    }
    
    public static String getTransitionName(final View key) {
        if (Build$VERSION.SDK_INT >= 21) {
            return key.getTransitionName();
        }
        final WeakHashMap<View, String> sTransitionNameMap = ViewCompat.sTransitionNameMap;
        if (sTransitionNameMap == null) {
            return null;
        }
        return sTransitionNameMap.get(key);
    }
    
    @Deprecated
    public static float getTranslationX(final View view) {
        return view.getTranslationX();
    }
    
    @Deprecated
    public static float getTranslationY(final View view) {
        return view.getTranslationY();
    }
    
    public static float getTranslationZ(final View view) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.getTranslationZ();
        }
        return 0.0f;
    }
    
    public static int getWindowSystemUiVisibility(final View view) {
        if (Build$VERSION.SDK_INT >= 16) {
            return view.getWindowSystemUiVisibility();
        }
        return 0;
    }
    
    @Deprecated
    public static float getX(final View view) {
        return view.getX();
    }
    
    @Deprecated
    public static float getY(final View view) {
        return view.getY();
    }
    
    public static float getZ(final View view) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.getZ();
        }
        return 0.0f;
    }
    
    public static boolean hasAccessibilityDelegate(final View obj) {
        final boolean sAccessibilityDelegateCheckFailed = ViewCompat.sAccessibilityDelegateCheckFailed;
        boolean b = false;
        if (sAccessibilityDelegateCheckFailed) {
            return false;
        }
        if (ViewCompat.sAccessibilityDelegateField == null) {
            try {
                (ViewCompat.sAccessibilityDelegateField = View.class.getDeclaredField("mAccessibilityDelegate")).setAccessible(true);
            }
            finally {
                ViewCompat.sAccessibilityDelegateCheckFailed = true;
                return false;
            }
        }
        try {
            if (ViewCompat.sAccessibilityDelegateField.get(obj) != null) {
                b = true;
            }
            return b;
        }
        finally {
            ViewCompat.sAccessibilityDelegateCheckFailed = true;
            return false;
        }
    }
    
    public static boolean hasExplicitFocusable(final View view) {
        if (Build$VERSION.SDK_INT >= 26) {
            return view.hasExplicitFocusable();
        }
        return view.hasFocusable();
    }
    
    public static boolean hasNestedScrollingParent(final View view) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.hasNestedScrollingParent();
        }
        return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).hasNestedScrollingParent();
    }
    
    public static boolean hasNestedScrollingParent(final View view, final int n) {
        if (view instanceof NestedScrollingChild2) {
            ((NestedScrollingChild2)view).hasNestedScrollingParent(n);
        }
        else if (n == 0) {
            return hasNestedScrollingParent(view);
        }
        return false;
    }
    
    public static boolean hasOnClickListeners(final View view) {
        return Build$VERSION.SDK_INT >= 15 && view.hasOnClickListeners();
    }
    
    public static boolean hasOverlappingRendering(final View view) {
        return Build$VERSION.SDK_INT < 16 || view.hasOverlappingRendering();
    }
    
    public static boolean hasTransientState(final View view) {
        return Build$VERSION.SDK_INT >= 16 && view.hasTransientState();
    }
    
    public static boolean isAttachedToWindow(final View view) {
        if (Build$VERSION.SDK_INT >= 19) {
            return view.isAttachedToWindow();
        }
        return view.getWindowToken() != null;
    }
    
    public static boolean isFocusedByDefault(final View view) {
        return Build$VERSION.SDK_INT >= 26 && view.isFocusedByDefault();
    }
    
    public static boolean isImportantForAccessibility(final View view) {
        return Build$VERSION.SDK_INT < 21 || view.isImportantForAccessibility();
    }
    
    public static boolean isImportantForAutofill(final View view) {
        return Build$VERSION.SDK_INT < 26 || view.isImportantForAutofill();
    }
    
    public static boolean isInLayout(final View view) {
        return Build$VERSION.SDK_INT >= 18 && view.isInLayout();
    }
    
    public static boolean isKeyboardNavigationCluster(final View view) {
        return Build$VERSION.SDK_INT >= 26 && view.isKeyboardNavigationCluster();
    }
    
    public static boolean isLaidOut(final View view) {
        if (Build$VERSION.SDK_INT >= 19) {
            return view.isLaidOut();
        }
        return view.getWidth() > 0 && view.getHeight() > 0;
    }
    
    public static boolean isLayoutDirectionResolved(final View view) {
        return Build$VERSION.SDK_INT >= 19 && view.isLayoutDirectionResolved();
    }
    
    public static boolean isNestedScrollingEnabled(final View view) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.isNestedScrollingEnabled();
        }
        return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).isNestedScrollingEnabled();
    }
    
    @Deprecated
    public static boolean isOpaque(final View view) {
        return view.isOpaque();
    }
    
    public static boolean isPaddingRelative(final View view) {
        return Build$VERSION.SDK_INT >= 17 && view.isPaddingRelative();
    }
    
    @Deprecated
    public static void jumpDrawablesToCurrentState(final View view) {
        view.jumpDrawablesToCurrentState();
    }
    
    public static View keyboardNavigationClusterSearch(final View view, final View view2, final int n) {
        if (Build$VERSION.SDK_INT >= 26) {
            return view.keyboardNavigationClusterSearch(view2, n);
        }
        return null;
    }
    
    public static void offsetLeftAndRight(final View view, final int n) {
        if (Build$VERSION.SDK_INT >= 23) {
            view.offsetLeftAndRight(n);
        }
        else if (Build$VERSION.SDK_INT >= 21) {
            final Rect emptyTempRect = getEmptyTempRect();
            boolean b = false;
            final ViewParent parent = view.getParent();
            if (parent instanceof View) {
                final View view2 = (View)parent;
                emptyTempRect.set(view2.getLeft(), view2.getTop(), view2.getRight(), view2.getBottom());
                b = (emptyTempRect.intersects(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()) ^ true);
            }
            compatOffsetLeftAndRight(view, n);
            if (b && emptyTempRect.intersect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())) {
                ((View)parent).invalidate(emptyTempRect);
            }
        }
        else {
            compatOffsetLeftAndRight(view, n);
        }
    }
    
    public static void offsetTopAndBottom(final View view, final int n) {
        if (Build$VERSION.SDK_INT >= 23) {
            view.offsetTopAndBottom(n);
        }
        else if (Build$VERSION.SDK_INT >= 21) {
            final Rect emptyTempRect = getEmptyTempRect();
            boolean b = false;
            final ViewParent parent = view.getParent();
            if (parent instanceof View) {
                final View view2 = (View)parent;
                emptyTempRect.set(view2.getLeft(), view2.getTop(), view2.getRight(), view2.getBottom());
                b = (emptyTempRect.intersects(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()) ^ true);
            }
            compatOffsetTopAndBottom(view, n);
            if (b && emptyTempRect.intersect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())) {
                ((View)parent).invalidate(emptyTempRect);
            }
        }
        else {
            compatOffsetTopAndBottom(view, n);
        }
    }
    
    public static WindowInsetsCompat onApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
        if (Build$VERSION.SDK_INT >= 21) {
            final WindowInsets windowInsets = (WindowInsets)WindowInsetsCompat.unwrap(windowInsetsCompat);
            final WindowInsets onApplyWindowInsets = view.onApplyWindowInsets(windowInsets);
            WindowInsets windowInsets2;
            if (onApplyWindowInsets != (windowInsets2 = windowInsets)) {
                windowInsets2 = new WindowInsets(onApplyWindowInsets);
            }
            return WindowInsetsCompat.wrap(windowInsets2);
        }
        return windowInsetsCompat;
    }
    
    @Deprecated
    public static void onInitializeAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
        view.onInitializeAccessibilityEvent(accessibilityEvent);
    }
    
    public static void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        view.onInitializeAccessibilityNodeInfo(accessibilityNodeInfoCompat.unwrap());
    }
    
    @Deprecated
    public static void onPopulateAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
        view.onPopulateAccessibilityEvent(accessibilityEvent);
    }
    
    public static boolean performAccessibilityAction(final View view, final int n, final Bundle bundle) {
        return Build$VERSION.SDK_INT >= 16 && view.performAccessibilityAction(n, bundle);
    }
    
    public static void postInvalidateOnAnimation(final View view) {
        if (Build$VERSION.SDK_INT >= 16) {
            view.postInvalidateOnAnimation();
        }
        else {
            view.postInvalidate();
        }
    }
    
    public static void postInvalidateOnAnimation(final View view, final int n, final int n2, final int n3, final int n4) {
        if (Build$VERSION.SDK_INT >= 16) {
            view.postInvalidateOnAnimation(n, n2, n3, n4);
        }
        else {
            view.postInvalidate(n, n2, n3, n4);
        }
    }
    
    public static void postOnAnimation(final View view, final Runnable runnable) {
        if (Build$VERSION.SDK_INT >= 16) {
            view.postOnAnimation(runnable);
        }
        else {
            view.postDelayed(runnable, ValueAnimator.getFrameDelay());
        }
    }
    
    public static void postOnAnimationDelayed(final View view, final Runnable runnable, final long n) {
        if (Build$VERSION.SDK_INT >= 16) {
            view.postOnAnimationDelayed(runnable, n);
        }
        else {
            view.postDelayed(runnable, ValueAnimator.getFrameDelay() + n);
        }
    }
    
    public static void removeOnUnhandledKeyEventListener(final View view, final OnUnhandledKeyEventListenerCompat o) {
        if (Build$VERSION.SDK_INT < 28) {
            final ArrayList list = (ArrayList)view.getTag(R.id.tag_unhandled_key_listeners);
            if (list != null) {
                list.remove(o);
                if (list.size() == 0) {
                    UnhandledKeyEventManager.unregisterListeningView(view);
                }
            }
            return;
        }
        final Map map = (Map)view.getTag(R.id.tag_unhandled_key_listeners);
        if (map == null) {
            return;
        }
        final View$OnUnhandledKeyEventListener view$OnUnhandledKeyEventListener = map.get(o);
        if (view$OnUnhandledKeyEventListener != null) {
            view.removeOnUnhandledKeyEventListener(view$OnUnhandledKeyEventListener);
        }
    }
    
    public static void requestApplyInsets(final View view) {
        if (Build$VERSION.SDK_INT >= 20) {
            view.requestApplyInsets();
        }
        else if (Build$VERSION.SDK_INT >= 16) {
            view.requestFitSystemWindows();
        }
    }
    
    public static <T extends View> T requireViewById(View viewById, final int n) {
        if (Build$VERSION.SDK_INT >= 28) {
            return (T)viewById.requireViewById(n);
        }
        viewById = viewById.findViewById(n);
        if (viewById != null) {
            return (T)viewById;
        }
        throw new IllegalArgumentException("ID does not reference a View inside this View");
    }
    
    @Deprecated
    public static int resolveSizeAndState(final int n, final int n2, final int n3) {
        return View.resolveSizeAndState(n, n2, n3);
    }
    
    public static boolean restoreDefaultFocus(final View view) {
        if (Build$VERSION.SDK_INT >= 26) {
            return view.restoreDefaultFocus();
        }
        return view.requestFocus();
    }
    
    public static void setAccessibilityDelegate(final View view, final AccessibilityDelegateCompat accessibilityDelegateCompat) {
        View$AccessibilityDelegate bridge;
        if (accessibilityDelegateCompat == null) {
            bridge = null;
        }
        else {
            bridge = accessibilityDelegateCompat.getBridge();
        }
        view.setAccessibilityDelegate(bridge);
    }
    
    public static void setAccessibilityLiveRegion(final View view, final int accessibilityLiveRegion) {
        if (Build$VERSION.SDK_INT >= 19) {
            view.setAccessibilityLiveRegion(accessibilityLiveRegion);
        }
    }
    
    @Deprecated
    public static void setActivated(final View view, final boolean activated) {
        view.setActivated(activated);
    }
    
    @Deprecated
    public static void setAlpha(final View view, final float alpha) {
        view.setAlpha(alpha);
    }
    
    public static void setAutofillHints(final View view, final String... autofillHints) {
        if (Build$VERSION.SDK_INT >= 26) {
            view.setAutofillHints(autofillHints);
        }
    }
    
    public static void setBackground(final View view, final Drawable drawable) {
        if (Build$VERSION.SDK_INT >= 16) {
            view.setBackground(drawable);
        }
        else {
            view.setBackgroundDrawable(drawable);
        }
    }
    
    public static void setBackgroundTintList(final View view, final ColorStateList list) {
        if (Build$VERSION.SDK_INT >= 21) {
            view.setBackgroundTintList(list);
            if (Build$VERSION.SDK_INT == 21) {
                final Drawable background = view.getBackground();
                final boolean b = view.getBackgroundTintList() != null || view.getBackgroundTintMode() != null;
                if (background != null && b) {
                    if (background.isStateful()) {
                        background.setState(view.getDrawableState());
                    }
                    view.setBackground(background);
                }
            }
        }
        else if (view instanceof TintableBackgroundView) {
            ((TintableBackgroundView)view).setSupportBackgroundTintList(list);
        }
    }
    
    public static void setBackgroundTintMode(final View view, final PorterDuff$Mode porterDuff$Mode) {
        if (Build$VERSION.SDK_INT >= 21) {
            view.setBackgroundTintMode(porterDuff$Mode);
            if (Build$VERSION.SDK_INT == 21) {
                final Drawable background = view.getBackground();
                final boolean b = view.getBackgroundTintList() != null || view.getBackgroundTintMode() != null;
                if (background != null && b) {
                    if (background.isStateful()) {
                        background.setState(view.getDrawableState());
                    }
                    view.setBackground(background);
                }
            }
        }
        else if (view instanceof TintableBackgroundView) {
            ((TintableBackgroundView)view).setSupportBackgroundTintMode(porterDuff$Mode);
        }
    }
    
    @Deprecated
    public static void setChildrenDrawingOrderEnabled(final ViewGroup obj, final boolean b) {
        if (ViewCompat.sChildrenDrawingOrderMethod == null) {
            try {
                ViewCompat.sChildrenDrawingOrderMethod = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", Boolean.TYPE);
            }
            catch (NoSuchMethodException ex) {
                Log.e("ViewCompat", "Unable to find childrenDrawingOrderEnabled", (Throwable)ex);
            }
            ViewCompat.sChildrenDrawingOrderMethod.setAccessible(true);
        }
        try {
            ViewCompat.sChildrenDrawingOrderMethod.invoke(obj, b);
        }
        catch (InvocationTargetException ex2) {
            Log.e("ViewCompat", "Unable to invoke childrenDrawingOrderEnabled", (Throwable)ex2);
        }
        catch (IllegalArgumentException ex3) {
            Log.e("ViewCompat", "Unable to invoke childrenDrawingOrderEnabled", (Throwable)ex3);
        }
        catch (IllegalAccessException ex4) {
            Log.e("ViewCompat", "Unable to invoke childrenDrawingOrderEnabled", (Throwable)ex4);
        }
    }
    
    public static void setClipBounds(final View view, final Rect clipBounds) {
        if (Build$VERSION.SDK_INT >= 18) {
            view.setClipBounds(clipBounds);
        }
    }
    
    public static void setElevation(final View view, final float elevation) {
        if (Build$VERSION.SDK_INT >= 21) {
            view.setElevation(elevation);
        }
    }
    
    @Deprecated
    public static void setFitsSystemWindows(final View view, final boolean fitsSystemWindows) {
        view.setFitsSystemWindows(fitsSystemWindows);
    }
    
    public static void setFocusedByDefault(final View view, final boolean focusedByDefault) {
        if (Build$VERSION.SDK_INT >= 26) {
            view.setFocusedByDefault(focusedByDefault);
        }
    }
    
    public static void setHasTransientState(final View view, final boolean hasTransientState) {
        if (Build$VERSION.SDK_INT >= 16) {
            view.setHasTransientState(hasTransientState);
        }
    }
    
    public static void setImportantForAccessibility(final View view, final int importantForAccessibility) {
        if (Build$VERSION.SDK_INT >= 19) {
            view.setImportantForAccessibility(importantForAccessibility);
        }
        else if (Build$VERSION.SDK_INT >= 16) {
            int importantForAccessibility2;
            if ((importantForAccessibility2 = importantForAccessibility) == 4) {
                importantForAccessibility2 = 2;
            }
            view.setImportantForAccessibility(importantForAccessibility2);
        }
    }
    
    public static void setImportantForAutofill(final View view, final int importantForAutofill) {
        if (Build$VERSION.SDK_INT >= 26) {
            view.setImportantForAutofill(importantForAutofill);
        }
    }
    
    public static void setKeyboardNavigationCluster(final View view, final boolean keyboardNavigationCluster) {
        if (Build$VERSION.SDK_INT >= 26) {
            view.setKeyboardNavigationCluster(keyboardNavigationCluster);
        }
    }
    
    public static void setLabelFor(final View view, final int labelFor) {
        if (Build$VERSION.SDK_INT >= 17) {
            view.setLabelFor(labelFor);
        }
    }
    
    public static void setLayerPaint(final View view, final Paint layerPaint) {
        if (Build$VERSION.SDK_INT >= 17) {
            view.setLayerPaint(layerPaint);
        }
        else {
            view.setLayerType(view.getLayerType(), layerPaint);
            view.invalidate();
        }
    }
    
    @Deprecated
    public static void setLayerType(final View view, final int n, final Paint paint) {
        view.setLayerType(n, paint);
    }
    
    public static void setLayoutDirection(final View view, final int layoutDirection) {
        if (Build$VERSION.SDK_INT >= 17) {
            view.setLayoutDirection(layoutDirection);
        }
    }
    
    public static void setNestedScrollingEnabled(final View view, final boolean b) {
        if (Build$VERSION.SDK_INT >= 21) {
            view.setNestedScrollingEnabled(b);
        }
        else if (view instanceof NestedScrollingChild) {
            ((NestedScrollingChild)view).setNestedScrollingEnabled(b);
        }
    }
    
    public static void setNextClusterForwardId(final View view, final int nextClusterForwardId) {
        if (Build$VERSION.SDK_INT >= 26) {
            view.setNextClusterForwardId(nextClusterForwardId);
        }
    }
    
    public static void setOnApplyWindowInsetsListener(final View view, final OnApplyWindowInsetsListener onApplyWindowInsetsListener) {
        if (Build$VERSION.SDK_INT >= 21) {
            if (onApplyWindowInsetsListener == null) {
                view.setOnApplyWindowInsetsListener((View$OnApplyWindowInsetsListener)null);
                return;
            }
            view.setOnApplyWindowInsetsListener((View$OnApplyWindowInsetsListener)new View$OnApplyWindowInsetsListener() {
                public WindowInsets onApplyWindowInsets(final View view, final WindowInsets windowInsets) {
                    return (WindowInsets)WindowInsetsCompat.unwrap(onApplyWindowInsetsListener.onApplyWindowInsets(view, WindowInsetsCompat.wrap(windowInsets)));
                }
            });
        }
    }
    
    @Deprecated
    public static void setOverScrollMode(final View view, final int overScrollMode) {
        view.setOverScrollMode(overScrollMode);
    }
    
    public static void setPaddingRelative(final View view, final int n, final int n2, final int n3, final int n4) {
        if (Build$VERSION.SDK_INT >= 17) {
            view.setPaddingRelative(n, n2, n3, n4);
        }
        else {
            view.setPadding(n, n2, n3, n4);
        }
    }
    
    @Deprecated
    public static void setPivotX(final View view, final float pivotX) {
        view.setPivotX(pivotX);
    }
    
    @Deprecated
    public static void setPivotY(final View view, final float pivotY) {
        view.setPivotY(pivotY);
    }
    
    public static void setPointerIcon(final View view, final PointerIconCompat pointerIconCompat) {
        if (Build$VERSION.SDK_INT >= 24) {
            Object pointerIcon;
            if (pointerIconCompat != null) {
                pointerIcon = pointerIconCompat.getPointerIcon();
            }
            else {
                pointerIcon = null;
            }
            view.setPointerIcon((PointerIcon)pointerIcon);
        }
    }
    
    @Deprecated
    public static void setRotation(final View view, final float rotation) {
        view.setRotation(rotation);
    }
    
    @Deprecated
    public static void setRotationX(final View view, final float rotationX) {
        view.setRotationX(rotationX);
    }
    
    @Deprecated
    public static void setRotationY(final View view, final float rotationY) {
        view.setRotationY(rotationY);
    }
    
    @Deprecated
    public static void setSaveFromParentEnabled(final View view, final boolean saveFromParentEnabled) {
        view.setSaveFromParentEnabled(saveFromParentEnabled);
    }
    
    @Deprecated
    public static void setScaleX(final View view, final float scaleX) {
        view.setScaleX(scaleX);
    }
    
    @Deprecated
    public static void setScaleY(final View view, final float scaleY) {
        view.setScaleY(scaleY);
    }
    
    public static void setScrollIndicators(final View view, final int scrollIndicators) {
        if (Build$VERSION.SDK_INT >= 23) {
            view.setScrollIndicators(scrollIndicators);
        }
    }
    
    public static void setScrollIndicators(final View view, final int n, final int n2) {
        if (Build$VERSION.SDK_INT >= 23) {
            view.setScrollIndicators(n, n2);
        }
    }
    
    public static void setTooltipText(final View view, final CharSequence tooltipText) {
        if (Build$VERSION.SDK_INT >= 26) {
            view.setTooltipText(tooltipText);
        }
    }
    
    public static void setTransitionName(final View key, final String s) {
        if (Build$VERSION.SDK_INT >= 21) {
            key.setTransitionName(s);
        }
        else {
            if (ViewCompat.sTransitionNameMap == null) {
                ViewCompat.sTransitionNameMap = new WeakHashMap<View, String>();
            }
            ViewCompat.sTransitionNameMap.put(key, s);
        }
    }
    
    @Deprecated
    public static void setTranslationX(final View view, final float translationX) {
        view.setTranslationX(translationX);
    }
    
    @Deprecated
    public static void setTranslationY(final View view, final float translationY) {
        view.setTranslationY(translationY);
    }
    
    public static void setTranslationZ(final View view, final float translationZ) {
        if (Build$VERSION.SDK_INT >= 21) {
            view.setTranslationZ(translationZ);
        }
    }
    
    @Deprecated
    public static void setX(final View view, final float x) {
        view.setX(x);
    }
    
    @Deprecated
    public static void setY(final View view, final float y) {
        view.setY(y);
    }
    
    public static void setZ(final View view, final float z) {
        if (Build$VERSION.SDK_INT >= 21) {
            view.setZ(z);
        }
    }
    
    public static boolean startDragAndDrop(final View view, final ClipData clipData, final View$DragShadowBuilder view$DragShadowBuilder, final Object o, final int n) {
        if (Build$VERSION.SDK_INT >= 24) {
            return view.startDragAndDrop(clipData, view$DragShadowBuilder, o, n);
        }
        return view.startDrag(clipData, view$DragShadowBuilder, o, n);
    }
    
    public static boolean startNestedScroll(final View view, final int n) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.startNestedScroll(n);
        }
        return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).startNestedScroll(n);
    }
    
    public static boolean startNestedScroll(final View view, final int n, final int n2) {
        if (view instanceof NestedScrollingChild2) {
            return ((NestedScrollingChild2)view).startNestedScroll(n, n2);
        }
        return n2 == 0 && startNestedScroll(view, n);
    }
    
    public static void stopNestedScroll(final View view) {
        if (Build$VERSION.SDK_INT >= 21) {
            view.stopNestedScroll();
        }
        else if (view instanceof NestedScrollingChild) {
            ((NestedScrollingChild)view).stopNestedScroll();
        }
    }
    
    public static void stopNestedScroll(final View view, final int n) {
        if (view instanceof NestedScrollingChild2) {
            ((NestedScrollingChild2)view).stopNestedScroll(n);
        }
        else if (n == 0) {
            stopNestedScroll(view);
        }
    }
    
    private static void tickleInvalidationFlag(final View view) {
        final float translationY = view.getTranslationY();
        view.setTranslationY(1.0f + translationY);
        view.setTranslationY(translationY);
    }
    
    public static void updateDragShadow(final View view, final View$DragShadowBuilder view$DragShadowBuilder) {
        if (Build$VERSION.SDK_INT >= 24) {
            view.updateDragShadow(view$DragShadowBuilder);
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface FocusDirection {
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface FocusRealDirection {
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface FocusRelativeDirection {
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface NestedScrollType {
    }
    
    public interface OnUnhandledKeyEventListenerCompat
    {
        boolean onUnhandledKeyEvent(final View p0, final KeyEvent p1);
    }
    
    private static class OnUnhandledKeyEventListenerWrapper implements View$OnUnhandledKeyEventListener
    {
        private OnUnhandledKeyEventListenerCompat mCompatListener;
        
        OnUnhandledKeyEventListenerWrapper(final OnUnhandledKeyEventListenerCompat mCompatListener) {
            this.mCompatListener = mCompatListener;
        }
        
        public boolean onUnhandledKeyEvent(final View view, final KeyEvent keyEvent) {
            return this.mCompatListener.onUnhandledKeyEvent(view, keyEvent);
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface ScrollAxis {
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface ScrollIndicators {
    }
    
    static class UnhandledKeyEventManager
    {
        private static final ArrayList<WeakReference<View>> sViewsWithListeners;
        private SparseArray<WeakReference<View>> mCapturedKeys;
        private WeakReference<KeyEvent> mLastDispatchedPreViewKeyEvent;
        private WeakHashMap<View, Boolean> mViewsContainingListeners;
        
        static {
            sViewsWithListeners = new ArrayList<WeakReference<View>>();
        }
        
        UnhandledKeyEventManager() {
            this.mViewsContainingListeners = null;
            this.mCapturedKeys = null;
            this.mLastDispatchedPreViewKeyEvent = null;
        }
        
        static UnhandledKeyEventManager at(final View view) {
            UnhandledKeyEventManager unhandledKeyEventManager;
            if ((unhandledKeyEventManager = (UnhandledKeyEventManager)view.getTag(R.id.tag_unhandled_key_event_manager)) == null) {
                unhandledKeyEventManager = new UnhandledKeyEventManager();
                view.setTag(R.id.tag_unhandled_key_event_manager, (Object)unhandledKeyEventManager);
            }
            return unhandledKeyEventManager;
        }
        
        private View dispatchInOrder(final View key, final KeyEvent keyEvent) {
            final WeakHashMap<View, Boolean> mViewsContainingListeners = this.mViewsContainingListeners;
            if (mViewsContainingListeners == null || !mViewsContainingListeners.containsKey(key)) {
                return null;
            }
            if (key instanceof ViewGroup) {
                final ViewGroup viewGroup = (ViewGroup)key;
                for (int i = viewGroup.getChildCount() - 1; i >= 0; --i) {
                    final View dispatchInOrder = this.dispatchInOrder(viewGroup.getChildAt(i), keyEvent);
                    if (dispatchInOrder != null) {
                        return dispatchInOrder;
                    }
                }
            }
            if (this.onUnhandledKeyEvent(key, keyEvent)) {
                return key;
            }
            return null;
        }
        
        private SparseArray<WeakReference<View>> getCapturedKeys() {
            if (this.mCapturedKeys == null) {
                this.mCapturedKeys = (SparseArray<WeakReference<View>>)new SparseArray();
            }
            return this.mCapturedKeys;
        }
        
        private boolean onUnhandledKeyEvent(final View view, final KeyEvent keyEvent) {
            final ArrayList list = (ArrayList)view.getTag(R.id.tag_unhandled_key_listeners);
            if (list != null) {
                for (int i = list.size() - 1; i >= 0; --i) {
                    if (list.get(i).onUnhandledKeyEvent(view, keyEvent)) {
                        return true;
                    }
                }
            }
            return false;
        }
        
        private void recalcViewsWithUnhandled() {
            final WeakHashMap<View, Boolean> mViewsContainingListeners = this.mViewsContainingListeners;
            if (mViewsContainingListeners != null) {
                mViewsContainingListeners.clear();
            }
            if (UnhandledKeyEventManager.sViewsWithListeners.isEmpty()) {
                return;
            }
            final ArrayList<WeakReference<View>> sViewsWithListeners = UnhandledKeyEventManager.sViewsWithListeners;
            // monitorenter(sViewsWithListeners)
            try {
                if (this.mViewsContainingListeners == null) {
                    this.mViewsContainingListeners = new WeakHashMap<View, Boolean>();
                }
                for (int i = UnhandledKeyEventManager.sViewsWithListeners.size() - 1; i >= 0; --i) {
                    final View key = UnhandledKeyEventManager.sViewsWithListeners.get(i).get();
                    if (key == null) {
                        UnhandledKeyEventManager.sViewsWithListeners.remove(i);
                    }
                    else {
                        this.mViewsContainingListeners.put(key, Boolean.TRUE);
                        for (ViewParent viewParent = key.getParent(); viewParent instanceof View; viewParent = viewParent.getParent()) {
                            this.mViewsContainingListeners.put((View)viewParent, Boolean.TRUE);
                        }
                    }
                }
            }
            // monitorexit(sViewsWithListeners)
            finally {
                // monitorexit(sViewsWithListeners)
                while (true) {}
            }
        }
        
        static void registerListeningView(final View referent) {
            final ArrayList<WeakReference<View>> sViewsWithListeners = UnhandledKeyEventManager.sViewsWithListeners;
            // monitorenter(sViewsWithListeners)
            try {
                final Iterator<WeakReference<View>> iterator = UnhandledKeyEventManager.sViewsWithListeners.iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().get() == referent) {
                        // monitorexit(sViewsWithListeners)
                        return;
                    }
                }
                UnhandledKeyEventManager.sViewsWithListeners.add(new WeakReference<View>(referent));
            }
            // monitorexit(sViewsWithListeners)
            finally {
                // monitorexit(sViewsWithListeners)
                while (true) {}
            }
        }
        
        static void unregisterListeningView(final View view) {
            final ArrayList<WeakReference<View>> sViewsWithListeners = UnhandledKeyEventManager.sViewsWithListeners;
            // monitorenter(sViewsWithListeners)
            int i = 0;
            try {
                while (i < UnhandledKeyEventManager.sViewsWithListeners.size()) {
                    if (UnhandledKeyEventManager.sViewsWithListeners.get(i).get() == view) {
                        UnhandledKeyEventManager.sViewsWithListeners.remove(i);
                        // monitorexit(sViewsWithListeners)
                        return;
                    }
                    ++i;
                }
            }
            // monitorexit(sViewsWithListeners)
            finally {
                // monitorexit(sViewsWithListeners)
                while (true) {}
            }
        }
        
        boolean dispatch(View dispatchInOrder, final KeyEvent keyEvent) {
            if (keyEvent.getAction() == 0) {
                this.recalcViewsWithUnhandled();
            }
            dispatchInOrder = this.dispatchInOrder(dispatchInOrder, keyEvent);
            if (keyEvent.getAction() == 0) {
                final int keyCode = keyEvent.getKeyCode();
                if (dispatchInOrder != null && !KeyEvent.isModifierKey(keyCode)) {
                    this.getCapturedKeys().put(keyCode, (Object)new WeakReference(dispatchInOrder));
                }
            }
            return dispatchInOrder != null;
        }
        
        boolean preDispatch(final KeyEvent referent) {
            final WeakReference<KeyEvent> mLastDispatchedPreViewKeyEvent = this.mLastDispatchedPreViewKeyEvent;
            if (mLastDispatchedPreViewKeyEvent != null && mLastDispatchedPreViewKeyEvent.get() == referent) {
                return false;
            }
            this.mLastDispatchedPreViewKeyEvent = new WeakReference<KeyEvent>(referent);
            final WeakReference<View> weakReference = null;
            final SparseArray<WeakReference<View>> capturedKeys = this.getCapturedKeys();
            WeakReference<View> weakReference2 = weakReference;
            if (referent.getAction() == 1) {
                final int indexOfKey = capturedKeys.indexOfKey(referent.getKeyCode());
                weakReference2 = weakReference;
                if (indexOfKey >= 0) {
                    weakReference2 = (WeakReference<View>)capturedKeys.valueAt(indexOfKey);
                    capturedKeys.removeAt(indexOfKey);
                }
            }
            WeakReference<View> weakReference3;
            if ((weakReference3 = weakReference2) == null) {
                weakReference3 = (WeakReference<View>)capturedKeys.get(referent.getKeyCode());
            }
            if (weakReference3 != null) {
                final View view = weakReference3.get();
                if (view != null && ViewCompat.isAttachedToWindow(view)) {
                    this.onUnhandledKeyEvent(view, referent);
                }
                return true;
            }
            return false;
        }
    }
}
