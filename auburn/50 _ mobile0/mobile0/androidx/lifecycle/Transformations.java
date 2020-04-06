// 
// Decompiled by Procyon v0.5.36
// 

package androidx.lifecycle;

import androidx.arch.core.util.Function;

public class Transformations
{
    private Transformations() {
    }
    
    public static <X, Y> LiveData<Y> map(final LiveData<X> liveData, final Function<X, Y> function) {
        final MediatorLiveData<Y> mediatorLiveData = new MediatorLiveData<Y>();
        mediatorLiveData.addSource(liveData, new Observer<X>() {
            @Override
            public void onChanged(final X x) {
                mediatorLiveData.setValue(function.apply(x));
            }
        });
        return mediatorLiveData;
    }
    
    public static <X, Y> LiveData<Y> switchMap(final LiveData<X> liveData, final Function<X, LiveData<Y>> function) {
        final MediatorLiveData<Y> mediatorLiveData = new MediatorLiveData<Y>();
        mediatorLiveData.addSource(liveData, new Observer<X>() {
            LiveData<Y> mSource;
            
            @Override
            public void onChanged(final X x) {
                final LiveData<Y> mSource = function.apply(x);
                final LiveData<Y> mSource2 = this.mSource;
                if (mSource2 == mSource) {
                    return;
                }
                if (mSource2 != null) {
                    mediatorLiveData.removeSource(mSource2);
                }
                if ((this.mSource = mSource) != null) {
                    mediatorLiveData.addSource(mSource, new Observer<Y>() {
                        @Override
                        public void onChanged(final Y value) {
                            mediatorLiveData.setValue(value);
                        }
                    });
                }
            }
        });
        return mediatorLiveData;
    }
}
