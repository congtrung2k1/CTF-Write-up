// 
// Decompiled by Procyon v0.5.36
// 

package androidx.lifecycle;

import androidx.arch.core.executor.ArchTaskExecutor;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ComputableLiveData<T>
{
    final AtomicBoolean mComputing;
    final Executor mExecutor;
    final AtomicBoolean mInvalid;
    final Runnable mInvalidationRunnable;
    final LiveData<T> mLiveData;
    final Runnable mRefreshRunnable;
    
    public ComputableLiveData() {
        this(ArchTaskExecutor.getIOThreadExecutor());
    }
    
    public ComputableLiveData(final Executor mExecutor) {
        this.mInvalid = new AtomicBoolean(true);
        this.mComputing = new AtomicBoolean(false);
        this.mRefreshRunnable = new Runnable() {
            @Override
            public void run() {
                int n;
                do {
                    n = 0;
                    final int n2 = 0;
                    if (ComputableLiveData.this.mComputing.compareAndSet(false, true)) {
                        T compute = null;
                        n = n2;
                        try {
                            while (ComputableLiveData.this.mInvalid.compareAndSet(true, false)) {
                                n = 1;
                                compute = ComputableLiveData.this.compute();
                            }
                            if (n == 0) {
                                continue;
                            }
                            ComputableLiveData.this.mLiveData.postValue(compute);
                        }
                        finally {
                            ComputableLiveData.this.mComputing.set(false);
                        }
                    }
                } while (n != 0 && ComputableLiveData.this.mInvalid.get());
            }
        };
        this.mInvalidationRunnable = new Runnable() {
            @Override
            public void run() {
                final boolean hasActiveObservers = ComputableLiveData.this.mLiveData.hasActiveObservers();
                if (ComputableLiveData.this.mInvalid.compareAndSet(false, true) && hasActiveObservers) {
                    ComputableLiveData.this.mExecutor.execute(ComputableLiveData.this.mRefreshRunnable);
                }
            }
        };
        this.mExecutor = mExecutor;
        this.mLiveData = new LiveData<T>() {
            @Override
            protected void onActive() {
                ComputableLiveData.this.mExecutor.execute(ComputableLiveData.this.mRefreshRunnable);
            }
        };
    }
    
    protected abstract T compute();
    
    public LiveData<T> getLiveData() {
        return this.mLiveData;
    }
    
    public void invalidate() {
        ArchTaskExecutor.getInstance().executeOnMainThread(this.mInvalidationRunnable);
    }
}
