// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.widget;

import java.math.BigDecimal;
import android.content.ComponentName;
import java.util.Collections;
import java.util.Collection;
import android.os.AsyncTask;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.Intent;
import android.content.Context;
import java.util.List;
import java.util.Map;
import android.database.DataSetObservable;

class ActivityChooserModel extends DataSetObservable
{
    static final String ATTRIBUTE_ACTIVITY = "activity";
    static final String ATTRIBUTE_TIME = "time";
    static final String ATTRIBUTE_WEIGHT = "weight";
    static final boolean DEBUG = false;
    private static final int DEFAULT_ACTIVITY_INFLATION = 5;
    private static final float DEFAULT_HISTORICAL_RECORD_WEIGHT = 1.0f;
    public static final String DEFAULT_HISTORY_FILE_NAME = "activity_choser_model_history.xml";
    public static final int DEFAULT_HISTORY_MAX_LENGTH = 50;
    private static final String HISTORY_FILE_EXTENSION = ".xml";
    private static final int INVALID_INDEX = -1;
    static final String LOG_TAG;
    static final String TAG_HISTORICAL_RECORD = "historical-record";
    static final String TAG_HISTORICAL_RECORDS = "historical-records";
    private static final Map<String, ActivityChooserModel> sDataModelRegistry;
    private static final Object sRegistryLock;
    private final List<ActivityResolveInfo> mActivities;
    private OnChooseActivityListener mActivityChoserModelPolicy;
    private ActivitySorter mActivitySorter;
    boolean mCanReadHistoricalData;
    final Context mContext;
    private final List<HistoricalRecord> mHistoricalRecords;
    private boolean mHistoricalRecordsChanged;
    final String mHistoryFileName;
    private int mHistoryMaxSize;
    private final Object mInstanceLock;
    private Intent mIntent;
    private boolean mReadShareHistoryCalled;
    private boolean mReloadActivities;
    
    static {
        LOG_TAG = ActivityChooserModel.class.getSimpleName();
        sRegistryLock = new Object();
        sDataModelRegistry = new HashMap<String, ActivityChooserModel>();
    }
    
    private ActivityChooserModel(final Context context, final String s) {
        this.mInstanceLock = new Object();
        this.mActivities = new ArrayList<ActivityResolveInfo>();
        this.mHistoricalRecords = new ArrayList<HistoricalRecord>();
        this.mActivitySorter = (ActivitySorter)new DefaultSorter();
        this.mHistoryMaxSize = 50;
        this.mCanReadHistoricalData = true;
        this.mReadShareHistoryCalled = false;
        this.mHistoricalRecordsChanged = true;
        this.mReloadActivities = false;
        this.mContext = context.getApplicationContext();
        if (!TextUtils.isEmpty((CharSequence)s) && !s.endsWith(".xml")) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(".xml");
            this.mHistoryFileName = sb.toString();
        }
        else {
            this.mHistoryFileName = s;
        }
    }
    
    private boolean addHistoricalRecord(final HistoricalRecord historicalRecord) {
        final boolean add = this.mHistoricalRecords.add(historicalRecord);
        if (add) {
            this.mHistoricalRecordsChanged = true;
            this.pruneExcessiveHistoricalRecordsIfNeeded();
            this.persistHistoricalDataIfNeeded();
            this.sortActivitiesIfNeeded();
            this.notifyChanged();
        }
        return add;
    }
    
    private void ensureConsistentState() {
        final boolean loadActivitiesIfNeeded = this.loadActivitiesIfNeeded();
        final boolean historicalDataIfNeeded = this.readHistoricalDataIfNeeded();
        this.pruneExcessiveHistoricalRecordsIfNeeded();
        if (loadActivitiesIfNeeded | historicalDataIfNeeded) {
            this.sortActivitiesIfNeeded();
            this.notifyChanged();
        }
    }
    
    public static ActivityChooserModel get(final Context context, final String s) {
        synchronized (ActivityChooserModel.sRegistryLock) {
            ActivityChooserModel activityChooserModel;
            if ((activityChooserModel = ActivityChooserModel.sDataModelRegistry.get(s)) == null) {
                activityChooserModel = new ActivityChooserModel(context, s);
                ActivityChooserModel.sDataModelRegistry.put(s, activityChooserModel);
            }
            return activityChooserModel;
        }
    }
    
    private boolean loadActivitiesIfNeeded() {
        if (this.mReloadActivities && this.mIntent != null) {
            this.mReloadActivities = false;
            this.mActivities.clear();
            final List queryIntentActivities = this.mContext.getPackageManager().queryIntentActivities(this.mIntent, 0);
            for (int size = queryIntentActivities.size(), i = 0; i < size; ++i) {
                this.mActivities.add(new ActivityResolveInfo(queryIntentActivities.get(i)));
            }
            return true;
        }
        return false;
    }
    
    private void persistHistoricalDataIfNeeded() {
        if (!this.mReadShareHistoryCalled) {
            throw new IllegalStateException("No preceding call to #readHistoricalData");
        }
        if (!this.mHistoricalRecordsChanged) {
            return;
        }
        this.mHistoricalRecordsChanged = false;
        if (!TextUtils.isEmpty((CharSequence)this.mHistoryFileName)) {
            new PersistHistoryAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[] { new ArrayList(this.mHistoricalRecords), this.mHistoryFileName });
        }
    }
    
    private void pruneExcessiveHistoricalRecordsIfNeeded() {
        final int n = this.mHistoricalRecords.size() - this.mHistoryMaxSize;
        if (n <= 0) {
            return;
        }
        this.mHistoricalRecordsChanged = true;
        for (int i = 0; i < n; ++i) {
            final HistoricalRecord historicalRecord = this.mHistoricalRecords.remove(0);
        }
    }
    
    private boolean readHistoricalDataIfNeeded() {
        if (this.mCanReadHistoricalData && this.mHistoricalRecordsChanged && !TextUtils.isEmpty((CharSequence)this.mHistoryFileName)) {
            this.mCanReadHistoricalData = false;
            this.mReadShareHistoryCalled = true;
            this.readHistoricalDataImpl();
            return true;
        }
        return false;
    }
    
    private void readHistoricalDataImpl() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        androidx/appcompat/widget/ActivityChooserModel.mContext:Landroid/content/Context;
        //     4: aload_0        
        //     5: getfield        androidx/appcompat/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
        //     8: invokevirtual   android/content/Context.openFileInput:(Ljava/lang/String;)Ljava/io/FileInputStream;
        //    11: astore_1       
        //    12: invokestatic    android/util/Xml.newPullParser:()Lorg/xmlpull/v1/XmlPullParser;
        //    15: astore_2       
        //    16: aload_2        
        //    17: aload_1        
        //    18: ldc_w           "UTF-8"
        //    21: invokeinterface org/xmlpull/v1/XmlPullParser.setInput:(Ljava/io/InputStream;Ljava/lang/String;)V
        //    26: iconst_0       
        //    27: istore_3       
        //    28: iload_3        
        //    29: iconst_1       
        //    30: if_icmpeq       48
        //    33: iload_3        
        //    34: iconst_2       
        //    35: if_icmpeq       48
        //    38: aload_2        
        //    39: invokeinterface org/xmlpull/v1/XmlPullParser.next:()I
        //    44: istore_3       
        //    45: goto            28
        //    48: ldc             "historical-records"
        //    50: aload_2        
        //    51: invokeinterface org/xmlpull/v1/XmlPullParser.getName:()Ljava/lang/String;
        //    56: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    59: ifeq            209
        //    62: aload_0        
        //    63: getfield        androidx/appcompat/widget/ActivityChooserModel.mHistoricalRecords:Ljava/util/List;
        //    66: astore          4
        //    68: aload           4
        //    70: invokeinterface java/util/List.clear:()V
        //    75: aload_2        
        //    76: invokeinterface org/xmlpull/v1/XmlPullParser.next:()I
        //    81: istore_3       
        //    82: iload_3        
        //    83: iconst_1       
        //    84: if_icmpne       98
        //    87: aload_1        
        //    88: ifnull          350
        //    91: aload_1        
        //    92: invokevirtual   java/io/FileInputStream.close:()V
        //    95: goto            343
        //    98: iload_3        
        //    99: iconst_3       
        //   100: if_icmpeq       75
        //   103: iload_3        
        //   104: iconst_4       
        //   105: if_icmpne       111
        //   108: goto            75
        //   111: ldc             "historical-record"
        //   113: aload_2        
        //   114: invokeinterface org/xmlpull/v1/XmlPullParser.getName:()Ljava/lang/String;
        //   119: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   122: ifeq            193
        //   125: aload_2        
        //   126: aconst_null    
        //   127: ldc             "activity"
        //   129: invokeinterface org/xmlpull/v1/XmlPullParser.getAttributeValue:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   134: astore          5
        //   136: aload_2        
        //   137: aconst_null    
        //   138: ldc             "time"
        //   140: invokeinterface org/xmlpull/v1/XmlPullParser.getAttributeValue:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   145: invokestatic    java/lang/Long.parseLong:(Ljava/lang/String;)J
        //   148: lstore          6
        //   150: aload_2        
        //   151: aconst_null    
        //   152: ldc             "weight"
        //   154: invokeinterface org/xmlpull/v1/XmlPullParser.getAttributeValue:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   159: invokestatic    java/lang/Float.parseFloat:(Ljava/lang/String;)F
        //   162: fstore          8
        //   164: new             Landroidx/appcompat/widget/ActivityChooserModel$HistoricalRecord;
        //   167: astore          9
        //   169: aload           9
        //   171: aload           5
        //   173: lload           6
        //   175: fload           8
        //   177: invokespecial   androidx/appcompat/widget/ActivityChooserModel$HistoricalRecord.<init>:(Ljava/lang/String;JF)V
        //   180: aload           4
        //   182: aload           9
        //   184: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   189: pop            
        //   190: goto            75
        //   193: new             Lorg/xmlpull/v1/XmlPullParserException;
        //   196: astore          9
        //   198: aload           9
        //   200: ldc_w           "Share records file not well-formed."
        //   203: invokespecial   org/xmlpull/v1/XmlPullParserException.<init>:(Ljava/lang/String;)V
        //   206: aload           9
        //   208: athrow         
        //   209: new             Lorg/xmlpull/v1/XmlPullParserException;
        //   212: astore          9
        //   214: aload           9
        //   216: ldc_w           "Share records file does not start with historical-records tag."
        //   219: invokespecial   org/xmlpull/v1/XmlPullParserException.<init>:(Ljava/lang/String;)V
        //   222: aload           9
        //   224: athrow         
        //   225: astore          9
        //   227: goto            351
        //   230: astore_2       
        //   231: getstatic       androidx/appcompat/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
        //   234: astore          5
        //   236: new             Ljava/lang/StringBuilder;
        //   239: astore          9
        //   241: aload           9
        //   243: invokespecial   java/lang/StringBuilder.<init>:()V
        //   246: aload           9
        //   248: ldc_w           "Error reading historical recrod file: "
        //   251: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   254: pop            
        //   255: aload           9
        //   257: aload_0        
        //   258: getfield        androidx/appcompat/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
        //   261: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   264: pop            
        //   265: aload           5
        //   267: aload           9
        //   269: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   272: aload_2        
        //   273: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   276: pop            
        //   277: aload_1        
        //   278: ifnull          350
        //   281: aload_1        
        //   282: invokevirtual   java/io/FileInputStream.close:()V
        //   285: goto            343
        //   288: astore          5
        //   290: getstatic       androidx/appcompat/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
        //   293: astore_2       
        //   294: new             Ljava/lang/StringBuilder;
        //   297: astore          9
        //   299: aload           9
        //   301: invokespecial   java/lang/StringBuilder.<init>:()V
        //   304: aload           9
        //   306: ldc_w           "Error reading historical recrod file: "
        //   309: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   312: pop            
        //   313: aload           9
        //   315: aload_0        
        //   316: getfield        androidx/appcompat/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
        //   319: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   322: pop            
        //   323: aload_2        
        //   324: aload           9
        //   326: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   329: aload           5
        //   331: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   334: pop            
        //   335: aload_1        
        //   336: ifnull          350
        //   339: aload_1        
        //   340: invokevirtual   java/io/FileInputStream.close:()V
        //   343: goto            350
        //   346: astore_1       
        //   347: goto            343
        //   350: return         
        //   351: aload_1        
        //   352: ifnull          363
        //   355: aload_1        
        //   356: invokevirtual   java/io/FileInputStream.close:()V
        //   359: goto            363
        //   362: astore_1       
        //   363: aload           9
        //   365: athrow         
        //   366: astore_1       
        //   367: return         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                   
        //  -----  -----  -----  -----  ---------------------------------------
        //  0      12     366    368    Ljava/io/FileNotFoundException;
        //  12     26     288    343    Lorg/xmlpull/v1/XmlPullParserException;
        //  12     26     230    288    Ljava/io/IOException;
        //  12     26     225    366    Any
        //  38     45     288    343    Lorg/xmlpull/v1/XmlPullParserException;
        //  38     45     230    288    Ljava/io/IOException;
        //  38     45     225    366    Any
        //  48     75     288    343    Lorg/xmlpull/v1/XmlPullParserException;
        //  48     75     230    288    Ljava/io/IOException;
        //  48     75     225    366    Any
        //  75     82     288    343    Lorg/xmlpull/v1/XmlPullParserException;
        //  75     82     230    288    Ljava/io/IOException;
        //  75     82     225    366    Any
        //  91     95     346    350    Ljava/io/IOException;
        //  111    190    288    343    Lorg/xmlpull/v1/XmlPullParserException;
        //  111    190    230    288    Ljava/io/IOException;
        //  111    190    225    366    Any
        //  193    209    288    343    Lorg/xmlpull/v1/XmlPullParserException;
        //  193    209    230    288    Ljava/io/IOException;
        //  193    209    225    366    Any
        //  209    225    288    343    Lorg/xmlpull/v1/XmlPullParserException;
        //  209    225    230    288    Ljava/io/IOException;
        //  209    225    225    366    Any
        //  231    277    225    366    Any
        //  281    285    346    350    Ljava/io/IOException;
        //  290    335    225    366    Any
        //  339    343    346    350    Ljava/io/IOException;
        //  355    359    362    363    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0098:
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
    
    private boolean sortActivitiesIfNeeded() {
        if (this.mActivitySorter != null && this.mIntent != null && !this.mActivities.isEmpty() && !this.mHistoricalRecords.isEmpty()) {
            this.mActivitySorter.sort(this.mIntent, this.mActivities, Collections.unmodifiableList((List<? extends HistoricalRecord>)this.mHistoricalRecords));
            return true;
        }
        return false;
    }
    
    public Intent chooseActivity(final int n) {
        synchronized (this.mInstanceLock) {
            if (this.mIntent == null) {
                return null;
            }
            this.ensureConsistentState();
            final ActivityResolveInfo activityResolveInfo = this.mActivities.get(n);
            final ComponentName component = new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name);
            final Intent intent = new Intent(this.mIntent);
            intent.setComponent(component);
            if (this.mActivityChoserModelPolicy != null && this.mActivityChoserModelPolicy.onChooseActivity(this, new Intent(intent))) {
                return null;
            }
            this.addHistoricalRecord(new HistoricalRecord(component, System.currentTimeMillis(), 1.0f));
            return intent;
        }
    }
    
    public ResolveInfo getActivity(final int n) {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            return this.mActivities.get(n).resolveInfo;
        }
    }
    
    public int getActivityCount() {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            return this.mActivities.size();
        }
    }
    
    public int getActivityIndex(final ResolveInfo resolveInfo) {
        final Object mInstanceLock = this.mInstanceLock;
        // monitorenter(mInstanceLock)
        try {
            this.ensureConsistentState();
            final List<ActivityResolveInfo> mActivities = this.mActivities;
            for (int size = mActivities.size(), i = 0; i < size; ++i) {
                if (mActivities.get(i).resolveInfo == resolveInfo) {
                    // monitorexit(mInstanceLock)
                    return i;
                }
            }
            // monitorexit(mInstanceLock)
            return -1;
        }
        finally {
            // monitorexit(mInstanceLock)
            while (true) {}
        }
    }
    
    public ResolveInfo getDefaultActivity() {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            if (!this.mActivities.isEmpty()) {
                return this.mActivities.get(0).resolveInfo;
            }
            return null;
        }
    }
    
    public int getHistoryMaxSize() {
        synchronized (this.mInstanceLock) {
            return this.mHistoryMaxSize;
        }
    }
    
    public int getHistorySize() {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            return this.mHistoricalRecords.size();
        }
    }
    
    public Intent getIntent() {
        synchronized (this.mInstanceLock) {
            return this.mIntent;
        }
    }
    
    public void setActivitySorter(final ActivitySorter mActivitySorter) {
        synchronized (this.mInstanceLock) {
            if (this.mActivitySorter == mActivitySorter) {
                return;
            }
            this.mActivitySorter = mActivitySorter;
            if (this.sortActivitiesIfNeeded()) {
                this.notifyChanged();
            }
        }
    }
    
    public void setDefaultActivity(final int n) {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            final ActivityResolveInfo activityResolveInfo = this.mActivities.get(n);
            final ActivityResolveInfo activityResolveInfo2 = this.mActivities.get(0);
            float n2;
            if (activityResolveInfo2 != null) {
                n2 = activityResolveInfo2.weight - activityResolveInfo.weight + 5.0f;
            }
            else {
                n2 = 1.0f;
            }
            this.addHistoricalRecord(new HistoricalRecord(new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name), System.currentTimeMillis(), n2));
        }
    }
    
    public void setHistoryMaxSize(final int mHistoryMaxSize) {
        synchronized (this.mInstanceLock) {
            if (this.mHistoryMaxSize == mHistoryMaxSize) {
                return;
            }
            this.mHistoryMaxSize = mHistoryMaxSize;
            this.pruneExcessiveHistoricalRecordsIfNeeded();
            if (this.sortActivitiesIfNeeded()) {
                this.notifyChanged();
            }
        }
    }
    
    public void setIntent(final Intent mIntent) {
        synchronized (this.mInstanceLock) {
            if (this.mIntent == mIntent) {
                return;
            }
            this.mIntent = mIntent;
            this.mReloadActivities = true;
            this.ensureConsistentState();
        }
    }
    
    public void setOnChooseActivityListener(final OnChooseActivityListener mActivityChoserModelPolicy) {
        synchronized (this.mInstanceLock) {
            this.mActivityChoserModelPolicy = mActivityChoserModelPolicy;
        }
    }
    
    public interface ActivityChooserModelClient
    {
        void setActivityChooserModel(final ActivityChooserModel p0);
    }
    
    public static final class ActivityResolveInfo implements Comparable<ActivityResolveInfo>
    {
        public final ResolveInfo resolveInfo;
        public float weight;
        
        public ActivityResolveInfo(final ResolveInfo resolveInfo) {
            this.resolveInfo = resolveInfo;
        }
        
        @Override
        public int compareTo(final ActivityResolveInfo activityResolveInfo) {
            return Float.floatToIntBits(activityResolveInfo.weight) - Float.floatToIntBits(this.weight);
        }
        
        @Override
        public boolean equals(final Object o) {
            return this == o || (o != null && this.getClass() == o.getClass() && Float.floatToIntBits(this.weight) == Float.floatToIntBits(((ActivityResolveInfo)o).weight));
        }
        
        @Override
        public int hashCode() {
            return Float.floatToIntBits(this.weight) + 31;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append("resolveInfo:");
            sb.append(this.resolveInfo.toString());
            sb.append("; weight:");
            sb.append(new BigDecimal(this.weight));
            sb.append("]");
            return sb.toString();
        }
    }
    
    public interface ActivitySorter
    {
        void sort(final Intent p0, final List<ActivityResolveInfo> p1, final List<HistoricalRecord> p2);
    }
    
    private static final class DefaultSorter implements ActivitySorter
    {
        private static final float WEIGHT_DECAY_COEFFICIENT = 0.95f;
        private final Map<ComponentName, ActivityResolveInfo> mPackageNameToActivityMap;
        
        DefaultSorter() {
            this.mPackageNameToActivityMap = new HashMap<ComponentName, ActivityResolveInfo>();
        }
        
        @Override
        public void sort(final Intent intent, final List<ActivityResolveInfo> list, final List<HistoricalRecord> list2) {
            final Map<ComponentName, ActivityResolveInfo> mPackageNameToActivityMap = this.mPackageNameToActivityMap;
            mPackageNameToActivityMap.clear();
            for (int size = list.size(), i = 0; i < size; ++i) {
                final ActivityResolveInfo activityResolveInfo = list.get(i);
                activityResolveInfo.weight = 0.0f;
                mPackageNameToActivityMap.put(new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name), activityResolveInfo);
            }
            int j = list2.size();
            float n = 1.0f;
            --j;
            while (j >= 0) {
                final HistoricalRecord historicalRecord = list2.get(j);
                final ActivityResolveInfo activityResolveInfo2 = mPackageNameToActivityMap.get(historicalRecord.activity);
                float n2 = n;
                if (activityResolveInfo2 != null) {
                    activityResolveInfo2.weight += historicalRecord.weight * n;
                    n2 = n * 0.95f;
                }
                --j;
                n = n2;
            }
            Collections.sort((List<Comparable>)list);
        }
    }
    
    public static final class HistoricalRecord
    {
        public final ComponentName activity;
        public final long time;
        public final float weight;
        
        public HistoricalRecord(final ComponentName activity, final long time, final float weight) {
            this.activity = activity;
            this.time = time;
            this.weight = weight;
        }
        
        public HistoricalRecord(final String s, final long n, final float n2) {
            this(ComponentName.unflattenFromString(s), n, n2);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final HistoricalRecord historicalRecord = (HistoricalRecord)o;
            final ComponentName activity = this.activity;
            if (activity == null) {
                if (historicalRecord.activity != null) {
                    return false;
                }
            }
            else if (!activity.equals((Object)historicalRecord.activity)) {
                return false;
            }
            return this.time == historicalRecord.time && Float.floatToIntBits(this.weight) == Float.floatToIntBits(historicalRecord.weight);
        }
        
        @Override
        public int hashCode() {
            final ComponentName activity = this.activity;
            int hashCode;
            if (activity == null) {
                hashCode = 0;
            }
            else {
                hashCode = activity.hashCode();
            }
            final long time = this.time;
            return ((1 * 31 + hashCode) * 31 + (int)(time ^ time >>> 32)) * 31 + Float.floatToIntBits(this.weight);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append("; activity:");
            sb.append(this.activity);
            sb.append("; time:");
            sb.append(this.time);
            sb.append("; weight:");
            sb.append(new BigDecimal(this.weight));
            sb.append("]");
            return sb.toString();
        }
    }
    
    public interface OnChooseActivityListener
    {
        boolean onChooseActivity(final ActivityChooserModel p0, final Intent p1);
    }
    
    private final class PersistHistoryAsyncTask extends AsyncTask<Object, Void, Void>
    {
        PersistHistoryAsyncTask() {
        }
        
        public Void doInBackground(final Object... p0) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: iconst_0       
            //     2: aaload         
            //     3: checkcast       Ljava/util/List;
            //     6: astore_2       
            //     7: aload_1        
            //     8: iconst_1       
            //     9: aaload         
            //    10: checkcast       Ljava/lang/String;
            //    13: astore_1       
            //    14: aload_0        
            //    15: getfield        androidx/appcompat/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroidx/appcompat/widget/ActivityChooserModel;
            //    18: getfield        androidx/appcompat/widget/ActivityChooserModel.mContext:Landroid/content/Context;
            //    21: aload_1        
            //    22: iconst_0       
            //    23: invokevirtual   android/content/Context.openFileOutput:(Ljava/lang/String;I)Ljava/io/FileOutputStream;
            //    26: astore_3       
            //    27: invokestatic    android/util/Xml.newSerializer:()Lorg/xmlpull/v1/XmlSerializer;
            //    30: astore          4
            //    32: aload_2        
            //    33: astore          5
            //    35: aload_2        
            //    36: astore          5
            //    38: aload_2        
            //    39: astore          5
            //    41: aload_2        
            //    42: astore          5
            //    44: aload           4
            //    46: aload_3        
            //    47: aconst_null    
            //    48: invokeinterface org/xmlpull/v1/XmlSerializer.setOutput:(Ljava/io/OutputStream;Ljava/lang/String;)V
            //    53: aload_2        
            //    54: astore          5
            //    56: aload_2        
            //    57: astore          5
            //    59: aload_2        
            //    60: astore          5
            //    62: aload_2        
            //    63: astore          5
            //    65: aload           4
            //    67: ldc             "UTF-8"
            //    69: iconst_1       
            //    70: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
            //    73: invokeinterface org/xmlpull/v1/XmlSerializer.startDocument:(Ljava/lang/String;Ljava/lang/Boolean;)V
            //    78: aload_2        
            //    79: astore          5
            //    81: aload_2        
            //    82: astore          5
            //    84: aload_2        
            //    85: astore          5
            //    87: aload_2        
            //    88: astore          5
            //    90: aload           4
            //    92: aconst_null    
            //    93: ldc             "historical-records"
            //    95: invokeinterface org/xmlpull/v1/XmlSerializer.startTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   100: pop            
            //   101: aload_2        
            //   102: astore          5
            //   104: aload_2        
            //   105: astore          5
            //   107: aload_2        
            //   108: astore          5
            //   110: aload_2        
            //   111: astore          5
            //   113: aload_2        
            //   114: invokeinterface java/util/List.size:()I
            //   119: istore          6
            //   121: iconst_0       
            //   122: istore          7
            //   124: aload_2        
            //   125: astore_1       
            //   126: iload           7
            //   128: iload           6
            //   130: if_icmpge       262
            //   133: aload_1        
            //   134: astore          5
            //   136: aload_1        
            //   137: astore          5
            //   139: aload_1        
            //   140: astore          5
            //   142: aload_1        
            //   143: astore          5
            //   145: aload_1        
            //   146: iconst_0       
            //   147: invokeinterface java/util/List.remove:(I)Ljava/lang/Object;
            //   152: checkcast       Landroidx/appcompat/widget/ActivityChooserModel$HistoricalRecord;
            //   155: astore_2       
            //   156: aload_1        
            //   157: astore          5
            //   159: aload_1        
            //   160: astore          5
            //   162: aload_1        
            //   163: astore          5
            //   165: aload_1        
            //   166: astore          5
            //   168: aload           4
            //   170: aconst_null    
            //   171: ldc             "historical-record"
            //   173: invokeinterface org/xmlpull/v1/XmlSerializer.startTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   178: pop            
            //   179: aload_1        
            //   180: astore          5
            //   182: aload_1        
            //   183: astore          5
            //   185: aload_1        
            //   186: astore          5
            //   188: aload_1        
            //   189: astore          5
            //   191: aload           4
            //   193: aconst_null    
            //   194: ldc             "activity"
            //   196: aload_2        
            //   197: getfield        androidx/appcompat/widget/ActivityChooserModel$HistoricalRecord.activity:Landroid/content/ComponentName;
            //   200: invokevirtual   android/content/ComponentName.flattenToString:()Ljava/lang/String;
            //   203: invokeinterface org/xmlpull/v1/XmlSerializer.attribute:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   208: pop            
            //   209: aload           4
            //   211: aconst_null    
            //   212: ldc             "time"
            //   214: aload_2        
            //   215: getfield        androidx/appcompat/widget/ActivityChooserModel$HistoricalRecord.time:J
            //   218: invokestatic    java/lang/String.valueOf:(J)Ljava/lang/String;
            //   221: invokeinterface org/xmlpull/v1/XmlSerializer.attribute:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   226: pop            
            //   227: aload           4
            //   229: aconst_null    
            //   230: ldc             "weight"
            //   232: aload_2        
            //   233: getfield        androidx/appcompat/widget/ActivityChooserModel$HistoricalRecord.weight:F
            //   236: invokestatic    java/lang/String.valueOf:(F)Ljava/lang/String;
            //   239: invokeinterface org/xmlpull/v1/XmlSerializer.attribute:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   244: pop            
            //   245: aload           4
            //   247: aconst_null    
            //   248: ldc             "historical-record"
            //   250: invokeinterface org/xmlpull/v1/XmlSerializer.endTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   255: pop            
            //   256: iinc            7, 1
            //   259: goto            126
            //   262: aload           4
            //   264: aconst_null    
            //   265: ldc             "historical-records"
            //   267: invokeinterface org/xmlpull/v1/XmlSerializer.endTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   272: pop            
            //   273: aload           4
            //   275: invokeinterface org/xmlpull/v1/XmlSerializer.endDocument:()V
            //   280: aload_0        
            //   281: getfield        androidx/appcompat/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroidx/appcompat/widget/ActivityChooserModel;
            //   284: iconst_1       
            //   285: putfield        androidx/appcompat/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   288: aload_3        
            //   289: ifnull          517
            //   292: aload_3        
            //   293: invokevirtual   java/io/FileOutputStream.close:()V
            //   296: goto            510
            //   299: astore_1       
            //   300: goto            316
            //   303: astore_1       
            //   304: goto            382
            //   307: astore_1       
            //   308: goto            448
            //   311: astore_1       
            //   312: goto            520
            //   315: astore_1       
            //   316: getstatic       androidx/appcompat/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   319: astore_2       
            //   320: new             Ljava/lang/StringBuilder;
            //   323: astore          5
            //   325: aload           5
            //   327: invokespecial   java/lang/StringBuilder.<init>:()V
            //   330: aload           5
            //   332: ldc             "Error writing historical record file: "
            //   334: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   337: pop            
            //   338: aload           5
            //   340: aload_0        
            //   341: getfield        androidx/appcompat/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroidx/appcompat/widget/ActivityChooserModel;
            //   344: getfield        androidx/appcompat/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
            //   347: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   350: pop            
            //   351: aload_2        
            //   352: aload           5
            //   354: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   357: aload_1        
            //   358: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   361: pop            
            //   362: aload_0        
            //   363: getfield        androidx/appcompat/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroidx/appcompat/widget/ActivityChooserModel;
            //   366: iconst_1       
            //   367: putfield        androidx/appcompat/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   370: aload_3        
            //   371: ifnull          517
            //   374: aload_3        
            //   375: invokevirtual   java/io/FileOutputStream.close:()V
            //   378: goto            510
            //   381: astore_1       
            //   382: getstatic       androidx/appcompat/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   385: astore_2       
            //   386: new             Ljava/lang/StringBuilder;
            //   389: astore          5
            //   391: aload           5
            //   393: invokespecial   java/lang/StringBuilder.<init>:()V
            //   396: aload           5
            //   398: ldc             "Error writing historical record file: "
            //   400: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   403: pop            
            //   404: aload           5
            //   406: aload_0        
            //   407: getfield        androidx/appcompat/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroidx/appcompat/widget/ActivityChooserModel;
            //   410: getfield        androidx/appcompat/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
            //   413: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   416: pop            
            //   417: aload_2        
            //   418: aload           5
            //   420: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   423: aload_1        
            //   424: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   427: pop            
            //   428: aload_0        
            //   429: getfield        androidx/appcompat/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroidx/appcompat/widget/ActivityChooserModel;
            //   432: iconst_1       
            //   433: putfield        androidx/appcompat/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   436: aload_3        
            //   437: ifnull          517
            //   440: aload_3        
            //   441: invokevirtual   java/io/FileOutputStream.close:()V
            //   444: goto            510
            //   447: astore_1       
            //   448: getstatic       androidx/appcompat/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   451: astore_2       
            //   452: new             Ljava/lang/StringBuilder;
            //   455: astore          5
            //   457: aload           5
            //   459: invokespecial   java/lang/StringBuilder.<init>:()V
            //   462: aload           5
            //   464: ldc             "Error writing historical record file: "
            //   466: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   469: pop            
            //   470: aload           5
            //   472: aload_0        
            //   473: getfield        androidx/appcompat/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroidx/appcompat/widget/ActivityChooserModel;
            //   476: getfield        androidx/appcompat/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
            //   479: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   482: pop            
            //   483: aload_2        
            //   484: aload           5
            //   486: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   489: aload_1        
            //   490: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   493: pop            
            //   494: aload_0        
            //   495: getfield        androidx/appcompat/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroidx/appcompat/widget/ActivityChooserModel;
            //   498: iconst_1       
            //   499: putfield        androidx/appcompat/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   502: aload_3        
            //   503: ifnull          517
            //   506: aload_3        
            //   507: invokevirtual   java/io/FileOutputStream.close:()V
            //   510: goto            517
            //   513: astore_1       
            //   514: goto            510
            //   517: aconst_null    
            //   518: areturn        
            //   519: astore_1       
            //   520: aload_0        
            //   521: getfield        androidx/appcompat/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroidx/appcompat/widget/ActivityChooserModel;
            //   524: iconst_1       
            //   525: putfield        androidx/appcompat/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   528: aload_3        
            //   529: ifnull          540
            //   532: aload_3        
            //   533: invokevirtual   java/io/FileOutputStream.close:()V
            //   536: goto            540
            //   539: astore_2       
            //   540: aload_1        
            //   541: athrow         
            //   542: astore          5
            //   544: getstatic       androidx/appcompat/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   547: astore_3       
            //   548: new             Ljava/lang/StringBuilder;
            //   551: dup            
            //   552: invokespecial   java/lang/StringBuilder.<init>:()V
            //   555: astore_2       
            //   556: aload_2        
            //   557: ldc             "Error writing historical record file: "
            //   559: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   562: pop            
            //   563: aload_2        
            //   564: aload_1        
            //   565: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   568: pop            
            //   569: aload_3        
            //   570: aload_2        
            //   571: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   574: aload           5
            //   576: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   579: pop            
            //   580: aconst_null    
            //   581: areturn        
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                                
            //  -----  -----  -----  -----  ------------------------------------
            //  14     27     542    582    Ljava/io/FileNotFoundException;
            //  44     53     447    448    Ljava/lang/IllegalArgumentException;
            //  44     53     381    382    Ljava/lang/IllegalStateException;
            //  44     53     315    316    Ljava/io/IOException;
            //  44     53     311    315    Any
            //  65     78     447    448    Ljava/lang/IllegalArgumentException;
            //  65     78     381    382    Ljava/lang/IllegalStateException;
            //  65     78     315    316    Ljava/io/IOException;
            //  65     78     311    315    Any
            //  90     101    447    448    Ljava/lang/IllegalArgumentException;
            //  90     101    381    382    Ljava/lang/IllegalStateException;
            //  90     101    315    316    Ljava/io/IOException;
            //  90     101    311    315    Any
            //  113    121    447    448    Ljava/lang/IllegalArgumentException;
            //  113    121    381    382    Ljava/lang/IllegalStateException;
            //  113    121    315    316    Ljava/io/IOException;
            //  113    121    311    315    Any
            //  145    156    447    448    Ljava/lang/IllegalArgumentException;
            //  145    156    381    382    Ljava/lang/IllegalStateException;
            //  145    156    315    316    Ljava/io/IOException;
            //  145    156    311    315    Any
            //  168    179    447    448    Ljava/lang/IllegalArgumentException;
            //  168    179    381    382    Ljava/lang/IllegalStateException;
            //  168    179    315    316    Ljava/io/IOException;
            //  168    179    311    315    Any
            //  191    209    447    448    Ljava/lang/IllegalArgumentException;
            //  191    209    381    382    Ljava/lang/IllegalStateException;
            //  191    209    315    316    Ljava/io/IOException;
            //  191    209    311    315    Any
            //  209    256    307    311    Ljava/lang/IllegalArgumentException;
            //  209    256    303    307    Ljava/lang/IllegalStateException;
            //  209    256    299    303    Ljava/io/IOException;
            //  209    256    519    520    Any
            //  262    280    307    311    Ljava/lang/IllegalArgumentException;
            //  262    280    303    307    Ljava/lang/IllegalStateException;
            //  262    280    299    303    Ljava/io/IOException;
            //  262    280    519    520    Any
            //  292    296    513    517    Ljava/io/IOException;
            //  316    362    519    520    Any
            //  374    378    513    517    Ljava/io/IOException;
            //  382    428    519    520    Any
            //  440    444    513    517    Ljava/io/IOException;
            //  448    494    519    520    Any
            //  506    510    513    517    Ljava/io/IOException;
            //  532    536    539    540    Ljava/io/IOException;
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0262:
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
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
    }
}
