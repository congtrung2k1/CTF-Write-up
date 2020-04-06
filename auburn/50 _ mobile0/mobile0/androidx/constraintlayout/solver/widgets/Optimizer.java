// 
// Decompiled by Procyon v0.5.36
// 

package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.Metrics;
import androidx.constraintlayout.solver.LinearSystem;

public class Optimizer
{
    static final int FLAG_CHAIN_DANGLING = 1;
    static final int FLAG_RECOMPUTE_BOUNDS = 2;
    static final int FLAG_USE_OPTIMIZE = 0;
    public static final int OPTIMIZATION_BARRIER = 2;
    public static final int OPTIMIZATION_CHAIN = 4;
    public static final int OPTIMIZATION_DIMENSIONS = 8;
    public static final int OPTIMIZATION_DIRECT = 1;
    public static final int OPTIMIZATION_GROUPS = 32;
    public static final int OPTIMIZATION_NONE = 0;
    public static final int OPTIMIZATION_RATIO = 16;
    public static final int OPTIMIZATION_STANDARD = 7;
    static boolean[] flags;
    
    static {
        Optimizer.flags = new boolean[3];
    }
    
    static void analyze(int n, final ConstraintWidget constraintWidget) {
        constraintWidget.updateResolutionNodes();
        final ResolutionAnchor resolutionNode = constraintWidget.mLeft.getResolutionNode();
        final ResolutionAnchor resolutionNode2 = constraintWidget.mTop.getResolutionNode();
        final ResolutionAnchor resolutionNode3 = constraintWidget.mRight.getResolutionNode();
        final ResolutionAnchor resolutionNode4 = constraintWidget.mBottom.getResolutionNode();
        if ((n & 0x8) == 0x8) {
            n = 1;
        }
        else {
            n = 0;
        }
        final boolean b = constraintWidget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && optimizableMatchConstraint(constraintWidget, 0);
        if (resolutionNode.type != 4 && resolutionNode3.type != 4) {
            if (constraintWidget.mListDimensionBehaviors[0] != ConstraintWidget.DimensionBehaviour.FIXED && (!b || constraintWidget.getVisibility() != 8)) {
                if (b) {
                    final int width = constraintWidget.getWidth();
                    resolutionNode.setType(1);
                    resolutionNode3.setType(1);
                    if (constraintWidget.mLeft.mTarget == null && constraintWidget.mRight.mTarget == null) {
                        if (n != 0) {
                            resolutionNode3.dependsOn(resolutionNode, 1, constraintWidget.getResolutionWidth());
                        }
                        else {
                            resolutionNode3.dependsOn(resolutionNode, width);
                        }
                    }
                    else if (constraintWidget.mLeft.mTarget != null && constraintWidget.mRight.mTarget == null) {
                        if (n != 0) {
                            resolutionNode3.dependsOn(resolutionNode, 1, constraintWidget.getResolutionWidth());
                        }
                        else {
                            resolutionNode3.dependsOn(resolutionNode, width);
                        }
                    }
                    else if (constraintWidget.mLeft.mTarget == null && constraintWidget.mRight.mTarget != null) {
                        if (n != 0) {
                            resolutionNode.dependsOn(resolutionNode3, -1, constraintWidget.getResolutionWidth());
                        }
                        else {
                            resolutionNode.dependsOn(resolutionNode3, -width);
                        }
                    }
                    else if (constraintWidget.mLeft.mTarget != null && constraintWidget.mRight.mTarget != null) {
                        if (n != 0) {
                            constraintWidget.getResolutionWidth().addDependent(resolutionNode);
                            constraintWidget.getResolutionWidth().addDependent(resolutionNode3);
                        }
                        if (constraintWidget.mDimensionRatio == 0.0f) {
                            resolutionNode.setType(3);
                            resolutionNode3.setType(3);
                            resolutionNode.setOpposite(resolutionNode3, 0.0f);
                            resolutionNode3.setOpposite(resolutionNode, 0.0f);
                        }
                        else {
                            resolutionNode.setType(2);
                            resolutionNode3.setType(2);
                            resolutionNode.setOpposite(resolutionNode3, (float)(-width));
                            resolutionNode3.setOpposite(resolutionNode, (float)width);
                            constraintWidget.setWidth(width);
                        }
                    }
                }
            }
            else if (constraintWidget.mLeft.mTarget == null && constraintWidget.mRight.mTarget == null) {
                resolutionNode.setType(1);
                resolutionNode3.setType(1);
                if (n != 0) {
                    resolutionNode3.dependsOn(resolutionNode, 1, constraintWidget.getResolutionWidth());
                }
                else {
                    resolutionNode3.dependsOn(resolutionNode, constraintWidget.getWidth());
                }
            }
            else if (constraintWidget.mLeft.mTarget != null && constraintWidget.mRight.mTarget == null) {
                resolutionNode.setType(1);
                resolutionNode3.setType(1);
                if (n != 0) {
                    resolutionNode3.dependsOn(resolutionNode, 1, constraintWidget.getResolutionWidth());
                }
                else {
                    resolutionNode3.dependsOn(resolutionNode, constraintWidget.getWidth());
                }
            }
            else if (constraintWidget.mLeft.mTarget == null && constraintWidget.mRight.mTarget != null) {
                resolutionNode.setType(1);
                resolutionNode3.setType(1);
                resolutionNode.dependsOn(resolutionNode3, -constraintWidget.getWidth());
                if (n != 0) {
                    resolutionNode.dependsOn(resolutionNode3, -1, constraintWidget.getResolutionWidth());
                }
                else {
                    resolutionNode.dependsOn(resolutionNode3, -constraintWidget.getWidth());
                }
            }
            else if (constraintWidget.mLeft.mTarget != null && constraintWidget.mRight.mTarget != null) {
                resolutionNode.setType(2);
                resolutionNode3.setType(2);
                if (n != 0) {
                    constraintWidget.getResolutionWidth().addDependent(resolutionNode);
                    constraintWidget.getResolutionWidth().addDependent(resolutionNode3);
                    resolutionNode.setOpposite(resolutionNode3, -1, constraintWidget.getResolutionWidth());
                    resolutionNode3.setOpposite(resolutionNode, 1, constraintWidget.getResolutionWidth());
                }
                else {
                    resolutionNode.setOpposite(resolutionNode3, (float)(-constraintWidget.getWidth()));
                    resolutionNode3.setOpposite(resolutionNode, (float)constraintWidget.getWidth());
                }
            }
        }
        final boolean b2 = constraintWidget.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && optimizableMatchConstraint(constraintWidget, 1);
        if (resolutionNode2.type != 4 && resolutionNode4.type != 4) {
            if (constraintWidget.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.FIXED && (!b2 || constraintWidget.getVisibility() != 8)) {
                if (b2) {
                    final int height = constraintWidget.getHeight();
                    resolutionNode2.setType(1);
                    resolutionNode4.setType(1);
                    if (constraintWidget.mTop.mTarget == null && constraintWidget.mBottom.mTarget == null) {
                        if (n != 0) {
                            resolutionNode4.dependsOn(resolutionNode2, 1, constraintWidget.getResolutionHeight());
                        }
                        else {
                            resolutionNode4.dependsOn(resolutionNode2, height);
                        }
                    }
                    else if (constraintWidget.mTop.mTarget != null && constraintWidget.mBottom.mTarget == null) {
                        if (n != 0) {
                            resolutionNode4.dependsOn(resolutionNode2, 1, constraintWidget.getResolutionHeight());
                        }
                        else {
                            resolutionNode4.dependsOn(resolutionNode2, height);
                        }
                    }
                    else if (constraintWidget.mTop.mTarget == null && constraintWidget.mBottom.mTarget != null) {
                        if (n != 0) {
                            resolutionNode2.dependsOn(resolutionNode4, -1, constraintWidget.getResolutionHeight());
                        }
                        else {
                            resolutionNode2.dependsOn(resolutionNode4, -height);
                        }
                    }
                    else if (constraintWidget.mTop.mTarget != null && constraintWidget.mBottom.mTarget != null) {
                        if (n != 0) {
                            constraintWidget.getResolutionHeight().addDependent(resolutionNode2);
                            constraintWidget.getResolutionWidth().addDependent(resolutionNode4);
                        }
                        if (constraintWidget.mDimensionRatio == 0.0f) {
                            resolutionNode2.setType(3);
                            resolutionNode4.setType(3);
                            resolutionNode2.setOpposite(resolutionNode4, 0.0f);
                            resolutionNode4.setOpposite(resolutionNode2, 0.0f);
                        }
                        else {
                            resolutionNode2.setType(2);
                            resolutionNode4.setType(2);
                            resolutionNode2.setOpposite(resolutionNode4, (float)(-height));
                            resolutionNode4.setOpposite(resolutionNode2, (float)height);
                            constraintWidget.setHeight(height);
                            if (constraintWidget.mBaselineDistance > 0) {
                                constraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionNode2, constraintWidget.mBaselineDistance);
                            }
                        }
                    }
                }
            }
            else if (constraintWidget.mTop.mTarget == null && constraintWidget.mBottom.mTarget == null) {
                resolutionNode2.setType(1);
                resolutionNode4.setType(1);
                if (n != 0) {
                    resolutionNode4.dependsOn(resolutionNode2, 1, constraintWidget.getResolutionHeight());
                }
                else {
                    resolutionNode4.dependsOn(resolutionNode2, constraintWidget.getHeight());
                }
                if (constraintWidget.mBaseline.mTarget != null) {
                    constraintWidget.mBaseline.getResolutionNode().setType(1);
                    resolutionNode2.dependsOn(1, constraintWidget.mBaseline.getResolutionNode(), -constraintWidget.mBaselineDistance);
                }
            }
            else if (constraintWidget.mTop.mTarget != null && constraintWidget.mBottom.mTarget == null) {
                resolutionNode2.setType(1);
                resolutionNode4.setType(1);
                if (n != 0) {
                    resolutionNode4.dependsOn(resolutionNode2, 1, constraintWidget.getResolutionHeight());
                }
                else {
                    resolutionNode4.dependsOn(resolutionNode2, constraintWidget.getHeight());
                }
                if (constraintWidget.mBaselineDistance > 0) {
                    constraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionNode2, constraintWidget.mBaselineDistance);
                }
            }
            else if (constraintWidget.mTop.mTarget == null && constraintWidget.mBottom.mTarget != null) {
                resolutionNode2.setType(1);
                resolutionNode4.setType(1);
                if (n != 0) {
                    resolutionNode2.dependsOn(resolutionNode4, -1, constraintWidget.getResolutionHeight());
                }
                else {
                    resolutionNode2.dependsOn(resolutionNode4, -constraintWidget.getHeight());
                }
                if (constraintWidget.mBaselineDistance > 0) {
                    constraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionNode2, constraintWidget.mBaselineDistance);
                }
            }
            else if (constraintWidget.mTop.mTarget != null && constraintWidget.mBottom.mTarget != null) {
                resolutionNode2.setType(2);
                resolutionNode4.setType(2);
                if (n != 0) {
                    resolutionNode2.setOpposite(resolutionNode4, -1, constraintWidget.getResolutionHeight());
                    resolutionNode4.setOpposite(resolutionNode2, 1, constraintWidget.getResolutionHeight());
                    constraintWidget.getResolutionHeight().addDependent(resolutionNode2);
                    constraintWidget.getResolutionWidth().addDependent(resolutionNode4);
                }
                else {
                    resolutionNode2.setOpposite(resolutionNode4, (float)(-constraintWidget.getHeight()));
                    resolutionNode4.setOpposite(resolutionNode2, (float)constraintWidget.getHeight());
                }
                if (constraintWidget.mBaselineDistance > 0) {
                    constraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionNode2, constraintWidget.mBaselineDistance);
                }
            }
        }
    }
    
    static boolean applyChainOptimized(final ConstraintWidgetContainer constraintWidgetContainer, final LinearSystem linearSystem, final int n, final int n2, final ChainHead chainHead) {
        final ConstraintWidget mFirst = chainHead.mFirst;
        final ConstraintWidget mLast = chainHead.mLast;
        final ConstraintWidget mFirstVisibleWidget = chainHead.mFirstVisibleWidget;
        final ConstraintWidget mLastVisibleWidget = chainHead.mLastVisibleWidget;
        final ConstraintWidget mHead = chainHead.mHead;
        final float mTotalWeight = chainHead.mTotalWeight;
        final ConstraintWidget mFirstMatchConstraintWidget = chainHead.mFirstMatchConstraintWidget;
        final ConstraintWidget mLastMatchConstraintWidget = chainHead.mLastMatchConstraintWidget;
        if (constraintWidgetContainer.mListDimensionBehaviors[n] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {}
        int n3;
        int n4;
        int n5;
        if (n == 0) {
            final boolean b = mHead.mHorizontalChainStyle == 0;
            final boolean b2 = mHead.mHorizontalChainStyle == 1;
            final boolean b3 = mHead.mHorizontalChainStyle == 2;
            final boolean b4 = b;
            n3 = (b3 ? 1 : 0);
            n4 = (b2 ? 1 : 0);
            n5 = (b4 ? 1 : 0);
        }
        else {
            final boolean b5 = mHead.mVerticalChainStyle == 0;
            final boolean b6 = mHead.mVerticalChainStyle == 1;
            final boolean b7 = mHead.mVerticalChainStyle == 2;
            n4 = (b6 ? 1 : 0);
            n5 = (b5 ? 1 : 0);
            n3 = (b7 ? 1 : 0);
        }
        float n6 = 0.0f;
        float n7 = 0.0f;
        int n8 = 0;
        int i = 0;
        ConstraintWidget constraintWidget = mFirst;
        int n9 = 0;
        while (i == 0) {
            int n10 = n9;
            float n11 = n6;
            float n12 = n7;
            if (constraintWidget.getVisibility() != 8) {
                n10 = n9 + 1;
                float n13;
                if (n == 0) {
                    n13 = n6 + constraintWidget.getWidth();
                }
                else {
                    n13 = n6 + constraintWidget.getHeight();
                }
                float n14 = n13;
                if (constraintWidget != mFirstVisibleWidget) {
                    n14 = n13 + constraintWidget.mListAnchors[n2].getMargin();
                }
                n11 = n14;
                if (constraintWidget != mLastVisibleWidget) {
                    n11 = n14 + constraintWidget.mListAnchors[n2 + 1].getMargin();
                }
                n12 = n7 + constraintWidget.mListAnchors[n2].getMargin() + constraintWidget.mListAnchors[n2 + 1].getMargin();
            }
            final ConstraintAnchor constraintAnchor = constraintWidget.mListAnchors[n2];
            int n15 = n8;
            if (constraintWidget.getVisibility() != 8) {
                n15 = n8;
                if (constraintWidget.mListDimensionBehaviors[n] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    n15 = n8 + 1;
                    if (n == 0) {
                        if (constraintWidget.mMatchConstraintDefaultWidth != 0) {
                            return false;
                        }
                        if (constraintWidget.mMatchConstraintMinWidth != 0 || constraintWidget.mMatchConstraintMaxWidth != 0) {
                            return false;
                        }
                    }
                    else {
                        if (constraintWidget.mMatchConstraintDefaultHeight != 0) {
                            return false;
                        }
                        if (constraintWidget.mMatchConstraintMinHeight != 0 || constraintWidget.mMatchConstraintMaxHeight != 0) {
                            return false;
                        }
                    }
                    if (constraintWidget.mDimensionRatio != 0.0f) {
                        return false;
                    }
                }
            }
            final ConstraintAnchor mTarget = constraintWidget.mListAnchors[n2 + 1].mTarget;
            ConstraintWidget mOwner;
            if (mTarget != null) {
                mOwner = mTarget.mOwner;
                if (mOwner.mListAnchors[n2].mTarget == null || mOwner.mListAnchors[n2].mTarget.mOwner != constraintWidget) {
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
            n8 = n15;
            n9 = n10;
            n6 = n11;
            n7 = n12;
        }
        final ResolutionAnchor resolutionNode = mFirst.mListAnchors[n2].getResolutionNode();
        final ResolutionAnchor resolutionNode2 = mLast.mListAnchors[n2 + 1].getResolutionNode();
        if (resolutionNode.target == null || resolutionNode2.target == null) {
            return false;
        }
        if (resolutionNode.target.state != 1 || resolutionNode2.target.state != 1) {
            return false;
        }
        if (n8 > 0 && n8 != n9) {
            return false;
        }
        float n16 = 0.0f;
        float n17 = 0.0f;
        if (n3 != 0 || n5 != 0 || n4 != 0) {
            if (mFirstVisibleWidget != null) {
                n17 = (float)mFirstVisibleWidget.mListAnchors[n2].getMargin();
            }
            n16 = n17;
            if (mLastVisibleWidget != null) {
                n16 = n17 + mLastVisibleWidget.mListAnchors[n2 + 1].getMargin();
            }
        }
        final float resolvedOffset = resolutionNode.target.resolvedOffset;
        final float resolvedOffset2 = resolutionNode2.target.resolvedOffset;
        float n18;
        if (resolvedOffset < resolvedOffset2) {
            n18 = resolvedOffset2 - resolvedOffset - n6;
        }
        else {
            n18 = resolvedOffset - resolvedOffset2 - n6;
        }
        if (n8 <= 0 || n8 != n9) {
            int n19;
            int n20;
            int n21;
            if (n18 < 0.0f) {
                n19 = 1;
                n20 = 0;
                n21 = 0;
            }
            else {
                n20 = n5;
                n19 = n3;
                n21 = n4;
            }
            if (n19 != 0) {
                final float biasPercent = mFirst.getBiasPercent(n);
                ConstraintWidget constraintWidget2 = mFirst;
                float n22 = biasPercent * (n18 - n16) + resolvedOffset;
                while (constraintWidget2 != null) {
                    if (LinearSystem.sMetrics != null) {
                        final Metrics sMetrics = LinearSystem.sMetrics;
                        --sMetrics.nonresolvedWidgets;
                        final Metrics sMetrics2 = LinearSystem.sMetrics;
                        ++sMetrics2.resolvedWidgets;
                        final Metrics sMetrics3 = LinearSystem.sMetrics;
                        ++sMetrics3.chainConnectionResolved;
                    }
                    final ConstraintWidget constraintWidget3 = constraintWidget2.mNextChainWidget[n];
                    if (constraintWidget3 != null || constraintWidget2 == mLast) {
                        float n23;
                        if (n == 0) {
                            n23 = (float)constraintWidget2.getWidth();
                        }
                        else {
                            n23 = (float)constraintWidget2.getHeight();
                        }
                        final float n24 = n22 + constraintWidget2.mListAnchors[n2].getMargin();
                        constraintWidget2.mListAnchors[n2].getResolutionNode().resolve(resolutionNode.resolvedTarget, n24);
                        constraintWidget2.mListAnchors[n2 + 1].getResolutionNode().resolve(resolutionNode.resolvedTarget, n24 + n23);
                        constraintWidget2.mListAnchors[n2].getResolutionNode().addResolvedValue(linearSystem);
                        constraintWidget2.mListAnchors[n2 + 1].getResolutionNode().addResolvedValue(linearSystem);
                        n22 = n24 + n23 + constraintWidget2.mListAnchors[n2 + 1].getMargin();
                    }
                    constraintWidget2 = constraintWidget3;
                }
            }
            else if (n20 != 0 || n21 != 0) {
                float n25;
                if (n20 != 0) {
                    n25 = n18 - n16;
                }
                else {
                    n25 = n18;
                    if (n21 != 0) {
                        n25 = n18 - n16;
                    }
                }
                float n26 = n25 / (n9 + 1);
                if (n21 != 0) {
                    if (n9 > 1) {
                        n26 = n25 / (n9 - 1);
                    }
                    else {
                        n26 = n25 / 2.0f;
                    }
                }
                float n28;
                final float n27 = n28 = resolvedOffset;
                if (mFirst.getVisibility() != 8) {
                    n28 = n27 + n26;
                }
                float n29 = n28;
                if (n21 != 0) {
                    n29 = n28;
                    if (n9 > 1) {
                        n29 = resolvedOffset + mFirstVisibleWidget.mListAnchors[n2].getMargin();
                    }
                }
                ConstraintWidget constraintWidget4;
                if (n20 != 0 && mFirstVisibleWidget != null) {
                    final float n30 = (float)mFirstVisibleWidget.mListAnchors[n2].getMargin();
                    constraintWidget4 = mFirst;
                    n29 += n30;
                }
                else {
                    constraintWidget4 = mFirst;
                }
                while (constraintWidget4 != null) {
                    if (LinearSystem.sMetrics != null) {
                        final Metrics sMetrics4 = LinearSystem.sMetrics;
                        --sMetrics4.nonresolvedWidgets;
                        final Metrics sMetrics5 = LinearSystem.sMetrics;
                        ++sMetrics5.resolvedWidgets;
                        final Metrics sMetrics6 = LinearSystem.sMetrics;
                        ++sMetrics6.chainConnectionResolved;
                    }
                    final ConstraintWidget constraintWidget5 = constraintWidget4.mNextChainWidget[n];
                    if (constraintWidget5 != null || constraintWidget4 == mLast) {
                        float n31;
                        if (n == 0) {
                            n31 = (float)constraintWidget4.getWidth();
                        }
                        else {
                            n31 = (float)constraintWidget4.getHeight();
                        }
                        if (constraintWidget4 != mFirstVisibleWidget) {
                            n29 += constraintWidget4.mListAnchors[n2].getMargin();
                        }
                        constraintWidget4.mListAnchors[n2].getResolutionNode().resolve(resolutionNode.resolvedTarget, n29);
                        constraintWidget4.mListAnchors[n2 + 1].getResolutionNode().resolve(resolutionNode.resolvedTarget, n29 + n31);
                        constraintWidget4.mListAnchors[n2].getResolutionNode().addResolvedValue(linearSystem);
                        constraintWidget4.mListAnchors[n2 + 1].getResolutionNode().addResolvedValue(linearSystem);
                        final float n32 = n29 + (constraintWidget4.mListAnchors[n2 + 1].getMargin() + n31);
                        if (constraintWidget5 != null) {
                            n29 = n32;
                            if (constraintWidget5.getVisibility() != 8) {
                                n29 = n32 + n26;
                            }
                        }
                        else {
                            n29 = n32;
                        }
                    }
                    constraintWidget4 = constraintWidget5;
                }
            }
            return true;
        }
        if (constraintWidget.getParent() != null && constraintWidget.getParent().mListDimensionBehaviors[n] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
            return false;
        }
        final float n33 = n18 + n6 - n7;
        ConstraintWidget constraintWidget6 = mFirst;
        float n34 = resolvedOffset;
        while (constraintWidget6 != null) {
            if (LinearSystem.sMetrics != null) {
                final Metrics sMetrics7 = LinearSystem.sMetrics;
                --sMetrics7.nonresolvedWidgets;
                final Metrics sMetrics8 = LinearSystem.sMetrics;
                ++sMetrics8.resolvedWidgets;
                final Metrics sMetrics9 = LinearSystem.sMetrics;
                ++sMetrics9.chainConnectionResolved;
            }
            final ConstraintWidget constraintWidget7 = constraintWidget6.mNextChainWidget[n];
            if (constraintWidget7 != null || constraintWidget6 == mLast) {
                float n35 = n33 / n8;
                if (mTotalWeight > 0.0f) {
                    if (constraintWidget6.mWeight[n] == -1.0f) {
                        n35 = 0.0f;
                    }
                    else {
                        n35 = constraintWidget6.mWeight[n] * n33 / mTotalWeight;
                    }
                }
                if (constraintWidget6.getVisibility() == 8) {
                    n35 = 0.0f;
                }
                final float n36 = n34 + constraintWidget6.mListAnchors[n2].getMargin();
                constraintWidget6.mListAnchors[n2].getResolutionNode().resolve(resolutionNode.resolvedTarget, n36);
                constraintWidget6.mListAnchors[n2 + 1].getResolutionNode().resolve(resolutionNode.resolvedTarget, n36 + n35);
                constraintWidget6.mListAnchors[n2].getResolutionNode().addResolvedValue(linearSystem);
                constraintWidget6.mListAnchors[n2 + 1].getResolutionNode().addResolvedValue(linearSystem);
                n34 = n36 + n35 + constraintWidget6.mListAnchors[n2 + 1].getMargin();
            }
            constraintWidget6 = constraintWidget7;
        }
        return true;
    }
    
    static void checkMatchParent(final ConstraintWidgetContainer constraintWidgetContainer, final LinearSystem linearSystem, final ConstraintWidget constraintWidget) {
        if (constraintWidgetContainer.mListDimensionBehaviors[0] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && constraintWidget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            final int mMargin = constraintWidget.mLeft.mMargin;
            final int n = constraintWidgetContainer.getWidth() - constraintWidget.mRight.mMargin;
            constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
            constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
            linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, mMargin);
            linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n);
            constraintWidget.mHorizontalResolution = 2;
            constraintWidget.setHorizontalDimension(mMargin, n);
        }
        if (constraintWidgetContainer.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && constraintWidget.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            final int mMargin2 = constraintWidget.mTop.mMargin;
            final int n2 = constraintWidgetContainer.getHeight() - constraintWidget.mBottom.mMargin;
            constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
            constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
            linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, mMargin2);
            linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n2);
            if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), constraintWidget.mBaselineDistance + mMargin2);
            }
            constraintWidget.mVerticalResolution = 2;
            constraintWidget.setVerticalDimension(mMargin2, n2);
        }
    }
    
    private static boolean optimizableMatchConstraint(final ConstraintWidget constraintWidget, int n) {
        if (constraintWidget.mListDimensionBehaviors[n] != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            return false;
        }
        final float mDimensionRatio = constraintWidget.mDimensionRatio;
        final int n2 = 1;
        if (mDimensionRatio != 0.0f) {
            final ConstraintWidget.DimensionBehaviour[] mListDimensionBehaviors = constraintWidget.mListDimensionBehaviors;
            if (n == 0) {
                n = n2;
            }
            else {
                n = 0;
            }
            return mListDimensionBehaviors[n] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && false;
        }
        if (n == 0) {
            if (constraintWidget.mMatchConstraintDefaultWidth != 0) {
                return false;
            }
            if (constraintWidget.mMatchConstraintMinWidth != 0 || constraintWidget.mMatchConstraintMaxWidth != 0) {
                return false;
            }
        }
        else {
            if (constraintWidget.mMatchConstraintDefaultHeight != 0) {
                return false;
            }
            if (constraintWidget.mMatchConstraintMinHeight != 0 || constraintWidget.mMatchConstraintMaxHeight != 0) {
                return false;
            }
        }
        return true;
    }
    
    static void setOptimizedWidget(final ConstraintWidget constraintWidget, final int n, final int n2) {
        final int n3 = n * 2;
        final int n4 = n3 + 1;
        constraintWidget.mListAnchors[n3].getResolutionNode().resolvedTarget = constraintWidget.getParent().mLeft.getResolutionNode();
        constraintWidget.mListAnchors[n3].getResolutionNode().resolvedOffset = (float)n2;
        constraintWidget.mListAnchors[n3].getResolutionNode().state = 1;
        constraintWidget.mListAnchors[n4].getResolutionNode().resolvedTarget = constraintWidget.mListAnchors[n3].getResolutionNode();
        constraintWidget.mListAnchors[n4].getResolutionNode().resolvedOffset = (float)constraintWidget.getLength(n);
        constraintWidget.mListAnchors[n4].getResolutionNode().state = 1;
    }
}
