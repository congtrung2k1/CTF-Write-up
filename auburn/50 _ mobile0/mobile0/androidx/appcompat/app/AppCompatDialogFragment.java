// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.app;

import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

public class AppCompatDialogFragment extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(final Bundle bundle) {
        return new AppCompatDialog(this.getContext(), this.getTheme());
    }
    
    @Override
    public void setupDialog(final Dialog dialog, final int n) {
        if (dialog instanceof AppCompatDialog) {
            final AppCompatDialog appCompatDialog = (AppCompatDialog)dialog;
            if (n != 1 && n != 2) {
                if (n != 3) {
                    return;
                }
                dialog.getWindow().addFlags(24);
            }
            appCompatDialog.supportRequestWindowFeature(1);
        }
        else {
            super.setupDialog(dialog, n);
        }
    }
}
