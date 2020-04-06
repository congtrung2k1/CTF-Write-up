// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.widget;

import android.graphics.drawable.Drawable;
import androidx.appcompat.content.res.AppCompatResources;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.widget.TextView;
import android.widget.CompoundButton;
import androidx.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import androidx.core.widget.TintableCompoundButton;
import android.widget.CheckBox;

public class AppCompatCheckBox extends CheckBox implements TintableCompoundButton
{
    private final AppCompatCompoundButtonHelper mCompoundButtonHelper;
    private final AppCompatTextHelper mTextHelper;
    
    public AppCompatCheckBox(final Context context) {
        this(context, null);
    }
    
    public AppCompatCheckBox(final Context context, final AttributeSet set) {
        this(context, set, R.attr.checkboxStyle);
    }
    
    public AppCompatCheckBox(final Context context, final AttributeSet set, final int n) {
        super(TintContextWrapper.wrap(context), set, n);
        (this.mCompoundButtonHelper = new AppCompatCompoundButtonHelper((CompoundButton)this)).loadFromAttributes(set, n);
        (this.mTextHelper = new AppCompatTextHelper((TextView)this)).loadFromAttributes(set, n);
    }
    
    public int getCompoundPaddingLeft() {
        int n = super.getCompoundPaddingLeft();
        final AppCompatCompoundButtonHelper mCompoundButtonHelper = this.mCompoundButtonHelper;
        if (mCompoundButtonHelper != null) {
            n = mCompoundButtonHelper.getCompoundPaddingLeft(n);
        }
        return n;
    }
    
    public ColorStateList getSupportButtonTintList() {
        final AppCompatCompoundButtonHelper mCompoundButtonHelper = this.mCompoundButtonHelper;
        ColorStateList supportButtonTintList;
        if (mCompoundButtonHelper != null) {
            supportButtonTintList = mCompoundButtonHelper.getSupportButtonTintList();
        }
        else {
            supportButtonTintList = null;
        }
        return supportButtonTintList;
    }
    
    public PorterDuff$Mode getSupportButtonTintMode() {
        final AppCompatCompoundButtonHelper mCompoundButtonHelper = this.mCompoundButtonHelper;
        PorterDuff$Mode supportButtonTintMode;
        if (mCompoundButtonHelper != null) {
            supportButtonTintMode = mCompoundButtonHelper.getSupportButtonTintMode();
        }
        else {
            supportButtonTintMode = null;
        }
        return supportButtonTintMode;
    }
    
    public void setButtonDrawable(final int n) {
        this.setButtonDrawable(AppCompatResources.getDrawable(this.getContext(), n));
    }
    
    public void setButtonDrawable(final Drawable buttonDrawable) {
        super.setButtonDrawable(buttonDrawable);
        final AppCompatCompoundButtonHelper mCompoundButtonHelper = this.mCompoundButtonHelper;
        if (mCompoundButtonHelper != null) {
            mCompoundButtonHelper.onSetButtonDrawable();
        }
    }
    
    public void setSupportButtonTintList(final ColorStateList supportButtonTintList) {
        final AppCompatCompoundButtonHelper mCompoundButtonHelper = this.mCompoundButtonHelper;
        if (mCompoundButtonHelper != null) {
            mCompoundButtonHelper.setSupportButtonTintList(supportButtonTintList);
        }
    }
    
    public void setSupportButtonTintMode(final PorterDuff$Mode supportButtonTintMode) {
        final AppCompatCompoundButtonHelper mCompoundButtonHelper = this.mCompoundButtonHelper;
        if (mCompoundButtonHelper != null) {
            mCompoundButtonHelper.setSupportButtonTintMode(supportButtonTintMode);
        }
    }
}
