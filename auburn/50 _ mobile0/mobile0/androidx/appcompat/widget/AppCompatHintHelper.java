// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.widget;

import android.view.ViewParent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

class AppCompatHintHelper
{
    private AppCompatHintHelper() {
    }
    
    static InputConnection onCreateInputConnection(final InputConnection inputConnection, final EditorInfo editorInfo, final View view) {
        if (inputConnection != null && editorInfo.hintText == null) {
            for (ViewParent viewParent = view.getParent(); viewParent instanceof View; viewParent = viewParent.getParent()) {
                if (viewParent instanceof WithHint) {
                    editorInfo.hintText = ((WithHint)viewParent).getHint();
                    break;
                }
            }
        }
        return inputConnection;
    }
}
