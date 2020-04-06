// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.content;

import java.util.Iterator;
import android.net.Uri$Builder;
import java.util.Map;
import android.text.TextUtils;
import android.database.MatrixCursor;
import android.database.Cursor;
import java.io.FileNotFoundException;
import android.os.ParcelFileDescriptor;
import android.content.ContentValues;
import android.webkit.MimeTypeMap;
import android.content.pm.ProviderInfo;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import android.content.res.XmlResourceParser;
import android.os.Build$VERSION;
import android.os.Environment;
import android.net.Uri;
import android.content.Context;
import java.util.HashMap;
import java.io.File;
import android.content.ContentProvider;

public class FileProvider extends ContentProvider
{
    private static final String ATTR_NAME = "name";
    private static final String ATTR_PATH = "path";
    private static final String[] COLUMNS;
    private static final File DEVICE_ROOT;
    private static final String META_DATA_FILE_PROVIDER_PATHS = "android.support.FILE_PROVIDER_PATHS";
    private static final String TAG_CACHE_PATH = "cache-path";
    private static final String TAG_EXTERNAL = "external-path";
    private static final String TAG_EXTERNAL_CACHE = "external-cache-path";
    private static final String TAG_EXTERNAL_FILES = "external-files-path";
    private static final String TAG_EXTERNAL_MEDIA = "external-media-path";
    private static final String TAG_FILES_PATH = "files-path";
    private static final String TAG_ROOT_PATH = "root-path";
    private static HashMap<String, PathStrategy> sCache;
    private PathStrategy mStrategy;
    
    static {
        COLUMNS = new String[] { "_display_name", "_size" };
        DEVICE_ROOT = new File("/");
        FileProvider.sCache = new HashMap<String, PathStrategy>();
    }
    
    private static File buildPath(File parent, final String... array) {
        File file;
        for (int length = array.length, i = 0; i < length; ++i, parent = file) {
            final String child = array[i];
            file = parent;
            if (child != null) {
                file = new File(parent, child);
            }
        }
        return parent;
    }
    
    private static Object[] copyOf(final Object[] array, final int n) {
        final Object[] array2 = new Object[n];
        System.arraycopy(array, 0, array2, 0, n);
        return array2;
    }
    
    private static String[] copyOf(final String[] array, final int n) {
        final String[] array2 = new String[n];
        System.arraycopy(array, 0, array2, 0, n);
        return array2;
    }
    
    private static PathStrategy getPathStrategy(final Context p0, final String p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: astore_2       
        //     4: aload_2        
        //     5: monitorenter   
        //     6: getstatic       androidx/core/content/FileProvider.sCache:Ljava/util/HashMap;
        //     9: aload_1        
        //    10: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    13: checkcast       Landroidx/core/content/FileProvider$PathStrategy;
        //    16: astore_3       
        //    17: aload_3        
        //    18: astore          4
        //    20: aload_3        
        //    21: ifnonnull       72
        //    24: aload_0        
        //    25: aload_1        
        //    26: invokestatic    androidx/core/content/FileProvider.parsePathStrategy:(Landroid/content/Context;Ljava/lang/String;)Landroidx/core/content/FileProvider$PathStrategy;
        //    29: astore          4
        //    31: getstatic       androidx/core/content/FileProvider.sCache:Ljava/util/HashMap;
        //    34: aload_1        
        //    35: aload           4
        //    37: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    40: pop            
        //    41: goto            72
        //    44: astore_0       
        //    45: new             Ljava/lang/IllegalArgumentException;
        //    48: astore_1       
        //    49: aload_1        
        //    50: ldc             "Failed to parse android.support.FILE_PROVIDER_PATHS meta-data"
        //    52: aload_0        
        //    53: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //    56: aload_1        
        //    57: athrow         
        //    58: astore_0       
        //    59: new             Ljava/lang/IllegalArgumentException;
        //    62: astore_1       
        //    63: aload_1        
        //    64: ldc             "Failed to parse android.support.FILE_PROVIDER_PATHS meta-data"
        //    66: aload_0        
        //    67: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //    70: aload_1        
        //    71: athrow         
        //    72: aload_2        
        //    73: monitorexit    
        //    74: aload           4
        //    76: areturn        
        //    77: astore_0       
        //    78: aload_2        
        //    79: monitorexit    
        //    80: aload_0        
        //    81: athrow         
        //    82: astore_0       
        //    83: goto            78
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                   
        //  -----  -----  -----  -----  ---------------------------------------
        //  6      17     77     78     Any
        //  24     31     58     72     Ljava/io/IOException;
        //  24     31     44     58     Lorg/xmlpull/v1/XmlPullParserException;
        //  24     31     82     86     Any
        //  31     41     82     86     Any
        //  45     58     82     86     Any
        //  59     72     82     86     Any
        //  72     74     82     86     Any
        //  78     80     82     86     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0072:
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
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static Uri getUriForFile(final Context context, final String s, final File file) {
        return getPathStrategy(context, s).getUriForFile(file);
    }
    
    private static int modeToMode(final String str) {
        int n;
        if ("r".equals(str)) {
            n = 268435456;
        }
        else if (!"w".equals(str) && !"wt".equals(str)) {
            if ("wa".equals(str)) {
                n = 704643072;
            }
            else if ("rw".equals(str)) {
                n = 939524096;
            }
            else {
                if (!"rwt".equals(str)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Invalid mode: ");
                    sb.append(str);
                    throw new IllegalArgumentException(sb.toString());
                }
                n = 1006632960;
            }
        }
        else {
            n = 738197504;
        }
        return n;
    }
    
    private static PathStrategy parsePathStrategy(final Context context, final String s) throws IOException, XmlPullParserException {
        final SimplePathStrategy simplePathStrategy = new SimplePathStrategy(s);
        final XmlResourceParser loadXmlMetaData = context.getPackageManager().resolveContentProvider(s, 128).loadXmlMetaData(context.getPackageManager(), "android.support.FILE_PROVIDER_PATHS");
        if (loadXmlMetaData != null) {
            while (true) {
                final int next = loadXmlMetaData.next();
                if (next == 1) {
                    break;
                }
                if (next != 2) {
                    continue;
                }
                final String name = loadXmlMetaData.getName();
                final String attributeValue = loadXmlMetaData.getAttributeValue((String)null, "name");
                final String attributeValue2 = loadXmlMetaData.getAttributeValue((String)null, "path");
                final File file = null;
                final File file2 = null;
                File file3 = null;
                if ("root-path".equals(name)) {
                    file3 = FileProvider.DEVICE_ROOT;
                }
                else if ("files-path".equals(name)) {
                    file3 = context.getFilesDir();
                }
                else if ("cache-path".equals(name)) {
                    file3 = context.getCacheDir();
                }
                else if ("external-path".equals(name)) {
                    file3 = Environment.getExternalStorageDirectory();
                }
                else if ("external-files-path".equals(name)) {
                    final File[] externalFilesDirs = ContextCompat.getExternalFilesDirs(context, null);
                    if (externalFilesDirs.length > 0) {
                        file3 = externalFilesDirs[0];
                    }
                }
                else if ("external-cache-path".equals(name)) {
                    final File[] externalCacheDirs = ContextCompat.getExternalCacheDirs(context);
                    file3 = file;
                    if (externalCacheDirs.length > 0) {
                        file3 = externalCacheDirs[0];
                    }
                }
                else {
                    file3 = file;
                    if (Build$VERSION.SDK_INT >= 21) {
                        file3 = file2;
                        if ("external-media-path".equals(name)) {
                            final File[] externalMediaDirs = context.getExternalMediaDirs();
                            file3 = file2;
                            if (externalMediaDirs.length > 0) {
                                file3 = externalMediaDirs[0];
                            }
                        }
                    }
                }
                if (file3 == null) {
                    continue;
                }
                simplePathStrategy.addRoot(attributeValue, buildPath(file3, attributeValue2));
            }
            return (PathStrategy)simplePathStrategy;
        }
        throw new IllegalArgumentException("Missing android.support.FILE_PROVIDER_PATHS meta-data");
    }
    
    public void attachInfo(final Context context, final ProviderInfo providerInfo) {
        super.attachInfo(context, providerInfo);
        if (providerInfo.exported) {
            throw new SecurityException("Provider must not be exported");
        }
        if (providerInfo.grantUriPermissions) {
            this.mStrategy = getPathStrategy(context, providerInfo.authority);
            return;
        }
        throw new SecurityException("Provider must grant uri permissions");
    }
    
    public int delete(final Uri uri, final String s, final String[] array) {
        return this.mStrategy.getFileForUri(uri).delete() ? 1 : 0;
    }
    
    public String getType(final Uri uri) {
        final File fileForUri = this.mStrategy.getFileForUri(uri);
        final int lastIndex = fileForUri.getName().lastIndexOf(46);
        if (lastIndex >= 0) {
            final String mimeTypeFromExtension = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileForUri.getName().substring(lastIndex + 1));
            if (mimeTypeFromExtension != null) {
                return mimeTypeFromExtension;
            }
        }
        return "application/octet-stream";
    }
    
    public Uri insert(final Uri uri, final ContentValues contentValues) {
        throw new UnsupportedOperationException("No external inserts");
    }
    
    public boolean onCreate() {
        return true;
    }
    
    public ParcelFileDescriptor openFile(final Uri uri, final String s) throws FileNotFoundException {
        return ParcelFileDescriptor.open(this.mStrategy.getFileForUri(uri), modeToMode(s));
    }
    
    public Cursor query(final Uri uri, final String[] array, final String s, String[] array2, String s2) {
        final File fileForUri = this.mStrategy.getFileForUri(uri);
        String[] columns = array;
        if (array == null) {
            columns = FileProvider.COLUMNS;
        }
        array2 = new String[columns.length];
        final Object[] array3 = new Object[columns.length];
        int n = 0;
        int n2;
        for (int length = columns.length, i = 0; i < length; ++i, n = n2) {
            s2 = columns[i];
            if ("_display_name".equals(s2)) {
                array2[n] = "_display_name";
                array3[n] = fileForUri.getName();
                n2 = n + 1;
            }
            else {
                n2 = n;
                if ("_size".equals(s2)) {
                    array2[n] = "_size";
                    array3[n] = fileForUri.length();
                    n2 = n + 1;
                }
            }
        }
        final String[] copy = copyOf(array2, n);
        final Object[] copy2 = copyOf(array3, n);
        final MatrixCursor matrixCursor = new MatrixCursor(copy, 1);
        matrixCursor.addRow(copy2);
        return (Cursor)matrixCursor;
    }
    
    public int update(final Uri uri, final ContentValues contentValues, final String s, final String[] array) {
        throw new UnsupportedOperationException("No external updates");
    }
    
    interface PathStrategy
    {
        File getFileForUri(final Uri p0);
        
        Uri getUriForFile(final File p0);
    }
    
    static class SimplePathStrategy implements PathStrategy
    {
        private final String mAuthority;
        private final HashMap<String, File> mRoots;
        
        SimplePathStrategy(final String mAuthority) {
            this.mRoots = new HashMap<String, File>();
            this.mAuthority = mAuthority;
        }
        
        void addRoot(final String key, final File obj) {
            if (!TextUtils.isEmpty((CharSequence)key)) {
                try {
                    this.mRoots.put(key, obj.getCanonicalFile());
                    return;
                }
                catch (IOException cause) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Failed to resolve canonical path for ");
                    sb.append(obj);
                    throw new IllegalArgumentException(sb.toString(), cause);
                }
            }
            throw new IllegalArgumentException("Name must not be empty");
        }
        
        @Override
        public File getFileForUri(Uri uri) {
            final String encodedPath = uri.getEncodedPath();
            final int index = encodedPath.indexOf(47, 1);
            final String decode = Uri.decode(encodedPath.substring(1, index));
            final String decode2 = Uri.decode(encodedPath.substring(index + 1));
            final File parent = this.mRoots.get(decode);
            if (parent != null) {
                uri = (Uri)new File(parent, decode2);
                try {
                    final File canonicalFile = ((File)uri).getCanonicalFile();
                    if (canonicalFile.getPath().startsWith(parent.getPath())) {
                        return canonicalFile;
                    }
                    throw new SecurityException("Resolved path jumped beyond configured root");
                }
                catch (IOException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Failed to resolve canonical path for ");
                    sb.append(uri);
                    throw new IllegalArgumentException(sb.toString());
                }
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Unable to find configured root for ");
            sb2.append(uri);
            throw new IllegalArgumentException(sb2.toString());
        }
        
        @Override
        public Uri getUriForFile(File string) {
            try {
                final String canonicalPath = string.getCanonicalPath();
                string = null;
                for (final Map.Entry<String, File> entry : this.mRoots.entrySet()) {
                    final String path = entry.getValue().getPath();
                    Object o = string;
                    Label_0101: {
                        if (canonicalPath.startsWith(path)) {
                            if (string != null) {
                                o = string;
                                if (path.length() <= ((Map.Entry<K, File>)string).getValue().getPath().length()) {
                                    break Label_0101;
                                }
                            }
                            o = entry;
                        }
                    }
                    string = (File)o;
                }
                if (string != null) {
                    final String path2 = ((Map.Entry<K, File>)string).getValue().getPath();
                    String s;
                    if (path2.endsWith("/")) {
                        s = canonicalPath.substring(path2.length());
                    }
                    else {
                        s = canonicalPath.substring(path2.length() + 1);
                    }
                    final StringBuilder sb = new StringBuilder();
                    sb.append(Uri.encode((String)((Map.Entry<String, V>)string).getKey()));
                    sb.append('/');
                    sb.append(Uri.encode(s, "/"));
                    string = (File)sb.toString();
                    return new Uri$Builder().scheme("content").authority(this.mAuthority).encodedPath((String)string).build();
                }
                string = (File)new StringBuilder();
                ((StringBuilder)string).append("Failed to find configured root that contains ");
                ((StringBuilder)string).append(canonicalPath);
                throw new IllegalArgumentException(((StringBuilder)string).toString());
            }
            catch (IOException ex) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Failed to resolve canonical path for ");
                sb2.append(string);
                throw new IllegalArgumentException(sb2.toString());
            }
        }
    }
}
