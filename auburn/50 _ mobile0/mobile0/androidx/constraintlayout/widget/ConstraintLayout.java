// 
// Decompiled by Procyon v0.5.36
// 

package androidx.constraintlayout.widget;

import android.content.res.TypedArray;
import android.util.SparseIntArray;
import android.util.Log;
import android.view.ViewGroup$MarginLayoutParams;
import androidx.constraintlayout.solver.widgets.Analyzer;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import android.view.View$MeasureSpec;
import android.os.Build$VERSION;
import androidx.constraintlayout.solver.widgets.Guideline;
import androidx.constraintlayout.solver.widgets.ResolutionAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import android.content.res.Resources$NotFoundException;
import android.util.AttributeSet;
import android.content.Context;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.Metrics;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import java.util.HashMap;
import java.util.ArrayList;
import android.view.View;
import android.util.SparseArray;
import android.view.ViewGroup;

public class ConstraintLayout extends ViewGroup
{
    static final boolean ALLOWS_EMBEDDED = false;
    private static final boolean CACHE_MEASURED_DIMENSION = false;
    private static final boolean DEBUG = false;
    public static final int DESIGN_INFO_ID = 0;
    private static final String TAG = "ConstraintLayout";
    private static final boolean USE_CONSTRAINTS_HELPER = true;
    public static final String VERSION = "ConstraintLayout-1.1.3";
    SparseArray<View> mChildrenByIds;
    private ArrayList<ConstraintHelper> mConstraintHelpers;
    private ConstraintSet mConstraintSet;
    private int mConstraintSetId;
    private HashMap<String, Integer> mDesignIds;
    private boolean mDirtyHierarchy;
    private int mLastMeasureHeight;
    int mLastMeasureHeightMode;
    int mLastMeasureHeightSize;
    private int mLastMeasureWidth;
    int mLastMeasureWidthMode;
    int mLastMeasureWidthSize;
    ConstraintWidgetContainer mLayoutWidget;
    private int mMaxHeight;
    private int mMaxWidth;
    private Metrics mMetrics;
    private int mMinHeight;
    private int mMinWidth;
    private int mOptimizationLevel;
    private final ArrayList<ConstraintWidget> mVariableDimensionsWidgets;
    
    public ConstraintLayout(final Context context) {
        super(context);
        this.mChildrenByIds = (SparseArray<View>)new SparseArray();
        this.mConstraintHelpers = new ArrayList<ConstraintHelper>(4);
        this.mVariableDimensionsWidgets = new ArrayList<ConstraintWidget>(100);
        this.mLayoutWidget = new ConstraintWidgetContainer();
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        this.mMaxWidth = Integer.MAX_VALUE;
        this.mMaxHeight = Integer.MAX_VALUE;
        this.mDirtyHierarchy = true;
        this.mOptimizationLevel = 7;
        this.mConstraintSet = null;
        this.mConstraintSetId = -1;
        this.mDesignIds = new HashMap<String, Integer>();
        this.mLastMeasureWidth = -1;
        this.mLastMeasureHeight = -1;
        this.mLastMeasureWidthSize = -1;
        this.mLastMeasureHeightSize = -1;
        this.mLastMeasureWidthMode = 0;
        this.mLastMeasureHeightMode = 0;
        this.init(null);
    }
    
    public ConstraintLayout(final Context context, final AttributeSet set) {
        super(context, set);
        this.mChildrenByIds = (SparseArray<View>)new SparseArray();
        this.mConstraintHelpers = new ArrayList<ConstraintHelper>(4);
        this.mVariableDimensionsWidgets = new ArrayList<ConstraintWidget>(100);
        this.mLayoutWidget = new ConstraintWidgetContainer();
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        this.mMaxWidth = Integer.MAX_VALUE;
        this.mMaxHeight = Integer.MAX_VALUE;
        this.mDirtyHierarchy = true;
        this.mOptimizationLevel = 7;
        this.mConstraintSet = null;
        this.mConstraintSetId = -1;
        this.mDesignIds = new HashMap<String, Integer>();
        this.mLastMeasureWidth = -1;
        this.mLastMeasureHeight = -1;
        this.mLastMeasureWidthSize = -1;
        this.mLastMeasureHeightSize = -1;
        this.mLastMeasureWidthMode = 0;
        this.mLastMeasureHeightMode = 0;
        this.init(set);
    }
    
    public ConstraintLayout(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mChildrenByIds = (SparseArray<View>)new SparseArray();
        this.mConstraintHelpers = new ArrayList<ConstraintHelper>(4);
        this.mVariableDimensionsWidgets = new ArrayList<ConstraintWidget>(100);
        this.mLayoutWidget = new ConstraintWidgetContainer();
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        this.mMaxWidth = Integer.MAX_VALUE;
        this.mMaxHeight = Integer.MAX_VALUE;
        this.mDirtyHierarchy = true;
        this.mOptimizationLevel = 7;
        this.mConstraintSet = null;
        this.mConstraintSetId = -1;
        this.mDesignIds = new HashMap<String, Integer>();
        this.mLastMeasureWidth = -1;
        this.mLastMeasureHeight = -1;
        this.mLastMeasureWidthSize = -1;
        this.mLastMeasureHeightSize = -1;
        this.mLastMeasureWidthMode = 0;
        this.mLastMeasureHeightMode = 0;
        this.init(set);
    }
    
    private final ConstraintWidget getTargetWidget(final int n) {
        if (n == 0) {
            return this.mLayoutWidget;
        }
        View view;
        if ((view = (View)this.mChildrenByIds.get(n)) == null) {
            final View viewById = this.findViewById(n);
            if ((view = viewById) != null && (view = viewById) != this) {
                view = viewById;
                if (viewById.getParent() == this) {
                    this.onViewAdded(viewById);
                    view = viewById;
                }
            }
        }
        if (view == this) {
            return this.mLayoutWidget;
        }
        ConstraintWidget widget;
        if (view == null) {
            widget = null;
        }
        else {
            widget = ((LayoutParams)view.getLayoutParams()).widget;
        }
        return widget;
    }
    
    private void init(AttributeSet obtainStyledAttributes) {
        this.mLayoutWidget.setCompanionWidget(this);
        this.mChildrenByIds.put(this.getId(), (Object)this);
        this.mConstraintSet = null;
        if (obtainStyledAttributes != null) {
            obtainStyledAttributes = (AttributeSet)this.getContext().obtainStyledAttributes(obtainStyledAttributes, R.styleable.ConstraintLayout_Layout);
            for (int indexCount = ((TypedArray)obtainStyledAttributes).getIndexCount(), i = 0; i < indexCount; ++i) {
                final int index = ((TypedArray)obtainStyledAttributes).getIndex(i);
                if (index == R.styleable.ConstraintLayout_Layout_android_minWidth) {
                    this.mMinWidth = ((TypedArray)obtainStyledAttributes).getDimensionPixelOffset(index, this.mMinWidth);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_android_minHeight) {
                    this.mMinHeight = ((TypedArray)obtainStyledAttributes).getDimensionPixelOffset(index, this.mMinHeight);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_android_maxWidth) {
                    this.mMaxWidth = ((TypedArray)obtainStyledAttributes).getDimensionPixelOffset(index, this.mMaxWidth);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_android_maxHeight) {
                    this.mMaxHeight = ((TypedArray)obtainStyledAttributes).getDimensionPixelOffset(index, this.mMaxHeight);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_optimizationLevel) {
                    this.mOptimizationLevel = ((TypedArray)obtainStyledAttributes).getInt(index, this.mOptimizationLevel);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_constraintSet) {
                    final int resourceId = ((TypedArray)obtainStyledAttributes).getResourceId(index, 0);
                    try {
                        (this.mConstraintSet = new ConstraintSet()).load(this.getContext(), resourceId);
                    }
                    catch (Resources$NotFoundException ex) {
                        this.mConstraintSet = null;
                    }
                    this.mConstraintSetId = resourceId;
                }
            }
            ((TypedArray)obtainStyledAttributes).recycle();
        }
        this.mLayoutWidget.setOptimizationLevel(this.mOptimizationLevel);
    }
    
    private void internalMeasureChildren(final int n, final int n2) {
        final int n3 = this.getPaddingTop() + this.getPaddingBottom();
        final int n4 = this.getPaddingLeft() + this.getPaddingRight();
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                final ConstraintWidget widget = layoutParams.widget;
                if (!layoutParams.isGuideline) {
                    if (!layoutParams.isHelper) {
                        widget.setVisibility(child.getVisibility());
                        final int width = layoutParams.width;
                        final int height = layoutParams.height;
                        boolean b = false;
                        Label_0205: {
                            Label_0202: {
                                if (!layoutParams.horizontalDimensionFixed && !layoutParams.verticalDimensionFixed && (layoutParams.horizontalDimensionFixed || layoutParams.matchConstraintDefaultWidth != 1) && layoutParams.width != -1) {
                                    if (!layoutParams.verticalDimensionFixed) {
                                        if (layoutParams.matchConstraintDefaultHeight == 1) {
                                            break Label_0202;
                                        }
                                        if (layoutParams.height == -1) {
                                            break Label_0202;
                                        }
                                    }
                                    b = false;
                                    break Label_0205;
                                }
                            }
                            b = true;
                        }
                        final int n5 = 0;
                        int n6 = 0;
                        final int n7 = 0;
                        final int n8 = 0;
                        int n9 = 0;
                        final int n10 = 0;
                        int measuredWidth = width;
                        int n11 = height;
                        if (b) {
                            int n12;
                            int n13;
                            if (width == 0) {
                                n12 = getChildMeasureSpec(n, n4, -2);
                                n13 = 1;
                            }
                            else if (width == -1) {
                                n12 = getChildMeasureSpec(n, n4, -1);
                                n13 = n5;
                            }
                            else {
                                n13 = n7;
                                if (width == -2) {
                                    n13 = 1;
                                }
                                n12 = getChildMeasureSpec(n, n4, width);
                            }
                            int n14;
                            int n15;
                            if (height == 0) {
                                n14 = getChildMeasureSpec(n2, n3, -2);
                                n15 = 1;
                            }
                            else if (height == -1) {
                                n14 = getChildMeasureSpec(n2, n3, -1);
                                n15 = n8;
                            }
                            else {
                                n15 = n10;
                                if (height == -2) {
                                    n15 = 1;
                                }
                                n14 = getChildMeasureSpec(n2, n3, height);
                            }
                            child.measure(n12, n14);
                            final Metrics mMetrics = this.mMetrics;
                            if (mMetrics != null) {
                                ++mMetrics.measures;
                            }
                            widget.setWidthWrapContent(width == -2);
                            widget.setHeightWrapContent(height == -2);
                            measuredWidth = child.getMeasuredWidth();
                            final int measuredHeight = child.getMeasuredHeight();
                            n9 = n15;
                            n6 = n13;
                            n11 = measuredHeight;
                        }
                        widget.setWidth(measuredWidth);
                        widget.setHeight(n11);
                        if (n6 != 0) {
                            widget.setWrapWidth(measuredWidth);
                        }
                        if (n9 != 0) {
                            widget.setWrapHeight(n11);
                        }
                        if (layoutParams.needsBaseline) {
                            final int baseline = child.getBaseline();
                            if (baseline != -1) {
                                widget.setBaselineDistance(baseline);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void internalMeasureDimensions(final int n, final int n2) {
        ConstraintLayout constraintLayout = this;
        final int n3 = this.getPaddingTop() + this.getPaddingBottom();
        final int n4 = this.getPaddingLeft() + this.getPaddingRight();
        final int childCount = this.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            final View child = constraintLayout.getChildAt(i);
            if (child.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                final ConstraintWidget widget = layoutParams.widget;
                if (!layoutParams.isGuideline) {
                    if (!layoutParams.isHelper) {
                        widget.setVisibility(child.getVisibility());
                        final int width = layoutParams.width;
                        final int height = layoutParams.height;
                        if (width != 0 && height != 0) {
                            boolean b = false;
                            boolean b2 = false;
                            if (width == -2) {
                                b = true;
                            }
                            final int childMeasureSpec = getChildMeasureSpec(n, n4, width);
                            if (height == -2) {
                                b2 = true;
                            }
                            child.measure(childMeasureSpec, getChildMeasureSpec(n2, n3, height));
                            final Metrics mMetrics = constraintLayout.mMetrics;
                            if (mMetrics != null) {
                                ++mMetrics.measures;
                            }
                            widget.setWidthWrapContent(width == -2);
                            widget.setHeightWrapContent(height == -2);
                            final int measuredWidth = child.getMeasuredWidth();
                            final int measuredHeight = child.getMeasuredHeight();
                            widget.setWidth(measuredWidth);
                            widget.setHeight(measuredHeight);
                            if (b) {
                                widget.setWrapWidth(measuredWidth);
                            }
                            if (b2) {
                                widget.setWrapHeight(measuredHeight);
                            }
                            if (layoutParams.needsBaseline) {
                                final int baseline = child.getBaseline();
                                if (baseline != -1) {
                                    widget.setBaselineDistance(baseline);
                                }
                            }
                            if (layoutParams.horizontalDimensionFixed && layoutParams.verticalDimensionFixed) {
                                widget.getResolutionWidth().resolve(measuredWidth);
                                widget.getResolutionHeight().resolve(measuredHeight);
                            }
                        }
                        else {
                            widget.getResolutionWidth().invalidate();
                            widget.getResolutionHeight().invalidate();
                        }
                    }
                }
            }
        }
        constraintLayout.mLayoutWidget.solveGraph();
        ConstraintLayout constraintLayout2;
        for (int j = 0; j < childCount; ++j, constraintLayout = constraintLayout2) {
            final View child2 = constraintLayout.getChildAt(j);
            if (child2.getVisibility() == 8) {
                constraintLayout2 = constraintLayout;
            }
            else {
                final LayoutParams layoutParams2 = (LayoutParams)child2.getLayoutParams();
                final ConstraintWidget widget2 = layoutParams2.widget;
                if (!layoutParams2.isGuideline) {
                    if (layoutParams2.isHelper) {
                        constraintLayout2 = constraintLayout;
                    }
                    else {
                        widget2.setVisibility(child2.getVisibility());
                        int width2 = layoutParams2.width;
                        int height2 = layoutParams2.height;
                        if (width2 != 0 && height2 != 0) {
                            constraintLayout2 = constraintLayout;
                        }
                        else {
                            final ResolutionAnchor resolutionNode = widget2.getAnchor(ConstraintAnchor.Type.LEFT).getResolutionNode();
                            final ResolutionAnchor resolutionNode2 = widget2.getAnchor(ConstraintAnchor.Type.RIGHT).getResolutionNode();
                            final boolean b3 = widget2.getAnchor(ConstraintAnchor.Type.LEFT).getTarget() != null && widget2.getAnchor(ConstraintAnchor.Type.RIGHT).getTarget() != null;
                            final ResolutionAnchor resolutionNode3 = widget2.getAnchor(ConstraintAnchor.Type.TOP).getResolutionNode();
                            final ResolutionAnchor resolutionNode4 = widget2.getAnchor(ConstraintAnchor.Type.BOTTOM).getResolutionNode();
                            final boolean b4 = widget2.getAnchor(ConstraintAnchor.Type.TOP).getTarget() != null && widget2.getAnchor(ConstraintAnchor.Type.BOTTOM).getTarget() != null;
                            if (width2 == 0 && height2 == 0 && b3 && b4) {
                                constraintLayout2 = constraintLayout;
                            }
                            else {
                                int n5 = 0;
                                final int n6 = 0;
                                final boolean b5 = false;
                                final int n7 = 0;
                                final boolean b6 = constraintLayout.mLayoutWidget.getHorizontalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                                final boolean b7 = constraintLayout.mLayoutWidget.getVerticalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                                if (!b6) {
                                    widget2.getResolutionWidth().invalidate();
                                }
                                if (!b7) {
                                    widget2.getResolutionHeight().invalidate();
                                }
                                int n8;
                                boolean b8;
                                if (width2 == 0) {
                                    if (b6 && widget2.isSpreadWidth() && b3 && resolutionNode.isResolved() && resolutionNode2.isResolved()) {
                                        width2 = (int)(resolutionNode2.getResolvedValue() - resolutionNode.getResolvedValue());
                                        widget2.getResolutionWidth().resolve(width2);
                                        n8 = getChildMeasureSpec(n, n4, width2);
                                        b8 = b6;
                                    }
                                    else {
                                        n8 = getChildMeasureSpec(n, n4, -2);
                                        n5 = 1;
                                        b8 = false;
                                    }
                                }
                                else if (width2 == -1) {
                                    n8 = getChildMeasureSpec(n, n4, -1);
                                    b8 = b6;
                                }
                                else {
                                    n5 = n6;
                                    if (width2 == -2) {
                                        n5 = 1;
                                    }
                                    n8 = getChildMeasureSpec(n, n4, width2);
                                    b8 = b6;
                                }
                                boolean b9;
                                int n9;
                                int n10;
                                if (height2 == 0) {
                                    if (b7 && widget2.isSpreadHeight() && b4 && resolutionNode3.isResolved() && resolutionNode4.isResolved()) {
                                        final float resolvedValue = resolutionNode4.getResolvedValue();
                                        final float resolvedValue2 = resolutionNode3.getResolvedValue();
                                        b9 = b7;
                                        height2 = (int)(resolvedValue - resolvedValue2);
                                        widget2.getResolutionHeight().resolve(height2);
                                        n9 = getChildMeasureSpec(n2, n3, height2);
                                        n10 = (b5 ? 1 : 0);
                                    }
                                    else {
                                        n9 = getChildMeasureSpec(n2, n3, -2);
                                        n10 = 1;
                                        b9 = false;
                                    }
                                }
                                else {
                                    b9 = b7;
                                    if (height2 == -1) {
                                        n9 = getChildMeasureSpec(n2, n3, -1);
                                        n10 = (b5 ? 1 : 0);
                                    }
                                    else {
                                        n10 = n7;
                                        if (height2 == -2) {
                                            n10 = 1;
                                        }
                                        n9 = getChildMeasureSpec(n2, n3, height2);
                                    }
                                }
                                child2.measure(n8, n9);
                                final Metrics mMetrics2 = this.mMetrics;
                                if (mMetrics2 != null) {
                                    ++mMetrics2.measures;
                                }
                                widget2.setWidthWrapContent(width2 == -2);
                                widget2.setHeightWrapContent(height2 == -2);
                                final int measuredWidth2 = child2.getMeasuredWidth();
                                final int measuredHeight2 = child2.getMeasuredHeight();
                                widget2.setWidth(measuredWidth2);
                                widget2.setHeight(measuredHeight2);
                                if (n5 != 0) {
                                    widget2.setWrapWidth(measuredWidth2);
                                }
                                if (n10 != 0) {
                                    widget2.setWrapHeight(measuredHeight2);
                                }
                                if (b8) {
                                    widget2.getResolutionWidth().resolve(measuredWidth2);
                                }
                                else {
                                    widget2.getResolutionWidth().remove();
                                }
                                if (b9) {
                                    widget2.getResolutionHeight().resolve(measuredHeight2);
                                }
                                else {
                                    widget2.getResolutionHeight().remove();
                                }
                                if (layoutParams2.needsBaseline) {
                                    final int baseline2 = child2.getBaseline();
                                    constraintLayout2 = this;
                                    if (baseline2 != -1) {
                                        widget2.setBaselineDistance(baseline2);
                                        constraintLayout2 = this;
                                    }
                                }
                                else {
                                    constraintLayout2 = this;
                                }
                            }
                        }
                    }
                }
                else {
                    constraintLayout2 = constraintLayout;
                }
            }
        }
    }
    
    private void setChildrenConstraints() {
        final boolean inEditMode = this.isInEditMode();
        final int childCount = this.getChildCount();
        if (inEditMode) {
            for (int i = 0; i < childCount; ++i) {
                final View child = this.getChildAt(i);
                try {
                    final String resourceName = this.getResources().getResourceName(child.getId());
                    this.setDesignInformation(0, resourceName, child.getId());
                    final int index = resourceName.indexOf(47);
                    String substring = resourceName;
                    if (index != -1) {
                        substring = resourceName.substring(index + 1);
                    }
                    this.getTargetWidget(child.getId()).setDebugName(substring);
                }
                catch (Resources$NotFoundException ex) {}
            }
        }
        for (int j = 0; j < childCount; ++j) {
            final ConstraintWidget viewWidget = this.getViewWidget(this.getChildAt(j));
            if (viewWidget != null) {
                viewWidget.reset();
            }
        }
        if (this.mConstraintSetId != -1) {
            for (int k = 0; k < childCount; ++k) {
                final View child2 = this.getChildAt(k);
                if (child2.getId() == this.mConstraintSetId && child2 instanceof Constraints) {
                    this.mConstraintSet = ((Constraints)child2).getConstraintSet();
                }
            }
        }
        final ConstraintSet mConstraintSet = this.mConstraintSet;
        if (mConstraintSet != null) {
            mConstraintSet.applyToInternal(this);
        }
        this.mLayoutWidget.removeAllChildren();
        final int size = this.mConstraintHelpers.size();
        if (size > 0) {
            for (int l = 0; l < size; ++l) {
                this.mConstraintHelpers.get(l).updatePreLayout(this);
            }
        }
        for (int n = 0; n < childCount; ++n) {
            final View child3 = this.getChildAt(n);
            if (child3 instanceof Placeholder) {
                ((Placeholder)child3).updatePreLayout(this);
            }
        }
        for (int n2 = 0; n2 < childCount; ++n2) {
            final View child4 = this.getChildAt(n2);
            final ConstraintWidget viewWidget2 = this.getViewWidget(child4);
            if (viewWidget2 != null) {
                final LayoutParams layoutParams = (LayoutParams)child4.getLayoutParams();
                layoutParams.validate();
                if (layoutParams.helped) {
                    layoutParams.helped = false;
                }
                else if (inEditMode) {
                    try {
                        final String resourceName2 = this.getResources().getResourceName(child4.getId());
                        this.setDesignInformation(0, resourceName2, child4.getId());
                        this.getTargetWidget(child4.getId()).setDebugName(resourceName2.substring(resourceName2.indexOf("id/") + 3));
                    }
                    catch (Resources$NotFoundException ex2) {}
                }
                viewWidget2.setVisibility(child4.getVisibility());
                if (layoutParams.isInPlaceholder) {
                    viewWidget2.setVisibility(8);
                }
                viewWidget2.setCompanionWidget(child4);
                this.mLayoutWidget.add(viewWidget2);
                if (!layoutParams.verticalDimensionFixed || !layoutParams.horizontalDimensionFixed) {
                    this.mVariableDimensionsWidgets.add(viewWidget2);
                }
                if (layoutParams.isGuideline) {
                    final Guideline guideline = (Guideline)viewWidget2;
                    int guideBegin = layoutParams.resolvedGuideBegin;
                    int guideEnd = layoutParams.resolvedGuideEnd;
                    float guidePercent = layoutParams.resolvedGuidePercent;
                    if (Build$VERSION.SDK_INT < 17) {
                        guideBegin = layoutParams.guideBegin;
                        guideEnd = layoutParams.guideEnd;
                        guidePercent = layoutParams.guidePercent;
                    }
                    if (guidePercent != -1.0f) {
                        guideline.setGuidePercent(guidePercent);
                    }
                    else if (guideBegin != -1) {
                        guideline.setGuideBegin(guideBegin);
                    }
                    else if (guideEnd != -1) {
                        guideline.setGuideEnd(guideEnd);
                    }
                }
                else if (layoutParams.leftToLeft != -1 || layoutParams.leftToRight != -1 || layoutParams.rightToLeft != -1 || layoutParams.rightToRight != -1 || layoutParams.startToStart != -1 || layoutParams.startToEnd != -1 || layoutParams.endToStart != -1 || layoutParams.endToEnd != -1 || layoutParams.topToTop != -1 || layoutParams.topToBottom != -1 || layoutParams.bottomToTop != -1 || layoutParams.bottomToBottom != -1 || layoutParams.baselineToBaseline != -1 || layoutParams.editorAbsoluteX != -1 || layoutParams.editorAbsoluteY != -1 || layoutParams.circleConstraint != -1 || layoutParams.width == -1 || layoutParams.height == -1) {
                    int resolvedLeftToLeft = layoutParams.resolvedLeftToLeft;
                    int resolvedLeftToRight = layoutParams.resolvedLeftToRight;
                    int resolvedRightToLeft = layoutParams.resolvedRightToLeft;
                    int resolvedRightToRight = layoutParams.resolvedRightToRight;
                    int resolveGoneLeftMargin = layoutParams.resolveGoneLeftMargin;
                    int resolveGoneRightMargin = layoutParams.resolveGoneRightMargin;
                    float horizontalBiasPercent = layoutParams.resolvedHorizontalBias;
                    Label_1107: {
                        if (Build$VERSION.SDK_INT < 17) {
                            final int leftToLeft = layoutParams.leftToLeft;
                            final int leftToRight = layoutParams.leftToRight;
                            final int rightToLeft = layoutParams.rightToLeft;
                            final int rightToRight = layoutParams.rightToRight;
                            final int goneLeftMargin = layoutParams.goneLeftMargin;
                            final int goneRightMargin = layoutParams.goneRightMargin;
                            horizontalBiasPercent = layoutParams.horizontalBias;
                            int n3 = 0;
                            int n4 = 0;
                            Label_0964: {
                                if (leftToLeft == -1 && leftToRight == -1) {
                                    if (layoutParams.startToStart != -1) {
                                        final int startToStart = layoutParams.startToStart;
                                        n3 = leftToRight;
                                        n4 = startToStart;
                                        break Label_0964;
                                    }
                                    if (layoutParams.startToEnd != -1) {
                                        final int startToEnd = layoutParams.startToEnd;
                                        n4 = leftToLeft;
                                        n3 = startToEnd;
                                        break Label_0964;
                                    }
                                }
                                final int n5 = leftToLeft;
                                n3 = leftToRight;
                                n4 = n5;
                            }
                            if (rightToLeft == -1 && rightToRight == -1) {
                                final int n6 = n4;
                                if (layoutParams.endToStart != -1) {
                                    final int endToStart = layoutParams.endToStart;
                                    final int n7 = n6;
                                    final int n8 = goneLeftMargin;
                                    resolvedLeftToRight = n3;
                                    resolvedRightToRight = rightToRight;
                                    resolvedLeftToLeft = n7;
                                    resolveGoneRightMargin = goneRightMargin;
                                    resolvedRightToLeft = endToStart;
                                    resolveGoneLeftMargin = n8;
                                    break Label_1107;
                                }
                                if (layoutParams.endToEnd != -1) {
                                    final int endToEnd = layoutParams.endToEnd;
                                    final int n9 = n6;
                                    resolveGoneLeftMargin = goneLeftMargin;
                                    resolvedLeftToRight = n3;
                                    resolvedRightToLeft = rightToLeft;
                                    resolvedRightToRight = endToEnd;
                                    resolvedLeftToLeft = n9;
                                    resolveGoneRightMargin = goneRightMargin;
                                    break Label_1107;
                                }
                            }
                            final int n10 = n4;
                            final int n11 = goneLeftMargin;
                            resolvedLeftToRight = n3;
                            resolvedRightToLeft = rightToLeft;
                            resolvedRightToRight = rightToRight;
                            resolvedLeftToLeft = n10;
                            resolveGoneRightMargin = goneRightMargin;
                            resolveGoneLeftMargin = n11;
                        }
                    }
                    if (layoutParams.circleConstraint != -1) {
                        final ConstraintWidget targetWidget = this.getTargetWidget(layoutParams.circleConstraint);
                        if (targetWidget != null) {
                            viewWidget2.connectCircularConstraint(targetWidget, layoutParams.circleAngle, layoutParams.circleRadius);
                        }
                    }
                    else {
                        if (resolvedLeftToLeft != -1) {
                            final ConstraintWidget targetWidget2 = this.getTargetWidget(resolvedLeftToLeft);
                            if (targetWidget2 != null) {
                                viewWidget2.immediateConnect(ConstraintAnchor.Type.LEFT, targetWidget2, ConstraintAnchor.Type.LEFT, layoutParams.leftMargin, resolveGoneLeftMargin);
                            }
                        }
                        else if (resolvedLeftToRight != -1) {
                            final ConstraintWidget targetWidget3 = this.getTargetWidget(resolvedLeftToRight);
                            if (targetWidget3 != null) {
                                viewWidget2.immediateConnect(ConstraintAnchor.Type.LEFT, targetWidget3, ConstraintAnchor.Type.RIGHT, layoutParams.leftMargin, resolveGoneLeftMargin);
                            }
                        }
                        final LayoutParams layoutParams2 = layoutParams;
                        if (resolvedRightToLeft != -1) {
                            final ConstraintWidget targetWidget4 = this.getTargetWidget(resolvedRightToLeft);
                            if (targetWidget4 != null) {
                                viewWidget2.immediateConnect(ConstraintAnchor.Type.RIGHT, targetWidget4, ConstraintAnchor.Type.LEFT, layoutParams2.rightMargin, resolveGoneRightMargin);
                            }
                        }
                        else if (resolvedRightToRight != -1) {
                            final ConstraintWidget targetWidget5 = this.getTargetWidget(resolvedRightToRight);
                            if (targetWidget5 != null) {
                                viewWidget2.immediateConnect(ConstraintAnchor.Type.RIGHT, targetWidget5, ConstraintAnchor.Type.RIGHT, layoutParams2.rightMargin, resolveGoneRightMargin);
                            }
                        }
                        if (layoutParams2.topToTop != -1) {
                            final ConstraintWidget targetWidget6 = this.getTargetWidget(layoutParams2.topToTop);
                            if (targetWidget6 != null) {
                                viewWidget2.immediateConnect(ConstraintAnchor.Type.TOP, targetWidget6, ConstraintAnchor.Type.TOP, layoutParams2.topMargin, layoutParams2.goneTopMargin);
                            }
                        }
                        else if (layoutParams2.topToBottom != -1) {
                            final ConstraintWidget targetWidget7 = this.getTargetWidget(layoutParams2.topToBottom);
                            if (targetWidget7 != null) {
                                viewWidget2.immediateConnect(ConstraintAnchor.Type.TOP, targetWidget7, ConstraintAnchor.Type.BOTTOM, layoutParams2.topMargin, layoutParams2.goneTopMargin);
                            }
                        }
                        if (layoutParams2.bottomToTop != -1) {
                            final ConstraintWidget targetWidget8 = this.getTargetWidget(layoutParams2.bottomToTop);
                            if (targetWidget8 != null) {
                                viewWidget2.immediateConnect(ConstraintAnchor.Type.BOTTOM, targetWidget8, ConstraintAnchor.Type.TOP, layoutParams2.bottomMargin, layoutParams2.goneBottomMargin);
                            }
                        }
                        else if (layoutParams2.bottomToBottom != -1) {
                            final ConstraintWidget targetWidget9 = this.getTargetWidget(layoutParams2.bottomToBottom);
                            if (targetWidget9 != null) {
                                viewWidget2.immediateConnect(ConstraintAnchor.Type.BOTTOM, targetWidget9, ConstraintAnchor.Type.BOTTOM, layoutParams2.bottomMargin, layoutParams2.goneBottomMargin);
                            }
                        }
                        if (layoutParams2.baselineToBaseline != -1) {
                            final View view = (View)this.mChildrenByIds.get(layoutParams2.baselineToBaseline);
                            final ConstraintWidget targetWidget10 = this.getTargetWidget(layoutParams2.baselineToBaseline);
                            if (targetWidget10 != null && view != null && view.getLayoutParams() instanceof LayoutParams) {
                                final LayoutParams layoutParams3 = (LayoutParams)view.getLayoutParams();
                                layoutParams2.needsBaseline = true;
                                layoutParams3.needsBaseline = true;
                                viewWidget2.getAnchor(ConstraintAnchor.Type.BASELINE).connect(targetWidget10.getAnchor(ConstraintAnchor.Type.BASELINE), 0, -1, ConstraintAnchor.Strength.STRONG, 0, true);
                                viewWidget2.getAnchor(ConstraintAnchor.Type.TOP).reset();
                                viewWidget2.getAnchor(ConstraintAnchor.Type.BOTTOM).reset();
                            }
                        }
                        if (horizontalBiasPercent >= 0.0f && horizontalBiasPercent != 0.5f) {
                            viewWidget2.setHorizontalBiasPercent(horizontalBiasPercent);
                        }
                        if (layoutParams2.verticalBias >= 0.0f && layoutParams2.verticalBias != 0.5f) {
                            viewWidget2.setVerticalBiasPercent(layoutParams2.verticalBias);
                        }
                    }
                    if (inEditMode && (layoutParams.editorAbsoluteX != -1 || layoutParams.editorAbsoluteY != -1)) {
                        viewWidget2.setOrigin(layoutParams.editorAbsoluteX, layoutParams.editorAbsoluteY);
                    }
                    if (!layoutParams.horizontalDimensionFixed) {
                        if (layoutParams.width == -1) {
                            viewWidget2.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
                            viewWidget2.getAnchor(ConstraintAnchor.Type.LEFT).mMargin = layoutParams.leftMargin;
                            viewWidget2.getAnchor(ConstraintAnchor.Type.RIGHT).mMargin = layoutParams.rightMargin;
                        }
                        else {
                            viewWidget2.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                            viewWidget2.setWidth(0);
                        }
                    }
                    else {
                        viewWidget2.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                        viewWidget2.setWidth(layoutParams.width);
                    }
                    if (!layoutParams.verticalDimensionFixed) {
                        if (layoutParams.height == -1) {
                            viewWidget2.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
                            viewWidget2.getAnchor(ConstraintAnchor.Type.TOP).mMargin = layoutParams.topMargin;
                            viewWidget2.getAnchor(ConstraintAnchor.Type.BOTTOM).mMargin = layoutParams.bottomMargin;
                        }
                        else {
                            viewWidget2.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                            viewWidget2.setHeight(0);
                        }
                    }
                    else {
                        viewWidget2.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                        viewWidget2.setHeight(layoutParams.height);
                    }
                    if (layoutParams.dimensionRatio != null) {
                        viewWidget2.setDimensionRatio(layoutParams.dimensionRatio);
                    }
                    viewWidget2.setHorizontalWeight(layoutParams.horizontalWeight);
                    viewWidget2.setVerticalWeight(layoutParams.verticalWeight);
                    viewWidget2.setHorizontalChainStyle(layoutParams.horizontalChainStyle);
                    viewWidget2.setVerticalChainStyle(layoutParams.verticalChainStyle);
                    viewWidget2.setHorizontalMatchStyle(layoutParams.matchConstraintDefaultWidth, layoutParams.matchConstraintMinWidth, layoutParams.matchConstraintMaxWidth, layoutParams.matchConstraintPercentWidth);
                    viewWidget2.setVerticalMatchStyle(layoutParams.matchConstraintDefaultHeight, layoutParams.matchConstraintMinHeight, layoutParams.matchConstraintMaxHeight, layoutParams.matchConstraintPercentHeight);
                }
            }
        }
    }
    
    private void setSelfDimensionBehaviour(int size, int size2) {
        final int mode = View$MeasureSpec.getMode(size);
        size = View$MeasureSpec.getSize(size);
        final int mode2 = View$MeasureSpec.getMode(size2);
        size2 = View$MeasureSpec.getSize(size2);
        final int paddingTop = this.getPaddingTop();
        final int paddingBottom = this.getPaddingBottom();
        final int paddingLeft = this.getPaddingLeft();
        final int paddingRight = this.getPaddingRight();
        ConstraintWidget.DimensionBehaviour horizontalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
        ConstraintWidget.DimensionBehaviour verticalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
        final int n = 0;
        final int n2 = 0;
        this.getLayoutParams();
        if (mode != Integer.MIN_VALUE) {
            if (mode != 0) {
                if (mode != 1073741824) {
                    size = n;
                }
                else {
                    size = Math.min(this.mMaxWidth, size) - (paddingLeft + paddingRight);
                }
            }
            else {
                horizontalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                size = n;
            }
        }
        else {
            horizontalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        }
        if (mode2 != Integer.MIN_VALUE) {
            if (mode2 != 0) {
                if (mode2 != 1073741824) {
                    size2 = n2;
                }
                else {
                    size2 = Math.min(this.mMaxHeight, size2) - (paddingTop + paddingBottom);
                }
            }
            else {
                verticalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                size2 = n2;
            }
        }
        else {
            verticalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        }
        this.mLayoutWidget.setMinWidth(0);
        this.mLayoutWidget.setMinHeight(0);
        this.mLayoutWidget.setHorizontalDimensionBehaviour(horizontalDimensionBehaviour);
        this.mLayoutWidget.setWidth(size);
        this.mLayoutWidget.setVerticalDimensionBehaviour(verticalDimensionBehaviour);
        this.mLayoutWidget.setHeight(size2);
        this.mLayoutWidget.setMinWidth(this.mMinWidth - this.getPaddingLeft() - this.getPaddingRight());
        this.mLayoutWidget.setMinHeight(this.mMinHeight - this.getPaddingTop() - this.getPaddingBottom());
    }
    
    private void updateHierarchy() {
        final int childCount = this.getChildCount();
        final int n = 0;
        int n2 = 0;
        int n3;
        while (true) {
            n3 = n;
            if (n2 >= childCount) {
                break;
            }
            if (this.getChildAt(n2).isLayoutRequested()) {
                n3 = 1;
                break;
            }
            ++n2;
        }
        if (n3 != 0) {
            this.mVariableDimensionsWidgets.clear();
            this.setChildrenConstraints();
        }
    }
    
    private void updatePostMeasures() {
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child instanceof Placeholder) {
                ((Placeholder)child).updatePostMeasure(this);
            }
        }
        final int size = this.mConstraintHelpers.size();
        if (size > 0) {
            for (int j = 0; j < size; ++j) {
                this.mConstraintHelpers.get(j).updatePostMeasure(this);
            }
        }
    }
    
    public void addView(final View view, final int n, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        super.addView(view, n, viewGroup$LayoutParams);
        if (Build$VERSION.SDK_INT < 14) {
            this.onViewAdded(view);
        }
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams instanceof LayoutParams;
    }
    
    public void dispatchDraw(final Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.isInEditMode()) {
            final int childCount = this.getChildCount();
            final float n = (float)this.getWidth();
            final float n2 = (float)this.getHeight();
            final float n3 = 1080.0f;
            for (int i = 0; i < childCount; ++i) {
                final View child = this.getChildAt(i);
                if (child.getVisibility() != 8) {
                    final Object tag = child.getTag();
                    if (tag != null && tag instanceof String) {
                        final String[] split = ((String)tag).split(",");
                        if (split.length == 4) {
                            final int int1 = Integer.parseInt(split[0]);
                            final int int2 = Integer.parseInt(split[1]);
                            final int int3 = Integer.parseInt(split[2]);
                            final int int4 = Integer.parseInt(split[3]);
                            final int n4 = (int)(int1 / n3 * n);
                            final int n5 = (int)(int2 / 1920.0f * n2);
                            final int n6 = (int)(int3 / n3 * n);
                            final int n7 = (int)(int4 / 1920.0f * n2);
                            final Paint paint = new Paint();
                            paint.setColor(-65536);
                            canvas.drawLine((float)n4, (float)n5, (float)(n4 + n6), (float)n5, paint);
                            canvas.drawLine((float)(n4 + n6), (float)n5, (float)(n4 + n6), (float)(n5 + n7), paint);
                            canvas.drawLine((float)(n4 + n6), (float)(n5 + n7), (float)n4, (float)(n5 + n7), paint);
                            canvas.drawLine((float)n4, (float)(n5 + n7), (float)n4, (float)n5, paint);
                            paint.setColor(-16711936);
                            canvas.drawLine((float)n4, (float)n5, (float)(n4 + n6), (float)(n5 + n7), paint);
                            canvas.drawLine((float)n4, (float)(n5 + n7), (float)(n4 + n6), (float)n5, paint);
                        }
                    }
                }
            }
        }
    }
    
    public void fillMetrics(final Metrics mMetrics) {
        this.mMetrics = mMetrics;
        this.mLayoutWidget.fillMetrics(mMetrics);
    }
    
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }
    
    protected ViewGroup$LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return (ViewGroup$LayoutParams)new LayoutParams(viewGroup$LayoutParams);
    }
    
    public LayoutParams generateLayoutParams(final AttributeSet set) {
        return new LayoutParams(this.getContext(), set);
    }
    
    public Object getDesignInformation(final int n, final Object o) {
        if (n == 0 && o instanceof String) {
            final String s = (String)o;
            final HashMap<String, Integer> mDesignIds = this.mDesignIds;
            if (mDesignIds != null && mDesignIds.containsKey(s)) {
                return this.mDesignIds.get(s);
            }
        }
        return null;
    }
    
    public int getMaxHeight() {
        return this.mMaxHeight;
    }
    
    public int getMaxWidth() {
        return this.mMaxWidth;
    }
    
    public int getMinHeight() {
        return this.mMinHeight;
    }
    
    public int getMinWidth() {
        return this.mMinWidth;
    }
    
    public int getOptimizationLevel() {
        return this.mLayoutWidget.getOptimizationLevel();
    }
    
    public View getViewById(final int n) {
        return (View)this.mChildrenByIds.get(n);
    }
    
    public final ConstraintWidget getViewWidget(final View view) {
        if (view == this) {
            return this.mLayoutWidget;
        }
        ConstraintWidget widget;
        if (view == null) {
            widget = null;
        }
        else {
            widget = ((LayoutParams)view.getLayoutParams()).widget;
        }
        return widget;
    }
    
    protected void onLayout(final boolean b, int i, int n, int drawY, int drawX) {
        n = this.getChildCount();
        final boolean inEditMode = this.isInEditMode();
        View child;
        LayoutParams layoutParams;
        ConstraintWidget widget;
        int n2;
        int n3;
        View content;
        for (i = 0; i < n; ++i) {
            child = this.getChildAt(i);
            layoutParams = (LayoutParams)child.getLayoutParams();
            widget = layoutParams.widget;
            if (child.getVisibility() != 8 || layoutParams.isGuideline || layoutParams.isHelper || inEditMode) {
                if (!layoutParams.isInPlaceholder) {
                    drawX = widget.getDrawX();
                    drawY = widget.getDrawY();
                    n2 = widget.getWidth() + drawX;
                    n3 = widget.getHeight() + drawY;
                    child.layout(drawX, drawY, n2, n3);
                    if (child instanceof Placeholder) {
                        content = ((Placeholder)child).getContent();
                        if (content != null) {
                            content.setVisibility(0);
                            content.layout(drawX, drawY, n2, n3);
                        }
                    }
                }
            }
        }
        n = this.mConstraintHelpers.size();
        if (n > 0) {
            for (i = 0; i < n; ++i) {
                this.mConstraintHelpers.get(i).updatePostLayout(this);
            }
        }
    }
    
    protected void onMeasure(int resolveSizeAndState, int min) {
        System.currentTimeMillis();
        final int mode = View$MeasureSpec.getMode(resolveSizeAndState);
        final int size = View$MeasureSpec.getSize(resolveSizeAndState);
        final int mode2 = View$MeasureSpec.getMode(min);
        final int size2 = View$MeasureSpec.getSize(min);
        final int paddingLeft = this.getPaddingLeft();
        final int paddingTop = this.getPaddingTop();
        this.mLayoutWidget.setX(paddingLeft);
        this.mLayoutWidget.setY(paddingTop);
        this.mLayoutWidget.setMaxWidth(this.mMaxWidth);
        this.mLayoutWidget.setMaxHeight(this.mMaxHeight);
        if (Build$VERSION.SDK_INT >= 17) {
            this.mLayoutWidget.setRtl(this.getLayoutDirection() == 1);
        }
        this.setSelfDimensionBehaviour(resolveSizeAndState, min);
        final int width = this.mLayoutWidget.getWidth();
        final int height = this.mLayoutWidget.getHeight();
        boolean b = false;
        if (this.mDirtyHierarchy) {
            this.mDirtyHierarchy = false;
            this.updateHierarchy();
            b = true;
        }
        final boolean b2 = (this.mOptimizationLevel & 0x8) == 0x8;
        if (b2) {
            this.mLayoutWidget.preOptimize();
            this.mLayoutWidget.optimizeForDimensions(width, height);
            this.internalMeasureDimensions(resolveSizeAndState, min);
        }
        else {
            this.internalMeasureChildren(resolveSizeAndState, min);
        }
        this.updatePostMeasures();
        if (this.getChildCount() > 0 && b) {
            Analyzer.determineGroups(this.mLayoutWidget);
        }
        if (this.mLayoutWidget.mGroupsWrapOptimized) {
            if (this.mLayoutWidget.mHorizontalWrapOptimized && mode == Integer.MIN_VALUE) {
                if (this.mLayoutWidget.mWrapFixedWidth < size) {
                    final ConstraintWidgetContainer mLayoutWidget = this.mLayoutWidget;
                    mLayoutWidget.setWidth(mLayoutWidget.mWrapFixedWidth);
                }
                this.mLayoutWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
            }
            if (this.mLayoutWidget.mVerticalWrapOptimized && mode2 == Integer.MIN_VALUE) {
                if (this.mLayoutWidget.mWrapFixedHeight < size2) {
                    final ConstraintWidgetContainer mLayoutWidget2 = this.mLayoutWidget;
                    mLayoutWidget2.setHeight(mLayoutWidget2.mWrapFixedHeight);
                }
                this.mLayoutWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
            }
        }
        final int mOptimizationLevel = this.mOptimizationLevel;
        int n = 0;
        if ((mOptimizationLevel & 0x20) == 0x20) {
            final int width2 = this.mLayoutWidget.getWidth();
            final int height2 = this.mLayoutWidget.getHeight();
            if (this.mLastMeasureWidth != width2 && mode == 1073741824) {
                Analyzer.setPosition(this.mLayoutWidget.mWidgetGroups, 0, width2);
            }
            if (this.mLastMeasureHeight != height2 && mode2 == 1073741824) {
                Analyzer.setPosition(this.mLayoutWidget.mWidgetGroups, 1, height2);
            }
            if (this.mLayoutWidget.mHorizontalWrapOptimized && this.mLayoutWidget.mWrapFixedWidth > size) {
                Analyzer.setPosition(this.mLayoutWidget.mWidgetGroups, 0, size);
            }
            if (this.mLayoutWidget.mVerticalWrapOptimized && this.mLayoutWidget.mWrapFixedHeight > size2) {
                Analyzer.setPosition(this.mLayoutWidget.mWidgetGroups, 1, size2);
            }
        }
        final int n2 = 0;
        if (this.getChildCount() > 0) {
            this.solveLinearSystem("First pass");
        }
        final int size3 = this.mVariableDimensionsWidgets.size();
        final int n3 = this.getPaddingBottom() + paddingTop;
        final int n4 = paddingLeft + this.getPaddingRight();
        int n18;
        if (size3 > 0) {
            final boolean b3 = this.mLayoutWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            final boolean b4 = this.mLayoutWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            int n5 = Math.max(this.mLayoutWidget.getWidth(), this.mMinWidth);
            int max = Math.max(this.mLayoutWidget.getHeight(), this.mMinHeight);
            int n6 = 0;
            int n7 = 0;
            int combineMeasuredStates;
            int n8;
            int n9;
            int n10;
            for (int i = 0; i < size3; ++i, n6 = combineMeasuredStates, n5 = n8, max = n9, n7 = n10) {
                final ConstraintWidget constraintWidget = this.mVariableDimensionsWidgets.get(i);
                final View view = (View)constraintWidget.getCompanionWidget();
                if (view == null) {
                    combineMeasuredStates = n6;
                    n8 = n5;
                    n9 = max;
                    n10 = n7;
                }
                else {
                    final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                    if (!layoutParams.isHelper) {
                        if (layoutParams.isGuideline) {
                            combineMeasuredStates = n6;
                            n8 = n5;
                            n9 = max;
                            n10 = n7;
                        }
                        else if (view.getVisibility() == 8) {
                            combineMeasuredStates = n6;
                            n8 = n5;
                            n9 = max;
                            n10 = n7;
                        }
                        else if (b2 && constraintWidget.getResolutionWidth().isResolved() && constraintWidget.getResolutionHeight().isResolved()) {
                            combineMeasuredStates = n6;
                            n8 = n5;
                            n9 = max;
                            n10 = n7;
                        }
                        else {
                            int n11;
                            if (layoutParams.width == -2 && layoutParams.horizontalDimensionFixed) {
                                n11 = getChildMeasureSpec(resolveSizeAndState, n4, layoutParams.width);
                            }
                            else {
                                n11 = View$MeasureSpec.makeMeasureSpec(constraintWidget.getWidth(), 1073741824);
                            }
                            int n12;
                            if (layoutParams.height == -2 && layoutParams.verticalDimensionFixed) {
                                n12 = getChildMeasureSpec(min, n3, layoutParams.height);
                            }
                            else {
                                n12 = View$MeasureSpec.makeMeasureSpec(constraintWidget.getHeight(), 1073741824);
                            }
                            view.measure(n11, n12);
                            final Metrics mMetrics = this.mMetrics;
                            if (mMetrics != null) {
                                ++mMetrics.additionalMeasures;
                            }
                            final int n13 = n + 1;
                            final int measuredWidth = view.getMeasuredWidth();
                            final int measuredHeight = view.getMeasuredHeight();
                            int n14;
                            if (measuredWidth != constraintWidget.getWidth()) {
                                constraintWidget.setWidth(measuredWidth);
                                if (b2) {
                                    constraintWidget.getResolutionWidth().resolve(measuredWidth);
                                }
                                if (b3 && constraintWidget.getRight() > n5) {
                                    n5 = Math.max(n5, constraintWidget.getRight() + constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).getMargin());
                                }
                                n14 = 1;
                            }
                            else {
                                n14 = n7;
                            }
                            int max2 = max;
                            if (measuredHeight != constraintWidget.getHeight()) {
                                constraintWidget.setHeight(measuredHeight);
                                if (b2) {
                                    constraintWidget.getResolutionHeight().resolve(measuredHeight);
                                }
                                max2 = max;
                                if (b4 && constraintWidget.getBottom() > (max2 = max)) {
                                    max2 = Math.max(max, constraintWidget.getBottom() + constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).getMargin());
                                }
                                n14 = 1;
                            }
                            int n15 = n14;
                            if (layoutParams.needsBaseline) {
                                final int baseline = view.getBaseline();
                                n15 = n14;
                                if (baseline != -1) {
                                    n15 = n14;
                                    if (baseline != constraintWidget.getBaselineDistance()) {
                                        constraintWidget.setBaselineDistance(baseline);
                                        n15 = 1;
                                    }
                                }
                            }
                            combineMeasuredStates = n6;
                            n8 = n5;
                            n9 = max2;
                            n10 = n15;
                            n = n13;
                            if (Build$VERSION.SDK_INT >= 11) {
                                combineMeasuredStates = combineMeasuredStates(n6, view.getMeasuredState());
                                n8 = n5;
                                n9 = max2;
                                n10 = n15;
                                n = n13;
                            }
                        }
                    }
                    else {
                        n10 = n7;
                        n9 = max;
                        n8 = n5;
                        combineMeasuredStates = n6;
                    }
                }
            }
            final int n16 = size3;
            if (n7 != 0) {
                this.mLayoutWidget.setWidth(width);
                this.mLayoutWidget.setHeight(height);
                if (b2) {
                    this.mLayoutWidget.solveGraph();
                }
                this.solveLinearSystem("2nd pass");
                boolean b5 = false;
                if (this.mLayoutWidget.getWidth() < n5) {
                    this.mLayoutWidget.setWidth(n5);
                    b5 = true;
                }
                if (this.mLayoutWidget.getHeight() < max) {
                    this.mLayoutWidget.setHeight(max);
                    b5 = true;
                }
                if (b5) {
                    this.solveLinearSystem("3rd pass");
                }
            }
            int j = 0;
            int n17 = n2;
            while (j < n16) {
                final ConstraintWidget constraintWidget2 = this.mVariableDimensionsWidgets.get(j);
                final View view2 = (View)constraintWidget2.getCompanionWidget();
                if (view2 != null) {
                    if (view2.getMeasuredWidth() != constraintWidget2.getWidth() || view2.getMeasuredHeight() != constraintWidget2.getHeight()) {
                        if (constraintWidget2.getVisibility() != 8) {
                            view2.measure(View$MeasureSpec.makeMeasureSpec(constraintWidget2.getWidth(), 1073741824), View$MeasureSpec.makeMeasureSpec(constraintWidget2.getHeight(), 1073741824));
                            final Metrics mMetrics2 = this.mMetrics;
                            if (mMetrics2 != null) {
                                ++mMetrics2.additionalMeasures;
                            }
                            ++n17;
                        }
                    }
                }
                ++j;
            }
            n18 = n6;
        }
        else {
            n18 = 0;
        }
        final int mLastMeasureWidth = this.mLayoutWidget.getWidth() + n4;
        final int mLastMeasureHeight = this.mLayoutWidget.getHeight() + n3;
        if (Build$VERSION.SDK_INT >= 11) {
            resolveSizeAndState = resolveSizeAndState(mLastMeasureWidth, resolveSizeAndState, n18);
            final int resolveSizeAndState2 = resolveSizeAndState(mLastMeasureHeight, min, n18 << 16);
            min = Math.min(this.mMaxWidth, resolveSizeAndState & 0xFFFFFF);
            final int min2 = Math.min(this.mMaxHeight, resolveSizeAndState2 & 0xFFFFFF);
            resolveSizeAndState = min;
            if (this.mLayoutWidget.isWidthMeasuredTooSmall()) {
                resolveSizeAndState = (min | 0x1000000);
            }
            min = min2;
            if (this.mLayoutWidget.isHeightMeasuredTooSmall()) {
                min = (min2 | 0x1000000);
            }
            this.setMeasuredDimension(resolveSizeAndState, min);
            this.mLastMeasureWidth = resolveSizeAndState;
            this.mLastMeasureHeight = min;
        }
        else {
            this.setMeasuredDimension(mLastMeasureWidth, mLastMeasureHeight);
            this.mLastMeasureWidth = mLastMeasureWidth;
            this.mLastMeasureHeight = mLastMeasureHeight;
        }
    }
    
    public void onViewAdded(final View view) {
        if (Build$VERSION.SDK_INT >= 14) {
            super.onViewAdded(view);
        }
        final ConstraintWidget viewWidget = this.getViewWidget(view);
        if (view instanceof androidx.constraintlayout.widget.Guideline && !(viewWidget instanceof Guideline)) {
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            layoutParams.widget = new Guideline();
            layoutParams.isGuideline = true;
            ((Guideline)layoutParams.widget).setOrientation(layoutParams.orientation);
        }
        if (view instanceof ConstraintHelper) {
            final ConstraintHelper constraintHelper = (ConstraintHelper)view;
            constraintHelper.validateParams();
            ((LayoutParams)view.getLayoutParams()).isHelper = true;
            if (!this.mConstraintHelpers.contains(constraintHelper)) {
                this.mConstraintHelpers.add(constraintHelper);
            }
        }
        this.mChildrenByIds.put(view.getId(), (Object)view);
        this.mDirtyHierarchy = true;
    }
    
    public void onViewRemoved(final View o) {
        if (Build$VERSION.SDK_INT >= 14) {
            super.onViewRemoved(o);
        }
        this.mChildrenByIds.remove(o.getId());
        final ConstraintWidget viewWidget = this.getViewWidget(o);
        this.mLayoutWidget.remove(viewWidget);
        this.mConstraintHelpers.remove(o);
        this.mVariableDimensionsWidgets.remove(viewWidget);
        this.mDirtyHierarchy = true;
    }
    
    public void removeView(final View view) {
        super.removeView(view);
        if (Build$VERSION.SDK_INT < 14) {
            this.onViewRemoved(view);
        }
    }
    
    public void requestLayout() {
        super.requestLayout();
        this.mDirtyHierarchy = true;
        this.mLastMeasureWidth = -1;
        this.mLastMeasureHeight = -1;
        this.mLastMeasureWidthSize = -1;
        this.mLastMeasureHeightSize = -1;
        this.mLastMeasureWidthMode = 0;
        this.mLastMeasureHeightMode = 0;
    }
    
    public void setConstraintSet(final ConstraintSet mConstraintSet) {
        this.mConstraintSet = mConstraintSet;
    }
    
    public void setDesignInformation(int i, final Object o, final Object o2) {
        if (i == 0 && o instanceof String && o2 instanceof Integer) {
            if (this.mDesignIds == null) {
                this.mDesignIds = new HashMap<String, Integer>();
            }
            final String s = (String)o;
            i = s.indexOf("/");
            String substring = s;
            if (i != -1) {
                substring = s.substring(i + 1);
            }
            i = (int)o2;
            this.mDesignIds.put(substring, i);
        }
    }
    
    public void setId(final int id) {
        this.mChildrenByIds.remove(this.getId());
        super.setId(id);
        this.mChildrenByIds.put(this.getId(), (Object)this);
    }
    
    public void setMaxHeight(final int mMaxHeight) {
        if (mMaxHeight == this.mMaxHeight) {
            return;
        }
        this.mMaxHeight = mMaxHeight;
        this.requestLayout();
    }
    
    public void setMaxWidth(final int mMaxWidth) {
        if (mMaxWidth == this.mMaxWidth) {
            return;
        }
        this.mMaxWidth = mMaxWidth;
        this.requestLayout();
    }
    
    public void setMinHeight(final int mMinHeight) {
        if (mMinHeight == this.mMinHeight) {
            return;
        }
        this.mMinHeight = mMinHeight;
        this.requestLayout();
    }
    
    public void setMinWidth(final int mMinWidth) {
        if (mMinWidth == this.mMinWidth) {
            return;
        }
        this.mMinWidth = mMinWidth;
        this.requestLayout();
    }
    
    public void setOptimizationLevel(final int optimizationLevel) {
        this.mLayoutWidget.setOptimizationLevel(optimizationLevel);
    }
    
    public boolean shouldDelayChildPressedState() {
        return false;
    }
    
    protected void solveLinearSystem(final String s) {
        this.mLayoutWidget.layout();
        final Metrics mMetrics = this.mMetrics;
        if (mMetrics != null) {
            ++mMetrics.resolutions;
        }
    }
    
    public static class LayoutParams extends ViewGroup$MarginLayoutParams
    {
        public static final int BASELINE = 5;
        public static final int BOTTOM = 4;
        public static final int CHAIN_PACKED = 2;
        public static final int CHAIN_SPREAD = 0;
        public static final int CHAIN_SPREAD_INSIDE = 1;
        public static final int END = 7;
        public static final int HORIZONTAL = 0;
        public static final int LEFT = 1;
        public static final int MATCH_CONSTRAINT = 0;
        public static final int MATCH_CONSTRAINT_PERCENT = 2;
        public static final int MATCH_CONSTRAINT_SPREAD = 0;
        public static final int MATCH_CONSTRAINT_WRAP = 1;
        public static final int PARENT_ID = 0;
        public static final int RIGHT = 2;
        public static final int START = 6;
        public static final int TOP = 3;
        public static final int UNSET = -1;
        public static final int VERTICAL = 1;
        public int baselineToBaseline;
        public int bottomToBottom;
        public int bottomToTop;
        public float circleAngle;
        public int circleConstraint;
        public int circleRadius;
        public boolean constrainedHeight;
        public boolean constrainedWidth;
        public String dimensionRatio;
        int dimensionRatioSide;
        float dimensionRatioValue;
        public int editorAbsoluteX;
        public int editorAbsoluteY;
        public int endToEnd;
        public int endToStart;
        public int goneBottomMargin;
        public int goneEndMargin;
        public int goneLeftMargin;
        public int goneRightMargin;
        public int goneStartMargin;
        public int goneTopMargin;
        public int guideBegin;
        public int guideEnd;
        public float guidePercent;
        public boolean helped;
        public float horizontalBias;
        public int horizontalChainStyle;
        boolean horizontalDimensionFixed;
        public float horizontalWeight;
        boolean isGuideline;
        boolean isHelper;
        boolean isInPlaceholder;
        public int leftToLeft;
        public int leftToRight;
        public int matchConstraintDefaultHeight;
        public int matchConstraintDefaultWidth;
        public int matchConstraintMaxHeight;
        public int matchConstraintMaxWidth;
        public int matchConstraintMinHeight;
        public int matchConstraintMinWidth;
        public float matchConstraintPercentHeight;
        public float matchConstraintPercentWidth;
        boolean needsBaseline;
        public int orientation;
        int resolveGoneLeftMargin;
        int resolveGoneRightMargin;
        int resolvedGuideBegin;
        int resolvedGuideEnd;
        float resolvedGuidePercent;
        float resolvedHorizontalBias;
        int resolvedLeftToLeft;
        int resolvedLeftToRight;
        int resolvedRightToLeft;
        int resolvedRightToRight;
        public int rightToLeft;
        public int rightToRight;
        public int startToEnd;
        public int startToStart;
        public int topToBottom;
        public int topToTop;
        public float verticalBias;
        public int verticalChainStyle;
        boolean verticalDimensionFixed;
        public float verticalWeight;
        ConstraintWidget widget;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.guideBegin = -1;
            this.guideEnd = -1;
            this.guidePercent = -1.0f;
            this.leftToLeft = -1;
            this.leftToRight = -1;
            this.rightToLeft = -1;
            this.rightToRight = -1;
            this.topToTop = -1;
            this.topToBottom = -1;
            this.bottomToTop = -1;
            this.bottomToBottom = -1;
            this.baselineToBaseline = -1;
            this.circleConstraint = -1;
            this.circleRadius = 0;
            this.circleAngle = 0.0f;
            this.startToEnd = -1;
            this.startToStart = -1;
            this.endToStart = -1;
            this.endToEnd = -1;
            this.goneLeftMargin = -1;
            this.goneTopMargin = -1;
            this.goneRightMargin = -1;
            this.goneBottomMargin = -1;
            this.goneStartMargin = -1;
            this.goneEndMargin = -1;
            this.horizontalBias = 0.5f;
            this.verticalBias = 0.5f;
            this.dimensionRatio = null;
            this.dimensionRatioValue = 0.0f;
            this.dimensionRatioSide = 1;
            this.horizontalWeight = -1.0f;
            this.verticalWeight = -1.0f;
            this.horizontalChainStyle = 0;
            this.verticalChainStyle = 0;
            this.matchConstraintDefaultWidth = 0;
            this.matchConstraintDefaultHeight = 0;
            this.matchConstraintMinWidth = 0;
            this.matchConstraintMinHeight = 0;
            this.matchConstraintMaxWidth = 0;
            this.matchConstraintMaxHeight = 0;
            this.matchConstraintPercentWidth = 1.0f;
            this.matchConstraintPercentHeight = 1.0f;
            this.editorAbsoluteX = -1;
            this.editorAbsoluteY = -1;
            this.orientation = -1;
            this.constrainedWidth = false;
            this.constrainedHeight = false;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            this.needsBaseline = false;
            this.isGuideline = false;
            this.isHelper = false;
            this.isInPlaceholder = false;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolvedHorizontalBias = 0.5f;
            this.widget = new ConstraintWidget();
            this.helped = false;
        }
        
        public LayoutParams(Context obtainStyledAttributes, final AttributeSet set) {
            super(obtainStyledAttributes, set);
            this.guideBegin = -1;
            this.guideEnd = -1;
            this.guidePercent = -1.0f;
            this.leftToLeft = -1;
            this.leftToRight = -1;
            this.rightToLeft = -1;
            this.rightToRight = -1;
            this.topToTop = -1;
            this.topToBottom = -1;
            this.bottomToTop = -1;
            this.bottomToBottom = -1;
            this.baselineToBaseline = -1;
            this.circleConstraint = -1;
            this.circleRadius = 0;
            this.circleAngle = 0.0f;
            this.startToEnd = -1;
            this.startToStart = -1;
            this.endToStart = -1;
            this.endToEnd = -1;
            this.goneLeftMargin = -1;
            this.goneTopMargin = -1;
            this.goneRightMargin = -1;
            this.goneBottomMargin = -1;
            this.goneStartMargin = -1;
            this.goneEndMargin = -1;
            this.horizontalBias = 0.5f;
            this.verticalBias = 0.5f;
            this.dimensionRatio = null;
            this.dimensionRatioValue = 0.0f;
            this.dimensionRatioSide = 1;
            this.horizontalWeight = -1.0f;
            this.verticalWeight = -1.0f;
            this.horizontalChainStyle = 0;
            this.verticalChainStyle = 0;
            this.matchConstraintDefaultWidth = 0;
            this.matchConstraintDefaultHeight = 0;
            this.matchConstraintMinWidth = 0;
            this.matchConstraintMinHeight = 0;
            this.matchConstraintMaxWidth = 0;
            this.matchConstraintMaxHeight = 0;
            this.matchConstraintPercentWidth = 1.0f;
            this.matchConstraintPercentHeight = 1.0f;
            this.editorAbsoluteX = -1;
            this.editorAbsoluteY = -1;
            this.orientation = -1;
            this.constrainedWidth = false;
            this.constrainedHeight = false;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            this.needsBaseline = false;
            this.isGuideline = false;
            this.isHelper = false;
            this.isInPlaceholder = false;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolvedHorizontalBias = 0.5f;
            this.widget = new ConstraintWidget();
            this.helped = false;
            obtainStyledAttributes = (Context)obtainStyledAttributes.obtainStyledAttributes(set, R.styleable.ConstraintLayout_Layout);
            for (int indexCount = ((TypedArray)obtainStyledAttributes).getIndexCount(), i = 0; i < indexCount; ++i) {
                final int index = ((TypedArray)obtainStyledAttributes).getIndex(i);
                final int value = Table.map.get(index);
                switch (value) {
                    default: {
                        switch (value) {
                            default: {
                                continue;
                            }
                            case 50: {
                                this.editorAbsoluteY = ((TypedArray)obtainStyledAttributes).getDimensionPixelOffset(index, this.editorAbsoluteY);
                                continue;
                            }
                            case 49: {
                                this.editorAbsoluteX = ((TypedArray)obtainStyledAttributes).getDimensionPixelOffset(index, this.editorAbsoluteX);
                                continue;
                            }
                            case 48: {
                                this.verticalChainStyle = ((TypedArray)obtainStyledAttributes).getInt(index, 0);
                                continue;
                            }
                            case 47: {
                                this.horizontalChainStyle = ((TypedArray)obtainStyledAttributes).getInt(index, 0);
                                continue;
                            }
                            case 46: {
                                this.verticalWeight = ((TypedArray)obtainStyledAttributes).getFloat(index, this.verticalWeight);
                                continue;
                            }
                            case 45: {
                                this.horizontalWeight = ((TypedArray)obtainStyledAttributes).getFloat(index, this.horizontalWeight);
                                continue;
                            }
                            case 44: {
                                final String string = ((TypedArray)obtainStyledAttributes).getString(index);
                                this.dimensionRatio = string;
                                this.dimensionRatioValue = Float.NaN;
                                this.dimensionRatioSide = -1;
                                if (string != null) {
                                    final int length = string.length();
                                    int index2 = this.dimensionRatio.indexOf(44);
                                    if (index2 > 0 && index2 < length - 1) {
                                        final String substring = this.dimensionRatio.substring(0, index2);
                                        if (substring.equalsIgnoreCase("W")) {
                                            this.dimensionRatioSide = 0;
                                        }
                                        else if (substring.equalsIgnoreCase("H")) {
                                            this.dimensionRatioSide = 1;
                                        }
                                        ++index2;
                                    }
                                    else {
                                        index2 = 0;
                                    }
                                    final int index3 = this.dimensionRatio.indexOf(58);
                                    if (index3 >= 0 && index3 < length - 1) {
                                        final String substring2 = this.dimensionRatio.substring(index2, index3);
                                        final String substring3 = this.dimensionRatio.substring(index3 + 1);
                                        if (substring2.length() > 0 && substring3.length() > 0) {
                                            try {
                                                final float float1 = Float.parseFloat(substring2);
                                                final float float2 = Float.parseFloat(substring3);
                                                if (float1 > 0.0f && float2 > 0.0f) {
                                                    if (this.dimensionRatioSide == 1) {
                                                        this.dimensionRatioValue = Math.abs(float2 / float1);
                                                    }
                                                    else {
                                                        this.dimensionRatioValue = Math.abs(float1 / float2);
                                                    }
                                                }
                                            }
                                            catch (NumberFormatException ex) {}
                                        }
                                    }
                                    else {
                                        final String substring4 = this.dimensionRatio.substring(index2);
                                        if (substring4.length() > 0) {
                                            try {
                                                this.dimensionRatioValue = Float.parseFloat(substring4);
                                            }
                                            catch (NumberFormatException ex2) {}
                                        }
                                    }
                                    continue;
                                }
                                continue;
                            }
                        }
                        break;
                    }
                    case 38: {
                        this.matchConstraintPercentHeight = Math.max(0.0f, ((TypedArray)obtainStyledAttributes).getFloat(index, this.matchConstraintPercentHeight));
                        break;
                    }
                    case 37: {
                        try {
                            this.matchConstraintMaxHeight = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.matchConstraintMaxHeight);
                        }
                        catch (Exception ex3) {
                            if (((TypedArray)obtainStyledAttributes).getInt(index, this.matchConstraintMaxHeight) == -2) {
                                this.matchConstraintMaxHeight = -2;
                            }
                        }
                        break;
                    }
                    case 36: {
                        try {
                            this.matchConstraintMinHeight = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.matchConstraintMinHeight);
                        }
                        catch (Exception ex4) {
                            if (((TypedArray)obtainStyledAttributes).getInt(index, this.matchConstraintMinHeight) == -2) {
                                this.matchConstraintMinHeight = -2;
                            }
                        }
                        break;
                    }
                    case 35: {
                        this.matchConstraintPercentWidth = Math.max(0.0f, ((TypedArray)obtainStyledAttributes).getFloat(index, this.matchConstraintPercentWidth));
                        break;
                    }
                    case 34: {
                        try {
                            this.matchConstraintMaxWidth = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.matchConstraintMaxWidth);
                        }
                        catch (Exception ex5) {
                            if (((TypedArray)obtainStyledAttributes).getInt(index, this.matchConstraintMaxWidth) == -2) {
                                this.matchConstraintMaxWidth = -2;
                            }
                        }
                        break;
                    }
                    case 33: {
                        try {
                            this.matchConstraintMinWidth = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.matchConstraintMinWidth);
                        }
                        catch (Exception ex6) {
                            if (((TypedArray)obtainStyledAttributes).getInt(index, this.matchConstraintMinWidth) == -2) {
                                this.matchConstraintMinWidth = -2;
                            }
                        }
                        break;
                    }
                    case 32: {
                        final int int1 = ((TypedArray)obtainStyledAttributes).getInt(index, 0);
                        this.matchConstraintDefaultHeight = int1;
                        if (int1 == 1) {
                            Log.e("ConstraintLayout", "layout_constraintHeight_default=\"wrap\" is deprecated.\nUse layout_height=\"WRAP_CONTENT\" and layout_constrainedHeight=\"true\" instead.");
                            break;
                        }
                        break;
                    }
                    case 31: {
                        final int int2 = ((TypedArray)obtainStyledAttributes).getInt(index, 0);
                        this.matchConstraintDefaultWidth = int2;
                        if (int2 == 1) {
                            Log.e("ConstraintLayout", "layout_constraintWidth_default=\"wrap\" is deprecated.\nUse layout_width=\"WRAP_CONTENT\" and layout_constrainedWidth=\"true\" instead.");
                            break;
                        }
                        break;
                    }
                    case 30: {
                        this.verticalBias = ((TypedArray)obtainStyledAttributes).getFloat(index, this.verticalBias);
                        break;
                    }
                    case 29: {
                        this.horizontalBias = ((TypedArray)obtainStyledAttributes).getFloat(index, this.horizontalBias);
                        break;
                    }
                    case 28: {
                        this.constrainedHeight = ((TypedArray)obtainStyledAttributes).getBoolean(index, this.constrainedHeight);
                        break;
                    }
                    case 27: {
                        this.constrainedWidth = ((TypedArray)obtainStyledAttributes).getBoolean(index, this.constrainedWidth);
                        break;
                    }
                    case 26: {
                        this.goneEndMargin = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.goneEndMargin);
                        break;
                    }
                    case 25: {
                        this.goneStartMargin = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.goneStartMargin);
                        break;
                    }
                    case 24: {
                        this.goneBottomMargin = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.goneBottomMargin);
                        break;
                    }
                    case 23: {
                        this.goneRightMargin = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.goneRightMargin);
                        break;
                    }
                    case 22: {
                        this.goneTopMargin = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.goneTopMargin);
                        break;
                    }
                    case 21: {
                        this.goneLeftMargin = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.goneLeftMargin);
                        break;
                    }
                    case 20: {
                        final int resourceId = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.endToEnd);
                        this.endToEnd = resourceId;
                        if (resourceId == -1) {
                            this.endToEnd = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                            break;
                        }
                        break;
                    }
                    case 19: {
                        final int resourceId2 = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.endToStart);
                        this.endToStart = resourceId2;
                        if (resourceId2 == -1) {
                            this.endToStart = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                            break;
                        }
                        break;
                    }
                    case 18: {
                        final int resourceId3 = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.startToStart);
                        this.startToStart = resourceId3;
                        if (resourceId3 == -1) {
                            this.startToStart = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                            break;
                        }
                        break;
                    }
                    case 17: {
                        final int resourceId4 = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.startToEnd);
                        this.startToEnd = resourceId4;
                        if (resourceId4 == -1) {
                            this.startToEnd = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                            break;
                        }
                        break;
                    }
                    case 16: {
                        final int resourceId5 = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.baselineToBaseline);
                        this.baselineToBaseline = resourceId5;
                        if (resourceId5 == -1) {
                            this.baselineToBaseline = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                            break;
                        }
                        break;
                    }
                    case 15: {
                        final int resourceId6 = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.bottomToBottom);
                        this.bottomToBottom = resourceId6;
                        if (resourceId6 == -1) {
                            this.bottomToBottom = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                            break;
                        }
                        break;
                    }
                    case 14: {
                        final int resourceId7 = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.bottomToTop);
                        this.bottomToTop = resourceId7;
                        if (resourceId7 == -1) {
                            this.bottomToTop = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                            break;
                        }
                        break;
                    }
                    case 13: {
                        final int resourceId8 = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.topToBottom);
                        this.topToBottom = resourceId8;
                        if (resourceId8 == -1) {
                            this.topToBottom = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                            break;
                        }
                        break;
                    }
                    case 12: {
                        final int resourceId9 = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.topToTop);
                        this.topToTop = resourceId9;
                        if (resourceId9 == -1) {
                            this.topToTop = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                            break;
                        }
                        break;
                    }
                    case 11: {
                        final int resourceId10 = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.rightToRight);
                        this.rightToRight = resourceId10;
                        if (resourceId10 == -1) {
                            this.rightToRight = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                            break;
                        }
                        break;
                    }
                    case 10: {
                        final int resourceId11 = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.rightToLeft);
                        this.rightToLeft = resourceId11;
                        if (resourceId11 == -1) {
                            this.rightToLeft = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                            break;
                        }
                        break;
                    }
                    case 9: {
                        final int resourceId12 = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.leftToRight);
                        this.leftToRight = resourceId12;
                        if (resourceId12 == -1) {
                            this.leftToRight = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                            break;
                        }
                        break;
                    }
                    case 8: {
                        final int resourceId13 = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.leftToLeft);
                        this.leftToLeft = resourceId13;
                        if (resourceId13 == -1) {
                            this.leftToLeft = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                            break;
                        }
                        break;
                    }
                    case 7: {
                        this.guidePercent = ((TypedArray)obtainStyledAttributes).getFloat(index, this.guidePercent);
                        break;
                    }
                    case 6: {
                        this.guideEnd = ((TypedArray)obtainStyledAttributes).getDimensionPixelOffset(index, this.guideEnd);
                        break;
                    }
                    case 5: {
                        this.guideBegin = ((TypedArray)obtainStyledAttributes).getDimensionPixelOffset(index, this.guideBegin);
                        break;
                    }
                    case 4: {
                        final float circleAngle = ((TypedArray)obtainStyledAttributes).getFloat(index, this.circleAngle) % 360.0f;
                        this.circleAngle = circleAngle;
                        if (circleAngle < 0.0f) {
                            this.circleAngle = (360.0f - circleAngle) % 360.0f;
                            break;
                        }
                        break;
                    }
                    case 3: {
                        this.circleRadius = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.circleRadius);
                        break;
                    }
                    case 2: {
                        final int resourceId14 = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.circleConstraint);
                        this.circleConstraint = resourceId14;
                        if (resourceId14 == -1) {
                            this.circleConstraint = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                            break;
                        }
                        break;
                    }
                    case 1: {
                        this.orientation = ((TypedArray)obtainStyledAttributes).getInt(index, this.orientation);
                        break;
                    }
                }
            }
            ((TypedArray)obtainStyledAttributes).recycle();
            this.validate();
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            this.guideBegin = -1;
            this.guideEnd = -1;
            this.guidePercent = -1.0f;
            this.leftToLeft = -1;
            this.leftToRight = -1;
            this.rightToLeft = -1;
            this.rightToRight = -1;
            this.topToTop = -1;
            this.topToBottom = -1;
            this.bottomToTop = -1;
            this.bottomToBottom = -1;
            this.baselineToBaseline = -1;
            this.circleConstraint = -1;
            this.circleRadius = 0;
            this.circleAngle = 0.0f;
            this.startToEnd = -1;
            this.startToStart = -1;
            this.endToStart = -1;
            this.endToEnd = -1;
            this.goneLeftMargin = -1;
            this.goneTopMargin = -1;
            this.goneRightMargin = -1;
            this.goneBottomMargin = -1;
            this.goneStartMargin = -1;
            this.goneEndMargin = -1;
            this.horizontalBias = 0.5f;
            this.verticalBias = 0.5f;
            this.dimensionRatio = null;
            this.dimensionRatioValue = 0.0f;
            this.dimensionRatioSide = 1;
            this.horizontalWeight = -1.0f;
            this.verticalWeight = -1.0f;
            this.horizontalChainStyle = 0;
            this.verticalChainStyle = 0;
            this.matchConstraintDefaultWidth = 0;
            this.matchConstraintDefaultHeight = 0;
            this.matchConstraintMinWidth = 0;
            this.matchConstraintMinHeight = 0;
            this.matchConstraintMaxWidth = 0;
            this.matchConstraintMaxHeight = 0;
            this.matchConstraintPercentWidth = 1.0f;
            this.matchConstraintPercentHeight = 1.0f;
            this.editorAbsoluteX = -1;
            this.editorAbsoluteY = -1;
            this.orientation = -1;
            this.constrainedWidth = false;
            this.constrainedHeight = false;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            this.needsBaseline = false;
            this.isGuideline = false;
            this.isHelper = false;
            this.isInPlaceholder = false;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolvedHorizontalBias = 0.5f;
            this.widget = new ConstraintWidget();
            this.helped = false;
        }
        
        public LayoutParams(final LayoutParams layoutParams) {
            super((ViewGroup$MarginLayoutParams)layoutParams);
            this.guideBegin = -1;
            this.guideEnd = -1;
            this.guidePercent = -1.0f;
            this.leftToLeft = -1;
            this.leftToRight = -1;
            this.rightToLeft = -1;
            this.rightToRight = -1;
            this.topToTop = -1;
            this.topToBottom = -1;
            this.bottomToTop = -1;
            this.bottomToBottom = -1;
            this.baselineToBaseline = -1;
            this.circleConstraint = -1;
            this.circleRadius = 0;
            this.circleAngle = 0.0f;
            this.startToEnd = -1;
            this.startToStart = -1;
            this.endToStart = -1;
            this.endToEnd = -1;
            this.goneLeftMargin = -1;
            this.goneTopMargin = -1;
            this.goneRightMargin = -1;
            this.goneBottomMargin = -1;
            this.goneStartMargin = -1;
            this.goneEndMargin = -1;
            this.horizontalBias = 0.5f;
            this.verticalBias = 0.5f;
            this.dimensionRatio = null;
            this.dimensionRatioValue = 0.0f;
            this.dimensionRatioSide = 1;
            this.horizontalWeight = -1.0f;
            this.verticalWeight = -1.0f;
            this.horizontalChainStyle = 0;
            this.verticalChainStyle = 0;
            this.matchConstraintDefaultWidth = 0;
            this.matchConstraintDefaultHeight = 0;
            this.matchConstraintMinWidth = 0;
            this.matchConstraintMinHeight = 0;
            this.matchConstraintMaxWidth = 0;
            this.matchConstraintMaxHeight = 0;
            this.matchConstraintPercentWidth = 1.0f;
            this.matchConstraintPercentHeight = 1.0f;
            this.editorAbsoluteX = -1;
            this.editorAbsoluteY = -1;
            this.orientation = -1;
            this.constrainedWidth = false;
            this.constrainedHeight = false;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            this.needsBaseline = false;
            this.isGuideline = false;
            this.isHelper = false;
            this.isInPlaceholder = false;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolvedHorizontalBias = 0.5f;
            this.widget = new ConstraintWidget();
            this.helped = false;
            this.guideBegin = layoutParams.guideBegin;
            this.guideEnd = layoutParams.guideEnd;
            this.guidePercent = layoutParams.guidePercent;
            this.leftToLeft = layoutParams.leftToLeft;
            this.leftToRight = layoutParams.leftToRight;
            this.rightToLeft = layoutParams.rightToLeft;
            this.rightToRight = layoutParams.rightToRight;
            this.topToTop = layoutParams.topToTop;
            this.topToBottom = layoutParams.topToBottom;
            this.bottomToTop = layoutParams.bottomToTop;
            this.bottomToBottom = layoutParams.bottomToBottom;
            this.baselineToBaseline = layoutParams.baselineToBaseline;
            this.circleConstraint = layoutParams.circleConstraint;
            this.circleRadius = layoutParams.circleRadius;
            this.circleAngle = layoutParams.circleAngle;
            this.startToEnd = layoutParams.startToEnd;
            this.startToStart = layoutParams.startToStart;
            this.endToStart = layoutParams.endToStart;
            this.endToEnd = layoutParams.endToEnd;
            this.goneLeftMargin = layoutParams.goneLeftMargin;
            this.goneTopMargin = layoutParams.goneTopMargin;
            this.goneRightMargin = layoutParams.goneRightMargin;
            this.goneBottomMargin = layoutParams.goneBottomMargin;
            this.goneStartMargin = layoutParams.goneStartMargin;
            this.goneEndMargin = layoutParams.goneEndMargin;
            this.horizontalBias = layoutParams.horizontalBias;
            this.verticalBias = layoutParams.verticalBias;
            this.dimensionRatio = layoutParams.dimensionRatio;
            this.dimensionRatioValue = layoutParams.dimensionRatioValue;
            this.dimensionRatioSide = layoutParams.dimensionRatioSide;
            this.horizontalWeight = layoutParams.horizontalWeight;
            this.verticalWeight = layoutParams.verticalWeight;
            this.horizontalChainStyle = layoutParams.horizontalChainStyle;
            this.verticalChainStyle = layoutParams.verticalChainStyle;
            this.constrainedWidth = layoutParams.constrainedWidth;
            this.constrainedHeight = layoutParams.constrainedHeight;
            this.matchConstraintDefaultWidth = layoutParams.matchConstraintDefaultWidth;
            this.matchConstraintDefaultHeight = layoutParams.matchConstraintDefaultHeight;
            this.matchConstraintMinWidth = layoutParams.matchConstraintMinWidth;
            this.matchConstraintMaxWidth = layoutParams.matchConstraintMaxWidth;
            this.matchConstraintMinHeight = layoutParams.matchConstraintMinHeight;
            this.matchConstraintMaxHeight = layoutParams.matchConstraintMaxHeight;
            this.matchConstraintPercentWidth = layoutParams.matchConstraintPercentWidth;
            this.matchConstraintPercentHeight = layoutParams.matchConstraintPercentHeight;
            this.editorAbsoluteX = layoutParams.editorAbsoluteX;
            this.editorAbsoluteY = layoutParams.editorAbsoluteY;
            this.orientation = layoutParams.orientation;
            this.horizontalDimensionFixed = layoutParams.horizontalDimensionFixed;
            this.verticalDimensionFixed = layoutParams.verticalDimensionFixed;
            this.needsBaseline = layoutParams.needsBaseline;
            this.isGuideline = layoutParams.isGuideline;
            this.resolvedLeftToLeft = layoutParams.resolvedLeftToLeft;
            this.resolvedLeftToRight = layoutParams.resolvedLeftToRight;
            this.resolvedRightToLeft = layoutParams.resolvedRightToLeft;
            this.resolvedRightToRight = layoutParams.resolvedRightToRight;
            this.resolveGoneLeftMargin = layoutParams.resolveGoneLeftMargin;
            this.resolveGoneRightMargin = layoutParams.resolveGoneRightMargin;
            this.resolvedHorizontalBias = layoutParams.resolvedHorizontalBias;
            this.widget = layoutParams.widget;
        }
        
        public void reset() {
            final ConstraintWidget widget = this.widget;
            if (widget != null) {
                widget.reset();
            }
        }
        
        public void resolveLayoutDirection(int n) {
            final int leftMargin = this.leftMargin;
            final int rightMargin = this.rightMargin;
            super.resolveLayoutDirection(n);
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolveGoneLeftMargin = this.goneLeftMargin;
            this.resolveGoneRightMargin = this.goneRightMargin;
            this.resolvedHorizontalBias = this.horizontalBias;
            this.resolvedGuideBegin = this.guideBegin;
            this.resolvedGuideEnd = this.guideEnd;
            this.resolvedGuidePercent = this.guidePercent;
            if (1 == this.getLayoutDirection()) {
                n = 1;
            }
            else {
                n = 0;
            }
            if (n != 0) {
                n = 0;
                final int startToEnd = this.startToEnd;
                if (startToEnd != -1) {
                    this.resolvedRightToLeft = startToEnd;
                    n = 1;
                }
                else {
                    final int startToStart = this.startToStart;
                    if (startToStart != -1) {
                        this.resolvedRightToRight = startToStart;
                        n = 1;
                    }
                }
                final int endToStart = this.endToStart;
                if (endToStart != -1) {
                    this.resolvedLeftToRight = endToStart;
                    n = 1;
                }
                final int endToEnd = this.endToEnd;
                if (endToEnd != -1) {
                    this.resolvedLeftToLeft = endToEnd;
                    n = 1;
                }
                final int goneStartMargin = this.goneStartMargin;
                if (goneStartMargin != -1) {
                    this.resolveGoneRightMargin = goneStartMargin;
                }
                final int goneEndMargin = this.goneEndMargin;
                if (goneEndMargin != -1) {
                    this.resolveGoneLeftMargin = goneEndMargin;
                }
                if (n != 0) {
                    this.resolvedHorizontalBias = 1.0f - this.horizontalBias;
                }
                if (this.isGuideline && this.orientation == 1) {
                    final float guidePercent = this.guidePercent;
                    if (guidePercent != -1.0f) {
                        this.resolvedGuidePercent = 1.0f - guidePercent;
                        this.resolvedGuideBegin = -1;
                        this.resolvedGuideEnd = -1;
                    }
                    else {
                        n = this.guideBegin;
                        if (n != -1) {
                            this.resolvedGuideEnd = n;
                            this.resolvedGuideBegin = -1;
                            this.resolvedGuidePercent = -1.0f;
                        }
                        else {
                            n = this.guideEnd;
                            if (n != -1) {
                                this.resolvedGuideBegin = n;
                                this.resolvedGuideEnd = -1;
                                this.resolvedGuidePercent = -1.0f;
                            }
                        }
                    }
                }
            }
            else {
                n = this.startToEnd;
                if (n != -1) {
                    this.resolvedLeftToRight = n;
                }
                n = this.startToStart;
                if (n != -1) {
                    this.resolvedLeftToLeft = n;
                }
                n = this.endToStart;
                if (n != -1) {
                    this.resolvedRightToLeft = n;
                }
                n = this.endToEnd;
                if (n != -1) {
                    this.resolvedRightToRight = n;
                }
                n = this.goneStartMargin;
                if (n != -1) {
                    this.resolveGoneLeftMargin = n;
                }
                n = this.goneEndMargin;
                if (n != -1) {
                    this.resolveGoneRightMargin = n;
                }
            }
            if (this.endToStart == -1 && this.endToEnd == -1 && this.startToStart == -1 && this.startToEnd == -1) {
                n = this.rightToLeft;
                if (n != -1) {
                    this.resolvedRightToLeft = n;
                    if (this.rightMargin <= 0 && rightMargin > 0) {
                        this.rightMargin = rightMargin;
                    }
                }
                else {
                    n = this.rightToRight;
                    if (n != -1) {
                        this.resolvedRightToRight = n;
                        if (this.rightMargin <= 0 && rightMargin > 0) {
                            this.rightMargin = rightMargin;
                        }
                    }
                }
                n = this.leftToLeft;
                if (n != -1) {
                    this.resolvedLeftToLeft = n;
                    if (this.leftMargin <= 0 && leftMargin > 0) {
                        this.leftMargin = leftMargin;
                    }
                }
                else {
                    n = this.leftToRight;
                    if (n != -1) {
                        this.resolvedLeftToRight = n;
                        if (this.leftMargin <= 0 && leftMargin > 0) {
                            this.leftMargin = leftMargin;
                        }
                    }
                }
            }
        }
        
        public void validate() {
            this.isGuideline = false;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            if (this.width == -2 && this.constrainedWidth) {
                this.horizontalDimensionFixed = false;
                this.matchConstraintDefaultWidth = 1;
            }
            if (this.height == -2 && this.constrainedHeight) {
                this.verticalDimensionFixed = false;
                this.matchConstraintDefaultHeight = 1;
            }
            if (this.width == 0 || this.width == -1) {
                this.horizontalDimensionFixed = false;
                if (this.width == 0 && this.matchConstraintDefaultWidth == 1) {
                    this.width = -2;
                    this.constrainedWidth = true;
                }
            }
            if (this.height == 0 || this.height == -1) {
                this.verticalDimensionFixed = false;
                if (this.height == 0 && this.matchConstraintDefaultHeight == 1) {
                    this.height = -2;
                    this.constrainedHeight = true;
                }
            }
            if (this.guidePercent != -1.0f || this.guideBegin != -1 || this.guideEnd != -1) {
                this.isGuideline = true;
                this.horizontalDimensionFixed = true;
                this.verticalDimensionFixed = true;
                if (!(this.widget instanceof Guideline)) {
                    this.widget = new Guideline();
                }
                ((Guideline)this.widget).setOrientation(this.orientation);
            }
        }
        
        private static class Table
        {
            public static final int ANDROID_ORIENTATION = 1;
            public static final int LAYOUT_CONSTRAINED_HEIGHT = 28;
            public static final int LAYOUT_CONSTRAINED_WIDTH = 27;
            public static final int LAYOUT_CONSTRAINT_BASELINE_CREATOR = 43;
            public static final int LAYOUT_CONSTRAINT_BASELINE_TO_BASELINE_OF = 16;
            public static final int LAYOUT_CONSTRAINT_BOTTOM_CREATOR = 42;
            public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_BOTTOM_OF = 15;
            public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_TOP_OF = 14;
            public static final int LAYOUT_CONSTRAINT_CIRCLE = 2;
            public static final int LAYOUT_CONSTRAINT_CIRCLE_ANGLE = 4;
            public static final int LAYOUT_CONSTRAINT_CIRCLE_RADIUS = 3;
            public static final int LAYOUT_CONSTRAINT_DIMENSION_RATIO = 44;
            public static final int LAYOUT_CONSTRAINT_END_TO_END_OF = 20;
            public static final int LAYOUT_CONSTRAINT_END_TO_START_OF = 19;
            public static final int LAYOUT_CONSTRAINT_GUIDE_BEGIN = 5;
            public static final int LAYOUT_CONSTRAINT_GUIDE_END = 6;
            public static final int LAYOUT_CONSTRAINT_GUIDE_PERCENT = 7;
            public static final int LAYOUT_CONSTRAINT_HEIGHT_DEFAULT = 32;
            public static final int LAYOUT_CONSTRAINT_HEIGHT_MAX = 37;
            public static final int LAYOUT_CONSTRAINT_HEIGHT_MIN = 36;
            public static final int LAYOUT_CONSTRAINT_HEIGHT_PERCENT = 38;
            public static final int LAYOUT_CONSTRAINT_HORIZONTAL_BIAS = 29;
            public static final int LAYOUT_CONSTRAINT_HORIZONTAL_CHAINSTYLE = 47;
            public static final int LAYOUT_CONSTRAINT_HORIZONTAL_WEIGHT = 45;
            public static final int LAYOUT_CONSTRAINT_LEFT_CREATOR = 39;
            public static final int LAYOUT_CONSTRAINT_LEFT_TO_LEFT_OF = 8;
            public static final int LAYOUT_CONSTRAINT_LEFT_TO_RIGHT_OF = 9;
            public static final int LAYOUT_CONSTRAINT_RIGHT_CREATOR = 41;
            public static final int LAYOUT_CONSTRAINT_RIGHT_TO_LEFT_OF = 10;
            public static final int LAYOUT_CONSTRAINT_RIGHT_TO_RIGHT_OF = 11;
            public static final int LAYOUT_CONSTRAINT_START_TO_END_OF = 17;
            public static final int LAYOUT_CONSTRAINT_START_TO_START_OF = 18;
            public static final int LAYOUT_CONSTRAINT_TOP_CREATOR = 40;
            public static final int LAYOUT_CONSTRAINT_TOP_TO_BOTTOM_OF = 13;
            public static final int LAYOUT_CONSTRAINT_TOP_TO_TOP_OF = 12;
            public static final int LAYOUT_CONSTRAINT_VERTICAL_BIAS = 30;
            public static final int LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE = 48;
            public static final int LAYOUT_CONSTRAINT_VERTICAL_WEIGHT = 46;
            public static final int LAYOUT_CONSTRAINT_WIDTH_DEFAULT = 31;
            public static final int LAYOUT_CONSTRAINT_WIDTH_MAX = 34;
            public static final int LAYOUT_CONSTRAINT_WIDTH_MIN = 33;
            public static final int LAYOUT_CONSTRAINT_WIDTH_PERCENT = 35;
            public static final int LAYOUT_EDITOR_ABSOLUTEX = 49;
            public static final int LAYOUT_EDITOR_ABSOLUTEY = 50;
            public static final int LAYOUT_GONE_MARGIN_BOTTOM = 24;
            public static final int LAYOUT_GONE_MARGIN_END = 26;
            public static final int LAYOUT_GONE_MARGIN_LEFT = 21;
            public static final int LAYOUT_GONE_MARGIN_RIGHT = 23;
            public static final int LAYOUT_GONE_MARGIN_START = 25;
            public static final int LAYOUT_GONE_MARGIN_TOP = 22;
            public static final int UNUSED = 0;
            public static final SparseIntArray map;
            
            static {
                (map = new SparseIntArray()).append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toLeftOf, 8);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toRightOf, 9);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toLeftOf, 10);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toRightOf, 11);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toTopOf, 12);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toBottomOf, 13);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toTopOf, 14);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toBottomOf, 15);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBaselineOf, 16);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircle, 2);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleRadius, 3);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleAngle, 4);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteX, 49);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteY, 50);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_begin, 5);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_end, 6);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_percent, 7);
                Table.map.append(R.styleable.ConstraintLayout_Layout_android_orientation, 1);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toEndOf, 17);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toStartOf, 18);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toStartOf, 19);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toEndOf, 20);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginLeft, 21);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginTop, 22);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginRight, 23);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginBottom, 24);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginStart, 25);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginEnd, 26);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_bias, 29);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_bias, 30);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintDimensionRatio, 44);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_weight, 45);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_weight, 46);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_chainStyle, 47);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_chainStyle, 48);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedWidth, 27);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedHeight, 28);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_default, 31);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_default, 32);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_min, 33);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_max, 34);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_percent, 35);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_min, 36);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_max, 37);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_percent, 38);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_creator, 39);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_creator, 40);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_creator, 41);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_creator, 42);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_creator, 43);
            }
        }
    }
}
