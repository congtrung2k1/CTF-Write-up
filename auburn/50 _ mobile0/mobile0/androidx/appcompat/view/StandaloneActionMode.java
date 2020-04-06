// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.view;

import androidx.appcompat.view.menu.MenuPopupHelper;
import android.view.MenuItem;
import androidx.appcompat.view.menu.SubMenuBuilder;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.View;
import java.lang.ref.WeakReference;
import androidx.appcompat.widget.ActionBarContextView;
import android.content.Context;
import androidx.appcompat.view.menu.MenuBuilder;

public class StandaloneActionMode extends ActionMode implements MenuBuilder.Callback
{
    private ActionMode.Callback mCallback;
    private Context mContext;
    private ActionBarContextView mContextView;
    private WeakReference<View> mCustomView;
    private boolean mFinished;
    private boolean mFocusable;
    private MenuBuilder mMenu;
    
    public StandaloneActionMode(final Context mContext, final ActionBarContextView mContextView, final ActionMode.Callback mCallback, final boolean mFocusable) {
        this.mContext = mContext;
        this.mContextView = mContextView;
        this.mCallback = mCallback;
        (this.mMenu = new MenuBuilder(mContextView.getContext()).setDefaultShowAsAction(1)).setCallback((MenuBuilder.Callback)this);
        this.mFocusable = mFocusable;
    }
    
    @Override
    public void finish() {
        if (this.mFinished) {
            return;
        }
        this.mFinished = true;
        this.mContextView.sendAccessibilityEvent(32);
        this.mCallback.onDestroyActionMode(this);
    }
    
    @Override
    public View getCustomView() {
        final WeakReference<View> mCustomView = this.mCustomView;
        View view;
        if (mCustomView != null) {
            view = mCustomView.get();
        }
        else {
            view = null;
        }
        return view;
    }
    
    @Override
    public Menu getMenu() {
        return (Menu)this.mMenu;
    }
    
    @Override
    public MenuInflater getMenuInflater() {
        return new SupportMenuInflater(this.mContextView.getContext());
    }
    
    @Override
    public CharSequence getSubtitle() {
        return this.mContextView.getSubtitle();
    }
    
    @Override
    public CharSequence getTitle() {
        return this.mContextView.getTitle();
    }
    
    @Override
    public void invalidate() {
        this.mCallback.onPrepareActionMode(this, (Menu)this.mMenu);
    }
    
    @Override
    public boolean isTitleOptional() {
        return this.mContextView.isTitleOptional();
    }
    
    @Override
    public boolean isUiFocusable() {
        return this.mFocusable;
    }
    
    public void onCloseMenu(final MenuBuilder menuBuilder, final boolean b) {
    }
    
    public void onCloseSubMenu(final SubMenuBuilder subMenuBuilder) {
    }
    
    @Override
    public boolean onMenuItemSelected(final MenuBuilder menuBuilder, final MenuItem menuItem) {
        return this.mCallback.onActionItemClicked(this, menuItem);
    }
    
    @Override
    public void onMenuModeChange(final MenuBuilder menuBuilder) {
        this.invalidate();
        this.mContextView.showOverflowMenu();
    }
    
    public boolean onSubMenuSelected(final SubMenuBuilder subMenuBuilder) {
        if (!subMenuBuilder.hasVisibleItems()) {
            return true;
        }
        new MenuPopupHelper(this.mContextView.getContext(), subMenuBuilder).show();
        return true;
    }
    
    @Override
    public void setCustomView(final View view) {
        this.mContextView.setCustomView(view);
        WeakReference<View> mCustomView;
        if (view != null) {
            mCustomView = new WeakReference<View>(view);
        }
        else {
            mCustomView = null;
        }
        this.mCustomView = mCustomView;
    }
    
    @Override
    public void setSubtitle(final int n) {
        this.setSubtitle(this.mContext.getString(n));
    }
    
    @Override
    public void setSubtitle(final CharSequence subtitle) {
        this.mContextView.setSubtitle(subtitle);
    }
    
    @Override
    public void setTitle(final int n) {
        this.setTitle(this.mContext.getString(n));
    }
    
    @Override
    public void setTitle(final CharSequence title) {
        this.mContextView.setTitle(title);
    }
    
    @Override
    public void setTitleOptionalHint(final boolean b) {
        super.setTitleOptionalHint(b);
        this.mContextView.setTitleOptional(b);
    }
}
