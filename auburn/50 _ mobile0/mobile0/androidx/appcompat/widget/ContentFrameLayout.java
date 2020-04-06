// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.widget;

import android.view.View;
import androidx.core.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.View$MeasureSpec;
import android.util.AttributeSet;
import android.content.Context;
import android.util.TypedValue;
import android.graphics.Rect;
import android.widget.FrameLayout;

public class ContentFrameLayout extends FrameLayout
{
    private OnAttachListener mAttachListener;
    private final Rect mDecorPadding;
    private TypedValue mFixedHeightMajor;
    private TypedValue mFixedHeightMinor;
    private TypedValue mFixedWidthMajor;
    private TypedValue mFixedWidthMinor;
    private TypedValue mMinWidthMajor;
    private TypedValue mMinWidthMinor;
    
    public ContentFrameLayout(final Context context) {
        this(context, null);
    }
    
    public ContentFrameLayout(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public ContentFrameLayout(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mDecorPadding = new Rect();
    }
    
    public void dispatchFitSystemWindows(final Rect rect) {
        this.fitSystemWindows(rect);
    }
    
    public TypedValue getFixedHeightMajor() {
        if (this.mFixedHeightMajor == null) {
            this.mFixedHeightMajor = new TypedValue();
        }
        return this.mFixedHeightMajor;
    }
    
    public TypedValue getFixedHeightMinor() {
        if (this.mFixedHeightMinor == null) {
            this.mFixedHeightMinor = new TypedValue();
        }
        return this.mFixedHeightMinor;
    }
    
    public TypedValue getFixedWidthMajor() {
        if (this.mFixedWidthMajor == null) {
            this.mFixedWidthMajor = new TypedValue();
        }
        return this.mFixedWidthMajor;
    }
    
    public TypedValue getFixedWidthMinor() {
        if (this.mFixedWidthMinor == null) {
            this.mFixedWidthMinor = new TypedValue();
        }
        return this.mFixedWidthMinor;
    }
    
    public TypedValue getMinWidthMajor() {
        if (this.mMinWidthMajor == null) {
            this.mMinWidthMajor = new TypedValue();
        }
        return this.mMinWidthMajor;
    }
    
    public TypedValue getMinWidthMinor() {
        if (this.mMinWidthMinor == null) {
            this.mMinWidthMinor = new TypedValue();
        }
        return this.mMinWidthMinor;
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        final OnAttachListener mAttachListener = this.mAttachListener;
        if (mAttachListener != null) {
            mAttachListener.onAttachedFromWindow();
        }
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        final OnAttachListener mAttachListener = this.mAttachListener;
        if (mAttachListener != null) {
            mAttachListener.onDetachedFromWindow();
        }
    }
    
    protected void onMeasure(int b, int size) {
        final DisplayMetrics displayMetrics = this.getContext().getResources().getDisplayMetrics();
        final boolean b2 = displayMetrics.widthPixels < displayMetrics.heightPixels;
        final int mode = View$MeasureSpec.getMode(b);
        final int mode2 = View$MeasureSpec.getMode(size);
        int n2;
        final int n = n2 = 0;
        int measureSpec = b;
        if (mode == Integer.MIN_VALUE) {
            TypedValue typedValue;
            if (b2) {
                typedValue = this.mFixedWidthMinor;
            }
            else {
                typedValue = this.mFixedWidthMajor;
            }
            n2 = n;
            measureSpec = b;
            if (typedValue != null) {
                n2 = n;
                measureSpec = b;
                if (typedValue.type != 0) {
                    int n3 = 0;
                    if (typedValue.type == 5) {
                        n3 = (int)typedValue.getDimension(displayMetrics);
                    }
                    else if (typedValue.type == 6) {
                        n3 = (int)typedValue.getFraction((float)displayMetrics.widthPixels, (float)displayMetrics.widthPixels);
                    }
                    n2 = n;
                    measureSpec = b;
                    if (n3 > 0) {
                        final int left = this.mDecorPadding.left;
                        final int right = this.mDecorPadding.right;
                        b = View$MeasureSpec.getSize(b);
                        measureSpec = View$MeasureSpec.makeMeasureSpec(Math.min(n3 - (left + right), b), 1073741824);
                        n2 = 1;
                    }
                }
            }
        }
        int measureSpec2 = size;
        if (mode2 == Integer.MIN_VALUE) {
            TypedValue typedValue2;
            if (b2) {
                typedValue2 = this.mFixedHeightMajor;
            }
            else {
                typedValue2 = this.mFixedHeightMinor;
            }
            measureSpec2 = size;
            if (typedValue2 != null) {
                measureSpec2 = size;
                if (typedValue2.type != 0) {
                    b = 0;
                    if (typedValue2.type == 5) {
                        b = (int)typedValue2.getDimension(displayMetrics);
                    }
                    else if (typedValue2.type == 6) {
                        b = (int)typedValue2.getFraction((float)displayMetrics.heightPixels, (float)displayMetrics.heightPixels);
                    }
                    measureSpec2 = size;
                    if (b > 0) {
                        final int top = this.mDecorPadding.top;
                        final int bottom = this.mDecorPadding.bottom;
                        size = View$MeasureSpec.getSize(size);
                        measureSpec2 = View$MeasureSpec.makeMeasureSpec(Math.min(b - (top + bottom), size), 1073741824);
                    }
                }
            }
        }
        super.onMeasure(measureSpec, measureSpec2);
        final int measuredWidth = this.getMeasuredWidth();
        final int n4 = 0;
        final int measureSpec3 = View$MeasureSpec.makeMeasureSpec(measuredWidth, 1073741824);
        size = n4;
        b = measureSpec3;
        if (n2 == 0) {
            size = n4;
            b = measureSpec3;
            if (mode == Integer.MIN_VALUE) {
                TypedValue typedValue3;
                if (b2) {
                    typedValue3 = this.mMinWidthMinor;
                }
                else {
                    typedValue3 = this.mMinWidthMajor;
                }
                size = n4;
                b = measureSpec3;
                if (typedValue3 != null) {
                    size = n4;
                    b = measureSpec3;
                    if (typedValue3.type != 0) {
                        b = 0;
                        if (typedValue3.type == 5) {
                            b = (int)typedValue3.getDimension(displayMetrics);
                        }
                        else if (typedValue3.type == 6) {
                            b = (int)typedValue3.getFraction((float)displayMetrics.widthPixels, (float)displayMetrics.widthPixels);
                        }
                        int n5;
                        if ((n5 = b) > 0) {
                            n5 = b - (this.mDecorPadding.left + this.mDecorPadding.right);
                        }
                        size = n4;
                        b = measureSpec3;
                        if (measuredWidth < n5) {
                            b = View$MeasureSpec.makeMeasureSpec(n5, 1073741824);
                            size = 1;
                        }
                    }
                }
            }
        }
        if (size != 0) {
            super.onMeasure(b, measureSpec2);
        }
    }
    
    public void setAttachListener(final OnAttachListener mAttachListener) {
        this.mAttachListener = mAttachListener;
    }
    
    public void setDecorPadding(final int n, final int n2, final int n3, final int n4) {
        this.mDecorPadding.set(n, n2, n3, n4);
        if (ViewCompat.isLaidOut((View)this)) {
            this.requestLayout();
        }
    }
    
    public interface OnAttachListener
    {
        void onAttachedFromWindow();
        
        void onDetachedFromWindow();
    }
}
