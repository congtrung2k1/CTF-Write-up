// 
// Decompiled by Procyon v0.5.36
// 

package androidx.fragment.app;

import android.view.View;
import android.os.Bundle;
import android.content.Context;

public abstract class FragmentContainer
{
    public Fragment instantiate(final Context context, final String s, final Bundle bundle) {
        return Fragment.instantiate(context, s, bundle);
    }
    
    public abstract View onFindViewById(final int p0);
    
    public abstract boolean onHasView();
}
