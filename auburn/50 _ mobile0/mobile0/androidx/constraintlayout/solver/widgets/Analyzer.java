// 
// Decompiled by Procyon v0.5.36
// 

package androidx.constraintlayout.solver.widgets;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class Analyzer
{
    private Analyzer() {
    }
    
    public static void determineGroups(final ConstraintWidgetContainer constraintWidgetContainer) {
        if ((constraintWidgetContainer.getOptimizationLevel() & 0x20) != 0x20) {
            singleGroup(constraintWidgetContainer);
            return;
        }
        constraintWidgetContainer.mSkipSolver = true;
        constraintWidgetContainer.mGroupsWrapOptimized = false;
        constraintWidgetContainer.mHorizontalWrapOptimized = false;
        constraintWidgetContainer.mVerticalWrapOptimized = false;
        final ArrayList<ConstraintWidget> mChildren = constraintWidgetContainer.mChildren;
        final List<ConstraintWidgetGroup> mWidgetGroups = constraintWidgetContainer.mWidgetGroups;
        final boolean b = constraintWidgetContainer.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        final boolean b2 = constraintWidgetContainer.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        final boolean b3 = b || b2;
        mWidgetGroups.clear();
        for (final ConstraintWidget constraintWidget : mChildren) {
            constraintWidget.mBelongingGroup = null;
            constraintWidget.mGroupsToSolver = false;
            constraintWidget.resetResolutionNodes();
        }
        for (final ConstraintWidget constraintWidget2 : mChildren) {
            if (constraintWidget2.mBelongingGroup == null && !determineGroups(constraintWidget2, mWidgetGroups, b3)) {
                singleGroup(constraintWidgetContainer);
                constraintWidgetContainer.mSkipSolver = false;
                return;
            }
        }
        int max = 0;
        int max2 = 0;
        for (final ConstraintWidgetGroup constraintWidgetGroup : mWidgetGroups) {
            max = Math.max(max, getMaxDimension(constraintWidgetGroup, 0));
            max2 = Math.max(max2, getMaxDimension(constraintWidgetGroup, 1));
        }
        if (b) {
            constraintWidgetContainer.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
            constraintWidgetContainer.setWidth(max);
            constraintWidgetContainer.mGroupsWrapOptimized = true;
            constraintWidgetContainer.mHorizontalWrapOptimized = true;
            constraintWidgetContainer.mWrapFixedWidth = max;
        }
        if (b2) {
            constraintWidgetContainer.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
            constraintWidgetContainer.setHeight(max2);
            constraintWidgetContainer.mGroupsWrapOptimized = true;
            constraintWidgetContainer.mVerticalWrapOptimized = true;
            constraintWidgetContainer.mWrapFixedHeight = max2;
        }
        setPosition(mWidgetGroups, 0, constraintWidgetContainer.getWidth());
        setPosition(mWidgetGroups, 1, constraintWidgetContainer.getHeight());
    }
    
    private static boolean determineGroups(final ConstraintWidget constraintWidget, final List<ConstraintWidgetGroup> list, final boolean b) {
        final ConstraintWidgetGroup constraintWidgetGroup = new ConstraintWidgetGroup(new ArrayList<ConstraintWidget>(), true);
        list.add(constraintWidgetGroup);
        return traverse(constraintWidget, constraintWidgetGroup, list, b);
    }
    
    private static int getMaxDimension(final ConstraintWidgetGroup constraintWidgetGroup, final int n) {
        int max = 0;
        final int n2 = n * 2;
        final List<ConstraintWidget> startWidgets = constraintWidgetGroup.getStartWidgets(n);
        for (int size = startWidgets.size(), i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = startWidgets.get(i);
            max = Math.max(max, getMaxDimensionTraversal(constraintWidget, n, constraintWidget.mListAnchors[n2 + 1].mTarget == null || (constraintWidget.mListAnchors[n2].mTarget != null && constraintWidget.mListAnchors[n2 + 1].mTarget != null), 0));
        }
        return constraintWidgetGroup.mGroupDimensions[n] = max;
    }
    
    private static int getMaxDimensionTraversal(final ConstraintWidget constraintWidget, final int n, final boolean b, int n2) {
        final boolean mOptimizerMeasurable = constraintWidget.mOptimizerMeasurable;
        final int n3 = 0;
        if (!mOptimizerMeasurable) {
            return 0;
        }
        final int n4 = 0;
        final int n5 = 0;
        int n6 = n3;
        if (constraintWidget.mBaseline.mTarget != null) {
            n6 = n3;
            if (n == 1) {
                n6 = 1;
            }
        }
        int baselineDistance;
        int baselineDistance2;
        int n7;
        int n8;
        if (b) {
            baselineDistance = constraintWidget.getBaselineDistance();
            baselineDistance2 = constraintWidget.getHeight() - constraintWidget.getBaselineDistance();
            n7 = n * 2;
            n8 = n7 + 1;
        }
        else {
            baselineDistance = constraintWidget.getHeight() - constraintWidget.getBaselineDistance();
            baselineDistance2 = constraintWidget.getBaselineDistance();
            n8 = n * 2;
            n7 = n8 + 1;
        }
        int n9;
        if (constraintWidget.mListAnchors[n8].mTarget != null && constraintWidget.mListAnchors[n7].mTarget == null) {
            n9 = -1;
            final int n10 = n7;
            n7 = n8;
            n8 = n10;
        }
        else {
            n9 = 1;
        }
        int n11;
        if (n6 != 0) {
            n11 = n2 - baselineDistance;
        }
        else {
            n11 = n2;
        }
        final int n12 = constraintWidget.mListAnchors[n7].getMargin() * n9 + getParentBiasOffset(constraintWidget, n);
        final int n13 = n12 + n11;
        if (n == 0) {
            n2 = constraintWidget.getWidth();
        }
        else {
            n2 = constraintWidget.getHeight();
        }
        int n14 = n2 * n9;
        final Iterator<ResolutionNode> iterator = constraintWidget.mListAnchors[n7].getResolutionNode().dependents.iterator();
        final int n15 = n5;
        n2 = n4;
        while (iterator.hasNext()) {
            n2 = Math.max(n2, getMaxDimensionTraversal(((ResolutionAnchor)iterator.next()).myAnchor.mOwner, n, b, n13));
        }
        final Iterator<ResolutionNode> iterator2 = constraintWidget.mListAnchors[n8].getResolutionNode().dependents.iterator();
        final int n16 = n15;
        final int n17 = n8;
        int max = n16;
        while (iterator2.hasNext()) {
            max = Math.max(max, getMaxDimensionTraversal(((ResolutionAnchor)iterator2.next()).myAnchor.mOwner, n, b, n14 + n13));
        }
        int a;
        int a2;
        if (n6 != 0) {
            a = n2 - baselineDistance;
            a2 = max + baselineDistance2;
        }
        else {
            int n18;
            if (n == 0) {
                n18 = constraintWidget.getWidth();
            }
            else {
                n18 = constraintWidget.getHeight();
            }
            a2 = max + n18 * n9;
            a = n2;
        }
        n2 = 0;
        final int n19 = 0;
        int n24;
        if (n == 1) {
            final Iterator<ResolutionNode> iterator3 = constraintWidget.mBaseline.getResolutionNode().dependents.iterator();
            n2 = n14;
            final int n20 = n7;
            int n21 = n19;
            while (iterator3.hasNext()) {
                final ResolutionAnchor resolutionAnchor = (ResolutionAnchor)iterator3.next();
                if (n9 == 1) {
                    n21 = Math.max(n21, getMaxDimensionTraversal(resolutionAnchor.myAnchor.mOwner, n, b, baselineDistance + n13));
                }
                else {
                    n21 = Math.max(n21, getMaxDimensionTraversal(resolutionAnchor.myAnchor.mOwner, n, b, baselineDistance2 * n9 + n13));
                }
            }
            final int n22 = n20;
            final int n23 = n2;
            n2 = n21;
            n24 = n22;
            n14 = n23;
            if (constraintWidget.mBaseline.getResolutionNode().dependents.size() > 0) {
                n2 = n21;
                n24 = n22;
                n14 = n23;
                if (n6 == 0) {
                    if (n9 == 1) {
                        n2 = n21 + baselineDistance;
                        n24 = n22;
                        n14 = n23;
                    }
                    else {
                        n2 = n21 - baselineDistance2;
                        n24 = n22;
                        n14 = n23;
                    }
                }
            }
        }
        else {
            n24 = n7;
        }
        final int max2 = Math.max(a, Math.max(a2, n2));
        final int n25 = n11 + n12;
        n2 = n25 + n14;
        int n26 = n25;
        int n27 = n2;
        if (n9 == -1) {
            n27 = n25;
            n26 = n2;
        }
        if (b) {
            Optimizer.setOptimizedWidget(constraintWidget, n, n26);
            constraintWidget.setFrame(n26, n27, n);
        }
        else {
            constraintWidget.mBelongingGroup.addWidgetsToSet(constraintWidget, n);
            constraintWidget.setRelativePositioning(n26, n);
        }
        if (constraintWidget.getDimensionBehaviour(n) == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mDimensionRatio != 0.0f) {
            constraintWidget.mBelongingGroup.addWidgetsToSet(constraintWidget, n);
        }
        if (constraintWidget.mListAnchors[n24].mTarget != null && constraintWidget.mListAnchors[n17].mTarget != null) {
            final ConstraintWidget parent = constraintWidget.getParent();
            if (constraintWidget.mListAnchors[n24].mTarget.mOwner == parent && constraintWidget.mListAnchors[n17].mTarget.mOwner == parent) {
                constraintWidget.mBelongingGroup.addWidgetsToSet(constraintWidget, n);
            }
        }
        return n12 + max2;
    }
    
    private static int getParentBiasOffset(final ConstraintWidget constraintWidget, int length) {
        final int n = length * 2;
        final ConstraintAnchor constraintAnchor = constraintWidget.mListAnchors[n];
        final ConstraintAnchor constraintAnchor2 = constraintWidget.mListAnchors[n + 1];
        if (constraintAnchor.mTarget != null && constraintAnchor.mTarget.mOwner == constraintWidget.mParent && constraintAnchor2.mTarget != null && constraintAnchor2.mTarget.mOwner == constraintWidget.mParent) {
            final int length2 = constraintWidget.mParent.getLength(length);
            float n2;
            if (length == 0) {
                n2 = constraintWidget.mHorizontalBiasPercent;
            }
            else {
                n2 = constraintWidget.mVerticalBiasPercent;
            }
            length = constraintWidget.getLength(length);
            return (int)((length2 - constraintAnchor.getMargin() - constraintAnchor2.getMargin() - length) * n2);
        }
        return 0;
    }
    
    private static void invalidate(final ConstraintWidgetContainer constraintWidgetContainer, final ConstraintWidget constraintWidget, final ConstraintWidgetGroup constraintWidgetGroup) {
        constraintWidgetGroup.mSkipSolver = false;
        constraintWidgetContainer.mSkipSolver = false;
        constraintWidget.mOptimizerMeasurable = false;
    }
    
    private static int resolveDimensionRatio(final ConstraintWidget constraintWidget) {
        int n = -1;
        if (constraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            if (constraintWidget.mDimensionRatioSide == 0) {
                n = (int)(constraintWidget.getHeight() * constraintWidget.mDimensionRatio);
            }
            else {
                n = (int)(constraintWidget.getHeight() / constraintWidget.mDimensionRatio);
            }
            constraintWidget.setWidth(n);
        }
        else if (constraintWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            if (constraintWidget.mDimensionRatioSide == 1) {
                n = (int)(constraintWidget.getWidth() * constraintWidget.mDimensionRatio);
            }
            else {
                n = (int)(constraintWidget.getWidth() / constraintWidget.mDimensionRatio);
            }
            constraintWidget.setHeight(n);
        }
        return n;
    }
    
    private static void setConnection(final ConstraintAnchor constraintAnchor) {
        final ResolutionAnchor resolutionNode = constraintAnchor.getResolutionNode();
        if (constraintAnchor.mTarget != null && constraintAnchor.mTarget.mTarget != constraintAnchor) {
            constraintAnchor.mTarget.getResolutionNode().addDependent(resolutionNode);
        }
    }
    
    public static void setPosition(final List<ConstraintWidgetGroup> list, final int n, final int n2) {
        for (int size = list.size(), i = 0; i < size; ++i) {
            for (final ConstraintWidget constraintWidget : list.get(i).getWidgetsToSet(n)) {
                if (constraintWidget.mOptimizerMeasurable) {
                    updateSizeDependentWidgets(constraintWidget, n, n2);
                }
            }
        }
    }
    
    private static void singleGroup(final ConstraintWidgetContainer constraintWidgetContainer) {
        constraintWidgetContainer.mWidgetGroups.clear();
        constraintWidgetContainer.mWidgetGroups.add(0, new ConstraintWidgetGroup(constraintWidgetContainer.mChildren));
    }
    
    private static boolean traverse(final ConstraintWidget constraintWidget, final ConstraintWidgetGroup constraintWidgetGroup, final List<ConstraintWidgetGroup> list, final boolean b) {
        if (constraintWidget == null) {
            return true;
        }
        constraintWidget.mOptimizerMeasured = false;
        final ConstraintWidgetContainer constraintWidgetContainer = (ConstraintWidgetContainer)constraintWidget.getParent();
        if (constraintWidget.mBelongingGroup == null) {
            constraintWidget.mOptimizerMeasurable = true;
            constraintWidgetGroup.mConstrainedGroup.add(constraintWidget);
            constraintWidget.mBelongingGroup = constraintWidgetGroup;
            if (constraintWidget.mLeft.mTarget == null && constraintWidget.mRight.mTarget == null && constraintWidget.mTop.mTarget == null && constraintWidget.mBottom.mTarget == null && constraintWidget.mBaseline.mTarget == null && constraintWidget.mCenter.mTarget == null) {
                invalidate(constraintWidgetContainer, constraintWidget, constraintWidgetGroup);
                if (b) {
                    return false;
                }
            }
            if (constraintWidget.mTop.mTarget != null && constraintWidget.mBottom.mTarget != null) {
                if (constraintWidgetContainer.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {}
                if (b) {
                    invalidate(constraintWidgetContainer, constraintWidget, constraintWidgetGroup);
                    return false;
                }
                if (constraintWidget.mTop.mTarget.mOwner != constraintWidget.getParent() || constraintWidget.mBottom.mTarget.mOwner != constraintWidget.getParent()) {
                    invalidate(constraintWidgetContainer, constraintWidget, constraintWidgetGroup);
                }
            }
            if (constraintWidget.mLeft.mTarget != null && constraintWidget.mRight.mTarget != null) {
                if (constraintWidgetContainer.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {}
                if (b) {
                    invalidate(constraintWidgetContainer, constraintWidget, constraintWidgetGroup);
                    return false;
                }
                if (constraintWidget.mLeft.mTarget.mOwner != constraintWidget.getParent() || constraintWidget.mRight.mTarget.mOwner != constraintWidget.getParent()) {
                    invalidate(constraintWidgetContainer, constraintWidget, constraintWidgetGroup);
                }
            }
            if ((constraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT ^ constraintWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) && constraintWidget.mDimensionRatio != 0.0f) {
                resolveDimensionRatio(constraintWidget);
            }
            else if (constraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || constraintWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                invalidate(constraintWidgetContainer, constraintWidget, constraintWidgetGroup);
                if (b) {
                    return false;
                }
            }
            if (((constraintWidget.mLeft.mTarget == null && constraintWidget.mRight.mTarget == null) || (constraintWidget.mLeft.mTarget != null && constraintWidget.mLeft.mTarget.mOwner == constraintWidget.mParent && constraintWidget.mRight.mTarget == null) || (constraintWidget.mRight.mTarget != null && constraintWidget.mRight.mTarget.mOwner == constraintWidget.mParent && constraintWidget.mLeft.mTarget == null) || (constraintWidget.mLeft.mTarget != null && constraintWidget.mLeft.mTarget.mOwner == constraintWidget.mParent && constraintWidget.mRight.mTarget != null && constraintWidget.mRight.mTarget.mOwner == constraintWidget.mParent)) && constraintWidget.mCenter.mTarget == null && !(constraintWidget instanceof Guideline) && !(constraintWidget instanceof Helper)) {
                constraintWidgetGroup.mStartHorizontalWidgets.add(constraintWidget);
            }
            if (((constraintWidget.mTop.mTarget == null && constraintWidget.mBottom.mTarget == null) || (constraintWidget.mTop.mTarget != null && constraintWidget.mTop.mTarget.mOwner == constraintWidget.mParent && constraintWidget.mBottom.mTarget == null) || (constraintWidget.mBottom.mTarget != null && constraintWidget.mBottom.mTarget.mOwner == constraintWidget.mParent && constraintWidget.mTop.mTarget == null) || (constraintWidget.mTop.mTarget != null && constraintWidget.mTop.mTarget.mOwner == constraintWidget.mParent && constraintWidget.mBottom.mTarget != null && constraintWidget.mBottom.mTarget.mOwner == constraintWidget.mParent)) && constraintWidget.mCenter.mTarget == null && constraintWidget.mBaseline.mTarget == null && !(constraintWidget instanceof Guideline) && !(constraintWidget instanceof Helper)) {
                constraintWidgetGroup.mStartVerticalWidgets.add(constraintWidget);
            }
            if (constraintWidget instanceof Helper) {
                invalidate(constraintWidgetContainer, constraintWidget, constraintWidgetGroup);
                if (b) {
                    return false;
                }
                final Helper helper = (Helper)constraintWidget;
                for (int i = 0; i < helper.mWidgetsCount; ++i) {
                    if (!traverse(helper.mWidgets[i], constraintWidgetGroup, list, b)) {
                        return false;
                    }
                }
            }
            for (int length = constraintWidget.mListAnchors.length, j = 0; j < length; ++j) {
                final ConstraintAnchor connection = constraintWidget.mListAnchors[j];
                if (connection.mTarget != null && connection.mTarget.mOwner != constraintWidget.getParent()) {
                    if (connection.mType == ConstraintAnchor.Type.CENTER) {
                        invalidate(constraintWidgetContainer, constraintWidget, constraintWidgetGroup);
                        if (b) {
                            return false;
                        }
                    }
                    else {
                        setConnection(connection);
                    }
                    if (!traverse(connection.mTarget.mOwner, constraintWidgetGroup, list, b)) {
                        return false;
                    }
                }
            }
            return true;
        }
        if (constraintWidget.mBelongingGroup != constraintWidgetGroup) {
            constraintWidgetGroup.mConstrainedGroup.addAll(constraintWidget.mBelongingGroup.mConstrainedGroup);
            constraintWidgetGroup.mStartHorizontalWidgets.addAll(constraintWidget.mBelongingGroup.mStartHorizontalWidgets);
            constraintWidgetGroup.mStartVerticalWidgets.addAll(constraintWidget.mBelongingGroup.mStartVerticalWidgets);
            if (!constraintWidget.mBelongingGroup.mSkipSolver) {
                constraintWidgetGroup.mSkipSolver = false;
            }
            list.remove(constraintWidget.mBelongingGroup);
            final Iterator<ConstraintWidget> iterator = constraintWidget.mBelongingGroup.mConstrainedGroup.iterator();
            while (iterator.hasNext()) {
                iterator.next().mBelongingGroup = constraintWidgetGroup;
            }
        }
        return true;
    }
    
    private static void updateSizeDependentWidgets(final ConstraintWidget constraintWidget, final int n, int resolveDimensionRatio) {
        final int n2 = n * 2;
        final ConstraintAnchor constraintAnchor = constraintWidget.mListAnchors[n2];
        final ConstraintAnchor constraintAnchor2 = constraintWidget.mListAnchors[n2 + 1];
        if (constraintAnchor.mTarget != null && constraintAnchor2.mTarget != null) {
            Optimizer.setOptimizedWidget(constraintWidget, n, getParentBiasOffset(constraintWidget, n) + constraintAnchor.getMargin());
            return;
        }
        if (constraintWidget.mDimensionRatio != 0.0f && constraintWidget.getDimensionBehaviour(n) == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            resolveDimensionRatio = resolveDimensionRatio(constraintWidget);
            final int n3 = (int)constraintWidget.mListAnchors[n2].getResolutionNode().resolvedOffset;
            constraintAnchor2.getResolutionNode().resolvedTarget = constraintAnchor.getResolutionNode();
            constraintAnchor2.getResolutionNode().resolvedOffset = (float)resolveDimensionRatio;
            constraintAnchor2.getResolutionNode().state = 1;
            constraintWidget.setFrame(n3, n3 + resolveDimensionRatio, n);
            return;
        }
        final int n4 = resolveDimensionRatio - constraintWidget.getRelativePositioning(n);
        resolveDimensionRatio = n4 - constraintWidget.getLength(n);
        constraintWidget.setFrame(resolveDimensionRatio, n4, n);
        Optimizer.setOptimizedWidget(constraintWidget, n, resolveDimensionRatio);
    }
}
