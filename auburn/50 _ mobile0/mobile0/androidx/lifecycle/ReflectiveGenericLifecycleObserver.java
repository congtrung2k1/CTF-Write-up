// 
// Decompiled by Procyon v0.5.36
// 

package androidx.lifecycle;

class ReflectiveGenericLifecycleObserver implements GenericLifecycleObserver
{
    private final ClassesInfoCache.CallbackInfo mInfo;
    private final Object mWrapped;
    
    ReflectiveGenericLifecycleObserver(final Object mWrapped) {
        this.mWrapped = mWrapped;
        this.mInfo = ClassesInfoCache.sInstance.getInfo(this.mWrapped.getClass());
    }
    
    @Override
    public void onStateChanged(final LifecycleOwner lifecycleOwner, final Lifecycle.Event event) {
        this.mInfo.invokeCallbacks(lifecycleOwner, event, this.mWrapped);
    }
}
