// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.app;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.menu.MenuPopupHelper;
import android.view.MenuItem;
import androidx.appcompat.view.menu.SubMenuBuilder;
import androidx.appcompat.view.SupportMenuInflater;
import android.view.MenuInflater;
import java.lang.ref.WeakReference;
import androidx.appcompat.view.menu.MenuBuilder;
import android.widget.AdapterView$OnItemSelectedListener;
import android.widget.SpinnerAdapter;
import android.view.ViewGroup$LayoutParams;
import android.view.LayoutInflater;
import android.graphics.drawable.Drawable;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentActivity;
import android.view.ViewGroup;
import android.view.Menu;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.content.res.Configuration;
import android.view.ContextThemeWrapper;
import android.util.TypedValue;
import androidx.core.view.ViewPropertyAnimatorCompat;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.appcompat.view.ActionBarPolicy;
import androidx.appcompat.R;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import androidx.core.view.ViewPropertyAnimatorUpdateListener;
import androidx.appcompat.widget.ScrollingTabContainerView;
import java.util.ArrayList;
import androidx.core.view.ViewPropertyAnimatorListener;
import android.app.Dialog;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.DecorToolbar;
import androidx.appcompat.view.ViewPropertyAnimatorCompatSet;
import androidx.appcompat.widget.ActionBarContextView;
import android.content.Context;
import android.view.View;
import androidx.appcompat.widget.ActionBarContainer;
import android.app.Activity;
import android.view.animation.Interpolator;
import androidx.appcompat.widget.ActionBarOverlayLayout;

public class WindowDecorActionBar extends ActionBar implements ActionBarVisibilityCallback
{
    private static final long FADE_IN_DURATION_MS = 200L;
    private static final long FADE_OUT_DURATION_MS = 100L;
    private static final int INVALID_POSITION = -1;
    private static final String TAG = "WindowDecorActionBar";
    private static final Interpolator sHideInterpolator;
    private static final Interpolator sShowInterpolator;
    ActionModeImpl mActionMode;
    private Activity mActivity;
    ActionBarContainer mContainerView;
    boolean mContentAnimations;
    View mContentView;
    Context mContext;
    ActionBarContextView mContextView;
    private int mCurWindowVisibility;
    ViewPropertyAnimatorCompatSet mCurrentShowAnim;
    DecorToolbar mDecorToolbar;
    ActionMode mDeferredDestroyActionMode;
    ActionMode.Callback mDeferredModeDestroyCallback;
    private Dialog mDialog;
    private boolean mDisplayHomeAsUpSet;
    private boolean mHasEmbeddedTabs;
    boolean mHiddenByApp;
    boolean mHiddenBySystem;
    final ViewPropertyAnimatorListener mHideListener;
    boolean mHideOnContentScroll;
    private boolean mLastMenuVisibility;
    private ArrayList<OnMenuVisibilityListener> mMenuVisibilityListeners;
    private boolean mNowShowing;
    ActionBarOverlayLayout mOverlayLayout;
    private int mSavedTabPosition;
    private TabImpl mSelectedTab;
    private boolean mShowHideAnimationEnabled;
    final ViewPropertyAnimatorListener mShowListener;
    private boolean mShowingForMode;
    ScrollingTabContainerView mTabScrollView;
    private ArrayList<TabImpl> mTabs;
    private Context mThemedContext;
    final ViewPropertyAnimatorUpdateListener mUpdateListener;
    
    static {
        sHideInterpolator = (Interpolator)new AccelerateInterpolator();
        sShowInterpolator = (Interpolator)new DecelerateInterpolator();
    }
    
    public WindowDecorActionBar(final Activity mActivity, final boolean b) {
        this.mTabs = new ArrayList<TabImpl>();
        this.mSavedTabPosition = -1;
        this.mMenuVisibilityListeners = new ArrayList<OnMenuVisibilityListener>();
        this.mCurWindowVisibility = 0;
        this.mContentAnimations = true;
        this.mNowShowing = true;
        this.mHideListener = new ViewPropertyAnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(final View view) {
                if (WindowDecorActionBar.this.mContentAnimations && WindowDecorActionBar.this.mContentView != null) {
                    WindowDecorActionBar.this.mContentView.setTranslationY(0.0f);
                    WindowDecorActionBar.this.mContainerView.setTranslationY(0.0f);
                }
                WindowDecorActionBar.this.mContainerView.setVisibility(8);
                WindowDecorActionBar.this.mContainerView.setTransitioning(false);
                WindowDecorActionBar.this.mCurrentShowAnim = null;
                WindowDecorActionBar.this.completeDeferredDestroyActionMode();
                if (WindowDecorActionBar.this.mOverlayLayout != null) {
                    ViewCompat.requestApplyInsets((View)WindowDecorActionBar.this.mOverlayLayout);
                }
            }
        };
        this.mShowListener = new ViewPropertyAnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(final View view) {
                WindowDecorActionBar.this.mCurrentShowAnim = null;
                WindowDecorActionBar.this.mContainerView.requestLayout();
            }
        };
        this.mUpdateListener = new ViewPropertyAnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final View view) {
                ((View)WindowDecorActionBar.this.mContainerView.getParent()).invalidate();
            }
        };
        this.mActivity = mActivity;
        final View decorView = mActivity.getWindow().getDecorView();
        this.init(decorView);
        if (!b) {
            this.mContentView = decorView.findViewById(16908290);
        }
    }
    
    public WindowDecorActionBar(final Dialog mDialog) {
        this.mTabs = new ArrayList<TabImpl>();
        this.mSavedTabPosition = -1;
        this.mMenuVisibilityListeners = new ArrayList<OnMenuVisibilityListener>();
        this.mCurWindowVisibility = 0;
        this.mContentAnimations = true;
        this.mNowShowing = true;
        this.mHideListener = new ViewPropertyAnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(final View view) {
                if (WindowDecorActionBar.this.mContentAnimations && WindowDecorActionBar.this.mContentView != null) {
                    WindowDecorActionBar.this.mContentView.setTranslationY(0.0f);
                    WindowDecorActionBar.this.mContainerView.setTranslationY(0.0f);
                }
                WindowDecorActionBar.this.mContainerView.setVisibility(8);
                WindowDecorActionBar.this.mContainerView.setTransitioning(false);
                WindowDecorActionBar.this.mCurrentShowAnim = null;
                WindowDecorActionBar.this.completeDeferredDestroyActionMode();
                if (WindowDecorActionBar.this.mOverlayLayout != null) {
                    ViewCompat.requestApplyInsets((View)WindowDecorActionBar.this.mOverlayLayout);
                }
            }
        };
        this.mShowListener = new ViewPropertyAnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(final View view) {
                WindowDecorActionBar.this.mCurrentShowAnim = null;
                WindowDecorActionBar.this.mContainerView.requestLayout();
            }
        };
        this.mUpdateListener = new ViewPropertyAnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final View view) {
                ((View)WindowDecorActionBar.this.mContainerView.getParent()).invalidate();
            }
        };
        this.mDialog = mDialog;
        this.init(mDialog.getWindow().getDecorView());
    }
    
    public WindowDecorActionBar(final View view) {
        this.mTabs = new ArrayList<TabImpl>();
        this.mSavedTabPosition = -1;
        this.mMenuVisibilityListeners = new ArrayList<OnMenuVisibilityListener>();
        this.mCurWindowVisibility = 0;
        this.mContentAnimations = true;
        this.mNowShowing = true;
        this.mHideListener = new ViewPropertyAnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(final View view) {
                if (WindowDecorActionBar.this.mContentAnimations && WindowDecorActionBar.this.mContentView != null) {
                    WindowDecorActionBar.this.mContentView.setTranslationY(0.0f);
                    WindowDecorActionBar.this.mContainerView.setTranslationY(0.0f);
                }
                WindowDecorActionBar.this.mContainerView.setVisibility(8);
                WindowDecorActionBar.this.mContainerView.setTransitioning(false);
                WindowDecorActionBar.this.mCurrentShowAnim = null;
                WindowDecorActionBar.this.completeDeferredDestroyActionMode();
                if (WindowDecorActionBar.this.mOverlayLayout != null) {
                    ViewCompat.requestApplyInsets((View)WindowDecorActionBar.this.mOverlayLayout);
                }
            }
        };
        this.mShowListener = new ViewPropertyAnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(final View view) {
                WindowDecorActionBar.this.mCurrentShowAnim = null;
                WindowDecorActionBar.this.mContainerView.requestLayout();
            }
        };
        this.mUpdateListener = new ViewPropertyAnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final View view) {
                ((View)WindowDecorActionBar.this.mContainerView.getParent()).invalidate();
            }
        };
        this.init(view);
    }
    
    static boolean checkShowingFlags(final boolean b, final boolean b2, final boolean b3) {
        return b3 || (!b && !b2);
    }
    
    private void cleanupTabs() {
        if (this.mSelectedTab != null) {
            this.selectTab(null);
        }
        this.mTabs.clear();
        final ScrollingTabContainerView mTabScrollView = this.mTabScrollView;
        if (mTabScrollView != null) {
            mTabScrollView.removeAllTabs();
        }
        this.mSavedTabPosition = -1;
    }
    
    private void configureTab(final Tab tab, int i) {
        final TabImpl element = (TabImpl)tab;
        if (element.getCallback() != null) {
            element.setPosition(i);
            this.mTabs.add(i, element);
            final int size = this.mTabs.size();
            ++i;
            while (i < size) {
                this.mTabs.get(i).setPosition(i);
                ++i;
            }
            return;
        }
        throw new IllegalStateException("Action Bar Tab must have a Callback");
    }
    
    private void ensureTabsExist() {
        if (this.mTabScrollView != null) {
            return;
        }
        final ScrollingTabContainerView mTabScrollView = new ScrollingTabContainerView(this.mContext);
        if (this.mHasEmbeddedTabs) {
            mTabScrollView.setVisibility(0);
            this.mDecorToolbar.setEmbeddedTabView(mTabScrollView);
        }
        else {
            if (this.getNavigationMode() == 2) {
                mTabScrollView.setVisibility(0);
                final ActionBarOverlayLayout mOverlayLayout = this.mOverlayLayout;
                if (mOverlayLayout != null) {
                    ViewCompat.requestApplyInsets((View)mOverlayLayout);
                }
            }
            else {
                mTabScrollView.setVisibility(8);
            }
            this.mContainerView.setTabContainer(mTabScrollView);
        }
        this.mTabScrollView = mTabScrollView;
    }
    
    private DecorToolbar getDecorToolbar(final View view) {
        if (view instanceof DecorToolbar) {
            return (DecorToolbar)view;
        }
        if (view instanceof Toolbar) {
            return ((Toolbar)view).getWrapper();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Can't make a decor toolbar out of ");
        String simpleName;
        if (view != null) {
            simpleName = view.getClass().getSimpleName();
        }
        else {
            simpleName = "null";
        }
        sb.append(simpleName);
        throw new IllegalStateException(sb.toString());
    }
    
    private void hideForActionMode() {
        if (this.mShowingForMode) {
            this.mShowingForMode = false;
            final ActionBarOverlayLayout mOverlayLayout = this.mOverlayLayout;
            if (mOverlayLayout != null) {
                mOverlayLayout.setShowingForActionMode(false);
            }
            this.updateVisibility(false);
        }
    }
    
    private void init(final View view) {
        final ActionBarOverlayLayout mOverlayLayout = (ActionBarOverlayLayout)view.findViewById(R.id.decor_content_parent);
        this.mOverlayLayout = mOverlayLayout;
        if (mOverlayLayout != null) {
            mOverlayLayout.setActionBarVisibilityCallback((ActionBarOverlayLayout.ActionBarVisibilityCallback)this);
        }
        this.mDecorToolbar = this.getDecorToolbar(view.findViewById(R.id.action_bar));
        this.mContextView = (ActionBarContextView)view.findViewById(R.id.action_context_bar);
        final ActionBarContainer mContainerView = (ActionBarContainer)view.findViewById(R.id.action_bar_container);
        this.mContainerView = mContainerView;
        final DecorToolbar mDecorToolbar = this.mDecorToolbar;
        if (mDecorToolbar != null && this.mContextView != null && mContainerView != null) {
            this.mContext = mDecorToolbar.getContext();
            final boolean b = (this.mDecorToolbar.getDisplayOptions() & 0x4) != 0x0;
            if (b) {
                this.mDisplayHomeAsUpSet = true;
            }
            final ActionBarPolicy value = ActionBarPolicy.get(this.mContext);
            this.setHomeButtonEnabled(value.enableHomeButtonByDefault() || b);
            this.setHasEmbeddedTabs(value.hasEmbeddedTabs());
            final TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes((AttributeSet)null, R.styleable.ActionBar, R.attr.actionBarStyle, 0);
            if (obtainStyledAttributes.getBoolean(R.styleable.ActionBar_hideOnContentScroll, false)) {
                this.setHideOnContentScrollEnabled(true);
            }
            final int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.ActionBar_elevation, 0);
            if (dimensionPixelSize != 0) {
                this.setElevation((float)dimensionPixelSize);
            }
            obtainStyledAttributes.recycle();
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName());
        sb.append(" can only be used ");
        sb.append("with a compatible window decor layout");
        throw new IllegalStateException(sb.toString());
    }
    
    private void setHasEmbeddedTabs(final boolean mHasEmbeddedTabs) {
        if (!(this.mHasEmbeddedTabs = mHasEmbeddedTabs)) {
            this.mDecorToolbar.setEmbeddedTabView(null);
            this.mContainerView.setTabContainer(this.mTabScrollView);
        }
        else {
            this.mContainerView.setTabContainer(null);
            this.mDecorToolbar.setEmbeddedTabView(this.mTabScrollView);
        }
        final int navigationMode = this.getNavigationMode();
        final boolean b = true;
        final boolean b2 = navigationMode == 2;
        final ScrollingTabContainerView mTabScrollView = this.mTabScrollView;
        if (mTabScrollView != null) {
            if (b2) {
                mTabScrollView.setVisibility(0);
                final ActionBarOverlayLayout mOverlayLayout = this.mOverlayLayout;
                if (mOverlayLayout != null) {
                    ViewCompat.requestApplyInsets((View)mOverlayLayout);
                }
            }
            else {
                mTabScrollView.setVisibility(8);
            }
        }
        this.mDecorToolbar.setCollapsible(!this.mHasEmbeddedTabs && b2);
        this.mOverlayLayout.setHasNonEmbeddedTabs(!this.mHasEmbeddedTabs && b2 && b);
    }
    
    private boolean shouldAnimateContextView() {
        return ViewCompat.isLaidOut((View)this.mContainerView);
    }
    
    private void showForActionMode() {
        if (!this.mShowingForMode) {
            this.mShowingForMode = true;
            final ActionBarOverlayLayout mOverlayLayout = this.mOverlayLayout;
            if (mOverlayLayout != null) {
                mOverlayLayout.setShowingForActionMode(true);
            }
            this.updateVisibility(false);
        }
    }
    
    private void updateVisibility(final boolean b) {
        if (checkShowingFlags(this.mHiddenByApp, this.mHiddenBySystem, this.mShowingForMode)) {
            if (!this.mNowShowing) {
                this.mNowShowing = true;
                this.doShow(b);
            }
        }
        else if (this.mNowShowing) {
            this.mNowShowing = false;
            this.doHide(b);
        }
    }
    
    @Override
    public void addOnMenuVisibilityListener(final OnMenuVisibilityListener e) {
        this.mMenuVisibilityListeners.add(e);
    }
    
    @Override
    public void addTab(final Tab tab) {
        this.addTab(tab, this.mTabs.isEmpty());
    }
    
    @Override
    public void addTab(final Tab tab, final int n) {
        this.addTab(tab, n, this.mTabs.isEmpty());
    }
    
    @Override
    public void addTab(final Tab tab, final int n, final boolean b) {
        this.ensureTabsExist();
        this.mTabScrollView.addTab(tab, n, b);
        this.configureTab(tab, n);
        if (b) {
            this.selectTab(tab);
        }
    }
    
    @Override
    public void addTab(final Tab tab, final boolean b) {
        this.ensureTabsExist();
        this.mTabScrollView.addTab(tab, b);
        this.configureTab(tab, this.mTabs.size());
        if (b) {
            this.selectTab(tab);
        }
    }
    
    public void animateToMode(final boolean b) {
        if (b) {
            this.showForActionMode();
        }
        else {
            this.hideForActionMode();
        }
        if (this.shouldAnimateContextView()) {
            ViewPropertyAnimatorCompat viewPropertyAnimatorCompat;
            ViewPropertyAnimatorCompat viewPropertyAnimatorCompat2;
            if (b) {
                viewPropertyAnimatorCompat = this.mDecorToolbar.setupAnimatorToVisibility(4, 100L);
                viewPropertyAnimatorCompat2 = this.mContextView.setupAnimatorToVisibility(0, 200L);
            }
            else {
                viewPropertyAnimatorCompat2 = this.mDecorToolbar.setupAnimatorToVisibility(0, 200L);
                viewPropertyAnimatorCompat = this.mContextView.setupAnimatorToVisibility(8, 100L);
            }
            final ViewPropertyAnimatorCompatSet set = new ViewPropertyAnimatorCompatSet();
            set.playSequentially(viewPropertyAnimatorCompat, viewPropertyAnimatorCompat2);
            set.start();
        }
        else if (b) {
            this.mDecorToolbar.setVisibility(4);
            this.mContextView.setVisibility(0);
        }
        else {
            this.mDecorToolbar.setVisibility(0);
            this.mContextView.setVisibility(8);
        }
    }
    
    @Override
    public boolean collapseActionView() {
        final DecorToolbar mDecorToolbar = this.mDecorToolbar;
        if (mDecorToolbar != null && mDecorToolbar.hasExpandedActionView()) {
            this.mDecorToolbar.collapseActionView();
            return true;
        }
        return false;
    }
    
    void completeDeferredDestroyActionMode() {
        final ActionMode.Callback mDeferredModeDestroyCallback = this.mDeferredModeDestroyCallback;
        if (mDeferredModeDestroyCallback != null) {
            mDeferredModeDestroyCallback.onDestroyActionMode(this.mDeferredDestroyActionMode);
            this.mDeferredDestroyActionMode = null;
            this.mDeferredModeDestroyCallback = null;
        }
    }
    
    @Override
    public void dispatchMenuVisibilityChanged(final boolean mLastMenuVisibility) {
        if (mLastMenuVisibility == this.mLastMenuVisibility) {
            return;
        }
        this.mLastMenuVisibility = mLastMenuVisibility;
        for (int size = this.mMenuVisibilityListeners.size(), i = 0; i < size; ++i) {
            this.mMenuVisibilityListeners.get(i).onMenuVisibilityChanged(mLastMenuVisibility);
        }
    }
    
    public void doHide(final boolean b) {
        final ViewPropertyAnimatorCompatSet mCurrentShowAnim = this.mCurrentShowAnim;
        if (mCurrentShowAnim != null) {
            mCurrentShowAnim.cancel();
        }
        if (this.mCurWindowVisibility == 0 && (this.mShowHideAnimationEnabled || b)) {
            this.mContainerView.setAlpha(1.0f);
            this.mContainerView.setTransitioning(true);
            final ViewPropertyAnimatorCompatSet mCurrentShowAnim2 = new ViewPropertyAnimatorCompatSet();
            float n2;
            final float n = n2 = (float)(-this.mContainerView.getHeight());
            if (b) {
                final int[] array2;
                final int[] array = array2 = new int[2];
                array2[1] = (array2[0] = 0);
                this.mContainerView.getLocationInWindow(array);
                n2 = n - array[1];
            }
            final ViewPropertyAnimatorCompat translationY = ViewCompat.animate((View)this.mContainerView).translationY(n2);
            translationY.setUpdateListener(this.mUpdateListener);
            mCurrentShowAnim2.play(translationY);
            if (this.mContentAnimations) {
                final View mContentView = this.mContentView;
                if (mContentView != null) {
                    mCurrentShowAnim2.play(ViewCompat.animate(mContentView).translationY(n2));
                }
            }
            mCurrentShowAnim2.setInterpolator(WindowDecorActionBar.sHideInterpolator);
            mCurrentShowAnim2.setDuration(250L);
            mCurrentShowAnim2.setListener(this.mHideListener);
            (this.mCurrentShowAnim = mCurrentShowAnim2).start();
        }
        else {
            this.mHideListener.onAnimationEnd(null);
        }
    }
    
    public void doShow(final boolean b) {
        final ViewPropertyAnimatorCompatSet mCurrentShowAnim = this.mCurrentShowAnim;
        if (mCurrentShowAnim != null) {
            mCurrentShowAnim.cancel();
        }
        this.mContainerView.setVisibility(0);
        if (this.mCurWindowVisibility == 0 && (this.mShowHideAnimationEnabled || b)) {
            this.mContainerView.setTranslationY(0.0f);
            float n2;
            final float n = n2 = (float)(-this.mContainerView.getHeight());
            if (b) {
                final int[] array2;
                final int[] array = array2 = new int[2];
                array2[1] = (array2[0] = 0);
                this.mContainerView.getLocationInWindow(array);
                n2 = n - array[1];
            }
            this.mContainerView.setTranslationY(n2);
            final ViewPropertyAnimatorCompatSet mCurrentShowAnim2 = new ViewPropertyAnimatorCompatSet();
            final ViewPropertyAnimatorCompat translationY = ViewCompat.animate((View)this.mContainerView).translationY(0.0f);
            translationY.setUpdateListener(this.mUpdateListener);
            mCurrentShowAnim2.play(translationY);
            if (this.mContentAnimations) {
                final View mContentView = this.mContentView;
                if (mContentView != null) {
                    mContentView.setTranslationY(n2);
                    mCurrentShowAnim2.play(ViewCompat.animate(this.mContentView).translationY(0.0f));
                }
            }
            mCurrentShowAnim2.setInterpolator(WindowDecorActionBar.sShowInterpolator);
            mCurrentShowAnim2.setDuration(250L);
            mCurrentShowAnim2.setListener(this.mShowListener);
            (this.mCurrentShowAnim = mCurrentShowAnim2).start();
        }
        else {
            this.mContainerView.setAlpha(1.0f);
            this.mContainerView.setTranslationY(0.0f);
            if (this.mContentAnimations) {
                final View mContentView2 = this.mContentView;
                if (mContentView2 != null) {
                    mContentView2.setTranslationY(0.0f);
                }
            }
            this.mShowListener.onAnimationEnd(null);
        }
        final ActionBarOverlayLayout mOverlayLayout = this.mOverlayLayout;
        if (mOverlayLayout != null) {
            ViewCompat.requestApplyInsets((View)mOverlayLayout);
        }
    }
    
    @Override
    public void enableContentAnimations(final boolean mContentAnimations) {
        this.mContentAnimations = mContentAnimations;
    }
    
    @Override
    public View getCustomView() {
        return this.mDecorToolbar.getCustomView();
    }
    
    @Override
    public int getDisplayOptions() {
        return this.mDecorToolbar.getDisplayOptions();
    }
    
    @Override
    public float getElevation() {
        return ViewCompat.getElevation((View)this.mContainerView);
    }
    
    @Override
    public int getHeight() {
        return this.mContainerView.getHeight();
    }
    
    @Override
    public int getHideOffset() {
        return this.mOverlayLayout.getActionBarHideOffset();
    }
    
    @Override
    public int getNavigationItemCount() {
        final int navigationMode = this.mDecorToolbar.getNavigationMode();
        if (navigationMode == 1) {
            return this.mDecorToolbar.getDropdownItemCount();
        }
        if (navigationMode != 2) {
            return 0;
        }
        return this.mTabs.size();
    }
    
    @Override
    public int getNavigationMode() {
        return this.mDecorToolbar.getNavigationMode();
    }
    
    @Override
    public int getSelectedNavigationIndex() {
        final int navigationMode = this.mDecorToolbar.getNavigationMode();
        if (navigationMode == 1) {
            return this.mDecorToolbar.getDropdownSelectedPosition();
        }
        int position = -1;
        if (navigationMode != 2) {
            return -1;
        }
        final TabImpl mSelectedTab = this.mSelectedTab;
        if (mSelectedTab != null) {
            position = mSelectedTab.getPosition();
        }
        return position;
    }
    
    @Override
    public Tab getSelectedTab() {
        return this.mSelectedTab;
    }
    
    @Override
    public CharSequence getSubtitle() {
        return this.mDecorToolbar.getSubtitle();
    }
    
    @Override
    public Tab getTabAt(final int index) {
        return this.mTabs.get(index);
    }
    
    @Override
    public int getTabCount() {
        return this.mTabs.size();
    }
    
    @Override
    public Context getThemedContext() {
        if (this.mThemedContext == null) {
            final TypedValue typedValue = new TypedValue();
            this.mContext.getTheme().resolveAttribute(R.attr.actionBarWidgetTheme, typedValue, true);
            final int resourceId = typedValue.resourceId;
            if (resourceId != 0) {
                this.mThemedContext = (Context)new ContextThemeWrapper(this.mContext, resourceId);
            }
            else {
                this.mThemedContext = this.mContext;
            }
        }
        return this.mThemedContext;
    }
    
    @Override
    public CharSequence getTitle() {
        return this.mDecorToolbar.getTitle();
    }
    
    public boolean hasIcon() {
        return this.mDecorToolbar.hasIcon();
    }
    
    public boolean hasLogo() {
        return this.mDecorToolbar.hasLogo();
    }
    
    @Override
    public void hide() {
        if (!this.mHiddenByApp) {
            this.mHiddenByApp = true;
            this.updateVisibility(false);
        }
    }
    
    @Override
    public void hideForSystem() {
        if (!this.mHiddenBySystem) {
            this.updateVisibility(this.mHiddenBySystem = true);
        }
    }
    
    @Override
    public boolean isHideOnContentScrollEnabled() {
        return this.mOverlayLayout.isHideOnContentScrollEnabled();
    }
    
    @Override
    public boolean isShowing() {
        final int height = this.getHeight();
        return this.mNowShowing && (height == 0 || this.getHideOffset() < height);
    }
    
    @Override
    public boolean isTitleTruncated() {
        final DecorToolbar mDecorToolbar = this.mDecorToolbar;
        return mDecorToolbar != null && mDecorToolbar.isTitleTruncated();
    }
    
    @Override
    public Tab newTab() {
        return new TabImpl();
    }
    
    @Override
    public void onConfigurationChanged(final Configuration configuration) {
        this.setHasEmbeddedTabs(ActionBarPolicy.get(this.mContext).hasEmbeddedTabs());
    }
    
    @Override
    public void onContentScrollStarted() {
        final ViewPropertyAnimatorCompatSet mCurrentShowAnim = this.mCurrentShowAnim;
        if (mCurrentShowAnim != null) {
            mCurrentShowAnim.cancel();
            this.mCurrentShowAnim = null;
        }
    }
    
    @Override
    public void onContentScrollStopped() {
    }
    
    @Override
    public boolean onKeyShortcut(final int n, final KeyEvent keyEvent) {
        final ActionModeImpl mActionMode = this.mActionMode;
        if (mActionMode == null) {
            return false;
        }
        final Menu menu = mActionMode.getMenu();
        if (menu != null) {
            int deviceId;
            if (keyEvent != null) {
                deviceId = keyEvent.getDeviceId();
            }
            else {
                deviceId = -1;
            }
            final int keyboardType = KeyCharacterMap.load(deviceId).getKeyboardType();
            boolean qwertyMode = true;
            if (keyboardType == 1) {
                qwertyMode = false;
            }
            menu.setQwertyMode(qwertyMode);
            return menu.performShortcut(n, keyEvent, 0);
        }
        return false;
    }
    
    @Override
    public void onWindowVisibilityChanged(final int mCurWindowVisibility) {
        this.mCurWindowVisibility = mCurWindowVisibility;
    }
    
    @Override
    public void removeAllTabs() {
        this.cleanupTabs();
    }
    
    @Override
    public void removeOnMenuVisibilityListener(final OnMenuVisibilityListener o) {
        this.mMenuVisibilityListeners.remove(o);
    }
    
    @Override
    public void removeTab(final Tab tab) {
        this.removeTabAt(tab.getPosition());
    }
    
    @Override
    public void removeTabAt(final int index) {
        if (this.mTabScrollView == null) {
            return;
        }
        final TabImpl mSelectedTab = this.mSelectedTab;
        int n;
        if (mSelectedTab != null) {
            n = mSelectedTab.getPosition();
        }
        else {
            n = this.mSavedTabPosition;
        }
        this.mTabScrollView.removeTabAt(index);
        final TabImpl tabImpl = this.mTabs.remove(index);
        if (tabImpl != null) {
            tabImpl.setPosition(-1);
        }
        for (int size = this.mTabs.size(), i = index; i < size; ++i) {
            this.mTabs.get(i).setPosition(i);
        }
        if (n == index) {
            Tab tab;
            if (this.mTabs.isEmpty()) {
                tab = null;
            }
            else {
                tab = this.mTabs.get(Math.max(0, index - 1));
            }
            this.selectTab(tab);
        }
    }
    
    public boolean requestFocus() {
        final ViewGroup viewGroup = this.mDecorToolbar.getViewGroup();
        if (viewGroup != null && !viewGroup.hasFocus()) {
            viewGroup.requestFocus();
            return true;
        }
        return false;
    }
    
    @Override
    public void selectTab(final Tab tab) {
        final int navigationMode = this.getNavigationMode();
        int n = -1;
        if (navigationMode != 2) {
            if (tab != null) {
                n = tab.getPosition();
            }
            this.mSavedTabPosition = n;
            return;
        }
        FragmentTransaction disallowAddToBackStack;
        if (this.mActivity instanceof FragmentActivity && !this.mDecorToolbar.getViewGroup().isInEditMode()) {
            disallowAddToBackStack = ((FragmentActivity)this.mActivity).getSupportFragmentManager().beginTransaction().disallowAddToBackStack();
        }
        else {
            disallowAddToBackStack = null;
        }
        final TabImpl mSelectedTab = this.mSelectedTab;
        if (mSelectedTab == tab) {
            if (mSelectedTab != null) {
                mSelectedTab.getCallback().onTabReselected(this.mSelectedTab, disallowAddToBackStack);
                this.mTabScrollView.animateToTab(tab.getPosition());
            }
        }
        else {
            final ScrollingTabContainerView mTabScrollView = this.mTabScrollView;
            if (tab != null) {
                n = tab.getPosition();
            }
            mTabScrollView.setTabSelected(n);
            final TabImpl mSelectedTab2 = this.mSelectedTab;
            if (mSelectedTab2 != null) {
                mSelectedTab2.getCallback().onTabUnselected(this.mSelectedTab, disallowAddToBackStack);
            }
            final TabImpl mSelectedTab3 = (TabImpl)tab;
            if ((this.mSelectedTab = mSelectedTab3) != null) {
                mSelectedTab3.getCallback().onTabSelected(this.mSelectedTab, disallowAddToBackStack);
            }
        }
        if (disallowAddToBackStack != null && !disallowAddToBackStack.isEmpty()) {
            disallowAddToBackStack.commit();
        }
    }
    
    @Override
    public void setBackgroundDrawable(final Drawable primaryBackground) {
        this.mContainerView.setPrimaryBackground(primaryBackground);
    }
    
    @Override
    public void setCustomView(final int n) {
        this.setCustomView(LayoutInflater.from(this.getThemedContext()).inflate(n, this.mDecorToolbar.getViewGroup(), false));
    }
    
    @Override
    public void setCustomView(final View customView) {
        this.mDecorToolbar.setCustomView(customView);
    }
    
    @Override
    public void setCustomView(final View customView, final ActionBar.LayoutParams layoutParams) {
        customView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        this.mDecorToolbar.setCustomView(customView);
    }
    
    @Override
    public void setDefaultDisplayHomeAsUpEnabled(final boolean displayHomeAsUpEnabled) {
        if (!this.mDisplayHomeAsUpSet) {
            this.setDisplayHomeAsUpEnabled(displayHomeAsUpEnabled);
        }
    }
    
    @Override
    public void setDisplayHomeAsUpEnabled(final boolean b) {
        int n;
        if (b) {
            n = 4;
        }
        else {
            n = 0;
        }
        this.setDisplayOptions(n, 4);
    }
    
    @Override
    public void setDisplayOptions(final int displayOptions) {
        if ((displayOptions & 0x4) != 0x0) {
            this.mDisplayHomeAsUpSet = true;
        }
        this.mDecorToolbar.setDisplayOptions(displayOptions);
    }
    
    @Override
    public void setDisplayOptions(final int n, final int n2) {
        final int displayOptions = this.mDecorToolbar.getDisplayOptions();
        if ((n2 & 0x4) != 0x0) {
            this.mDisplayHomeAsUpSet = true;
        }
        this.mDecorToolbar.setDisplayOptions((n & n2) | (~n2 & displayOptions));
    }
    
    @Override
    public void setDisplayShowCustomEnabled(final boolean b) {
        int n;
        if (b) {
            n = 16;
        }
        else {
            n = 0;
        }
        this.setDisplayOptions(n, 16);
    }
    
    @Override
    public void setDisplayShowHomeEnabled(final boolean b) {
        int n;
        if (b) {
            n = 2;
        }
        else {
            n = 0;
        }
        this.setDisplayOptions(n, 2);
    }
    
    @Override
    public void setDisplayShowTitleEnabled(final boolean b) {
        int n;
        if (b) {
            n = 8;
        }
        else {
            n = 0;
        }
        this.setDisplayOptions(n, 8);
    }
    
    @Override
    public void setDisplayUseLogoEnabled(final boolean b) {
        this.setDisplayOptions(b ? 1 : 0, 1);
    }
    
    @Override
    public void setElevation(final float n) {
        ViewCompat.setElevation((View)this.mContainerView, n);
    }
    
    @Override
    public void setHideOffset(final int actionBarHideOffset) {
        if (actionBarHideOffset != 0 && !this.mOverlayLayout.isInOverlayMode()) {
            throw new IllegalStateException("Action bar must be in overlay mode (Window.FEATURE_OVERLAY_ACTION_BAR) to set a non-zero hide offset");
        }
        this.mOverlayLayout.setActionBarHideOffset(actionBarHideOffset);
    }
    
    @Override
    public void setHideOnContentScrollEnabled(final boolean b) {
        if (b && !this.mOverlayLayout.isInOverlayMode()) {
            throw new IllegalStateException("Action bar must be in overlay mode (Window.FEATURE_OVERLAY_ACTION_BAR) to enable hide on content scroll");
        }
        this.mHideOnContentScroll = b;
        this.mOverlayLayout.setHideOnContentScrollEnabled(b);
    }
    
    @Override
    public void setHomeActionContentDescription(final int navigationContentDescription) {
        this.mDecorToolbar.setNavigationContentDescription(navigationContentDescription);
    }
    
    @Override
    public void setHomeActionContentDescription(final CharSequence navigationContentDescription) {
        this.mDecorToolbar.setNavigationContentDescription(navigationContentDescription);
    }
    
    @Override
    public void setHomeAsUpIndicator(final int navigationIcon) {
        this.mDecorToolbar.setNavigationIcon(navigationIcon);
    }
    
    @Override
    public void setHomeAsUpIndicator(final Drawable navigationIcon) {
        this.mDecorToolbar.setNavigationIcon(navigationIcon);
    }
    
    @Override
    public void setHomeButtonEnabled(final boolean homeButtonEnabled) {
        this.mDecorToolbar.setHomeButtonEnabled(homeButtonEnabled);
    }
    
    @Override
    public void setIcon(final int icon) {
        this.mDecorToolbar.setIcon(icon);
    }
    
    @Override
    public void setIcon(final Drawable icon) {
        this.mDecorToolbar.setIcon(icon);
    }
    
    @Override
    public void setListNavigationCallbacks(final SpinnerAdapter spinnerAdapter, final OnNavigationListener onNavigationListener) {
        this.mDecorToolbar.setDropdownParams(spinnerAdapter, (AdapterView$OnItemSelectedListener)new NavItemSelectedListener(onNavigationListener));
    }
    
    @Override
    public void setLogo(final int logo) {
        this.mDecorToolbar.setLogo(logo);
    }
    
    @Override
    public void setLogo(final Drawable logo) {
        this.mDecorToolbar.setLogo(logo);
    }
    
    @Override
    public void setNavigationMode(final int navigationMode) {
        final int navigationMode2 = this.mDecorToolbar.getNavigationMode();
        if (navigationMode2 == 2) {
            this.mSavedTabPosition = this.getSelectedNavigationIndex();
            this.selectTab(null);
            this.mTabScrollView.setVisibility(8);
        }
        if (navigationMode2 != navigationMode && !this.mHasEmbeddedTabs) {
            final ActionBarOverlayLayout mOverlayLayout = this.mOverlayLayout;
            if (mOverlayLayout != null) {
                ViewCompat.requestApplyInsets((View)mOverlayLayout);
            }
        }
        this.mDecorToolbar.setNavigationMode(navigationMode);
        final boolean b = false;
        if (navigationMode == 2) {
            this.ensureTabsExist();
            this.mTabScrollView.setVisibility(0);
            final int mSavedTabPosition = this.mSavedTabPosition;
            if (mSavedTabPosition != -1) {
                this.setSelectedNavigationItem(mSavedTabPosition);
                this.mSavedTabPosition = -1;
            }
        }
        this.mDecorToolbar.setCollapsible(navigationMode == 2 && !this.mHasEmbeddedTabs);
        final ActionBarOverlayLayout mOverlayLayout2 = this.mOverlayLayout;
        boolean hasNonEmbeddedTabs = b;
        if (navigationMode == 2) {
            hasNonEmbeddedTabs = b;
            if (!this.mHasEmbeddedTabs) {
                hasNonEmbeddedTabs = true;
            }
        }
        mOverlayLayout2.setHasNonEmbeddedTabs(hasNonEmbeddedTabs);
    }
    
    @Override
    public void setSelectedNavigationItem(final int n) {
        final int navigationMode = this.mDecorToolbar.getNavigationMode();
        if (navigationMode != 1) {
            if (navigationMode != 2) {
                throw new IllegalStateException("setSelectedNavigationIndex not valid for current navigation mode");
            }
            this.selectTab(this.mTabs.get(n));
        }
        else {
            this.mDecorToolbar.setDropdownSelectedPosition(n);
        }
    }
    
    @Override
    public void setShowHideAnimationEnabled(final boolean mShowHideAnimationEnabled) {
        if (!(this.mShowHideAnimationEnabled = mShowHideAnimationEnabled)) {
            final ViewPropertyAnimatorCompatSet mCurrentShowAnim = this.mCurrentShowAnim;
            if (mCurrentShowAnim != null) {
                mCurrentShowAnim.cancel();
            }
        }
    }
    
    @Override
    public void setSplitBackgroundDrawable(final Drawable drawable) {
    }
    
    @Override
    public void setStackedBackgroundDrawable(final Drawable stackedBackground) {
        this.mContainerView.setStackedBackground(stackedBackground);
    }
    
    @Override
    public void setSubtitle(final int n) {
        this.setSubtitle(this.mContext.getString(n));
    }
    
    @Override
    public void setSubtitle(final CharSequence subtitle) {
        this.mDecorToolbar.setSubtitle(subtitle);
    }
    
    @Override
    public void setTitle(final int n) {
        this.setTitle(this.mContext.getString(n));
    }
    
    @Override
    public void setTitle(final CharSequence title) {
        this.mDecorToolbar.setTitle(title);
    }
    
    @Override
    public void setWindowTitle(final CharSequence windowTitle) {
        this.mDecorToolbar.setWindowTitle(windowTitle);
    }
    
    @Override
    public void show() {
        if (this.mHiddenByApp) {
            this.updateVisibility(this.mHiddenByApp = false);
        }
    }
    
    @Override
    public void showForSystem() {
        if (this.mHiddenBySystem) {
            this.mHiddenBySystem = false;
            this.updateVisibility(true);
        }
    }
    
    @Override
    public ActionMode startActionMode(final ActionMode.Callback callback) {
        final ActionModeImpl mActionMode = this.mActionMode;
        if (mActionMode != null) {
            mActionMode.finish();
        }
        this.mOverlayLayout.setHideOnContentScrollEnabled(false);
        this.mContextView.killMode();
        final ActionModeImpl mActionMode2 = new ActionModeImpl(this.mContextView.getContext(), callback);
        if (mActionMode2.dispatchOnCreate()) {
            (this.mActionMode = mActionMode2).invalidate();
            this.mContextView.initForMode(mActionMode2);
            this.animateToMode(true);
            this.mContextView.sendAccessibilityEvent(32);
            return mActionMode2;
        }
        return null;
    }
    
    public class ActionModeImpl extends ActionMode implements MenuBuilder.Callback
    {
        private final Context mActionModeContext;
        private ActionMode.Callback mCallback;
        private WeakReference<View> mCustomView;
        private final MenuBuilder mMenu;
        
        public ActionModeImpl(final Context mActionModeContext, final ActionMode.Callback mCallback) {
            this.mActionModeContext = mActionModeContext;
            this.mCallback = mCallback;
            (this.mMenu = new MenuBuilder(mActionModeContext).setDefaultShowAsAction(1)).setCallback((MenuBuilder.Callback)this);
        }
        
        public boolean dispatchOnCreate() {
            this.mMenu.stopDispatchingItemsChanged();
            try {
                return this.mCallback.onCreateActionMode(this, (Menu)this.mMenu);
            }
            finally {
                this.mMenu.startDispatchingItemsChanged();
            }
        }
        
        @Override
        public void finish() {
            if (WindowDecorActionBar.this.mActionMode != this) {
                return;
            }
            if (!WindowDecorActionBar.checkShowingFlags(WindowDecorActionBar.this.mHiddenByApp, WindowDecorActionBar.this.mHiddenBySystem, false)) {
                WindowDecorActionBar.this.mDeferredDestroyActionMode = this;
                WindowDecorActionBar.this.mDeferredModeDestroyCallback = this.mCallback;
            }
            else {
                this.mCallback.onDestroyActionMode(this);
            }
            this.mCallback = null;
            WindowDecorActionBar.this.animateToMode(false);
            WindowDecorActionBar.this.mContextView.closeMode();
            WindowDecorActionBar.this.mDecorToolbar.getViewGroup().sendAccessibilityEvent(32);
            WindowDecorActionBar.this.mOverlayLayout.setHideOnContentScrollEnabled(WindowDecorActionBar.this.mHideOnContentScroll);
            WindowDecorActionBar.this.mActionMode = null;
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
            return new SupportMenuInflater(this.mActionModeContext);
        }
        
        @Override
        public CharSequence getSubtitle() {
            return WindowDecorActionBar.this.mContextView.getSubtitle();
        }
        
        @Override
        public CharSequence getTitle() {
            return WindowDecorActionBar.this.mContextView.getTitle();
        }
        
        @Override
        public void invalidate() {
            if (WindowDecorActionBar.this.mActionMode != this) {
                return;
            }
            this.mMenu.stopDispatchingItemsChanged();
            try {
                this.mCallback.onPrepareActionMode(this, (Menu)this.mMenu);
            }
            finally {
                this.mMenu.startDispatchingItemsChanged();
            }
        }
        
        @Override
        public boolean isTitleOptional() {
            return WindowDecorActionBar.this.mContextView.isTitleOptional();
        }
        
        public void onCloseMenu(final MenuBuilder menuBuilder, final boolean b) {
        }
        
        public void onCloseSubMenu(final SubMenuBuilder subMenuBuilder) {
        }
        
        @Override
        public boolean onMenuItemSelected(final MenuBuilder menuBuilder, final MenuItem menuItem) {
            final ActionMode.Callback mCallback = this.mCallback;
            return mCallback != null && mCallback.onActionItemClicked(this, menuItem);
        }
        
        @Override
        public void onMenuModeChange(final MenuBuilder menuBuilder) {
            if (this.mCallback == null) {
                return;
            }
            this.invalidate();
            WindowDecorActionBar.this.mContextView.showOverflowMenu();
        }
        
        public boolean onSubMenuSelected(final SubMenuBuilder subMenuBuilder) {
            if (this.mCallback == null) {
                return false;
            }
            if (!subMenuBuilder.hasVisibleItems()) {
                return true;
            }
            new MenuPopupHelper(WindowDecorActionBar.this.getThemedContext(), subMenuBuilder).show();
            return true;
        }
        
        @Override
        public void setCustomView(final View view) {
            WindowDecorActionBar.this.mContextView.setCustomView(view);
            this.mCustomView = new WeakReference<View>(view);
        }
        
        @Override
        public void setSubtitle(final int n) {
            this.setSubtitle(WindowDecorActionBar.this.mContext.getResources().getString(n));
        }
        
        @Override
        public void setSubtitle(final CharSequence subtitle) {
            WindowDecorActionBar.this.mContextView.setSubtitle(subtitle);
        }
        
        @Override
        public void setTitle(final int n) {
            this.setTitle(WindowDecorActionBar.this.mContext.getResources().getString(n));
        }
        
        @Override
        public void setTitle(final CharSequence title) {
            WindowDecorActionBar.this.mContextView.setTitle(title);
        }
        
        @Override
        public void setTitleOptionalHint(final boolean b) {
            super.setTitleOptionalHint(b);
            WindowDecorActionBar.this.mContextView.setTitleOptional(b);
        }
    }
    
    public class TabImpl extends Tab
    {
        private TabListener mCallback;
        private CharSequence mContentDesc;
        private View mCustomView;
        private Drawable mIcon;
        private int mPosition;
        private Object mTag;
        private CharSequence mText;
        
        public TabImpl() {
            this.mPosition = -1;
        }
        
        public TabListener getCallback() {
            return this.mCallback;
        }
        
        @Override
        public CharSequence getContentDescription() {
            return this.mContentDesc;
        }
        
        @Override
        public View getCustomView() {
            return this.mCustomView;
        }
        
        @Override
        public Drawable getIcon() {
            return this.mIcon;
        }
        
        @Override
        public int getPosition() {
            return this.mPosition;
        }
        
        @Override
        public Object getTag() {
            return this.mTag;
        }
        
        @Override
        public CharSequence getText() {
            return this.mText;
        }
        
        @Override
        public void select() {
            WindowDecorActionBar.this.selectTab(this);
        }
        
        @Override
        public Tab setContentDescription(final int n) {
            return this.setContentDescription(WindowDecorActionBar.this.mContext.getResources().getText(n));
        }
        
        @Override
        public Tab setContentDescription(final CharSequence mContentDesc) {
            this.mContentDesc = mContentDesc;
            if (this.mPosition >= 0) {
                WindowDecorActionBar.this.mTabScrollView.updateTab(this.mPosition);
            }
            return this;
        }
        
        @Override
        public Tab setCustomView(final int n) {
            return this.setCustomView(LayoutInflater.from(WindowDecorActionBar.this.getThemedContext()).inflate(n, (ViewGroup)null));
        }
        
        @Override
        public Tab setCustomView(final View mCustomView) {
            this.mCustomView = mCustomView;
            if (this.mPosition >= 0) {
                WindowDecorActionBar.this.mTabScrollView.updateTab(this.mPosition);
            }
            return this;
        }
        
        @Override
        public Tab setIcon(final int n) {
            return this.setIcon(AppCompatResources.getDrawable(WindowDecorActionBar.this.mContext, n));
        }
        
        @Override
        public Tab setIcon(final Drawable mIcon) {
            this.mIcon = mIcon;
            if (this.mPosition >= 0) {
                WindowDecorActionBar.this.mTabScrollView.updateTab(this.mPosition);
            }
            return this;
        }
        
        public void setPosition(final int mPosition) {
            this.mPosition = mPosition;
        }
        
        @Override
        public Tab setTabListener(final TabListener mCallback) {
            this.mCallback = mCallback;
            return this;
        }
        
        @Override
        public Tab setTag(final Object mTag) {
            this.mTag = mTag;
            return this;
        }
        
        @Override
        public Tab setText(final int n) {
            return this.setText(WindowDecorActionBar.this.mContext.getResources().getText(n));
        }
        
        @Override
        public Tab setText(final CharSequence mText) {
            this.mText = mText;
            if (this.mPosition >= 0) {
                WindowDecorActionBar.this.mTabScrollView.updateTab(this.mPosition);
            }
            return this;
        }
    }
}
