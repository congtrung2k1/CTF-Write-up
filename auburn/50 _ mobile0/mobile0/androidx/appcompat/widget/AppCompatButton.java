// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.widget;

import androidx.core.widget.TextViewCompat;
import android.view.ActionMode$Callback;
import android.graphics.drawable.Drawable;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityEvent;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.widget.TextView;
import android.view.View;
import androidx.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import androidx.core.widget.AutoSizeableTextView;
import androidx.core.view.TintableBackgroundView;
import android.widget.Button;

public class AppCompatButton extends Button implements TintableBackgroundView, AutoSizeableTextView
{
    private final AppCompatBackgroundHelper mBackgroundTintHelper;
    private final AppCompatTextHelper mTextHelper;
    
    public AppCompatButton(final Context context) {
        this(context, null);
    }
    
    public AppCompatButton(final Context context, final AttributeSet set) {
        this(context, set, R.attr.buttonStyle);
    }
    
    public AppCompatButton(final Context context, final AttributeSet set, final int n) {
        super(TintContextWrapper.wrap(context), set, n);
        (this.mBackgroundTintHelper = new AppCompatBackgroundHelper((View)this)).loadFromAttributes(set, n);
        (this.mTextHelper = new AppCompatTextHelper((TextView)this)).loadFromAttributes(set, n);
        this.mTextHelper.applyCompoundDrawablesTints();
    }
    
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        final AppCompatBackgroundHelper mBackgroundTintHelper = this.mBackgroundTintHelper;
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.applySupportBackgroundTint();
        }
        final AppCompatTextHelper mTextHelper = this.mTextHelper;
        if (mTextHelper != null) {
            mTextHelper.applyCompoundDrawablesTints();
        }
    }
    
    public int getAutoSizeMaxTextSize() {
        if (AppCompatButton.PLATFORM_SUPPORTS_AUTOSIZE) {
            return super.getAutoSizeMaxTextSize();
        }
        final AppCompatTextHelper mTextHelper = this.mTextHelper;
        if (mTextHelper != null) {
            return mTextHelper.getAutoSizeMaxTextSize();
        }
        return -1;
    }
    
    public int getAutoSizeMinTextSize() {
        if (AppCompatButton.PLATFORM_SUPPORTS_AUTOSIZE) {
            return super.getAutoSizeMinTextSize();
        }
        final AppCompatTextHelper mTextHelper = this.mTextHelper;
        if (mTextHelper != null) {
            return mTextHelper.getAutoSizeMinTextSize();
        }
        return -1;
    }
    
    public int getAutoSizeStepGranularity() {
        if (AppCompatButton.PLATFORM_SUPPORTS_AUTOSIZE) {
            return super.getAutoSizeStepGranularity();
        }
        final AppCompatTextHelper mTextHelper = this.mTextHelper;
        if (mTextHelper != null) {
            return mTextHelper.getAutoSizeStepGranularity();
        }
        return -1;
    }
    
    public int[] getAutoSizeTextAvailableSizes() {
        if (AppCompatButton.PLATFORM_SUPPORTS_AUTOSIZE) {
            return super.getAutoSizeTextAvailableSizes();
        }
        final AppCompatTextHelper mTextHelper = this.mTextHelper;
        if (mTextHelper != null) {
            return mTextHelper.getAutoSizeTextAvailableSizes();
        }
        return new int[0];
    }
    
    public int getAutoSizeTextType() {
        final boolean platform_SUPPORTS_AUTOSIZE = AppCompatButton.PLATFORM_SUPPORTS_AUTOSIZE;
        int n = 0;
        if (platform_SUPPORTS_AUTOSIZE) {
            if (super.getAutoSizeTextType() == 1) {
                n = 1;
            }
            return n;
        }
        final AppCompatTextHelper mTextHelper = this.mTextHelper;
        if (mTextHelper != null) {
            return mTextHelper.getAutoSizeTextType();
        }
        return 0;
    }
    
    public ColorStateList getSupportBackgroundTintList() {
        final AppCompatBackgroundHelper mBackgroundTintHelper = this.mBackgroundTintHelper;
        ColorStateList supportBackgroundTintList;
        if (mBackgroundTintHelper != null) {
            supportBackgroundTintList = mBackgroundTintHelper.getSupportBackgroundTintList();
        }
        else {
            supportBackgroundTintList = null;
        }
        return supportBackgroundTintList;
    }
    
    public PorterDuff$Mode getSupportBackgroundTintMode() {
        final AppCompatBackgroundHelper mBackgroundTintHelper = this.mBackgroundTintHelper;
        PorterDuff$Mode supportBackgroundTintMode;
        if (mBackgroundTintHelper != null) {
            supportBackgroundTintMode = mBackgroundTintHelper.getSupportBackgroundTintMode();
        }
        else {
            supportBackgroundTintMode = null;
        }
        return supportBackgroundTintMode;
    }
    
    public void onInitializeAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName((CharSequence)Button.class.getName());
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName((CharSequence)Button.class.getName());
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        final AppCompatTextHelper mTextHelper = this.mTextHelper;
        if (mTextHelper != null) {
            mTextHelper.onLayout(b, n, n2, n3, n4);
        }
    }
    
    protected void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
        super.onTextChanged(charSequence, n, n2, n3);
        if (this.mTextHelper != null && !AppCompatButton.PLATFORM_SUPPORTS_AUTOSIZE && this.mTextHelper.isAutoSizeEnabled()) {
            this.mTextHelper.autoSizeText();
        }
    }
    
    public void setAutoSizeTextTypeUniformWithConfiguration(final int n, final int n2, final int n3, final int n4) throws IllegalArgumentException {
        if (AppCompatButton.PLATFORM_SUPPORTS_AUTOSIZE) {
            super.setAutoSizeTextTypeUniformWithConfiguration(n, n2, n3, n4);
        }
        else {
            final AppCompatTextHelper mTextHelper = this.mTextHelper;
            if (mTextHelper != null) {
                mTextHelper.setAutoSizeTextTypeUniformWithConfiguration(n, n2, n3, n4);
            }
        }
    }
    
    public void setAutoSizeTextTypeUniformWithPresetSizes(final int[] array, final int n) throws IllegalArgumentException {
        if (AppCompatButton.PLATFORM_SUPPORTS_AUTOSIZE) {
            super.setAutoSizeTextTypeUniformWithPresetSizes(array, n);
        }
        else {
            final AppCompatTextHelper mTextHelper = this.mTextHelper;
            if (mTextHelper != null) {
                mTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(array, n);
            }
        }
    }
    
    public void setAutoSizeTextTypeWithDefaults(final int n) {
        if (AppCompatButton.PLATFORM_SUPPORTS_AUTOSIZE) {
            super.setAutoSizeTextTypeWithDefaults(n);
        }
        else {
            final AppCompatTextHelper mTextHelper = this.mTextHelper;
            if (mTextHelper != null) {
                mTextHelper.setAutoSizeTextTypeWithDefaults(n);
            }
        }
    }
    
    public void setBackgroundDrawable(final Drawable backgroundDrawable) {
        super.setBackgroundDrawable(backgroundDrawable);
        final AppCompatBackgroundHelper mBackgroundTintHelper = this.mBackgroundTintHelper;
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.onSetBackgroundDrawable(backgroundDrawable);
        }
    }
    
    public void setBackgroundResource(final int backgroundResource) {
        super.setBackgroundResource(backgroundResource);
        final AppCompatBackgroundHelper mBackgroundTintHelper = this.mBackgroundTintHelper;
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.onSetBackgroundResource(backgroundResource);
        }
    }
    
    public void setCustomSelectionActionModeCallback(final ActionMode$Callback actionMode$Callback) {
        super.setCustomSelectionActionModeCallback(TextViewCompat.wrapCustomSelectionActionModeCallback((TextView)this, actionMode$Callback));
    }
    
    public void setSupportAllCaps(final boolean allCaps) {
        final AppCompatTextHelper mTextHelper = this.mTextHelper;
        if (mTextHelper != null) {
            mTextHelper.setAllCaps(allCaps);
        }
    }
    
    public void setSupportBackgroundTintList(final ColorStateList supportBackgroundTintList) {
        final AppCompatBackgroundHelper mBackgroundTintHelper = this.mBackgroundTintHelper;
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.setSupportBackgroundTintList(supportBackgroundTintList);
        }
    }
    
    public void setSupportBackgroundTintMode(final PorterDuff$Mode supportBackgroundTintMode) {
        final AppCompatBackgroundHelper mBackgroundTintHelper = this.mBackgroundTintHelper;
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.setSupportBackgroundTintMode(supportBackgroundTintMode);
        }
    }
    
    public void setTextAppearance(final Context context, final int n) {
        super.setTextAppearance(context, n);
        final AppCompatTextHelper mTextHelper = this.mTextHelper;
        if (mTextHelper != null) {
            mTextHelper.onSetTextAppearance(context, n);
        }
    }
    
    public void setTextSize(final int n, final float n2) {
        if (AppCompatButton.PLATFORM_SUPPORTS_AUTOSIZE) {
            super.setTextSize(n, n2);
        }
        else {
            final AppCompatTextHelper mTextHelper = this.mTextHelper;
            if (mTextHelper != null) {
                mTextHelper.setTextSize(n, n2);
            }
        }
    }
}
