// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.widget;

import android.os.Build$VERSION;

public interface AutoSizeableTextView
{
    public static final boolean PLATFORM_SUPPORTS_AUTOSIZE = Build$VERSION.SDK_INT >= 27;
    
    int getAutoSizeMaxTextSize();
    
    int getAutoSizeMinTextSize();
    
    int getAutoSizeStepGranularity();
    
    int[] getAutoSizeTextAvailableSizes();
    
    int getAutoSizeTextType();
    
    void setAutoSizeTextTypeUniformWithConfiguration(final int p0, final int p1, final int p2, final int p3) throws IllegalArgumentException;
    
    void setAutoSizeTextTypeUniformWithPresetSizes(final int[] p0, final int p1) throws IllegalArgumentException;
    
    void setAutoSizeTextTypeWithDefaults(final int p0);
}
