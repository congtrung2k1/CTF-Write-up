// 
// Decompiled by Procyon v0.5.36
// 

package androidx.fragment.app;

import android.view.animation.Transformation;
import androidx.core.util.DebugUtils;
import androidx.lifecycle.ViewModelStore;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.lifecycle.LifecycleOwner;
import android.animation.AnimatorInflater;
import android.content.res.Resources$NotFoundException;
import android.view.animation.AnimationUtils;
import java.util.Collections;
import java.util.Arrays;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.content.res.Configuration;
import java.io.FileDescriptor;
import java.io.Writer;
import java.io.PrintWriter;
import androidx.core.util.LogWriter;
import androidx.core.view.ViewCompat;
import android.os.Build$VERSION;
import java.util.Iterator;
import android.graphics.Paint;
import java.util.List;
import android.animation.PropertyValuesHolder;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.view.animation.ScaleAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.AlphaAnimation;
import android.content.Context;
import android.util.Log;
import java.util.Collection;
import android.os.Looper;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.animation.Animation$AnimationListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import androidx.collection.ArraySet;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.os.Bundle;
import android.os.Parcelable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.ArrayList;
import android.util.SparseArray;
import java.lang.reflect.Field;
import android.view.animation.Interpolator;
import android.view.LayoutInflater$Factory2;

final class FragmentManagerImpl extends FragmentManager implements LayoutInflater$Factory2
{
    static final Interpolator ACCELERATE_CUBIC;
    static final Interpolator ACCELERATE_QUINT;
    static final int ANIM_DUR = 220;
    public static final int ANIM_STYLE_CLOSE_ENTER = 3;
    public static final int ANIM_STYLE_CLOSE_EXIT = 4;
    public static final int ANIM_STYLE_FADE_ENTER = 5;
    public static final int ANIM_STYLE_FADE_EXIT = 6;
    public static final int ANIM_STYLE_OPEN_ENTER = 1;
    public static final int ANIM_STYLE_OPEN_EXIT = 2;
    static boolean DEBUG = false;
    static final Interpolator DECELERATE_CUBIC;
    static final Interpolator DECELERATE_QUINT;
    static final String TAG = "FragmentManager";
    static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
    static final String TARGET_STATE_TAG = "android:target_state";
    static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
    static final String VIEW_STATE_TAG = "android:view_state";
    static Field sAnimationListenerField;
    SparseArray<Fragment> mActive;
    final ArrayList<Fragment> mAdded;
    ArrayList<Integer> mAvailBackStackIndices;
    ArrayList<BackStackRecord> mBackStack;
    ArrayList<OnBackStackChangedListener> mBackStackChangeListeners;
    ArrayList<BackStackRecord> mBackStackIndices;
    FragmentContainer mContainer;
    ArrayList<Fragment> mCreatedMenus;
    int mCurState;
    boolean mDestroyed;
    Runnable mExecCommit;
    boolean mExecutingActions;
    boolean mHavePendingDeferredStart;
    FragmentHostCallback mHost;
    private final CopyOnWriteArrayList<FragmentLifecycleCallbacksHolder> mLifecycleCallbacks;
    boolean mNeedMenuInvalidate;
    int mNextFragmentIndex;
    String mNoTransactionsBecause;
    Fragment mParent;
    ArrayList<OpGenerator> mPendingActions;
    ArrayList<StartEnterTransitionListener> mPostponedTransactions;
    Fragment mPrimaryNav;
    FragmentManagerNonConfig mSavedNonConfig;
    SparseArray<Parcelable> mStateArray;
    Bundle mStateBundle;
    boolean mStateSaved;
    boolean mStopped;
    ArrayList<Fragment> mTmpAddedFragments;
    ArrayList<Boolean> mTmpIsPop;
    ArrayList<BackStackRecord> mTmpRecords;
    
    static {
        FragmentManagerImpl.DEBUG = false;
        FragmentManagerImpl.sAnimationListenerField = null;
        DECELERATE_QUINT = (Interpolator)new DecelerateInterpolator(2.5f);
        DECELERATE_CUBIC = (Interpolator)new DecelerateInterpolator(1.5f);
        ACCELERATE_QUINT = (Interpolator)new AccelerateInterpolator(2.5f);
        ACCELERATE_CUBIC = (Interpolator)new AccelerateInterpolator(1.5f);
    }
    
    FragmentManagerImpl() {
        this.mNextFragmentIndex = 0;
        this.mAdded = new ArrayList<Fragment>();
        this.mLifecycleCallbacks = new CopyOnWriteArrayList<FragmentLifecycleCallbacksHolder>();
        this.mCurState = 0;
        this.mStateBundle = null;
        this.mStateArray = null;
        this.mExecCommit = new Runnable() {
            @Override
            public void run() {
                FragmentManagerImpl.this.execPendingActions();
            }
        };
    }
    
    private void addAddedFragments(final ArraySet<Fragment> set) {
        final int mCurState = this.mCurState;
        if (mCurState < 1) {
            return;
        }
        final int min = Math.min(mCurState, 3);
        for (int size = this.mAdded.size(), i = 0; i < size; ++i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment.mState < min) {
                this.moveToState(fragment, min, fragment.getNextAnim(), fragment.getNextTransition(), false);
                if (fragment.mView != null && !fragment.mHidden && fragment.mIsNewlyAdded) {
                    set.add(fragment);
                }
            }
        }
    }
    
    private void animateRemoveFragment(final Fragment fragment, final AnimationOrAnimator animationOrAnimator, final int stateAfterAnimating) {
        final View mView = fragment.mView;
        final ViewGroup mContainer = fragment.mContainer;
        mContainer.startViewTransition(mView);
        fragment.setStateAfterAnimating(stateAfterAnimating);
        if (animationOrAnimator.animation != null) {
            final EndViewTransitionAnimator endViewTransitionAnimator = new EndViewTransitionAnimator(animationOrAnimator.animation, mContainer, mView);
            fragment.setAnimatingAway(fragment.mView);
            ((Animation)endViewTransitionAnimator).setAnimationListener((Animation$AnimationListener)new AnimationListenerWrapper(getAnimationListener((Animation)endViewTransitionAnimator)) {
                @Override
                public void onAnimationEnd(final Animation animation) {
                    super.onAnimationEnd(animation);
                    mContainer.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            if (fragment.getAnimatingAway() != null) {
                                fragment.setAnimatingAway(null);
                                FragmentManagerImpl.this.moveToState(fragment, fragment.getStateAfterAnimating(), 0, 0, false);
                            }
                        }
                    });
                }
            });
            setHWLayerAnimListenerIfAlpha(mView, animationOrAnimator);
            fragment.mView.startAnimation((Animation)endViewTransitionAnimator);
        }
        else {
            final Animator animator = animationOrAnimator.animator;
            fragment.setAnimator(animationOrAnimator.animator);
            animator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    mContainer.endViewTransition(mView);
                    animator = fragment.getAnimator();
                    fragment.setAnimator(null);
                    if (animator != null && mContainer.indexOfChild(mView) < 0) {
                        final FragmentManagerImpl this$0 = FragmentManagerImpl.this;
                        final Fragment val$fragment = fragment;
                        this$0.moveToState(val$fragment, val$fragment.getStateAfterAnimating(), 0, 0, false);
                    }
                }
            });
            animator.setTarget((Object)fragment.mView);
            setHWLayerAnimListenerIfAlpha(fragment.mView, animationOrAnimator);
            animator.start();
        }
    }
    
    private void burpActive() {
        final SparseArray<Fragment> mActive = this.mActive;
        if (mActive != null) {
            for (int i = mActive.size() - 1; i >= 0; --i) {
                if (this.mActive.valueAt(i) == null) {
                    final SparseArray<Fragment> mActive2 = this.mActive;
                    mActive2.delete(mActive2.keyAt(i));
                }
            }
        }
    }
    
    private void checkStateLoss() {
        if (this.isStateSaved()) {
            throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
        }
        if (this.mNoTransactionsBecause == null) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Can not perform this action inside of ");
        sb.append(this.mNoTransactionsBecause);
        throw new IllegalStateException(sb.toString());
    }
    
    private void cleanupExec() {
        this.mExecutingActions = false;
        this.mTmpIsPop.clear();
        this.mTmpRecords.clear();
    }
    
    private void dispatchStateChange(final int n) {
        try {
            this.mExecutingActions = true;
            this.moveToState(n, false);
            this.mExecutingActions = false;
            this.execPendingActions();
        }
        finally {
            this.mExecutingActions = false;
        }
    }
    
    private void endAnimatingAwayFragments() {
        final SparseArray<Fragment> mActive = this.mActive;
        int size;
        if (mActive == null) {
            size = 0;
        }
        else {
            size = mActive.size();
        }
        for (int i = 0; i < size; ++i) {
            final Fragment fragment = (Fragment)this.mActive.valueAt(i);
            if (fragment != null) {
                if (fragment.getAnimatingAway() != null) {
                    final int stateAfterAnimating = fragment.getStateAfterAnimating();
                    final View animatingAway = fragment.getAnimatingAway();
                    final Animation animation = animatingAway.getAnimation();
                    if (animation != null) {
                        animation.cancel();
                        animatingAway.clearAnimation();
                    }
                    fragment.setAnimatingAway(null);
                    this.moveToState(fragment, stateAfterAnimating, 0, 0, false);
                }
                else if (fragment.getAnimator() != null) {
                    fragment.getAnimator().end();
                }
            }
        }
    }
    
    private void ensureExecReady(final boolean b) {
        if (this.mExecutingActions) {
            throw new IllegalStateException("FragmentManager is already executing transactions");
        }
        if (this.mHost != null) {
            if (Looper.myLooper() == this.mHost.getHandler().getLooper()) {
                if (!b) {
                    this.checkStateLoss();
                }
                if (this.mTmpRecords == null) {
                    this.mTmpRecords = new ArrayList<BackStackRecord>();
                    this.mTmpIsPop = new ArrayList<Boolean>();
                }
                this.mExecutingActions = true;
                try {
                    this.executePostponedTransaction(null, null);
                    return;
                }
                finally {
                    this.mExecutingActions = false;
                }
            }
            throw new IllegalStateException("Must be called from main thread of fragment host");
        }
        throw new IllegalStateException("Fragment host has been destroyed");
    }
    
    private static void executeOps(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2, int i, final int n) {
        while (i < n) {
            final BackStackRecord backStackRecord = list.get(i);
            final boolean booleanValue = list2.get(i);
            boolean b = true;
            if (booleanValue) {
                backStackRecord.bumpBackStackNesting(-1);
                if (i != n - 1) {
                    b = false;
                }
                backStackRecord.executePopOps(b);
            }
            else {
                backStackRecord.bumpBackStackNesting(1);
                backStackRecord.executeOps();
            }
            ++i;
        }
    }
    
    private void executeOpsTogether(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2, int i, final int n) {
        final boolean mReorderingAllowed = list.get(i).mReorderingAllowed;
        final ArrayList<Fragment> mTmpAddedFragments = this.mTmpAddedFragments;
        if (mTmpAddedFragments == null) {
            this.mTmpAddedFragments = new ArrayList<Fragment>();
        }
        else {
            mTmpAddedFragments.clear();
        }
        this.mTmpAddedFragments.addAll(this.mAdded);
        Fragment fragment = this.getPrimaryNavigationFragment();
        int n2 = i;
        int n3 = 0;
        while (true) {
            final boolean b = true;
            if (n2 >= n) {
                break;
            }
            final BackStackRecord backStackRecord = list.get(n2);
            if (!list2.get(n2)) {
                fragment = backStackRecord.expandOps(this.mTmpAddedFragments, fragment);
            }
            else {
                fragment = backStackRecord.trackAddedFragmentsInPop(this.mTmpAddedFragments, fragment);
            }
            int n4 = b ? 1 : 0;
            if (n3 == 0) {
                if (backStackRecord.mAddToBackStack) {
                    n4 = (b ? 1 : 0);
                }
                else {
                    n4 = 0;
                }
            }
            ++n2;
            n3 = n4;
        }
        this.mTmpAddedFragments.clear();
        if (!mReorderingAllowed) {
            FragmentTransition.startTransitions(this, list, list2, i, n, false);
        }
        executeOps(list, list2, i, n);
        int postponePostponableTransactions = n;
        if (mReorderingAllowed) {
            final ArraySet<Fragment> set = new ArraySet<Fragment>();
            this.addAddedFragments(set);
            postponePostponableTransactions = this.postponePostponableTransactions(list, list2, i, n, set);
            this.makeRemovedFragmentsInvisible(set);
        }
        if (postponePostponableTransactions != i && mReorderingAllowed) {
            FragmentTransition.startTransitions(this, list, list2, i, postponePostponableTransactions, true);
            this.moveToState(this.mCurState, true);
        }
        while (i < n) {
            final BackStackRecord backStackRecord2 = list.get(i);
            if (list2.get(i) && backStackRecord2.mIndex >= 0) {
                this.freeBackStackIndex(backStackRecord2.mIndex);
                backStackRecord2.mIndex = -1;
            }
            backStackRecord2.runOnCommitRunnables();
            ++i;
        }
        if (n3 != 0) {
            this.reportBackStackChanged();
        }
    }
    
    private void executePostponedTransaction(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2) {
        final ArrayList<StartEnterTransitionListener> mPostponedTransactions = this.mPostponedTransactions;
        int size;
        if (mPostponedTransactions == null) {
            size = 0;
        }
        else {
            size = mPostponedTransactions.size();
        }
        int n;
        int n2;
        for (int i = 0; i < size; i = n2 + 1, size = n) {
            final StartEnterTransitionListener startEnterTransitionListener = this.mPostponedTransactions.get(i);
            if (list != null && !startEnterTransitionListener.mIsBack) {
                final int index = list.indexOf(startEnterTransitionListener.mRecord);
                if (index != -1 && list2.get(index)) {
                    startEnterTransitionListener.cancelTransaction();
                    n = size;
                    n2 = i;
                    continue;
                }
            }
            if (!startEnterTransitionListener.isReady()) {
                n = size;
                n2 = i;
                if (list == null) {
                    continue;
                }
                n = size;
                n2 = i;
                if (!startEnterTransitionListener.mRecord.interactsWith(list, 0, list.size())) {
                    continue;
                }
            }
            this.mPostponedTransactions.remove(i);
            n2 = i - 1;
            n = size - 1;
            if (list != null && !startEnterTransitionListener.mIsBack) {
                final int index2 = list.indexOf(startEnterTransitionListener.mRecord);
                if (index2 != -1 && list2.get(index2)) {
                    startEnterTransitionListener.cancelTransaction();
                    continue;
                }
            }
            startEnterTransitionListener.completeTransaction();
        }
    }
    
    private Fragment findFragmentUnder(Fragment o) {
        final ViewGroup mContainer = o.mContainer;
        final View mView = o.mView;
        if (mContainer != null && mView != null) {
            for (int i = this.mAdded.indexOf(o) - 1; i >= 0; --i) {
                o = this.mAdded.get(i);
                if (o.mContainer == mContainer && o.mView != null) {
                    return o;
                }
            }
            return null;
        }
        return null;
    }
    
    private void forcePostponedTransactions() {
        if (this.mPostponedTransactions != null) {
            while (!this.mPostponedTransactions.isEmpty()) {
                this.mPostponedTransactions.remove(0).completeTransaction();
            }
        }
    }
    
    private boolean generateOpsForPendingActions(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2) {
        boolean b = false;
        // monitorenter(this)
        try {
            if (this.mPendingActions != null && this.mPendingActions.size() != 0) {
                for (int size = this.mPendingActions.size(), i = 0; i < size; ++i) {
                    b |= this.mPendingActions.get(i).generateOps(list, list2);
                }
                this.mPendingActions.clear();
                this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                // monitorexit(this)
                return b;
            }
            // monitorexit(this)
            return false;
        }
        finally {
            // monitorexit(this)
            while (true) {}
        }
    }
    
    private static Animation$AnimationListener getAnimationListener(final Animation obj) {
        final Animation$AnimationListener animation$AnimationListener = null;
        final Animation$AnimationListener animation$AnimationListener2 = null;
        Animation$AnimationListener animation$AnimationListener3;
        try {
            if (FragmentManagerImpl.sAnimationListenerField == null) {
                (FragmentManagerImpl.sAnimationListenerField = Animation.class.getDeclaredField("mListener")).setAccessible(true);
            }
            animation$AnimationListener3 = (Animation$AnimationListener)FragmentManagerImpl.sAnimationListenerField.get(obj);
        }
        catch (IllegalAccessException ex) {
            Log.e("FragmentManager", "Cannot access Animation's mListener field", (Throwable)ex);
            animation$AnimationListener3 = animation$AnimationListener;
        }
        catch (NoSuchFieldException ex2) {
            Log.e("FragmentManager", "No field with the name mListener is found in Animation class", (Throwable)ex2);
            animation$AnimationListener3 = animation$AnimationListener2;
        }
        return animation$AnimationListener3;
    }
    
    static AnimationOrAnimator makeFadeAnimation(final Context context, final float n, final float n2) {
        final AlphaAnimation alphaAnimation = new AlphaAnimation(n, n2);
        alphaAnimation.setInterpolator(FragmentManagerImpl.DECELERATE_CUBIC);
        alphaAnimation.setDuration(220L);
        return new AnimationOrAnimator((Animation)alphaAnimation);
    }
    
    static AnimationOrAnimator makeOpenCloseAnimation(final Context context, final float n, final float n2, final float n3, final float n4) {
        final AnimationSet set = new AnimationSet(false);
        final ScaleAnimation scaleAnimation = new ScaleAnimation(n, n2, n, n2, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setInterpolator(FragmentManagerImpl.DECELERATE_QUINT);
        scaleAnimation.setDuration(220L);
        set.addAnimation((Animation)scaleAnimation);
        final AlphaAnimation alphaAnimation = new AlphaAnimation(n3, n4);
        alphaAnimation.setInterpolator(FragmentManagerImpl.DECELERATE_CUBIC);
        alphaAnimation.setDuration(220L);
        set.addAnimation((Animation)alphaAnimation);
        return new AnimationOrAnimator((Animation)set);
    }
    
    private void makeRemovedFragmentsInvisible(final ArraySet<Fragment> set) {
        for (int size = set.size(), i = 0; i < size; ++i) {
            final Fragment fragment = set.valueAt(i);
            if (!fragment.mAdded) {
                final View view = fragment.getView();
                fragment.mPostponedAlpha = view.getAlpha();
                view.setAlpha(0.0f);
            }
        }
    }
    
    static boolean modifiesAlpha(final Animator animator) {
        if (animator == null) {
            return false;
        }
        if (animator instanceof ValueAnimator) {
            final PropertyValuesHolder[] values = ((ValueAnimator)animator).getValues();
            for (int i = 0; i < values.length; ++i) {
                if ("alpha".equals(values[i].getPropertyName())) {
                    return true;
                }
            }
        }
        else if (animator instanceof AnimatorSet) {
            final ArrayList childAnimations = ((AnimatorSet)animator).getChildAnimations();
            for (int j = 0; j < childAnimations.size(); ++j) {
                if (modifiesAlpha((Animator)childAnimations.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    static boolean modifiesAlpha(final AnimationOrAnimator animationOrAnimator) {
        if (animationOrAnimator.animation instanceof AlphaAnimation) {
            return true;
        }
        if (animationOrAnimator.animation instanceof AnimationSet) {
            final List animations = ((AnimationSet)animationOrAnimator.animation).getAnimations();
            for (int i = 0; i < animations.size(); ++i) {
                if (animations.get(i) instanceof AlphaAnimation) {
                    return true;
                }
            }
            return false;
        }
        return modifiesAlpha(animationOrAnimator.animator);
    }
    
    private boolean popBackStackImmediate(final String s, final int n, final int n2) {
        this.execPendingActions();
        this.ensureExecReady(true);
        final Fragment mPrimaryNav = this.mPrimaryNav;
        if (mPrimaryNav != null && n < 0 && s == null) {
            final FragmentManager peekChildFragmentManager = mPrimaryNav.peekChildFragmentManager();
            if (peekChildFragmentManager != null && peekChildFragmentManager.popBackStackImmediate()) {
                return true;
            }
        }
        final boolean popBackStackState = this.popBackStackState(this.mTmpRecords, this.mTmpIsPop, s, n, n2);
        if (popBackStackState) {
            this.mExecutingActions = true;
            try {
                this.removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
            }
            finally {
                this.cleanupExec();
            }
        }
        this.doPendingDeferredStart();
        this.burpActive();
        return popBackStackState;
    }
    
    private int postponePostponableTransactions(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2, final int n, final int n2, final ArraySet<Fragment> set) {
        int n3 = n2;
        int index;
        for (int i = n2 - 1; i >= n; --i, n3 = index) {
            final BackStackRecord element = list.get(i);
            final boolean booleanValue = list2.get(i);
            final boolean b = element.isPostponed() && !element.interactsWith(list, i + 1, n2);
            index = n3;
            if (b) {
                if (this.mPostponedTransactions == null) {
                    this.mPostponedTransactions = new ArrayList<StartEnterTransitionListener>();
                }
                final StartEnterTransitionListener startEnterTransitionListener = new StartEnterTransitionListener(element, booleanValue);
                this.mPostponedTransactions.add(startEnterTransitionListener);
                element.setOnStartPostponedListener(startEnterTransitionListener);
                if (booleanValue) {
                    element.executeOps();
                }
                else {
                    element.executePopOps(false);
                }
                index = n3 - 1;
                if (i != index) {
                    list.remove(i);
                    list.add(index, element);
                }
                this.addAddedFragments(set);
            }
        }
        return n3;
    }
    
    private void removeRedundantOperationsAndExecute(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2) {
        if (list == null || list.isEmpty()) {
            return;
        }
        if (list2 != null && list.size() == list2.size()) {
            this.executePostponedTransaction(list, list2);
            final int size = list.size();
            int n = 0;
            int n2;
            int n3;
            for (int i = 0; i < size; i = n3 + 1, n = n2) {
                n2 = n;
                n3 = i;
                if (!list.get(i).mReorderingAllowed) {
                    if (n != i) {
                        this.executeOpsTogether(list, list2, n, i);
                    }
                    int n5;
                    int n4 = n5 = i + 1;
                    if (list2.get(i)) {
                        while ((n5 = n4) < size) {
                            n5 = n4;
                            if (!list2.get(n4)) {
                                break;
                            }
                            n5 = n4;
                            if (list.get(n4).mReorderingAllowed) {
                                break;
                            }
                            ++n4;
                        }
                    }
                    this.executeOpsTogether(list, list2, i, n5);
                    n2 = n5;
                    n3 = n5 - 1;
                }
            }
            if (n != size) {
                this.executeOpsTogether(list, list2, n, size);
            }
            return;
        }
        throw new IllegalStateException("Internal error with the back stack records");
    }
    
    public static int reverseTransit(int n) {
        final int n2 = 0;
        if (n != 4097) {
            if (n != 4099) {
                if (n != 8194) {
                    n = n2;
                }
                else {
                    n = 4097;
                }
            }
            else {
                n = 4099;
            }
        }
        else {
            n = 8194;
        }
        return n;
    }
    
    private static void setHWLayerAnimListenerIfAlpha(final View view, final AnimationOrAnimator animationOrAnimator) {
        if (view != null && animationOrAnimator != null) {
            if (shouldRunOnHWLayer(view, animationOrAnimator)) {
                if (animationOrAnimator.animator != null) {
                    animationOrAnimator.animator.addListener((Animator$AnimatorListener)new AnimatorOnHWLayerIfNeededListener(view));
                }
                else {
                    final Animation$AnimationListener animationListener = getAnimationListener(animationOrAnimator.animation);
                    view.setLayerType(2, (Paint)null);
                    animationOrAnimator.animation.setAnimationListener((Animation$AnimationListener)new AnimateOnHWLayerIfNeededListener(view, animationListener));
                }
            }
        }
    }
    
    private static void setRetaining(final FragmentManagerNonConfig fragmentManagerNonConfig) {
        if (fragmentManagerNonConfig == null) {
            return;
        }
        final List<Fragment> fragments = fragmentManagerNonConfig.getFragments();
        if (fragments != null) {
            final Iterator<Fragment> iterator = fragments.iterator();
            while (iterator.hasNext()) {
                iterator.next().mRetaining = true;
            }
        }
        final List<FragmentManagerNonConfig> childNonConfigs = fragmentManagerNonConfig.getChildNonConfigs();
        if (childNonConfigs != null) {
            final Iterator<FragmentManagerNonConfig> iterator2 = childNonConfigs.iterator();
            while (iterator2.hasNext()) {
                setRetaining(iterator2.next());
            }
        }
    }
    
    static boolean shouldRunOnHWLayer(final View view, final AnimationOrAnimator animationOrAnimator) {
        boolean b = false;
        if (view != null && animationOrAnimator != null) {
            if (Build$VERSION.SDK_INT >= 19 && view.getLayerType() == 0 && ViewCompat.hasOverlappingRendering(view) && modifiesAlpha(animationOrAnimator)) {
                b = true;
            }
            return b;
        }
        return false;
    }
    
    private void throwException(final RuntimeException ex) {
        Log.e("FragmentManager", ex.getMessage());
        Log.e("FragmentManager", "Activity state:");
        final PrintWriter printWriter = new PrintWriter(new LogWriter("FragmentManager"));
        final FragmentHostCallback mHost = this.mHost;
        if (mHost != null) {
            try {
                mHost.onDump("  ", null, printWriter, new String[0]);
            }
            catch (Exception ex2) {
                Log.e("FragmentManager", "Failed dumping state", (Throwable)ex2);
            }
        }
        else {
            try {
                this.dump("  ", null, printWriter, new String[0]);
            }
            catch (Exception ex3) {
                Log.e("FragmentManager", "Failed dumping state", (Throwable)ex3);
            }
        }
        throw ex;
    }
    
    public static int transitToStyleIndex(int n, final boolean b) {
        final int n2 = -1;
        if (n != 4097) {
            if (n != 4099) {
                if (n != 8194) {
                    n = n2;
                }
                else if (b) {
                    n = 3;
                }
                else {
                    n = 4;
                }
            }
            else if (b) {
                n = 5;
            }
            else {
                n = 6;
            }
        }
        else if (b) {
            n = 1;
        }
        else {
            n = 2;
        }
        return n;
    }
    
    void addBackStackState(final BackStackRecord e) {
        if (this.mBackStack == null) {
            this.mBackStack = new ArrayList<BackStackRecord>();
        }
        this.mBackStack.add(e);
    }
    
    public void addFragment(final Fragment fragment, final boolean b) {
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("add: ");
            sb.append(fragment);
            Log.v("FragmentManager", sb.toString());
        }
        this.makeActive(fragment);
        if (!fragment.mDetached) {
            if (!this.mAdded.contains(fragment)) {
                synchronized (this.mAdded) {
                    this.mAdded.add(fragment);
                    // monitorexit(this.mAdded)
                    fragment.mAdded = true;
                    fragment.mRemoving = false;
                    if (fragment.mView == null) {
                        fragment.mHiddenChanged = false;
                    }
                    if (fragment.mHasMenu && fragment.mMenuVisible) {
                        this.mNeedMenuInvalidate = true;
                    }
                    if (b) {
                        this.moveToState(fragment);
                    }
                    return;
                }
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Fragment already added: ");
            sb2.append(fragment);
            throw new IllegalStateException(sb2.toString());
        }
    }
    
    @Override
    public void addOnBackStackChangedListener(final OnBackStackChangedListener e) {
        if (this.mBackStackChangeListeners == null) {
            this.mBackStackChangeListeners = new ArrayList<OnBackStackChangedListener>();
        }
        this.mBackStackChangeListeners.add(e);
    }
    
    public int allocBackStackIndex(final BackStackRecord backStackRecord) {
        synchronized (this) {
            if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                final int intValue = this.mAvailBackStackIndices.remove(this.mAvailBackStackIndices.size() - 1);
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Adding back stack index ");
                    sb.append(intValue);
                    sb.append(" with ");
                    sb.append(backStackRecord);
                    Log.v("FragmentManager", sb.toString());
                }
                this.mBackStackIndices.set(intValue, backStackRecord);
                return intValue;
            }
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList<BackStackRecord>();
            }
            final int size = this.mBackStackIndices.size();
            if (FragmentManagerImpl.DEBUG) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Setting back stack index ");
                sb2.append(size);
                sb2.append(" to ");
                sb2.append(backStackRecord);
                Log.v("FragmentManager", sb2.toString());
            }
            this.mBackStackIndices.add(backStackRecord);
            return size;
        }
    }
    
    public void attachController(final FragmentHostCallback mHost, final FragmentContainer mContainer, final Fragment mParent) {
        if (this.mHost == null) {
            this.mHost = mHost;
            this.mContainer = mContainer;
            this.mParent = mParent;
            return;
        }
        throw new IllegalStateException("Already attached");
    }
    
    public void attachFragment(final Fragment obj) {
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("attach: ");
            sb.append(obj);
            Log.v("FragmentManager", sb.toString());
        }
        if (obj.mDetached) {
            obj.mDetached = false;
            if (!obj.mAdded) {
                if (!this.mAdded.contains(obj)) {
                    if (FragmentManagerImpl.DEBUG) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("add from attach: ");
                        sb2.append(obj);
                        Log.v("FragmentManager", sb2.toString());
                    }
                    synchronized (this.mAdded) {
                        this.mAdded.add(obj);
                        // monitorexit(this.mAdded)
                        obj.mAdded = true;
                        if (obj.mHasMenu && obj.mMenuVisible) {
                            this.mNeedMenuInvalidate = true;
                        }
                        return;
                    }
                }
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Fragment already added: ");
                sb3.append(obj);
                throw new IllegalStateException(sb3.toString());
            }
        }
    }
    
    @Override
    public FragmentTransaction beginTransaction() {
        return new BackStackRecord(this);
    }
    
    void completeExecute(final BackStackRecord e, final boolean b, final boolean b2, final boolean b3) {
        if (b) {
            e.executePopOps(b3);
        }
        else {
            e.executeOps();
        }
        final ArrayList<BackStackRecord> list = new ArrayList<BackStackRecord>(1);
        final ArrayList<Boolean> list2 = new ArrayList<Boolean>(1);
        list.add(e);
        list2.add(b);
        if (b2) {
            FragmentTransition.startTransitions(this, list, list2, 0, 1, true);
        }
        if (b3) {
            this.moveToState(this.mCurState, true);
        }
        final SparseArray<Fragment> mActive = this.mActive;
        if (mActive != null) {
            for (int size = mActive.size(), i = 0; i < size; ++i) {
                final Fragment fragment = (Fragment)this.mActive.valueAt(i);
                if (fragment != null && fragment.mView != null && fragment.mIsNewlyAdded && e.interactsWith(fragment.mContainerId)) {
                    if (fragment.mPostponedAlpha > 0.0f) {
                        fragment.mView.setAlpha(fragment.mPostponedAlpha);
                    }
                    if (b3) {
                        fragment.mPostponedAlpha = 0.0f;
                    }
                    else {
                        fragment.mPostponedAlpha = -1.0f;
                        fragment.mIsNewlyAdded = false;
                    }
                }
            }
        }
    }
    
    void completeShowHideFragment(final Fragment fragment) {
        if (fragment.mView != null) {
            final AnimationOrAnimator loadAnimation = this.loadAnimation(fragment, fragment.getNextTransition(), fragment.mHidden ^ true, fragment.getNextTransitionStyle());
            if (loadAnimation != null && loadAnimation.animator != null) {
                loadAnimation.animator.setTarget((Object)fragment.mView);
                if (fragment.mHidden) {
                    if (fragment.isHideReplaced()) {
                        fragment.setHideReplaced(false);
                    }
                    else {
                        final ViewGroup mContainer = fragment.mContainer;
                        final View mView = fragment.mView;
                        mContainer.startViewTransition(mView);
                        loadAnimation.animator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                            public void onAnimationEnd(final Animator animator) {
                                mContainer.endViewTransition(mView);
                                animator.removeListener((Animator$AnimatorListener)this);
                                if (fragment.mView != null) {
                                    fragment.mView.setVisibility(8);
                                }
                            }
                        });
                    }
                }
                else {
                    fragment.mView.setVisibility(0);
                }
                setHWLayerAnimListenerIfAlpha(fragment.mView, loadAnimation);
                loadAnimation.animator.start();
            }
            else {
                if (loadAnimation != null) {
                    setHWLayerAnimListenerIfAlpha(fragment.mView, loadAnimation);
                    fragment.mView.startAnimation(loadAnimation.animation);
                    loadAnimation.animation.start();
                }
                int visibility;
                if (fragment.mHidden && !fragment.isHideReplaced()) {
                    visibility = 8;
                }
                else {
                    visibility = 0;
                }
                fragment.mView.setVisibility(visibility);
                if (fragment.isHideReplaced()) {
                    fragment.setHideReplaced(false);
                }
            }
        }
        if (fragment.mAdded && fragment.mHasMenu && fragment.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
        }
        fragment.mHiddenChanged = false;
        fragment.onHiddenChanged(fragment.mHidden);
    }
    
    public void detachFragment(final Fragment o) {
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("detach: ");
            sb.append(o);
            Log.v("FragmentManager", sb.toString());
        }
        if (!o.mDetached) {
            o.mDetached = true;
            if (o.mAdded) {
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("remove from detach: ");
                    sb2.append(o);
                    Log.v("FragmentManager", sb2.toString());
                }
                synchronized (this.mAdded) {
                    this.mAdded.remove(o);
                    // monitorexit(this.mAdded)
                    if (o.mHasMenu && o.mMenuVisible) {
                        this.mNeedMenuInvalidate = true;
                    }
                    o.mAdded = false;
                }
            }
        }
    }
    
    public void dispatchActivityCreated() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.dispatchStateChange(2);
    }
    
    public void dispatchConfigurationChanged(final Configuration configuration) {
        for (int i = 0; i < this.mAdded.size(); ++i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null) {
                fragment.performConfigurationChanged(configuration);
            }
        }
    }
    
    public boolean dispatchContextItemSelected(final MenuItem menuItem) {
        if (this.mCurState < 1) {
            return false;
        }
        for (int i = 0; i < this.mAdded.size(); ++i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null && fragment.performContextItemSelected(menuItem)) {
                return true;
            }
        }
        return false;
    }
    
    public void dispatchCreate() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.dispatchStateChange(1);
    }
    
    public boolean dispatchCreateOptionsMenu(final Menu menu, final MenuInflater menuInflater) {
        if (this.mCurState < 1) {
            return false;
        }
        boolean b = false;
        ArrayList<Fragment> mCreatedMenus = null;
        boolean b2;
        ArrayList<Fragment> list;
        for (int i = 0; i < this.mAdded.size(); ++i, b = b2, mCreatedMenus = list) {
            final Fragment e = this.mAdded.get(i);
            b2 = b;
            list = mCreatedMenus;
            if (e != null) {
                b2 = b;
                list = mCreatedMenus;
                if (e.performCreateOptionsMenu(menu, menuInflater)) {
                    b2 = true;
                    if ((list = mCreatedMenus) == null) {
                        list = new ArrayList<Fragment>();
                    }
                    list.add(e);
                }
            }
        }
        if (this.mCreatedMenus != null) {
            for (int j = 0; j < this.mCreatedMenus.size(); ++j) {
                final Fragment o = this.mCreatedMenus.get(j);
                if (mCreatedMenus == null || !mCreatedMenus.contains(o)) {
                    o.onDestroyOptionsMenu();
                }
            }
        }
        this.mCreatedMenus = mCreatedMenus;
        return b;
    }
    
    public void dispatchDestroy() {
        this.mDestroyed = true;
        this.execPendingActions();
        this.dispatchStateChange(0);
        this.mHost = null;
        this.mContainer = null;
        this.mParent = null;
    }
    
    public void dispatchDestroyView() {
        this.dispatchStateChange(1);
    }
    
    public void dispatchLowMemory() {
        for (int i = 0; i < this.mAdded.size(); ++i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null) {
                fragment.performLowMemory();
            }
        }
    }
    
    public void dispatchMultiWindowModeChanged(final boolean b) {
        for (int i = this.mAdded.size() - 1; i >= 0; --i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null) {
                fragment.performMultiWindowModeChanged(b);
            }
        }
    }
    
    void dispatchOnFragmentActivityCreated(final Fragment fragment, final Bundle bundle, final boolean b) {
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            final FragmentManager fragmentManager = mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentActivityCreated(fragment, bundle, true);
            }
        }
        for (final FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (!b || fragmentLifecycleCallbacksHolder.mRecursive) {
                fragmentLifecycleCallbacksHolder.mCallback.onFragmentActivityCreated(this, fragment, bundle);
            }
        }
    }
    
    void dispatchOnFragmentAttached(final Fragment fragment, final Context context, final boolean b) {
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            final FragmentManager fragmentManager = mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentAttached(fragment, context, true);
            }
        }
        for (final FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (!b || fragmentLifecycleCallbacksHolder.mRecursive) {
                fragmentLifecycleCallbacksHolder.mCallback.onFragmentAttached(this, fragment, context);
            }
        }
    }
    
    void dispatchOnFragmentCreated(final Fragment fragment, final Bundle bundle, final boolean b) {
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            final FragmentManager fragmentManager = mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentCreated(fragment, bundle, true);
            }
        }
        for (final FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (!b || fragmentLifecycleCallbacksHolder.mRecursive) {
                fragmentLifecycleCallbacksHolder.mCallback.onFragmentCreated(this, fragment, bundle);
            }
        }
    }
    
    void dispatchOnFragmentDestroyed(final Fragment fragment, final boolean b) {
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            final FragmentManager fragmentManager = mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentDestroyed(fragment, true);
            }
        }
        for (final FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (!b || fragmentLifecycleCallbacksHolder.mRecursive) {
                fragmentLifecycleCallbacksHolder.mCallback.onFragmentDestroyed(this, fragment);
            }
        }
    }
    
    void dispatchOnFragmentDetached(final Fragment fragment, final boolean b) {
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            final FragmentManager fragmentManager = mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentDetached(fragment, true);
            }
        }
        for (final FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (!b || fragmentLifecycleCallbacksHolder.mRecursive) {
                fragmentLifecycleCallbacksHolder.mCallback.onFragmentDetached(this, fragment);
            }
        }
    }
    
    void dispatchOnFragmentPaused(final Fragment fragment, final boolean b) {
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            final FragmentManager fragmentManager = mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentPaused(fragment, true);
            }
        }
        for (final FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (!b || fragmentLifecycleCallbacksHolder.mRecursive) {
                fragmentLifecycleCallbacksHolder.mCallback.onFragmentPaused(this, fragment);
            }
        }
    }
    
    void dispatchOnFragmentPreAttached(final Fragment fragment, final Context context, final boolean b) {
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            final FragmentManager fragmentManager = mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentPreAttached(fragment, context, true);
            }
        }
        for (final FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (!b || fragmentLifecycleCallbacksHolder.mRecursive) {
                fragmentLifecycleCallbacksHolder.mCallback.onFragmentPreAttached(this, fragment, context);
            }
        }
    }
    
    void dispatchOnFragmentPreCreated(final Fragment fragment, final Bundle bundle, final boolean b) {
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            final FragmentManager fragmentManager = mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentPreCreated(fragment, bundle, true);
            }
        }
        for (final FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (!b || fragmentLifecycleCallbacksHolder.mRecursive) {
                fragmentLifecycleCallbacksHolder.mCallback.onFragmentPreCreated(this, fragment, bundle);
            }
        }
    }
    
    void dispatchOnFragmentResumed(final Fragment fragment, final boolean b) {
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            final FragmentManager fragmentManager = mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentResumed(fragment, true);
            }
        }
        for (final FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (!b || fragmentLifecycleCallbacksHolder.mRecursive) {
                fragmentLifecycleCallbacksHolder.mCallback.onFragmentResumed(this, fragment);
            }
        }
    }
    
    void dispatchOnFragmentSaveInstanceState(final Fragment fragment, final Bundle bundle, final boolean b) {
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            final FragmentManager fragmentManager = mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentSaveInstanceState(fragment, bundle, true);
            }
        }
        for (final FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (!b || fragmentLifecycleCallbacksHolder.mRecursive) {
                fragmentLifecycleCallbacksHolder.mCallback.onFragmentSaveInstanceState(this, fragment, bundle);
            }
        }
    }
    
    void dispatchOnFragmentStarted(final Fragment fragment, final boolean b) {
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            final FragmentManager fragmentManager = mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentStarted(fragment, true);
            }
        }
        for (final FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (!b || fragmentLifecycleCallbacksHolder.mRecursive) {
                fragmentLifecycleCallbacksHolder.mCallback.onFragmentStarted(this, fragment);
            }
        }
    }
    
    void dispatchOnFragmentStopped(final Fragment fragment, final boolean b) {
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            final FragmentManager fragmentManager = mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentStopped(fragment, true);
            }
        }
        for (final FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (!b || fragmentLifecycleCallbacksHolder.mRecursive) {
                fragmentLifecycleCallbacksHolder.mCallback.onFragmentStopped(this, fragment);
            }
        }
    }
    
    void dispatchOnFragmentViewCreated(final Fragment fragment, final View view, final Bundle bundle, final boolean b) {
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            final FragmentManager fragmentManager = mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentViewCreated(fragment, view, bundle, true);
            }
        }
        for (final FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (!b || fragmentLifecycleCallbacksHolder.mRecursive) {
                fragmentLifecycleCallbacksHolder.mCallback.onFragmentViewCreated(this, fragment, view, bundle);
            }
        }
    }
    
    void dispatchOnFragmentViewDestroyed(final Fragment fragment, final boolean b) {
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            final FragmentManager fragmentManager = mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentViewDestroyed(fragment, true);
            }
        }
        for (final FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (!b || fragmentLifecycleCallbacksHolder.mRecursive) {
                fragmentLifecycleCallbacksHolder.mCallback.onFragmentViewDestroyed(this, fragment);
            }
        }
    }
    
    public boolean dispatchOptionsItemSelected(final MenuItem menuItem) {
        if (this.mCurState < 1) {
            return false;
        }
        for (int i = 0; i < this.mAdded.size(); ++i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null && fragment.performOptionsItemSelected(menuItem)) {
                return true;
            }
        }
        return false;
    }
    
    public void dispatchOptionsMenuClosed(final Menu menu) {
        if (this.mCurState < 1) {
            return;
        }
        for (int i = 0; i < this.mAdded.size(); ++i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null) {
                fragment.performOptionsMenuClosed(menu);
            }
        }
    }
    
    public void dispatchPause() {
        this.dispatchStateChange(3);
    }
    
    public void dispatchPictureInPictureModeChanged(final boolean b) {
        for (int i = this.mAdded.size() - 1; i >= 0; --i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null) {
                fragment.performPictureInPictureModeChanged(b);
            }
        }
    }
    
    public boolean dispatchPrepareOptionsMenu(final Menu menu) {
        if (this.mCurState < 1) {
            return false;
        }
        boolean b = false;
        boolean b2;
        for (int i = 0; i < this.mAdded.size(); ++i, b = b2) {
            final Fragment fragment = this.mAdded.get(i);
            b2 = b;
            if (fragment != null) {
                b2 = b;
                if (fragment.performPrepareOptionsMenu(menu)) {
                    b2 = true;
                }
            }
        }
        return b;
    }
    
    public void dispatchResume() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.dispatchStateChange(4);
    }
    
    public void dispatchStart() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.dispatchStateChange(3);
    }
    
    public void dispatchStop() {
        this.mStopped = true;
        this.dispatchStateChange(2);
    }
    
    void doPendingDeferredStart() {
        if (this.mHavePendingDeferredStart) {
            this.mHavePendingDeferredStart = false;
            this.startPendingDeferredFragments();
        }
    }
    
    @Override
    public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("    ");
        final String string = sb.toString();
        final SparseArray<Fragment> mActive = this.mActive;
        if (mActive != null) {
            final int size = mActive.size();
            if (size > 0) {
                printWriter.print(s);
                printWriter.print("Active Fragments in ");
                printWriter.print(Integer.toHexString(System.identityHashCode(this)));
                printWriter.println(":");
                for (int i = 0; i < size; ++i) {
                    final Fragment x = (Fragment)this.mActive.valueAt(i);
                    printWriter.print(s);
                    printWriter.print("  #");
                    printWriter.print(i);
                    printWriter.print(": ");
                    printWriter.println(x);
                    if (x != null) {
                        x.dump(string, fileDescriptor, printWriter, array);
                    }
                }
            }
        }
        final int size2 = this.mAdded.size();
        if (size2 > 0) {
            printWriter.print(s);
            printWriter.println("Added Fragments:");
            for (int j = 0; j < size2; ++j) {
                final Fragment fragment = this.mAdded.get(j);
                printWriter.print(s);
                printWriter.print("  #");
                printWriter.print(j);
                printWriter.print(": ");
                printWriter.println(fragment.toString());
            }
        }
        final ArrayList<Fragment> mCreatedMenus = this.mCreatedMenus;
        if (mCreatedMenus != null) {
            final int size3 = mCreatedMenus.size();
            if (size3 > 0) {
                printWriter.print(s);
                printWriter.println("Fragments Created Menus:");
                for (int k = 0; k < size3; ++k) {
                    final Fragment fragment2 = this.mCreatedMenus.get(k);
                    printWriter.print(s);
                    printWriter.print("  #");
                    printWriter.print(k);
                    printWriter.print(": ");
                    printWriter.println(fragment2.toString());
                }
            }
        }
        final ArrayList<BackStackRecord> mBackStack = this.mBackStack;
        if (mBackStack != null) {
            final int size4 = mBackStack.size();
            if (size4 > 0) {
                printWriter.print(s);
                printWriter.println("Back Stack:");
                for (int l = 0; l < size4; ++l) {
                    final BackStackRecord backStackRecord = this.mBackStack.get(l);
                    printWriter.print(s);
                    printWriter.print("  #");
                    printWriter.print(l);
                    printWriter.print(": ");
                    printWriter.println(backStackRecord.toString());
                    backStackRecord.dump(string, fileDescriptor, printWriter, array);
                }
            }
        }
        // monitorenter(this)
        try {
            if (this.mBackStackIndices != null) {
                final int size5 = this.mBackStackIndices.size();
                if (size5 > 0) {
                    printWriter.print(s);
                    printWriter.println("Back Stack Indices:");
                    for (int n = 0; n < size5; ++n) {
                        final BackStackRecord x2 = this.mBackStackIndices.get(n);
                        printWriter.print(s);
                        printWriter.print("  #");
                        printWriter.print(n);
                        printWriter.print(": ");
                        printWriter.println(x2);
                    }
                }
            }
            if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                printWriter.print(s);
                printWriter.print("mAvailBackStackIndices: ");
                printWriter.println(Arrays.toString(this.mAvailBackStackIndices.toArray()));
            }
            // monitorexit(this)
            final ArrayList<OpGenerator> mPendingActions = this.mPendingActions;
            if (mPendingActions != null) {
                final int size6 = mPendingActions.size();
                if (size6 > 0) {
                    printWriter.print(s);
                    printWriter.println("Pending Actions:");
                    for (int n2 = 0; n2 < size6; ++n2) {
                        final OpGenerator x3 = this.mPendingActions.get(n2);
                        printWriter.print(s);
                        printWriter.print("  #");
                        printWriter.print(n2);
                        printWriter.print(": ");
                        printWriter.println(x3);
                    }
                }
            }
            printWriter.print(s);
            printWriter.println("FragmentManager misc state:");
            printWriter.print(s);
            printWriter.print("  mHost=");
            printWriter.println(this.mHost);
            printWriter.print(s);
            printWriter.print("  mContainer=");
            printWriter.println(this.mContainer);
            if (this.mParent != null) {
                printWriter.print(s);
                printWriter.print("  mParent=");
                printWriter.println(this.mParent);
            }
            printWriter.print(s);
            printWriter.print("  mCurState=");
            printWriter.print(this.mCurState);
            printWriter.print(" mStateSaved=");
            printWriter.print(this.mStateSaved);
            printWriter.print(" mStopped=");
            printWriter.print(this.mStopped);
            printWriter.print(" mDestroyed=");
            printWriter.println(this.mDestroyed);
            if (this.mNeedMenuInvalidate) {
                printWriter.print(s);
                printWriter.print("  mNeedMenuInvalidate=");
                printWriter.println(this.mNeedMenuInvalidate);
            }
            if (this.mNoTransactionsBecause != null) {
                printWriter.print(s);
                printWriter.print("  mNoTransactionsBecause=");
                printWriter.println(this.mNoTransactionsBecause);
            }
        }
        finally {
            // monitorexit(this)
            while (true) {}
        }
    }
    
    public void enqueueAction(final OpGenerator e, final boolean b) {
        if (!b) {
            this.checkStateLoss();
        }
        synchronized (this) {
            if (!this.mDestroyed && this.mHost != null) {
                if (this.mPendingActions == null) {
                    this.mPendingActions = new ArrayList<OpGenerator>();
                }
                this.mPendingActions.add(e);
                this.scheduleCommit();
                return;
            }
            if (b) {
                return;
            }
            throw new IllegalStateException("Activity has been destroyed");
        }
    }
    
    void ensureInflatedFragmentView(final Fragment fragment) {
        if (fragment.mFromLayout && !fragment.mPerformedCreateView) {
            fragment.performCreateView(fragment.performGetLayoutInflater(fragment.mSavedFragmentState), null, fragment.mSavedFragmentState);
            if (fragment.mView != null) {
                fragment.mInnerView = fragment.mView;
                fragment.mView.setSaveFromParentEnabled(false);
                if (fragment.mHidden) {
                    fragment.mView.setVisibility(8);
                }
                fragment.onViewCreated(fragment.mView, fragment.mSavedFragmentState);
                this.dispatchOnFragmentViewCreated(fragment, fragment.mView, fragment.mSavedFragmentState, false);
            }
            else {
                fragment.mInnerView = null;
            }
        }
    }
    
    public boolean execPendingActions() {
        this.ensureExecReady(true);
        boolean b = false;
        while (this.generateOpsForPendingActions(this.mTmpRecords, this.mTmpIsPop)) {
            this.mExecutingActions = true;
            try {
                this.removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
                this.cleanupExec();
                b = true;
                continue;
            }
            finally {
                this.cleanupExec();
            }
            break;
        }
        this.doPendingDeferredStart();
        this.burpActive();
        return b;
    }
    
    public void execSingleAction(final OpGenerator opGenerator, final boolean b) {
        if (b && (this.mHost == null || this.mDestroyed)) {
            return;
        }
        this.ensureExecReady(b);
        if (opGenerator.generateOps(this.mTmpRecords, this.mTmpIsPop)) {
            this.mExecutingActions = true;
            try {
                this.removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
            }
            finally {
                this.cleanupExec();
            }
        }
        this.doPendingDeferredStart();
        this.burpActive();
    }
    
    @Override
    public boolean executePendingTransactions() {
        final boolean execPendingActions = this.execPendingActions();
        this.forcePostponedTransactions();
        return execPendingActions;
    }
    
    @Override
    public Fragment findFragmentById(final int n) {
        for (int i = this.mAdded.size() - 1; i >= 0; --i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null && fragment.mFragmentId == n) {
                return fragment;
            }
        }
        final SparseArray<Fragment> mActive = this.mActive;
        if (mActive != null) {
            for (int j = mActive.size() - 1; j >= 0; --j) {
                final Fragment fragment2 = (Fragment)this.mActive.valueAt(j);
                if (fragment2 != null && fragment2.mFragmentId == n) {
                    return fragment2;
                }
            }
        }
        return null;
    }
    
    @Override
    public Fragment findFragmentByTag(final String s) {
        if (s != null) {
            for (int i = this.mAdded.size() - 1; i >= 0; --i) {
                final Fragment fragment = this.mAdded.get(i);
                if (fragment != null && s.equals(fragment.mTag)) {
                    return fragment;
                }
            }
        }
        final SparseArray<Fragment> mActive = this.mActive;
        if (mActive != null && s != null) {
            for (int j = mActive.size() - 1; j >= 0; --j) {
                final Fragment fragment2 = (Fragment)this.mActive.valueAt(j);
                if (fragment2 != null && s.equals(fragment2.mTag)) {
                    return fragment2;
                }
            }
        }
        return null;
    }
    
    public Fragment findFragmentByWho(final String s) {
        final SparseArray<Fragment> mActive = this.mActive;
        if (mActive != null && s != null) {
            for (int i = mActive.size() - 1; i >= 0; --i) {
                final Fragment fragment = (Fragment)this.mActive.valueAt(i);
                if (fragment != null) {
                    final Fragment fragmentByWho = fragment.findFragmentByWho(s);
                    if (fragmentByWho != null) {
                        return fragmentByWho;
                    }
                }
            }
        }
        return null;
    }
    
    public void freeBackStackIndex(final int i) {
        synchronized (this) {
            this.mBackStackIndices.set(i, null);
            if (this.mAvailBackStackIndices == null) {
                this.mAvailBackStackIndices = new ArrayList<Integer>();
            }
            if (FragmentManagerImpl.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Freeing back stack index ");
                sb.append(i);
                Log.v("FragmentManager", sb.toString());
            }
            this.mAvailBackStackIndices.add(i);
        }
    }
    
    int getActiveFragmentCount() {
        final SparseArray<Fragment> mActive = this.mActive;
        if (mActive == null) {
            return 0;
        }
        return mActive.size();
    }
    
    List<Fragment> getActiveFragments() {
        final SparseArray<Fragment> mActive = this.mActive;
        if (mActive == null) {
            return null;
        }
        final int size = mActive.size();
        final ArrayList list = new ArrayList<Fragment>(size);
        for (int i = 0; i < size; ++i) {
            list.add((Fragment)this.mActive.valueAt(i));
        }
        return (List<Fragment>)list;
    }
    
    @Override
    public BackStackEntry getBackStackEntryAt(final int index) {
        return this.mBackStack.get(index);
    }
    
    @Override
    public int getBackStackEntryCount() {
        final ArrayList<BackStackRecord> mBackStack = this.mBackStack;
        int size;
        if (mBackStack != null) {
            size = mBackStack.size();
        }
        else {
            size = 0;
        }
        return size;
    }
    
    @Override
    public Fragment getFragment(final Bundle bundle, final String str) {
        final int int1 = bundle.getInt(str, -1);
        if (int1 == -1) {
            return null;
        }
        final Fragment fragment = (Fragment)this.mActive.get(int1);
        if (fragment == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment no longer exists for key ");
            sb.append(str);
            sb.append(": index ");
            sb.append(int1);
            this.throwException(new IllegalStateException(sb.toString()));
        }
        return fragment;
    }
    
    @Override
    public List<Fragment> getFragments() {
        if (this.mAdded.isEmpty()) {
            return Collections.emptyList();
        }
        synchronized (this.mAdded) {
            return (List<Fragment>)this.mAdded.clone();
        }
    }
    
    LayoutInflater$Factory2 getLayoutInflaterFactory() {
        return (LayoutInflater$Factory2)this;
    }
    
    @Override
    public Fragment getPrimaryNavigationFragment() {
        return this.mPrimaryNav;
    }
    
    public void hideFragment(final Fragment obj) {
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("hide: ");
            sb.append(obj);
            Log.v("FragmentManager", sb.toString());
        }
        if (!obj.mHidden) {
            obj.mHidden = true;
            obj.mHiddenChanged ^= true;
        }
    }
    
    @Override
    public boolean isDestroyed() {
        return this.mDestroyed;
    }
    
    boolean isStateAtLeast(final int n) {
        return this.mCurState >= n;
    }
    
    @Override
    public boolean isStateSaved() {
        return this.mStateSaved || this.mStopped;
    }
    
    AnimationOrAnimator loadAnimation(final Fragment fragment, int n, final boolean b, final int n2) {
        final int nextAnim = fragment.getNextAnim();
        final Animation onCreateAnimation = fragment.onCreateAnimation(n, b, nextAnim);
        if (onCreateAnimation != null) {
            return new AnimationOrAnimator(onCreateAnimation);
        }
        final Animator onCreateAnimator = fragment.onCreateAnimator(n, b, nextAnim);
        if (onCreateAnimator != null) {
            return new AnimationOrAnimator(onCreateAnimator);
        }
        if (nextAnim != 0) {
            final boolean equals = "anim".equals(this.mHost.getContext().getResources().getResourceTypeName(nextAnim));
            int n3 = 0;
            if (equals) {
                try {
                    final Animation loadAnimation = AnimationUtils.loadAnimation(this.mHost.getContext(), nextAnim);
                    if (loadAnimation != null) {
                        return new AnimationOrAnimator(loadAnimation);
                    }
                    n3 = 1;
                }
                catch (RuntimeException ex3) {
                    n3 = n3;
                }
                catch (Resources$NotFoundException ex) {
                    throw ex;
                }
            }
            if (n3 == 0) {
                try {
                    final Animator loadAnimator = AnimatorInflater.loadAnimator(this.mHost.getContext(), nextAnim);
                    if (loadAnimator != null) {
                        return new AnimationOrAnimator(loadAnimator);
                    }
                }
                catch (RuntimeException ex2) {
                    if (equals) {
                        throw ex2;
                    }
                    final Animation loadAnimation2 = AnimationUtils.loadAnimation(this.mHost.getContext(), nextAnim);
                    if (loadAnimation2 != null) {
                        return new AnimationOrAnimator(loadAnimation2);
                    }
                }
            }
        }
        if (n == 0) {
            return null;
        }
        n = transitToStyleIndex(n, b);
        if (n < 0) {
            return null;
        }
        switch (n) {
            default: {
                n = n2;
                if (n2 == 0) {
                    n = n2;
                    if (this.mHost.onHasWindowAnimations()) {
                        n = this.mHost.onGetWindowAnimations();
                    }
                }
                if (n == 0) {
                    return null;
                }
                return null;
            }
            case 6: {
                return makeFadeAnimation(this.mHost.getContext(), 1.0f, 0.0f);
            }
            case 5: {
                return makeFadeAnimation(this.mHost.getContext(), 0.0f, 1.0f);
            }
            case 4: {
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.0f, 1.075f, 1.0f, 0.0f);
            }
            case 3: {
                return makeOpenCloseAnimation(this.mHost.getContext(), 0.975f, 1.0f, 0.0f, 1.0f);
            }
            case 2: {
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.0f, 0.975f, 1.0f, 0.0f);
            }
            case 1: {
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.125f, 1.0f, 0.0f, 1.0f);
            }
        }
    }
    
    void makeActive(final Fragment obj) {
        if (obj.mIndex >= 0) {
            return;
        }
        obj.setIndex(this.mNextFragmentIndex++, this.mParent);
        if (this.mActive == null) {
            this.mActive = (SparseArray<Fragment>)new SparseArray();
        }
        this.mActive.put(obj.mIndex, (Object)obj);
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Allocated fragment index ");
            sb.append(obj);
            Log.v("FragmentManager", sb.toString());
        }
    }
    
    void makeInactive(final Fragment obj) {
        if (obj.mIndex < 0) {
            return;
        }
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Freeing fragment index ");
            sb.append(obj);
            Log.v("FragmentManager", sb.toString());
        }
        this.mActive.put(obj.mIndex, (Object)null);
        obj.initState();
    }
    
    void moveFragmentToExpectedState(final Fragment fragment) {
        if (fragment == null) {
            return;
        }
        int n2;
        final int n = n2 = this.mCurState;
        if (fragment.mRemoving) {
            if (fragment.isInBackStack()) {
                n2 = Math.min(n, 1);
            }
            else {
                n2 = Math.min(n, 0);
            }
        }
        this.moveToState(fragment, n2, fragment.getNextTransition(), fragment.getNextTransitionStyle(), false);
        if (fragment.mView != null) {
            final Fragment fragmentUnder = this.findFragmentUnder(fragment);
            if (fragmentUnder != null) {
                final View mView = fragmentUnder.mView;
                final ViewGroup mContainer = fragment.mContainer;
                final int indexOfChild = mContainer.indexOfChild(mView);
                final int indexOfChild2 = mContainer.indexOfChild(fragment.mView);
                if (indexOfChild2 < indexOfChild) {
                    mContainer.removeViewAt(indexOfChild2);
                    mContainer.addView(fragment.mView, indexOfChild);
                }
            }
            if (fragment.mIsNewlyAdded && fragment.mContainer != null) {
                if (fragment.mPostponedAlpha > 0.0f) {
                    fragment.mView.setAlpha(fragment.mPostponedAlpha);
                }
                fragment.mPostponedAlpha = 0.0f;
                fragment.mIsNewlyAdded = false;
                final AnimationOrAnimator loadAnimation = this.loadAnimation(fragment, fragment.getNextTransition(), true, fragment.getNextTransitionStyle());
                if (loadAnimation != null) {
                    setHWLayerAnimListenerIfAlpha(fragment.mView, loadAnimation);
                    if (loadAnimation.animation != null) {
                        fragment.mView.startAnimation(loadAnimation.animation);
                    }
                    else {
                        loadAnimation.animator.setTarget((Object)fragment.mView);
                        loadAnimation.animator.start();
                    }
                }
            }
        }
        if (fragment.mHiddenChanged) {
            this.completeShowHideFragment(fragment);
        }
    }
    
    void moveToState(int i, final boolean b) {
        if (this.mHost == null && i != 0) {
            throw new IllegalStateException("No activity");
        }
        if (!b && i == this.mCurState) {
            return;
        }
        this.mCurState = i;
        if (this.mActive != null) {
            int size;
            for (size = this.mAdded.size(), i = 0; i < size; ++i) {
                this.moveFragmentToExpectedState(this.mAdded.get(i));
            }
            int size2;
            Fragment fragment;
            for (size2 = this.mActive.size(), i = 0; i < size2; ++i) {
                fragment = (Fragment)this.mActive.valueAt(i);
                if (fragment != null && (fragment.mRemoving || fragment.mDetached) && !fragment.mIsNewlyAdded) {
                    this.moveFragmentToExpectedState(fragment);
                }
            }
            this.startPendingDeferredFragments();
            if (this.mNeedMenuInvalidate) {
                final FragmentHostCallback mHost = this.mHost;
                if (mHost != null && this.mCurState == 4) {
                    mHost.onSupportInvalidateOptionsMenu();
                    this.mNeedMenuInvalidate = false;
                }
            }
        }
    }
    
    void moveToState(final Fragment fragment) {
        this.moveToState(fragment, this.mCurState, 0, 0, false);
    }
    
    void moveToState(final Fragment obj, int stateAfterAnimating, int n, int n2, final boolean b) {
        final boolean mAdded = obj.mAdded;
        final boolean b2 = true;
        if (!mAdded || obj.mDetached) {
            if ((stateAfterAnimating = stateAfterAnimating) > 1) {
                stateAfterAnimating = 1;
            }
        }
        int mState = stateAfterAnimating;
        if (obj.mRemoving && (mState = stateAfterAnimating) > obj.mState) {
            if (obj.mState == 0 && obj.isInBackStack()) {
                mState = 1;
            }
            else {
                mState = obj.mState;
            }
        }
        stateAfterAnimating = mState;
        if (obj.mDeferStart) {
            stateAfterAnimating = mState;
            if (obj.mState < 3 && (stateAfterAnimating = mState) > 2) {
                stateAfterAnimating = 2;
            }
        }
        Label_1954: {
            if (obj.mState <= stateAfterAnimating) {
                if (obj.mFromLayout && !obj.mInLayout) {
                    return;
                }
                if (obj.getAnimatingAway() != null || obj.getAnimator() != null) {
                    obj.setAnimatingAway(null);
                    obj.setAnimator(null);
                    this.moveToState(obj, obj.getStateAfterAnimating(), 0, 0, true);
                }
                final int mState2 = obj.mState;
                Label_1336: {
                    Label_1265: {
                        Label_1201: {
                            if (mState2 != 0) {
                                if (mState2 != 1) {
                                    n2 = stateAfterAnimating;
                                    if (mState2 == 2) {
                                        break Label_1201;
                                    }
                                    n = stateAfterAnimating;
                                    if (mState2 != 3) {
                                        break Label_1336;
                                    }
                                    break Label_1265;
                                }
                            }
                            else if (stateAfterAnimating > 0) {
                                if (FragmentManagerImpl.DEBUG) {
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("moveto CREATED: ");
                                    sb.append(obj);
                                    Log.v("FragmentManager", sb.toString());
                                }
                                n = stateAfterAnimating;
                                if (obj.mSavedFragmentState != null) {
                                    obj.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                                    obj.mSavedViewState = (SparseArray<Parcelable>)obj.mSavedFragmentState.getSparseParcelableArray("android:view_state");
                                    obj.mTarget = this.getFragment(obj.mSavedFragmentState, "android:target_state");
                                    if (obj.mTarget != null) {
                                        obj.mTargetRequestCode = obj.mSavedFragmentState.getInt("android:target_req_state", 0);
                                    }
                                    if (obj.mSavedUserVisibleHint != null) {
                                        obj.mUserVisibleHint = obj.mSavedUserVisibleHint;
                                        obj.mSavedUserVisibleHint = null;
                                    }
                                    else {
                                        obj.mUserVisibleHint = obj.mSavedFragmentState.getBoolean("android:user_visible_hint", true);
                                    }
                                    n = stateAfterAnimating;
                                    if (!obj.mUserVisibleHint) {
                                        obj.mDeferStart = true;
                                        if ((n = stateAfterAnimating) > 2) {
                                            n = 2;
                                        }
                                    }
                                }
                                obj.mHost = this.mHost;
                                obj.mParentFragment = this.mParent;
                                final Fragment mParent = this.mParent;
                                FragmentManagerImpl mFragmentManager;
                                if (mParent != null) {
                                    mFragmentManager = mParent.mChildFragmentManager;
                                }
                                else {
                                    mFragmentManager = this.mHost.getFragmentManagerImpl();
                                }
                                obj.mFragmentManager = mFragmentManager;
                                if (obj.mTarget != null) {
                                    if (this.mActive.get(obj.mTarget.mIndex) != obj.mTarget) {
                                        final StringBuilder sb2 = new StringBuilder();
                                        sb2.append("Fragment ");
                                        sb2.append(obj);
                                        sb2.append(" declared target fragment ");
                                        sb2.append(obj.mTarget);
                                        sb2.append(" that does not belong to this FragmentManager!");
                                        throw new IllegalStateException(sb2.toString());
                                    }
                                    if (obj.mTarget.mState < 1) {
                                        this.moveToState(obj.mTarget, 1, 0, 0, true);
                                    }
                                }
                                this.dispatchOnFragmentPreAttached(obj, this.mHost.getContext(), false);
                                obj.mCalled = false;
                                obj.onAttach(this.mHost.getContext());
                                if (!obj.mCalled) {
                                    final StringBuilder sb3 = new StringBuilder();
                                    sb3.append("Fragment ");
                                    sb3.append(obj);
                                    sb3.append(" did not call through to super.onAttach()");
                                    throw new SuperNotCalledException(sb3.toString());
                                }
                                if (obj.mParentFragment == null) {
                                    this.mHost.onAttachFragment(obj);
                                }
                                else {
                                    obj.mParentFragment.onAttachFragment(obj);
                                }
                                this.dispatchOnFragmentAttached(obj, this.mHost.getContext(), false);
                                if (!obj.mIsCreated) {
                                    this.dispatchOnFragmentPreCreated(obj, obj.mSavedFragmentState, false);
                                    obj.performCreate(obj.mSavedFragmentState);
                                    this.dispatchOnFragmentCreated(obj, obj.mSavedFragmentState, false);
                                }
                                else {
                                    obj.restoreChildFragmentState(obj.mSavedFragmentState);
                                    obj.mState = 1;
                                }
                                obj.mRetaining = false;
                                stateAfterAnimating = n;
                            }
                            this.ensureInflatedFragmentView(obj);
                            if (stateAfterAnimating > 1) {
                                if (FragmentManagerImpl.DEBUG) {
                                    final StringBuilder sb4 = new StringBuilder();
                                    sb4.append("moveto ACTIVITY_CREATED: ");
                                    sb4.append(obj);
                                    Log.v("FragmentManager", sb4.toString());
                                }
                                if (!obj.mFromLayout) {
                                    ViewGroup mContainer = null;
                                    if (obj.mContainerId != 0) {
                                        if (obj.mContainerId == -1) {
                                            final StringBuilder sb5 = new StringBuilder();
                                            sb5.append("Cannot create fragment ");
                                            sb5.append(obj);
                                            sb5.append(" for a container view with no id");
                                            this.throwException(new IllegalArgumentException(sb5.toString()));
                                        }
                                        final ViewGroup viewGroup = (ViewGroup)this.mContainer.onFindViewById(obj.mContainerId);
                                        if (viewGroup == null && !obj.mRestored) {
                                            String resourceName;
                                            try {
                                                resourceName = obj.getResources().getResourceName(obj.mContainerId);
                                            }
                                            catch (Resources$NotFoundException ex) {
                                                resourceName = "unknown";
                                            }
                                            final StringBuilder sb6 = new StringBuilder();
                                            sb6.append("No view found for id 0x");
                                            sb6.append(Integer.toHexString(obj.mContainerId));
                                            sb6.append(" (");
                                            sb6.append(resourceName);
                                            sb6.append(") for fragment ");
                                            sb6.append(obj);
                                            this.throwException(new IllegalArgumentException(sb6.toString()));
                                        }
                                        mContainer = viewGroup;
                                    }
                                    obj.mContainer = mContainer;
                                    obj.performCreateView(obj.performGetLayoutInflater(obj.mSavedFragmentState), mContainer, obj.mSavedFragmentState);
                                    if (obj.mView != null) {
                                        obj.mInnerView = obj.mView;
                                        obj.mView.setSaveFromParentEnabled(false);
                                        if (mContainer != null) {
                                            mContainer.addView(obj.mView);
                                        }
                                        if (obj.mHidden) {
                                            obj.mView.setVisibility(8);
                                        }
                                        obj.onViewCreated(obj.mView, obj.mSavedFragmentState);
                                        this.dispatchOnFragmentViewCreated(obj, obj.mView, obj.mSavedFragmentState, false);
                                        obj.mIsNewlyAdded = (obj.mView.getVisibility() == 0 && obj.mContainer != null && b2);
                                    }
                                    else {
                                        obj.mInnerView = null;
                                    }
                                }
                                obj.performActivityCreated(obj.mSavedFragmentState);
                                this.dispatchOnFragmentActivityCreated(obj, obj.mSavedFragmentState, false);
                                if (obj.mView != null) {
                                    obj.restoreViewState(obj.mSavedFragmentState);
                                }
                                obj.mSavedFragmentState = null;
                            }
                            n2 = stateAfterAnimating;
                        }
                        if ((n = n2) > 2) {
                            if (FragmentManagerImpl.DEBUG) {
                                final StringBuilder sb7 = new StringBuilder();
                                sb7.append("moveto STARTED: ");
                                sb7.append(obj);
                                Log.v("FragmentManager", sb7.toString());
                            }
                            obj.performStart();
                            this.dispatchOnFragmentStarted(obj, false);
                            n = n2;
                        }
                    }
                    if ((stateAfterAnimating = n) > 3) {
                        if (FragmentManagerImpl.DEBUG) {
                            final StringBuilder sb8 = new StringBuilder();
                            sb8.append("moveto RESUMED: ");
                            sb8.append(obj);
                            Log.v("FragmentManager", sb8.toString());
                        }
                        obj.performResume();
                        this.dispatchOnFragmentResumed(obj, false);
                        obj.mSavedFragmentState = null;
                        obj.mSavedViewState = null;
                        stateAfterAnimating = n;
                    }
                }
                n = stateAfterAnimating;
            }
            else if (obj.mState > stateAfterAnimating) {
                final int mState3 = obj.mState;
                if (mState3 != 1) {
                    if (mState3 != 2) {
                        if (mState3 != 3) {
                            if (mState3 != 4) {
                                n = stateAfterAnimating;
                                break Label_1954;
                            }
                            if (stateAfterAnimating < 4) {
                                if (FragmentManagerImpl.DEBUG) {
                                    final StringBuilder sb9 = new StringBuilder();
                                    sb9.append("movefrom RESUMED: ");
                                    sb9.append(obj);
                                    Log.v("FragmentManager", sb9.toString());
                                }
                                obj.performPause();
                                this.dispatchOnFragmentPaused(obj, false);
                            }
                        }
                        if (stateAfterAnimating < 3) {
                            if (FragmentManagerImpl.DEBUG) {
                                final StringBuilder sb10 = new StringBuilder();
                                sb10.append("movefrom STARTED: ");
                                sb10.append(obj);
                                Log.v("FragmentManager", sb10.toString());
                            }
                            obj.performStop();
                            this.dispatchOnFragmentStopped(obj, false);
                        }
                    }
                    if (stateAfterAnimating < 2) {
                        if (FragmentManagerImpl.DEBUG) {
                            final StringBuilder sb11 = new StringBuilder();
                            sb11.append("movefrom ACTIVITY_CREATED: ");
                            sb11.append(obj);
                            Log.v("FragmentManager", sb11.toString());
                        }
                        if (obj.mView != null && this.mHost.onShouldSaveFragmentState(obj) && obj.mSavedViewState == null) {
                            this.saveFragmentViewState(obj);
                        }
                        obj.performDestroyView();
                        this.dispatchOnFragmentViewDestroyed(obj, false);
                        if (obj.mView != null && obj.mContainer != null) {
                            obj.mContainer.endViewTransition(obj.mView);
                            obj.mView.clearAnimation();
                            AnimationOrAnimator loadAnimation = null;
                            if (this.mCurState > 0 && !this.mDestroyed) {
                                if (obj.mView.getVisibility() == 0 && obj.mPostponedAlpha >= 0.0f) {
                                    loadAnimation = this.loadAnimation(obj, n, false, n2);
                                }
                            }
                            obj.mPostponedAlpha = 0.0f;
                            if (loadAnimation != null) {
                                this.animateRemoveFragment(obj, loadAnimation, stateAfterAnimating);
                            }
                            obj.mContainer.removeView(obj.mView);
                        }
                        obj.mContainer = null;
                        obj.mView = null;
                        obj.mViewLifecycleOwner = null;
                        obj.mViewLifecycleOwnerLiveData.setValue(null);
                        obj.mInnerView = null;
                        obj.mInLayout = false;
                    }
                }
                if ((n = stateAfterAnimating) < 1) {
                    if (this.mDestroyed) {
                        if (obj.getAnimatingAway() != null) {
                            final View animatingAway = obj.getAnimatingAway();
                            obj.setAnimatingAway(null);
                            animatingAway.clearAnimation();
                        }
                        else if (obj.getAnimator() != null) {
                            final Animator animator = obj.getAnimator();
                            obj.setAnimator(null);
                            animator.cancel();
                        }
                    }
                    if (obj.getAnimatingAway() == null && obj.getAnimator() == null) {
                        if (FragmentManagerImpl.DEBUG) {
                            final StringBuilder sb12 = new StringBuilder();
                            sb12.append("movefrom CREATED: ");
                            sb12.append(obj);
                            Log.v("FragmentManager", sb12.toString());
                        }
                        if (!obj.mRetaining) {
                            obj.performDestroy();
                            this.dispatchOnFragmentDestroyed(obj, false);
                        }
                        else {
                            obj.mState = 0;
                        }
                        obj.performDetach();
                        this.dispatchOnFragmentDetached(obj, false);
                        n = stateAfterAnimating;
                        if (!b) {
                            if (!obj.mRetaining) {
                                this.makeInactive(obj);
                                n = stateAfterAnimating;
                            }
                            else {
                                obj.mHost = null;
                                obj.mParentFragment = null;
                                obj.mFragmentManager = null;
                                n = stateAfterAnimating;
                            }
                        }
                    }
                    else {
                        obj.setStateAfterAnimating(stateAfterAnimating);
                        n = 1;
                    }
                }
            }
            else {
                n = stateAfterAnimating;
            }
        }
        if (obj.mState != n) {
            final StringBuilder sb13 = new StringBuilder();
            sb13.append("moveToState: Fragment state for ");
            sb13.append(obj);
            sb13.append(" not updated inline; ");
            sb13.append("expected state ");
            sb13.append(n);
            sb13.append(" found ");
            sb13.append(obj.mState);
            Log.w("FragmentManager", sb13.toString());
            obj.mState = n;
        }
    }
    
    public void noteStateNotSaved() {
        this.mSavedNonConfig = null;
        this.mStateSaved = false;
        this.mStopped = false;
        for (int size = this.mAdded.size(), i = 0; i < size; ++i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null) {
                fragment.noteStateNotSaved();
            }
        }
    }
    
    public View onCreateView(final View view, final String anObject, final Context context, final AttributeSet set) {
        if (!"fragment".equals(anObject)) {
            return null;
        }
        String s = set.getAttributeValue((String)null, "class");
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, FragmentTag.Fragment);
        int id = 0;
        if (s == null) {
            s = obtainStyledAttributes.getString(0);
        }
        final int resourceId = obtainStyledAttributes.getResourceId(1, -1);
        final String string = obtainStyledAttributes.getString(2);
        obtainStyledAttributes.recycle();
        if (!Fragment.isSupportFragmentClass(this.mHost.getContext(), s)) {
            return null;
        }
        if (view != null) {
            id = view.getId();
        }
        if (id == -1 && resourceId == -1 && string == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(set.getPositionDescription());
            sb.append(": Must specify unique android:id, android:tag, or have a parent with an id for ");
            sb.append(s);
            throw new IllegalArgumentException(sb.toString());
        }
        Fragment fragmentById;
        if (resourceId != -1) {
            fragmentById = this.findFragmentById(resourceId);
        }
        else {
            fragmentById = null;
        }
        Fragment fragmentByTag = fragmentById;
        if (fragmentById == null) {
            fragmentByTag = fragmentById;
            if (string != null) {
                fragmentByTag = this.findFragmentByTag(string);
            }
        }
        Fragment obj;
        if ((obj = fragmentByTag) == null) {
            obj = fragmentByTag;
            if (id != -1) {
                obj = this.findFragmentById(id);
            }
        }
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("onCreateView: id=0x");
            sb2.append(Integer.toHexString(resourceId));
            sb2.append(" fname=");
            sb2.append(s);
            sb2.append(" existing=");
            sb2.append(obj);
            Log.v("FragmentManager", sb2.toString());
        }
        if (obj == null) {
            obj = this.mContainer.instantiate(context, s, null);
            obj.mFromLayout = true;
            int mFragmentId;
            if (resourceId != 0) {
                mFragmentId = resourceId;
            }
            else {
                mFragmentId = id;
            }
            obj.mFragmentId = mFragmentId;
            obj.mContainerId = id;
            obj.mTag = string;
            obj.mInLayout = true;
            obj.mFragmentManager = this;
            obj.mHost = this.mHost;
            obj.onInflate(this.mHost.getContext(), set, obj.mSavedFragmentState);
            this.addFragment(obj, true);
        }
        else {
            if (obj.mInLayout) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(set.getPositionDescription());
                sb3.append(": Duplicate id 0x");
                sb3.append(Integer.toHexString(resourceId));
                sb3.append(", tag ");
                sb3.append(string);
                sb3.append(", or parent id 0x");
                sb3.append(Integer.toHexString(id));
                sb3.append(" with another fragment for ");
                sb3.append(s);
                throw new IllegalArgumentException(sb3.toString());
            }
            obj.mInLayout = true;
            obj.mHost = this.mHost;
            if (!obj.mRetaining) {
                obj.onInflate(this.mHost.getContext(), set, obj.mSavedFragmentState);
            }
        }
        if (this.mCurState < 1 && obj.mFromLayout) {
            this.moveToState(obj, 1, 0, 0, false);
        }
        else {
            this.moveToState(obj);
        }
        if (obj.mView != null) {
            if (resourceId != 0) {
                obj.mView.setId(resourceId);
            }
            if (obj.mView.getTag() == null) {
                obj.mView.setTag((Object)string);
            }
            return obj.mView;
        }
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("Fragment ");
        sb4.append(s);
        sb4.append(" did not create a view.");
        throw new IllegalStateException(sb4.toString());
    }
    
    public View onCreateView(final String s, final Context context, final AttributeSet set) {
        return this.onCreateView(null, s, context, set);
    }
    
    public void performPendingDeferredStart(final Fragment fragment) {
        if (fragment.mDeferStart) {
            if (this.mExecutingActions) {
                this.mHavePendingDeferredStart = true;
                return;
            }
            fragment.mDeferStart = false;
            this.moveToState(fragment, this.mCurState, 0, 0, false);
        }
    }
    
    @Override
    public void popBackStack() {
        this.enqueueAction((OpGenerator)new PopBackStackState(null, -1, 0), false);
    }
    
    @Override
    public void popBackStack(final int i, final int n) {
        if (i >= 0) {
            this.enqueueAction((OpGenerator)new PopBackStackState(null, i, n), false);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad id: ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }
    
    @Override
    public void popBackStack(final String s, final int n) {
        this.enqueueAction((OpGenerator)new PopBackStackState(s, -1, n), false);
    }
    
    @Override
    public boolean popBackStackImmediate() {
        this.checkStateLoss();
        return this.popBackStackImmediate(null, -1, 0);
    }
    
    @Override
    public boolean popBackStackImmediate(final int i, final int n) {
        this.checkStateLoss();
        this.execPendingActions();
        if (i >= 0) {
            return this.popBackStackImmediate(null, i, n);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad id: ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }
    
    @Override
    public boolean popBackStackImmediate(final String s, final int n) {
        this.checkStateLoss();
        return this.popBackStackImmediate(s, -1, n);
    }
    
    boolean popBackStackState(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2, final String s, int i, int index) {
        final ArrayList<BackStackRecord> mBackStack = this.mBackStack;
        if (mBackStack == null) {
            return false;
        }
        if (s == null && i < 0 && (index & 0x1) == 0x0) {
            i = mBackStack.size() - 1;
            if (i < 0) {
                return false;
            }
            list.add(this.mBackStack.remove(i));
            list2.add(true);
        }
        else {
            int n = -1;
            if (s != null || i >= 0) {
                int j;
                for (j = this.mBackStack.size() - 1; j >= 0; --j) {
                    final BackStackRecord backStackRecord = this.mBackStack.get(j);
                    if (s != null && s.equals(backStackRecord.getName())) {
                        break;
                    }
                    if (i >= 0 && i == backStackRecord.mIndex) {
                        break;
                    }
                }
                if (j < 0) {
                    return false;
                }
                n = j;
                if ((index & 0x1) != 0x0) {
                    index = j - 1;
                    while (true) {
                        n = index;
                        if (index < 0) {
                            break;
                        }
                        final BackStackRecord backStackRecord2 = this.mBackStack.get(index);
                        if (s == null || !s.equals(backStackRecord2.getName())) {
                            n = index;
                            if (i < 0) {
                                break;
                            }
                            n = index;
                            if (i != backStackRecord2.mIndex) {
                                break;
                            }
                        }
                        --index;
                    }
                }
            }
            if (n == this.mBackStack.size() - 1) {
                return false;
            }
            for (i = this.mBackStack.size() - 1; i > n; --i) {
                list.add(this.mBackStack.remove(i));
                list2.add(true);
            }
        }
        return true;
    }
    
    @Override
    public void putFragment(final Bundle bundle, final String s, final Fragment obj) {
        if (obj.mIndex < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(obj);
            sb.append(" is not currently in the FragmentManager");
            this.throwException(new IllegalStateException(sb.toString()));
        }
        bundle.putInt(s, obj.mIndex);
    }
    
    @Override
    public void registerFragmentLifecycleCallbacks(final FragmentLifecycleCallbacks fragmentLifecycleCallbacks, final boolean b) {
        this.mLifecycleCallbacks.add(new FragmentLifecycleCallbacksHolder(fragmentLifecycleCallbacks, b));
    }
    
    public void removeFragment(final Fragment fragment) {
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("remove: ");
            sb.append(fragment);
            sb.append(" nesting=");
            sb.append(fragment.mBackStackNesting);
            Log.v("FragmentManager", sb.toString());
        }
        final boolean inBackStack = fragment.isInBackStack();
        if (fragment.mDetached && !(inBackStack ^ true)) {
            return;
        }
        synchronized (this.mAdded) {
            this.mAdded.remove(fragment);
            // monitorexit(this.mAdded)
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.mAdded = false;
            fragment.mRemoving = true;
        }
    }
    
    @Override
    public void removeOnBackStackChangedListener(final OnBackStackChangedListener o) {
        final ArrayList<OnBackStackChangedListener> mBackStackChangeListeners = this.mBackStackChangeListeners;
        if (mBackStackChangeListeners != null) {
            mBackStackChangeListeners.remove(o);
        }
    }
    
    void reportBackStackChanged() {
        if (this.mBackStackChangeListeners != null) {
            for (int i = 0; i < this.mBackStackChangeListeners.size(); ++i) {
                this.mBackStackChangeListeners.get(i).onBackStackChanged();
            }
        }
    }
    
    void restoreAllState(final Parcelable parcelable, final FragmentManagerNonConfig fragmentManagerNonConfig) {
        if (parcelable == null) {
            return;
        }
        final FragmentManagerState fragmentManagerState = (FragmentManagerState)parcelable;
        if (fragmentManagerState.mActive == null) {
            return;
        }
        List<FragmentManagerNonConfig> childNonConfigs;
        List<ViewModelStore> viewModelStores;
        if (fragmentManagerNonConfig != null) {
            final List<Fragment> fragments = fragmentManagerNonConfig.getFragments();
            childNonConfigs = fragmentManagerNonConfig.getChildNonConfigs();
            viewModelStores = fragmentManagerNonConfig.getViewModelStores();
            int size;
            if (fragments != null) {
                size = fragments.size();
            }
            else {
                size = 0;
            }
            for (int i = 0; i < size; ++i) {
                final Fragment fragment = fragments.get(i);
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("restoreAllState: re-attaching retained ");
                    sb.append(fragment);
                    Log.v("FragmentManager", sb.toString());
                }
                int n;
                for (n = 0; n < fragmentManagerState.mActive.length && fragmentManagerState.mActive[n].mIndex != fragment.mIndex; ++n) {}
                if (n == fragmentManagerState.mActive.length) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Could not find active fragment with index ");
                    sb2.append(fragment.mIndex);
                    this.throwException(new IllegalStateException(sb2.toString()));
                }
                final FragmentState fragmentState = fragmentManagerState.mActive[n];
                fragmentState.mInstance = fragment;
                fragment.mSavedViewState = null;
                fragment.mBackStackNesting = 0;
                fragment.mInLayout = false;
                fragment.mAdded = false;
                fragment.mTarget = null;
                if (fragmentState.mSavedFragmentState != null) {
                    fragmentState.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                    fragment.mSavedViewState = (SparseArray<Parcelable>)fragmentState.mSavedFragmentState.getSparseParcelableArray("android:view_state");
                    fragment.mSavedFragmentState = fragmentState.mSavedFragmentState;
                }
            }
        }
        else {
            viewModelStores = null;
            childNonConfigs = null;
        }
        this.mActive = (SparseArray<Fragment>)new SparseArray(fragmentManagerState.mActive.length);
        for (int j = 0; j < fragmentManagerState.mActive.length; ++j) {
            final FragmentState fragmentState2 = fragmentManagerState.mActive[j];
            if (fragmentState2 != null) {
                FragmentManagerNonConfig fragmentManagerNonConfig2;
                if (childNonConfigs != null && j < childNonConfigs.size()) {
                    fragmentManagerNonConfig2 = childNonConfigs.get(j);
                }
                else {
                    fragmentManagerNonConfig2 = null;
                }
                ViewModelStore viewModelStore;
                if (viewModelStores != null && j < viewModelStores.size()) {
                    viewModelStore = viewModelStores.get(j);
                }
                else {
                    viewModelStore = null;
                }
                final Fragment instantiate = fragmentState2.instantiate(this.mHost, this.mContainer, this.mParent, fragmentManagerNonConfig2, viewModelStore);
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("restoreAllState: active #");
                    sb3.append(j);
                    sb3.append(": ");
                    sb3.append(instantiate);
                    Log.v("FragmentManager", sb3.toString());
                }
                this.mActive.put(instantiate.mIndex, (Object)instantiate);
                fragmentState2.mInstance = null;
            }
        }
        if (fragmentManagerNonConfig != null) {
            final List<Fragment> fragments2 = fragmentManagerNonConfig.getFragments();
            int size2;
            if (fragments2 != null) {
                size2 = fragments2.size();
            }
            else {
                size2 = 0;
            }
            for (int k = 0; k < size2; ++k) {
                final Fragment obj = fragments2.get(k);
                if (obj.mTargetIndex >= 0) {
                    obj.mTarget = (Fragment)this.mActive.get(obj.mTargetIndex);
                    if (obj.mTarget == null) {
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append("Re-attaching retained fragment ");
                        sb4.append(obj);
                        sb4.append(" target no longer exists: ");
                        sb4.append(obj.mTargetIndex);
                        Log.w("FragmentManager", sb4.toString());
                    }
                }
            }
        }
        this.mAdded.clear();
        if (fragmentManagerState.mAdded != null) {
            int l = 0;
            while (l < fragmentManagerState.mAdded.length) {
                final Fragment e = (Fragment)this.mActive.get(fragmentManagerState.mAdded[l]);
                if (e == null) {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("No instantiated fragment for index #");
                    sb5.append(fragmentManagerState.mAdded[l]);
                    this.throwException(new IllegalStateException(sb5.toString()));
                }
                e.mAdded = true;
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append("restoreAllState: added #");
                    sb6.append(l);
                    sb6.append(": ");
                    sb6.append(e);
                    Log.v("FragmentManager", sb6.toString());
                }
                if (!this.mAdded.contains(e)) {
                    synchronized (this.mAdded) {
                        this.mAdded.add(e);
                        // monitorexit(this.mAdded)
                        ++l;
                        continue;
                    }
                }
                throw new IllegalStateException("Already added!");
            }
        }
        if (fragmentManagerState.mBackStack != null) {
            this.mBackStack = new ArrayList<BackStackRecord>(fragmentManagerState.mBackStack.length);
            for (int m = 0; m < fragmentManagerState.mBackStack.length; ++m) {
                final BackStackRecord instantiate2 = fragmentManagerState.mBackStack[m].instantiate(this);
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb7 = new StringBuilder();
                    sb7.append("restoreAllState: back stack #");
                    sb7.append(m);
                    sb7.append(" (index ");
                    sb7.append(instantiate2.mIndex);
                    sb7.append("): ");
                    sb7.append(instantiate2);
                    Log.v("FragmentManager", sb7.toString());
                    final PrintWriter printWriter = new PrintWriter(new LogWriter("FragmentManager"));
                    instantiate2.dump("  ", printWriter, false);
                    printWriter.close();
                }
                this.mBackStack.add(instantiate2);
                if (instantiate2.mIndex >= 0) {
                    this.setBackStackIndex(instantiate2.mIndex, instantiate2);
                }
            }
        }
        else {
            this.mBackStack = null;
        }
        if (fragmentManagerState.mPrimaryNavActiveIndex >= 0) {
            this.mPrimaryNav = (Fragment)this.mActive.get(fragmentManagerState.mPrimaryNavActiveIndex);
        }
        this.mNextFragmentIndex = fragmentManagerState.mNextFragmentIndex;
    }
    
    FragmentManagerNonConfig retainNonConfig() {
        setRetaining(this.mSavedNonConfig);
        return this.mSavedNonConfig;
    }
    
    Parcelable saveAllState() {
        this.forcePostponedTransactions();
        this.endAnimatingAwayFragments();
        this.execPendingActions();
        this.mStateSaved = true;
        this.mSavedNonConfig = null;
        final SparseArray<Fragment> mActive = this.mActive;
        if (mActive == null || mActive.size() <= 0) {
            return null;
        }
        final int size = this.mActive.size();
        final FragmentState[] mActive2 = new FragmentState[size];
        int n = 0;
        for (int i = 0; i < size; ++i) {
            final Fragment obj = (Fragment)this.mActive.valueAt(i);
            if (obj != null) {
                if (obj.mIndex < 0) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Failure saving state: active ");
                    sb.append(obj);
                    sb.append(" has cleared index: ");
                    sb.append(obj.mIndex);
                    this.throwException(new IllegalStateException(sb.toString()));
                }
                final boolean b = true;
                final FragmentState fragmentState = new FragmentState(obj);
                mActive2[i] = fragmentState;
                if (obj.mState > 0 && fragmentState.mSavedFragmentState == null) {
                    fragmentState.mSavedFragmentState = this.saveFragmentBasicState(obj);
                    if (obj.mTarget != null) {
                        if (obj.mTarget.mIndex < 0) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("Failure saving state: ");
                            sb2.append(obj);
                            sb2.append(" has target not in fragment manager: ");
                            sb2.append(obj.mTarget);
                            this.throwException(new IllegalStateException(sb2.toString()));
                        }
                        if (fragmentState.mSavedFragmentState == null) {
                            fragmentState.mSavedFragmentState = new Bundle();
                        }
                        this.putFragment(fragmentState.mSavedFragmentState, "android:target_state", obj.mTarget);
                        if (obj.mTargetRequestCode != 0) {
                            fragmentState.mSavedFragmentState.putInt("android:target_req_state", obj.mTargetRequestCode);
                        }
                    }
                }
                else {
                    fragmentState.mSavedFragmentState = obj.mSavedFragmentState;
                }
                n = (b ? 1 : 0);
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Saved state of ");
                    sb3.append(obj);
                    sb3.append(": ");
                    sb3.append(fragmentState.mSavedFragmentState);
                    Log.v("FragmentManager", sb3.toString());
                    n = (b ? 1 : 0);
                }
            }
        }
        if (n == 0) {
            if (FragmentManagerImpl.DEBUG) {
                Log.v("FragmentManager", "saveAllState: no fragments!");
            }
            return null;
        }
        int[] mAdded = null;
        final BackStackState[] array = null;
        final int size2 = this.mAdded.size();
        if (size2 > 0) {
            final int[] array2 = new int[size2];
            int n2 = 0;
            while (true) {
                mAdded = array2;
                if (n2 >= size2) {
                    break;
                }
                array2[n2] = this.mAdded.get(n2).mIndex;
                if (array2[n2] < 0) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("Failure saving state: active ");
                    sb4.append(this.mAdded.get(n2));
                    sb4.append(" has cleared index: ");
                    sb4.append(array2[n2]);
                    this.throwException(new IllegalStateException(sb4.toString()));
                }
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("saveAllState: adding fragment #");
                    sb5.append(n2);
                    sb5.append(": ");
                    sb5.append(this.mAdded.get(n2));
                    Log.v("FragmentManager", sb5.toString());
                }
                ++n2;
            }
        }
        final ArrayList<BackStackRecord> mBackStack = this.mBackStack;
        BackStackState[] mBackStack2 = array;
        if (mBackStack != null) {
            final int size3 = mBackStack.size();
            mBackStack2 = array;
            if (size3 > 0) {
                final BackStackState[] array3 = new BackStackState[size3];
                int index = 0;
                while (true) {
                    mBackStack2 = array3;
                    if (index >= size3) {
                        break;
                    }
                    array3[index] = new BackStackState(this.mBackStack.get(index));
                    if (FragmentManagerImpl.DEBUG) {
                        final StringBuilder sb6 = new StringBuilder();
                        sb6.append("saveAllState: adding back stack #");
                        sb6.append(index);
                        sb6.append(": ");
                        sb6.append(this.mBackStack.get(index));
                        Log.v("FragmentManager", sb6.toString());
                    }
                    ++index;
                }
            }
        }
        final FragmentManagerState fragmentManagerState = new FragmentManagerState();
        fragmentManagerState.mActive = mActive2;
        fragmentManagerState.mAdded = mAdded;
        fragmentManagerState.mBackStack = mBackStack2;
        final Fragment mPrimaryNav = this.mPrimaryNav;
        if (mPrimaryNav != null) {
            fragmentManagerState.mPrimaryNavActiveIndex = mPrimaryNav.mIndex;
        }
        fragmentManagerState.mNextFragmentIndex = this.mNextFragmentIndex;
        this.saveNonConfig();
        return (Parcelable)fragmentManagerState;
    }
    
    Bundle saveFragmentBasicState(final Fragment fragment) {
        Bundle mStateBundle = null;
        if (this.mStateBundle == null) {
            this.mStateBundle = new Bundle();
        }
        fragment.performSaveInstanceState(this.mStateBundle);
        this.dispatchOnFragmentSaveInstanceState(fragment, this.mStateBundle, false);
        if (!this.mStateBundle.isEmpty()) {
            mStateBundle = this.mStateBundle;
            this.mStateBundle = null;
        }
        if (fragment.mView != null) {
            this.saveFragmentViewState(fragment);
        }
        Bundle bundle = mStateBundle;
        if (fragment.mSavedViewState != null) {
            if ((bundle = mStateBundle) == null) {
                bundle = new Bundle();
            }
            bundle.putSparseParcelableArray("android:view_state", (SparseArray)fragment.mSavedViewState);
        }
        Bundle bundle2 = bundle;
        if (!fragment.mUserVisibleHint) {
            if ((bundle2 = bundle) == null) {
                bundle2 = new Bundle();
            }
            bundle2.putBoolean("android:user_visible_hint", fragment.mUserVisibleHint);
        }
        return bundle2;
    }
    
    @Override
    public Fragment.SavedState saveFragmentInstanceState(final Fragment obj) {
        if (obj.mIndex < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(obj);
            sb.append(" is not currently in the FragmentManager");
            this.throwException(new IllegalStateException(sb.toString()));
        }
        final int mState = obj.mState;
        final Fragment.SavedState savedState = null;
        if (mState > 0) {
            final Bundle saveFragmentBasicState = this.saveFragmentBasicState(obj);
            Object o = savedState;
            if (saveFragmentBasicState != null) {
                o = new Fragment.SavedState(saveFragmentBasicState);
            }
            return (Fragment.SavedState)o;
        }
        return null;
    }
    
    void saveFragmentViewState(final Fragment fragment) {
        if (fragment.mInnerView == null) {
            return;
        }
        final SparseArray<Parcelable> mStateArray = this.mStateArray;
        if (mStateArray == null) {
            this.mStateArray = (SparseArray<Parcelable>)new SparseArray();
        }
        else {
            mStateArray.clear();
        }
        fragment.mInnerView.saveHierarchyState((SparseArray)this.mStateArray);
        if (this.mStateArray.size() > 0) {
            fragment.mSavedViewState = this.mStateArray;
            this.mStateArray = null;
        }
    }
    
    void saveNonConfig() {
        List<Fragment> list = null;
        ArrayList<Fragment> list2 = null;
        List<FragmentManagerNonConfig> list3 = null;
        ArrayList<FragmentManagerNonConfig> list4 = null;
        List<ViewModelStore> list5 = null;
        ArrayList<ViewModelStore> list6 = null;
        if (this.mActive != null) {
            int n = 0;
            while (true) {
                list = list2;
                list3 = list4;
                list5 = list6;
                if (n >= this.mActive.size()) {
                    break;
                }
                final Fragment fragment = (Fragment)this.mActive.valueAt(n);
                ArrayList<Fragment> list7 = list2;
                ArrayList<FragmentManagerNonConfig> list8 = list4;
                ArrayList<ViewModelStore> list9 = list6;
                if (fragment != null) {
                    ArrayList<Fragment> list10 = list2;
                    if (fragment.mRetainInstance) {
                        ArrayList<Fragment> list11;
                        if ((list11 = list2) == null) {
                            list11 = new ArrayList<Fragment>();
                        }
                        list11.add(fragment);
                        int mIndex;
                        if (fragment.mTarget != null) {
                            mIndex = fragment.mTarget.mIndex;
                        }
                        else {
                            mIndex = -1;
                        }
                        fragment.mTargetIndex = mIndex;
                        list10 = list11;
                        if (FragmentManagerImpl.DEBUG) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("retainNonConfig: keeping retained ");
                            sb.append(fragment);
                            Log.v("FragmentManager", sb.toString());
                            list10 = list11;
                        }
                    }
                    FragmentManagerNonConfig e;
                    if (fragment.mChildFragmentManager != null) {
                        fragment.mChildFragmentManager.saveNonConfig();
                        e = fragment.mChildFragmentManager.mSavedNonConfig;
                    }
                    else {
                        e = fragment.mChildNonConfig;
                    }
                    ArrayList<FragmentManagerNonConfig> list12 = list4;
                    if (list4 == null) {
                        list12 = list4;
                        if (e != null) {
                            final ArrayList<FragmentManagerNonConfig> list13 = new ArrayList<FragmentManagerNonConfig>(this.mActive.size());
                            int n2 = 0;
                            while (true) {
                                list12 = list13;
                                if (n2 >= n) {
                                    break;
                                }
                                list13.add(null);
                                ++n2;
                            }
                        }
                    }
                    if (list12 != null) {
                        list12.add(e);
                    }
                    ArrayList<ViewModelStore> list14;
                    if ((list14 = list6) == null) {
                        list14 = list6;
                        if (fragment.mViewModelStore != null) {
                            final ArrayList<ViewModelStore> list15 = new ArrayList<ViewModelStore>(this.mActive.size());
                            int n3 = 0;
                            while (true) {
                                list14 = list15;
                                if (n3 >= n) {
                                    break;
                                }
                                list15.add(null);
                                ++n3;
                            }
                        }
                    }
                    list7 = list10;
                    list8 = list12;
                    if ((list9 = list14) != null) {
                        list14.add(fragment.mViewModelStore);
                        list9 = list14;
                        list8 = list12;
                        list7 = list10;
                    }
                }
                ++n;
                list2 = list7;
                list4 = list8;
                list6 = list9;
            }
        }
        if (list == null && list3 == null && list5 == null) {
            this.mSavedNonConfig = null;
        }
        else {
            this.mSavedNonConfig = new FragmentManagerNonConfig(list, list3, list5);
        }
    }
    
    void scheduleCommit() {
        synchronized (this) {
            final ArrayList<StartEnterTransitionListener> mPostponedTransactions = this.mPostponedTransactions;
            final int n = 0;
            final boolean b = mPostponedTransactions != null && !this.mPostponedTransactions.isEmpty();
            int n2 = n;
            if (this.mPendingActions != null) {
                n2 = n;
                if (this.mPendingActions.size() == 1) {
                    n2 = 1;
                }
            }
            if (b || n2 != 0) {
                this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                this.mHost.getHandler().post(this.mExecCommit);
            }
        }
    }
    
    public void setBackStackIndex(final int i, final BackStackRecord backStackRecord) {
        // monitorenter(this)
        try {
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList<BackStackRecord>();
            }
            int j;
            if (i < (j = this.mBackStackIndices.size())) {
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Setting back stack index ");
                    sb.append(i);
                    sb.append(" to ");
                    sb.append(backStackRecord);
                    Log.v("FragmentManager", sb.toString());
                }
                this.mBackStackIndices.set(i, backStackRecord);
            }
            else {
                while (j < i) {
                    this.mBackStackIndices.add(null);
                    if (this.mAvailBackStackIndices == null) {
                        this.mAvailBackStackIndices = new ArrayList<Integer>();
                    }
                    if (FragmentManagerImpl.DEBUG) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Adding available back stack index ");
                        sb2.append(j);
                        Log.v("FragmentManager", sb2.toString());
                    }
                    this.mAvailBackStackIndices.add(j);
                    ++j;
                }
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Adding back stack index ");
                    sb3.append(i);
                    sb3.append(" with ");
                    sb3.append(backStackRecord);
                    Log.v("FragmentManager", sb3.toString());
                }
                this.mBackStackIndices.add(backStackRecord);
            }
        }
        // monitorexit(this)
        finally {
            // monitorexit(this)
            while (true) {}
        }
    }
    
    public void setPrimaryNavigationFragment(final Fragment fragment) {
        Label_0085: {
            if (fragment != null) {
                if (this.mActive.get(fragment.mIndex) == fragment) {
                    if (fragment.mHost == null) {
                        break Label_0085;
                    }
                    if (fragment.getFragmentManager() == this) {
                        break Label_0085;
                    }
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("Fragment ");
                sb.append(fragment);
                sb.append(" is not an active fragment of FragmentManager ");
                sb.append(this);
                throw new IllegalArgumentException(sb.toString());
            }
        }
        this.mPrimaryNav = fragment;
    }
    
    public void showFragment(final Fragment obj) {
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("show: ");
            sb.append(obj);
            Log.v("FragmentManager", sb.toString());
        }
        if (obj.mHidden) {
            obj.mHidden = false;
            obj.mHiddenChanged ^= true;
        }
    }
    
    void startPendingDeferredFragments() {
        if (this.mActive == null) {
            return;
        }
        for (int i = 0; i < this.mActive.size(); ++i) {
            final Fragment fragment = (Fragment)this.mActive.valueAt(i);
            if (fragment != null) {
                this.performPendingDeferredStart(fragment);
            }
        }
    }
    
    public String toString() {
        final StringBuilder sb = new StringBuilder(128);
        sb.append("FragmentManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            DebugUtils.buildShortClassTag(mParent, sb);
        }
        else {
            DebugUtils.buildShortClassTag(this.mHost, sb);
        }
        sb.append("}}");
        return sb.toString();
    }
    
    @Override
    public void unregisterFragmentLifecycleCallbacks(final FragmentLifecycleCallbacks fragmentLifecycleCallbacks) {
        final CopyOnWriteArrayList<FragmentLifecycleCallbacksHolder> mLifecycleCallbacks = this.mLifecycleCallbacks;
        // monitorenter(mLifecycleCallbacks)
        int i = 0;
        try {
            while (i < this.mLifecycleCallbacks.size()) {
                if (this.mLifecycleCallbacks.get(i).mCallback == fragmentLifecycleCallbacks) {
                    this.mLifecycleCallbacks.remove(i);
                    break;
                }
                ++i;
            }
        }
        // monitorexit(mLifecycleCallbacks)
        finally {
            // monitorexit(mLifecycleCallbacks)
            while (true) {}
        }
    }
    
    private static class AnimateOnHWLayerIfNeededListener extends AnimationListenerWrapper
    {
        View mView;
        
        AnimateOnHWLayerIfNeededListener(final View mView, final Animation$AnimationListener animation$AnimationListener) {
            super(animation$AnimationListener);
            this.mView = mView;
        }
        
        @Override
        public void onAnimationEnd(final Animation animation) {
            if (!ViewCompat.isAttachedToWindow(this.mView) && Build$VERSION.SDK_INT < 24) {
                this.mView.setLayerType(0, (Paint)null);
            }
            else {
                this.mView.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        AnimateOnHWLayerIfNeededListener.this.mView.setLayerType(0, (Paint)null);
                    }
                });
            }
            super.onAnimationEnd(animation);
        }
    }
    
    private static class AnimationListenerWrapper implements Animation$AnimationListener
    {
        private final Animation$AnimationListener mWrapped;
        
        AnimationListenerWrapper(final Animation$AnimationListener mWrapped) {
            this.mWrapped = mWrapped;
        }
        
        public void onAnimationEnd(final Animation animation) {
            final Animation$AnimationListener mWrapped = this.mWrapped;
            if (mWrapped != null) {
                mWrapped.onAnimationEnd(animation);
            }
        }
        
        public void onAnimationRepeat(final Animation animation) {
            final Animation$AnimationListener mWrapped = this.mWrapped;
            if (mWrapped != null) {
                mWrapped.onAnimationRepeat(animation);
            }
        }
        
        public void onAnimationStart(final Animation animation) {
            final Animation$AnimationListener mWrapped = this.mWrapped;
            if (mWrapped != null) {
                mWrapped.onAnimationStart(animation);
            }
        }
    }
    
    private static class AnimationOrAnimator
    {
        public final Animation animation;
        public final Animator animator;
        
        AnimationOrAnimator(final Animator animator) {
            this.animation = null;
            this.animator = animator;
            if (animator != null) {
                return;
            }
            throw new IllegalStateException("Animator cannot be null");
        }
        
        AnimationOrAnimator(final Animation animation) {
            this.animation = animation;
            this.animator = null;
            if (animation != null) {
                return;
            }
            throw new IllegalStateException("Animation cannot be null");
        }
    }
    
    private static class AnimatorOnHWLayerIfNeededListener extends AnimatorListenerAdapter
    {
        View mView;
        
        AnimatorOnHWLayerIfNeededListener(final View mView) {
            this.mView = mView;
        }
        
        public void onAnimationEnd(final Animator animator) {
            this.mView.setLayerType(0, (Paint)null);
            animator.removeListener((Animator$AnimatorListener)this);
        }
        
        public void onAnimationStart(final Animator animator) {
            this.mView.setLayerType(2, (Paint)null);
        }
    }
    
    private static class EndViewTransitionAnimator extends AnimationSet implements Runnable
    {
        private boolean mAnimating;
        private final View mChild;
        private boolean mEnded;
        private final ViewGroup mParent;
        private boolean mTransitionEnded;
        
        EndViewTransitionAnimator(final Animation animation, final ViewGroup mParent, final View mChild) {
            super(false);
            this.mAnimating = true;
            this.mParent = mParent;
            this.mChild = mChild;
            this.addAnimation(animation);
            this.mParent.post((Runnable)this);
        }
        
        public boolean getTransformation(final long n, final Transformation transformation) {
            this.mAnimating = true;
            if (this.mEnded) {
                return true ^ this.mTransitionEnded;
            }
            if (!super.getTransformation(n, transformation)) {
                this.mEnded = true;
                OneShotPreDrawListener.add((View)this.mParent, this);
            }
            return true;
        }
        
        public boolean getTransformation(final long n, final Transformation transformation, final float n2) {
            this.mAnimating = true;
            if (this.mEnded) {
                return true ^ this.mTransitionEnded;
            }
            if (!super.getTransformation(n, transformation, n2)) {
                this.mEnded = true;
                OneShotPreDrawListener.add((View)this.mParent, this);
            }
            return true;
        }
        
        public void run() {
            if (!this.mEnded && this.mAnimating) {
                this.mAnimating = false;
                this.mParent.post((Runnable)this);
            }
            else {
                this.mParent.endViewTransition(this.mChild);
                this.mTransitionEnded = true;
            }
        }
    }
    
    private static final class FragmentLifecycleCallbacksHolder
    {
        final FragmentLifecycleCallbacks mCallback;
        final boolean mRecursive;
        
        FragmentLifecycleCallbacksHolder(final FragmentLifecycleCallbacks mCallback, final boolean mRecursive) {
            this.mCallback = mCallback;
            this.mRecursive = mRecursive;
        }
    }
    
    static class FragmentTag
    {
        public static final int[] Fragment;
        public static final int Fragment_id = 1;
        public static final int Fragment_name = 0;
        public static final int Fragment_tag = 2;
        
        static {
            Fragment = new int[] { 16842755, 16842960, 16842961 };
        }
        
        private FragmentTag() {
        }
    }
    
    interface OpGenerator
    {
        boolean generateOps(final ArrayList<BackStackRecord> p0, final ArrayList<Boolean> p1);
    }
    
    private class PopBackStackState implements OpGenerator
    {
        final int mFlags;
        final int mId;
        final String mName;
        
        PopBackStackState(final String mName, final int mId, final int mFlags) {
            this.mName = mName;
            this.mId = mId;
            this.mFlags = mFlags;
        }
        
        @Override
        public boolean generateOps(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2) {
            if (FragmentManagerImpl.this.mPrimaryNav != null && this.mId < 0 && this.mName == null) {
                final FragmentManager peekChildFragmentManager = FragmentManagerImpl.this.mPrimaryNav.peekChildFragmentManager();
                if (peekChildFragmentManager != null && peekChildFragmentManager.popBackStackImmediate()) {
                    return false;
                }
            }
            return FragmentManagerImpl.this.popBackStackState(list, list2, this.mName, this.mId, this.mFlags);
        }
    }
    
    static class StartEnterTransitionListener implements OnStartEnterTransitionListener
    {
        final boolean mIsBack;
        private int mNumPostponed;
        final BackStackRecord mRecord;
        
        StartEnterTransitionListener(final BackStackRecord mRecord, final boolean mIsBack) {
            this.mIsBack = mIsBack;
            this.mRecord = mRecord;
        }
        
        public void cancelTransaction() {
            this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, false, false);
        }
        
        public void completeTransaction() {
            final int mNumPostponed = this.mNumPostponed;
            boolean b = false;
            final boolean b2 = mNumPostponed > 0;
            final FragmentManagerImpl mManager = this.mRecord.mManager;
            for (int size = mManager.mAdded.size(), i = 0; i < size; ++i) {
                final Fragment fragment = mManager.mAdded.get(i);
                fragment.setOnStartEnterTransitionListener(null);
                if (b2 && fragment.isPostponed()) {
                    fragment.startPostponedEnterTransition();
                }
            }
            final FragmentManagerImpl mManager2 = this.mRecord.mManager;
            final BackStackRecord mRecord = this.mRecord;
            final boolean mIsBack = this.mIsBack;
            if (!b2) {
                b = true;
            }
            mManager2.completeExecute(mRecord, mIsBack, b, true);
        }
        
        public boolean isReady() {
            return this.mNumPostponed == 0;
        }
        
        @Override
        public void onStartEnterTransition() {
            final int mNumPostponed = this.mNumPostponed - 1;
            this.mNumPostponed = mNumPostponed;
            if (mNumPostponed != 0) {
                return;
            }
            this.mRecord.mManager.scheduleCommit();
        }
        
        @Override
        public void startListening() {
            ++this.mNumPostponed;
        }
    }
}
