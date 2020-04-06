// 
// Decompiled by Procyon v0.5.36
// 

package androidx.interpolator.view.animation;

import android.view.animation.Interpolator;

abstract class LookupTableInterpolator implements Interpolator
{
    private final float mStepSize;
    private final float[] mValues;
    
    protected LookupTableInterpolator(final float[] mValues) {
        this.mValues = mValues;
        this.mStepSize = 1.0f / (mValues.length - 1);
    }
    
    public float getInterpolation(float n) {
        if (n >= 1.0f) {
            return 1.0f;
        }
        if (n <= 0.0f) {
            return 0.0f;
        }
        final float[] mValues = this.mValues;
        final int min = Math.min((int)((mValues.length - 1) * n), mValues.length - 2);
        final float n2 = (float)min;
        final float mStepSize = this.mStepSize;
        n = (n - n2 * mStepSize) / mStepSize;
        final float[] mValues2 = this.mValues;
        return mValues2[min] + (mValues2[min + 1] - mValues2[min]) * n;
    }
}
