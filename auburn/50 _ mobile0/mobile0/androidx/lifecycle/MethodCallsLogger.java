// 
// Decompiled by Procyon v0.5.36
// 

package androidx.lifecycle;

import java.util.HashMap;
import java.util.Map;

public class MethodCallsLogger
{
    private Map<String, Integer> mCalledMethods;
    
    public MethodCallsLogger() {
        this.mCalledMethods = new HashMap<String, Integer>();
    }
    
    public boolean approveCall(final String s, final int n) {
        final Integer n2 = this.mCalledMethods.get(s);
        boolean b = false;
        int intValue;
        if (n2 != null) {
            intValue = n2;
        }
        else {
            intValue = 0;
        }
        final boolean b2 = (intValue & n) != 0x0;
        this.mCalledMethods.put(s, intValue | n);
        if (!b2) {
            b = true;
        }
        return b;
    }
}
