// 
// Decompiled by Procyon v0.5.36
// 

package androidx.constraintlayout.solver;

import java.io.PrintStream;
import java.util.Arrays;

public class ArrayLinkedVariables
{
    private static final boolean DEBUG = false;
    private static final boolean FULL_NEW_CHECK = false;
    private static final int NONE = -1;
    private int ROW_SIZE;
    private SolverVariable candidate;
    int currentSize;
    private int[] mArrayIndices;
    private int[] mArrayNextIndices;
    private float[] mArrayValues;
    private final Cache mCache;
    private boolean mDidFillOnce;
    private int mHead;
    private int mLast;
    private final ArrayRow mRow;
    
    ArrayLinkedVariables(final ArrayRow mRow, final Cache mCache) {
        this.currentSize = 0;
        this.ROW_SIZE = 8;
        this.candidate = null;
        this.mArrayIndices = new int[8];
        this.mArrayNextIndices = new int[8];
        this.mArrayValues = new float[8];
        this.mHead = -1;
        this.mLast = -1;
        this.mDidFillOnce = false;
        this.mRow = mRow;
        this.mCache = mCache;
    }
    
    private boolean isNew(final SolverVariable solverVariable, final LinearSystem linearSystem) {
        final int usageInRowCount = solverVariable.usageInRowCount;
        boolean b = true;
        if (usageInRowCount > 1) {
            b = false;
        }
        return b;
    }
    
    final void add(final SolverVariable solverVariable, final float n, final boolean b) {
        if (n == 0.0f) {
            return;
        }
        if (this.mHead == -1) {
            this.mHead = 0;
            this.mArrayValues[0] = n;
            this.mArrayIndices[0] = solverVariable.id;
            this.mArrayNextIndices[this.mHead] = -1;
            ++solverVariable.usageInRowCount;
            solverVariable.addToRow(this.mRow);
            ++this.currentSize;
            if (!this.mDidFillOnce) {
                final int mLast = this.mLast + 1;
                this.mLast = mLast;
                final int[] mArrayIndices = this.mArrayIndices;
                if (mLast >= mArrayIndices.length) {
                    this.mDidFillOnce = true;
                    this.mLast = mArrayIndices.length - 1;
                }
            }
            return;
        }
        int mHead = this.mHead;
        int n2 = -1;
        for (int n3 = 0; mHead != -1 && n3 < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n3) {
            if (this.mArrayIndices[mHead] == solverVariable.id) {
                final float[] mArrayValues = this.mArrayValues;
                mArrayValues[mHead] += n;
                if (mArrayValues[mHead] == 0.0f) {
                    if (mHead == this.mHead) {
                        this.mHead = this.mArrayNextIndices[mHead];
                    }
                    else {
                        final int[] mArrayNextIndices = this.mArrayNextIndices;
                        mArrayNextIndices[n2] = mArrayNextIndices[mHead];
                    }
                    if (b) {
                        solverVariable.removeFromRow(this.mRow);
                    }
                    if (this.mDidFillOnce) {
                        this.mLast = mHead;
                    }
                    --solverVariable.usageInRowCount;
                    --this.currentSize;
                }
                return;
            }
            if (this.mArrayIndices[mHead] < solverVariable.id) {
                n2 = mHead;
            }
        }
        final int mLast2 = this.mLast;
        int n4 = mLast2 + 1;
        if (this.mDidFillOnce) {
            final int[] mArrayIndices2 = this.mArrayIndices;
            if (mArrayIndices2[mLast2] == -1) {
                n4 = this.mLast;
            }
            else {
                n4 = mArrayIndices2.length;
            }
        }
        final int[] mArrayIndices3 = this.mArrayIndices;
        int n5;
        if ((n5 = n4) >= mArrayIndices3.length) {
            n5 = n4;
            if (this.currentSize < mArrayIndices3.length) {
                int n6 = 0;
                while (true) {
                    final int[] mArrayIndices4 = this.mArrayIndices;
                    n5 = n4;
                    if (n6 >= mArrayIndices4.length) {
                        break;
                    }
                    if (mArrayIndices4[n6] == -1) {
                        n5 = n6;
                        break;
                    }
                    ++n6;
                }
            }
        }
        final int[] mArrayIndices5 = this.mArrayIndices;
        int length;
        if ((length = n5) >= mArrayIndices5.length) {
            length = mArrayIndices5.length;
            final int n7 = this.ROW_SIZE * 2;
            this.ROW_SIZE = n7;
            this.mDidFillOnce = false;
            this.mLast = length - 1;
            this.mArrayValues = Arrays.copyOf(this.mArrayValues, n7);
            this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
            this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
        }
        this.mArrayIndices[length] = solverVariable.id;
        this.mArrayValues[length] = n;
        if (n2 != -1) {
            final int[] mArrayNextIndices2 = this.mArrayNextIndices;
            mArrayNextIndices2[length] = mArrayNextIndices2[n2];
            mArrayNextIndices2[n2] = length;
        }
        else {
            this.mArrayNextIndices[length] = this.mHead;
            this.mHead = length;
        }
        ++solverVariable.usageInRowCount;
        solverVariable.addToRow(this.mRow);
        ++this.currentSize;
        if (!this.mDidFillOnce) {
            ++this.mLast;
        }
        final int mLast3 = this.mLast;
        final int[] mArrayIndices6 = this.mArrayIndices;
        if (mLast3 >= mArrayIndices6.length) {
            this.mDidFillOnce = true;
            this.mLast = mArrayIndices6.length - 1;
        }
    }
    
    SolverVariable chooseSubject(final LinearSystem linearSystem) {
        SolverVariable solverVariable = null;
        SolverVariable solverVariable2 = null;
        float n = 0.0f;
        float n2 = 0.0f;
        int n3 = 0;
        int n4 = 0;
        SolverVariable solverVariable4;
        SolverVariable solverVariable5;
        float n8;
        float n9;
        int n10;
        int n11;
        for (int mHead = this.mHead, n5 = 0; mHead != -1 && n5 < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n5, solverVariable = solverVariable4, solverVariable2 = solverVariable5, n = n8, n2 = n9, n3 = n10, n4 = n11) {
            final float n6 = this.mArrayValues[mHead];
            final SolverVariable solverVariable3 = this.mCache.mIndexedVariables[this.mArrayIndices[mHead]];
            float n7;
            if (n6 < 0.0f) {
                n7 = n6;
                if (n6 > -0.001f) {
                    this.mArrayValues[mHead] = 0.0f;
                    n7 = 0.0f;
                    solverVariable3.removeFromRow(this.mRow);
                }
            }
            else {
                n7 = n6;
                if (n6 < 0.001f) {
                    this.mArrayValues[mHead] = 0.0f;
                    n7 = 0.0f;
                    solverVariable3.removeFromRow(this.mRow);
                }
            }
            solverVariable4 = solverVariable;
            solverVariable5 = solverVariable2;
            n8 = n;
            n9 = n2;
            n10 = n3;
            n11 = n4;
            if (n7 != 0.0f) {
                if (solverVariable3.mType == SolverVariable.Type.UNRESTRICTED) {
                    if (solverVariable2 == null) {
                        solverVariable5 = solverVariable3;
                        n10 = (this.isNew(solverVariable3, linearSystem) ? 1 : 0);
                        solverVariable4 = solverVariable;
                        n8 = n7;
                        n9 = n2;
                        n11 = n4;
                    }
                    else if (n > n7) {
                        solverVariable5 = solverVariable3;
                        n10 = (this.isNew(solverVariable3, linearSystem) ? 1 : 0);
                        solverVariable4 = solverVariable;
                        n8 = n7;
                        n9 = n2;
                        n11 = n4;
                    }
                    else {
                        solverVariable4 = solverVariable;
                        solverVariable5 = solverVariable2;
                        n8 = n;
                        n9 = n2;
                        n10 = n3;
                        n11 = n4;
                        if (n3 == 0) {
                            solverVariable4 = solverVariable;
                            solverVariable5 = solverVariable2;
                            n8 = n;
                            n9 = n2;
                            n10 = n3;
                            n11 = n4;
                            if (this.isNew(solverVariable3, linearSystem)) {
                                n10 = 1;
                                solverVariable4 = solverVariable;
                                solverVariable5 = solverVariable3;
                                n8 = n7;
                                n9 = n2;
                                n11 = n4;
                            }
                        }
                    }
                }
                else {
                    solverVariable4 = solverVariable;
                    solverVariable5 = solverVariable2;
                    n8 = n;
                    n9 = n2;
                    n10 = n3;
                    n11 = n4;
                    if (solverVariable2 == null) {
                        solverVariable4 = solverVariable;
                        solverVariable5 = solverVariable2;
                        n8 = n;
                        n9 = n2;
                        n10 = n3;
                        n11 = n4;
                        if (n7 < 0.0f) {
                            if (solverVariable == null) {
                                solverVariable4 = solverVariable3;
                                n11 = (this.isNew(solverVariable3, linearSystem) ? 1 : 0);
                                solverVariable5 = solverVariable2;
                                n8 = n;
                                n9 = n7;
                                n10 = n3;
                            }
                            else if (n2 > n7) {
                                solverVariable4 = solverVariable3;
                                n11 = (this.isNew(solverVariable3, linearSystem) ? 1 : 0);
                                solverVariable5 = solverVariable2;
                                n8 = n;
                                n9 = n7;
                                n10 = n3;
                            }
                            else {
                                solverVariable4 = solverVariable;
                                solverVariable5 = solverVariable2;
                                n8 = n;
                                n9 = n2;
                                n10 = n3;
                                if ((n11 = n4) == 0) {
                                    solverVariable4 = solverVariable;
                                    solverVariable5 = solverVariable2;
                                    n8 = n;
                                    n9 = n2;
                                    n10 = n3;
                                    n11 = n4;
                                    if (this.isNew(solverVariable3, linearSystem)) {
                                        n11 = 1;
                                        n10 = n3;
                                        n9 = n7;
                                        n8 = n;
                                        solverVariable5 = solverVariable2;
                                        solverVariable4 = solverVariable3;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (solverVariable2 != null) {
            return solverVariable2;
        }
        return solverVariable;
    }
    
    public final void clear() {
        for (int mHead = this.mHead, n = 0; mHead != -1 && n < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n) {
            final SolverVariable solverVariable = this.mCache.mIndexedVariables[this.mArrayIndices[mHead]];
            if (solverVariable != null) {
                solverVariable.removeFromRow(this.mRow);
            }
        }
        this.mHead = -1;
        this.mLast = -1;
        this.mDidFillOnce = false;
        this.currentSize = 0;
    }
    
    final boolean containsKey(final SolverVariable solverVariable) {
        if (this.mHead == -1) {
            return false;
        }
        for (int mHead = this.mHead, n = 0; mHead != -1 && n < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n) {
            if (this.mArrayIndices[mHead] == solverVariable.id) {
                return true;
            }
        }
        return false;
    }
    
    public void display() {
        final int currentSize = this.currentSize;
        System.out.print("{ ");
        for (int i = 0; i < currentSize; ++i) {
            final SolverVariable variable = this.getVariable(i);
            if (variable != null) {
                final PrintStream out = System.out;
                final StringBuilder sb = new StringBuilder();
                sb.append(variable);
                sb.append(" = ");
                sb.append(this.getVariableValue(i));
                sb.append(" ");
                out.print(sb.toString());
            }
        }
        System.out.println(" }");
    }
    
    void divideByAmount(final float n) {
        for (int mHead = this.mHead, n2 = 0; mHead != -1 && n2 < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n2) {
            final float[] mArrayValues = this.mArrayValues;
            mArrayValues[mHead] /= n;
        }
    }
    
    public final float get(final SolverVariable solverVariable) {
        for (int mHead = this.mHead, n = 0; mHead != -1 && n < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n) {
            if (this.mArrayIndices[mHead] == solverVariable.id) {
                return this.mArrayValues[mHead];
            }
        }
        return 0.0f;
    }
    
    SolverVariable getPivotCandidate() {
        final SolverVariable candidate = this.candidate;
        if (candidate == null) {
            int mHead = this.mHead;
            int n = 0;
            SolverVariable solverVariable = null;
            while (mHead != -1 && n < this.currentSize) {
                SolverVariable solverVariable2 = solverVariable;
                Label_0086: {
                    if (this.mArrayValues[mHead] < 0.0f) {
                        final SolverVariable solverVariable3 = this.mCache.mIndexedVariables[this.mArrayIndices[mHead]];
                        if (solverVariable != null) {
                            solverVariable2 = solverVariable;
                            if (solverVariable.strength >= solverVariable3.strength) {
                                break Label_0086;
                            }
                        }
                        solverVariable2 = solverVariable3;
                    }
                }
                mHead = this.mArrayNextIndices[mHead];
                ++n;
                solverVariable = solverVariable2;
            }
            return solverVariable;
        }
        return candidate;
    }
    
    SolverVariable getPivotCandidate(final boolean[] array, final SolverVariable solverVariable) {
        int mHead = this.mHead;
        int n = 0;
        SolverVariable solverVariable2 = null;
        float n2 = 0.0f;
        while (mHead != -1 && n < this.currentSize) {
            SolverVariable solverVariable3 = solverVariable2;
            float n3 = n2;
            Label_0161: {
                if (this.mArrayValues[mHead] < 0.0f) {
                    final SolverVariable solverVariable4 = this.mCache.mIndexedVariables[this.mArrayIndices[mHead]];
                    if (array != null) {
                        solverVariable3 = solverVariable2;
                        n3 = n2;
                        if (array[solverVariable4.id]) {
                            break Label_0161;
                        }
                    }
                    solverVariable3 = solverVariable2;
                    n3 = n2;
                    if (solverVariable4 != solverVariable) {
                        if (solverVariable4.mType != SolverVariable.Type.SLACK) {
                            solverVariable3 = solverVariable2;
                            n3 = n2;
                            if (solverVariable4.mType != SolverVariable.Type.ERROR) {
                                break Label_0161;
                            }
                        }
                        final float n4 = this.mArrayValues[mHead];
                        solverVariable3 = solverVariable2;
                        n3 = n2;
                        if (n4 < n2) {
                            n3 = n4;
                            solverVariable3 = solverVariable4;
                        }
                    }
                }
            }
            mHead = this.mArrayNextIndices[mHead];
            ++n;
            solverVariable2 = solverVariable3;
            n2 = n3;
        }
        return solverVariable2;
    }
    
    final SolverVariable getVariable(final int n) {
        for (int mHead = this.mHead, n2 = 0; mHead != -1 && n2 < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n2) {
            if (n2 == n) {
                return this.mCache.mIndexedVariables[this.mArrayIndices[mHead]];
            }
        }
        return null;
    }
    
    final float getVariableValue(final int n) {
        for (int mHead = this.mHead, n2 = 0; mHead != -1 && n2 < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n2) {
            if (n2 == n) {
                return this.mArrayValues[mHead];
            }
        }
        return 0.0f;
    }
    
    boolean hasAtLeastOnePositiveVariable() {
        for (int mHead = this.mHead, n = 0; mHead != -1 && n < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n) {
            if (this.mArrayValues[mHead] > 0.0f) {
                return true;
            }
        }
        return false;
    }
    
    void invert() {
        for (int mHead = this.mHead, n = 0; mHead != -1 && n < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n) {
            final float[] mArrayValues = this.mArrayValues;
            mArrayValues[mHead] *= -1.0f;
        }
    }
    
    public final void put(final SolverVariable solverVariable, final float n) {
        if (n == 0.0f) {
            this.remove(solverVariable, true);
            return;
        }
        if (this.mHead == -1) {
            this.mHead = 0;
            this.mArrayValues[0] = n;
            this.mArrayIndices[0] = solverVariable.id;
            this.mArrayNextIndices[this.mHead] = -1;
            ++solverVariable.usageInRowCount;
            solverVariable.addToRow(this.mRow);
            ++this.currentSize;
            if (!this.mDidFillOnce) {
                final int mLast = this.mLast + 1;
                this.mLast = mLast;
                final int[] mArrayIndices = this.mArrayIndices;
                if (mLast >= mArrayIndices.length) {
                    this.mDidFillOnce = true;
                    this.mLast = mArrayIndices.length - 1;
                }
            }
            return;
        }
        int mHead = this.mHead;
        int n2 = -1;
        for (int n3 = 0; mHead != -1 && n3 < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n3) {
            if (this.mArrayIndices[mHead] == solverVariable.id) {
                this.mArrayValues[mHead] = n;
                return;
            }
            if (this.mArrayIndices[mHead] < solverVariable.id) {
                n2 = mHead;
            }
        }
        final int mLast2 = this.mLast;
        int n4 = mLast2 + 1;
        if (this.mDidFillOnce) {
            final int[] mArrayIndices2 = this.mArrayIndices;
            if (mArrayIndices2[mLast2] == -1) {
                n4 = this.mLast;
            }
            else {
                n4 = mArrayIndices2.length;
            }
        }
        final int[] mArrayIndices3 = this.mArrayIndices;
        int n5;
        if ((n5 = n4) >= mArrayIndices3.length) {
            n5 = n4;
            if (this.currentSize < mArrayIndices3.length) {
                int n6 = 0;
                while (true) {
                    final int[] mArrayIndices4 = this.mArrayIndices;
                    n5 = n4;
                    if (n6 >= mArrayIndices4.length) {
                        break;
                    }
                    if (mArrayIndices4[n6] == -1) {
                        n5 = n6;
                        break;
                    }
                    ++n6;
                }
            }
        }
        final int[] mArrayIndices5 = this.mArrayIndices;
        int length;
        if ((length = n5) >= mArrayIndices5.length) {
            length = mArrayIndices5.length;
            final int n7 = this.ROW_SIZE * 2;
            this.ROW_SIZE = n7;
            this.mDidFillOnce = false;
            this.mLast = length - 1;
            this.mArrayValues = Arrays.copyOf(this.mArrayValues, n7);
            this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
            this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
        }
        this.mArrayIndices[length] = solverVariable.id;
        this.mArrayValues[length] = n;
        if (n2 != -1) {
            final int[] mArrayNextIndices = this.mArrayNextIndices;
            mArrayNextIndices[length] = mArrayNextIndices[n2];
            mArrayNextIndices[n2] = length;
        }
        else {
            this.mArrayNextIndices[length] = this.mHead;
            this.mHead = length;
        }
        ++solverVariable.usageInRowCount;
        solverVariable.addToRow(this.mRow);
        ++this.currentSize;
        if (!this.mDidFillOnce) {
            ++this.mLast;
        }
        if (this.currentSize >= this.mArrayIndices.length) {
            this.mDidFillOnce = true;
        }
        final int mLast3 = this.mLast;
        final int[] mArrayIndices6 = this.mArrayIndices;
        if (mLast3 >= mArrayIndices6.length) {
            this.mDidFillOnce = true;
            this.mLast = mArrayIndices6.length - 1;
        }
    }
    
    public final float remove(final SolverVariable solverVariable, final boolean b) {
        if (this.candidate == solverVariable) {
            this.candidate = null;
        }
        if (this.mHead == -1) {
            return 0.0f;
        }
        int mHead = this.mHead;
        int n = -1;
        for (int n2 = 0; mHead != -1 && n2 < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n2) {
            if (this.mArrayIndices[mHead] == solverVariable.id) {
                if (mHead == this.mHead) {
                    this.mHead = this.mArrayNextIndices[mHead];
                }
                else {
                    final int[] mArrayNextIndices = this.mArrayNextIndices;
                    mArrayNextIndices[n] = mArrayNextIndices[mHead];
                }
                if (b) {
                    solverVariable.removeFromRow(this.mRow);
                }
                --solverVariable.usageInRowCount;
                --this.currentSize;
                this.mArrayIndices[mHead] = -1;
                if (this.mDidFillOnce) {
                    this.mLast = mHead;
                }
                return this.mArrayValues[mHead];
            }
            n = mHead;
        }
        return 0.0f;
    }
    
    int sizeInBytes() {
        return 0 + this.mArrayIndices.length * 4 * 3 + 36;
    }
    
    @Override
    public String toString() {
        String string = "";
        for (int mHead = this.mHead, n = 0; mHead != -1 && n < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n) {
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(" -> ");
            final String string2 = sb.toString();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(string2);
            sb2.append(this.mArrayValues[mHead]);
            sb2.append(" : ");
            final String string3 = sb2.toString();
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(string3);
            sb3.append(this.mCache.mIndexedVariables[this.mArrayIndices[mHead]]);
            string = sb3.toString();
        }
        return string;
    }
    
    final void updateFromRow(final ArrayRow arrayRow, final ArrayRow arrayRow2, final boolean b) {
        int n = this.mHead;
        int n2 = 0;
        while (n != -1 && n2 < this.currentSize) {
            if (this.mArrayIndices[n] == arrayRow2.variable.id) {
                final float n3 = this.mArrayValues[n];
                this.remove(arrayRow2.variable, b);
                final ArrayLinkedVariables arrayLinkedVariables = arrayRow2.variables;
                for (int mHead = arrayLinkedVariables.mHead, n4 = 0; mHead != -1 && n4 < arrayLinkedVariables.currentSize; mHead = arrayLinkedVariables.mArrayNextIndices[mHead], ++n4) {
                    this.add(this.mCache.mIndexedVariables[arrayLinkedVariables.mArrayIndices[mHead]], arrayLinkedVariables.mArrayValues[mHead] * n3, b);
                }
                arrayRow.constantValue += arrayRow2.constantValue * n3;
                if (b) {
                    arrayRow2.variable.removeFromRow(arrayRow);
                }
                n = this.mHead;
                n2 = 0;
            }
            else {
                n = this.mArrayNextIndices[n];
                ++n2;
            }
        }
    }
    
    void updateFromSystem(final ArrayRow arrayRow, final ArrayRow[] array) {
        int n = this.mHead;
        int n2 = 0;
        while (n != -1 && n2 < this.currentSize) {
            final SolverVariable solverVariable = this.mCache.mIndexedVariables[this.mArrayIndices[n]];
            if (solverVariable.definitionId != -1) {
                final float n3 = this.mArrayValues[n];
                this.remove(solverVariable, true);
                final ArrayRow arrayRow2 = array[solverVariable.definitionId];
                if (!arrayRow2.isSimpleDefinition) {
                    final ArrayLinkedVariables arrayLinkedVariables = arrayRow2.variables;
                    for (int mHead = arrayLinkedVariables.mHead, n4 = 0; mHead != -1 && n4 < arrayLinkedVariables.currentSize; mHead = arrayLinkedVariables.mArrayNextIndices[mHead], ++n4) {
                        this.add(this.mCache.mIndexedVariables[arrayLinkedVariables.mArrayIndices[mHead]], arrayLinkedVariables.mArrayValues[mHead] * n3, true);
                    }
                }
                arrayRow.constantValue += arrayRow2.constantValue * n3;
                arrayRow2.variable.removeFromRow(arrayRow);
                n = this.mHead;
                n2 = 0;
            }
            else {
                n = this.mArrayNextIndices[n];
                ++n2;
            }
        }
    }
}
