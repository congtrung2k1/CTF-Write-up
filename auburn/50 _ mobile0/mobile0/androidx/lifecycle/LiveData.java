// 
// Decompiled by Procyon v0.5.36
// 

package androidx.lifecycle;

import java.util.Iterator;
import java.util.Map;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.arch.core.internal.SafeIterableMap;

public abstract class LiveData<T>
{
    static final Object NOT_SET;
    static final int START_VERSION = -1;
    int mActiveCount;
    private volatile Object mData;
    final Object mDataLock;
    private boolean mDispatchInvalidated;
    private boolean mDispatchingValue;
    private SafeIterableMap<Observer<? super T>, ObserverWrapper> mObservers;
    volatile Object mPendingData;
    private final Runnable mPostValueRunnable;
    private int mVersion;
    
    static {
        NOT_SET = new Object();
    }
    
    public LiveData() {
        this.mDataLock = new Object();
        this.mObservers = new SafeIterableMap<Observer<? super T>, ObserverWrapper>();
        this.mActiveCount = 0;
        this.mData = LiveData.NOT_SET;
        this.mPendingData = LiveData.NOT_SET;
        this.mVersion = -1;
        this.mPostValueRunnable = new Runnable() {
            final /* synthetic */ LiveData this$0;
            
            @Override
            public void run() {
                final Object mDataLock = LiveData.this.mDataLock;
                // monitorenter(mDataLock)
                Object mPendingData;
                try {
                    mPendingData = LiveData.this.mPendingData;
                    final Runnable runnable = this;
                    final LiveData liveData = runnable.this$0;
                    final Object o = LiveData.NOT_SET;
                    liveData.mPendingData = o;
                    final Object o2 = mDataLock;
                    // monitorexit(o2)
                    final Runnable runnable2 = this;
                    final LiveData liveData2 = runnable2.this$0;
                    final Object o3 = mPendingData;
                    liveData2.setValue(o3);
                    return;
                }
                finally {
                    final Object o5;
                    final Object o4 = o5;
                }
                while (true) {
                    try {
                        final Runnable runnable = this;
                        final LiveData liveData = runnable.this$0;
                        final Object o = LiveData.NOT_SET;
                        liveData.mPendingData = o;
                        final Object o2 = mDataLock;
                        // monitorexit(o2)
                        final Runnable runnable2 = this;
                        final LiveData liveData2 = runnable2.this$0;
                        final Object o3 = mPendingData;
                        liveData2.setValue(o3);
                        return;
                        // monitorexit(mDataLock)
                        throw;
                    }
                    finally {
                        continue;
                    }
                    break;
                }
            }
        };
    }
    
    private static void assertMainThread(final String str) {
        if (ArchTaskExecutor.getInstance().isMainThread()) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Cannot invoke ");
        sb.append(str);
        sb.append(" on a background");
        sb.append(" thread");
        throw new IllegalStateException(sb.toString());
    }
    
    private void considerNotify(final ObserverWrapper observerWrapper) {
        if (!observerWrapper.mActive) {
            return;
        }
        if (!observerWrapper.shouldBeActive()) {
            observerWrapper.activeStateChanged(false);
            return;
        }
        final int mLastVersion = observerWrapper.mLastVersion;
        final int mVersion = this.mVersion;
        if (mLastVersion >= mVersion) {
            return;
        }
        observerWrapper.mLastVersion = mVersion;
        observerWrapper.mObserver.onChanged((Object)this.mData);
    }
    
    void dispatchingValue(ObserverWrapper observerWrapper) {
        if (this.mDispatchingValue) {
            this.mDispatchInvalidated = true;
            return;
        }
        this.mDispatchingValue = true;
        while (true) {
            this.mDispatchInvalidated = false;
            ObserverWrapper observerWrapper2 = null;
            Label_0086: {
                if (observerWrapper != null) {
                    this.considerNotify(observerWrapper);
                    observerWrapper2 = null;
                }
                else {
                    final SafeIterableMap.IteratorWithAdditions iteratorWithAdditions = this.mObservers.iteratorWithAdditions();
                    do {
                        observerWrapper2 = observerWrapper;
                        if (!iteratorWithAdditions.hasNext()) {
                            break Label_0086;
                        }
                        this.considerNotify(((Iterator<Map.Entry<K, ObserverWrapper>>)iteratorWithAdditions).next().getValue());
                    } while (!this.mDispatchInvalidated);
                    observerWrapper2 = observerWrapper;
                }
            }
            if (!this.mDispatchInvalidated) {
                break;
            }
            observerWrapper = observerWrapper2;
        }
        this.mDispatchingValue = false;
    }
    
    public T getValue() {
        final Object mData = this.mData;
        if (mData != LiveData.NOT_SET) {
            return (T)mData;
        }
        return null;
    }
    
    int getVersion() {
        return this.mVersion;
    }
    
    public boolean hasActiveObservers() {
        return this.mActiveCount > 0;
    }
    
    public boolean hasObservers() {
        return this.mObservers.size() > 0;
    }
    
    public void observe(final LifecycleOwner lifecycleOwner, final Observer<? super T> observer) {
        assertMainThread("observe");
        if (lifecycleOwner.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
            return;
        }
        final LifecycleBoundObserver lifecycleBoundObserver = new LifecycleBoundObserver(lifecycleOwner, observer);
        final ObserverWrapper observerWrapper = this.mObservers.putIfAbsent(observer, (ObserverWrapper)lifecycleBoundObserver);
        if (observerWrapper != null && !observerWrapper.isAttachedTo(lifecycleOwner)) {
            throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
        }
        if (observerWrapper != null) {
            return;
        }
        lifecycleOwner.getLifecycle().addObserver(lifecycleBoundObserver);
    }
    
    public void observeForever(final Observer<? super T> observer) {
        assertMainThread("observeForever");
        final AlwaysActiveObserver alwaysActiveObserver = new AlwaysActiveObserver(observer);
        final ObserverWrapper observerWrapper = this.mObservers.putIfAbsent(observer, (ObserverWrapper)alwaysActiveObserver);
        if (observerWrapper != null && observerWrapper instanceof LifecycleBoundObserver) {
            throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
        }
        if (observerWrapper != null) {
            return;
        }
        ((ObserverWrapper)alwaysActiveObserver).activeStateChanged(true);
    }
    
    protected void onActive() {
    }
    
    protected void onInactive() {
    }
    
    protected void postValue(final T p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        androidx/lifecycle/LiveData.mDataLock:Ljava/lang/Object;
        //     4: astore_2       
        //     5: aload_2        
        //     6: monitorenter   
        //     7: iconst_0       
        //     8: istore_3       
        //     9: aload_0        
        //    10: getfield        androidx/lifecycle/LiveData.mPendingData:Ljava/lang/Object;
        //    13: astore          4
        //    15: getstatic       androidx/lifecycle/LiveData.NOT_SET:Ljava/lang/Object;
        //    18: astore          5
        //    20: aload           4
        //    22: aload           5
        //    24: if_acmpne       29
        //    27: iconst_1       
        //    28: istore_3       
        //    29: aload_0        
        //    30: aload_1        
        //    31: putfield        androidx/lifecycle/LiveData.mPendingData:Ljava/lang/Object;
        //    34: aload_2        
        //    35: monitorexit    
        //    36: iload_3        
        //    37: ifne            41
        //    40: return         
        //    41: invokestatic    androidx/arch/core/executor/ArchTaskExecutor.getInstance:()Landroidx/arch/core/executor/ArchTaskExecutor;
        //    44: aload_0        
        //    45: getfield        androidx/lifecycle/LiveData.mPostValueRunnable:Ljava/lang/Runnable;
        //    48: invokevirtual   androidx/arch/core/executor/ArchTaskExecutor.postToMainThread:(Ljava/lang/Runnable;)V
        //    51: return         
        //    52: astore_1       
        //    53: aload_2        
        //    54: monitorexit    
        //    55: aload_1        
        //    56: athrow         
        //    57: astore_1       
        //    58: goto            53
        //    Signature:
        //  (TT;)V
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  9      20     52     53     Any
        //  29     36     57     61     Any
        //  53     55     57     61     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0029:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public void removeObserver(final Observer<? super T> observer) {
        assertMainThread("removeObserver");
        final ObserverWrapper observerWrapper = this.mObservers.remove(observer);
        if (observerWrapper == null) {
            return;
        }
        observerWrapper.detachObserver();
        observerWrapper.activeStateChanged(false);
    }
    
    public void removeObservers(final LifecycleOwner lifecycleOwner) {
        assertMainThread("removeObservers");
        for (final Map.Entry<Observer<? super T>, ObserverWrapper> entry : this.mObservers) {
            if (entry.getValue().isAttachedTo(lifecycleOwner)) {
                this.removeObserver(entry.getKey());
            }
        }
    }
    
    protected void setValue(final T mData) {
        assertMainThread("setValue");
        ++this.mVersion;
        this.mData = mData;
        this.dispatchingValue(null);
    }
    
    private class AlwaysActiveObserver extends ObserverWrapper
    {
        AlwaysActiveObserver(final Observer<? super T> observer) {
            super(observer);
        }
        
        @Override
        boolean shouldBeActive() {
            return true;
        }
    }
    
    class LifecycleBoundObserver extends ObserverWrapper implements GenericLifecycleObserver
    {
        final LifecycleOwner mOwner;
        
        LifecycleBoundObserver(final LifecycleOwner mOwner, final Observer<? super T> observer) {
            super(observer);
            this.mOwner = mOwner;
        }
        
        @Override
        void detachObserver() {
            this.mOwner.getLifecycle().removeObserver(this);
        }
        
        @Override
        boolean isAttachedTo(final LifecycleOwner lifecycleOwner) {
            return this.mOwner == lifecycleOwner;
        }
        
        @Override
        public void onStateChanged(final LifecycleOwner lifecycleOwner, final Lifecycle.Event event) {
            if (this.mOwner.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
                LiveData.this.removeObserver(this.mObserver);
                return;
            }
            ((ObserverWrapper)this).activeStateChanged(this.shouldBeActive());
        }
        
        @Override
        boolean shouldBeActive() {
            return this.mOwner.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED);
        }
    }
    
    private abstract class ObserverWrapper
    {
        boolean mActive;
        int mLastVersion;
        final Observer<? super T> mObserver;
        
        ObserverWrapper(final Observer<? super T> mObserver) {
            this.mLastVersion = -1;
            this.mObserver = mObserver;
        }
        
        void activeStateChanged(final boolean mActive) {
            if (mActive == this.mActive) {
                return;
            }
            this.mActive = mActive;
            final int mActiveCount = LiveData.this.mActiveCount;
            int n = 1;
            final boolean b = mActiveCount == 0;
            final LiveData this$0 = LiveData.this;
            final int mActiveCount2 = this$0.mActiveCount;
            if (!this.mActive) {
                n = -1;
            }
            this$0.mActiveCount = mActiveCount2 + n;
            if (b && this.mActive) {
                LiveData.this.onActive();
            }
            if (LiveData.this.mActiveCount == 0 && !this.mActive) {
                LiveData.this.onInactive();
            }
            if (this.mActive) {
                LiveData.this.dispatchingValue(this);
            }
        }
        
        void detachObserver() {
        }
        
        boolean isAttachedTo(final LifecycleOwner lifecycleOwner) {
            return false;
        }
        
        abstract boolean shouldBeActive();
    }
}
