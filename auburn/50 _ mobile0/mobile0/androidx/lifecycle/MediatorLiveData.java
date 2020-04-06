// 
// Decompiled by Procyon v0.5.36
// 

package androidx.lifecycle;

import java.util.Iterator;
import java.util.Map;
import androidx.arch.core.internal.SafeIterableMap;

public class MediatorLiveData<T> extends MutableLiveData<T>
{
    private SafeIterableMap<LiveData<?>, Source<?>> mSources;
    
    public MediatorLiveData() {
        this.mSources = new SafeIterableMap<LiveData<?>, Source<?>>();
    }
    
    public <S> void addSource(final LiveData<S> liveData, final Observer<? super S> observer) {
        final Source<Object> source = new Source<Object>((LiveData<Object>)liveData, (Observer<? super Object>)observer);
        final Source<Object> source2 = this.mSources.putIfAbsent(liveData, source);
        if (source2 != null && source2.mObserver != observer) {
            throw new IllegalArgumentException("This source was already added with the different observer");
        }
        if (source2 != null) {
            return;
        }
        if (this.hasActiveObservers()) {
            source.plug();
        }
    }
    
    @Override
    protected void onActive() {
        final Iterator<Map.Entry<LiveData<?>, Source<?>>> iterator = this.mSources.iterator();
        while (iterator.hasNext()) {
            ((Map.Entry<K, Source>)iterator.next()).getValue().plug();
        }
    }
    
    @Override
    protected void onInactive() {
        final Iterator<Map.Entry<LiveData<?>, Source<?>>> iterator = this.mSources.iterator();
        while (iterator.hasNext()) {
            ((Map.Entry<K, Source>)iterator.next()).getValue().unplug();
        }
    }
    
    public <S> void removeSource(final LiveData<S> liveData) {
        final Source<?> source = this.mSources.remove(liveData);
        if (source != null) {
            source.unplug();
        }
    }
    
    private static class Source<V> implements Observer<V>
    {
        final LiveData<V> mLiveData;
        final Observer<? super V> mObserver;
        int mVersion;
        
        Source(final LiveData<V> mLiveData, final Observer<? super V> mObserver) {
            this.mVersion = -1;
            this.mLiveData = mLiveData;
            this.mObserver = mObserver;
        }
        
        @Override
        public void onChanged(final V v) {
            if (this.mVersion != this.mLiveData.getVersion()) {
                this.mVersion = this.mLiveData.getVersion();
                this.mObserver.onChanged((Object)v);
            }
        }
        
        void plug() {
            this.mLiveData.observeForever(this);
        }
        
        void unplug() {
            this.mLiveData.removeObserver(this);
        }
    }
}
