// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.view.menu;

import android.widget.CompoundButton;
import android.view.ViewGroup$LayoutParams;
import androidx.core.view.ViewCompat;
import android.widget.LinearLayout$LayoutParams;
import android.graphics.Rect;
import android.view.ViewGroup;
import android.view.View;
import android.content.res.TypedArray;
import android.content.res.Resources$Theme;
import androidx.appcompat.widget.TintTypedArray;
import androidx.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.TextView;
import android.widget.RadioButton;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.CheckBox;
import android.graphics.drawable.Drawable;
import android.widget.AbsListView$SelectionBoundsAdjuster;
import android.widget.LinearLayout;

public class ListMenuItemView extends LinearLayout implements ItemView, AbsListView$SelectionBoundsAdjuster
{
    private static final String TAG = "ListMenuItemView";
    private Drawable mBackground;
    private CheckBox mCheckBox;
    private LinearLayout mContent;
    private boolean mForceShowIcon;
    private ImageView mGroupDivider;
    private boolean mHasListDivider;
    private ImageView mIconView;
    private LayoutInflater mInflater;
    private MenuItemImpl mItemData;
    private int mMenuType;
    private boolean mPreserveIconSpacing;
    private RadioButton mRadioButton;
    private TextView mShortcutView;
    private Drawable mSubMenuArrow;
    private ImageView mSubMenuArrowView;
    private int mTextAppearance;
    private Context mTextAppearanceContext;
    private TextView mTitleView;
    
    public ListMenuItemView(final Context context, final AttributeSet set) {
        this(context, set, R.attr.listMenuViewStyle);
    }
    
    public ListMenuItemView(final Context mTextAppearanceContext, final AttributeSet set, int dropDownListViewStyle) {
        super(mTextAppearanceContext, set);
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(this.getContext(), set, R.styleable.MenuView, dropDownListViewStyle, 0);
        this.mBackground = obtainStyledAttributes.getDrawable(R.styleable.MenuView_android_itemBackground);
        this.mTextAppearance = obtainStyledAttributes.getResourceId(R.styleable.MenuView_android_itemTextAppearance, -1);
        this.mPreserveIconSpacing = obtainStyledAttributes.getBoolean(R.styleable.MenuView_preserveIconSpacing, false);
        this.mTextAppearanceContext = mTextAppearanceContext;
        this.mSubMenuArrow = obtainStyledAttributes.getDrawable(R.styleable.MenuView_subMenuArrow);
        final Resources$Theme theme = mTextAppearanceContext.getTheme();
        dropDownListViewStyle = R.attr.dropDownListViewStyle;
        final TypedArray obtainStyledAttributes2 = theme.obtainStyledAttributes((AttributeSet)null, new int[] { 16843049 }, dropDownListViewStyle, 0);
        this.mHasListDivider = obtainStyledAttributes2.hasValue(0);
        obtainStyledAttributes.recycle();
        obtainStyledAttributes2.recycle();
    }
    
    private void addContentView(final View view) {
        this.addContentView(view, -1);
    }
    
    private void addContentView(final View view, final int n) {
        final LinearLayout mContent = this.mContent;
        if (mContent != null) {
            mContent.addView(view, n);
        }
        else {
            this.addView(view, n);
        }
    }
    
    private LayoutInflater getInflater() {
        if (this.mInflater == null) {
            this.mInflater = LayoutInflater.from(this.getContext());
        }
        return this.mInflater;
    }
    
    private void insertCheckBox() {
        this.addContentView((View)(this.mCheckBox = (CheckBox)this.getInflater().inflate(R.layout.abc_list_menu_item_checkbox, (ViewGroup)this, false)));
    }
    
    private void insertIconView() {
        this.addContentView((View)(this.mIconView = (ImageView)this.getInflater().inflate(R.layout.abc_list_menu_item_icon, (ViewGroup)this, false)), 0);
    }
    
    private void insertRadioButton() {
        this.addContentView((View)(this.mRadioButton = (RadioButton)this.getInflater().inflate(R.layout.abc_list_menu_item_radio, (ViewGroup)this, false)));
    }
    
    private void setSubMenuArrowVisible(final boolean b) {
        final ImageView mSubMenuArrowView = this.mSubMenuArrowView;
        if (mSubMenuArrowView != null) {
            int visibility;
            if (b) {
                visibility = 0;
            }
            else {
                visibility = 8;
            }
            mSubMenuArrowView.setVisibility(visibility);
        }
    }
    
    public void adjustListItemSelectionBounds(final Rect rect) {
        final ImageView mGroupDivider = this.mGroupDivider;
        if (mGroupDivider != null && mGroupDivider.getVisibility() == 0) {
            final LinearLayout$LayoutParams linearLayout$LayoutParams = (LinearLayout$LayoutParams)this.mGroupDivider.getLayoutParams();
            rect.top += this.mGroupDivider.getHeight() + linearLayout$LayoutParams.topMargin + linearLayout$LayoutParams.bottomMargin;
        }
    }
    
    public MenuItemImpl getItemData() {
        return this.mItemData;
    }
    
    public void initialize(final MenuItemImpl mItemData, int n) {
        this.mItemData = mItemData;
        this.mMenuType = n;
        if (mItemData.isVisible()) {
            n = 0;
        }
        else {
            n = 8;
        }
        this.setVisibility(n);
        this.setTitle(mItemData.getTitleForItemView(this));
        this.setCheckable(mItemData.isCheckable());
        this.setShortcut(mItemData.shouldShowShortcut(), mItemData.getShortcut());
        this.setIcon(mItemData.getIcon());
        ((MenuView.ItemView)this).setEnabled(mItemData.isEnabled());
        this.setSubMenuArrowVisible(mItemData.hasSubMenu());
        this.setContentDescription(mItemData.getContentDescription());
    }
    
    protected void onFinishInflate() {
        super.onFinishInflate();
        ViewCompat.setBackground((View)this, this.mBackground);
        final TextView mTitleView = (TextView)this.findViewById(R.id.title);
        this.mTitleView = mTitleView;
        final int mTextAppearance = this.mTextAppearance;
        if (mTextAppearance != -1) {
            mTitleView.setTextAppearance(this.mTextAppearanceContext, mTextAppearance);
        }
        this.mShortcutView = (TextView)this.findViewById(R.id.shortcut);
        final ImageView mSubMenuArrowView = (ImageView)this.findViewById(R.id.submenuarrow);
        if ((this.mSubMenuArrowView = mSubMenuArrowView) != null) {
            mSubMenuArrowView.setImageDrawable(this.mSubMenuArrow);
        }
        this.mGroupDivider = (ImageView)this.findViewById(R.id.group_divider);
        this.mContent = (LinearLayout)this.findViewById(R.id.content);
    }
    
    protected void onMeasure(final int n, final int n2) {
        if (this.mIconView != null && this.mPreserveIconSpacing) {
            final ViewGroup$LayoutParams layoutParams = this.getLayoutParams();
            final LinearLayout$LayoutParams linearLayout$LayoutParams = (LinearLayout$LayoutParams)this.mIconView.getLayoutParams();
            if (layoutParams.height > 0 && linearLayout$LayoutParams.width <= 0) {
                linearLayout$LayoutParams.width = layoutParams.height;
            }
        }
        super.onMeasure(n, n2);
    }
    
    public boolean prefersCondensedTitle() {
        return false;
    }
    
    public void setCheckable(final boolean b) {
        if (!b && this.mRadioButton == null && this.mCheckBox == null) {
            return;
        }
        Object o;
        Object o2;
        if (this.mItemData.isExclusiveCheckable()) {
            if (this.mRadioButton == null) {
                this.insertRadioButton();
            }
            o = this.mRadioButton;
            o2 = this.mCheckBox;
        }
        else {
            if (this.mCheckBox == null) {
                this.insertCheckBox();
            }
            o = this.mCheckBox;
            o2 = this.mRadioButton;
        }
        if (b) {
            ((CompoundButton)o).setChecked(this.mItemData.isChecked());
            if (((CompoundButton)o).getVisibility() != 0) {
                ((CompoundButton)o).setVisibility(0);
            }
            if (o2 != null && ((CompoundButton)o2).getVisibility() != 8) {
                ((CompoundButton)o2).setVisibility(8);
            }
        }
        else {
            final CheckBox mCheckBox = this.mCheckBox;
            if (mCheckBox != null) {
                mCheckBox.setVisibility(8);
            }
            final RadioButton mRadioButton = this.mRadioButton;
            if (mRadioButton != null) {
                mRadioButton.setVisibility(8);
            }
        }
    }
    
    public void setChecked(final boolean checked) {
        Object o;
        if (this.mItemData.isExclusiveCheckable()) {
            if (this.mRadioButton == null) {
                this.insertRadioButton();
            }
            o = this.mRadioButton;
        }
        else {
            if (this.mCheckBox == null) {
                this.insertCheckBox();
            }
            o = this.mCheckBox;
        }
        ((CompoundButton)o).setChecked(checked);
    }
    
    public void setForceShowIcon(final boolean b) {
        this.mForceShowIcon = b;
        this.mPreserveIconSpacing = b;
    }
    
    public void setGroupDividerEnabled(final boolean b) {
        final ImageView mGroupDivider = this.mGroupDivider;
        if (mGroupDivider != null) {
            int visibility;
            if (!this.mHasListDivider && b) {
                visibility = 0;
            }
            else {
                visibility = 8;
            }
            mGroupDivider.setVisibility(visibility);
        }
    }
    
    public void setIcon(Drawable imageDrawable) {
        final boolean b = this.mItemData.shouldShowIcon() || this.mForceShowIcon;
        if (!b && !this.mPreserveIconSpacing) {
            return;
        }
        if (this.mIconView == null && imageDrawable == null && !this.mPreserveIconSpacing) {
            return;
        }
        if (this.mIconView == null) {
            this.insertIconView();
        }
        if (imageDrawable == null && !this.mPreserveIconSpacing) {
            this.mIconView.setVisibility(8);
        }
        else {
            final ImageView mIconView = this.mIconView;
            if (!b) {
                imageDrawable = null;
            }
            mIconView.setImageDrawable(imageDrawable);
            if (this.mIconView.getVisibility() != 0) {
                this.mIconView.setVisibility(0);
            }
        }
    }
    
    public void setShortcut(final boolean b, final char c) {
        int visibility;
        if (b && this.mItemData.shouldShowShortcut()) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        if (visibility == 0) {
            this.mShortcutView.setText((CharSequence)this.mItemData.getShortcutLabel());
        }
        if (this.mShortcutView.getVisibility() != visibility) {
            this.mShortcutView.setVisibility(visibility);
        }
    }
    
    public void setTitle(final CharSequence text) {
        if (text != null) {
            this.mTitleView.setText(text);
            if (this.mTitleView.getVisibility() != 0) {
                this.mTitleView.setVisibility(0);
            }
        }
        else if (this.mTitleView.getVisibility() != 8) {
            this.mTitleView.setVisibility(8);
        }
    }
    
    public boolean showsIcon() {
        return this.mForceShowIcon;
    }
}
