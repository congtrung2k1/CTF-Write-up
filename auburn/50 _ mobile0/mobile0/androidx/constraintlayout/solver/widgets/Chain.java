// 
// Decompiled by Procyon v0.5.36
// 

package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.ArrayRow;
import androidx.constraintlayout.solver.SolverVariable;
import java.util.ArrayList;
import androidx.constraintlayout.solver.LinearSystem;

class Chain
{
    private static final boolean DEBUG = false;
    
    static void applyChainConstraints(final ConstraintWidgetContainer constraintWidgetContainer, final LinearSystem linearSystem, final int n) {
        int n2;
        ChainHead[] array;
        if (n == 0) {
            n2 = 0;
            final int n3 = constraintWidgetContainer.mHorizontalChainsSize;
            array = constraintWidgetContainer.mHorizontalChainsArray;
        }
        else {
            n2 = 2;
            final int n3 = constraintWidgetContainer.mVerticalChainsSize;
            array = constraintWidgetContainer.mVerticalChainsArray;
        }
        for (final ChainHead chainHead : array) {
            chainHead.define();
            if (constraintWidgetContainer.optimizeFor(4)) {
                if (!Optimizer.applyChainOptimized(constraintWidgetContainer, linearSystem, n, n2, chainHead)) {
                    applyChainConstraints(constraintWidgetContainer, linearSystem, n, n2, chainHead);
                }
            }
            else {
                applyChainConstraints(constraintWidgetContainer, linearSystem, n, n2, chainHead);
            }
        }
    }
    
    static void applyChainConstraints(final ConstraintWidgetContainer constraintWidgetContainer, final LinearSystem linearSystem, int n, int margin, final ChainHead chainHead) {
        final ConstraintWidget mFirst = chainHead.mFirst;
        final ConstraintWidget mLast = chainHead.mLast;
        final ConstraintWidget mFirstVisibleWidget = chainHead.mFirstVisibleWidget;
        final ConstraintWidget mLastVisibleWidget = chainHead.mLastVisibleWidget;
        final ConstraintWidget mHead = chainHead.mHead;
        final float mTotalWeight = chainHead.mTotalWeight;
        final ConstraintWidget mFirstMatchConstraintWidget = chainHead.mFirstMatchConstraintWidget;
        final ConstraintWidget mLastMatchConstraintWidget = chainHead.mLastMatchConstraintWidget;
        final boolean b = constraintWidgetContainer.mListDimensionBehaviors[n] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        int n2;
        ConstraintWidget constraintWidget;
        int n4;
        int i;
        int n5;
        if (n == 0) {
            final boolean b2 = mHead.mHorizontalChainStyle == 0;
            final int mHorizontalChainStyle = mHead.mHorizontalChainStyle;
            n2 = (b2 ? 1 : 0);
            final boolean b3 = mHorizontalChainStyle == 1;
            final boolean b4 = mHead.mHorizontalChainStyle == 2;
            final int n3 = 0;
            constraintWidget = mFirst;
            n4 = (b3 ? 1 : 0);
            i = n3;
            n5 = (b4 ? 1 : 0);
        }
        else {
            final boolean b5 = mHead.mVerticalChainStyle == 0;
            final int mVerticalChainStyle = mHead.mVerticalChainStyle;
            n2 = (b5 ? 1 : 0);
            final boolean b6 = mVerticalChainStyle == 1;
            final boolean b7 = mHead.mVerticalChainStyle == 2;
            final int n6 = 0;
            constraintWidget = mFirst;
            n4 = (b6 ? 1 : 0);
            n5 = (b7 ? 1 : 0);
            i = n6;
        }
        while (i == 0) {
            final ConstraintAnchor constraintAnchor = constraintWidget.mListAnchors[margin];
            int n7 = 4;
            if (b || n5 != 0) {
                n7 = 1;
            }
            int margin2 = constraintAnchor.getMargin();
            if (constraintAnchor.mTarget != null && constraintWidget != mFirst) {
                margin2 += constraintAnchor.mTarget.getMargin();
            }
            if (n5 != 0 && constraintWidget != mFirst && constraintWidget != mFirstVisibleWidget) {
                n7 = 6;
            }
            else if (n2 != 0 && b) {
                n7 = 4;
            }
            if (constraintAnchor.mTarget != null) {
                if (constraintWidget == mFirstVisibleWidget) {
                    linearSystem.addGreaterThan(constraintAnchor.mSolverVariable, constraintAnchor.mTarget.mSolverVariable, margin2, 5);
                }
                else {
                    linearSystem.addGreaterThan(constraintAnchor.mSolverVariable, constraintAnchor.mTarget.mSolverVariable, margin2, 6);
                }
                linearSystem.addEquality(constraintAnchor.mSolverVariable, constraintAnchor.mTarget.mSolverVariable, margin2, n7);
            }
            if (b) {
                if (constraintWidget.getVisibility() != 8 && constraintWidget.mListDimensionBehaviors[n] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    linearSystem.addGreaterThan(constraintWidget.mListAnchors[margin + 1].mSolverVariable, constraintWidget.mListAnchors[margin].mSolverVariable, 0, 5);
                }
                linearSystem.addGreaterThan(constraintWidget.mListAnchors[margin].mSolverVariable, constraintWidgetContainer.mListAnchors[margin].mSolverVariable, 0, 6);
            }
            final ConstraintAnchor mTarget = constraintWidget.mListAnchors[margin + 1].mTarget;
            ConstraintWidget mOwner;
            if (mTarget != null) {
                mOwner = mTarget.mOwner;
                if (mOwner.mListAnchors[margin].mTarget == null || mOwner.mListAnchors[margin].mTarget.mOwner != constraintWidget) {
                    mOwner = null;
                }
            }
            else {
                mOwner = null;
            }
            if (mOwner != null) {
                constraintWidget = mOwner;
            }
            else {
                i = 1;
            }
        }
        if (mLastVisibleWidget != null && mLast.mListAnchors[margin + 1].mTarget != null) {
            final ConstraintAnchor constraintAnchor2 = mLastVisibleWidget.mListAnchors[margin + 1];
            linearSystem.addLowerThan(constraintAnchor2.mSolverVariable, mLast.mListAnchors[margin + 1].mTarget.mSolverVariable, -constraintAnchor2.getMargin(), 5);
        }
        if (b) {
            linearSystem.addGreaterThan(constraintWidgetContainer.mListAnchors[margin + 1].mSolverVariable, mLast.mListAnchors[margin + 1].mSolverVariable, mLast.mListAnchors[margin + 1].getMargin(), 6);
        }
        final ArrayList<ConstraintWidget> mWeightedMatchConstraintsWidgets = chainHead.mWeightedMatchConstraintsWidgets;
        if (mWeightedMatchConstraintsWidgets != null) {
            final int size = mWeightedMatchConstraintsWidgets.size();
            if (size > 1) {
                final ConstraintWidget constraintWidget2 = null;
                float n8 = 0.0f;
                float n9;
                if (chainHead.mHasUndefinedWeights && !chainHead.mHasComplexMatchWeights) {
                    n9 = (float)chainHead.mWidgetsMatchCount;
                }
                else {
                    n9 = mTotalWeight;
                }
                int j = 0;
                ConstraintWidget constraintWidget3 = constraintWidget2;
                while (j < size) {
                    final ConstraintWidget constraintWidget4 = mWeightedMatchConstraintsWidgets.get(j);
                    float n10 = constraintWidget4.mWeight[n];
                    Label_1021: {
                        if (n10 < 0.0f) {
                            if (chainHead.mHasComplexMatchWeights) {
                                linearSystem.addEquality(constraintWidget4.mListAnchors[margin + 1].mSolverVariable, constraintWidget4.mListAnchors[margin].mSolverVariable, 0, 4);
                                n10 = n8;
                                break Label_1021;
                            }
                            n10 = 1.0f;
                        }
                        if (n10 == 0.0f) {
                            linearSystem.addEquality(constraintWidget4.mListAnchors[margin + 1].mSolverVariable, constraintWidget4.mListAnchors[margin].mSolverVariable, 0, 6);
                            n10 = n8;
                        }
                        else {
                            if (constraintWidget3 != null) {
                                final SolverVariable mSolverVariable = constraintWidget3.mListAnchors[margin].mSolverVariable;
                                final SolverVariable mSolverVariable2 = constraintWidget3.mListAnchors[margin + 1].mSolverVariable;
                                final SolverVariable mSolverVariable3 = constraintWidget4.mListAnchors[margin].mSolverVariable;
                                final SolverVariable mSolverVariable4 = constraintWidget4.mListAnchors[margin + 1].mSolverVariable;
                                final ArrayRow row = linearSystem.createRow();
                                row.createRowEqualMatchDimensions(n8, n9, n10, mSolverVariable, mSolverVariable2, mSolverVariable3, mSolverVariable4);
                                linearSystem.addConstraint(row);
                            }
                            constraintWidget3 = constraintWidget4;
                        }
                    }
                    ++j;
                    n8 = n10;
                }
            }
        }
        if (mFirstVisibleWidget != null && (mFirstVisibleWidget == mLastVisibleWidget || n5 != 0)) {
            ConstraintAnchor constraintAnchor3 = mFirst.mListAnchors[margin];
            final ConstraintAnchor constraintAnchor4 = mLast.mListAnchors[margin + 1];
            SolverVariable mSolverVariable5;
            if (mFirst.mListAnchors[margin].mTarget != null) {
                mSolverVariable5 = mFirst.mListAnchors[margin].mTarget.mSolverVariable;
            }
            else {
                mSolverVariable5 = null;
            }
            SolverVariable mSolverVariable6;
            if (mLast.mListAnchors[margin + 1].mTarget != null) {
                mSolverVariable6 = mLast.mListAnchors[margin + 1].mTarget.mSolverVariable;
            }
            else {
                mSolverVariable6 = null;
            }
            ConstraintAnchor constraintAnchor6;
            if (mFirstVisibleWidget == mLastVisibleWidget) {
                final ConstraintAnchor constraintAnchor5 = mFirstVisibleWidget.mListAnchors[margin];
                constraintAnchor6 = mFirstVisibleWidget.mListAnchors[margin + 1];
                constraintAnchor3 = constraintAnchor5;
            }
            else {
                constraintAnchor6 = constraintAnchor4;
            }
            if (mSolverVariable5 != null && mSolverVariable6 != null) {
                float n11;
                if (n == 0) {
                    n11 = mHead.mHorizontalBiasPercent;
                }
                else {
                    n11 = mHead.mVerticalBiasPercent;
                }
                n = constraintAnchor3.getMargin();
                linearSystem.addCentering(constraintAnchor3.mSolverVariable, mSolverVariable5, n, n11, mSolverVariable6, constraintAnchor6.mSolverVariable, constraintAnchor6.getMargin(), 5);
            }
        }
        else if (n2 != 0 && mFirstVisibleWidget != null) {
            final boolean b8 = chainHead.mWidgetsMatchCount > 0 && chainHead.mWidgetsCount == chainHead.mWidgetsMatchCount;
            ConstraintWidget constraintWidget5 = mFirstVisibleWidget;
            ConstraintWidget constraintWidget6 = mFirstVisibleWidget;
            while (constraintWidget5 != null) {
                ConstraintWidget constraintWidget7;
                for (constraintWidget7 = constraintWidget5.mNextChainWidget[n]; constraintWidget7 != null && constraintWidget7.getVisibility() == 8; constraintWidget7 = constraintWidget7.mNextChainWidget[n]) {}
                if (constraintWidget7 != null || constraintWidget5 == mLastVisibleWidget) {
                    final ConstraintAnchor constraintAnchor7 = constraintWidget5.mListAnchors[margin];
                    final SolverVariable mSolverVariable7 = constraintAnchor7.mSolverVariable;
                    SolverVariable solverVariable;
                    if (constraintAnchor7.mTarget != null) {
                        solverVariable = constraintAnchor7.mTarget.mSolverVariable;
                    }
                    else {
                        solverVariable = null;
                    }
                    if (constraintWidget6 != constraintWidget5) {
                        solverVariable = constraintWidget6.mListAnchors[margin + 1].mSolverVariable;
                    }
                    else if (constraintWidget5 == mFirstVisibleWidget && constraintWidget6 == constraintWidget5) {
                        if (mFirst.mListAnchors[margin].mTarget != null) {
                            solverVariable = mFirst.mListAnchors[margin].mTarget.mSolverVariable;
                        }
                        else {
                            solverVariable = null;
                        }
                    }
                    SolverVariable mSolverVariable8 = null;
                    final int margin3 = constraintAnchor7.getMargin();
                    final int margin4 = constraintWidget5.mListAnchors[margin + 1].getMargin();
                    ConstraintAnchor mTarget2;
                    SolverVariable solverVariable2;
                    SolverVariable solverVariable3;
                    if (constraintWidget7 != null) {
                        mTarget2 = constraintWidget7.mListAnchors[margin];
                        final SolverVariable mSolverVariable9 = mTarget2.mSolverVariable;
                        final SolverVariable mSolverVariable10 = constraintWidget5.mListAnchors[margin + 1].mSolverVariable;
                        solverVariable2 = mSolverVariable9;
                        solverVariable3 = mSolverVariable10;
                    }
                    else {
                        mTarget2 = mLast.mListAnchors[margin + 1].mTarget;
                        if (mTarget2 != null) {
                            mSolverVariable8 = mTarget2.mSolverVariable;
                        }
                        final SolverVariable mSolverVariable11 = constraintWidget5.mListAnchors[margin + 1].mSolverVariable;
                        solverVariable2 = mSolverVariable8;
                        solverVariable3 = mSolverVariable11;
                    }
                    int margin5 = margin4;
                    if (mTarget2 != null) {
                        margin5 = margin4 + mTarget2.getMargin();
                    }
                    int margin6 = margin3;
                    if (constraintWidget6 != null) {
                        margin6 = margin3 + constraintWidget6.mListAnchors[margin + 1].getMargin();
                    }
                    if (mSolverVariable7 != null && solverVariable != null && solverVariable2 != null && solverVariable3 != null) {
                        if (constraintWidget5 == mFirstVisibleWidget) {
                            margin6 = mFirstVisibleWidget.mListAnchors[margin].getMargin();
                        }
                        if (constraintWidget5 == mLastVisibleWidget) {
                            margin5 = mLastVisibleWidget.mListAnchors[margin + 1].getMargin();
                        }
                        int n12;
                        if (b8) {
                            n12 = 6;
                        }
                        else {
                            n12 = 4;
                        }
                        linearSystem.addCentering(mSolverVariable7, solverVariable, margin6, 0.5f, solverVariable2, solverVariable3, margin5, n12);
                    }
                }
                if (constraintWidget5.getVisibility() != 8) {
                    constraintWidget6 = constraintWidget5;
                }
                constraintWidget5 = constraintWidget7;
            }
        }
        else if (n4 != 0 && mFirstVisibleWidget != null) {
            final boolean b9 = chainHead.mWidgetsMatchCount > 0 && chainHead.mWidgetsCount == chainHead.mWidgetsMatchCount;
            ConstraintWidget constraintWidget8 = mFirstVisibleWidget;
            ConstraintWidget constraintWidget9 = mFirstVisibleWidget;
            while (constraintWidget8 != null) {
                ConstraintWidget constraintWidget10;
                for (constraintWidget10 = constraintWidget8.mNextChainWidget[n]; constraintWidget10 != null && constraintWidget10.getVisibility() == 8; constraintWidget10 = constraintWidget10.mNextChainWidget[n]) {}
                if (constraintWidget8 != mFirstVisibleWidget && constraintWidget8 != mLastVisibleWidget && constraintWidget10 != null) {
                    if (constraintWidget10 == mLastVisibleWidget) {
                        constraintWidget10 = null;
                    }
                    final ConstraintAnchor constraintAnchor8 = constraintWidget8.mListAnchors[margin];
                    final SolverVariable mSolverVariable12 = constraintAnchor8.mSolverVariable;
                    if (constraintAnchor8.mTarget != null) {
                        final SolverVariable mSolverVariable13 = constraintAnchor8.mTarget.mSolverVariable;
                    }
                    final SolverVariable mSolverVariable14 = constraintWidget9.mListAnchors[margin + 1].mSolverVariable;
                    SolverVariable mSolverVariable15 = null;
                    final int margin7 = constraintAnchor8.getMargin();
                    final int margin8 = constraintWidget8.mListAnchors[margin + 1].getMargin();
                    ConstraintAnchor constraintAnchor9;
                    SolverVariable mSolverVariable18;
                    if (constraintWidget10 != null) {
                        constraintAnchor9 = constraintWidget10.mListAnchors[margin];
                        final SolverVariable mSolverVariable16 = constraintAnchor9.mSolverVariable;
                        SolverVariable mSolverVariable17;
                        if (constraintAnchor9.mTarget != null) {
                            mSolverVariable17 = constraintAnchor9.mTarget.mSolverVariable;
                        }
                        else {
                            mSolverVariable17 = null;
                        }
                        mSolverVariable18 = mSolverVariable17;
                        mSolverVariable15 = mSolverVariable16;
                    }
                    else {
                        final ConstraintAnchor mTarget3 = constraintWidget8.mListAnchors[margin + 1].mTarget;
                        if (mTarget3 != null) {
                            mSolverVariable15 = mTarget3.mSolverVariable;
                        }
                        mSolverVariable18 = constraintWidget8.mListAnchors[margin + 1].mSolverVariable;
                        constraintAnchor9 = mTarget3;
                    }
                    int n13 = margin8;
                    if (constraintAnchor9 != null) {
                        n13 = margin8 + constraintAnchor9.getMargin();
                    }
                    int n14 = margin7;
                    if (constraintWidget9 != null) {
                        n14 = margin7 + constraintWidget9.mListAnchors[margin + 1].getMargin();
                    }
                    int n15;
                    if (b9) {
                        n15 = 6;
                    }
                    else {
                        n15 = 4;
                    }
                    if (mSolverVariable12 != null && mSolverVariable14 != null && mSolverVariable15 != null && mSolverVariable18 != null) {
                        linearSystem.addCentering(mSolverVariable12, mSolverVariable14, n14, 0.5f, mSolverVariable15, mSolverVariable18, n13, n15);
                    }
                }
                if (constraintWidget8.getVisibility() != 8) {
                    constraintWidget9 = constraintWidget8;
                }
                constraintWidget8 = constraintWidget10;
            }
            final ConstraintAnchor constraintAnchor10 = mFirstVisibleWidget.mListAnchors[margin];
            final ConstraintAnchor mTarget4 = mFirst.mListAnchors[margin].mTarget;
            final ConstraintAnchor constraintAnchor11 = mLastVisibleWidget.mListAnchors[margin + 1];
            final ConstraintAnchor mTarget5 = mLast.mListAnchors[margin + 1].mTarget;
            if (mTarget4 != null) {
                if (mFirstVisibleWidget != mLastVisibleWidget) {
                    linearSystem.addEquality(constraintAnchor10.mSolverVariable, mTarget4.mSolverVariable, constraintAnchor10.getMargin(), 5);
                }
                else if (mTarget5 != null) {
                    linearSystem.addCentering(constraintAnchor10.mSolverVariable, mTarget4.mSolverVariable, constraintAnchor10.getMargin(), 0.5f, constraintAnchor11.mSolverVariable, mTarget5.mSolverVariable, constraintAnchor11.getMargin(), 5);
                }
            }
            if (mTarget5 != null && mFirstVisibleWidget != mLastVisibleWidget) {
                linearSystem.addEquality(constraintAnchor11.mSolverVariable, mTarget5.mSolverVariable, -constraintAnchor11.getMargin(), 5);
            }
        }
        if ((n2 != 0 || n4 != 0) && mFirstVisibleWidget != null) {
            final ConstraintAnchor constraintAnchor12 = mFirstVisibleWidget.mListAnchors[margin];
            ConstraintAnchor constraintAnchor13 = mLastVisibleWidget.mListAnchors[margin + 1];
            SolverVariable mSolverVariable19;
            if (constraintAnchor12.mTarget != null) {
                mSolverVariable19 = constraintAnchor12.mTarget.mSolverVariable;
            }
            else {
                mSolverVariable19 = null;
            }
            SolverVariable solverVariable4;
            if (constraintAnchor13.mTarget != null) {
                solverVariable4 = constraintAnchor13.mTarget.mSolverVariable;
            }
            else {
                solverVariable4 = null;
            }
            if (mLast != mLastVisibleWidget) {
                final ConstraintAnchor constraintAnchor14 = mLast.mListAnchors[margin + 1];
                if (constraintAnchor14.mTarget != null) {
                    solverVariable4 = constraintAnchor14.mTarget.mSolverVariable;
                }
                else {
                    solverVariable4 = null;
                }
            }
            ConstraintAnchor constraintAnchor17;
            if (mFirstVisibleWidget == mLastVisibleWidget) {
                final ConstraintAnchor constraintAnchor15 = mFirstVisibleWidget.mListAnchors[margin];
                final ConstraintAnchor constraintAnchor16 = mFirstVisibleWidget.mListAnchors[margin + 1];
                constraintAnchor17 = constraintAnchor15;
                constraintAnchor13 = constraintAnchor16;
            }
            else {
                constraintAnchor17 = constraintAnchor12;
            }
            if (mSolverVariable19 != null && solverVariable4 != null) {
                n = constraintAnchor17.getMargin();
                ConstraintWidget constraintWidget11;
                if ((constraintWidget11 = mLastVisibleWidget) == null) {
                    constraintWidget11 = mLast;
                }
                margin = constraintWidget11.mListAnchors[margin + 1].getMargin();
                linearSystem.addCentering(constraintAnchor17.mSolverVariable, mSolverVariable19, n, 0.5f, solverVariable4, constraintAnchor13.mSolverVariable, margin, 5);
            }
        }
    }
}
