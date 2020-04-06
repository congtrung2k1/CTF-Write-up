// 
// Decompiled by Procyon v0.5.36
// 

package androidx.versionedparcelable;

import android.os.IInterface;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.os.Parcel;

class VersionedParcelParcel extends VersionedParcel
{
    private static final boolean DEBUG = false;
    private static final String TAG = "VersionedParcelParcel";
    private int mCurrentField;
    private final int mEnd;
    private int mNextRead;
    private final int mOffset;
    private final Parcel mParcel;
    private final SparseIntArray mPositionLookup;
    private final String mPrefix;
    
    VersionedParcelParcel(final Parcel parcel) {
        this(parcel, parcel.dataPosition(), parcel.dataSize(), "");
    }
    
    VersionedParcelParcel(final Parcel mParcel, final int n, final int mEnd, final String mPrefix) {
        this.mPositionLookup = new SparseIntArray();
        this.mCurrentField = -1;
        this.mNextRead = 0;
        this.mParcel = mParcel;
        this.mOffset = n;
        this.mEnd = mEnd;
        this.mNextRead = n;
        this.mPrefix = mPrefix;
    }
    
    private int readUntilField(final int n) {
        while (true) {
            final int mNextRead = this.mNextRead;
            if (mNextRead >= this.mEnd) {
                return -1;
            }
            this.mParcel.setDataPosition(mNextRead);
            final int int1 = this.mParcel.readInt();
            final int int2 = this.mParcel.readInt();
            this.mNextRead += int1;
            if (int2 == n) {
                return this.mParcel.dataPosition();
            }
        }
    }
    
    public void closeField() {
        final int mCurrentField = this.mCurrentField;
        if (mCurrentField >= 0) {
            final int value = this.mPositionLookup.get(mCurrentField);
            final int dataPosition = this.mParcel.dataPosition();
            this.mParcel.setDataPosition(value);
            this.mParcel.writeInt(dataPosition - value);
            this.mParcel.setDataPosition(dataPosition);
        }
    }
    
    @Override
    protected VersionedParcel createSubParcel() {
        final Parcel mParcel = this.mParcel;
        final int dataPosition = mParcel.dataPosition();
        int n;
        if ((n = this.mNextRead) == this.mOffset) {
            n = this.mEnd;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(this.mPrefix);
        sb.append("  ");
        return new VersionedParcelParcel(mParcel, dataPosition, n, sb.toString());
    }
    
    public boolean readBoolean() {
        return this.mParcel.readInt() != 0;
    }
    
    public Bundle readBundle() {
        return this.mParcel.readBundle(this.getClass().getClassLoader());
    }
    
    public byte[] readByteArray() {
        final int int1 = this.mParcel.readInt();
        if (int1 < 0) {
            return null;
        }
        final byte[] array = new byte[int1];
        this.mParcel.readByteArray(array);
        return array;
    }
    
    public double readDouble() {
        return this.mParcel.readDouble();
    }
    
    public boolean readField(int untilField) {
        untilField = this.readUntilField(untilField);
        if (untilField == -1) {
            return false;
        }
        this.mParcel.setDataPosition(untilField);
        return true;
    }
    
    public float readFloat() {
        return this.mParcel.readFloat();
    }
    
    public int readInt() {
        return this.mParcel.readInt();
    }
    
    public long readLong() {
        return this.mParcel.readLong();
    }
    
    public <T extends Parcelable> T readParcelable() {
        return (T)this.mParcel.readParcelable(this.getClass().getClassLoader());
    }
    
    public String readString() {
        return this.mParcel.readString();
    }
    
    public IBinder readStrongBinder() {
        return this.mParcel.readStrongBinder();
    }
    
    public void setOutputField(final int mCurrentField) {
        this.closeField();
        this.mCurrentField = mCurrentField;
        this.mPositionLookup.put(mCurrentField, this.mParcel.dataPosition());
        this.writeInt(0);
        this.writeInt(mCurrentField);
    }
    
    public void writeBoolean(final boolean b) {
        this.mParcel.writeInt((int)(b ? 1 : 0));
    }
    
    public void writeBundle(final Bundle bundle) {
        this.mParcel.writeBundle(bundle);
    }
    
    public void writeByteArray(final byte[] array) {
        if (array != null) {
            this.mParcel.writeInt(array.length);
            this.mParcel.writeByteArray(array);
        }
        else {
            this.mParcel.writeInt(-1);
        }
    }
    
    public void writeByteArray(final byte[] array, final int n, final int n2) {
        if (array != null) {
            this.mParcel.writeInt(array.length);
            this.mParcel.writeByteArray(array, n, n2);
        }
        else {
            this.mParcel.writeInt(-1);
        }
    }
    
    public void writeDouble(final double n) {
        this.mParcel.writeDouble(n);
    }
    
    public void writeFloat(final float n) {
        this.mParcel.writeFloat(n);
    }
    
    public void writeInt(final int n) {
        this.mParcel.writeInt(n);
    }
    
    public void writeLong(final long n) {
        this.mParcel.writeLong(n);
    }
    
    public void writeParcelable(final Parcelable parcelable) {
        this.mParcel.writeParcelable(parcelable, 0);
    }
    
    public void writeString(final String s) {
        this.mParcel.writeString(s);
    }
    
    public void writeStrongBinder(final IBinder binder) {
        this.mParcel.writeStrongBinder(binder);
    }
    
    public void writeStrongInterface(final IInterface interface1) {
        this.mParcel.writeStrongInterface(interface1);
    }
}
