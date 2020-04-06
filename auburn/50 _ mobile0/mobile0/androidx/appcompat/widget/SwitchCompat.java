// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.widget;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.text.AllCapsTransformationMethod;
import android.widget.TextView;
import androidx.core.widget.TextViewCompat;
import android.view.ActionMode$Callback;
import androidx.core.view.ViewCompat;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityEvent;
import android.graphics.Region$Op;
import android.text.TextUtils;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.StaticLayout;
import android.text.Layout$Alignment;
import android.view.View;
import android.view.MotionEvent;
import androidx.core.graphics.drawable.DrawableCompat;
import android.os.Build$VERSION;
import android.view.ViewConfiguration;
import android.graphics.drawable.Drawable$Callback;
import androidx.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.view.VelocityTracker;
import android.graphics.PorterDuff$Mode;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.text.method.TransformationMethod;
import android.animation.ObjectAnimator;
import android.text.Layout;
import android.util.Property;
import android.widget.CompoundButton;

public class SwitchCompat extends CompoundButton
{
    private static final String ACCESSIBILITY_EVENT_CLASS_NAME = "android.widget.Switch";
    private static final int[] CHECKED_STATE_SET;
    private static final int MONOSPACE = 3;
    private static final int SANS = 1;
    private static final int SERIF = 2;
    private static final int THUMB_ANIMATION_DURATION = 250;
    private static final Property<SwitchCompat, Float> THUMB_POS;
    private static final int TOUCH_MODE_DOWN = 1;
    private static final int TOUCH_MODE_DRAGGING = 2;
    private static final int TOUCH_MODE_IDLE = 0;
    private boolean mHasThumbTint;
    private boolean mHasThumbTintMode;
    private boolean mHasTrackTint;
    private boolean mHasTrackTintMode;
    private int mMinFlingVelocity;
    private Layout mOffLayout;
    private Layout mOnLayout;
    ObjectAnimator mPositionAnimator;
    private boolean mShowText;
    private boolean mSplitTrack;
    private int mSwitchBottom;
    private int mSwitchHeight;
    private int mSwitchLeft;
    private int mSwitchMinWidth;
    private int mSwitchPadding;
    private int mSwitchRight;
    private int mSwitchTop;
    private TransformationMethod mSwitchTransformationMethod;
    private int mSwitchWidth;
    private final Rect mTempRect;
    private ColorStateList mTextColors;
    private CharSequence mTextOff;
    private CharSequence mTextOn;
    private final TextPaint mTextPaint;
    private Drawable mThumbDrawable;
    float mThumbPosition;
    private int mThumbTextPadding;
    private ColorStateList mThumbTintList;
    private PorterDuff$Mode mThumbTintMode;
    private int mThumbWidth;
    private int mTouchMode;
    private int mTouchSlop;
    private float mTouchX;
    private float mTouchY;
    private Drawable mTrackDrawable;
    private ColorStateList mTrackTintList;
    private PorterDuff$Mode mTrackTintMode;
    private VelocityTracker mVelocityTracker;
    
    static {
        THUMB_POS = new Property<SwitchCompat, Float>(Float.class, "thumbPos") {
            public Float get(final SwitchCompat switchCompat) {
                return switchCompat.mThumbPosition;
            }
            
            public void set(final SwitchCompat switchCompat, final Float n) {
                switchCompat.setThumbPosition(n);
            }
        };
        CHECKED_STATE_SET = new int[] { 16842912 };
    }
    
    public SwitchCompat(final Context context) {
        this(context, null);
    }
    
    public SwitchCompat(final Context context, final AttributeSet set) {
        this(context, set, R.attr.switchStyle);
    }
    
    public SwitchCompat(final Context context, final AttributeSet set, int resourceId) {
        super(context, set, resourceId);
        this.mThumbTintList = null;
        this.mThumbTintMode = null;
        this.mHasThumbTint = false;
        this.mHasThumbTintMode = false;
        this.mTrackTintList = null;
        this.mTrackTintMode = null;
        this.mHasTrackTint = false;
        this.mHasTrackTintMode = false;
        this.mVelocityTracker = VelocityTracker.obtain();
        this.mTempRect = new Rect();
        this.mTextPaint = new TextPaint(1);
        this.mTextPaint.density = this.getResources().getDisplayMetrics().density;
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, set, R.styleable.SwitchCompat, resourceId, 0);
        final Drawable drawable = obtainStyledAttributes.getDrawable(R.styleable.SwitchCompat_android_thumb);
        this.mThumbDrawable = drawable;
        if (drawable != null) {
            drawable.setCallback((Drawable$Callback)this);
        }
        final Drawable drawable2 = obtainStyledAttributes.getDrawable(R.styleable.SwitchCompat_track);
        if ((this.mTrackDrawable = drawable2) != null) {
            drawable2.setCallback((Drawable$Callback)this);
        }
        this.mTextOn = obtainStyledAttributes.getText(R.styleable.SwitchCompat_android_textOn);
        this.mTextOff = obtainStyledAttributes.getText(R.styleable.SwitchCompat_android_textOff);
        this.mShowText = obtainStyledAttributes.getBoolean(R.styleable.SwitchCompat_showText, true);
        this.mThumbTextPadding = obtainStyledAttributes.getDimensionPixelSize(R.styleable.SwitchCompat_thumbTextPadding, 0);
        this.mSwitchMinWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.SwitchCompat_switchMinWidth, 0);
        this.mSwitchPadding = obtainStyledAttributes.getDimensionPixelSize(R.styleable.SwitchCompat_switchPadding, 0);
        this.mSplitTrack = obtainStyledAttributes.getBoolean(R.styleable.SwitchCompat_splitTrack, false);
        final ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(R.styleable.SwitchCompat_thumbTint);
        if (colorStateList != null) {
            this.mThumbTintList = colorStateList;
            this.mHasThumbTint = true;
        }
        final PorterDuff$Mode tintMode = DrawableUtils.parseTintMode(obtainStyledAttributes.getInt(R.styleable.SwitchCompat_thumbTintMode, -1), null);
        if (this.mThumbTintMode != tintMode) {
            this.mThumbTintMode = tintMode;
            this.mHasThumbTintMode = true;
        }
        if (this.mHasThumbTint || this.mHasThumbTintMode) {
            this.applyThumbTint();
        }
        final ColorStateList colorStateList2 = obtainStyledAttributes.getColorStateList(R.styleable.SwitchCompat_trackTint);
        if (colorStateList2 != null) {
            this.mTrackTintList = colorStateList2;
            this.mHasTrackTint = true;
        }
        final PorterDuff$Mode tintMode2 = DrawableUtils.parseTintMode(obtainStyledAttributes.getInt(R.styleable.SwitchCompat_trackTintMode, -1), null);
        if (this.mTrackTintMode != tintMode2) {
            this.mTrackTintMode = tintMode2;
            this.mHasTrackTintMode = true;
        }
        if (this.mHasTrackTint || this.mHasTrackTintMode) {
            this.applyTrackTint();
        }
        resourceId = obtainStyledAttributes.getResourceId(R.styleable.SwitchCompat_switchTextAppearance, 0);
        if (resourceId != 0) {
            this.setSwitchTextAppearance(context, resourceId);
        }
        obtainStyledAttributes.recycle();
        final ViewConfiguration value = ViewConfiguration.get(context);
        this.mTouchSlop = value.getScaledTouchSlop();
        this.mMinFlingVelocity = value.getScaledMinimumFlingVelocity();
        this.refreshDrawableState();
        this.setChecked(this.isChecked());
    }
    
    private void animateThumbToCheckedState(final boolean b) {
        float n;
        if (b) {
            n = 1.0f;
        }
        else {
            n = 0.0f;
        }
        (this.mPositionAnimator = ObjectAnimator.ofFloat((Object)this, (Property)SwitchCompat.THUMB_POS, new float[] { n })).setDuration(250L);
        if (Build$VERSION.SDK_INT >= 18) {
            this.mPositionAnimator.setAutoCancel(true);
        }
        this.mPositionAnimator.start();
    }
    
    private void applyThumbTint() {
        if (this.mThumbDrawable != null && (this.mHasThumbTint || this.mHasThumbTintMode)) {
            final Drawable mutate = this.mThumbDrawable.mutate();
            this.mThumbDrawable = mutate;
            if (this.mHasThumbTint) {
                DrawableCompat.setTintList(mutate, this.mThumbTintList);
            }
            if (this.mHasThumbTintMode) {
                DrawableCompat.setTintMode(this.mThumbDrawable, this.mThumbTintMode);
            }
            if (this.mThumbDrawable.isStateful()) {
                this.mThumbDrawable.setState(this.getDrawableState());
            }
        }
    }
    
    private void applyTrackTint() {
        if (this.mTrackDrawable != null && (this.mHasTrackTint || this.mHasTrackTintMode)) {
            final Drawable mutate = this.mTrackDrawable.mutate();
            this.mTrackDrawable = mutate;
            if (this.mHasTrackTint) {
                DrawableCompat.setTintList(mutate, this.mTrackTintList);
            }
            if (this.mHasTrackTintMode) {
                DrawableCompat.setTintMode(this.mTrackDrawable, this.mTrackTintMode);
            }
            if (this.mTrackDrawable.isStateful()) {
                this.mTrackDrawable.setState(this.getDrawableState());
            }
        }
    }
    
    private void cancelPositionAnimator() {
        final ObjectAnimator mPositionAnimator = this.mPositionAnimator;
        if (mPositionAnimator != null) {
            mPositionAnimator.cancel();
        }
    }
    
    private void cancelSuperTouch(MotionEvent obtain) {
        obtain = MotionEvent.obtain(obtain);
        obtain.setAction(3);
        super.onTouchEvent(obtain);
        obtain.recycle();
    }
    
    private static float constrain(float n, final float n2, final float n3) {
        if (n < n2) {
            n = n2;
        }
        else if (n > n3) {
            n = n3;
        }
        return n;
    }
    
    private boolean getTargetCheckedState() {
        return this.mThumbPosition > 0.5f;
    }
    
    private int getThumbOffset() {
        float mThumbPosition;
        if (ViewUtils.isLayoutRtl((View)this)) {
            mThumbPosition = 1.0f - this.mThumbPosition;
        }
        else {
            mThumbPosition = this.mThumbPosition;
        }
        return (int)(this.getThumbScrollRange() * mThumbPosition + 0.5f);
    }
    
    private int getThumbScrollRange() {
        final Drawable mTrackDrawable = this.mTrackDrawable;
        if (mTrackDrawable != null) {
            final Rect mTempRect = this.mTempRect;
            mTrackDrawable.getPadding(mTempRect);
            final Drawable mThumbDrawable = this.mThumbDrawable;
            Rect rect;
            if (mThumbDrawable != null) {
                rect = DrawableUtils.getOpticalBounds(mThumbDrawable);
            }
            else {
                rect = DrawableUtils.INSETS_NONE;
            }
            return this.mSwitchWidth - this.mThumbWidth - mTempRect.left - mTempRect.right - rect.left - rect.right;
        }
        return 0;
    }
    
    private boolean hitThumb(final float n, final float n2) {
        final Drawable mThumbDrawable = this.mThumbDrawable;
        final boolean b = false;
        if (mThumbDrawable == null) {
            return false;
        }
        final int thumbOffset = this.getThumbOffset();
        this.mThumbDrawable.getPadding(this.mTempRect);
        final int mSwitchTop = this.mSwitchTop;
        final int mTouchSlop = this.mTouchSlop;
        final int n3 = this.mSwitchLeft + thumbOffset - mTouchSlop;
        final int mThumbWidth = this.mThumbWidth;
        final int left = this.mTempRect.left;
        final int right = this.mTempRect.right;
        final int mTouchSlop2 = this.mTouchSlop;
        final int mSwitchBottom = this.mSwitchBottom;
        boolean b2 = b;
        if (n > n3) {
            b2 = b;
            if (n < mThumbWidth + n3 + left + right + mTouchSlop2) {
                b2 = b;
                if (n2 > mSwitchTop - mTouchSlop) {
                    b2 = b;
                    if (n2 < mSwitchBottom + mTouchSlop2) {
                        b2 = true;
                    }
                }
            }
        }
        return b2;
    }
    
    private Layout makeLayout(CharSequence transformation) {
        final TransformationMethod mSwitchTransformationMethod = this.mSwitchTransformationMethod;
        if (mSwitchTransformationMethod != null) {
            transformation = mSwitchTransformationMethod.getTransformation(transformation, (View)this);
        }
        final TextPaint mTextPaint = this.mTextPaint;
        int n;
        if (transformation != null) {
            n = (int)Math.ceil(Layout.getDesiredWidth(transformation, mTextPaint));
        }
        else {
            n = 0;
        }
        return (Layout)new StaticLayout(transformation, mTextPaint, n, Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
    }
    
    private void setSwitchTypefaceByIndex(final int n, final int n2) {
        Typeface typeface = null;
        if (n != 1) {
            if (n != 2) {
                if (n == 3) {
                    typeface = Typeface.MONOSPACE;
                }
            }
            else {
                typeface = Typeface.SERIF;
            }
        }
        else {
            typeface = Typeface.SANS_SERIF;
        }
        this.setSwitchTypeface(typeface, n2);
    }
    
    private void stopDrag(final MotionEvent motionEvent) {
        this.mTouchMode = 0;
        final int action = motionEvent.getAction();
        boolean targetCheckedState = true;
        final boolean b = action == 1 && this.isEnabled();
        final boolean checked = this.isChecked();
        if (b) {
            this.mVelocityTracker.computeCurrentVelocity(1000);
            final float xVelocity = this.mVelocityTracker.getXVelocity();
            if (Math.abs(xVelocity) > this.mMinFlingVelocity) {
                Label_0103: {
                    Label_0101: {
                        if (ViewUtils.isLayoutRtl((View)this)) {
                            if (xVelocity >= 0.0f) {
                                break Label_0101;
                            }
                        }
                        else if (xVelocity <= 0.0f) {
                            break Label_0101;
                        }
                        break Label_0103;
                    }
                    targetCheckedState = false;
                }
            }
            else {
                targetCheckedState = this.getTargetCheckedState();
            }
        }
        else {
            targetCheckedState = checked;
        }
        if (targetCheckedState != checked) {
            this.playSoundEffect(0);
        }
        this.setChecked(targetCheckedState);
        this.cancelSuperTouch(motionEvent);
    }
    
    public void draw(final Canvas canvas) {
        final Rect mTempRect = this.mTempRect;
        final int mSwitchLeft = this.mSwitchLeft;
        final int mSwitchTop = this.mSwitchTop;
        final int mSwitchRight = this.mSwitchRight;
        final int mSwitchBottom = this.mSwitchBottom;
        final int n = this.getThumbOffset() + mSwitchLeft;
        final Drawable mThumbDrawable = this.mThumbDrawable;
        Rect rect;
        if (mThumbDrawable != null) {
            rect = DrawableUtils.getOpticalBounds(mThumbDrawable);
        }
        else {
            rect = DrawableUtils.INSETS_NONE;
        }
        final Drawable mTrackDrawable = this.mTrackDrawable;
        int n2 = n;
        if (mTrackDrawable != null) {
            mTrackDrawable.getPadding(mTempRect);
            final int n3 = n + mTempRect.left;
            final int n4 = mSwitchTop;
            final int n5 = mSwitchBottom;
            int n6 = mSwitchLeft;
            int n7 = n4;
            int n8 = mSwitchRight;
            int n9 = n5;
            if (rect != null) {
                int n10 = mSwitchLeft;
                if (rect.left > mTempRect.left) {
                    n10 = mSwitchLeft + (rect.left - mTempRect.left);
                }
                int n11 = n4;
                if (rect.top > mTempRect.top) {
                    n11 = n4 + (rect.top - mTempRect.top);
                }
                int n12 = mSwitchRight;
                if (rect.right > mTempRect.right) {
                    n12 = mSwitchRight - (rect.right - mTempRect.right);
                }
                n6 = n10;
                n7 = n11;
                n8 = n12;
                n9 = n5;
                if (rect.bottom > mTempRect.bottom) {
                    n9 = n5 - (rect.bottom - mTempRect.bottom);
                    n8 = n12;
                    n7 = n11;
                    n6 = n10;
                }
            }
            this.mTrackDrawable.setBounds(n6, n7, n8, n9);
            n2 = n3;
        }
        final Drawable mThumbDrawable2 = this.mThumbDrawable;
        if (mThumbDrawable2 != null) {
            mThumbDrawable2.getPadding(mTempRect);
            final int n13 = n2 - mTempRect.left;
            final int n14 = this.mThumbWidth + n2 + mTempRect.right;
            this.mThumbDrawable.setBounds(n13, mSwitchTop, n14, mSwitchBottom);
            final Drawable background = this.getBackground();
            if (background != null) {
                DrawableCompat.setHotspotBounds(background, n13, mSwitchTop, n14, mSwitchBottom);
            }
        }
        super.draw(canvas);
    }
    
    public void drawableHotspotChanged(final float n, final float n2) {
        if (Build$VERSION.SDK_INT >= 21) {
            super.drawableHotspotChanged(n, n2);
        }
        final Drawable mThumbDrawable = this.mThumbDrawable;
        if (mThumbDrawable != null) {
            DrawableCompat.setHotspot(mThumbDrawable, n, n2);
        }
        final Drawable mTrackDrawable = this.mTrackDrawable;
        if (mTrackDrawable != null) {
            DrawableCompat.setHotspot(mTrackDrawable, n, n2);
        }
    }
    
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        final int[] drawableState = this.getDrawableState();
        final boolean b = false;
        final Drawable mThumbDrawable = this.mThumbDrawable;
        boolean b2 = b;
        if (mThumbDrawable != null) {
            b2 = b;
            if (mThumbDrawable.isStateful()) {
                b2 = (false | mThumbDrawable.setState(drawableState));
            }
        }
        final Drawable mTrackDrawable = this.mTrackDrawable;
        int n = b2 ? 1 : 0;
        if (mTrackDrawable != null) {
            n = (b2 ? 1 : 0);
            if (mTrackDrawable.isStateful()) {
                n = ((b2 | mTrackDrawable.setState(drawableState)) ? 1 : 0);
            }
        }
        if (n != 0) {
            this.invalidate();
        }
    }
    
    public int getCompoundPaddingLeft() {
        if (!ViewUtils.isLayoutRtl((View)this)) {
            return super.getCompoundPaddingLeft();
        }
        int n = super.getCompoundPaddingLeft() + this.mSwitchWidth;
        if (!TextUtils.isEmpty(this.getText())) {
            n += this.mSwitchPadding;
        }
        return n;
    }
    
    public int getCompoundPaddingRight() {
        if (ViewUtils.isLayoutRtl((View)this)) {
            return super.getCompoundPaddingRight();
        }
        int n = super.getCompoundPaddingRight() + this.mSwitchWidth;
        if (!TextUtils.isEmpty(this.getText())) {
            n += this.mSwitchPadding;
        }
        return n;
    }
    
    public boolean getShowText() {
        return this.mShowText;
    }
    
    public boolean getSplitTrack() {
        return this.mSplitTrack;
    }
    
    public int getSwitchMinWidth() {
        return this.mSwitchMinWidth;
    }
    
    public int getSwitchPadding() {
        return this.mSwitchPadding;
    }
    
    public CharSequence getTextOff() {
        return this.mTextOff;
    }
    
    public CharSequence getTextOn() {
        return this.mTextOn;
    }
    
    public Drawable getThumbDrawable() {
        return this.mThumbDrawable;
    }
    
    public int getThumbTextPadding() {
        return this.mThumbTextPadding;
    }
    
    public ColorStateList getThumbTintList() {
        return this.mThumbTintList;
    }
    
    public PorterDuff$Mode getThumbTintMode() {
        return this.mThumbTintMode;
    }
    
    public Drawable getTrackDrawable() {
        return this.mTrackDrawable;
    }
    
    public ColorStateList getTrackTintList() {
        return this.mTrackTintList;
    }
    
    public PorterDuff$Mode getTrackTintMode() {
        return this.mTrackTintMode;
    }
    
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        final Drawable mThumbDrawable = this.mThumbDrawable;
        if (mThumbDrawable != null) {
            mThumbDrawable.jumpToCurrentState();
        }
        final Drawable mTrackDrawable = this.mTrackDrawable;
        if (mTrackDrawable != null) {
            mTrackDrawable.jumpToCurrentState();
        }
        final ObjectAnimator mPositionAnimator = this.mPositionAnimator;
        if (mPositionAnimator != null && mPositionAnimator.isStarted()) {
            this.mPositionAnimator.end();
            this.mPositionAnimator = null;
        }
    }
    
    protected int[] onCreateDrawableState(final int n) {
        final int[] onCreateDrawableState = super.onCreateDrawableState(n + 1);
        if (this.isChecked()) {
            mergeDrawableStates(onCreateDrawableState, SwitchCompat.CHECKED_STATE_SET);
        }
        return onCreateDrawableState;
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        final Rect mTempRect = this.mTempRect;
        final Drawable mTrackDrawable = this.mTrackDrawable;
        if (mTrackDrawable != null) {
            mTrackDrawable.getPadding(mTempRect);
        }
        else {
            mTempRect.setEmpty();
        }
        final int mSwitchTop = this.mSwitchTop;
        final int mSwitchBottom = this.mSwitchBottom;
        final int top = mTempRect.top;
        final int bottom = mTempRect.bottom;
        final Drawable mThumbDrawable = this.mThumbDrawable;
        if (mTrackDrawable != null) {
            if (this.mSplitTrack && mThumbDrawable != null) {
                final Rect opticalBounds = DrawableUtils.getOpticalBounds(mThumbDrawable);
                mThumbDrawable.copyBounds(mTempRect);
                mTempRect.left += opticalBounds.left;
                mTempRect.right -= opticalBounds.right;
                final int save = canvas.save();
                canvas.clipRect(mTempRect, Region$Op.DIFFERENCE);
                mTrackDrawable.draw(canvas);
                canvas.restoreToCount(save);
            }
            else {
                mTrackDrawable.draw(canvas);
            }
        }
        final int save2 = canvas.save();
        if (mThumbDrawable != null) {
            mThumbDrawable.draw(canvas);
        }
        Layout layout;
        if (this.getTargetCheckedState()) {
            layout = this.mOnLayout;
        }
        else {
            layout = this.mOffLayout;
        }
        if (layout != null) {
            final int[] drawableState = this.getDrawableState();
            final ColorStateList mTextColors = this.mTextColors;
            if (mTextColors != null) {
                this.mTextPaint.setColor(mTextColors.getColorForState(drawableState, 0));
            }
            this.mTextPaint.drawableState = drawableState;
            int width;
            if (mThumbDrawable != null) {
                final Rect bounds = mThumbDrawable.getBounds();
                width = bounds.left + bounds.right;
            }
            else {
                width = this.getWidth();
            }
            canvas.translate((float)(width / 2 - layout.getWidth() / 2), (float)((top + mSwitchTop + (mSwitchBottom - bottom)) / 2 - layout.getHeight() / 2));
            layout.draw(canvas);
        }
        canvas.restoreToCount(save2);
    }
    
    public void onInitializeAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName((CharSequence)"android.widget.Switch");
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName((CharSequence)"android.widget.Switch");
        CharSequence charSequence;
        if (this.isChecked()) {
            charSequence = this.mTextOn;
        }
        else {
            charSequence = this.mTextOff;
        }
        if (!TextUtils.isEmpty(charSequence)) {
            final CharSequence text = accessibilityNodeInfo.getText();
            if (TextUtils.isEmpty(text)) {
                accessibilityNodeInfo.setText(charSequence);
            }
            else {
                final StringBuilder text2 = new StringBuilder();
                text2.append(text);
                text2.append(' ');
                text2.append(charSequence);
                accessibilityNodeInfo.setText((CharSequence)text2);
            }
        }
    }
    
    protected void onLayout(final boolean b, int mSwitchTop, int mSwitchBottom, int mSwitchRight, int mSwitchLeft) {
        super.onLayout(b, mSwitchTop, mSwitchBottom, mSwitchRight, mSwitchLeft);
        mSwitchBottom = 0;
        mSwitchTop = 0;
        if (this.mThumbDrawable != null) {
            final Rect mTempRect = this.mTempRect;
            final Drawable mTrackDrawable = this.mTrackDrawable;
            if (mTrackDrawable != null) {
                mTrackDrawable.getPadding(mTempRect);
            }
            else {
                mTempRect.setEmpty();
            }
            final Rect opticalBounds = DrawableUtils.getOpticalBounds(this.mThumbDrawable);
            mSwitchBottom = Math.max(0, opticalBounds.left - mTempRect.left);
            mSwitchTop = Math.max(0, opticalBounds.right - mTempRect.right);
        }
        if (ViewUtils.isLayoutRtl((View)this)) {
            mSwitchLeft = this.getPaddingLeft() + mSwitchBottom;
            mSwitchRight = this.mSwitchWidth + mSwitchLeft - mSwitchBottom - mSwitchTop;
        }
        else {
            mSwitchRight = this.getWidth() - this.getPaddingRight() - mSwitchTop;
            mSwitchLeft = mSwitchRight - this.mSwitchWidth + mSwitchBottom + mSwitchTop;
        }
        mSwitchTop = (this.getGravity() & 0x70);
        if (mSwitchTop != 16) {
            if (mSwitchTop != 80) {
                mSwitchTop = this.getPaddingTop();
                mSwitchBottom = this.mSwitchHeight + mSwitchTop;
            }
            else {
                mSwitchBottom = this.getHeight() - this.getPaddingBottom();
                mSwitchTop = mSwitchBottom - this.mSwitchHeight;
            }
        }
        else {
            mSwitchTop = (this.getPaddingTop() + this.getHeight() - this.getPaddingBottom()) / 2;
            mSwitchBottom = this.mSwitchHeight;
            mSwitchTop -= mSwitchBottom / 2;
            mSwitchBottom += mSwitchTop;
        }
        this.mSwitchLeft = mSwitchLeft;
        this.mSwitchTop = mSwitchTop;
        this.mSwitchBottom = mSwitchBottom;
        this.mSwitchRight = mSwitchRight;
    }
    
    public void onMeasure(final int n, final int n2) {
        if (this.mShowText) {
            if (this.mOnLayout == null) {
                this.mOnLayout = this.makeLayout(this.mTextOn);
            }
            if (this.mOffLayout == null) {
                this.mOffLayout = this.makeLayout(this.mTextOff);
            }
        }
        final Rect mTempRect = this.mTempRect;
        final Drawable mThumbDrawable = this.mThumbDrawable;
        int b;
        int intrinsicHeight;
        if (mThumbDrawable != null) {
            mThumbDrawable.getPadding(mTempRect);
            b = this.mThumbDrawable.getIntrinsicWidth() - mTempRect.left - mTempRect.right;
            intrinsicHeight = this.mThumbDrawable.getIntrinsicHeight();
        }
        else {
            b = 0;
            intrinsicHeight = 0;
        }
        int a;
        if (this.mShowText) {
            a = Math.max(this.mOnLayout.getWidth(), this.mOffLayout.getWidth()) + this.mThumbTextPadding * 2;
        }
        else {
            a = 0;
        }
        this.mThumbWidth = Math.max(a, b);
        final Drawable mTrackDrawable = this.mTrackDrawable;
        int intrinsicHeight2;
        if (mTrackDrawable != null) {
            mTrackDrawable.getPadding(mTempRect);
            intrinsicHeight2 = this.mTrackDrawable.getIntrinsicHeight();
        }
        else {
            mTempRect.setEmpty();
            intrinsicHeight2 = 0;
        }
        final int left = mTempRect.left;
        final int right = mTempRect.right;
        final Drawable mThumbDrawable2 = this.mThumbDrawable;
        int max = left;
        int max2 = right;
        if (mThumbDrawable2 != null) {
            final Rect opticalBounds = DrawableUtils.getOpticalBounds(mThumbDrawable2);
            max = Math.max(left, opticalBounds.left);
            max2 = Math.max(right, opticalBounds.right);
        }
        final int max3 = Math.max(this.mSwitchMinWidth, this.mThumbWidth * 2 + max + max2);
        final int max4 = Math.max(intrinsicHeight2, intrinsicHeight);
        this.mSwitchWidth = max3;
        this.mSwitchHeight = max4;
        super.onMeasure(n, n2);
        if (this.getMeasuredHeight() < max4) {
            this.setMeasuredDimension(this.getMeasuredWidthAndState(), max4);
        }
    }
    
    public void onPopulateAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        super.onPopulateAccessibilityEvent(accessibilityEvent);
        CharSequence charSequence;
        if (this.isChecked()) {
            charSequence = this.mTextOn;
        }
        else {
            charSequence = this.mTextOff;
        }
        if (charSequence != null) {
            accessibilityEvent.getText().add(charSequence);
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        this.mVelocityTracker.addMovement(motionEvent);
        final int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    final int mTouchMode = this.mTouchMode;
                    if (mTouchMode != 1) {
                        if (mTouchMode == 2) {
                            final float x = motionEvent.getX();
                            final int thumbScrollRange = this.getThumbScrollRange();
                            final float n = x - this.mTouchX;
                            float n2;
                            if (thumbScrollRange != 0) {
                                n2 = n / thumbScrollRange;
                            }
                            else if (n > 0.0f) {
                                n2 = 1.0f;
                            }
                            else {
                                n2 = -1.0f;
                            }
                            float n3 = n2;
                            if (ViewUtils.isLayoutRtl((View)this)) {
                                n3 = -n2;
                            }
                            final float constrain = constrain(this.mThumbPosition + n3, 0.0f, 1.0f);
                            if (constrain != this.mThumbPosition) {
                                this.mTouchX = x;
                                this.setThumbPosition(constrain);
                            }
                            return true;
                        }
                    }
                    else {
                        final float x2 = motionEvent.getX();
                        final float y = motionEvent.getY();
                        if (Math.abs(x2 - this.mTouchX) > this.mTouchSlop || Math.abs(y - this.mTouchY) > this.mTouchSlop) {
                            this.mTouchMode = 2;
                            this.getParent().requestDisallowInterceptTouchEvent(true);
                            this.mTouchX = x2;
                            this.mTouchY = y;
                            return true;
                        }
                    }
                    return super.onTouchEvent(motionEvent);
                }
                if (actionMasked != 3) {
                    return super.onTouchEvent(motionEvent);
                }
            }
            if (this.mTouchMode == 2) {
                this.stopDrag(motionEvent);
                super.onTouchEvent(motionEvent);
                return true;
            }
            this.mTouchMode = 0;
            this.mVelocityTracker.clear();
        }
        else {
            final float x3 = motionEvent.getX();
            final float y2 = motionEvent.getY();
            if (this.isEnabled() && this.hitThumb(x3, y2)) {
                this.mTouchMode = 1;
                this.mTouchX = x3;
                this.mTouchY = y2;
            }
        }
        return super.onTouchEvent(motionEvent);
    }
    
    public void setChecked(final boolean checked) {
        super.setChecked(checked);
        final boolean checked2 = this.isChecked();
        if (this.getWindowToken() != null && ViewCompat.isLaidOut((View)this)) {
            this.animateThumbToCheckedState(checked2);
        }
        else {
            this.cancelPositionAnimator();
            float thumbPosition;
            if (checked2) {
                thumbPosition = 1.0f;
            }
            else {
                thumbPosition = 0.0f;
            }
            this.setThumbPosition(thumbPosition);
        }
    }
    
    public void setCustomSelectionActionModeCallback(final ActionMode$Callback actionMode$Callback) {
        super.setCustomSelectionActionModeCallback(TextViewCompat.wrapCustomSelectionActionModeCallback((TextView)this, actionMode$Callback));
    }
    
    public void setShowText(final boolean mShowText) {
        if (this.mShowText != mShowText) {
            this.mShowText = mShowText;
            this.requestLayout();
        }
    }
    
    public void setSplitTrack(final boolean mSplitTrack) {
        this.mSplitTrack = mSplitTrack;
        this.invalidate();
    }
    
    public void setSwitchMinWidth(final int mSwitchMinWidth) {
        this.mSwitchMinWidth = mSwitchMinWidth;
        this.requestLayout();
    }
    
    public void setSwitchPadding(final int mSwitchPadding) {
        this.mSwitchPadding = mSwitchPadding;
        this.requestLayout();
    }
    
    public void setSwitchTextAppearance(final Context context, int dimensionPixelSize) {
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, dimensionPixelSize, R.styleable.TextAppearance);
        final ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(R.styleable.TextAppearance_android_textColor);
        if (colorStateList != null) {
            this.mTextColors = colorStateList;
        }
        else {
            this.mTextColors = this.getTextColors();
        }
        dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, 0);
        if (dimensionPixelSize != 0 && dimensionPixelSize != this.mTextPaint.getTextSize()) {
            this.mTextPaint.setTextSize((float)dimensionPixelSize);
            this.requestLayout();
        }
        this.setSwitchTypefaceByIndex(obtainStyledAttributes.getInt(R.styleable.TextAppearance_android_typeface, -1), obtainStyledAttributes.getInt(R.styleable.TextAppearance_android_textStyle, -1));
        if (obtainStyledAttributes.getBoolean(R.styleable.TextAppearance_textAllCaps, false)) {
            this.mSwitchTransformationMethod = (TransformationMethod)new AllCapsTransformationMethod(this.getContext());
        }
        else {
            this.mSwitchTransformationMethod = null;
        }
        obtainStyledAttributes.recycle();
    }
    
    public void setSwitchTypeface(final Typeface typeface) {
        if ((this.mTextPaint.getTypeface() != null && !this.mTextPaint.getTypeface().equals((Object)typeface)) || (this.mTextPaint.getTypeface() == null && typeface != null)) {
            this.mTextPaint.setTypeface(typeface);
            this.requestLayout();
            this.invalidate();
        }
    }
    
    public void setSwitchTypeface(Typeface typeface, int n) {
        float textSkewX = 0.0f;
        boolean fakeBoldText = false;
        if (n > 0) {
            if (typeface == null) {
                typeface = Typeface.defaultFromStyle(n);
            }
            else {
                typeface = Typeface.create(typeface, n);
            }
            this.setSwitchTypeface(typeface);
            int style;
            if (typeface != null) {
                style = typeface.getStyle();
            }
            else {
                style = 0;
            }
            n &= ~style;
            final TextPaint mTextPaint = this.mTextPaint;
            if ((n & 0x1) != 0x0) {
                fakeBoldText = true;
            }
            mTextPaint.setFakeBoldText(fakeBoldText);
            final TextPaint mTextPaint2 = this.mTextPaint;
            if ((n & 0x2) != 0x0) {
                textSkewX = -0.25f;
            }
            mTextPaint2.setTextSkewX(textSkewX);
        }
        else {
            this.mTextPaint.setFakeBoldText(false);
            this.mTextPaint.setTextSkewX(0.0f);
            this.setSwitchTypeface(typeface);
        }
    }
    
    public void setTextOff(final CharSequence mTextOff) {
        this.mTextOff = mTextOff;
        this.requestLayout();
    }
    
    public void setTextOn(final CharSequence mTextOn) {
        this.mTextOn = mTextOn;
        this.requestLayout();
    }
    
    public void setThumbDrawable(final Drawable mThumbDrawable) {
        final Drawable mThumbDrawable2 = this.mThumbDrawable;
        if (mThumbDrawable2 != null) {
            mThumbDrawable2.setCallback((Drawable$Callback)null);
        }
        if ((this.mThumbDrawable = mThumbDrawable) != null) {
            mThumbDrawable.setCallback((Drawable$Callback)this);
        }
        this.requestLayout();
    }
    
    void setThumbPosition(final float mThumbPosition) {
        this.mThumbPosition = mThumbPosition;
        this.invalidate();
    }
    
    public void setThumbResource(final int n) {
        this.setThumbDrawable(AppCompatResources.getDrawable(this.getContext(), n));
    }
    
    public void setThumbTextPadding(final int mThumbTextPadding) {
        this.mThumbTextPadding = mThumbTextPadding;
        this.requestLayout();
    }
    
    public void setThumbTintList(final ColorStateList mThumbTintList) {
        this.mThumbTintList = mThumbTintList;
        this.mHasThumbTint = true;
        this.applyThumbTint();
    }
    
    public void setThumbTintMode(final PorterDuff$Mode mThumbTintMode) {
        this.mThumbTintMode = mThumbTintMode;
        this.mHasThumbTintMode = true;
        this.applyThumbTint();
    }
    
    public void setTrackDrawable(final Drawable mTrackDrawable) {
        final Drawable mTrackDrawable2 = this.mTrackDrawable;
        if (mTrackDrawable2 != null) {
            mTrackDrawable2.setCallback((Drawable$Callback)null);
        }
        if ((this.mTrackDrawable = mTrackDrawable) != null) {
            mTrackDrawable.setCallback((Drawable$Callback)this);
        }
        this.requestLayout();
    }
    
    public void setTrackResource(final int n) {
        this.setTrackDrawable(AppCompatResources.getDrawable(this.getContext(), n));
    }
    
    public void setTrackTintList(final ColorStateList mTrackTintList) {
        this.mTrackTintList = mTrackTintList;
        this.mHasTrackTint = true;
        this.applyTrackTint();
    }
    
    public void setTrackTintMode(final PorterDuff$Mode mTrackTintMode) {
        this.mTrackTintMode = mTrackTintMode;
        this.mHasTrackTintMode = true;
        this.applyTrackTint();
    }
    
    public void toggle() {
        this.setChecked(this.isChecked() ^ true);
    }
    
    protected boolean verifyDrawable(final Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.mThumbDrawable || drawable == this.mTrackDrawable;
    }
}
