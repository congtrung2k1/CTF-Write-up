// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.os;

import android.os.Parcel;

public final class ParcelCompat
{
    private ParcelCompat() {
    }
    
    public static boolean readBoolean(final Parcel parcel) {
        return parcel.readInt() != 0;
    }
    
    public static void writeBoolean(final Parcel parcel, final boolean b) {
        parcel.writeInt((int)(b ? 1 : 0));
    }
}
