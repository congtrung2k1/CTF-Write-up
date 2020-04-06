// 
// Decompiled by Procyon v0.5.36
// 

package androidx.constraintlayout.solver;

import java.util.Arrays;

public class SolverVariable
{
    private static final boolean INTERNAL_DEBUG = false;
    static final int MAX_STRENGTH = 7;
    public static final int STRENGTH_BARRIER = 7;
    public static final int STRENGTH_EQUALITY = 5;
    public static final int STRENGTH_FIXED = 6;
    public static final int STRENGTH_HIGH = 3;
    public static final int STRENGTH_HIGHEST = 4;
    public static final int STRENGTH_LOW = 1;
    public static final int STRENGTH_MEDIUM = 2;
    public static final int STRENGTH_NONE = 0;
    private static int uniqueConstantId;
    private static int uniqueErrorId;
    private static int uniqueId;
    private static int uniqueSlackId;
    private static int uniqueUnrestrictedId;
    public float computedValue;
    int definitionId;
    public int id;
    ArrayRow[] mClientEquations;
    int mClientEquationsCount;
    private String mName;
    Type mType;
    public int strength;
    float[] strengthVector;
    public int usageInRowCount;
    
    static {
        SolverVariable.uniqueSlackId = 1;
        SolverVariable.uniqueErrorId = 1;
        SolverVariable.uniqueUnrestrictedId = 1;
        SolverVariable.uniqueConstantId = 1;
        SolverVariable.uniqueId = 1;
    }
    
    public SolverVariable(final Type mType, final String s) {
        this.id = -1;
        this.definitionId = -1;
        this.strength = 0;
        this.strengthVector = new float[7];
        this.mClientEquations = new ArrayRow[8];
        this.mClientEquationsCount = 0;
        this.usageInRowCount = 0;
        this.mType = mType;
    }
    
    public SolverVariable(final String mName, final Type mType) {
        this.id = -1;
        this.definitionId = -1;
        this.strength = 0;
        this.strengthVector = new float[7];
        this.mClientEquations = new ArrayRow[8];
        this.mClientEquationsCount = 0;
        this.usageInRowCount = 0;
        this.mName = mName;
        this.mType = mType;
    }
    
    private static String getUniqueName(final Type type, final String str) {
        if (str != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(SolverVariable.uniqueErrorId);
            return sb.toString();
        }
        final int n = SolverVariable$1.$SwitchMap$androidx$constraintlayout$solver$SolverVariable$Type[type.ordinal()];
        if (n == 1) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("U");
            sb2.append(++SolverVariable.uniqueUnrestrictedId);
            return sb2.toString();
        }
        if (n == 2) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("C");
            sb3.append(++SolverVariable.uniqueConstantId);
            return sb3.toString();
        }
        if (n == 3) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("S");
            sb4.append(++SolverVariable.uniqueSlackId);
            return sb4.toString();
        }
        if (n == 4) {
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("e");
            sb5.append(++SolverVariable.uniqueErrorId);
            return sb5.toString();
        }
        if (n == 5) {
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("V");
            sb6.append(++SolverVariable.uniqueId);
            return sb6.toString();
        }
        throw new AssertionError((Object)type.name());
    }
    
    static void increaseErrorId() {
        ++SolverVariable.uniqueErrorId;
    }
    
    public final void addToRow(final ArrayRow arrayRow) {
        int n = 0;
        while (true) {
            final int mClientEquationsCount = this.mClientEquationsCount;
            if (n >= mClientEquationsCount) {
                final ArrayRow[] mClientEquations = this.mClientEquations;
                if (mClientEquationsCount >= mClientEquations.length) {
                    this.mClientEquations = Arrays.copyOf(mClientEquations, mClientEquations.length * 2);
                }
                final ArrayRow[] mClientEquations2 = this.mClientEquations;
                final int mClientEquationsCount2 = this.mClientEquationsCount;
                mClientEquations2[mClientEquationsCount2] = arrayRow;
                this.mClientEquationsCount = mClientEquationsCount2 + 1;
                return;
            }
            if (this.mClientEquations[n] == arrayRow) {
                return;
            }
            ++n;
        }
    }
    
    void clearStrengths() {
        for (int i = 0; i < 7; ++i) {
            this.strengthVector[i] = 0.0f;
        }
    }
    
    public String getName() {
        return this.mName;
    }
    
    public final void removeFromRow(final ArrayRow arrayRow) {
        for (int mClientEquationsCount = this.mClientEquationsCount, i = 0; i < mClientEquationsCount; ++i) {
            if (this.mClientEquations[i] == arrayRow) {
                for (int j = 0; j < mClientEquationsCount - i - 1; ++j) {
                    final ArrayRow[] mClientEquations = this.mClientEquations;
                    mClientEquations[i + j] = mClientEquations[i + j + 1];
                }
                --this.mClientEquationsCount;
                return;
            }
        }
    }
    
    public void reset() {
        this.mName = null;
        this.mType = Type.UNKNOWN;
        this.strength = 0;
        this.id = -1;
        this.definitionId = -1;
        this.computedValue = 0.0f;
        this.mClientEquationsCount = 0;
        this.usageInRowCount = 0;
    }
    
    public void setName(final String mName) {
        this.mName = mName;
    }
    
    public void setType(final Type mType, final String s) {
        this.mType = mType;
    }
    
    String strengthsToString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this);
        sb.append("[");
        String s = sb.toString();
        boolean b = false;
        boolean b2 = true;
        for (int i = 0; i < this.strengthVector.length; ++i) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append(this.strengthVector[i]);
            final String string = sb2.toString();
            final float[] strengthVector = this.strengthVector;
            if (strengthVector[i] > 0.0f) {
                b = false;
            }
            else if (strengthVector[i] < 0.0f) {
                b = true;
            }
            if (this.strengthVector[i] != 0.0f) {
                b2 = false;
            }
            if (i < this.strengthVector.length - 1) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(string);
                sb3.append(", ");
                s = sb3.toString();
            }
            else {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append(string);
                sb4.append("] ");
                s = sb4.toString();
            }
        }
        String string2 = s;
        if (b) {
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(s);
            sb5.append(" (-)");
            string2 = sb5.toString();
        }
        String string3 = string2;
        if (b2) {
            final StringBuilder sb6 = new StringBuilder();
            sb6.append(string2);
            sb6.append(" (*)");
            string3 = sb6.toString();
        }
        return string3;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(this.mName);
        return sb.toString();
    }
    
    public final void updateReferencesWithNewDefinition(final ArrayRow arrayRow) {
        for (int mClientEquationsCount = this.mClientEquationsCount, i = 0; i < mClientEquationsCount; ++i) {
            this.mClientEquations[i].variables.updateFromRow(this.mClientEquations[i], arrayRow, false);
        }
        this.mClientEquationsCount = 0;
    }
    
    public enum Type
    {
        CONSTANT, 
        ERROR, 
        SLACK, 
        UNKNOWN, 
        UNRESTRICTED;
    }
}
