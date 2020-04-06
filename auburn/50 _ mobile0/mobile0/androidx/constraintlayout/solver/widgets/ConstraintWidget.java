// 
// Decompiled by Procyon v0.5.36
// 

package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.Cache;
import androidx.constraintlayout.solver.Metrics;
import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.LinearSystem;
import java.util.ArrayList;

public class ConstraintWidget
{
    protected static final int ANCHOR_BASELINE = 4;
    protected static final int ANCHOR_BOTTOM = 3;
    protected static final int ANCHOR_LEFT = 0;
    protected static final int ANCHOR_RIGHT = 1;
    protected static final int ANCHOR_TOP = 2;
    private static final boolean AUTOTAG_CENTER = false;
    public static final int CHAIN_PACKED = 2;
    public static final int CHAIN_SPREAD = 0;
    public static final int CHAIN_SPREAD_INSIDE = 1;
    public static float DEFAULT_BIAS = 0.0f;
    static final int DIMENSION_HORIZONTAL = 0;
    static final int DIMENSION_VERTICAL = 1;
    protected static final int DIRECT = 2;
    public static final int GONE = 8;
    public static final int HORIZONTAL = 0;
    public static final int INVISIBLE = 4;
    public static final int MATCH_CONSTRAINT_PERCENT = 2;
    public static final int MATCH_CONSTRAINT_RATIO = 3;
    public static final int MATCH_CONSTRAINT_RATIO_RESOLVED = 4;
    public static final int MATCH_CONSTRAINT_SPREAD = 0;
    public static final int MATCH_CONSTRAINT_WRAP = 1;
    protected static final int SOLVER = 1;
    public static final int UNKNOWN = -1;
    public static final int VERTICAL = 1;
    public static final int VISIBLE = 0;
    private static final int WRAP = -2;
    protected ArrayList<ConstraintAnchor> mAnchors;
    ConstraintAnchor mBaseline;
    int mBaselineDistance;
    ConstraintWidgetGroup mBelongingGroup;
    ConstraintAnchor mBottom;
    boolean mBottomHasCentered;
    ConstraintAnchor mCenter;
    ConstraintAnchor mCenterX;
    ConstraintAnchor mCenterY;
    private float mCircleConstraintAngle;
    private Object mCompanionWidget;
    private int mContainerItemSkip;
    private String mDebugName;
    protected float mDimensionRatio;
    protected int mDimensionRatioSide;
    int mDistToBottom;
    int mDistToLeft;
    int mDistToRight;
    int mDistToTop;
    private int mDrawHeight;
    private int mDrawWidth;
    private int mDrawX;
    private int mDrawY;
    boolean mGroupsToSolver;
    int mHeight;
    float mHorizontalBiasPercent;
    boolean mHorizontalChainFixedPosition;
    int mHorizontalChainStyle;
    ConstraintWidget mHorizontalNextWidget;
    public int mHorizontalResolution;
    boolean mHorizontalWrapVisited;
    boolean mIsHeightWrapContent;
    boolean mIsWidthWrapContent;
    ConstraintAnchor mLeft;
    boolean mLeftHasCentered;
    protected ConstraintAnchor[] mListAnchors;
    protected DimensionBehaviour[] mListDimensionBehaviors;
    protected ConstraintWidget[] mListNextMatchConstraintsWidget;
    int mMatchConstraintDefaultHeight;
    int mMatchConstraintDefaultWidth;
    int mMatchConstraintMaxHeight;
    int mMatchConstraintMaxWidth;
    int mMatchConstraintMinHeight;
    int mMatchConstraintMinWidth;
    float mMatchConstraintPercentHeight;
    float mMatchConstraintPercentWidth;
    private int[] mMaxDimension;
    protected int mMinHeight;
    protected int mMinWidth;
    protected ConstraintWidget[] mNextChainWidget;
    protected int mOffsetX;
    protected int mOffsetY;
    boolean mOptimizerMeasurable;
    boolean mOptimizerMeasured;
    ConstraintWidget mParent;
    int mRelX;
    int mRelY;
    ResolutionDimension mResolutionHeight;
    ResolutionDimension mResolutionWidth;
    float mResolvedDimensionRatio;
    int mResolvedDimensionRatioSide;
    int[] mResolvedMatchConstraintDefault;
    ConstraintAnchor mRight;
    boolean mRightHasCentered;
    ConstraintAnchor mTop;
    boolean mTopHasCentered;
    private String mType;
    float mVerticalBiasPercent;
    boolean mVerticalChainFixedPosition;
    int mVerticalChainStyle;
    ConstraintWidget mVerticalNextWidget;
    public int mVerticalResolution;
    boolean mVerticalWrapVisited;
    private int mVisibility;
    float[] mWeight;
    int mWidth;
    private int mWrapHeight;
    private int mWrapWidth;
    protected int mX;
    protected int mY;
    
    static {
        ConstraintWidget.DEFAULT_BIAS = 0.5f;
    }
    
    public ConstraintWidget() {
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mResolvedMatchConstraintDefault = new int[2];
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMaxWidth = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintMinHeight = 0;
        this.mMatchConstraintMaxHeight = 0;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        this.mBelongingGroup = null;
        this.mMaxDimension = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE };
        this.mCircleConstraintAngle = 0.0f;
        this.mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
        this.mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
        this.mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
        final ConstraintAnchor mCenter = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
        this.mCenter = mCenter;
        this.mListAnchors = new ConstraintAnchor[] { this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, mCenter };
        this.mAnchors = new ArrayList<ConstraintAnchor>();
        this.mListDimensionBehaviors = new DimensionBehaviour[] { DimensionBehaviour.FIXED, DimensionBehaviour.FIXED };
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mRelX = 0;
        this.mRelY = 0;
        this.mDrawX = 0;
        this.mDrawY = 0;
        this.mDrawWidth = 0;
        this.mDrawHeight = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        final float default_BIAS = ConstraintWidget.DEFAULT_BIAS;
        this.mHorizontalBiasPercent = default_BIAS;
        this.mVerticalBiasPercent = default_BIAS;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mOptimizerMeasurable = false;
        this.mOptimizerMeasured = false;
        this.mGroupsToSolver = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mWeight = new float[] { -1.0f, -1.0f };
        this.mListNextMatchConstraintsWidget = new ConstraintWidget[] { null, null };
        this.mNextChainWidget = new ConstraintWidget[] { null, null };
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        this.addAnchors();
    }
    
    public ConstraintWidget(final int n, final int n2) {
        this(0, 0, n, n2);
    }
    
    public ConstraintWidget(final int mx, final int my, final int mWidth, final int mHeight) {
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mResolvedMatchConstraintDefault = new int[2];
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMaxWidth = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintMinHeight = 0;
        this.mMatchConstraintMaxHeight = 0;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        this.mBelongingGroup = null;
        this.mMaxDimension = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE };
        this.mCircleConstraintAngle = 0.0f;
        this.mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
        this.mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
        this.mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
        final ConstraintAnchor mCenter = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
        this.mCenter = mCenter;
        this.mListAnchors = new ConstraintAnchor[] { this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, mCenter };
        this.mAnchors = new ArrayList<ConstraintAnchor>();
        this.mListDimensionBehaviors = new DimensionBehaviour[] { DimensionBehaviour.FIXED, DimensionBehaviour.FIXED };
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mRelX = 0;
        this.mRelY = 0;
        this.mDrawX = 0;
        this.mDrawY = 0;
        this.mDrawWidth = 0;
        this.mDrawHeight = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        final float default_BIAS = ConstraintWidget.DEFAULT_BIAS;
        this.mHorizontalBiasPercent = default_BIAS;
        this.mVerticalBiasPercent = default_BIAS;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mOptimizerMeasurable = false;
        this.mOptimizerMeasured = false;
        this.mGroupsToSolver = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mWeight = new float[] { -1.0f, -1.0f };
        this.mListNextMatchConstraintsWidget = new ConstraintWidget[] { null, null };
        this.mNextChainWidget = new ConstraintWidget[] { null, null };
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        this.mX = mx;
        this.mY = my;
        this.mWidth = mWidth;
        this.mHeight = mHeight;
        this.addAnchors();
        this.forceUpdateDrawPosition();
    }
    
    private void addAnchors() {
        this.mAnchors.add(this.mLeft);
        this.mAnchors.add(this.mTop);
        this.mAnchors.add(this.mRight);
        this.mAnchors.add(this.mBottom);
        this.mAnchors.add(this.mCenterX);
        this.mAnchors.add(this.mCenterY);
        this.mAnchors.add(this.mCenter);
        this.mAnchors.add(this.mBaseline);
    }
    
    private void applyConstraints(final LinearSystem linearSystem, final boolean b, final SolverVariable solverVariable, final SolverVariable solverVariable2, final DimensionBehaviour dimensionBehaviour, final boolean b2, final ConstraintAnchor constraintAnchor, final ConstraintAnchor constraintAnchor2, int n, int max, int n2, int b3, final float n3, final boolean b4, final boolean b5, int n4, int min, final int n5, final float n6, final boolean b6) {
        final SolverVariable objectVariable = linearSystem.createObjectVariable(constraintAnchor);
        SolverVariable objectVariable2;
        final SolverVariable solverVariable3 = objectVariable2 = linearSystem.createObjectVariable(constraintAnchor2);
        final SolverVariable objectVariable3 = linearSystem.createObjectVariable(constraintAnchor.getTarget());
        final SolverVariable objectVariable4 = linearSystem.createObjectVariable(constraintAnchor2.getTarget());
        if (linearSystem.graphOptimizer && constraintAnchor.getResolutionNode().state == 1 && constraintAnchor2.getResolutionNode().state == 1) {
            if (LinearSystem.getMetrics() != null) {
                final Metrics metrics = LinearSystem.getMetrics();
                ++metrics.resolvedWidgets;
            }
            constraintAnchor.getResolutionNode().addResolvedValue(linearSystem);
            constraintAnchor2.getResolutionNode().addResolvedValue(linearSystem);
            if (!b5 && b) {
                linearSystem.addGreaterThan(solverVariable2, objectVariable2, 0, 6);
            }
            return;
        }
        if (LinearSystem.getMetrics() != null) {
            final Metrics metrics2 = LinearSystem.getMetrics();
            ++metrics2.nonresolvedWidgets;
        }
        final boolean connected = constraintAnchor.isConnected();
        final boolean connected2 = constraintAnchor2.isConnected();
        final boolean connected3 = this.mCenter.isConnected();
        int n7 = 0;
        if (connected) {
            n7 = 0 + 1;
        }
        int n8 = n7;
        if (connected2) {
            n8 = n7 + 1;
        }
        int n9 = n8;
        if (connected3) {
            n9 = n8 + 1;
        }
        int n10;
        if (b4) {
            n10 = 3;
        }
        else {
            n10 = n4;
        }
        n4 = ConstraintWidget$1.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintWidget$DimensionBehaviour[dimensionBehaviour.ordinal()];
        if (n4 != 1) {
            if (n4 != 2) {
                if (n4 != 3) {
                    if (n4 != 4) {
                        n4 = 0;
                    }
                    else if (n10 == 4) {
                        n4 = 0;
                    }
                    else {
                        n4 = 1;
                    }
                }
                else {
                    n4 = 0;
                }
            }
            else {
                n4 = 0;
            }
        }
        else {
            n4 = 0;
        }
        if (this.mVisibility == 8) {
            max = 0;
            n4 = 0;
        }
        if (b6) {
            if (!connected && !connected2 && !connected3) {
                linearSystem.addEquality(objectVariable, n);
            }
            else if (connected && !connected2) {
                linearSystem.addEquality(objectVariable, objectVariable3, constraintAnchor.getMargin(), 6);
            }
        }
        if (n4 == 0) {
            if (b2) {
                linearSystem.addEquality(objectVariable2, objectVariable, 0, 3);
                if (n2 > 0) {
                    linearSystem.addGreaterThan(objectVariable2, objectVariable, n2, 6);
                }
                if (b3 < Integer.MAX_VALUE) {
                    linearSystem.addLowerThan(objectVariable2, objectVariable, b3, 6);
                }
            }
            else {
                linearSystem.addEquality(objectVariable2, objectVariable, max, 6);
            }
            b3 = min;
            max = n5;
        }
        else {
            if ((n = min) == -2) {
                n = max;
            }
            if ((b3 = n5) == -2) {
                b3 = max;
            }
            if (n > 0) {
                linearSystem.addGreaterThan(objectVariable2, objectVariable, n, 6);
                max = Math.max(max, n);
            }
            min = max;
            if (b3 > 0) {
                linearSystem.addLowerThan(objectVariable2, objectVariable, b3, 6);
                min = Math.min(max, b3);
            }
            if (n10 == 1) {
                if (b) {
                    linearSystem.addEquality(objectVariable2, objectVariable, min, 6);
                }
                else if (b5) {
                    linearSystem.addEquality(objectVariable2, objectVariable, min, 4);
                }
                else {
                    linearSystem.addEquality(objectVariable2, objectVariable, min, 1);
                }
            }
            else if (n10 == 2) {
                SolverVariable objectVariable6;
                SolverVariable solverVariable4;
                if (constraintAnchor.getType() != ConstraintAnchor.Type.TOP && constraintAnchor.getType() != ConstraintAnchor.Type.BOTTOM) {
                    final SolverVariable objectVariable5 = linearSystem.createObjectVariable(this.mParent.getAnchor(ConstraintAnchor.Type.LEFT));
                    objectVariable6 = linearSystem.createObjectVariable(this.mParent.getAnchor(ConstraintAnchor.Type.RIGHT));
                    solverVariable4 = objectVariable5;
                }
                else {
                    final SolverVariable objectVariable7 = linearSystem.createObjectVariable(this.mParent.getAnchor(ConstraintAnchor.Type.TOP));
                    final SolverVariable objectVariable8 = linearSystem.createObjectVariable(this.mParent.getAnchor(ConstraintAnchor.Type.BOTTOM));
                    solverVariable4 = objectVariable7;
                    objectVariable6 = objectVariable8;
                }
                linearSystem.addConstraint(linearSystem.createRow().createRowDimensionRatio(objectVariable2, objectVariable, objectVariable6, solverVariable4, n6));
                n4 = 0;
            }
            max = b3;
            if (n4 != 0 && n9 != 2 && !b4) {
                n4 = 0;
                min = (b3 = Math.max(n, min));
                if (max > 0) {
                    b3 = Math.min(max, min);
                }
                linearSystem.addEquality(objectVariable2, objectVariable, b3, 6);
                b3 = n;
            }
            else {
                b3 = n;
            }
        }
        final SolverVariable solverVariable5 = objectVariable3;
        if (b6 && !b5) {
            if (!connected && !connected2 && !connected3) {
                if (b) {
                    linearSystem.addGreaterThan(solverVariable2, objectVariable2, 0, 5);
                }
            }
            else if (connected && !connected2) {
                if (b) {
                    linearSystem.addGreaterThan(solverVariable2, objectVariable2, 0, 5);
                }
            }
            else if (!connected && connected2) {
                linearSystem.addEquality(objectVariable2, objectVariable4, -constraintAnchor2.getMargin(), 6);
                if (b) {
                    linearSystem.addGreaterThan(objectVariable, solverVariable, 0, 5);
                }
            }
            else if (connected && connected2) {
                n = 0;
                min = 0;
                if (n4 != 0) {
                    if (b && n2 == 0) {
                        linearSystem.addGreaterThan(objectVariable2, objectVariable, 0, 6);
                    }
                    if (n10 == 0) {
                        if (max <= 0 && b3 <= 0) {
                            n2 = 6;
                        }
                        else {
                            n = 1;
                            n2 = 4;
                        }
                        linearSystem.addEquality(objectVariable, solverVariable5, constraintAnchor.getMargin(), n2);
                        linearSystem.addEquality(objectVariable2, objectVariable4, -constraintAnchor2.getMargin(), n2);
                        Label_1086: {
                            if (max <= 0) {
                                max = min;
                                if (b3 <= 0) {
                                    break Label_1086;
                                }
                            }
                            max = 1;
                        }
                        n2 = 5;
                    }
                    else if (n10 == 1) {
                        n = 1;
                        max = 1;
                        n2 = 6;
                    }
                    else if (n10 == 3) {
                        n2 = 4;
                        if (!b4) {
                            n = n2;
                            if (this.mResolvedDimensionRatioSide != -1) {
                                n = n2;
                                if (max <= 0) {
                                    n = 6;
                                }
                            }
                        }
                        else {
                            n = n2;
                        }
                        linearSystem.addEquality(objectVariable, solverVariable5, constraintAnchor.getMargin(), n);
                        linearSystem.addEquality(objectVariable2, objectVariable4, -constraintAnchor2.getMargin(), n);
                        n = 1;
                        max = 1;
                        n2 = 5;
                    }
                    else {
                        n = 0;
                        max = 0;
                        n2 = 5;
                    }
                }
                else {
                    n = 0;
                    max = 1;
                    n2 = 5;
                }
                min = 5;
                b3 = 5;
                boolean b9;
                boolean b10;
                if (max != 0) {
                    linearSystem.addCentering(objectVariable, solverVariable5, constraintAnchor.getMargin(), n3, objectVariable4, objectVariable2, constraintAnchor2.getMargin(), n2);
                    final boolean b7 = constraintAnchor.mTarget.mOwner instanceof Barrier;
                    final boolean b8 = constraintAnchor2.mTarget.mOwner instanceof Barrier;
                    if (b7 && !b8) {
                        n2 = 6;
                        b9 = true;
                        max = min;
                        b10 = b;
                    }
                    else {
                        max = min;
                        n2 = b3;
                        b10 = b;
                        b9 = b;
                        if (!b7) {
                            max = min;
                            n2 = b3;
                            b10 = b;
                            b9 = b;
                            if (b8) {
                                max = 6;
                                b10 = true;
                                n2 = b3;
                                b9 = b;
                            }
                        }
                    }
                }
                else {
                    b9 = b;
                    b10 = b;
                    n2 = b3;
                    max = min;
                }
                if (n != 0) {
                    max = 6;
                    n2 = 6;
                }
                if ((n4 == 0 && b10) || n != 0) {
                    linearSystem.addGreaterThan(objectVariable, solverVariable5, constraintAnchor.getMargin(), max);
                }
                if ((n4 == 0 && b9) || n != 0) {
                    linearSystem.addLowerThan(solverVariable3, objectVariable4, -constraintAnchor2.getMargin(), n2);
                }
                if (b) {
                    linearSystem.addGreaterThan(objectVariable, solverVariable, 0, 6);
                    objectVariable2 = solverVariable3;
                }
                else {
                    objectVariable2 = solverVariable3;
                }
            }
            if (b) {
                linearSystem.addGreaterThan(solverVariable2, objectVariable2, 0, 6);
            }
            return;
        }
        if (n9 < 2 && b) {
            linearSystem.addGreaterThan(objectVariable, solverVariable, 0, 6);
            linearSystem.addGreaterThan(solverVariable2, objectVariable2, 0, 6);
        }
    }
    
    private boolean isChainHead(int n) {
        n *= 2;
        if (this.mListAnchors[n].mTarget != null) {
            final ConstraintAnchor mTarget = this.mListAnchors[n].mTarget.mTarget;
            final ConstraintAnchor[] mListAnchors = this.mListAnchors;
            if (mTarget != mListAnchors[n] && mListAnchors[n + 1].mTarget != null && this.mListAnchors[n + 1].mTarget.mTarget == this.mListAnchors[n + 1]) {
                return true;
            }
        }
        return false;
    }
    
    public void addToSolver(final LinearSystem linearSystem) {
        final SolverVariable objectVariable = linearSystem.createObjectVariable(this.mLeft);
        final SolverVariable objectVariable2 = linearSystem.createObjectVariable(this.mRight);
        final SolverVariable objectVariable3 = linearSystem.createObjectVariable(this.mTop);
        final SolverVariable objectVariable4 = linearSystem.createObjectVariable(this.mBottom);
        final SolverVariable objectVariable5 = linearSystem.createObjectVariable(this.mBaseline);
        final ConstraintWidget mParent = this.mParent;
        boolean b2;
        boolean b3;
        boolean b4;
        boolean b5;
        if (mParent != null) {
            final boolean b = mParent != null && mParent.mListDimensionBehaviors[0] == DimensionBehaviour.WRAP_CONTENT;
            final ConstraintWidget mParent2 = this.mParent;
            b2 = (mParent2 != null && mParent2.mListDimensionBehaviors[1] == DimensionBehaviour.WRAP_CONTENT);
            boolean inHorizontalChain;
            if (this.isChainHead(0)) {
                ((ConstraintWidgetContainer)this.mParent).addChain(this, 0);
                inHorizontalChain = true;
            }
            else {
                inHorizontalChain = this.isInHorizontalChain();
            }
            boolean inVerticalChain;
            if (this.isChainHead(1)) {
                ((ConstraintWidgetContainer)this.mParent).addChain(this, 1);
                inVerticalChain = true;
            }
            else {
                inVerticalChain = this.isInVerticalChain();
            }
            if (b && this.mVisibility != 8 && this.mLeft.mTarget == null && this.mRight.mTarget == null) {
                linearSystem.addGreaterThan(linearSystem.createObjectVariable(this.mParent.mRight), objectVariable2, 0, 1);
            }
            if (b2 && this.mVisibility != 8 && this.mTop.mTarget == null && this.mBottom.mTarget == null && this.mBaseline == null) {
                linearSystem.addGreaterThan(linearSystem.createObjectVariable(this.mParent.mBottom), objectVariable4, 0, 1);
            }
            b3 = inHorizontalChain;
            b4 = inVerticalChain;
            b5 = b;
        }
        else {
            b3 = false;
            b4 = false;
            b5 = false;
            b2 = false;
        }
        int n;
        if ((n = this.mWidth) < this.mMinWidth) {
            n = this.mMinWidth;
        }
        int n2;
        if ((n2 = this.mHeight) < this.mMinHeight) {
            n2 = this.mMinHeight;
        }
        final boolean b6 = this.mListDimensionBehaviors[0] != DimensionBehaviour.MATCH_CONSTRAINT;
        final boolean b7 = this.mListDimensionBehaviors[1] != DimensionBehaviour.MATCH_CONSTRAINT;
        int n3 = 0;
        this.mResolvedDimensionRatioSide = this.mDimensionRatioSide;
        final float mDimensionRatio = this.mDimensionRatio;
        this.mResolvedDimensionRatio = mDimensionRatio;
        int mMatchConstraintDefaultWidth = this.mMatchConstraintDefaultWidth;
        int mMatchConstraintDefaultHeight = this.mMatchConstraintDefaultHeight;
        int n8 = 0;
        int n11 = 0;
        int n12 = 0;
        int n13 = 0;
        Label_0867: {
            if (mDimensionRatio > 0.0f && this.mVisibility != 8) {
                final int n4 = 1;
                int n5 = mMatchConstraintDefaultWidth;
                if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT && (n5 = mMatchConstraintDefaultWidth) == 0) {
                    n5 = 3;
                }
                int n6 = mMatchConstraintDefaultHeight;
                if (this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT && (n6 = mMatchConstraintDefaultHeight) == 0) {
                    n6 = 3;
                }
                if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT && this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT && n5 == 3 && n6 == 3) {
                    this.setupDimensionRatio(b5, b2, b6, b7);
                    mMatchConstraintDefaultHeight = n6;
                    mMatchConstraintDefaultWidth = n5;
                    n3 = n4;
                }
                else if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT && n5 == 3) {
                    this.mResolvedDimensionRatioSide = 0;
                    final int n7 = (int)(this.mResolvedDimensionRatio * this.mHeight);
                    if (this.mListDimensionBehaviors[1] != DimensionBehaviour.MATCH_CONSTRAINT) {
                        n8 = n7;
                        final int n9 = n6;
                        final int n10 = 4;
                        n3 = 0;
                        n11 = n2;
                        n12 = n9;
                        n13 = n10;
                        break Label_0867;
                    }
                    final int n14 = n2;
                    n12 = n6;
                    n13 = n5;
                    final int n15 = 1;
                    n8 = n7;
                    n11 = n14;
                    n3 = n15;
                    break Label_0867;
                }
                else {
                    mMatchConstraintDefaultHeight = n6;
                    mMatchConstraintDefaultWidth = n5;
                    n3 = n4;
                    if (this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT) {
                        mMatchConstraintDefaultHeight = n6;
                        mMatchConstraintDefaultWidth = n5;
                        n3 = n4;
                        if (n6 == 3) {
                            this.mResolvedDimensionRatioSide = 1;
                            if (this.mDimensionRatioSide == -1) {
                                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                            }
                            final int n16 = (int)(this.mResolvedDimensionRatio * this.mWidth);
                            if (this.mListDimensionBehaviors[0] != DimensionBehaviour.MATCH_CONSTRAINT) {
                                final int n17 = n;
                                n11 = n16;
                                final int n18 = 4;
                                n13 = n5;
                                n3 = 0;
                                n8 = n17;
                                n12 = n18;
                                break Label_0867;
                            }
                            final int n19 = n;
                            final int n20 = n16;
                            n12 = n6;
                            final int n21 = n5;
                            final int n22 = 1;
                            n8 = n19;
                            n11 = n20;
                            n13 = n21;
                            n3 = n22;
                            break Label_0867;
                        }
                    }
                }
            }
            n8 = n;
            n11 = n2;
            n12 = mMatchConstraintDefaultHeight;
            n13 = mMatchConstraintDefaultWidth;
        }
        final int[] mResolvedMatchConstraintDefault = this.mResolvedMatchConstraintDefault;
        mResolvedMatchConstraintDefault[0] = n13;
        mResolvedMatchConstraintDefault[1] = n12;
        boolean b8 = false;
        Label_0919: {
            if (n3 != 0) {
                final int mResolvedDimensionRatioSide = this.mResolvedDimensionRatioSide;
                if (mResolvedDimensionRatioSide == 0 || mResolvedDimensionRatioSide == -1) {
                    b8 = true;
                    break Label_0919;
                }
            }
            b8 = false;
        }
        final boolean b9 = this.mListDimensionBehaviors[0] == DimensionBehaviour.WRAP_CONTENT && this instanceof ConstraintWidgetContainer;
        boolean b10 = !this.mCenter.isConnected();
        if (this.mHorizontalResolution != 2) {
            final ConstraintWidget mParent3 = this.mParent;
            SolverVariable objectVariable6;
            if (mParent3 != null) {
                objectVariable6 = linearSystem.createObjectVariable(mParent3.mRight);
            }
            else {
                objectVariable6 = null;
            }
            final ConstraintWidget mParent4 = this.mParent;
            SolverVariable objectVariable7;
            if (mParent4 != null) {
                objectVariable7 = linearSystem.createObjectVariable(mParent4.mLeft);
            }
            else {
                objectVariable7 = null;
            }
            this.applyConstraints(linearSystem, b5, objectVariable7, objectVariable6, this.mListDimensionBehaviors[0], b9, this.mLeft, this.mRight, this.mX, n8, this.mMinWidth, this.mMaxDimension[0], this.mHorizontalBiasPercent, b8, b3, n13, this.mMatchConstraintMinWidth, this.mMatchConstraintMaxWidth, this.mMatchConstraintPercentWidth, b10);
        }
        final SolverVariable solverVariable = objectVariable2;
        final SolverVariable solverVariable2 = objectVariable3;
        final SolverVariable solverVariable3 = objectVariable4;
        if (this.mVerticalResolution == 2) {
            return;
        }
        final boolean b11 = this.mListDimensionBehaviors[1] == DimensionBehaviour.WRAP_CONTENT && this instanceof ConstraintWidgetContainer;
        boolean b12 = false;
        Label_1180: {
            if (n3 != 0) {
                final int mResolvedDimensionRatioSide2 = this.mResolvedDimensionRatioSide;
                if (mResolvedDimensionRatioSide2 == 1 || mResolvedDimensionRatioSide2 == -1) {
                    b12 = true;
                    break Label_1180;
                }
            }
            b12 = false;
        }
        if (this.mBaselineDistance > 0) {
            if (this.mBaseline.getResolutionNode().state == 1) {
                this.mBaseline.getResolutionNode().addResolvedValue(linearSystem);
            }
            else {
                linearSystem.addEquality(objectVariable5, solverVariable2, this.getBaselineDistance(), 6);
                if (this.mBaseline.mTarget != null) {
                    linearSystem.addEquality(objectVariable5, linearSystem.createObjectVariable(this.mBaseline.mTarget), 0, 6);
                    b10 = false;
                }
            }
        }
        final SolverVariable solverVariable4 = solverVariable2;
        final ConstraintWidget mParent5 = this.mParent;
        SolverVariable objectVariable8;
        if (mParent5 != null) {
            objectVariable8 = linearSystem.createObjectVariable(mParent5.mBottom);
        }
        else {
            objectVariable8 = null;
        }
        final ConstraintWidget mParent6 = this.mParent;
        SolverVariable objectVariable9;
        if (mParent6 != null) {
            objectVariable9 = linearSystem.createObjectVariable(mParent6.mTop);
        }
        else {
            objectVariable9 = null;
        }
        this.applyConstraints(linearSystem, b2, objectVariable9, objectVariable8, this.mListDimensionBehaviors[1], b11, this.mTop, this.mBottom, this.mY, n11, this.mMinHeight, this.mMaxDimension[1], this.mVerticalBiasPercent, b12, b4, n12, this.mMatchConstraintMinHeight, this.mMatchConstraintMaxHeight, this.mMatchConstraintPercentHeight, b10);
        if (n3 != 0) {
            if (this.mResolvedDimensionRatioSide == 1) {
                linearSystem.addRatio(solverVariable3, solverVariable4, solverVariable, objectVariable, this.mResolvedDimensionRatio, 6);
            }
            else {
                linearSystem.addRatio(solverVariable, objectVariable, solverVariable3, solverVariable4, this.mResolvedDimensionRatio, 6);
            }
        }
        if (this.mCenter.isConnected()) {
            linearSystem.addCenterPoint(this, this.mCenter.getTarget().getOwner(), (float)Math.toRadians(this.mCircleConstraintAngle + 90.0f), this.mCenter.getMargin());
        }
    }
    
    public boolean allowedInBarrier() {
        return this.mVisibility != 8;
    }
    
    public void analyze(final int n) {
        Optimizer.analyze(n, this);
    }
    
    public void connect(final ConstraintAnchor.Type type, final ConstraintWidget constraintWidget, final ConstraintAnchor.Type type2) {
        this.connect(type, constraintWidget, type2, 0, ConstraintAnchor.Strength.STRONG);
    }
    
    public void connect(final ConstraintAnchor.Type type, final ConstraintWidget constraintWidget, final ConstraintAnchor.Type type2, final int n) {
        this.connect(type, constraintWidget, type2, n, ConstraintAnchor.Strength.STRONG);
    }
    
    public void connect(final ConstraintAnchor.Type type, final ConstraintWidget constraintWidget, final ConstraintAnchor.Type type2, final int n, final ConstraintAnchor.Strength strength) {
        this.connect(type, constraintWidget, type2, n, strength, 0);
    }
    
    public void connect(ConstraintAnchor.Type right, final ConstraintWidget constraintWidget, final ConstraintAnchor.Type type, int n, final ConstraintAnchor.Strength strength, final int n2) {
        Label_0422: {
            if (right != ConstraintAnchor.Type.CENTER) {
                break Label_0422;
            }
            if (type == ConstraintAnchor.Type.CENTER) {
                final ConstraintAnchor anchor = this.getAnchor(ConstraintAnchor.Type.LEFT);
                final ConstraintAnchor anchor2 = this.getAnchor(ConstraintAnchor.Type.RIGHT);
                final ConstraintAnchor anchor3 = this.getAnchor(ConstraintAnchor.Type.TOP);
                final ConstraintAnchor anchor4 = this.getAnchor(ConstraintAnchor.Type.BOTTOM);
                final int n3 = 0;
                final int n4 = 0;
                Label_0124: {
                    if (anchor != null) {
                        n = n3;
                        if (anchor.isConnected()) {
                            break Label_0124;
                        }
                    }
                    if (anchor2 != null && anchor2.isConnected()) {
                        n = n3;
                    }
                    else {
                        this.connect(ConstraintAnchor.Type.LEFT, constraintWidget, ConstraintAnchor.Type.LEFT, 0, strength, n2);
                        this.connect(ConstraintAnchor.Type.RIGHT, constraintWidget, ConstraintAnchor.Type.RIGHT, 0, strength, n2);
                        n = 1;
                    }
                }
                int n5 = 0;
                Label_0194: {
                    if (anchor3 != null) {
                        n5 = n4;
                        if (anchor3.isConnected()) {
                            break Label_0194;
                        }
                    }
                    if (anchor4 != null && anchor4.isConnected()) {
                        n5 = n4;
                    }
                    else {
                        this.connect(ConstraintAnchor.Type.TOP, constraintWidget, ConstraintAnchor.Type.TOP, 0, strength, n2);
                        this.connect(ConstraintAnchor.Type.BOTTOM, constraintWidget, ConstraintAnchor.Type.BOTTOM, 0, strength, n2);
                        n5 = 1;
                    }
                }
                if (n != 0 && n5 != 0) {
                    this.getAnchor(ConstraintAnchor.Type.CENTER).connect(constraintWidget.getAnchor(ConstraintAnchor.Type.CENTER), 0, n2);
                    return;
                }
                if (n != 0) {
                    this.getAnchor(ConstraintAnchor.Type.CENTER_X).connect(constraintWidget.getAnchor(ConstraintAnchor.Type.CENTER_X), 0, n2);
                    return;
                }
                if (n5 != 0) {
                    this.getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(constraintWidget.getAnchor(ConstraintAnchor.Type.CENTER_Y), 0, n2);
                }
                return;
            }
            if (type != ConstraintAnchor.Type.LEFT && type != ConstraintAnchor.Type.RIGHT) {
                if (type == ConstraintAnchor.Type.TOP || type == ConstraintAnchor.Type.BOTTOM) {
                    this.connect(ConstraintAnchor.Type.TOP, constraintWidget, type, 0, strength, n2);
                    this.connect(ConstraintAnchor.Type.BOTTOM, constraintWidget, type, 0, strength, n2);
                    this.getAnchor(ConstraintAnchor.Type.CENTER).connect(constraintWidget.getAnchor(type), 0, n2);
                }
                return;
            }
            this.connect(ConstraintAnchor.Type.LEFT, constraintWidget, type, 0, strength, n2);
            right = ConstraintAnchor.Type.RIGHT;
            try {
                this.connect(right, constraintWidget, type, 0, strength, n2);
                this.getAnchor(ConstraintAnchor.Type.CENTER).connect(constraintWidget.getAnchor(type), 0, n2);
                Label_0990: {
                    return;
                }
                // iftrue(Label_0891:, right == ConstraintAnchor.Type.TOP || right == ConstraintAnchor.Type.BOTTOM)
                // iftrue(Label_0501:, right != ConstraintAnchor.Type.CENTER_X || type != ConstraintAnchor.Type.LEFT && type != ConstraintAnchor.Type.RIGHT)
                // iftrue(Label_0836:, right == ConstraintAnchor.Type.LEFT || right == ConstraintAnchor.Type.RIGHT)
                // iftrue(Label_0576:, right != ConstraintAnchor.Type.CENTER_Y || type != ConstraintAnchor.Type.TOP && type != ConstraintAnchor.Type.BOTTOM)
                // iftrue(Label_0732:, right != ConstraintAnchor.Type.CENTER_Y || type != ConstraintAnchor.Type.CENTER_Y)
                // iftrue(Label_0793:, anchor5 == null)
                // iftrue(Label_0856:, anchor9.getTarget() == anchor11)
                // iftrue(Label_0833:, !anchor6.isConnected())
                // iftrue(Label_0799:, right != ConstraintAnchor.Type.BASELINE)
                // iftrue(Label_0785:, anchor7 == null)
                // iftrue(Label_0927:, anchor12.getTarget() == anchor11)
                // iftrue(Label_0907:, anchor10 == null)
                // iftrue(Label_0990:, !anchor16.isValidConnection(anchor11))
                // iftrue(Label_0962:, !anchor17.isConnected())
                // iftrue(Label_0654:, right != ConstraintAnchor.Type.CENTER_X || type != ConstraintAnchor.Type.CENTER_X)
            Label_0962:
                while (true) {
                    ConstraintAnchor opposite2 = null;
                    ConstraintAnchor anchor17 = null;
                    Block_35: {
                        ConstraintAnchor anchor5 = null;
                        ConstraintAnchor opposite;
                        ConstraintAnchor anchor6;
                        ConstraintAnchor anchor7;
                        ConstraintAnchor anchor8;
                        ConstraintAnchor anchor9;
                        ConstraintAnchor anchor10;
                        ConstraintAnchor anchor11;
                        ConstraintAnchor anchor12 = null;
                        ConstraintAnchor anchor13;
                        ConstraintAnchor anchor14;
                        ConstraintAnchor anchor15;
                        ConstraintAnchor anchor16;
                        Block_28_Outer:Block_36_Outer:Label_0927_Outer:Block_26_Outer:
                        while (true) {
                            Label_0856: {
                                while (true) {
                                    Label_0833: {
                                        while (true) {
                                        Block_37:
                                            while (true) {
                                                Label_0443: {
                                                Label_0907:
                                                    while (true) {
                                                        Block_27: {
                                                            Label_0793: {
                                                                Block_25: {
                                                                Block_34_Outer:
                                                                    while (true) {
                                                                        while (true) {
                                                                            while (true) {
                                                                                anchor5.reset();
                                                                                break Label_0793;
                                                                                Label_0799:
                                                                                Label_0785: {
                                                                                    while (true) {
                                                                                        Label_0816: {
                                                                                            break Label_0816;
                                                                                            opposite.reset();
                                                                                            anchor6.reset();
                                                                                            break Label_0833;
                                                                                            break Label_0443;
                                                                                            this.getAnchor(ConstraintAnchor.Type.LEFT).connect(constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT), 0, n2);
                                                                                            this.getAnchor(ConstraintAnchor.Type.RIGHT).connect(constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT), 0, n2);
                                                                                            this.getAnchor(ConstraintAnchor.Type.CENTER_X).connect(constraintWidget.getAnchor(type), 0, n2);
                                                                                            return;
                                                                                            anchor7.reset();
                                                                                            break Label_0785;
                                                                                            anchor8 = constraintWidget.getAnchor(type);
                                                                                            this.getAnchor(ConstraintAnchor.Type.TOP).connect(anchor8, 0, n2);
                                                                                            this.getAnchor(ConstraintAnchor.Type.BOTTOM).connect(anchor8, 0, n2);
                                                                                            this.getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(anchor8, 0, n2);
                                                                                            return;
                                                                                            anchor9.reset();
                                                                                            break Label_0856;
                                                                                            anchor10.reset();
                                                                                            break Label_0907;
                                                                                        }
                                                                                        break Label_0833;
                                                                                        Label_0501:
                                                                                        continue Block_34_Outer;
                                                                                    }
                                                                                    Label_0654:
                                                                                    break Block_25;
                                                                                }
                                                                                continue Block_28_Outer;
                                                                            }
                                                                            Label_0836:
                                                                            anchor9 = this.getAnchor(ConstraintAnchor.Type.CENTER);
                                                                            continue Block_36_Outer;
                                                                        }
                                                                        opposite = this.getAnchor(right).getOpposite();
                                                                        anchor6 = this.getAnchor(ConstraintAnchor.Type.CENTER_Y);
                                                                        continue Block_28_Outer;
                                                                    }
                                                                    break Block_27;
                                                                }
                                                                this.getAnchor(ConstraintAnchor.Type.TOP).connect(constraintWidget.getAnchor(ConstraintAnchor.Type.TOP), 0, n2);
                                                                this.getAnchor(ConstraintAnchor.Type.BOTTOM).connect(constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM), 0, n2);
                                                                this.getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(constraintWidget.getAnchor(type), 0, n2);
                                                                return;
                                                            }
                                                            n = 0;
                                                            break Label_0962;
                                                        }
                                                        anchor7 = this.getAnchor(ConstraintAnchor.Type.TOP);
                                                        anchor5 = this.getAnchor(ConstraintAnchor.Type.BOTTOM);
                                                        continue Block_36_Outer;
                                                    }
                                                    anchor12 = this.getAnchor(ConstraintAnchor.Type.CENTER);
                                                    break Block_37;
                                                }
                                                anchor13 = this.getAnchor(ConstraintAnchor.Type.LEFT);
                                                anchor14 = constraintWidget.getAnchor(type);
                                                anchor15 = this.getAnchor(ConstraintAnchor.Type.RIGHT);
                                                anchor13.connect(anchor14, 0, n2);
                                                anchor15.connect(anchor14, 0, n2);
                                                this.getAnchor(ConstraintAnchor.Type.CENTER_X).connect(anchor14, 0, n2);
                                                return;
                                                Label_0891:
                                                anchor10 = this.getAnchor(ConstraintAnchor.Type.BASELINE);
                                                continue Label_0927_Outer;
                                            }
                                            anchor12.reset();
                                            continue Block_26_Outer;
                                        }
                                        anchor16.connect(anchor11, n, strength, n2);
                                        anchor11.getOwner().connectedTo(anchor16.getOwner());
                                        return;
                                    }
                                    continue Label_0962;
                                    Label_0732:
                                    anchor16 = this.getAnchor(right);
                                    anchor11 = constraintWidget.getAnchor(type);
                                    continue;
                                }
                            }
                            opposite2 = this.getAnchor(right).getOpposite();
                            anchor17 = this.getAnchor(ConstraintAnchor.Type.CENTER_X);
                            break Block_35;
                            Label_0576:
                            continue;
                        }
                    }
                    opposite2.reset();
                    anchor17.reset();
                    continue Label_0962;
                }
            }
            finally {
                while (true) {}
            }
        }
    }
    
    public void connect(final ConstraintAnchor constraintAnchor, final ConstraintAnchor constraintAnchor2, final int n) {
        this.connect(constraintAnchor, constraintAnchor2, n, ConstraintAnchor.Strength.STRONG, 0);
    }
    
    public void connect(final ConstraintAnchor constraintAnchor, final ConstraintAnchor constraintAnchor2, final int n, final int n2) {
        this.connect(constraintAnchor, constraintAnchor2, n, ConstraintAnchor.Strength.STRONG, n2);
    }
    
    public void connect(final ConstraintAnchor constraintAnchor, final ConstraintAnchor constraintAnchor2, final int n, final ConstraintAnchor.Strength strength, final int n2) {
        if (constraintAnchor.getOwner() == this) {
            this.connect(constraintAnchor.getType(), constraintAnchor2.getOwner(), constraintAnchor2.getType(), n, strength, n2);
        }
    }
    
    public void connectCircularConstraint(final ConstraintWidget constraintWidget, final float mCircleConstraintAngle, final int n) {
        this.immediateConnect(ConstraintAnchor.Type.CENTER, constraintWidget, ConstraintAnchor.Type.CENTER, n, 0);
        this.mCircleConstraintAngle = mCircleConstraintAngle;
    }
    
    public void connectedTo(final ConstraintWidget constraintWidget) {
    }
    
    public void createObjectVariables(final LinearSystem linearSystem) {
        linearSystem.createObjectVariable(this.mLeft);
        linearSystem.createObjectVariable(this.mTop);
        linearSystem.createObjectVariable(this.mRight);
        linearSystem.createObjectVariable(this.mBottom);
        if (this.mBaselineDistance > 0) {
            linearSystem.createObjectVariable(this.mBaseline);
        }
    }
    
    public void disconnectUnlockedWidget(final ConstraintWidget constraintWidget) {
        final ArrayList<ConstraintAnchor> anchors = this.getAnchors();
        for (int i = 0; i < anchors.size(); ++i) {
            final ConstraintAnchor constraintAnchor = anchors.get(i);
            if (constraintAnchor.isConnected() && constraintAnchor.getTarget().getOwner() == constraintWidget && constraintAnchor.getConnectionCreator() == 2) {
                constraintAnchor.reset();
            }
        }
    }
    
    public void disconnectWidget(final ConstraintWidget constraintWidget) {
        final ArrayList<ConstraintAnchor> anchors = this.getAnchors();
        for (int i = 0; i < anchors.size(); ++i) {
            final ConstraintAnchor constraintAnchor = anchors.get(i);
            if (constraintAnchor.isConnected() && constraintAnchor.getTarget().getOwner() == constraintWidget) {
                constraintAnchor.reset();
            }
        }
    }
    
    public void forceUpdateDrawPosition() {
        final int mx = this.mX;
        final int my = this.mY;
        final int mx2 = this.mX;
        final int mWidth = this.mWidth;
        final int my2 = this.mY;
        final int mHeight = this.mHeight;
        this.mDrawX = mx;
        this.mDrawY = my;
        this.mDrawWidth = mx2 + mWidth - mx;
        this.mDrawHeight = my2 + mHeight - my;
    }
    
    public ConstraintAnchor getAnchor(final ConstraintAnchor.Type type) {
        switch (ConstraintWidget$1.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[type.ordinal()]) {
            default: {
                throw new AssertionError((Object)type.name());
            }
            case 9: {
                return null;
            }
            case 8: {
                return this.mCenterY;
            }
            case 7: {
                return this.mCenterX;
            }
            case 6: {
                return this.mCenter;
            }
            case 5: {
                return this.mBaseline;
            }
            case 4: {
                return this.mBottom;
            }
            case 3: {
                return this.mRight;
            }
            case 2: {
                return this.mTop;
            }
            case 1: {
                return this.mLeft;
            }
        }
    }
    
    public ArrayList<ConstraintAnchor> getAnchors() {
        return this.mAnchors;
    }
    
    public int getBaselineDistance() {
        return this.mBaselineDistance;
    }
    
    public float getBiasPercent(final int n) {
        if (n == 0) {
            return this.mHorizontalBiasPercent;
        }
        if (n == 1) {
            return this.mVerticalBiasPercent;
        }
        return -1.0f;
    }
    
    public int getBottom() {
        return this.getY() + this.mHeight;
    }
    
    public Object getCompanionWidget() {
        return this.mCompanionWidget;
    }
    
    public int getContainerItemSkip() {
        return this.mContainerItemSkip;
    }
    
    public String getDebugName() {
        return this.mDebugName;
    }
    
    public DimensionBehaviour getDimensionBehaviour(final int n) {
        if (n == 0) {
            return this.getHorizontalDimensionBehaviour();
        }
        if (n == 1) {
            return this.getVerticalDimensionBehaviour();
        }
        return null;
    }
    
    public float getDimensionRatio() {
        return this.mDimensionRatio;
    }
    
    public int getDimensionRatioSide() {
        return this.mDimensionRatioSide;
    }
    
    public int getDrawBottom() {
        return this.getDrawY() + this.mDrawHeight;
    }
    
    public int getDrawHeight() {
        return this.mDrawHeight;
    }
    
    public int getDrawRight() {
        return this.getDrawX() + this.mDrawWidth;
    }
    
    public int getDrawWidth() {
        return this.mDrawWidth;
    }
    
    public int getDrawX() {
        return this.mDrawX + this.mOffsetX;
    }
    
    public int getDrawY() {
        return this.mDrawY + this.mOffsetY;
    }
    
    public int getHeight() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mHeight;
    }
    
    public float getHorizontalBiasPercent() {
        return this.mHorizontalBiasPercent;
    }
    
    public ConstraintWidget getHorizontalChainControlWidget() {
        ConstraintWidget constraintWidget = null;
        ConstraintWidget constraintWidget2 = null;
        if (this.isInHorizontalChain()) {
            ConstraintWidget constraintWidget3 = this;
            while (true) {
                constraintWidget = constraintWidget2;
                if (constraintWidget2 != null) {
                    break;
                }
                constraintWidget = constraintWidget2;
                if (constraintWidget3 == null) {
                    break;
                }
                final ConstraintAnchor anchor = constraintWidget3.getAnchor(ConstraintAnchor.Type.LEFT);
                ConstraintAnchor target = null;
                ConstraintAnchor target2;
                if (anchor == null) {
                    target2 = null;
                }
                else {
                    target2 = anchor.getTarget();
                }
                ConstraintWidget owner;
                if (target2 == null) {
                    owner = null;
                }
                else {
                    owner = target2.getOwner();
                }
                if (owner == this.getParent()) {
                    constraintWidget = constraintWidget3;
                    break;
                }
                if (owner != null) {
                    target = owner.getAnchor(ConstraintAnchor.Type.RIGHT).getTarget();
                }
                if (target != null && target.getOwner() != constraintWidget3) {
                    constraintWidget2 = constraintWidget3;
                }
                else {
                    constraintWidget3 = owner;
                }
            }
        }
        return constraintWidget;
    }
    
    public int getHorizontalChainStyle() {
        return this.mHorizontalChainStyle;
    }
    
    public DimensionBehaviour getHorizontalDimensionBehaviour() {
        return this.mListDimensionBehaviors[0];
    }
    
    public int getInternalDrawBottom() {
        return this.mDrawY + this.mDrawHeight;
    }
    
    public int getInternalDrawRight() {
        return this.mDrawX + this.mDrawWidth;
    }
    
    int getInternalDrawX() {
        return this.mDrawX;
    }
    
    int getInternalDrawY() {
        return this.mDrawY;
    }
    
    public int getLeft() {
        return this.getX();
    }
    
    public int getLength(final int n) {
        if (n == 0) {
            return this.getWidth();
        }
        if (n == 1) {
            return this.getHeight();
        }
        return 0;
    }
    
    public int getMaxHeight() {
        return this.mMaxDimension[1];
    }
    
    public int getMaxWidth() {
        return this.mMaxDimension[0];
    }
    
    public int getMinHeight() {
        return this.mMinHeight;
    }
    
    public int getMinWidth() {
        return this.mMinWidth;
    }
    
    public int getOptimizerWrapHeight() {
        int b = this.mHeight;
        if (this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT) {
            int mHeight;
            if (this.mMatchConstraintDefaultHeight == 1) {
                mHeight = Math.max(this.mMatchConstraintMinHeight, b);
            }
            else if (this.mMatchConstraintMinHeight > 0) {
                mHeight = this.mMatchConstraintMinHeight;
                this.mHeight = mHeight;
            }
            else {
                mHeight = 0;
            }
            final int mMatchConstraintMaxHeight = this.mMatchConstraintMaxHeight;
            b = mHeight;
            if (mMatchConstraintMaxHeight > 0 && mMatchConstraintMaxHeight < (b = mHeight)) {
                b = this.mMatchConstraintMaxHeight;
            }
        }
        return b;
    }
    
    public int getOptimizerWrapWidth() {
        int b = this.mWidth;
        if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT) {
            int mWidth;
            if (this.mMatchConstraintDefaultWidth == 1) {
                mWidth = Math.max(this.mMatchConstraintMinWidth, b);
            }
            else if (this.mMatchConstraintMinWidth > 0) {
                mWidth = this.mMatchConstraintMinWidth;
                this.mWidth = mWidth;
            }
            else {
                mWidth = 0;
            }
            final int mMatchConstraintMaxWidth = this.mMatchConstraintMaxWidth;
            b = mWidth;
            if (mMatchConstraintMaxWidth > 0 && mMatchConstraintMaxWidth < (b = mWidth)) {
                b = this.mMatchConstraintMaxWidth;
            }
        }
        return b;
    }
    
    public ConstraintWidget getParent() {
        return this.mParent;
    }
    
    int getRelativePositioning(final int n) {
        if (n == 0) {
            return this.mRelX;
        }
        if (n == 1) {
            return this.mRelY;
        }
        return 0;
    }
    
    public ResolutionDimension getResolutionHeight() {
        if (this.mResolutionHeight == null) {
            this.mResolutionHeight = new ResolutionDimension();
        }
        return this.mResolutionHeight;
    }
    
    public ResolutionDimension getResolutionWidth() {
        if (this.mResolutionWidth == null) {
            this.mResolutionWidth = new ResolutionDimension();
        }
        return this.mResolutionWidth;
    }
    
    public int getRight() {
        return this.getX() + this.mWidth;
    }
    
    public WidgetContainer getRootWidgetContainer() {
        ConstraintWidget parent;
        for (parent = this; parent.getParent() != null; parent = parent.getParent()) {}
        if (parent instanceof WidgetContainer) {
            return (WidgetContainer)parent;
        }
        return null;
    }
    
    protected int getRootX() {
        return this.mX + this.mOffsetX;
    }
    
    protected int getRootY() {
        return this.mY + this.mOffsetY;
    }
    
    public int getTop() {
        return this.getY();
    }
    
    public String getType() {
        return this.mType;
    }
    
    public float getVerticalBiasPercent() {
        return this.mVerticalBiasPercent;
    }
    
    public ConstraintWidget getVerticalChainControlWidget() {
        ConstraintWidget constraintWidget = null;
        ConstraintWidget constraintWidget2 = null;
        if (this.isInVerticalChain()) {
            ConstraintWidget constraintWidget3 = this;
            while (true) {
                constraintWidget = constraintWidget2;
                if (constraintWidget2 != null) {
                    break;
                }
                constraintWidget = constraintWidget2;
                if (constraintWidget3 == null) {
                    break;
                }
                final ConstraintAnchor anchor = constraintWidget3.getAnchor(ConstraintAnchor.Type.TOP);
                ConstraintAnchor target = null;
                ConstraintAnchor target2;
                if (anchor == null) {
                    target2 = null;
                }
                else {
                    target2 = anchor.getTarget();
                }
                ConstraintWidget owner;
                if (target2 == null) {
                    owner = null;
                }
                else {
                    owner = target2.getOwner();
                }
                if (owner == this.getParent()) {
                    constraintWidget = constraintWidget3;
                    break;
                }
                if (owner != null) {
                    target = owner.getAnchor(ConstraintAnchor.Type.BOTTOM).getTarget();
                }
                if (target != null && target.getOwner() != constraintWidget3) {
                    constraintWidget2 = constraintWidget3;
                }
                else {
                    constraintWidget3 = owner;
                }
            }
        }
        return constraintWidget;
    }
    
    public int getVerticalChainStyle() {
        return this.mVerticalChainStyle;
    }
    
    public DimensionBehaviour getVerticalDimensionBehaviour() {
        return this.mListDimensionBehaviors[1];
    }
    
    public int getVisibility() {
        return this.mVisibility;
    }
    
    public int getWidth() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mWidth;
    }
    
    public int getWrapHeight() {
        return this.mWrapHeight;
    }
    
    public int getWrapWidth() {
        return this.mWrapWidth;
    }
    
    public int getX() {
        return this.mX;
    }
    
    public int getY() {
        return this.mY;
    }
    
    public boolean hasAncestor(final ConstraintWidget constraintWidget) {
        final ConstraintWidget parent = this.getParent();
        if (parent == constraintWidget) {
            return true;
        }
        ConstraintWidget parent2;
        if ((parent2 = parent) == constraintWidget.getParent()) {
            return false;
        }
        while (parent2 != null) {
            if (parent2 == constraintWidget) {
                return true;
            }
            if (parent2 == constraintWidget.getParent()) {
                return true;
            }
            parent2 = parent2.getParent();
        }
        return false;
    }
    
    public boolean hasBaseline() {
        return this.mBaselineDistance > 0;
    }
    
    public void immediateConnect(final ConstraintAnchor.Type type, final ConstraintWidget constraintWidget, final ConstraintAnchor.Type type2, final int n, final int n2) {
        this.getAnchor(type).connect(constraintWidget.getAnchor(type2), n, n2, ConstraintAnchor.Strength.STRONG, 0, true);
    }
    
    public boolean isFullyResolved() {
        return this.mLeft.getResolutionNode().state == 1 && this.mRight.getResolutionNode().state == 1 && this.mTop.getResolutionNode().state == 1 && this.mBottom.getResolutionNode().state == 1;
    }
    
    public boolean isHeightWrapContent() {
        return this.mIsHeightWrapContent;
    }
    
    public boolean isInHorizontalChain() {
        return (this.mLeft.mTarget != null && this.mLeft.mTarget.mTarget == this.mLeft) || (this.mRight.mTarget != null && this.mRight.mTarget.mTarget == this.mRight);
    }
    
    public boolean isInVerticalChain() {
        return (this.mTop.mTarget != null && this.mTop.mTarget.mTarget == this.mTop) || (this.mBottom.mTarget != null && this.mBottom.mTarget.mTarget == this.mBottom);
    }
    
    public boolean isInsideConstraintLayout() {
        ConstraintWidget constraintWidget;
        if ((constraintWidget = this.getParent()) == null) {
            return false;
        }
        while (constraintWidget != null) {
            if (constraintWidget instanceof ConstraintWidgetContainer) {
                return true;
            }
            constraintWidget = constraintWidget.getParent();
        }
        return false;
    }
    
    public boolean isRoot() {
        return this.mParent == null;
    }
    
    public boolean isRootContainer() {
        if (this instanceof ConstraintWidgetContainer) {
            final ConstraintWidget mParent = this.mParent;
            if (mParent == null || !(mParent instanceof ConstraintWidgetContainer)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isSpreadHeight() {
        final int mMatchConstraintDefaultHeight = this.mMatchConstraintDefaultHeight;
        boolean b = true;
        if (mMatchConstraintDefaultHeight != 0 || this.mDimensionRatio != 0.0f || this.mMatchConstraintMinHeight != 0 || this.mMatchConstraintMaxHeight != 0 || this.mListDimensionBehaviors[1] != DimensionBehaviour.MATCH_CONSTRAINT) {
            b = false;
        }
        return b;
    }
    
    public boolean isSpreadWidth() {
        final int mMatchConstraintDefaultWidth = this.mMatchConstraintDefaultWidth;
        boolean b2;
        final boolean b = b2 = false;
        if (mMatchConstraintDefaultWidth == 0) {
            b2 = b;
            if (this.mDimensionRatio == 0.0f) {
                b2 = b;
                if (this.mMatchConstraintMinWidth == 0) {
                    b2 = b;
                    if (this.mMatchConstraintMaxWidth == 0) {
                        b2 = b;
                        if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT) {
                            b2 = true;
                        }
                    }
                }
            }
        }
        return b2;
    }
    
    public boolean isWidthWrapContent() {
        return this.mIsWidthWrapContent;
    }
    
    public void reset() {
        this.mLeft.reset();
        this.mTop.reset();
        this.mRight.reset();
        this.mBottom.reset();
        this.mBaseline.reset();
        this.mCenterX.reset();
        this.mCenterY.reset();
        this.mCenter.reset();
        this.mParent = null;
        this.mCircleConstraintAngle = 0.0f;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mDrawX = 0;
        this.mDrawY = 0;
        this.mDrawWidth = 0;
        this.mDrawHeight = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        this.mWrapWidth = 0;
        this.mWrapHeight = 0;
        final float default_BIAS = ConstraintWidget.DEFAULT_BIAS;
        this.mHorizontalBiasPercent = default_BIAS;
        this.mVerticalBiasPercent = default_BIAS;
        this.mListDimensionBehaviors[0] = DimensionBehaviour.FIXED;
        this.mListDimensionBehaviors[1] = DimensionBehaviour.FIXED;
        this.mCompanionWidget = null;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mType = null;
        this.mHorizontalWrapVisited = false;
        this.mVerticalWrapVisited = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mHorizontalChainFixedPosition = false;
        this.mVerticalChainFixedPosition = false;
        final float[] mWeight = this.mWeight;
        mWeight[1] = (mWeight[0] = -1.0f);
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        final int[] mMaxDimension = this.mMaxDimension;
        mMaxDimension[1] = (mMaxDimension[0] = Integer.MAX_VALUE);
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mMatchConstraintMaxWidth = Integer.MAX_VALUE;
        this.mMatchConstraintMaxHeight = Integer.MAX_VALUE;
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMinHeight = 0;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        final ResolutionDimension mResolutionWidth = this.mResolutionWidth;
        if (mResolutionWidth != null) {
            mResolutionWidth.reset();
        }
        final ResolutionDimension mResolutionHeight = this.mResolutionHeight;
        if (mResolutionHeight != null) {
            mResolutionHeight.reset();
        }
        this.mBelongingGroup = null;
        this.mOptimizerMeasurable = false;
        this.mOptimizerMeasured = false;
        this.mGroupsToSolver = false;
    }
    
    public void resetAllConstraints() {
        this.resetAnchors();
        this.setVerticalBiasPercent(ConstraintWidget.DEFAULT_BIAS);
        this.setHorizontalBiasPercent(ConstraintWidget.DEFAULT_BIAS);
        if (this instanceof ConstraintWidgetContainer) {
            return;
        }
        if (this.getHorizontalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT) {
            if (this.getWidth() == this.getWrapWidth()) {
                this.setHorizontalDimensionBehaviour(DimensionBehaviour.WRAP_CONTENT);
            }
            else if (this.getWidth() > this.getMinWidth()) {
                this.setHorizontalDimensionBehaviour(DimensionBehaviour.FIXED);
            }
        }
        if (this.getVerticalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT) {
            if (this.getHeight() == this.getWrapHeight()) {
                this.setVerticalDimensionBehaviour(DimensionBehaviour.WRAP_CONTENT);
            }
            else if (this.getHeight() > this.getMinHeight()) {
                this.setVerticalDimensionBehaviour(DimensionBehaviour.FIXED);
            }
        }
    }
    
    public void resetAnchor(final ConstraintAnchor constraintAnchor) {
        if (this.getParent() != null && this.getParent() instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)this.getParent()).handlesInternalConstraints()) {
            return;
        }
        final ConstraintAnchor anchor = this.getAnchor(ConstraintAnchor.Type.LEFT);
        final ConstraintAnchor anchor2 = this.getAnchor(ConstraintAnchor.Type.RIGHT);
        final ConstraintAnchor anchor3 = this.getAnchor(ConstraintAnchor.Type.TOP);
        final ConstraintAnchor anchor4 = this.getAnchor(ConstraintAnchor.Type.BOTTOM);
        final ConstraintAnchor anchor5 = this.getAnchor(ConstraintAnchor.Type.CENTER);
        final ConstraintAnchor anchor6 = this.getAnchor(ConstraintAnchor.Type.CENTER_X);
        final ConstraintAnchor anchor7 = this.getAnchor(ConstraintAnchor.Type.CENTER_Y);
        if (constraintAnchor == anchor5) {
            if (anchor.isConnected() && anchor2.isConnected() && anchor.getTarget() == anchor2.getTarget()) {
                anchor.reset();
                anchor2.reset();
            }
            if (anchor3.isConnected() && anchor4.isConnected() && anchor3.getTarget() == anchor4.getTarget()) {
                anchor3.reset();
                anchor4.reset();
            }
            this.mHorizontalBiasPercent = 0.5f;
            this.mVerticalBiasPercent = 0.5f;
        }
        else if (constraintAnchor == anchor6) {
            if (anchor.isConnected() && anchor2.isConnected() && anchor.getTarget().getOwner() == anchor2.getTarget().getOwner()) {
                anchor.reset();
                anchor2.reset();
            }
            this.mHorizontalBiasPercent = 0.5f;
        }
        else if (constraintAnchor == anchor7) {
            if (anchor3.isConnected() && anchor4.isConnected() && anchor3.getTarget().getOwner() == anchor4.getTarget().getOwner()) {
                anchor3.reset();
                anchor4.reset();
            }
            this.mVerticalBiasPercent = 0.5f;
        }
        else if (constraintAnchor != anchor && constraintAnchor != anchor2) {
            if ((constraintAnchor == anchor3 || constraintAnchor == anchor4) && anchor3.isConnected() && anchor3.getTarget() == anchor4.getTarget()) {
                anchor5.reset();
            }
        }
        else if (anchor.isConnected() && anchor.getTarget() == anchor2.getTarget()) {
            anchor5.reset();
        }
        constraintAnchor.reset();
    }
    
    public void resetAnchors() {
        final ConstraintWidget parent = this.getParent();
        if (parent != null && parent instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)this.getParent()).handlesInternalConstraints()) {
            return;
        }
        for (int i = 0; i < this.mAnchors.size(); ++i) {
            this.mAnchors.get(i).reset();
        }
    }
    
    public void resetAnchors(final int n) {
        final ConstraintWidget parent = this.getParent();
        if (parent != null && parent instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)this.getParent()).handlesInternalConstraints()) {
            return;
        }
        for (int i = 0; i < this.mAnchors.size(); ++i) {
            final ConstraintAnchor constraintAnchor = this.mAnchors.get(i);
            if (n == constraintAnchor.getConnectionCreator()) {
                if (constraintAnchor.isVerticalAnchor()) {
                    this.setVerticalBiasPercent(ConstraintWidget.DEFAULT_BIAS);
                }
                else {
                    this.setHorizontalBiasPercent(ConstraintWidget.DEFAULT_BIAS);
                }
                constraintAnchor.reset();
            }
        }
    }
    
    public void resetResolutionNodes() {
        for (int i = 0; i < 6; ++i) {
            this.mListAnchors[i].getResolutionNode().reset();
        }
    }
    
    public void resetSolverVariables(final Cache cache) {
        this.mLeft.resetSolverVariable(cache);
        this.mTop.resetSolverVariable(cache);
        this.mRight.resetSolverVariable(cache);
        this.mBottom.resetSolverVariable(cache);
        this.mBaseline.resetSolverVariable(cache);
        this.mCenter.resetSolverVariable(cache);
        this.mCenterX.resetSolverVariable(cache);
        this.mCenterY.resetSolverVariable(cache);
    }
    
    public void resolve() {
    }
    
    public void setBaselineDistance(final int mBaselineDistance) {
        this.mBaselineDistance = mBaselineDistance;
    }
    
    public void setCompanionWidget(final Object mCompanionWidget) {
        this.mCompanionWidget = mCompanionWidget;
    }
    
    public void setContainerItemSkip(final int mContainerItemSkip) {
        if (mContainerItemSkip >= 0) {
            this.mContainerItemSkip = mContainerItemSkip;
        }
        else {
            this.mContainerItemSkip = 0;
        }
    }
    
    public void setDebugName(final String mDebugName) {
        this.mDebugName = mDebugName;
    }
    
    public void setDebugSolverName(final LinearSystem linearSystem, final String s) {
        this.mDebugName = s;
        final SolverVariable objectVariable = linearSystem.createObjectVariable(this.mLeft);
        final SolverVariable objectVariable2 = linearSystem.createObjectVariable(this.mTop);
        final SolverVariable objectVariable3 = linearSystem.createObjectVariable(this.mRight);
        final SolverVariable objectVariable4 = linearSystem.createObjectVariable(this.mBottom);
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(".left");
        objectVariable.setName(sb.toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(s);
        sb2.append(".top");
        objectVariable2.setName(sb2.toString());
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(s);
        sb3.append(".right");
        objectVariable3.setName(sb3.toString());
        final StringBuilder sb4 = new StringBuilder();
        sb4.append(s);
        sb4.append(".bottom");
        objectVariable4.setName(sb4.toString());
        if (this.mBaselineDistance > 0) {
            final SolverVariable objectVariable5 = linearSystem.createObjectVariable(this.mBaseline);
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(s);
            sb5.append(".baseline");
            objectVariable5.setName(sb5.toString());
        }
    }
    
    public void setDimension(int mMinHeight, final int mHeight) {
        this.mWidth = mMinHeight;
        final int mMinWidth = this.mMinWidth;
        if (mMinHeight < mMinWidth) {
            this.mWidth = mMinWidth;
        }
        this.mHeight = mHeight;
        mMinHeight = this.mMinHeight;
        if (mHeight < mMinHeight) {
            this.mHeight = mMinHeight;
        }
    }
    
    public void setDimensionRatio(final float mDimensionRatio, final int mDimensionRatioSide) {
        this.mDimensionRatio = mDimensionRatio;
        this.mDimensionRatioSide = mDimensionRatioSide;
    }
    
    public void setDimensionRatio(String s) {
        if (s != null && s.length() != 0) {
            int mDimensionRatioSide = -1;
            final float n = 0.0f;
            final float n2 = 0.0f;
            final float n3 = 0.0f;
            final int length = s.length();
            int index = s.indexOf(44);
            int n5;
            if (index > 0 && index < length - 1) {
                final String substring = s.substring(0, index);
                int n4;
                if (substring.equalsIgnoreCase("W")) {
                    n4 = 0;
                }
                else {
                    n4 = mDimensionRatioSide;
                    if (substring.equalsIgnoreCase("H")) {
                        n4 = 1;
                    }
                }
                ++index;
                mDimensionRatioSide = n4;
                n5 = index;
            }
            else {
                n5 = 0;
            }
            final int index2 = s.indexOf(58);
            float mDimensionRatio;
            if (index2 >= 0 && index2 < length - 1) {
                final String substring2 = s.substring(n5, index2);
                s = s.substring(index2 + 1);
                mDimensionRatio = n;
                if (substring2.length() > 0) {
                    mDimensionRatio = n;
                    if (s.length() > 0) {
                        try {
                            final float float1 = Float.parseFloat(substring2);
                            final float float2 = Float.parseFloat(s);
                            mDimensionRatio = n3;
                            if (float1 > 0.0f) {
                                mDimensionRatio = n3;
                                if (float2 > 0.0f) {
                                    if (mDimensionRatioSide == 1) {
                                        mDimensionRatio = Math.abs(float2 / float1);
                                    }
                                    else {
                                        mDimensionRatio = Math.abs(float1 / float2);
                                    }
                                }
                            }
                        }
                        catch (NumberFormatException ex) {
                            mDimensionRatio = n;
                        }
                    }
                }
            }
            else {
                s = s.substring(n5);
                mDimensionRatio = n2;
                if (s.length() > 0) {
                    try {
                        mDimensionRatio = Float.parseFloat(s);
                    }
                    catch (NumberFormatException ex2) {
                        mDimensionRatio = n2;
                    }
                }
            }
            if (mDimensionRatio > 0.0f) {
                this.mDimensionRatio = mDimensionRatio;
                this.mDimensionRatioSide = mDimensionRatioSide;
            }
            return;
        }
        this.mDimensionRatio = 0.0f;
    }
    
    public void setDrawHeight(final int mDrawHeight) {
        this.mDrawHeight = mDrawHeight;
    }
    
    public void setDrawOrigin(int n, int n2) {
        n -= this.mOffsetX;
        this.mDrawX = n;
        n2 -= this.mOffsetY;
        this.mDrawY = n2;
        this.mX = n;
        this.mY = n2;
    }
    
    public void setDrawWidth(final int mDrawWidth) {
        this.mDrawWidth = mDrawWidth;
    }
    
    public void setDrawX(int n) {
        n -= this.mOffsetX;
        this.mDrawX = n;
        this.mX = n;
    }
    
    public void setDrawY(int n) {
        n -= this.mOffsetY;
        this.mDrawY = n;
        this.mY = n;
    }
    
    public void setFrame(final int n, final int n2, final int n3) {
        if (n3 == 0) {
            this.setHorizontalDimension(n, n2);
        }
        else if (n3 == 1) {
            this.setVerticalDimension(n, n2);
        }
        this.mOptimizerMeasured = true;
    }
    
    public void setFrame(int n, int n2, int n3, final int n4) {
        final int n5 = n3 - n;
        n3 = n4 - n2;
        this.mX = n;
        this.mY = n2;
        if (this.mVisibility == 8) {
            this.mWidth = 0;
            this.mHeight = 0;
            return;
        }
        n = n5;
        if (this.mListDimensionBehaviors[0] == DimensionBehaviour.FIXED && (n = n5) < this.mWidth) {
            n = this.mWidth;
        }
        n2 = n3;
        if (this.mListDimensionBehaviors[1] == DimensionBehaviour.FIXED && (n2 = n3) < this.mHeight) {
            n2 = this.mHeight;
        }
        this.mWidth = n;
        this.mHeight = n2;
        n = this.mMinHeight;
        if (n2 < n) {
            this.mHeight = n;
        }
        n2 = this.mWidth;
        n = this.mMinWidth;
        if (n2 < n) {
            this.mWidth = n;
        }
        this.mOptimizerMeasured = true;
    }
    
    public void setGoneMargin(final ConstraintAnchor.Type type, final int n) {
        final int n2 = ConstraintWidget$1.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[type.ordinal()];
        if (n2 != 1) {
            if (n2 != 2) {
                if (n2 != 3) {
                    if (n2 == 4) {
                        this.mBottom.mGoneMargin = n;
                    }
                }
                else {
                    this.mRight.mGoneMargin = n;
                }
            }
            else {
                this.mTop.mGoneMargin = n;
            }
        }
        else {
            this.mLeft.mGoneMargin = n;
        }
    }
    
    public void setHeight(final int mHeight) {
        this.mHeight = mHeight;
        final int mMinHeight = this.mMinHeight;
        if (mHeight < mMinHeight) {
            this.mHeight = mMinHeight;
        }
    }
    
    public void setHeightWrapContent(final boolean mIsHeightWrapContent) {
        this.mIsHeightWrapContent = mIsHeightWrapContent;
    }
    
    public void setHorizontalBiasPercent(final float mHorizontalBiasPercent) {
        this.mHorizontalBiasPercent = mHorizontalBiasPercent;
    }
    
    public void setHorizontalChainStyle(final int mHorizontalChainStyle) {
        this.mHorizontalChainStyle = mHorizontalChainStyle;
    }
    
    public void setHorizontalDimension(int n, int mMinWidth) {
        this.mX = n;
        n = mMinWidth - n;
        this.mWidth = n;
        mMinWidth = this.mMinWidth;
        if (n < mMinWidth) {
            this.mWidth = mMinWidth;
        }
    }
    
    public void setHorizontalDimensionBehaviour(final DimensionBehaviour dimensionBehaviour) {
        this.mListDimensionBehaviors[0] = dimensionBehaviour;
        if (dimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
            this.setWidth(this.mWrapWidth);
        }
    }
    
    public void setHorizontalMatchStyle(final int mMatchConstraintDefaultWidth, final int mMatchConstraintMinWidth, final int mMatchConstraintMaxWidth, final float mMatchConstraintPercentWidth) {
        this.mMatchConstraintDefaultWidth = mMatchConstraintDefaultWidth;
        this.mMatchConstraintMinWidth = mMatchConstraintMinWidth;
        this.mMatchConstraintMaxWidth = mMatchConstraintMaxWidth;
        this.mMatchConstraintPercentWidth = mMatchConstraintPercentWidth;
        if (mMatchConstraintPercentWidth < 1.0f && mMatchConstraintDefaultWidth == 0) {
            this.mMatchConstraintDefaultWidth = 2;
        }
    }
    
    public void setHorizontalWeight(final float n) {
        this.mWeight[0] = n;
    }
    
    public void setLength(final int n, final int n2) {
        if (n2 == 0) {
            this.setWidth(n);
        }
        else if (n2 == 1) {
            this.setHeight(n);
        }
    }
    
    public void setMaxHeight(final int n) {
        this.mMaxDimension[1] = n;
    }
    
    public void setMaxWidth(final int n) {
        this.mMaxDimension[0] = n;
    }
    
    public void setMinHeight(final int mMinHeight) {
        if (mMinHeight < 0) {
            this.mMinHeight = 0;
        }
        else {
            this.mMinHeight = mMinHeight;
        }
    }
    
    public void setMinWidth(final int mMinWidth) {
        if (mMinWidth < 0) {
            this.mMinWidth = 0;
        }
        else {
            this.mMinWidth = mMinWidth;
        }
    }
    
    public void setOffset(final int mOffsetX, final int mOffsetY) {
        this.mOffsetX = mOffsetX;
        this.mOffsetY = mOffsetY;
    }
    
    public void setOrigin(final int mx, final int my) {
        this.mX = mx;
        this.mY = my;
    }
    
    public void setParent(final ConstraintWidget mParent) {
        this.mParent = mParent;
    }
    
    void setRelativePositioning(final int n, final int n2) {
        if (n2 == 0) {
            this.mRelX = n;
        }
        else if (n2 == 1) {
            this.mRelY = n;
        }
    }
    
    public void setType(final String mType) {
        this.mType = mType;
    }
    
    public void setVerticalBiasPercent(final float mVerticalBiasPercent) {
        this.mVerticalBiasPercent = mVerticalBiasPercent;
    }
    
    public void setVerticalChainStyle(final int mVerticalChainStyle) {
        this.mVerticalChainStyle = mVerticalChainStyle;
    }
    
    public void setVerticalDimension(int mMinHeight, int mHeight) {
        this.mY = mMinHeight;
        mHeight -= mMinHeight;
        this.mHeight = mHeight;
        mMinHeight = this.mMinHeight;
        if (mHeight < mMinHeight) {
            this.mHeight = mMinHeight;
        }
    }
    
    public void setVerticalDimensionBehaviour(final DimensionBehaviour dimensionBehaviour) {
        this.mListDimensionBehaviors[1] = dimensionBehaviour;
        if (dimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
            this.setHeight(this.mWrapHeight);
        }
    }
    
    public void setVerticalMatchStyle(final int mMatchConstraintDefaultHeight, final int mMatchConstraintMinHeight, final int mMatchConstraintMaxHeight, final float mMatchConstraintPercentHeight) {
        this.mMatchConstraintDefaultHeight = mMatchConstraintDefaultHeight;
        this.mMatchConstraintMinHeight = mMatchConstraintMinHeight;
        this.mMatchConstraintMaxHeight = mMatchConstraintMaxHeight;
        this.mMatchConstraintPercentHeight = mMatchConstraintPercentHeight;
        if (mMatchConstraintPercentHeight < 1.0f && mMatchConstraintDefaultHeight == 0) {
            this.mMatchConstraintDefaultHeight = 2;
        }
    }
    
    public void setVerticalWeight(final float n) {
        this.mWeight[1] = n;
    }
    
    public void setVisibility(final int mVisibility) {
        this.mVisibility = mVisibility;
    }
    
    public void setWidth(final int mWidth) {
        this.mWidth = mWidth;
        final int mMinWidth = this.mMinWidth;
        if (mWidth < mMinWidth) {
            this.mWidth = mMinWidth;
        }
    }
    
    public void setWidthWrapContent(final boolean mIsWidthWrapContent) {
        this.mIsWidthWrapContent = mIsWidthWrapContent;
    }
    
    public void setWrapHeight(final int mWrapHeight) {
        this.mWrapHeight = mWrapHeight;
    }
    
    public void setWrapWidth(final int mWrapWidth) {
        this.mWrapWidth = mWrapWidth;
    }
    
    public void setX(final int mx) {
        this.mX = mx;
    }
    
    public void setY(final int my) {
        this.mY = my;
    }
    
    public void setupDimensionRatio(final boolean b, final boolean b2, final boolean b3, final boolean b4) {
        if (this.mResolvedDimensionRatioSide == -1) {
            if (b3 && !b4) {
                this.mResolvedDimensionRatioSide = 0;
            }
            else if (!b3 && b4) {
                this.mResolvedDimensionRatioSide = 1;
                if (this.mDimensionRatioSide == -1) {
                    this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                }
            }
        }
        if (this.mResolvedDimensionRatioSide == 0 && (!this.mTop.isConnected() || !this.mBottom.isConnected())) {
            this.mResolvedDimensionRatioSide = 1;
        }
        else if (this.mResolvedDimensionRatioSide == 1 && (!this.mLeft.isConnected() || !this.mRight.isConnected())) {
            this.mResolvedDimensionRatioSide = 0;
        }
        if (this.mResolvedDimensionRatioSide == -1 && (!this.mTop.isConnected() || !this.mBottom.isConnected() || !this.mLeft.isConnected() || !this.mRight.isConnected())) {
            if (this.mTop.isConnected() && this.mBottom.isConnected()) {
                this.mResolvedDimensionRatioSide = 0;
            }
            else if (this.mLeft.isConnected() && this.mRight.isConnected()) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
        if (this.mResolvedDimensionRatioSide == -1) {
            if (b && !b2) {
                this.mResolvedDimensionRatioSide = 0;
            }
            else if (!b && b2) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
        if (this.mResolvedDimensionRatioSide == -1) {
            if (this.mMatchConstraintMinWidth > 0 && this.mMatchConstraintMinHeight == 0) {
                this.mResolvedDimensionRatioSide = 0;
            }
            else if (this.mMatchConstraintMinWidth == 0 && this.mMatchConstraintMinHeight > 0) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
        if (this.mResolvedDimensionRatioSide == -1 && b && b2) {
            this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
            this.mResolvedDimensionRatioSide = 1;
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        final String mType = this.mType;
        final String s = "";
        String string;
        if (mType != null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("type: ");
            sb2.append(this.mType);
            sb2.append(" ");
            string = sb2.toString();
        }
        else {
            string = "";
        }
        sb.append(string);
        String string2 = s;
        if (this.mDebugName != null) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("id: ");
            sb3.append(this.mDebugName);
            sb3.append(" ");
            string2 = sb3.toString();
        }
        sb.append(string2);
        sb.append("(");
        sb.append(this.mX);
        sb.append(", ");
        sb.append(this.mY);
        sb.append(") - (");
        sb.append(this.mWidth);
        sb.append(" x ");
        sb.append(this.mHeight);
        sb.append(") wrap: (");
        sb.append(this.mWrapWidth);
        sb.append(" x ");
        sb.append(this.mWrapHeight);
        sb.append(")");
        return sb.toString();
    }
    
    public void updateDrawPosition() {
        final int mx = this.mX;
        final int my = this.mY;
        final int mx2 = this.mX;
        final int mWidth = this.mWidth;
        final int my2 = this.mY;
        final int mHeight = this.mHeight;
        this.mDrawX = mx;
        this.mDrawY = my;
        this.mDrawWidth = mx2 + mWidth - mx;
        this.mDrawHeight = my2 + mHeight - my;
    }
    
    public void updateFromSolver(final LinearSystem linearSystem) {
        int objectVariableValue = linearSystem.getObjectVariableValue(this.mLeft);
        int objectVariableValue2 = linearSystem.getObjectVariableValue(this.mTop);
        int objectVariableValue3 = linearSystem.getObjectVariableValue(this.mRight);
        final int objectVariableValue4 = linearSystem.getObjectVariableValue(this.mBottom);
        int n;
        if (objectVariableValue3 - objectVariableValue < 0 || objectVariableValue4 - objectVariableValue2 < 0 || objectVariableValue == Integer.MIN_VALUE || objectVariableValue == Integer.MAX_VALUE || objectVariableValue2 == Integer.MIN_VALUE || objectVariableValue2 == Integer.MAX_VALUE || objectVariableValue3 == Integer.MIN_VALUE || objectVariableValue3 == Integer.MAX_VALUE || objectVariableValue4 == Integer.MIN_VALUE || (n = objectVariableValue4) == Integer.MAX_VALUE) {
            objectVariableValue = 0;
            objectVariableValue2 = 0;
            objectVariableValue3 = 0;
            n = 0;
        }
        this.setFrame(objectVariableValue, objectVariableValue2, objectVariableValue3, n);
    }
    
    public void updateResolutionNodes() {
        for (int i = 0; i < 6; ++i) {
            this.mListAnchors[i].getResolutionNode().update();
        }
    }
    
    public enum ContentAlignment
    {
        BEGIN, 
        BOTTOM, 
        END, 
        LEFT, 
        MIDDLE, 
        RIGHT, 
        TOP, 
        VERTICAL_MIDDLE;
    }
    
    public enum DimensionBehaviour
    {
        FIXED, 
        MATCH_CONSTRAINT, 
        MATCH_PARENT, 
        WRAP_CONTENT;
    }
}
