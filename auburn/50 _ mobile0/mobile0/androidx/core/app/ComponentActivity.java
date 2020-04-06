// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.app;

import androidx.lifecycle.ReportFragment;
import android.os.Bundle;
import androidx.lifecycle.Lifecycle;
import android.view.View;
import android.view.Window$Callback;
import android.view.KeyEvent;
import androidx.lifecycle.LifecycleRegistry;
import androidx.collection.SimpleArrayMap;
import androidx.core.view.KeyEventDispatcher;
import androidx.lifecycle.LifecycleOwner;
import android.app.Activity;

public class ComponentActivity extends Activity implements LifecycleOwner, Component
{
    private SimpleArrayMap<Class<? extends ExtraData>, ExtraData> mExtraDataMap;
    private LifecycleRegistry mLifecycleRegistry;
    
    public ComponentActivity() {
        this.mExtraDataMap = new SimpleArrayMap<Class<? extends ExtraData>, ExtraData>();
        this.mLifecycleRegistry = new LifecycleRegistry(this);
    }
    
    public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
        final View decorView = this.getWindow().getDecorView();
        return (decorView != null && KeyEventDispatcher.dispatchBeforeHierarchy(decorView, keyEvent)) || KeyEventDispatcher.dispatchKeyEvent((KeyEventDispatcher.Component)this, decorView, (Window$Callback)this, keyEvent);
    }
    
    public boolean dispatchKeyShortcutEvent(final KeyEvent keyEvent) {
        final View decorView = this.getWindow().getDecorView();
        return (decorView != null && KeyEventDispatcher.dispatchBeforeHierarchy(decorView, keyEvent)) || super.dispatchKeyShortcutEvent(keyEvent);
    }
    
    public <T extends ExtraData> T getExtraData(final Class<T> clazz) {
        return (T)this.mExtraDataMap.get(clazz);
    }
    
    public Lifecycle getLifecycle() {
        return this.mLifecycleRegistry;
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        ReportFragment.injectIfNeededIn(this);
    }
    
    protected void onSaveInstanceState(final Bundle bundle) {
        this.mLifecycleRegistry.markState(Lifecycle.State.CREATED);
        super.onSaveInstanceState(bundle);
    }
    
    public void putExtraData(final ExtraData extraData) {
        this.mExtraDataMap.put(extraData.getClass(), extraData);
    }
    
    public boolean superDispatchKeyEvent(final KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent);
    }
    
    public static class ExtraData
    {
    }
}
