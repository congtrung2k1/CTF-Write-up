// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.app;

import android.os.Parcel;
import android.os.Parcelable$ClassLoaderCreator;
import android.os.Parcelable$Creator;
import android.os.Parcelable;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.view.menu.ListMenuPresenter;
import androidx.appcompat.content.res.AppCompatResources;
import android.view.MotionEvent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import androidx.appcompat.view.SupportActionModeWrapper;
import android.view.ActionMode$Callback;
import android.view.KeyboardShortcutGroup;
import java.util.List;
import androidx.appcompat.view.WindowCallbackWrapper;
import android.view.ViewGroup$MarginLayoutParams;
import androidx.appcompat.view.StandaloneActionMode;
import androidx.appcompat.widget.ViewStubCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;
import androidx.core.widget.PopupWindowCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import androidx.core.app.NavUtils;
import androidx.appcompat.widget.AppCompatDrawableManager;
import android.app.UiModeManager;
import androidx.core.view.LayoutInflaterCompat;
import androidx.appcompat.view.SupportMenuInflater;
import android.os.Bundle;
import androidx.core.view.KeyEventDispatcher;
import androidx.appcompat.widget.VectorEnabledTintResources;
import org.xmlpull.v1.XmlPullParser;
import android.util.DisplayMetrics;
import android.content.res.Resources;
import android.content.res.Configuration;
import android.util.AndroidRuntimeException;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager$NameNotFoundException;
import android.content.ComponentName;
import android.view.KeyCharacterMap;
import android.view.ViewParent;
import android.view.WindowManager$LayoutParams;
import android.view.ViewGroup$LayoutParams;
import android.view.WindowManager;
import android.view.Menu;
import android.util.Log;
import android.media.AudioManager;
import android.view.ViewConfiguration;
import android.view.KeyEvent;
import android.content.res.Resources$Theme;
import androidx.appcompat.view.menu.MenuPresenter;
import android.app.Dialog;
import android.app.Activity;
import android.text.TextUtils;
import android.widget.FrameLayout;
import androidx.appcompat.widget.ViewUtils;
import androidx.appcompat.widget.FitWindowsViewGroup;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.appcompat.view.ContextThemeWrapper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.content.res.TypedArray;
import androidx.appcompat.R;
import androidx.appcompat.widget.ContentFrameLayout;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import androidx.appcompat.widget.TintTypedArray;
import android.content.res.Resources$NotFoundException;
import android.os.Build$VERSION;
import android.view.Window;
import android.widget.TextView;
import android.graphics.Rect;
import android.view.ViewGroup;
import android.view.View;
import android.view.MenuInflater;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.appcompat.widget.DecorContentParent;
import android.content.Context;
import android.view.Window$Callback;
import androidx.appcompat.widget.ActionBarContextView;
import android.widget.PopupWindow;
import androidx.appcompat.view.ActionMode;
import android.view.LayoutInflater$Factory2;
import androidx.appcompat.view.menu.MenuBuilder;

class AppCompatDelegateImpl extends AppCompatDelegate implements Callback, LayoutInflater$Factory2
{
    private static final boolean DEBUG = false;
    static final String EXCEPTION_HANDLER_MESSAGE_SUFFIX = ". If the resource you are trying to use is a vector resource, you may be referencing it in an unsupported way. See AppCompatDelegate.setCompatVectorFromResourcesEnabled() for more info.";
    private static final boolean IS_PRE_LOLLIPOP;
    private static final String KEY_LOCAL_NIGHT_MODE = "appcompat:local_night_mode";
    private static boolean sInstalledExceptionHandler;
    private static final int[] sWindowBackgroundStyleable;
    ActionBar mActionBar;
    private ActionMenuPresenterCallback mActionMenuPresenterCallback;
    ActionMode mActionMode;
    PopupWindow mActionModePopup;
    ActionBarContextView mActionModeView;
    final AppCompatCallback mAppCompatCallback;
    private AppCompatViewInflater mAppCompatViewInflater;
    final Window$Callback mAppCompatWindowCallback;
    private boolean mApplyDayNightCalled;
    private AutoNightModeManager mAutoNightModeManager;
    private boolean mClosingActionMenu;
    final Context mContext;
    private DecorContentParent mDecorContentParent;
    private boolean mEnableDefaultActionBarUp;
    ViewPropertyAnimatorCompat mFadeAnim;
    private boolean mFeatureIndeterminateProgress;
    private boolean mFeatureProgress;
    private boolean mHandleNativeActionModes;
    boolean mHasActionBar;
    int mInvalidatePanelMenuFeatures;
    boolean mInvalidatePanelMenuPosted;
    private final Runnable mInvalidatePanelMenuRunnable;
    boolean mIsDestroyed;
    boolean mIsFloating;
    private int mLocalNightMode;
    private boolean mLongPressBackDown;
    MenuInflater mMenuInflater;
    final Window$Callback mOriginalWindowCallback;
    boolean mOverlayActionBar;
    boolean mOverlayActionMode;
    private PanelMenuPresenterCallback mPanelMenuPresenterCallback;
    private PanelFeatureState[] mPanels;
    private PanelFeatureState mPreparedPanel;
    Runnable mShowActionModePopup;
    private View mStatusGuard;
    private ViewGroup mSubDecor;
    private boolean mSubDecorInstalled;
    private Rect mTempRect1;
    private Rect mTempRect2;
    private CharSequence mTitle;
    private TextView mTitleView;
    final Window mWindow;
    boolean mWindowNoTitle;
    
    static {
        final boolean b = IS_PRE_LOLLIPOP = (Build$VERSION.SDK_INT < 21);
        sWindowBackgroundStyleable = new int[] { 16842836 };
        if (b && !AppCompatDelegateImpl.sInstalledExceptionHandler) {
            Thread.setDefaultUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new Thread.UncaughtExceptionHandler() {
                final /* synthetic */ UncaughtExceptionHandler val$defHandler = Thread.getDefaultUncaughtExceptionHandler();
                
                private boolean shouldWrapException(final Throwable t) {
                    final boolean b = t instanceof Resources$NotFoundException;
                    boolean b2 = false;
                    if (b) {
                        final String message = t.getMessage();
                        if (message != null && (message.contains("drawable") || message.contains("Drawable"))) {
                            b2 = true;
                        }
                        return b2;
                    }
                    return false;
                }
                
                @Override
                public void uncaughtException(final Thread thread, final Throwable t) {
                    if (this.shouldWrapException(t)) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append(t.getMessage());
                        sb.append(". If the resource you are trying to use is a vector resource, you may be referencing it in an unsupported way. See AppCompatDelegate.setCompatVectorFromResourcesEnabled() for more info.");
                        final Resources$NotFoundException ex = new Resources$NotFoundException(sb.toString());
                        ((Throwable)ex).initCause(t.getCause());
                        ((Throwable)ex).setStackTrace(t.getStackTrace());
                        this.val$defHandler.uncaughtException(thread, (Throwable)ex);
                    }
                    else {
                        this.val$defHandler.uncaughtException(thread, t);
                    }
                }
            });
            AppCompatDelegateImpl.sInstalledExceptionHandler = true;
        }
    }
    
    AppCompatDelegateImpl(final Context mContext, final Window mWindow, final AppCompatCallback mAppCompatCallback) {
        this.mFadeAnim = null;
        this.mHandleNativeActionModes = true;
        this.mLocalNightMode = -100;
        this.mInvalidatePanelMenuRunnable = new Runnable() {
            @Override
            public void run() {
                if ((AppCompatDelegateImpl.this.mInvalidatePanelMenuFeatures & 0x1) != 0x0) {
                    AppCompatDelegateImpl.this.doInvalidatePanelMenu(0);
                }
                if ((AppCompatDelegateImpl.this.mInvalidatePanelMenuFeatures & 0x1000) != 0x0) {
                    AppCompatDelegateImpl.this.doInvalidatePanelMenu(108);
                }
                AppCompatDelegateImpl.this.mInvalidatePanelMenuPosted = false;
                AppCompatDelegateImpl.this.mInvalidatePanelMenuFeatures = 0;
            }
        };
        this.mContext = mContext;
        this.mWindow = mWindow;
        this.mAppCompatCallback = mAppCompatCallback;
        final Window$Callback callback = mWindow.getCallback();
        this.mOriginalWindowCallback = callback;
        if (!(callback instanceof AppCompatWindowCallback)) {
            final AppCompatWindowCallback appCompatWindowCallback = new AppCompatWindowCallback(this.mOriginalWindowCallback);
            this.mAppCompatWindowCallback = (Window$Callback)appCompatWindowCallback;
            this.mWindow.setCallback((Window$Callback)appCompatWindowCallback);
            final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(mContext, null, AppCompatDelegateImpl.sWindowBackgroundStyleable);
            final Drawable drawableIfKnown = obtainStyledAttributes.getDrawableIfKnown(0);
            if (drawableIfKnown != null) {
                this.mWindow.setBackgroundDrawable(drawableIfKnown);
            }
            obtainStyledAttributes.recycle();
            return;
        }
        throw new IllegalStateException("AppCompat has already installed itself into the Window");
    }
    
    private void applyFixedSizeWindow() {
        final ContentFrameLayout contentFrameLayout = (ContentFrameLayout)this.mSubDecor.findViewById(16908290);
        final View decorView = this.mWindow.getDecorView();
        contentFrameLayout.setDecorPadding(decorView.getPaddingLeft(), decorView.getPaddingTop(), decorView.getPaddingRight(), decorView.getPaddingBottom());
        final TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(R.styleable.AppCompatTheme);
        obtainStyledAttributes.getValue(R.styleable.AppCompatTheme_windowMinWidthMajor, contentFrameLayout.getMinWidthMajor());
        obtainStyledAttributes.getValue(R.styleable.AppCompatTheme_windowMinWidthMinor, contentFrameLayout.getMinWidthMinor());
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTheme_windowFixedWidthMajor)) {
            obtainStyledAttributes.getValue(R.styleable.AppCompatTheme_windowFixedWidthMajor, contentFrameLayout.getFixedWidthMajor());
        }
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTheme_windowFixedWidthMinor)) {
            obtainStyledAttributes.getValue(R.styleable.AppCompatTheme_windowFixedWidthMinor, contentFrameLayout.getFixedWidthMinor());
        }
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTheme_windowFixedHeightMajor)) {
            obtainStyledAttributes.getValue(R.styleable.AppCompatTheme_windowFixedHeightMajor, contentFrameLayout.getFixedHeightMajor());
        }
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTheme_windowFixedHeightMinor)) {
            obtainStyledAttributes.getValue(R.styleable.AppCompatTheme_windowFixedHeightMinor, contentFrameLayout.getFixedHeightMinor());
        }
        obtainStyledAttributes.recycle();
        contentFrameLayout.requestLayout();
    }
    
    private ViewGroup createSubDecor() {
        final TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(R.styleable.AppCompatTheme);
        if (!obtainStyledAttributes.hasValue(R.styleable.AppCompatTheme_windowActionBar)) {
            obtainStyledAttributes.recycle();
            throw new IllegalStateException("You need to use a Theme.AppCompat theme (or descendant) with this activity.");
        }
        if (obtainStyledAttributes.getBoolean(R.styleable.AppCompatTheme_windowNoTitle, false)) {
            this.requestWindowFeature(1);
        }
        else if (obtainStyledAttributes.getBoolean(R.styleable.AppCompatTheme_windowActionBar, false)) {
            this.requestWindowFeature(108);
        }
        if (obtainStyledAttributes.getBoolean(R.styleable.AppCompatTheme_windowActionBarOverlay, false)) {
            this.requestWindowFeature(109);
        }
        if (obtainStyledAttributes.getBoolean(R.styleable.AppCompatTheme_windowActionModeOverlay, false)) {
            this.requestWindowFeature(10);
        }
        this.mIsFloating = obtainStyledAttributes.getBoolean(R.styleable.AppCompatTheme_android_windowIsFloating, false);
        obtainStyledAttributes.recycle();
        this.mWindow.getDecorView();
        final LayoutInflater from = LayoutInflater.from(this.mContext);
        Object contentView = null;
        if (!this.mWindowNoTitle) {
            if (this.mIsFloating) {
                contentView = from.inflate(R.layout.abc_dialog_title_material, (ViewGroup)null);
                this.mOverlayActionBar = false;
                this.mHasActionBar = false;
            }
            else if (this.mHasActionBar) {
                final TypedValue typedValue = new TypedValue();
                this.mContext.getTheme().resolveAttribute(R.attr.actionBarTheme, typedValue, true);
                Object mContext;
                if (typedValue.resourceId != 0) {
                    mContext = new ContextThemeWrapper(this.mContext, typedValue.resourceId);
                }
                else {
                    mContext = this.mContext;
                }
                contentView = LayoutInflater.from((Context)mContext).inflate(R.layout.abc_screen_toolbar, (ViewGroup)null);
                (this.mDecorContentParent = (DecorContentParent)((ViewGroup)contentView).findViewById(R.id.decor_content_parent)).setWindowCallback(this.getWindowCallback());
                if (this.mOverlayActionBar) {
                    this.mDecorContentParent.initFeature(109);
                }
                if (this.mFeatureProgress) {
                    this.mDecorContentParent.initFeature(2);
                }
                if (this.mFeatureIndeterminateProgress) {
                    this.mDecorContentParent.initFeature(5);
                }
            }
        }
        else {
            if (this.mOverlayActionMode) {
                contentView = from.inflate(R.layout.abc_screen_simple_overlay_action_mode, (ViewGroup)null);
            }
            else {
                contentView = from.inflate(R.layout.abc_screen_simple, (ViewGroup)null);
            }
            if (Build$VERSION.SDK_INT >= 21) {
                ViewCompat.setOnApplyWindowInsetsListener((View)contentView, new OnApplyWindowInsetsListener() {
                    @Override
                    public WindowInsetsCompat onApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
                        final int systemWindowInsetTop = windowInsetsCompat.getSystemWindowInsetTop();
                        final int updateStatusGuard = AppCompatDelegateImpl.this.updateStatusGuard(systemWindowInsetTop);
                        WindowInsetsCompat replaceSystemWindowInsets = windowInsetsCompat;
                        if (systemWindowInsetTop != updateStatusGuard) {
                            replaceSystemWindowInsets = windowInsetsCompat.replaceSystemWindowInsets(windowInsetsCompat.getSystemWindowInsetLeft(), updateStatusGuard, windowInsetsCompat.getSystemWindowInsetRight(), windowInsetsCompat.getSystemWindowInsetBottom());
                        }
                        return ViewCompat.onApplyWindowInsets(view, replaceSystemWindowInsets);
                    }
                });
            }
            else {
                ((FitWindowsViewGroup)contentView).setOnFitSystemWindowsListener((FitWindowsViewGroup.OnFitSystemWindowsListener)new FitWindowsViewGroup.OnFitSystemWindowsListener() {
                    @Override
                    public void onFitSystemWindows(final Rect rect) {
                        rect.top = AppCompatDelegateImpl.this.updateStatusGuard(rect.top);
                    }
                });
            }
        }
        if (contentView != null) {
            if (this.mDecorContentParent == null) {
                this.mTitleView = (TextView)((ViewGroup)contentView).findViewById(R.id.title);
            }
            ViewUtils.makeOptionalFitsSystemWindows((View)contentView);
            final ContentFrameLayout contentFrameLayout = (ContentFrameLayout)((ViewGroup)contentView).findViewById(R.id.action_bar_activity_content);
            final ViewGroup viewGroup = (ViewGroup)this.mWindow.findViewById(16908290);
            if (viewGroup != null) {
                while (viewGroup.getChildCount() > 0) {
                    final View child = viewGroup.getChildAt(0);
                    viewGroup.removeViewAt(0);
                    contentFrameLayout.addView(child);
                }
                viewGroup.setId(-1);
                contentFrameLayout.setId(16908290);
                if (viewGroup instanceof FrameLayout) {
                    ((FrameLayout)viewGroup).setForeground((Drawable)null);
                }
            }
            this.mWindow.setContentView((View)contentView);
            contentFrameLayout.setAttachListener((ContentFrameLayout.OnAttachListener)new ContentFrameLayout.OnAttachListener() {
                @Override
                public void onAttachedFromWindow() {
                }
                
                @Override
                public void onDetachedFromWindow() {
                    AppCompatDelegateImpl.this.dismissPopups();
                }
            });
            return (ViewGroup)contentView;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("AppCompat does not support the current theme features: { windowActionBar: ");
        sb.append(this.mHasActionBar);
        sb.append(", windowActionBarOverlay: ");
        sb.append(this.mOverlayActionBar);
        sb.append(", android:windowIsFloating: ");
        sb.append(this.mIsFloating);
        sb.append(", windowActionModeOverlay: ");
        sb.append(this.mOverlayActionMode);
        sb.append(", windowNoTitle: ");
        sb.append(this.mWindowNoTitle);
        sb.append(" }");
        throw new IllegalArgumentException(sb.toString());
    }
    
    private void ensureAutoNightModeManager() {
        if (this.mAutoNightModeManager == null) {
            this.mAutoNightModeManager = new AutoNightModeManager(TwilightManager.getInstance(this.mContext));
        }
    }
    
    private void ensureSubDecor() {
        if (!this.mSubDecorInstalled) {
            this.mSubDecor = this.createSubDecor();
            final CharSequence title = this.getTitle();
            if (!TextUtils.isEmpty(title)) {
                final DecorContentParent mDecorContentParent = this.mDecorContentParent;
                if (mDecorContentParent != null) {
                    mDecorContentParent.setWindowTitle(title);
                }
                else if (this.peekSupportActionBar() != null) {
                    this.peekSupportActionBar().setWindowTitle(title);
                }
                else {
                    final TextView mTitleView = this.mTitleView;
                    if (mTitleView != null) {
                        mTitleView.setText(title);
                    }
                }
            }
            this.applyFixedSizeWindow();
            this.onSubDecorInstalled(this.mSubDecor);
            this.mSubDecorInstalled = true;
            final PanelFeatureState panelState = this.getPanelState(0, false);
            if (!this.mIsDestroyed && (panelState == null || panelState.menu == null)) {
                this.invalidatePanelMenu(108);
            }
        }
    }
    
    private int getNightMode() {
        int n = this.mLocalNightMode;
        if (n == -100) {
            n = AppCompatDelegate.getDefaultNightMode();
        }
        return n;
    }
    
    private void initWindowDecorActionBar() {
        this.ensureSubDecor();
        if (this.mHasActionBar && this.mActionBar == null) {
            final Window$Callback mOriginalWindowCallback = this.mOriginalWindowCallback;
            if (mOriginalWindowCallback instanceof Activity) {
                this.mActionBar = new WindowDecorActionBar((Activity)this.mOriginalWindowCallback, this.mOverlayActionBar);
            }
            else if (mOriginalWindowCallback instanceof Dialog) {
                this.mActionBar = new WindowDecorActionBar((Dialog)this.mOriginalWindowCallback);
            }
            final ActionBar mActionBar = this.mActionBar;
            if (mActionBar != null) {
                mActionBar.setDefaultDisplayHomeAsUpEnabled(this.mEnableDefaultActionBarUp);
            }
        }
    }
    
    private boolean initializePanelContent(final PanelFeatureState panelFeatureState) {
        final View createdPanelView = panelFeatureState.createdPanelView;
        boolean b = true;
        if (createdPanelView != null) {
            panelFeatureState.shownPanelView = panelFeatureState.createdPanelView;
            return true;
        }
        if (panelFeatureState.menu == null) {
            return false;
        }
        if (this.mPanelMenuPresenterCallback == null) {
            this.mPanelMenuPresenterCallback = new PanelMenuPresenterCallback();
        }
        panelFeatureState.shownPanelView = (View)panelFeatureState.getListMenuView(this.mPanelMenuPresenterCallback);
        if (panelFeatureState.shownPanelView == null) {
            b = false;
        }
        return b;
    }
    
    private boolean initializePanelDecor(final PanelFeatureState panelFeatureState) {
        panelFeatureState.setStyle(this.getActionBarThemedContext());
        panelFeatureState.decorView = (ViewGroup)new ListMenuDecorView(panelFeatureState.listPresenterContext);
        panelFeatureState.gravity = 81;
        return true;
    }
    
    private boolean initializePanelMenu(final PanelFeatureState panelFeatureState) {
        final Context mContext = this.mContext;
        Object o = null;
        Label_0190: {
            if (panelFeatureState.featureId != 0) {
                o = mContext;
                if (panelFeatureState.featureId != 108) {
                    break Label_0190;
                }
            }
            o = mContext;
            if (this.mDecorContentParent != null) {
                final TypedValue typedValue = new TypedValue();
                final Resources$Theme theme = mContext.getTheme();
                theme.resolveAttribute(R.attr.actionBarTheme, typedValue, true);
                Resources$Theme theme2 = null;
                if (typedValue.resourceId != 0) {
                    theme2 = mContext.getResources().newTheme();
                    theme2.setTo(theme);
                    theme2.applyStyle(typedValue.resourceId, true);
                    theme2.resolveAttribute(R.attr.actionBarWidgetTheme, typedValue, true);
                }
                else {
                    theme.resolveAttribute(R.attr.actionBarWidgetTheme, typedValue, true);
                }
                Resources$Theme theme3 = theme2;
                if (typedValue.resourceId != 0) {
                    if ((theme3 = theme2) == null) {
                        theme3 = mContext.getResources().newTheme();
                        theme3.setTo(theme);
                    }
                    theme3.applyStyle(typedValue.resourceId, true);
                }
                o = mContext;
                if (theme3 != null) {
                    o = new ContextThemeWrapper(mContext, 0);
                    ((Context)o).getTheme().setTo(theme3);
                }
            }
        }
        final MenuBuilder menu = new MenuBuilder((Context)o);
        menu.setCallback((MenuBuilder.Callback)this);
        panelFeatureState.setMenu(menu);
        return true;
    }
    
    private void invalidatePanelMenu(final int n) {
        this.mInvalidatePanelMenuFeatures |= 1 << n;
        if (!this.mInvalidatePanelMenuPosted) {
            ViewCompat.postOnAnimation(this.mWindow.getDecorView(), this.mInvalidatePanelMenuRunnable);
            this.mInvalidatePanelMenuPosted = true;
        }
    }
    
    private boolean onKeyDownPanel(final int n, final KeyEvent keyEvent) {
        if (keyEvent.getRepeatCount() == 0) {
            final PanelFeatureState panelState = this.getPanelState(n, true);
            if (!panelState.isOpen) {
                return this.preparePanel(panelState, keyEvent);
            }
        }
        return false;
    }
    
    private boolean onKeyUpPanel(final int n, final KeyEvent keyEvent) {
        if (this.mActionMode != null) {
            return false;
        }
        final boolean b = false;
        final PanelFeatureState panelState = this.getPanelState(n, true);
        boolean b2 = false;
        Label_0211: {
            if (n == 0) {
                final DecorContentParent mDecorContentParent = this.mDecorContentParent;
                if (mDecorContentParent != null && mDecorContentParent.canShowOverflowMenu() && !ViewConfiguration.get(this.mContext).hasPermanentMenuKey()) {
                    if (this.mDecorContentParent.isOverflowMenuShowing()) {
                        b2 = this.mDecorContentParent.hideOverflowMenu();
                        break Label_0211;
                    }
                    b2 = b;
                    if (this.mIsDestroyed) {
                        break Label_0211;
                    }
                    b2 = b;
                    if (this.preparePanel(panelState, keyEvent)) {
                        b2 = this.mDecorContentParent.showOverflowMenu();
                    }
                    break Label_0211;
                }
            }
            if (!panelState.isOpen && !panelState.isHandled) {
                b2 = b;
                if (panelState.isPrepared) {
                    boolean preparePanel = true;
                    if (panelState.refreshMenuContent) {
                        panelState.isPrepared = false;
                        preparePanel = this.preparePanel(panelState, keyEvent);
                    }
                    b2 = b;
                    if (preparePanel) {
                        this.openPanel(panelState, keyEvent);
                        b2 = true;
                    }
                }
            }
            else {
                b2 = panelState.isOpen;
                this.closePanel(panelState, true);
            }
        }
        if (b2) {
            final AudioManager audioManager = (AudioManager)this.mContext.getSystemService("audio");
            if (audioManager != null) {
                audioManager.playSoundEffect(0);
            }
            else {
                Log.w("AppCompatDelegate", "Couldn't get audio manager");
            }
        }
        return b2;
    }
    
    private void openPanel(final PanelFeatureState panelFeatureState, final KeyEvent keyEvent) {
        if (panelFeatureState.isOpen || this.mIsDestroyed) {
            return;
        }
        if (panelFeatureState.featureId == 0 && (this.mContext.getResources().getConfiguration().screenLayout & 0xF) == 0x4) {
            return;
        }
        final Window$Callback windowCallback = this.getWindowCallback();
        if (windowCallback != null && !windowCallback.onMenuOpened(panelFeatureState.featureId, (Menu)panelFeatureState.menu)) {
            this.closePanel(panelFeatureState, true);
            return;
        }
        final WindowManager windowManager = (WindowManager)this.mContext.getSystemService("window");
        if (windowManager == null) {
            return;
        }
        if (!this.preparePanel(panelFeatureState, keyEvent)) {
            return;
        }
        final int n = -2;
        int n2 = 0;
        Label_0362: {
            if (panelFeatureState.decorView != null && !panelFeatureState.refreshDecorView) {
                if (panelFeatureState.createdPanelView != null) {
                    final ViewGroup$LayoutParams layoutParams = panelFeatureState.createdPanelView.getLayoutParams();
                    n2 = n;
                    if (layoutParams == null) {
                        break Label_0362;
                    }
                    n2 = n;
                    if (layoutParams.width == -1) {
                        n2 = -1;
                    }
                    break Label_0362;
                }
            }
            else {
                if (panelFeatureState.decorView == null) {
                    if (!this.initializePanelDecor(panelFeatureState) || panelFeatureState.decorView == null) {
                        return;
                    }
                }
                else if (panelFeatureState.refreshDecorView && panelFeatureState.decorView.getChildCount() > 0) {
                    panelFeatureState.decorView.removeAllViews();
                }
                if (!this.initializePanelContent(panelFeatureState) || !panelFeatureState.hasPanelItems()) {
                    return;
                }
                ViewGroup$LayoutParams layoutParams2;
                if ((layoutParams2 = panelFeatureState.shownPanelView.getLayoutParams()) == null) {
                    layoutParams2 = new ViewGroup$LayoutParams(-2, -2);
                }
                panelFeatureState.decorView.setBackgroundResource(panelFeatureState.background);
                final ViewParent parent = panelFeatureState.shownPanelView.getParent();
                if (parent != null && parent instanceof ViewGroup) {
                    ((ViewGroup)parent).removeView(panelFeatureState.shownPanelView);
                }
                panelFeatureState.decorView.addView(panelFeatureState.shownPanelView, layoutParams2);
                if (!panelFeatureState.shownPanelView.hasFocus()) {
                    panelFeatureState.shownPanelView.requestFocus();
                }
            }
            n2 = n;
        }
        panelFeatureState.isHandled = false;
        final WindowManager$LayoutParams windowManager$LayoutParams = new WindowManager$LayoutParams(n2, -2, panelFeatureState.x, panelFeatureState.y, 1002, 8519680, -3);
        windowManager$LayoutParams.gravity = panelFeatureState.gravity;
        windowManager$LayoutParams.windowAnimations = panelFeatureState.windowAnimations;
        windowManager.addView((View)panelFeatureState.decorView, (ViewGroup$LayoutParams)windowManager$LayoutParams);
        panelFeatureState.isOpen = true;
    }
    
    private boolean performPanelShortcut(final PanelFeatureState panelFeatureState, final int n, final KeyEvent keyEvent, final int n2) {
        if (keyEvent.isSystem()) {
            return false;
        }
        final boolean b = false;
        boolean performShortcut = false;
        Label_0056: {
            if (!panelFeatureState.isPrepared) {
                performShortcut = b;
                if (!this.preparePanel(panelFeatureState, keyEvent)) {
                    break Label_0056;
                }
            }
            performShortcut = b;
            if (panelFeatureState.menu != null) {
                performShortcut = panelFeatureState.menu.performShortcut(n, keyEvent, n2);
            }
        }
        if (performShortcut && (n2 & 0x1) == 0x0 && this.mDecorContentParent == null) {
            this.closePanel(panelFeatureState, true);
        }
        return performShortcut;
    }
    
    private boolean preparePanel(final PanelFeatureState mPreparedPanel, final KeyEvent keyEvent) {
        if (this.mIsDestroyed) {
            return false;
        }
        if (mPreparedPanel.isPrepared) {
            return true;
        }
        final PanelFeatureState mPreparedPanel2 = this.mPreparedPanel;
        if (mPreparedPanel2 != null && mPreparedPanel2 != mPreparedPanel) {
            this.closePanel(mPreparedPanel2, false);
        }
        final Window$Callback windowCallback = this.getWindowCallback();
        if (windowCallback != null) {
            mPreparedPanel.createdPanelView = windowCallback.onCreatePanelView(mPreparedPanel.featureId);
        }
        final boolean b = mPreparedPanel.featureId == 0 || mPreparedPanel.featureId == 108;
        if (b) {
            final DecorContentParent mDecorContentParent = this.mDecorContentParent;
            if (mDecorContentParent != null) {
                mDecorContentParent.setMenuPrepared();
            }
        }
        if (mPreparedPanel.createdPanelView == null && (!b || !(this.peekSupportActionBar() instanceof ToolbarActionBar))) {
            if (mPreparedPanel.menu == null || mPreparedPanel.refreshMenuContent) {
                if (mPreparedPanel.menu == null && (!this.initializePanelMenu(mPreparedPanel) || mPreparedPanel.menu == null)) {
                    return false;
                }
                if (b && this.mDecorContentParent != null) {
                    if (this.mActionMenuPresenterCallback == null) {
                        this.mActionMenuPresenterCallback = new ActionMenuPresenterCallback();
                    }
                    this.mDecorContentParent.setMenu((Menu)mPreparedPanel.menu, this.mActionMenuPresenterCallback);
                }
                mPreparedPanel.menu.stopDispatchingItemsChanged();
                if (!windowCallback.onCreatePanelMenu(mPreparedPanel.featureId, (Menu)mPreparedPanel.menu)) {
                    mPreparedPanel.setMenu(null);
                    if (b) {
                        final DecorContentParent mDecorContentParent2 = this.mDecorContentParent;
                        if (mDecorContentParent2 != null) {
                            mDecorContentParent2.setMenu(null, this.mActionMenuPresenterCallback);
                        }
                    }
                    return false;
                }
                mPreparedPanel.refreshMenuContent = false;
            }
            mPreparedPanel.menu.stopDispatchingItemsChanged();
            if (mPreparedPanel.frozenActionViewState != null) {
                mPreparedPanel.menu.restoreActionViewStates(mPreparedPanel.frozenActionViewState);
                mPreparedPanel.frozenActionViewState = null;
            }
            if (!windowCallback.onPreparePanel(0, mPreparedPanel.createdPanelView, (Menu)mPreparedPanel.menu)) {
                if (b) {
                    final DecorContentParent mDecorContentParent3 = this.mDecorContentParent;
                    if (mDecorContentParent3 != null) {
                        mDecorContentParent3.setMenu(null, this.mActionMenuPresenterCallback);
                    }
                }
                mPreparedPanel.menu.startDispatchingItemsChanged();
                return false;
            }
            int deviceId;
            if (keyEvent != null) {
                deviceId = keyEvent.getDeviceId();
            }
            else {
                deviceId = -1;
            }
            mPreparedPanel.qwertyMode = (KeyCharacterMap.load(deviceId).getKeyboardType() != 1);
            mPreparedPanel.menu.setQwertyMode(mPreparedPanel.qwertyMode);
            mPreparedPanel.menu.startDispatchingItemsChanged();
        }
        mPreparedPanel.isPrepared = true;
        mPreparedPanel.isHandled = false;
        this.mPreparedPanel = mPreparedPanel;
        return true;
    }
    
    private void reopenMenu(final MenuBuilder menuBuilder, final boolean b) {
        final DecorContentParent mDecorContentParent = this.mDecorContentParent;
        if (mDecorContentParent != null && mDecorContentParent.canShowOverflowMenu() && (!ViewConfiguration.get(this.mContext).hasPermanentMenuKey() || this.mDecorContentParent.isOverflowMenuShowPending())) {
            final Window$Callback windowCallback = this.getWindowCallback();
            if (this.mDecorContentParent.isOverflowMenuShowing() && b) {
                this.mDecorContentParent.hideOverflowMenu();
                if (!this.mIsDestroyed) {
                    windowCallback.onPanelClosed(108, (Menu)this.getPanelState(0, true).menu);
                }
            }
            else if (windowCallback != null && !this.mIsDestroyed) {
                if (this.mInvalidatePanelMenuPosted && (this.mInvalidatePanelMenuFeatures & 0x1) != 0x0) {
                    this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable);
                    this.mInvalidatePanelMenuRunnable.run();
                }
                final PanelFeatureState panelState = this.getPanelState(0, true);
                if (panelState.menu != null && !panelState.refreshMenuContent && windowCallback.onPreparePanel(0, panelState.createdPanelView, (Menu)panelState.menu)) {
                    windowCallback.onMenuOpened(108, (Menu)panelState.menu);
                    this.mDecorContentParent.showOverflowMenu();
                }
            }
            return;
        }
        final PanelFeatureState panelState2 = this.getPanelState(0, true);
        panelState2.refreshDecorView = true;
        this.closePanel(panelState2, false);
        this.openPanel(panelState2, null);
    }
    
    private int sanitizeWindowFeatureId(final int n) {
        if (n == 8) {
            Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR id when requesting this feature.");
            return 108;
        }
        if (n == 9) {
            Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY id when requesting this feature.");
            return 109;
        }
        return n;
    }
    
    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            return false;
        }
        final View decorView = this.mWindow.getDecorView();
        while (parent != null) {
            if (parent == decorView || !(parent instanceof View) || ViewCompat.isAttachedToWindow((View)parent)) {
                return false;
            }
            parent = parent.getParent();
        }
        return true;
    }
    
    private boolean shouldRecreateOnNightModeChange() {
        final boolean mApplyDayNightCalled = this.mApplyDayNightCalled;
        boolean b = false;
        if (mApplyDayNightCalled) {
            final Context mContext = this.mContext;
            if (mContext instanceof Activity) {
                final PackageManager packageManager = mContext.getPackageManager();
                try {
                    if ((packageManager.getActivityInfo(new ComponentName(this.mContext, (Class)this.mContext.getClass()), 0).configChanges & 0x200) == 0x0) {
                        b = true;
                    }
                    return b;
                }
                catch (PackageManager$NameNotFoundException ex) {
                    Log.d("AppCompatDelegate", "Exception while getting ActivityInfo", (Throwable)ex);
                    return true;
                }
            }
        }
        return false;
    }
    
    private void throwFeatureRequestIfSubDecorInstalled() {
        if (!this.mSubDecorInstalled) {
            return;
        }
        throw new AndroidRuntimeException("Window feature must be requested before adding content");
    }
    
    private boolean updateForNightMode(int n) {
        final Resources resources = this.mContext.getResources();
        final Configuration configuration = resources.getConfiguration();
        final int uiMode = configuration.uiMode;
        if (n == 2) {
            n = 32;
        }
        else {
            n = 16;
        }
        if ((uiMode & 0x30) != n) {
            if (this.shouldRecreateOnNightModeChange()) {
                ((Activity)this.mContext).recreate();
            }
            else {
                final Configuration configuration2 = new Configuration(configuration);
                final DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                configuration2.uiMode = ((configuration2.uiMode & 0xFFFFFFCF) | n);
                resources.updateConfiguration(configuration2, displayMetrics);
                if (Build$VERSION.SDK_INT < 26) {
                    ResourcesFlusher.flush(resources);
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void addContentView(final View view, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        this.ensureSubDecor();
        ((ViewGroup)this.mSubDecor.findViewById(16908290)).addView(view, viewGroup$LayoutParams);
        this.mOriginalWindowCallback.onContentChanged();
    }
    
    @Override
    public boolean applyDayNight() {
        boolean updateForNightMode = false;
        final int nightMode = this.getNightMode();
        final int mapNightMode = this.mapNightMode(nightMode);
        if (mapNightMode != -1) {
            updateForNightMode = this.updateForNightMode(mapNightMode);
        }
        if (nightMode == 0) {
            this.ensureAutoNightModeManager();
            this.mAutoNightModeManager.setup();
        }
        this.mApplyDayNightCalled = true;
        return updateForNightMode;
    }
    
    void callOnPanelClosed(final int n, final PanelFeatureState panelFeatureState, final Menu menu) {
        PanelFeatureState panelFeatureState2 = panelFeatureState;
        Object menu2 = menu;
        if (menu == null) {
            PanelFeatureState panelFeatureState3;
            if ((panelFeatureState3 = panelFeatureState) == null) {
                panelFeatureState3 = panelFeatureState;
                if (n >= 0) {
                    final PanelFeatureState[] mPanels = this.mPanels;
                    panelFeatureState3 = panelFeatureState;
                    if (n < mPanels.length) {
                        panelFeatureState3 = mPanels[n];
                    }
                }
            }
            panelFeatureState2 = panelFeatureState3;
            menu2 = menu;
            if (panelFeatureState3 != null) {
                menu2 = panelFeatureState3.menu;
                panelFeatureState2 = panelFeatureState3;
            }
        }
        if (panelFeatureState2 != null && !panelFeatureState2.isOpen) {
            return;
        }
        if (!this.mIsDestroyed) {
            this.mOriginalWindowCallback.onPanelClosed(n, (Menu)menu2);
        }
    }
    
    void checkCloseActionMenu(final MenuBuilder menuBuilder) {
        if (this.mClosingActionMenu) {
            return;
        }
        this.mClosingActionMenu = true;
        this.mDecorContentParent.dismissPopups();
        final Window$Callback windowCallback = this.getWindowCallback();
        if (windowCallback != null && !this.mIsDestroyed) {
            windowCallback.onPanelClosed(108, (Menu)menuBuilder);
        }
        this.mClosingActionMenu = false;
    }
    
    void closePanel(final int n) {
        this.closePanel(this.getPanelState(n, true), true);
    }
    
    void closePanel(final PanelFeatureState panelFeatureState, final boolean b) {
        if (b && panelFeatureState.featureId == 0) {
            final DecorContentParent mDecorContentParent = this.mDecorContentParent;
            if (mDecorContentParent != null && mDecorContentParent.isOverflowMenuShowing()) {
                this.checkCloseActionMenu(panelFeatureState.menu);
                return;
            }
        }
        final WindowManager windowManager = (WindowManager)this.mContext.getSystemService("window");
        if (windowManager != null && panelFeatureState.isOpen && panelFeatureState.decorView != null) {
            windowManager.removeView((View)panelFeatureState.decorView);
            if (b) {
                this.callOnPanelClosed(panelFeatureState.featureId, panelFeatureState, null);
            }
        }
        panelFeatureState.isPrepared = false;
        panelFeatureState.isHandled = false;
        panelFeatureState.isOpen = false;
        panelFeatureState.shownPanelView = null;
        panelFeatureState.refreshDecorView = true;
        if (this.mPreparedPanel == panelFeatureState) {
            this.mPreparedPanel = null;
        }
    }
    
    @Override
    public View createView(final View view, final String s, final Context context, final AttributeSet set) {
        final AppCompatViewInflater mAppCompatViewInflater = this.mAppCompatViewInflater;
        final boolean b = false;
        if (mAppCompatViewInflater == null) {
            final String string = this.mContext.obtainStyledAttributes(R.styleable.AppCompatTheme).getString(R.styleable.AppCompatTheme_viewInflaterClass);
            if (string != null && !AppCompatViewInflater.class.getName().equals(string)) {
                try {
                    this.mAppCompatViewInflater = (AppCompatViewInflater)Class.forName(string).getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
                }
                finally {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Failed to instantiate custom view inflater ");
                    sb.append(string);
                    sb.append(". Falling back to default.");
                    final Throwable t;
                    Log.i("AppCompatDelegate", sb.toString(), t);
                    this.mAppCompatViewInflater = new AppCompatViewInflater();
                }
            }
            else {
                this.mAppCompatViewInflater = new AppCompatViewInflater();
            }
        }
        boolean shouldInheritContext = false;
        if (AppCompatDelegateImpl.IS_PRE_LOLLIPOP) {
            if (set instanceof XmlPullParser) {
                shouldInheritContext = b;
                if (((XmlPullParser)set).getDepth() > 1) {
                    shouldInheritContext = true;
                }
            }
            else {
                shouldInheritContext = this.shouldInheritContext((ViewParent)view);
            }
        }
        return this.mAppCompatViewInflater.createView(view, s, context, set, shouldInheritContext, AppCompatDelegateImpl.IS_PRE_LOLLIPOP, true, VectorEnabledTintResources.shouldBeUsed());
    }
    
    void dismissPopups() {
        final DecorContentParent mDecorContentParent = this.mDecorContentParent;
        if (mDecorContentParent != null) {
            mDecorContentParent.dismissPopups();
        }
        if (this.mActionModePopup != null) {
            this.mWindow.getDecorView().removeCallbacks(this.mShowActionModePopup);
            if (this.mActionModePopup.isShowing()) {
                try {
                    this.mActionModePopup.dismiss();
                }
                catch (IllegalArgumentException ex) {}
            }
            this.mActionModePopup = null;
        }
        this.endOnGoingFadeAnimation();
        final PanelFeatureState panelState = this.getPanelState(0, false);
        if (panelState != null && panelState.menu != null) {
            panelState.menu.close();
        }
    }
    
    boolean dispatchKeyEvent(final KeyEvent keyEvent) {
        final Window$Callback mOriginalWindowCallback = this.mOriginalWindowCallback;
        final boolean b = mOriginalWindowCallback instanceof KeyEventDispatcher.Component;
        boolean b2 = true;
        if (b || mOriginalWindowCallback instanceof AppCompatDialog) {
            final View decorView = this.mWindow.getDecorView();
            if (decorView != null && KeyEventDispatcher.dispatchBeforeHierarchy(decorView, keyEvent)) {
                return true;
            }
        }
        if (keyEvent.getKeyCode() == 82 && this.mOriginalWindowCallback.dispatchKeyEvent(keyEvent)) {
            return true;
        }
        final int keyCode = keyEvent.getKeyCode();
        if (keyEvent.getAction() != 0) {
            b2 = false;
        }
        boolean b3;
        if (b2) {
            b3 = this.onKeyDown(keyCode, keyEvent);
        }
        else {
            b3 = this.onKeyUp(keyCode, keyEvent);
        }
        return b3;
    }
    
    void doInvalidatePanelMenu(final int n) {
        final PanelFeatureState panelState = this.getPanelState(n, true);
        if (panelState.menu != null) {
            final Bundle frozenActionViewState = new Bundle();
            panelState.menu.saveActionViewStates(frozenActionViewState);
            if (frozenActionViewState.size() > 0) {
                panelState.frozenActionViewState = frozenActionViewState;
            }
            panelState.menu.stopDispatchingItemsChanged();
            panelState.menu.clear();
        }
        panelState.refreshMenuContent = true;
        panelState.refreshDecorView = true;
        if ((n == 108 || n == 0) && this.mDecorContentParent != null) {
            final PanelFeatureState panelState2 = this.getPanelState(0, false);
            if (panelState2 != null) {
                panelState2.isPrepared = false;
                this.preparePanel(panelState2, null);
            }
        }
    }
    
    void endOnGoingFadeAnimation() {
        final ViewPropertyAnimatorCompat mFadeAnim = this.mFadeAnim;
        if (mFadeAnim != null) {
            mFadeAnim.cancel();
        }
    }
    
    PanelFeatureState findMenuPanel(final Menu menu) {
        final PanelFeatureState[] mPanels = this.mPanels;
        if (mPanels != null) {
            final int length = mPanels.length;
        }
        else {
            final int length = 0;
        }
        for (final PanelFeatureState panelFeatureState : mPanels) {
            if (panelFeatureState != null && panelFeatureState.menu == menu) {
                return panelFeatureState;
            }
        }
        return null;
    }
    
    @Override
    public <T extends View> T findViewById(final int n) {
        this.ensureSubDecor();
        return (T)this.mWindow.findViewById(n);
    }
    
    final Context getActionBarThemedContext() {
        Context themedContext = null;
        final ActionBar supportActionBar = this.getSupportActionBar();
        if (supportActionBar != null) {
            themedContext = supportActionBar.getThemedContext();
        }
        Context mContext;
        if ((mContext = themedContext) == null) {
            mContext = this.mContext;
        }
        return mContext;
    }
    
    final AutoNightModeManager getAutoNightModeManager() {
        this.ensureAutoNightModeManager();
        return this.mAutoNightModeManager;
    }
    
    @Override
    public final ActionBarDrawerToggle.Delegate getDrawerToggleDelegate() {
        return new ActionBarDrawableToggleImpl();
    }
    
    @Override
    public MenuInflater getMenuInflater() {
        if (this.mMenuInflater == null) {
            this.initWindowDecorActionBar();
            final ActionBar mActionBar = this.mActionBar;
            Context context;
            if (mActionBar != null) {
                context = mActionBar.getThemedContext();
            }
            else {
                context = this.mContext;
            }
            this.mMenuInflater = new SupportMenuInflater(context);
        }
        return this.mMenuInflater;
    }
    
    protected PanelFeatureState getPanelState(final int n, final boolean b) {
        PanelFeatureState[] array = null;
        Label_0056: {
            final PanelFeatureState[] mPanels;
            if ((mPanels = this.mPanels) != null) {
                array = mPanels;
                if (mPanels.length > n) {
                    break Label_0056;
                }
            }
            final PanelFeatureState[] mPanels2 = new PanelFeatureState[n + 1];
            if (mPanels != null) {
                System.arraycopy(mPanels, 0, mPanels2, 0, mPanels.length);
            }
            array = mPanels2;
            this.mPanels = mPanels2;
        }
        PanelFeatureState panelFeatureState;
        if ((panelFeatureState = array[n]) == null) {
            panelFeatureState = (array[n] = new PanelFeatureState(n));
        }
        return panelFeatureState;
    }
    
    ViewGroup getSubDecor() {
        return this.mSubDecor;
    }
    
    @Override
    public ActionBar getSupportActionBar() {
        this.initWindowDecorActionBar();
        return this.mActionBar;
    }
    
    final CharSequence getTitle() {
        final Window$Callback mOriginalWindowCallback = this.mOriginalWindowCallback;
        if (mOriginalWindowCallback instanceof Activity) {
            return ((Activity)mOriginalWindowCallback).getTitle();
        }
        return this.mTitle;
    }
    
    final Window$Callback getWindowCallback() {
        return this.mWindow.getCallback();
    }
    
    @Override
    public boolean hasWindowFeature(final int n) {
        boolean b = false;
        final int sanitizeWindowFeatureId = this.sanitizeWindowFeatureId(n);
        final boolean b2 = true;
        if (sanitizeWindowFeatureId != 1) {
            if (sanitizeWindowFeatureId != 2) {
                if (sanitizeWindowFeatureId != 5) {
                    if (sanitizeWindowFeatureId != 10) {
                        if (sanitizeWindowFeatureId != 108) {
                            if (sanitizeWindowFeatureId == 109) {
                                b = this.mOverlayActionBar;
                            }
                        }
                        else {
                            b = this.mHasActionBar;
                        }
                    }
                    else {
                        b = this.mOverlayActionMode;
                    }
                }
                else {
                    b = this.mFeatureIndeterminateProgress;
                }
            }
            else {
                b = this.mFeatureProgress;
            }
        }
        else {
            b = this.mWindowNoTitle;
        }
        boolean b3 = b2;
        if (!b) {
            b3 = (this.mWindow.hasFeature(n) && b2);
        }
        return b3;
    }
    
    @Override
    public void installViewFactory() {
        final LayoutInflater from = LayoutInflater.from(this.mContext);
        if (from.getFactory() == null) {
            LayoutInflaterCompat.setFactory2(from, (LayoutInflater$Factory2)this);
        }
        else if (!(from.getFactory2() instanceof AppCompatDelegateImpl)) {
            Log.i("AppCompatDelegate", "The Activity's LayoutInflater already has a Factory installed so we can not install AppCompat's");
        }
    }
    
    @Override
    public void invalidateOptionsMenu() {
        final ActionBar supportActionBar = this.getSupportActionBar();
        if (supportActionBar != null && supportActionBar.invalidateOptionsMenu()) {
            return;
        }
        this.invalidatePanelMenu(0);
    }
    
    @Override
    public boolean isHandleNativeActionModesEnabled() {
        return this.mHandleNativeActionModes;
    }
    
    int mapNightMode(final int n) {
        if (n == -100) {
            return -1;
        }
        if (n != 0) {
            return n;
        }
        if (Build$VERSION.SDK_INT >= 23 && ((UiModeManager)this.mContext.getSystemService((Class)UiModeManager.class)).getNightMode() == 0) {
            return -1;
        }
        this.ensureAutoNightModeManager();
        return this.mAutoNightModeManager.getApplyableNightMode();
    }
    
    boolean onBackPressed() {
        final ActionMode mActionMode = this.mActionMode;
        if (mActionMode != null) {
            mActionMode.finish();
            return true;
        }
        final ActionBar supportActionBar = this.getSupportActionBar();
        return supportActionBar != null && supportActionBar.collapseActionView();
    }
    
    @Override
    public void onConfigurationChanged(final Configuration configuration) {
        if (this.mHasActionBar && this.mSubDecorInstalled) {
            final ActionBar supportActionBar = this.getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.onConfigurationChanged(configuration);
            }
        }
        AppCompatDrawableManager.get().onConfigurationChanged(this.mContext);
        this.applyDayNight();
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        final Window$Callback mOriginalWindowCallback = this.mOriginalWindowCallback;
        if (mOriginalWindowCallback instanceof Activity) {
            String parentActivityName = null;
            try {
                parentActivityName = NavUtils.getParentActivityName((Activity)mOriginalWindowCallback);
            }
            catch (IllegalArgumentException ex) {}
            if (parentActivityName != null) {
                final ActionBar peekSupportActionBar = this.peekSupportActionBar();
                if (peekSupportActionBar == null) {
                    this.mEnableDefaultActionBarUp = true;
                }
                else {
                    peekSupportActionBar.setDefaultDisplayHomeAsUpEnabled(true);
                }
            }
        }
        if (bundle != null && this.mLocalNightMode == -100) {
            this.mLocalNightMode = bundle.getInt("appcompat:local_night_mode", -100);
        }
    }
    
    public final View onCreateView(final View view, final String s, final Context context, final AttributeSet set) {
        return this.createView(view, s, context, set);
    }
    
    public View onCreateView(final String s, final Context context, final AttributeSet set) {
        return this.onCreateView(null, s, context, set);
    }
    
    @Override
    public void onDestroy() {
        if (this.mInvalidatePanelMenuPosted) {
            this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable);
        }
        this.mIsDestroyed = true;
        final ActionBar mActionBar = this.mActionBar;
        if (mActionBar != null) {
            mActionBar.onDestroy();
        }
        final AutoNightModeManager mAutoNightModeManager = this.mAutoNightModeManager;
        if (mAutoNightModeManager != null) {
            mAutoNightModeManager.cleanup();
        }
    }
    
    boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        boolean mLongPressBackDown = true;
        if (n != 4) {
            if (n == 82) {
                this.onKeyDownPanel(0, keyEvent);
                return true;
            }
        }
        else {
            if ((keyEvent.getFlags() & 0x80) == 0x0) {
                mLongPressBackDown = false;
            }
            this.mLongPressBackDown = mLongPressBackDown;
        }
        return false;
    }
    
    boolean onKeyShortcut(final int n, final KeyEvent keyEvent) {
        final ActionBar supportActionBar = this.getSupportActionBar();
        if (supportActionBar != null && supportActionBar.onKeyShortcut(n, keyEvent)) {
            return true;
        }
        final PanelFeatureState mPreparedPanel = this.mPreparedPanel;
        if (mPreparedPanel != null && this.performPanelShortcut(mPreparedPanel, keyEvent.getKeyCode(), keyEvent, 1)) {
            final PanelFeatureState mPreparedPanel2 = this.mPreparedPanel;
            if (mPreparedPanel2 != null) {
                mPreparedPanel2.isHandled = true;
            }
            return true;
        }
        if (this.mPreparedPanel == null) {
            final PanelFeatureState panelState = this.getPanelState(0, true);
            this.preparePanel(panelState, keyEvent);
            final boolean performPanelShortcut = this.performPanelShortcut(panelState, keyEvent.getKeyCode(), keyEvent, 1);
            panelState.isPrepared = false;
            if (performPanelShortcut) {
                return true;
            }
        }
        return false;
    }
    
    boolean onKeyUp(final int n, final KeyEvent keyEvent) {
        if (n != 4) {
            if (n == 82) {
                this.onKeyUpPanel(0, keyEvent);
                return true;
            }
        }
        else {
            final boolean mLongPressBackDown = this.mLongPressBackDown;
            this.mLongPressBackDown = false;
            final PanelFeatureState panelState = this.getPanelState(0, false);
            if (panelState != null && panelState.isOpen) {
                if (!mLongPressBackDown) {
                    this.closePanel(panelState, true);
                }
                return true;
            }
            if (this.onBackPressed()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean onMenuItemSelected(final MenuBuilder menuBuilder, final MenuItem menuItem) {
        final Window$Callback windowCallback = this.getWindowCallback();
        if (windowCallback != null && !this.mIsDestroyed) {
            final PanelFeatureState menuPanel = this.findMenuPanel((Menu)menuBuilder.getRootMenu());
            if (menuPanel != null) {
                return windowCallback.onMenuItemSelected(menuPanel.featureId, menuItem);
            }
        }
        return false;
    }
    
    @Override
    public void onMenuModeChange(final MenuBuilder menuBuilder) {
        this.reopenMenu(menuBuilder, true);
    }
    
    void onMenuOpened(final int n) {
        if (n == 108) {
            final ActionBar supportActionBar = this.getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.dispatchMenuVisibilityChanged(true);
            }
        }
    }
    
    void onPanelClosed(final int n) {
        if (n == 108) {
            final ActionBar supportActionBar = this.getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.dispatchMenuVisibilityChanged(false);
            }
        }
        else if (n == 0) {
            final PanelFeatureState panelState = this.getPanelState(n, true);
            if (panelState.isOpen) {
                this.closePanel(panelState, false);
            }
        }
    }
    
    @Override
    public void onPostCreate(final Bundle bundle) {
        this.ensureSubDecor();
    }
    
    @Override
    public void onPostResume() {
        final ActionBar supportActionBar = this.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setShowHideAnimationEnabled(true);
        }
    }
    
    @Override
    public void onSaveInstanceState(final Bundle bundle) {
        final int mLocalNightMode = this.mLocalNightMode;
        if (mLocalNightMode != -100) {
            bundle.putInt("appcompat:local_night_mode", mLocalNightMode);
        }
    }
    
    @Override
    public void onStart() {
        this.applyDayNight();
    }
    
    @Override
    public void onStop() {
        final ActionBar supportActionBar = this.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setShowHideAnimationEnabled(false);
        }
        final AutoNightModeManager mAutoNightModeManager = this.mAutoNightModeManager;
        if (mAutoNightModeManager != null) {
            mAutoNightModeManager.cleanup();
        }
    }
    
    void onSubDecorInstalled(final ViewGroup viewGroup) {
    }
    
    final ActionBar peekSupportActionBar() {
        return this.mActionBar;
    }
    
    @Override
    public boolean requestWindowFeature(int sanitizeWindowFeatureId) {
        sanitizeWindowFeatureId = this.sanitizeWindowFeatureId(sanitizeWindowFeatureId);
        if (this.mWindowNoTitle && sanitizeWindowFeatureId == 108) {
            return false;
        }
        if (this.mHasActionBar && sanitizeWindowFeatureId == 1) {
            this.mHasActionBar = false;
        }
        if (sanitizeWindowFeatureId == 1) {
            this.throwFeatureRequestIfSubDecorInstalled();
            return this.mWindowNoTitle = true;
        }
        if (sanitizeWindowFeatureId == 2) {
            this.throwFeatureRequestIfSubDecorInstalled();
            return this.mFeatureProgress = true;
        }
        if (sanitizeWindowFeatureId == 5) {
            this.throwFeatureRequestIfSubDecorInstalled();
            return this.mFeatureIndeterminateProgress = true;
        }
        if (sanitizeWindowFeatureId == 10) {
            this.throwFeatureRequestIfSubDecorInstalled();
            return this.mOverlayActionMode = true;
        }
        if (sanitizeWindowFeatureId == 108) {
            this.throwFeatureRequestIfSubDecorInstalled();
            return this.mHasActionBar = true;
        }
        if (sanitizeWindowFeatureId != 109) {
            return this.mWindow.requestFeature(sanitizeWindowFeatureId);
        }
        this.throwFeatureRequestIfSubDecorInstalled();
        return this.mOverlayActionBar = true;
    }
    
    @Override
    public void setContentView(final int n) {
        this.ensureSubDecor();
        final ViewGroup viewGroup = (ViewGroup)this.mSubDecor.findViewById(16908290);
        viewGroup.removeAllViews();
        LayoutInflater.from(this.mContext).inflate(n, viewGroup);
        this.mOriginalWindowCallback.onContentChanged();
    }
    
    @Override
    public void setContentView(final View view) {
        this.ensureSubDecor();
        final ViewGroup viewGroup = (ViewGroup)this.mSubDecor.findViewById(16908290);
        viewGroup.removeAllViews();
        viewGroup.addView(view);
        this.mOriginalWindowCallback.onContentChanged();
    }
    
    @Override
    public void setContentView(final View view, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        this.ensureSubDecor();
        final ViewGroup viewGroup = (ViewGroup)this.mSubDecor.findViewById(16908290);
        viewGroup.removeAllViews();
        viewGroup.addView(view, viewGroup$LayoutParams);
        this.mOriginalWindowCallback.onContentChanged();
    }
    
    @Override
    public void setHandleNativeActionModesEnabled(final boolean mHandleNativeActionModes) {
        this.mHandleNativeActionModes = mHandleNativeActionModes;
    }
    
    @Override
    public void setLocalNightMode(final int mLocalNightMode) {
        if (mLocalNightMode != -1 && mLocalNightMode != 0 && mLocalNightMode != 1 && mLocalNightMode != 2) {
            Log.i("AppCompatDelegate", "setLocalNightMode() called with an unknown mode");
        }
        else if (this.mLocalNightMode != mLocalNightMode) {
            this.mLocalNightMode = mLocalNightMode;
            if (this.mApplyDayNightCalled) {
                this.applyDayNight();
            }
        }
    }
    
    @Override
    public void setSupportActionBar(final Toolbar toolbar) {
        if (!(this.mOriginalWindowCallback instanceof Activity)) {
            return;
        }
        final ActionBar supportActionBar = this.getSupportActionBar();
        if (!(supportActionBar instanceof WindowDecorActionBar)) {
            this.mMenuInflater = null;
            if (supportActionBar != null) {
                supportActionBar.onDestroy();
            }
            if (toolbar != null) {
                final ToolbarActionBar mActionBar = new ToolbarActionBar(toolbar, ((Activity)this.mOriginalWindowCallback).getTitle(), this.mAppCompatWindowCallback);
                this.mActionBar = mActionBar;
                this.mWindow.setCallback(mActionBar.getWrappedWindowCallback());
            }
            else {
                this.mActionBar = null;
                this.mWindow.setCallback(this.mAppCompatWindowCallback);
            }
            this.invalidateOptionsMenu();
            return;
        }
        throw new IllegalStateException("This Activity already has an action bar supplied by the window decor. Do not request Window.FEATURE_SUPPORT_ACTION_BAR and set windowActionBar to false in your theme to use a Toolbar instead.");
    }
    
    @Override
    public final void setTitle(final CharSequence charSequence) {
        this.mTitle = charSequence;
        final DecorContentParent mDecorContentParent = this.mDecorContentParent;
        if (mDecorContentParent != null) {
            mDecorContentParent.setWindowTitle(charSequence);
        }
        else if (this.peekSupportActionBar() != null) {
            this.peekSupportActionBar().setWindowTitle(charSequence);
        }
        else {
            final TextView mTitleView = this.mTitleView;
            if (mTitleView != null) {
                mTitleView.setText(charSequence);
            }
        }
    }
    
    final boolean shouldAnimateActionModeView() {
        if (this.mSubDecorInstalled) {
            final ViewGroup mSubDecor = this.mSubDecor;
            if (mSubDecor != null && ViewCompat.isLaidOut((View)mSubDecor)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public ActionMode startSupportActionMode(final ActionMode.Callback callback) {
        if (callback != null) {
            final ActionMode mActionMode = this.mActionMode;
            if (mActionMode != null) {
                mActionMode.finish();
            }
            final ActionModeCallbackWrapperV9 actionModeCallbackWrapperV9 = new ActionModeCallbackWrapperV9(callback);
            final ActionBar supportActionBar = this.getSupportActionBar();
            if (supportActionBar != null) {
                final ActionMode startActionMode = supportActionBar.startActionMode(actionModeCallbackWrapperV9);
                if ((this.mActionMode = startActionMode) != null) {
                    final AppCompatCallback mAppCompatCallback = this.mAppCompatCallback;
                    if (mAppCompatCallback != null) {
                        mAppCompatCallback.onSupportActionModeStarted(startActionMode);
                    }
                }
            }
            if (this.mActionMode == null) {
                this.mActionMode = this.startSupportActionModeFromWindow(actionModeCallbackWrapperV9);
            }
            return this.mActionMode;
        }
        throw new IllegalArgumentException("ActionMode callback can not be null.");
    }
    
    ActionMode startSupportActionModeFromWindow(final ActionMode.Callback callback) {
        this.endOnGoingFadeAnimation();
        final ActionMode mActionMode = this.mActionMode;
        if (mActionMode != null) {
            mActionMode.finish();
        }
        ActionMode.Callback callback2 = callback;
        if (!(callback instanceof ActionModeCallbackWrapperV9)) {
            callback2 = new ActionModeCallbackWrapperV9(callback);
        }
        final ActionMode actionMode = null;
        final AppCompatCallback mAppCompatCallback = this.mAppCompatCallback;
        ActionMode onWindowStartingSupportActionMode = actionMode;
        if (mAppCompatCallback != null) {
            onWindowStartingSupportActionMode = actionMode;
            if (!this.mIsDestroyed) {
                try {
                    onWindowStartingSupportActionMode = mAppCompatCallback.onWindowStartingSupportActionMode(callback2);
                }
                catch (AbstractMethodError abstractMethodError) {
                    onWindowStartingSupportActionMode = actionMode;
                }
            }
        }
        if (onWindowStartingSupportActionMode != null) {
            this.mActionMode = onWindowStartingSupportActionMode;
        }
        else {
            final ActionBarContextView mActionModeView = this.mActionModeView;
            boolean b = true;
            if (mActionModeView == null) {
                if (this.mIsFloating) {
                    final TypedValue typedValue = new TypedValue();
                    final Resources$Theme theme = this.mContext.getTheme();
                    theme.resolveAttribute(R.attr.actionBarTheme, typedValue, true);
                    Object mContext;
                    if (typedValue.resourceId != 0) {
                        final Resources$Theme theme2 = this.mContext.getResources().newTheme();
                        theme2.setTo(theme);
                        theme2.applyStyle(typedValue.resourceId, true);
                        mContext = new ContextThemeWrapper(this.mContext, 0);
                        ((Context)mContext).getTheme().setTo(theme2);
                    }
                    else {
                        mContext = this.mContext;
                    }
                    this.mActionModeView = new ActionBarContextView((Context)mContext);
                    PopupWindowCompat.setWindowLayoutType(this.mActionModePopup = new PopupWindow((Context)mContext, (AttributeSet)null, R.attr.actionModePopupWindowStyle), 2);
                    this.mActionModePopup.setContentView((View)this.mActionModeView);
                    this.mActionModePopup.setWidth(-1);
                    ((Context)mContext).getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true);
                    this.mActionModeView.setContentHeight(TypedValue.complexToDimensionPixelSize(typedValue.data, ((Context)mContext).getResources().getDisplayMetrics()));
                    this.mActionModePopup.setHeight(-2);
                    this.mShowActionModePopup = new Runnable() {
                        @Override
                        public void run() {
                            AppCompatDelegateImpl.this.mActionModePopup.showAtLocation((View)AppCompatDelegateImpl.this.mActionModeView, 55, 0, 0);
                            AppCompatDelegateImpl.this.endOnGoingFadeAnimation();
                            if (AppCompatDelegateImpl.this.shouldAnimateActionModeView()) {
                                AppCompatDelegateImpl.this.mActionModeView.setAlpha(0.0f);
                                final AppCompatDelegateImpl this$0 = AppCompatDelegateImpl.this;
                                this$0.mFadeAnim = ViewCompat.animate((View)this$0.mActionModeView).alpha(1.0f);
                                AppCompatDelegateImpl.this.mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(final View view) {
                                        AppCompatDelegateImpl.this.mActionModeView.setAlpha(1.0f);
                                        AppCompatDelegateImpl.this.mFadeAnim.setListener(null);
                                        AppCompatDelegateImpl.this.mFadeAnim = null;
                                    }
                                    
                                    @Override
                                    public void onAnimationStart(final View view) {
                                        AppCompatDelegateImpl.this.mActionModeView.setVisibility(0);
                                    }
                                });
                            }
                            else {
                                AppCompatDelegateImpl.this.mActionModeView.setAlpha(1.0f);
                                AppCompatDelegateImpl.this.mActionModeView.setVisibility(0);
                            }
                        }
                    };
                }
                else {
                    final ViewStubCompat viewStubCompat = (ViewStubCompat)this.mSubDecor.findViewById(R.id.action_mode_bar_stub);
                    if (viewStubCompat != null) {
                        viewStubCompat.setLayoutInflater(LayoutInflater.from(this.getActionBarThemedContext()));
                        this.mActionModeView = (ActionBarContextView)viewStubCompat.inflate();
                    }
                }
            }
            if (this.mActionModeView != null) {
                this.endOnGoingFadeAnimation();
                this.mActionModeView.killMode();
                final Context context = this.mActionModeView.getContext();
                final ActionBarContextView mActionModeView2 = this.mActionModeView;
                if (this.mActionModePopup != null) {
                    b = false;
                }
                final StandaloneActionMode mActionMode2 = new StandaloneActionMode(context, mActionModeView2, callback2, b);
                if (callback2.onCreateActionMode(mActionMode2, mActionMode2.getMenu())) {
                    mActionMode2.invalidate();
                    this.mActionModeView.initForMode(mActionMode2);
                    this.mActionMode = mActionMode2;
                    if (this.shouldAnimateActionModeView()) {
                        this.mActionModeView.setAlpha(0.0f);
                        (this.mFadeAnim = ViewCompat.animate((View)this.mActionModeView).alpha(1.0f)).setListener(new ViewPropertyAnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(final View view) {
                                AppCompatDelegateImpl.this.mActionModeView.setAlpha(1.0f);
                                AppCompatDelegateImpl.this.mFadeAnim.setListener(null);
                                AppCompatDelegateImpl.this.mFadeAnim = null;
                            }
                            
                            @Override
                            public void onAnimationStart(final View view) {
                                AppCompatDelegateImpl.this.mActionModeView.setVisibility(0);
                                AppCompatDelegateImpl.this.mActionModeView.sendAccessibilityEvent(32);
                                if (AppCompatDelegateImpl.this.mActionModeView.getParent() instanceof View) {
                                    ViewCompat.requestApplyInsets((View)AppCompatDelegateImpl.this.mActionModeView.getParent());
                                }
                            }
                        });
                    }
                    else {
                        this.mActionModeView.setAlpha(1.0f);
                        this.mActionModeView.setVisibility(0);
                        this.mActionModeView.sendAccessibilityEvent(32);
                        if (this.mActionModeView.getParent() instanceof View) {
                            ViewCompat.requestApplyInsets((View)this.mActionModeView.getParent());
                        }
                    }
                    if (this.mActionModePopup != null) {
                        this.mWindow.getDecorView().post(this.mShowActionModePopup);
                    }
                }
                else {
                    this.mActionMode = null;
                }
            }
        }
        final ActionMode mActionMode3 = this.mActionMode;
        if (mActionMode3 != null) {
            final AppCompatCallback mAppCompatCallback2 = this.mAppCompatCallback;
            if (mAppCompatCallback2 != null) {
                mAppCompatCallback2.onSupportActionModeStarted(mActionMode3);
            }
        }
        return this.mActionMode;
    }
    
    int updateStatusGuard(int visibility) {
        final boolean b = false;
        final int n = 0;
        final ActionBarContextView mActionModeView = this.mActionModeView;
        final int n2 = 0;
        int n3 = b ? 1 : 0;
        int n4 = visibility;
        if (mActionModeView != null) {
            n3 = (b ? 1 : 0);
            n4 = visibility;
            if (mActionModeView.getLayoutParams() instanceof ViewGroup$MarginLayoutParams) {
                final ViewGroup$MarginLayoutParams layoutParams = (ViewGroup$MarginLayoutParams)this.mActionModeView.getLayoutParams();
                int n5 = 0;
                int n6 = 0;
                int n8;
                int n10;
                if (this.mActionModeView.isShown()) {
                    if (this.mTempRect1 == null) {
                        this.mTempRect1 = new Rect();
                        this.mTempRect2 = new Rect();
                    }
                    final Rect mTempRect1 = this.mTempRect1;
                    final Rect mTempRect2 = this.mTempRect2;
                    mTempRect1.set(0, visibility, 0, 0);
                    ViewUtils.computeFitSystemWindows((View)this.mSubDecor, mTempRect1, mTempRect2);
                    int n7;
                    if (mTempRect2.top == 0) {
                        n7 = visibility;
                    }
                    else {
                        n7 = 0;
                    }
                    if (layoutParams.topMargin != n7) {
                        final boolean b2 = true;
                        layoutParams.topMargin = visibility;
                        final View mStatusGuard = this.mStatusGuard;
                        if (mStatusGuard == null) {
                            (this.mStatusGuard = new View(this.mContext)).setBackgroundColor(this.mContext.getResources().getColor(R.color.abc_input_method_navigation_guard));
                            this.mSubDecor.addView(this.mStatusGuard, -1, new ViewGroup$LayoutParams(-1, visibility));
                            n6 = (b2 ? 1 : 0);
                        }
                        else {
                            final ViewGroup$LayoutParams layoutParams2 = mStatusGuard.getLayoutParams();
                            n6 = (b2 ? 1 : 0);
                            if (layoutParams2.height != visibility) {
                                layoutParams2.height = visibility;
                                this.mStatusGuard.setLayoutParams(layoutParams2);
                                n6 = (b2 ? 1 : 0);
                            }
                        }
                    }
                    if (this.mStatusGuard != null) {
                        n8 = 1;
                    }
                    else {
                        n8 = 0;
                    }
                    int n9 = visibility;
                    if (!this.mOverlayActionMode) {
                        n9 = visibility;
                        if (n8 != 0) {
                            n9 = 0;
                        }
                    }
                    n5 = n6;
                    n10 = n9;
                }
                else {
                    n8 = n;
                    n10 = visibility;
                    if (layoutParams.topMargin != 0) {
                        n5 = 1;
                        layoutParams.topMargin = 0;
                        n10 = visibility;
                        n8 = n;
                    }
                }
                n3 = n8;
                n4 = n10;
                if (n5 != 0) {
                    this.mActionModeView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                    n4 = n10;
                    n3 = n8;
                }
            }
        }
        final View mStatusGuard2 = this.mStatusGuard;
        if (mStatusGuard2 != null) {
            if (n3 != 0) {
                visibility = n2;
            }
            else {
                visibility = 8;
            }
            mStatusGuard2.setVisibility(visibility);
        }
        return n4;
    }
    
    private class ActionBarDrawableToggleImpl implements Delegate
    {
        ActionBarDrawableToggleImpl() {
        }
        
        @Override
        public Context getActionBarThemedContext() {
            return AppCompatDelegateImpl.this.getActionBarThemedContext();
        }
        
        @Override
        public Drawable getThemeUpIndicator() {
            final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(this.getActionBarThemedContext(), null, new int[] { R.attr.homeAsUpIndicator });
            final Drawable drawable = obtainStyledAttributes.getDrawable(0);
            obtainStyledAttributes.recycle();
            return drawable;
        }
        
        @Override
        public boolean isNavigationVisible() {
            final ActionBar supportActionBar = AppCompatDelegateImpl.this.getSupportActionBar();
            return supportActionBar != null && (supportActionBar.getDisplayOptions() & 0x4) != 0x0;
        }
        
        @Override
        public void setActionBarDescription(final int homeActionContentDescription) {
            final ActionBar supportActionBar = AppCompatDelegateImpl.this.getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setHomeActionContentDescription(homeActionContentDescription);
            }
        }
        
        @Override
        public void setActionBarUpIndicator(final Drawable homeAsUpIndicator, final int homeActionContentDescription) {
            final ActionBar supportActionBar = AppCompatDelegateImpl.this.getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setHomeAsUpIndicator(homeAsUpIndicator);
                supportActionBar.setHomeActionContentDescription(homeActionContentDescription);
            }
        }
    }
    
    private final class ActionMenuPresenterCallback implements MenuPresenter.Callback
    {
        ActionMenuPresenterCallback() {
        }
        
        @Override
        public void onCloseMenu(final MenuBuilder menuBuilder, final boolean b) {
            AppCompatDelegateImpl.this.checkCloseActionMenu(menuBuilder);
        }
        
        @Override
        public boolean onOpenSubMenu(final MenuBuilder menuBuilder) {
            final Window$Callback windowCallback = AppCompatDelegateImpl.this.getWindowCallback();
            if (windowCallback != null) {
                windowCallback.onMenuOpened(108, (Menu)menuBuilder);
            }
            return true;
        }
    }
    
    class ActionModeCallbackWrapperV9 implements ActionMode.Callback
    {
        private ActionMode.Callback mWrapped;
        
        public ActionModeCallbackWrapperV9(final ActionMode.Callback mWrapped) {
            this.mWrapped = mWrapped;
        }
        
        @Override
        public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
            return this.mWrapped.onActionItemClicked(actionMode, menuItem);
        }
        
        @Override
        public boolean onCreateActionMode(final ActionMode actionMode, final Menu menu) {
            return this.mWrapped.onCreateActionMode(actionMode, menu);
        }
        
        @Override
        public void onDestroyActionMode(final ActionMode actionMode) {
            this.mWrapped.onDestroyActionMode(actionMode);
            if (AppCompatDelegateImpl.this.mActionModePopup != null) {
                AppCompatDelegateImpl.this.mWindow.getDecorView().removeCallbacks(AppCompatDelegateImpl.this.mShowActionModePopup);
            }
            if (AppCompatDelegateImpl.this.mActionModeView != null) {
                AppCompatDelegateImpl.this.endOnGoingFadeAnimation();
                final AppCompatDelegateImpl this$0 = AppCompatDelegateImpl.this;
                this$0.mFadeAnim = ViewCompat.animate((View)this$0.mActionModeView).alpha(0.0f);
                AppCompatDelegateImpl.this.mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(final View view) {
                        AppCompatDelegateImpl.this.mActionModeView.setVisibility(8);
                        if (AppCompatDelegateImpl.this.mActionModePopup != null) {
                            AppCompatDelegateImpl.this.mActionModePopup.dismiss();
                        }
                        else if (AppCompatDelegateImpl.this.mActionModeView.getParent() instanceof View) {
                            ViewCompat.requestApplyInsets((View)AppCompatDelegateImpl.this.mActionModeView.getParent());
                        }
                        AppCompatDelegateImpl.this.mActionModeView.removeAllViews();
                        AppCompatDelegateImpl.this.mFadeAnim.setListener(null);
                        AppCompatDelegateImpl.this.mFadeAnim = null;
                    }
                });
            }
            if (AppCompatDelegateImpl.this.mAppCompatCallback != null) {
                AppCompatDelegateImpl.this.mAppCompatCallback.onSupportActionModeFinished(AppCompatDelegateImpl.this.mActionMode);
            }
            AppCompatDelegateImpl.this.mActionMode = null;
        }
        
        @Override
        public boolean onPrepareActionMode(final ActionMode actionMode, final Menu menu) {
            return this.mWrapped.onPrepareActionMode(actionMode, menu);
        }
    }
    
    class AppCompatWindowCallback extends WindowCallbackWrapper
    {
        AppCompatWindowCallback(final Window$Callback window$Callback) {
            super(window$Callback);
        }
        
        @Override
        public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
            return AppCompatDelegateImpl.this.dispatchKeyEvent(keyEvent) || super.dispatchKeyEvent(keyEvent);
        }
        
        @Override
        public boolean dispatchKeyShortcutEvent(final KeyEvent keyEvent) {
            return super.dispatchKeyShortcutEvent(keyEvent) || AppCompatDelegateImpl.this.onKeyShortcut(keyEvent.getKeyCode(), keyEvent);
        }
        
        @Override
        public void onContentChanged() {
        }
        
        @Override
        public boolean onCreatePanelMenu(final int n, final Menu menu) {
            return (n != 0 || menu instanceof MenuBuilder) && super.onCreatePanelMenu(n, menu);
        }
        
        @Override
        public boolean onMenuOpened(final int n, final Menu menu) {
            super.onMenuOpened(n, menu);
            AppCompatDelegateImpl.this.onMenuOpened(n);
            return true;
        }
        
        @Override
        public void onPanelClosed(final int n, final Menu menu) {
            super.onPanelClosed(n, menu);
            AppCompatDelegateImpl.this.onPanelClosed(n);
        }
        
        @Override
        public boolean onPreparePanel(final int n, final View view, final Menu menu) {
            MenuBuilder menuBuilder;
            if (menu instanceof MenuBuilder) {
                menuBuilder = (MenuBuilder)menu;
            }
            else {
                menuBuilder = null;
            }
            if (n == 0 && menuBuilder == null) {
                return false;
            }
            if (menuBuilder != null) {
                menuBuilder.setOverrideVisibleItems(true);
            }
            final boolean onPreparePanel = super.onPreparePanel(n, view, menu);
            if (menuBuilder != null) {
                menuBuilder.setOverrideVisibleItems(false);
            }
            return onPreparePanel;
        }
        
        @Override
        public void onProvideKeyboardShortcuts(final List<KeyboardShortcutGroup> list, final Menu menu, final int n) {
            final PanelFeatureState panelState = AppCompatDelegateImpl.this.getPanelState(0, true);
            if (panelState != null && panelState.menu != null) {
                super.onProvideKeyboardShortcuts(list, (Menu)panelState.menu, n);
            }
            else {
                super.onProvideKeyboardShortcuts(list, menu, n);
            }
        }
        
        @Override
        public android.view.ActionMode onWindowStartingActionMode(final ActionMode$Callback actionMode$Callback) {
            if (Build$VERSION.SDK_INT >= 23) {
                return null;
            }
            if (AppCompatDelegateImpl.this.isHandleNativeActionModesEnabled()) {
                return this.startAsSupportActionMode(actionMode$Callback);
            }
            return super.onWindowStartingActionMode(actionMode$Callback);
        }
        
        @Override
        public android.view.ActionMode onWindowStartingActionMode(final ActionMode$Callback actionMode$Callback, final int n) {
            if (AppCompatDelegateImpl.this.isHandleNativeActionModesEnabled() && n == 0) {
                return this.startAsSupportActionMode(actionMode$Callback);
            }
            return super.onWindowStartingActionMode(actionMode$Callback, n);
        }
        
        final android.view.ActionMode startAsSupportActionMode(final ActionMode$Callback actionMode$Callback) {
            final SupportActionModeWrapper.CallbackWrapper callbackWrapper = new SupportActionModeWrapper.CallbackWrapper(AppCompatDelegateImpl.this.mContext, actionMode$Callback);
            final ActionMode startSupportActionMode = AppCompatDelegateImpl.this.startSupportActionMode(callbackWrapper);
            if (startSupportActionMode != null) {
                return callbackWrapper.getActionModeWrapper(startSupportActionMode);
            }
            return null;
        }
    }
    
    final class AutoNightModeManager
    {
        private BroadcastReceiver mAutoTimeChangeReceiver;
        private IntentFilter mAutoTimeChangeReceiverFilter;
        private boolean mIsNight;
        private TwilightManager mTwilightManager;
        
        AutoNightModeManager(final TwilightManager mTwilightManager) {
            this.mTwilightManager = mTwilightManager;
            this.mIsNight = mTwilightManager.isNight();
        }
        
        void cleanup() {
            if (this.mAutoTimeChangeReceiver != null) {
                AppCompatDelegateImpl.this.mContext.unregisterReceiver(this.mAutoTimeChangeReceiver);
                this.mAutoTimeChangeReceiver = null;
            }
        }
        
        void dispatchTimeChanged() {
            final boolean night = this.mTwilightManager.isNight();
            if (night != this.mIsNight) {
                this.mIsNight = night;
                AppCompatDelegateImpl.this.applyDayNight();
            }
        }
        
        int getApplyableNightMode() {
            final boolean night = this.mTwilightManager.isNight();
            this.mIsNight = night;
            int n;
            if (night) {
                n = 2;
            }
            else {
                n = 1;
            }
            return n;
        }
        
        void setup() {
            this.cleanup();
            if (this.mAutoTimeChangeReceiver == null) {
                this.mAutoTimeChangeReceiver = new BroadcastReceiver() {
                    public void onReceive(final Context context, final Intent intent) {
                        AutoNightModeManager.this.dispatchTimeChanged();
                    }
                };
            }
            if (this.mAutoTimeChangeReceiverFilter == null) {
                (this.mAutoTimeChangeReceiverFilter = new IntentFilter()).addAction("android.intent.action.TIME_SET");
                this.mAutoTimeChangeReceiverFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
                this.mAutoTimeChangeReceiverFilter.addAction("android.intent.action.TIME_TICK");
            }
            AppCompatDelegateImpl.this.mContext.registerReceiver(this.mAutoTimeChangeReceiver, this.mAutoTimeChangeReceiverFilter);
        }
    }
    
    private class ListMenuDecorView extends ContentFrameLayout
    {
        public ListMenuDecorView(final Context context) {
            super(context);
        }
        
        private boolean isOutOfBounds(final int n, final int n2) {
            return n < -5 || n2 < -5 || n > this.getWidth() + 5 || n2 > this.getHeight() + 5;
        }
        
        public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
            return AppCompatDelegateImpl.this.dispatchKeyEvent(keyEvent) || super.dispatchKeyEvent(keyEvent);
        }
        
        public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0 && this.isOutOfBounds((int)motionEvent.getX(), (int)motionEvent.getY())) {
                AppCompatDelegateImpl.this.closePanel(0);
                return true;
            }
            return super.onInterceptTouchEvent(motionEvent);
        }
        
        public void setBackgroundResource(final int n) {
            this.setBackgroundDrawable(AppCompatResources.getDrawable(this.getContext(), n));
        }
    }
    
    protected static final class PanelFeatureState
    {
        int background;
        View createdPanelView;
        ViewGroup decorView;
        int featureId;
        Bundle frozenActionViewState;
        Bundle frozenMenuState;
        int gravity;
        boolean isHandled;
        boolean isOpen;
        boolean isPrepared;
        ListMenuPresenter listMenuPresenter;
        Context listPresenterContext;
        MenuBuilder menu;
        public boolean qwertyMode;
        boolean refreshDecorView;
        boolean refreshMenuContent;
        View shownPanelView;
        boolean wasLastOpen;
        int windowAnimations;
        int x;
        int y;
        
        PanelFeatureState(final int featureId) {
            this.featureId = featureId;
            this.refreshDecorView = false;
        }
        
        void applyFrozenState() {
            final MenuBuilder menu = this.menu;
            if (menu != null) {
                final Bundle frozenMenuState = this.frozenMenuState;
                if (frozenMenuState != null) {
                    menu.restorePresenterStates(frozenMenuState);
                    this.frozenMenuState = null;
                }
            }
        }
        
        public void clearMenuPresenters() {
            final MenuBuilder menu = this.menu;
            if (menu != null) {
                menu.removeMenuPresenter(this.listMenuPresenter);
            }
            this.listMenuPresenter = null;
        }
        
        MenuView getListMenuView(final MenuPresenter.Callback callback) {
            if (this.menu == null) {
                return null;
            }
            if (this.listMenuPresenter == null) {
                (this.listMenuPresenter = new ListMenuPresenter(this.listPresenterContext, R.layout.abc_list_menu_item_layout)).setCallback(callback);
                this.menu.addMenuPresenter(this.listMenuPresenter);
            }
            return this.listMenuPresenter.getMenuView(this.decorView);
        }
        
        public boolean hasPanelItems() {
            final View shownPanelView = this.shownPanelView;
            boolean b = false;
            if (shownPanelView == null) {
                return false;
            }
            if (this.createdPanelView != null) {
                return true;
            }
            if (this.listMenuPresenter.getAdapter().getCount() > 0) {
                b = true;
            }
            return b;
        }
        
        void onRestoreInstanceState(final Parcelable parcelable) {
            final SavedState savedState = (SavedState)parcelable;
            this.featureId = savedState.featureId;
            this.wasLastOpen = savedState.isOpen;
            this.frozenMenuState = savedState.menuState;
            this.shownPanelView = null;
            this.decorView = null;
        }
        
        Parcelable onSaveInstanceState() {
            final SavedState savedState = new SavedState();
            savedState.featureId = this.featureId;
            savedState.isOpen = this.isOpen;
            if (this.menu != null) {
                savedState.menuState = new Bundle();
                this.menu.savePresenterStates(savedState.menuState);
            }
            return (Parcelable)savedState;
        }
        
        void setMenu(final MenuBuilder menu) {
            final MenuBuilder menu2 = this.menu;
            if (menu == menu2) {
                return;
            }
            if (menu2 != null) {
                menu2.removeMenuPresenter(this.listMenuPresenter);
            }
            if ((this.menu = menu) != null) {
                final ListMenuPresenter listMenuPresenter = this.listMenuPresenter;
                if (listMenuPresenter != null) {
                    menu.addMenuPresenter(listMenuPresenter);
                }
            }
        }
        
        void setStyle(final Context context) {
            final TypedValue typedValue = new TypedValue();
            final Resources$Theme theme = context.getResources().newTheme();
            theme.setTo(context.getTheme());
            theme.resolveAttribute(R.attr.actionBarPopupTheme, typedValue, true);
            if (typedValue.resourceId != 0) {
                theme.applyStyle(typedValue.resourceId, true);
            }
            theme.resolveAttribute(R.attr.panelMenuListTheme, typedValue, true);
            if (typedValue.resourceId != 0) {
                theme.applyStyle(typedValue.resourceId, true);
            }
            else {
                theme.applyStyle(R.style.Theme_AppCompat_CompactMenu, true);
            }
            final ContextThemeWrapper listPresenterContext = new ContextThemeWrapper(context, 0);
            ((Context)listPresenterContext).getTheme().setTo(theme);
            this.listPresenterContext = (Context)listPresenterContext;
            final TypedArray obtainStyledAttributes = ((Context)listPresenterContext).obtainStyledAttributes(R.styleable.AppCompatTheme);
            this.background = obtainStyledAttributes.getResourceId(R.styleable.AppCompatTheme_panelBackground, 0);
            this.windowAnimations = obtainStyledAttributes.getResourceId(R.styleable.AppCompatTheme_android_windowAnimationStyle, 0);
            obtainStyledAttributes.recycle();
        }
        
        private static class SavedState implements Parcelable
        {
            public static final Parcelable$Creator<SavedState> CREATOR;
            int featureId;
            boolean isOpen;
            Bundle menuState;
            
            static {
                CREATOR = (Parcelable$Creator)new Parcelable$ClassLoaderCreator<SavedState>() {
                    public SavedState createFromParcel(final Parcel parcel) {
                        return SavedState.readFromParcel(parcel, null);
                    }
                    
                    public SavedState createFromParcel(final Parcel parcel, final ClassLoader classLoader) {
                        return SavedState.readFromParcel(parcel, classLoader);
                    }
                    
                    public SavedState[] newArray(final int n) {
                        return new SavedState[n];
                    }
                };
            }
            
            SavedState() {
            }
            
            static SavedState readFromParcel(final Parcel parcel, final ClassLoader classLoader) {
                final SavedState savedState = new SavedState();
                savedState.featureId = parcel.readInt();
                final int int1 = parcel.readInt();
                boolean isOpen = true;
                if (int1 != 1) {
                    isOpen = false;
                }
                savedState.isOpen = isOpen;
                if (isOpen) {
                    savedState.menuState = parcel.readBundle(classLoader);
                }
                return savedState;
            }
            
            public int describeContents() {
                return 0;
            }
            
            public void writeToParcel(final Parcel parcel, final int n) {
                parcel.writeInt(this.featureId);
                parcel.writeInt((int)(this.isOpen ? 1 : 0));
                if (this.isOpen) {
                    parcel.writeBundle(this.menuState);
                }
            }
        }
    }
    
    private final class PanelMenuPresenterCallback implements MenuPresenter.Callback
    {
        PanelMenuPresenterCallback() {
        }
        
        @Override
        public void onCloseMenu(MenuBuilder menuBuilder, final boolean b) {
            final Object rootMenu = menuBuilder.getRootMenu();
            final boolean b2 = rootMenu != menuBuilder;
            final AppCompatDelegateImpl this$0 = AppCompatDelegateImpl.this;
            if (b2) {
                menuBuilder = (MenuBuilder)rootMenu;
            }
            final PanelFeatureState menuPanel = this$0.findMenuPanel((Menu)menuBuilder);
            if (menuPanel != null) {
                if (b2) {
                    AppCompatDelegateImpl.this.callOnPanelClosed(menuPanel.featureId, menuPanel, (Menu)rootMenu);
                    AppCompatDelegateImpl.this.closePanel(menuPanel, true);
                }
                else {
                    AppCompatDelegateImpl.this.closePanel(menuPanel, b);
                }
            }
        }
        
        @Override
        public boolean onOpenSubMenu(final MenuBuilder menuBuilder) {
            if (menuBuilder == null && AppCompatDelegateImpl.this.mHasActionBar) {
                final Window$Callback windowCallback = AppCompatDelegateImpl.this.getWindowCallback();
                if (windowCallback != null && !AppCompatDelegateImpl.this.mIsDestroyed) {
                    windowCallback.onMenuOpened(108, (Menu)menuBuilder);
                }
            }
            return true;
        }
    }
}
