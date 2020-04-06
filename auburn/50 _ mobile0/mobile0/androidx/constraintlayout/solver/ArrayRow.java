// 
// Decompiled by Procyon v0.5.36
// 

package androidx.constraintlayout.solver;

public class ArrayRow implements Row
{
    private static final boolean DEBUG = false;
    private static final float epsilon = 0.001f;
    float constantValue;
    boolean isSimpleDefinition;
    boolean used;
    SolverVariable variable;
    public final ArrayLinkedVariables variables;
    
    public ArrayRow(final Cache cache) {
        this.variable = null;
        this.constantValue = 0.0f;
        this.used = false;
        this.isSimpleDefinition = false;
        this.variables = new ArrayLinkedVariables(this, cache);
    }
    
    public ArrayRow addError(final LinearSystem linearSystem, final int n) {
        this.variables.put(linearSystem.createErrorVariable(n, "ep"), 1.0f);
        this.variables.put(linearSystem.createErrorVariable(n, "em"), -1.0f);
        return this;
    }
    
    @Override
    public void addError(final SolverVariable solverVariable) {
        float n = 1.0f;
        if (solverVariable.strength == 1) {
            n = 1.0f;
        }
        else if (solverVariable.strength == 2) {
            n = 1000.0f;
        }
        else if (solverVariable.strength == 3) {
            n = 1000000.0f;
        }
        else if (solverVariable.strength == 4) {
            n = 1.0E9f;
        }
        else if (solverVariable.strength == 5) {
            n = 1.0E12f;
        }
        this.variables.put(solverVariable, n);
    }
    
    ArrayRow addSingleError(final SolverVariable solverVariable, final int n) {
        this.variables.put(solverVariable, (float)n);
        return this;
    }
    
    boolean chooseSubject(final LinearSystem linearSystem) {
        boolean b = false;
        final SolverVariable chooseSubject = this.variables.chooseSubject(linearSystem);
        if (chooseSubject == null) {
            b = true;
        }
        else {
            this.pivot(chooseSubject);
        }
        if (this.variables.currentSize == 0) {
            this.isSimpleDefinition = true;
        }
        return b;
    }
    
    @Override
    public void clear() {
        this.variables.clear();
        this.variable = null;
        this.constantValue = 0.0f;
    }
    
    ArrayRow createRowCentering(final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final float n2, final SolverVariable solverVariable3, final SolverVariable solverVariable4, final int n3) {
        if (solverVariable2 == solverVariable3) {
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable4, 1.0f);
            this.variables.put(solverVariable2, -2.0f);
            return this;
        }
        if (n2 == 0.5f) {
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
            this.variables.put(solverVariable3, -1.0f);
            this.variables.put(solverVariable4, 1.0f);
            if (n > 0 || n3 > 0) {
                this.constantValue = (float)(-n + n3);
            }
        }
        else if (n2 <= 0.0f) {
            this.variables.put(solverVariable, -1.0f);
            this.variables.put(solverVariable2, 1.0f);
            this.constantValue = (float)n;
        }
        else if (n2 >= 1.0f) {
            this.variables.put(solverVariable3, -1.0f);
            this.variables.put(solverVariable4, 1.0f);
            this.constantValue = (float)n3;
        }
        else {
            this.variables.put(solverVariable, (1.0f - n2) * 1.0f);
            this.variables.put(solverVariable2, (1.0f - n2) * -1.0f);
            this.variables.put(solverVariable3, -1.0f * n2);
            this.variables.put(solverVariable4, n2 * 1.0f);
            if (n > 0 || n3 > 0) {
                this.constantValue = -n * (1.0f - n2) + n3 * n2;
            }
        }
        return this;
    }
    
    ArrayRow createRowDefinition(final SolverVariable variable, final int n) {
        this.variable = variable;
        variable.computedValue = (float)n;
        this.constantValue = (float)n;
        this.isSimpleDefinition = true;
        return this;
    }
    
    ArrayRow createRowDimensionPercent(final SolverVariable solverVariable, final SolverVariable solverVariable2, final SolverVariable solverVariable3, final float n) {
        this.variables.put(solverVariable, -1.0f);
        this.variables.put(solverVariable2, 1.0f - n);
        this.variables.put(solverVariable3, n);
        return this;
    }
    
    public ArrayRow createRowDimensionRatio(final SolverVariable solverVariable, final SolverVariable solverVariable2, final SolverVariable solverVariable3, final SolverVariable solverVariable4, final float n) {
        this.variables.put(solverVariable, -1.0f);
        this.variables.put(solverVariable2, 1.0f);
        this.variables.put(solverVariable3, n);
        this.variables.put(solverVariable4, -n);
        return this;
    }
    
    public ArrayRow createRowEqualDimension(float n, final float n2, final float n3, final SolverVariable solverVariable, final int n4, final SolverVariable solverVariable2, final int n5, final SolverVariable solverVariable3, final int n6, final SolverVariable solverVariable4, final int n7) {
        if (n2 != 0.0f && n != n3) {
            n = n / n2 / (n3 / n2);
            this.constantValue = -n4 - n5 + n6 * n + n7 * n;
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
            this.variables.put(solverVariable4, n);
            this.variables.put(solverVariable3, -n);
        }
        else {
            this.constantValue = (float)(-n4 - n5 + n6 + n7);
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
            this.variables.put(solverVariable4, 1.0f);
            this.variables.put(solverVariable3, -1.0f);
        }
        return this;
    }
    
    public ArrayRow createRowEqualMatchDimensions(float n, final float n2, final float n3, final SolverVariable solverVariable, final SolverVariable solverVariable2, final SolverVariable solverVariable3, final SolverVariable solverVariable4) {
        this.constantValue = 0.0f;
        if (n2 != 0.0f && n != n3) {
            if (n == 0.0f) {
                this.variables.put(solverVariable, 1.0f);
                this.variables.put(solverVariable2, -1.0f);
            }
            else if (n3 == 0.0f) {
                this.variables.put(solverVariable3, 1.0f);
                this.variables.put(solverVariable4, -1.0f);
            }
            else {
                n = n / n2 / (n3 / n2);
                this.variables.put(solverVariable, 1.0f);
                this.variables.put(solverVariable2, -1.0f);
                this.variables.put(solverVariable4, n);
                this.variables.put(solverVariable3, -n);
            }
        }
        else {
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
            this.variables.put(solverVariable4, 1.0f);
            this.variables.put(solverVariable3, -1.0f);
        }
        return this;
    }
    
    public ArrayRow createRowEquals(final SolverVariable solverVariable, final int n) {
        if (n < 0) {
            this.constantValue = (float)(n * -1);
            this.variables.put(solverVariable, 1.0f);
        }
        else {
            this.constantValue = (float)n;
            this.variables.put(solverVariable, -1.0f);
        }
        return this;
    }
    
    public ArrayRow createRowEquals(final SolverVariable solverVariable, final SolverVariable solverVariable2, int n) {
        int n2 = 0;
        final int n3 = 0;
        if (n != 0) {
            final int n4 = n;
            n = n3;
            int n5;
            if ((n5 = n4) < 0) {
                n5 = n4 * -1;
                n = 1;
            }
            this.constantValue = (float)n5;
            n2 = n;
        }
        if (n2 == 0) {
            this.variables.put(solverVariable, -1.0f);
            this.variables.put(solverVariable2, 1.0f);
        }
        else {
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
        }
        return this;
    }
    
    public ArrayRow createRowGreaterThan(final SolverVariable solverVariable, final int n, final SolverVariable solverVariable2) {
        this.constantValue = (float)n;
        this.variables.put(solverVariable, -1.0f);
        return this;
    }
    
    public ArrayRow createRowGreaterThan(final SolverVariable solverVariable, final SolverVariable solverVariable2, final SolverVariable solverVariable3, int n) {
        int n2 = 0;
        final int n3 = 0;
        if (n != 0) {
            final int n4 = n;
            n = n3;
            int n5;
            if ((n5 = n4) < 0) {
                n5 = n4 * -1;
                n = 1;
            }
            this.constantValue = (float)n5;
            n2 = n;
        }
        if (n2 == 0) {
            this.variables.put(solverVariable, -1.0f);
            this.variables.put(solverVariable2, 1.0f);
            this.variables.put(solverVariable3, 1.0f);
        }
        else {
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
            this.variables.put(solverVariable3, -1.0f);
        }
        return this;
    }
    
    public ArrayRow createRowLowerThan(final SolverVariable solverVariable, final SolverVariable solverVariable2, final SolverVariable solverVariable3, int n) {
        int n2 = 0;
        final int n3 = 0;
        if (n != 0) {
            final int n4 = n;
            n = n3;
            int n5;
            if ((n5 = n4) < 0) {
                n5 = n4 * -1;
                n = 1;
            }
            this.constantValue = (float)n5;
            n2 = n;
        }
        if (n2 == 0) {
            this.variables.put(solverVariable, -1.0f);
            this.variables.put(solverVariable2, 1.0f);
            this.variables.put(solverVariable3, -1.0f);
        }
        else {
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
            this.variables.put(solverVariable3, 1.0f);
        }
        return this;
    }
    
    public ArrayRow createRowWithAngle(final SolverVariable solverVariable, final SolverVariable solverVariable2, final SolverVariable solverVariable3, final SolverVariable solverVariable4, final float n) {
        this.variables.put(solverVariable3, 0.5f);
        this.variables.put(solverVariable4, 0.5f);
        this.variables.put(solverVariable, -0.5f);
        this.variables.put(solverVariable2, -0.5f);
        this.constantValue = -n;
        return this;
    }
    
    void ensurePositiveConstant() {
        final float constantValue = this.constantValue;
        if (constantValue < 0.0f) {
            this.constantValue = constantValue * -1.0f;
            this.variables.invert();
        }
    }
    
    @Override
    public SolverVariable getKey() {
        return this.variable;
    }
    
    @Override
    public SolverVariable getPivotCandidate(final LinearSystem linearSystem, final boolean[] array) {
        return this.variables.getPivotCandidate(array, null);
    }
    
    boolean hasKeyVariable() {
        final SolverVariable variable = this.variable;
        return variable != null && (variable.mType == SolverVariable.Type.UNRESTRICTED || this.constantValue >= 0.0f);
    }
    
    boolean hasVariable(final SolverVariable solverVariable) {
        return this.variables.containsKey(solverVariable);
    }
    
    @Override
    public void initFromRow(final Row row) {
        if (row instanceof ArrayRow) {
            final ArrayRow arrayRow = (ArrayRow)row;
            this.variable = null;
            this.variables.clear();
            for (int i = 0; i < arrayRow.variables.currentSize; ++i) {
                this.variables.add(arrayRow.variables.getVariable(i), arrayRow.variables.getVariableValue(i), true);
            }
        }
    }
    
    @Override
    public boolean isEmpty() {
        return this.variable == null && this.constantValue == 0.0f && this.variables.currentSize == 0;
    }
    
    SolverVariable pickPivot(final SolverVariable solverVariable) {
        return this.variables.getPivotCandidate(null, solverVariable);
    }
    
    void pivot(final SolverVariable variable) {
        final SolverVariable variable2 = this.variable;
        if (variable2 != null) {
            this.variables.put(variable2, -1.0f);
            this.variable = null;
        }
        final float n = this.variables.remove(variable, true) * -1.0f;
        this.variable = variable;
        if (n == 1.0f) {
            return;
        }
        this.constantValue /= n;
        this.variables.divideByAmount(n);
    }
    
    public void reset() {
        this.variable = null;
        this.variables.clear();
        this.constantValue = 0.0f;
        this.isSimpleDefinition = false;
    }
    
    int sizeInBytes() {
        int n = 0;
        if (this.variable != null) {
            n = 0 + 4;
        }
        return n + 4 + 4 + this.variables.sizeInBytes();
    }
    
    String toReadableString() {
        String str;
        if (this.variable == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append("0");
            str = sb.toString();
        }
        else {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            sb2.append(this.variable);
            str = sb2.toString();
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(str);
        sb3.append(" = ");
        final String string = sb3.toString();
        int n = 0;
        String s = string;
        if (this.constantValue != 0.0f) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(string);
            sb4.append(this.constantValue);
            s = sb4.toString();
            n = 1;
        }
        for (int currentSize = this.variables.currentSize, i = 0; i < currentSize; ++i) {
            final SolverVariable variable = this.variables.getVariable(i);
            if (variable != null) {
                final float variableValue = this.variables.getVariableValue(i);
                if (variableValue != 0.0f) {
                    final String string2 = variable.toString();
                    String s2;
                    float f;
                    if (n == 0) {
                        s2 = s;
                        f = variableValue;
                        if (variableValue < 0.0f) {
                            final StringBuilder sb5 = new StringBuilder();
                            sb5.append(s);
                            sb5.append("- ");
                            s2 = sb5.toString();
                            f = variableValue * -1.0f;
                        }
                    }
                    else if (variableValue > 0.0f) {
                        final StringBuilder sb6 = new StringBuilder();
                        sb6.append(s);
                        sb6.append(" + ");
                        s2 = sb6.toString();
                        f = variableValue;
                    }
                    else {
                        final StringBuilder sb7 = new StringBuilder();
                        sb7.append(s);
                        sb7.append(" - ");
                        s2 = sb7.toString();
                        f = variableValue * -1.0f;
                    }
                    if (f == 1.0f) {
                        final StringBuilder sb8 = new StringBuilder();
                        sb8.append(s2);
                        sb8.append(string2);
                        s = sb8.toString();
                    }
                    else {
                        final StringBuilder sb9 = new StringBuilder();
                        sb9.append(s2);
                        sb9.append(f);
                        sb9.append(" ");
                        sb9.append(string2);
                        s = sb9.toString();
                    }
                    n = 1;
                }
            }
        }
        String string3 = s;
        if (n == 0) {
            final StringBuilder sb10 = new StringBuilder();
            sb10.append(s);
            sb10.append("0.0");
            string3 = sb10.toString();
        }
        return string3;
    }
    
    @Override
    public String toString() {
        return this.toReadableString();
    }
}
