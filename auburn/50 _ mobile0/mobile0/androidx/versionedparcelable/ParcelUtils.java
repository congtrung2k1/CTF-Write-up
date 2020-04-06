// 
// Decompiled by Procyon v0.5.36
// 

package androidx.versionedparcelable;

import android.os.Parcelable;
import java.io.OutputStream;
import java.io.InputStream;

public class ParcelUtils
{
    private ParcelUtils() {
    }
    
    public static <T extends VersionedParcelable> T fromInputStream(final InputStream inputStream) {
        return new VersionedParcelStream(inputStream, null).readVersionedParcelable();
    }
    
    public static <T extends VersionedParcelable> T fromParcelable(final Parcelable parcelable) {
        if (parcelable instanceof ParcelImpl) {
            return ((ParcelImpl)parcelable).getVersionedParcel();
        }
        throw new IllegalArgumentException("Invalid parcel");
    }
    
    public static void toOutputStream(final VersionedParcelable versionedParcelable, final OutputStream outputStream) {
        final VersionedParcelStream versionedParcelStream = new VersionedParcelStream(null, outputStream);
        versionedParcelStream.writeVersionedParcelable(versionedParcelable);
        versionedParcelStream.closeField();
    }
    
    public static Parcelable toParcelable(final VersionedParcelable versionedParcelable) {
        return (Parcelable)new ParcelImpl(versionedParcelable);
    }
}
