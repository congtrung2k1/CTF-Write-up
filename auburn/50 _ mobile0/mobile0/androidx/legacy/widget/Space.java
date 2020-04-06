// 
// Decompiled by Procyon v0.5.36
// 

package androidx.legacy.widget;

import android.graphics.Canvas;
import android.view.View$MeasureSpec;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;

@Deprecated
public class Space extends View
{
    @Deprecated
    public Space(final Context context) {
        this(context, null);
    }
    
    @Deprecated
    public Space(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    @Deprecated
    public Space(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        if (this.getVisibility() == 0) {
            this.setVisibility(4);
        }
    }
    
    private static int getDefaultSize2(int min, int size) {
        final int n = min;
        final int mode = View$MeasureSpec.getMode(size);
        size = View$MeasureSpec.getSize(size);
        if (mode != Integer.MIN_VALUE) {
            if (mode != 0) {
                if (mode != 1073741824) {
                    min = n;
                }
                else {
                    min = size;
                }
            }
        }
        else {
            min = Math.min(min, size);
        }
        return min;
    }
    
    @Deprecated
    public void draw(final Canvas canvas) {
    }
    
    @Deprecated
    protected void onMeasure(final int n, final int n2) {
        this.setMeasuredDimension(getDefaultSize2(this.getSuggestedMinimumWidth(), n), getDefaultSize2(this.getSuggestedMinimumHeight(), n2));
    }
}
