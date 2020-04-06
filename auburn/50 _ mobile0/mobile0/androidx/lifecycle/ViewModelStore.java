// 
// Decompiled by Procyon v0.5.36
// 

package androidx.lifecycle;

import java.util.Iterator;
import java.util.HashMap;

public class ViewModelStore
{
    private final HashMap<String, ViewModel> mMap;
    
    public ViewModelStore() {
        this.mMap = new HashMap<String, ViewModel>();
    }
    
    public final void clear() {
        final Iterator<ViewModel> iterator = this.mMap.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().onCleared();
        }
        this.mMap.clear();
    }
    
    final ViewModel get(final String key) {
        return this.mMap.get(key);
    }
    
    final void put(final String key, final ViewModel value) {
        final ViewModel viewModel = this.mMap.put(key, value);
        if (viewModel != null) {
            viewModel.onCleared();
        }
    }
}
