// 
// Decompiled by Procyon v0.5.36
// 

package androidx.versionedparcelable;

import android.os.IInterface;
import android.util.SparseBooleanArray;
import android.util.SizeF;
import android.util.Size;
import androidx.collection.ArraySet;
import java.util.Set;
import java.io.ObjectStreamClass;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.util.List;
import android.os.Bundle;
import java.util.ArrayList;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import android.os.IBinder;
import java.io.Serializable;
import android.os.Parcelable;
import android.os.NetworkOnMainThreadException;
import android.os.BadParcelableException;

public abstract class VersionedParcel
{
    private static final int EX_BAD_PARCELABLE = -2;
    private static final int EX_ILLEGAL_ARGUMENT = -3;
    private static final int EX_ILLEGAL_STATE = -5;
    private static final int EX_NETWORK_MAIN_THREAD = -6;
    private static final int EX_NULL_POINTER = -4;
    private static final int EX_PARCELABLE = -9;
    private static final int EX_SECURITY = -1;
    private static final int EX_UNSUPPORTED_OPERATION = -7;
    private static final String TAG = "VersionedParcel";
    private static final int TYPE_BINDER = 5;
    private static final int TYPE_PARCELABLE = 2;
    private static final int TYPE_SERIALIZABLE = 3;
    private static final int TYPE_STRING = 4;
    private static final int TYPE_VERSIONED_PARCELABLE = 1;
    
    private Exception createException(final int i, final String s) {
        switch (i) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unknown exception code: ");
                sb.append(i);
                sb.append(" msg ");
                sb.append(s);
                return new RuntimeException(sb.toString());
            }
            case -1: {
                return new SecurityException(s);
            }
            case -2: {
                return (Exception)new BadParcelableException(s);
            }
            case -3: {
                return new IllegalArgumentException(s);
            }
            case -4: {
                return new NullPointerException(s);
            }
            case -5: {
                return new IllegalStateException(s);
            }
            case -6: {
                return (Exception)new NetworkOnMainThreadException();
            }
            case -7: {
                return new UnsupportedOperationException(s);
            }
            case -9: {
                return this.readParcelable();
            }
        }
    }
    
    private static <T extends VersionedParcelable> Class findParcelClass(final T t) throws ClassNotFoundException {
        return findParcelClass(t.getClass());
    }
    
    private static Class findParcelClass(final Class<? extends VersionedParcelable> clazz) throws ClassNotFoundException {
        return Class.forName(String.format("%s.%sParcelizer", clazz.getPackage().getName(), clazz.getSimpleName()), false, clazz.getClassLoader());
    }
    
    protected static Throwable getRootCause(Throwable cause) {
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause;
    }
    
    private <T> int getType(final T t) {
        if (t instanceof String) {
            return 4;
        }
        if (t instanceof Parcelable) {
            return 2;
        }
        if (t instanceof VersionedParcelable) {
            return 1;
        }
        if (t instanceof Serializable) {
            return 3;
        }
        if (t instanceof IBinder) {
            return 5;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(t.getClass().getName());
        sb.append(" cannot be VersionedParcelled");
        throw new IllegalArgumentException(sb.toString());
    }
    
    private <T, S extends Collection<T>> S readCollection(int i, final S n) {
        i = this.readInt();
        if (i < 0) {
            return null;
        }
        if (i != 0) {
            final int int1 = this.readInt();
            if (i < 0) {
                return null;
            }
            int j = i;
            if (int1 != 1) {
                int k = i;
                if (int1 != 2) {
                    int l = i;
                    if (int1 != 3) {
                        int n2 = i;
                        if (int1 != 4) {
                            if (int1 == 5) {
                                while (i > 0) {
                                    n.add((T)this.readStrongBinder());
                                    --i;
                                }
                            }
                        }
                        else {
                            while (n2 > 0) {
                                n.add((T)this.readString());
                                --n2;
                            }
                        }
                    }
                    else {
                        while (l > 0) {
                            n.add((T)this.readSerializable());
                            --l;
                        }
                    }
                }
                else {
                    while (k > 0) {
                        n.add(this.readParcelable());
                        --k;
                    }
                }
            }
            else {
                while (j > 0) {
                    n.add(this.readVersionedParcelable());
                    --j;
                }
            }
        }
        return n;
    }
    
    private Exception readException(final int n, final String s) {
        return this.createException(n, s);
    }
    
    private int readExceptionCode() {
        return this.readInt();
    }
    
    protected static <T extends VersionedParcelable> T readFromParcel(final String name, final VersionedParcel versionedParcel) {
        try {
            return (T)Class.forName(name, true, VersionedParcel.class.getClassLoader()).getDeclaredMethod("read", VersionedParcel.class).invoke(null, versionedParcel);
        }
        catch (ClassNotFoundException cause) {
            throw new RuntimeException("VersionedParcel encountered ClassNotFoundException", cause);
        }
        catch (NoSuchMethodException cause2) {
            throw new RuntimeException("VersionedParcel encountered NoSuchMethodException", cause2);
        }
        catch (InvocationTargetException cause3) {
            if (cause3.getCause() instanceof RuntimeException) {
                throw (RuntimeException)cause3.getCause();
            }
            throw new RuntimeException("VersionedParcel encountered InvocationTargetException", cause3);
        }
        catch (IllegalAccessException ex) {}
        final IllegalAccessException cause4;
        throw new RuntimeException("VersionedParcel encountered IllegalAccessException", cause4);
    }
    
    private <T> void writeCollection(final Collection<T> collection, int outputField) {
        this.setOutputField(outputField);
        if (collection == null) {
            this.writeInt(-1);
            return;
        }
        outputField = collection.size();
        this.writeInt(outputField);
        if (outputField > 0) {
            outputField = this.getType(collection.iterator().next());
            this.writeInt(outputField);
            if (outputField != 1) {
                if (outputField != 2) {
                    if (outputField != 3) {
                        if (outputField != 4) {
                            if (outputField == 5) {
                                final Iterator<T> iterator = collection.iterator();
                                while (iterator.hasNext()) {
                                    this.writeStrongBinder((IBinder)iterator.next());
                                }
                            }
                        }
                        else {
                            final Iterator<T> iterator2 = collection.iterator();
                            while (iterator2.hasNext()) {
                                this.writeString((String)iterator2.next());
                            }
                        }
                    }
                    else {
                        final Iterator<T> iterator3 = collection.iterator();
                        while (iterator3.hasNext()) {
                            this.writeSerializable((Serializable)iterator3.next());
                        }
                    }
                }
                else {
                    final Iterator<T> iterator4 = collection.iterator();
                    while (iterator4.hasNext()) {
                        this.writeParcelable((Parcelable)iterator4.next());
                    }
                }
            }
            else {
                final Iterator<T> iterator5 = collection.iterator();
                while (iterator5.hasNext()) {
                    this.writeVersionedParcelable((VersionedParcelable)iterator5.next());
                }
            }
        }
    }
    
    private void writeSerializable(final Serializable obj) {
        if (obj == null) {
            this.writeString(null);
            return;
        }
        final String name = obj.getClass().getName();
        this.writeString(name);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            final ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
            objectOutputStream.writeObject(obj);
            objectOutputStream.close();
            this.writeByteArray(out.toByteArray());
        }
        catch (IOException cause) {
            final StringBuilder sb = new StringBuilder();
            sb.append("VersionedParcelable encountered IOException writing serializable object (name = ");
            sb.append(name);
            sb.append(")");
            throw new RuntimeException(sb.toString(), cause);
        }
    }
    
    protected static <T extends VersionedParcelable> void writeToParcel(final T t, final VersionedParcel versionedParcel) {
        try {
            findParcelClass(t).getDeclaredMethod("write", t.getClass(), VersionedParcel.class).invoke(null, t, versionedParcel);
        }
        catch (ClassNotFoundException cause) {
            throw new RuntimeException("VersionedParcel encountered ClassNotFoundException", cause);
        }
        catch (NoSuchMethodException cause2) {
            throw new RuntimeException("VersionedParcel encountered NoSuchMethodException", cause2);
        }
        catch (InvocationTargetException cause3) {
            if (cause3.getCause() instanceof RuntimeException) {
                throw (RuntimeException)cause3.getCause();
            }
            throw new RuntimeException("VersionedParcel encountered InvocationTargetException", cause3);
        }
        catch (IllegalAccessException cause4) {
            throw new RuntimeException("VersionedParcel encountered IllegalAccessException", cause4);
        }
    }
    
    private void writeVersionedParcelableCreator(final VersionedParcelable versionedParcelable) {
        try {
            this.writeString(findParcelClass(versionedParcelable.getClass()).getName());
        }
        catch (ClassNotFoundException cause) {
            final StringBuilder sb = new StringBuilder();
            sb.append(versionedParcelable.getClass().getSimpleName());
            sb.append(" does not have a Parcelizer");
            throw new RuntimeException(sb.toString(), cause);
        }
    }
    
    protected abstract void closeField();
    
    protected abstract VersionedParcel createSubParcel();
    
    public boolean isStream() {
        return false;
    }
    
    protected <T> T[] readArray(final T[] a) {
        int i = this.readInt();
        if (i < 0) {
            return null;
        }
        final ArrayList list = new ArrayList<String>(i);
        if (i != 0) {
            final int int1 = this.readInt();
            if (i < 0) {
                return null;
            }
            int j = i;
            if (int1 != 1) {
                int k = i;
                if (int1 != 2) {
                    int l = i;
                    if (int1 != 3) {
                        int n = i;
                        if (int1 != 4) {
                            if (int1 == 5) {
                                while (i > 0) {
                                    list.add((String)this.readStrongBinder());
                                    --i;
                                }
                            }
                        }
                        else {
                            while (n > 0) {
                                list.add(this.readString());
                                --n;
                            }
                        }
                    }
                    else {
                        while (l > 0) {
                            list.add((String)this.readSerializable());
                            --l;
                        }
                    }
                }
                else {
                    while (k > 0) {
                        list.add(this.readParcelable());
                        --k;
                    }
                }
            }
            else {
                while (j > 0) {
                    list.add(this.readVersionedParcelable());
                    --j;
                }
            }
        }
        return list.toArray(a);
    }
    
    public <T> T[] readArray(final T[] array, final int n) {
        if (!this.readField(n)) {
            return array;
        }
        return this.readArray(array);
    }
    
    protected abstract boolean readBoolean();
    
    public boolean readBoolean(final boolean b, final int n) {
        if (!this.readField(n)) {
            return b;
        }
        return this.readBoolean();
    }
    
    protected boolean[] readBooleanArray() {
        final int int1 = this.readInt();
        if (int1 < 0) {
            return null;
        }
        final boolean[] array = new boolean[int1];
        for (int i = 0; i < int1; ++i) {
            array[i] = (this.readInt() != 0);
        }
        return array;
    }
    
    public boolean[] readBooleanArray(final boolean[] array, final int n) {
        if (!this.readField(n)) {
            return array;
        }
        return this.readBooleanArray();
    }
    
    protected abstract Bundle readBundle();
    
    public Bundle readBundle(final Bundle bundle, final int n) {
        if (!this.readField(n)) {
            return bundle;
        }
        return this.readBundle();
    }
    
    public byte readByte(final byte b, final int n) {
        if (!this.readField(n)) {
            return b;
        }
        return (byte)(this.readInt() & 0xFF);
    }
    
    protected abstract byte[] readByteArray();
    
    public byte[] readByteArray(final byte[] array, final int n) {
        if (!this.readField(n)) {
            return array;
        }
        return this.readByteArray();
    }
    
    public char[] readCharArray(char[] array, int i) {
        if (!this.readField(i)) {
            return array;
        }
        final int int1 = this.readInt();
        if (int1 < 0) {
            return null;
        }
        array = new char[int1];
        for (i = 0; i < int1; ++i) {
            array[i] = (char)this.readInt();
        }
        return array;
    }
    
    protected abstract double readDouble();
    
    public double readDouble(final double n, final int n2) {
        if (!this.readField(n2)) {
            return n;
        }
        return this.readDouble();
    }
    
    protected double[] readDoubleArray() {
        final int int1 = this.readInt();
        if (int1 < 0) {
            return null;
        }
        final double[] array = new double[int1];
        for (int i = 0; i < int1; ++i) {
            array[i] = this.readDouble();
        }
        return array;
    }
    
    public double[] readDoubleArray(final double[] array, final int n) {
        if (!this.readField(n)) {
            return array;
        }
        return this.readDoubleArray();
    }
    
    public Exception readException(final Exception ex, int exceptionCode) {
        if (!this.readField(exceptionCode)) {
            return ex;
        }
        exceptionCode = this.readExceptionCode();
        if (exceptionCode != 0) {
            return this.readException(exceptionCode, this.readString());
        }
        return ex;
    }
    
    protected abstract boolean readField(final int p0);
    
    protected abstract float readFloat();
    
    public float readFloat(final float n, final int n2) {
        if (!this.readField(n2)) {
            return n;
        }
        return this.readFloat();
    }
    
    protected float[] readFloatArray() {
        final int int1 = this.readInt();
        if (int1 < 0) {
            return null;
        }
        final float[] array = new float[int1];
        for (int i = 0; i < int1; ++i) {
            array[i] = this.readFloat();
        }
        return array;
    }
    
    public float[] readFloatArray(final float[] array, final int n) {
        if (!this.readField(n)) {
            return array;
        }
        return this.readFloatArray();
    }
    
    protected abstract int readInt();
    
    public int readInt(final int n, final int n2) {
        if (!this.readField(n2)) {
            return n;
        }
        return this.readInt();
    }
    
    protected int[] readIntArray() {
        final int int1 = this.readInt();
        if (int1 < 0) {
            return null;
        }
        final int[] array = new int[int1];
        for (int i = 0; i < int1; ++i) {
            array[i] = this.readInt();
        }
        return array;
    }
    
    public int[] readIntArray(final int[] array, final int n) {
        if (!this.readField(n)) {
            return array;
        }
        return this.readIntArray();
    }
    
    public <T> List<T> readList(final List<T> list, final int n) {
        if (!this.readField(n)) {
            return list;
        }
        return this.readCollection(n, new ArrayList<T>());
    }
    
    protected abstract long readLong();
    
    public long readLong(final long n, final int n2) {
        if (!this.readField(n2)) {
            return n;
        }
        return this.readLong();
    }
    
    protected long[] readLongArray() {
        final int int1 = this.readInt();
        if (int1 < 0) {
            return null;
        }
        final long[] array = new long[int1];
        for (int i = 0; i < int1; ++i) {
            array[i] = this.readLong();
        }
        return array;
    }
    
    public long[] readLongArray(final long[] array, final int n) {
        if (!this.readField(n)) {
            return array;
        }
        return this.readLongArray();
    }
    
    protected abstract <T extends Parcelable> T readParcelable();
    
    public <T extends Parcelable> T readParcelable(final T t, final int n) {
        if (!this.readField(n)) {
            return t;
        }
        return this.readParcelable();
    }
    
    protected Serializable readSerializable() {
        final String string = this.readString();
        if (string == null) {
            return null;
        }
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.readByteArray());
        try {
            return (Serializable)new ObjectInputStream(byteArrayInputStream) {
                @Override
                protected Class<?> resolveClass(final ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                    final Class<?> forName = Class.forName(desc.getName(), false, this.getClass().getClassLoader());
                    if (forName != null) {
                        return forName;
                    }
                    return super.resolveClass(desc);
                }
            }.readObject();
        }
        catch (ClassNotFoundException cause) {
            final StringBuilder sb = new StringBuilder();
            sb.append("VersionedParcelable encountered ClassNotFoundException reading a Serializable object (name = ");
            sb.append(string);
            sb.append(")");
            throw new RuntimeException(sb.toString(), cause);
        }
        catch (IOException cause2) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("VersionedParcelable encountered IOException reading a Serializable object (name = ");
            sb2.append(string);
            sb2.append(")");
            throw new RuntimeException(sb2.toString(), cause2);
        }
    }
    
    public <T> Set<T> readSet(final Set<T> set, final int n) {
        if (!this.readField(n)) {
            return set;
        }
        return this.readCollection(n, new ArraySet<T>());
    }
    
    public Size readSize(final Size size, final int n) {
        if (!this.readField(n)) {
            return size;
        }
        if (this.readBoolean()) {
            return new Size(this.readInt(), this.readInt());
        }
        return null;
    }
    
    public SizeF readSizeF(final SizeF sizeF, final int n) {
        if (!this.readField(n)) {
            return sizeF;
        }
        if (this.readBoolean()) {
            return new SizeF(this.readFloat(), this.readFloat());
        }
        return null;
    }
    
    public SparseBooleanArray readSparseBooleanArray(SparseBooleanArray sparseBooleanArray, int i) {
        if (!this.readField(i)) {
            return sparseBooleanArray;
        }
        final int int1 = this.readInt();
        if (int1 < 0) {
            return null;
        }
        sparseBooleanArray = new SparseBooleanArray(int1);
        for (i = 0; i < int1; ++i) {
            sparseBooleanArray.put(this.readInt(), this.readBoolean());
        }
        return sparseBooleanArray;
    }
    
    protected abstract String readString();
    
    public String readString(final String s, final int n) {
        if (!this.readField(n)) {
            return s;
        }
        return this.readString();
    }
    
    protected abstract IBinder readStrongBinder();
    
    public IBinder readStrongBinder(final IBinder binder, final int n) {
        if (!this.readField(n)) {
            return binder;
        }
        return this.readStrongBinder();
    }
    
    protected <T extends VersionedParcelable> T readVersionedParcelable() {
        final String string = this.readString();
        if (string == null) {
            return null;
        }
        return (T)readFromParcel(string, this.createSubParcel());
    }
    
    public <T extends VersionedParcelable> T readVersionedParcelable(final T t, final int n) {
        if (!this.readField(n)) {
            return t;
        }
        return this.readVersionedParcelable();
    }
    
    protected abstract void setOutputField(final int p0);
    
    public void setSerializationFlags(final boolean b, final boolean b2) {
    }
    
    protected <T> void writeArray(final T[] array) {
        if (array == null) {
            this.writeInt(-1);
            return;
        }
        final int length = array.length;
        final int n = 0;
        final int n2 = 0;
        final int n3 = 0;
        int i = 0;
        final int n4 = 0;
        this.writeInt(length);
        if (length > 0) {
            final int type = this.getType(array[0]);
            this.writeInt(type);
            if (type != 1) {
                int j = n3;
                if (type != 2) {
                    int k = n2;
                    if (type != 3) {
                        int l = n;
                        if (type != 4) {
                            int n5 = n4;
                            if (type == 5) {
                                while (n5 < length) {
                                    this.writeStrongBinder((IBinder)array[n5]);
                                    ++n5;
                                }
                            }
                        }
                        else {
                            while (l < length) {
                                this.writeString((String)array[l]);
                                ++l;
                            }
                        }
                    }
                    else {
                        while (k < length) {
                            this.writeSerializable((Serializable)array[k]);
                            ++k;
                        }
                    }
                }
                else {
                    while (j < length) {
                        this.writeParcelable((Parcelable)array[j]);
                        ++j;
                    }
                }
            }
            else {
                while (i < length) {
                    this.writeVersionedParcelable((VersionedParcelable)array[i]);
                    ++i;
                }
            }
        }
    }
    
    public <T> void writeArray(final T[] array, final int outputField) {
        this.setOutputField(outputField);
        this.writeArray(array);
    }
    
    protected abstract void writeBoolean(final boolean p0);
    
    public void writeBoolean(final boolean b, final int outputField) {
        this.setOutputField(outputField);
        this.writeBoolean(b);
    }
    
    protected void writeBooleanArray(final boolean[] array) {
        if (array != null) {
            final int length = array.length;
            this.writeInt(length);
            for (int i = 0; i < length; ++i) {
                this.writeInt(array[i] ? 1 : 0);
            }
        }
        else {
            this.writeInt(-1);
        }
    }
    
    public void writeBooleanArray(final boolean[] array, final int outputField) {
        this.setOutputField(outputField);
        this.writeBooleanArray(array);
    }
    
    protected abstract void writeBundle(final Bundle p0);
    
    public void writeBundle(final Bundle bundle, final int outputField) {
        this.setOutputField(outputField);
        this.writeBundle(bundle);
    }
    
    public void writeByte(final byte b, final int outputField) {
        this.setOutputField(outputField);
        this.writeInt(b);
    }
    
    protected abstract void writeByteArray(final byte[] p0);
    
    public void writeByteArray(final byte[] array, final int outputField) {
        this.setOutputField(outputField);
        this.writeByteArray(array);
    }
    
    protected abstract void writeByteArray(final byte[] p0, final int p1, final int p2);
    
    public void writeByteArray(final byte[] array, final int n, final int n2, final int outputField) {
        this.setOutputField(outputField);
        this.writeByteArray(array, n, n2);
    }
    
    public void writeCharArray(final char[] array, int i) {
        this.setOutputField(i);
        if (array != null) {
            final int length = array.length;
            this.writeInt(length);
            for (i = 0; i < length; ++i) {
                this.writeInt(array[i]);
            }
        }
        else {
            this.writeInt(-1);
        }
    }
    
    protected abstract void writeDouble(final double p0);
    
    public void writeDouble(final double n, final int outputField) {
        this.setOutputField(outputField);
        this.writeDouble(n);
    }
    
    protected void writeDoubleArray(final double[] array) {
        if (array != null) {
            final int length = array.length;
            this.writeInt(length);
            for (int i = 0; i < length; ++i) {
                this.writeDouble(array[i]);
            }
        }
        else {
            this.writeInt(-1);
        }
    }
    
    public void writeDoubleArray(final double[] array, final int outputField) {
        this.setOutputField(outputField);
        this.writeDoubleArray(array);
    }
    
    public void writeException(final Exception cause, int outputField) {
        this.setOutputField(outputField);
        if (cause == null) {
            this.writeNoException();
            return;
        }
        outputField = 0;
        if (cause instanceof Parcelable && cause.getClass().getClassLoader() == Parcelable.class.getClassLoader()) {
            outputField = -9;
        }
        else if (cause instanceof SecurityException) {
            outputField = -1;
        }
        else if (cause instanceof BadParcelableException) {
            outputField = -2;
        }
        else if (cause instanceof IllegalArgumentException) {
            outputField = -3;
        }
        else if (cause instanceof NullPointerException) {
            outputField = -4;
        }
        else if (cause instanceof IllegalStateException) {
            outputField = -5;
        }
        else if (cause instanceof NetworkOnMainThreadException) {
            outputField = -6;
        }
        else if (cause instanceof UnsupportedOperationException) {
            outputField = -7;
        }
        this.writeInt(outputField);
        if (outputField != 0) {
            this.writeString(cause.getMessage());
            if (outputField == -9) {
                this.writeParcelable((Parcelable)cause);
            }
            return;
        }
        if (cause instanceof RuntimeException) {
            throw (RuntimeException)cause;
        }
        throw new RuntimeException(cause);
    }
    
    protected abstract void writeFloat(final float p0);
    
    public void writeFloat(final float n, final int outputField) {
        this.setOutputField(outputField);
        this.writeFloat(n);
    }
    
    protected void writeFloatArray(final float[] array) {
        if (array != null) {
            final int length = array.length;
            this.writeInt(length);
            for (int i = 0; i < length; ++i) {
                this.writeFloat(array[i]);
            }
        }
        else {
            this.writeInt(-1);
        }
    }
    
    public void writeFloatArray(final float[] array, final int outputField) {
        this.setOutputField(outputField);
        this.writeFloatArray(array);
    }
    
    protected abstract void writeInt(final int p0);
    
    public void writeInt(final int n, final int outputField) {
        this.setOutputField(outputField);
        this.writeInt(n);
    }
    
    protected void writeIntArray(final int[] array) {
        if (array != null) {
            final int length = array.length;
            this.writeInt(length);
            for (int i = 0; i < length; ++i) {
                this.writeInt(array[i]);
            }
        }
        else {
            this.writeInt(-1);
        }
    }
    
    public void writeIntArray(final int[] array, final int outputField) {
        this.setOutputField(outputField);
        this.writeIntArray(array);
    }
    
    public <T> void writeList(final List<T> list, final int n) {
        this.writeCollection(list, n);
    }
    
    protected abstract void writeLong(final long p0);
    
    public void writeLong(final long n, final int outputField) {
        this.setOutputField(outputField);
        this.writeLong(n);
    }
    
    protected void writeLongArray(final long[] array) {
        if (array != null) {
            final int length = array.length;
            this.writeInt(length);
            for (int i = 0; i < length; ++i) {
                this.writeLong(array[i]);
            }
        }
        else {
            this.writeInt(-1);
        }
    }
    
    public void writeLongArray(final long[] array, final int outputField) {
        this.setOutputField(outputField);
        this.writeLongArray(array);
    }
    
    protected void writeNoException() {
        this.writeInt(0);
    }
    
    protected abstract void writeParcelable(final Parcelable p0);
    
    public void writeParcelable(final Parcelable parcelable, final int outputField) {
        this.setOutputField(outputField);
        this.writeParcelable(parcelable);
    }
    
    public void writeSerializable(final Serializable s, final int outputField) {
        this.setOutputField(outputField);
        this.writeSerializable(s);
    }
    
    public <T> void writeSet(final Set<T> set, final int n) {
        this.writeCollection(set, n);
    }
    
    public void writeSize(final Size size, final int outputField) {
        this.setOutputField(outputField);
        this.writeBoolean(size != null);
        if (size != null) {
            this.writeInt(size.getWidth());
            this.writeInt(size.getHeight());
        }
    }
    
    public void writeSizeF(final SizeF sizeF, final int outputField) {
        this.setOutputField(outputField);
        this.writeBoolean(sizeF != null);
        if (sizeF != null) {
            this.writeFloat(sizeF.getWidth());
            this.writeFloat(sizeF.getHeight());
        }
    }
    
    public void writeSparseBooleanArray(final SparseBooleanArray sparseBooleanArray, int i) {
        this.setOutputField(i);
        if (sparseBooleanArray == null) {
            this.writeInt(-1);
            return;
        }
        final int size = sparseBooleanArray.size();
        this.writeInt(size);
        for (i = 0; i < size; ++i) {
            this.writeInt(sparseBooleanArray.keyAt(i));
            this.writeBoolean(sparseBooleanArray.valueAt(i));
        }
    }
    
    protected abstract void writeString(final String p0);
    
    public void writeString(final String s, final int outputField) {
        this.setOutputField(outputField);
        this.writeString(s);
    }
    
    protected abstract void writeStrongBinder(final IBinder p0);
    
    public void writeStrongBinder(final IBinder binder, final int outputField) {
        this.setOutputField(outputField);
        this.writeStrongBinder(binder);
    }
    
    protected abstract void writeStrongInterface(final IInterface p0);
    
    public void writeStrongInterface(final IInterface interface1, final int outputField) {
        this.setOutputField(outputField);
        this.writeStrongInterface(interface1);
    }
    
    protected void writeVersionedParcelable(final VersionedParcelable versionedParcelable) {
        if (versionedParcelable == null) {
            this.writeString(null);
            return;
        }
        this.writeVersionedParcelableCreator(versionedParcelable);
        final VersionedParcel subParcel = this.createSubParcel();
        writeToParcel(versionedParcelable, subParcel);
        subParcel.closeField();
    }
    
    public void writeVersionedParcelable(final VersionedParcelable versionedParcelable, final int outputField) {
        this.setOutputField(outputField);
        this.writeVersionedParcelable(versionedParcelable);
    }
    
    public static class ParcelException extends RuntimeException
    {
        public ParcelException(final Throwable cause) {
            super(cause);
        }
    }
}
