// 
// Decompiled by Procyon v0.5.36
// 

package androidx.collection;

public final class CircularIntArray
{
    private int mCapacityBitmask;
    private int[] mElements;
    private int mHead;
    private int mTail;
    
    public CircularIntArray() {
        this(8);
    }
    
    public CircularIntArray(int i) {
        if (i < 1) {
            throw new IllegalArgumentException("capacity must be >= 1");
        }
        if (i <= 1073741824) {
            if (Integer.bitCount(i) != 1) {
                i = Integer.highestOneBit(i - 1) << 1;
            }
            this.mCapacityBitmask = i - 1;
            this.mElements = new int[i];
            return;
        }
        throw new IllegalArgumentException("capacity must be <= 2^30");
    }
    
    private void doubleCapacity() {
        final int[] mElements = this.mElements;
        final int length = mElements.length;
        final int mHead = this.mHead;
        final int n = length - mHead;
        final int n2 = length << 1;
        if (n2 >= 0) {
            final int[] mElements2 = new int[n2];
            System.arraycopy(mElements, mHead, mElements2, 0, n);
            System.arraycopy(this.mElements, 0, mElements2, n, this.mHead);
            this.mElements = mElements2;
            this.mHead = 0;
            this.mTail = length;
            this.mCapacityBitmask = n2 - 1;
            return;
        }
        throw new RuntimeException("Max array capacity exceeded");
    }
    
    public void addFirst(final int n) {
        final int mHead = this.mHead - 1 & this.mCapacityBitmask;
        this.mHead = mHead;
        this.mElements[mHead] = n;
        if (mHead == this.mTail) {
            this.doubleCapacity();
        }
    }
    
    public void addLast(int mTail) {
        final int[] mElements = this.mElements;
        final int mTail2 = this.mTail;
        mElements[mTail2] = mTail;
        mTail = (this.mCapacityBitmask & mTail2 + 1);
        this.mTail = mTail;
        if (mTail == this.mHead) {
            this.doubleCapacity();
        }
    }
    
    public void clear() {
        this.mTail = this.mHead;
    }
    
    public int get(final int n) {
        if (n >= 0 && n < this.size()) {
            return this.mElements[this.mHead + n & this.mCapacityBitmask];
        }
        throw new ArrayIndexOutOfBoundsException();
    }
    
    public int getFirst() {
        final int mHead = this.mHead;
        if (mHead != this.mTail) {
            return this.mElements[mHead];
        }
        throw new ArrayIndexOutOfBoundsException();
    }
    
    public int getLast() {
        final int mHead = this.mHead;
        final int mTail = this.mTail;
        if (mHead != mTail) {
            return this.mElements[mTail - 1 & this.mCapacityBitmask];
        }
        throw new ArrayIndexOutOfBoundsException();
    }
    
    public boolean isEmpty() {
        return this.mHead == this.mTail;
    }
    
    public int popFirst() {
        final int mHead = this.mHead;
        if (mHead != this.mTail) {
            final int n = this.mElements[mHead];
            this.mHead = (mHead + 1 & this.mCapacityBitmask);
            return n;
        }
        throw new ArrayIndexOutOfBoundsException();
    }
    
    public int popLast() {
        final int mHead = this.mHead;
        final int mTail = this.mTail;
        if (mHead != mTail) {
            final int mTail2 = this.mCapacityBitmask & mTail - 1;
            final int n = this.mElements[mTail2];
            this.mTail = mTail2;
            return n;
        }
        throw new ArrayIndexOutOfBoundsException();
    }
    
    public void removeFromEnd(final int n) {
        if (n <= 0) {
            return;
        }
        if (n <= this.size()) {
            this.mTail = (this.mTail - n & this.mCapacityBitmask);
            return;
        }
        throw new ArrayIndexOutOfBoundsException();
    }
    
    public void removeFromStart(final int n) {
        if (n <= 0) {
            return;
        }
        if (n <= this.size()) {
            this.mHead = (this.mHead + n & this.mCapacityBitmask);
            return;
        }
        throw new ArrayIndexOutOfBoundsException();
    }
    
    public int size() {
        return this.mTail - this.mHead & this.mCapacityBitmask;
    }
}
