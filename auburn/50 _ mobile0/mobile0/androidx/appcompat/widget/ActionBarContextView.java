// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.widget;

import androidx.core.view.ViewPropertyAnimatorCompat;
import android.view.View$MeasureSpec;
import android.view.accessibility.AccessibilityEvent;
import android.view.MotionEvent;
import android.graphics.drawable.Drawable;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuBuilder;
import android.view.View$OnClickListener;
import androidx.appcompat.view.ActionMode;
import android.view.ViewGroup$MarginLayoutParams;
import android.view.ViewGroup$LayoutParams;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import androidx.core.view.ViewCompat;
import androidx.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;

public class ActionBarContextView extends AbsActionBarView
{
    private static final String TAG = "ActionBarContextView";
    private View mClose;
    private int mCloseItemLayout;
    private View mCustomView;
    private CharSequence mSubtitle;
    private int mSubtitleStyleRes;
    private TextView mSubtitleView;
    private CharSequence mTitle;
    private LinearLayout mTitleLayout;
    private boolean mTitleOptional;
    private int mTitleStyleRes;
    private TextView mTitleView;
    
    public ActionBarContextView(final Context context) {
        this(context, null);
    }
    
    public ActionBarContextView(final Context context, final AttributeSet set) {
        this(context, set, R.attr.actionModeStyle);
    }
    
    public ActionBarContextView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, set, R.styleable.ActionMode, n, 0);
        ViewCompat.setBackground((View)this, obtainStyledAttributes.getDrawable(R.styleable.ActionMode_background));
        this.mTitleStyleRes = obtainStyledAttributes.getResourceId(R.styleable.ActionMode_titleTextStyle, 0);
        this.mSubtitleStyleRes = obtainStyledAttributes.getResourceId(R.styleable.ActionMode_subtitleTextStyle, 0);
        this.mContentHeight = obtainStyledAttributes.getLayoutDimension(R.styleable.ActionMode_height, 0);
        this.mCloseItemLayout = obtainStyledAttributes.getResourceId(R.styleable.ActionMode_closeItemLayout, R.layout.abc_action_mode_close_item_material);
        obtainStyledAttributes.recycle();
    }
    
    private void initTitle() {
        if (this.mTitleLayout == null) {
            LayoutInflater.from(this.getContext()).inflate(R.layout.abc_action_bar_title_item, (ViewGroup)this);
            final LinearLayout mTitleLayout = (LinearLayout)this.getChildAt(this.getChildCount() - 1);
            this.mTitleLayout = mTitleLayout;
            this.mTitleView = (TextView)mTitleLayout.findViewById(R.id.action_bar_title);
            this.mSubtitleView = (TextView)this.mTitleLayout.findViewById(R.id.action_bar_subtitle);
            if (this.mTitleStyleRes != 0) {
                this.mTitleView.setTextAppearance(this.getContext(), this.mTitleStyleRes);
            }
            if (this.mSubtitleStyleRes != 0) {
                this.mSubtitleView.setTextAppearance(this.getContext(), this.mSubtitleStyleRes);
            }
        }
        this.mTitleView.setText(this.mTitle);
        this.mSubtitleView.setText(this.mSubtitle);
        final boolean empty = TextUtils.isEmpty(this.mTitle);
        final boolean b = TextUtils.isEmpty(this.mSubtitle) ^ true;
        final TextView mSubtitleView = this.mSubtitleView;
        final int n = 0;
        int visibility;
        if (b) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        mSubtitleView.setVisibility(visibility);
        final LinearLayout mTitleLayout2 = this.mTitleLayout;
        int visibility2 = n;
        if (!(empty ^ true)) {
            if (b) {
                visibility2 = n;
            }
            else {
                visibility2 = 8;
            }
        }
        mTitleLayout2.setVisibility(visibility2);
        if (this.mTitleLayout.getParent() == null) {
            this.addView((View)this.mTitleLayout);
        }
    }
    
    public void closeMode() {
        if (this.mClose == null) {
            this.killMode();
        }
    }
    
    protected ViewGroup$LayoutParams generateDefaultLayoutParams() {
        return (ViewGroup$LayoutParams)new ViewGroup$MarginLayoutParams(-1, -2);
    }
    
    public ViewGroup$LayoutParams generateLayoutParams(final AttributeSet set) {
        return (ViewGroup$LayoutParams)new ViewGroup$MarginLayoutParams(this.getContext(), set);
    }
    
    public CharSequence getSubtitle() {
        return this.mSubtitle;
    }
    
    public CharSequence getTitle() {
        return this.mTitle;
    }
    
    @Override
    public boolean hideOverflowMenu() {
        return this.mActionMenuPresenter != null && this.mActionMenuPresenter.hideOverflowMenu();
    }
    
    public void initForMode(final ActionMode actionMode) {
        final View mClose = this.mClose;
        if (mClose == null) {
            this.addView(this.mClose = LayoutInflater.from(this.getContext()).inflate(this.mCloseItemLayout, (ViewGroup)this, false));
        }
        else if (mClose.getParent() == null) {
            this.addView(this.mClose);
        }
        this.mClose.findViewById(R.id.action_mode_close_button).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                actionMode.finish();
            }
        });
        final MenuBuilder menuBuilder = (MenuBuilder)actionMode.getMenu();
        if (this.mActionMenuPresenter != null) {
            this.mActionMenuPresenter.dismissPopupMenus();
        }
        (this.mActionMenuPresenter = new ActionMenuPresenter(this.getContext())).setReserveOverflow(true);
        final ViewGroup$LayoutParams viewGroup$LayoutParams = new ViewGroup$LayoutParams(-2, -1);
        menuBuilder.addMenuPresenter(this.mActionMenuPresenter, this.mPopupContext);
        ViewCompat.setBackground((View)(this.mMenuView = (ActionMenuView)this.mActionMenuPresenter.getMenuView(this)), null);
        this.addView((View)this.mMenuView, viewGroup$LayoutParams);
    }
    
    @Override
    public boolean isOverflowMenuShowing() {
        return this.mActionMenuPresenter != null && this.mActionMenuPresenter.isOverflowMenuShowing();
    }
    
    public boolean isTitleOptional() {
        return this.mTitleOptional;
    }
    
    public void killMode() {
        this.removeAllViews();
        this.mCustomView = null;
        this.mMenuView = null;
    }
    
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mActionMenuPresenter != null) {
            this.mActionMenuPresenter.hideOverflowMenu();
            this.mActionMenuPresenter.hideSubMenus();
        }
    }
    
    public void onInitializeAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getEventType() == 32) {
            accessibilityEvent.setSource((View)this);
            accessibilityEvent.setClassName((CharSequence)this.getClass().getName());
            accessibilityEvent.setPackageName((CharSequence)this.getContext().getPackageName());
            accessibilityEvent.setContentDescription(this.mTitle);
        }
        else {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
        }
    }
    
    protected void onLayout(final boolean b, int paddingLeft, int n, final int n2, int n3) {
        final boolean layoutRtl = ViewUtils.isLayoutRtl((View)this);
        int paddingLeft2;
        if (layoutRtl) {
            paddingLeft2 = n2 - paddingLeft - this.getPaddingRight();
        }
        else {
            paddingLeft2 = this.getPaddingLeft();
        }
        final int paddingTop = this.getPaddingTop();
        final int n4 = n3 - n - this.getPaddingTop() - this.getPaddingBottom();
        final View mClose = this.mClose;
        if (mClose != null && mClose.getVisibility() != 8) {
            final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams = (ViewGroup$MarginLayoutParams)this.mClose.getLayoutParams();
            if (layoutRtl) {
                n = viewGroup$MarginLayoutParams.rightMargin;
            }
            else {
                n = viewGroup$MarginLayoutParams.leftMargin;
            }
            if (layoutRtl) {
                n3 = viewGroup$MarginLayoutParams.leftMargin;
            }
            else {
                n3 = viewGroup$MarginLayoutParams.rightMargin;
            }
            n = AbsActionBarView.next(paddingLeft2, n, layoutRtl);
            n = AbsActionBarView.next(n + this.positionChild(this.mClose, n, paddingTop, n4, layoutRtl), n3, layoutRtl);
        }
        else {
            n = paddingLeft2;
        }
        final LinearLayout mTitleLayout = this.mTitleLayout;
        n3 = n;
        if (mTitleLayout != null) {
            n3 = n;
            if (this.mCustomView == null) {
                n3 = n;
                if (mTitleLayout.getVisibility() != 8) {
                    n3 = n + this.positionChild((View)this.mTitleLayout, n, paddingTop, n4, layoutRtl);
                }
            }
        }
        final View mCustomView = this.mCustomView;
        if (mCustomView != null) {
            this.positionChild(mCustomView, n3, paddingTop, n4, layoutRtl);
        }
        if (layoutRtl) {
            paddingLeft = this.getPaddingLeft();
        }
        else {
            paddingLeft = n2 - paddingLeft - this.getPaddingRight();
        }
        if (this.mMenuView != null) {
            this.positionChild((View)this.mMenuView, paddingLeft, paddingTop, n4, layoutRtl ^ true);
        }
    }
    
    protected void onMeasure(int i, int b) {
        final int mode = View$MeasureSpec.getMode(i);
        final int n = 1073741824;
        if (mode != 1073741824) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.getClass().getSimpleName());
            sb.append(" can only be used ");
            sb.append("with android:layout_width=\"match_parent\" (or fill_parent)");
            throw new IllegalStateException(sb.toString());
        }
        if (View$MeasureSpec.getMode(b) != 0) {
            final int size = View$MeasureSpec.getSize(i);
            int n2;
            if (this.mContentHeight > 0) {
                n2 = this.mContentHeight;
            }
            else {
                n2 = View$MeasureSpec.getSize(b);
            }
            final int n3 = this.getPaddingTop() + this.getPaddingBottom();
            i = size - this.getPaddingLeft() - this.getPaddingRight();
            final int b2 = n2 - n3;
            final int measureSpec = View$MeasureSpec.makeMeasureSpec(b2, Integer.MIN_VALUE);
            final View mClose = this.mClose;
            final int n4 = 0;
            b = i;
            if (mClose != null) {
                i = this.measureChildView(mClose, i, measureSpec, 0);
                final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams = (ViewGroup$MarginLayoutParams)this.mClose.getLayoutParams();
                b = i - (viewGroup$MarginLayoutParams.leftMargin + viewGroup$MarginLayoutParams.rightMargin);
            }
            i = b;
            if (this.mMenuView != null) {
                i = b;
                if (this.mMenuView.getParent() == this) {
                    i = this.measureChildView((View)this.mMenuView, b, measureSpec, 0);
                }
            }
            final LinearLayout mTitleLayout = this.mTitleLayout;
            b = i;
            if (mTitleLayout != null) {
                b = i;
                if (this.mCustomView == null) {
                    if (this.mTitleOptional) {
                        b = View$MeasureSpec.makeMeasureSpec(0, 0);
                        this.mTitleLayout.measure(b, measureSpec);
                        final int measuredWidth = this.mTitleLayout.getMeasuredWidth();
                        final boolean b3 = measuredWidth <= i;
                        b = i;
                        if (b3) {
                            b = i - measuredWidth;
                        }
                        final LinearLayout mTitleLayout2 = this.mTitleLayout;
                        if (b3) {
                            i = n4;
                        }
                        else {
                            i = 8;
                        }
                        mTitleLayout2.setVisibility(i);
                    }
                    else {
                        b = this.measureChildView((View)mTitleLayout, i, measureSpec, 0);
                    }
                }
            }
            final View mCustomView = this.mCustomView;
            if (mCustomView != null) {
                final ViewGroup$LayoutParams layoutParams = mCustomView.getLayoutParams();
                if (layoutParams.width != -2) {
                    i = 1073741824;
                }
                else {
                    i = Integer.MIN_VALUE;
                }
                if (layoutParams.width >= 0) {
                    b = Math.min(layoutParams.width, b);
                }
                int n5;
                if (layoutParams.height != -2) {
                    n5 = n;
                }
                else {
                    n5 = Integer.MIN_VALUE;
                }
                int min;
                if (layoutParams.height >= 0) {
                    min = Math.min(layoutParams.height, b2);
                }
                else {
                    min = b2;
                }
                this.mCustomView.measure(View$MeasureSpec.makeMeasureSpec(b, i), View$MeasureSpec.makeMeasureSpec(min, n5));
            }
            if (this.mContentHeight <= 0) {
                b = 0;
                int childCount;
                int n6;
                int n7;
                for (childCount = this.getChildCount(), i = 0; i < childCount; ++i, b = n7) {
                    n6 = this.getChildAt(i).getMeasuredHeight() + n3;
                    if (n6 > (n7 = b)) {
                        n7 = n6;
                    }
                }
                this.setMeasuredDimension(size, b);
            }
            else {
                this.setMeasuredDimension(size, n2);
            }
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(this.getClass().getSimpleName());
        sb2.append(" can only be used ");
        sb2.append("with android:layout_height=\"wrap_content\"");
        throw new IllegalStateException(sb2.toString());
    }
    
    @Override
    public void setContentHeight(final int mContentHeight) {
        this.mContentHeight = mContentHeight;
    }
    
    public void setCustomView(final View mCustomView) {
        final View mCustomView2 = this.mCustomView;
        if (mCustomView2 != null) {
            this.removeView(mCustomView2);
        }
        if ((this.mCustomView = mCustomView) != null) {
            final LinearLayout mTitleLayout = this.mTitleLayout;
            if (mTitleLayout != null) {
                this.removeView((View)mTitleLayout);
                this.mTitleLayout = null;
            }
        }
        if (mCustomView != null) {
            this.addView(mCustomView);
        }
        this.requestLayout();
    }
    
    public void setSubtitle(final CharSequence mSubtitle) {
        this.mSubtitle = mSubtitle;
        this.initTitle();
    }
    
    public void setTitle(final CharSequence mTitle) {
        this.mTitle = mTitle;
        this.initTitle();
    }
    
    public void setTitleOptional(final boolean mTitleOptional) {
        if (mTitleOptional != this.mTitleOptional) {
            this.requestLayout();
        }
        this.mTitleOptional = mTitleOptional;
    }
    
    public boolean shouldDelayChildPressedState() {
        return false;
    }
    
    @Override
    public boolean showOverflowMenu() {
        return this.mActionMenuPresenter != null && this.mActionMenuPresenter.showOverflowMenu();
    }
}
