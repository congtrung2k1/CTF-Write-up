// 
// Decompiled by Procyon v0.5.36
// 

package androidx.constraintlayout.solver.widgets;

import java.util.Arrays;

public class Helper extends ConstraintWidget
{
    protected ConstraintWidget[] mWidgets;
    protected int mWidgetsCount;
    
    public Helper() {
        this.mWidgets = new ConstraintWidget[4];
        this.mWidgetsCount = 0;
    }
    
    public void add(final ConstraintWidget constraintWidget) {
        final int mWidgetsCount = this.mWidgetsCount;
        final ConstraintWidget[] mWidgets = this.mWidgets;
        if (mWidgetsCount + 1 > mWidgets.length) {
            this.mWidgets = Arrays.copyOf(mWidgets, mWidgets.length * 2);
        }
        final ConstraintWidget[] mWidgets2 = this.mWidgets;
        final int mWidgetsCount2 = this.mWidgetsCount;
        mWidgets2[mWidgetsCount2] = constraintWidget;
        this.mWidgetsCount = mWidgetsCount2 + 1;
    }
    
    public void removeAllIds() {
        this.mWidgetsCount = 0;
    }
}
