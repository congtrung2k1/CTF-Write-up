// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.widget;

import android.os.Parcel;
import android.os.Parcelable$ClassLoaderCreator;
import android.os.Parcelable$Creator;
import androidx.customview.view.AbsSavedState;
import androidx.appcompat.view.menu.SubMenuBuilder;
import androidx.appcompat.view.menu.MenuView;
import android.view.ViewParent;
import androidx.appcompat.view.CollapsibleActionView;
import android.text.TextUtils$TruncateAt;
import android.view.ContextThemeWrapper;
import androidx.appcompat.content.res.AppCompatResources;
import android.os.Build$VERSION;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.text.Layout;
import android.view.Menu;
import androidx.appcompat.app.ActionBar;
import android.view.View$OnClickListener;
import androidx.appcompat.view.menu.MenuItemImpl;
import android.view.View$MeasureSpec;
import androidx.appcompat.view.SupportMenuInflater;
import android.view.MenuInflater;
import androidx.core.view.MarginLayoutParamsCompat;
import android.view.ViewGroup$MarginLayoutParams;
import android.view.ViewGroup$LayoutParams;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import java.util.List;
import android.text.TextUtils;
import android.view.MenuItem;
import androidx.appcompat.R;
import android.util.AttributeSet;
import android.widget.TextView;
import android.content.Context;
import androidx.appcompat.view.menu.MenuBuilder;
import android.widget.ImageView;
import java.util.ArrayList;
import android.view.View;
import android.graphics.drawable.Drawable;
import android.widget.ImageButton;
import androidx.appcompat.view.menu.MenuPresenter;
import android.view.ViewGroup;

public class Toolbar extends ViewGroup
{
    private static final String TAG = "Toolbar";
    private MenuPresenter.Callback mActionMenuPresenterCallback;
    int mButtonGravity;
    ImageButton mCollapseButtonView;
    private CharSequence mCollapseDescription;
    private Drawable mCollapseIcon;
    private boolean mCollapsible;
    private int mContentInsetEndWithActions;
    private int mContentInsetStartWithNavigation;
    private RtlSpacingHelper mContentInsets;
    private boolean mEatingHover;
    private boolean mEatingTouch;
    View mExpandedActionView;
    private ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
    private int mGravity;
    private final ArrayList<View> mHiddenViews;
    private ImageView mLogoView;
    private int mMaxButtonHeight;
    private MenuBuilder.Callback mMenuBuilderCallback;
    private ActionMenuView mMenuView;
    private final ActionMenuView.OnMenuItemClickListener mMenuViewItemClickListener;
    private ImageButton mNavButtonView;
    OnMenuItemClickListener mOnMenuItemClickListener;
    private ActionMenuPresenter mOuterActionMenuPresenter;
    private Context mPopupContext;
    private int mPopupTheme;
    private final Runnable mShowOverflowMenuRunnable;
    private CharSequence mSubtitleText;
    private int mSubtitleTextAppearance;
    private int mSubtitleTextColor;
    private TextView mSubtitleTextView;
    private final int[] mTempMargins;
    private final ArrayList<View> mTempViews;
    private int mTitleMarginBottom;
    private int mTitleMarginEnd;
    private int mTitleMarginStart;
    private int mTitleMarginTop;
    private CharSequence mTitleText;
    private int mTitleTextAppearance;
    private int mTitleTextColor;
    private TextView mTitleTextView;
    private ToolbarWidgetWrapper mWrapper;
    
    public Toolbar(final Context context) {
        this(context, null);
    }
    
    public Toolbar(final Context context, final AttributeSet set) {
        this(context, set, R.attr.toolbarStyle);
    }
    
    public Toolbar(final Context context, final AttributeSet set, int n) {
        super(context, set, n);
        this.mGravity = 8388627;
        this.mTempViews = new ArrayList<View>();
        this.mHiddenViews = new ArrayList<View>();
        this.mTempMargins = new int[2];
        this.mMenuViewItemClickListener = new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem menuItem) {
                return Toolbar.this.mOnMenuItemClickListener != null && Toolbar.this.mOnMenuItemClickListener.onMenuItemClick(menuItem);
            }
        };
        this.mShowOverflowMenuRunnable = new Runnable() {
            @Override
            public void run() {
                Toolbar.this.showOverflowMenu();
            }
        };
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(this.getContext(), set, R.styleable.Toolbar, n, 0);
        this.mTitleTextAppearance = obtainStyledAttributes.getResourceId(R.styleable.Toolbar_titleTextAppearance, 0);
        this.mSubtitleTextAppearance = obtainStyledAttributes.getResourceId(R.styleable.Toolbar_subtitleTextAppearance, 0);
        this.mGravity = obtainStyledAttributes.getInteger(R.styleable.Toolbar_android_gravity, this.mGravity);
        this.mButtonGravity = obtainStyledAttributes.getInteger(R.styleable.Toolbar_buttonGravity, 48);
        final int n2 = n = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_titleMargin, 0);
        if (obtainStyledAttributes.hasValue(R.styleable.Toolbar_titleMargins)) {
            n = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_titleMargins, n2);
        }
        this.mTitleMarginBottom = n;
        this.mTitleMarginTop = n;
        this.mTitleMarginEnd = n;
        this.mTitleMarginStart = n;
        n = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginStart, -1);
        if (n >= 0) {
            this.mTitleMarginStart = n;
        }
        n = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginEnd, -1);
        if (n >= 0) {
            this.mTitleMarginEnd = n;
        }
        n = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginTop, -1);
        if (n >= 0) {
            this.mTitleMarginTop = n;
        }
        n = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginBottom, -1);
        if (n >= 0) {
            this.mTitleMarginBottom = n;
        }
        this.mMaxButtonHeight = obtainStyledAttributes.getDimensionPixelSize(R.styleable.Toolbar_maxButtonHeight, -1);
        final int dimensionPixelOffset = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetStart, Integer.MIN_VALUE);
        final int dimensionPixelOffset2 = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetEnd, Integer.MIN_VALUE);
        n = obtainStyledAttributes.getDimensionPixelSize(R.styleable.Toolbar_contentInsetLeft, 0);
        final int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.Toolbar_contentInsetRight, 0);
        this.ensureContentInsets();
        this.mContentInsets.setAbsolute(n, dimensionPixelSize);
        if (dimensionPixelOffset != Integer.MIN_VALUE || dimensionPixelOffset2 != Integer.MIN_VALUE) {
            this.mContentInsets.setRelative(dimensionPixelOffset, dimensionPixelOffset2);
        }
        this.mContentInsetStartWithNavigation = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetStartWithNavigation, Integer.MIN_VALUE);
        this.mContentInsetEndWithActions = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetEndWithActions, Integer.MIN_VALUE);
        this.mCollapseIcon = obtainStyledAttributes.getDrawable(R.styleable.Toolbar_collapseIcon);
        this.mCollapseDescription = obtainStyledAttributes.getText(R.styleable.Toolbar_collapseContentDescription);
        final CharSequence text = obtainStyledAttributes.getText(R.styleable.Toolbar_title);
        if (!TextUtils.isEmpty(text)) {
            this.setTitle(text);
        }
        final CharSequence text2 = obtainStyledAttributes.getText(R.styleable.Toolbar_subtitle);
        if (!TextUtils.isEmpty(text2)) {
            this.setSubtitle(text2);
        }
        this.mPopupContext = this.getContext();
        this.setPopupTheme(obtainStyledAttributes.getResourceId(R.styleable.Toolbar_popupTheme, 0));
        final Drawable drawable = obtainStyledAttributes.getDrawable(R.styleable.Toolbar_navigationIcon);
        if (drawable != null) {
            this.setNavigationIcon(drawable);
        }
        final CharSequence text3 = obtainStyledAttributes.getText(R.styleable.Toolbar_navigationContentDescription);
        if (!TextUtils.isEmpty(text3)) {
            this.setNavigationContentDescription(text3);
        }
        final Drawable drawable2 = obtainStyledAttributes.getDrawable(R.styleable.Toolbar_logo);
        if (drawable2 != null) {
            this.setLogo(drawable2);
        }
        final CharSequence text4 = obtainStyledAttributes.getText(R.styleable.Toolbar_logoDescription);
        if (!TextUtils.isEmpty(text4)) {
            this.setLogoDescription(text4);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.Toolbar_titleTextColor)) {
            this.setTitleTextColor(obtainStyledAttributes.getColor(R.styleable.Toolbar_titleTextColor, -1));
        }
        if (obtainStyledAttributes.hasValue(R.styleable.Toolbar_subtitleTextColor)) {
            this.setSubtitleTextColor(obtainStyledAttributes.getColor(R.styleable.Toolbar_subtitleTextColor, -1));
        }
        obtainStyledAttributes.recycle();
    }
    
    private void addCustomViewsWithGravity(final List<View> list, int i) {
        final int layoutDirection = ViewCompat.getLayoutDirection((View)this);
        boolean b = true;
        if (layoutDirection != 1) {
            b = false;
        }
        final int childCount = this.getChildCount();
        final int absoluteGravity = GravityCompat.getAbsoluteGravity(i, ViewCompat.getLayoutDirection((View)this));
        list.clear();
        if (b) {
            View child;
            LayoutParams layoutParams;
            for (i = childCount - 1; i >= 0; --i) {
                child = this.getChildAt(i);
                layoutParams = (LayoutParams)child.getLayoutParams();
                if (layoutParams.mViewType == 0 && this.shouldLayout(child) && this.getChildHorizontalGravity(layoutParams.gravity) == absoluteGravity) {
                    list.add(child);
                }
            }
        }
        else {
            View child2;
            LayoutParams layoutParams2;
            for (i = 0; i < childCount; ++i) {
                child2 = this.getChildAt(i);
                layoutParams2 = (LayoutParams)child2.getLayoutParams();
                if (layoutParams2.mViewType == 0 && this.shouldLayout(child2) && this.getChildHorizontalGravity(layoutParams2.gravity) == absoluteGravity) {
                    list.add(child2);
                }
            }
        }
    }
    
    private void addSystemView(final View e, final boolean b) {
        final ViewGroup$LayoutParams layoutParams = e.getLayoutParams();
        LayoutParams layoutParams2;
        if (layoutParams == null) {
            layoutParams2 = this.generateDefaultLayoutParams();
        }
        else if (!this.checkLayoutParams(layoutParams)) {
            layoutParams2 = this.generateLayoutParams(layoutParams);
        }
        else {
            layoutParams2 = (LayoutParams)layoutParams;
        }
        layoutParams2.mViewType = 1;
        if (b && this.mExpandedActionView != null) {
            e.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
            this.mHiddenViews.add(e);
        }
        else {
            this.addView(e, (ViewGroup$LayoutParams)layoutParams2);
        }
    }
    
    private void ensureContentInsets() {
        if (this.mContentInsets == null) {
            this.mContentInsets = new RtlSpacingHelper();
        }
    }
    
    private void ensureLogoView() {
        if (this.mLogoView == null) {
            this.mLogoView = new AppCompatImageView(this.getContext());
        }
    }
    
    private void ensureMenu() {
        this.ensureMenuView();
        if (this.mMenuView.peekMenu() == null) {
            final MenuBuilder menuBuilder = (MenuBuilder)this.mMenuView.getMenu();
            if (this.mExpandedMenuPresenter == null) {
                this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter();
            }
            this.mMenuView.setExpandedActionViewsExclusive(true);
            menuBuilder.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
        }
    }
    
    private void ensureMenuView() {
        if (this.mMenuView == null) {
            (this.mMenuView = new ActionMenuView(this.getContext())).setPopupTheme(this.mPopupTheme);
            this.mMenuView.setOnMenuItemClickListener(this.mMenuViewItemClickListener);
            this.mMenuView.setMenuCallbacks(this.mActionMenuPresenterCallback, this.mMenuBuilderCallback);
            final LayoutParams generateDefaultLayoutParams = this.generateDefaultLayoutParams();
            generateDefaultLayoutParams.gravity = (0x800005 | (this.mButtonGravity & 0x70));
            this.mMenuView.setLayoutParams((ViewGroup$LayoutParams)generateDefaultLayoutParams);
            this.addSystemView((View)this.mMenuView, false);
        }
    }
    
    private void ensureNavButtonView() {
        if (this.mNavButtonView == null) {
            this.mNavButtonView = new AppCompatImageButton(this.getContext(), null, R.attr.toolbarNavigationButtonStyle);
            final LayoutParams generateDefaultLayoutParams = this.generateDefaultLayoutParams();
            generateDefaultLayoutParams.gravity = (0x800003 | (this.mButtonGravity & 0x70));
            this.mNavButtonView.setLayoutParams((ViewGroup$LayoutParams)generateDefaultLayoutParams);
        }
    }
    
    private int getChildHorizontalGravity(int n) {
        final int layoutDirection = ViewCompat.getLayoutDirection((View)this);
        final int n2 = GravityCompat.getAbsoluteGravity(n, layoutDirection) & 0x7;
        if (n2 != 1) {
            n = 3;
            if (n2 != 3 && n2 != 5) {
                if (layoutDirection == 1) {
                    n = 5;
                }
                return n;
            }
        }
        return n2;
    }
    
    private int getChildTop(final View view, int n) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        final int measuredHeight = view.getMeasuredHeight();
        if (n > 0) {
            n = (measuredHeight - n) / 2;
        }
        else {
            n = 0;
        }
        final int childVerticalGravity = this.getChildVerticalGravity(layoutParams.gravity);
        if (childVerticalGravity == 48) {
            return this.getPaddingTop() - n;
        }
        if (childVerticalGravity != 80) {
            final int paddingTop = this.getPaddingTop();
            final int paddingBottom = this.getPaddingBottom();
            n = this.getHeight();
            final int n2 = (n - paddingTop - paddingBottom - measuredHeight) / 2;
            if (n2 < layoutParams.topMargin) {
                n = layoutParams.topMargin;
            }
            else {
                final int n3 = n - paddingBottom - measuredHeight - n2 - paddingTop;
                n = n2;
                if (n3 < layoutParams.bottomMargin) {
                    n = Math.max(0, n2 - (layoutParams.bottomMargin - n3));
                }
            }
            return paddingTop + n;
        }
        return this.getHeight() - this.getPaddingBottom() - measuredHeight - layoutParams.bottomMargin - n;
    }
    
    private int getChildVerticalGravity(int n) {
        n &= 0x70;
        if (n != 16 && n != 48 && n != 80) {
            return this.mGravity & 0x70;
        }
        return n;
    }
    
    private int getHorizontalMargins(final View view) {
        final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams = (ViewGroup$MarginLayoutParams)view.getLayoutParams();
        return MarginLayoutParamsCompat.getMarginStart(viewGroup$MarginLayoutParams) + MarginLayoutParamsCompat.getMarginEnd(viewGroup$MarginLayoutParams);
    }
    
    private MenuInflater getMenuInflater() {
        return new SupportMenuInflater(this.getContext());
    }
    
    private int getVerticalMargins(final View view) {
        final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams = (ViewGroup$MarginLayoutParams)view.getLayoutParams();
        return viewGroup$MarginLayoutParams.topMargin + viewGroup$MarginLayoutParams.bottomMargin;
    }
    
    private int getViewListMeasuredWidth(final List<View> list, final int[] array) {
        int max = array[0];
        int max2 = array[1];
        int n = 0;
        for (int size = list.size(), i = 0; i < size; ++i) {
            final View view = list.get(i);
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            final int b = layoutParams.leftMargin - max;
            final int b2 = layoutParams.rightMargin - max2;
            final int max3 = Math.max(0, b);
            final int max4 = Math.max(0, b2);
            max = Math.max(0, -b);
            max2 = Math.max(0, -b2);
            n += view.getMeasuredWidth() + max3 + max4;
        }
        return n;
    }
    
    private boolean isChildOrHidden(final View o) {
        return o.getParent() == this || this.mHiddenViews.contains(o);
    }
    
    private static boolean isCustomView(final View view) {
        return ((LayoutParams)view.getLayoutParams()).mViewType == 0;
    }
    
    private int layoutChildLeft(final View view, int n, final int[] array, int measuredWidth) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        final int b = layoutParams.leftMargin - array[0];
        n += Math.max(0, b);
        array[0] = Math.max(0, -b);
        final int childTop = this.getChildTop(view, measuredWidth);
        measuredWidth = view.getMeasuredWidth();
        view.layout(n, childTop, n + measuredWidth, view.getMeasuredHeight() + childTop);
        return n + (layoutParams.rightMargin + measuredWidth);
    }
    
    private int layoutChildRight(final View view, int n, final int[] array, int childTop) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        final int b = layoutParams.rightMargin - array[1];
        n -= Math.max(0, b);
        array[1] = Math.max(0, -b);
        childTop = this.getChildTop(view, childTop);
        final int measuredWidth = view.getMeasuredWidth();
        view.layout(n - measuredWidth, childTop, n, view.getMeasuredHeight() + childTop);
        return n - (layoutParams.leftMargin + measuredWidth);
    }
    
    private int measureChildCollapseMargins(final View view, int childMeasureSpec, final int n, final int n2, final int n3, final int[] array) {
        final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams = (ViewGroup$MarginLayoutParams)view.getLayoutParams();
        final int b = viewGroup$MarginLayoutParams.leftMargin - array[0];
        final int b2 = viewGroup$MarginLayoutParams.rightMargin - array[1];
        final int n4 = Math.max(0, b) + Math.max(0, b2);
        array[0] = Math.max(0, -b);
        array[1] = Math.max(0, -b2);
        childMeasureSpec = getChildMeasureSpec(childMeasureSpec, this.getPaddingLeft() + this.getPaddingRight() + n4 + n, viewGroup$MarginLayoutParams.width);
        view.measure(childMeasureSpec, getChildMeasureSpec(n2, this.getPaddingTop() + this.getPaddingBottom() + viewGroup$MarginLayoutParams.topMargin + viewGroup$MarginLayoutParams.bottomMargin + n3, viewGroup$MarginLayoutParams.height));
        return view.getMeasuredWidth() + n4;
    }
    
    private void measureChildConstrained(final View view, int n, int childMeasureSpec, int mode, final int n2, final int b) {
        final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams = (ViewGroup$MarginLayoutParams)view.getLayoutParams();
        final int childMeasureSpec2 = getChildMeasureSpec(n, this.getPaddingLeft() + this.getPaddingRight() + viewGroup$MarginLayoutParams.leftMargin + viewGroup$MarginLayoutParams.rightMargin + childMeasureSpec, viewGroup$MarginLayoutParams.width);
        childMeasureSpec = getChildMeasureSpec(mode, this.getPaddingTop() + this.getPaddingBottom() + viewGroup$MarginLayoutParams.topMargin + viewGroup$MarginLayoutParams.bottomMargin + n2, viewGroup$MarginLayoutParams.height);
        mode = View$MeasureSpec.getMode(childMeasureSpec);
        n = childMeasureSpec;
        if (mode != 1073741824) {
            n = childMeasureSpec;
            if (b >= 0) {
                if (mode != 0) {
                    n = Math.min(View$MeasureSpec.getSize(childMeasureSpec), b);
                }
                else {
                    n = b;
                }
                n = View$MeasureSpec.makeMeasureSpec(n, 1073741824);
            }
        }
        view.measure(childMeasureSpec2, n);
    }
    
    private void postShowOverflowMenu() {
        this.removeCallbacks(this.mShowOverflowMenuRunnable);
        this.post(this.mShowOverflowMenuRunnable);
    }
    
    private boolean shouldCollapse() {
        if (!this.mCollapsible) {
            return false;
        }
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (this.shouldLayout(child) && child.getMeasuredWidth() > 0 && child.getMeasuredHeight() > 0) {
                return false;
            }
        }
        return true;
    }
    
    private boolean shouldLayout(final View view) {
        return view != null && view.getParent() == this && view.getVisibility() != 8;
    }
    
    void addChildrenForExpandedActionView() {
        for (int i = this.mHiddenViews.size() - 1; i >= 0; --i) {
            this.addView((View)this.mHiddenViews.get(i));
        }
        this.mHiddenViews.clear();
    }
    
    public boolean canShowOverflowMenu() {
        if (this.getVisibility() == 0) {
            final ActionMenuView mMenuView = this.mMenuView;
            if (mMenuView != null && mMenuView.isOverflowReserved()) {
                return true;
            }
        }
        return false;
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return super.checkLayoutParams(viewGroup$LayoutParams) && viewGroup$LayoutParams instanceof LayoutParams;
    }
    
    public void collapseActionView() {
        final ExpandedActionViewMenuPresenter mExpandedMenuPresenter = this.mExpandedMenuPresenter;
        MenuItemImpl mCurrentExpandedItem;
        if (mExpandedMenuPresenter == null) {
            mCurrentExpandedItem = null;
        }
        else {
            mCurrentExpandedItem = mExpandedMenuPresenter.mCurrentExpandedItem;
        }
        if (mCurrentExpandedItem != null) {
            mCurrentExpandedItem.collapseActionView();
        }
    }
    
    public void dismissPopupMenus() {
        final ActionMenuView mMenuView = this.mMenuView;
        if (mMenuView != null) {
            mMenuView.dismissPopupMenus();
        }
    }
    
    void ensureCollapseButtonView() {
        if (this.mCollapseButtonView == null) {
            (this.mCollapseButtonView = new AppCompatImageButton(this.getContext(), null, R.attr.toolbarNavigationButtonStyle)).setImageDrawable(this.mCollapseIcon);
            this.mCollapseButtonView.setContentDescription(this.mCollapseDescription);
            final LayoutParams generateDefaultLayoutParams = this.generateDefaultLayoutParams();
            generateDefaultLayoutParams.gravity = (0x800003 | (this.mButtonGravity & 0x70));
            generateDefaultLayoutParams.mViewType = 2;
            this.mCollapseButtonView.setLayoutParams((ViewGroup$LayoutParams)generateDefaultLayoutParams);
            this.mCollapseButtonView.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    Toolbar.this.collapseActionView();
                }
            });
        }
    }
    
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }
    
    public LayoutParams generateLayoutParams(final AttributeSet set) {
        return new LayoutParams(this.getContext(), set);
    }
    
    protected LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        if (viewGroup$LayoutParams instanceof LayoutParams) {
            return new LayoutParams((LayoutParams)viewGroup$LayoutParams);
        }
        if (viewGroup$LayoutParams instanceof ActionBar.LayoutParams) {
            return new LayoutParams((ActionBar.LayoutParams)viewGroup$LayoutParams);
        }
        if (viewGroup$LayoutParams instanceof ViewGroup$MarginLayoutParams) {
            return new LayoutParams((ViewGroup$MarginLayoutParams)viewGroup$LayoutParams);
        }
        return new LayoutParams(viewGroup$LayoutParams);
    }
    
    public int getContentInsetEnd() {
        final RtlSpacingHelper mContentInsets = this.mContentInsets;
        int end;
        if (mContentInsets != null) {
            end = mContentInsets.getEnd();
        }
        else {
            end = 0;
        }
        return end;
    }
    
    public int getContentInsetEndWithActions() {
        int n = this.mContentInsetEndWithActions;
        if (n == Integer.MIN_VALUE) {
            n = this.getContentInsetEnd();
        }
        return n;
    }
    
    public int getContentInsetLeft() {
        final RtlSpacingHelper mContentInsets = this.mContentInsets;
        int left;
        if (mContentInsets != null) {
            left = mContentInsets.getLeft();
        }
        else {
            left = 0;
        }
        return left;
    }
    
    public int getContentInsetRight() {
        final RtlSpacingHelper mContentInsets = this.mContentInsets;
        int right;
        if (mContentInsets != null) {
            right = mContentInsets.getRight();
        }
        else {
            right = 0;
        }
        return right;
    }
    
    public int getContentInsetStart() {
        final RtlSpacingHelper mContentInsets = this.mContentInsets;
        int start;
        if (mContentInsets != null) {
            start = mContentInsets.getStart();
        }
        else {
            start = 0;
        }
        return start;
    }
    
    public int getContentInsetStartWithNavigation() {
        int n = this.mContentInsetStartWithNavigation;
        if (n == Integer.MIN_VALUE) {
            n = this.getContentInsetStart();
        }
        return n;
    }
    
    public int getCurrentContentInsetEnd() {
        boolean b = false;
        final ActionMenuView mMenuView = this.mMenuView;
        if (mMenuView != null) {
            final MenuBuilder peekMenu = mMenuView.peekMenu();
            b = (peekMenu != null && peekMenu.hasVisibleItems());
        }
        int n;
        if (b) {
            n = Math.max(this.getContentInsetEnd(), Math.max(this.mContentInsetEndWithActions, 0));
        }
        else {
            n = this.getContentInsetEnd();
        }
        return n;
    }
    
    public int getCurrentContentInsetLeft() {
        int n;
        if (ViewCompat.getLayoutDirection((View)this) == 1) {
            n = this.getCurrentContentInsetEnd();
        }
        else {
            n = this.getCurrentContentInsetStart();
        }
        return n;
    }
    
    public int getCurrentContentInsetRight() {
        int n;
        if (ViewCompat.getLayoutDirection((View)this) == 1) {
            n = this.getCurrentContentInsetStart();
        }
        else {
            n = this.getCurrentContentInsetEnd();
        }
        return n;
    }
    
    public int getCurrentContentInsetStart() {
        int n;
        if (this.getNavigationIcon() != null) {
            n = Math.max(this.getContentInsetStart(), Math.max(this.mContentInsetStartWithNavigation, 0));
        }
        else {
            n = this.getContentInsetStart();
        }
        return n;
    }
    
    public Drawable getLogo() {
        final ImageView mLogoView = this.mLogoView;
        Drawable drawable;
        if (mLogoView != null) {
            drawable = mLogoView.getDrawable();
        }
        else {
            drawable = null;
        }
        return drawable;
    }
    
    public CharSequence getLogoDescription() {
        final ImageView mLogoView = this.mLogoView;
        CharSequence contentDescription;
        if (mLogoView != null) {
            contentDescription = mLogoView.getContentDescription();
        }
        else {
            contentDescription = null;
        }
        return contentDescription;
    }
    
    public Menu getMenu() {
        this.ensureMenu();
        return this.mMenuView.getMenu();
    }
    
    public CharSequence getNavigationContentDescription() {
        final ImageButton mNavButtonView = this.mNavButtonView;
        CharSequence contentDescription;
        if (mNavButtonView != null) {
            contentDescription = mNavButtonView.getContentDescription();
        }
        else {
            contentDescription = null;
        }
        return contentDescription;
    }
    
    public Drawable getNavigationIcon() {
        final ImageButton mNavButtonView = this.mNavButtonView;
        Drawable drawable;
        if (mNavButtonView != null) {
            drawable = mNavButtonView.getDrawable();
        }
        else {
            drawable = null;
        }
        return drawable;
    }
    
    ActionMenuPresenter getOuterActionMenuPresenter() {
        return this.mOuterActionMenuPresenter;
    }
    
    public Drawable getOverflowIcon() {
        this.ensureMenu();
        return this.mMenuView.getOverflowIcon();
    }
    
    Context getPopupContext() {
        return this.mPopupContext;
    }
    
    public int getPopupTheme() {
        return this.mPopupTheme;
    }
    
    public CharSequence getSubtitle() {
        return this.mSubtitleText;
    }
    
    public CharSequence getTitle() {
        return this.mTitleText;
    }
    
    public int getTitleMarginBottom() {
        return this.mTitleMarginBottom;
    }
    
    public int getTitleMarginEnd() {
        return this.mTitleMarginEnd;
    }
    
    public int getTitleMarginStart() {
        return this.mTitleMarginStart;
    }
    
    public int getTitleMarginTop() {
        return this.mTitleMarginTop;
    }
    
    public DecorToolbar getWrapper() {
        if (this.mWrapper == null) {
            this.mWrapper = new ToolbarWidgetWrapper(this, true);
        }
        return this.mWrapper;
    }
    
    public boolean hasExpandedActionView() {
        final ExpandedActionViewMenuPresenter mExpandedMenuPresenter = this.mExpandedMenuPresenter;
        return mExpandedMenuPresenter != null && mExpandedMenuPresenter.mCurrentExpandedItem != null;
    }
    
    public boolean hideOverflowMenu() {
        final ActionMenuView mMenuView = this.mMenuView;
        return mMenuView != null && mMenuView.hideOverflowMenu();
    }
    
    public void inflateMenu(final int n) {
        this.getMenuInflater().inflate(n, this.getMenu());
    }
    
    public boolean isOverflowMenuShowPending() {
        final ActionMenuView mMenuView = this.mMenuView;
        return mMenuView != null && mMenuView.isOverflowMenuShowPending();
    }
    
    public boolean isOverflowMenuShowing() {
        final ActionMenuView mMenuView = this.mMenuView;
        return mMenuView != null && mMenuView.isOverflowMenuShowing();
    }
    
    public boolean isTitleTruncated() {
        final TextView mTitleTextView = this.mTitleTextView;
        if (mTitleTextView == null) {
            return false;
        }
        final Layout layout = mTitleTextView.getLayout();
        if (layout == null) {
            return false;
        }
        for (int lineCount = layout.getLineCount(), i = 0; i < lineCount; ++i) {
            if (layout.getEllipsisCount(i) > 0) {
                return true;
            }
        }
        return false;
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.removeCallbacks(this.mShowOverflowMenuRunnable);
    }
    
    public boolean onHoverEvent(final MotionEvent motionEvent) {
        final int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 9) {
            this.mEatingHover = false;
        }
        if (!this.mEatingHover) {
            final boolean onHoverEvent = super.onHoverEvent(motionEvent);
            if (actionMasked == 9 && !onHoverEvent) {
                this.mEatingHover = true;
            }
        }
        if (actionMasked == 10 || actionMasked == 3) {
            this.mEatingHover = false;
        }
        return true;
    }
    
    protected void onLayout(final boolean b, int a, int i, int paddingLeft, int min) {
        final boolean b2 = ViewCompat.getLayoutDirection((View)this) == 1;
        final int width = this.getWidth();
        final int height = this.getHeight();
        paddingLeft = this.getPaddingLeft();
        final int paddingRight = this.getPaddingRight();
        final int paddingTop = this.getPaddingTop();
        final int paddingBottom = this.getPaddingBottom();
        final int n = width - paddingRight;
        final int[] mTempMargins = this.mTempMargins;
        mTempMargins[mTempMargins[1] = 0] = 0;
        a = ViewCompat.getMinimumHeight((View)this);
        if (a >= 0) {
            min = Math.min(a, min - i);
        }
        else {
            min = 0;
        }
        if (this.shouldLayout((View)this.mNavButtonView)) {
            if (b2) {
                i = this.layoutChildRight((View)this.mNavButtonView, n, mTempMargins, min);
                a = paddingLeft;
            }
            else {
                a = this.layoutChildLeft((View)this.mNavButtonView, paddingLeft, mTempMargins, min);
                i = n;
            }
        }
        else {
            a = paddingLeft;
            i = n;
        }
        int layoutChildLeft = a;
        int layoutChildRight = i;
        if (this.shouldLayout((View)this.mCollapseButtonView)) {
            if (b2) {
                layoutChildRight = this.layoutChildRight((View)this.mCollapseButtonView, i, mTempMargins, min);
                layoutChildLeft = a;
            }
            else {
                layoutChildLeft = this.layoutChildLeft((View)this.mCollapseButtonView, a, mTempMargins, min);
                layoutChildRight = i;
            }
        }
        i = layoutChildLeft;
        a = layoutChildRight;
        if (this.shouldLayout((View)this.mMenuView)) {
            if (b2) {
                i = this.layoutChildLeft((View)this.mMenuView, layoutChildLeft, mTempMargins, min);
                a = layoutChildRight;
            }
            else {
                a = this.layoutChildRight((View)this.mMenuView, layoutChildRight, mTempMargins, min);
                i = layoutChildLeft;
            }
        }
        final int currentContentInsetLeft = this.getCurrentContentInsetLeft();
        final int currentContentInsetRight = this.getCurrentContentInsetRight();
        mTempMargins[0] = Math.max(0, currentContentInsetLeft - i);
        mTempMargins[1] = Math.max(0, currentContentInsetRight - (n - a));
        i = Math.max(i, currentContentInsetLeft);
        final int min2 = Math.min(a, n - currentContentInsetRight);
        a = i;
        int layoutChildRight2 = min2;
        if (this.shouldLayout(this.mExpandedActionView)) {
            if (b2) {
                layoutChildRight2 = this.layoutChildRight(this.mExpandedActionView, min2, mTempMargins, min);
                a = i;
            }
            else {
                a = this.layoutChildLeft(this.mExpandedActionView, i, mTempMargins, min);
                layoutChildRight2 = min2;
            }
        }
        int layoutChildLeft2 = a;
        i = layoutChildRight2;
        if (this.shouldLayout((View)this.mLogoView)) {
            if (b2) {
                i = this.layoutChildRight((View)this.mLogoView, layoutChildRight2, mTempMargins, min);
                layoutChildLeft2 = a;
            }
            else {
                layoutChildLeft2 = this.layoutChildLeft((View)this.mLogoView, a, mTempMargins, min);
                i = layoutChildRight2;
            }
        }
        final boolean shouldLayout = this.shouldLayout((View)this.mTitleTextView);
        final boolean shouldLayout2 = this.shouldLayout((View)this.mSubtitleTextView);
        if (shouldLayout) {
            final LayoutParams layoutParams = (LayoutParams)this.mTitleTextView.getLayoutParams();
            a = layoutParams.topMargin + this.mTitleTextView.getMeasuredHeight() + layoutParams.bottomMargin + 0;
        }
        else {
            a = 0;
        }
        if (shouldLayout2) {
            final LayoutParams layoutParams2 = (LayoutParams)this.mSubtitleTextView.getLayoutParams();
            a += layoutParams2.topMargin + this.mSubtitleTextView.getMeasuredHeight() + layoutParams2.bottomMargin;
        }
        int layoutChildRight3;
        if (!shouldLayout && !shouldLayout2) {
            a = layoutChildLeft2;
            layoutChildRight3 = i;
        }
        else {
            TextView textView;
            if (shouldLayout) {
                textView = this.mTitleTextView;
            }
            else {
                textView = this.mSubtitleTextView;
            }
            TextView textView2;
            if (shouldLayout2) {
                textView2 = this.mSubtitleTextView;
            }
            else {
                textView2 = this.mTitleTextView;
            }
            final LayoutParams layoutParams3 = (LayoutParams)((View)textView).getLayoutParams();
            final LayoutParams layoutParams4 = (LayoutParams)((View)textView2).getLayoutParams();
            final boolean b3 = (shouldLayout && this.mTitleTextView.getMeasuredWidth() > 0) || (shouldLayout2 && this.mSubtitleTextView.getMeasuredWidth() > 0);
            final int n2 = this.mGravity & 0x70;
            if (n2 != 48) {
                if (n2 != 80) {
                    final int n3 = (height - paddingTop - paddingBottom - a) / 2;
                    if (n3 < layoutParams3.topMargin + this.mTitleMarginTop) {
                        a = layoutParams3.topMargin + this.mTitleMarginTop;
                    }
                    else {
                        final int n4 = height - paddingBottom - a - n3 - paddingTop;
                        a = n3;
                        if (n4 < layoutParams3.bottomMargin + this.mTitleMarginBottom) {
                            a = Math.max(0, n3 - (layoutParams4.bottomMargin + this.mTitleMarginBottom - n4));
                        }
                    }
                    a += paddingTop;
                }
                else {
                    a = height - paddingBottom - layoutParams4.bottomMargin - this.mTitleMarginBottom - a;
                }
            }
            else {
                a = this.getPaddingTop() + layoutParams3.topMargin + this.mTitleMarginTop;
            }
            if (b2) {
                int mTitleMarginStart;
                if (b3) {
                    mTitleMarginStart = this.mTitleMarginStart;
                }
                else {
                    mTitleMarginStart = 0;
                }
                final int b4 = mTitleMarginStart - mTempMargins[1];
                i -= Math.max(0, b4);
                mTempMargins[1] = Math.max(0, -b4);
                int n7;
                if (shouldLayout) {
                    final LayoutParams layoutParams5 = (LayoutParams)this.mTitleTextView.getLayoutParams();
                    final int n5 = i - this.mTitleTextView.getMeasuredWidth();
                    final int n6 = this.mTitleTextView.getMeasuredHeight() + a;
                    this.mTitleTextView.layout(n5, a, i, n6);
                    a = n5 - this.mTitleMarginEnd;
                    n7 = n6 + layoutParams5.bottomMargin;
                }
                else {
                    final int n8 = i;
                    n7 = a;
                    a = n8;
                }
                int b5;
                if (shouldLayout2) {
                    final LayoutParams layoutParams6 = (LayoutParams)this.mSubtitleTextView.getLayoutParams();
                    final int n9 = n7 + layoutParams6.topMargin;
                    this.mSubtitleTextView.layout(i - this.mSubtitleTextView.getMeasuredWidth(), n9, i, this.mSubtitleTextView.getMeasuredHeight() + n9);
                    b5 = i - this.mTitleMarginEnd;
                    final int bottomMargin = layoutParams6.bottomMargin;
                }
                else {
                    b5 = i;
                }
                if (b3) {
                    i = Math.min(a, b5);
                }
                a = layoutChildLeft2;
                layoutChildRight3 = i;
            }
            else {
                int mTitleMarginStart2;
                if (b3) {
                    mTitleMarginStart2 = this.mTitleMarginStart;
                }
                else {
                    mTitleMarginStart2 = 0;
                }
                final int b6 = mTitleMarginStart2 - mTempMargins[0];
                final int n10 = layoutChildLeft2 + Math.max(0, b6);
                mTempMargins[0] = Math.max(0, -b6);
                int a2;
                if (shouldLayout) {
                    final LayoutParams layoutParams7 = (LayoutParams)this.mTitleTextView.getLayoutParams();
                    final int n11 = this.mTitleTextView.getMeasuredWidth() + n10;
                    final int n12 = this.mTitleTextView.getMeasuredHeight() + a;
                    this.mTitleTextView.layout(n10, a, n11, n12);
                    a2 = n11 + this.mTitleMarginEnd;
                    a = n12 + layoutParams7.bottomMargin;
                }
                else {
                    a2 = n10;
                }
                int b7;
                if (shouldLayout2) {
                    final LayoutParams layoutParams8 = (LayoutParams)this.mSubtitleTextView.getLayoutParams();
                    final int n13 = a + layoutParams8.topMargin;
                    a = this.mSubtitleTextView.getMeasuredWidth() + n10;
                    this.mSubtitleTextView.layout(n10, n13, a, this.mSubtitleTextView.getMeasuredHeight() + n13);
                    b7 = a + this.mTitleMarginEnd;
                    a = layoutParams8.bottomMargin;
                }
                else {
                    b7 = n10;
                }
                a = n10;
                layoutChildRight3 = i;
                if (b3) {
                    a = Math.max(a2, b7);
                    layoutChildRight3 = i;
                }
            }
        }
        final int n14 = paddingLeft;
        paddingLeft = 0;
        this.addCustomViewsWithGravity(this.mTempViews, 3);
        int size;
        for (size = this.mTempViews.size(), i = 0; i < size; ++i) {
            a = this.layoutChildLeft(this.mTempViews.get(i), a, mTempMargins, min);
        }
        this.addCustomViewsWithGravity(this.mTempViews, 5);
        int size2;
        for (size2 = this.mTempViews.size(), i = 0; i < size2; ++i) {
            layoutChildRight3 = this.layoutChildRight(this.mTempViews.get(i), layoutChildRight3, mTempMargins, min);
        }
        this.addCustomViewsWithGravity(this.mTempViews, 1);
        final int viewListMeasuredWidth = this.getViewListMeasuredWidth(this.mTempViews, mTempMargins);
        i = n14 + (width - n14 - paddingRight) / 2 - viewListMeasuredWidth / 2;
        final int n15 = viewListMeasuredWidth + i;
        if (i >= a) {
            if (n15 > layoutChildRight3) {
                a = i - (n15 - layoutChildRight3);
            }
            else {
                a = i;
            }
        }
        int size3;
        for (size3 = this.mTempViews.size(), i = paddingLeft; i < size3; ++i) {
            a = this.layoutChildLeft(this.mTempViews.get(i), a, mTempMargins, min);
        }
        this.mTempViews.clear();
    }
    
    protected void onMeasure(int resolveSizeAndState, final int n) {
        int max = 0;
        int combineMeasuredStates = 0;
        final int[] mTempMargins = this.mTempMargins;
        int n2;
        int n3;
        if (ViewUtils.isLayoutRtl((View)this)) {
            n2 = 1;
            n3 = 0;
        }
        else {
            n2 = 0;
            n3 = 1;
        }
        int b = 0;
        if (this.shouldLayout((View)this.mNavButtonView)) {
            this.measureChildConstrained((View)this.mNavButtonView, resolveSizeAndState, 0, n, 0, this.mMaxButtonHeight);
            b = this.mNavButtonView.getMeasuredWidth() + this.getHorizontalMargins((View)this.mNavButtonView);
            max = Math.max(0, this.mNavButtonView.getMeasuredHeight() + this.getVerticalMargins((View)this.mNavButtonView));
            combineMeasuredStates = View.combineMeasuredStates(0, this.mNavButtonView.getMeasuredState());
        }
        int n4 = max;
        int n5 = combineMeasuredStates;
        if (this.shouldLayout((View)this.mCollapseButtonView)) {
            this.measureChildConstrained((View)this.mCollapseButtonView, resolveSizeAndState, 0, n, 0, this.mMaxButtonHeight);
            b = this.mCollapseButtonView.getMeasuredWidth() + this.getHorizontalMargins((View)this.mCollapseButtonView);
            n4 = Math.max(max, this.mCollapseButtonView.getMeasuredHeight() + this.getVerticalMargins((View)this.mCollapseButtonView));
            n5 = View.combineMeasuredStates(combineMeasuredStates, this.mCollapseButtonView.getMeasuredState());
        }
        final int currentContentInsetStart = this.getCurrentContentInsetStart();
        final int n6 = 0 + Math.max(currentContentInsetStart, b);
        mTempMargins[n2] = Math.max(0, currentContentInsetStart - b);
        int b2;
        if (this.shouldLayout((View)this.mMenuView)) {
            this.measureChildConstrained((View)this.mMenuView, resolveSizeAndState, n6, n, 0, this.mMaxButtonHeight);
            final int measuredWidth = this.mMenuView.getMeasuredWidth();
            final int horizontalMargins = this.getHorizontalMargins((View)this.mMenuView);
            n4 = Math.max(n4, this.mMenuView.getMeasuredHeight() + this.getVerticalMargins((View)this.mMenuView));
            n5 = View.combineMeasuredStates(n5, this.mMenuView.getMeasuredState());
            b2 = measuredWidth + horizontalMargins;
        }
        else {
            b2 = 0;
        }
        final int currentContentInsetEnd = this.getCurrentContentInsetEnd();
        final int n7 = n6 + Math.max(currentContentInsetEnd, b2);
        mTempMargins[n3] = Math.max(0, currentContentInsetEnd - b2);
        int n8;
        int max2;
        if (this.shouldLayout(this.mExpandedActionView)) {
            n8 = n7 + this.measureChildCollapseMargins(this.mExpandedActionView, resolveSizeAndState, n7, n, 0, mTempMargins);
            max2 = Math.max(n4, this.mExpandedActionView.getMeasuredHeight() + this.getVerticalMargins(this.mExpandedActionView));
            n5 = View.combineMeasuredStates(n5, this.mExpandedActionView.getMeasuredState());
        }
        else {
            max2 = n4;
            n8 = n7;
        }
        int n9 = n8;
        int max3 = max2;
        int combineMeasuredStates2 = n5;
        if (this.shouldLayout((View)this.mLogoView)) {
            n9 = n8 + this.measureChildCollapseMargins((View)this.mLogoView, resolveSizeAndState, n8, n, 0, mTempMargins);
            max3 = Math.max(max2, this.mLogoView.getMeasuredHeight() + this.getVerticalMargins((View)this.mLogoView));
            combineMeasuredStates2 = View.combineMeasuredStates(n5, this.mLogoView.getMeasuredState());
        }
        final int childCount = this.getChildCount();
        int combineMeasuredStates3 = combineMeasuredStates2;
        int n10 = n9;
        int i = 0;
        int max4 = max3;
        while (i < childCount) {
            final View child = this.getChildAt(i);
            if (((LayoutParams)child.getLayoutParams()).mViewType == 0) {
                if (this.shouldLayout(child)) {
                    n10 += this.measureChildCollapseMargins(child, resolveSizeAndState, n10, n, 0, mTempMargins);
                    max4 = Math.max(max4, child.getMeasuredHeight() + this.getVerticalMargins(child));
                    combineMeasuredStates3 = View.combineMeasuredStates(combineMeasuredStates3, child.getMeasuredState());
                }
            }
            ++i;
        }
        final int n11 = combineMeasuredStates3;
        int max5 = 0;
        int b3 = 0;
        final int n12 = this.mTitleMarginTop + this.mTitleMarginBottom;
        final int n13 = this.mTitleMarginStart + this.mTitleMarginEnd;
        int n14 = n11;
        if (this.shouldLayout((View)this.mTitleTextView)) {
            this.measureChildCollapseMargins((View)this.mTitleTextView, resolveSizeAndState, n10 + n13, n, n12, mTempMargins);
            max5 = this.mTitleTextView.getMeasuredWidth() + this.getHorizontalMargins((View)this.mTitleTextView);
            b3 = this.mTitleTextView.getMeasuredHeight() + this.getVerticalMargins((View)this.mTitleTextView);
            n14 = View.combineMeasuredStates(n11, this.mTitleTextView.getMeasuredState());
        }
        if (this.shouldLayout((View)this.mSubtitleTextView)) {
            max5 = Math.max(max5, this.measureChildCollapseMargins((View)this.mSubtitleTextView, resolveSizeAndState, n10 + n13, n, b3 + n12, mTempMargins));
            final int measuredHeight = this.mSubtitleTextView.getMeasuredHeight();
            final int verticalMargins = this.getVerticalMargins((View)this.mSubtitleTextView);
            n14 = View.combineMeasuredStates(n14, this.mSubtitleTextView.getMeasuredState());
            b3 += measuredHeight + verticalMargins;
        }
        final int max6 = Math.max(max4, b3);
        final int paddingLeft = this.getPaddingLeft();
        final int paddingRight = this.getPaddingRight();
        final int paddingTop = this.getPaddingTop();
        final int paddingBottom = this.getPaddingBottom();
        final int resolveSizeAndState2 = View.resolveSizeAndState(Math.max(n10 + max5 + (paddingLeft + paddingRight), this.getSuggestedMinimumWidth()), resolveSizeAndState, 0xFF000000 & n14);
        resolveSizeAndState = View.resolveSizeAndState(Math.max(max6 + (paddingTop + paddingBottom), this.getSuggestedMinimumHeight()), n, n14 << 16);
        if (this.shouldCollapse()) {
            resolveSizeAndState = 0;
        }
        this.setMeasuredDimension(resolveSizeAndState2, resolveSizeAndState);
    }
    
    protected void onRestoreInstanceState(final Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        final SavedState savedState = (SavedState)parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        final ActionMenuView mMenuView = this.mMenuView;
        Object peekMenu;
        if (mMenuView != null) {
            peekMenu = mMenuView.peekMenu();
        }
        else {
            peekMenu = null;
        }
        if (savedState.expandedMenuItemId != 0 && this.mExpandedMenuPresenter != null && peekMenu != null) {
            final MenuItem item = ((Menu)peekMenu).findItem(savedState.expandedMenuItemId);
            if (item != null) {
                item.expandActionView();
            }
        }
        if (savedState.isOverflowOpen) {
            this.postShowOverflowMenu();
        }
    }
    
    public void onRtlPropertiesChanged(final int n) {
        if (Build$VERSION.SDK_INT >= 17) {
            super.onRtlPropertiesChanged(n);
        }
        this.ensureContentInsets();
        final RtlSpacingHelper mContentInsets = this.mContentInsets;
        boolean direction = true;
        if (n != 1) {
            direction = false;
        }
        mContentInsets.setDirection(direction);
    }
    
    protected Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        final ExpandedActionViewMenuPresenter mExpandedMenuPresenter = this.mExpandedMenuPresenter;
        if (mExpandedMenuPresenter != null && mExpandedMenuPresenter.mCurrentExpandedItem != null) {
            savedState.expandedMenuItemId = this.mExpandedMenuPresenter.mCurrentExpandedItem.getItemId();
        }
        savedState.isOverflowOpen = this.isOverflowMenuShowing();
        return (Parcelable)savedState;
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mEatingTouch = false;
        }
        if (!this.mEatingTouch) {
            final boolean onTouchEvent = super.onTouchEvent(motionEvent);
            if (actionMasked == 0 && !onTouchEvent) {
                this.mEatingTouch = true;
            }
        }
        if (actionMasked == 1 || actionMasked == 3) {
            this.mEatingTouch = false;
        }
        return true;
    }
    
    void removeChildrenForExpandedActionView() {
        for (int i = this.getChildCount() - 1; i >= 0; --i) {
            final View child = this.getChildAt(i);
            if (((LayoutParams)child.getLayoutParams()).mViewType != 2 && child != this.mMenuView) {
                this.removeViewAt(i);
                this.mHiddenViews.add(child);
            }
        }
    }
    
    public void setCollapsible(final boolean mCollapsible) {
        this.mCollapsible = mCollapsible;
        this.requestLayout();
    }
    
    public void setContentInsetEndWithActions(final int n) {
        int mContentInsetEndWithActions = n;
        if (n < 0) {
            mContentInsetEndWithActions = Integer.MIN_VALUE;
        }
        if (mContentInsetEndWithActions != this.mContentInsetEndWithActions) {
            this.mContentInsetEndWithActions = mContentInsetEndWithActions;
            if (this.getNavigationIcon() != null) {
                this.requestLayout();
            }
        }
    }
    
    public void setContentInsetStartWithNavigation(final int n) {
        int mContentInsetStartWithNavigation = n;
        if (n < 0) {
            mContentInsetStartWithNavigation = Integer.MIN_VALUE;
        }
        if (mContentInsetStartWithNavigation != this.mContentInsetStartWithNavigation) {
            this.mContentInsetStartWithNavigation = mContentInsetStartWithNavigation;
            if (this.getNavigationIcon() != null) {
                this.requestLayout();
            }
        }
    }
    
    public void setContentInsetsAbsolute(final int n, final int n2) {
        this.ensureContentInsets();
        this.mContentInsets.setAbsolute(n, n2);
    }
    
    public void setContentInsetsRelative(final int n, final int n2) {
        this.ensureContentInsets();
        this.mContentInsets.setRelative(n, n2);
    }
    
    public void setLogo(final int n) {
        this.setLogo(AppCompatResources.getDrawable(this.getContext(), n));
    }
    
    public void setLogo(final Drawable imageDrawable) {
        if (imageDrawable != null) {
            this.ensureLogoView();
            if (!this.isChildOrHidden((View)this.mLogoView)) {
                this.addSystemView((View)this.mLogoView, true);
            }
        }
        else {
            final ImageView mLogoView = this.mLogoView;
            if (mLogoView != null && this.isChildOrHidden((View)mLogoView)) {
                this.removeView((View)this.mLogoView);
                this.mHiddenViews.remove(this.mLogoView);
            }
        }
        final ImageView mLogoView2 = this.mLogoView;
        if (mLogoView2 != null) {
            mLogoView2.setImageDrawable(imageDrawable);
        }
    }
    
    public void setLogoDescription(final int n) {
        this.setLogoDescription(this.getContext().getText(n));
    }
    
    public void setLogoDescription(final CharSequence contentDescription) {
        if (!TextUtils.isEmpty(contentDescription)) {
            this.ensureLogoView();
        }
        final ImageView mLogoView = this.mLogoView;
        if (mLogoView != null) {
            mLogoView.setContentDescription(contentDescription);
        }
    }
    
    public void setMenu(final MenuBuilder menuBuilder, final ActionMenuPresenter actionMenuPresenter) {
        if (menuBuilder == null && this.mMenuView == null) {
            return;
        }
        this.ensureMenuView();
        final MenuBuilder peekMenu = this.mMenuView.peekMenu();
        if (peekMenu == menuBuilder) {
            return;
        }
        if (peekMenu != null) {
            peekMenu.removeMenuPresenter(this.mOuterActionMenuPresenter);
            peekMenu.removeMenuPresenter(this.mExpandedMenuPresenter);
        }
        if (this.mExpandedMenuPresenter == null) {
            this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter();
        }
        actionMenuPresenter.setExpandedActionViewsExclusive(true);
        if (menuBuilder != null) {
            menuBuilder.addMenuPresenter(actionMenuPresenter, this.mPopupContext);
            menuBuilder.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
        }
        else {
            actionMenuPresenter.initForMenu(this.mPopupContext, null);
            this.mExpandedMenuPresenter.initForMenu(this.mPopupContext, null);
            actionMenuPresenter.updateMenuView(true);
            this.mExpandedMenuPresenter.updateMenuView(true);
        }
        this.mMenuView.setPopupTheme(this.mPopupTheme);
        this.mMenuView.setPresenter(actionMenuPresenter);
        this.mOuterActionMenuPresenter = actionMenuPresenter;
    }
    
    public void setMenuCallbacks(final MenuPresenter.Callback mActionMenuPresenterCallback, final MenuBuilder.Callback mMenuBuilderCallback) {
        this.mActionMenuPresenterCallback = mActionMenuPresenterCallback;
        this.mMenuBuilderCallback = mMenuBuilderCallback;
        final ActionMenuView mMenuView = this.mMenuView;
        if (mMenuView != null) {
            mMenuView.setMenuCallbacks(mActionMenuPresenterCallback, mMenuBuilderCallback);
        }
    }
    
    public void setNavigationContentDescription(final int n) {
        CharSequence text;
        if (n != 0) {
            text = this.getContext().getText(n);
        }
        else {
            text = null;
        }
        this.setNavigationContentDescription(text);
    }
    
    public void setNavigationContentDescription(final CharSequence contentDescription) {
        if (!TextUtils.isEmpty(contentDescription)) {
            this.ensureNavButtonView();
        }
        final ImageButton mNavButtonView = this.mNavButtonView;
        if (mNavButtonView != null) {
            mNavButtonView.setContentDescription(contentDescription);
        }
    }
    
    public void setNavigationIcon(final int n) {
        this.setNavigationIcon(AppCompatResources.getDrawable(this.getContext(), n));
    }
    
    public void setNavigationIcon(final Drawable imageDrawable) {
        if (imageDrawable != null) {
            this.ensureNavButtonView();
            if (!this.isChildOrHidden((View)this.mNavButtonView)) {
                this.addSystemView((View)this.mNavButtonView, true);
            }
        }
        else {
            final ImageButton mNavButtonView = this.mNavButtonView;
            if (mNavButtonView != null && this.isChildOrHidden((View)mNavButtonView)) {
                this.removeView((View)this.mNavButtonView);
                this.mHiddenViews.remove(this.mNavButtonView);
            }
        }
        final ImageButton mNavButtonView2 = this.mNavButtonView;
        if (mNavButtonView2 != null) {
            mNavButtonView2.setImageDrawable(imageDrawable);
        }
    }
    
    public void setNavigationOnClickListener(final View$OnClickListener onClickListener) {
        this.ensureNavButtonView();
        this.mNavButtonView.setOnClickListener(onClickListener);
    }
    
    public void setOnMenuItemClickListener(final OnMenuItemClickListener mOnMenuItemClickListener) {
        this.mOnMenuItemClickListener = mOnMenuItemClickListener;
    }
    
    public void setOverflowIcon(final Drawable overflowIcon) {
        this.ensureMenu();
        this.mMenuView.setOverflowIcon(overflowIcon);
    }
    
    public void setPopupTheme(final int mPopupTheme) {
        if (this.mPopupTheme != mPopupTheme) {
            if ((this.mPopupTheme = mPopupTheme) == 0) {
                this.mPopupContext = this.getContext();
            }
            else {
                this.mPopupContext = (Context)new ContextThemeWrapper(this.getContext(), mPopupTheme);
            }
        }
    }
    
    public void setSubtitle(final int n) {
        this.setSubtitle(this.getContext().getText(n));
    }
    
    public void setSubtitle(final CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            if (this.mSubtitleTextView == null) {
                final Context context = this.getContext();
                (this.mSubtitleTextView = new AppCompatTextView(context)).setSingleLine();
                this.mSubtitleTextView.setEllipsize(TextUtils$TruncateAt.END);
                final int mSubtitleTextAppearance = this.mSubtitleTextAppearance;
                if (mSubtitleTextAppearance != 0) {
                    this.mSubtitleTextView.setTextAppearance(context, mSubtitleTextAppearance);
                }
                final int mSubtitleTextColor = this.mSubtitleTextColor;
                if (mSubtitleTextColor != 0) {
                    this.mSubtitleTextView.setTextColor(mSubtitleTextColor);
                }
            }
            if (!this.isChildOrHidden((View)this.mSubtitleTextView)) {
                this.addSystemView((View)this.mSubtitleTextView, true);
            }
        }
        else {
            final TextView mSubtitleTextView = this.mSubtitleTextView;
            if (mSubtitleTextView != null && this.isChildOrHidden((View)mSubtitleTextView)) {
                this.removeView((View)this.mSubtitleTextView);
                this.mHiddenViews.remove(this.mSubtitleTextView);
            }
        }
        final TextView mSubtitleTextView2 = this.mSubtitleTextView;
        if (mSubtitleTextView2 != null) {
            mSubtitleTextView2.setText(charSequence);
        }
        this.mSubtitleText = charSequence;
    }
    
    public void setSubtitleTextAppearance(final Context context, final int mSubtitleTextAppearance) {
        this.mSubtitleTextAppearance = mSubtitleTextAppearance;
        final TextView mSubtitleTextView = this.mSubtitleTextView;
        if (mSubtitleTextView != null) {
            mSubtitleTextView.setTextAppearance(context, mSubtitleTextAppearance);
        }
    }
    
    public void setSubtitleTextColor(final int n) {
        this.mSubtitleTextColor = n;
        final TextView mSubtitleTextView = this.mSubtitleTextView;
        if (mSubtitleTextView != null) {
            mSubtitleTextView.setTextColor(n);
        }
    }
    
    public void setTitle(final int n) {
        this.setTitle(this.getContext().getText(n));
    }
    
    public void setTitle(final CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            if (this.mTitleTextView == null) {
                final Context context = this.getContext();
                (this.mTitleTextView = new AppCompatTextView(context)).setSingleLine();
                this.mTitleTextView.setEllipsize(TextUtils$TruncateAt.END);
                final int mTitleTextAppearance = this.mTitleTextAppearance;
                if (mTitleTextAppearance != 0) {
                    this.mTitleTextView.setTextAppearance(context, mTitleTextAppearance);
                }
                final int mTitleTextColor = this.mTitleTextColor;
                if (mTitleTextColor != 0) {
                    this.mTitleTextView.setTextColor(mTitleTextColor);
                }
            }
            if (!this.isChildOrHidden((View)this.mTitleTextView)) {
                this.addSystemView((View)this.mTitleTextView, true);
            }
        }
        else {
            final TextView mTitleTextView = this.mTitleTextView;
            if (mTitleTextView != null && this.isChildOrHidden((View)mTitleTextView)) {
                this.removeView((View)this.mTitleTextView);
                this.mHiddenViews.remove(this.mTitleTextView);
            }
        }
        final TextView mTitleTextView2 = this.mTitleTextView;
        if (mTitleTextView2 != null) {
            mTitleTextView2.setText(charSequence);
        }
        this.mTitleText = charSequence;
    }
    
    public void setTitleMargin(final int mTitleMarginStart, final int mTitleMarginTop, final int mTitleMarginEnd, final int mTitleMarginBottom) {
        this.mTitleMarginStart = mTitleMarginStart;
        this.mTitleMarginTop = mTitleMarginTop;
        this.mTitleMarginEnd = mTitleMarginEnd;
        this.mTitleMarginBottom = mTitleMarginBottom;
        this.requestLayout();
    }
    
    public void setTitleMarginBottom(final int mTitleMarginBottom) {
        this.mTitleMarginBottom = mTitleMarginBottom;
        this.requestLayout();
    }
    
    public void setTitleMarginEnd(final int mTitleMarginEnd) {
        this.mTitleMarginEnd = mTitleMarginEnd;
        this.requestLayout();
    }
    
    public void setTitleMarginStart(final int mTitleMarginStart) {
        this.mTitleMarginStart = mTitleMarginStart;
        this.requestLayout();
    }
    
    public void setTitleMarginTop(final int mTitleMarginTop) {
        this.mTitleMarginTop = mTitleMarginTop;
        this.requestLayout();
    }
    
    public void setTitleTextAppearance(final Context context, final int mTitleTextAppearance) {
        this.mTitleTextAppearance = mTitleTextAppearance;
        final TextView mTitleTextView = this.mTitleTextView;
        if (mTitleTextView != null) {
            mTitleTextView.setTextAppearance(context, mTitleTextAppearance);
        }
    }
    
    public void setTitleTextColor(final int n) {
        this.mTitleTextColor = n;
        final TextView mTitleTextView = this.mTitleTextView;
        if (mTitleTextView != null) {
            mTitleTextView.setTextColor(n);
        }
    }
    
    public boolean showOverflowMenu() {
        final ActionMenuView mMenuView = this.mMenuView;
        return mMenuView != null && mMenuView.showOverflowMenu();
    }
    
    private class ExpandedActionViewMenuPresenter implements MenuPresenter
    {
        MenuItemImpl mCurrentExpandedItem;
        MenuBuilder mMenu;
        
        ExpandedActionViewMenuPresenter() {
        }
        
        @Override
        public boolean collapseItemActionView(final MenuBuilder menuBuilder, final MenuItemImpl menuItemImpl) {
            if (Toolbar.this.mExpandedActionView instanceof CollapsibleActionView) {
                ((CollapsibleActionView)Toolbar.this.mExpandedActionView).onActionViewCollapsed();
            }
            final Toolbar this$0 = Toolbar.this;
            this$0.removeView(this$0.mExpandedActionView);
            final Toolbar this$2 = Toolbar.this;
            this$2.removeView((View)this$2.mCollapseButtonView);
            Toolbar.this.mExpandedActionView = null;
            Toolbar.this.addChildrenForExpandedActionView();
            this.mCurrentExpandedItem = null;
            Toolbar.this.requestLayout();
            menuItemImpl.setActionViewExpanded(false);
            return true;
        }
        
        @Override
        public boolean expandItemActionView(final MenuBuilder menuBuilder, final MenuItemImpl mCurrentExpandedItem) {
            Toolbar.this.ensureCollapseButtonView();
            final ViewParent parent = Toolbar.this.mCollapseButtonView.getParent();
            final Toolbar this$0 = Toolbar.this;
            if (parent != this$0) {
                if (parent instanceof ViewGroup) {
                    ((ViewGroup)parent).removeView((View)this$0.mCollapseButtonView);
                }
                final Toolbar this$2 = Toolbar.this;
                this$2.addView((View)this$2.mCollapseButtonView);
            }
            Toolbar.this.mExpandedActionView = mCurrentExpandedItem.getActionView();
            this.mCurrentExpandedItem = mCurrentExpandedItem;
            final ViewParent parent2 = Toolbar.this.mExpandedActionView.getParent();
            final Toolbar this$3 = Toolbar.this;
            if (parent2 != this$3) {
                if (parent2 instanceof ViewGroup) {
                    ((ViewGroup)parent2).removeView(this$3.mExpandedActionView);
                }
                final LayoutParams generateDefaultLayoutParams = Toolbar.this.generateDefaultLayoutParams();
                generateDefaultLayoutParams.gravity = (0x800003 | (Toolbar.this.mButtonGravity & 0x70));
                generateDefaultLayoutParams.mViewType = 2;
                Toolbar.this.mExpandedActionView.setLayoutParams((ViewGroup$LayoutParams)generateDefaultLayoutParams);
                final Toolbar this$4 = Toolbar.this;
                this$4.addView(this$4.mExpandedActionView);
            }
            Toolbar.this.removeChildrenForExpandedActionView();
            Toolbar.this.requestLayout();
            mCurrentExpandedItem.setActionViewExpanded(true);
            if (Toolbar.this.mExpandedActionView instanceof CollapsibleActionView) {
                ((CollapsibleActionView)Toolbar.this.mExpandedActionView).onActionViewExpanded();
            }
            return true;
        }
        
        @Override
        public boolean flagActionItems() {
            return false;
        }
        
        @Override
        public int getId() {
            return 0;
        }
        
        @Override
        public MenuView getMenuView(final ViewGroup viewGroup) {
            return null;
        }
        
        @Override
        public void initForMenu(final Context context, final MenuBuilder mMenu) {
            final MenuBuilder mMenu2 = this.mMenu;
            if (mMenu2 != null) {
                final MenuItemImpl mCurrentExpandedItem = this.mCurrentExpandedItem;
                if (mCurrentExpandedItem != null) {
                    mMenu2.collapseItemActionView(mCurrentExpandedItem);
                }
            }
            this.mMenu = mMenu;
        }
        
        @Override
        public void onCloseMenu(final MenuBuilder menuBuilder, final boolean b) {
        }
        
        @Override
        public void onRestoreInstanceState(final Parcelable parcelable) {
        }
        
        @Override
        public Parcelable onSaveInstanceState() {
            return null;
        }
        
        @Override
        public boolean onSubMenuSelected(final SubMenuBuilder subMenuBuilder) {
            return false;
        }
        
        @Override
        public void setCallback(final Callback callback) {
        }
        
        @Override
        public void updateMenuView(final boolean b) {
            if (this.mCurrentExpandedItem != null) {
                final boolean b2 = false;
                final MenuBuilder mMenu = this.mMenu;
                int n = b2 ? 1 : 0;
                if (mMenu != null) {
                    final int size = mMenu.size();
                    int n2 = 0;
                    while (true) {
                        n = (b2 ? 1 : 0);
                        if (n2 >= size) {
                            break;
                        }
                        if (this.mMenu.getItem(n2) == this.mCurrentExpandedItem) {
                            n = 1;
                            break;
                        }
                        ++n2;
                    }
                }
                if (n == 0) {
                    this.collapseItemActionView(this.mMenu, this.mCurrentExpandedItem);
                }
            }
        }
    }
    
    public static class LayoutParams extends ActionBar.LayoutParams
    {
        static final int CUSTOM = 0;
        static final int EXPANDED = 2;
        static final int SYSTEM = 1;
        int mViewType;
        
        public LayoutParams(final int n) {
            this(-2, -1, n);
        }
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.mViewType = 0;
            this.gravity = 8388627;
        }
        
        public LayoutParams(final int n, final int n2, final int gravity) {
            super(n, n2);
            this.mViewType = 0;
            this.gravity = gravity;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
            this.mViewType = 0;
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            this.mViewType = 0;
        }
        
        public LayoutParams(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            super((ViewGroup$LayoutParams)viewGroup$MarginLayoutParams);
            this.mViewType = 0;
            this.copyMarginsFromCompat(viewGroup$MarginLayoutParams);
        }
        
        public LayoutParams(final ActionBar.LayoutParams layoutParams) {
            super(layoutParams);
            this.mViewType = 0;
        }
        
        public LayoutParams(final LayoutParams layoutParams) {
            super((ActionBar.LayoutParams)layoutParams);
            this.mViewType = 0;
            this.mViewType = layoutParams.mViewType;
        }
        
        void copyMarginsFromCompat(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            this.leftMargin = viewGroup$MarginLayoutParams.leftMargin;
            this.topMargin = viewGroup$MarginLayoutParams.topMargin;
            this.rightMargin = viewGroup$MarginLayoutParams.rightMargin;
            this.bottomMargin = viewGroup$MarginLayoutParams.bottomMargin;
        }
    }
    
    public interface OnMenuItemClickListener
    {
        boolean onMenuItemClick(final MenuItem p0);
    }
    
    public static class SavedState extends AbsSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        int expandedMenuItemId;
        boolean isOverflowOpen;
        
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
        
        public SavedState(final Parcel parcel) {
            this(parcel, null);
        }
        
        public SavedState(final Parcel parcel, final ClassLoader classLoader) {
            super(parcel, classLoader);
            this.expandedMenuItemId = parcel.readInt();
            this.isOverflowOpen = (parcel.readInt() != 0);
        }
        
        public SavedState(final Parcelable parcelable) {
            super(parcelable);
        }
        
        @Override
        public void writeToParcel(final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.expandedMenuItemId);
            parcel.writeInt((int)(this.isOverflowOpen ? 1 : 0));
        }
    }
}
