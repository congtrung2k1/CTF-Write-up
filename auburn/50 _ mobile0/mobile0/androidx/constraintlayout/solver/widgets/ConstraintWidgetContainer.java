// 
// Decompiled by Procyon v0.5.36
// 

package androidx.constraintlayout.solver.widgets;

import java.io.PrintStream;
import androidx.constraintlayout.solver.Metrics;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import androidx.constraintlayout.solver.LinearSystem;

public class ConstraintWidgetContainer extends WidgetContainer
{
    private static final boolean DEBUG = false;
    static final boolean DEBUG_GRAPH = false;
    private static final boolean DEBUG_LAYOUT = false;
    private static final int MAX_ITERATIONS = 8;
    private static final boolean USE_SNAPSHOT = true;
    int mDebugSolverPassCount;
    public boolean mGroupsWrapOptimized;
    private boolean mHeightMeasuredTooSmall;
    ChainHead[] mHorizontalChainsArray;
    int mHorizontalChainsSize;
    public boolean mHorizontalWrapOptimized;
    private boolean mIsRtl;
    private int mOptimizationLevel;
    int mPaddingBottom;
    int mPaddingLeft;
    int mPaddingRight;
    int mPaddingTop;
    public boolean mSkipSolver;
    private Snapshot mSnapshot;
    protected LinearSystem mSystem;
    ChainHead[] mVerticalChainsArray;
    int mVerticalChainsSize;
    public boolean mVerticalWrapOptimized;
    public List<ConstraintWidgetGroup> mWidgetGroups;
    private boolean mWidthMeasuredTooSmall;
    public int mWrapFixedHeight;
    public int mWrapFixedWidth;
    
    public ConstraintWidgetContainer() {
        this.mIsRtl = false;
        this.mSystem = new LinearSystem();
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
        this.mVerticalChainsArray = new ChainHead[4];
        this.mHorizontalChainsArray = new ChainHead[4];
        this.mWidgetGroups = new ArrayList<ConstraintWidgetGroup>();
        this.mGroupsWrapOptimized = false;
        this.mHorizontalWrapOptimized = false;
        this.mVerticalWrapOptimized = false;
        this.mWrapFixedWidth = 0;
        this.mWrapFixedHeight = 0;
        this.mOptimizationLevel = 7;
        this.mSkipSolver = false;
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
        this.mDebugSolverPassCount = 0;
    }
    
    public ConstraintWidgetContainer(final int n, final int n2) {
        super(n, n2);
        this.mIsRtl = false;
        this.mSystem = new LinearSystem();
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
        this.mVerticalChainsArray = new ChainHead[4];
        this.mHorizontalChainsArray = new ChainHead[4];
        this.mWidgetGroups = new ArrayList<ConstraintWidgetGroup>();
        this.mGroupsWrapOptimized = false;
        this.mHorizontalWrapOptimized = false;
        this.mVerticalWrapOptimized = false;
        this.mWrapFixedWidth = 0;
        this.mWrapFixedHeight = 0;
        this.mOptimizationLevel = 7;
        this.mSkipSolver = false;
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
        this.mDebugSolverPassCount = 0;
    }
    
    public ConstraintWidgetContainer(final int n, final int n2, final int n3, final int n4) {
        super(n, n2, n3, n4);
        this.mIsRtl = false;
        this.mSystem = new LinearSystem();
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
        this.mVerticalChainsArray = new ChainHead[4];
        this.mHorizontalChainsArray = new ChainHead[4];
        this.mWidgetGroups = new ArrayList<ConstraintWidgetGroup>();
        this.mGroupsWrapOptimized = false;
        this.mHorizontalWrapOptimized = false;
        this.mVerticalWrapOptimized = false;
        this.mWrapFixedWidth = 0;
        this.mWrapFixedHeight = 0;
        this.mOptimizationLevel = 7;
        this.mSkipSolver = false;
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
        this.mDebugSolverPassCount = 0;
    }
    
    private void addHorizontalChain(final ConstraintWidget constraintWidget) {
        final int mHorizontalChainsSize = this.mHorizontalChainsSize;
        final ChainHead[] mHorizontalChainsArray = this.mHorizontalChainsArray;
        if (mHorizontalChainsSize + 1 >= mHorizontalChainsArray.length) {
            this.mHorizontalChainsArray = Arrays.copyOf(mHorizontalChainsArray, mHorizontalChainsArray.length * 2);
        }
        this.mHorizontalChainsArray[this.mHorizontalChainsSize] = new ChainHead(constraintWidget, 0, this.isRtl());
        ++this.mHorizontalChainsSize;
    }
    
    private void addVerticalChain(final ConstraintWidget constraintWidget) {
        final int mVerticalChainsSize = this.mVerticalChainsSize;
        final ChainHead[] mVerticalChainsArray = this.mVerticalChainsArray;
        if (mVerticalChainsSize + 1 >= mVerticalChainsArray.length) {
            this.mVerticalChainsArray = Arrays.copyOf(mVerticalChainsArray, mVerticalChainsArray.length * 2);
        }
        this.mVerticalChainsArray[this.mVerticalChainsSize] = new ChainHead(constraintWidget, 1, this.isRtl());
        ++this.mVerticalChainsSize;
    }
    
    private void resetChains() {
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
    }
    
    void addChain(final ConstraintWidget constraintWidget, final int n) {
        if (n == 0) {
            this.addHorizontalChain(constraintWidget);
        }
        else if (n == 1) {
            this.addVerticalChain(constraintWidget);
        }
    }
    
    public boolean addChildrenToSolver(final LinearSystem linearSystem) {
        this.addToSolver(linearSystem);
        for (int size = this.mChildren.size(), i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            if (constraintWidget instanceof ConstraintWidgetContainer) {
                final DimensionBehaviour horizontalDimensionBehaviour = constraintWidget.mListDimensionBehaviors[0];
                final DimensionBehaviour verticalDimensionBehaviour = constraintWidget.mListDimensionBehaviors[1];
                if (horizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setHorizontalDimensionBehaviour(DimensionBehaviour.FIXED);
                }
                if (verticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setVerticalDimensionBehaviour(DimensionBehaviour.FIXED);
                }
                constraintWidget.addToSolver(linearSystem);
                if (horizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setHorizontalDimensionBehaviour(horizontalDimensionBehaviour);
                }
                if (verticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setVerticalDimensionBehaviour(verticalDimensionBehaviour);
                }
            }
            else {
                Optimizer.checkMatchParent(this, linearSystem, constraintWidget);
                constraintWidget.addToSolver(linearSystem);
            }
        }
        if (this.mHorizontalChainsSize > 0) {
            Chain.applyChainConstraints(this, linearSystem, 0);
        }
        if (this.mVerticalChainsSize > 0) {
            Chain.applyChainConstraints(this, linearSystem, 1);
        }
        return true;
    }
    
    @Override
    public void analyze(final int n) {
        super.analyze(n);
        for (int size = this.mChildren.size(), i = 0; i < size; ++i) {
            this.mChildren.get(i).analyze(n);
        }
    }
    
    public void fillMetrics(final Metrics metrics) {
        this.mSystem.fillMetrics(metrics);
    }
    
    public ArrayList<Guideline> getHorizontalGuidelines() {
        final ArrayList<Guideline> list = new ArrayList<Guideline>();
        for (int i = 0; i < this.mChildren.size(); ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            if (constraintWidget instanceof Guideline) {
                final Guideline e = (Guideline)constraintWidget;
                if (e.getOrientation() == 0) {
                    list.add(e);
                }
            }
        }
        return list;
    }
    
    public int getOptimizationLevel() {
        return this.mOptimizationLevel;
    }
    
    public LinearSystem getSystem() {
        return this.mSystem;
    }
    
    @Override
    public String getType() {
        return "ConstraintLayout";
    }
    
    public ArrayList<Guideline> getVerticalGuidelines() {
        final ArrayList<Guideline> list = new ArrayList<Guideline>();
        for (int i = 0; i < this.mChildren.size(); ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            if (constraintWidget instanceof Guideline) {
                final Guideline e = (Guideline)constraintWidget;
                if (e.getOrientation() == 1) {
                    list.add(e);
                }
            }
        }
        return list;
    }
    
    public List<ConstraintWidgetGroup> getWidgetGroups() {
        return this.mWidgetGroups;
    }
    
    public boolean handlesInternalConstraints() {
        return false;
    }
    
    public boolean isHeightMeasuredTooSmall() {
        return this.mHeightMeasuredTooSmall;
    }
    
    public boolean isRtl() {
        return this.mIsRtl;
    }
    
    public boolean isWidthMeasuredTooSmall() {
        return this.mWidthMeasuredTooSmall;
    }
    
    @Override
    public void layout() {
        final int mx = this.mX;
        final int my = this.mY;
        final int max = Math.max(0, this.getWidth());
        final int max2 = Math.max(0, this.getHeight());
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
        if (this.mParent != null) {
            if (this.mSnapshot == null) {
                this.mSnapshot = new Snapshot(this);
            }
            this.mSnapshot.updateFrom(this);
            this.setX(this.mPaddingLeft);
            this.setY(this.mPaddingTop);
            this.resetAnchors();
            this.resetSolverVariables(this.mSystem.getCache());
        }
        else {
            this.mX = 0;
            this.mY = 0;
        }
        if (this.mOptimizationLevel != 0) {
            if (!this.optimizeFor(8)) {
                this.optimizeReset();
            }
            if (!this.optimizeFor(32)) {
                this.optimize();
            }
            this.mSystem.graphOptimizer = true;
        }
        else {
            this.mSystem.graphOptimizer = false;
        }
        int n = 0;
        final DimensionBehaviour dimensionBehaviour = this.mListDimensionBehaviors[1];
        final DimensionBehaviour dimensionBehaviour2 = this.mListDimensionBehaviors[0];
        this.resetChains();
        if (this.mWidgetGroups.size() == 0) {
            this.mWidgetGroups.clear();
            this.mWidgetGroups.add(0, new ConstraintWidgetGroup(this.mChildren));
        }
        int size = this.mWidgetGroups.size();
        final ArrayList<ConstraintWidget> mChildren = this.mChildren;
        final boolean b = this.getHorizontalDimensionBehaviour() == DimensionBehaviour.WRAP_CONTENT || this.getVerticalDimensionBehaviour() == DimensionBehaviour.WRAP_CONTENT;
        for (int n2 = 0; n2 < size && !this.mSkipSolver; ++n2) {
            if (!this.mWidgetGroups.get(n2).mSkipSolver) {
                if (this.optimizeFor(32)) {
                    if (this.getHorizontalDimensionBehaviour() == DimensionBehaviour.FIXED && this.getVerticalDimensionBehaviour() == DimensionBehaviour.FIXED) {
                        this.mChildren = (ArrayList<ConstraintWidget>)(ArrayList)this.mWidgetGroups.get(n2).getWidgetsToSolve();
                    }
                    else {
                        this.mChildren = (ArrayList<ConstraintWidget>)(ArrayList)this.mWidgetGroups.get(n2).mConstrainedGroup;
                    }
                }
                this.resetChains();
                final int size2 = this.mChildren.size();
                final int n3 = 0;
                ConstraintWidget obj = null;
                for (int i = 0; i < size2; ++i) {
                    obj = this.mChildren.get(i);
                    if (obj instanceof WidgetContainer) {
                        ((WidgetContainer)obj).layout();
                    }
                }
                int j = 1;
                final int n4 = n;
                int n5 = n3;
                final int n6 = size;
                int n7 = size2;
                int n8 = n4;
                while (j != 0) {
                    final int n9 = n5 + 1;
                    try {
                        this.mSystem.reset();
                        this.resetChains();
                        this.createObjectVariables(this.mSystem);
                        int index = 0;
                    Block_53_Outer:
                        while (true) {
                            Label_0574: {
                                if (index >= n7) {
                                    break Label_0574;
                                }
                                final int n10 = n8;
                                try {
                                    final ConstraintWidget constraintWidget;
                                    obj = (constraintWidget = this.mChildren.get(index));
                                    final ConstraintWidgetContainer constraintWidgetContainer = this;
                                    final LinearSystem linearSystem = constraintWidgetContainer.mSystem;
                                    constraintWidget.createObjectVariables(linearSystem);
                                    ++index;
                                    n8 = n10;
                                    continue;
                                }
                                catch (Exception obj) {
                                    n8 = n10;
                                    break;
                                }
                                try {
                                    final ConstraintWidget constraintWidget = obj;
                                    final ConstraintWidgetContainer constraintWidgetContainer = this;
                                    final LinearSystem linearSystem = constraintWidgetContainer.mSystem;
                                    constraintWidget.createObjectVariables(linearSystem);
                                    ++index;
                                    n8 = n10;
                                    continue Block_53_Outer;
                                    // iftrue(Label_0616:, j == 0)
                                    int n11 = 0;
                                Label_0616:
                                    while (true) {
                                        try {
                                            this.mSystem.minimize();
                                        }
                                        catch (Exception obj) {
                                            n8 = n11;
                                            break;
                                        }
                                        break Label_0616;
                                        n11 = n8;
                                        j = (this.addChildrenToSolver(this.mSystem) ? 1 : 0);
                                        continue;
                                    }
                                    n8 = n11;
                                }
                                catch (Exception obj) {}
                            }
                        }
                    }
                    catch (Exception ex) {}
                    ((Exception)obj).printStackTrace();
                    final PrintStream out = System.out;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("EXCEPTION : ");
                    sb.append(obj);
                    out.println(sb.toString());
                    if (j != 0) {
                        this.updateChildrenFromSolver(this.mSystem, Optimizer.flags);
                    }
                    else {
                        this.updateFromSolver(this.mSystem);
                        for (int k = 0; k < n7; ++k) {
                            obj = this.mChildren.get(k);
                            if (obj.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT && obj.getWidth() < obj.getWrapWidth()) {
                                Optimizer.flags[2] = true;
                                break;
                            }
                            if (obj.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT && obj.getHeight() < obj.getWrapHeight()) {
                                Optimizer.flags[2] = true;
                                break;
                            }
                        }
                    }
                    final int n12 = 0;
                    Label_1061: {
                        int n16;
                        int n17;
                        if (b && n9 < 8 && Optimizer.flags[2]) {
                            int max3 = 0;
                            int max4 = 0;
                            for (int l = 0; l < n7; ++l) {
                                obj = this.mChildren.get(l);
                                max3 = Math.max(max3, obj.mX + obj.getWidth());
                                max4 = Math.max(max4, obj.mY + obj.getHeight());
                            }
                            final int n13 = n12;
                            final int max5 = Math.max(this.mMinWidth, max3);
                            final int max6 = Math.max(this.mMinHeight, max4);
                            int n14 = n13;
                            int n15 = n8;
                            if (dimensionBehaviour2 == DimensionBehaviour.WRAP_CONTENT) {
                                n14 = n13;
                                n15 = n8;
                                if (this.getWidth() < max5) {
                                    this.setWidth(max5);
                                    this.mListDimensionBehaviors[0] = DimensionBehaviour.WRAP_CONTENT;
                                    n15 = 1;
                                    n14 = 1;
                                }
                            }
                            n16 = n14;
                            n8 = n15;
                            n17 = n7;
                            if (dimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                                n16 = n14;
                                n8 = n15;
                                n17 = n7;
                                if (this.getHeight() < max6) {
                                    this.setHeight(max6);
                                    this.mListDimensionBehaviors[1] = DimensionBehaviour.WRAP_CONTENT;
                                    n8 = 1;
                                    j = 1;
                                    break Label_1061;
                                }
                            }
                        }
                        else {
                            n16 = 0;
                            n17 = n7;
                        }
                        j = n16;
                        n7 = n17;
                    }
                    final int max7 = Math.max(this.mMinWidth, this.getWidth());
                    if (max7 > this.getWidth()) {
                        this.setWidth(max7);
                        this.mListDimensionBehaviors[0] = DimensionBehaviour.FIXED;
                        n8 = 1;
                        j = 1;
                    }
                    final int max8 = Math.max(this.mMinHeight, this.getHeight());
                    if (max8 > this.getHeight()) {
                        this.setHeight(max8);
                        this.mListDimensionBehaviors[1] = DimensionBehaviour.FIXED;
                        n8 = 1;
                        j = 1;
                    }
                    if (n8 == 0) {
                        if (this.mListDimensionBehaviors[0] == DimensionBehaviour.WRAP_CONTENT && max > 0 && this.getWidth() > max) {
                            this.mWidthMeasuredTooSmall = true;
                            n8 = 1;
                            this.mListDimensionBehaviors[0] = DimensionBehaviour.FIXED;
                            this.setWidth(max);
                            j = 1;
                        }
                        if (this.mListDimensionBehaviors[1] == DimensionBehaviour.WRAP_CONTENT && max2 > 0 && this.getHeight() > max2) {
                            this.mHeightMeasuredTooSmall = true;
                            this.mListDimensionBehaviors[1] = DimensionBehaviour.FIXED;
                            this.setHeight(max2);
                            j = 1;
                            n8 = 1;
                        }
                    }
                    n5 = n9;
                }
                this.mWidgetGroups.get(n2).updateUnresolvedWidgets();
                n = n8;
                size = n6;
            }
        }
        this.mChildren = mChildren;
        if (this.mParent != null) {
            final int max9 = Math.max(this.mMinWidth, this.getWidth());
            final int max10 = Math.max(this.mMinHeight, this.getHeight());
            this.mSnapshot.applyTo(this);
            this.setWidth(this.mPaddingLeft + max9 + this.mPaddingRight);
            this.setHeight(this.mPaddingTop + max10 + this.mPaddingBottom);
        }
        else {
            this.mX = mx;
            this.mY = my;
        }
        if (n != 0) {
            this.mListDimensionBehaviors[0] = dimensionBehaviour2;
            this.mListDimensionBehaviors[1] = dimensionBehaviour;
        }
        this.resetSolverVariables(this.mSystem.getCache());
        if (this == this.getRootConstraintContainer()) {
            this.updateDrawPosition();
        }
    }
    
    public void optimize() {
        if (!this.optimizeFor(8)) {
            this.analyze(this.mOptimizationLevel);
        }
        this.solveGraph();
    }
    
    public boolean optimizeFor(final int n) {
        return (this.mOptimizationLevel & n) == n;
    }
    
    public void optimizeForDimensions(final int n, final int n2) {
        if (this.mListDimensionBehaviors[0] != DimensionBehaviour.WRAP_CONTENT && this.mResolutionWidth != null) {
            this.mResolutionWidth.resolve(n);
        }
        if (this.mListDimensionBehaviors[1] != DimensionBehaviour.WRAP_CONTENT && this.mResolutionHeight != null) {
            this.mResolutionHeight.resolve(n2);
        }
    }
    
    public void optimizeReset() {
        final int size = this.mChildren.size();
        this.resetResolutionNodes();
        for (int i = 0; i < size; ++i) {
            this.mChildren.get(i).resetResolutionNodes();
        }
    }
    
    public void preOptimize() {
        this.optimizeReset();
        this.analyze(this.mOptimizationLevel);
    }
    
    @Override
    public void reset() {
        this.mSystem.reset();
        this.mPaddingLeft = 0;
        this.mPaddingRight = 0;
        this.mPaddingTop = 0;
        this.mPaddingBottom = 0;
        this.mWidgetGroups.clear();
        this.mSkipSolver = false;
        super.reset();
    }
    
    public void resetGraph() {
        final ResolutionAnchor resolutionNode = this.getAnchor(ConstraintAnchor.Type.LEFT).getResolutionNode();
        final ResolutionAnchor resolutionNode2 = this.getAnchor(ConstraintAnchor.Type.TOP).getResolutionNode();
        resolutionNode.invalidateAnchors();
        resolutionNode2.invalidateAnchors();
        resolutionNode.resolve(null, 0.0f);
        resolutionNode2.resolve(null, 0.0f);
    }
    
    public void setOptimizationLevel(final int mOptimizationLevel) {
        this.mOptimizationLevel = mOptimizationLevel;
    }
    
    public void setPadding(final int mPaddingLeft, final int mPaddingTop, final int mPaddingRight, final int mPaddingBottom) {
        this.mPaddingLeft = mPaddingLeft;
        this.mPaddingTop = mPaddingTop;
        this.mPaddingRight = mPaddingRight;
        this.mPaddingBottom = mPaddingBottom;
    }
    
    public void setRtl(final boolean mIsRtl) {
        this.mIsRtl = mIsRtl;
    }
    
    public void solveGraph() {
        final ResolutionAnchor resolutionNode = this.getAnchor(ConstraintAnchor.Type.LEFT).getResolutionNode();
        final ResolutionAnchor resolutionNode2 = this.getAnchor(ConstraintAnchor.Type.TOP).getResolutionNode();
        resolutionNode.resolve(null, 0.0f);
        resolutionNode2.resolve(null, 0.0f);
    }
    
    public void updateChildrenFromSolver(final LinearSystem linearSystem, final boolean[] array) {
        array[2] = false;
        this.updateFromSolver(linearSystem);
        for (int size = this.mChildren.size(), i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            constraintWidget.updateFromSolver(linearSystem);
            if (constraintWidget.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.getWidth() < constraintWidget.getWrapWidth()) {
                array[2] = true;
            }
            if (constraintWidget.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.getHeight() < constraintWidget.getWrapHeight()) {
                array[2] = true;
            }
        }
    }
}
