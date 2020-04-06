// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.graphics.drawable;

import android.os.Parcelable;
import android.content.res.ColorStateList;
import androidx.versionedparcelable.VersionedParcel;

public class IconCompatParcelizer
{
    public static IconCompat read(final VersionedParcel versionedParcel) {
        final IconCompat iconCompat = new IconCompat();
        iconCompat.mType = versionedParcel.readInt(iconCompat.mType, 1);
        iconCompat.mData = versionedParcel.readByteArray(iconCompat.mData, 2);
        iconCompat.mParcelable = versionedParcel.readParcelable(iconCompat.mParcelable, 3);
        iconCompat.mInt1 = versionedParcel.readInt(iconCompat.mInt1, 4);
        iconCompat.mInt2 = versionedParcel.readInt(iconCompat.mInt2, 5);
        iconCompat.mTintList = versionedParcel.readParcelable(iconCompat.mTintList, 6);
        iconCompat.mTintModeStr = versionedParcel.readString(iconCompat.mTintModeStr, 7);
        iconCompat.onPostParceling();
        return iconCompat;
    }
    
    public static void write(final IconCompat iconCompat, final VersionedParcel versionedParcel) {
        versionedParcel.setSerializationFlags(true, true);
        iconCompat.onPreParceling(versionedParcel.isStream());
        versionedParcel.writeInt(iconCompat.mType, 1);
        versionedParcel.writeByteArray(iconCompat.mData, 2);
        versionedParcel.writeParcelable(iconCompat.mParcelable, 3);
        versionedParcel.writeInt(iconCompat.mInt1, 4);
        versionedParcel.writeInt(iconCompat.mInt2, 5);
        versionedParcel.writeParcelable((Parcelable)iconCompat.mTintList, 6);
        versionedParcel.writeString(iconCompat.mTintModeStr, 7);
    }
}
