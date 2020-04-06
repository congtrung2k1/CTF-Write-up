// 
// Decompiled by Procyon v0.5.36
// 

package androidx.localbroadcastmanager.content;

import java.util.Set;
import android.net.Uri;
import android.util.Log;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.os.Looper;
import android.content.BroadcastReceiver;
import android.os.Handler;
import android.content.Context;
import java.util.ArrayList;
import java.util.HashMap;

public final class LocalBroadcastManager
{
    private static final boolean DEBUG = false;
    static final int MSG_EXEC_PENDING_BROADCASTS = 1;
    private static final String TAG = "LocalBroadcastManager";
    private static LocalBroadcastManager mInstance;
    private static final Object mLock;
    private final HashMap<String, ArrayList<ReceiverRecord>> mActions;
    private final Context mAppContext;
    private final Handler mHandler;
    private final ArrayList<BroadcastRecord> mPendingBroadcasts;
    private final HashMap<BroadcastReceiver, ArrayList<ReceiverRecord>> mReceivers;
    
    static {
        mLock = new Object();
    }
    
    private LocalBroadcastManager(final Context mAppContext) {
        this.mReceivers = new HashMap<BroadcastReceiver, ArrayList<ReceiverRecord>>();
        this.mActions = new HashMap<String, ArrayList<ReceiverRecord>>();
        this.mPendingBroadcasts = new ArrayList<BroadcastRecord>();
        this.mAppContext = mAppContext;
        this.mHandler = new Handler(mAppContext.getMainLooper()) {
            public void handleMessage(final Message message) {
                if (message.what != 1) {
                    super.handleMessage(message);
                }
                else {
                    LocalBroadcastManager.this.executePendingBroadcasts();
                }
            }
        };
    }
    
    public static LocalBroadcastManager getInstance(final Context context) {
        synchronized (LocalBroadcastManager.mLock) {
            if (LocalBroadcastManager.mInstance == null) {
                LocalBroadcastManager.mInstance = new LocalBroadcastManager(context.getApplicationContext());
            }
            return LocalBroadcastManager.mInstance;
        }
    }
    
    void executePendingBroadcasts() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     4: astore_1       
        //     5: aload_1        
        //     6: monitorenter   
        //     7: aload_0        
        //     8: getfield        androidx/localbroadcastmanager/content/LocalBroadcastManager.mPendingBroadcasts:Ljava/util/ArrayList;
        //    11: invokevirtual   java/util/ArrayList.size:()I
        //    14: istore_2       
        //    15: iload_2        
        //    16: ifgt            22
        //    19: aload_1        
        //    20: monitorexit    
        //    21: return         
        //    22: iload_2        
        //    23: anewarray       Landroidx/localbroadcastmanager/content/LocalBroadcastManager$BroadcastRecord;
        //    26: astore_3       
        //    27: aload_0        
        //    28: getfield        androidx/localbroadcastmanager/content/LocalBroadcastManager.mPendingBroadcasts:Ljava/util/ArrayList;
        //    31: aload_3        
        //    32: invokevirtual   java/util/ArrayList.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //    35: pop            
        //    36: aload_0        
        //    37: getfield        androidx/localbroadcastmanager/content/LocalBroadcastManager.mPendingBroadcasts:Ljava/util/ArrayList;
        //    40: invokevirtual   java/util/ArrayList.clear:()V
        //    43: aload_1        
        //    44: monitorexit    
        //    45: iconst_0       
        //    46: istore_2       
        //    47: iload_2        
        //    48: aload_3        
        //    49: arraylength    
        //    50: if_icmpge       127
        //    53: aload_3        
        //    54: iload_2        
        //    55: aaload         
        //    56: astore          4
        //    58: aload           4
        //    60: getfield        androidx/localbroadcastmanager/content/LocalBroadcastManager$BroadcastRecord.receivers:Ljava/util/ArrayList;
        //    63: invokevirtual   java/util/ArrayList.size:()I
        //    66: istore          5
        //    68: iconst_0       
        //    69: istore          6
        //    71: iload           6
        //    73: iload           5
        //    75: if_icmpge       121
        //    78: aload           4
        //    80: getfield        androidx/localbroadcastmanager/content/LocalBroadcastManager$BroadcastRecord.receivers:Ljava/util/ArrayList;
        //    83: iload           6
        //    85: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //    88: checkcast       Landroidx/localbroadcastmanager/content/LocalBroadcastManager$ReceiverRecord;
        //    91: astore_1       
        //    92: aload_1        
        //    93: getfield        androidx/localbroadcastmanager/content/LocalBroadcastManager$ReceiverRecord.dead:Z
        //    96: ifne            115
        //    99: aload_1        
        //   100: getfield        androidx/localbroadcastmanager/content/LocalBroadcastManager$ReceiverRecord.receiver:Landroid/content/BroadcastReceiver;
        //   103: aload_0        
        //   104: getfield        androidx/localbroadcastmanager/content/LocalBroadcastManager.mAppContext:Landroid/content/Context;
        //   107: aload           4
        //   109: getfield        androidx/localbroadcastmanager/content/LocalBroadcastManager$BroadcastRecord.intent:Landroid/content/Intent;
        //   112: invokevirtual   android/content/BroadcastReceiver.onReceive:(Landroid/content/Context;Landroid/content/Intent;)V
        //   115: iinc            6, 1
        //   118: goto            71
        //   121: iinc            2, 1
        //   124: goto            47
        //   127: goto            0
        //   130: astore_3       
        //   131: aload_1        
        //   132: monitorexit    
        //   133: aload_3        
        //   134: athrow         
        //   135: astore_3       
        //   136: goto            131
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  7      15     130    131    Any
        //  19     21     130    131    Any
        //  22     27     130    131    Any
        //  27     45     135    139    Any
        //  131    133    135    139    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0047:
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
    
    public void registerReceiver(final BroadcastReceiver broadcastReceiver, final IntentFilter intentFilter) {
        final HashMap<BroadcastReceiver, ArrayList<ReceiverRecord>> mReceivers = this.mReceivers;
        // monitorenter(mReceivers)
        try {
            final ReceiverRecord receiverRecord = new ReceiverRecord(intentFilter, broadcastReceiver);
            ArrayList<ReceiverRecord> value;
            if ((value = this.mReceivers.get(broadcastReceiver)) == null) {
                value = new ArrayList<ReceiverRecord>(1);
                this.mReceivers.put(broadcastReceiver, value);
            }
            value.add(receiverRecord);
            for (int i = 0; i < intentFilter.countActions(); ++i) {
                final String action = intentFilter.getAction(i);
                ArrayList<ReceiverRecord> value2;
                if ((value2 = this.mActions.get(action)) == null) {
                    value2 = new ArrayList<ReceiverRecord>(1);
                    this.mActions.put(action, value2);
                }
                value2.add(receiverRecord);
            }
        }
        // monitorexit(mReceivers)
        finally {
            // monitorexit(mReceivers)
            while (true) {}
        }
    }
    
    public boolean sendBroadcast(final Intent obj) {
        final HashMap<BroadcastReceiver, ArrayList<ReceiverRecord>> mReceivers = this.mReceivers;
        // monitorenter(mReceivers)
        try {
            final String action = obj.getAction();
            final String resolveTypeIfNeeded = obj.resolveTypeIfNeeded(this.mAppContext.getContentResolver());
            final Uri data = obj.getData();
            final String scheme = obj.getScheme();
            final Set categories = obj.getCategories();
            final boolean b = (obj.getFlags() & 0x8) != 0x0;
            if (b) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Resolving type ");
                sb.append(resolveTypeIfNeeded);
                sb.append(" scheme ");
                sb.append(scheme);
                sb.append(" of intent ");
                sb.append(obj);
                Log.v("LocalBroadcastManager", sb.toString());
            }
            final ArrayList<ReceiverRecord> obj2 = this.mActions.get(obj.getAction());
            if (obj2 != null) {
                if (b) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Action list: ");
                    sb2.append(obj2);
                    Log.v("LocalBroadcastManager", sb2.toString());
                }
                ArrayList<ReceiverRecord> list = null;
                ArrayList<ReceiverRecord> list2;
                for (int i = 0; i < obj2.size(); ++i, list = list2) {
                    final ReceiverRecord e = obj2.get(i);
                    if (b) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("Matching against filter ");
                        sb3.append(e.filter);
                        Log.v("LocalBroadcastManager", sb3.toString());
                    }
                    if (e.broadcasting) {
                        if (b) {
                            Log.v("LocalBroadcastManager", "  Filter's target already added");
                        }
                    }
                    else {
                        final IntentFilter filter = e.filter;
                        list2 = list;
                        final int match = filter.match(action, resolveTypeIfNeeded, scheme, data, categories, "LocalBroadcastManager");
                        if (match >= 0) {
                            if (b) {
                                final StringBuilder sb4 = new StringBuilder();
                                sb4.append("  Filter matched!  match=0x");
                                sb4.append(Integer.toHexString(match));
                                Log.v("LocalBroadcastManager", sb4.toString());
                            }
                            if (list2 == null) {
                                list2 = new ArrayList<ReceiverRecord>();
                            }
                            list2.add(e);
                            e.broadcasting = true;
                            continue;
                        }
                        if (b) {
                            String str;
                            if (match != -4) {
                                if (match != -3) {
                                    if (match != -2) {
                                        if (match != -1) {
                                            str = "unknown reason";
                                        }
                                        else {
                                            str = "type";
                                        }
                                    }
                                    else {
                                        str = "data";
                                    }
                                }
                                else {
                                    str = "action";
                                }
                            }
                            else {
                                str = "category";
                            }
                            final StringBuilder sb5 = new StringBuilder();
                            sb5.append("  Filter did not match: ");
                            sb5.append(str);
                            Log.v("LocalBroadcastManager", sb5.toString());
                        }
                    }
                    list2 = list;
                }
                if (list != null) {
                    for (int j = 0; j < list.size(); ++j) {
                        list.get(j).broadcasting = false;
                    }
                    this.mPendingBroadcasts.add(new BroadcastRecord(obj, list));
                    if (!this.mHandler.hasMessages(1)) {
                        this.mHandler.sendEmptyMessage(1);
                    }
                    // monitorexit(mReceivers)
                    return true;
                }
            }
            // monitorexit(mReceivers)
            return false;
        }
        finally {
            // monitorexit(mReceivers)
            while (true) {}
        }
    }
    
    public void sendBroadcastSync(final Intent intent) {
        if (this.sendBroadcast(intent)) {
            this.executePendingBroadcasts();
        }
    }
    
    public void unregisterReceiver(final BroadcastReceiver key) {
        final HashMap<BroadcastReceiver, ArrayList<ReceiverRecord>> mReceivers = this.mReceivers;
        // monitorenter(mReceivers)
        try {
            final ArrayList<ReceiverRecord> list = this.mReceivers.remove(key);
            if (list == null) {
                // monitorexit(mReceivers)
                return;
            }
            for (int i = list.size() - 1; i >= 0; --i) {
                final ReceiverRecord receiverRecord = list.get(i);
                receiverRecord.dead = true;
                for (int j = 0; j < receiverRecord.filter.countActions(); ++j) {
                    final String action = receiverRecord.filter.getAction(j);
                    final ArrayList<ReceiverRecord> list2 = this.mActions.get(action);
                    if (list2 != null) {
                        for (int k = list2.size() - 1; k >= 0; --k) {
                            final ReceiverRecord receiverRecord2 = list2.get(k);
                            if (receiverRecord2.receiver == key) {
                                receiverRecord2.dead = true;
                                list2.remove(k);
                            }
                        }
                        if (list2.size() <= 0) {
                            this.mActions.remove(action);
                        }
                    }
                }
            }
        }
        // monitorexit(mReceivers)
        finally {
            // monitorexit(mReceivers)
            while (true) {}
        }
    }
    
    private static final class BroadcastRecord
    {
        final Intent intent;
        final ArrayList<ReceiverRecord> receivers;
        
        BroadcastRecord(final Intent intent, final ArrayList<ReceiverRecord> receivers) {
            this.intent = intent;
            this.receivers = receivers;
        }
    }
    
    private static final class ReceiverRecord
    {
        boolean broadcasting;
        boolean dead;
        final IntentFilter filter;
        final BroadcastReceiver receiver;
        
        ReceiverRecord(final IntentFilter filter, final BroadcastReceiver receiver) {
            this.filter = filter;
            this.receiver = receiver;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder(128);
            sb.append("Receiver{");
            sb.append(this.receiver);
            sb.append(" filter=");
            sb.append(this.filter);
            if (this.dead) {
                sb.append(" DEAD");
            }
            sb.append("}");
            return sb.toString();
        }
    }
}
