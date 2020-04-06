// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.graphics;

import android.os.ParcelFileDescriptor;
import java.nio.MappedByteBuffer;
import android.content.ContentResolver;
import java.nio.channels.FileChannel;
import java.io.FileInputStream;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Process;
import android.os.StrictMode$ThreadPolicy;
import java.io.FileOutputStream;
import android.os.StrictMode;
import java.io.InputStream;
import java.io.File;
import java.nio.ByteBuffer;
import android.content.res.Resources;
import android.content.Context;
import java.io.IOException;
import java.io.Closeable;

public class TypefaceCompatUtil
{
    private static final String CACHE_FILE_PREFIX = ".font";
    private static final String TAG = "TypefaceCompatUtil";
    
    private TypefaceCompatUtil() {
    }
    
    public static void closeQuietly(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            }
            catch (IOException ex) {}
        }
    }
    
    public static ByteBuffer copyToDirectBuffer(final Context context, final Resources resources, final int n) {
        final File tempFile = getTempFile(context);
        ByteBuffer mmap = null;
        if (tempFile == null) {
            return null;
        }
        try {
            if (copyToFile(tempFile, resources, n)) {
                mmap = mmap(tempFile);
            }
            tempFile.delete();
            return mmap;
        }
        finally {
            tempFile.delete();
            while (true) {}
        }
    }
    
    public static boolean copyToFile(final File file, final Resources resources, final int n) {
        Closeable openRawResource = null;
        try {
            return copyToFile(file, (InputStream)(openRawResource = resources.openRawResource(n)));
        }
        finally {
            closeQuietly(openRawResource);
        }
    }
    
    public static boolean copyToFile(final File file, final InputStream inputStream) {
        final Closeable closeable = null;
        final StrictMode$ThreadPolicy allowThreadDiskWrites = StrictMode.allowThreadDiskWrites();
        Closeable closeable2 = closeable;
        try {
            try {
                closeable2 = closeable;
                final FileOutputStream fileOutputStream = (FileOutputStream)(closeable2 = new FileOutputStream(file, (boolean)(0 != 0)));
                final byte[] array = new byte[1024];
                while (true) {
                    closeable2 = fileOutputStream;
                    final int read = inputStream.read(array);
                    if (read == -1) {
                        break;
                    }
                    closeable2 = fileOutputStream;
                    fileOutputStream.write(array, 0, read);
                }
                closeQuietly(fileOutputStream);
                StrictMode.setThreadPolicy(allowThreadDiskWrites);
                return true;
            }
            finally {
                closeQuietly(closeable2);
                StrictMode.setThreadPolicy(allowThreadDiskWrites);
                while (true) {}
            }
        }
        catch (IOException ex) {}
    }
    
    public static File getTempFile(final Context context) {
        final StringBuilder sb = new StringBuilder();
        sb.append(".font");
        sb.append(Process.myPid());
        sb.append("-");
        sb.append(Process.myTid());
        sb.append("-");
        final String string = sb.toString();
        for (int i = 0; i < 100; ++i) {
            final File cacheDir = context.getCacheDir();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(string);
            sb2.append(i);
            final File file = new File(cacheDir, sb2.toString());
            try {
                if (file.createNewFile()) {
                    return file;
                }
            }
            catch (IOException ex) {}
        }
        return null;
    }
    
    public static ByteBuffer mmap(Context openFileDescriptor, CancellationSignal cancellationSignal, final Uri uri) {
        final ContentResolver contentResolver = openFileDescriptor.getContentResolver();
        try {
            openFileDescriptor = (Context)contentResolver.openFileDescriptor(uri, "r", cancellationSignal);
            if (openFileDescriptor == null) {
                if (openFileDescriptor != null) {
                    ((ParcelFileDescriptor)openFileDescriptor).close();
                }
                return null;
            }
            try {
                cancellationSignal = (CancellationSignal)new FileInputStream(((ParcelFileDescriptor)openFileDescriptor).getFileDescriptor());
                try {
                    final FileChannel channel = ((FileInputStream)cancellationSignal).getChannel();
                    final MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_ONLY, 0L, channel.size());
                    ((FileInputStream)cancellationSignal).close();
                    if (openFileDescriptor != null) {
                        ((ParcelFileDescriptor)openFileDescriptor).close();
                    }
                    return map;
                }
                finally {
                    try {}
                    finally {
                        try {
                            ((FileInputStream)cancellationSignal).close();
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
                            ((Throwable)cancellationSignal).addSuppressed(exception2);
                        }
                    }
                }
            }
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    private static ByteBuffer mmap(final File file) {
        try {
            final FileInputStream fileInputStream = new FileInputStream(file);
            try {
                final FileChannel channel = fileInputStream.getChannel();
                final MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_ONLY, 0L, channel.size());
                fileInputStream.close();
                return map;
            }
            finally {
                try {}
                finally {
                    try {
                        fileInputStream.close();
                    }
                    finally {
                        final Throwable t;
                        final Throwable exception;
                        t.addSuppressed(exception);
                    }
                }
            }
        }
        catch (IOException ex) {
            return null;
        }
    }
}
