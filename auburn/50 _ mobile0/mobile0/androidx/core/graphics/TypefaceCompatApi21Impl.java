// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.graphics;

import android.content.ContentResolver;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import android.graphics.Typeface;
import androidx.core.provider.FontsContractCompat;
import android.os.CancellationSignal;
import android.content.Context;
import android.system.ErrnoException;
import android.system.OsConstants;
import android.system.Os;
import java.io.File;
import android.os.ParcelFileDescriptor;

class TypefaceCompatApi21Impl extends TypefaceCompatBaseImpl
{
    private static final String TAG = "TypefaceCompatApi21Impl";
    
    private File getFile(final ParcelFileDescriptor parcelFileDescriptor) {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("/proc/self/fd/");
            sb.append(parcelFileDescriptor.getFd());
            final String readlink = Os.readlink(sb.toString());
            if (OsConstants.S_ISREG(Os.stat(readlink).st_mode)) {
                return new File(readlink);
            }
            return null;
        }
        catch (ErrnoException ex) {
            return null;
        }
    }
    
    @Override
    public Typeface createFromFontInfo(final Context context, CancellationSignal openFileDescriptor, FontsContractCompat.FontInfo[] array, final int n) {
        if (array.length < 1) {
            return null;
        }
        final FontsContractCompat.FontInfo bestInfo = this.findBestInfo(array, n);
        final ContentResolver contentResolver = context.getContentResolver();
        try {
            openFileDescriptor = (CancellationSignal)contentResolver.openFileDescriptor(bestInfo.getUri(), "r", openFileDescriptor);
            try {
                final File file = this.getFile((ParcelFileDescriptor)openFileDescriptor);
                if (file != null && file.canRead()) {
                    final Typeface fromFile = Typeface.createFromFile(file);
                    if (openFileDescriptor != null) {
                        ((ParcelFileDescriptor)openFileDescriptor).close();
                    }
                    return fromFile;
                }
                array = (FontsContractCompat.FontInfo[])(Object)new FileInputStream(((ParcelFileDescriptor)openFileDescriptor).getFileDescriptor());
                try {
                    final Typeface fromInputStream = super.createFromInputStream(context, (InputStream)(Object)array);
                    ((FileInputStream)(Object)array).close();
                    if (openFileDescriptor != null) {
                        ((ParcelFileDescriptor)openFileDescriptor).close();
                    }
                    return fromInputStream;
                }
                finally {
                    try {}
                    finally {
                        try {
                            ((FileInputStream)(Object)array).close();
                        }
                        finally {
                            final Throwable t;
                            final Throwable exception;
                            t.addSuppressed(exception);
                        }
                    }
                }
            }
            finally {
                try {}
                finally {
                    if (openFileDescriptor != null) {
                        try {
                            ((ParcelFileDescriptor)openFileDescriptor).close();
                        }
                        finally {
                            final Throwable exception2;
                            ((Throwable)context).addSuppressed(exception2);
                        }
                    }
                }
            }
        }
        catch (IOException ex) {
            return null;
        }
    }
}
