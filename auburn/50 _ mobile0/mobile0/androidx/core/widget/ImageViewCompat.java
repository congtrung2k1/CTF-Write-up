// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.widget;

import android.graphics.drawable.Drawable;
import android.graphics.PorterDuff$Mode;
import android.os.Build$VERSION;
import android.content.res.ColorStateList;
import android.widget.ImageView;

public class ImageViewCompat
{
    private ImageViewCompat() {
    }
    
    public static ColorStateList getImageTintList(final ImageView imageView) {
        if (Build$VERSION.SDK_INT >= 21) {
            return imageView.getImageTintList();
        }
        ColorStateList supportImageTintList;
        if (imageView instanceof TintableImageSourceView) {
            supportImageTintList = ((TintableImageSourceView)imageView).getSupportImageTintList();
        }
        else {
            supportImageTintList = null;
        }
        return supportImageTintList;
    }
    
    public static PorterDuff$Mode getImageTintMode(final ImageView imageView) {
        if (Build$VERSION.SDK_INT >= 21) {
            return imageView.getImageTintMode();
        }
        PorterDuff$Mode supportImageTintMode;
        if (imageView instanceof TintableImageSourceView) {
            supportImageTintMode = ((TintableImageSourceView)imageView).getSupportImageTintMode();
        }
        else {
            supportImageTintMode = null;
        }
        return supportImageTintMode;
    }
    
    public static void setImageTintList(final ImageView imageView, final ColorStateList list) {
        if (Build$VERSION.SDK_INT >= 21) {
            imageView.setImageTintList(list);
            if (Build$VERSION.SDK_INT == 21) {
                final Drawable drawable = imageView.getDrawable();
                final boolean b = imageView.getImageTintList() != null && imageView.getImageTintMode() != null;
                if (drawable != null && b) {
                    if (drawable.isStateful()) {
                        drawable.setState(imageView.getDrawableState());
                    }
                    imageView.setImageDrawable(drawable);
                }
            }
        }
        else if (imageView instanceof TintableImageSourceView) {
            ((TintableImageSourceView)imageView).setSupportImageTintList(list);
        }
    }
    
    public static void setImageTintMode(final ImageView imageView, final PorterDuff$Mode porterDuff$Mode) {
        if (Build$VERSION.SDK_INT >= 21) {
            imageView.setImageTintMode(porterDuff$Mode);
            if (Build$VERSION.SDK_INT == 21) {
                final Drawable drawable = imageView.getDrawable();
                final boolean b = imageView.getImageTintList() != null && imageView.getImageTintMode() != null;
                if (drawable != null && b) {
                    if (drawable.isStateful()) {
                        drawable.setState(imageView.getDrawableState());
                    }
                    imageView.setImageDrawable(drawable);
                }
            }
        }
        else if (imageView instanceof TintableImageSourceView) {
            ((TintableImageSourceView)imageView).setSupportImageTintMode(porterDuff$Mode);
        }
    }
}
