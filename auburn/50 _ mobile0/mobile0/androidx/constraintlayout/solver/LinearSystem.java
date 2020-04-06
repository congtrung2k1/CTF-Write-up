// 
// Decompiled by Procyon v0.5.36
// 

package androidx.constraintlayout.solver;

import java.io.PrintStream;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import java.util.Arrays;
import java.util.HashMap;

public class LinearSystem
{
    private static final boolean DEBUG = false;
    public static final boolean FULL_DEBUG = false;
    private static int POOL_SIZE;
    public static Metrics sMetrics;
    private int TABLE_SIZE;
    public boolean graphOptimizer;
    private boolean[] mAlreadyTestedCandidates;
    final Cache mCache;
    private Row mGoal;
    private int mMaxColumns;
    private int mMaxRows;
    int mNumColumns;
    int mNumRows;
    private SolverVariable[] mPoolVariables;
    private int mPoolVariablesCount;
    ArrayRow[] mRows;
    private final Row mTempGoal;
    private HashMap<String, SolverVariable> mVariables;
    int mVariablesID;
    private ArrayRow[] tempClientsCopy;
    
    static {
        LinearSystem.POOL_SIZE = 1000;
    }
    
    public LinearSystem() {
        this.mVariablesID = 0;
        this.mVariables = null;
        this.TABLE_SIZE = 32;
        this.mMaxColumns = 32;
        this.mRows = null;
        this.graphOptimizer = false;
        this.mAlreadyTestedCandidates = new boolean[32];
        this.mNumColumns = 1;
        this.mNumRows = 0;
        this.mMaxRows = 32;
        this.mPoolVariables = new SolverVariable[LinearSystem.POOL_SIZE];
        this.mPoolVariablesCount = 0;
        this.tempClientsCopy = new ArrayRow[32];
        this.mRows = new ArrayRow[32];
        this.releaseRows();
        this.mCache = new Cache();
        this.mGoal = (Row)new GoalRow(this.mCache);
        this.mTempGoal = (Row)new ArrayRow(this.mCache);
    }
    
    private SolverVariable acquireSolverVariable(final SolverVariable.Type type, final String s) {
        final SolverVariable solverVariable = this.mCache.solverVariablePool.acquire();
        SolverVariable solverVariable3;
        if (solverVariable == null) {
            final SolverVariable solverVariable2 = new SolverVariable(type, s);
            solverVariable2.setType(type, s);
            solverVariable3 = solverVariable2;
        }
        else {
            solverVariable.reset();
            solverVariable.setType(type, s);
            solverVariable3 = solverVariable;
        }
        final int mPoolVariablesCount = this.mPoolVariablesCount;
        final int pool_SIZE = LinearSystem.POOL_SIZE;
        if (mPoolVariablesCount >= pool_SIZE) {
            this.mPoolVariables = Arrays.copyOf(this.mPoolVariables, LinearSystem.POOL_SIZE = pool_SIZE * 2);
        }
        return this.mPoolVariables[this.mPoolVariablesCount++] = solverVariable3;
    }
    
    private void addError(final ArrayRow arrayRow) {
        arrayRow.addError(this, 0);
    }
    
    private final void addRow(final ArrayRow arrayRow) {
        if (this.mRows[this.mNumRows] != null) {
            this.mCache.arrayRowPool.release(this.mRows[this.mNumRows]);
        }
        this.mRows[this.mNumRows] = arrayRow;
        arrayRow.variable.definitionId = this.mNumRows;
        ++this.mNumRows;
        arrayRow.variable.updateReferencesWithNewDefinition(arrayRow);
    }
    
    private void addSingleError(final ArrayRow arrayRow, final int n) {
        this.addSingleError(arrayRow, n, 0);
    }
    
    private void computeValues() {
        for (int i = 0; i < this.mNumRows; ++i) {
            final ArrayRow arrayRow = this.mRows[i];
            arrayRow.variable.computedValue = arrayRow.constantValue;
        }
    }
    
    public static ArrayRow createRowCentering(final LinearSystem linearSystem, final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final float n2, final SolverVariable solverVariable3, final SolverVariable solverVariable4, final int n3, final boolean b) {
        final ArrayRow row = linearSystem.createRow();
        row.createRowCentering(solverVariable, solverVariable2, n, n2, solverVariable3, solverVariable4, n3);
        if (b) {
            row.addError(linearSystem, 4);
        }
        return row;
    }
    
    public static ArrayRow createRowDimensionPercent(final LinearSystem linearSystem, final SolverVariable solverVariable, final SolverVariable solverVariable2, final SolverVariable solverVariable3, final float n, final boolean b) {
        final ArrayRow row = linearSystem.createRow();
        if (b) {
            linearSystem.addError(row);
        }
        return row.createRowDimensionPercent(solverVariable, solverVariable2, solverVariable3, n);
    }
    
    public static ArrayRow createRowEquals(final LinearSystem linearSystem, final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final boolean b) {
        final ArrayRow row = linearSystem.createRow();
        row.createRowEquals(solverVariable, solverVariable2, n);
        if (b) {
            linearSystem.addSingleError(row, 1);
        }
        return row;
    }
    
    public static ArrayRow createRowGreaterThan(final LinearSystem linearSystem, final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final boolean b) {
        final SolverVariable slackVariable = linearSystem.createSlackVariable();
        final ArrayRow row = linearSystem.createRow();
        row.createRowGreaterThan(solverVariable, solverVariable2, slackVariable, n);
        if (b) {
            linearSystem.addSingleError(row, (int)(-1.0f * row.variables.get(slackVariable)));
        }
        return row;
    }
    
    public static ArrayRow createRowLowerThan(final LinearSystem linearSystem, final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final boolean b) {
        final SolverVariable slackVariable = linearSystem.createSlackVariable();
        final ArrayRow row = linearSystem.createRow();
        row.createRowLowerThan(solverVariable, solverVariable2, slackVariable, n);
        if (b) {
            linearSystem.addSingleError(row, (int)(-1.0f * row.variables.get(slackVariable)));
        }
        return row;
    }
    
    private SolverVariable createVariable(final String s, final SolverVariable.Type type) {
        final Metrics sMetrics = LinearSystem.sMetrics;
        if (sMetrics != null) {
            ++sMetrics.variables;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        final SolverVariable acquireSolverVariable = this.acquireSolverVariable(type, null);
        acquireSolverVariable.setName(s);
        final int n = this.mVariablesID + 1;
        this.mVariablesID = n;
        ++this.mNumColumns;
        acquireSolverVariable.id = n;
        if (this.mVariables == null) {
            this.mVariables = new HashMap<String, SolverVariable>();
        }
        this.mVariables.put(s, acquireSolverVariable);
        return this.mCache.mIndexedVariables[this.mVariablesID] = acquireSolverVariable;
    }
    
    private void displayRows() {
        this.displaySolverVariables();
        String string = "";
        for (int i = 0; i < this.mNumRows; ++i) {
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(this.mRows[i]);
            final String string2 = sb.toString();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(string2);
            sb2.append("\n");
            string = sb2.toString();
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(string);
        sb3.append(this.mGoal);
        sb3.append("\n");
        System.out.println(sb3.toString());
    }
    
    private void displaySolverVariables() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Display Rows (");
        sb.append(this.mNumRows);
        sb.append("x");
        sb.append(this.mNumColumns);
        sb.append(")\n");
        System.out.println(sb.toString());
    }
    
    private int enforceBFS(final Row row) throws Exception {
        final int n = 0;
        final int n2 = 0;
        int n3 = 0;
        int n4;
        while (true) {
            n4 = n2;
            if (n3 >= this.mNumRows) {
                break;
            }
            if (this.mRows[n3].variable.mType != SolverVariable.Type.UNRESTRICTED) {
                if (this.mRows[n3].constantValue < 0.0f) {
                    n4 = 1;
                    break;
                }
            }
            ++n3;
        }
        int n5 = n;
        if (n4 != 0) {
            int n6 = 0;
            int n7 = 0;
            while (true) {
                n5 = n7;
                if (n6 != 0) {
                    break;
                }
                final Metrics sMetrics = LinearSystem.sMetrics;
                if (sMetrics != null) {
                    ++sMetrics.bfs;
                }
                final int n8 = n7 + 1;
                float n9 = Float.MAX_VALUE;
                int n10 = 0;
                int definitionId = -1;
                int n11 = -1;
                float n12;
                int n13;
                int n14;
                int n15;
                for (int i = 0; i < this.mNumRows; ++i, n9 = n12, n10 = n13, definitionId = n14, n11 = n15) {
                    final ArrayRow arrayRow = this.mRows[i];
                    if (arrayRow.variable.mType == SolverVariable.Type.UNRESTRICTED) {
                        n12 = n9;
                        n13 = n10;
                        n14 = definitionId;
                        n15 = n11;
                    }
                    else if (arrayRow.isSimpleDefinition) {
                        n12 = n9;
                        n13 = n10;
                        n14 = definitionId;
                        n15 = n11;
                    }
                    else {
                        n12 = n9;
                        n13 = n10;
                        n14 = definitionId;
                        n15 = n11;
                        if (arrayRow.constantValue < 0.0f) {
                            int n16 = 1;
                            while (true) {
                                n12 = n9;
                                n13 = n10;
                                n14 = definitionId;
                                n15 = n11;
                                if (n16 >= this.mNumColumns) {
                                    break;
                                }
                                final SolverVariable solverVariable = this.mCache.mIndexedVariables[n16];
                                final float value = arrayRow.variables.get(solverVariable);
                                float n17;
                                int n18;
                                int n19;
                                int n20;
                                if (value <= 0.0f) {
                                    n17 = n9;
                                    n18 = n10;
                                    n19 = definitionId;
                                    n20 = n11;
                                }
                                else {
                                    final int n21 = 0;
                                    int n22 = n10;
                                    int n23 = n21;
                                    while (true) {
                                        n17 = n9;
                                        n18 = n22;
                                        n19 = definitionId;
                                        n20 = n11;
                                        if (n23 >= 7) {
                                            break;
                                        }
                                        final float n24 = solverVariable.strengthVector[n23] / value;
                                        int n25;
                                        if ((n24 < n9 && n23 == n22) || n23 > (n25 = n22)) {
                                            n9 = n24;
                                            definitionId = i;
                                            n11 = n16;
                                            n25 = n23;
                                        }
                                        ++n23;
                                        n22 = n25;
                                    }
                                }
                                ++n16;
                                n9 = n17;
                                n10 = n18;
                                definitionId = n19;
                                n11 = n20;
                            }
                        }
                    }
                }
                if (definitionId != -1) {
                    final ArrayRow arrayRow2 = this.mRows[definitionId];
                    arrayRow2.variable.definitionId = -1;
                    final Metrics sMetrics2 = LinearSystem.sMetrics;
                    if (sMetrics2 != null) {
                        ++sMetrics2.pivots;
                    }
                    arrayRow2.pivot(this.mCache.mIndexedVariables[n11]);
                    arrayRow2.variable.definitionId = definitionId;
                    arrayRow2.variable.updateReferencesWithNewDefinition(arrayRow2);
                }
                else {
                    n6 = 1;
                }
                if (n8 > this.mNumColumns / 2) {
                    n6 = 1;
                }
                n7 = n8;
            }
        }
        return n5;
    }
    
    private String getDisplaySize(final int n) {
        final int i = n * 4 / 1024 / 1024;
        if (i > 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(i);
            sb.append(" Mb");
            return sb.toString();
        }
        final int j = n * 4 / 1024;
        if (j > 0) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            sb2.append(j);
            sb2.append(" Kb");
            return sb2.toString();
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("");
        sb3.append(n * 4);
        sb3.append(" bytes");
        return sb3.toString();
    }
    
    private String getDisplayStrength(final int n) {
        if (n == 1) {
            return "LOW";
        }
        if (n == 2) {
            return "MEDIUM";
        }
        if (n == 3) {
            return "HIGH";
        }
        if (n == 4) {
            return "HIGHEST";
        }
        if (n == 5) {
            return "EQUALITY";
        }
        if (n == 6) {
            return "FIXED";
        }
        return "NONE";
    }
    
    public static Metrics getMetrics() {
        return LinearSystem.sMetrics;
    }
    
    private void increaseTableSize() {
        final int n = this.TABLE_SIZE * 2;
        this.TABLE_SIZE = n;
        this.mRows = Arrays.copyOf(this.mRows, n);
        final Cache mCache = this.mCache;
        mCache.mIndexedVariables = Arrays.copyOf(mCache.mIndexedVariables, this.TABLE_SIZE);
        final int table_SIZE = this.TABLE_SIZE;
        this.mAlreadyTestedCandidates = new boolean[table_SIZE];
        this.mMaxColumns = table_SIZE;
        this.mMaxRows = table_SIZE;
        final Metrics sMetrics = LinearSystem.sMetrics;
        if (sMetrics != null) {
            ++sMetrics.tableSizeIncrease;
            final Metrics sMetrics2 = LinearSystem.sMetrics;
            sMetrics2.maxTableSize = Math.max(sMetrics2.maxTableSize, this.TABLE_SIZE);
            final Metrics sMetrics3 = LinearSystem.sMetrics;
            sMetrics3.lastTableSize = sMetrics3.maxTableSize;
        }
    }
    
    private final int optimize(final Row row, final boolean b) {
        final Metrics sMetrics = LinearSystem.sMetrics;
        if (sMetrics != null) {
            ++sMetrics.optimize;
        }
        final int n = 0;
        final int n2 = 0;
        int n3 = 0;
        int i;
        int n4;
        while (true) {
            i = n;
            n4 = n2;
            if (n3 >= this.mNumColumns) {
                break;
            }
            this.mAlreadyTestedCandidates[n3] = false;
            ++n3;
        }
        while (i == 0) {
            final Metrics sMetrics2 = LinearSystem.sMetrics;
            if (sMetrics2 != null) {
                ++sMetrics2.iterations;
            }
            final int n5 = n4 + 1;
            if (n5 >= this.mNumColumns * 2) {
                return n5;
            }
            if (row.getKey() != null) {
                this.mAlreadyTestedCandidates[row.getKey().id] = true;
            }
            final SolverVariable pivotCandidate = row.getPivotCandidate(this, this.mAlreadyTestedCandidates);
            if (pivotCandidate != null) {
                if (this.mAlreadyTestedCandidates[pivotCandidate.id]) {
                    return n5;
                }
                this.mAlreadyTestedCandidates[pivotCandidate.id] = true;
            }
            if (pivotCandidate != null) {
                float n6 = Float.MAX_VALUE;
                int definitionId = -1;
                float n7;
                int n8;
                for (int j = 0; j < this.mNumRows; ++j, n6 = n7, definitionId = n8) {
                    final ArrayRow arrayRow = this.mRows[j];
                    if (arrayRow.variable.mType == SolverVariable.Type.UNRESTRICTED) {
                        n7 = n6;
                        n8 = definitionId;
                    }
                    else if (arrayRow.isSimpleDefinition) {
                        n7 = n6;
                        n8 = definitionId;
                    }
                    else {
                        n7 = n6;
                        n8 = definitionId;
                        if (arrayRow.hasVariable(pivotCandidate)) {
                            final float value = arrayRow.variables.get(pivotCandidate);
                            n7 = n6;
                            n8 = definitionId;
                            if (value < 0.0f) {
                                final float n9 = -arrayRow.constantValue / value;
                                n7 = n6;
                                n8 = definitionId;
                                if (n9 < n6) {
                                    n7 = n9;
                                    n8 = j;
                                }
                            }
                        }
                    }
                }
                if (definitionId > -1) {
                    final ArrayRow arrayRow2 = this.mRows[definitionId];
                    arrayRow2.variable.definitionId = -1;
                    final Metrics sMetrics3 = LinearSystem.sMetrics;
                    if (sMetrics3 != null) {
                        ++sMetrics3.pivots;
                    }
                    arrayRow2.pivot(pivotCandidate);
                    arrayRow2.variable.definitionId = definitionId;
                    arrayRow2.variable.updateReferencesWithNewDefinition(arrayRow2);
                }
                else {
                    i = 1;
                }
            }
            else {
                i = 1;
            }
            n4 = n5;
        }
        return n4;
    }
    
    private void releaseRows() {
        int n = 0;
        while (true) {
            final ArrayRow[] mRows = this.mRows;
            if (n >= mRows.length) {
                break;
            }
            final ArrayRow arrayRow = mRows[n];
            if (arrayRow != null) {
                this.mCache.arrayRowPool.release(arrayRow);
            }
            this.mRows[n] = null;
            ++n;
        }
    }
    
    private final void updateRowFromVariables(final ArrayRow arrayRow) {
        if (this.mNumRows > 0) {
            arrayRow.variables.updateFromSystem(arrayRow, this.mRows);
            if (arrayRow.variables.currentSize == 0) {
                arrayRow.isSimpleDefinition = true;
            }
        }
    }
    
    public void addCenterPoint(final ConstraintWidget constraintWidget, final ConstraintWidget constraintWidget2, final float n, final int n2) {
        final SolverVariable objectVariable = this.createObjectVariable(constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT));
        final SolverVariable objectVariable2 = this.createObjectVariable(constraintWidget.getAnchor(ConstraintAnchor.Type.TOP));
        final SolverVariable objectVariable3 = this.createObjectVariable(constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT));
        final SolverVariable objectVariable4 = this.createObjectVariable(constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM));
        final SolverVariable objectVariable5 = this.createObjectVariable(constraintWidget2.getAnchor(ConstraintAnchor.Type.LEFT));
        final SolverVariable objectVariable6 = this.createObjectVariable(constraintWidget2.getAnchor(ConstraintAnchor.Type.TOP));
        final SolverVariable objectVariable7 = this.createObjectVariable(constraintWidget2.getAnchor(ConstraintAnchor.Type.RIGHT));
        final SolverVariable objectVariable8 = this.createObjectVariable(constraintWidget2.getAnchor(ConstraintAnchor.Type.BOTTOM));
        final ArrayRow row = this.createRow();
        final double sin = Math.sin(n);
        final double v = n2;
        Double.isNaN(v);
        row.createRowWithAngle(objectVariable2, objectVariable4, objectVariable6, objectVariable8, (float)(sin * v));
        this.addConstraint(row);
        final ArrayRow row2 = this.createRow();
        final double cos = Math.cos(n);
        final double v2 = n2;
        Double.isNaN(v2);
        row2.createRowWithAngle(objectVariable, objectVariable3, objectVariable5, objectVariable7, (float)(cos * v2));
        this.addConstraint(row2);
    }
    
    public void addCentering(final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final float n2, final SolverVariable solverVariable3, final SolverVariable solverVariable4, final int n3, final int n4) {
        final ArrayRow row = this.createRow();
        row.createRowCentering(solverVariable, solverVariable2, n, n2, solverVariable3, solverVariable4, n3);
        if (n4 != 6) {
            row.addError(this, n4);
        }
        this.addConstraint(row);
    }
    
    public void addConstraint(final ArrayRow arrayRow) {
        if (arrayRow == null) {
            return;
        }
        final Metrics sMetrics = LinearSystem.sMetrics;
        if (sMetrics != null) {
            ++sMetrics.constraints;
            if (arrayRow.isSimpleDefinition) {
                final Metrics sMetrics2 = LinearSystem.sMetrics;
                ++sMetrics2.simpleconstraints;
            }
        }
        if (this.mNumRows + 1 >= this.mMaxRows || this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        int n = 0;
        final int n2 = 0;
        if (!arrayRow.isSimpleDefinition) {
            this.updateRowFromVariables(arrayRow);
            if (arrayRow.isEmpty()) {
                return;
            }
            arrayRow.ensurePositiveConstant();
            n = n2;
            if (arrayRow.chooseSubject(this)) {
                final SolverVariable extraVariable = this.createExtraVariable();
                arrayRow.variable = extraVariable;
                this.addRow(arrayRow);
                final boolean b = true;
                this.mTempGoal.initFromRow((Row)arrayRow);
                this.optimize(this.mTempGoal, true);
                n = (b ? 1 : 0);
                if (extraVariable.definitionId == -1) {
                    if (arrayRow.variable == extraVariable) {
                        final SolverVariable pickPivot = arrayRow.pickPivot(extraVariable);
                        if (pickPivot != null) {
                            final Metrics sMetrics3 = LinearSystem.sMetrics;
                            if (sMetrics3 != null) {
                                ++sMetrics3.pivots;
                            }
                            arrayRow.pivot(pickPivot);
                        }
                    }
                    if (!arrayRow.isSimpleDefinition) {
                        arrayRow.variable.updateReferencesWithNewDefinition(arrayRow);
                    }
                    --this.mNumRows;
                    n = (b ? 1 : 0);
                }
            }
            if (!arrayRow.hasKeyVariable()) {
                return;
            }
        }
        if (n == 0) {
            this.addRow(arrayRow);
        }
    }
    
    public ArrayRow addEquality(final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final int n2) {
        final ArrayRow row = this.createRow();
        row.createRowEquals(solverVariable, solverVariable2, n);
        if (n2 != 6) {
            row.addError(this, n2);
        }
        this.addConstraint(row);
        return row;
    }
    
    public void addEquality(final SolverVariable solverVariable, final int n) {
        final int definitionId = solverVariable.definitionId;
        if (solverVariable.definitionId != -1) {
            final ArrayRow arrayRow = this.mRows[definitionId];
            if (arrayRow.isSimpleDefinition) {
                arrayRow.constantValue = (float)n;
            }
            else if (arrayRow.variables.currentSize == 0) {
                arrayRow.isSimpleDefinition = true;
                arrayRow.constantValue = (float)n;
            }
            else {
                final ArrayRow row = this.createRow();
                row.createRowEquals(solverVariable, n);
                this.addConstraint(row);
            }
        }
        else {
            final ArrayRow row2 = this.createRow();
            row2.createRowDefinition(solverVariable, n);
            this.addConstraint(row2);
        }
    }
    
    public void addEquality(final SolverVariable solverVariable, final int n, final int n2) {
        final int definitionId = solverVariable.definitionId;
        if (solverVariable.definitionId != -1) {
            final ArrayRow arrayRow = this.mRows[definitionId];
            if (arrayRow.isSimpleDefinition) {
                arrayRow.constantValue = (float)n;
            }
            else {
                final ArrayRow row = this.createRow();
                row.createRowEquals(solverVariable, n);
                row.addError(this, n2);
                this.addConstraint(row);
            }
        }
        else {
            final ArrayRow row2 = this.createRow();
            row2.createRowDefinition(solverVariable, n);
            row2.addError(this, n2);
            this.addConstraint(row2);
        }
    }
    
    public void addGreaterBarrier(final SolverVariable solverVariable, final SolverVariable solverVariable2, final boolean b) {
        final ArrayRow row = this.createRow();
        final SolverVariable slackVariable = this.createSlackVariable();
        row.createRowGreaterThan(solverVariable, solverVariable2, slackVariable, slackVariable.strength = 0);
        if (b) {
            this.addSingleError(row, (int)(-1.0f * row.variables.get(slackVariable)), 1);
        }
        this.addConstraint(row);
    }
    
    public void addGreaterThan(final SolverVariable solverVariable, final int n) {
        final ArrayRow row = this.createRow();
        final SolverVariable slackVariable = this.createSlackVariable();
        slackVariable.strength = 0;
        row.createRowGreaterThan(solverVariable, n, slackVariable);
        this.addConstraint(row);
    }
    
    public void addGreaterThan(final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final int n2) {
        final ArrayRow row = this.createRow();
        final SolverVariable slackVariable = this.createSlackVariable();
        slackVariable.strength = 0;
        row.createRowGreaterThan(solverVariable, solverVariable2, slackVariable, n);
        if (n2 != 6) {
            this.addSingleError(row, (int)(-1.0f * row.variables.get(slackVariable)), n2);
        }
        this.addConstraint(row);
    }
    
    public void addLowerBarrier(final SolverVariable solverVariable, final SolverVariable solverVariable2, final boolean b) {
        final ArrayRow row = this.createRow();
        final SolverVariable slackVariable = this.createSlackVariable();
        row.createRowLowerThan(solverVariable, solverVariable2, slackVariable, slackVariable.strength = 0);
        if (b) {
            this.addSingleError(row, (int)(-1.0f * row.variables.get(slackVariable)), 1);
        }
        this.addConstraint(row);
    }
    
    public void addLowerThan(final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final int n2) {
        final ArrayRow row = this.createRow();
        final SolverVariable slackVariable = this.createSlackVariable();
        slackVariable.strength = 0;
        row.createRowLowerThan(solverVariable, solverVariable2, slackVariable, n);
        if (n2 != 6) {
            this.addSingleError(row, (int)(-1.0f * row.variables.get(slackVariable)), n2);
        }
        this.addConstraint(row);
    }
    
    public void addRatio(final SolverVariable solverVariable, final SolverVariable solverVariable2, final SolverVariable solverVariable3, final SolverVariable solverVariable4, final float n, final int n2) {
        final ArrayRow row = this.createRow();
        row.createRowDimensionRatio(solverVariable, solverVariable2, solverVariable3, solverVariable4, n);
        if (n2 != 6) {
            row.addError(this, n2);
        }
        this.addConstraint(row);
    }
    
    void addSingleError(final ArrayRow arrayRow, final int n, final int n2) {
        arrayRow.addSingleError(this.createErrorVariable(n2, null), n);
    }
    
    public SolverVariable createErrorVariable(final int strength, final String s) {
        final Metrics sMetrics = LinearSystem.sMetrics;
        if (sMetrics != null) {
            ++sMetrics.errors;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        final SolverVariable acquireSolverVariable = this.acquireSolverVariable(SolverVariable.Type.ERROR, s);
        final int n = this.mVariablesID + 1;
        this.mVariablesID = n;
        ++this.mNumColumns;
        acquireSolverVariable.id = n;
        acquireSolverVariable.strength = strength;
        this.mCache.mIndexedVariables[this.mVariablesID] = acquireSolverVariable;
        this.mGoal.addError(acquireSolverVariable);
        return acquireSolverVariable;
    }
    
    public SolverVariable createExtraVariable() {
        final Metrics sMetrics = LinearSystem.sMetrics;
        if (sMetrics != null) {
            ++sMetrics.extravariables;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        final SolverVariable acquireSolverVariable = this.acquireSolverVariable(SolverVariable.Type.SLACK, null);
        final int n = this.mVariablesID + 1;
        this.mVariablesID = n;
        ++this.mNumColumns;
        acquireSolverVariable.id = n;
        return this.mCache.mIndexedVariables[this.mVariablesID] = acquireSolverVariable;
    }
    
    public SolverVariable createObjectVariable(final Object o) {
        if (o == null) {
            return null;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        SolverVariable solverVariable = null;
        if (o instanceof ConstraintAnchor) {
            SolverVariable solverVariable2;
            if ((solverVariable2 = ((ConstraintAnchor)o).getSolverVariable()) == null) {
                ((ConstraintAnchor)o).resetSolverVariable(this.mCache);
                solverVariable2 = ((ConstraintAnchor)o).getSolverVariable();
            }
            if (solverVariable2.id != -1 && solverVariable2.id <= this.mVariablesID) {
                solverVariable = solverVariable2;
                if (this.mCache.mIndexedVariables[solverVariable2.id] != null) {
                    return solverVariable;
                }
            }
            if (solverVariable2.id != -1) {
                solverVariable2.reset();
            }
            final int n = this.mVariablesID + 1;
            this.mVariablesID = n;
            ++this.mNumColumns;
            solverVariable2.id = n;
            solverVariable2.mType = SolverVariable.Type.UNRESTRICTED;
            this.mCache.mIndexedVariables[this.mVariablesID] = solverVariable2;
            solverVariable = solverVariable2;
        }
        return solverVariable;
    }
    
    public ArrayRow createRow() {
        ArrayRow arrayRow = this.mCache.arrayRowPool.acquire();
        if (arrayRow == null) {
            arrayRow = new ArrayRow(this.mCache);
        }
        else {
            arrayRow.reset();
        }
        SolverVariable.increaseErrorId();
        return arrayRow;
    }
    
    public SolverVariable createSlackVariable() {
        final Metrics sMetrics = LinearSystem.sMetrics;
        if (sMetrics != null) {
            ++sMetrics.slackvariables;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        final SolverVariable acquireSolverVariable = this.acquireSolverVariable(SolverVariable.Type.SLACK, null);
        final int n = this.mVariablesID + 1;
        this.mVariablesID = n;
        ++this.mNumColumns;
        acquireSolverVariable.id = n;
        return this.mCache.mIndexedVariables[this.mVariablesID] = acquireSolverVariable;
    }
    
    void displayReadableRows() {
        this.displaySolverVariables();
        String string = " #  ";
        for (int i = 0; i < this.mNumRows; ++i) {
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(this.mRows[i].toReadableString());
            final String string2 = sb.toString();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(string2);
            sb2.append("\n #  ");
            string = sb2.toString();
        }
        String string3 = string;
        if (this.mGoal != null) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(string);
            sb3.append(this.mGoal);
            sb3.append("\n");
            string3 = sb3.toString();
        }
        System.out.println(string3);
    }
    
    void displaySystemInformations() {
        int n = 0;
        int n2;
        for (int i = 0; i < this.TABLE_SIZE; ++i, n = n2) {
            final ArrayRow[] mRows = this.mRows;
            n2 = n;
            if (mRows[i] != null) {
                n2 = n + mRows[i].sizeInBytes();
            }
        }
        int n3 = 0;
        int n4;
        for (int j = 0; j < this.mNumRows; ++j, n3 = n4) {
            final ArrayRow[] mRows2 = this.mRows;
            n4 = n3;
            if (mRows2[j] != null) {
                n4 = n3 + mRows2[j].sizeInBytes();
            }
        }
        final PrintStream out = System.out;
        final StringBuilder sb = new StringBuilder();
        sb.append("Linear System -> Table size: ");
        sb.append(this.TABLE_SIZE);
        sb.append(" (");
        final int table_SIZE = this.TABLE_SIZE;
        sb.append(this.getDisplaySize(table_SIZE * table_SIZE));
        sb.append(") -- row sizes: ");
        sb.append(this.getDisplaySize(n));
        sb.append(", actual size: ");
        sb.append(this.getDisplaySize(n3));
        sb.append(" rows: ");
        sb.append(this.mNumRows);
        sb.append("/");
        sb.append(this.mMaxRows);
        sb.append(" cols: ");
        sb.append(this.mNumColumns);
        sb.append("/");
        sb.append(this.mMaxColumns);
        sb.append(" ");
        sb.append(0);
        sb.append(" occupied cells, ");
        sb.append(this.getDisplaySize(0));
        out.println(sb.toString());
    }
    
    public void displayVariablesReadableRows() {
        this.displaySolverVariables();
        String s = "";
        String string;
        for (int i = 0; i < this.mNumRows; ++i, s = string) {
            string = s;
            if (this.mRows[i].variable.mType == SolverVariable.Type.UNRESTRICTED) {
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append(this.mRows[i].toReadableString());
                final String string2 = sb.toString();
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(string2);
                sb2.append("\n");
                string = sb2.toString();
            }
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(s);
        sb3.append(this.mGoal);
        sb3.append("\n");
        System.out.println(sb3.toString());
    }
    
    public void fillMetrics(final Metrics sMetrics) {
        LinearSystem.sMetrics = sMetrics;
    }
    
    public Cache getCache() {
        return this.mCache;
    }
    
    Row getGoal() {
        return this.mGoal;
    }
    
    public int getMemoryUsed() {
        int n = 0;
        int n2;
        for (int i = 0; i < this.mNumRows; ++i, n = n2) {
            final ArrayRow[] mRows = this.mRows;
            n2 = n;
            if (mRows[i] != null) {
                n2 = n + mRows[i].sizeInBytes();
            }
        }
        return n;
    }
    
    public int getNumEquations() {
        return this.mNumRows;
    }
    
    public int getNumVariables() {
        return this.mVariablesID;
    }
    
    public int getObjectVariableValue(final Object o) {
        final SolverVariable solverVariable = ((ConstraintAnchor)o).getSolverVariable();
        if (solverVariable != null) {
            return (int)(solverVariable.computedValue + 0.5f);
        }
        return 0;
    }
    
    ArrayRow getRow(final int n) {
        return this.mRows[n];
    }
    
    float getValueFor(final String s) {
        final SolverVariable variable = this.getVariable(s, SolverVariable.Type.UNRESTRICTED);
        if (variable == null) {
            return 0.0f;
        }
        return variable.computedValue;
    }
    
    SolverVariable getVariable(final String key, final SolverVariable.Type type) {
        if (this.mVariables == null) {
            this.mVariables = new HashMap<String, SolverVariable>();
        }
        SolverVariable variable;
        if ((variable = this.mVariables.get(key)) == null) {
            variable = this.createVariable(key, type);
        }
        return variable;
    }
    
    public void minimize() throws Exception {
        final Metrics sMetrics = LinearSystem.sMetrics;
        if (sMetrics != null) {
            ++sMetrics.minimize;
        }
        if (this.graphOptimizer) {
            final Metrics sMetrics2 = LinearSystem.sMetrics;
            if (sMetrics2 != null) {
                ++sMetrics2.graphOptimizer;
            }
            final int n = 1;
            int n2 = 0;
            int n3;
            while (true) {
                n3 = n;
                if (n2 >= this.mNumRows) {
                    break;
                }
                if (!this.mRows[n2].isSimpleDefinition) {
                    n3 = 0;
                    break;
                }
                ++n2;
            }
            if (n3 == 0) {
                this.minimizeGoal(this.mGoal);
            }
            else {
                final Metrics sMetrics3 = LinearSystem.sMetrics;
                if (sMetrics3 != null) {
                    ++sMetrics3.fullySolved;
                }
                this.computeValues();
            }
        }
        else {
            this.minimizeGoal(this.mGoal);
        }
    }
    
    void minimizeGoal(final Row row) throws Exception {
        final Metrics sMetrics = LinearSystem.sMetrics;
        if (sMetrics != null) {
            ++sMetrics.minimizeGoal;
            final Metrics sMetrics2 = LinearSystem.sMetrics;
            sMetrics2.maxVariables = Math.max(sMetrics2.maxVariables, this.mNumColumns);
            final Metrics sMetrics3 = LinearSystem.sMetrics;
            sMetrics3.maxRows = Math.max(sMetrics3.maxRows, this.mNumRows);
        }
        this.updateRowFromVariables((ArrayRow)row);
        this.enforceBFS(row);
        this.optimize(row, false);
        this.computeValues();
    }
    
    public void reset() {
        for (int i = 0; i < this.mCache.mIndexedVariables.length; ++i) {
            final SolverVariable solverVariable = this.mCache.mIndexedVariables[i];
            if (solverVariable != null) {
                solverVariable.reset();
            }
        }
        this.mCache.solverVariablePool.releaseAll(this.mPoolVariables, this.mPoolVariablesCount);
        this.mPoolVariablesCount = 0;
        Arrays.fill(this.mCache.mIndexedVariables, null);
        final HashMap<String, SolverVariable> mVariables = this.mVariables;
        if (mVariables != null) {
            mVariables.clear();
        }
        this.mVariablesID = 0;
        this.mGoal.clear();
        this.mNumColumns = 1;
        for (int j = 0; j < this.mNumRows; ++j) {
            this.mRows[j].used = false;
        }
        this.releaseRows();
        this.mNumRows = 0;
    }
    
    interface Row
    {
        void addError(final SolverVariable p0);
        
        void clear();
        
        SolverVariable getKey();
        
        SolverVariable getPivotCandidate(final LinearSystem p0, final boolean[] p1);
        
        void initFromRow(final Row p0);
        
        boolean isEmpty();
    }
}
