// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.widget;

import android.view.ViewDebug$ExportedProperty;
import android.view.ContextThemeWrapper;
import android.content.res.Configuration;
import android.view.MenuItem;
import androidx.appcompat.view.menu.MenuItemImpl;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.accessibility.AccessibilityEvent;
import android.view.ViewGroup$LayoutParams;
import androidx.appcompat.view.menu.ActionMenuItemView;
import android.view.View$MeasureSpec;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.view.menu.MenuBuilder;

public class ActionMenuView extends LinearLayoutCompat implements ItemInvoker, MenuView
{
    static final int GENERATED_ITEM_PADDING = 4;
    static final int MIN_CELL_SIZE = 56;
    private static final String TAG = "ActionMenuView";
    private MenuPresenter.Callback mActionMenuPresenterCallback;
    private boolean mFormatItems;
    private int mFormatItemsWidth;
    private int mGeneratedItemPadding;
    private MenuBuilder mMenu;
    Callback mMenuBuilderCallback;
    private int mMinCellSize;
    OnMenuItemClickListener mOnMenuItemClickListener;
    private Context mPopupContext;
    private int mPopupTheme;
    private ActionMenuPresenter mPresenter;
    private boolean mReserveOverflow;
    
    public ActionMenuView(final Context context) {
        this(context, null);
    }
    
    public ActionMenuView(final Context mPopupContext, final AttributeSet set) {
        super(mPopupContext, set);
        this.setBaselineAligned(false);
        final float density = mPopupContext.getResources().getDisplayMetrics().density;
        this.mMinCellSize = (int)(56.0f * density);
        this.mGeneratedItemPadding = (int)(4.0f * density);
        this.mPopupContext = mPopupContext;
        this.mPopupTheme = 0;
    }
    
    static int measureChildForCells(final View view, final int n, int n2, int cellsUsed, int n3) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(cellsUsed) - n3, View$MeasureSpec.getMode(cellsUsed));
        ActionMenuItemView actionMenuItemView;
        if (view instanceof ActionMenuItemView) {
            actionMenuItemView = (ActionMenuItemView)view;
        }
        else {
            actionMenuItemView = null;
        }
        final boolean b = false;
        if (actionMenuItemView != null && actionMenuItemView.hasText()) {
            n3 = 1;
        }
        else {
            n3 = 0;
        }
        final int n4 = cellsUsed = 0;
        Label_0146: {
            if (n2 > 0) {
                if (n3 != 0) {
                    cellsUsed = n4;
                    if (n2 < 2) {
                        break Label_0146;
                    }
                }
                view.measure(View$MeasureSpec.makeMeasureSpec(n * n2, Integer.MIN_VALUE), measureSpec);
                final int measuredWidth = view.getMeasuredWidth();
                cellsUsed = (n2 = measuredWidth / n);
                if (measuredWidth % n != 0) {
                    n2 = cellsUsed + 1;
                }
                cellsUsed = n2;
                if (n3 != 0 && (cellsUsed = n2) < 2) {
                    cellsUsed = 2;
                }
            }
        }
        boolean expandable = b;
        if (!layoutParams.isOverflowButton) {
            expandable = b;
            if (n3 != 0) {
                expandable = true;
            }
        }
        layoutParams.expandable = expandable;
        layoutParams.cellsUsed = cellsUsed;
        view.measure(View$MeasureSpec.makeMeasureSpec(cellsUsed * n, 1073741824), measureSpec);
        return cellsUsed;
    }
    
    private void onMeasureExactFormat(int i, int n) {
        final int mode = View$MeasureSpec.getMode(n);
        final int size = View$MeasureSpec.getSize(i);
        final int size2 = View$MeasureSpec.getSize(n);
        i = this.getPaddingLeft();
        final int paddingRight = this.getPaddingRight();
        final int n2 = this.getPaddingTop() + this.getPaddingBottom();
        final int childMeasureSpec = getChildMeasureSpec(n, n2, -2);
        final int n3 = size - (i + paddingRight);
        i = this.mMinCellSize;
        final int n4 = n3 / i;
        final int n5 = n3 % i;
        if (n4 == 0) {
            this.setMeasuredDimension(n3, 0);
            return;
        }
        final int n6 = i + n5 / n4;
        i = n4;
        int max = 0;
        int max2 = 0;
        n = 0;
        boolean b = false;
        long j = 0L;
        final int childCount = this.getChildCount();
        int n7 = 0;
        int n8;
        for (int k = 0; k < childCount; ++k, n7 = n8) {
            final View child = this.getChildAt(k);
            if (child.getVisibility() == 8) {
                n8 = n7;
            }
            else {
                final boolean b2 = child instanceof ActionMenuItemView;
                n8 = n7 + 1;
                if (b2) {
                    final int mGeneratedItemPadding = this.mGeneratedItemPadding;
                    child.setPadding(mGeneratedItemPadding, 0, mGeneratedItemPadding, 0);
                }
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                layoutParams.expanded = false;
                layoutParams.extraPixels = 0;
                layoutParams.cellsUsed = 0;
                layoutParams.expandable = false;
                layoutParams.leftMargin = 0;
                layoutParams.rightMargin = 0;
                layoutParams.preventEdgeOffset = (b2 && ((ActionMenuItemView)child).hasText());
                int n9;
                if (layoutParams.isOverflowButton) {
                    n9 = 1;
                }
                else {
                    n9 = i;
                }
                final int measureChildForCells = measureChildForCells(child, n6, n9, childMeasureSpec, n2);
                max2 = Math.max(max2, measureChildForCells);
                int n10 = n;
                if (layoutParams.expandable) {
                    n10 = n + 1;
                }
                if (layoutParams.isOverflowButton) {
                    b = true;
                }
                i -= measureChildForCells;
                max = Math.max(max, child.getMeasuredHeight());
                if (measureChildForCells == 1) {
                    j |= 1 << k;
                    n = n10;
                }
                else {
                    n = n10;
                }
            }
        }
        final boolean b3 = b && n7 == 2;
        final int n11 = 0;
        int n12 = i;
        i = n11;
        final int n13 = n3;
        final int n14 = mode;
        while (n > 0 && n12 > 0) {
            int n15 = Integer.MAX_VALUE;
            long n16 = 0L;
            int n17 = 0;
            int n18;
            int cellsUsed;
            long n19;
            for (int l = 0; l < childCount; ++l, n17 = n18, n15 = cellsUsed, n16 = n19) {
                final LayoutParams layoutParams2 = (LayoutParams)this.getChildAt(l).getLayoutParams();
                if (!layoutParams2.expandable) {
                    n18 = n17;
                    cellsUsed = n15;
                    n19 = n16;
                }
                else if (layoutParams2.cellsUsed < n15) {
                    cellsUsed = layoutParams2.cellsUsed;
                    n19 = 1L << l;
                    n18 = 1;
                }
                else {
                    n18 = n17;
                    cellsUsed = n15;
                    n19 = n16;
                    if (layoutParams2.cellsUsed == n15) {
                        n19 = (n16 | 1L << l);
                        n18 = n17 + 1;
                        cellsUsed = n15;
                    }
                }
            }
            j |= n16;
            if (n17 > n12) {
                break;
            }
            View child2;
            LayoutParams layoutParams3;
            int n20;
            long n21;
            int mGeneratedItemPadding2;
            for (i = 0; i < childCount; ++i, n12 = n20, j = n21) {
                child2 = this.getChildAt(i);
                layoutParams3 = (LayoutParams)child2.getLayoutParams();
                if ((n16 & (long)(1 << i)) == 0x0L) {
                    n20 = n12;
                    n21 = j;
                    if (layoutParams3.cellsUsed == n15 + 1) {
                        n21 = (j | (long)(1 << i));
                        n20 = n12;
                    }
                }
                else {
                    if (b3 && layoutParams3.preventEdgeOffset && n12 == 1) {
                        mGeneratedItemPadding2 = this.mGeneratedItemPadding;
                        child2.setPadding(mGeneratedItemPadding2 + n6, 0, mGeneratedItemPadding2, 0);
                    }
                    ++layoutParams3.cellsUsed;
                    layoutParams3.expanded = true;
                    n20 = n12 - 1;
                    n21 = j;
                }
            }
            i = 1;
        }
        if (!b && n7 == 1) {
            n = 1;
        }
        else {
            n = 0;
        }
        int n27;
        if (n12 > 0 && j != 0L && (n12 < n7 - 1 || n != 0 || max2 > 1)) {
            float n23;
            final float n22 = n23 = (float)Long.bitCount(j);
            if (n == 0) {
                float n24;
                if ((j & 0x1L) != 0x0L) {
                    n24 = n22;
                    if (!((LayoutParams)this.getChildAt(0).getLayoutParams()).preventEdgeOffset) {
                        n24 = n22 - 0.5f;
                    }
                }
                else {
                    n24 = n22;
                }
                n23 = n24;
                if ((j & (long)(1 << childCount - 1)) != 0x0L) {
                    n23 = n24;
                    if (!((LayoutParams)this.getChildAt(childCount - 1).getLayoutParams()).preventEdgeOffset) {
                        n23 = n24 - 0.5f;
                    }
                }
            }
            int n25;
            if (n23 > 0.0f) {
                n25 = (int)(n12 * n6 / n23);
            }
            else {
                n25 = 0;
            }
            int n26 = 0;
            n27 = i;
            while (n26 < childCount) {
                if ((j & (long)(1 << n26)) == 0x0L) {
                    i = n27;
                }
                else {
                    final View child3 = this.getChildAt(n26);
                    final LayoutParams layoutParams4 = (LayoutParams)child3.getLayoutParams();
                    if (child3 instanceof ActionMenuItemView) {
                        layoutParams4.extraPixels = n25;
                        layoutParams4.expanded = true;
                        if (n26 == 0 && !layoutParams4.preventEdgeOffset) {
                            layoutParams4.leftMargin = -n25 / 2;
                        }
                        i = 1;
                    }
                    else if (layoutParams4.isOverflowButton) {
                        layoutParams4.extraPixels = n25;
                        layoutParams4.expanded = true;
                        layoutParams4.rightMargin = -n25 / 2;
                        i = 1;
                    }
                    else {
                        if (n26 != 0) {
                            layoutParams4.leftMargin = n25 / 2;
                        }
                        i = n27;
                        if (n26 != childCount - 1) {
                            layoutParams4.rightMargin = n25 / 2;
                            i = n27;
                        }
                    }
                }
                ++n26;
                n27 = i;
            }
        }
        else {
            n27 = i;
        }
        if (n27 != 0) {
            View child4;
            LayoutParams layoutParams5;
            for (i = 0; i < childCount; ++i) {
                child4 = this.getChildAt(i);
                layoutParams5 = (LayoutParams)child4.getLayoutParams();
                if (layoutParams5.expanded) {
                    child4.measure(View$MeasureSpec.makeMeasureSpec(layoutParams5.cellsUsed * n6 + layoutParams5.extraPixels, 1073741824), childMeasureSpec);
                }
            }
        }
        if (n14 == 1073741824) {
            max = size2;
        }
        this.setMeasuredDimension(n13, max);
    }
    
    @Override
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams != null && viewGroup$LayoutParams instanceof LayoutParams;
    }
    
    public void dismissPopupMenus() {
        final ActionMenuPresenter mPresenter = this.mPresenter;
        if (mPresenter != null) {
            mPresenter.dismissPopupMenus();
        }
    }
    
    public boolean dispatchPopulateAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        return false;
    }
    
    protected LayoutParams generateDefaultLayoutParams() {
        final LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.gravity = 16;
        return layoutParams;
    }
    
    public LayoutParams generateLayoutParams(final AttributeSet set) {
        return new LayoutParams(this.getContext(), set);
    }
    
    protected LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        if (viewGroup$LayoutParams != null) {
            LayoutParams layoutParams;
            if (viewGroup$LayoutParams instanceof LayoutParams) {
                layoutParams = new LayoutParams((LayoutParams)viewGroup$LayoutParams);
            }
            else {
                layoutParams = new LayoutParams(viewGroup$LayoutParams);
            }
            if (layoutParams.gravity <= 0) {
                layoutParams.gravity = 16;
            }
            return layoutParams;
        }
        return this.generateDefaultLayoutParams();
    }
    
    public LayoutParams generateOverflowButtonLayoutParams() {
        final LayoutParams generateDefaultLayoutParams = this.generateDefaultLayoutParams();
        generateDefaultLayoutParams.isOverflowButton = true;
        return generateDefaultLayoutParams;
    }
    
    public Menu getMenu() {
        if (this.mMenu == null) {
            final Context context = this.getContext();
            (this.mMenu = new MenuBuilder(context)).setCallback((MenuBuilder.Callback)new MenuBuilderCallback());
            (this.mPresenter = new ActionMenuPresenter(context)).setReserveOverflow(true);
            final ActionMenuPresenter mPresenter = this.mPresenter;
            MenuPresenter.Callback mActionMenuPresenterCallback = this.mActionMenuPresenterCallback;
            if (mActionMenuPresenterCallback == null) {
                mActionMenuPresenterCallback = new ActionMenuPresenterCallback();
            }
            mPresenter.setCallback(mActionMenuPresenterCallback);
            this.mMenu.addMenuPresenter(this.mPresenter, this.mPopupContext);
            this.mPresenter.setMenuView(this);
        }
        return (Menu)this.mMenu;
    }
    
    public Drawable getOverflowIcon() {
        this.getMenu();
        return this.mPresenter.getOverflowIcon();
    }
    
    public int getPopupTheme() {
        return this.mPopupTheme;
    }
    
    @Override
    public int getWindowAnimations() {
        return 0;
    }
    
    protected boolean hasSupportDividerBeforeChildAt(final int n) {
        if (n == 0) {
            return false;
        }
        final View child = this.getChildAt(n - 1);
        final View child2 = this.getChildAt(n);
        int n3;
        final int n2 = n3 = 0;
        if (n < this.getChildCount()) {
            n3 = n2;
            if (child instanceof ActionMenuChildView) {
                n3 = ((false | ((ActionMenuChildView)child).needsDividerAfter()) ? 1 : 0);
            }
        }
        boolean b = n3 != 0;
        if (n > 0) {
            b = (n3 != 0);
            if (child2 instanceof ActionMenuChildView) {
                b = ((n3 | (((ActionMenuChildView)child2).needsDividerBefore() ? 1 : 0)) != 0x0);
            }
        }
        return b;
    }
    
    public boolean hideOverflowMenu() {
        final ActionMenuPresenter mPresenter = this.mPresenter;
        return mPresenter != null && mPresenter.hideOverflowMenu();
    }
    
    @Override
    public void initialize(final MenuBuilder mMenu) {
        this.mMenu = mMenu;
    }
    
    @Override
    public boolean invokeItem(final MenuItemImpl menuItemImpl) {
        return this.mMenu.performItemAction((MenuItem)menuItemImpl, 0);
    }
    
    public boolean isOverflowMenuShowPending() {
        final ActionMenuPresenter mPresenter = this.mPresenter;
        return mPresenter != null && mPresenter.isOverflowMenuShowPending();
    }
    
    public boolean isOverflowMenuShowing() {
        final ActionMenuPresenter mPresenter = this.mPresenter;
        return mPresenter != null && mPresenter.isOverflowMenuShowing();
    }
    
    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        final ActionMenuPresenter mPresenter = this.mPresenter;
        if (mPresenter != null) {
            mPresenter.updateMenuView(false);
            if (this.mPresenter.isOverflowMenuShowing()) {
                this.mPresenter.hideOverflowMenu();
                this.mPresenter.showOverflowMenu();
            }
        }
    }
    
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.dismissPopupMenus();
    }
    
    @Override
    protected void onLayout(final boolean b, int i, int n, int n2, int measuredWidth) {
        if (!this.mFormatItems) {
            super.onLayout(b, i, n, n2, measuredWidth);
            return;
        }
        final int childCount = this.getChildCount();
        final int n3 = (measuredWidth - n) / 2;
        final int dividerWidth = this.getDividerWidth();
        n = 0;
        int n4 = 0;
        int n5 = 0;
        measuredWidth = n2 - i - this.getPaddingRight() - this.getPaddingLeft();
        boolean b2 = false;
        final boolean layoutRtl = ViewUtils.isLayoutRtl((View)this);
        for (int j = 0; j < childCount; ++j) {
            final View child = this.getChildAt(j);
            if (child.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                if (layoutParams.isOverflowButton) {
                    final int n6 = n = child.getMeasuredWidth();
                    if (this.hasSupportDividerBeforeChildAt(j)) {
                        n = n6 + dividerWidth;
                    }
                    final int measuredHeight = child.getMeasuredHeight();
                    int n7;
                    int n8;
                    if (layoutRtl) {
                        n7 = this.getPaddingLeft() + layoutParams.leftMargin;
                        n8 = n7 + n;
                    }
                    else {
                        n8 = this.getWidth() - this.getPaddingRight() - layoutParams.rightMargin;
                        n7 = n8 - n;
                    }
                    final int n9 = n3 - measuredHeight / 2;
                    child.layout(n7, n9, n8, n9 + measuredHeight);
                    measuredWidth -= n;
                    b2 = true;
                }
                else {
                    final int n10 = child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
                    final int n11 = n4 + n10;
                    measuredWidth -= n10;
                    n4 = n11;
                    if (this.hasSupportDividerBeforeChildAt(j)) {
                        n4 = n11 + dividerWidth;
                    }
                    ++n5;
                }
            }
        }
        if (childCount == 1 && !b2) {
            final View child2 = this.getChildAt(0);
            measuredWidth = child2.getMeasuredWidth();
            n = child2.getMeasuredHeight();
            i = (n2 - i) / 2 - measuredWidth / 2;
            n2 = n3 - n / 2;
            child2.layout(i, n2, i + measuredWidth, n2 + n);
            return;
        }
        i = n5 - ((b2 ^ true) ? 1 : 0);
        if (i > 0) {
            i = measuredWidth / i;
        }
        else {
            i = 0;
        }
        final int max = Math.max(0, i);
        if (layoutRtl) {
            measuredWidth = this.getWidth() - this.getPaddingRight();
            i = 0;
            n2 = dividerWidth;
            while (i < childCount) {
                final View child3 = this.getChildAt(i);
                final LayoutParams layoutParams2 = (LayoutParams)child3.getLayoutParams();
                if (child3.getVisibility() != 8) {
                    if (!layoutParams2.isOverflowButton) {
                        final int n12 = measuredWidth - layoutParams2.rightMargin;
                        final int measuredWidth2 = child3.getMeasuredWidth();
                        final int measuredHeight2 = child3.getMeasuredHeight();
                        measuredWidth = n3 - measuredHeight2 / 2;
                        child3.layout(n12 - measuredWidth2, measuredWidth, n12, measuredWidth + measuredHeight2);
                        measuredWidth = n12 - (layoutParams2.leftMargin + measuredWidth2 + max);
                    }
                }
                ++i;
            }
        }
        else {
            n = this.getPaddingLeft();
            View child4;
            LayoutParams layoutParams3;
            int measuredWidth3;
            for (i = 0; i < childCount; ++i, n = n2) {
                child4 = this.getChildAt(i);
                layoutParams3 = (LayoutParams)child4.getLayoutParams();
                n2 = n;
                if (child4.getVisibility() != 8) {
                    if (layoutParams3.isOverflowButton) {
                        n2 = n;
                    }
                    else {
                        measuredWidth = n + layoutParams3.leftMargin;
                        measuredWidth3 = child4.getMeasuredWidth();
                        n = child4.getMeasuredHeight();
                        n2 = n3 - n / 2;
                        child4.layout(measuredWidth, n2, measuredWidth + measuredWidth3, n2 + n);
                        n2 = measuredWidth + (layoutParams3.rightMargin + measuredWidth3 + max);
                    }
                }
            }
        }
    }
    
    @Override
    protected void onMeasure(final int n, final int n2) {
        final boolean mFormatItems = this.mFormatItems;
        int mFormatItems2;
        if (View$MeasureSpec.getMode(n) == 1073741824) {
            mFormatItems2 = 1;
        }
        else {
            mFormatItems2 = 0;
        }
        this.mFormatItems = (mFormatItems2 != 0);
        if ((mFormatItems ? 1 : 0) != mFormatItems2) {
            this.mFormatItemsWidth = 0;
        }
        final int size = View$MeasureSpec.getSize(n);
        if (this.mFormatItems) {
            final MenuBuilder mMenu = this.mMenu;
            if (mMenu != null && size != this.mFormatItemsWidth) {
                this.mFormatItemsWidth = size;
                mMenu.onItemsChanged(true);
            }
        }
        final int childCount = this.getChildCount();
        if (this.mFormatItems && childCount > 0) {
            this.onMeasureExactFormat(n, n2);
        }
        else {
            for (int i = 0; i < childCount; ++i) {
                final LayoutParams layoutParams = (LayoutParams)this.getChildAt(i).getLayoutParams();
                layoutParams.rightMargin = 0;
                layoutParams.leftMargin = 0;
            }
            super.onMeasure(n, n2);
        }
    }
    
    public MenuBuilder peekMenu() {
        return this.mMenu;
    }
    
    public void setExpandedActionViewsExclusive(final boolean expandedActionViewsExclusive) {
        this.mPresenter.setExpandedActionViewsExclusive(expandedActionViewsExclusive);
    }
    
    public void setMenuCallbacks(final MenuPresenter.Callback mActionMenuPresenterCallback, final Callback mMenuBuilderCallback) {
        this.mActionMenuPresenterCallback = mActionMenuPresenterCallback;
        this.mMenuBuilderCallback = mMenuBuilderCallback;
    }
    
    public void setOnMenuItemClickListener(final OnMenuItemClickListener mOnMenuItemClickListener) {
        this.mOnMenuItemClickListener = mOnMenuItemClickListener;
    }
    
    public void setOverflowIcon(final Drawable overflowIcon) {
        this.getMenu();
        this.mPresenter.setOverflowIcon(overflowIcon);
    }
    
    public void setOverflowReserved(final boolean mReserveOverflow) {
        this.mReserveOverflow = mReserveOverflow;
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
    
    public void setPresenter(final ActionMenuPresenter mPresenter) {
        (this.mPresenter = mPresenter).setMenuView(this);
    }
    
    public boolean showOverflowMenu() {
        final ActionMenuPresenter mPresenter = this.mPresenter;
        return mPresenter != null && mPresenter.showOverflowMenu();
    }
    
    public interface ActionMenuChildView
    {
        boolean needsDividerAfter();
        
        boolean needsDividerBefore();
    }
    
    private static class ActionMenuPresenterCallback implements MenuPresenter.Callback
    {
        ActionMenuPresenterCallback() {
        }
        
        @Override
        public void onCloseMenu(final MenuBuilder menuBuilder, final boolean b) {
        }
        
        @Override
        public boolean onOpenSubMenu(final MenuBuilder menuBuilder) {
            return false;
        }
    }
    
    public static class LayoutParams extends LinearLayoutCompat.LayoutParams
    {
        @ViewDebug$ExportedProperty
        public int cellsUsed;
        @ViewDebug$ExportedProperty
        public boolean expandable;
        boolean expanded;
        @ViewDebug$ExportedProperty
        public int extraPixels;
        @ViewDebug$ExportedProperty
        public boolean isOverflowButton;
        @ViewDebug$ExportedProperty
        public boolean preventEdgeOffset;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.isOverflowButton = false;
        }
        
        LayoutParams(final int n, final int n2, final boolean isOverflowButton) {
            super(n, n2);
            this.isOverflowButton = isOverflowButton;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
        }
        
        public LayoutParams(final LayoutParams layoutParams) {
            super((ViewGroup$LayoutParams)layoutParams);
            this.isOverflowButton = layoutParams.isOverflowButton;
        }
    }
    
    private class MenuBuilderCallback implements Callback
    {
        MenuBuilderCallback() {
        }
        
        @Override
        public boolean onMenuItemSelected(final MenuBuilder menuBuilder, final MenuItem menuItem) {
            return ActionMenuView.this.mOnMenuItemClickListener != null && ActionMenuView.this.mOnMenuItemClickListener.onMenuItemClick(menuItem);
        }
        
        @Override
        public void onMenuModeChange(final MenuBuilder menuBuilder) {
            if (ActionMenuView.this.mMenuBuilderCallback != null) {
                ActionMenuView.this.mMenuBuilderCallback.onMenuModeChange(menuBuilder);
            }
        }
    }
    
    public interface OnMenuItemClickListener
    {
        boolean onMenuItemClick(final MenuItem p0);
    }
}
