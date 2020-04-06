// 
// Decompiled by Procyon v0.5.36
// 

package androidx.documentfile.provider;

import android.database.Cursor;
import android.content.ContentResolver;
import java.util.ArrayList;
import android.provider.DocumentsContract;
import android.net.Uri;
import android.content.Context;

class TreeDocumentFile extends DocumentFile
{
    private Context mContext;
    private Uri mUri;
    
    TreeDocumentFile(final DocumentFile documentFile, final Context mContext, final Uri mUri) {
        super(documentFile);
        this.mContext = mContext;
        this.mUri = mUri;
    }
    
    private static void closeQuietly(final AutoCloseable autoCloseable) {
        if (autoCloseable != null) {
            try {
                autoCloseable.close();
            }
            catch (Exception ex2) {}
            catch (RuntimeException ex) {
                throw ex;
            }
        }
    }
    
    private static Uri createFile(final Context context, final Uri uri, final String s, final String s2) {
        try {
            return DocumentsContract.createDocument(context.getContentResolver(), uri, s, s2);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    @Override
    public boolean canRead() {
        return DocumentsContractApi19.canRead(this.mContext, this.mUri);
    }
    
    @Override
    public boolean canWrite() {
        return DocumentsContractApi19.canWrite(this.mContext, this.mUri);
    }
    
    @Override
    public DocumentFile createDirectory(final String s) {
        final Uri file = createFile(this.mContext, this.mUri, "vnd.android.document/directory", s);
        TreeDocumentFile treeDocumentFile;
        if (file != null) {
            treeDocumentFile = new TreeDocumentFile(this, this.mContext, file);
        }
        else {
            treeDocumentFile = null;
        }
        return treeDocumentFile;
    }
    
    @Override
    public DocumentFile createFile(final String s, final String s2) {
        final Uri file = createFile(this.mContext, this.mUri, s, s2);
        TreeDocumentFile treeDocumentFile;
        if (file != null) {
            treeDocumentFile = new TreeDocumentFile(this, this.mContext, file);
        }
        else {
            treeDocumentFile = null;
        }
        return treeDocumentFile;
    }
    
    @Override
    public boolean delete() {
        try {
            return DocumentsContract.deleteDocument(this.mContext.getContentResolver(), this.mUri);
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    @Override
    public boolean exists() {
        return DocumentsContractApi19.exists(this.mContext, this.mUri);
    }
    
    @Override
    public String getName() {
        return DocumentsContractApi19.getName(this.mContext, this.mUri);
    }
    
    @Override
    public String getType() {
        return DocumentsContractApi19.getType(this.mContext, this.mUri);
    }
    
    @Override
    public Uri getUri() {
        return this.mUri;
    }
    
    @Override
    public boolean isDirectory() {
        return DocumentsContractApi19.isDirectory(this.mContext, this.mUri);
    }
    
    @Override
    public boolean isFile() {
        return DocumentsContractApi19.isFile(this.mContext, this.mUri);
    }
    
    @Override
    public boolean isVirtual() {
        return DocumentsContractApi19.isVirtual(this.mContext, this.mUri);
    }
    
    @Override
    public long lastModified() {
        return DocumentsContractApi19.lastModified(this.mContext, this.mUri);
    }
    
    @Override
    public long length() {
        return DocumentsContractApi19.length(this.mContext, this.mUri);
    }
    
    @Override
    public DocumentFile[] listFiles() {
        final ContentResolver contentResolver = this.mContext.getContentResolver();
        final Uri mUri = this.mUri;
        final Uri buildChildDocumentsUriUsingTree = DocumentsContract.buildChildDocumentsUriUsingTree(mUri, DocumentsContract.getDocumentId(mUri));
        final ArrayList<Uri> list = new ArrayList<Uri>();
        Object o = null;
        try {
            try {
                final Cursor query = contentResolver.query(buildChildDocumentsUriUsingTree, new String[] { "document_id" }, (String)null, (String[])null, (String)null);
                while (true) {
                    o = query;
                    if (!query.moveToNext()) {
                        break;
                    }
                    o = query;
                    final String string = query.getString(0);
                    o = query;
                    list.add(DocumentsContract.buildDocumentUriUsingTree(this.mUri, string));
                }
                closeQuietly((AutoCloseable)query);
            }
            finally {
                closeQuietly((AutoCloseable)o);
                while (true) {}
                final Uri[] array = list.toArray(new Uri[list.size()]);
                final DocumentFile[] array2 = new DocumentFile[array.length];
                int n = 0;
                Label_0193: {
                    break Label_0193;
                    array2[n] = new TreeDocumentFile(this, this.mContext, array[n]);
                    ++n;
                    break Label_0193;
                    Label_0227: {
                        return array2;
                    }
                }
            }
            // iftrue(Label_0227:, n >= array.length)
        }
        catch (Exception ex) {}
    }
    
    @Override
    public boolean renameTo(final String s) {
        try {
            final Uri renameDocument = DocumentsContract.renameDocument(this.mContext.getContentResolver(), this.mUri, s);
            if (renameDocument != null) {
                this.mUri = renameDocument;
                return true;
            }
            return false;
        }
        catch (Exception ex) {
            return false;
        }
    }
}
