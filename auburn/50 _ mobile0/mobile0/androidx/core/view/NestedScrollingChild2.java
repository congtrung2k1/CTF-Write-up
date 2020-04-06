// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.view;

public interface NestedScrollingChild2 extends NestedScrollingChild
{
    boolean dispatchNestedPreScroll(final int p0, final int p1, final int[] p2, final int[] p3, final int p4);
    
    boolean dispatchNestedScroll(final int p0, final int p1, final int p2, final int p3, final int[] p4, final int p5);
    
    boolean hasNestedScrollingParent(final int p0);
    
    boolean startNestedScroll(final int p0, final int p1);
    
    void stopNestedScroll(final int p0);
}
