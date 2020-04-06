// 
// Decompiled by Procyon v0.5.36
// 

package androidx.lifecycle;

import android.util.Log;
import androidx.arch.core.internal.SafeIterableMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;
import androidx.arch.core.internal.FastSafeIterableMap;
import java.lang.ref.WeakReference;

public class LifecycleRegistry extends Lifecycle
{
    private static final String LOG_TAG = "LifecycleRegistry";
    private int mAddingObserverCounter;
    private boolean mHandlingEvent;
    private final WeakReference<LifecycleOwner> mLifecycleOwner;
    private boolean mNewEventOccurred;
    private FastSafeIterableMap<LifecycleObserver, ObserverWithState> mObserverMap;
    private ArrayList<State> mParentStates;
    private State mState;
    
    public LifecycleRegistry(final LifecycleOwner referent) {
        this.mObserverMap = new FastSafeIterableMap<LifecycleObserver, ObserverWithState>();
        this.mAddingObserverCounter = 0;
        this.mHandlingEvent = false;
        this.mNewEventOccurred = false;
        this.mParentStates = new ArrayList<State>();
        this.mLifecycleOwner = new WeakReference<LifecycleOwner>(referent);
        this.mState = State.INITIALIZED;
    }
    
    private void backwardPass(final LifecycleOwner lifecycleOwner) {
        final Iterator<Map.Entry<LifecycleObserver, ObserverWithState>> descendingIterator = (Iterator<Map.Entry<LifecycleObserver, ObserverWithState>>)this.mObserverMap.descendingIterator();
        while (descendingIterator.hasNext() && !this.mNewEventOccurred) {
            final Map.Entry<LifecycleObserver, ObserverWithState> entry = descendingIterator.next();
            final ObserverWithState observerWithState = entry.getValue();
            while (observerWithState.mState.compareTo(this.mState) > 0 && !this.mNewEventOccurred && this.mObserverMap.contains(entry.getKey())) {
                final Event downEvent = downEvent(observerWithState.mState);
                this.pushParentState(getStateAfter(downEvent));
                observerWithState.dispatchEvent(lifecycleOwner, downEvent);
                this.popParentState();
            }
        }
    }
    
    private State calculateTargetState(final LifecycleObserver lifecycleObserver) {
        final Map.Entry<LifecycleObserver, ObserverWithState> ceil = this.mObserverMap.ceil(lifecycleObserver);
        State state = null;
        State mState;
        if (ceil != null) {
            mState = ceil.getValue().mState;
        }
        else {
            mState = null;
        }
        if (!this.mParentStates.isEmpty()) {
            final ArrayList<State> mParentStates = this.mParentStates;
            state = mParentStates.get(mParentStates.size() - 1);
        }
        return min(min(this.mState, mState), state);
    }
    
    private static Event downEvent(final State obj) {
        final int n = LifecycleRegistry$1.$SwitchMap$androidx$lifecycle$Lifecycle$State[obj.ordinal()];
        if (n == 1) {
            throw new IllegalArgumentException();
        }
        if (n == 2) {
            return Event.ON_DESTROY;
        }
        if (n == 3) {
            return Event.ON_STOP;
        }
        if (n == 4) {
            return Event.ON_PAUSE;
        }
        if (n != 5) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unexpected state value ");
            sb.append(obj);
            throw new IllegalArgumentException(sb.toString());
        }
        throw new IllegalArgumentException();
    }
    
    private void forwardPass(final LifecycleOwner lifecycleOwner) {
        final SafeIterableMap.IteratorWithAdditions iteratorWithAdditions = this.mObserverMap.iteratorWithAdditions();
        while (iteratorWithAdditions.hasNext() && !this.mNewEventOccurred) {
            final Map.Entry<K, ObserverWithState> entry = ((Iterator<Map.Entry<K, ObserverWithState>>)iteratorWithAdditions).next();
            final ObserverWithState observerWithState = entry.getValue();
            while (observerWithState.mState.compareTo(this.mState) < 0 && !this.mNewEventOccurred && this.mObserverMap.contains((LifecycleObserver)entry.getKey())) {
                this.pushParentState(observerWithState.mState);
                observerWithState.dispatchEvent(lifecycleOwner, upEvent(observerWithState.mState));
                this.popParentState();
            }
        }
    }
    
    static State getStateAfter(final Event obj) {
        switch (LifecycleRegistry$1.$SwitchMap$androidx$lifecycle$Lifecycle$Event[obj.ordinal()]) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unexpected event value ");
                sb.append(obj);
                throw new IllegalArgumentException(sb.toString());
            }
            case 6: {
                return State.DESTROYED;
            }
            case 5: {
                return State.RESUMED;
            }
            case 3:
            case 4: {
                return State.STARTED;
            }
            case 1:
            case 2: {
                return State.CREATED;
            }
        }
    }
    
    private boolean isSynced() {
        final int size = this.mObserverMap.size();
        boolean b = true;
        if (size == 0) {
            return true;
        }
        final State mState = this.mObserverMap.eldest().getValue().mState;
        final State mState2 = this.mObserverMap.newest().getValue().mState;
        if (mState != mState2 || this.mState != mState2) {
            b = false;
        }
        return b;
    }
    
    static State min(final State o, State state) {
        if (state == null || state.compareTo(o) >= 0) {
            state = o;
        }
        return state;
    }
    
    private void moveToState(final State mState) {
        if (this.mState == mState) {
            return;
        }
        this.mState = mState;
        if (!this.mHandlingEvent && this.mAddingObserverCounter == 0) {
            this.mHandlingEvent = true;
            this.sync();
            this.mHandlingEvent = false;
            return;
        }
        this.mNewEventOccurred = true;
    }
    
    private void popParentState() {
        final ArrayList<State> mParentStates = this.mParentStates;
        mParentStates.remove(mParentStates.size() - 1);
    }
    
    private void pushParentState(final State e) {
        this.mParentStates.add(e);
    }
    
    private void sync() {
        final LifecycleOwner lifecycleOwner = this.mLifecycleOwner.get();
        if (lifecycleOwner == null) {
            Log.w("LifecycleRegistry", "LifecycleOwner is garbage collected, you shouldn't try dispatch new events from it.");
            return;
        }
        while (!this.isSynced()) {
            this.mNewEventOccurred = false;
            if (this.mState.compareTo(this.mObserverMap.eldest().getValue().mState) < 0) {
                this.backwardPass(lifecycleOwner);
            }
            final Map.Entry<LifecycleObserver, ObserverWithState> newest = this.mObserverMap.newest();
            if (!this.mNewEventOccurred && newest != null && this.mState.compareTo(newest.getValue().mState) > 0) {
                this.forwardPass(lifecycleOwner);
            }
        }
        this.mNewEventOccurred = false;
    }
    
    private static Event upEvent(final State obj) {
        final int n = LifecycleRegistry$1.$SwitchMap$androidx$lifecycle$Lifecycle$State[obj.ordinal()];
        if (n != 1) {
            if (n == 2) {
                return Event.ON_START;
            }
            if (n == 3) {
                return Event.ON_RESUME;
            }
            if (n == 4) {
                throw new IllegalArgumentException();
            }
            if (n != 5) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unexpected state value ");
                sb.append(obj);
                throw new IllegalArgumentException(sb.toString());
            }
        }
        return Event.ON_CREATE;
    }
    
    @Override
    public void addObserver(final LifecycleObserver lifecycleObserver) {
        State state;
        if (this.mState == State.DESTROYED) {
            state = State.DESTROYED;
        }
        else {
            state = State.INITIALIZED;
        }
        final ObserverWithState observerWithState = new ObserverWithState(lifecycleObserver, state);
        if (this.mObserverMap.putIfAbsent(lifecycleObserver, observerWithState) != null) {
            return;
        }
        final LifecycleOwner lifecycleOwner = this.mLifecycleOwner.get();
        if (lifecycleOwner == null) {
            return;
        }
        final boolean b = this.mAddingObserverCounter != 0 || this.mHandlingEvent;
        State o = this.calculateTargetState(lifecycleObserver);
        ++this.mAddingObserverCounter;
        while (observerWithState.mState.compareTo(o) < 0 && this.mObserverMap.contains(lifecycleObserver)) {
            this.pushParentState(observerWithState.mState);
            observerWithState.dispatchEvent(lifecycleOwner, upEvent(observerWithState.mState));
            this.popParentState();
            o = this.calculateTargetState(lifecycleObserver);
        }
        if (!b) {
            this.sync();
        }
        --this.mAddingObserverCounter;
    }
    
    @Override
    public State getCurrentState() {
        return this.mState;
    }
    
    public int getObserverCount() {
        return this.mObserverMap.size();
    }
    
    public void handleLifecycleEvent(final Event event) {
        this.moveToState(getStateAfter(event));
    }
    
    public void markState(final State state) {
        this.moveToState(state);
    }
    
    @Override
    public void removeObserver(final LifecycleObserver lifecycleObserver) {
        this.mObserverMap.remove(lifecycleObserver);
    }
    
    static class ObserverWithState
    {
        GenericLifecycleObserver mLifecycleObserver;
        State mState;
        
        ObserverWithState(final LifecycleObserver lifecycleObserver, final State mState) {
            this.mLifecycleObserver = Lifecycling.getCallback(lifecycleObserver);
            this.mState = mState;
        }
        
        void dispatchEvent(final LifecycleOwner lifecycleOwner, final Event event) {
            final State stateAfter = LifecycleRegistry.getStateAfter(event);
            this.mState = LifecycleRegistry.min(this.mState, stateAfter);
            this.mLifecycleObserver.onStateChanged(lifecycleOwner, event);
            this.mState = stateAfter;
        }
    }
}
