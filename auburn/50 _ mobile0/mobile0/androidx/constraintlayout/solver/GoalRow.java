// 
// Decompiled by Procyon v0.5.36
// 

package androidx.constraintlayout.solver;

public class GoalRow extends ArrayRow
{
    public GoalRow(final Cache cache) {
        super(cache);
    }
    
    @Override
    public void addError(final SolverVariable solverVariable) {
        super.addError(solverVariable);
        --solverVariable.usageInRowCount;
    }
}
