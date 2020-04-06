// 
// Decompiled by Procyon v0.5.36
// 

package androidx.lifecycle;

public class CompositeGeneratedAdaptersObserver implements GenericLifecycleObserver
{
    private final GeneratedAdapter[] mGeneratedAdapters;
    
    CompositeGeneratedAdaptersObserver(final GeneratedAdapter[] mGeneratedAdapters) {
        this.mGeneratedAdapters = mGeneratedAdapters;
    }
    
    @Override
    public void onStateChanged(final LifecycleOwner lifecycleOwner, final Lifecycle.Event event) {
        final MethodCallsLogger methodCallsLogger = new MethodCallsLogger();
        final GeneratedAdapter[] mGeneratedAdapters = this.mGeneratedAdapters;
        final int length = mGeneratedAdapters.length;
        final int n = 0;
        for (int i = 0; i < length; ++i) {
            mGeneratedAdapters[i].callMethods(lifecycleOwner, event, false, methodCallsLogger);
        }
        final GeneratedAdapter[] mGeneratedAdapters2 = this.mGeneratedAdapters;
        for (int length2 = mGeneratedAdapters2.length, j = n; j < length2; ++j) {
            mGeneratedAdapters2[j].callMethods(lifecycleOwner, event, true, methodCallsLogger);
        }
    }
}
