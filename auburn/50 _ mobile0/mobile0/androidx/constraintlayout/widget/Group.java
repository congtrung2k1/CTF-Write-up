// 
// Decompiled by Procyon v0.5.36
// 

package androidx.constraintlayout.widget;

import android.view.View;
import android.os.Build$VERSION;
import android.util.AttributeSet;
import android.content.Context;

public class Group extends ConstraintHelper
{
    public Group(final Context context) {
        super(context);
    }
    
    public Group(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public Group(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    @Override
    protected void init(final AttributeSet set) {
        super.init(set);
        this.mUseViewMeasure = false;
    }
    
    @Override
    public void updatePostLayout(final ConstraintLayout constraintLayout) {
        final ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)this.getLayoutParams();
        layoutParams.widget.setWidth(0);
        layoutParams.widget.setHeight(0);
    }
    
    @Override
    public void updatePreLayout(final ConstraintLayout constraintLayout) {
        final int visibility = this.getVisibility();
        float elevation = 0.0f;
        if (Build$VERSION.SDK_INT >= 21) {
            elevation = this.getElevation();
        }
        for (int i = 0; i < this.mCount; ++i) {
            final View viewById = constraintLayout.getViewById(this.mIds[i]);
            if (viewById != null) {
                viewById.setVisibility(visibility);
                if (elevation > 0.0f && Build$VERSION.SDK_INT >= 21) {
                    viewById.setElevation(elevation);
                }
            }
        }
    }
}
