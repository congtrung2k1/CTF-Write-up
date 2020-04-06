// 
// Decompiled by Procyon v0.5.36
// 

package androidx.collection;

import java.util.Locale;
import java.util.Map;
import java.util.LinkedHashMap;

public class LruCache<K, V>
{
    private int createCount;
    private int evictionCount;
    private int hitCount;
    private final LinkedHashMap<K, V> map;
    private int maxSize;
    private int missCount;
    private int putCount;
    private int size;
    
    public LruCache(final int maxSize) {
        if (maxSize > 0) {
            this.maxSize = maxSize;
            this.map = new LinkedHashMap<K, V>(0, 0.75f, true);
            return;
        }
        throw new IllegalArgumentException("maxSize <= 0");
    }
    
    private int safeSizeOf(final K obj, final V obj2) {
        final int size = this.sizeOf(obj, obj2);
        if (size >= 0) {
            return size;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Negative size: ");
        sb.append(obj);
        sb.append("=");
        sb.append(obj2);
        throw new IllegalStateException(sb.toString());
    }
    
    protected V create(final K k) {
        return null;
    }
    
    public final int createCount() {
        synchronized (this) {
            return this.createCount;
        }
    }
    
    protected void entryRemoved(final boolean b, final K k, final V v, final V v2) {
    }
    
    public final void evictAll() {
        this.trimToSize(-1);
    }
    
    public final int evictionCount() {
        synchronized (this) {
            return this.evictionCount;
        }
    }
    
    public final V get(final K p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ifnull          151
        //     4: aload_0        
        //     5: monitorenter   
        //     6: aload_0        
        //     7: getfield        androidx/collection/LruCache.map:Ljava/util/LinkedHashMap;
        //    10: aload_1        
        //    11: invokevirtual   java/util/LinkedHashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    14: astore_2       
        //    15: aload_2        
        //    16: ifnull          33
        //    19: aload_0        
        //    20: aload_0        
        //    21: getfield        androidx/collection/LruCache.hitCount:I
        //    24: iconst_1       
        //    25: iadd           
        //    26: putfield        androidx/collection/LruCache.hitCount:I
        //    29: aload_0        
        //    30: monitorexit    
        //    31: aload_2        
        //    32: areturn        
        //    33: aload_0        
        //    34: aload_0        
        //    35: getfield        androidx/collection/LruCache.missCount:I
        //    38: iconst_1       
        //    39: iadd           
        //    40: putfield        androidx/collection/LruCache.missCount:I
        //    43: aload_0        
        //    44: monitorexit    
        //    45: aload_0        
        //    46: aload_1        
        //    47: invokevirtual   androidx/collection/LruCache.create:(Ljava/lang/Object;)Ljava/lang/Object;
        //    50: astore_2       
        //    51: aload_2        
        //    52: ifnonnull       57
        //    55: aconst_null    
        //    56: areturn        
        //    57: aload_0        
        //    58: monitorenter   
        //    59: aload_0        
        //    60: aload_0        
        //    61: getfield        androidx/collection/LruCache.createCount:I
        //    64: iconst_1       
        //    65: iadd           
        //    66: putfield        androidx/collection/LruCache.createCount:I
        //    69: aload_0        
        //    70: getfield        androidx/collection/LruCache.map:Ljava/util/LinkedHashMap;
        //    73: aload_1        
        //    74: aload_2        
        //    75: invokevirtual   java/util/LinkedHashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    78: astore_3       
        //    79: aload_3        
        //    80: ifnull          96
        //    83: aload_0        
        //    84: getfield        androidx/collection/LruCache.map:Ljava/util/LinkedHashMap;
        //    87: aload_1        
        //    88: aload_3        
        //    89: invokevirtual   java/util/LinkedHashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    92: pop            
        //    93: goto            111
        //    96: aload_0        
        //    97: aload_0        
        //    98: getfield        androidx/collection/LruCache.size:I
        //   101: aload_0        
        //   102: aload_1        
        //   103: aload_2        
        //   104: invokespecial   androidx/collection/LruCache.safeSizeOf:(Ljava/lang/Object;Ljava/lang/Object;)I
        //   107: iadd           
        //   108: putfield        androidx/collection/LruCache.size:I
        //   111: aload_0        
        //   112: monitorexit    
        //   113: aload_3        
        //   114: ifnull          127
        //   117: aload_0        
        //   118: iconst_0       
        //   119: aload_1        
        //   120: aload_2        
        //   121: aload_3        
        //   122: invokevirtual   androidx/collection/LruCache.entryRemoved:(ZLjava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
        //   125: aload_3        
        //   126: areturn        
        //   127: aload_0        
        //   128: aload_0        
        //   129: getfield        androidx/collection/LruCache.maxSize:I
        //   132: invokevirtual   androidx/collection/LruCache.trimToSize:(I)V
        //   135: aload_2        
        //   136: areturn        
        //   137: astore_1       
        //   138: aload_0        
        //   139: monitorexit    
        //   140: aload_1        
        //   141: athrow         
        //   142: astore_1       
        //   143: aload_0        
        //   144: monitorexit    
        //   145: aload_1        
        //   146: athrow         
        //   147: astore_1       
        //   148: goto            143
        //   151: new             Ljava/lang/NullPointerException;
        //   154: dup            
        //   155: ldc             "key == null"
        //   157: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   160: astore_1       
        //   161: goto            166
        //   164: aload_1        
        //   165: athrow         
        //   166: goto            164
        //    Signature:
        //  (TK;)TV;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  6      15     142    143    Any
        //  19     31     147    151    Any
        //  33     45     147    151    Any
        //  59     79     137    142    Any
        //  83     93     137    142    Any
        //  96     111    137    142    Any
        //  111    113    137    142    Any
        //  138    140    137    142    Any
        //  143    145    147    151    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0033:
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
    
    public final int hitCount() {
        synchronized (this) {
            return this.hitCount;
        }
    }
    
    public final int maxSize() {
        synchronized (this) {
            return this.maxSize;
        }
    }
    
    public final int missCount() {
        synchronized (this) {
            return this.missCount;
        }
    }
    
    public final V put(final K p0, final V p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ifnull          97
        //     4: aload_2        
        //     5: ifnull          97
        //     8: aload_0        
        //     9: monitorenter   
        //    10: aload_0        
        //    11: aload_0        
        //    12: getfield        androidx/collection/LruCache.putCount:I
        //    15: iconst_1       
        //    16: iadd           
        //    17: putfield        androidx/collection/LruCache.putCount:I
        //    20: aload_0        
        //    21: aload_0        
        //    22: getfield        androidx/collection/LruCache.size:I
        //    25: aload_0        
        //    26: aload_1        
        //    27: aload_2        
        //    28: invokespecial   androidx/collection/LruCache.safeSizeOf:(Ljava/lang/Object;Ljava/lang/Object;)I
        //    31: iadd           
        //    32: putfield        androidx/collection/LruCache.size:I
        //    35: aload_0        
        //    36: getfield        androidx/collection/LruCache.map:Ljava/util/LinkedHashMap;
        //    39: aload_1        
        //    40: aload_2        
        //    41: invokevirtual   java/util/LinkedHashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    44: astore_3       
        //    45: aload_3        
        //    46: ifnull          64
        //    49: aload_0        
        //    50: aload_0        
        //    51: getfield        androidx/collection/LruCache.size:I
        //    54: aload_0        
        //    55: aload_1        
        //    56: aload_3        
        //    57: invokespecial   androidx/collection/LruCache.safeSizeOf:(Ljava/lang/Object;Ljava/lang/Object;)I
        //    60: isub           
        //    61: putfield        androidx/collection/LruCache.size:I
        //    64: aload_0        
        //    65: monitorexit    
        //    66: aload_3        
        //    67: ifnull          78
        //    70: aload_0        
        //    71: iconst_0       
        //    72: aload_1        
        //    73: aload_3        
        //    74: aload_2        
        //    75: invokevirtual   androidx/collection/LruCache.entryRemoved:(ZLjava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
        //    78: aload_0        
        //    79: aload_0        
        //    80: getfield        androidx/collection/LruCache.maxSize:I
        //    83: invokevirtual   androidx/collection/LruCache.trimToSize:(I)V
        //    86: aload_3        
        //    87: areturn        
        //    88: astore_1       
        //    89: aload_0        
        //    90: monitorexit    
        //    91: aload_1        
        //    92: athrow         
        //    93: astore_1       
        //    94: goto            89
        //    97: new             Ljava/lang/NullPointerException;
        //   100: dup            
        //   101: ldc             "key == null || value == null"
        //   103: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   106: astore_1       
        //   107: goto            112
        //   110: aload_1        
        //   111: athrow         
        //   112: goto            110
        //    Signature:
        //  (TK;TV;)TV;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  10     45     88     89     Any
        //  49     64     93     97     Any
        //  64     66     93     97     Any
        //  89     91     93     97     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0064:
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
    
    public final int putCount() {
        synchronized (this) {
            return this.putCount;
        }
    }
    
    public final V remove(final K p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ifnull          59
        //     4: aload_0        
        //     5: monitorenter   
        //     6: aload_0        
        //     7: getfield        androidx/collection/LruCache.map:Ljava/util/LinkedHashMap;
        //    10: aload_1        
        //    11: invokevirtual   java/util/LinkedHashMap.remove:(Ljava/lang/Object;)Ljava/lang/Object;
        //    14: astore_2       
        //    15: aload_2        
        //    16: ifnull          34
        //    19: aload_0        
        //    20: aload_0        
        //    21: getfield        androidx/collection/LruCache.size:I
        //    24: aload_0        
        //    25: aload_1        
        //    26: aload_2        
        //    27: invokespecial   androidx/collection/LruCache.safeSizeOf:(Ljava/lang/Object;Ljava/lang/Object;)I
        //    30: isub           
        //    31: putfield        androidx/collection/LruCache.size:I
        //    34: aload_0        
        //    35: monitorexit    
        //    36: aload_2        
        //    37: ifnull          48
        //    40: aload_0        
        //    41: iconst_0       
        //    42: aload_1        
        //    43: aload_2        
        //    44: aconst_null    
        //    45: invokevirtual   androidx/collection/LruCache.entryRemoved:(ZLjava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
        //    48: aload_2        
        //    49: areturn        
        //    50: astore_1       
        //    51: aload_0        
        //    52: monitorexit    
        //    53: aload_1        
        //    54: athrow         
        //    55: astore_1       
        //    56: goto            51
        //    59: new             Ljava/lang/NullPointerException;
        //    62: dup            
        //    63: ldc             "key == null"
        //    65: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //    68: astore_1       
        //    69: goto            74
        //    72: aload_1        
        //    73: athrow         
        //    74: goto            72
        //    Signature:
        //  (TK;)TV;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  6      15     50     51     Any
        //  19     34     55     59     Any
        //  34     36     55     59     Any
        //  51     53     55     59     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0034:
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
    
    public void resize(final int maxSize) {
        if (maxSize > 0) {
            synchronized (this) {
                this.maxSize = maxSize;
                // monitorexit(this)
                this.trimToSize(maxSize);
                return;
            }
        }
        throw new IllegalArgumentException("maxSize <= 0");
    }
    
    public final int size() {
        synchronized (this) {
            return this.size;
        }
    }
    
    protected int sizeOf(final K k, final V v) {
        return 1;
    }
    
    public final Map<K, V> snapshot() {
        synchronized (this) {
            return new LinkedHashMap<K, V>((Map<? extends K, ? extends V>)this.map);
        }
    }
    
    @Override
    public final String toString() {
        synchronized (this) {
            final int n = this.hitCount + this.missCount;
            int i;
            if (n != 0) {
                i = this.hitCount * 100 / n;
            }
            else {
                i = 0;
            }
            return String.format(Locale.US, "LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", this.maxSize, this.hitCount, this.missCount, i);
        }
    }
    
    public void trimToSize(final int p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: monitorenter   
        //     2: aload_0        
        //     3: getfield        androidx/collection/LruCache.size:I
        //     6: iflt            136
        //     9: aload_0        
        //    10: getfield        androidx/collection/LruCache.map:Ljava/util/LinkedHashMap;
        //    13: invokevirtual   java/util/LinkedHashMap.isEmpty:()Z
        //    16: ifeq            26
        //    19: aload_0        
        //    20: getfield        androidx/collection/LruCache.size:I
        //    23: ifne            136
        //    26: aload_0        
        //    27: getfield        androidx/collection/LruCache.size:I
        //    30: iload_1        
        //    31: if_icmple       133
        //    34: aload_0        
        //    35: getfield        androidx/collection/LruCache.map:Ljava/util/LinkedHashMap;
        //    38: invokevirtual   java/util/LinkedHashMap.isEmpty:()Z
        //    41: ifeq            47
        //    44: goto            133
        //    47: aload_0        
        //    48: getfield        androidx/collection/LruCache.map:Ljava/util/LinkedHashMap;
        //    51: invokevirtual   java/util/LinkedHashMap.entrySet:()Ljava/util/Set;
        //    54: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //    59: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    64: checkcast       Ljava/util/Map$Entry;
        //    67: astore_2       
        //    68: aload_2        
        //    69: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
        //    74: astore_3       
        //    75: aload_2        
        //    76: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
        //    81: astore_2       
        //    82: aload_0        
        //    83: getfield        androidx/collection/LruCache.map:Ljava/util/LinkedHashMap;
        //    86: aload_3        
        //    87: invokevirtual   java/util/LinkedHashMap.remove:(Ljava/lang/Object;)Ljava/lang/Object;
        //    90: pop            
        //    91: aload_0        
        //    92: aload_0        
        //    93: getfield        androidx/collection/LruCache.size:I
        //    96: aload_0        
        //    97: aload_3        
        //    98: aload_2        
        //    99: invokespecial   androidx/collection/LruCache.safeSizeOf:(Ljava/lang/Object;Ljava/lang/Object;)I
        //   102: isub           
        //   103: putfield        androidx/collection/LruCache.size:I
        //   106: aload_0        
        //   107: aload_0        
        //   108: getfield        androidx/collection/LruCache.evictionCount:I
        //   111: iconst_1       
        //   112: iadd           
        //   113: putfield        androidx/collection/LruCache.evictionCount:I
        //   116: aload_0        
        //   117: monitorexit    
        //   118: aload_0        
        //   119: iconst_1       
        //   120: aload_3        
        //   121: aload_2        
        //   122: aconst_null    
        //   123: invokevirtual   androidx/collection/LruCache.entryRemoved:(ZLjava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
        //   126: goto            0
        //   129: astore_3       
        //   130: goto            178
        //   133: aload_0        
        //   134: monitorexit    
        //   135: return         
        //   136: new             Ljava/lang/IllegalStateException;
        //   139: astore_3       
        //   140: new             Ljava/lang/StringBuilder;
        //   143: astore_2       
        //   144: aload_2        
        //   145: invokespecial   java/lang/StringBuilder.<init>:()V
        //   148: aload_2        
        //   149: aload_0        
        //   150: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //   153: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
        //   156: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   159: pop            
        //   160: aload_2        
        //   161: ldc             ".sizeOf() is reporting inconsistent results!"
        //   163: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   166: pop            
        //   167: aload_3        
        //   168: aload_2        
        //   169: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   172: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //   175: aload_3        
        //   176: athrow         
        //   177: astore_3       
        //   178: aload_0        
        //   179: monitorexit    
        //   180: aload_3        
        //   181: athrow         
        //   182: astore_3       
        //   183: goto            178
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  2      26     177    178    Any
        //  26     44     177    178    Any
        //  47     75     177    178    Any
        //  75     82     129    133    Any
        //  82     118    182    186    Any
        //  133    135    177    178    Any
        //  136    177    177    178    Any
        //  178    180    182    186    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0133:
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
}
