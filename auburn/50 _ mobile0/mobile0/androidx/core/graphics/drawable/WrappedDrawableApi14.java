// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.graphics.drawable;

import android.graphics.ColorFilter;
import android.graphics.Region;
import android.graphics.Rect;
import android.graphics.drawable.Drawable$ConstantState;
import android.graphics.Canvas;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff$Mode;
import android.graphics.drawable.Drawable$Callback;
import android.graphics.drawable.Drawable;

class WrappedDrawableApi14 extends Drawable implements Drawable$Callback, WrappedDrawable, TintAwareDrawable
{
    static final PorterDuff$Mode DEFAULT_TINT_MODE;
    private boolean mColorFilterSet;
    private int mCurrentColor;
    private PorterDuff$Mode mCurrentMode;
    Drawable mDrawable;
    private boolean mMutated;
    DrawableWrapperState mState;
    
    static {
        DEFAULT_TINT_MODE = PorterDuff$Mode.SRC_IN;
    }
    
    WrappedDrawableApi14(final Drawable wrappedDrawable) {
        this.mState = this.mutateConstantState();
        this.setWrappedDrawable(wrappedDrawable);
    }
    
    WrappedDrawableApi14(final DrawableWrapperState mState, final Resources resources) {
        this.mState = mState;
        this.updateLocalState(resources);
    }
    
    private void updateLocalState(final Resources resources) {
        final DrawableWrapperState mState = this.mState;
        if (mState != null && mState.mDrawableState != null) {
            this.setWrappedDrawable(this.mState.mDrawableState.newDrawable(resources));
        }
    }
    
    private boolean updateTint(final int[] array) {
        if (!this.isCompatTintEnabled()) {
            return false;
        }
        final ColorStateList mTint = this.mState.mTint;
        final PorterDuff$Mode mTintMode = this.mState.mTintMode;
        if (mTint != null && mTintMode != null) {
            final int colorForState = mTint.getColorForState(array, mTint.getDefaultColor());
            if (!this.mColorFilterSet || colorForState != this.mCurrentColor || mTintMode != this.mCurrentMode) {
                this.setColorFilter(colorForState, mTintMode);
                this.mCurrentColor = colorForState;
                this.mCurrentMode = mTintMode;
                return this.mColorFilterSet = true;
            }
        }
        else {
            this.mColorFilterSet = false;
            this.clearColorFilter();
        }
        return false;
    }
    
    public void draw(final Canvas canvas) {
        this.mDrawable.draw(canvas);
    }
    
    public int getChangingConfigurations() {
        final int changingConfigurations = super.getChangingConfigurations();
        final DrawableWrapperState mState = this.mState;
        int changingConfigurations2;
        if (mState != null) {
            changingConfigurations2 = mState.getChangingConfigurations();
        }
        else {
            changingConfigurations2 = 0;
        }
        return changingConfigurations | changingConfigurations2 | this.mDrawable.getChangingConfigurations();
    }
    
    public Drawable$ConstantState getConstantState() {
        final DrawableWrapperState mState = this.mState;
        if (mState != null && mState.canConstantState()) {
            this.mState.mChangingConfigurations = this.getChangingConfigurations();
            return this.mState;
        }
        return null;
    }
    
    public Drawable getCurrent() {
        return this.mDrawable.getCurrent();
    }
    
    public int getIntrinsicHeight() {
        return this.mDrawable.getIntrinsicHeight();
    }
    
    public int getIntrinsicWidth() {
        return this.mDrawable.getIntrinsicWidth();
    }
    
    public int getMinimumHeight() {
        return this.mDrawable.getMinimumHeight();
    }
    
    public int getMinimumWidth() {
        return this.mDrawable.getMinimumWidth();
    }
    
    public int getOpacity() {
        return this.mDrawable.getOpacity();
    }
    
    public boolean getPadding(final Rect rect) {
        return this.mDrawable.getPadding(rect);
    }
    
    public int[] getState() {
        return this.mDrawable.getState();
    }
    
    public Region getTransparentRegion() {
        return this.mDrawable.getTransparentRegion();
    }
    
    public final Drawable getWrappedDrawable() {
        return this.mDrawable;
    }
    
    public void invalidateDrawable(final Drawable drawable) {
        this.invalidateSelf();
    }
    
    public boolean isAutoMirrored() {
        return this.mDrawable.isAutoMirrored();
    }
    
    protected boolean isCompatTintEnabled() {
        return true;
    }
    
    public boolean isStateful() {
        if (this.isCompatTintEnabled()) {
            final DrawableWrapperState mState = this.mState;
            if (mState != null) {
                final ColorStateList mTint = mState.mTint;
                return (mTint != null && mTint.isStateful()) || this.mDrawable.isStateful();
            }
        }
        final ColorStateList mTint = null;
        return (mTint != null && mTint.isStateful()) || this.mDrawable.isStateful();
    }
    
    public void jumpToCurrentState() {
        this.mDrawable.jumpToCurrentState();
    }
    
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mState = this.mutateConstantState();
            final Drawable mDrawable = this.mDrawable;
            if (mDrawable != null) {
                mDrawable.mutate();
            }
            final DrawableWrapperState mState = this.mState;
            if (mState != null) {
                final Drawable mDrawable2 = this.mDrawable;
                Drawable$ConstantState constantState;
                if (mDrawable2 != null) {
                    constantState = mDrawable2.getConstantState();
                }
                else {
                    constantState = null;
                }
                mState.mDrawableState = constantState;
            }
            this.mMutated = true;
        }
        return this;
    }
    
    DrawableWrapperState mutateConstantState() {
        return (DrawableWrapperState)new DrawableWrapperStateBase(this.mState, null);
    }
    
    protected void onBoundsChange(final Rect bounds) {
        final Drawable mDrawable = this.mDrawable;
        if (mDrawable != null) {
            mDrawable.setBounds(bounds);
        }
    }
    
    protected boolean onLevelChange(final int level) {
        return this.mDrawable.setLevel(level);
    }
    
    public void scheduleDrawable(final Drawable drawable, final Runnable runnable, final long n) {
        this.scheduleSelf(runnable, n);
    }
    
    public void setAlpha(final int alpha) {
        this.mDrawable.setAlpha(alpha);
    }
    
    public void setAutoMirrored(final boolean autoMirrored) {
        this.mDrawable.setAutoMirrored(autoMirrored);
    }
    
    public void setChangingConfigurations(final int changingConfigurations) {
        this.mDrawable.setChangingConfigurations(changingConfigurations);
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.mDrawable.setColorFilter(colorFilter);
    }
    
    public void setDither(final boolean dither) {
        this.mDrawable.setDither(dither);
    }
    
    public void setFilterBitmap(final boolean filterBitmap) {
        this.mDrawable.setFilterBitmap(filterBitmap);
    }
    
    public boolean setState(final int[] state) {
        final boolean setState = this.mDrawable.setState(state);
        return this.updateTint(state) || setState;
    }
    
    public void setTint(final int n) {
        this.setTintList(ColorStateList.valueOf(n));
    }
    
    public void setTintList(final ColorStateList mTint) {
        this.mState.mTint = mTint;
        this.updateTint(this.getState());
    }
    
    public void setTintMode(final PorterDuff$Mode mTintMode) {
        this.mState.mTintMode = mTintMode;
        this.updateTint(this.getState());
    }
    
    public boolean setVisible(final boolean b, final boolean b2) {
        return super.setVisible(b, b2) || this.mDrawable.setVisible(b, b2);
    }
    
    public final void setWrappedDrawable(final Drawable mDrawable) {
        final Drawable mDrawable2 = this.mDrawable;
        if (mDrawable2 != null) {
            mDrawable2.setCallback((Drawable$Callback)null);
        }
        if ((this.mDrawable = mDrawable) != null) {
            mDrawable.setCallback((Drawable$Callback)this);
            this.setVisible(mDrawable.isVisible(), true);
            this.setState(mDrawable.getState());
            this.setLevel(mDrawable.getLevel());
            this.setBounds(mDrawable.getBounds());
            final DrawableWrapperState mState = this.mState;
            if (mState != null) {
                mState.mDrawableState = mDrawable.getConstantState();
            }
        }
        this.invalidateSelf();
    }
    
    public void unscheduleDrawable(final Drawable drawable, final Runnable runnable) {
        this.unscheduleSelf(runnable);
    }
    
    protected abstract static class DrawableWrapperState extends Drawable$ConstantState
    {
        int mChangingConfigurations;
        Drawable$ConstantState mDrawableState;
        ColorStateList mTint;
        PorterDuff$Mode mTintMode;
        
        DrawableWrapperState(final DrawableWrapperState drawableWrapperState, final Resources resources) {
            this.mTint = null;
            this.mTintMode = WrappedDrawableApi14.DEFAULT_TINT_MODE;
            if (drawableWrapperState != null) {
                this.mChangingConfigurations = drawableWrapperState.mChangingConfigurations;
                this.mDrawableState = drawableWrapperState.mDrawableState;
                this.mTint = drawableWrapperState.mTint;
                this.mTintMode = drawableWrapperState.mTintMode;
            }
        }
        
        boolean canConstantState() {
            return this.mDrawableState != null;
        }
        
        public int getChangingConfigurations() {
            final int mChangingConfigurations = this.mChangingConfigurations;
            final Drawable$ConstantState mDrawableState = this.mDrawableState;
            int changingConfigurations;
            if (mDrawableState != null) {
                changingConfigurations = mDrawableState.getChangingConfigurations();
            }
            else {
                changingConfigurations = 0;
            }
            return mChangingConfigurations | changingConfigurations;
        }
        
        public Drawable newDrawable() {
            return this.newDrawable(null);
        }
        
        public abstract Drawable newDrawable(final Resources p0);
    }
    
    private static class DrawableWrapperStateBase extends DrawableWrapperState
    {
        DrawableWrapperStateBase(final DrawableWrapperState drawableWrapperState, final Resources resources) {
            super(drawableWrapperState, resources);
        }
        
        @Override
        public Drawable newDrawable(final Resources resources) {
            return new WrappedDrawableApi14((DrawableWrapperState)this, resources);
        }
    }
}