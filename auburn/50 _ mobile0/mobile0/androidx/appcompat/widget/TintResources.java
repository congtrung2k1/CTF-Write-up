// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.widget;

import android.content.res.Resources$NotFoundException;
import android.graphics.drawable.Drawable;
import android.content.res.Resources;
import android.content.Context;
import java.lang.ref.WeakReference;

class TintResources extends ResourcesWrapper
{
    private final WeakReference<Context> mContextRef;
    
    public TintResources(final Context referent, final Resources resources) {
        super(resources);
        this.mContextRef = new WeakReference<Context>(referent);
    }
    
    @Override
    public Drawable getDrawable(final int n) throws Resources$NotFoundException {
        final Drawable drawable = super.getDrawable(n);
        final Context context = this.mContextRef.get();
        if (drawable != null && context != null) {
            AppCompatDrawableManager.get();
            AppCompatDrawableManager.tintDrawableUsingColorFilter(context, n, drawable);
        }
        return drawable;
    }
}
