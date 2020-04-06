// 
// Decompiled by Procyon v0.5.36
// 

package androidx.constraintlayout.widget;

import android.content.res.XmlResourceParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import android.util.Xml;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import java.util.Iterator;
import android.os.Build$VERSION;
import android.view.ViewGroup$LayoutParams;
import java.util.Collection;
import java.util.HashSet;
import android.util.Log;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.content.Context;
import java.util.Arrays;
import android.view.View;
import java.util.HashMap;
import android.util.SparseIntArray;

public class ConstraintSet
{
    private static final int ALPHA = 43;
    private static final int BARRIER_ALLOWS_GONE_WIDGETS = 74;
    private static final int BARRIER_DIRECTION = 72;
    private static final int BARRIER_TYPE = 1;
    public static final int BASELINE = 5;
    private static final int BASELINE_TO_BASELINE = 1;
    public static final int BOTTOM = 4;
    private static final int BOTTOM_MARGIN = 2;
    private static final int BOTTOM_TO_BOTTOM = 3;
    private static final int BOTTOM_TO_TOP = 4;
    public static final int CHAIN_PACKED = 2;
    public static final int CHAIN_SPREAD = 0;
    public static final int CHAIN_SPREAD_INSIDE = 1;
    private static final int CHAIN_USE_RTL = 71;
    private static final int CIRCLE = 61;
    private static final int CIRCLE_ANGLE = 63;
    private static final int CIRCLE_RADIUS = 62;
    private static final int CONSTRAINT_REFERENCED_IDS = 73;
    private static final boolean DEBUG = false;
    private static final int DIMENSION_RATIO = 5;
    private static final int EDITOR_ABSOLUTE_X = 6;
    private static final int EDITOR_ABSOLUTE_Y = 7;
    private static final int ELEVATION = 44;
    public static final int END = 7;
    private static final int END_MARGIN = 8;
    private static final int END_TO_END = 9;
    private static final int END_TO_START = 10;
    public static final int GONE = 8;
    private static final int GONE_BOTTOM_MARGIN = 11;
    private static final int GONE_END_MARGIN = 12;
    private static final int GONE_LEFT_MARGIN = 13;
    private static final int GONE_RIGHT_MARGIN = 14;
    private static final int GONE_START_MARGIN = 15;
    private static final int GONE_TOP_MARGIN = 16;
    private static final int GUIDE_BEGIN = 17;
    private static final int GUIDE_END = 18;
    private static final int GUIDE_PERCENT = 19;
    private static final int HEIGHT_DEFAULT = 55;
    private static final int HEIGHT_MAX = 57;
    private static final int HEIGHT_MIN = 59;
    private static final int HEIGHT_PERCENT = 70;
    public static final int HORIZONTAL = 0;
    private static final int HORIZONTAL_BIAS = 20;
    public static final int HORIZONTAL_GUIDELINE = 0;
    private static final int HORIZONTAL_STYLE = 41;
    private static final int HORIZONTAL_WEIGHT = 39;
    public static final int INVISIBLE = 4;
    private static final int LAYOUT_HEIGHT = 21;
    private static final int LAYOUT_VISIBILITY = 22;
    private static final int LAYOUT_WIDTH = 23;
    public static final int LEFT = 1;
    private static final int LEFT_MARGIN = 24;
    private static final int LEFT_TO_LEFT = 25;
    private static final int LEFT_TO_RIGHT = 26;
    public static final int MATCH_CONSTRAINT = 0;
    public static final int MATCH_CONSTRAINT_SPREAD = 0;
    public static final int MATCH_CONSTRAINT_WRAP = 1;
    private static final int ORIENTATION = 27;
    public static final int PARENT_ID = 0;
    public static final int RIGHT = 2;
    private static final int RIGHT_MARGIN = 28;
    private static final int RIGHT_TO_LEFT = 29;
    private static final int RIGHT_TO_RIGHT = 30;
    private static final int ROTATION = 60;
    private static final int ROTATION_X = 45;
    private static final int ROTATION_Y = 46;
    private static final int SCALE_X = 47;
    private static final int SCALE_Y = 48;
    public static final int START = 6;
    private static final int START_MARGIN = 31;
    private static final int START_TO_END = 32;
    private static final int START_TO_START = 33;
    private static final String TAG = "ConstraintSet";
    public static final int TOP = 3;
    private static final int TOP_MARGIN = 34;
    private static final int TOP_TO_BOTTOM = 35;
    private static final int TOP_TO_TOP = 36;
    private static final int TRANSFORM_PIVOT_X = 49;
    private static final int TRANSFORM_PIVOT_Y = 50;
    private static final int TRANSLATION_X = 51;
    private static final int TRANSLATION_Y = 52;
    private static final int TRANSLATION_Z = 53;
    public static final int UNSET = -1;
    private static final int UNUSED = 75;
    public static final int VERTICAL = 1;
    private static final int VERTICAL_BIAS = 37;
    public static final int VERTICAL_GUIDELINE = 1;
    private static final int VERTICAL_STYLE = 42;
    private static final int VERTICAL_WEIGHT = 40;
    private static final int VIEW_ID = 38;
    private static final int[] VISIBILITY_FLAGS;
    public static final int VISIBLE = 0;
    private static final int WIDTH_DEFAULT = 54;
    private static final int WIDTH_MAX = 56;
    private static final int WIDTH_MIN = 58;
    private static final int WIDTH_PERCENT = 69;
    public static final int WRAP_CONTENT = -2;
    private static SparseIntArray mapToConstant;
    private HashMap<Integer, Constraint> mConstraints;
    
    static {
        VISIBILITY_FLAGS = new int[] { 0, 4, 8 };
        (ConstraintSet.mapToConstant = new SparseIntArray()).append(R.styleable.ConstraintSet_layout_constraintLeft_toLeftOf, 25);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintLeft_toRightOf, 26);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintRight_toLeftOf, 29);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintRight_toRightOf, 30);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintTop_toTopOf, 36);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintTop_toBottomOf, 35);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintBottom_toTopOf, 4);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintBottom_toBottomOf, 3);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintBaseline_toBaselineOf, 1);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_editor_absoluteX, 6);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_editor_absoluteY, 7);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintGuide_begin, 17);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintGuide_end, 18);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintGuide_percent, 19);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_orientation, 27);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintStart_toEndOf, 32);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintStart_toStartOf, 33);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintEnd_toStartOf, 10);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintEnd_toEndOf, 9);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_goneMarginLeft, 13);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_goneMarginTop, 16);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_goneMarginRight, 14);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_goneMarginBottom, 11);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_goneMarginStart, 15);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_goneMarginEnd, 12);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintVertical_weight, 40);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintHorizontal_weight, 39);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintHorizontal_chainStyle, 41);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintVertical_chainStyle, 42);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintHorizontal_bias, 20);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintVertical_bias, 37);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintDimensionRatio, 5);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintLeft_creator, 75);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintTop_creator, 75);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintRight_creator, 75);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintBottom_creator, 75);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintBaseline_creator, 75);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_layout_marginLeft, 24);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_layout_marginRight, 28);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_layout_marginStart, 31);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_layout_marginEnd, 8);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_layout_marginTop, 34);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_layout_marginBottom, 2);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_layout_width, 23);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_layout_height, 21);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_visibility, 22);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_alpha, 43);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_elevation, 44);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_rotationX, 45);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_rotationY, 46);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_rotation, 60);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_scaleX, 47);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_scaleY, 48);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_transformPivotX, 49);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_transformPivotY, 50);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_translationX, 51);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_translationY, 52);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_translationZ, 53);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintWidth_default, 54);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintHeight_default, 55);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintWidth_max, 56);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintHeight_max, 57);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintWidth_min, 58);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintHeight_min, 59);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintCircle, 61);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintCircleRadius, 62);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintCircleAngle, 63);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_id, 38);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintWidth_percent, 69);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintHeight_percent, 70);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_chainUseRtl, 71);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_barrierDirection, 72);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_constraint_referenced_ids, 73);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_barrierAllowsGoneWidgets, 74);
    }
    
    public ConstraintSet() {
        this.mConstraints = new HashMap<Integer, Constraint>();
    }
    
    private int[] convertReferenceString(final View view, String original) {
        final String[] split = original.split(",");
        final Context context = view.getContext();
        original = (String)(Object)new int[split.length];
        int newLength = 0;
        for (int i = 0; i < split.length; ++i, ++newLength) {
            final String trim = split[i].trim();
            final int n = 0;
            int int1;
            try {
                int1 = R.id.class.getField(trim).getInt(null);
            }
            catch (Exception ex) {
                int1 = n;
            }
            int identifier = int1;
            if (int1 == 0) {
                identifier = context.getResources().getIdentifier(trim, "id", context.getPackageName());
            }
            int intValue;
            if ((intValue = identifier) == 0) {
                intValue = identifier;
                if (view.isInEditMode()) {
                    intValue = identifier;
                    if (view.getParent() instanceof ConstraintLayout) {
                        final Object designInformation = ((ConstraintLayout)view.getParent()).getDesignInformation(0, trim);
                        intValue = identifier;
                        if (designInformation != null) {
                            intValue = identifier;
                            if (designInformation instanceof Integer) {
                                intValue = (int)designInformation;
                            }
                        }
                    }
                }
            }
            original[newLength] = intValue;
        }
        Object copy = original;
        if (newLength != split.length) {
            copy = Arrays.copyOf((int[])(Object)original, newLength);
        }
        return (int[])copy;
    }
    
    private void createHorizontalChain(int i, int n, final int n2, final int n3, final int[] array, final float[] array2, final int horizontalChainStyle, final int n4, final int n5) {
        if (array.length < 2) {
            throw new IllegalArgumentException("must have 2 or more widgets in a chain");
        }
        if (array2 != null && array2.length != array.length) {
            throw new IllegalArgumentException("must have 2 or more widgets in a chain");
        }
        if (array2 != null) {
            this.get(array[0]).horizontalWeight = array2[0];
        }
        this.get(array[0]).horizontalChainStyle = horizontalChainStyle;
        this.connect(array[0], n4, i, n, -1);
        for (i = 1; i < array.length; ++i) {
            n = array[i];
            this.connect(array[i], n4, array[i - 1], n5, -1);
            this.connect(array[i - 1], n5, array[i], n4, -1);
            if (array2 != null) {
                this.get(array[i]).horizontalWeight = array2[i];
            }
        }
        this.connect(array[array.length - 1], n5, n2, n3, -1);
    }
    
    private Constraint fillFromAttributeList(final Context context, final AttributeSet set) {
        final Constraint constraint = new Constraint();
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.ConstraintSet);
        this.populateConstraint(constraint, obtainStyledAttributes);
        obtainStyledAttributes.recycle();
        return constraint;
    }
    
    private Constraint get(final int i) {
        if (!this.mConstraints.containsKey(i)) {
            this.mConstraints.put(i, new Constraint());
        }
        return this.mConstraints.get(i);
    }
    
    private static int lookupID(final TypedArray typedArray, final int n, int n2) {
        if ((n2 = typedArray.getResourceId(n, n2)) == -1) {
            n2 = typedArray.getInt(n, -1);
        }
        return n2;
    }
    
    private void populateConstraint(final Constraint constraint, final TypedArray typedArray) {
        for (int indexCount = typedArray.getIndexCount(), i = 0; i < indexCount; ++i) {
            final int index = typedArray.getIndex(i);
            final int value = ConstraintSet.mapToConstant.get(index);
            switch (value) {
                default: {
                    switch (value) {
                        default: {
                            switch (value) {
                                default: {
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("Unknown attribute 0x");
                                    sb.append(Integer.toHexString(index));
                                    sb.append("   ");
                                    sb.append(ConstraintSet.mapToConstant.get(index));
                                    Log.w("ConstraintSet", sb.toString());
                                    continue;
                                }
                                case 75: {
                                    final StringBuilder sb2 = new StringBuilder();
                                    sb2.append("unused attribute 0x");
                                    sb2.append(Integer.toHexString(index));
                                    sb2.append("   ");
                                    sb2.append(ConstraintSet.mapToConstant.get(index));
                                    Log.w("ConstraintSet", sb2.toString());
                                    continue;
                                }
                                case 74: {
                                    constraint.mBarrierAllowsGoneWidgets = typedArray.getBoolean(index, constraint.mBarrierAllowsGoneWidgets);
                                    continue;
                                }
                                case 73: {
                                    constraint.mReferenceIdString = typedArray.getString(index);
                                    continue;
                                }
                                case 72: {
                                    constraint.mBarrierDirection = typedArray.getInt(index, constraint.mBarrierDirection);
                                    continue;
                                }
                                case 71: {
                                    Log.e("ConstraintSet", "CURRENTLY UNSUPPORTED");
                                    continue;
                                }
                                case 70: {
                                    constraint.heightPercent = typedArray.getFloat(index, 1.0f);
                                    continue;
                                }
                                case 69: {
                                    constraint.widthPercent = typedArray.getFloat(index, 1.0f);
                                    continue;
                                }
                            }
                            break;
                        }
                        case 63: {
                            constraint.circleAngle = typedArray.getFloat(index, constraint.circleAngle);
                            continue;
                        }
                        case 62: {
                            constraint.circleRadius = typedArray.getDimensionPixelSize(index, constraint.circleRadius);
                            continue;
                        }
                        case 61: {
                            constraint.circleConstraint = lookupID(typedArray, index, constraint.circleConstraint);
                            continue;
                        }
                        case 60: {
                            constraint.rotation = typedArray.getFloat(index, constraint.rotation);
                            continue;
                        }
                    }
                    break;
                }
                case 53: {
                    constraint.translationZ = typedArray.getDimension(index, constraint.translationZ);
                    break;
                }
                case 52: {
                    constraint.translationY = typedArray.getDimension(index, constraint.translationY);
                    break;
                }
                case 51: {
                    constraint.translationX = typedArray.getDimension(index, constraint.translationX);
                    break;
                }
                case 50: {
                    constraint.transformPivotY = typedArray.getFloat(index, constraint.transformPivotY);
                    break;
                }
                case 49: {
                    constraint.transformPivotX = typedArray.getFloat(index, constraint.transformPivotX);
                    break;
                }
                case 48: {
                    constraint.scaleY = typedArray.getFloat(index, constraint.scaleY);
                    break;
                }
                case 47: {
                    constraint.scaleX = typedArray.getFloat(index, constraint.scaleX);
                    break;
                }
                case 46: {
                    constraint.rotationY = typedArray.getFloat(index, constraint.rotationY);
                    break;
                }
                case 45: {
                    constraint.rotationX = typedArray.getFloat(index, constraint.rotationX);
                    break;
                }
                case 44: {
                    constraint.applyElevation = true;
                    constraint.elevation = typedArray.getDimension(index, constraint.elevation);
                    break;
                }
                case 43: {
                    constraint.alpha = typedArray.getFloat(index, constraint.alpha);
                    break;
                }
                case 42: {
                    constraint.verticalChainStyle = typedArray.getInt(index, constraint.verticalChainStyle);
                    break;
                }
                case 41: {
                    constraint.horizontalChainStyle = typedArray.getInt(index, constraint.horizontalChainStyle);
                    break;
                }
                case 40: {
                    constraint.verticalWeight = typedArray.getFloat(index, constraint.verticalWeight);
                    break;
                }
                case 39: {
                    constraint.horizontalWeight = typedArray.getFloat(index, constraint.horizontalWeight);
                    break;
                }
                case 38: {
                    constraint.mViewId = typedArray.getResourceId(index, constraint.mViewId);
                    break;
                }
                case 37: {
                    constraint.verticalBias = typedArray.getFloat(index, constraint.verticalBias);
                    break;
                }
                case 36: {
                    constraint.topToTop = lookupID(typedArray, index, constraint.topToTop);
                    break;
                }
                case 35: {
                    constraint.topToBottom = lookupID(typedArray, index, constraint.topToBottom);
                    break;
                }
                case 34: {
                    constraint.topMargin = typedArray.getDimensionPixelSize(index, constraint.topMargin);
                    break;
                }
                case 33: {
                    constraint.startToStart = lookupID(typedArray, index, constraint.startToStart);
                    break;
                }
                case 32: {
                    constraint.startToEnd = lookupID(typedArray, index, constraint.startToEnd);
                    break;
                }
                case 31: {
                    constraint.startMargin = typedArray.getDimensionPixelSize(index, constraint.startMargin);
                    break;
                }
                case 30: {
                    constraint.rightToRight = lookupID(typedArray, index, constraint.rightToRight);
                    break;
                }
                case 29: {
                    constraint.rightToLeft = lookupID(typedArray, index, constraint.rightToLeft);
                    break;
                }
                case 28: {
                    constraint.rightMargin = typedArray.getDimensionPixelSize(index, constraint.rightMargin);
                    break;
                }
                case 27: {
                    constraint.orientation = typedArray.getInt(index, constraint.orientation);
                    break;
                }
                case 26: {
                    constraint.leftToRight = lookupID(typedArray, index, constraint.leftToRight);
                    break;
                }
                case 25: {
                    constraint.leftToLeft = lookupID(typedArray, index, constraint.leftToLeft);
                    break;
                }
                case 24: {
                    constraint.leftMargin = typedArray.getDimensionPixelSize(index, constraint.leftMargin);
                    break;
                }
                case 23: {
                    constraint.mWidth = typedArray.getLayoutDimension(index, constraint.mWidth);
                    break;
                }
                case 22: {
                    constraint.visibility = typedArray.getInt(index, constraint.visibility);
                    constraint.visibility = ConstraintSet.VISIBILITY_FLAGS[constraint.visibility];
                    break;
                }
                case 21: {
                    constraint.mHeight = typedArray.getLayoutDimension(index, constraint.mHeight);
                    break;
                }
                case 20: {
                    constraint.horizontalBias = typedArray.getFloat(index, constraint.horizontalBias);
                    break;
                }
                case 19: {
                    constraint.guidePercent = typedArray.getFloat(index, constraint.guidePercent);
                    break;
                }
                case 18: {
                    constraint.guideEnd = typedArray.getDimensionPixelOffset(index, constraint.guideEnd);
                    break;
                }
                case 17: {
                    constraint.guideBegin = typedArray.getDimensionPixelOffset(index, constraint.guideBegin);
                    break;
                }
                case 16: {
                    constraint.goneTopMargin = typedArray.getDimensionPixelSize(index, constraint.goneTopMargin);
                    break;
                }
                case 15: {
                    constraint.goneStartMargin = typedArray.getDimensionPixelSize(index, constraint.goneStartMargin);
                    break;
                }
                case 14: {
                    constraint.goneRightMargin = typedArray.getDimensionPixelSize(index, constraint.goneRightMargin);
                    break;
                }
                case 13: {
                    constraint.goneLeftMargin = typedArray.getDimensionPixelSize(index, constraint.goneLeftMargin);
                    break;
                }
                case 12: {
                    constraint.goneEndMargin = typedArray.getDimensionPixelSize(index, constraint.goneEndMargin);
                    break;
                }
                case 11: {
                    constraint.goneBottomMargin = typedArray.getDimensionPixelSize(index, constraint.goneBottomMargin);
                    break;
                }
                case 10: {
                    constraint.endToStart = lookupID(typedArray, index, constraint.endToStart);
                    break;
                }
                case 9: {
                    constraint.endToEnd = lookupID(typedArray, index, constraint.endToEnd);
                    break;
                }
                case 8: {
                    constraint.endMargin = typedArray.getDimensionPixelSize(index, constraint.endMargin);
                    break;
                }
                case 7: {
                    constraint.editorAbsoluteY = typedArray.getDimensionPixelOffset(index, constraint.editorAbsoluteY);
                    break;
                }
                case 6: {
                    constraint.editorAbsoluteX = typedArray.getDimensionPixelOffset(index, constraint.editorAbsoluteX);
                    break;
                }
                case 5: {
                    constraint.dimensionRatio = typedArray.getString(index);
                    break;
                }
                case 4: {
                    constraint.bottomToTop = lookupID(typedArray, index, constraint.bottomToTop);
                    break;
                }
                case 3: {
                    constraint.bottomToBottom = lookupID(typedArray, index, constraint.bottomToBottom);
                    break;
                }
                case 2: {
                    constraint.bottomMargin = typedArray.getDimensionPixelSize(index, constraint.bottomMargin);
                    break;
                }
                case 1: {
                    constraint.baselineToBaseline = lookupID(typedArray, index, constraint.baselineToBaseline);
                    break;
                }
            }
        }
    }
    
    private String sideToString(final int n) {
        switch (n) {
            default: {
                return "undefined";
            }
            case 7: {
                return "end";
            }
            case 6: {
                return "start";
            }
            case 5: {
                return "baseline";
            }
            case 4: {
                return "bottom";
            }
            case 3: {
                return "top";
            }
            case 2: {
                return "right";
            }
            case 1: {
                return "left";
            }
        }
    }
    
    public void addToHorizontalChain(final int n, final int n2, final int n3) {
        int n4;
        if (n2 == 0) {
            n4 = 1;
        }
        else {
            n4 = 2;
        }
        this.connect(n, 1, n2, n4, 0);
        int n5;
        if (n3 == 0) {
            n5 = 2;
        }
        else {
            n5 = 1;
        }
        this.connect(n, 2, n3, n5, 0);
        if (n2 != 0) {
            this.connect(n2, 2, n, 1, 0);
        }
        if (n3 != 0) {
            this.connect(n3, 1, n, 2, 0);
        }
    }
    
    public void addToHorizontalChainRTL(final int n, final int n2, final int n3) {
        int n4;
        if (n2 == 0) {
            n4 = 6;
        }
        else {
            n4 = 7;
        }
        this.connect(n, 6, n2, n4, 0);
        int n5;
        if (n3 == 0) {
            n5 = 7;
        }
        else {
            n5 = 6;
        }
        this.connect(n, 7, n3, n5, 0);
        if (n2 != 0) {
            this.connect(n2, 7, n, 6, 0);
        }
        if (n3 != 0) {
            this.connect(n3, 6, n, 7, 0);
        }
    }
    
    public void addToVerticalChain(final int n, final int n2, final int n3) {
        int n4;
        if (n2 == 0) {
            n4 = 3;
        }
        else {
            n4 = 4;
        }
        this.connect(n, 3, n2, n4, 0);
        int n5;
        if (n3 == 0) {
            n5 = 4;
        }
        else {
            n5 = 3;
        }
        this.connect(n, 4, n3, n5, 0);
        if (n2 != 0) {
            this.connect(n2, 4, n, 3, 0);
        }
        if (n2 != 0) {
            this.connect(n3, 3, n, 4, 0);
        }
    }
    
    public void applyTo(final ConstraintLayout constraintLayout) {
        this.applyToInternal(constraintLayout);
        constraintLayout.setConstraintSet(null);
    }
    
    void applyToInternal(final ConstraintLayout constraintLayout) {
        final int childCount = constraintLayout.getChildCount();
        final HashSet<Integer> set = new HashSet<Integer>(this.mConstraints.keySet());
        for (int i = 0; i < childCount; ++i) {
            final View child = constraintLayout.getChildAt(i);
            final int id = child.getId();
            if (id == -1) {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            }
            if (this.mConstraints.containsKey(id)) {
                set.remove(id);
                final Constraint constraint = this.mConstraints.get(id);
                if (child instanceof Barrier) {
                    constraint.mHelperType = 1;
                }
                if (constraint.mHelperType != -1) {
                    if (constraint.mHelperType == 1) {
                        final Barrier barrier = (Barrier)child;
                        barrier.setId(id);
                        barrier.setType(constraint.mBarrierDirection);
                        barrier.setAllowsGoneWidget(constraint.mBarrierAllowsGoneWidgets);
                        if (constraint.mReferenceIds != null) {
                            barrier.setReferencedIds(constraint.mReferenceIds);
                        }
                        else if (constraint.mReferenceIdString != null) {
                            barrier.setReferencedIds(constraint.mReferenceIds = this.convertReferenceString(barrier, constraint.mReferenceIdString));
                        }
                    }
                }
                final ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)child.getLayoutParams();
                constraint.applyTo(layoutParams);
                child.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                child.setVisibility(constraint.visibility);
                if (Build$VERSION.SDK_INT >= 17) {
                    child.setAlpha(constraint.alpha);
                    child.setRotation(constraint.rotation);
                    child.setRotationX(constraint.rotationX);
                    child.setRotationY(constraint.rotationY);
                    child.setScaleX(constraint.scaleX);
                    child.setScaleY(constraint.scaleY);
                    if (!Float.isNaN(constraint.transformPivotX)) {
                        child.setPivotX(constraint.transformPivotX);
                    }
                    if (!Float.isNaN(constraint.transformPivotY)) {
                        child.setPivotY(constraint.transformPivotY);
                    }
                    child.setTranslationX(constraint.translationX);
                    child.setTranslationY(constraint.translationY);
                    if (Build$VERSION.SDK_INT >= 21) {
                        child.setTranslationZ(constraint.translationZ);
                        if (constraint.applyElevation) {
                            child.setElevation(constraint.elevation);
                        }
                    }
                }
            }
        }
        for (final Integer key : set) {
            final Constraint constraint2 = this.mConstraints.get(key);
            if (constraint2.mHelperType != -1) {
                if (constraint2.mHelperType == 1) {
                    final Barrier barrier2 = new Barrier(constraintLayout.getContext());
                    barrier2.setId((int)key);
                    if (constraint2.mReferenceIds != null) {
                        barrier2.setReferencedIds(constraint2.mReferenceIds);
                    }
                    else if (constraint2.mReferenceIdString != null) {
                        barrier2.setReferencedIds(constraint2.mReferenceIds = this.convertReferenceString(barrier2, constraint2.mReferenceIdString));
                    }
                    barrier2.setType(constraint2.mBarrierDirection);
                    final ConstraintLayout.LayoutParams generateDefaultLayoutParams = constraintLayout.generateDefaultLayoutParams();
                    barrier2.validateParams();
                    constraint2.applyTo(generateDefaultLayoutParams);
                    constraintLayout.addView((View)barrier2, (ViewGroup$LayoutParams)generateDefaultLayoutParams);
                }
            }
            if (constraint2.mIsGuideline) {
                final Guideline guideline = new Guideline(constraintLayout.getContext());
                guideline.setId((int)key);
                final ConstraintLayout.LayoutParams generateDefaultLayoutParams2 = constraintLayout.generateDefaultLayoutParams();
                constraint2.applyTo(generateDefaultLayoutParams2);
                constraintLayout.addView((View)guideline, (ViewGroup$LayoutParams)generateDefaultLayoutParams2);
            }
        }
    }
    
    public void center(final int i, final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final float horizontalBias) {
        if (n3 < 0) {
            throw new IllegalArgumentException("margin must be > 0");
        }
        if (n6 < 0) {
            throw new IllegalArgumentException("margin must be > 0");
        }
        if (horizontalBias > 0.0f && horizontalBias <= 1.0f) {
            if (n2 != 1 && n2 != 2) {
                if (n2 != 6 && n2 != 7) {
                    this.connect(i, 3, n, n2, n3);
                    this.connect(i, 4, n4, n5, n6);
                    this.mConstraints.get(i).verticalBias = horizontalBias;
                }
                else {
                    this.connect(i, 6, n, n2, n3);
                    this.connect(i, 7, n4, n5, n6);
                    this.mConstraints.get(i).horizontalBias = horizontalBias;
                }
            }
            else {
                this.connect(i, 1, n, n2, n3);
                this.connect(i, 2, n4, n5, n6);
                this.mConstraints.get(i).horizontalBias = horizontalBias;
            }
            return;
        }
        throw new IllegalArgumentException("bias must be between 0 and 1 inclusive");
    }
    
    public void centerHorizontally(final int n, final int n2) {
        if (n2 == 0) {
            this.center(n, 0, 1, 0, 0, 2, 0, 0.5f);
        }
        else {
            this.center(n, n2, 2, 0, n2, 1, 0, 0.5f);
        }
    }
    
    public void centerHorizontally(final int i, final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final float horizontalBias) {
        this.connect(i, 1, n, n2, n3);
        this.connect(i, 2, n4, n5, n6);
        this.mConstraints.get(i).horizontalBias = horizontalBias;
    }
    
    public void centerHorizontallyRtl(final int n, final int n2) {
        if (n2 == 0) {
            this.center(n, 0, 6, 0, 0, 7, 0, 0.5f);
        }
        else {
            this.center(n, n2, 7, 0, n2, 6, 0, 0.5f);
        }
    }
    
    public void centerHorizontallyRtl(final int i, final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final float horizontalBias) {
        this.connect(i, 6, n, n2, n3);
        this.connect(i, 7, n4, n5, n6);
        this.mConstraints.get(i).horizontalBias = horizontalBias;
    }
    
    public void centerVertically(final int n, final int n2) {
        if (n2 == 0) {
            this.center(n, 0, 3, 0, 0, 4, 0, 0.5f);
        }
        else {
            this.center(n, n2, 4, 0, n2, 3, 0, 0.5f);
        }
    }
    
    public void centerVertically(final int i, final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final float verticalBias) {
        this.connect(i, 3, n, n2, n3);
        this.connect(i, 4, n4, n5, n6);
        this.mConstraints.get(i).verticalBias = verticalBias;
    }
    
    public void clear(final int i) {
        this.mConstraints.remove(i);
    }
    
    public void clear(final int n, final int n2) {
        if (this.mConstraints.containsKey(n)) {
            final Constraint constraint = this.mConstraints.get(n);
            switch (n2) {
                default: {
                    throw new IllegalArgumentException("unknown constraint");
                }
                case 7: {
                    constraint.endToStart = -1;
                    constraint.endToEnd = -1;
                    constraint.endMargin = -1;
                    constraint.goneEndMargin = -1;
                    break;
                }
                case 6: {
                    constraint.startToEnd = -1;
                    constraint.startToStart = -1;
                    constraint.startMargin = -1;
                    constraint.goneStartMargin = -1;
                    break;
                }
                case 5: {
                    constraint.baselineToBaseline = -1;
                    break;
                }
                case 4: {
                    constraint.bottomToTop = -1;
                    constraint.bottomToBottom = -1;
                    constraint.bottomMargin = -1;
                    constraint.goneBottomMargin = -1;
                    break;
                }
                case 3: {
                    constraint.topToBottom = -1;
                    constraint.topToTop = -1;
                    constraint.topMargin = -1;
                    constraint.goneTopMargin = -1;
                    break;
                }
                case 2: {
                    constraint.rightToRight = -1;
                    constraint.rightToLeft = -1;
                    constraint.rightMargin = -1;
                    constraint.goneRightMargin = -1;
                    break;
                }
                case 1: {
                    constraint.leftToRight = -1;
                    constraint.leftToLeft = -1;
                    constraint.leftMargin = -1;
                    constraint.goneLeftMargin = -1;
                    break;
                }
            }
        }
    }
    
    public void clone(final Context context, final int n) {
        this.clone((ConstraintLayout)LayoutInflater.from(context).inflate(n, (ViewGroup)null));
    }
    
    public void clone(final ConstraintLayout constraintLayout) {
        final int childCount = constraintLayout.getChildCount();
        this.mConstraints.clear();
        for (int i = 0; i < childCount; ++i) {
            final View child = constraintLayout.getChildAt(i);
            final ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)child.getLayoutParams();
            final int id = child.getId();
            if (id == -1) {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            }
            if (!this.mConstraints.containsKey(id)) {
                this.mConstraints.put(id, new Constraint());
            }
            final Constraint constraint = this.mConstraints.get(id);
            constraint.fillFrom(id, layoutParams);
            constraint.visibility = child.getVisibility();
            if (Build$VERSION.SDK_INT >= 17) {
                constraint.alpha = child.getAlpha();
                constraint.rotation = child.getRotation();
                constraint.rotationX = child.getRotationX();
                constraint.rotationY = child.getRotationY();
                constraint.scaleX = child.getScaleX();
                constraint.scaleY = child.getScaleY();
                final float pivotX = child.getPivotX();
                final float pivotY = child.getPivotY();
                if (pivotX != 0.0 || pivotY != 0.0) {
                    constraint.transformPivotX = pivotX;
                    constraint.transformPivotY = pivotY;
                }
                constraint.translationX = child.getTranslationX();
                constraint.translationY = child.getTranslationY();
                if (Build$VERSION.SDK_INT >= 21) {
                    constraint.translationZ = child.getTranslationZ();
                    if (constraint.applyElevation) {
                        constraint.elevation = child.getElevation();
                    }
                }
            }
            if (child instanceof Barrier) {
                final Barrier barrier = (Barrier)child;
                constraint.mBarrierAllowsGoneWidgets = barrier.allowsGoneWidget();
                constraint.mReferenceIds = barrier.getReferencedIds();
                constraint.mBarrierDirection = barrier.getType();
            }
        }
    }
    
    public void clone(final ConstraintSet set) {
        this.mConstraints.clear();
        for (final Integer n : set.mConstraints.keySet()) {
            this.mConstraints.put(n, set.mConstraints.get(n).clone());
        }
    }
    
    public void clone(final Constraints constraints) {
        final int childCount = constraints.getChildCount();
        this.mConstraints.clear();
        for (int i = 0; i < childCount; ++i) {
            final View child = constraints.getChildAt(i);
            final Constraints.LayoutParams layoutParams = (Constraints.LayoutParams)child.getLayoutParams();
            final int id = child.getId();
            if (id == -1) {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            }
            if (!this.mConstraints.containsKey(id)) {
                this.mConstraints.put(id, new Constraint());
            }
            final Constraint constraint = this.mConstraints.get(id);
            if (child instanceof ConstraintHelper) {
                constraint.fillFromConstraints((ConstraintHelper)child, id, layoutParams);
            }
            constraint.fillFromConstraints(id, layoutParams);
        }
    }
    
    public void connect(final int i, final int n, final int leftToRight, final int n2) {
        if (!this.mConstraints.containsKey(i)) {
            this.mConstraints.put(i, new Constraint());
        }
        final Constraint constraint = this.mConstraints.get(i);
        switch (n) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append(this.sideToString(n));
                sb.append(" to ");
                sb.append(this.sideToString(n2));
                sb.append(" unknown");
                throw new IllegalArgumentException(sb.toString());
            }
            case 7: {
                if (n2 == 7) {
                    constraint.endToEnd = leftToRight;
                    constraint.endToStart = -1;
                    break;
                }
                if (n2 == 6) {
                    constraint.endToStart = leftToRight;
                    constraint.endToEnd = -1;
                    break;
                }
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("right to ");
                sb2.append(this.sideToString(n2));
                sb2.append(" undefined");
                throw new IllegalArgumentException(sb2.toString());
            }
            case 6: {
                if (n2 == 6) {
                    constraint.startToStart = leftToRight;
                    constraint.startToEnd = -1;
                    break;
                }
                if (n2 == 7) {
                    constraint.startToEnd = leftToRight;
                    constraint.startToStart = -1;
                    break;
                }
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("right to ");
                sb3.append(this.sideToString(n2));
                sb3.append(" undefined");
                throw new IllegalArgumentException(sb3.toString());
            }
            case 5: {
                if (n2 == 5) {
                    constraint.baselineToBaseline = leftToRight;
                    constraint.bottomToBottom = -1;
                    constraint.bottomToTop = -1;
                    constraint.topToTop = -1;
                    constraint.topToBottom = -1;
                    break;
                }
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("right to ");
                sb4.append(this.sideToString(n2));
                sb4.append(" undefined");
                throw new IllegalArgumentException(sb4.toString());
            }
            case 4: {
                if (n2 == 4) {
                    constraint.bottomToBottom = leftToRight;
                    constraint.bottomToTop = -1;
                    constraint.baselineToBaseline = -1;
                    break;
                }
                if (n2 == 3) {
                    constraint.bottomToTop = leftToRight;
                    constraint.bottomToBottom = -1;
                    constraint.baselineToBaseline = -1;
                    break;
                }
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("right to ");
                sb5.append(this.sideToString(n2));
                sb5.append(" undefined");
                throw new IllegalArgumentException(sb5.toString());
            }
            case 3: {
                if (n2 == 3) {
                    constraint.topToTop = leftToRight;
                    constraint.topToBottom = -1;
                    constraint.baselineToBaseline = -1;
                    break;
                }
                if (n2 == 4) {
                    constraint.topToBottom = leftToRight;
                    constraint.topToTop = -1;
                    constraint.baselineToBaseline = -1;
                    break;
                }
                final StringBuilder sb6 = new StringBuilder();
                sb6.append("right to ");
                sb6.append(this.sideToString(n2));
                sb6.append(" undefined");
                throw new IllegalArgumentException(sb6.toString());
            }
            case 2: {
                if (n2 == 1) {
                    constraint.rightToLeft = leftToRight;
                    constraint.rightToRight = -1;
                    break;
                }
                if (n2 == 2) {
                    constraint.rightToRight = leftToRight;
                    constraint.rightToLeft = -1;
                    break;
                }
                final StringBuilder sb7 = new StringBuilder();
                sb7.append("right to ");
                sb7.append(this.sideToString(n2));
                sb7.append(" undefined");
                throw new IllegalArgumentException(sb7.toString());
            }
            case 1: {
                if (n2 == 1) {
                    constraint.leftToLeft = leftToRight;
                    constraint.leftToRight = -1;
                    break;
                }
                if (n2 == 2) {
                    constraint.leftToRight = leftToRight;
                    constraint.leftToLeft = -1;
                    break;
                }
                final StringBuilder sb8 = new StringBuilder();
                sb8.append("left to ");
                sb8.append(this.sideToString(n2));
                sb8.append(" undefined");
                throw new IllegalArgumentException(sb8.toString());
            }
        }
    }
    
    public void connect(final int i, final int n, final int leftToRight, final int n2, final int n3) {
        if (!this.mConstraints.containsKey(i)) {
            this.mConstraints.put(i, new Constraint());
        }
        final Constraint constraint = this.mConstraints.get(i);
        switch (n) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append(this.sideToString(n));
                sb.append(" to ");
                sb.append(this.sideToString(n2));
                sb.append(" unknown");
                throw new IllegalArgumentException(sb.toString());
            }
            case 7: {
                if (n2 == 7) {
                    constraint.endToEnd = leftToRight;
                    constraint.endToStart = -1;
                }
                else {
                    if (n2 != 6) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("right to ");
                        sb2.append(this.sideToString(n2));
                        sb2.append(" undefined");
                        throw new IllegalArgumentException(sb2.toString());
                    }
                    constraint.endToStart = leftToRight;
                    constraint.endToEnd = -1;
                }
                constraint.endMargin = n3;
                break;
            }
            case 6: {
                if (n2 == 6) {
                    constraint.startToStart = leftToRight;
                    constraint.startToEnd = -1;
                }
                else {
                    if (n2 != 7) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("right to ");
                        sb3.append(this.sideToString(n2));
                        sb3.append(" undefined");
                        throw new IllegalArgumentException(sb3.toString());
                    }
                    constraint.startToEnd = leftToRight;
                    constraint.startToStart = -1;
                }
                constraint.startMargin = n3;
                break;
            }
            case 5: {
                if (n2 == 5) {
                    constraint.baselineToBaseline = leftToRight;
                    constraint.bottomToBottom = -1;
                    constraint.bottomToTop = -1;
                    constraint.topToTop = -1;
                    constraint.topToBottom = -1;
                    break;
                }
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("right to ");
                sb4.append(this.sideToString(n2));
                sb4.append(" undefined");
                throw new IllegalArgumentException(sb4.toString());
            }
            case 4: {
                if (n2 == 4) {
                    constraint.bottomToBottom = leftToRight;
                    constraint.bottomToTop = -1;
                    constraint.baselineToBaseline = -1;
                }
                else {
                    if (n2 != 3) {
                        final StringBuilder sb5 = new StringBuilder();
                        sb5.append("right to ");
                        sb5.append(this.sideToString(n2));
                        sb5.append(" undefined");
                        throw new IllegalArgumentException(sb5.toString());
                    }
                    constraint.bottomToTop = leftToRight;
                    constraint.bottomToBottom = -1;
                    constraint.baselineToBaseline = -1;
                }
                constraint.bottomMargin = n3;
                break;
            }
            case 3: {
                if (n2 == 3) {
                    constraint.topToTop = leftToRight;
                    constraint.topToBottom = -1;
                    constraint.baselineToBaseline = -1;
                }
                else {
                    if (n2 != 4) {
                        final StringBuilder sb6 = new StringBuilder();
                        sb6.append("right to ");
                        sb6.append(this.sideToString(n2));
                        sb6.append(" undefined");
                        throw new IllegalArgumentException(sb6.toString());
                    }
                    constraint.topToBottom = leftToRight;
                    constraint.topToTop = -1;
                    constraint.baselineToBaseline = -1;
                }
                constraint.topMargin = n3;
                break;
            }
            case 2: {
                if (n2 == 1) {
                    constraint.rightToLeft = leftToRight;
                    constraint.rightToRight = -1;
                }
                else {
                    if (n2 != 2) {
                        final StringBuilder sb7 = new StringBuilder();
                        sb7.append("right to ");
                        sb7.append(this.sideToString(n2));
                        sb7.append(" undefined");
                        throw new IllegalArgumentException(sb7.toString());
                    }
                    constraint.rightToRight = leftToRight;
                    constraint.rightToLeft = -1;
                }
                constraint.rightMargin = n3;
                break;
            }
            case 1: {
                if (n2 == 1) {
                    constraint.leftToLeft = leftToRight;
                    constraint.leftToRight = -1;
                }
                else {
                    if (n2 != 2) {
                        final StringBuilder sb8 = new StringBuilder();
                        sb8.append("Left to ");
                        sb8.append(this.sideToString(n2));
                        sb8.append(" undefined");
                        throw new IllegalArgumentException(sb8.toString());
                    }
                    constraint.leftToRight = leftToRight;
                    constraint.leftToLeft = -1;
                }
                constraint.leftMargin = n3;
                break;
            }
        }
    }
    
    public void constrainCircle(final int n, final int circleConstraint, final int circleRadius, final float circleAngle) {
        final Constraint value = this.get(n);
        value.circleConstraint = circleConstraint;
        value.circleRadius = circleRadius;
        value.circleAngle = circleAngle;
    }
    
    public void constrainDefaultHeight(final int n, final int heightDefault) {
        this.get(n).heightDefault = heightDefault;
    }
    
    public void constrainDefaultWidth(final int n, final int widthDefault) {
        this.get(n).widthDefault = widthDefault;
    }
    
    public void constrainHeight(final int n, final int mHeight) {
        this.get(n).mHeight = mHeight;
    }
    
    public void constrainMaxHeight(final int n, final int heightMax) {
        this.get(n).heightMax = heightMax;
    }
    
    public void constrainMaxWidth(final int n, final int widthMax) {
        this.get(n).widthMax = widthMax;
    }
    
    public void constrainMinHeight(final int n, final int heightMin) {
        this.get(n).heightMin = heightMin;
    }
    
    public void constrainMinWidth(final int n, final int widthMin) {
        this.get(n).widthMin = widthMin;
    }
    
    public void constrainPercentHeight(final int n, final float heightPercent) {
        this.get(n).heightPercent = heightPercent;
    }
    
    public void constrainPercentWidth(final int n, final float widthPercent) {
        this.get(n).widthPercent = widthPercent;
    }
    
    public void constrainWidth(final int n, final int mWidth) {
        this.get(n).mWidth = mWidth;
    }
    
    public void create(final int n, final int orientation) {
        final Constraint value = this.get(n);
        value.mIsGuideline = true;
        value.orientation = orientation;
    }
    
    public void createBarrier(final int n, final int mBarrierDirection, final int... mReferenceIds) {
        final Constraint value = this.get(n);
        value.mHelperType = 1;
        value.mBarrierDirection = mBarrierDirection;
        value.mIsGuideline = false;
        value.mReferenceIds = mReferenceIds;
    }
    
    public void createHorizontalChain(final int n, final int n2, final int n3, final int n4, final int[] array, final float[] array2, final int n5) {
        this.createHorizontalChain(n, n2, n3, n4, array, array2, n5, 1, 2);
    }
    
    public void createHorizontalChainRtl(final int n, final int n2, final int n3, final int n4, final int[] array, final float[] array2, final int n5) {
        this.createHorizontalChain(n, n2, n3, n4, array, array2, n5, 6, 7);
    }
    
    public void createVerticalChain(int i, int n, final int n2, final int n3, final int[] array, final float[] array2, final int verticalChainStyle) {
        if (array.length < 2) {
            throw new IllegalArgumentException("must have 2 or more widgets in a chain");
        }
        if (array2 != null && array2.length != array.length) {
            throw new IllegalArgumentException("must have 2 or more widgets in a chain");
        }
        if (array2 != null) {
            this.get(array[0]).verticalWeight = array2[0];
        }
        this.get(array[0]).verticalChainStyle = verticalChainStyle;
        this.connect(array[0], 3, i, n, 0);
        for (i = 1; i < array.length; ++i) {
            n = array[i];
            this.connect(array[i], 3, array[i - 1], 4, 0);
            this.connect(array[i - 1], 4, array[i], 3, 0);
            if (array2 != null) {
                this.get(array[i]).verticalWeight = array2[i];
            }
        }
        this.connect(array[array.length - 1], 4, n2, n3, 0);
    }
    
    public boolean getApplyElevation(final int n) {
        return this.get(n).applyElevation;
    }
    
    public Constraint getParameters(final int n) {
        return this.get(n);
    }
    
    public void load(final Context context, int i) {
        final XmlResourceParser xml = context.getResources().getXml(i);
        try {
            String name;
            Constraint fillFromAttributeList;
            for (i = ((XmlPullParser)xml).getEventType(); i != 1; i = ((XmlPullParser)xml).next()) {
                if (i != 0) {
                    if (i != 2) {
                        if (i != 3) {}
                    }
                    else {
                        name = ((XmlPullParser)xml).getName();
                        fillFromAttributeList = this.fillFromAttributeList(context, Xml.asAttributeSet((XmlPullParser)xml));
                        if (name.equalsIgnoreCase("Guideline")) {
                            fillFromAttributeList.mIsGuideline = true;
                        }
                        this.mConstraints.put(fillFromAttributeList.mViewId, fillFromAttributeList);
                    }
                }
                else {
                    ((XmlPullParser)xml).getName();
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        catch (XmlPullParserException ex2) {
            ex2.printStackTrace();
        }
    }
    
    public void removeFromHorizontalChain(final int n) {
        if (this.mConstraints.containsKey(n)) {
            final Constraint constraint = this.mConstraints.get(n);
            final int leftToRight = constraint.leftToRight;
            final int rightToLeft = constraint.rightToLeft;
            if (leftToRight == -1 && rightToLeft == -1) {
                final int startToEnd = constraint.startToEnd;
                final int endToStart = constraint.endToStart;
                if (startToEnd != -1 || endToStart != -1) {
                    if (startToEnd != -1 && endToStart != -1) {
                        this.connect(startToEnd, 7, endToStart, 6, 0);
                        this.connect(endToStart, 6, leftToRight, 7, 0);
                    }
                    else if (leftToRight != -1 || endToStart != -1) {
                        if (constraint.rightToRight != -1) {
                            this.connect(leftToRight, 7, constraint.rightToRight, 7, 0);
                        }
                        else if (constraint.leftToLeft != -1) {
                            this.connect(endToStart, 6, constraint.leftToLeft, 6, 0);
                        }
                    }
                }
                this.clear(n, 6);
                this.clear(n, 7);
            }
            else {
                if (leftToRight != -1 && rightToLeft != -1) {
                    this.connect(leftToRight, 2, rightToLeft, 1, 0);
                    this.connect(rightToLeft, 1, leftToRight, 2, 0);
                }
                else if (leftToRight != -1 || rightToLeft != -1) {
                    if (constraint.rightToRight != -1) {
                        this.connect(leftToRight, 2, constraint.rightToRight, 2, 0);
                    }
                    else if (constraint.leftToLeft != -1) {
                        this.connect(rightToLeft, 1, constraint.leftToLeft, 1, 0);
                    }
                }
                this.clear(n, 1);
                this.clear(n, 2);
            }
        }
    }
    
    public void removeFromVerticalChain(final int n) {
        if (this.mConstraints.containsKey(n)) {
            final Constraint constraint = this.mConstraints.get(n);
            final int topToBottom = constraint.topToBottom;
            final int bottomToTop = constraint.bottomToTop;
            if (topToBottom != -1 || bottomToTop != -1) {
                if (topToBottom != -1 && bottomToTop != -1) {
                    this.connect(topToBottom, 4, bottomToTop, 3, 0);
                    this.connect(bottomToTop, 3, topToBottom, 4, 0);
                }
                else if (topToBottom != -1 || bottomToTop != -1) {
                    if (constraint.bottomToBottom != -1) {
                        this.connect(topToBottom, 4, constraint.bottomToBottom, 4, 0);
                    }
                    else if (constraint.topToTop != -1) {
                        this.connect(bottomToTop, 3, constraint.topToTop, 3, 0);
                    }
                }
            }
        }
        this.clear(n, 3);
        this.clear(n, 4);
    }
    
    public void setAlpha(final int n, final float alpha) {
        this.get(n).alpha = alpha;
    }
    
    public void setApplyElevation(final int n, final boolean applyElevation) {
        this.get(n).applyElevation = applyElevation;
    }
    
    public void setBarrierType(final int n, final int n2) {
    }
    
    public void setDimensionRatio(final int n, final String dimensionRatio) {
        this.get(n).dimensionRatio = dimensionRatio;
    }
    
    public void setElevation(final int n, final float elevation) {
        this.get(n).elevation = elevation;
        this.get(n).applyElevation = true;
    }
    
    public void setGoneMargin(final int n, final int n2, final int n3) {
        final Constraint value = this.get(n);
        switch (n2) {
            default: {
                throw new IllegalArgumentException("unknown constraint");
            }
            case 7: {
                value.goneEndMargin = n3;
                break;
            }
            case 6: {
                value.goneStartMargin = n3;
                break;
            }
            case 5: {
                throw new IllegalArgumentException("baseline does not support margins");
            }
            case 4: {
                value.goneBottomMargin = n3;
                break;
            }
            case 3: {
                value.goneTopMargin = n3;
                break;
            }
            case 2: {
                value.goneRightMargin = n3;
                break;
            }
            case 1: {
                value.goneLeftMargin = n3;
                break;
            }
        }
    }
    
    public void setGuidelineBegin(final int n, final int guideBegin) {
        this.get(n).guideBegin = guideBegin;
        this.get(n).guideEnd = -1;
        this.get(n).guidePercent = -1.0f;
    }
    
    public void setGuidelineEnd(final int n, final int guideEnd) {
        this.get(n).guideEnd = guideEnd;
        this.get(n).guideBegin = -1;
        this.get(n).guidePercent = -1.0f;
    }
    
    public void setGuidelinePercent(final int n, final float guidePercent) {
        this.get(n).guidePercent = guidePercent;
        this.get(n).guideEnd = -1;
        this.get(n).guideBegin = -1;
    }
    
    public void setHorizontalBias(final int n, final float horizontalBias) {
        this.get(n).horizontalBias = horizontalBias;
    }
    
    public void setHorizontalChainStyle(final int n, final int horizontalChainStyle) {
        this.get(n).horizontalChainStyle = horizontalChainStyle;
    }
    
    public void setHorizontalWeight(final int n, final float horizontalWeight) {
        this.get(n).horizontalWeight = horizontalWeight;
    }
    
    public void setMargin(final int n, final int n2, final int n3) {
        final Constraint value = this.get(n);
        switch (n2) {
            default: {
                throw new IllegalArgumentException("unknown constraint");
            }
            case 7: {
                value.endMargin = n3;
                break;
            }
            case 6: {
                value.startMargin = n3;
                break;
            }
            case 5: {
                throw new IllegalArgumentException("baseline does not support margins");
            }
            case 4: {
                value.bottomMargin = n3;
                break;
            }
            case 3: {
                value.topMargin = n3;
                break;
            }
            case 2: {
                value.rightMargin = n3;
                break;
            }
            case 1: {
                value.leftMargin = n3;
                break;
            }
        }
    }
    
    public void setRotation(final int n, final float rotation) {
        this.get(n).rotation = rotation;
    }
    
    public void setRotationX(final int n, final float rotationX) {
        this.get(n).rotationX = rotationX;
    }
    
    public void setRotationY(final int n, final float rotationY) {
        this.get(n).rotationY = rotationY;
    }
    
    public void setScaleX(final int n, final float scaleX) {
        this.get(n).scaleX = scaleX;
    }
    
    public void setScaleY(final int n, final float scaleY) {
        this.get(n).scaleY = scaleY;
    }
    
    public void setTransformPivot(final int n, final float transformPivotX, final float transformPivotY) {
        final Constraint value = this.get(n);
        value.transformPivotY = transformPivotY;
        value.transformPivotX = transformPivotX;
    }
    
    public void setTransformPivotX(final int n, final float transformPivotX) {
        this.get(n).transformPivotX = transformPivotX;
    }
    
    public void setTransformPivotY(final int n, final float transformPivotY) {
        this.get(n).transformPivotY = transformPivotY;
    }
    
    public void setTranslation(final int n, final float translationX, final float translationY) {
        final Constraint value = this.get(n);
        value.translationX = translationX;
        value.translationY = translationY;
    }
    
    public void setTranslationX(final int n, final float translationX) {
        this.get(n).translationX = translationX;
    }
    
    public void setTranslationY(final int n, final float translationY) {
        this.get(n).translationY = translationY;
    }
    
    public void setTranslationZ(final int n, final float translationZ) {
        this.get(n).translationZ = translationZ;
    }
    
    public void setVerticalBias(final int n, final float verticalBias) {
        this.get(n).verticalBias = verticalBias;
    }
    
    public void setVerticalChainStyle(final int n, final int verticalChainStyle) {
        this.get(n).verticalChainStyle = verticalChainStyle;
    }
    
    public void setVerticalWeight(final int n, final float verticalWeight) {
        this.get(n).verticalWeight = verticalWeight;
    }
    
    public void setVisibility(final int n, final int visibility) {
        this.get(n).visibility = visibility;
    }
    
    private static class Constraint
    {
        static final int UNSET = -1;
        public float alpha;
        public boolean applyElevation;
        public int baselineToBaseline;
        public int bottomMargin;
        public int bottomToBottom;
        public int bottomToTop;
        public float circleAngle;
        public int circleConstraint;
        public int circleRadius;
        public boolean constrainedHeight;
        public boolean constrainedWidth;
        public String dimensionRatio;
        public int editorAbsoluteX;
        public int editorAbsoluteY;
        public float elevation;
        public int endMargin;
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
        public int heightDefault;
        public int heightMax;
        public int heightMin;
        public float heightPercent;
        public float horizontalBias;
        public int horizontalChainStyle;
        public float horizontalWeight;
        public int leftMargin;
        public int leftToLeft;
        public int leftToRight;
        public boolean mBarrierAllowsGoneWidgets;
        public int mBarrierDirection;
        public int mHeight;
        public int mHelperType;
        boolean mIsGuideline;
        public String mReferenceIdString;
        public int[] mReferenceIds;
        int mViewId;
        public int mWidth;
        public int orientation;
        public int rightMargin;
        public int rightToLeft;
        public int rightToRight;
        public float rotation;
        public float rotationX;
        public float rotationY;
        public float scaleX;
        public float scaleY;
        public int startMargin;
        public int startToEnd;
        public int startToStart;
        public int topMargin;
        public int topToBottom;
        public int topToTop;
        public float transformPivotX;
        public float transformPivotY;
        public float translationX;
        public float translationY;
        public float translationZ;
        public float verticalBias;
        public int verticalChainStyle;
        public float verticalWeight;
        public int visibility;
        public int widthDefault;
        public int widthMax;
        public int widthMin;
        public float widthPercent;
        
        private Constraint() {
            this.mIsGuideline = false;
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
            this.startToEnd = -1;
            this.startToStart = -1;
            this.endToStart = -1;
            this.endToEnd = -1;
            this.horizontalBias = 0.5f;
            this.verticalBias = 0.5f;
            this.dimensionRatio = null;
            this.circleConstraint = -1;
            this.circleRadius = 0;
            this.circleAngle = 0.0f;
            this.editorAbsoluteX = -1;
            this.editorAbsoluteY = -1;
            this.orientation = -1;
            this.leftMargin = -1;
            this.rightMargin = -1;
            this.topMargin = -1;
            this.bottomMargin = -1;
            this.endMargin = -1;
            this.startMargin = -1;
            this.visibility = 0;
            this.goneLeftMargin = -1;
            this.goneTopMargin = -1;
            this.goneRightMargin = -1;
            this.goneBottomMargin = -1;
            this.goneEndMargin = -1;
            this.goneStartMargin = -1;
            this.verticalWeight = 0.0f;
            this.horizontalWeight = 0.0f;
            this.horizontalChainStyle = 0;
            this.verticalChainStyle = 0;
            this.alpha = 1.0f;
            this.applyElevation = false;
            this.elevation = 0.0f;
            this.rotation = 0.0f;
            this.rotationX = 0.0f;
            this.rotationY = 0.0f;
            this.scaleX = 1.0f;
            this.scaleY = 1.0f;
            this.transformPivotX = Float.NaN;
            this.transformPivotY = Float.NaN;
            this.translationX = 0.0f;
            this.translationY = 0.0f;
            this.translationZ = 0.0f;
            this.constrainedWidth = false;
            this.constrainedHeight = false;
            this.widthDefault = 0;
            this.heightDefault = 0;
            this.widthMax = -1;
            this.heightMax = -1;
            this.widthMin = -1;
            this.heightMin = -1;
            this.widthPercent = 1.0f;
            this.heightPercent = 1.0f;
            this.mBarrierAllowsGoneWidgets = false;
            this.mBarrierDirection = -1;
            this.mHelperType = -1;
        }
        
        private void fillFrom(final int mViewId, final ConstraintLayout.LayoutParams layoutParams) {
            this.mViewId = mViewId;
            this.leftToLeft = layoutParams.leftToLeft;
            this.leftToRight = layoutParams.leftToRight;
            this.rightToLeft = layoutParams.rightToLeft;
            this.rightToRight = layoutParams.rightToRight;
            this.topToTop = layoutParams.topToTop;
            this.topToBottom = layoutParams.topToBottom;
            this.bottomToTop = layoutParams.bottomToTop;
            this.bottomToBottom = layoutParams.bottomToBottom;
            this.baselineToBaseline = layoutParams.baselineToBaseline;
            this.startToEnd = layoutParams.startToEnd;
            this.startToStart = layoutParams.startToStart;
            this.endToStart = layoutParams.endToStart;
            this.endToEnd = layoutParams.endToEnd;
            this.horizontalBias = layoutParams.horizontalBias;
            this.verticalBias = layoutParams.verticalBias;
            this.dimensionRatio = layoutParams.dimensionRatio;
            this.circleConstraint = layoutParams.circleConstraint;
            this.circleRadius = layoutParams.circleRadius;
            this.circleAngle = layoutParams.circleAngle;
            this.editorAbsoluteX = layoutParams.editorAbsoluteX;
            this.editorAbsoluteY = layoutParams.editorAbsoluteY;
            this.orientation = layoutParams.orientation;
            this.guidePercent = layoutParams.guidePercent;
            this.guideBegin = layoutParams.guideBegin;
            this.guideEnd = layoutParams.guideEnd;
            this.mWidth = layoutParams.width;
            this.mHeight = layoutParams.height;
            this.leftMargin = layoutParams.leftMargin;
            this.rightMargin = layoutParams.rightMargin;
            this.topMargin = layoutParams.topMargin;
            this.bottomMargin = layoutParams.bottomMargin;
            this.verticalWeight = layoutParams.verticalWeight;
            this.horizontalWeight = layoutParams.horizontalWeight;
            this.verticalChainStyle = layoutParams.verticalChainStyle;
            this.horizontalChainStyle = layoutParams.horizontalChainStyle;
            this.constrainedWidth = layoutParams.constrainedWidth;
            this.constrainedHeight = layoutParams.constrainedHeight;
            this.widthDefault = layoutParams.matchConstraintDefaultWidth;
            this.heightDefault = layoutParams.matchConstraintDefaultHeight;
            this.constrainedWidth = layoutParams.constrainedWidth;
            this.widthMax = layoutParams.matchConstraintMaxWidth;
            this.heightMax = layoutParams.matchConstraintMaxHeight;
            this.widthMin = layoutParams.matchConstraintMinWidth;
            this.heightMin = layoutParams.matchConstraintMinHeight;
            this.widthPercent = layoutParams.matchConstraintPercentWidth;
            this.heightPercent = layoutParams.matchConstraintPercentHeight;
            if (Build$VERSION.SDK_INT >= 17) {
                this.endMargin = layoutParams.getMarginEnd();
                this.startMargin = layoutParams.getMarginStart();
            }
        }
        
        private void fillFromConstraints(final int n, final Constraints.LayoutParams layoutParams) {
            this.fillFrom(n, layoutParams);
            this.alpha = layoutParams.alpha;
            this.rotation = layoutParams.rotation;
            this.rotationX = layoutParams.rotationX;
            this.rotationY = layoutParams.rotationY;
            this.scaleX = layoutParams.scaleX;
            this.scaleY = layoutParams.scaleY;
            this.transformPivotX = layoutParams.transformPivotX;
            this.transformPivotY = layoutParams.transformPivotY;
            this.translationX = layoutParams.translationX;
            this.translationY = layoutParams.translationY;
            this.translationZ = layoutParams.translationZ;
            this.elevation = layoutParams.elevation;
            this.applyElevation = layoutParams.applyElevation;
        }
        
        private void fillFromConstraints(final ConstraintHelper constraintHelper, final int n, final Constraints.LayoutParams layoutParams) {
            this.fillFromConstraints(n, layoutParams);
            if (constraintHelper instanceof Barrier) {
                this.mHelperType = 1;
                final Barrier barrier = (Barrier)constraintHelper;
                this.mBarrierDirection = barrier.getType();
                this.mReferenceIds = barrier.getReferencedIds();
            }
        }
        
        public void applyTo(final ConstraintLayout.LayoutParams layoutParams) {
            layoutParams.leftToLeft = this.leftToLeft;
            layoutParams.leftToRight = this.leftToRight;
            layoutParams.rightToLeft = this.rightToLeft;
            layoutParams.rightToRight = this.rightToRight;
            layoutParams.topToTop = this.topToTop;
            layoutParams.topToBottom = this.topToBottom;
            layoutParams.bottomToTop = this.bottomToTop;
            layoutParams.bottomToBottom = this.bottomToBottom;
            layoutParams.baselineToBaseline = this.baselineToBaseline;
            layoutParams.startToEnd = this.startToEnd;
            layoutParams.startToStart = this.startToStart;
            layoutParams.endToStart = this.endToStart;
            layoutParams.endToEnd = this.endToEnd;
            layoutParams.leftMargin = this.leftMargin;
            layoutParams.rightMargin = this.rightMargin;
            layoutParams.topMargin = this.topMargin;
            layoutParams.bottomMargin = this.bottomMargin;
            layoutParams.goneStartMargin = this.goneStartMargin;
            layoutParams.goneEndMargin = this.goneEndMargin;
            layoutParams.horizontalBias = this.horizontalBias;
            layoutParams.verticalBias = this.verticalBias;
            layoutParams.circleConstraint = this.circleConstraint;
            layoutParams.circleRadius = this.circleRadius;
            layoutParams.circleAngle = this.circleAngle;
            layoutParams.dimensionRatio = this.dimensionRatio;
            layoutParams.editorAbsoluteX = this.editorAbsoluteX;
            layoutParams.editorAbsoluteY = this.editorAbsoluteY;
            layoutParams.verticalWeight = this.verticalWeight;
            layoutParams.horizontalWeight = this.horizontalWeight;
            layoutParams.verticalChainStyle = this.verticalChainStyle;
            layoutParams.horizontalChainStyle = this.horizontalChainStyle;
            layoutParams.constrainedWidth = this.constrainedWidth;
            layoutParams.constrainedHeight = this.constrainedHeight;
            layoutParams.matchConstraintDefaultWidth = this.widthDefault;
            layoutParams.matchConstraintDefaultHeight = this.heightDefault;
            layoutParams.matchConstraintMaxWidth = this.widthMax;
            layoutParams.matchConstraintMaxHeight = this.heightMax;
            layoutParams.matchConstraintMinWidth = this.widthMin;
            layoutParams.matchConstraintMinHeight = this.heightMin;
            layoutParams.matchConstraintPercentWidth = this.widthPercent;
            layoutParams.matchConstraintPercentHeight = this.heightPercent;
            layoutParams.orientation = this.orientation;
            layoutParams.guidePercent = this.guidePercent;
            layoutParams.guideBegin = this.guideBegin;
            layoutParams.guideEnd = this.guideEnd;
            layoutParams.width = this.mWidth;
            layoutParams.height = this.mHeight;
            if (Build$VERSION.SDK_INT >= 17) {
                layoutParams.setMarginStart(this.startMargin);
                layoutParams.setMarginEnd(this.endMargin);
            }
            layoutParams.validate();
        }
        
        public Constraint clone() {
            final Constraint constraint = new Constraint();
            constraint.mIsGuideline = this.mIsGuideline;
            constraint.mWidth = this.mWidth;
            constraint.mHeight = this.mHeight;
            constraint.guideBegin = this.guideBegin;
            constraint.guideEnd = this.guideEnd;
            constraint.guidePercent = this.guidePercent;
            constraint.leftToLeft = this.leftToLeft;
            constraint.leftToRight = this.leftToRight;
            constraint.rightToLeft = this.rightToLeft;
            constraint.rightToRight = this.rightToRight;
            constraint.topToTop = this.topToTop;
            constraint.topToBottom = this.topToBottom;
            constraint.bottomToTop = this.bottomToTop;
            constraint.bottomToBottom = this.bottomToBottom;
            constraint.baselineToBaseline = this.baselineToBaseline;
            constraint.startToEnd = this.startToEnd;
            constraint.startToStart = this.startToStart;
            constraint.endToStart = this.endToStart;
            constraint.endToEnd = this.endToEnd;
            constraint.horizontalBias = this.horizontalBias;
            constraint.verticalBias = this.verticalBias;
            constraint.dimensionRatio = this.dimensionRatio;
            constraint.editorAbsoluteX = this.editorAbsoluteX;
            constraint.editorAbsoluteY = this.editorAbsoluteY;
            constraint.horizontalBias = this.horizontalBias;
            constraint.horizontalBias = this.horizontalBias;
            constraint.horizontalBias = this.horizontalBias;
            constraint.horizontalBias = this.horizontalBias;
            constraint.horizontalBias = this.horizontalBias;
            constraint.orientation = this.orientation;
            constraint.leftMargin = this.leftMargin;
            constraint.rightMargin = this.rightMargin;
            constraint.topMargin = this.topMargin;
            constraint.bottomMargin = this.bottomMargin;
            constraint.endMargin = this.endMargin;
            constraint.startMargin = this.startMargin;
            constraint.visibility = this.visibility;
            constraint.goneLeftMargin = this.goneLeftMargin;
            constraint.goneTopMargin = this.goneTopMargin;
            constraint.goneRightMargin = this.goneRightMargin;
            constraint.goneBottomMargin = this.goneBottomMargin;
            constraint.goneEndMargin = this.goneEndMargin;
            constraint.goneStartMargin = this.goneStartMargin;
            constraint.verticalWeight = this.verticalWeight;
            constraint.horizontalWeight = this.horizontalWeight;
            constraint.horizontalChainStyle = this.horizontalChainStyle;
            constraint.verticalChainStyle = this.verticalChainStyle;
            constraint.alpha = this.alpha;
            constraint.applyElevation = this.applyElevation;
            constraint.elevation = this.elevation;
            constraint.rotation = this.rotation;
            constraint.rotationX = this.rotationX;
            constraint.rotationY = this.rotationY;
            constraint.scaleX = this.scaleX;
            constraint.scaleY = this.scaleY;
            constraint.transformPivotX = this.transformPivotX;
            constraint.transformPivotY = this.transformPivotY;
            constraint.translationX = this.translationX;
            constraint.translationY = this.translationY;
            constraint.translationZ = this.translationZ;
            constraint.constrainedWidth = this.constrainedWidth;
            constraint.constrainedHeight = this.constrainedHeight;
            constraint.widthDefault = this.widthDefault;
            constraint.heightDefault = this.heightDefault;
            constraint.widthMax = this.widthMax;
            constraint.heightMax = this.heightMax;
            constraint.widthMin = this.widthMin;
            constraint.heightMin = this.heightMin;
            constraint.widthPercent = this.widthPercent;
            constraint.heightPercent = this.heightPercent;
            constraint.mBarrierDirection = this.mBarrierDirection;
            constraint.mHelperType = this.mHelperType;
            final int[] mReferenceIds = this.mReferenceIds;
            if (mReferenceIds != null) {
                constraint.mReferenceIds = Arrays.copyOf(mReferenceIds, mReferenceIds.length);
            }
            constraint.circleConstraint = this.circleConstraint;
            constraint.circleRadius = this.circleRadius;
            constraint.circleAngle = this.circleAngle;
            constraint.mBarrierAllowsGoneWidgets = this.mBarrierAllowsGoneWidgets;
            return constraint;
        }
    }
}
