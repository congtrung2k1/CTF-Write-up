// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.view.menu;

class BaseWrapper<T>
{
    final T mWrappedObject;
    
    BaseWrapper(final T mWrappedObject) {
        if (mWrappedObject != null) {
            this.mWrappedObject = mWrappedObject;
            return;
        }
        throw new IllegalArgumentException("Wrapped Object can not be null.");
    }
    
    public T getWrappedObject() {
        return this.mWrappedObject;
    }
}
