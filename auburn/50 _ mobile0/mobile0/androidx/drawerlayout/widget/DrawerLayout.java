// 
// Decompiled by Procyon v0.5.36
// 

package androidx.drawerlayout.widget;

import android.os.Parcel;
import android.os.Parcelable$ClassLoaderCreator;
import android.os.Parcelable$Creator;
import androidx.customview.view.AbsSavedState;
import android.content.res.TypedArray;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import android.graphics.drawable.ColorDrawable;
import androidx.core.content.ContextCompat;
import android.os.Parcelable;
import android.view.View$MeasureSpec;
import android.view.KeyEvent;
import android.view.ViewGroup$MarginLayoutParams;
import androidx.core.view.GravityCompat;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.view.ViewGroup$LayoutParams;
import androidx.core.graphics.drawable.DrawableCompat;
import android.view.MotionEvent;
import android.view.WindowInsets;
import android.view.View$OnApplyWindowInsetsListener;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.content.Context;
import android.os.Build$VERSION;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import androidx.customview.widget.ViewDragHelper;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.view.ViewGroup;

public class DrawerLayout extends ViewGroup
{
    private static final boolean ALLOW_EDGE_LOCK = false;
    static final boolean CAN_HIDE_DESCENDANTS;
    private static final boolean CHILDREN_DISALLOW_INTERCEPT = true;
    private static final int DEFAULT_SCRIM_COLOR = -1728053248;
    private static final int DRAWER_ELEVATION = 10;
    static final int[] LAYOUT_ATTRS;
    public static final int LOCK_MODE_LOCKED_CLOSED = 1;
    public static final int LOCK_MODE_LOCKED_OPEN = 2;
    public static final int LOCK_MODE_UNDEFINED = 3;
    public static final int LOCK_MODE_UNLOCKED = 0;
    private static final int MIN_DRAWER_MARGIN = 64;
    private static final int MIN_FLING_VELOCITY = 400;
    private static final int PEEK_DELAY = 160;
    private static final boolean SET_DRAWER_SHADOW_FROM_ELEVATION;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    private static final String TAG = "DrawerLayout";
    private static final int[] THEME_ATTRS;
    private static final float TOUCH_SLOP_SENSITIVITY = 1.0f;
    private final ChildAccessibilityDelegate mChildAccessibilityDelegate;
    private Rect mChildHitRect;
    private Matrix mChildInvertedMatrix;
    private boolean mChildrenCanceledTouch;
    private boolean mDisallowInterceptRequested;
    private boolean mDrawStatusBarBackground;
    private float mDrawerElevation;
    private int mDrawerState;
    private boolean mFirstLayout;
    private boolean mInLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private Object mLastInsets;
    private final ViewDragCallback mLeftCallback;
    private final ViewDragHelper mLeftDragger;
    private DrawerListener mListener;
    private List<DrawerListener> mListeners;
    private int mLockModeEnd;
    private int mLockModeLeft;
    private int mLockModeRight;
    private int mLockModeStart;
    private int mMinDrawerMargin;
    private final ArrayList<View> mNonDrawerViews;
    private final ViewDragCallback mRightCallback;
    private final ViewDragHelper mRightDragger;
    private int mScrimColor;
    private float mScrimOpacity;
    private Paint mScrimPaint;
    private Drawable mShadowEnd;
    private Drawable mShadowLeft;
    private Drawable mShadowLeftResolved;
    private Drawable mShadowRight;
    private Drawable mShadowRightResolved;
    private Drawable mShadowStart;
    private Drawable mStatusBarBackground;
    private CharSequence mTitleLeft;
    private CharSequence mTitleRight;
    
    static {
        final boolean b = true;
        THEME_ATTRS = new int[] { 16843828 };
        LAYOUT_ATTRS = new int[] { 16842931 };
        CAN_HIDE_DESCENDANTS = (Build$VERSION.SDK_INT >= 19);
        SET_DRAWER_SHADOW_FROM_ELEVATION = (Build$VERSION.SDK_INT >= 21 && b);
    }
    
    public DrawerLayout(final Context context) {
        this(context, null);
    }
    
    public DrawerLayout(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public DrawerLayout(final Context context, AttributeSet obtainStyledAttributes, final int n) {
        super(context, obtainStyledAttributes, n);
        this.mChildAccessibilityDelegate = new ChildAccessibilityDelegate();
        this.mScrimColor = -1728053248;
        this.mScrimPaint = new Paint();
        this.mFirstLayout = true;
        this.mLockModeLeft = 3;
        this.mLockModeRight = 3;
        this.mLockModeStart = 3;
        this.mLockModeEnd = 3;
        this.mShadowStart = null;
        this.mShadowEnd = null;
        this.mShadowLeft = null;
        this.mShadowRight = null;
        this.setDescendantFocusability(262144);
        final float density = this.getResources().getDisplayMetrics().density;
        this.mMinDrawerMargin = (int)(64.0f * density + 0.5f);
        final float n2 = 400.0f * density;
        this.mLeftCallback = new ViewDragCallback(3);
        this.mRightCallback = new ViewDragCallback(5);
        (this.mLeftDragger = ViewDragHelper.create(this, 1.0f, (ViewDragHelper.Callback)this.mLeftCallback)).setEdgeTrackingEnabled(1);
        this.mLeftDragger.setMinVelocity(n2);
        this.mLeftCallback.setDragger(this.mLeftDragger);
        (this.mRightDragger = ViewDragHelper.create(this, 1.0f, (ViewDragHelper.Callback)this.mRightCallback)).setEdgeTrackingEnabled(2);
        this.mRightDragger.setMinVelocity(n2);
        this.mRightCallback.setDragger(this.mRightDragger);
        this.setFocusableInTouchMode(true);
        ViewCompat.setImportantForAccessibility((View)this, 1);
        ViewCompat.setAccessibilityDelegate((View)this, new AccessibilityDelegate());
        this.setMotionEventSplittingEnabled(false);
        Label_0321: {
            if (ViewCompat.getFitsSystemWindows((View)this)) {
                if (Build$VERSION.SDK_INT >= 21) {
                    this.setOnApplyWindowInsetsListener((View$OnApplyWindowInsetsListener)new View$OnApplyWindowInsetsListener() {
                        public WindowInsets onApplyWindowInsets(final View view, final WindowInsets windowInsets) {
                            ((DrawerLayout)view).setChildInsets(windowInsets, windowInsets.getSystemWindowInsetTop() > 0);
                            return windowInsets.consumeSystemWindowInsets();
                        }
                    });
                    this.setSystemUiVisibility(1280);
                    obtainStyledAttributes = (AttributeSet)context.obtainStyledAttributes(DrawerLayout.THEME_ATTRS);
                    try {
                        this.mStatusBarBackground = ((TypedArray)obtainStyledAttributes).getDrawable(0);
                        break Label_0321;
                    }
                    finally {
                        ((TypedArray)obtainStyledAttributes).recycle();
                    }
                }
                this.mStatusBarBackground = null;
            }
        }
        this.mDrawerElevation = 10.0f * density;
        this.mNonDrawerViews = new ArrayList<View>();
    }
    
    private boolean dispatchTransformedGenericPointerEvent(MotionEvent transformedMotionEvent, final View view) {
        boolean b;
        if (!view.getMatrix().isIdentity()) {
            transformedMotionEvent = this.getTransformedMotionEvent(transformedMotionEvent, view);
            b = view.dispatchGenericMotionEvent(transformedMotionEvent);
            transformedMotionEvent.recycle();
        }
        else {
            final float n = (float)(this.getScrollX() - view.getLeft());
            final float n2 = (float)(this.getScrollY() - view.getTop());
            transformedMotionEvent.offsetLocation(n, n2);
            b = view.dispatchGenericMotionEvent(transformedMotionEvent);
            transformedMotionEvent.offsetLocation(-n, -n2);
        }
        return b;
    }
    
    private MotionEvent getTransformedMotionEvent(MotionEvent obtain, final View view) {
        final float n = (float)(this.getScrollX() - view.getLeft());
        final float n2 = (float)(this.getScrollY() - view.getTop());
        obtain = MotionEvent.obtain(obtain);
        obtain.offsetLocation(n, n2);
        final Matrix matrix = view.getMatrix();
        if (!matrix.isIdentity()) {
            if (this.mChildInvertedMatrix == null) {
                this.mChildInvertedMatrix = new Matrix();
            }
            matrix.invert(this.mChildInvertedMatrix);
            obtain.transform(this.mChildInvertedMatrix);
        }
        return obtain;
    }
    
    static String gravityToString(final int i) {
        if ((i & 0x3) == 0x3) {
            return "LEFT";
        }
        if ((i & 0x5) == 0x5) {
            return "RIGHT";
        }
        return Integer.toHexString(i);
    }
    
    private static boolean hasOpaqueBackground(final View view) {
        final Drawable background = view.getBackground();
        boolean b = false;
        if (background != null) {
            if (background.getOpacity() == -1) {
                b = true;
            }
            return b;
        }
        return false;
    }
    
    private boolean hasPeekingDrawer() {
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            if (((LayoutParams)this.getChildAt(i).getLayoutParams()).isPeeking) {
                return true;
            }
        }
        return false;
    }
    
    private boolean hasVisibleDrawer() {
        return this.findVisibleDrawer() != null;
    }
    
    static boolean includeChildForAccessibility(final View view) {
        return ViewCompat.getImportantForAccessibility(view) != 4 && ViewCompat.getImportantForAccessibility(view) != 2;
    }
    
    private boolean isInBoundsOfChild(final float n, final float n2, final View view) {
        if (this.mChildHitRect == null) {
            this.mChildHitRect = new Rect();
        }
        view.getHitRect(this.mChildHitRect);
        return this.mChildHitRect.contains((int)n, (int)n2);
    }
    
    private boolean mirror(final Drawable drawable, final int n) {
        if (drawable != null && DrawableCompat.isAutoMirrored(drawable)) {
            DrawableCompat.setLayoutDirection(drawable, n);
            return true;
        }
        return false;
    }
    
    private Drawable resolveLeftShadow() {
        final int layoutDirection = ViewCompat.getLayoutDirection((View)this);
        if (layoutDirection == 0) {
            final Drawable mShadowStart = this.mShadowStart;
            if (mShadowStart != null) {
                this.mirror(mShadowStart, layoutDirection);
                return this.mShadowStart;
            }
        }
        else {
            final Drawable mShadowEnd = this.mShadowEnd;
            if (mShadowEnd != null) {
                this.mirror(mShadowEnd, layoutDirection);
                return this.mShadowEnd;
            }
        }
        return this.mShadowLeft;
    }
    
    private Drawable resolveRightShadow() {
        final int layoutDirection = ViewCompat.getLayoutDirection((View)this);
        if (layoutDirection == 0) {
            final Drawable mShadowEnd = this.mShadowEnd;
            if (mShadowEnd != null) {
                this.mirror(mShadowEnd, layoutDirection);
                return this.mShadowEnd;
            }
        }
        else {
            final Drawable mShadowStart = this.mShadowStart;
            if (mShadowStart != null) {
                this.mirror(mShadowStart, layoutDirection);
                return this.mShadowStart;
            }
        }
        return this.mShadowRight;
    }
    
    private void resolveShadowDrawables() {
        if (DrawerLayout.SET_DRAWER_SHADOW_FROM_ELEVATION) {
            return;
        }
        this.mShadowLeftResolved = this.resolveLeftShadow();
        this.mShadowRightResolved = this.resolveRightShadow();
    }
    
    private void updateChildrenImportantForAccessibility(final View view, final boolean b) {
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if ((!b && !this.isDrawerView(child)) || (b && child == view)) {
                ViewCompat.setImportantForAccessibility(child, 1);
            }
            else {
                ViewCompat.setImportantForAccessibility(child, 4);
            }
        }
    }
    
    public void addDrawerListener(final DrawerListener drawerListener) {
        if (drawerListener == null) {
            return;
        }
        if (this.mListeners == null) {
            this.mListeners = new ArrayList<DrawerListener>();
        }
        this.mListeners.add(drawerListener);
    }
    
    public void addFocusables(final ArrayList<View> list, final int n, final int n2) {
        if (this.getDescendantFocusability() == 393216) {
            return;
        }
        final int childCount = this.getChildCount();
        boolean b = false;
        for (int i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (this.isDrawerView(child)) {
                if (this.isDrawerOpen(child)) {
                    b = true;
                    child.addFocusables((ArrayList)list, n, n2);
                }
            }
            else {
                this.mNonDrawerViews.add(child);
            }
        }
        if (!b) {
            for (int size = this.mNonDrawerViews.size(), j = 0; j < size; ++j) {
                final View view = this.mNonDrawerViews.get(j);
                if (view.getVisibility() == 0) {
                    view.addFocusables((ArrayList)list, n, n2);
                }
            }
        }
        this.mNonDrawerViews.clear();
    }
    
    public void addView(final View view, final int n, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        super.addView(view, n, viewGroup$LayoutParams);
        if (this.findOpenDrawer() == null && !this.isDrawerView(view)) {
            ViewCompat.setImportantForAccessibility(view, 1);
        }
        else {
            ViewCompat.setImportantForAccessibility(view, 4);
        }
        if (!DrawerLayout.CAN_HIDE_DESCENDANTS) {
            ViewCompat.setAccessibilityDelegate(view, this.mChildAccessibilityDelegate);
        }
    }
    
    void cancelChildViewTouch() {
        if (!this.mChildrenCanceledTouch) {
            final long uptimeMillis = SystemClock.uptimeMillis();
            final MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
            for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                this.getChildAt(i).dispatchTouchEvent(obtain);
            }
            obtain.recycle();
            this.mChildrenCanceledTouch = true;
        }
    }
    
    boolean checkDrawerViewAbsoluteGravity(final View view, final int n) {
        return (this.getDrawerViewAbsoluteGravity(view) & n) == n;
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams instanceof LayoutParams && super.checkLayoutParams(viewGroup$LayoutParams);
    }
    
    public void closeDrawer(final int n) {
        this.closeDrawer(n, true);
    }
    
    public void closeDrawer(final int n, final boolean b) {
        final View drawerWithGravity = this.findDrawerWithGravity(n);
        if (drawerWithGravity != null) {
            this.closeDrawer(drawerWithGravity, b);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("No drawer view found with gravity ");
        sb.append(gravityToString(n));
        throw new IllegalArgumentException(sb.toString());
    }
    
    public void closeDrawer(final View view) {
        this.closeDrawer(view, true);
    }
    
    public void closeDrawer(final View obj, final boolean b) {
        if (this.isDrawerView(obj)) {
            final LayoutParams layoutParams = (LayoutParams)obj.getLayoutParams();
            if (this.mFirstLayout) {
                layoutParams.onScreen = 0.0f;
                layoutParams.openState = 0;
            }
            else if (b) {
                layoutParams.openState |= 0x4;
                if (this.checkDrawerViewAbsoluteGravity(obj, 3)) {
                    this.mLeftDragger.smoothSlideViewTo(obj, -obj.getWidth(), obj.getTop());
                }
                else {
                    this.mRightDragger.smoothSlideViewTo(obj, this.getWidth(), obj.getTop());
                }
            }
            else {
                this.moveDrawerToOffset(obj, 0.0f);
                this.updateDrawerState(layoutParams.gravity, 0, obj);
                obj.setVisibility(4);
            }
            this.invalidate();
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("View ");
        sb.append(obj);
        sb.append(" is not a sliding drawer");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public void closeDrawers() {
        this.closeDrawers(false);
    }
    
    void closeDrawers(final boolean b) {
        int n = 0;
        int n2;
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i, n = n2) {
            final View child = this.getChildAt(i);
            final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
            n2 = n;
            if (this.isDrawerView(child)) {
                if (b && !layoutParams.isPeeking) {
                    n2 = n;
                }
                else {
                    final int width = child.getWidth();
                    boolean b2;
                    if (this.checkDrawerViewAbsoluteGravity(child, 3)) {
                        b2 = ((n | (this.mLeftDragger.smoothSlideViewTo(child, -width, child.getTop()) ? 1 : 0)) != 0x0);
                    }
                    else {
                        b2 = ((n | (this.mRightDragger.smoothSlideViewTo(child, this.getWidth(), child.getTop()) ? 1 : 0)) != 0x0);
                    }
                    layoutParams.isPeeking = false;
                    n2 = (b2 ? 1 : 0);
                }
            }
        }
        this.mLeftCallback.removeCallbacks();
        this.mRightCallback.removeCallbacks();
        if (n != 0) {
            this.invalidate();
        }
    }
    
    public void computeScroll() {
        final int childCount = this.getChildCount();
        float max = 0.0f;
        for (int i = 0; i < childCount; ++i) {
            max = Math.max(max, ((LayoutParams)this.getChildAt(i).getLayoutParams()).onScreen);
        }
        this.mScrimOpacity = max;
        final boolean continueSettling = this.mLeftDragger.continueSettling(true);
        final boolean continueSettling2 = this.mRightDragger.continueSettling(true);
        if (continueSettling || continueSettling2) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }
    
    public boolean dispatchGenericMotionEvent(final MotionEvent motionEvent) {
        if ((motionEvent.getSource() & 0x2) != 0x0 && motionEvent.getAction() != 10 && this.mScrimOpacity > 0.0f) {
            int i = this.getChildCount();
            if (i != 0) {
                final float x = motionEvent.getX();
                final float y = motionEvent.getY();
                --i;
                while (i >= 0) {
                    final View child = this.getChildAt(i);
                    if (this.isInBoundsOfChild(x, y, child)) {
                        if (!this.isContentView(child)) {
                            if (this.dispatchTransformedGenericPointerEvent(motionEvent, child)) {
                                return true;
                            }
                        }
                    }
                    --i;
                }
            }
            return false;
        }
        return super.dispatchGenericMotionEvent(motionEvent);
    }
    
    void dispatchOnDrawerClosed(View rootView) {
        final LayoutParams layoutParams = (LayoutParams)rootView.getLayoutParams();
        if ((layoutParams.openState & 0x1) == 0x1) {
            layoutParams.openState = 0;
            final List<DrawerListener> mListeners = this.mListeners;
            if (mListeners != null) {
                for (int i = mListeners.size() - 1; i >= 0; --i) {
                    this.mListeners.get(i).onDrawerClosed(rootView);
                }
            }
            this.updateChildrenImportantForAccessibility(rootView, false);
            if (this.hasWindowFocus()) {
                rootView = this.getRootView();
                if (rootView != null) {
                    rootView.sendAccessibilityEvent(32);
                }
            }
        }
    }
    
    void dispatchOnDrawerOpened(final View view) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if ((layoutParams.openState & 0x1) == 0x0) {
            layoutParams.openState = 1;
            final List<DrawerListener> mListeners = this.mListeners;
            if (mListeners != null) {
                for (int i = mListeners.size() - 1; i >= 0; --i) {
                    this.mListeners.get(i).onDrawerOpened(view);
                }
            }
            this.updateChildrenImportantForAccessibility(view, true);
            if (this.hasWindowFocus()) {
                this.sendAccessibilityEvent(32);
            }
        }
    }
    
    void dispatchOnDrawerSlide(final View view, final float n) {
        final List<DrawerListener> mListeners = this.mListeners;
        if (mListeners != null) {
            for (int i = mListeners.size() - 1; i >= 0; --i) {
                this.mListeners.get(i).onDrawerSlide(view, n);
            }
        }
    }
    
    protected boolean drawChild(final Canvas canvas, final View view, final long n) {
        final int height = this.getHeight();
        final boolean contentView = this.isContentView(view);
        int n2 = 0;
        int width = this.getWidth();
        final int save = canvas.save();
        int n7;
        int n8;
        if (contentView) {
            int n3;
            int n4;
            for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i, n2 = n3, width = n4) {
                final View child = this.getChildAt(i);
                n3 = n2;
                n4 = width;
                if (child != view) {
                    n3 = n2;
                    n4 = width;
                    if (child.getVisibility() == 0) {
                        n3 = n2;
                        n4 = width;
                        if (hasOpaqueBackground(child)) {
                            n3 = n2;
                            n4 = width;
                            if (this.isDrawerView(child)) {
                                if (child.getHeight() < height) {
                                    n3 = n2;
                                    n4 = width;
                                }
                                else if (this.checkDrawerViewAbsoluteGravity(child, 3)) {
                                    final int right = child.getRight();
                                    int n5;
                                    if (right > (n5 = n2)) {
                                        n5 = right;
                                    }
                                    n3 = n5;
                                    n4 = width;
                                }
                                else {
                                    final int left = child.getLeft();
                                    n3 = n2;
                                    if (left < (n4 = width)) {
                                        n4 = left;
                                        n3 = n2;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            canvas.clipRect(n2, 0, width, this.getHeight());
            final int n6 = width;
            n7 = n2;
            n8 = n6;
        }
        else {
            final int n9 = 0;
            n8 = width;
            n7 = n9;
        }
        final boolean drawChild = super.drawChild(canvas, view, n);
        canvas.restoreToCount(save);
        final float mScrimOpacity = this.mScrimOpacity;
        if (mScrimOpacity > 0.0f && contentView) {
            final int mScrimColor = this.mScrimColor;
            this.mScrimPaint.setColor((int)(((0xFF000000 & mScrimColor) >>> 24) * mScrimOpacity) << 24 | (mScrimColor & 0xFFFFFF));
            canvas.drawRect((float)n7, 0.0f, (float)n8, (float)this.getHeight(), this.mScrimPaint);
        }
        else if (this.mShadowLeftResolved != null && this.checkDrawerViewAbsoluteGravity(view, 3)) {
            final int intrinsicWidth = this.mShadowLeftResolved.getIntrinsicWidth();
            final int right2 = view.getRight();
            final float max = Math.max(0.0f, Math.min(right2 / (float)this.mLeftDragger.getEdgeSize(), 1.0f));
            this.mShadowLeftResolved.setBounds(right2, view.getTop(), right2 + intrinsicWidth, view.getBottom());
            this.mShadowLeftResolved.setAlpha((int)(255.0f * max));
            this.mShadowLeftResolved.draw(canvas);
        }
        else if (this.mShadowRightResolved != null && this.checkDrawerViewAbsoluteGravity(view, 5)) {
            final int intrinsicWidth2 = this.mShadowRightResolved.getIntrinsicWidth();
            final int left2 = view.getLeft();
            final float max2 = Math.max(0.0f, Math.min((this.getWidth() - left2) / (float)this.mRightDragger.getEdgeSize(), 1.0f));
            this.mShadowRightResolved.setBounds(left2 - intrinsicWidth2, view.getTop(), left2, view.getBottom());
            this.mShadowRightResolved.setAlpha((int)(255.0f * max2));
            this.mShadowRightResolved.draw(canvas);
        }
        return drawChild;
    }
    
    View findDrawerWithGravity(int i) {
        final int absoluteGravity = GravityCompat.getAbsoluteGravity(i, ViewCompat.getLayoutDirection((View)this));
        int childCount;
        View child;
        for (childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            child = this.getChildAt(i);
            if ((this.getDrawerViewAbsoluteGravity(child) & 0x7) == (absoluteGravity & 0x7)) {
                return child;
            }
        }
        return null;
    }
    
    View findOpenDrawer() {
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if ((((LayoutParams)child.getLayoutParams()).openState & 0x1) == 0x1) {
                return child;
            }
        }
        return null;
    }
    
    View findVisibleDrawer() {
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (this.isDrawerView(child) && this.isDrawerVisible(child)) {
                return child;
            }
        }
        return null;
    }
    
    protected ViewGroup$LayoutParams generateDefaultLayoutParams() {
        return (ViewGroup$LayoutParams)new LayoutParams(-1, -1);
    }
    
    public ViewGroup$LayoutParams generateLayoutParams(final AttributeSet set) {
        return (ViewGroup$LayoutParams)new LayoutParams(this.getContext(), set);
    }
    
    protected ViewGroup$LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        LayoutParams layoutParams;
        if (viewGroup$LayoutParams instanceof LayoutParams) {
            layoutParams = new LayoutParams((LayoutParams)viewGroup$LayoutParams);
        }
        else if (viewGroup$LayoutParams instanceof ViewGroup$MarginLayoutParams) {
            layoutParams = new LayoutParams((ViewGroup$MarginLayoutParams)viewGroup$LayoutParams);
        }
        else {
            layoutParams = new LayoutParams(viewGroup$LayoutParams);
        }
        return (ViewGroup$LayoutParams)layoutParams;
    }
    
    public float getDrawerElevation() {
        if (DrawerLayout.SET_DRAWER_SHADOW_FROM_ELEVATION) {
            return this.mDrawerElevation;
        }
        return 0.0f;
    }
    
    public int getDrawerLockMode(int n) {
        final int layoutDirection = ViewCompat.getLayoutDirection((View)this);
        if (n != 3) {
            if (n != 5) {
                if (n != 8388611) {
                    if (n == 8388613) {
                        n = this.mLockModeEnd;
                        if (n != 3) {
                            return n;
                        }
                        if (layoutDirection == 0) {
                            n = this.mLockModeRight;
                        }
                        else {
                            n = this.mLockModeLeft;
                        }
                        if (n != 3) {
                            return n;
                        }
                    }
                }
                else {
                    n = this.mLockModeStart;
                    if (n != 3) {
                        return n;
                    }
                    if (layoutDirection == 0) {
                        n = this.mLockModeLeft;
                    }
                    else {
                        n = this.mLockModeRight;
                    }
                    if (n != 3) {
                        return n;
                    }
                }
            }
            else {
                n = this.mLockModeRight;
                if (n != 3) {
                    return n;
                }
                if (layoutDirection == 0) {
                    n = this.mLockModeEnd;
                }
                else {
                    n = this.mLockModeStart;
                }
                if (n != 3) {
                    return n;
                }
            }
        }
        else {
            n = this.mLockModeLeft;
            if (n != 3) {
                return n;
            }
            if (layoutDirection == 0) {
                n = this.mLockModeStart;
            }
            else {
                n = this.mLockModeEnd;
            }
            if (n != 3) {
                return n;
            }
        }
        return 0;
    }
    
    public int getDrawerLockMode(final View obj) {
        if (this.isDrawerView(obj)) {
            return this.getDrawerLockMode(((LayoutParams)obj.getLayoutParams()).gravity);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("View ");
        sb.append(obj);
        sb.append(" is not a drawer");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public CharSequence getDrawerTitle(int absoluteGravity) {
        absoluteGravity = GravityCompat.getAbsoluteGravity(absoluteGravity, ViewCompat.getLayoutDirection((View)this));
        if (absoluteGravity == 3) {
            return this.mTitleLeft;
        }
        if (absoluteGravity == 5) {
            return this.mTitleRight;
        }
        return null;
    }
    
    int getDrawerViewAbsoluteGravity(final View view) {
        return GravityCompat.getAbsoluteGravity(((LayoutParams)view.getLayoutParams()).gravity, ViewCompat.getLayoutDirection((View)this));
    }
    
    float getDrawerViewOffset(final View view) {
        return ((LayoutParams)view.getLayoutParams()).onScreen;
    }
    
    public Drawable getStatusBarBackgroundDrawable() {
        return this.mStatusBarBackground;
    }
    
    boolean isContentView(final View view) {
        return ((LayoutParams)view.getLayoutParams()).gravity == 0;
    }
    
    public boolean isDrawerOpen(final int n) {
        final View drawerWithGravity = this.findDrawerWithGravity(n);
        return drawerWithGravity != null && this.isDrawerOpen(drawerWithGravity);
    }
    
    public boolean isDrawerOpen(final View obj) {
        if (this.isDrawerView(obj)) {
            final int openState = ((LayoutParams)obj.getLayoutParams()).openState;
            boolean b = true;
            if ((openState & 0x1) != 0x1) {
                b = false;
            }
            return b;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("View ");
        sb.append(obj);
        sb.append(" is not a drawer");
        throw new IllegalArgumentException(sb.toString());
    }
    
    boolean isDrawerView(final View view) {
        final int absoluteGravity = GravityCompat.getAbsoluteGravity(((LayoutParams)view.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(view));
        return (absoluteGravity & 0x3) != 0x0 || (absoluteGravity & 0x5) != 0x0;
    }
    
    public boolean isDrawerVisible(final int n) {
        final View drawerWithGravity = this.findDrawerWithGravity(n);
        return drawerWithGravity != null && this.isDrawerVisible(drawerWithGravity);
    }
    
    public boolean isDrawerVisible(final View obj) {
        if (this.isDrawerView(obj)) {
            return ((LayoutParams)obj.getLayoutParams()).onScreen > 0.0f;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("View ");
        sb.append(obj);
        sb.append(" is not a drawer");
        throw new IllegalArgumentException(sb.toString());
    }
    
    void moveDrawerToOffset(final View view, final float n) {
        final float drawerViewOffset = this.getDrawerViewOffset(view);
        final int width = view.getWidth();
        int n2 = (int)(width * n) - (int)(width * drawerViewOffset);
        if (!this.checkDrawerViewAbsoluteGravity(view, 3)) {
            n2 = -n2;
        }
        view.offsetLeftAndRight(n2);
        this.setDrawerViewOffset(view, n);
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mFirstLayout = true;
    }
    
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (this.mDrawStatusBarBackground && this.mStatusBarBackground != null) {
            int systemWindowInsetTop;
            if (Build$VERSION.SDK_INT >= 21) {
                final Object mLastInsets = this.mLastInsets;
                if (mLastInsets != null) {
                    systemWindowInsetTop = ((WindowInsets)mLastInsets).getSystemWindowInsetTop();
                }
                else {
                    systemWindowInsetTop = 0;
                }
            }
            else {
                systemWindowInsetTop = 0;
            }
            if (systemWindowInsetTop > 0) {
                this.mStatusBarBackground.setBounds(0, 0, this.getWidth(), systemWindowInsetTop);
                this.mStatusBarBackground.draw(canvas);
            }
        }
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        final int actionMasked = motionEvent.getActionMasked();
        final boolean shouldInterceptTouchEvent = this.mLeftDragger.shouldInterceptTouchEvent(motionEvent);
        final boolean shouldInterceptTouchEvent2 = this.mRightDragger.shouldInterceptTouchEvent(motionEvent);
        final boolean b = false;
        final boolean b2 = false;
        final boolean b3 = true;
        int n = 0;
        Label_0193: {
            if (actionMasked != 0) {
                if (actionMasked != 1) {
                    if (actionMasked != 2) {
                        if (actionMasked != 3) {
                            n = (b ? 1 : 0);
                            break Label_0193;
                        }
                    }
                    else {
                        n = (b ? 1 : 0);
                        if (this.mLeftDragger.checkTouchSlop(3)) {
                            this.mLeftCallback.removeCallbacks();
                            this.mRightCallback.removeCallbacks();
                            n = (b ? 1 : 0);
                        }
                        break Label_0193;
                    }
                }
                this.closeDrawers(true);
                this.mDisallowInterceptRequested = false;
                this.mChildrenCanceledTouch = false;
                n = (b ? 1 : 0);
            }
            else {
                final float x = motionEvent.getX();
                final float y = motionEvent.getY();
                this.mInitialMotionX = x;
                this.mInitialMotionY = y;
                n = (b2 ? 1 : 0);
                if (this.mScrimOpacity > 0.0f) {
                    final View topChildUnder = this.mLeftDragger.findTopChildUnder((int)x, (int)y);
                    n = (b2 ? 1 : 0);
                    if (topChildUnder != null) {
                        n = (b2 ? 1 : 0);
                        if (this.isContentView(topChildUnder)) {
                            n = 1;
                        }
                    }
                }
                this.mDisallowInterceptRequested = false;
                this.mChildrenCanceledTouch = false;
            }
        }
        boolean b4 = b3;
        if (!(shouldInterceptTouchEvent | shouldInterceptTouchEvent2)) {
            b4 = b3;
            if (n == 0) {
                b4 = b3;
                if (!this.hasPeekingDrawer()) {
                    b4 = (this.mChildrenCanceledTouch && b3);
                }
            }
        }
        return b4;
    }
    
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        if (n == 4 && this.hasVisibleDrawer()) {
            keyEvent.startTracking();
            return true;
        }
        return super.onKeyDown(n, keyEvent);
    }
    
    public boolean onKeyUp(final int n, final KeyEvent keyEvent) {
        if (n == 4) {
            final View visibleDrawer = this.findVisibleDrawer();
            if (visibleDrawer != null && this.getDrawerLockMode(visibleDrawer) == 0) {
                this.closeDrawers();
            }
            return visibleDrawer != null;
        }
        return super.onKeyUp(n, keyEvent);
    }
    
    protected void onLayout(final boolean b, int topMargin, final int n, int i, final int n2) {
        this.mInLayout = true;
        final int n3 = i - topMargin;
        int childCount;
        View child;
        LayoutParams layoutParams;
        int measuredWidth;
        int measuredHeight;
        int n4;
        float n5;
        boolean b2;
        int n6;
        int n7;
        for (childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                layoutParams = (LayoutParams)child.getLayoutParams();
                if (this.isContentView(child)) {
                    child.layout(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.leftMargin + child.getMeasuredWidth(), layoutParams.topMargin + child.getMeasuredHeight());
                }
                else {
                    measuredWidth = child.getMeasuredWidth();
                    measuredHeight = child.getMeasuredHeight();
                    if (this.checkDrawerViewAbsoluteGravity(child, 3)) {
                        n4 = -measuredWidth + (int)(measuredWidth * layoutParams.onScreen);
                        n5 = (measuredWidth + n4) / (float)measuredWidth;
                    }
                    else {
                        n4 = n3 - (int)(measuredWidth * layoutParams.onScreen);
                        n5 = (n3 - n4) / (float)measuredWidth;
                    }
                    b2 = (n5 != layoutParams.onScreen);
                    topMargin = (layoutParams.gravity & 0x70);
                    if (topMargin != 16) {
                        if (topMargin != 80) {
                            child.layout(n4, layoutParams.topMargin, n4 + measuredWidth, layoutParams.topMargin + measuredHeight);
                        }
                        else {
                            topMargin = n2 - n;
                            child.layout(n4, topMargin - layoutParams.bottomMargin - child.getMeasuredHeight(), n4 + measuredWidth, topMargin - layoutParams.bottomMargin);
                        }
                    }
                    else {
                        n6 = n2 - n;
                        n7 = (n6 - measuredHeight) / 2;
                        if (n7 < layoutParams.topMargin) {
                            topMargin = layoutParams.topMargin;
                        }
                        else {
                            topMargin = n7;
                            if (n7 + measuredHeight > n6 - layoutParams.bottomMargin) {
                                topMargin = n6 - layoutParams.bottomMargin - measuredHeight;
                            }
                        }
                        child.layout(n4, topMargin, n4 + measuredWidth, topMargin + measuredHeight);
                    }
                    if (b2) {
                        this.setDrawerViewOffset(child, n5);
                    }
                    if (layoutParams.onScreen > 0.0f) {
                        topMargin = 0;
                    }
                    else {
                        topMargin = 4;
                    }
                    if (child.getVisibility() != topMargin) {
                        child.setVisibility(topMargin);
                    }
                }
            }
        }
        this.mInLayout = false;
        this.mFirstLayout = false;
    }
    
    protected void onMeasure(final int n, final int n2) {
        final int mode = View$MeasureSpec.getMode(n);
        final int mode2 = View$MeasureSpec.getMode(n2);
        int size = View$MeasureSpec.getSize(n);
        final int size2 = View$MeasureSpec.getSize(n2);
        int n3 = 0;
        int n4 = 0;
        Label_0167: {
            if (mode == 1073741824) {
                n3 = size;
                n4 = size2;
                if (mode2 == 1073741824) {
                    break Label_0167;
                }
            }
            if (!this.isInEditMode()) {
                throw new IllegalArgumentException("DrawerLayout must be measured with MeasureSpec.EXACTLY.");
            }
            if (mode != Integer.MIN_VALUE) {
                final int n5;
                if ((n5 = mode) == 0) {
                    size = 300;
                }
            }
            if (mode2 == Integer.MIN_VALUE) {
                n3 = size;
                n4 = size2;
            }
            else {
                n3 = size;
                n4 = size2;
                if (mode2 == 0) {
                    n4 = 300;
                    n3 = size;
                }
            }
        }
        this.setMeasuredDimension(n3, n4);
        final boolean b = this.mLastInsets != null && ViewCompat.getFitsSystemWindows((View)this);
        final int layoutDirection = ViewCompat.getLayoutDirection((View)this);
        int n6 = 0;
        int n7 = 0;
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                if (b) {
                    final int absoluteGravity = GravityCompat.getAbsoluteGravity(layoutParams.gravity, layoutDirection);
                    if (ViewCompat.getFitsSystemWindows(child)) {
                        if (Build$VERSION.SDK_INT >= 21) {
                            final WindowInsets windowInsets = (WindowInsets)this.mLastInsets;
                            WindowInsets windowInsets2;
                            if (absoluteGravity == 3) {
                                windowInsets2 = windowInsets.replaceSystemWindowInsets(windowInsets.getSystemWindowInsetLeft(), windowInsets.getSystemWindowInsetTop(), 0, windowInsets.getSystemWindowInsetBottom());
                            }
                            else {
                                windowInsets2 = windowInsets;
                                if (absoluteGravity == 5) {
                                    windowInsets2 = windowInsets.replaceSystemWindowInsets(0, windowInsets.getSystemWindowInsetTop(), windowInsets.getSystemWindowInsetRight(), windowInsets.getSystemWindowInsetBottom());
                                }
                            }
                            child.dispatchApplyWindowInsets(windowInsets2);
                        }
                    }
                    else if (Build$VERSION.SDK_INT >= 21) {
                        final WindowInsets windowInsets3 = (WindowInsets)this.mLastInsets;
                        WindowInsets windowInsets4;
                        if (absoluteGravity == 3) {
                            windowInsets4 = windowInsets3.replaceSystemWindowInsets(windowInsets3.getSystemWindowInsetLeft(), windowInsets3.getSystemWindowInsetTop(), 0, windowInsets3.getSystemWindowInsetBottom());
                        }
                        else {
                            windowInsets4 = windowInsets3;
                            if (absoluteGravity == 5) {
                                windowInsets4 = windowInsets3.replaceSystemWindowInsets(0, windowInsets3.getSystemWindowInsetTop(), windowInsets3.getSystemWindowInsetRight(), windowInsets3.getSystemWindowInsetBottom());
                            }
                        }
                        layoutParams.leftMargin = windowInsets4.getSystemWindowInsetLeft();
                        layoutParams.topMargin = windowInsets4.getSystemWindowInsetTop();
                        layoutParams.rightMargin = windowInsets4.getSystemWindowInsetRight();
                        layoutParams.bottomMargin = windowInsets4.getSystemWindowInsetBottom();
                    }
                }
                if (this.isContentView(child)) {
                    child.measure(View$MeasureSpec.makeMeasureSpec(n3 - layoutParams.leftMargin - layoutParams.rightMargin, 1073741824), View$MeasureSpec.makeMeasureSpec(n4 - layoutParams.topMargin - layoutParams.bottomMargin, 1073741824));
                }
                else {
                    if (!this.isDrawerView(child)) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Child ");
                        sb.append(child);
                        sb.append(" at index ");
                        sb.append(i);
                        sb.append(" does not have a valid layout_gravity - must be Gravity.LEFT, ");
                        sb.append("Gravity.RIGHT or Gravity.NO_GRAVITY");
                        throw new IllegalStateException(sb.toString());
                    }
                    if (DrawerLayout.SET_DRAWER_SHADOW_FROM_ELEVATION) {
                        final float elevation = ViewCompat.getElevation(child);
                        final float mDrawerElevation = this.mDrawerElevation;
                        if (elevation != mDrawerElevation) {
                            ViewCompat.setElevation(child, mDrawerElevation);
                        }
                    }
                    final int n8 = this.getDrawerViewAbsoluteGravity(child) & 0x7;
                    final boolean b2 = n8 == 3;
                    if ((b2 && n6 != 0) || (!b2 && n7 != 0)) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Child drawer has absolute gravity ");
                        sb2.append(gravityToString(n8));
                        sb2.append(" but this ");
                        sb2.append("DrawerLayout");
                        sb2.append(" already has a ");
                        sb2.append("drawer view along that edge");
                        throw new IllegalStateException(sb2.toString());
                    }
                    if (b2) {
                        n6 = 1;
                    }
                    else {
                        n7 = 1;
                    }
                    child.measure(getChildMeasureSpec(n, this.mMinDrawerMargin + layoutParams.leftMargin + layoutParams.rightMargin, layoutParams.width), getChildMeasureSpec(n2, layoutParams.topMargin + layoutParams.bottomMargin, layoutParams.height));
                }
            }
        }
    }
    
    protected void onRestoreInstanceState(final Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        final SavedState savedState = (SavedState)parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        if (savedState.openDrawerGravity != 0) {
            final View drawerWithGravity = this.findDrawerWithGravity(savedState.openDrawerGravity);
            if (drawerWithGravity != null) {
                this.openDrawer(drawerWithGravity);
            }
        }
        if (savedState.lockModeLeft != 3) {
            this.setDrawerLockMode(savedState.lockModeLeft, 3);
        }
        if (savedState.lockModeRight != 3) {
            this.setDrawerLockMode(savedState.lockModeRight, 5);
        }
        if (savedState.lockModeStart != 3) {
            this.setDrawerLockMode(savedState.lockModeStart, 8388611);
        }
        if (savedState.lockModeEnd != 3) {
            this.setDrawerLockMode(savedState.lockModeEnd, 8388613);
        }
    }
    
    public void onRtlPropertiesChanged(final int n) {
        this.resolveShadowDrawables();
    }
    
    protected Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final LayoutParams layoutParams = (LayoutParams)this.getChildAt(i).getLayoutParams();
            final int openState = layoutParams.openState;
            boolean b = false;
            final boolean b2 = openState == 1;
            if (layoutParams.openState == 2) {
                b = true;
            }
            if (b2 || b) {
                savedState.openDrawerGravity = layoutParams.gravity;
                break;
            }
        }
        savedState.lockModeLeft = this.mLockModeLeft;
        savedState.lockModeRight = this.mLockModeRight;
        savedState.lockModeStart = this.mLockModeStart;
        savedState.lockModeEnd = this.mLockModeEnd;
        return (Parcelable)savedState;
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        this.mLeftDragger.processTouchEvent(motionEvent);
        this.mRightDragger.processTouchEvent(motionEvent);
        final int n = motionEvent.getAction() & 0xFF;
        if (n != 0) {
            final boolean b = true;
            if (n != 1) {
                if (n == 3) {
                    this.closeDrawers(true);
                    this.mDisallowInterceptRequested = false;
                    this.mChildrenCanceledTouch = false;
                }
            }
            else {
                final float x = motionEvent.getX();
                final float y = motionEvent.getY();
                final boolean b2 = true;
                final View topChildUnder = this.mLeftDragger.findTopChildUnder((int)x, (int)y);
                boolean b3 = b2;
                if (topChildUnder != null) {
                    b3 = b2;
                    if (this.isContentView(topChildUnder)) {
                        final float n2 = x - this.mInitialMotionX;
                        final float n3 = y - this.mInitialMotionY;
                        final int touchSlop = this.mLeftDragger.getTouchSlop();
                        b3 = b2;
                        if (n2 * n2 + n3 * n3 < touchSlop * touchSlop) {
                            final View openDrawer = this.findOpenDrawer();
                            b3 = b2;
                            if (openDrawer != null) {
                                b3 = (this.getDrawerLockMode(openDrawer) == 2 && b);
                            }
                        }
                    }
                }
                this.closeDrawers(b3);
                this.mDisallowInterceptRequested = false;
            }
        }
        else {
            final float x2 = motionEvent.getX();
            final float y2 = motionEvent.getY();
            this.mInitialMotionX = x2;
            this.mInitialMotionY = y2;
            this.mDisallowInterceptRequested = false;
            this.mChildrenCanceledTouch = false;
        }
        return true;
    }
    
    public void openDrawer(final int n) {
        this.openDrawer(n, true);
    }
    
    public void openDrawer(final int n, final boolean b) {
        final View drawerWithGravity = this.findDrawerWithGravity(n);
        if (drawerWithGravity != null) {
            this.openDrawer(drawerWithGravity, b);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("No drawer view found with gravity ");
        sb.append(gravityToString(n));
        throw new IllegalArgumentException(sb.toString());
    }
    
    public void openDrawer(final View view) {
        this.openDrawer(view, true);
    }
    
    public void openDrawer(final View obj, final boolean b) {
        if (this.isDrawerView(obj)) {
            final LayoutParams layoutParams = (LayoutParams)obj.getLayoutParams();
            if (this.mFirstLayout) {
                layoutParams.onScreen = 1.0f;
                layoutParams.openState = 1;
                this.updateChildrenImportantForAccessibility(obj, true);
            }
            else if (b) {
                layoutParams.openState |= 0x2;
                if (this.checkDrawerViewAbsoluteGravity(obj, 3)) {
                    this.mLeftDragger.smoothSlideViewTo(obj, 0, obj.getTop());
                }
                else {
                    this.mRightDragger.smoothSlideViewTo(obj, this.getWidth() - obj.getWidth(), obj.getTop());
                }
            }
            else {
                this.moveDrawerToOffset(obj, 1.0f);
                this.updateDrawerState(layoutParams.gravity, 0, obj);
                obj.setVisibility(0);
            }
            this.invalidate();
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("View ");
        sb.append(obj);
        sb.append(" is not a sliding drawer");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public void removeDrawerListener(final DrawerListener drawerListener) {
        if (drawerListener == null) {
            return;
        }
        final List<DrawerListener> mListeners = this.mListeners;
        if (mListeners == null) {
            return;
        }
        mListeners.remove(drawerListener);
    }
    
    public void requestDisallowInterceptTouchEvent(final boolean mDisallowInterceptRequested) {
        super.requestDisallowInterceptTouchEvent(mDisallowInterceptRequested);
        this.mDisallowInterceptRequested = mDisallowInterceptRequested;
        if (mDisallowInterceptRequested) {
            this.closeDrawers(true);
        }
    }
    
    public void requestLayout() {
        if (!this.mInLayout) {
            super.requestLayout();
        }
    }
    
    public void setChildInsets(final Object mLastInsets, final boolean mDrawStatusBarBackground) {
        this.mLastInsets = mLastInsets;
        this.mDrawStatusBarBackground = mDrawStatusBarBackground;
        this.setWillNotDraw(!mDrawStatusBarBackground && this.getBackground() == null);
        this.requestLayout();
    }
    
    public void setDrawerElevation(final float mDrawerElevation) {
        this.mDrawerElevation = mDrawerElevation;
        for (int i = 0; i < this.getChildCount(); ++i) {
            final View child = this.getChildAt(i);
            if (this.isDrawerView(child)) {
                ViewCompat.setElevation(child, this.mDrawerElevation);
            }
        }
    }
    
    @Deprecated
    public void setDrawerListener(final DrawerListener mListener) {
        final DrawerListener mListener2 = this.mListener;
        if (mListener2 != null) {
            this.removeDrawerListener(mListener2);
        }
        if (mListener != null) {
            this.addDrawerListener(mListener);
        }
        this.mListener = mListener;
    }
    
    public void setDrawerLockMode(final int n) {
        this.setDrawerLockMode(n, 3);
        this.setDrawerLockMode(n, 5);
    }
    
    public void setDrawerLockMode(final int n, final int n2) {
        final int absoluteGravity = GravityCompat.getAbsoluteGravity(n2, ViewCompat.getLayoutDirection((View)this));
        if (n2 != 3) {
            if (n2 != 5) {
                if (n2 != 8388611) {
                    if (n2 == 8388613) {
                        this.mLockModeEnd = n;
                    }
                }
                else {
                    this.mLockModeStart = n;
                }
            }
            else {
                this.mLockModeRight = n;
            }
        }
        else {
            this.mLockModeLeft = n;
        }
        if (n != 0) {
            ViewDragHelper viewDragHelper;
            if (absoluteGravity == 3) {
                viewDragHelper = this.mLeftDragger;
            }
            else {
                viewDragHelper = this.mRightDragger;
            }
            viewDragHelper.cancel();
        }
        if (n != 1) {
            if (n == 2) {
                final View drawerWithGravity = this.findDrawerWithGravity(absoluteGravity);
                if (drawerWithGravity != null) {
                    this.openDrawer(drawerWithGravity);
                }
            }
        }
        else {
            final View drawerWithGravity2 = this.findDrawerWithGravity(absoluteGravity);
            if (drawerWithGravity2 != null) {
                this.closeDrawer(drawerWithGravity2);
            }
        }
    }
    
    public void setDrawerLockMode(final int n, final View obj) {
        if (this.isDrawerView(obj)) {
            this.setDrawerLockMode(n, ((LayoutParams)obj.getLayoutParams()).gravity);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("View ");
        sb.append(obj);
        sb.append(" is not a ");
        sb.append("drawer with appropriate layout_gravity");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public void setDrawerShadow(final int n, final int n2) {
        this.setDrawerShadow(ContextCompat.getDrawable(this.getContext(), n), n2);
    }
    
    public void setDrawerShadow(final Drawable drawable, final int n) {
        if (DrawerLayout.SET_DRAWER_SHADOW_FROM_ELEVATION) {
            return;
        }
        if ((n & 0x800003) == 0x800003) {
            this.mShadowStart = drawable;
        }
        else if ((n & 0x800005) == 0x800005) {
            this.mShadowEnd = drawable;
        }
        else if ((n & 0x3) == 0x3) {
            this.mShadowLeft = drawable;
        }
        else {
            if ((n & 0x5) != 0x5) {
                return;
            }
            this.mShadowRight = drawable;
        }
        this.resolveShadowDrawables();
        this.invalidate();
    }
    
    public void setDrawerTitle(int absoluteGravity, final CharSequence charSequence) {
        absoluteGravity = GravityCompat.getAbsoluteGravity(absoluteGravity, ViewCompat.getLayoutDirection((View)this));
        if (absoluteGravity == 3) {
            this.mTitleLeft = charSequence;
        }
        else if (absoluteGravity == 5) {
            this.mTitleRight = charSequence;
        }
    }
    
    void setDrawerViewOffset(final View view, final float onScreen) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (onScreen == layoutParams.onScreen) {
            return;
        }
        this.dispatchOnDrawerSlide(view, layoutParams.onScreen = onScreen);
    }
    
    public void setScrimColor(final int mScrimColor) {
        this.mScrimColor = mScrimColor;
        this.invalidate();
    }
    
    public void setStatusBarBackground(final int n) {
        Drawable drawable;
        if (n != 0) {
            drawable = ContextCompat.getDrawable(this.getContext(), n);
        }
        else {
            drawable = null;
        }
        this.mStatusBarBackground = drawable;
        this.invalidate();
    }
    
    public void setStatusBarBackground(final Drawable mStatusBarBackground) {
        this.mStatusBarBackground = mStatusBarBackground;
        this.invalidate();
    }
    
    public void setStatusBarBackgroundColor(final int n) {
        this.mStatusBarBackground = (Drawable)new ColorDrawable(n);
        this.invalidate();
    }
    
    void updateDrawerState(int viewDragState, int i, final View view) {
        viewDragState = this.mLeftDragger.getViewDragState();
        final int viewDragState2 = this.mRightDragger.getViewDragState();
        if (viewDragState != 1 && viewDragState2 != 1) {
            if (viewDragState != 2 && viewDragState2 != 2) {
                viewDragState = 0;
            }
            else {
                viewDragState = 2;
            }
        }
        else {
            viewDragState = 1;
        }
        if (view != null && i == 0) {
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (layoutParams.onScreen == 0.0f) {
                this.dispatchOnDrawerClosed(view);
            }
            else if (layoutParams.onScreen == 1.0f) {
                this.dispatchOnDrawerOpened(view);
            }
        }
        if (viewDragState != this.mDrawerState) {
            this.mDrawerState = viewDragState;
            final List<DrawerListener> mListeners = this.mListeners;
            if (mListeners != null) {
                for (i = mListeners.size() - 1; i >= 0; --i) {
                    this.mListeners.get(i).onDrawerStateChanged(viewDragState);
                }
            }
        }
    }
    
    class AccessibilityDelegate extends AccessibilityDelegateCompat
    {
        private final Rect mTmpRect;
        
        AccessibilityDelegate() {
            this.mTmpRect = new Rect();
        }
        
        private void addChildrenForAccessibility(final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, final ViewGroup viewGroup) {
            for (int childCount = viewGroup.getChildCount(), i = 0; i < childCount; ++i) {
                final View child = viewGroup.getChildAt(i);
                if (DrawerLayout.includeChildForAccessibility(child)) {
                    accessibilityNodeInfoCompat.addChild(child);
                }
            }
        }
        
        private void copyNodeInfoNoChildren(final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2) {
            final Rect mTmpRect = this.mTmpRect;
            accessibilityNodeInfoCompat2.getBoundsInParent(mTmpRect);
            accessibilityNodeInfoCompat.setBoundsInParent(mTmpRect);
            accessibilityNodeInfoCompat2.getBoundsInScreen(mTmpRect);
            accessibilityNodeInfoCompat.setBoundsInScreen(mTmpRect);
            accessibilityNodeInfoCompat.setVisibleToUser(accessibilityNodeInfoCompat2.isVisibleToUser());
            accessibilityNodeInfoCompat.setPackageName(accessibilityNodeInfoCompat2.getPackageName());
            accessibilityNodeInfoCompat.setClassName(accessibilityNodeInfoCompat2.getClassName());
            accessibilityNodeInfoCompat.setContentDescription(accessibilityNodeInfoCompat2.getContentDescription());
            accessibilityNodeInfoCompat.setEnabled(accessibilityNodeInfoCompat2.isEnabled());
            accessibilityNodeInfoCompat.setClickable(accessibilityNodeInfoCompat2.isClickable());
            accessibilityNodeInfoCompat.setFocusable(accessibilityNodeInfoCompat2.isFocusable());
            accessibilityNodeInfoCompat.setFocused(accessibilityNodeInfoCompat2.isFocused());
            accessibilityNodeInfoCompat.setAccessibilityFocused(accessibilityNodeInfoCompat2.isAccessibilityFocused());
            accessibilityNodeInfoCompat.setSelected(accessibilityNodeInfoCompat2.isSelected());
            accessibilityNodeInfoCompat.setLongClickable(accessibilityNodeInfoCompat2.isLongClickable());
            accessibilityNodeInfoCompat.addAction(accessibilityNodeInfoCompat2.getActions());
        }
        
        @Override
        public boolean dispatchPopulateAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
            if (accessibilityEvent.getEventType() == 32) {
                final List text = accessibilityEvent.getText();
                final View visibleDrawer = DrawerLayout.this.findVisibleDrawer();
                if (visibleDrawer != null) {
                    final CharSequence drawerTitle = DrawerLayout.this.getDrawerTitle(DrawerLayout.this.getDrawerViewAbsoluteGravity(visibleDrawer));
                    if (drawerTitle != null) {
                        text.add(drawerTitle);
                    }
                }
                return true;
            }
            return super.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
        }
        
        @Override
        public void onInitializeAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(view, accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)DrawerLayout.class.getName());
        }
        
        @Override
        public void onInitializeAccessibilityNodeInfo(final View source, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            if (DrawerLayout.CAN_HIDE_DESCENDANTS) {
                super.onInitializeAccessibilityNodeInfo(source, accessibilityNodeInfoCompat);
            }
            else {
                final AccessibilityNodeInfoCompat obtain = AccessibilityNodeInfoCompat.obtain(accessibilityNodeInfoCompat);
                super.onInitializeAccessibilityNodeInfo(source, obtain);
                accessibilityNodeInfoCompat.setSource(source);
                final ViewParent parentForAccessibility = ViewCompat.getParentForAccessibility(source);
                if (parentForAccessibility instanceof View) {
                    accessibilityNodeInfoCompat.setParent((View)parentForAccessibility);
                }
                this.copyNodeInfoNoChildren(accessibilityNodeInfoCompat, obtain);
                obtain.recycle();
                this.addChildrenForAccessibility(accessibilityNodeInfoCompat, (ViewGroup)source);
            }
            accessibilityNodeInfoCompat.setClassName(DrawerLayout.class.getName());
            accessibilityNodeInfoCompat.setFocusable(false);
            accessibilityNodeInfoCompat.setFocused(false);
            accessibilityNodeInfoCompat.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_FOCUS);
            accessibilityNodeInfoCompat.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLEAR_FOCUS);
        }
        
        @Override
        public boolean onRequestSendAccessibilityEvent(final ViewGroup viewGroup, final View view, final AccessibilityEvent accessibilityEvent) {
            return (DrawerLayout.CAN_HIDE_DESCENDANTS || DrawerLayout.includeChildForAccessibility(view)) && super.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
        }
    }
    
    static final class ChildAccessibilityDelegate extends AccessibilityDelegateCompat
    {
        @Override
        public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            if (!DrawerLayout.includeChildForAccessibility(view)) {
                accessibilityNodeInfoCompat.setParent(null);
            }
        }
    }
    
    public interface DrawerListener
    {
        void onDrawerClosed(final View p0);
        
        void onDrawerOpened(final View p0);
        
        void onDrawerSlide(final View p0, final float p1);
        
        void onDrawerStateChanged(final int p0);
    }
    
    public static class LayoutParams extends ViewGroup$MarginLayoutParams
    {
        private static final int FLAG_IS_CLOSING = 4;
        private static final int FLAG_IS_OPENED = 1;
        private static final int FLAG_IS_OPENING = 2;
        public int gravity;
        boolean isPeeking;
        float onScreen;
        int openState;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.gravity = 0;
        }
        
        public LayoutParams(final int n, final int n2, final int gravity) {
            this(n, n2);
            this.gravity = gravity;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
            this.gravity = 0;
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, DrawerLayout.LAYOUT_ATTRS);
            this.gravity = obtainStyledAttributes.getInt(0, 0);
            obtainStyledAttributes.recycle();
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            this.gravity = 0;
        }
        
        public LayoutParams(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            super(viewGroup$MarginLayoutParams);
            this.gravity = 0;
        }
        
        public LayoutParams(final LayoutParams layoutParams) {
            super((ViewGroup$MarginLayoutParams)layoutParams);
            this.gravity = 0;
            this.gravity = layoutParams.gravity;
        }
    }
    
    protected static class SavedState extends AbsSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        int lockModeEnd;
        int lockModeLeft;
        int lockModeRight;
        int lockModeStart;
        int openDrawerGravity;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$ClassLoaderCreator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel, null);
                }
                
                public SavedState createFromParcel(final Parcel parcel, final ClassLoader classLoader) {
                    return new SavedState(parcel, classLoader);
                }
                
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }
        
        public SavedState(final Parcel parcel, final ClassLoader classLoader) {
            super(parcel, classLoader);
            this.openDrawerGravity = 0;
            this.openDrawerGravity = parcel.readInt();
            this.lockModeLeft = parcel.readInt();
            this.lockModeRight = parcel.readInt();
            this.lockModeStart = parcel.readInt();
            this.lockModeEnd = parcel.readInt();
        }
        
        public SavedState(final Parcelable parcelable) {
            super(parcelable);
            this.openDrawerGravity = 0;
        }
        
        @Override
        public void writeToParcel(final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.openDrawerGravity);
            parcel.writeInt(this.lockModeLeft);
            parcel.writeInt(this.lockModeRight);
            parcel.writeInt(this.lockModeStart);
            parcel.writeInt(this.lockModeEnd);
        }
    }
    
    public abstract static class SimpleDrawerListener implements DrawerListener
    {
        @Override
        public void onDrawerClosed(final View view) {
        }
        
        @Override
        public void onDrawerOpened(final View view) {
        }
        
        @Override
        public void onDrawerSlide(final View view, final float n) {
        }
        
        @Override
        public void onDrawerStateChanged(final int n) {
        }
    }
    
    private class ViewDragCallback extends Callback
    {
        private final int mAbsGravity;
        private ViewDragHelper mDragger;
        private final Runnable mPeekRunnable;
        
        ViewDragCallback(final int mAbsGravity) {
            this.mPeekRunnable = new Runnable() {
                @Override
                public void run() {
                    ViewDragCallback.this.peekDrawer();
                }
            };
            this.mAbsGravity = mAbsGravity;
        }
        
        private void closeOtherDrawer() {
            final int mAbsGravity = this.mAbsGravity;
            int n = 3;
            if (mAbsGravity == 3) {
                n = 5;
            }
            final View drawerWithGravity = DrawerLayout.this.findDrawerWithGravity(n);
            if (drawerWithGravity != null) {
                DrawerLayout.this.closeDrawer(drawerWithGravity);
            }
        }
        
        @Override
        public int clampViewPositionHorizontal(final View view, final int n, int width) {
            if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(view, 3)) {
                return Math.max(-view.getWidth(), Math.min(n, 0));
            }
            width = DrawerLayout.this.getWidth();
            return Math.max(width - view.getWidth(), Math.min(n, width));
        }
        
        @Override
        public int clampViewPositionVertical(final View view, final int n, final int n2) {
            return view.getTop();
        }
        
        @Override
        public int getViewHorizontalDragRange(final View view) {
            int width;
            if (DrawerLayout.this.isDrawerView(view)) {
                width = view.getWidth();
            }
            else {
                width = 0;
            }
            return width;
        }
        
        @Override
        public void onEdgeDragStarted(final int n, final int n2) {
            View view;
            if ((n & 0x1) == 0x1) {
                view = DrawerLayout.this.findDrawerWithGravity(3);
            }
            else {
                view = DrawerLayout.this.findDrawerWithGravity(5);
            }
            if (view != null && DrawerLayout.this.getDrawerLockMode(view) == 0) {
                this.mDragger.captureChildView(view, n2);
            }
        }
        
        @Override
        public boolean onEdgeLock(final int n) {
            return false;
        }
        
        @Override
        public void onEdgeTouched(final int n, final int n2) {
            DrawerLayout.this.postDelayed(this.mPeekRunnable, 160L);
        }
        
        @Override
        public void onViewCaptured(final View view, final int n) {
            ((LayoutParams)view.getLayoutParams()).isPeeking = false;
            this.closeOtherDrawer();
        }
        
        @Override
        public void onViewDragStateChanged(final int n) {
            DrawerLayout.this.updateDrawerState(this.mAbsGravity, n, this.mDragger.getCapturedView());
        }
        
        @Override
        public void onViewPositionChanged(final View view, int visibility, int width, final int n, final int n2) {
            width = view.getWidth();
            float n3;
            if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(view, 3)) {
                n3 = (width + visibility) / (float)width;
            }
            else {
                n3 = (DrawerLayout.this.getWidth() - visibility) / (float)width;
            }
            DrawerLayout.this.setDrawerViewOffset(view, n3);
            if (n3 == 0.0f) {
                visibility = 4;
            }
            else {
                visibility = 0;
            }
            view.setVisibility(visibility);
            DrawerLayout.this.invalidate();
        }
        
        @Override
        public void onViewReleased(final View view, final float n, float drawerViewOffset) {
            drawerViewOffset = DrawerLayout.this.getDrawerViewOffset(view);
            final int width = view.getWidth();
            int width2;
            if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(view, 3)) {
                if (n <= 0.0f && (n != 0.0f || drawerViewOffset <= 0.5f)) {
                    width2 = -width;
                }
                else {
                    width2 = 0;
                }
            }
            else {
                width2 = DrawerLayout.this.getWidth();
                if (n < 0.0f || (n == 0.0f && drawerViewOffset > 0.5f)) {
                    width2 -= width;
                }
            }
            this.mDragger.settleCapturedViewAt(width2, view.getTop());
            DrawerLayout.this.invalidate();
        }
        
        void peekDrawer() {
            final int edgeSize = this.mDragger.getEdgeSize();
            final int mAbsGravity = this.mAbsGravity;
            int n = 0;
            final boolean b = mAbsGravity == 3;
            View view;
            int n2;
            if (b) {
                view = DrawerLayout.this.findDrawerWithGravity(3);
                if (view != null) {
                    n = -view.getWidth();
                }
                n2 = n + edgeSize;
            }
            else {
                view = DrawerLayout.this.findDrawerWithGravity(5);
                n2 = DrawerLayout.this.getWidth() - edgeSize;
            }
            if (view != null && ((b && view.getLeft() < n2) || (!b && view.getLeft() > n2)) && DrawerLayout.this.getDrawerLockMode(view) == 0) {
                final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                this.mDragger.smoothSlideViewTo(view, n2, view.getTop());
                layoutParams.isPeeking = true;
                DrawerLayout.this.invalidate();
                this.closeOtherDrawer();
                DrawerLayout.this.cancelChildViewTouch();
            }
        }
        
        public void removeCallbacks() {
            DrawerLayout.this.removeCallbacks(this.mPeekRunnable);
        }
        
        public void setDragger(final ViewDragHelper mDragger) {
            this.mDragger = mDragger;
        }
        
        @Override
        public boolean tryCaptureView(final View view, final int n) {
            return DrawerLayout.this.isDrawerView(view) && DrawerLayout.this.checkDrawerViewAbsoluteGravity(view, this.mAbsGravity) && DrawerLayout.this.getDrawerLockMode(view) == 0;
        }
    }
}
