// 
// Decompiled by Procyon v0.5.36
// 

package androidx.arch.core.executor;

public abstract class TaskExecutor
{
    public abstract void executeOnDiskIO(final Runnable p0);
    
    public void executeOnMainThread(final Runnable runnable) {
        if (this.isMainThread()) {
            runnable.run();
        }
        else {
            this.postToMainThread(runnable);
        }
    }
    
    public abstract boolean isMainThread();
    
    public abstract void postToMainThread(final Runnable p0);
}
