// 
// Decompiled by Procyon v0.5.36
// 

package androidx.print;

import android.os.CancellationSignal$OnCancelListener;
import android.print.PrintDocumentInfo$Builder;
import android.os.Bundle;
import android.print.PrintDocumentAdapter$LayoutResultCallback;
import android.print.PageRange;
import android.graphics.pdf.PdfDocument$Page;
import java.io.OutputStream;
import java.io.FileOutputStream;
import android.print.pdf.PrintedPdfDocument;
import android.os.AsyncTask;
import android.print.PrintAttributes$Margins;
import android.print.PrintDocumentAdapter$WriteResultCallback;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PrintDocumentAdapter;
import android.print.PrintAttributes$MediaSize;
import android.print.PrintManager;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import android.util.Log;
import android.graphics.Rect;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.print.PrintAttributes$Builder;
import android.print.PrintAttributes;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Bitmap$Config;
import android.graphics.Bitmap;
import android.os.Build$VERSION;
import android.graphics.BitmapFactory$Options;
import android.content.Context;

public final class PrintHelper
{
    public static final int COLOR_MODE_COLOR = 2;
    public static final int COLOR_MODE_MONOCHROME = 1;
    static final boolean IS_MIN_MARGINS_HANDLING_CORRECT;
    private static final String LOG_TAG = "PrintHelper";
    private static final int MAX_PRINT_SIZE = 3500;
    public static final int ORIENTATION_LANDSCAPE = 1;
    public static final int ORIENTATION_PORTRAIT = 2;
    static final boolean PRINT_ACTIVITY_RESPECTS_ORIENTATION;
    public static final int SCALE_MODE_FILL = 2;
    public static final int SCALE_MODE_FIT = 1;
    int mColorMode;
    final Context mContext;
    BitmapFactory$Options mDecodeOptions;
    final Object mLock;
    int mOrientation;
    int mScaleMode;
    
    static {
        final int sdk_INT = Build$VERSION.SDK_INT;
        final boolean b = false;
        PRINT_ACTIVITY_RESPECTS_ORIENTATION = (sdk_INT < 20 || Build$VERSION.SDK_INT > 23);
        boolean is_MIN_MARGINS_HANDLING_CORRECT = b;
        if (Build$VERSION.SDK_INT != 23) {
            is_MIN_MARGINS_HANDLING_CORRECT = true;
        }
        IS_MIN_MARGINS_HANDLING_CORRECT = is_MIN_MARGINS_HANDLING_CORRECT;
    }
    
    public PrintHelper(final Context mContext) {
        this.mDecodeOptions = null;
        this.mLock = new Object();
        this.mScaleMode = 2;
        this.mColorMode = 2;
        this.mOrientation = 1;
        this.mContext = mContext;
    }
    
    static Bitmap convertBitmapForColorMode(final Bitmap bitmap, final int n) {
        if (n != 1) {
            return bitmap;
        }
        final Bitmap bitmap2 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap$Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap2);
        final Paint paint = new Paint();
        final ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        paint.setColorFilter((ColorFilter)new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        canvas.setBitmap((Bitmap)null);
        return bitmap2;
    }
    
    private static PrintAttributes$Builder copyAttributes(final PrintAttributes printAttributes) {
        final PrintAttributes$Builder setMinMargins = new PrintAttributes$Builder().setMediaSize(printAttributes.getMediaSize()).setResolution(printAttributes.getResolution()).setMinMargins(printAttributes.getMinMargins());
        if (printAttributes.getColorMode() != 0) {
            setMinMargins.setColorMode(printAttributes.getColorMode());
        }
        if (Build$VERSION.SDK_INT >= 23 && printAttributes.getDuplexMode() != 0) {
            setMinMargins.setDuplexMode(printAttributes.getDuplexMode());
        }
        return setMinMargins;
    }
    
    static Matrix getMatrix(final int n, final int n2, final RectF rectF, final int n3) {
        final Matrix matrix = new Matrix();
        final float n4 = rectF.width() / n;
        float n5;
        if (n3 == 2) {
            n5 = Math.max(n4, rectF.height() / n2);
        }
        else {
            n5 = Math.min(n4, rectF.height() / n2);
        }
        matrix.postScale(n5, n5);
        matrix.postTranslate((rectF.width() - n * n5) / 2.0f, (rectF.height() - n2 * n5) / 2.0f);
        return matrix;
    }
    
    static boolean isPortrait(final Bitmap bitmap) {
        return bitmap.getWidth() <= bitmap.getHeight();
    }
    
    private Bitmap loadBitmap(final Uri uri, BitmapFactory$Options decodeStream) throws FileNotFoundException {
        if (uri != null) {
            final Context mContext = this.mContext;
            if (mContext != null) {
                InputStream openInputStream = null;
                try {
                    decodeStream = (BitmapFactory$Options)BitmapFactory.decodeStream(openInputStream = mContext.getContentResolver().openInputStream(uri), (Rect)null, decodeStream);
                    return (Bitmap)decodeStream;
                }
                finally {
                    if (openInputStream != null) {
                        try {
                            openInputStream.close();
                        }
                        catch (IOException ex) {
                            Log.w("PrintHelper", "close fail ", (Throwable)ex);
                        }
                    }
                }
            }
        }
        throw new IllegalArgumentException("bad argument to loadBitmap");
    }
    
    public static boolean systemSupportsPrint() {
        return Build$VERSION.SDK_INT >= 19;
    }
    
    public int getColorMode() {
        return this.mColorMode;
    }
    
    public int getOrientation() {
        if (Build$VERSION.SDK_INT >= 19 && this.mOrientation == 0) {
            return 1;
        }
        return this.mOrientation;
    }
    
    public int getScaleMode() {
        return this.mScaleMode;
    }
    
    Bitmap loadConstrainedBitmap(Uri mLock) throws FileNotFoundException {
        if (mLock == null || this.mContext == null) {
            throw new IllegalArgumentException("bad argument to getScaledBitmap");
        }
        final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
        bitmapFactory$Options.inJustDecodeBounds = true;
        this.loadBitmap(mLock, bitmapFactory$Options);
        final int outWidth = bitmapFactory$Options.outWidth;
        final int outHeight = bitmapFactory$Options.outHeight;
        if (outWidth > 0 && outHeight > 0) {
            int i;
            int inSampleSize;
            for (i = Math.max(outWidth, outHeight), inSampleSize = 1; i > 3500; i >>>= 1, inSampleSize <<= 1) {}
            if (inSampleSize > 0) {
                if (Math.min(outWidth, outHeight) / inSampleSize > 0) {
                    Object mLock2 = this.mLock;
                    // monitorenter(mLock2)
                    BitmapFactory$Options mDecodeOptions2;
                    try {
                        final BitmapFactory$Options mDecodeOptions = new BitmapFactory$Options();
                        this.mDecodeOptions = mDecodeOptions;
                        mDecodeOptions.inMutable = true;
                        this.mDecodeOptions.inSampleSize = inSampleSize;
                        mDecodeOptions2 = this.mDecodeOptions;
                        final Object o = mLock2;
                        // monitorexit(o)
                        try {
                            final PrintHelper printHelper = this;
                            final Uri uri = mLock;
                            final BitmapFactory$Options bitmapFactory$Options2 = mDecodeOptions2;
                            final Bitmap bitmap = printHelper.loadBitmap(uri, bitmapFactory$Options2);
                            final PrintHelper printHelper2 = this;
                            final Object o2 = printHelper2.mLock;
                            final Object o3;
                            mLock = (Uri)(o3 = o2);
                            synchronized (o3) {
                                final PrintHelper printHelper3 = this;
                                final BitmapFactory$Options bitmapFactory$Options3 = null;
                                printHelper3.mDecodeOptions = bitmapFactory$Options3;
                                return bitmap;
                            }
                        }
                        finally {
                            final Object o4;
                            mLock2 = o4;
                            mLock = (Uri)this.mLock;
                            // monitorenter(mLock)
                            try {
                                this.mDecodeOptions = null;
                            }
                            // monitorexit(mLock)
                            finally {
                                final Object o5;
                                mLock2 = o5;
                            }
                            // monitorexit(mLock)
                        }
                    }
                    finally {
                        final Object o7;
                        final Object o6 = o7;
                    }
                    while (true) {
                        try {
                            final Object o = mLock2;
                            // monitorexit(o)
                            final PrintHelper printHelper = this;
                            final Uri uri = mLock;
                            final BitmapFactory$Options bitmapFactory$Options2 = mDecodeOptions2;
                            final Bitmap bitmap = printHelper.loadBitmap(uri, bitmapFactory$Options2);
                            final PrintHelper printHelper2 = this;
                            final Object o2 = printHelper2.mLock;
                            final Object o3;
                            mLock = (Uri)(o3 = o2);
                            // monitorenter(o3)
                            final PrintHelper printHelper3 = this;
                            final BitmapFactory$Options bitmapFactory$Options3 = null;
                            printHelper3.mDecodeOptions = bitmapFactory$Options3;
                            return bitmap;
                            // monitorexit(mLock2)
                            throw;
                        }
                        finally {
                            continue;
                        }
                        break;
                    }
                }
            }
            return null;
        }
        return null;
    }
    
    public void printBitmap(final String s, final Bitmap bitmap) {
        this.printBitmap(s, bitmap, null);
    }
    
    public void printBitmap(final String s, final Bitmap bitmap, final OnPrintFinishCallback onPrintFinishCallback) {
        if (Build$VERSION.SDK_INT >= 19 && bitmap != null) {
            final PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
            PrintAttributes$MediaSize mediaSize;
            if (isPortrait(bitmap)) {
                mediaSize = PrintAttributes$MediaSize.UNKNOWN_PORTRAIT;
            }
            else {
                mediaSize = PrintAttributes$MediaSize.UNKNOWN_LANDSCAPE;
            }
            printManager.print(s, (PrintDocumentAdapter)new PrintBitmapAdapter(s, this.mScaleMode, bitmap, onPrintFinishCallback), new PrintAttributes$Builder().setMediaSize(mediaSize).setColorMode(this.mColorMode).build());
        }
    }
    
    public void printBitmap(final String s, final Uri uri) throws FileNotFoundException {
        this.printBitmap(s, uri, null);
    }
    
    public void printBitmap(final String s, final Uri uri, final OnPrintFinishCallback onPrintFinishCallback) throws FileNotFoundException {
        if (Build$VERSION.SDK_INT < 19) {
            return;
        }
        final PrintUriAdapter printUriAdapter = new PrintUriAdapter(s, uri, onPrintFinishCallback, this.mScaleMode);
        final PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
        final PrintAttributes$Builder printAttributes$Builder = new PrintAttributes$Builder();
        printAttributes$Builder.setColorMode(this.mColorMode);
        final int mOrientation = this.mOrientation;
        if (mOrientation != 1 && mOrientation != 0) {
            if (mOrientation == 2) {
                printAttributes$Builder.setMediaSize(PrintAttributes$MediaSize.UNKNOWN_PORTRAIT);
            }
        }
        else {
            printAttributes$Builder.setMediaSize(PrintAttributes$MediaSize.UNKNOWN_LANDSCAPE);
        }
        printManager.print(s, (PrintDocumentAdapter)printUriAdapter, printAttributes$Builder.build());
    }
    
    public void setColorMode(final int mColorMode) {
        this.mColorMode = mColorMode;
    }
    
    public void setOrientation(final int mOrientation) {
        this.mOrientation = mOrientation;
    }
    
    public void setScaleMode(final int mScaleMode) {
        this.mScaleMode = mScaleMode;
    }
    
    void writeBitmap(final PrintAttributes printAttributes, final int n, final Bitmap bitmap, final ParcelFileDescriptor parcelFileDescriptor, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$WriteResultCallback printDocumentAdapter$WriteResultCallback) {
        PrintAttributes build;
        if (PrintHelper.IS_MIN_MARGINS_HANDLING_CORRECT) {
            build = printAttributes;
        }
        else {
            build = copyAttributes(printAttributes).setMinMargins(new PrintAttributes$Margins(0, 0, 0, 0)).build();
        }
        new AsyncTask<Void, Void, Throwable>() {
            protected Throwable doInBackground(final Void... array) {
                try {
                    if (cancellationSignal.isCanceled()) {
                        return null;
                    }
                    final PrintedPdfDocument printedPdfDocument = new PrintedPdfDocument(PrintHelper.this.mContext, build);
                    final Bitmap convertBitmapForColorMode = PrintHelper.convertBitmapForColorMode(bitmap, build.getColorMode());
                    if (cancellationSignal.isCanceled()) {
                        return null;
                    }
                    try {
                        final PdfDocument$Page startPage = printedPdfDocument.startPage(1);
                        RectF rectF;
                        if (PrintHelper.IS_MIN_MARGINS_HANDLING_CORRECT) {
                            rectF = new RectF(startPage.getInfo().getContentRect());
                        }
                        else {
                            final PrintedPdfDocument printedPdfDocument2 = new PrintedPdfDocument(PrintHelper.this.mContext, printAttributes);
                            final PdfDocument$Page startPage2 = printedPdfDocument2.startPage(1);
                            rectF = new RectF(startPage2.getInfo().getContentRect());
                            printedPdfDocument2.finishPage(startPage2);
                            printedPdfDocument2.close();
                        }
                        final Matrix matrix = PrintHelper.getMatrix(convertBitmapForColorMode.getWidth(), convertBitmapForColorMode.getHeight(), rectF, n);
                        if (!PrintHelper.IS_MIN_MARGINS_HANDLING_CORRECT) {
                            matrix.postTranslate(rectF.left, rectF.top);
                            startPage.getCanvas().clipRect(rectF);
                        }
                        startPage.getCanvas().drawBitmap(convertBitmapForColorMode, matrix, (Paint)null);
                        printedPdfDocument.finishPage(startPage);
                        if (cancellationSignal.isCanceled()) {
                            return null;
                        }
                        printedPdfDocument.writeTo((OutputStream)new FileOutputStream(parcelFileDescriptor.getFileDescriptor()));
                        return null;
                    }
                    finally {
                        printedPdfDocument.close();
                        if (parcelFileDescriptor != null) {
                            try {
                                parcelFileDescriptor.close();
                            }
                            catch (IOException ex) {}
                        }
                        if (convertBitmapForColorMode != bitmap) {
                            convertBitmapForColorMode.recycle();
                        }
                    }
                }
                finally {
                    return;
                }
            }
            
            protected void onPostExecute(final Throwable t) {
                if (cancellationSignal.isCanceled()) {
                    printDocumentAdapter$WriteResultCallback.onWriteCancelled();
                }
                else if (t == null) {
                    printDocumentAdapter$WriteResultCallback.onWriteFinished(new PageRange[] { PageRange.ALL_PAGES });
                }
                else {
                    Log.e("PrintHelper", "Error writing printed content", t);
                    printDocumentAdapter$WriteResultCallback.onWriteFailed((CharSequence)null);
                }
            }
        }.execute((Object[])new Void[0]);
    }
    
    public interface OnPrintFinishCallback
    {
        void onFinish();
    }
    
    private class PrintBitmapAdapter extends PrintDocumentAdapter
    {
        private PrintAttributes mAttributes;
        private final Bitmap mBitmap;
        private final OnPrintFinishCallback mCallback;
        private final int mFittingMode;
        private final String mJobName;
        
        PrintBitmapAdapter(final String mJobName, final int mFittingMode, final Bitmap mBitmap, final OnPrintFinishCallback mCallback) {
            this.mJobName = mJobName;
            this.mFittingMode = mFittingMode;
            this.mBitmap = mBitmap;
            this.mCallback = mCallback;
        }
        
        public void onFinish() {
            final OnPrintFinishCallback mCallback = this.mCallback;
            if (mCallback != null) {
                mCallback.onFinish();
            }
        }
        
        public void onLayout(final PrintAttributes printAttributes, final PrintAttributes mAttributes, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$LayoutResultCallback printDocumentAdapter$LayoutResultCallback, final Bundle bundle) {
            this.mAttributes = mAttributes;
            printDocumentAdapter$LayoutResultCallback.onLayoutFinished(new PrintDocumentInfo$Builder(this.mJobName).setContentType(1).setPageCount(1).build(), true ^ mAttributes.equals((Object)printAttributes));
        }
        
        public void onWrite(final PageRange[] array, final ParcelFileDescriptor parcelFileDescriptor, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$WriteResultCallback printDocumentAdapter$WriteResultCallback) {
            PrintHelper.this.writeBitmap(this.mAttributes, this.mFittingMode, this.mBitmap, parcelFileDescriptor, cancellationSignal, printDocumentAdapter$WriteResultCallback);
        }
    }
    
    private class PrintUriAdapter extends PrintDocumentAdapter
    {
        PrintAttributes mAttributes;
        Bitmap mBitmap;
        final OnPrintFinishCallback mCallback;
        final int mFittingMode;
        final Uri mImageFile;
        final String mJobName;
        AsyncTask<Uri, Boolean, Bitmap> mLoadBitmap;
        
        PrintUriAdapter(final String mJobName, final Uri mImageFile, final OnPrintFinishCallback mCallback, final int mFittingMode) {
            this.mJobName = mJobName;
            this.mImageFile = mImageFile;
            this.mCallback = mCallback;
            this.mFittingMode = mFittingMode;
            this.mBitmap = null;
        }
        
        void cancelLoad() {
            synchronized (PrintHelper.this.mLock) {
                if (PrintHelper.this.mDecodeOptions != null) {
                    if (Build$VERSION.SDK_INT < 24) {
                        PrintHelper.this.mDecodeOptions.requestCancelDecode();
                    }
                    PrintHelper.this.mDecodeOptions = null;
                }
            }
        }
        
        public void onFinish() {
            super.onFinish();
            this.cancelLoad();
            final AsyncTask<Uri, Boolean, Bitmap> mLoadBitmap = this.mLoadBitmap;
            if (mLoadBitmap != null) {
                mLoadBitmap.cancel(true);
            }
            final OnPrintFinishCallback mCallback = this.mCallback;
            if (mCallback != null) {
                mCallback.onFinish();
            }
            final Bitmap mBitmap = this.mBitmap;
            if (mBitmap != null) {
                mBitmap.recycle();
                this.mBitmap = null;
            }
        }
        
        public void onLayout(final PrintAttributes printAttributes, final PrintAttributes mAttributes, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$LayoutResultCallback printDocumentAdapter$LayoutResultCallback, final Bundle bundle) {
            synchronized (this) {
                this.mAttributes = mAttributes;
                // monitorexit(this)
                if (cancellationSignal.isCanceled()) {
                    printDocumentAdapter$LayoutResultCallback.onLayoutCancelled();
                    return;
                }
                if (this.mBitmap != null) {
                    printDocumentAdapter$LayoutResultCallback.onLayoutFinished(new PrintDocumentInfo$Builder(this.mJobName).setContentType(1).setPageCount(1).build(), true ^ mAttributes.equals((Object)printAttributes));
                    return;
                }
                this.mLoadBitmap = (AsyncTask<Uri, Boolean, Bitmap>)new AsyncTask<Uri, Boolean, Bitmap>() {
                    protected Bitmap doInBackground(final Uri... array) {
                        try {
                            return PrintHelper.this.loadConstrainedBitmap(PrintUriAdapter.this.mImageFile);
                        }
                        catch (FileNotFoundException ex) {
                            return null;
                        }
                    }
                    
                    protected void onCancelled(final Bitmap bitmap) {
                        printDocumentAdapter$LayoutResultCallback.onLayoutCancelled();
                        PrintUriAdapter.this.mLoadBitmap = null;
                    }
                    
                    protected void onPostExecute(final Bitmap bitmap) {
                        super.onPostExecute((Object)bitmap);
                        Object mBitmap = bitmap;
                        Label_0110: {
                            if (bitmap != null) {
                                if (PrintHelper.PRINT_ACTIVITY_RESPECTS_ORIENTATION) {
                                    mBitmap = bitmap;
                                    if (PrintHelper.this.mOrientation != 0) {
                                        break Label_0110;
                                    }
                                }
                                // monitorenter(this)
                                PrintAttributes$MediaSize mediaSize;
                                try {
                                    mediaSize = PrintUriAdapter.this.mAttributes.getMediaSize();
                                    final AsyncTask<Uri, Boolean, Bitmap> asyncTask = this;
                                    // monitorexit(asyncTask)
                                    mBitmap = bitmap;
                                    final PrintAttributes$MediaSize printAttributes$MediaSize = mediaSize;
                                    if (printAttributes$MediaSize == null) {
                                        break Label_0110;
                                    }
                                    mBitmap = bitmap;
                                    final PrintAttributes$MediaSize printAttributes$MediaSize2 = mediaSize;
                                    final boolean b = printAttributes$MediaSize2.isPortrait();
                                    final Bitmap bitmap2 = bitmap;
                                    final boolean b2 = PrintHelper.isPortrait(bitmap2);
                                    if (b != b2) {
                                        final Matrix matrix = new Matrix();
                                        final Matrix matrix2;
                                        mBitmap = (matrix2 = matrix);
                                        final float n = 90.0f;
                                        matrix2.postRotate(n);
                                        final Bitmap bitmap3 = bitmap;
                                        final int n2 = 0;
                                        final int n3 = 0;
                                        final Bitmap bitmap4 = bitmap;
                                        final int n4 = bitmap4.getWidth();
                                        final Bitmap bitmap5 = bitmap;
                                        final int n5 = bitmap5.getHeight();
                                        final Object o = mBitmap;
                                        final boolean b3 = true;
                                        final Bitmap bitmap6 = (Bitmap)(mBitmap = Bitmap.createBitmap(bitmap3, n2, n3, n4, n5, (Matrix)o, b3));
                                    }
                                    break Label_0110;
                                }
                                finally {
                                    final Object o3;
                                    final Object o2 = o3;
                                }
                                while (true) {
                                    try {
                                        final AsyncTask<Uri, Boolean, Bitmap> asyncTask = this;
                                        // monitorexit(asyncTask)
                                        mBitmap = bitmap;
                                        final PrintAttributes$MediaSize printAttributes$MediaSize = mediaSize;
                                        if (printAttributes$MediaSize == null) {
                                            break Label_0110;
                                        }
                                        mBitmap = bitmap;
                                        final PrintAttributes$MediaSize printAttributes$MediaSize2 = mediaSize;
                                        final boolean b = printAttributes$MediaSize2.isPortrait();
                                        final Bitmap bitmap2 = bitmap;
                                        final boolean b2 = PrintHelper.isPortrait(bitmap2);
                                        if (b != b2) {
                                            final Matrix matrix = new Matrix();
                                            final Matrix matrix2;
                                            mBitmap = (matrix2 = matrix);
                                            final float n = 90.0f;
                                            matrix2.postRotate(n);
                                            final Bitmap bitmap3 = bitmap;
                                            final int n2 = 0;
                                            final int n3 = 0;
                                            final Bitmap bitmap4 = bitmap;
                                            final int n4 = bitmap4.getWidth();
                                            final Bitmap bitmap5 = bitmap;
                                            final int n5 = bitmap5.getHeight();
                                            final Object o = mBitmap;
                                            final boolean b3 = true;
                                            mBitmap = Bitmap.createBitmap(bitmap3, n2, n3, n4, n5, (Matrix)o, b3);
                                        }
                                        break Label_0110;
                                        // monitorexit(this)
                                        throw;
                                    }
                                    finally {
                                        continue;
                                    }
                                    break;
                                }
                            }
                        }
                        if ((PrintUriAdapter.this.mBitmap = (Bitmap)mBitmap) != null) {
                            printDocumentAdapter$LayoutResultCallback.onLayoutFinished(new PrintDocumentInfo$Builder(PrintUriAdapter.this.mJobName).setContentType(1).setPageCount(1).build(), true ^ mAttributes.equals((Object)printAttributes));
                        }
                        else {
                            printDocumentAdapter$LayoutResultCallback.onLayoutFailed((CharSequence)null);
                        }
                        PrintUriAdapter.this.mLoadBitmap = null;
                    }
                    
                    protected void onPreExecute() {
                        cancellationSignal.setOnCancelListener((CancellationSignal$OnCancelListener)new CancellationSignal$OnCancelListener() {
                            public void onCancel() {
                                PrintUriAdapter.this.cancelLoad();
                                AsyncTask.this.cancel(false);
                            }
                        });
                    }
                }.execute((Object[])new Uri[0]);
            }
        }
        
        public void onWrite(final PageRange[] array, final ParcelFileDescriptor parcelFileDescriptor, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$WriteResultCallback printDocumentAdapter$WriteResultCallback) {
            PrintHelper.this.writeBitmap(this.mAttributes, this.mFittingMode, this.mBitmap, parcelFileDescriptor, cancellationSignal, printDocumentAdapter$WriteResultCallback);
        }
    }
}
