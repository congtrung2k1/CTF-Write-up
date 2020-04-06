// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.content.res;

import android.os.Build$VERSION;
import android.content.res.Resources;

public final class ConfigurationHelper
{
    private ConfigurationHelper() {
    }
    
    public static int getDensityDpi(final Resources resources) {
        if (Build$VERSION.SDK_INT >= 17) {
            return resources.getConfiguration().densityDpi;
        }
        return resources.getDisplayMetrics().densityDpi;
    }
}
