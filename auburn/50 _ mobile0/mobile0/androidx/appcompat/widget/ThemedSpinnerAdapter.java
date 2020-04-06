// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.widget;

import androidx.appcompat.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.content.Context;
import android.content.res.Resources$Theme;
import android.widget.SpinnerAdapter;

public interface ThemedSpinnerAdapter extends SpinnerAdapter
{
    Resources$Theme getDropDownViewTheme();
    
    void setDropDownViewTheme(final Resources$Theme p0);
    
    public static final class Helper
    {
        private final Context mContext;
        private LayoutInflater mDropDownInflater;
        private final LayoutInflater mInflater;
        
        public Helper(final Context mContext) {
            this.mContext = mContext;
            this.mInflater = LayoutInflater.from(mContext);
        }
        
        public LayoutInflater getDropDownViewInflater() {
            LayoutInflater layoutInflater = this.mDropDownInflater;
            if (layoutInflater == null) {
                layoutInflater = this.mInflater;
            }
            return layoutInflater;
        }
        
        public Resources$Theme getDropDownViewTheme() {
            final LayoutInflater mDropDownInflater = this.mDropDownInflater;
            Resources$Theme theme;
            if (mDropDownInflater == null) {
                theme = null;
            }
            else {
                theme = mDropDownInflater.getContext().getTheme();
            }
            return theme;
        }
        
        public void setDropDownViewTheme(final Resources$Theme resources$Theme) {
            if (resources$Theme == null) {
                this.mDropDownInflater = null;
            }
            else if (resources$Theme == this.mContext.getTheme()) {
                this.mDropDownInflater = this.mInflater;
            }
            else {
                this.mDropDownInflater = LayoutInflater.from((Context)new ContextThemeWrapper(this.mContext, resources$Theme));
            }
        }
    }
}
