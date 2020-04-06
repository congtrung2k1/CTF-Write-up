// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.os;

import android.os.Build$VERSION;

public final class CancellationSignal
{
    private boolean mCancelInProgress;
    private Object mCancellationSignalObj;
    private boolean mIsCanceled;
    private OnCancelListener mOnCancelListener;
    
    private void waitForCancelFinishedLocked() {
    Label_0011_Outer:
        while (this.mCancelInProgress) {
            while (true) {
                try {
                    this.wait();
                    continue Label_0011_Outer;
                }
                catch (InterruptedException ex) {
                    continue;
                }
                break;
            }
            break;
        }
    }
    
    public void cancel() {
        // monitorenter(this)
        try {
            if (this.mIsCanceled) {
                // monitorexit(this)
                return;
            }
            this.mIsCanceled = true;
            this.mCancelInProgress = true;
            final OnCancelListener mOnCancelListener = this.mOnCancelListener;
            try {
                final Object mCancellationSignalObj = this.mCancellationSignalObj;
                try {
                    // monitorexit(this)
                    Label_0051: {
                        if (mOnCancelListener == null) {
                            break Label_0051;
                        }
                        try {
                            mOnCancelListener.onCancel();
                            break Label_0051;
                        }
                        finally {
                            synchronized (this) {
                                this.mCancelInProgress = false;
                                this.notifyAll();
                            }
                            while (true) {
                                ((android.os.CancellationSignal)mCancellationSignalObj).cancel();
                                break Label_0051;
                                continue;
                            }
                        }
                        // iftrue(Label_0093:, mCancellationSignalObj == null || Build$VERSION.SDK_INT < 16)
                    }
                    synchronized (this) {
                        this.mCancelInProgress = false;
                        this.notifyAll();
                        return;
                    }
                }
                finally {}
            }
            finally {}
        }
        finally {}
        while (true) {
            try {
                // monitorexit(this)
                throw;
            }
            finally {
                continue;
            }
            break;
        }
    }
    
    public Object getCancellationSignalObject() {
        if (Build$VERSION.SDK_INT < 16) {
            return null;
        }
        synchronized (this) {
            if (this.mCancellationSignalObj == null) {
                final android.os.CancellationSignal mCancellationSignalObj = new android.os.CancellationSignal();
                this.mCancellationSignalObj = mCancellationSignalObj;
                if (this.mIsCanceled) {
                    mCancellationSignalObj.cancel();
                }
            }
            return this.mCancellationSignalObj;
        }
    }
    
    public boolean isCanceled() {
        synchronized (this) {
            return this.mIsCanceled;
        }
    }
    
    public void setOnCancelListener(final OnCancelListener mOnCancelListener) {
        synchronized (this) {
            this.waitForCancelFinishedLocked();
            if (this.mOnCancelListener == mOnCancelListener) {
                return;
            }
            this.mOnCancelListener = mOnCancelListener;
            if (this.mIsCanceled && mOnCancelListener != null) {
                // monitorexit(this)
                mOnCancelListener.onCancel();
            }
        }
    }
    
    public void throwIfCanceled() {
        if (!this.isCanceled()) {
            return;
        }
        throw new OperationCanceledException();
    }
    
    public interface OnCancelListener
    {
        void onCancel();
    }
}
