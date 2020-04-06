// 
// Decompiled by Procyon v0.5.36
// 

package androidx.loader.app;

import android.os.Bundle;
import androidx.loader.content.Loader;
import java.io.PrintWriter;
import java.io.FileDescriptor;
import androidx.lifecycle.ViewModelStoreOwner;

public abstract class LoaderManager
{
    public static void enableDebugLogging(final boolean debug) {
        LoaderManagerImpl.DEBUG = debug;
    }
    
    public static <T extends LifecycleOwner> LoaderManager getInstance(final T t) {
        return new LoaderManagerImpl((LifecycleOwner)t, ((ViewModelStoreOwner)t).getViewModelStore());
    }
    
    public abstract void destroyLoader(final int p0);
    
    @Deprecated
    public abstract void dump(final String p0, final FileDescriptor p1, final PrintWriter p2, final String[] p3);
    
    public abstract <D> Loader<D> getLoader(final int p0);
    
    public boolean hasRunningLoaders() {
        return false;
    }
    
    public abstract <D> Loader<D> initLoader(final int p0, final Bundle p1, final LoaderCallbacks<D> p2);
    
    public abstract void markForRedelivery();
    
    public abstract <D> Loader<D> restartLoader(final int p0, final Bundle p1, final LoaderCallbacks<D> p2);
    
    public interface LoaderCallbacks<D>
    {
        Loader<D> onCreateLoader(final int p0, final Bundle p1);
        
        void onLoadFinished(final Loader<D> p0, final D p1);
        
        void onLoaderReset(final Loader<D> p0);
    }
}
