// 
// Decompiled by Procyon v0.5.36
// 

package androidx.loader.content;

import android.os.Message;
import android.os.Looper;
import java.util.concurrent.TimeoutException;
import android.os.Handler;
import android.util.Log;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Callable;
import android.os.Binder;
import android.os.Process;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

abstract class ModernAsyncTask<Params, Progress, Result>
{
    private static final int CORE_POOL_SIZE = 5;
    private static final int KEEP_ALIVE = 1;
    private static final String LOG_TAG = "AsyncTask";
    private static final int MAXIMUM_POOL_SIZE = 128;
    private static final int MESSAGE_POST_PROGRESS = 2;
    private static final int MESSAGE_POST_RESULT = 1;
    public static final Executor THREAD_POOL_EXECUTOR;
    private static volatile Executor sDefaultExecutor;
    private static InternalHandler sHandler;
    private static final BlockingQueue<Runnable> sPoolWorkQueue;
    private static final ThreadFactory sThreadFactory;
    final AtomicBoolean mCancelled;
    private final FutureTask<Result> mFuture;
    private volatile Status mStatus;
    final AtomicBoolean mTaskInvoked;
    private final WorkerRunnable<Params, Result> mWorker;
    
    static {
        sThreadFactory = new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);
            
            @Override
            public Thread newThread(final Runnable target) {
                final StringBuilder sb = new StringBuilder();
                sb.append("ModernAsyncTask #");
                sb.append(this.mCount.getAndIncrement());
                return new Thread(target, sb.toString());
            }
        };
        sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(10);
        ModernAsyncTask.sDefaultExecutor = (THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(5, 128, 1L, TimeUnit.SECONDS, ModernAsyncTask.sPoolWorkQueue, ModernAsyncTask.sThreadFactory));
    }
    
    ModernAsyncTask() {
        this.mStatus = Status.PENDING;
        this.mCancelled = new AtomicBoolean();
        this.mTaskInvoked = new AtomicBoolean();
        this.mWorker = (WorkerRunnable<Params, Result>)new WorkerRunnable<Params, Result>() {
            @Override
            public Result call() throws Exception {
                ModernAsyncTask.this.mTaskInvoked.set(true);
                Result doInBackground = null;
                try {
                    Process.setThreadPriority(10);
                    doInBackground = doInBackground;
                    final Result result = doInBackground = ModernAsyncTask.this.doInBackground(this.mParams);
                    Binder.flushPendingCommands();
                    ModernAsyncTask.this.postResult(result);
                    return result;
                }
                finally {
                    try {
                        ModernAsyncTask.this.mCancelled.set(true);
                    }
                    finally {
                        ModernAsyncTask.this.postResult(doInBackground);
                    }
                }
            }
        };
        this.mFuture = new FutureTask<Result>(this.mWorker) {
            @Override
            protected void done() {
                try {
                    ModernAsyncTask.this.postResultIfNotInvoked(this.get());
                }
                catch (CancellationException ex3) {
                    ModernAsyncTask.this.postResultIfNotInvoked(null);
                }
                catch (ExecutionException ex) {
                    throw new RuntimeException("An error occurred while executing doInBackground()", ex.getCause());
                }
                catch (InterruptedException ex2) {
                    Log.w("AsyncTask", (Throwable)ex2);
                }
                finally {
                    final Throwable cause;
                    throw new RuntimeException("An error occurred while executing doInBackground()", cause);
                }
            }
        };
    }
    
    public static void execute(final Runnable runnable) {
        ModernAsyncTask.sDefaultExecutor.execute(runnable);
    }
    
    private static Handler getHandler() {
        synchronized (ModernAsyncTask.class) {
            if (ModernAsyncTask.sHandler == null) {
                ModernAsyncTask.sHandler = new InternalHandler();
            }
            return ModernAsyncTask.sHandler;
        }
    }
    
    public static void setDefaultExecutor(final Executor sDefaultExecutor) {
        ModernAsyncTask.sDefaultExecutor = sDefaultExecutor;
    }
    
    public final boolean cancel(final boolean mayInterruptIfRunning) {
        this.mCancelled.set(true);
        return this.mFuture.cancel(mayInterruptIfRunning);
    }
    
    protected abstract Result doInBackground(final Params... p0);
    
    public final ModernAsyncTask<Params, Progress, Result> execute(final Params... array) {
        return this.executeOnExecutor(ModernAsyncTask.sDefaultExecutor, array);
    }
    
    public final ModernAsyncTask<Params, Progress, Result> executeOnExecutor(final Executor executor, final Params... mParams) {
        if (this.mStatus == Status.PENDING) {
            this.mStatus = Status.RUNNING;
            this.onPreExecute();
            this.mWorker.mParams = mParams;
            executor.execute(this.mFuture);
            return this;
        }
        final int n = ModernAsyncTask$4.$SwitchMap$androidx$loader$content$ModernAsyncTask$Status[this.mStatus.ordinal()];
        if (n == 1) {
            throw new IllegalStateException("Cannot execute task: the task is already running.");
        }
        if (n != 2) {
            throw new IllegalStateException("We should never reach this state");
        }
        throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
    }
    
    void finish(final Result result) {
        if (this.isCancelled()) {
            this.onCancelled(result);
        }
        else {
            this.onPostExecute(result);
        }
        this.mStatus = Status.FINISHED;
    }
    
    public final Result get() throws InterruptedException, ExecutionException {
        return this.mFuture.get();
    }
    
    public final Result get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.mFuture.get(timeout, unit);
    }
    
    public final Status getStatus() {
        return this.mStatus;
    }
    
    public final boolean isCancelled() {
        return this.mCancelled.get();
    }
    
    protected void onCancelled() {
    }
    
    protected void onCancelled(final Result result) {
        this.onCancelled();
    }
    
    protected void onPostExecute(final Result result) {
    }
    
    protected void onPreExecute() {
    }
    
    protected void onProgressUpdate(final Progress... array) {
    }
    
    Result postResult(final Result result) {
        getHandler().obtainMessage(1, (Object)new AsyncTaskResult(this, new Object[] { result })).sendToTarget();
        return result;
    }
    
    void postResultIfNotInvoked(final Result result) {
        if (!this.mTaskInvoked.get()) {
            this.postResult(result);
        }
    }
    
    protected final void publishProgress(final Progress... array) {
        if (!this.isCancelled()) {
            getHandler().obtainMessage(2, (Object)new AsyncTaskResult(this, (Object[])array)).sendToTarget();
        }
    }
    
    private static class AsyncTaskResult<Data>
    {
        final Data[] mData;
        final ModernAsyncTask mTask;
        
        AsyncTaskResult(final ModernAsyncTask mTask, final Data... mData) {
            this.mTask = mTask;
            this.mData = mData;
        }
    }
    
    private static class InternalHandler extends Handler
    {
        InternalHandler() {
            super(Looper.getMainLooper());
        }
        
        public void handleMessage(final Message message) {
            final AsyncTaskResult asyncTaskResult = (AsyncTaskResult)message.obj;
            final int what = message.what;
            if (what != 1) {
                if (what == 2) {
                    asyncTaskResult.mTask.onProgressUpdate(asyncTaskResult.mData);
                }
            }
            else {
                asyncTaskResult.mTask.finish(asyncTaskResult.mData[0]);
            }
        }
    }
    
    public enum Status
    {
        FINISHED, 
        PENDING, 
        RUNNING;
    }
    
    private abstract static class WorkerRunnable<Params, Result> implements Callable<Result>
    {
        Params[] mParams;
        
        WorkerRunnable() {
        }
    }
}
