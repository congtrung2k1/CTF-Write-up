// 
// Decompiled by Procyon v0.5.36
// 

package androidx.lifecycle;

public abstract class Lifecycle
{
    public abstract void addObserver(final LifecycleObserver p0);
    
    public abstract State getCurrentState();
    
    public abstract void removeObserver(final LifecycleObserver p0);
    
    public enum Event
    {
        ON_ANY, 
        ON_CREATE, 
        ON_DESTROY, 
        ON_PAUSE, 
        ON_RESUME, 
        ON_START, 
        ON_STOP;
    }
    
    public enum State
    {
        CREATED, 
        DESTROYED, 
        INITIALIZED, 
        RESUMED, 
        STARTED;
        
        public boolean isAtLeast(final State o) {
            return this.compareTo(o) >= 0;
        }
    }
}
