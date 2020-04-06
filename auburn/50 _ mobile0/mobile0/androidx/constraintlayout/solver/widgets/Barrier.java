// 
// Decompiled by Procyon v0.5.36
// 

package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.Metrics;
import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.LinearSystem;
import java.util.ArrayList;

public class Barrier extends Helper
{
    public static final int BOTTOM = 3;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int TOP = 2;
    private boolean mAllowsGoneWidget;
    private int mBarrierType;
    private ArrayList<ResolutionAnchor> mNodes;
    
    public Barrier() {
        this.mBarrierType = 0;
        this.mNodes = new ArrayList<ResolutionAnchor>(4);
        this.mAllowsGoneWidget = true;
    }
    
    @Override
    public void addToSolver(final LinearSystem linearSystem) {
        this.mListAnchors[0] = this.mLeft;
        this.mListAnchors[2] = this.mTop;
        this.mListAnchors[1] = this.mRight;
        this.mListAnchors[3] = this.mBottom;
        for (int i = 0; i < this.mListAnchors.length; ++i) {
            this.mListAnchors[i].mSolverVariable = linearSystem.createObjectVariable(this.mListAnchors[i]);
        }
        final int mBarrierType = this.mBarrierType;
        if (mBarrierType >= 0 && mBarrierType < 4) {
            final ConstraintAnchor constraintAnchor = this.mListAnchors[this.mBarrierType];
            final boolean b = false;
            int n = 0;
            boolean b2;
            while (true) {
                b2 = b;
                if (n >= this.mWidgetsCount) {
                    break;
                }
                final ConstraintWidget constraintWidget = this.mWidgets[n];
                if (this.mAllowsGoneWidget || constraintWidget.allowedInBarrier()) {
                    final int mBarrierType2 = this.mBarrierType;
                    if ((mBarrierType2 == 0 || mBarrierType2 == 1) && constraintWidget.getHorizontalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT) {
                        b2 = true;
                        break;
                    }
                    final int mBarrierType3 = this.mBarrierType;
                    if ((mBarrierType3 == 2 || mBarrierType3 == 3) && constraintWidget.getVerticalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT) {
                        b2 = true;
                        break;
                    }
                }
                ++n;
            }
            final int mBarrierType4 = this.mBarrierType;
            if (mBarrierType4 != 0 && mBarrierType4 != 1) {
                if (this.getParent().getVerticalDimensionBehaviour() == DimensionBehaviour.WRAP_CONTENT) {
                    b2 = false;
                }
            }
            else if (this.getParent().getHorizontalDimensionBehaviour() == DimensionBehaviour.WRAP_CONTENT) {
                b2 = false;
            }
            for (int j = 0; j < this.mWidgetsCount; ++j) {
                final ConstraintWidget constraintWidget2 = this.mWidgets[j];
                if (this.mAllowsGoneWidget || constraintWidget2.allowedInBarrier()) {
                    final SolverVariable objectVariable = linearSystem.createObjectVariable(constraintWidget2.mListAnchors[this.mBarrierType]);
                    constraintWidget2.mListAnchors[this.mBarrierType].mSolverVariable = objectVariable;
                    final int mBarrierType5 = this.mBarrierType;
                    if (mBarrierType5 != 0 && mBarrierType5 != 2) {
                        linearSystem.addGreaterBarrier(constraintAnchor.mSolverVariable, objectVariable, b2);
                    }
                    else {
                        linearSystem.addLowerBarrier(constraintAnchor.mSolverVariable, objectVariable, b2);
                    }
                }
            }
            final int mBarrierType6 = this.mBarrierType;
            if (mBarrierType6 == 0) {
                linearSystem.addEquality(this.mRight.mSolverVariable, this.mLeft.mSolverVariable, 0, 6);
                if (!b2) {
                    linearSystem.addEquality(this.mLeft.mSolverVariable, this.mParent.mRight.mSolverVariable, 0, 5);
                }
            }
            else if (mBarrierType6 == 1) {
                linearSystem.addEquality(this.mLeft.mSolverVariable, this.mRight.mSolverVariable, 0, 6);
                if (!b2) {
                    linearSystem.addEquality(this.mLeft.mSolverVariable, this.mParent.mLeft.mSolverVariable, 0, 5);
                }
            }
            else if (mBarrierType6 == 2) {
                linearSystem.addEquality(this.mBottom.mSolverVariable, this.mTop.mSolverVariable, 0, 6);
                if (!b2) {
                    linearSystem.addEquality(this.mTop.mSolverVariable, this.mParent.mBottom.mSolverVariable, 0, 5);
                }
            }
            else if (mBarrierType6 == 3) {
                linearSystem.addEquality(this.mTop.mSolverVariable, this.mBottom.mSolverVariable, 0, 6);
                if (!b2) {
                    linearSystem.addEquality(this.mTop.mSolverVariable, this.mParent.mTop.mSolverVariable, 0, 5);
                }
            }
        }
    }
    
    @Override
    public boolean allowedInBarrier() {
        return true;
    }
    
    public boolean allowsGoneWidget() {
        return this.mAllowsGoneWidget;
    }
    
    @Override
    public void analyze(int i) {
        if (this.mParent == null) {
            return;
        }
        if (!((ConstraintWidgetContainer)this.mParent).optimizeFor(2)) {
            return;
        }
        i = this.mBarrierType;
        ResolutionAnchor resolutionAnchor;
        if (i != 0) {
            if (i != 1) {
                if (i != 2) {
                    if (i != 3) {
                        return;
                    }
                    resolutionAnchor = this.mBottom.getResolutionNode();
                }
                else {
                    resolutionAnchor = this.mTop.getResolutionNode();
                }
            }
            else {
                resolutionAnchor = this.mRight.getResolutionNode();
            }
        }
        else {
            resolutionAnchor = this.mLeft.getResolutionNode();
        }
        resolutionAnchor.setType(5);
        i = this.mBarrierType;
        if (i != 0 && i != 1) {
            this.mLeft.getResolutionNode().resolve(null, 0.0f);
            this.mRight.getResolutionNode().resolve(null, 0.0f);
        }
        else {
            this.mTop.getResolutionNode().resolve(null, 0.0f);
            this.mBottom.getResolutionNode().resolve(null, 0.0f);
        }
        this.mNodes.clear();
        ConstraintWidget constraintWidget;
        ResolutionAnchor e;
        int mBarrierType;
        for (i = 0; i < this.mWidgetsCount; ++i) {
            constraintWidget = this.mWidgets[i];
            if (this.mAllowsGoneWidget || constraintWidget.allowedInBarrier()) {
                e = null;
                mBarrierType = this.mBarrierType;
                if (mBarrierType != 0) {
                    if (mBarrierType != 1) {
                        if (mBarrierType != 2) {
                            if (mBarrierType == 3) {
                                e = constraintWidget.mBottom.getResolutionNode();
                            }
                        }
                        else {
                            e = constraintWidget.mTop.getResolutionNode();
                        }
                    }
                    else {
                        e = constraintWidget.mRight.getResolutionNode();
                    }
                }
                else {
                    e = constraintWidget.mLeft.getResolutionNode();
                }
                if (e != null) {
                    this.mNodes.add(e);
                    e.addDependent(resolutionAnchor);
                }
            }
        }
    }
    
    @Override
    public void resetResolutionNodes() {
        super.resetResolutionNodes();
        this.mNodes.clear();
    }
    
    @Override
    public void resolve() {
        float n = 0.0f;
        final int mBarrierType = this.mBarrierType;
        ResolutionAnchor resolutionAnchor;
        if (mBarrierType != 0) {
            if (mBarrierType != 1) {
                if (mBarrierType != 2) {
                    if (mBarrierType != 3) {
                        return;
                    }
                    resolutionAnchor = this.mBottom.getResolutionNode();
                }
                else {
                    resolutionAnchor = this.mTop.getResolutionNode();
                    n = Float.MAX_VALUE;
                }
            }
            else {
                resolutionAnchor = this.mRight.getResolutionNode();
            }
        }
        else {
            resolutionAnchor = this.mLeft.getResolutionNode();
            n = Float.MAX_VALUE;
        }
        final int size = this.mNodes.size();
        ResolutionAnchor resolvedTarget = null;
        int i = 0;
        float resolvedOffset = n;
        while (i < size) {
            final ResolutionAnchor resolutionAnchor2 = this.mNodes.get(i);
            if (resolutionAnchor2.state != 1) {
                return;
            }
            final int mBarrierType2 = this.mBarrierType;
            float n2;
            if (mBarrierType2 != 0 && mBarrierType2 != 2) {
                n2 = resolvedOffset;
                if (resolutionAnchor2.resolvedOffset > resolvedOffset) {
                    n2 = resolutionAnchor2.resolvedOffset;
                    resolvedTarget = resolutionAnchor2.resolvedTarget;
                }
            }
            else {
                n2 = resolvedOffset;
                if (resolutionAnchor2.resolvedOffset < resolvedOffset) {
                    n2 = resolutionAnchor2.resolvedOffset;
                    resolvedTarget = resolutionAnchor2.resolvedTarget;
                }
            }
            ++i;
            resolvedOffset = n2;
        }
        if (LinearSystem.getMetrics() != null) {
            final Metrics metrics = LinearSystem.getMetrics();
            ++metrics.barrierConnectionResolved;
        }
        resolutionAnchor.resolvedTarget = resolvedTarget;
        resolutionAnchor.resolvedOffset = resolvedOffset;
        resolutionAnchor.didResolve();
        final int mBarrierType3 = this.mBarrierType;
        if (mBarrierType3 != 0) {
            if (mBarrierType3 != 1) {
                if (mBarrierType3 != 2) {
                    if (mBarrierType3 != 3) {
                        return;
                    }
                    this.mTop.getResolutionNode().resolve(resolvedTarget, resolvedOffset);
                }
                else {
                    this.mBottom.getResolutionNode().resolve(resolvedTarget, resolvedOffset);
                }
            }
            else {
                this.mLeft.getResolutionNode().resolve(resolvedTarget, resolvedOffset);
            }
        }
        else {
            this.mRight.getResolutionNode().resolve(resolvedTarget, resolvedOffset);
        }
    }
    
    public void setAllowsGoneWidget(final boolean mAllowsGoneWidget) {
        this.mAllowsGoneWidget = mAllowsGoneWidget;
    }
    
    public void setBarrierType(final int mBarrierType) {
        this.mBarrierType = mBarrierType;
    }
}
