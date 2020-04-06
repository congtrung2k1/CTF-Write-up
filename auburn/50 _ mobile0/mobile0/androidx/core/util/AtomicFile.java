// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.util;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import android.util.Log;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.File;

public class AtomicFile
{
    private final File mBackupName;
    private final File mBaseName;
    
    public AtomicFile(final File mBaseName) {
        this.mBaseName = mBaseName;
        final StringBuilder sb = new StringBuilder();
        sb.append(mBaseName.getPath());
        sb.append(".bak");
        this.mBackupName = new File(sb.toString());
    }
    
    private static boolean sync(final FileOutputStream fileOutputStream) {
        try {
            fileOutputStream.getFD().sync();
            return true;
        }
        catch (IOException ex) {
            return false;
        }
    }
    
    public void delete() {
        this.mBaseName.delete();
        this.mBackupName.delete();
    }
    
    public void failWrite(final FileOutputStream fileOutputStream) {
        if (fileOutputStream != null) {
            sync(fileOutputStream);
            try {
                fileOutputStream.close();
                this.mBaseName.delete();
                this.mBackupName.renameTo(this.mBaseName);
            }
            catch (IOException ex) {
                Log.w("AtomicFile", "failWrite: Got exception:", (Throwable)ex);
            }
        }
    }
    
    public void finishWrite(final FileOutputStream fileOutputStream) {
        if (fileOutputStream != null) {
            sync(fileOutputStream);
            try {
                fileOutputStream.close();
                this.mBackupName.delete();
            }
            catch (IOException ex) {
                Log.w("AtomicFile", "finishWrite: Got exception:", (Throwable)ex);
            }
        }
    }
    
    public File getBaseFile() {
        return this.mBaseName;
    }
    
    public FileInputStream openRead() throws FileNotFoundException {
        if (this.mBackupName.exists()) {
            this.mBaseName.delete();
            this.mBackupName.renameTo(this.mBaseName);
        }
        return new FileInputStream(this.mBaseName);
    }
    
    public byte[] readFully() throws IOException {
        final FileInputStream openRead = this.openRead();
        int off = 0;
        try {
            byte[] b = new byte[openRead.available()];
            while (true) {
                final int read = openRead.read(b, off, b.length - off);
                if (read <= 0) {
                    break;
                }
                off += read;
                final int available = openRead.available();
                byte[] array = b;
                if (available > b.length - off) {
                    array = new byte[off + available];
                    System.arraycopy(b, 0, array, 0, off);
                }
                b = array;
            }
            openRead.close();
            return b;
        }
        finally {
            openRead.close();
            while (true) {}
        }
    }
    
    public FileOutputStream startWrite() throws IOException {
        if (this.mBaseName.exists()) {
            if (!this.mBackupName.exists()) {
                if (!this.mBaseName.renameTo(this.mBackupName)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Couldn't rename file ");
                    sb.append(this.mBaseName);
                    sb.append(" to backup file ");
                    sb.append(this.mBackupName);
                    Log.w("AtomicFile", sb.toString());
                }
            }
            else {
                this.mBaseName.delete();
            }
        }
        try {
            return new FileOutputStream(this.mBaseName);
        }
        catch (FileNotFoundException ex) {
            if (this.mBaseName.getParentFile().mkdirs()) {
                try {
                    return new FileOutputStream(this.mBaseName);
                }
                catch (FileNotFoundException ex2) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Couldn't create ");
                    sb2.append(this.mBaseName);
                    throw new IOException(sb2.toString());
                }
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Couldn't create directory ");
            sb3.append(this.mBaseName);
            throw new IOException(sb3.toString());
        }
    }
}
