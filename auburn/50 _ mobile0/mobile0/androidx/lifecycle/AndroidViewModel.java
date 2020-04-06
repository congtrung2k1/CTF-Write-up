// 
// Decompiled by Procyon v0.5.36
// 

package androidx.lifecycle;

import android.app.Application;

public class AndroidViewModel extends ViewModel
{
    private Application mApplication;
    
    public AndroidViewModel(final Application mApplication) {
        this.mApplication = mApplication;
    }
    
    public <T extends Application> T getApplication() {
        return (T)this.mApplication;
    }
}
