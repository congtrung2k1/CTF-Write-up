// 
// Decompiled by Procyon v0.5.36
// 

package androidx.fragment.app;

import android.os.Parcel;
import android.os.Parcelable$ClassLoaderCreator;
import android.os.Parcelable$Creator;
import androidx.core.util.DebugUtils;
import android.os.Looper;
import android.content.IntentSender$SendIntentException;
import android.content.IntentSender;
import android.util.AttributeSet;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.ContextMenu$ContextMenuInfo;
import android.view.ContextMenu;
import android.view.animation.Animation;
import android.view.MenuItem;
import android.content.res.Configuration;
import android.app.Activity;
import android.content.Intent;
import androidx.lifecycle.LiveData;
import android.content.res.Resources;
import androidx.lifecycle.Lifecycle;
import androidx.core.view.LayoutInflaterCompat;
import androidx.core.app.SharedElementCallback;
import android.animation.Animator;
import androidx.loader.app.LoaderManager;
import java.io.PrintWriter;
import java.io.FileDescriptor;
import java.lang.reflect.InvocationTargetException;
import android.content.Context;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.MutableLiveData;
import android.os.Parcelable;
import android.util.SparseArray;
import androidx.lifecycle.LifecycleRegistry;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import androidx.collection.SimpleArrayMap;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.lifecycle.LifecycleOwner;
import android.view.View$OnCreateContextMenuListener;
import android.content.ComponentCallbacks;

public class Fragment implements ComponentCallbacks, View$OnCreateContextMenuListener, LifecycleOwner, ViewModelStoreOwner
{
    static final int ACTIVITY_CREATED = 2;
    static final int CREATED = 1;
    static final int INITIALIZING = 0;
    static final int RESUMED = 4;
    static final int STARTED = 3;
    static final Object USE_DEFAULT_TRANSITION;
    private static final SimpleArrayMap<String, Class<?>> sClassMap;
    boolean mAdded;
    AnimationInfo mAnimationInfo;
    Bundle mArguments;
    int mBackStackNesting;
    boolean mCalled;
    FragmentManagerImpl mChildFragmentManager;
    FragmentManagerNonConfig mChildNonConfig;
    ViewGroup mContainer;
    int mContainerId;
    boolean mDeferStart;
    boolean mDetached;
    int mFragmentId;
    FragmentManagerImpl mFragmentManager;
    boolean mFromLayout;
    boolean mHasMenu;
    boolean mHidden;
    boolean mHiddenChanged;
    FragmentHostCallback mHost;
    boolean mInLayout;
    int mIndex;
    View mInnerView;
    boolean mIsCreated;
    boolean mIsNewlyAdded;
    LayoutInflater mLayoutInflater;
    LifecycleRegistry mLifecycleRegistry;
    boolean mMenuVisible;
    Fragment mParentFragment;
    boolean mPerformedCreateView;
    float mPostponedAlpha;
    boolean mRemoving;
    boolean mRestored;
    boolean mRetainInstance;
    boolean mRetaining;
    Bundle mSavedFragmentState;
    Boolean mSavedUserVisibleHint;
    SparseArray<Parcelable> mSavedViewState;
    int mState;
    String mTag;
    Fragment mTarget;
    int mTargetIndex;
    int mTargetRequestCode;
    boolean mUserVisibleHint;
    View mView;
    LifecycleOwner mViewLifecycleOwner;
    MutableLiveData<LifecycleOwner> mViewLifecycleOwnerLiveData;
    LifecycleRegistry mViewLifecycleRegistry;
    ViewModelStore mViewModelStore;
    String mWho;
    
    static {
        sClassMap = new SimpleArrayMap<String, Class<?>>();
        USE_DEFAULT_TRANSITION = new Object();
    }
    
    public Fragment() {
        this.mState = 0;
        this.mIndex = -1;
        this.mTargetIndex = -1;
        this.mMenuVisible = true;
        this.mUserVisibleHint = true;
        this.mLifecycleRegistry = new LifecycleRegistry(this);
        this.mViewLifecycleOwnerLiveData = new MutableLiveData<LifecycleOwner>();
    }
    
    private AnimationInfo ensureAnimationInfo() {
        if (this.mAnimationInfo == null) {
            this.mAnimationInfo = new AnimationInfo();
        }
        return this.mAnimationInfo;
    }
    
    public static Fragment instantiate(final Context context, final String s) {
        return instantiate(context, s, null);
    }
    
    public static Fragment instantiate(final Context context, final String s, final Bundle arguments) {
        try {
            Class<?> loadClass;
            if ((loadClass = Fragment.sClassMap.get(s)) == null) {
                loadClass = context.getClassLoader().loadClass(s);
                Fragment.sClassMap.put(s, loadClass);
            }
            final Fragment fragment = (Fragment)loadClass.getConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
            if (arguments != null) {
                arguments.setClassLoader(fragment.getClass().getClassLoader());
                fragment.setArguments(arguments);
            }
            return fragment;
        }
        catch (InvocationTargetException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unable to instantiate fragment ");
            sb.append(s);
            sb.append(": calling Fragment constructor caused an exception");
            throw new InstantiationException(sb.toString(), ex);
        }
        catch (NoSuchMethodException ex2) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Unable to instantiate fragment ");
            sb2.append(s);
            sb2.append(": could not find Fragment constructor");
            throw new InstantiationException(sb2.toString(), ex2);
        }
        catch (IllegalAccessException ex3) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Unable to instantiate fragment ");
            sb3.append(s);
            sb3.append(": make sure class name exists, is public, and has an");
            sb3.append(" empty constructor that is public");
            throw new InstantiationException(sb3.toString(), ex3);
        }
        catch (java.lang.InstantiationException ex4) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Unable to instantiate fragment ");
            sb4.append(s);
            sb4.append(": make sure class name exists, is public, and has an");
            sb4.append(" empty constructor that is public");
            throw new InstantiationException(sb4.toString(), ex4);
        }
        catch (ClassNotFoundException ex5) {
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("Unable to instantiate fragment ");
            sb5.append(s);
            sb5.append(": make sure class name exists, is public, and has an");
            sb5.append(" empty constructor that is public");
            throw new InstantiationException(sb5.toString(), ex5);
        }
    }
    
    static boolean isSupportFragmentClass(final Context context, final String name) {
        try {
            Class<?> loadClass;
            if ((loadClass = Fragment.sClassMap.get(name)) == null) {
                loadClass = context.getClassLoader().loadClass(name);
                Fragment.sClassMap.put(name, loadClass);
            }
            return Fragment.class.isAssignableFrom(loadClass);
        }
        catch (ClassNotFoundException ex) {
            return false;
        }
    }
    
    void callStartTransitionListener() {
        final AnimationInfo mAnimationInfo = this.mAnimationInfo;
        Object mStartEnterTransitionListener;
        if (mAnimationInfo == null) {
            mStartEnterTransitionListener = null;
        }
        else {
            mAnimationInfo.mEnterTransitionPostponed = false;
            mStartEnterTransitionListener = this.mAnimationInfo.mStartEnterTransitionListener;
            this.mAnimationInfo.mStartEnterTransitionListener = null;
        }
        if (mStartEnterTransitionListener != null) {
            ((OnStartEnterTransitionListener)mStartEnterTransitionListener).onStartEnterTransition();
        }
    }
    
    public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
        printWriter.print(s);
        printWriter.print("mFragmentId=#");
        printWriter.print(Integer.toHexString(this.mFragmentId));
        printWriter.print(" mContainerId=#");
        printWriter.print(Integer.toHexString(this.mContainerId));
        printWriter.print(" mTag=");
        printWriter.println(this.mTag);
        printWriter.print(s);
        printWriter.print("mState=");
        printWriter.print(this.mState);
        printWriter.print(" mIndex=");
        printWriter.print(this.mIndex);
        printWriter.print(" mWho=");
        printWriter.print(this.mWho);
        printWriter.print(" mBackStackNesting=");
        printWriter.println(this.mBackStackNesting);
        printWriter.print(s);
        printWriter.print("mAdded=");
        printWriter.print(this.mAdded);
        printWriter.print(" mRemoving=");
        printWriter.print(this.mRemoving);
        printWriter.print(" mFromLayout=");
        printWriter.print(this.mFromLayout);
        printWriter.print(" mInLayout=");
        printWriter.println(this.mInLayout);
        printWriter.print(s);
        printWriter.print("mHidden=");
        printWriter.print(this.mHidden);
        printWriter.print(" mDetached=");
        printWriter.print(this.mDetached);
        printWriter.print(" mMenuVisible=");
        printWriter.print(this.mMenuVisible);
        printWriter.print(" mHasMenu=");
        printWriter.println(this.mHasMenu);
        printWriter.print(s);
        printWriter.print("mRetainInstance=");
        printWriter.print(this.mRetainInstance);
        printWriter.print(" mRetaining=");
        printWriter.print(this.mRetaining);
        printWriter.print(" mUserVisibleHint=");
        printWriter.println(this.mUserVisibleHint);
        if (this.mFragmentManager != null) {
            printWriter.print(s);
            printWriter.print("mFragmentManager=");
            printWriter.println(this.mFragmentManager);
        }
        if (this.mHost != null) {
            printWriter.print(s);
            printWriter.print("mHost=");
            printWriter.println(this.mHost);
        }
        if (this.mParentFragment != null) {
            printWriter.print(s);
            printWriter.print("mParentFragment=");
            printWriter.println(this.mParentFragment);
        }
        if (this.mArguments != null) {
            printWriter.print(s);
            printWriter.print("mArguments=");
            printWriter.println(this.mArguments);
        }
        if (this.mSavedFragmentState != null) {
            printWriter.print(s);
            printWriter.print("mSavedFragmentState=");
            printWriter.println(this.mSavedFragmentState);
        }
        if (this.mSavedViewState != null) {
            printWriter.print(s);
            printWriter.print("mSavedViewState=");
            printWriter.println(this.mSavedViewState);
        }
        if (this.mTarget != null) {
            printWriter.print(s);
            printWriter.print("mTarget=");
            printWriter.print(this.mTarget);
            printWriter.print(" mTargetRequestCode=");
            printWriter.println(this.mTargetRequestCode);
        }
        if (this.getNextAnim() != 0) {
            printWriter.print(s);
            printWriter.print("mNextAnim=");
            printWriter.println(this.getNextAnim());
        }
        if (this.mContainer != null) {
            printWriter.print(s);
            printWriter.print("mContainer=");
            printWriter.println(this.mContainer);
        }
        if (this.mView != null) {
            printWriter.print(s);
            printWriter.print("mView=");
            printWriter.println(this.mView);
        }
        if (this.mInnerView != null) {
            printWriter.print(s);
            printWriter.print("mInnerView=");
            printWriter.println(this.mView);
        }
        if (this.getAnimatingAway() != null) {
            printWriter.print(s);
            printWriter.print("mAnimatingAway=");
            printWriter.println(this.getAnimatingAway());
            printWriter.print(s);
            printWriter.print("mStateAfterAnimating=");
            printWriter.println(this.getStateAfterAnimating());
        }
        if (this.getContext() != null) {
            LoaderManager.getInstance(this).dump(s, fileDescriptor, printWriter, array);
        }
        if (this.mChildFragmentManager != null) {
            printWriter.print(s);
            final StringBuilder sb = new StringBuilder();
            sb.append("Child ");
            sb.append(this.mChildFragmentManager);
            sb.append(":");
            printWriter.println(sb.toString());
            final FragmentManagerImpl mChildFragmentManager = this.mChildFragmentManager;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append("  ");
            mChildFragmentManager.dump(sb2.toString(), fileDescriptor, printWriter, array);
        }
    }
    
    @Override
    public final boolean equals(final Object obj) {
        return super.equals(obj);
    }
    
    Fragment findFragmentByWho(final String s) {
        if (s.equals(this.mWho)) {
            return this;
        }
        final FragmentManagerImpl mChildFragmentManager = this.mChildFragmentManager;
        if (mChildFragmentManager != null) {
            return mChildFragmentManager.findFragmentByWho(s);
        }
        return null;
    }
    
    public final FragmentActivity getActivity() {
        final FragmentHostCallback mHost = this.mHost;
        FragmentActivity fragmentActivity;
        if (mHost == null) {
            fragmentActivity = null;
        }
        else {
            fragmentActivity = (FragmentActivity)mHost.getActivity();
        }
        return fragmentActivity;
    }
    
    public boolean getAllowEnterTransitionOverlap() {
        final AnimationInfo mAnimationInfo = this.mAnimationInfo;
        return mAnimationInfo == null || mAnimationInfo.mAllowEnterTransitionOverlap == null || this.mAnimationInfo.mAllowEnterTransitionOverlap;
    }
    
    public boolean getAllowReturnTransitionOverlap() {
        final AnimationInfo mAnimationInfo = this.mAnimationInfo;
        return mAnimationInfo == null || mAnimationInfo.mAllowReturnTransitionOverlap == null || this.mAnimationInfo.mAllowReturnTransitionOverlap;
    }
    
    View getAnimatingAway() {
        final AnimationInfo mAnimationInfo = this.mAnimationInfo;
        if (mAnimationInfo == null) {
            return null;
        }
        return mAnimationInfo.mAnimatingAway;
    }
    
    Animator getAnimator() {
        final AnimationInfo mAnimationInfo = this.mAnimationInfo;
        if (mAnimationInfo == null) {
            return null;
        }
        return mAnimationInfo.mAnimator;
    }
    
    public final Bundle getArguments() {
        return this.mArguments;
    }
    
    public final FragmentManager getChildFragmentManager() {
        if (this.mChildFragmentManager == null) {
            this.instantiateChildFragmentManager();
            final int mState = this.mState;
            if (mState >= 4) {
                this.mChildFragmentManager.dispatchResume();
            }
            else if (mState >= 3) {
                this.mChildFragmentManager.dispatchStart();
            }
            else if (mState >= 2) {
                this.mChildFragmentManager.dispatchActivityCreated();
            }
            else if (mState >= 1) {
                this.mChildFragmentManager.dispatchCreate();
            }
        }
        return this.mChildFragmentManager;
    }
    
    public Context getContext() {
        final FragmentHostCallback mHost = this.mHost;
        Context context;
        if (mHost == null) {
            context = null;
        }
        else {
            context = mHost.getContext();
        }
        return context;
    }
    
    public Object getEnterTransition() {
        final AnimationInfo mAnimationInfo = this.mAnimationInfo;
        if (mAnimationInfo == null) {
            return null;
        }
        return mAnimationInfo.mEnterTransition;
    }
    
    SharedElementCallback getEnterTransitionCallback() {
        final AnimationInfo mAnimationInfo = this.mAnimationInfo;
        if (mAnimationInfo == null) {
            return null;
        }
        return mAnimationInfo.mEnterTransitionCallback;
    }
    
    public Object getExitTransition() {
        final AnimationInfo mAnimationInfo = this.mAnimationInfo;
        if (mAnimationInfo == null) {
            return null;
        }
        return mAnimationInfo.mExitTransition;
    }
    
    SharedElementCallback getExitTransitionCallback() {
        final AnimationInfo mAnimationInfo = this.mAnimationInfo;
        if (mAnimationInfo == null) {
            return null;
        }
        return mAnimationInfo.mExitTransitionCallback;
    }
    
    public final FragmentManager getFragmentManager() {
        return this.mFragmentManager;
    }
    
    public final Object getHost() {
        final FragmentHostCallback mHost = this.mHost;
        Object onGetHost;
        if (mHost == null) {
            onGetHost = null;
        }
        else {
            onGetHost = mHost.onGetHost();
        }
        return onGetHost;
    }
    
    public final int getId() {
        return this.mFragmentId;
    }
    
    public final LayoutInflater getLayoutInflater() {
        final LayoutInflater mLayoutInflater = this.mLayoutInflater;
        if (mLayoutInflater == null) {
            return this.performGetLayoutInflater(null);
        }
        return mLayoutInflater;
    }
    
    @Deprecated
    public LayoutInflater getLayoutInflater(final Bundle bundle) {
        final FragmentHostCallback mHost = this.mHost;
        if (mHost != null) {
            final LayoutInflater onGetLayoutInflater = mHost.onGetLayoutInflater();
            this.getChildFragmentManager();
            LayoutInflaterCompat.setFactory2(onGetLayoutInflater, this.mChildFragmentManager.getLayoutInflaterFactory());
            return onGetLayoutInflater;
        }
        throw new IllegalStateException("onGetLayoutInflater() cannot be executed until the Fragment is attached to the FragmentManager.");
    }
    
    public Lifecycle getLifecycle() {
        return this.mLifecycleRegistry;
    }
    
    @Deprecated
    public LoaderManager getLoaderManager() {
        return LoaderManager.getInstance(this);
    }
    
    int getNextAnim() {
        final AnimationInfo mAnimationInfo = this.mAnimationInfo;
        if (mAnimationInfo == null) {
            return 0;
        }
        return mAnimationInfo.mNextAnim;
    }
    
    int getNextTransition() {
        final AnimationInfo mAnimationInfo = this.mAnimationInfo;
        if (mAnimationInfo == null) {
            return 0;
        }
        return mAnimationInfo.mNextTransition;
    }
    
    int getNextTransitionStyle() {
        final AnimationInfo mAnimationInfo = this.mAnimationInfo;
        if (mAnimationInfo == null) {
            return 0;
        }
        return mAnimationInfo.mNextTransitionStyle;
    }
    
    public final Fragment getParentFragment() {
        return this.mParentFragment;
    }
    
    public Object getReenterTransition() {
        final AnimationInfo mAnimationInfo = this.mAnimationInfo;
        if (mAnimationInfo == null) {
            return null;
        }
        Object o;
        if (mAnimationInfo.mReenterTransition == Fragment.USE_DEFAULT_TRANSITION) {
            o = this.getExitTransition();
        }
        else {
            o = this.mAnimationInfo.mReenterTransition;
        }
        return o;
    }
    
    public final Resources getResources() {
        return this.requireContext().getResources();
    }
    
    public final boolean getRetainInstance() {
        return this.mRetainInstance;
    }
    
    public Object getReturnTransition() {
        final AnimationInfo mAnimationInfo = this.mAnimationInfo;
        if (mAnimationInfo == null) {
            return null;
        }
        Object o;
        if (mAnimationInfo.mReturnTransition == Fragment.USE_DEFAULT_TRANSITION) {
            o = this.getEnterTransition();
        }
        else {
            o = this.mAnimationInfo.mReturnTransition;
        }
        return o;
    }
    
    public Object getSharedElementEnterTransition() {
        final AnimationInfo mAnimationInfo = this.mAnimationInfo;
        if (mAnimationInfo == null) {
            return null;
        }
        return mAnimationInfo.mSharedElementEnterTransition;
    }
    
    public Object getSharedElementReturnTransition() {
        final AnimationInfo mAnimationInfo = this.mAnimationInfo;
        if (mAnimationInfo == null) {
            return null;
        }
        Object o;
        if (mAnimationInfo.mSharedElementReturnTransition == Fragment.USE_DEFAULT_TRANSITION) {
            o = this.getSharedElementEnterTransition();
        }
        else {
            o = this.mAnimationInfo.mSharedElementReturnTransition;
        }
        return o;
    }
    
    int getStateAfterAnimating() {
        final AnimationInfo mAnimationInfo = this.mAnimationInfo;
        if (mAnimationInfo == null) {
            return 0;
        }
        return mAnimationInfo.mStateAfterAnimating;
    }
    
    public final String getString(final int n) {
        return this.getResources().getString(n);
    }
    
    public final String getString(final int n, final Object... array) {
        return this.getResources().getString(n, array);
    }
    
    public final String getTag() {
        return this.mTag;
    }
    
    public final Fragment getTargetFragment() {
        return this.mTarget;
    }
    
    public final int getTargetRequestCode() {
        return this.mTargetRequestCode;
    }
    
    public final CharSequence getText(final int n) {
        return this.getResources().getText(n);
    }
    
    public boolean getUserVisibleHint() {
        return this.mUserVisibleHint;
    }
    
    public View getView() {
        return this.mView;
    }
    
    public LifecycleOwner getViewLifecycleOwner() {
        final LifecycleOwner mViewLifecycleOwner = this.mViewLifecycleOwner;
        if (mViewLifecycleOwner != null) {
            return mViewLifecycleOwner;
        }
        throw new IllegalStateException("Can't access the Fragment View's LifecycleOwner when getView() is null i.e., before onCreateView() or after onDestroyView()");
    }
    
    public LiveData<LifecycleOwner> getViewLifecycleOwnerLiveData() {
        return this.mViewLifecycleOwnerLiveData;
    }
    
    public ViewModelStore getViewModelStore() {
        if (this.getContext() != null) {
            if (this.mViewModelStore == null) {
                this.mViewModelStore = new ViewModelStore();
            }
            return this.mViewModelStore;
        }
        throw new IllegalStateException("Can't access ViewModels from detached fragment");
    }
    
    public final boolean hasOptionsMenu() {
        return this.mHasMenu;
    }
    
    @Override
    public final int hashCode() {
        return super.hashCode();
    }
    
    void initState() {
        this.mIndex = -1;
        this.mWho = null;
        this.mAdded = false;
        this.mRemoving = false;
        this.mFromLayout = false;
        this.mInLayout = false;
        this.mRestored = false;
        this.mBackStackNesting = 0;
        this.mFragmentManager = null;
        this.mChildFragmentManager = null;
        this.mHost = null;
        this.mFragmentId = 0;
        this.mContainerId = 0;
        this.mTag = null;
        this.mHidden = false;
        this.mDetached = false;
        this.mRetaining = false;
    }
    
    void instantiateChildFragmentManager() {
        if (this.mHost != null) {
            (this.mChildFragmentManager = new FragmentManagerImpl()).attachController(this.mHost, new FragmentContainer() {
                @Override
                public Fragment instantiate(final Context context, final String s, final Bundle bundle) {
                    return Fragment.this.mHost.instantiate(context, s, bundle);
                }
                
                @Override
                public View onFindViewById(final int n) {
                    if (Fragment.this.mView != null) {
                        return Fragment.this.mView.findViewById(n);
                    }
                    throw new IllegalStateException("Fragment does not have a view");
                }
                
                @Override
                public boolean onHasView() {
                    return Fragment.this.mView != null;
                }
            }, this);
            return;
        }
        throw new IllegalStateException("Fragment has not been attached yet.");
    }
    
    public final boolean isAdded() {
        return this.mHost != null && this.mAdded;
    }
    
    public final boolean isDetached() {
        return this.mDetached;
    }
    
    public final boolean isHidden() {
        return this.mHidden;
    }
    
    boolean isHideReplaced() {
        final AnimationInfo mAnimationInfo = this.mAnimationInfo;
        return mAnimationInfo != null && mAnimationInfo.mIsHideReplaced;
    }
    
    final boolean isInBackStack() {
        return this.mBackStackNesting > 0;
    }
    
    public final boolean isInLayout() {
        return this.mInLayout;
    }
    
    public final boolean isMenuVisible() {
        return this.mMenuVisible;
    }
    
    boolean isPostponed() {
        final AnimationInfo mAnimationInfo = this.mAnimationInfo;
        return mAnimationInfo != null && mAnimationInfo.mEnterTransitionPostponed;
    }
    
    public final boolean isRemoving() {
        return this.mRemoving;
    }
    
    public final boolean isResumed() {
        return this.mState >= 4;
    }
    
    public final boolean isStateSaved() {
        final FragmentManagerImpl mFragmentManager = this.mFragmentManager;
        return mFragmentManager != null && mFragmentManager.isStateSaved();
    }
    
    public final boolean isVisible() {
        if (this.isAdded() && !this.isHidden()) {
            final View mView = this.mView;
            if (mView != null && mView.getWindowToken() != null && this.mView.getVisibility() == 0) {
                return true;
            }
        }
        return false;
    }
    
    void noteStateNotSaved() {
        final FragmentManagerImpl mChildFragmentManager = this.mChildFragmentManager;
        if (mChildFragmentManager != null) {
            mChildFragmentManager.noteStateNotSaved();
        }
    }
    
    public void onActivityCreated(final Bundle bundle) {
        this.mCalled = true;
    }
    
    public void onActivityResult(final int n, final int n2, final Intent intent) {
    }
    
    @Deprecated
    public void onAttach(final Activity activity) {
        this.mCalled = true;
    }
    
    public void onAttach(final Context context) {
        this.mCalled = true;
        final FragmentHostCallback mHost = this.mHost;
        Activity activity;
        if (mHost == null) {
            activity = null;
        }
        else {
            activity = mHost.getActivity();
        }
        if (activity != null) {
            this.mCalled = false;
            this.onAttach(activity);
        }
    }
    
    public void onAttachFragment(final Fragment fragment) {
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        this.mCalled = true;
    }
    
    public boolean onContextItemSelected(final MenuItem menuItem) {
        return false;
    }
    
    public void onCreate(final Bundle bundle) {
        this.mCalled = true;
        this.restoreChildFragmentState(bundle);
        final FragmentManagerImpl mChildFragmentManager = this.mChildFragmentManager;
        if (mChildFragmentManager != null && !mChildFragmentManager.isStateAtLeast(1)) {
            this.mChildFragmentManager.dispatchCreate();
        }
    }
    
    public Animation onCreateAnimation(final int n, final boolean b, final int n2) {
        return null;
    }
    
    public Animator onCreateAnimator(final int n, final boolean b, final int n2) {
        return null;
    }
    
    public void onCreateContextMenu(final ContextMenu contextMenu, final View view, final ContextMenu$ContextMenuInfo contextMenu$ContextMenuInfo) {
        this.getActivity().onCreateContextMenu(contextMenu, view, contextMenu$ContextMenuInfo);
    }
    
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater menuInflater) {
    }
    
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return null;
    }
    
    public void onDestroy() {
        boolean b = true;
        this.mCalled = true;
        final FragmentActivity activity = this.getActivity();
        if (activity == null || !activity.isChangingConfigurations()) {
            b = false;
        }
        final ViewModelStore mViewModelStore = this.mViewModelStore;
        if (mViewModelStore != null && !b) {
            mViewModelStore.clear();
        }
    }
    
    public void onDestroyOptionsMenu() {
    }
    
    public void onDestroyView() {
        this.mCalled = true;
    }
    
    public void onDetach() {
        this.mCalled = true;
    }
    
    public LayoutInflater onGetLayoutInflater(final Bundle bundle) {
        return this.getLayoutInflater(bundle);
    }
    
    public void onHiddenChanged(final boolean b) {
    }
    
    @Deprecated
    public void onInflate(final Activity activity, final AttributeSet set, final Bundle bundle) {
        this.mCalled = true;
    }
    
    public void onInflate(final Context context, final AttributeSet set, final Bundle bundle) {
        this.mCalled = true;
        final FragmentHostCallback mHost = this.mHost;
        Activity activity;
        if (mHost == null) {
            activity = null;
        }
        else {
            activity = mHost.getActivity();
        }
        if (activity != null) {
            this.mCalled = false;
            this.onInflate(activity, set, bundle);
        }
    }
    
    public void onLowMemory() {
        this.mCalled = true;
    }
    
    public void onMultiWindowModeChanged(final boolean b) {
    }
    
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        return false;
    }
    
    public void onOptionsMenuClosed(final Menu menu) {
    }
    
    public void onPause() {
        this.mCalled = true;
    }
    
    public void onPictureInPictureModeChanged(final boolean b) {
    }
    
    public void onPrepareOptionsMenu(final Menu menu) {
    }
    
    public void onRequestPermissionsResult(final int n, final String[] array, final int[] array2) {
    }
    
    public void onResume() {
        this.mCalled = true;
    }
    
    public void onSaveInstanceState(final Bundle bundle) {
    }
    
    public void onStart() {
        this.mCalled = true;
    }
    
    public void onStop() {
        this.mCalled = true;
    }
    
    public void onViewCreated(final View view, final Bundle bundle) {
    }
    
    public void onViewStateRestored(final Bundle bundle) {
        this.mCalled = true;
    }
    
    FragmentManager peekChildFragmentManager() {
        return this.mChildFragmentManager;
    }
    
    void performActivityCreated(final Bundle bundle) {
        final FragmentManagerImpl mChildFragmentManager = this.mChildFragmentManager;
        if (mChildFragmentManager != null) {
            mChildFragmentManager.noteStateNotSaved();
        }
        this.mState = 2;
        this.mCalled = false;
        this.onActivityCreated(bundle);
        if (this.mCalled) {
            final FragmentManagerImpl mChildFragmentManager2 = this.mChildFragmentManager;
            if (mChildFragmentManager2 != null) {
                mChildFragmentManager2.dispatchActivityCreated();
            }
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" did not call through to super.onActivityCreated()");
        throw new SuperNotCalledException(sb.toString());
    }
    
    void performConfigurationChanged(final Configuration configuration) {
        this.onConfigurationChanged(configuration);
        final FragmentManagerImpl mChildFragmentManager = this.mChildFragmentManager;
        if (mChildFragmentManager != null) {
            mChildFragmentManager.dispatchConfigurationChanged(configuration);
        }
    }
    
    boolean performContextItemSelected(final MenuItem menuItem) {
        if (!this.mHidden) {
            if (this.onContextItemSelected(menuItem)) {
                return true;
            }
            final FragmentManagerImpl mChildFragmentManager = this.mChildFragmentManager;
            if (mChildFragmentManager != null && mChildFragmentManager.dispatchContextItemSelected(menuItem)) {
                return true;
            }
        }
        return false;
    }
    
    void performCreate(final Bundle bundle) {
        final FragmentManagerImpl mChildFragmentManager = this.mChildFragmentManager;
        if (mChildFragmentManager != null) {
            mChildFragmentManager.noteStateNotSaved();
        }
        this.mState = 1;
        this.mCalled = false;
        this.onCreate(bundle);
        this.mIsCreated = true;
        if (this.mCalled) {
            this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" did not call through to super.onCreate()");
        throw new SuperNotCalledException(sb.toString());
    }
    
    boolean performCreateOptionsMenu(final Menu menu, final MenuInflater menuInflater) {
        int n = 0;
        final boolean b = false;
        if (!this.mHidden) {
            int n2 = b ? 1 : 0;
            if (this.mHasMenu) {
                n2 = (b ? 1 : 0);
                if (this.mMenuVisible) {
                    n2 = 1;
                    this.onCreateOptionsMenu(menu, menuInflater);
                }
            }
            final FragmentManagerImpl mChildFragmentManager = this.mChildFragmentManager;
            n = n2;
            if (mChildFragmentManager != null) {
                n = (n2 | (mChildFragmentManager.dispatchCreateOptionsMenu(menu, menuInflater) ? 1 : 0));
            }
        }
        return n != 0;
    }
    
    void performCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final FragmentManagerImpl mChildFragmentManager = this.mChildFragmentManager;
        if (mChildFragmentManager != null) {
            mChildFragmentManager.noteStateNotSaved();
        }
        this.mPerformedCreateView = true;
        this.mViewLifecycleOwner = new LifecycleOwner() {
            @Override
            public Lifecycle getLifecycle() {
                if (Fragment.this.mViewLifecycleRegistry == null) {
                    Fragment.this.mViewLifecycleRegistry = new LifecycleRegistry(Fragment.this.mViewLifecycleOwner);
                }
                return Fragment.this.mViewLifecycleRegistry;
            }
        };
        this.mViewLifecycleRegistry = null;
        if ((this.mView = this.onCreateView(layoutInflater, viewGroup, bundle)) != null) {
            this.mViewLifecycleOwner.getLifecycle();
            this.mViewLifecycleOwnerLiveData.setValue(this.mViewLifecycleOwner);
        }
        else {
            if (this.mViewLifecycleRegistry != null) {
                throw new IllegalStateException("Called getViewLifecycleOwner() but onCreateView() returned null");
            }
            this.mViewLifecycleOwner = null;
        }
    }
    
    void performDestroy() {
        this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        final FragmentManagerImpl mChildFragmentManager = this.mChildFragmentManager;
        if (mChildFragmentManager != null) {
            mChildFragmentManager.dispatchDestroy();
        }
        this.mState = 0;
        this.mCalled = false;
        this.mIsCreated = false;
        this.onDestroy();
        if (this.mCalled) {
            this.mChildFragmentManager = null;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" did not call through to super.onDestroy()");
        throw new SuperNotCalledException(sb.toString());
    }
    
    void performDestroyView() {
        if (this.mView != null) {
            this.mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        }
        final FragmentManagerImpl mChildFragmentManager = this.mChildFragmentManager;
        if (mChildFragmentManager != null) {
            mChildFragmentManager.dispatchDestroyView();
        }
        this.mState = 1;
        this.mCalled = false;
        this.onDestroyView();
        if (this.mCalled) {
            LoaderManager.getInstance(this).markForRedelivery();
            this.mPerformedCreateView = false;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" did not call through to super.onDestroyView()");
        throw new SuperNotCalledException(sb.toString());
    }
    
    void performDetach() {
        this.mCalled = false;
        this.onDetach();
        this.mLayoutInflater = null;
        if (this.mCalled) {
            final FragmentManagerImpl mChildFragmentManager = this.mChildFragmentManager;
            if (mChildFragmentManager != null) {
                if (!this.mRetaining) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Child FragmentManager of ");
                    sb.append(this);
                    sb.append(" was not ");
                    sb.append(" destroyed and this fragment is not retaining instance");
                    throw new IllegalStateException(sb.toString());
                }
                mChildFragmentManager.dispatchDestroy();
                this.mChildFragmentManager = null;
            }
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Fragment ");
        sb2.append(this);
        sb2.append(" did not call through to super.onDetach()");
        throw new SuperNotCalledException(sb2.toString());
    }
    
    LayoutInflater performGetLayoutInflater(final Bundle bundle) {
        return this.mLayoutInflater = this.onGetLayoutInflater(bundle);
    }
    
    void performLowMemory() {
        this.onLowMemory();
        final FragmentManagerImpl mChildFragmentManager = this.mChildFragmentManager;
        if (mChildFragmentManager != null) {
            mChildFragmentManager.dispatchLowMemory();
        }
    }
    
    void performMultiWindowModeChanged(final boolean b) {
        this.onMultiWindowModeChanged(b);
        final FragmentManagerImpl mChildFragmentManager = this.mChildFragmentManager;
        if (mChildFragmentManager != null) {
            mChildFragmentManager.dispatchMultiWindowModeChanged(b);
        }
    }
    
    boolean performOptionsItemSelected(final MenuItem menuItem) {
        if (!this.mHidden) {
            if (this.mHasMenu && this.mMenuVisible && this.onOptionsItemSelected(menuItem)) {
                return true;
            }
            final FragmentManagerImpl mChildFragmentManager = this.mChildFragmentManager;
            if (mChildFragmentManager != null && mChildFragmentManager.dispatchOptionsItemSelected(menuItem)) {
                return true;
            }
        }
        return false;
    }
    
    void performOptionsMenuClosed(final Menu menu) {
        if (!this.mHidden) {
            if (this.mHasMenu && this.mMenuVisible) {
                this.onOptionsMenuClosed(menu);
            }
            final FragmentManagerImpl mChildFragmentManager = this.mChildFragmentManager;
            if (mChildFragmentManager != null) {
                mChildFragmentManager.dispatchOptionsMenuClosed(menu);
            }
        }
    }
    
    void performPause() {
        if (this.mView != null) {
            this.mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
        }
        this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
        final FragmentManagerImpl mChildFragmentManager = this.mChildFragmentManager;
        if (mChildFragmentManager != null) {
            mChildFragmentManager.dispatchPause();
        }
        this.mState = 3;
        this.mCalled = false;
        this.onPause();
        if (this.mCalled) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" did not call through to super.onPause()");
        throw new SuperNotCalledException(sb.toString());
    }
    
    void performPictureInPictureModeChanged(final boolean b) {
        this.onPictureInPictureModeChanged(b);
        final FragmentManagerImpl mChildFragmentManager = this.mChildFragmentManager;
        if (mChildFragmentManager != null) {
            mChildFragmentManager.dispatchPictureInPictureModeChanged(b);
        }
    }
    
    boolean performPrepareOptionsMenu(final Menu menu) {
        int n = 0;
        final boolean b = false;
        if (!this.mHidden) {
            int n2 = b ? 1 : 0;
            if (this.mHasMenu) {
                n2 = (b ? 1 : 0);
                if (this.mMenuVisible) {
                    n2 = 1;
                    this.onPrepareOptionsMenu(menu);
                }
            }
            final FragmentManagerImpl mChildFragmentManager = this.mChildFragmentManager;
            n = n2;
            if (mChildFragmentManager != null) {
                n = (n2 | (mChildFragmentManager.dispatchPrepareOptionsMenu(menu) ? 1 : 0));
            }
        }
        return n != 0;
    }
    
    void performResume() {
        final FragmentManagerImpl mChildFragmentManager = this.mChildFragmentManager;
        if (mChildFragmentManager != null) {
            mChildFragmentManager.noteStateNotSaved();
            this.mChildFragmentManager.execPendingActions();
        }
        this.mState = 4;
        this.mCalled = false;
        this.onResume();
        if (this.mCalled) {
            final FragmentManagerImpl mChildFragmentManager2 = this.mChildFragmentManager;
            if (mChildFragmentManager2 != null) {
                mChildFragmentManager2.dispatchResume();
                this.mChildFragmentManager.execPendingActions();
            }
            this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
            if (this.mView != null) {
                this.mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
            }
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" did not call through to super.onResume()");
        throw new SuperNotCalledException(sb.toString());
    }
    
    void performSaveInstanceState(final Bundle bundle) {
        this.onSaveInstanceState(bundle);
        final FragmentManagerImpl mChildFragmentManager = this.mChildFragmentManager;
        if (mChildFragmentManager != null) {
            final Parcelable saveAllState = mChildFragmentManager.saveAllState();
            if (saveAllState != null) {
                bundle.putParcelable("android:support:fragments", saveAllState);
            }
        }
    }
    
    void performStart() {
        final FragmentManagerImpl mChildFragmentManager = this.mChildFragmentManager;
        if (mChildFragmentManager != null) {
            mChildFragmentManager.noteStateNotSaved();
            this.mChildFragmentManager.execPendingActions();
        }
        this.mState = 3;
        this.mCalled = false;
        this.onStart();
        if (this.mCalled) {
            final FragmentManagerImpl mChildFragmentManager2 = this.mChildFragmentManager;
            if (mChildFragmentManager2 != null) {
                mChildFragmentManager2.dispatchStart();
            }
            this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
            if (this.mView != null) {
                this.mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
            }
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" did not call through to super.onStart()");
        throw new SuperNotCalledException(sb.toString());
    }
    
    void performStop() {
        if (this.mView != null) {
            this.mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        }
        this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        final FragmentManagerImpl mChildFragmentManager = this.mChildFragmentManager;
        if (mChildFragmentManager != null) {
            mChildFragmentManager.dispatchStop();
        }
        this.mState = 2;
        this.mCalled = false;
        this.onStop();
        if (this.mCalled) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" did not call through to super.onStop()");
        throw new SuperNotCalledException(sb.toString());
    }
    
    public void postponeEnterTransition() {
        this.ensureAnimationInfo().mEnterTransitionPostponed = true;
    }
    
    public void registerForContextMenu(final View view) {
        view.setOnCreateContextMenuListener((View$OnCreateContextMenuListener)this);
    }
    
    public final void requestPermissions(final String[] array, final int n) {
        final FragmentHostCallback mHost = this.mHost;
        if (mHost != null) {
            mHost.onRequestPermissionsFromFragment(this, array, n);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" not attached to Activity");
        throw new IllegalStateException(sb.toString());
    }
    
    public final FragmentActivity requireActivity() {
        final FragmentActivity activity = this.getActivity();
        if (activity != null) {
            return activity;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" not attached to an activity.");
        throw new IllegalStateException(sb.toString());
    }
    
    public final Context requireContext() {
        final Context context = this.getContext();
        if (context != null) {
            return context;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" not attached to a context.");
        throw new IllegalStateException(sb.toString());
    }
    
    public final FragmentManager requireFragmentManager() {
        final FragmentManager fragmentManager = this.getFragmentManager();
        if (fragmentManager != null) {
            return fragmentManager;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" not associated with a fragment manager.");
        throw new IllegalStateException(sb.toString());
    }
    
    public final Object requireHost() {
        final Object host = this.getHost();
        if (host != null) {
            return host;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" not attached to a host.");
        throw new IllegalStateException(sb.toString());
    }
    
    void restoreChildFragmentState(final Bundle bundle) {
        if (bundle != null) {
            final Parcelable parcelable = bundle.getParcelable("android:support:fragments");
            if (parcelable != null) {
                if (this.mChildFragmentManager == null) {
                    this.instantiateChildFragmentManager();
                }
                this.mChildFragmentManager.restoreAllState(parcelable, this.mChildNonConfig);
                this.mChildNonConfig = null;
                this.mChildFragmentManager.dispatchCreate();
            }
        }
    }
    
    final void restoreViewState(final Bundle bundle) {
        final SparseArray<Parcelable> mSavedViewState = this.mSavedViewState;
        if (mSavedViewState != null) {
            this.mInnerView.restoreHierarchyState((SparseArray)mSavedViewState);
            this.mSavedViewState = null;
        }
        this.mCalled = false;
        this.onViewStateRestored(bundle);
        if (this.mCalled) {
            if (this.mView != null) {
                this.mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
            }
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" did not call through to super.onViewStateRestored()");
        throw new SuperNotCalledException(sb.toString());
    }
    
    public void setAllowEnterTransitionOverlap(final boolean b) {
        this.ensureAnimationInfo().mAllowEnterTransitionOverlap = b;
    }
    
    public void setAllowReturnTransitionOverlap(final boolean b) {
        this.ensureAnimationInfo().mAllowReturnTransitionOverlap = b;
    }
    
    void setAnimatingAway(final View mAnimatingAway) {
        this.ensureAnimationInfo().mAnimatingAway = mAnimatingAway;
    }
    
    void setAnimator(final Animator mAnimator) {
        this.ensureAnimationInfo().mAnimator = mAnimator;
    }
    
    public void setArguments(final Bundle mArguments) {
        if (this.mIndex >= 0 && this.isStateSaved()) {
            throw new IllegalStateException("Fragment already active and state has been saved");
        }
        this.mArguments = mArguments;
    }
    
    public void setEnterSharedElementCallback(final SharedElementCallback mEnterTransitionCallback) {
        this.ensureAnimationInfo().mEnterTransitionCallback = mEnterTransitionCallback;
    }
    
    public void setEnterTransition(final Object mEnterTransition) {
        this.ensureAnimationInfo().mEnterTransition = mEnterTransition;
    }
    
    public void setExitSharedElementCallback(final SharedElementCallback mExitTransitionCallback) {
        this.ensureAnimationInfo().mExitTransitionCallback = mExitTransitionCallback;
    }
    
    public void setExitTransition(final Object mExitTransition) {
        this.ensureAnimationInfo().mExitTransition = mExitTransition;
    }
    
    public void setHasOptionsMenu(final boolean mHasMenu) {
        if (this.mHasMenu != mHasMenu) {
            this.mHasMenu = mHasMenu;
            if (this.isAdded() && !this.isHidden()) {
                this.mHost.onSupportInvalidateOptionsMenu();
            }
        }
    }
    
    void setHideReplaced(final boolean mIsHideReplaced) {
        this.ensureAnimationInfo().mIsHideReplaced = mIsHideReplaced;
    }
    
    final void setIndex(final int mIndex, final Fragment fragment) {
        this.mIndex = mIndex;
        if (fragment != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(fragment.mWho);
            sb.append(":");
            sb.append(this.mIndex);
            this.mWho = sb.toString();
        }
        else {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("android:fragment:");
            sb2.append(this.mIndex);
            this.mWho = sb2.toString();
        }
    }
    
    public void setInitialSavedState(final SavedState savedState) {
        if (this.mIndex < 0) {
            Bundle mState;
            if (savedState != null && savedState.mState != null) {
                mState = savedState.mState;
            }
            else {
                mState = null;
            }
            this.mSavedFragmentState = mState;
            return;
        }
        throw new IllegalStateException("Fragment already active");
    }
    
    public void setMenuVisibility(final boolean mMenuVisible) {
        if (this.mMenuVisible != mMenuVisible) {
            this.mMenuVisible = mMenuVisible;
            if (this.mHasMenu && this.isAdded() && !this.isHidden()) {
                this.mHost.onSupportInvalidateOptionsMenu();
            }
        }
    }
    
    void setNextAnim(final int mNextAnim) {
        if (this.mAnimationInfo == null && mNextAnim == 0) {
            return;
        }
        this.ensureAnimationInfo().mNextAnim = mNextAnim;
    }
    
    void setNextTransition(final int mNextTransition, final int mNextTransitionStyle) {
        if (this.mAnimationInfo == null && mNextTransition == 0 && mNextTransitionStyle == 0) {
            return;
        }
        this.ensureAnimationInfo();
        this.mAnimationInfo.mNextTransition = mNextTransition;
        this.mAnimationInfo.mNextTransitionStyle = mNextTransitionStyle;
    }
    
    void setOnStartEnterTransitionListener(final OnStartEnterTransitionListener mStartEnterTransitionListener) {
        this.ensureAnimationInfo();
        if (mStartEnterTransitionListener == this.mAnimationInfo.mStartEnterTransitionListener) {
            return;
        }
        if (mStartEnterTransitionListener != null && this.mAnimationInfo.mStartEnterTransitionListener != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Trying to set a replacement startPostponedEnterTransition on ");
            sb.append(this);
            throw new IllegalStateException(sb.toString());
        }
        if (this.mAnimationInfo.mEnterTransitionPostponed) {
            this.mAnimationInfo.mStartEnterTransitionListener = mStartEnterTransitionListener;
        }
        if (mStartEnterTransitionListener != null) {
            mStartEnterTransitionListener.startListening();
        }
    }
    
    public void setReenterTransition(final Object mReenterTransition) {
        this.ensureAnimationInfo().mReenterTransition = mReenterTransition;
    }
    
    public void setRetainInstance(final boolean mRetainInstance) {
        this.mRetainInstance = mRetainInstance;
    }
    
    public void setReturnTransition(final Object mReturnTransition) {
        this.ensureAnimationInfo().mReturnTransition = mReturnTransition;
    }
    
    public void setSharedElementEnterTransition(final Object mSharedElementEnterTransition) {
        this.ensureAnimationInfo().mSharedElementEnterTransition = mSharedElementEnterTransition;
    }
    
    public void setSharedElementReturnTransition(final Object mSharedElementReturnTransition) {
        this.ensureAnimationInfo().mSharedElementReturnTransition = mSharedElementReturnTransition;
    }
    
    void setStateAfterAnimating(final int mStateAfterAnimating) {
        this.ensureAnimationInfo().mStateAfterAnimating = mStateAfterAnimating;
    }
    
    public void setTargetFragment(final Fragment mTarget, final int mTargetRequestCode) {
        final FragmentManager fragmentManager = this.getFragmentManager();
        FragmentManager fragmentManager2;
        if (mTarget != null) {
            fragmentManager2 = mTarget.getFragmentManager();
        }
        else {
            fragmentManager2 = null;
        }
        if (fragmentManager != null && fragmentManager2 != null && fragmentManager != fragmentManager2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(mTarget);
            sb.append(" must share the same FragmentManager to be set as a target fragment");
            throw new IllegalArgumentException(sb.toString());
        }
        for (Fragment targetFragment = mTarget; targetFragment != null; targetFragment = targetFragment.getTargetFragment()) {
            if (targetFragment == this) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Setting ");
                sb2.append(mTarget);
                sb2.append(" as the target of ");
                sb2.append(this);
                sb2.append(" would create a target cycle");
                throw new IllegalArgumentException(sb2.toString());
            }
        }
        this.mTarget = mTarget;
        this.mTargetRequestCode = mTargetRequestCode;
    }
    
    public void setUserVisibleHint(final boolean b) {
        if (!this.mUserVisibleHint && b && this.mState < 3 && this.mFragmentManager != null && this.isAdded() && this.mIsCreated) {
            this.mFragmentManager.performPendingDeferredStart(this);
        }
        this.mUserVisibleHint = b;
        this.mDeferStart = (this.mState < 3 && !b);
        if (this.mSavedFragmentState != null) {
            this.mSavedUserVisibleHint = b;
        }
    }
    
    public boolean shouldShowRequestPermissionRationale(final String s) {
        final FragmentHostCallback mHost = this.mHost;
        return mHost != null && mHost.onShouldShowRequestPermissionRationale(s);
    }
    
    public void startActivity(final Intent intent) {
        this.startActivity(intent, null);
    }
    
    public void startActivity(final Intent intent, final Bundle bundle) {
        final FragmentHostCallback mHost = this.mHost;
        if (mHost != null) {
            mHost.onStartActivityFromFragment(this, intent, -1, bundle);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" not attached to Activity");
        throw new IllegalStateException(sb.toString());
    }
    
    public void startActivityForResult(final Intent intent, final int n) {
        this.startActivityForResult(intent, n, null);
    }
    
    public void startActivityForResult(final Intent intent, final int n, final Bundle bundle) {
        final FragmentHostCallback mHost = this.mHost;
        if (mHost != null) {
            mHost.onStartActivityFromFragment(this, intent, n, bundle);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" not attached to Activity");
        throw new IllegalStateException(sb.toString());
    }
    
    public void startIntentSenderForResult(final IntentSender intentSender, final int n, final Intent intent, final int n2, final int n3, final int n4, final Bundle bundle) throws IntentSender$SendIntentException {
        final FragmentHostCallback mHost = this.mHost;
        if (mHost != null) {
            mHost.onStartIntentSenderFromFragment(this, intentSender, n, intent, n2, n3, n4, bundle);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" not attached to Activity");
        throw new IllegalStateException(sb.toString());
    }
    
    public void startPostponedEnterTransition() {
        final FragmentManagerImpl mFragmentManager = this.mFragmentManager;
        if (mFragmentManager != null && mFragmentManager.mHost != null) {
            if (Looper.myLooper() != this.mFragmentManager.mHost.getHandler().getLooper()) {
                this.mFragmentManager.mHost.getHandler().postAtFrontOfQueue((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        Fragment.this.callStartTransitionListener();
                    }
                });
            }
            else {
                this.callStartTransitionListener();
            }
        }
        else {
            this.ensureAnimationInfo().mEnterTransitionPostponed = false;
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(128);
        DebugUtils.buildShortClassTag(this, sb);
        if (this.mIndex >= 0) {
            sb.append(" #");
            sb.append(this.mIndex);
        }
        if (this.mFragmentId != 0) {
            sb.append(" id=0x");
            sb.append(Integer.toHexString(this.mFragmentId));
        }
        if (this.mTag != null) {
            sb.append(" ");
            sb.append(this.mTag);
        }
        sb.append('}');
        return sb.toString();
    }
    
    public void unregisterForContextMenu(final View view) {
        view.setOnCreateContextMenuListener((View$OnCreateContextMenuListener)null);
    }
    
    static class AnimationInfo
    {
        Boolean mAllowEnterTransitionOverlap;
        Boolean mAllowReturnTransitionOverlap;
        View mAnimatingAway;
        Animator mAnimator;
        Object mEnterTransition;
        SharedElementCallback mEnterTransitionCallback;
        boolean mEnterTransitionPostponed;
        Object mExitTransition;
        SharedElementCallback mExitTransitionCallback;
        boolean mIsHideReplaced;
        int mNextAnim;
        int mNextTransition;
        int mNextTransitionStyle;
        Object mReenterTransition;
        Object mReturnTransition;
        Object mSharedElementEnterTransition;
        Object mSharedElementReturnTransition;
        OnStartEnterTransitionListener mStartEnterTransitionListener;
        int mStateAfterAnimating;
        
        AnimationInfo() {
            this.mEnterTransition = null;
            this.mReturnTransition = Fragment.USE_DEFAULT_TRANSITION;
            this.mExitTransition = null;
            this.mReenterTransition = Fragment.USE_DEFAULT_TRANSITION;
            this.mSharedElementEnterTransition = null;
            this.mSharedElementReturnTransition = Fragment.USE_DEFAULT_TRANSITION;
            this.mEnterTransitionCallback = null;
            this.mExitTransitionCallback = null;
        }
    }
    
    public static class InstantiationException extends RuntimeException
    {
        public InstantiationException(final String message, final Exception cause) {
            super(message, cause);
        }
    }
    
    interface OnStartEnterTransitionListener
    {
        void onStartEnterTransition();
        
        void startListening();
    }
    
    public static class SavedState implements Parcelable
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        final Bundle mState;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$ClassLoaderCreator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel, null);
                }
                
                public SavedState createFromParcel(final Parcel parcel, final ClassLoader classLoader) {
                    return new SavedState(parcel, classLoader);
                }
                
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }
        
        SavedState(final Bundle mState) {
            this.mState = mState;
        }
        
        SavedState(final Parcel parcel, final ClassLoader classLoader) {
            final Bundle bundle = parcel.readBundle();
            this.mState = bundle;
            if (classLoader != null && bundle != null) {
                bundle.setClassLoader(classLoader);
            }
        }
        
        public int describeContents() {
            return 0;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            parcel.writeBundle(this.mState);
        }
    }
}
