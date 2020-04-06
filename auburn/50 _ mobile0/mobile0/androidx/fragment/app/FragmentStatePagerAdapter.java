// 
// Decompiled by Procyon v0.5.36
// 

package androidx.fragment.app;

import java.util.Iterator;
import android.util.Log;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import androidx.viewpager.widget.PagerAdapter;

public abstract class FragmentStatePagerAdapter extends PagerAdapter
{
    private static final boolean DEBUG = false;
    private static final String TAG = "FragmentStatePagerAdapt";
    private FragmentTransaction mCurTransaction;
    private Fragment mCurrentPrimaryItem;
    private final FragmentManager mFragmentManager;
    private ArrayList<Fragment> mFragments;
    private ArrayList<Fragment.SavedState> mSavedState;
    
    public FragmentStatePagerAdapter(final FragmentManager mFragmentManager) {
        this.mCurTransaction = null;
        this.mSavedState = new ArrayList<Fragment.SavedState>();
        this.mFragments = new ArrayList<Fragment>();
        this.mCurrentPrimaryItem = null;
        this.mFragmentManager = mFragmentManager;
    }
    
    @Override
    public void destroyItem(final ViewGroup viewGroup, final int n, final Object o) {
        final Fragment fragment = (Fragment)o;
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        while (this.mSavedState.size() <= n) {
            this.mSavedState.add(null);
        }
        final ArrayList<Fragment.SavedState> mSavedState = this.mSavedState;
        Fragment.SavedState saveFragmentInstanceState;
        if (fragment.isAdded()) {
            saveFragmentInstanceState = this.mFragmentManager.saveFragmentInstanceState(fragment);
        }
        else {
            saveFragmentInstanceState = null;
        }
        mSavedState.set(n, saveFragmentInstanceState);
        this.mFragments.set(n, null);
        this.mCurTransaction.remove(fragment);
    }
    
    @Override
    public void finishUpdate(final ViewGroup viewGroup) {
        final FragmentTransaction mCurTransaction = this.mCurTransaction;
        if (mCurTransaction != null) {
            mCurTransaction.commitNowAllowingStateLoss();
            this.mCurTransaction = null;
        }
    }
    
    public abstract Fragment getItem(final int p0);
    
    @Override
    public Object instantiateItem(final ViewGroup viewGroup, final int index) {
        if (this.mFragments.size() > index) {
            final Fragment fragment = this.mFragments.get(index);
            if (fragment != null) {
                return fragment;
            }
        }
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        final Fragment item = this.getItem(index);
        if (this.mSavedState.size() > index) {
            final Fragment.SavedState initialSavedState = this.mSavedState.get(index);
            if (initialSavedState != null) {
                item.setInitialSavedState(initialSavedState);
            }
        }
        while (this.mFragments.size() <= index) {
            this.mFragments.add(null);
        }
        item.setMenuVisibility(false);
        item.setUserVisibleHint(false);
        this.mFragments.set(index, item);
        this.mCurTransaction.add(viewGroup.getId(), item);
        return item;
    }
    
    @Override
    public boolean isViewFromObject(final View view, final Object o) {
        return ((Fragment)o).getView() == view;
    }
    
    @Override
    public void restoreState(final Parcelable parcelable, final ClassLoader classLoader) {
        if (parcelable != null) {
            final Bundle bundle = (Bundle)parcelable;
            bundle.setClassLoader(classLoader);
            final Parcelable[] parcelableArray = bundle.getParcelableArray("states");
            this.mSavedState.clear();
            this.mFragments.clear();
            if (parcelableArray != null) {
                for (int i = 0; i < parcelableArray.length; ++i) {
                    this.mSavedState.add((Fragment.SavedState)parcelableArray[i]);
                }
            }
            for (final String str : bundle.keySet()) {
                if (str.startsWith("f")) {
                    final int int1 = Integer.parseInt(str.substring(1));
                    final Fragment fragment = this.mFragmentManager.getFragment(bundle, str);
                    if (fragment != null) {
                        while (this.mFragments.size() <= int1) {
                            this.mFragments.add(null);
                        }
                        fragment.setMenuVisibility(false);
                        this.mFragments.set(int1, fragment);
                    }
                    else {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Bad fragment at key ");
                        sb.append(str);
                        Log.w("FragmentStatePagerAdapt", sb.toString());
                    }
                }
            }
        }
    }
    
    @Override
    public Parcelable saveState() {
        Bundle bundle = null;
        if (this.mSavedState.size() > 0) {
            bundle = new Bundle();
            final Fragment.SavedState[] a = new Fragment.SavedState[this.mSavedState.size()];
            this.mSavedState.toArray(a);
            bundle.putParcelableArray("states", (Parcelable[])a);
        }
        Object o;
        for (int i = 0; i < this.mFragments.size(); ++i, bundle = (Bundle)o) {
            final Fragment fragment = this.mFragments.get(i);
            o = bundle;
            if (fragment != null) {
                o = bundle;
                if (fragment.isAdded()) {
                    if ((o = bundle) == null) {
                        o = new Bundle();
                    }
                    final StringBuilder sb = new StringBuilder();
                    sb.append("f");
                    sb.append(i);
                    this.mFragmentManager.putFragment((Bundle)o, sb.toString(), fragment);
                }
            }
        }
        return (Parcelable)bundle;
    }
    
    @Override
    public void setPrimaryItem(final ViewGroup viewGroup, final int n, final Object o) {
        final Fragment mCurrentPrimaryItem = (Fragment)o;
        final Fragment mCurrentPrimaryItem2 = this.mCurrentPrimaryItem;
        if (mCurrentPrimaryItem != mCurrentPrimaryItem2) {
            if (mCurrentPrimaryItem2 != null) {
                mCurrentPrimaryItem2.setMenuVisibility(false);
                this.mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            mCurrentPrimaryItem.setMenuVisibility(true);
            mCurrentPrimaryItem.setUserVisibleHint(true);
            this.mCurrentPrimaryItem = mCurrentPrimaryItem;
        }
    }
    
    @Override
    public void startUpdate(final ViewGroup viewGroup) {
        if (viewGroup.getId() != -1) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("ViewPager with adapter ");
        sb.append(this);
        sb.append(" requires a view id");
        throw new IllegalStateException(sb.toString());
    }
}
