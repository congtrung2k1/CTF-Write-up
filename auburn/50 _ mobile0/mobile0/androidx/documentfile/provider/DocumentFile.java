// 
// Decompiled by Procyon v0.5.36
// 

package androidx.documentfile.provider;

import android.provider.DocumentsContract;
import android.os.Build$VERSION;
import android.net.Uri;
import android.content.Context;
import java.io.File;

public abstract class DocumentFile
{
    static final String TAG = "DocumentFile";
    private final DocumentFile mParent;
    
    DocumentFile(final DocumentFile mParent) {
        this.mParent = mParent;
    }
    
    public static DocumentFile fromFile(final File file) {
        return new RawDocumentFile(null, file);
    }
    
    public static DocumentFile fromSingleUri(final Context context, final Uri uri) {
        if (Build$VERSION.SDK_INT >= 19) {
            return new SingleDocumentFile(null, context, uri);
        }
        return null;
    }
    
    public static DocumentFile fromTreeUri(final Context context, final Uri uri) {
        if (Build$VERSION.SDK_INT >= 21) {
            return new TreeDocumentFile(null, context, DocumentsContract.buildDocumentUriUsingTree(uri, DocumentsContract.getTreeDocumentId(uri)));
        }
        return null;
    }
    
    public static boolean isDocumentUri(final Context context, final Uri uri) {
        return Build$VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context, uri);
    }
    
    public abstract boolean canRead();
    
    public abstract boolean canWrite();
    
    public abstract DocumentFile createDirectory(final String p0);
    
    public abstract DocumentFile createFile(final String p0, final String p1);
    
    public abstract boolean delete();
    
    public abstract boolean exists();
    
    public DocumentFile findFile(final String s) {
        for (final DocumentFile documentFile : this.listFiles()) {
            if (s.equals(documentFile.getName())) {
                return documentFile;
            }
        }
        return null;
    }
    
    public abstract String getName();
    
    public DocumentFile getParentFile() {
        return this.mParent;
    }
    
    public abstract String getType();
    
    public abstract Uri getUri();
    
    public abstract boolean isDirectory();
    
    public abstract boolean isFile();
    
    public abstract boolean isVirtual();
    
    public abstract long lastModified();
    
    public abstract long length();
    
    public abstract DocumentFile[] listFiles();
    
    public abstract boolean renameTo(final String p0);
}
