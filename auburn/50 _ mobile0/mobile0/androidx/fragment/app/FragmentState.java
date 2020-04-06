// 
// Decompiled by Procyon v0.5.36
// 

package androidx.fragment.app;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.ViewModelStore;
import android.os.Parcel;
import android.os.Bundle;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

final class FragmentState implements Parcelable
{
    public static final Parcelable$Creator<FragmentState> CREATOR;
    final Bundle mArguments;
    final String mClassName;
    final int mContainerId;
    final boolean mDetached;
    final int mFragmentId;
    final boolean mFromLayout;
    final boolean mHidden;
    final int mIndex;
    Fragment mInstance;
    final boolean mRetainInstance;
    Bundle mSavedFragmentState;
    final String mTag;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<FragmentState>() {
            public FragmentState createFromParcel(final Parcel parcel) {
                return new FragmentState(parcel);
            }
            
            public FragmentState[] newArray(final int n) {
                return new FragmentState[n];
            }
        };
    }
    
    FragmentState(final Parcel parcel) {
        this.mClassName = parcel.readString();
        this.mIndex = parcel.readInt();
        final int int1 = parcel.readInt();
        final boolean b = true;
        this.mFromLayout = (int1 != 0);
        this.mFragmentId = parcel.readInt();
        this.mContainerId = parcel.readInt();
        this.mTag = parcel.readString();
        this.mRetainInstance = (parcel.readInt() != 0);
        this.mDetached = (parcel.readInt() != 0);
        this.mArguments = parcel.readBundle();
        this.mHidden = (parcel.readInt() != 0 && b);
        this.mSavedFragmentState = parcel.readBundle();
    }
    
    FragmentState(final Fragment fragment) {
        this.mClassName = fragment.getClass().getName();
        this.mIndex = fragment.mIndex;
        this.mFromLayout = fragment.mFromLayout;
        this.mFragmentId = fragment.mFragmentId;
        this.mContainerId = fragment.mContainerId;
        this.mTag = fragment.mTag;
        this.mRetainInstance = fragment.mRetainInstance;
        this.mDetached = fragment.mDetached;
        this.mArguments = fragment.mArguments;
        this.mHidden = fragment.mHidden;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public Fragment instantiate(final FragmentHostCallback fragmentHostCallback, final FragmentContainer fragmentContainer, final Fragment fragment, final FragmentManagerNonConfig mChildNonConfig, final ViewModelStore mViewModelStore) {
        if (this.mInstance == null) {
            final Context context = fragmentHostCallback.getContext();
            final Bundle mArguments = this.mArguments;
            if (mArguments != null) {
                mArguments.setClassLoader(context.getClassLoader());
            }
            if (fragmentContainer != null) {
                this.mInstance = fragmentContainer.instantiate(context, this.mClassName, this.mArguments);
            }
            else {
                this.mInstance = Fragment.instantiate(context, this.mClassName, this.mArguments);
            }
            final Bundle mSavedFragmentState = this.mSavedFragmentState;
            if (mSavedFragmentState != null) {
                mSavedFragmentState.setClassLoader(context.getClassLoader());
                this.mInstance.mSavedFragmentState = this.mSavedFragmentState;
            }
            this.mInstance.setIndex(this.mIndex, fragment);
            this.mInstance.mFromLayout = this.mFromLayout;
            this.mInstance.mRestored = true;
            this.mInstance.mFragmentId = this.mFragmentId;
            this.mInstance.mContainerId = this.mContainerId;
            this.mInstance.mTag = this.mTag;
            this.mInstance.mRetainInstance = this.mRetainInstance;
            this.mInstance.mDetached = this.mDetached;
            this.mInstance.mHidden = this.mHidden;
            this.mInstance.mFragmentManager = fragmentHostCallback.mFragmentManager;
            if (FragmentManagerImpl.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Instantiated fragment ");
                sb.append(this.mInstance);
                Log.v("FragmentManager", sb.toString());
            }
        }
        this.mInstance.mChildNonConfig = mChildNonConfig;
        this.mInstance.mViewModelStore = mViewModelStore;
        return this.mInstance;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(this.mClassName);
        parcel.writeInt(this.mIndex);
        parcel.writeInt((int)(this.mFromLayout ? 1 : 0));
        parcel.writeInt(this.mFragmentId);
        parcel.writeInt(this.mContainerId);
        parcel.writeString(this.mTag);
        parcel.writeInt((int)(this.mRetainInstance ? 1 : 0));
        parcel.writeInt((int)(this.mDetached ? 1 : 0));
        parcel.writeBundle(this.mArguments);
        parcel.writeInt((int)(this.mHidden ? 1 : 0));
        parcel.writeBundle(this.mSavedFragmentState);
    }
}
