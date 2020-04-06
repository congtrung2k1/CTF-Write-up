// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.widget;

import androidx.core.widget.TextViewCompat;
import android.view.ActionMode$Callback;
import android.content.res.TypedArray;
import android.text.Layout;
import androidx.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.TextView;

public class DialogTitle extends TextView
{
    public DialogTitle(final Context context) {
        super(context);
    }
    
    public DialogTitle(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public DialogTitle(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
        final Layout layout = this.getLayout();
        if (layout != null) {
            final int lineCount = layout.getLineCount();
            if (lineCount > 0 && layout.getEllipsisCount(lineCount - 1) > 0) {
                this.setSingleLine(false);
                this.setMaxLines(2);
                final TypedArray obtainStyledAttributes = this.getContext().obtainStyledAttributes((AttributeSet)null, R.styleable.TextAppearance, 16842817, 16973892);
                final int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, 0);
                if (dimensionPixelSize != 0) {
                    this.setTextSize(0, (float)dimensionPixelSize);
                }
                obtainStyledAttributes.recycle();
                super.onMeasure(n, n2);
            }
        }
    }
    
    public void setCustomSelectionActionModeCallback(final ActionMode$Callback actionMode$Callback) {
        super.setCustomSelectionActionModeCallback(TextViewCompat.wrapCustomSelectionActionModeCallback(this, actionMode$Callback));
    }
}
