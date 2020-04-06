// 
// Decompiled by Procyon v0.5.36
// 

package androidx.lifecycle;

class FullLifecycleObserverAdapter implements GenericLifecycleObserver
{
    private final FullLifecycleObserver mObserver;
    
    FullLifecycleObserverAdapter(final FullLifecycleObserver mObserver) {
        this.mObserver = mObserver;
    }
    
    @Override
    public void onStateChanged(final LifecycleOwner lifecycleOwner, final Lifecycle.Event event) {
        switch (FullLifecycleObserverAdapter$1.$SwitchMap$androidx$lifecycle$Lifecycle$Event[event.ordinal()]) {
            case 7: {
                throw new IllegalArgumentException("ON_ANY must not been send by anybody");
            }
            case 6: {
                this.mObserver.onDestroy(lifecycleOwner);
                break;
            }
            case 5: {
                this.mObserver.onStop(lifecycleOwner);
                break;
            }
            case 4: {
                this.mObserver.onPause(lifecycleOwner);
                break;
            }
            case 3: {
                this.mObserver.onResume(lifecycleOwner);
                break;
            }
            case 2: {
                this.mObserver.onStart(lifecycleOwner);
                break;
            }
            case 1: {
                this.mObserver.onCreate(lifecycleOwner);
                break;
            }
        }
    }
}
