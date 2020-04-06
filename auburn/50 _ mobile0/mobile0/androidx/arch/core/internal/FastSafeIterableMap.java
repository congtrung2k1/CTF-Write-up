// 
// Decompiled by Procyon v0.5.36
// 

package androidx.arch.core.internal;

import java.util.Map;
import java.util.HashMap;

public class FastSafeIterableMap<K, V> extends SafeIterableMap<K, V>
{
    private HashMap<K, Entry<K, V>> mHashMap;
    
    public FastSafeIterableMap() {
        this.mHashMap = new HashMap<K, Entry<K, V>>();
    }
    
    public Map.Entry<K, V> ceil(final K key) {
        if (this.contains(key)) {
            return this.mHashMap.get(key).mPrevious;
        }
        return null;
    }
    
    public boolean contains(final K key) {
        return this.mHashMap.containsKey(key);
    }
    
    @Override
    protected Entry<K, V> get(final K key) {
        return this.mHashMap.get(key);
    }
    
    @Override
    public V putIfAbsent(final K key, final V v) {
        final Entry<K, V> value = this.get(key);
        if (value != null) {
            return value.mValue;
        }
        this.mHashMap.put(key, this.put(key, v));
        return null;
    }
    
    @Override
    public V remove(final K key) {
        final V remove = super.remove(key);
        this.mHashMap.remove(key);
        return remove;
    }
}
