// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.view;

import android.view.View;

public interface NestedScrollingParent2 extends NestedScrollingParent
{
    void onNestedPreScroll(final View p0, final int p1, final int p2, final int[] p3, final int p4);
    
    void onNestedScroll(final View p0, final int p1, final int p2, final int p3, final int p4, final int p5);
    
    void onNestedScrollAccepted(final View p0, final View p1, final int p2, final int p3);
    
    boolean onStartNestedScroll(final View p0, final View p1, final int p2, final int p3);
    
    void onStopNestedScroll(final View p0, final int p1);
}
