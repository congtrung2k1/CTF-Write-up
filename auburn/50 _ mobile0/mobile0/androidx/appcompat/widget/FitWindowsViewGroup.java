// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.widget;

import android.graphics.Rect;

public interface FitWindowsViewGroup
{
    void setOnFitSystemWindowsListener(final OnFitSystemWindowsListener p0);
    
    public interface OnFitSystemWindowsListener
    {
        void onFitSystemWindows(final Rect p0);
    }
}
