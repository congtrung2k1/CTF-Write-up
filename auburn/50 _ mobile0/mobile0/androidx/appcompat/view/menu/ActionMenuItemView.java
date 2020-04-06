// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.view.menu;

import android.view.MotionEvent;
import android.os.Parcelable;
import android.view.View$MeasureSpec;
import android.view.View;
import androidx.appcompat.widget.TooltipCompat;
import android.text.TextUtils;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.content.res.Resources;
import androidx.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.appcompat.widget.ForwardingListener;
import androidx.appcompat.widget.ActionMenuView;
import android.view.View$OnClickListener;
import androidx.appcompat.widget.AppCompatTextView;

public class ActionMenuItemView extends AppCompatTextView implements ItemView, View$OnClickListener, ActionMenuChildView
{
    private static final int MAX_ICON_SIZE = 32;
    private static final String TAG = "ActionMenuItemView";
    private boolean mAllowTextWithIcon;
    private boolean mExpandedFormat;
    private ForwardingListener mForwardingListener;
    private Drawable mIcon;
    MenuItemImpl mItemData;
    ItemInvoker mItemInvoker;
    private int mMaxIconSize;
    private int mMinWidth;
    PopupCallback mPopupCallback;
    private int mSavedPaddingLeft;
    private CharSequence mTitle;
    
    public ActionMenuItemView(final Context context) {
        this(context, null);
    }
    
    public ActionMenuItemView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public ActionMenuItemView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        final Resources resources = context.getResources();
        this.mAllowTextWithIcon = this.shouldAllowTextWithIcon();
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.ActionMenuItemView, n, 0);
        this.mMinWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.ActionMenuItemView_android_minWidth, 0);
        obtainStyledAttributes.recycle();
        this.mMaxIconSize = (int)(32.0f * resources.getDisplayMetrics().density + 0.5f);
        this.setOnClickListener((View$OnClickListener)this);
        this.mSavedPaddingLeft = -1;
        this.setSaveEnabled(false);
    }
    
    private boolean shouldAllowTextWithIcon() {
        final Configuration configuration = this.getContext().getResources().getConfiguration();
        final int screenWidthDp = configuration.screenWidthDp;
        final int screenHeightDp = configuration.screenHeightDp;
        return screenWidthDp >= 480 || (screenWidthDp >= 640 && screenHeightDp >= 480) || configuration.orientation == 2;
    }
    
    private void updateTextButtonVisibility() {
        final boolean empty = TextUtils.isEmpty(this.mTitle);
        boolean b = true;
        if (this.mIcon != null) {
            if (!this.mItemData.showsTextAsAction() || (!this.mAllowTextWithIcon && !this.mExpandedFormat)) {
                b = false;
            }
        }
        final boolean b2 = (empty ^ true) & b;
        final CharSequence charSequence = null;
        CharSequence mTitle;
        if (b2) {
            mTitle = this.mTitle;
        }
        else {
            mTitle = null;
        }
        this.setText(mTitle);
        final CharSequence contentDescription = this.mItemData.getContentDescription();
        if (TextUtils.isEmpty(contentDescription)) {
            CharSequence title;
            if (b2) {
                title = null;
            }
            else {
                title = this.mItemData.getTitle();
            }
            this.setContentDescription(title);
        }
        else {
            this.setContentDescription(contentDescription);
        }
        final CharSequence tooltipText = this.mItemData.getTooltipText();
        if (TextUtils.isEmpty(tooltipText)) {
            CharSequence title2;
            if (b2) {
                title2 = charSequence;
            }
            else {
                title2 = this.mItemData.getTitle();
            }
            TooltipCompat.setTooltipText((View)this, title2);
        }
        else {
            TooltipCompat.setTooltipText((View)this, tooltipText);
        }
    }
    
    @Override
    public MenuItemImpl getItemData() {
        return this.mItemData;
    }
    
    public boolean hasText() {
        return TextUtils.isEmpty(this.getText()) ^ true;
    }
    
    @Override
    public void initialize(final MenuItemImpl mItemData, int visibility) {
        this.mItemData = mItemData;
        this.setIcon(mItemData.getIcon());
        this.setTitle(mItemData.getTitleForItemView(this));
        this.setId(mItemData.getItemId());
        if (mItemData.isVisible()) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        this.setVisibility(visibility);
        ((MenuView.ItemView)this).setEnabled(mItemData.isEnabled());
        if (mItemData.hasSubMenu() && this.mForwardingListener == null) {
            this.mForwardingListener = new ActionMenuItemForwardingListener();
        }
    }
    
    public boolean needsDividerAfter() {
        return this.hasText();
    }
    
    public boolean needsDividerBefore() {
        return this.hasText() && this.mItemData.getIcon() == null;
    }
    
    public void onClick(final View view) {
        final ItemInvoker mItemInvoker = this.mItemInvoker;
        if (mItemInvoker != null) {
            mItemInvoker.invokeItem(this.mItemData);
        }
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mAllowTextWithIcon = this.shouldAllowTextWithIcon();
        this.updateTextButtonVisibility();
    }
    
    @Override
    protected void onMeasure(int a, final int n) {
        final boolean hasText = this.hasText();
        if (hasText) {
            final int mSavedPaddingLeft = this.mSavedPaddingLeft;
            if (mSavedPaddingLeft >= 0) {
                super.setPadding(mSavedPaddingLeft, this.getPaddingTop(), this.getPaddingRight(), this.getPaddingBottom());
            }
        }
        super.onMeasure(a, n);
        final int mode = View$MeasureSpec.getMode(a);
        a = View$MeasureSpec.getSize(a);
        final int measuredWidth = this.getMeasuredWidth();
        if (mode == Integer.MIN_VALUE) {
            a = Math.min(a, this.mMinWidth);
        }
        else {
            a = this.mMinWidth;
        }
        if (mode != 1073741824 && this.mMinWidth > 0 && measuredWidth < a) {
            super.onMeasure(View$MeasureSpec.makeMeasureSpec(a, 1073741824), n);
        }
        if (!hasText && this.mIcon != null) {
            super.setPadding((this.getMeasuredWidth() - this.mIcon.getBounds().width()) / 2, this.getPaddingTop(), this.getPaddingRight(), this.getPaddingBottom());
        }
    }
    
    public void onRestoreInstanceState(final Parcelable parcelable) {
        super.onRestoreInstanceState((Parcelable)null);
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (this.mItemData.hasSubMenu()) {
            final ForwardingListener mForwardingListener = this.mForwardingListener;
            if (mForwardingListener != null && mForwardingListener.onTouch((View)this, motionEvent)) {
                return true;
            }
        }
        return super.onTouchEvent(motionEvent);
    }
    
    @Override
    public boolean prefersCondensedTitle() {
        return true;
    }
    
    @Override
    public void setCheckable(final boolean b) {
    }
    
    @Override
    public void setChecked(final boolean b) {
    }
    
    public void setExpandedFormat(final boolean mExpandedFormat) {
        if (this.mExpandedFormat != mExpandedFormat) {
            this.mExpandedFormat = mExpandedFormat;
            final MenuItemImpl mItemData = this.mItemData;
            if (mItemData != null) {
                mItemData.actionFormatChanged();
            }
        }
    }
    
    @Override
    public void setIcon(final Drawable mIcon) {
        this.mIcon = mIcon;
        if (mIcon != null) {
            final int intrinsicWidth = mIcon.getIntrinsicWidth();
            final int intrinsicHeight = mIcon.getIntrinsicHeight();
            final int mMaxIconSize = this.mMaxIconSize;
            int mMaxIconSize2 = intrinsicWidth;
            int n = intrinsicHeight;
            if (intrinsicWidth > mMaxIconSize) {
                final float n2 = mMaxIconSize / (float)intrinsicWidth;
                mMaxIconSize2 = this.mMaxIconSize;
                n = (int)(intrinsicHeight * n2);
            }
            final int mMaxIconSize3 = this.mMaxIconSize;
            int n3 = mMaxIconSize2;
            int mMaxIconSize4;
            if ((mMaxIconSize4 = n) > mMaxIconSize3) {
                final float n4 = mMaxIconSize3 / (float)n;
                mMaxIconSize4 = this.mMaxIconSize;
                n3 = (int)(mMaxIconSize2 * n4);
            }
            mIcon.setBounds(0, 0, n3, mMaxIconSize4);
        }
        this.setCompoundDrawables(mIcon, (Drawable)null, (Drawable)null, (Drawable)null);
        this.updateTextButtonVisibility();
    }
    
    public void setItemInvoker(final ItemInvoker mItemInvoker) {
        this.mItemInvoker = mItemInvoker;
    }
    
    public void setPadding(final int mSavedPaddingLeft, final int n, final int n2, final int n3) {
        super.setPadding(this.mSavedPaddingLeft = mSavedPaddingLeft, n, n2, n3);
    }
    
    public void setPopupCallback(final PopupCallback mPopupCallback) {
        this.mPopupCallback = mPopupCallback;
    }
    
    @Override
    public void setShortcut(final boolean b, final char c) {
    }
    
    @Override
    public void setTitle(final CharSequence mTitle) {
        this.mTitle = mTitle;
        this.updateTextButtonVisibility();
    }
    
    @Override
    public boolean showsIcon() {
        return true;
    }
    
    private class ActionMenuItemForwardingListener extends ForwardingListener
    {
        public ActionMenuItemForwardingListener() {
            super((View)ActionMenuItemView.this);
        }
        
        @Override
        public ShowableListMenu getPopup() {
            if (ActionMenuItemView.this.mPopupCallback != null) {
                return ActionMenuItemView.this.mPopupCallback.getPopup();
            }
            return null;
        }
        
        @Override
        protected boolean onForwardingStarted() {
            final ItemInvoker mItemInvoker = ActionMenuItemView.this.mItemInvoker;
            final boolean b = false;
            if (mItemInvoker != null && ActionMenuItemView.this.mItemInvoker.invokeItem(ActionMenuItemView.this.mItemData)) {
                final ShowableListMenu popup = this.getPopup();
                boolean b2 = b;
                if (popup != null) {
                    b2 = b;
                    if (popup.isShowing()) {
                        b2 = true;
                    }
                }
                return b2;
            }
            return false;
        }
    }
    
    public abstract static class PopupCallback
    {
        public abstract ShowableListMenu getPopup();
    }
}
