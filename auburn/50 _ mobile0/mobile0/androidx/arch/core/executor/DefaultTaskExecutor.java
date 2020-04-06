// 
// Decompiled by Procyon v0.5.36
// 

package androidx.arch.core.executor;

import android.os.Looper;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ThreadFactory;
import android.os.Handler;
import java.util.concurrent.ExecutorService;

public class DefaultTaskExecutor extends TaskExecutor
{
    private final ExecutorService mDiskIO;
    private final Object mLock;
    private volatile Handler mMainHandler;
    
    public DefaultTaskExecutor() {
        this.mLock = new Object();
        this.mDiskIO = Executors.newFixedThreadPool(2, new ThreadFactory() {
            private static final String THREAD_NAME_STEM = "arch_disk_io_%d";
            private final AtomicInteger mThreadId = new AtomicInteger(0);
            
            @Override
            public Thread newThread(final Runnable target) {
                final Thread thread = new Thread(target);
                thread.setName(String.format("arch_disk_io_%d", this.mThreadId.getAndIncrement()));
                return thread;
            }
        });
    }
    
    @Override
    public void executeOnDiskIO(final Runnable runnable) {
        this.mDiskIO.execute(runnable);
    }
    
    @Override
    public boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
    
    @Override
    public void postToMainThread(final Runnable runnable) {
        if (this.mMainHandler == null) {
            synchronized (this.mLock) {
                if (this.mMainHandler == null) {
                    this.mMainHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
        this.mMainHandler.post(runnable);
    }
}
