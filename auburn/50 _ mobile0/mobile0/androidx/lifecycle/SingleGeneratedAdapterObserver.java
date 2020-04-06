// 
// Decompiled by Procyon v0.5.36
// 

package androidx.lifecycle;

public class SingleGeneratedAdapterObserver implements GenericLifecycleObserver
{
    private final GeneratedAdapter mGeneratedAdapter;
    
    SingleGeneratedAdapterObserver(final GeneratedAdapter mGeneratedAdapter) {
        this.mGeneratedAdapter = mGeneratedAdapter;
    }
    
    @Override
    public void onStateChanged(final LifecycleOwner lifecycleOwner, final Lifecycle.Event event) {
        this.mGeneratedAdapter.callMethods(lifecycleOwner, event, false, null);
        this.mGeneratedAdapter.callMethods(lifecycleOwner, event, true, null);
    }
}
