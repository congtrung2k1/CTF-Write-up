// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.widget;

import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;

public interface TintableCompoundButton
{
    ColorStateList getSupportButtonTintList();
    
    PorterDuff$Mode getSupportButtonTintMode();
    
    void setSupportButtonTintList(final ColorStateList p0);
    
    void setSupportButtonTintMode(final PorterDuff$Mode p0);
}
