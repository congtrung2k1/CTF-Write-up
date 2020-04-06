// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.view.inputmethod;

import android.net.Uri;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.view.inputmethod.InputConnectionWrapper;
import android.content.ClipDescription;
import android.os.Parcelable;
import android.view.inputmethod.InputContentInfo;
import android.os.Build$VERSION;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

public final class InputConnectionCompat
{
    private static final String COMMIT_CONTENT_ACTION = "androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
    private static final String COMMIT_CONTENT_CONTENT_URI_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_URI";
    private static final String COMMIT_CONTENT_DESCRIPTION_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
    private static final String COMMIT_CONTENT_FLAGS_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
    private static final String COMMIT_CONTENT_LINK_URI_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
    private static final String COMMIT_CONTENT_OPTS_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
    private static final String COMMIT_CONTENT_RESULT_RECEIVER = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
    public static final int INPUT_CONTENT_GRANT_READ_URI_PERMISSION = 1;
    
    @Deprecated
    public InputConnectionCompat() {
    }
    
    public static boolean commitContent(final InputConnection inputConnection, final EditorInfo editorInfo, final InputContentInfoCompat inputContentInfoCompat, final int n, final Bundle bundle) {
        final ClipDescription description = inputContentInfoCompat.getDescription();
        final int n2 = 0;
        final String[] contentMimeTypes = EditorInfoCompat.getContentMimeTypes(editorInfo);
        final int length = contentMimeTypes.length;
        int n3 = 0;
        int n4;
        while (true) {
            n4 = n2;
            if (n3 >= length) {
                break;
            }
            if (description.hasMimeType(contentMimeTypes[n3])) {
                n4 = 1;
                break;
            }
            ++n3;
        }
        if (n4 == 0) {
            return false;
        }
        if (Build$VERSION.SDK_INT >= 25) {
            return inputConnection.commitContent((InputContentInfo)inputContentInfoCompat.unwrap(), n, bundle);
        }
        final Bundle bundle2 = new Bundle();
        bundle2.putParcelable("androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_URI", (Parcelable)inputContentInfoCompat.getContentUri());
        bundle2.putParcelable("androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION", (Parcelable)inputContentInfoCompat.getDescription());
        bundle2.putParcelable("androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI", (Parcelable)inputContentInfoCompat.getLinkUri());
        bundle2.putInt("androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS", n);
        bundle2.putParcelable("androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_OPTS", (Parcelable)bundle);
        return inputConnection.performPrivateCommand("androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT", bundle2);
    }
    
    public static InputConnection createWrapper(final InputConnection inputConnection, final EditorInfo editorInfo, final OnCommitContentListener onCommitContentListener) {
        if (inputConnection == null) {
            throw new IllegalArgumentException("inputConnection must be non-null");
        }
        if (editorInfo == null) {
            throw new IllegalArgumentException("editorInfo must be non-null");
        }
        if (onCommitContentListener == null) {
            throw new IllegalArgumentException("onCommitContentListener must be non-null");
        }
        if (Build$VERSION.SDK_INT >= 25) {
            return (InputConnection)new InputConnectionWrapper(inputConnection, false) {
                public boolean commitContent(final InputContentInfo inputContentInfo, final int n, final Bundle bundle) {
                    return onCommitContentListener.onCommitContent(InputContentInfoCompat.wrap(inputContentInfo), n, bundle) || super.commitContent(inputContentInfo, n, bundle);
                }
            };
        }
        if (EditorInfoCompat.getContentMimeTypes(editorInfo).length == 0) {
            return inputConnection;
        }
        return (InputConnection)new InputConnectionWrapper(inputConnection, false) {
            public boolean performPrivateCommand(final String s, final Bundle bundle) {
                return InputConnectionCompat.handlePerformPrivateCommand(s, bundle, onCommitContentListener) || super.performPrivateCommand(s, bundle);
            }
        };
    }
    
    static boolean handlePerformPrivateCommand(String s, final Bundle bundle, final OnCommitContentListener onCommitContentListener) {
        final boolean equals = TextUtils.equals((CharSequence)"androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT", (CharSequence)s);
        int n = 0;
        if (!equals) {
            return false;
        }
        if (bundle == null) {
            return false;
        }
        s = null;
        try {
            final Object o = s = (String)bundle.getParcelable("androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER");
            final Uri uri = (Uri)bundle.getParcelable("androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_URI");
            s = (String)o;
            final ClipDescription clipDescription = (ClipDescription)bundle.getParcelable("androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION");
            s = (String)o;
            final Uri uri2 = (Uri)bundle.getParcelable("androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI");
            s = (String)o;
            final int int1 = bundle.getInt("androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS");
            s = (String)o;
            final Bundle bundle2 = (Bundle)bundle.getParcelable("androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_OPTS");
            s = (String)o;
            s = (String)o;
            final InputContentInfoCompat inputContentInfoCompat = new InputContentInfoCompat(uri, clipDescription, uri2);
            s = (String)o;
            final boolean onCommitContent = onCommitContentListener.onCommitContent(inputContentInfoCompat, int1, bundle2);
            if (o != null) {
                if (onCommitContent) {
                    n = 1;
                }
                ((ResultReceiver)o).send(n, (Bundle)null);
            }
            return onCommitContent;
        }
        finally {
            if (s != null) {
                ((ResultReceiver)s).send(0, (Bundle)null);
            }
        }
    }
    
    public interface OnCommitContentListener
    {
        boolean onCommitContent(final InputContentInfoCompat p0, final int p1, final Bundle p2);
    }
}
