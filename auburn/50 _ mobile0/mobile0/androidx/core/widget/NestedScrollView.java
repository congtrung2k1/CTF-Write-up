// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.widget;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.view.View$BaseSavedState;
import android.os.Bundle;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.accessibility.AccessibilityRecord;
import androidx.core.view.accessibility.AccessibilityRecordCompat;
import android.widget.ScrollView;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.AnimationUtils;
import android.os.Parcelable;
import android.util.Log;
import android.view.ViewGroup$MarginLayoutParams;
import android.view.View$MeasureSpec;
import android.os.Build$VERSION;
import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.FocusFinder;
import android.view.ViewGroup$LayoutParams;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.view.ViewConfiguration;
import android.util.TypedValue;
import java.util.ArrayList;
import android.widget.FrameLayout$LayoutParams;
import android.content.res.TypedArray;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import android.view.ViewGroup;
import android.util.AttributeSet;
import android.content.Context;
import android.view.VelocityTracker;
import android.graphics.Rect;
import android.widget.OverScroller;
import androidx.core.view.NestedScrollingParentHelper;
import android.widget.EdgeEffect;
import android.view.View;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ScrollingView;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingParent2;
import android.widget.FrameLayout;

public class NestedScrollView extends FrameLayout implements NestedScrollingParent2, NestedScrollingChild2, ScrollingView
{
    private static final AccessibilityDelegate ACCESSIBILITY_DELEGATE;
    static final int ANIMATED_SCROLL_GAP = 250;
    private static final int INVALID_POINTER = -1;
    static final float MAX_SCROLL_FACTOR = 0.5f;
    private static final int[] SCROLLVIEW_STYLEABLE;
    private static final String TAG = "NestedScrollView";
    private int mActivePointerId;
    private final NestedScrollingChildHelper mChildHelper;
    private View mChildToScrollTo;
    private EdgeEffect mEdgeGlowBottom;
    private EdgeEffect mEdgeGlowTop;
    private boolean mFillViewport;
    private boolean mIsBeingDragged;
    private boolean mIsLaidOut;
    private boolean mIsLayoutDirty;
    private int mLastMotionY;
    private long mLastScroll;
    private int mLastScrollerY;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private int mNestedYOffset;
    private OnScrollChangeListener mOnScrollChangeListener;
    private final NestedScrollingParentHelper mParentHelper;
    private SavedState mSavedState;
    private final int[] mScrollConsumed;
    private final int[] mScrollOffset;
    private OverScroller mScroller;
    private boolean mSmoothScrollingEnabled;
    private final Rect mTempRect;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    private float mVerticalScrollFactor;
    
    static {
        ACCESSIBILITY_DELEGATE = new AccessibilityDelegate();
        SCROLLVIEW_STYLEABLE = new int[] { 16843130 };
    }
    
    public NestedScrollView(final Context context) {
        this(context, null);
    }
    
    public NestedScrollView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public NestedScrollView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mTempRect = new Rect();
        this.mIsLayoutDirty = true;
        this.mIsLaidOut = false;
        this.mChildToScrollTo = null;
        this.mIsBeingDragged = false;
        this.mSmoothScrollingEnabled = true;
        this.mActivePointerId = -1;
        this.mScrollOffset = new int[2];
        this.mScrollConsumed = new int[2];
        this.initScrollView();
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, NestedScrollView.SCROLLVIEW_STYLEABLE, n, 0);
        this.setFillViewport(obtainStyledAttributes.getBoolean(0, false));
        obtainStyledAttributes.recycle();
        this.mParentHelper = new NestedScrollingParentHelper((ViewGroup)this);
        this.mChildHelper = new NestedScrollingChildHelper((View)this);
        this.setNestedScrollingEnabled(true);
        ViewCompat.setAccessibilityDelegate((View)this, NestedScrollView.ACCESSIBILITY_DELEGATE);
    }
    
    private boolean canScroll() {
        final int childCount = this.getChildCount();
        boolean b = false;
        if (childCount > 0) {
            final View child = this.getChildAt(0);
            final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)child.getLayoutParams();
            if (child.getHeight() + frameLayout$LayoutParams.topMargin + frameLayout$LayoutParams.bottomMargin > this.getHeight() - this.getPaddingTop() - this.getPaddingBottom()) {
                b = true;
            }
            return b;
        }
        return false;
    }
    
    private static int clamp(final int n, final int n2, final int n3) {
        if (n2 >= n3 || n < 0) {
            return 0;
        }
        if (n2 + n > n3) {
            return n3 - n2;
        }
        return n;
    }
    
    private void doScrollY(final int n) {
        if (n != 0) {
            if (this.mSmoothScrollingEnabled) {
                this.smoothScrollBy(0, n);
            }
            else {
                this.scrollBy(0, n);
            }
        }
    }
    
    private void endDrag() {
        this.mIsBeingDragged = false;
        this.recycleVelocityTracker();
        this.stopNestedScroll(0);
        final EdgeEffect mEdgeGlowTop = this.mEdgeGlowTop;
        if (mEdgeGlowTop != null) {
            mEdgeGlowTop.onRelease();
            this.mEdgeGlowBottom.onRelease();
        }
    }
    
    private void ensureGlows() {
        if (this.getOverScrollMode() != 2) {
            if (this.mEdgeGlowTop == null) {
                final Context context = this.getContext();
                this.mEdgeGlowTop = new EdgeEffect(context);
                this.mEdgeGlowBottom = new EdgeEffect(context);
            }
        }
        else {
            this.mEdgeGlowTop = null;
            this.mEdgeGlowBottom = null;
        }
    }
    
    private View findFocusableViewInBounds(final boolean b, final int n, final int n2) {
        final ArrayList focusables = this.getFocusables(2);
        View view = null;
        int n3 = 0;
        View view3;
        int n4;
        for (int size = focusables.size(), i = 0; i < size; ++i, view = view3, n3 = n4) {
            final View view2 = focusables.get(i);
            final int top = view2.getTop();
            final int bottom = view2.getBottom();
            view3 = view;
            n4 = n3;
            if (n < bottom) {
                view3 = view;
                n4 = n3;
                if (top < n2) {
                    boolean b2 = false;
                    final boolean b3 = n < top && bottom < n2;
                    if (view == null) {
                        view3 = view2;
                        n4 = (b3 ? 1 : 0);
                    }
                    else {
                        if ((b && top < view.getTop()) || (!b && bottom > view.getBottom())) {
                            b2 = true;
                        }
                        if (n3 != 0) {
                            view3 = view;
                            n4 = n3;
                            if (b3) {
                                view3 = view;
                                n4 = n3;
                                if (b2) {
                                    view3 = view2;
                                    n4 = n3;
                                }
                            }
                        }
                        else if (b3) {
                            view3 = view2;
                            n4 = 1;
                        }
                        else {
                            view3 = view;
                            n4 = n3;
                            if (b2) {
                                view3 = view2;
                                n4 = n3;
                            }
                        }
                    }
                }
            }
        }
        return view;
    }
    
    private void flingWithNestedDispatch(final int n) {
        final int scrollY = this.getScrollY();
        final boolean b = (scrollY > 0 || n > 0) && (scrollY < this.getScrollRange() || n < 0);
        if (!this.dispatchNestedPreFling(0.0f, (float)n)) {
            this.dispatchNestedFling(0.0f, (float)n, b);
            this.fling(n);
        }
    }
    
    private float getVerticalScrollFactorCompat() {
        if (this.mVerticalScrollFactor == 0.0f) {
            final TypedValue typedValue = new TypedValue();
            final Context context = this.getContext();
            if (!context.getTheme().resolveAttribute(16842829, typedValue, true)) {
                throw new IllegalStateException("Expected theme to define listPreferredItemHeight.");
            }
            this.mVerticalScrollFactor = typedValue.getDimension(context.getResources().getDisplayMetrics());
        }
        return this.mVerticalScrollFactor;
    }
    
    private boolean inChild(final int n, final int n2) {
        final int childCount = this.getChildCount();
        boolean b = false;
        if (childCount > 0) {
            final int scrollY = this.getScrollY();
            final View child = this.getChildAt(0);
            if (n2 >= child.getTop() - scrollY && n2 < child.getBottom() - scrollY && n >= child.getLeft() && n < child.getRight()) {
                b = true;
            }
            return b;
        }
        return false;
    }
    
    private void initOrResetVelocityTracker() {
        final VelocityTracker mVelocityTracker = this.mVelocityTracker;
        if (mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        else {
            mVelocityTracker.clear();
        }
    }
    
    private void initScrollView() {
        this.mScroller = new OverScroller(this.getContext());
        this.setFocusable(true);
        this.setDescendantFocusability(262144);
        this.setWillNotDraw(false);
        final ViewConfiguration value = ViewConfiguration.get(this.getContext());
        this.mTouchSlop = value.getScaledTouchSlop();
        this.mMinimumVelocity = value.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = value.getScaledMaximumFlingVelocity();
    }
    
    private void initVelocityTrackerIfNotExists() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
    }
    
    private boolean isOffScreen(final View view) {
        return this.isWithinDeltaOfScreen(view, 0, this.getHeight()) ^ true;
    }
    
    private static boolean isViewDescendantOf(final View view, final View view2) {
        boolean b = true;
        if (view == view2) {
            return true;
        }
        final ViewParent parent = view.getParent();
        if (!(parent instanceof ViewGroup) || !isViewDescendantOf((View)parent, view2)) {
            b = false;
        }
        return b;
    }
    
    private boolean isWithinDeltaOfScreen(final View view, final int n, final int n2) {
        view.getDrawingRect(this.mTempRect);
        this.offsetDescendantRectToMyCoords(view, this.mTempRect);
        return this.mTempRect.bottom + n >= this.getScrollY() && this.mTempRect.top - n <= this.getScrollY() + n2;
    }
    
    private void onSecondaryPointerUp(final MotionEvent motionEvent) {
        final int actionIndex = motionEvent.getActionIndex();
        if (motionEvent.getPointerId(actionIndex) == this.mActivePointerId) {
            int n;
            if (actionIndex == 0) {
                n = 1;
            }
            else {
                n = 0;
            }
            this.mLastMotionY = (int)motionEvent.getY(n);
            this.mActivePointerId = motionEvent.getPointerId(n);
            final VelocityTracker mVelocityTracker = this.mVelocityTracker;
            if (mVelocityTracker != null) {
                mVelocityTracker.clear();
            }
        }
    }
    
    private void recycleVelocityTracker() {
        final VelocityTracker mVelocityTracker = this.mVelocityTracker;
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }
    
    private boolean scrollAndFocus(final int n, int n2, final int n3) {
        final boolean b = true;
        final int height = this.getHeight();
        final int scrollY = this.getScrollY();
        final int n4 = scrollY + height;
        final boolean b2 = n == 33;
        Object focusableViewInBounds;
        if ((focusableViewInBounds = this.findFocusableViewInBounds(b2, n2, n3)) == null) {
            focusableViewInBounds = this;
        }
        boolean b3;
        if (n2 >= scrollY && n3 <= n4) {
            b3 = false;
        }
        else {
            if (b2) {
                n2 -= scrollY;
            }
            else {
                n2 = n3 - n4;
            }
            this.doScrollY(n2);
            b3 = b;
        }
        if (focusableViewInBounds != this.findFocus()) {
            ((View)focusableViewInBounds).requestFocus(n);
        }
        return b3;
    }
    
    private void scrollToChild(final View view) {
        view.getDrawingRect(this.mTempRect);
        this.offsetDescendantRectToMyCoords(view, this.mTempRect);
        final int computeScrollDeltaToGetChildRectOnScreen = this.computeScrollDeltaToGetChildRectOnScreen(this.mTempRect);
        if (computeScrollDeltaToGetChildRectOnScreen != 0) {
            this.scrollBy(0, computeScrollDeltaToGetChildRectOnScreen);
        }
    }
    
    private boolean scrollToChildRect(final Rect rect, final boolean b) {
        final int computeScrollDeltaToGetChildRectOnScreen = this.computeScrollDeltaToGetChildRectOnScreen(rect);
        final boolean b2 = computeScrollDeltaToGetChildRectOnScreen != 0;
        if (b2) {
            if (b) {
                this.scrollBy(0, computeScrollDeltaToGetChildRectOnScreen);
            }
            else {
                this.smoothScrollBy(0, computeScrollDeltaToGetChildRectOnScreen);
            }
        }
        return b2;
    }
    
    public void addView(final View view) {
        if (this.getChildCount() <= 0) {
            super.addView(view);
            return;
        }
        throw new IllegalStateException("ScrollView can host only one direct child");
    }
    
    public void addView(final View view, final int n) {
        if (this.getChildCount() <= 0) {
            super.addView(view, n);
            return;
        }
        throw new IllegalStateException("ScrollView can host only one direct child");
    }
    
    public void addView(final View view, final int n, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        if (this.getChildCount() <= 0) {
            super.addView(view, n, viewGroup$LayoutParams);
            return;
        }
        throw new IllegalStateException("ScrollView can host only one direct child");
    }
    
    public void addView(final View view, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        if (this.getChildCount() <= 0) {
            super.addView(view, viewGroup$LayoutParams);
            return;
        }
        throw new IllegalStateException("ScrollView can host only one direct child");
    }
    
    public boolean arrowScroll(int descendantFocusability) {
        View focus;
        if ((focus = this.findFocus()) == this) {
            focus = null;
        }
        final View nextFocus = FocusFinder.getInstance().findNextFocus((ViewGroup)this, focus, descendantFocusability);
        final int maxScrollAmount = this.getMaxScrollAmount();
        if (nextFocus != null && this.isWithinDeltaOfScreen(nextFocus, maxScrollAmount, this.getHeight())) {
            nextFocus.getDrawingRect(this.mTempRect);
            this.offsetDescendantRectToMyCoords(nextFocus, this.mTempRect);
            this.doScrollY(this.computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
            nextFocus.requestFocus(descendantFocusability);
        }
        else {
            final int n = maxScrollAmount;
            int n2;
            if (descendantFocusability == 33 && this.getScrollY() < n) {
                n2 = this.getScrollY();
            }
            else {
                n2 = n;
                if (descendantFocusability == 130) {
                    n2 = n;
                    if (this.getChildCount() > 0) {
                        final View child = this.getChildAt(0);
                        n2 = Math.min(child.getBottom() + ((FrameLayout$LayoutParams)child.getLayoutParams()).bottomMargin - (this.getScrollY() + this.getHeight() - this.getPaddingBottom()), maxScrollAmount);
                    }
                }
            }
            if (n2 == 0) {
                return false;
            }
            if (descendantFocusability != 130) {
                n2 = -n2;
            }
            this.doScrollY(n2);
        }
        if (focus != null && focus.isFocused() && this.isOffScreen(focus)) {
            descendantFocusability = this.getDescendantFocusability();
            this.setDescendantFocusability(131072);
            this.requestFocus();
            this.setDescendantFocusability(descendantFocusability);
        }
        return true;
    }
    
    public int computeHorizontalScrollExtent() {
        return super.computeHorizontalScrollExtent();
    }
    
    public int computeHorizontalScrollOffset() {
        return super.computeHorizontalScrollOffset();
    }
    
    public int computeHorizontalScrollRange() {
        return super.computeHorizontalScrollRange();
    }
    
    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            this.mScroller.getCurrX();
            final int currY = this.mScroller.getCurrY();
            int n = currY - this.mLastScrollerY;
            if (this.dispatchNestedPreScroll(0, n, this.mScrollConsumed, null, 1)) {
                n -= this.mScrollConsumed[1];
            }
            if (n != 0) {
                final int scrollRange = this.getScrollRange();
                final int scrollY = this.getScrollY();
                this.overScrollByCompat(0, n, this.getScrollX(), scrollY, 0, scrollRange, 0, 0, false);
                final int n2 = this.getScrollY() - scrollY;
                if (!this.dispatchNestedScroll(0, n2, 0, n - n2, null, 1)) {
                    final int overScrollMode = this.getOverScrollMode();
                    if (overScrollMode == 0 || (overScrollMode == 1 && scrollRange > 0)) {
                        this.ensureGlows();
                        if (currY <= 0 && scrollY > 0) {
                            this.mEdgeGlowTop.onAbsorb((int)this.mScroller.getCurrVelocity());
                        }
                        else if (currY >= scrollRange && scrollY < scrollRange) {
                            this.mEdgeGlowBottom.onAbsorb((int)this.mScroller.getCurrVelocity());
                        }
                    }
                }
            }
            this.mLastScrollerY = currY;
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
        else {
            if (this.hasNestedScrollingParent(1)) {
                this.stopNestedScroll(1);
            }
            this.mLastScrollerY = 0;
        }
    }
    
    protected int computeScrollDeltaToGetChildRectOnScreen(final Rect rect) {
        if (this.getChildCount() == 0) {
            return 0;
        }
        final int height = this.getHeight();
        final int scrollY = this.getScrollY();
        final int n = scrollY + height;
        final int verticalFadingEdgeLength = this.getVerticalFadingEdgeLength();
        int n2 = scrollY;
        if (rect.top > 0) {
            n2 = scrollY + verticalFadingEdgeLength;
        }
        final View child = this.getChildAt(0);
        final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)child.getLayoutParams();
        int n3 = n;
        if (rect.bottom < child.getHeight() + frameLayout$LayoutParams.topMargin + frameLayout$LayoutParams.bottomMargin) {
            n3 = n - verticalFadingEdgeLength;
        }
        final int n4 = n3;
        final boolean b = false;
        int n5;
        if (rect.bottom > n4 && rect.top > n2) {
            int a;
            if (rect.height() > height) {
                a = 0 + (rect.top - n2);
            }
            else {
                a = 0 + (rect.bottom - n4);
            }
            n5 = Math.min(a, child.getBottom() + frameLayout$LayoutParams.bottomMargin - n);
        }
        else {
            n5 = (b ? 1 : 0);
            if (rect.top < n2) {
                n5 = (b ? 1 : 0);
                if (rect.bottom < n4) {
                    int a2;
                    if (rect.height() > height) {
                        a2 = 0 - (n4 - rect.bottom);
                    }
                    else {
                        a2 = 0 - (n2 - rect.top);
                    }
                    n5 = Math.max(a2, -this.getScrollY());
                }
            }
        }
        return n5;
    }
    
    public int computeVerticalScrollExtent() {
        return super.computeVerticalScrollExtent();
    }
    
    public int computeVerticalScrollOffset() {
        return Math.max(0, super.computeVerticalScrollOffset());
    }
    
    public int computeVerticalScrollRange() {
        final int childCount = this.getChildCount();
        final int n = this.getHeight() - this.getPaddingBottom() - this.getPaddingTop();
        if (childCount == 0) {
            return n;
        }
        final View child = this.getChildAt(0);
        final int n2 = child.getBottom() + ((FrameLayout$LayoutParams)child.getLayoutParams()).bottomMargin;
        final int scrollY = this.getScrollY();
        final int max = Math.max(0, n2 - n);
        int n3;
        if (scrollY < 0) {
            n3 = n2 - scrollY;
        }
        else {
            n3 = n2;
            if (scrollY > max) {
                n3 = n2 + (scrollY - max);
            }
        }
        return n3;
    }
    
    public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent) || this.executeKeyEvent(keyEvent);
    }
    
    public boolean dispatchNestedFling(final float n, final float n2, final boolean b) {
        return this.mChildHelper.dispatchNestedFling(n, n2, b);
    }
    
    public boolean dispatchNestedPreFling(final float n, final float n2) {
        return this.mChildHelper.dispatchNestedPreFling(n, n2);
    }
    
    public boolean dispatchNestedPreScroll(final int n, final int n2, final int[] array, final int[] array2) {
        return this.dispatchNestedPreScroll(n, n2, array, array2, 0);
    }
    
    public boolean dispatchNestedPreScroll(final int n, final int n2, final int[] array, final int[] array2, final int n3) {
        return this.mChildHelper.dispatchNestedPreScroll(n, n2, array, array2, n3);
    }
    
    public boolean dispatchNestedScroll(final int n, final int n2, final int n3, final int n4, final int[] array) {
        return this.dispatchNestedScroll(n, n2, n3, n4, array, 0);
    }
    
    public boolean dispatchNestedScroll(final int n, final int n2, final int n3, final int n4, final int[] array, final int n5) {
        return this.mChildHelper.dispatchNestedScroll(n, n2, n3, n4, array, n5);
    }
    
    public void draw(final Canvas canvas) {
        super.draw(canvas);
        if (this.mEdgeGlowTop != null) {
            final int scrollY = this.getScrollY();
            if (!this.mEdgeGlowTop.isFinished()) {
                final int save = canvas.save();
                final int width = this.getWidth();
                final int height = this.getHeight();
                int n = 0;
                final int min = Math.min(0, scrollY);
                int n2 = 0;
                Label_0095: {
                    if (Build$VERSION.SDK_INT >= 21) {
                        n2 = width;
                        if (!this.getClipToPadding()) {
                            break Label_0095;
                        }
                    }
                    n2 = width - (this.getPaddingLeft() + this.getPaddingRight());
                    n = 0 + this.getPaddingLeft();
                }
                int n3 = height;
                int n4 = min;
                if (Build$VERSION.SDK_INT >= 21) {
                    n3 = height;
                    n4 = min;
                    if (this.getClipToPadding()) {
                        n3 = height - (this.getPaddingTop() + this.getPaddingBottom());
                        n4 = min + this.getPaddingTop();
                    }
                }
                canvas.translate((float)n, (float)n4);
                this.mEdgeGlowTop.setSize(n2, n3);
                if (this.mEdgeGlowTop.draw(canvas)) {
                    ViewCompat.postInvalidateOnAnimation((View)this);
                }
                canvas.restoreToCount(save);
            }
            if (!this.mEdgeGlowBottom.isFinished()) {
                final int save2 = canvas.save();
                final int width2 = this.getWidth();
                final int height2 = this.getHeight();
                int n5 = 0;
                final int n6 = Math.max(this.getScrollRange(), scrollY) + height2;
                int n7 = 0;
                Label_0274: {
                    if (Build$VERSION.SDK_INT >= 21) {
                        n7 = width2;
                        if (!this.getClipToPadding()) {
                            break Label_0274;
                        }
                    }
                    n7 = width2 - (this.getPaddingLeft() + this.getPaddingRight());
                    n5 = 0 + this.getPaddingLeft();
                }
                int n8 = height2;
                int n9 = n6;
                if (Build$VERSION.SDK_INT >= 21) {
                    n8 = height2;
                    n9 = n6;
                    if (this.getClipToPadding()) {
                        n8 = height2 - (this.getPaddingTop() + this.getPaddingBottom());
                        n9 = n6 - this.getPaddingBottom();
                    }
                }
                canvas.translate((float)(n5 - n7), (float)n9);
                canvas.rotate(180.0f, (float)n7, 0.0f);
                this.mEdgeGlowBottom.setSize(n7, n8);
                if (this.mEdgeGlowBottom.draw(canvas)) {
                    ViewCompat.postInvalidateOnAnimation((View)this);
                }
                canvas.restoreToCount(save2);
            }
        }
    }
    
    public boolean executeKeyEvent(final KeyEvent keyEvent) {
        this.mTempRect.setEmpty();
        final boolean canScroll = this.canScroll();
        int n = 130;
        if (canScroll) {
            boolean b2;
            final boolean b = b2 = false;
            if (keyEvent.getAction() == 0) {
                final int keyCode = keyEvent.getKeyCode();
                if (keyCode != 19) {
                    if (keyCode != 20) {
                        if (keyCode != 62) {
                            b2 = b;
                        }
                        else {
                            if (keyEvent.isShiftPressed()) {
                                n = 33;
                            }
                            this.pageScroll(n);
                            b2 = b;
                        }
                    }
                    else if (!keyEvent.isAltPressed()) {
                        b2 = this.arrowScroll(130);
                    }
                    else {
                        b2 = this.fullScroll(130);
                    }
                }
                else if (!keyEvent.isAltPressed()) {
                    b2 = this.arrowScroll(33);
                }
                else {
                    b2 = this.fullScroll(33);
                }
            }
            return b2;
        }
        final boolean focused = this.isFocused();
        boolean b3 = false;
        if (focused && keyEvent.getKeyCode() != 4) {
            View focus;
            if ((focus = this.findFocus()) == this) {
                focus = null;
            }
            final View nextFocus = FocusFinder.getInstance().findNextFocus((ViewGroup)this, focus, 130);
            if (nextFocus != null && nextFocus != this && nextFocus.requestFocus(130)) {
                b3 = true;
            }
            return b3;
        }
        return false;
    }
    
    public void fling(final int n) {
        if (this.getChildCount() > 0) {
            this.startNestedScroll(2, 1);
            this.mScroller.fling(this.getScrollX(), this.getScrollY(), 0, n, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
            this.mLastScrollerY = this.getScrollY();
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }
    
    public boolean fullScroll(final int n) {
        final boolean b = n == 130;
        final int height = this.getHeight();
        this.mTempRect.top = 0;
        this.mTempRect.bottom = height;
        if (b) {
            final int childCount = this.getChildCount();
            if (childCount > 0) {
                final View child = this.getChildAt(childCount - 1);
                this.mTempRect.bottom = child.getBottom() + ((FrameLayout$LayoutParams)child.getLayoutParams()).bottomMargin + this.getPaddingBottom();
                final Rect mTempRect = this.mTempRect;
                mTempRect.top = mTempRect.bottom - height;
            }
        }
        return this.scrollAndFocus(n, this.mTempRect.top, this.mTempRect.bottom);
    }
    
    protected float getBottomFadingEdgeStrength() {
        if (this.getChildCount() == 0) {
            return 0.0f;
        }
        final View child = this.getChildAt(0);
        final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)child.getLayoutParams();
        final int verticalFadingEdgeLength = this.getVerticalFadingEdgeLength();
        final int n = child.getBottom() + frameLayout$LayoutParams.bottomMargin - this.getScrollY() - (this.getHeight() - this.getPaddingBottom());
        if (n < verticalFadingEdgeLength) {
            return n / (float)verticalFadingEdgeLength;
        }
        return 1.0f;
    }
    
    public int getMaxScrollAmount() {
        return (int)(this.getHeight() * 0.5f);
    }
    
    public int getNestedScrollAxes() {
        return this.mParentHelper.getNestedScrollAxes();
    }
    
    int getScrollRange() {
        int max = 0;
        if (this.getChildCount() > 0) {
            final View child = this.getChildAt(0);
            final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)child.getLayoutParams();
            max = Math.max(0, child.getHeight() + frameLayout$LayoutParams.topMargin + frameLayout$LayoutParams.bottomMargin - (this.getHeight() - this.getPaddingTop() - this.getPaddingBottom()));
        }
        return max;
    }
    
    protected float getTopFadingEdgeStrength() {
        if (this.getChildCount() == 0) {
            return 0.0f;
        }
        final int verticalFadingEdgeLength = this.getVerticalFadingEdgeLength();
        final int scrollY = this.getScrollY();
        if (scrollY < verticalFadingEdgeLength) {
            return scrollY / (float)verticalFadingEdgeLength;
        }
        return 1.0f;
    }
    
    public boolean hasNestedScrollingParent() {
        return this.hasNestedScrollingParent(0);
    }
    
    public boolean hasNestedScrollingParent(final int n) {
        return this.mChildHelper.hasNestedScrollingParent(n);
    }
    
    public boolean isFillViewport() {
        return this.mFillViewport;
    }
    
    public boolean isNestedScrollingEnabled() {
        return this.mChildHelper.isNestedScrollingEnabled();
    }
    
    public boolean isSmoothScrollingEnabled() {
        return this.mSmoothScrollingEnabled;
    }
    
    protected void measureChild(final View view, final int n, final int n2) {
        view.measure(getChildMeasureSpec(n, this.getPaddingLeft() + this.getPaddingRight(), view.getLayoutParams().width), View$MeasureSpec.makeMeasureSpec(0, 0));
    }
    
    protected void measureChildWithMargins(final View view, final int n, final int n2, final int n3, final int n4) {
        final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams = (ViewGroup$MarginLayoutParams)view.getLayoutParams();
        view.measure(getChildMeasureSpec(n, this.getPaddingLeft() + this.getPaddingRight() + viewGroup$MarginLayoutParams.leftMargin + viewGroup$MarginLayoutParams.rightMargin + n2, viewGroup$MarginLayoutParams.width), View$MeasureSpec.makeMeasureSpec(viewGroup$MarginLayoutParams.topMargin + viewGroup$MarginLayoutParams.bottomMargin, 0));
    }
    
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mIsLaidOut = false;
    }
    
    public boolean onGenericMotionEvent(final MotionEvent motionEvent) {
        if ((motionEvent.getSource() & 0x2) != 0x0) {
            if (motionEvent.getAction() == 8) {
                if (!this.mIsBeingDragged) {
                    final float axisValue = motionEvent.getAxisValue(9);
                    if (axisValue != 0.0f) {
                        final int n = (int)(this.getVerticalScrollFactorCompat() * axisValue);
                        final int scrollRange = this.getScrollRange();
                        final int scrollY = this.getScrollY();
                        final int n2 = scrollY - n;
                        int n3;
                        if (n2 < 0) {
                            n3 = 0;
                        }
                        else if ((n3 = n2) > scrollRange) {
                            n3 = scrollRange;
                        }
                        if (n3 != scrollY) {
                            super.scrollTo(this.getScrollX(), n3);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        final int action = motionEvent.getAction();
        if (action == 2 && this.mIsBeingDragged) {
            return true;
        }
        final int n = action & 0xFF;
        if (n != 0) {
            if (n != 1) {
                if (n != 2) {
                    if (n != 3) {
                        if (n != 6) {
                            return this.mIsBeingDragged;
                        }
                        this.onSecondaryPointerUp(motionEvent);
                        return this.mIsBeingDragged;
                    }
                }
                else {
                    final int mActivePointerId = this.mActivePointerId;
                    if (mActivePointerId == -1) {
                        return this.mIsBeingDragged;
                    }
                    final int pointerIndex = motionEvent.findPointerIndex(mActivePointerId);
                    if (pointerIndex == -1) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Invalid pointerId=");
                        sb.append(mActivePointerId);
                        sb.append(" in onInterceptTouchEvent");
                        Log.e("NestedScrollView", sb.toString());
                        return this.mIsBeingDragged;
                    }
                    final int mLastMotionY = (int)motionEvent.getY(pointerIndex);
                    if (Math.abs(mLastMotionY - this.mLastMotionY) > this.mTouchSlop && (0x2 & this.getNestedScrollAxes()) == 0x0) {
                        this.mIsBeingDragged = true;
                        this.mLastMotionY = mLastMotionY;
                        this.initVelocityTrackerIfNotExists();
                        this.mVelocityTracker.addMovement(motionEvent);
                        this.mNestedYOffset = 0;
                        final ViewParent parent = this.getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                        }
                        return this.mIsBeingDragged;
                    }
                    return this.mIsBeingDragged;
                }
            }
            this.mIsBeingDragged = false;
            this.mActivePointerId = -1;
            this.recycleVelocityTracker();
            if (this.mScroller.springBack(this.getScrollX(), this.getScrollY(), 0, 0, 0, this.getScrollRange())) {
                ViewCompat.postInvalidateOnAnimation((View)this);
            }
            this.stopNestedScroll(0);
        }
        else {
            final int mLastMotionY2 = (int)motionEvent.getY();
            if (!this.inChild((int)motionEvent.getX(), mLastMotionY2)) {
                this.mIsBeingDragged = false;
                this.recycleVelocityTracker();
            }
            else {
                this.mLastMotionY = mLastMotionY2;
                this.mActivePointerId = motionEvent.getPointerId(0);
                this.initOrResetVelocityTracker();
                this.mVelocityTracker.addMovement(motionEvent);
                this.mScroller.computeScrollOffset();
                this.mIsBeingDragged = (true ^ this.mScroller.isFinished());
                this.startNestedScroll(2, 0);
            }
        }
        return this.mIsBeingDragged;
    }
    
    protected void onLayout(final boolean b, int clamp, final int n, int scrollY, final int n2) {
        super.onLayout(b, clamp, n, scrollY, n2);
        this.mIsLayoutDirty = false;
        final View mChildToScrollTo = this.mChildToScrollTo;
        if (mChildToScrollTo != null && isViewDescendantOf(mChildToScrollTo, (View)this)) {
            this.scrollToChild(this.mChildToScrollTo);
        }
        this.mChildToScrollTo = null;
        if (!this.mIsLaidOut) {
            if (this.mSavedState != null) {
                this.scrollTo(this.getScrollX(), this.mSavedState.scrollPosition);
                this.mSavedState = null;
            }
            clamp = 0;
            if (this.getChildCount() > 0) {
                final View child = this.getChildAt(0);
                final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)child.getLayoutParams();
                clamp = child.getMeasuredHeight() + frameLayout$LayoutParams.topMargin + frameLayout$LayoutParams.bottomMargin;
            }
            final int paddingTop = this.getPaddingTop();
            final int paddingBottom = this.getPaddingBottom();
            scrollY = this.getScrollY();
            clamp = clamp(scrollY, n2 - n - paddingTop - paddingBottom, clamp);
            if (clamp != scrollY) {
                this.scrollTo(this.getScrollX(), clamp);
            }
        }
        this.scrollTo(this.getScrollX(), this.getScrollY());
        this.mIsLaidOut = true;
    }
    
    protected void onMeasure(final int n, int n2) {
        super.onMeasure(n, n2);
        if (!this.mFillViewport) {
            return;
        }
        if (View$MeasureSpec.getMode(n2) == 0) {
            return;
        }
        if (this.getChildCount() > 0) {
            final View child = this.getChildAt(0);
            final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)child.getLayoutParams();
            final int measuredHeight = child.getMeasuredHeight();
            n2 = this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom() - frameLayout$LayoutParams.topMargin - frameLayout$LayoutParams.bottomMargin;
            if (measuredHeight < n2) {
                child.measure(getChildMeasureSpec(n, this.getPaddingLeft() + this.getPaddingRight() + frameLayout$LayoutParams.leftMargin + frameLayout$LayoutParams.rightMargin, frameLayout$LayoutParams.width), View$MeasureSpec.makeMeasureSpec(n2, 1073741824));
            }
        }
    }
    
    public boolean onNestedFling(final View view, final float n, final float n2, final boolean b) {
        if (!b) {
            this.flingWithNestedDispatch((int)n2);
            return true;
        }
        return false;
    }
    
    public boolean onNestedPreFling(final View view, final float n, final float n2) {
        return this.dispatchNestedPreFling(n, n2);
    }
    
    public void onNestedPreScroll(final View view, final int n, final int n2, final int[] array) {
        this.onNestedPreScroll(view, n, n2, array, 0);
    }
    
    public void onNestedPreScroll(final View view, final int n, final int n2, final int[] array, final int n3) {
        this.dispatchNestedPreScroll(n, n2, array, null, n3);
    }
    
    public void onNestedScroll(final View view, final int n, final int n2, final int n3, final int n4) {
        this.onNestedScroll(view, n, n2, n3, n4, 0);
    }
    
    public void onNestedScroll(final View view, int scrollY, final int n, final int n2, final int n3, final int n4) {
        scrollY = this.getScrollY();
        this.scrollBy(0, n3);
        scrollY = this.getScrollY() - scrollY;
        this.dispatchNestedScroll(0, scrollY, 0, n3 - scrollY, null, n4);
    }
    
    public void onNestedScrollAccepted(final View view, final View view2, final int n) {
        this.onNestedScrollAccepted(view, view2, n, 0);
    }
    
    public void onNestedScrollAccepted(final View view, final View view2, final int n, final int n2) {
        this.mParentHelper.onNestedScrollAccepted(view, view2, n, n2);
        this.startNestedScroll(2, n2);
    }
    
    protected void onOverScrolled(final int n, final int n2, final boolean b, final boolean b2) {
        super.scrollTo(n, n2);
    }
    
    protected boolean onRequestFocusInDescendants(final int n, final Rect rect) {
        int n2;
        if (n == 2) {
            n2 = 130;
        }
        else if ((n2 = n) == 1) {
            n2 = 33;
        }
        View view;
        if (rect == null) {
            view = FocusFinder.getInstance().findNextFocus((ViewGroup)this, (View)null, n2);
        }
        else {
            view = FocusFinder.getInstance().findNextFocusFromRect((ViewGroup)this, rect, n2);
        }
        return view != null && !this.isOffScreen(view) && view.requestFocus(n2, rect);
    }
    
    protected void onRestoreInstanceState(final Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        final SavedState mSavedState = (SavedState)parcelable;
        super.onRestoreInstanceState(mSavedState.getSuperState());
        this.mSavedState = mSavedState;
        this.requestLayout();
    }
    
    protected Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.scrollPosition = this.getScrollY();
        return (Parcelable)savedState;
    }
    
    protected void onScrollChanged(final int n, final int n2, final int n3, final int n4) {
        super.onScrollChanged(n, n2, n3, n4);
        final OnScrollChangeListener mOnScrollChangeListener = this.mOnScrollChangeListener;
        if (mOnScrollChangeListener != null) {
            mOnScrollChangeListener.onScrollChange(this, n, n2, n3, n4);
        }
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        final View focus = this.findFocus();
        if (focus != null && this != focus) {
            if (this.isWithinDeltaOfScreen(focus, 0, n4)) {
                focus.getDrawingRect(this.mTempRect);
                this.offsetDescendantRectToMyCoords(focus, this.mTempRect);
                this.doScrollY(this.computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
            }
        }
    }
    
    public boolean onStartNestedScroll(final View view, final View view2, final int n) {
        return this.onStartNestedScroll(view, view2, n, 0);
    }
    
    public boolean onStartNestedScroll(final View view, final View view2, final int n, final int n2) {
        return (n & 0x2) != 0x0;
    }
    
    public void onStopNestedScroll(final View view) {
        this.onStopNestedScroll(view, 0);
    }
    
    public void onStopNestedScroll(final View view, final int n) {
        this.mParentHelper.onStopNestedScroll(view, n);
        this.stopNestedScroll(n);
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        this.initVelocityTrackerIfNotExists();
        final MotionEvent obtain = MotionEvent.obtain(motionEvent);
        final int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mNestedYOffset = 0;
        }
        obtain.offsetLocation(0.0f, (float)this.mNestedYOffset);
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked != 2) {
                    if (actionMasked != 3) {
                        if (actionMasked != 5) {
                            if (actionMasked == 6) {
                                this.onSecondaryPointerUp(motionEvent);
                                this.mLastMotionY = (int)motionEvent.getY(motionEvent.findPointerIndex(this.mActivePointerId));
                            }
                        }
                        else {
                            final int actionIndex = motionEvent.getActionIndex();
                            this.mLastMotionY = (int)motionEvent.getY(actionIndex);
                            this.mActivePointerId = motionEvent.getPointerId(actionIndex);
                        }
                    }
                    else {
                        if (this.mIsBeingDragged && this.getChildCount() > 0 && this.mScroller.springBack(this.getScrollX(), this.getScrollY(), 0, 0, 0, this.getScrollRange())) {
                            ViewCompat.postInvalidateOnAnimation((View)this);
                        }
                        this.mActivePointerId = -1;
                        this.endDrag();
                    }
                }
                else {
                    final int pointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (pointerIndex == -1) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Invalid pointerId=");
                        sb.append(this.mActivePointerId);
                        sb.append(" in onTouchEvent");
                        Log.e("NestedScrollView", sb.toString());
                    }
                    else {
                        final int n = (int)motionEvent.getY(pointerIndex);
                        int a;
                        final int n2 = a = this.mLastMotionY - n;
                        if (this.dispatchNestedPreScroll(0, n2, this.mScrollConsumed, this.mScrollOffset, 0)) {
                            a = n2 - this.mScrollConsumed[1];
                            obtain.offsetLocation(0.0f, (float)this.mScrollOffset[1]);
                            this.mNestedYOffset += this.mScrollOffset[1];
                        }
                        int n3 = a;
                        if (!this.mIsBeingDragged) {
                            n3 = a;
                            if (Math.abs(a) > this.mTouchSlop) {
                                final ViewParent parent = this.getParent();
                                if (parent != null) {
                                    parent.requestDisallowInterceptTouchEvent(true);
                                }
                                this.mIsBeingDragged = true;
                                if (a > 0) {
                                    n3 = a - this.mTouchSlop;
                                }
                                else {
                                    n3 = a + this.mTouchSlop;
                                }
                            }
                        }
                        if (this.mIsBeingDragged) {
                            this.mLastMotionY = n - this.mScrollOffset[1];
                            final int scrollY = this.getScrollY();
                            final int scrollRange = this.getScrollRange();
                            final int overScrollMode = this.getOverScrollMode();
                            final boolean b = overScrollMode == 0 || (overScrollMode == 1 && scrollRange > 0);
                            if (this.overScrollByCompat(0, n3, 0, this.getScrollY(), 0, scrollRange, 0, 0, true) && !this.hasNestedScrollingParent(0)) {
                                this.mVelocityTracker.clear();
                            }
                            final int n4 = this.getScrollY() - scrollY;
                            if (this.dispatchNestedScroll(0, n4, 0, n3 - n4, this.mScrollOffset, 0)) {
                                final int mLastMotionY = this.mLastMotionY;
                                final int[] mScrollOffset = this.mScrollOffset;
                                this.mLastMotionY = mLastMotionY - mScrollOffset[1];
                                obtain.offsetLocation(0.0f, (float)mScrollOffset[1]);
                                this.mNestedYOffset += this.mScrollOffset[1];
                            }
                            else if (b) {
                                this.ensureGlows();
                                final int n5 = scrollY + n3;
                                if (n5 < 0) {
                                    EdgeEffectCompat.onPull(this.mEdgeGlowTop, n3 / (float)this.getHeight(), motionEvent.getX(pointerIndex) / this.getWidth());
                                    if (!this.mEdgeGlowBottom.isFinished()) {
                                        this.mEdgeGlowBottom.onRelease();
                                    }
                                }
                                else if (n5 > scrollRange) {
                                    EdgeEffectCompat.onPull(this.mEdgeGlowBottom, n3 / (float)this.getHeight(), 1.0f - motionEvent.getX(pointerIndex) / this.getWidth());
                                    if (!this.mEdgeGlowTop.isFinished()) {
                                        this.mEdgeGlowTop.onRelease();
                                    }
                                }
                                final EdgeEffect mEdgeGlowTop = this.mEdgeGlowTop;
                                if (mEdgeGlowTop != null && (!mEdgeGlowTop.isFinished() || !this.mEdgeGlowBottom.isFinished())) {
                                    ViewCompat.postInvalidateOnAnimation((View)this);
                                }
                            }
                        }
                    }
                }
            }
            else {
                final VelocityTracker mVelocityTracker = this.mVelocityTracker;
                mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
                final int a2 = (int)mVelocityTracker.getYVelocity(this.mActivePointerId);
                if (Math.abs(a2) > this.mMinimumVelocity) {
                    this.flingWithNestedDispatch(-a2);
                }
                else if (this.mScroller.springBack(this.getScrollX(), this.getScrollY(), 0, 0, 0, this.getScrollRange())) {
                    ViewCompat.postInvalidateOnAnimation((View)this);
                }
                this.mActivePointerId = -1;
                this.endDrag();
            }
        }
        else {
            if (this.getChildCount() == 0) {
                return false;
            }
            final boolean mIsBeingDragged = this.mScroller.isFinished() ^ true;
            this.mIsBeingDragged = mIsBeingDragged;
            if (mIsBeingDragged) {
                final ViewParent parent2 = this.getParent();
                if (parent2 != null) {
                    parent2.requestDisallowInterceptTouchEvent(true);
                }
            }
            if (!this.mScroller.isFinished()) {
                this.mScroller.abortAnimation();
            }
            this.mLastMotionY = (int)motionEvent.getY();
            this.mActivePointerId = motionEvent.getPointerId(0);
            this.startNestedScroll(2, 0);
        }
        final VelocityTracker mVelocityTracker2 = this.mVelocityTracker;
        if (mVelocityTracker2 != null) {
            mVelocityTracker2.addMovement(obtain);
        }
        obtain.recycle();
        return true;
    }
    
    boolean overScrollByCompat(int n, int n2, int n3, int n4, int n5, final int n6, int n7, final int n8, final boolean b) {
        final int overScrollMode = this.getOverScrollMode();
        final boolean b2 = this.computeHorizontalScrollRange() > this.computeHorizontalScrollExtent();
        final boolean b3 = this.computeVerticalScrollRange() > this.computeVerticalScrollExtent();
        final boolean b4 = overScrollMode == 0 || (overScrollMode == 1 && b2);
        final boolean b5 = overScrollMode == 0 || (overScrollMode == 1 && b3);
        n3 += n;
        if (!b4) {
            n = 0;
        }
        else {
            n = n7;
        }
        n4 += n2;
        if (!b5) {
            n2 = 0;
        }
        else {
            n2 = n8;
        }
        n7 = -n;
        n += n5;
        n5 = -n2;
        n2 += n6;
        boolean b6;
        if (n3 > n) {
            b6 = true;
        }
        else if (n3 < n7) {
            n = n7;
            b6 = true;
        }
        else {
            b6 = false;
            n = n3;
        }
        boolean b7;
        if (n4 > n2) {
            b7 = true;
        }
        else if (n4 < n5) {
            n2 = n5;
            b7 = true;
        }
        else {
            b7 = false;
            n2 = n4;
        }
        if (b7 && !this.hasNestedScrollingParent(1)) {
            this.mScroller.springBack(n, n2, 0, 0, 0, this.getScrollRange());
        }
        this.onOverScrolled(n, n2, b6, b7);
        return b6 || b7;
    }
    
    public boolean pageScroll(final int n) {
        final boolean b = n == 130;
        final int height = this.getHeight();
        if (b) {
            this.mTempRect.top = this.getScrollY() + height;
            final int childCount = this.getChildCount();
            if (childCount > 0) {
                final View child = this.getChildAt(childCount - 1);
                final int n2 = child.getBottom() + ((FrameLayout$LayoutParams)child.getLayoutParams()).bottomMargin + this.getPaddingBottom();
                if (this.mTempRect.top + height > n2) {
                    this.mTempRect.top = n2 - height;
                }
            }
        }
        else {
            this.mTempRect.top = this.getScrollY() - height;
            if (this.mTempRect.top < 0) {
                this.mTempRect.top = 0;
            }
        }
        final Rect mTempRect = this.mTempRect;
        mTempRect.bottom = mTempRect.top + height;
        return this.scrollAndFocus(n, this.mTempRect.top, this.mTempRect.bottom);
    }
    
    public void requestChildFocus(final View view, final View mChildToScrollTo) {
        if (!this.mIsLayoutDirty) {
            this.scrollToChild(mChildToScrollTo);
        }
        else {
            this.mChildToScrollTo = mChildToScrollTo;
        }
        super.requestChildFocus(view, mChildToScrollTo);
    }
    
    public boolean requestChildRectangleOnScreen(final View view, final Rect rect, final boolean b) {
        rect.offset(view.getLeft() - view.getScrollX(), view.getTop() - view.getScrollY());
        return this.scrollToChildRect(rect, b);
    }
    
    public void requestDisallowInterceptTouchEvent(final boolean b) {
        if (b) {
            this.recycleVelocityTracker();
        }
        super.requestDisallowInterceptTouchEvent(b);
    }
    
    public void requestLayout() {
        this.mIsLayoutDirty = true;
        super.requestLayout();
    }
    
    public void scrollTo(int clamp, int clamp2) {
        if (this.getChildCount() > 0) {
            final View child = this.getChildAt(0);
            final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)child.getLayoutParams();
            final int width = this.getWidth();
            final int paddingLeft = this.getPaddingLeft();
            final int paddingRight = this.getPaddingRight();
            final int width2 = child.getWidth();
            final int leftMargin = frameLayout$LayoutParams.leftMargin;
            final int rightMargin = frameLayout$LayoutParams.rightMargin;
            final int height = this.getHeight();
            final int paddingTop = this.getPaddingTop();
            final int paddingBottom = this.getPaddingBottom();
            final int height2 = child.getHeight();
            final int topMargin = frameLayout$LayoutParams.topMargin;
            final int bottomMargin = frameLayout$LayoutParams.bottomMargin;
            clamp = clamp(clamp, width - paddingLeft - paddingRight, width2 + leftMargin + rightMargin);
            clamp2 = clamp(clamp2, height - paddingTop - paddingBottom, height2 + topMargin + bottomMargin);
            if (clamp != this.getScrollX() || clamp2 != this.getScrollY()) {
                super.scrollTo(clamp, clamp2);
            }
        }
    }
    
    public void setFillViewport(final boolean mFillViewport) {
        if (mFillViewport != this.mFillViewport) {
            this.mFillViewport = mFillViewport;
            this.requestLayout();
        }
    }
    
    public void setNestedScrollingEnabled(final boolean nestedScrollingEnabled) {
        this.mChildHelper.setNestedScrollingEnabled(nestedScrollingEnabled);
    }
    
    public void setOnScrollChangeListener(final OnScrollChangeListener mOnScrollChangeListener) {
        this.mOnScrollChangeListener = mOnScrollChangeListener;
    }
    
    public void setSmoothScrollingEnabled(final boolean mSmoothScrollingEnabled) {
        this.mSmoothScrollingEnabled = mSmoothScrollingEnabled;
    }
    
    public boolean shouldDelayChildPressedState() {
        return true;
    }
    
    public final void smoothScrollBy(int scrollY, int max) {
        if (this.getChildCount() == 0) {
            return;
        }
        if (AnimationUtils.currentAnimationTimeMillis() - this.mLastScroll > 250L) {
            final View child = this.getChildAt(0);
            final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)child.getLayoutParams();
            final int height = child.getHeight();
            final int topMargin = frameLayout$LayoutParams.topMargin;
            final int bottomMargin = frameLayout$LayoutParams.bottomMargin;
            final int height2 = this.getHeight();
            final int paddingTop = this.getPaddingTop();
            final int paddingBottom = this.getPaddingBottom();
            scrollY = this.getScrollY();
            max = Math.max(0, Math.min(scrollY + max, Math.max(0, height + topMargin + bottomMargin - (height2 - paddingTop - paddingBottom))));
            this.mLastScrollerY = this.getScrollY();
            this.mScroller.startScroll(this.getScrollX(), scrollY, 0, max - scrollY);
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
        else {
            if (!this.mScroller.isFinished()) {
                this.mScroller.abortAnimation();
            }
            this.scrollBy(scrollY, max);
        }
        this.mLastScroll = AnimationUtils.currentAnimationTimeMillis();
    }
    
    public final void smoothScrollTo(final int n, final int n2) {
        this.smoothScrollBy(n - this.getScrollX(), n2 - this.getScrollY());
    }
    
    public boolean startNestedScroll(final int n) {
        return this.startNestedScroll(n, 0);
    }
    
    public boolean startNestedScroll(final int n, final int n2) {
        return this.mChildHelper.startNestedScroll(n, n2);
    }
    
    public void stopNestedScroll() {
        this.stopNestedScroll(0);
    }
    
    public void stopNestedScroll(final int n) {
        this.mChildHelper.stopNestedScroll(n);
    }
    
    static class AccessibilityDelegate extends AccessibilityDelegateCompat
    {
        @Override
        public void onInitializeAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(view, accessibilityEvent);
            final NestedScrollView nestedScrollView = (NestedScrollView)view;
            accessibilityEvent.setClassName((CharSequence)ScrollView.class.getName());
            accessibilityEvent.setScrollable(nestedScrollView.getScrollRange() > 0);
            accessibilityEvent.setScrollX(nestedScrollView.getScrollX());
            accessibilityEvent.setScrollY(nestedScrollView.getScrollY());
            AccessibilityRecordCompat.setMaxScrollX((AccessibilityRecord)accessibilityEvent, nestedScrollView.getScrollX());
            AccessibilityRecordCompat.setMaxScrollY((AccessibilityRecord)accessibilityEvent, nestedScrollView.getScrollRange());
        }
        
        @Override
        public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            final NestedScrollView nestedScrollView = (NestedScrollView)view;
            accessibilityNodeInfoCompat.setClassName(ScrollView.class.getName());
            if (nestedScrollView.isEnabled()) {
                final int scrollRange = nestedScrollView.getScrollRange();
                if (scrollRange > 0) {
                    accessibilityNodeInfoCompat.setScrollable(true);
                    if (nestedScrollView.getScrollY() > 0) {
                        accessibilityNodeInfoCompat.addAction(8192);
                    }
                    if (nestedScrollView.getScrollY() < scrollRange) {
                        accessibilityNodeInfoCompat.addAction(4096);
                    }
                }
            }
        }
        
        @Override
        public boolean performAccessibilityAction(final View view, int n, final Bundle bundle) {
            if (super.performAccessibilityAction(view, n, bundle)) {
                return true;
            }
            final NestedScrollView nestedScrollView = (NestedScrollView)view;
            if (!nestedScrollView.isEnabled()) {
                return false;
            }
            if (n != 4096) {
                if (n != 8192) {
                    return false;
                }
                final int height = nestedScrollView.getHeight();
                n = nestedScrollView.getPaddingBottom();
                n = Math.max(nestedScrollView.getScrollY() - (height - n - nestedScrollView.getPaddingTop()), 0);
                if (n != nestedScrollView.getScrollY()) {
                    nestedScrollView.smoothScrollTo(0, n);
                    return true;
                }
                return false;
            }
            else {
                n = nestedScrollView.getHeight();
                n = Math.min(nestedScrollView.getScrollY() + (n - nestedScrollView.getPaddingBottom() - nestedScrollView.getPaddingTop()), nestedScrollView.getScrollRange());
                if (n != nestedScrollView.getScrollY()) {
                    nestedScrollView.smoothScrollTo(0, n);
                    return true;
                }
                return false;
            }
        }
    }
    
    public interface OnScrollChangeListener
    {
        void onScrollChange(final NestedScrollView p0, final int p1, final int p2, final int p3, final int p4);
    }
    
    static class SavedState extends View$BaseSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        public int scrollPosition;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel);
                }
                
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }
        
        SavedState(final Parcel parcel) {
            super(parcel);
            this.scrollPosition = parcel.readInt();
        }
        
        SavedState(final Parcelable parcelable) {
            super(parcelable);
        }
        
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("HorizontalScrollView.SavedState{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(" scrollPosition=");
            sb.append(this.scrollPosition);
            sb.append("}");
            return sb.toString();
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.scrollPosition);
        }
    }
}
