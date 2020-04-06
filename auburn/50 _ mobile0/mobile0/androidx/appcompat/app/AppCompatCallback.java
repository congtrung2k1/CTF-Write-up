// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.app;

import androidx.appcompat.view.ActionMode;

public interface AppCompatCallback
{
    void onSupportActionModeFinished(final ActionMode p0);
    
    void onSupportActionModeStarted(final ActionMode p0);
    
    ActionMode onWindowStartingSupportActionMode(final ActionMode.Callback p0);
}
