// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.graphics.drawable;

import android.util.StateSet;
import java.io.IOException;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build$VERSION;
import org.xmlpull.v1.XmlPullParserException;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.TypedArrayUtils;
import androidx.appcompat.R;
import android.content.res.Resources$Theme;
import android.util.AttributeSet;
import org.xmlpull.v1.XmlPullParser;
import android.content.Context;
import android.content.res.Resources;

class StateListDrawable extends DrawableContainer
{
    private static final boolean DEBUG = false;
    private static final String TAG = "StateListDrawable";
    private boolean mMutated;
    private StateListState mStateListState;
    
    StateListDrawable() {
        this(null, null);
    }
    
    StateListDrawable(final StateListState constantState) {
        if (constantState != null) {
            this.setConstantState(constantState);
        }
    }
    
    StateListDrawable(final StateListState stateListState, final Resources resources) {
        this.setConstantState(new StateListState(stateListState, this, resources));
        this.onStateChange(this.getState());
    }
    
    private void inflateChildElements(final Context context, final Resources resources, final XmlPullParser xmlPullParser, final AttributeSet set, final Resources$Theme resources$Theme) throws XmlPullParserException, IOException {
        final StateListState mStateListState = this.mStateListState;
        final int n = xmlPullParser.getDepth() + 1;
        while (true) {
            final int next = xmlPullParser.next();
            if (next == 1) {
                break;
            }
            final int depth = xmlPullParser.getDepth();
            if (depth < n && next == 3) {
                break;
            }
            if (next != 2) {
                continue;
            }
            if (depth > n) {
                continue;
            }
            if (!xmlPullParser.getName().equals("item")) {
                continue;
            }
            final TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, R.styleable.StateListDrawableItem);
            Drawable drawable = null;
            final int resourceId = obtainAttributes.getResourceId(R.styleable.StateListDrawableItem_android_drawable, -1);
            if (resourceId > 0) {
                drawable = AppCompatResources.getDrawable(context, resourceId);
            }
            obtainAttributes.recycle();
            final int[] stateSet = this.extractStateSet(set);
            Drawable drawable2;
            if ((drawable2 = drawable) == null) {
                int next2;
                do {
                    next2 = xmlPullParser.next();
                } while (next2 == 4);
                if (next2 != 2) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(xmlPullParser.getPositionDescription());
                    sb.append(": <item> tag requires a 'drawable' attribute or ");
                    sb.append("child tag defining a drawable");
                    throw new XmlPullParserException(sb.toString());
                }
                if (Build$VERSION.SDK_INT >= 21) {
                    drawable2 = Drawable.createFromXmlInner(resources, xmlPullParser, set, resources$Theme);
                }
                else {
                    drawable2 = Drawable.createFromXmlInner(resources, xmlPullParser, set);
                }
            }
            mStateListState.addStateSet(stateSet, drawable2);
        }
    }
    
    private void updateStateFromTypedArray(final TypedArray typedArray) {
        final StateListState mStateListState = this.mStateListState;
        if (Build$VERSION.SDK_INT >= 21) {
            mStateListState.mChangingConfigurations |= typedArray.getChangingConfigurations();
        }
        mStateListState.mVariablePadding = typedArray.getBoolean(R.styleable.StateListDrawable_android_variablePadding, mStateListState.mVariablePadding);
        mStateListState.mConstantSize = typedArray.getBoolean(R.styleable.StateListDrawable_android_constantSize, mStateListState.mConstantSize);
        mStateListState.mEnterFadeDuration = typedArray.getInt(R.styleable.StateListDrawable_android_enterFadeDuration, mStateListState.mEnterFadeDuration);
        mStateListState.mExitFadeDuration = typedArray.getInt(R.styleable.StateListDrawable_android_exitFadeDuration, mStateListState.mExitFadeDuration);
        mStateListState.mDither = typedArray.getBoolean(R.styleable.StateListDrawable_android_dither, mStateListState.mDither);
    }
    
    public void addState(final int[] array, final Drawable drawable) {
        if (drawable != null) {
            this.mStateListState.addStateSet(array, drawable);
            this.onStateChange(this.getState());
        }
    }
    
    @Override
    public void applyTheme(final Resources$Theme resources$Theme) {
        super.applyTheme(resources$Theme);
        this.onStateChange(this.getState());
    }
    
    @Override
    void clearMutated() {
        super.clearMutated();
        this.mMutated = false;
    }
    
    StateListState cloneConstantState() {
        return new StateListState(this.mStateListState, this, null);
    }
    
    int[] extractStateSet(final AttributeSet set) {
        int n = 0;
        final int attributeCount = set.getAttributeCount();
        final int[] array = new int[attributeCount];
        for (int i = 0; i < attributeCount; ++i) {
            int attributeNameResource = set.getAttributeNameResource(i);
            if (attributeNameResource != 0) {
                if (attributeNameResource != 16842960 && attributeNameResource != 16843161) {
                    if (!set.getAttributeBooleanValue(i, false)) {
                        attributeNameResource = -attributeNameResource;
                    }
                    array[n] = attributeNameResource;
                    ++n;
                }
            }
        }
        return StateSet.trimStateSet(array, n);
    }
    
    int getStateCount() {
        return ((DrawableContainerState)this.mStateListState).getChildCount();
    }
    
    Drawable getStateDrawable(final int n) {
        return ((DrawableContainerState)this.mStateListState).getChild(n);
    }
    
    int getStateDrawableIndex(final int[] array) {
        return this.mStateListState.indexOfStateSet(array);
    }
    
    StateListState getStateListState() {
        return this.mStateListState;
    }
    
    int[] getStateSet(final int n) {
        return this.mStateListState.mStateSets[n];
    }
    
    public void inflate(final Context context, final Resources resources, final XmlPullParser xmlPullParser, final AttributeSet set, final Resources$Theme resources$Theme) throws XmlPullParserException, IOException {
        final TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, R.styleable.StateListDrawable);
        this.setVisible(obtainAttributes.getBoolean(R.styleable.StateListDrawable_android_visible, true), true);
        this.updateStateFromTypedArray(obtainAttributes);
        this.updateDensity(resources);
        obtainAttributes.recycle();
        this.inflateChildElements(context, resources, xmlPullParser, set, resources$Theme);
        this.onStateChange(this.getState());
    }
    
    @Override
    public boolean isStateful() {
        return true;
    }
    
    @Override
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mStateListState.mutate();
            this.mMutated = true;
        }
        return this;
    }
    
    @Override
    protected boolean onStateChange(final int[] array) {
        final boolean onStateChange = super.onStateChange(array);
        int n;
        if ((n = this.mStateListState.indexOfStateSet(array)) < 0) {
            n = this.mStateListState.indexOfStateSet(StateSet.WILD_CARD);
        }
        return this.selectDrawable(n) || onStateChange;
    }
    
    @Override
    protected void setConstantState(final DrawableContainerState constantState) {
        super.setConstantState(constantState);
        if (constantState instanceof StateListState) {
            this.mStateListState = (StateListState)constantState;
        }
    }
    
    static class StateListState extends DrawableContainerState
    {
        int[][] mStateSets;
        
        StateListState(final StateListState stateListState, final StateListDrawable stateListDrawable, final Resources resources) {
            super((DrawableContainerState)stateListState, stateListDrawable, resources);
            if (stateListState != null) {
                this.mStateSets = stateListState.mStateSets;
            }
            else {
                this.mStateSets = new int[((DrawableContainerState)this).getCapacity()][];
            }
        }
        
        int addStateSet(final int[] array, final Drawable drawable) {
            final int addChild = ((DrawableContainerState)this).addChild(drawable);
            this.mStateSets[addChild] = array;
            return addChild;
        }
        
        @Override
        public void growArray(final int n, final int n2) {
            super.growArray(n, n2);
            final int[][] mStateSets = new int[n2][];
            System.arraycopy(this.mStateSets, 0, mStateSets, 0, n);
            this.mStateSets = mStateSets;
        }
        
        int indexOfStateSet(final int[] array) {
            final int[][] mStateSets = this.mStateSets;
            for (int childCount = ((DrawableContainerState)this).getChildCount(), i = 0; i < childCount; ++i) {
                if (StateSet.stateSetMatches(mStateSets[i], array)) {
                    return i;
                }
            }
            return -1;
        }
        
        @Override
        void mutate() {
            final int[][] mStateSets = this.mStateSets;
            final int[][] mStateSets2 = new int[mStateSets.length][];
            for (int i = mStateSets.length - 1; i >= 0; --i) {
                final int[][] mStateSets3 = this.mStateSets;
                int[] array;
                if (mStateSets3[i] != null) {
                    array = mStateSets3[i].clone();
                }
                else {
                    array = null;
                }
                mStateSets2[i] = array;
            }
            this.mStateSets = mStateSets2;
        }
        
        public Drawable newDrawable() {
            return new StateListDrawable(this, null);
        }
        
        public Drawable newDrawable(final Resources resources) {
            return new StateListDrawable(this, resources);
        }
    }
}
