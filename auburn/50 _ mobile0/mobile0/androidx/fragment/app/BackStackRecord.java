// 
// Decompiled by Procyon v0.5.36
// 

package androidx.fragment.app;

import java.io.FileDescriptor;
import java.io.Writer;
import java.io.PrintWriter;
import androidx.core.util.LogWriter;
import android.util.Log;
import androidx.core.view.ViewCompat;
import android.view.View;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

final class BackStackRecord extends FragmentTransaction implements BackStackEntry, OpGenerator
{
    static final int OP_ADD = 1;
    static final int OP_ATTACH = 7;
    static final int OP_DETACH = 6;
    static final int OP_HIDE = 4;
    static final int OP_NULL = 0;
    static final int OP_REMOVE = 3;
    static final int OP_REPLACE = 2;
    static final int OP_SET_PRIMARY_NAV = 8;
    static final int OP_SHOW = 5;
    static final int OP_UNSET_PRIMARY_NAV = 9;
    static final String TAG = "FragmentManager";
    boolean mAddToBackStack;
    boolean mAllowAddToBackStack;
    int mBreadCrumbShortTitleRes;
    CharSequence mBreadCrumbShortTitleText;
    int mBreadCrumbTitleRes;
    CharSequence mBreadCrumbTitleText;
    ArrayList<Runnable> mCommitRunnables;
    boolean mCommitted;
    int mEnterAnim;
    int mExitAnim;
    int mIndex;
    final FragmentManagerImpl mManager;
    String mName;
    ArrayList<Op> mOps;
    int mPopEnterAnim;
    int mPopExitAnim;
    boolean mReorderingAllowed;
    ArrayList<String> mSharedElementSourceNames;
    ArrayList<String> mSharedElementTargetNames;
    int mTransition;
    int mTransitionStyle;
    
    public BackStackRecord(final FragmentManagerImpl mManager) {
        this.mOps = new ArrayList<Op>();
        this.mAllowAddToBackStack = true;
        this.mIndex = -1;
        this.mReorderingAllowed = false;
        this.mManager = mManager;
    }
    
    private void doAddOp(final int mContainerId, final Fragment obj, final String str, final int n) {
        final Class<? extends Fragment> class1 = obj.getClass();
        final int modifiers = class1.getModifiers();
        if (!class1.isAnonymousClass() && Modifier.isPublic(modifiers) && (!class1.isMemberClass() || Modifier.isStatic(modifiers))) {
            obj.mFragmentManager = this.mManager;
            if (str != null) {
                if (obj.mTag != null && !str.equals(obj.mTag)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Can't change tag of fragment ");
                    sb.append(obj);
                    sb.append(": was ");
                    sb.append(obj.mTag);
                    sb.append(" now ");
                    sb.append(str);
                    throw new IllegalStateException(sb.toString());
                }
                obj.mTag = str;
            }
            if (mContainerId != 0) {
                if (mContainerId == -1) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Can't add fragment ");
                    sb2.append(obj);
                    sb2.append(" with tag ");
                    sb2.append(str);
                    sb2.append(" to container view with no id");
                    throw new IllegalArgumentException(sb2.toString());
                }
                if (obj.mFragmentId != 0 && obj.mFragmentId != mContainerId) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Can't change container ID of fragment ");
                    sb3.append(obj);
                    sb3.append(": was ");
                    sb3.append(obj.mFragmentId);
                    sb3.append(" now ");
                    sb3.append(mContainerId);
                    throw new IllegalStateException(sb3.toString());
                }
                obj.mFragmentId = mContainerId;
                obj.mContainerId = mContainerId;
            }
            this.addOp(new Op(n, obj));
            return;
        }
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("Fragment ");
        sb4.append(class1.getCanonicalName());
        sb4.append(" must be a public static class to be  properly recreated from");
        sb4.append(" instance state.");
        throw new IllegalStateException(sb4.toString());
    }
    
    private static boolean isFragmentPostponed(final Op op) {
        final Fragment fragment = op.fragment;
        return fragment != null && fragment.mAdded && fragment.mView != null && !fragment.mDetached && !fragment.mHidden && fragment.isPostponed();
    }
    
    @Override
    public FragmentTransaction add(final int n, final Fragment fragment) {
        this.doAddOp(n, fragment, null, 1);
        return this;
    }
    
    @Override
    public FragmentTransaction add(final int n, final Fragment fragment, final String s) {
        this.doAddOp(n, fragment, s, 1);
        return this;
    }
    
    @Override
    public FragmentTransaction add(final Fragment fragment, final String s) {
        this.doAddOp(0, fragment, s, 1);
        return this;
    }
    
    void addOp(final Op e) {
        this.mOps.add(e);
        e.enterAnim = this.mEnterAnim;
        e.exitAnim = this.mExitAnim;
        e.popEnterAnim = this.mPopEnterAnim;
        e.popExitAnim = this.mPopExitAnim;
    }
    
    @Override
    public FragmentTransaction addSharedElement(final View view, final String e) {
        if (FragmentTransition.supportsTransition()) {
            final String transitionName = ViewCompat.getTransitionName(view);
            if (transitionName == null) {
                throw new IllegalArgumentException("Unique transitionNames are required for all sharedElements");
            }
            if (this.mSharedElementSourceNames == null) {
                this.mSharedElementSourceNames = new ArrayList<String>();
                this.mSharedElementTargetNames = new ArrayList<String>();
            }
            else {
                if (this.mSharedElementTargetNames.contains(e)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("A shared element with the target name '");
                    sb.append(e);
                    sb.append("' has already been added to the transaction.");
                    throw new IllegalArgumentException(sb.toString());
                }
                if (this.mSharedElementSourceNames.contains(transitionName)) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("A shared element with the source name '");
                    sb2.append(transitionName);
                    sb2.append(" has already been added to the transaction.");
                    throw new IllegalArgumentException(sb2.toString());
                }
            }
            this.mSharedElementSourceNames.add(transitionName);
            this.mSharedElementTargetNames.add(e);
        }
        return this;
    }
    
    @Override
    public FragmentTransaction addToBackStack(final String mName) {
        if (this.mAllowAddToBackStack) {
            this.mAddToBackStack = true;
            this.mName = mName;
            return this;
        }
        throw new IllegalStateException("This FragmentTransaction is not allowed to be added to the back stack.");
    }
    
    @Override
    public FragmentTransaction attach(final Fragment fragment) {
        this.addOp(new Op(7, fragment));
        return this;
    }
    
    void bumpBackStackNesting(final int i) {
        if (!this.mAddToBackStack) {
            return;
        }
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Bump nesting in ");
            sb.append(this);
            sb.append(" by ");
            sb.append(i);
            Log.v("FragmentManager", sb.toString());
        }
        for (int size = this.mOps.size(), j = 0; j < size; ++j) {
            final Op op = this.mOps.get(j);
            if (op.fragment != null) {
                final Fragment fragment = op.fragment;
                fragment.mBackStackNesting += i;
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Bump nesting of ");
                    sb2.append(op.fragment);
                    sb2.append(" to ");
                    sb2.append(op.fragment.mBackStackNesting);
                    Log.v("FragmentManager", sb2.toString());
                }
            }
        }
    }
    
    @Override
    public int commit() {
        return this.commitInternal(false);
    }
    
    @Override
    public int commitAllowingStateLoss() {
        return this.commitInternal(true);
    }
    
    int commitInternal(final boolean b) {
        if (!this.mCommitted) {
            if (FragmentManagerImpl.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Commit: ");
                sb.append(this);
                Log.v("FragmentManager", sb.toString());
                final PrintWriter printWriter = new PrintWriter(new LogWriter("FragmentManager"));
                this.dump("  ", null, printWriter, null);
                printWriter.close();
            }
            this.mCommitted = true;
            if (this.mAddToBackStack) {
                this.mIndex = this.mManager.allocBackStackIndex(this);
            }
            else {
                this.mIndex = -1;
            }
            this.mManager.enqueueAction((FragmentManagerImpl.OpGenerator)this, b);
            return this.mIndex;
        }
        throw new IllegalStateException("commit already called");
    }
    
    @Override
    public void commitNow() {
        this.disallowAddToBackStack();
        this.mManager.execSingleAction((FragmentManagerImpl.OpGenerator)this, false);
    }
    
    @Override
    public void commitNowAllowingStateLoss() {
        this.disallowAddToBackStack();
        this.mManager.execSingleAction((FragmentManagerImpl.OpGenerator)this, true);
    }
    
    @Override
    public FragmentTransaction detach(final Fragment fragment) {
        this.addOp(new Op(6, fragment));
        return this;
    }
    
    @Override
    public FragmentTransaction disallowAddToBackStack() {
        if (!this.mAddToBackStack) {
            this.mAllowAddToBackStack = false;
            return this;
        }
        throw new IllegalStateException("This transaction is already being added to the back stack");
    }
    
    public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
        this.dump(s, printWriter, true);
    }
    
    public void dump(final String s, final PrintWriter printWriter, final boolean b) {
        if (b) {
            printWriter.print(s);
            printWriter.print("mName=");
            printWriter.print(this.mName);
            printWriter.print(" mIndex=");
            printWriter.print(this.mIndex);
            printWriter.print(" mCommitted=");
            printWriter.println(this.mCommitted);
            if (this.mTransition != 0) {
                printWriter.print(s);
                printWriter.print("mTransition=#");
                printWriter.print(Integer.toHexString(this.mTransition));
                printWriter.print(" mTransitionStyle=#");
                printWriter.println(Integer.toHexString(this.mTransitionStyle));
            }
            if (this.mEnterAnim != 0 || this.mExitAnim != 0) {
                printWriter.print(s);
                printWriter.print("mEnterAnim=#");
                printWriter.print(Integer.toHexString(this.mEnterAnim));
                printWriter.print(" mExitAnim=#");
                printWriter.println(Integer.toHexString(this.mExitAnim));
            }
            if (this.mPopEnterAnim != 0 || this.mPopExitAnim != 0) {
                printWriter.print(s);
                printWriter.print("mPopEnterAnim=#");
                printWriter.print(Integer.toHexString(this.mPopEnterAnim));
                printWriter.print(" mPopExitAnim=#");
                printWriter.println(Integer.toHexString(this.mPopExitAnim));
            }
            if (this.mBreadCrumbTitleRes != 0 || this.mBreadCrumbTitleText != null) {
                printWriter.print(s);
                printWriter.print("mBreadCrumbTitleRes=#");
                printWriter.print(Integer.toHexString(this.mBreadCrumbTitleRes));
                printWriter.print(" mBreadCrumbTitleText=");
                printWriter.println(this.mBreadCrumbTitleText);
            }
            if (this.mBreadCrumbShortTitleRes != 0 || this.mBreadCrumbShortTitleText != null) {
                printWriter.print(s);
                printWriter.print("mBreadCrumbShortTitleRes=#");
                printWriter.print(Integer.toHexString(this.mBreadCrumbShortTitleRes));
                printWriter.print(" mBreadCrumbShortTitleText=");
                printWriter.println(this.mBreadCrumbShortTitleText);
            }
        }
        if (!this.mOps.isEmpty()) {
            printWriter.print(s);
            printWriter.println("Operations:");
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append("    ");
            sb.toString();
            for (int size = this.mOps.size(), i = 0; i < size; ++i) {
                final Op op = this.mOps.get(i);
                String string = null;
                switch (op.cmd) {
                    default: {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("cmd=");
                        sb2.append(op.cmd);
                        string = sb2.toString();
                        break;
                    }
                    case 9: {
                        string = "UNSET_PRIMARY_NAV";
                        break;
                    }
                    case 8: {
                        string = "SET_PRIMARY_NAV";
                        break;
                    }
                    case 7: {
                        string = "ATTACH";
                        break;
                    }
                    case 6: {
                        string = "DETACH";
                        break;
                    }
                    case 5: {
                        string = "SHOW";
                        break;
                    }
                    case 4: {
                        string = "HIDE";
                        break;
                    }
                    case 3: {
                        string = "REMOVE";
                        break;
                    }
                    case 2: {
                        string = "REPLACE";
                        break;
                    }
                    case 1: {
                        string = "ADD";
                        break;
                    }
                    case 0: {
                        string = "NULL";
                        break;
                    }
                }
                printWriter.print(s);
                printWriter.print("  Op #");
                printWriter.print(i);
                printWriter.print(": ");
                printWriter.print(string);
                printWriter.print(" ");
                printWriter.println(op.fragment);
                if (b) {
                    if (op.enterAnim != 0 || op.exitAnim != 0) {
                        printWriter.print(s);
                        printWriter.print("enterAnim=#");
                        printWriter.print(Integer.toHexString(op.enterAnim));
                        printWriter.print(" exitAnim=#");
                        printWriter.println(Integer.toHexString(op.exitAnim));
                    }
                    if (op.popEnterAnim != 0 || op.popExitAnim != 0) {
                        printWriter.print(s);
                        printWriter.print("popEnterAnim=#");
                        printWriter.print(Integer.toHexString(op.popEnterAnim));
                        printWriter.print(" popExitAnim=#");
                        printWriter.println(Integer.toHexString(op.popExitAnim));
                    }
                }
            }
        }
    }
    
    void executeOps() {
        for (int size = this.mOps.size(), i = 0; i < size; ++i) {
            final Op op = this.mOps.get(i);
            final Fragment fragment = op.fragment;
            if (fragment != null) {
                fragment.setNextTransition(this.mTransition, this.mTransitionStyle);
            }
            switch (op.cmd) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unknown cmd: ");
                    sb.append(op.cmd);
                    throw new IllegalArgumentException(sb.toString());
                }
                case 9: {
                    this.mManager.setPrimaryNavigationFragment(null);
                    break;
                }
                case 8: {
                    this.mManager.setPrimaryNavigationFragment(fragment);
                    break;
                }
                case 7: {
                    fragment.setNextAnim(op.enterAnim);
                    this.mManager.attachFragment(fragment);
                    break;
                }
                case 6: {
                    fragment.setNextAnim(op.exitAnim);
                    this.mManager.detachFragment(fragment);
                    break;
                }
                case 5: {
                    fragment.setNextAnim(op.enterAnim);
                    this.mManager.showFragment(fragment);
                    break;
                }
                case 4: {
                    fragment.setNextAnim(op.exitAnim);
                    this.mManager.hideFragment(fragment);
                    break;
                }
                case 3: {
                    fragment.setNextAnim(op.exitAnim);
                    this.mManager.removeFragment(fragment);
                    break;
                }
                case 1: {
                    fragment.setNextAnim(op.enterAnim);
                    this.mManager.addFragment(fragment, false);
                    break;
                }
            }
            if (!this.mReorderingAllowed && op.cmd != 1 && fragment != null) {
                this.mManager.moveFragmentToExpectedState(fragment);
            }
        }
        if (!this.mReorderingAllowed) {
            final FragmentManagerImpl mManager = this.mManager;
            mManager.moveToState(mManager.mCurState, true);
        }
    }
    
    void executePopOps(final boolean b) {
        for (int i = this.mOps.size() - 1; i >= 0; --i) {
            final Op op = this.mOps.get(i);
            final Fragment fragment = op.fragment;
            if (fragment != null) {
                fragment.setNextTransition(FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle);
            }
            switch (op.cmd) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unknown cmd: ");
                    sb.append(op.cmd);
                    throw new IllegalArgumentException(sb.toString());
                }
                case 9: {
                    this.mManager.setPrimaryNavigationFragment(fragment);
                    break;
                }
                case 8: {
                    this.mManager.setPrimaryNavigationFragment(null);
                    break;
                }
                case 7: {
                    fragment.setNextAnim(op.popExitAnim);
                    this.mManager.detachFragment(fragment);
                    break;
                }
                case 6: {
                    fragment.setNextAnim(op.popEnterAnim);
                    this.mManager.attachFragment(fragment);
                    break;
                }
                case 5: {
                    fragment.setNextAnim(op.popExitAnim);
                    this.mManager.hideFragment(fragment);
                    break;
                }
                case 4: {
                    fragment.setNextAnim(op.popEnterAnim);
                    this.mManager.showFragment(fragment);
                    break;
                }
                case 3: {
                    fragment.setNextAnim(op.popEnterAnim);
                    this.mManager.addFragment(fragment, false);
                    break;
                }
                case 1: {
                    fragment.setNextAnim(op.popExitAnim);
                    this.mManager.removeFragment(fragment);
                    break;
                }
            }
            if (!this.mReorderingAllowed && op.cmd != 3 && fragment != null) {
                this.mManager.moveFragmentToExpectedState(fragment);
            }
        }
        if (!this.mReorderingAllowed && b) {
            final FragmentManagerImpl mManager = this.mManager;
            mManager.moveToState(mManager.mCurState, true);
        }
    }
    
    Fragment expandOps(final ArrayList<Fragment> list, Fragment fragment) {
        int i = 0;
        Fragment fragment2 = fragment;
        while (i < this.mOps.size()) {
            final Op op = this.mOps.get(i);
            final int cmd = op.cmd;
            int n3 = 0;
            Label_0439: {
                if (cmd != 1) {
                    if (cmd == 2) {
                        final Fragment fragment3 = op.fragment;
                        final int mContainerId = fragment3.mContainerId;
                        int n = 0;
                        int j = list.size() - 1;
                        fragment = fragment2;
                        while (j >= 0) {
                            final Fragment o = list.get(j);
                            int index = i;
                            int n2 = n;
                            Fragment fragment4 = fragment;
                            if (o.mContainerId == mContainerId) {
                                if (o == fragment3) {
                                    n2 = 1;
                                    index = i;
                                    fragment4 = fragment;
                                }
                                else {
                                    index = i;
                                    if (o == (fragment4 = fragment)) {
                                        this.mOps.add(i, new Op(9, o));
                                        index = i + 1;
                                        fragment4 = null;
                                    }
                                    final Op element = new Op(3, o);
                                    element.enterAnim = op.enterAnim;
                                    element.popEnterAnim = op.popEnterAnim;
                                    element.exitAnim = op.exitAnim;
                                    element.popExitAnim = op.popExitAnim;
                                    this.mOps.add(index, element);
                                    list.remove(o);
                                    ++index;
                                    n2 = n;
                                }
                            }
                            --j;
                            i = index;
                            n = n2;
                            fragment = fragment4;
                        }
                        if (n != 0) {
                            this.mOps.remove(i);
                            --i;
                        }
                        else {
                            op.cmd = 1;
                            list.add(fragment3);
                        }
                        n3 = i;
                        break Label_0439;
                    }
                    if (cmd != 3 && cmd != 6) {
                        if (cmd != 7) {
                            if (cmd != 8) {
                                n3 = i;
                                fragment = fragment2;
                                break Label_0439;
                            }
                            this.mOps.add(i, new Op(9, fragment2));
                            n3 = i + 1;
                            fragment = op.fragment;
                            break Label_0439;
                        }
                    }
                    else {
                        list.remove(op.fragment);
                        n3 = i;
                        if (op.fragment == (fragment = fragment2)) {
                            this.mOps.add(i, new Op(9, op.fragment));
                            n3 = i + 1;
                            fragment = null;
                        }
                        break Label_0439;
                    }
                }
                list.add(op.fragment);
                fragment = fragment2;
                n3 = i;
            }
            i = n3 + 1;
            fragment2 = fragment;
        }
        return fragment2;
    }
    
    @Override
    public boolean generateOps(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2) {
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Run: ");
            sb.append(this);
            Log.v("FragmentManager", sb.toString());
        }
        list.add(this);
        list2.add(false);
        if (this.mAddToBackStack) {
            this.mManager.addBackStackState(this);
        }
        return true;
    }
    
    @Override
    public CharSequence getBreadCrumbShortTitle() {
        if (this.mBreadCrumbShortTitleRes != 0) {
            return this.mManager.mHost.getContext().getText(this.mBreadCrumbShortTitleRes);
        }
        return this.mBreadCrumbShortTitleText;
    }
    
    @Override
    public int getBreadCrumbShortTitleRes() {
        return this.mBreadCrumbShortTitleRes;
    }
    
    @Override
    public CharSequence getBreadCrumbTitle() {
        if (this.mBreadCrumbTitleRes != 0) {
            return this.mManager.mHost.getContext().getText(this.mBreadCrumbTitleRes);
        }
        return this.mBreadCrumbTitleText;
    }
    
    @Override
    public int getBreadCrumbTitleRes() {
        return this.mBreadCrumbTitleRes;
    }
    
    @Override
    public int getId() {
        return this.mIndex;
    }
    
    @Override
    public String getName() {
        return this.mName;
    }
    
    public int getTransition() {
        return this.mTransition;
    }
    
    public int getTransitionStyle() {
        return this.mTransitionStyle;
    }
    
    @Override
    public FragmentTransaction hide(final Fragment fragment) {
        this.addOp(new Op(4, fragment));
        return this;
    }
    
    boolean interactsWith(final int n) {
        final int size = this.mOps.size();
        int index = 0;
        while (true) {
            int mContainerId = 0;
            if (index >= size) {
                return false;
            }
            final Op op = this.mOps.get(index);
            if (op.fragment != null) {
                mContainerId = op.fragment.mContainerId;
            }
            if (mContainerId != 0 && mContainerId == n) {
                return true;
            }
            ++index;
        }
    }
    
    boolean interactsWith(final ArrayList<BackStackRecord> list, final int n, final int n2) {
        if (n2 == n) {
            return false;
        }
        final int size = this.mOps.size();
        int n3 = -1;
        int n4;
        for (int i = 0; i < size; ++i, n3 = n4) {
            final Op op = this.mOps.get(i);
            int mContainerId;
            if (op.fragment != null) {
                mContainerId = op.fragment.mContainerId;
            }
            else {
                mContainerId = 0;
            }
            n4 = n3;
            if (mContainerId != 0 && mContainerId != (n4 = n3)) {
                final int n5 = mContainerId;
                int index = n;
                while (true) {
                    n4 = n5;
                    if (index >= n2) {
                        break;
                    }
                    final BackStackRecord backStackRecord = list.get(index);
                    for (int size2 = backStackRecord.mOps.size(), j = 0; j < size2; ++j) {
                        final Op op2 = backStackRecord.mOps.get(j);
                        int mContainerId2;
                        if (op2.fragment != null) {
                            mContainerId2 = op2.fragment.mContainerId;
                        }
                        else {
                            mContainerId2 = 0;
                        }
                        if (mContainerId2 == mContainerId) {
                            return true;
                        }
                    }
                    ++index;
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean isAddToBackStackAllowed() {
        return this.mAllowAddToBackStack;
    }
    
    @Override
    public boolean isEmpty() {
        return this.mOps.isEmpty();
    }
    
    boolean isPostponed() {
        for (int i = 0; i < this.mOps.size(); ++i) {
            if (isFragmentPostponed(this.mOps.get(i))) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public FragmentTransaction remove(final Fragment fragment) {
        this.addOp(new Op(3, fragment));
        return this;
    }
    
    @Override
    public FragmentTransaction replace(final int n, final Fragment fragment) {
        return this.replace(n, fragment, null);
    }
    
    @Override
    public FragmentTransaction replace(final int n, final Fragment fragment, final String s) {
        if (n != 0) {
            this.doAddOp(n, fragment, s, 2);
            return this;
        }
        throw new IllegalArgumentException("Must use non-zero containerViewId");
    }
    
    @Override
    public FragmentTransaction runOnCommit(final Runnable e) {
        if (e != null) {
            this.disallowAddToBackStack();
            if (this.mCommitRunnables == null) {
                this.mCommitRunnables = new ArrayList<Runnable>();
            }
            this.mCommitRunnables.add(e);
            return this;
        }
        throw new IllegalArgumentException("runnable cannot be null");
    }
    
    public void runOnCommitRunnables() {
        final ArrayList<Runnable> mCommitRunnables = this.mCommitRunnables;
        if (mCommitRunnables != null) {
            for (int i = 0; i < mCommitRunnables.size(); ++i) {
                this.mCommitRunnables.get(i).run();
            }
            this.mCommitRunnables = null;
        }
    }
    
    @Override
    public FragmentTransaction setAllowOptimization(final boolean reorderingAllowed) {
        return this.setReorderingAllowed(reorderingAllowed);
    }
    
    @Override
    public FragmentTransaction setBreadCrumbShortTitle(final int mBreadCrumbShortTitleRes) {
        this.mBreadCrumbShortTitleRes = mBreadCrumbShortTitleRes;
        this.mBreadCrumbShortTitleText = null;
        return this;
    }
    
    @Override
    public FragmentTransaction setBreadCrumbShortTitle(final CharSequence mBreadCrumbShortTitleText) {
        this.mBreadCrumbShortTitleRes = 0;
        this.mBreadCrumbShortTitleText = mBreadCrumbShortTitleText;
        return this;
    }
    
    @Override
    public FragmentTransaction setBreadCrumbTitle(final int mBreadCrumbTitleRes) {
        this.mBreadCrumbTitleRes = mBreadCrumbTitleRes;
        this.mBreadCrumbTitleText = null;
        return this;
    }
    
    @Override
    public FragmentTransaction setBreadCrumbTitle(final CharSequence mBreadCrumbTitleText) {
        this.mBreadCrumbTitleRes = 0;
        this.mBreadCrumbTitleText = mBreadCrumbTitleText;
        return this;
    }
    
    @Override
    public FragmentTransaction setCustomAnimations(final int n, final int n2) {
        return this.setCustomAnimations(n, n2, 0, 0);
    }
    
    @Override
    public FragmentTransaction setCustomAnimations(final int mEnterAnim, final int mExitAnim, final int mPopEnterAnim, final int mPopExitAnim) {
        this.mEnterAnim = mEnterAnim;
        this.mExitAnim = mExitAnim;
        this.mPopEnterAnim = mPopEnterAnim;
        this.mPopExitAnim = mPopExitAnim;
        return this;
    }
    
    void setOnStartPostponedListener(final Fragment.OnStartEnterTransitionListener onStartEnterTransitionListener) {
        for (int i = 0; i < this.mOps.size(); ++i) {
            final Op op = this.mOps.get(i);
            if (isFragmentPostponed(op)) {
                op.fragment.setOnStartEnterTransitionListener(onStartEnterTransitionListener);
            }
        }
    }
    
    @Override
    public FragmentTransaction setPrimaryNavigationFragment(final Fragment fragment) {
        this.addOp(new Op(8, fragment));
        return this;
    }
    
    @Override
    public FragmentTransaction setReorderingAllowed(final boolean mReorderingAllowed) {
        this.mReorderingAllowed = mReorderingAllowed;
        return this;
    }
    
    @Override
    public FragmentTransaction setTransition(final int mTransition) {
        this.mTransition = mTransition;
        return this;
    }
    
    @Override
    public FragmentTransaction setTransitionStyle(final int mTransitionStyle) {
        this.mTransitionStyle = mTransitionStyle;
        return this;
    }
    
    @Override
    public FragmentTransaction show(final Fragment fragment) {
        this.addOp(new Op(5, fragment));
        return this;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(128);
        sb.append("BackStackEntry{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        if (this.mIndex >= 0) {
            sb.append(" #");
            sb.append(this.mIndex);
        }
        if (this.mName != null) {
            sb.append(" ");
            sb.append(this.mName);
        }
        sb.append("}");
        return sb.toString();
    }
    
    Fragment trackAddedFragmentsInPop(final ArrayList<Fragment> list, Fragment fragment) {
        for (int i = 0; i < this.mOps.size(); ++i) {
            final Op op = this.mOps.get(i);
            final int cmd = op.cmd;
            Label_0106: {
                if (cmd != 1) {
                    if (cmd != 3) {
                        switch (cmd) {
                            default: {
                                continue;
                            }
                            case 9: {
                                fragment = op.fragment;
                                continue;
                            }
                            case 8: {
                                fragment = null;
                                continue;
                            }
                            case 6: {
                                break;
                            }
                            case 7: {
                                break Label_0106;
                            }
                        }
                    }
                    list.add(op.fragment);
                    continue;
                }
            }
            list.remove(op.fragment);
        }
        return fragment;
    }
    
    static final class Op
    {
        int cmd;
        int enterAnim;
        int exitAnim;
        Fragment fragment;
        int popEnterAnim;
        int popExitAnim;
        
        Op() {
        }
        
        Op(final int cmd, final Fragment fragment) {
            this.cmd = cmd;
            this.fragment = fragment;
        }
    }
}
