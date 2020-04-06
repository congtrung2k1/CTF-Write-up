// 
// Decompiled by Procyon v0.5.36
// 

package androidx.versionedparcelable;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class ParcelImpl implements Parcelable
{
    public static final Parcelable$Creator<ParcelImpl> CREATOR;
    private final VersionedParcelable mParcel;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<ParcelImpl>() {
            public ParcelImpl createFromParcel(final Parcel parcel) {
                return new ParcelImpl(parcel);
            }
            
            public ParcelImpl[] newArray(final int n) {
                return new ParcelImpl[n];
            }
        };
    }
    
    protected ParcelImpl(final Parcel parcel) {
        this.mParcel = new VersionedParcelParcel(parcel).readVersionedParcelable();
    }
    
    public ParcelImpl(final VersionedParcelable mParcel) {
        this.mParcel = mParcel;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public <T extends VersionedParcelable> T getVersionedParcel() {
        return (T)this.mParcel;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        new VersionedParcelParcel(parcel).writeVersionedParcelable(this.mParcel);
    }
}
