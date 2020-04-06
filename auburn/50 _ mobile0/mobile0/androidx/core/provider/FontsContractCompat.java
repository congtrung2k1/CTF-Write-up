// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.provider;

import android.database.Cursor;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import androidx.core.util.Preconditions;
import android.provider.BaseColumns;
import androidx.core.graphics.TypefaceCompatUtil;
import java.util.HashMap;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Collection;
import java.util.Collections;
import android.content.pm.PackageManager;
import java.util.concurrent.Callable;
import android.os.Handler;
import androidx.core.content.res.ResourcesCompat;
import android.content.ContentResolver;
import android.net.Uri;
import android.content.ContentUris;
import android.os.Build$VERSION;
import android.net.Uri$Builder;
import androidx.core.content.res.FontResourcesParserCompat;
import android.content.res.Resources;
import android.content.pm.PackageManager$NameNotFoundException;
import android.content.pm.ProviderInfo;
import java.util.Arrays;
import java.util.List;
import android.content.pm.Signature;
import androidx.core.graphics.TypefaceCompat;
import android.os.CancellationSignal;
import android.content.Context;
import android.graphics.Typeface;
import androidx.collection.LruCache;
import java.util.ArrayList;
import androidx.collection.SimpleArrayMap;
import java.util.Comparator;

public class FontsContractCompat
{
    private static final int BACKGROUND_THREAD_KEEP_ALIVE_DURATION_MS = 10000;
    public static final String PARCEL_FONT_RESULTS = "font_results";
    static final int RESULT_CODE_PROVIDER_NOT_FOUND = -1;
    static final int RESULT_CODE_WRONG_CERTIFICATES = -2;
    private static final String TAG = "FontsContractCompat";
    private static final SelfDestructiveThread sBackgroundThread;
    private static final Comparator<byte[]> sByteArrayComparator;
    static final Object sLock;
    static final SimpleArrayMap<String, ArrayList<SelfDestructiveThread.ReplyCallback<TypefaceResult>>> sPendingReplies;
    static final LruCache<String, Typeface> sTypefaceCache;
    
    static {
        sTypefaceCache = new LruCache<String, Typeface>(16);
        sBackgroundThread = new SelfDestructiveThread("fonts", 10, 10000);
        sLock = new Object();
        sPendingReplies = new SimpleArrayMap<String, ArrayList<SelfDestructiveThread.ReplyCallback<TypefaceResult>>>();
        sByteArrayComparator = new Comparator<byte[]>() {
            @Override
            public int compare(final byte[] array, final byte[] array2) {
                if (array.length != array2.length) {
                    return array.length - array2.length;
                }
                for (int i = 0; i < array.length; ++i) {
                    if (array[i] != array2[i]) {
                        return array[i] - array2[i];
                    }
                }
                return 0;
            }
        };
    }
    
    private FontsContractCompat() {
    }
    
    public static Typeface buildTypeface(final Context context, final CancellationSignal cancellationSignal, final FontInfo[] array) {
        return TypefaceCompat.createFromFontInfo(context, cancellationSignal, array, 0);
    }
    
    private static List<byte[]> convertToByteArrayList(final Signature[] array) {
        final ArrayList<byte[]> list = new ArrayList<byte[]>();
        for (int i = 0; i < array.length; ++i) {
            list.add(array[i].toByteArray());
        }
        return list;
    }
    
    private static boolean equalsByteArrayList(final List<byte[]> list, final List<byte[]> list2) {
        if (list.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list.size(); ++i) {
            if (!Arrays.equals(list.get(i), list2.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static FontFamilyResult fetchFonts(final Context context, final CancellationSignal cancellationSignal, final FontRequest fontRequest) throws PackageManager$NameNotFoundException {
        final ProviderInfo provider = getProvider(context.getPackageManager(), fontRequest, context.getResources());
        if (provider == null) {
            return new FontFamilyResult(1, null);
        }
        return new FontFamilyResult(0, getFontFromProvider(context, fontRequest, provider.authority, cancellationSignal));
    }
    
    private static List<List<byte[]>> getCertificates(final FontRequest fontRequest, final Resources resources) {
        if (fontRequest.getCertificates() != null) {
            return fontRequest.getCertificates();
        }
        return FontResourcesParserCompat.readCerts(resources, fontRequest.getCertificatesArrayResId());
    }
    
    static FontInfo[] getFontFromProvider(Context query, final FontRequest fontRequest, String e, final CancellationSignal cancellationSignal) {
        final ArrayList list = new ArrayList();
        final Uri build = new Uri$Builder().scheme("content").authority(e).build();
        final Uri build2 = new Uri$Builder().scheme("content").authority(e).appendPath("file").build();
        e = null;
        final String s = null;
        String s2 = null;
        Label_0520: {
            try {
                Label_0224: {
                    if (Build$VERSION.SDK_INT > 16) {
                        try {
                            ((Context)query).getContentResolver().query(build, new String[] { "_id", "file_id", "font_ttc_index", "font_variation_settings", "font_weight", "font_italic", "result_code" }, "query = ?", new String[] { fontRequest.getQuery() }, (String)null, cancellationSignal);
                            break Label_0224;
                        }
                        finally {
                            break Label_0520;
                        }
                    }
                    final ContentResolver contentResolver = ((Context)query).getContentResolver();
                    final String query2 = fontRequest.getQuery();
                    e = s;
                    try {
                        query = contentResolver.query(build, new String[] { "_id", "file_id", "font_ttc_index", "font_variation_settings", "font_weight", "font_italic", "result_code" }, "query = ?", new String[] { query2 }, (String)null);
                        final ArrayList list3;
                        Label_0489: {
                            if (query != null) {
                                e = (String)query;
                                if (((Cursor)query).getCount() > 0) {
                                    e = (String)query;
                                    final int columnIndex = ((Cursor)query).getColumnIndex("result_code");
                                    e = (String)query;
                                    e = (String)query;
                                    final ArrayList<String> list2 = new ArrayList<String>();
                                    try {
                                        final int columnIndex2 = ((Cursor)query).getColumnIndex("_id");
                                        final int columnIndex3 = ((Cursor)query).getColumnIndex("file_id");
                                        final int columnIndex4 = ((Cursor)query).getColumnIndex("font_ttc_index");
                                        final int columnIndex5 = ((Cursor)query).getColumnIndex("font_weight");
                                        final int columnIndex6 = ((Cursor)query).getColumnIndex("font_italic");
                                        while (((Cursor)query).moveToNext()) {
                                            int int1;
                                            if (columnIndex != -1) {
                                                int1 = ((Cursor)query).getInt(columnIndex);
                                            }
                                            else {
                                                int1 = 0;
                                            }
                                            int int2;
                                            if (columnIndex4 != -1) {
                                                int2 = ((Cursor)query).getInt(columnIndex4);
                                            }
                                            else {
                                                int2 = 0;
                                            }
                                            Uri uri;
                                            if (columnIndex3 == -1) {
                                                uri = ContentUris.withAppendedId(build, ((Cursor)query).getLong(columnIndex2));
                                            }
                                            else {
                                                uri = ContentUris.withAppendedId(build2, ((Cursor)query).getLong(columnIndex3));
                                            }
                                            int int3;
                                            if (columnIndex5 != -1) {
                                                int3 = ((Cursor)query).getInt(columnIndex5);
                                            }
                                            else {
                                                int3 = 400;
                                            }
                                            e = (String)new FontInfo(uri, int2, int3, columnIndex6 != -1 && ((Cursor)query).getInt(columnIndex6) == 1, int1);
                                            list2.add(e);
                                        }
                                        break Label_0489;
                                    }
                                    finally {}
                                }
                            }
                            list3 = list;
                        }
                        if (query != null) {
                            ((Cursor)query).close();
                        }
                        return (FontInfo[])list3.toArray(new FontInfo[0]);
                    }
                    finally {}
                }
            }
            finally {
                s2 = e;
            }
        }
        if (s2 != null) {
            ((Cursor)s2).close();
        }
        throw;
    }
    
    static TypefaceResult getFontInternal(final Context context, final FontRequest fontRequest, final int n) {
        try {
            final FontFamilyResult fetchFonts = fetchFonts(context, null, fontRequest);
            final int statusCode = fetchFonts.getStatusCode();
            int n2 = -3;
            if (statusCode == 0) {
                final Typeface fromFontInfo = TypefaceCompat.createFromFontInfo(context, null, fetchFonts.getFonts(), n);
                if (fromFontInfo != null) {
                    n2 = 0;
                }
                return new TypefaceResult(fromFontInfo, n2);
            }
            if (fetchFonts.getStatusCode() == 1) {
                n2 = -2;
            }
            return new TypefaceResult(null, n2);
        }
        catch (PackageManager$NameNotFoundException ex) {
            return new TypefaceResult(null, -1);
        }
    }
    
    public static Typeface getFontSync(final Context context, final FontRequest fontRequest, final ResourcesCompat.FontCallback fontCallback, final Handler handler, final boolean b, final int n, final int i) {
        final StringBuilder sb = new StringBuilder();
        sb.append(fontRequest.getIdentifier());
        sb.append("-");
        sb.append(i);
        final String string = sb.toString();
        final Typeface typeface = FontsContractCompat.sTypefaceCache.get(string);
        if (typeface != null) {
            if (fontCallback != null) {
                fontCallback.onFontRetrieved(typeface);
            }
            return typeface;
        }
        if (b && n == -1) {
            final TypefaceResult fontInternal = getFontInternal(context, fontRequest, i);
            if (fontCallback != null) {
                if (fontInternal.mResult == 0) {
                    fontCallback.callbackSuccessAsync(fontInternal.mTypeface, handler);
                }
                else {
                    fontCallback.callbackFailAsync(fontInternal.mResult, handler);
                }
            }
            return fontInternal.mTypeface;
        }
        final Callable<TypefaceResult> callable = new Callable<TypefaceResult>() {
            @Override
            public TypefaceResult call() throws Exception {
                final TypefaceResult fontInternal = FontsContractCompat.getFontInternal(context, fontRequest, i);
                if (fontInternal.mTypeface != null) {
                    FontsContractCompat.sTypefaceCache.put(string, fontInternal.mTypeface);
                }
                return fontInternal;
            }
        };
        if (b) {
            try {
                return FontsContractCompat.sBackgroundThread.postAndWait((Callable<TypefaceResult>)callable, n).mTypeface;
            }
            catch (InterruptedException ex) {
                return null;
            }
        }
        SelfDestructiveThread.ReplyCallback<TypefaceResult> replyCallback;
        if (fontCallback == null) {
            replyCallback = null;
        }
        else {
            replyCallback = new SelfDestructiveThread.ReplyCallback<TypefaceResult>() {
                public void onReply(final TypefaceResult typefaceResult) {
                    if (typefaceResult == null) {
                        fontCallback.callbackFailAsync(1, handler);
                    }
                    else if (typefaceResult.mResult == 0) {
                        fontCallback.callbackSuccessAsync(typefaceResult.mTypeface, handler);
                    }
                    else {
                        fontCallback.callbackFailAsync(typefaceResult.mResult, handler);
                    }
                }
            };
        }
        synchronized (FontsContractCompat.sLock) {
            if (FontsContractCompat.sPendingReplies.containsKey(string)) {
                if (replyCallback != null) {
                    FontsContractCompat.sPendingReplies.get(string).add(replyCallback);
                }
                return null;
            }
            if (replyCallback != null) {
                final ArrayList<SelfDestructiveThread.ReplyCallback<TypefaceResult>> list = new ArrayList<SelfDestructiveThread.ReplyCallback<TypefaceResult>>();
                list.add(replyCallback);
                FontsContractCompat.sPendingReplies.put(string, list);
            }
            // monitorexit(FontsContractCompat.sLock)
            FontsContractCompat.sBackgroundThread.postAndReply((Callable<T>)callable, (SelfDestructiveThread.ReplyCallback<T>)new SelfDestructiveThread.ReplyCallback<TypefaceResult>() {
                public void onReply(final TypefaceResult p0) {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Original Bytecode:
                    // 
                    //     3: astore_2       
                    //     4: aload_2        
                    //     5: monitorenter   
                    //     6: getstatic       androidx/core/provider/FontsContractCompat.sPendingReplies:Landroidx/collection/SimpleArrayMap;
                    //     9: aload_0        
                    //    10: getfield        androidx/core/provider/FontsContractCompat$3.val$id:Ljava/lang/String;
                    //    13: invokevirtual   androidx/collection/SimpleArrayMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
                    //    16: checkcast       Ljava/util/ArrayList;
                    //    19: astore_3       
                    //    20: aload_3        
                    //    21: ifnonnull       27
                    //    24: aload_2        
                    //    25: monitorexit    
                    //    26: return         
                    //    27: getstatic       androidx/core/provider/FontsContractCompat.sPendingReplies:Landroidx/collection/SimpleArrayMap;
                    //    30: aload_0        
                    //    31: getfield        androidx/core/provider/FontsContractCompat$3.val$id:Ljava/lang/String;
                    //    34: invokevirtual   androidx/collection/SimpleArrayMap.remove:(Ljava/lang/Object;)Ljava/lang/Object;
                    //    37: pop            
                    //    38: aload_2        
                    //    39: monitorexit    
                    //    40: iconst_0       
                    //    41: istore          4
                    //    43: iload           4
                    //    45: aload_3        
                    //    46: invokevirtual   java/util/ArrayList.size:()I
                    //    49: if_icmpge       73
                    //    52: aload_3        
                    //    53: iload           4
                    //    55: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
                    //    58: checkcast       Landroidx/core/provider/SelfDestructiveThread$ReplyCallback;
                    //    61: aload_1        
                    //    62: invokeinterface androidx/core/provider/SelfDestructiveThread$ReplyCallback.onReply:(Ljava/lang/Object;)V
                    //    67: iinc            4, 1
                    //    70: goto            43
                    //    73: return         
                    //    74: astore_1       
                    //    75: aload_2        
                    //    76: monitorexit    
                    //    77: aload_1        
                    //    78: athrow         
                    //    79: astore_1       
                    //    80: goto            75
                    //    Exceptions:
                    //  Try           Handler
                    //  Start  End    Start  End    Type
                    //  -----  -----  -----  -----  ----
                    //  6      20     74     75     Any
                    //  24     26     79     83     Any
                    //  27     40     79     83     Any
                    //  75     77     79     83     Any
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.IllegalStateException: Expression is linked from several locations: Label_0027:
                    //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
                    //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
                    //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
                    //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1164)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformSynchronized(AstMethodBodyBuilder.java:529)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:375)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
                    //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
                    //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
                    //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
                    //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
                    //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
                    // 
                    throw new IllegalStateException("An error occurred while decompiling this method.");
                }
            });
            return null;
        }
    }
    
    public static ProviderInfo getProvider(final PackageManager packageManager, final FontRequest fontRequest, final Resources resources) throws PackageManager$NameNotFoundException {
        final String providerAuthority = fontRequest.getProviderAuthority();
        final ProviderInfo resolveContentProvider = packageManager.resolveContentProvider(providerAuthority, 0);
        if (resolveContentProvider == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("No package found for authority: ");
            sb.append(providerAuthority);
            throw new PackageManager$NameNotFoundException(sb.toString());
        }
        if (resolveContentProvider.packageName.equals(fontRequest.getProviderPackage())) {
            final List<byte[]> convertToByteArrayList = convertToByteArrayList(packageManager.getPackageInfo(resolveContentProvider.packageName, 64).signatures);
            Collections.sort((List<Object>)convertToByteArrayList, (Comparator<? super Object>)FontsContractCompat.sByteArrayComparator);
            final List<List<byte[]>> certificates = getCertificates(fontRequest, resources);
            for (int i = 0; i < certificates.size(); ++i) {
                final ArrayList list = new ArrayList<byte[]>((Collection<? extends T>)certificates.get(i));
                Collections.sort((List<E>)list, (Comparator<? super E>)FontsContractCompat.sByteArrayComparator);
                if (equalsByteArrayList(convertToByteArrayList, (List<byte[]>)list)) {
                    return resolveContentProvider;
                }
            }
            return null;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Found content provider ");
        sb2.append(providerAuthority);
        sb2.append(", but package was not ");
        sb2.append(fontRequest.getProviderPackage());
        throw new PackageManager$NameNotFoundException(sb2.toString());
    }
    
    public static Map<Uri, ByteBuffer> prepareFontData(final Context context, final FontInfo[] array, final CancellationSignal cancellationSignal) {
        final HashMap<Uri, ByteBuffer> m = new HashMap<Uri, ByteBuffer>();
        for (final FontInfo fontInfo : array) {
            if (fontInfo.getResultCode() == 0) {
                final Uri uri = fontInfo.getUri();
                if (!m.containsKey(uri)) {
                    m.put(uri, TypefaceCompatUtil.mmap(context, cancellationSignal, uri));
                }
            }
        }
        return (Map<Uri, ByteBuffer>)Collections.unmodifiableMap((Map<?, ?>)m);
    }
    
    public static void requestFont(final Context context, final FontRequest fontRequest, final FontRequestCallback fontRequestCallback, final Handler handler) {
        handler.post((Runnable)new Runnable() {
            final /* synthetic */ Handler val$callerThreadHandler = new Handler();
            
            @Override
            public void run() {
                try {
                    final FontFamilyResult fetchFonts = FontsContractCompat.fetchFonts(context, null, fontRequest);
                    if (fetchFonts.getStatusCode() != 0) {
                        final int statusCode = fetchFonts.getStatusCode();
                        if (statusCode == 1) {
                            this.val$callerThreadHandler.post((Runnable)new Runnable() {
                                @Override
                                public void run() {
                                    fontRequestCallback.onTypefaceRequestFailed(-2);
                                }
                            });
                            return;
                        }
                        if (statusCode != 2) {
                            this.val$callerThreadHandler.post((Runnable)new Runnable() {
                                @Override
                                public void run() {
                                    fontRequestCallback.onTypefaceRequestFailed(-3);
                                }
                            });
                            return;
                        }
                        this.val$callerThreadHandler.post((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                fontRequestCallback.onTypefaceRequestFailed(-3);
                            }
                        });
                    }
                    else {
                        final FontInfo[] fonts = fetchFonts.getFonts();
                        if (fonts == null || fonts.length == 0) {
                            this.val$callerThreadHandler.post((Runnable)new Runnable() {
                                @Override
                                public void run() {
                                    fontRequestCallback.onTypefaceRequestFailed(1);
                                }
                            });
                            return;
                        }
                        for (final FontInfo fontInfo : fonts) {
                            if (fontInfo.getResultCode() != 0) {
                                final int resultCode = fontInfo.getResultCode();
                                if (resultCode < 0) {
                                    this.val$callerThreadHandler.post((Runnable)new Runnable() {
                                        @Override
                                        public void run() {
                                            fontRequestCallback.onTypefaceRequestFailed(-3);
                                        }
                                    });
                                }
                                else {
                                    this.val$callerThreadHandler.post((Runnable)new Runnable() {
                                        @Override
                                        public void run() {
                                            fontRequestCallback.onTypefaceRequestFailed(resultCode);
                                        }
                                    });
                                }
                                return;
                            }
                        }
                        final Typeface buildTypeface = FontsContractCompat.buildTypeface(context, null, fonts);
                        if (buildTypeface == null) {
                            this.val$callerThreadHandler.post((Runnable)new Runnable() {
                                @Override
                                public void run() {
                                    fontRequestCallback.onTypefaceRequestFailed(-3);
                                }
                            });
                            return;
                        }
                        this.val$callerThreadHandler.post((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                fontRequestCallback.onTypefaceRetrieved(buildTypeface);
                            }
                        });
                    }
                }
                catch (PackageManager$NameNotFoundException ex) {
                    this.val$callerThreadHandler.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            fontRequestCallback.onTypefaceRequestFailed(-1);
                        }
                    });
                }
            }
        });
    }
    
    public static void resetCache() {
        FontsContractCompat.sTypefaceCache.evictAll();
    }
    
    public static final class Columns implements BaseColumns
    {
        public static final String FILE_ID = "file_id";
        public static final String ITALIC = "font_italic";
        public static final String RESULT_CODE = "result_code";
        public static final int RESULT_CODE_FONT_NOT_FOUND = 1;
        public static final int RESULT_CODE_FONT_UNAVAILABLE = 2;
        public static final int RESULT_CODE_MALFORMED_QUERY = 3;
        public static final int RESULT_CODE_OK = 0;
        public static final String TTC_INDEX = "font_ttc_index";
        public static final String VARIATION_SETTINGS = "font_variation_settings";
        public static final String WEIGHT = "font_weight";
    }
    
    public static class FontFamilyResult
    {
        public static final int STATUS_OK = 0;
        public static final int STATUS_UNEXPECTED_DATA_PROVIDED = 2;
        public static final int STATUS_WRONG_CERTIFICATES = 1;
        private final FontInfo[] mFonts;
        private final int mStatusCode;
        
        public FontFamilyResult(final int mStatusCode, final FontInfo[] mFonts) {
            this.mStatusCode = mStatusCode;
            this.mFonts = mFonts;
        }
        
        public FontInfo[] getFonts() {
            return this.mFonts;
        }
        
        public int getStatusCode() {
            return this.mStatusCode;
        }
    }
    
    public static class FontInfo
    {
        private final boolean mItalic;
        private final int mResultCode;
        private final int mTtcIndex;
        private final Uri mUri;
        private final int mWeight;
        
        public FontInfo(final Uri uri, final int mTtcIndex, final int mWeight, final boolean mItalic, final int mResultCode) {
            this.mUri = Preconditions.checkNotNull(uri);
            this.mTtcIndex = mTtcIndex;
            this.mWeight = mWeight;
            this.mItalic = mItalic;
            this.mResultCode = mResultCode;
        }
        
        public int getResultCode() {
            return this.mResultCode;
        }
        
        public int getTtcIndex() {
            return this.mTtcIndex;
        }
        
        public Uri getUri() {
            return this.mUri;
        }
        
        public int getWeight() {
            return this.mWeight;
        }
        
        public boolean isItalic() {
            return this.mItalic;
        }
    }
    
    public static class FontRequestCallback
    {
        public static final int FAIL_REASON_FONT_LOAD_ERROR = -3;
        public static final int FAIL_REASON_FONT_NOT_FOUND = 1;
        public static final int FAIL_REASON_FONT_UNAVAILABLE = 2;
        public static final int FAIL_REASON_MALFORMED_QUERY = 3;
        public static final int FAIL_REASON_PROVIDER_NOT_FOUND = -1;
        public static final int FAIL_REASON_SECURITY_VIOLATION = -4;
        public static final int FAIL_REASON_WRONG_CERTIFICATES = -2;
        public static final int RESULT_OK = 0;
        
        public void onTypefaceRequestFailed(final int n) {
        }
        
        public void onTypefaceRetrieved(final Typeface typeface) {
        }
        
        @Retention(RetentionPolicy.SOURCE)
        public @interface FontRequestFailReason {
        }
    }
    
    private static final class TypefaceResult
    {
        final int mResult;
        final Typeface mTypeface;
        
        TypefaceResult(final Typeface mTypeface, final int mResult) {
            this.mTypeface = mTypeface;
            this.mResult = mResult;
        }
    }
}
