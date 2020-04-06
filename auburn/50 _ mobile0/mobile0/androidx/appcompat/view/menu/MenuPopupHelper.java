// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.view.menu;

import android.widget.ListView;
import android.graphics.Rect;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import android.view.Display;
import android.os.Build$VERSION;
import android.graphics.Point;
import android.view.WindowManager;
import androidx.appcompat.R;
import android.widget.PopupWindow$OnDismissListener;
import android.content.Context;
import android.view.View;

public class MenuPopupHelper implements MenuHelper
{
    private static final int TOUCH_EPICENTER_SIZE_DP = 48;
    private View mAnchorView;
    private final Context mContext;
    private int mDropDownGravity;
    private boolean mForceShowIcon;
    private final PopupWindow$OnDismissListener mInternalOnDismissListener;
    private final MenuBuilder mMenu;
    private PopupWindow$OnDismissListener mOnDismissListener;
    private final boolean mOverflowOnly;
    private MenuPopup mPopup;
    private final int mPopupStyleAttr;
    private final int mPopupStyleRes;
    private MenuPresenter.Callback mPresenterCallback;
    
    public MenuPopupHelper(final Context context, final MenuBuilder menuBuilder) {
        this(context, menuBuilder, null, false, R.attr.popupMenuStyle, 0);
    }
    
    public MenuPopupHelper(final Context context, final MenuBuilder menuBuilder, final View view) {
        this(context, menuBuilder, view, false, R.attr.popupMenuStyle, 0);
    }
    
    public MenuPopupHelper(final Context context, final MenuBuilder menuBuilder, final View view, final boolean b, final int n) {
        this(context, menuBuilder, view, b, n, 0);
    }
    
    public MenuPopupHelper(final Context mContext, final MenuBuilder mMenu, final View mAnchorView, final boolean mOverflowOnly, final int mPopupStyleAttr, final int mPopupStyleRes) {
        this.mDropDownGravity = 8388611;
        this.mInternalOnDismissListener = (PopupWindow$OnDismissListener)new PopupWindow$OnDismissListener() {
            public void onDismiss() {
                MenuPopupHelper.this.onDismiss();
            }
        };
        this.mContext = mContext;
        this.mMenu = mMenu;
        this.mAnchorView = mAnchorView;
        this.mOverflowOnly = mOverflowOnly;
        this.mPopupStyleAttr = mPopupStyleAttr;
        this.mPopupStyleRes = mPopupStyleRes;
    }
    
    private MenuPopup createPopup() {
        final Display defaultDisplay = ((WindowManager)this.mContext.getSystemService("window")).getDefaultDisplay();
        final Point point = new Point();
        if (Build$VERSION.SDK_INT >= 17) {
            defaultDisplay.getRealSize(point);
        }
        else {
            defaultDisplay.getSize(point);
        }
        MenuPresenter menuPresenter;
        if (Math.min(point.x, point.y) >= this.mContext.getResources().getDimensionPixelSize(R.dimen.abc_cascading_menus_min_smallest_width)) {
            menuPresenter = new CascadingMenuPopup(this.mContext, this.mAnchorView, this.mPopupStyleAttr, this.mPopupStyleRes, this.mOverflowOnly);
        }
        else {
            menuPresenter = new StandardMenuPopup(this.mContext, this.mMenu, this.mAnchorView, this.mPopupStyleAttr, this.mPopupStyleRes, this.mOverflowOnly);
        }
        ((MenuPopup)menuPresenter).addMenu(this.mMenu);
        ((MenuPopup)menuPresenter).setOnDismissListener(this.mInternalOnDismissListener);
        ((MenuPopup)menuPresenter).setAnchorView(this.mAnchorView);
        menuPresenter.setCallback(this.mPresenterCallback);
        ((MenuPopup)menuPresenter).setForceShowIcon(this.mForceShowIcon);
        ((MenuPopup)menuPresenter).setGravity(this.mDropDownGravity);
        return (MenuPopup)menuPresenter;
    }
    
    private void showPopup(int n, final int verticalOffset, final boolean b, final boolean showTitle) {
        final MenuPopup popup = this.getPopup();
        popup.setShowTitle(showTitle);
        if (b) {
            int horizontalOffset = n;
            if ((GravityCompat.getAbsoluteGravity(this.mDropDownGravity, ViewCompat.getLayoutDirection(this.mAnchorView)) & 0x7) == 0x5) {
                horizontalOffset = n - this.mAnchorView.getWidth();
            }
            popup.setHorizontalOffset(horizontalOffset);
            popup.setVerticalOffset(verticalOffset);
            n = (int)(48.0f * this.mContext.getResources().getDisplayMetrics().density / 2.0f);
            popup.setEpicenterBounds(new Rect(horizontalOffset - n, verticalOffset - n, horizontalOffset + n, verticalOffset + n));
        }
        popup.show();
    }
    
    @Override
    public void dismiss() {
        if (this.isShowing()) {
            this.mPopup.dismiss();
        }
    }
    
    public int getGravity() {
        return this.mDropDownGravity;
    }
    
    public ListView getListView() {
        return this.getPopup().getListView();
    }
    
    public MenuPopup getPopup() {
        if (this.mPopup == null) {
            this.mPopup = this.createPopup();
        }
        return this.mPopup;
    }
    
    public boolean isShowing() {
        final MenuPopup mPopup = this.mPopup;
        return mPopup != null && mPopup.isShowing();
    }
    
    protected void onDismiss() {
        this.mPopup = null;
        final PopupWindow$OnDismissListener mOnDismissListener = this.mOnDismissListener;
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss();
        }
    }
    
    public void setAnchorView(final View mAnchorView) {
        this.mAnchorView = mAnchorView;
    }
    
    public void setForceShowIcon(final boolean b) {
        this.mForceShowIcon = b;
        final MenuPopup mPopup = this.mPopup;
        if (mPopup != null) {
            mPopup.setForceShowIcon(b);
        }
    }
    
    public void setGravity(final int mDropDownGravity) {
        this.mDropDownGravity = mDropDownGravity;
    }
    
    public void setOnDismissListener(final PopupWindow$OnDismissListener mOnDismissListener) {
        this.mOnDismissListener = mOnDismissListener;
    }
    
    @Override
    public void setPresenterCallback(final MenuPresenter.Callback callback) {
        this.mPresenterCallback = callback;
        final MenuPopup mPopup = this.mPopup;
        if (mPopup != null) {
            mPopup.setCallback(callback);
        }
    }
    
    public void show() {
        if (this.tryShow()) {
            return;
        }
        throw new IllegalStateException("MenuPopupHelper cannot be used without an anchor");
    }
    
    public void show(final int n, final int n2) {
        if (this.tryShow(n, n2)) {
            return;
        }
        throw new IllegalStateException("MenuPopupHelper cannot be used without an anchor");
    }
    
    public boolean tryShow() {
        if (this.isShowing()) {
            return true;
        }
        if (this.mAnchorView == null) {
            return false;
        }
        this.showPopup(0, 0, false, false);
        return true;
    }
    
    public boolean tryShow(final int n, final int n2) {
        if (this.isShowing()) {
            return true;
        }
        if (this.mAnchorView == null) {
            return false;
        }
        this.showPopup(n, n2, true, true);
        return true;
    }
}
