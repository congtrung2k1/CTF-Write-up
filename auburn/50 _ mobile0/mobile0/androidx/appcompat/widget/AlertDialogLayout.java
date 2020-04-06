// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.widget;

import android.graphics.drawable.Drawable;
import androidx.core.view.GravityCompat;
import androidx.appcompat.R;
import android.view.ViewGroup;
import androidx.core.view.ViewCompat;
import android.view.View;
import android.view.View$MeasureSpec;
import android.util.AttributeSet;
import android.content.Context;

public class AlertDialogLayout extends LinearLayoutCompat
{
    public AlertDialogLayout(final Context context) {
        super(context);
    }
    
    public AlertDialogLayout(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    private void forceUniformWidth(final int n, final int n2) {
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 1073741824);
        for (int i = 0; i < n; ++i) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                if (layoutParams.width == -1) {
                    final int height = layoutParams.height;
                    layoutParams.height = child.getMeasuredHeight();
                    this.measureChildWithMargins(child, measureSpec, 0, n2, 0);
                    layoutParams.height = height;
                }
            }
        }
    }
    
    private static int resolveMinimumHeight(final View view) {
        final int minimumHeight = ViewCompat.getMinimumHeight(view);
        if (minimumHeight > 0) {
            return minimumHeight;
        }
        if (view instanceof ViewGroup) {
            final ViewGroup viewGroup = (ViewGroup)view;
            if (viewGroup.getChildCount() == 1) {
                return resolveMinimumHeight(viewGroup.getChildAt(0));
            }
        }
        return 0;
    }
    
    private void setChildFrame(final View view, final int n, final int n2, final int n3, final int n4) {
        view.layout(n, n2, n + n3, n2 + n4);
    }
    
    private boolean tryOnMeasure(final int n, final int n2) {
        View view = null;
        View view2 = null;
        View view3 = null;
        final int childCount = this.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                final int id = child.getId();
                if (id == R.id.topPanel) {
                    view = child;
                }
                else if (id == R.id.buttonPanel) {
                    view2 = child;
                }
                else {
                    if (id != R.id.contentPanel && id != R.id.customPanel) {
                        return false;
                    }
                    if (view3 != null) {
                        return false;
                    }
                    view3 = child;
                }
            }
        }
        final int mode = View$MeasureSpec.getMode(n2);
        final int size = View$MeasureSpec.getSize(n2);
        final int mode2 = View$MeasureSpec.getMode(n);
        int combineMeasuredStates = 0;
        int n4;
        final int n3 = n4 = this.getPaddingTop() + this.getPaddingBottom();
        if (view != null) {
            view.measure(n, 0);
            n4 = n3 + view.getMeasuredHeight();
            combineMeasuredStates = View.combineMeasuredStates(0, view.getMeasuredState());
        }
        int resolveMinimumHeight = 0;
        int b = 0;
        int n5 = combineMeasuredStates;
        int n6 = n4;
        if (view2 != null) {
            view2.measure(n, 0);
            resolveMinimumHeight = resolveMinimumHeight(view2);
            b = view2.getMeasuredHeight() - resolveMinimumHeight;
            n6 = n4 + resolveMinimumHeight;
            n5 = View.combineMeasuredStates(combineMeasuredStates, view2.getMeasuredState());
        }
        int measuredHeight = 0;
        if (view3 != null) {
            int measureSpec;
            if (mode == 0) {
                measureSpec = 0;
            }
            else {
                measureSpec = View$MeasureSpec.makeMeasureSpec(Math.max(0, size - n6), mode);
            }
            view3.measure(n, measureSpec);
            measuredHeight = view3.getMeasuredHeight();
            n6 += measuredHeight;
            n5 = View.combineMeasuredStates(n5, view3.getMeasuredState());
        }
        int n7;
        final int a = n7 = size - n6;
        int n8 = n5;
        int n9 = n6;
        if (view2 != null) {
            final int min = Math.min(a, b);
            int n10 = a;
            int n11 = resolveMinimumHeight;
            if (min > 0) {
                n10 = a - min;
                n11 = resolveMinimumHeight + min;
            }
            view2.measure(n, View$MeasureSpec.makeMeasureSpec(n11, 1073741824));
            n9 = n6 - resolveMinimumHeight + view2.getMeasuredHeight();
            final int combineMeasuredStates2 = View.combineMeasuredStates(n5, view2.getMeasuredState());
            n7 = n10;
            n8 = combineMeasuredStates2;
        }
        int combineMeasuredStates3 = n8;
        int n12 = n9;
        if (view3 != null) {
            combineMeasuredStates3 = n8;
            n12 = n9;
            if (n7 > 0) {
                view3.measure(n, View$MeasureSpec.makeMeasureSpec(measuredHeight + n7, mode));
                n12 = n9 - measuredHeight + view3.getMeasuredHeight();
                combineMeasuredStates3 = View.combineMeasuredStates(n8, view3.getMeasuredState());
            }
        }
        int a2 = 0;
        int max;
        for (int j = 0; j < childCount; ++j, a2 = max) {
            final View child2 = this.getChildAt(j);
            max = a2;
            if (child2.getVisibility() != 8) {
                max = Math.max(a2, child2.getMeasuredWidth());
            }
        }
        this.setMeasuredDimension(View.resolveSizeAndState(a2 + (this.getPaddingLeft() + this.getPaddingRight()), n, combineMeasuredStates3), View.resolveSizeAndState(n12, n2, 0));
        if (mode2 != 1073741824) {
            this.forceUniformWidth(childCount, n2);
        }
        return true;
    }
    
    @Override
    protected void onLayout(final boolean b, int paddingTop, int gravity, int n, int i) {
        final int paddingLeft = this.getPaddingLeft();
        final int n2 = n - paddingTop;
        final int paddingRight = this.getPaddingRight();
        final int paddingRight2 = this.getPaddingRight();
        n = this.getMeasuredHeight();
        final int childCount = this.getChildCount();
        final int gravity2 = this.getGravity();
        paddingTop = (gravity2 & 0x70);
        if (paddingTop != 16) {
            if (paddingTop != 80) {
                paddingTop = this.getPaddingTop();
            }
            else {
                paddingTop = this.getPaddingTop() + i - gravity - n;
            }
        }
        else {
            paddingTop = this.getPaddingTop() + (i - gravity - n) / 2;
        }
        final Drawable dividerDrawable = this.getDividerDrawable();
        if (dividerDrawable == null) {
            n = 0;
        }
        else {
            n = dividerDrawable.getIntrinsicHeight();
        }
        View child;
        int measuredWidth;
        int measuredHeight;
        LayoutParams layoutParams;
        int n3;
        for (i = 0; i < childCount; ++i) {
            child = this.getChildAt(i);
            if (child != null && child.getVisibility() != 8) {
                measuredWidth = child.getMeasuredWidth();
                measuredHeight = child.getMeasuredHeight();
                layoutParams = (LayoutParams)child.getLayoutParams();
                gravity = layoutParams.gravity;
                if (gravity < 0) {
                    gravity = (gravity2 & 0x800007);
                }
                gravity = (GravityCompat.getAbsoluteGravity(gravity, ViewCompat.getLayoutDirection((View)this)) & 0x7);
                if (gravity != 1) {
                    if (gravity != 5) {
                        gravity = layoutParams.leftMargin + paddingLeft;
                    }
                    else {
                        gravity = n2 - paddingRight - measuredWidth - layoutParams.rightMargin;
                    }
                }
                else {
                    gravity = (n2 - paddingLeft - paddingRight2 - measuredWidth) / 2 + paddingLeft + layoutParams.leftMargin - layoutParams.rightMargin;
                }
                n3 = paddingTop;
                if (this.hasDividerBeforeChildAt(i)) {
                    n3 = paddingTop + n;
                }
                paddingTop = n3 + layoutParams.topMargin;
                this.setChildFrame(child, gravity, paddingTop, measuredWidth, measuredHeight);
                paddingTop += measuredHeight + layoutParams.bottomMargin;
            }
        }
    }
    
    @Override
    protected void onMeasure(final int n, final int n2) {
        if (!this.tryOnMeasure(n, n2)) {
            super.onMeasure(n, n2);
        }
    }
}
