// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.view.inputmethod;

import android.view.inputmethod.InputContentInfo;
import android.os.Build$VERSION;
import android.content.ClipDescription;
import android.net.Uri;

public final class InputContentInfoCompat
{
    private final InputContentInfoCompatImpl mImpl;
    
    public InputContentInfoCompat(final Uri uri, final ClipDescription clipDescription, final Uri uri2) {
        if (Build$VERSION.SDK_INT >= 25) {
            this.mImpl = (InputContentInfoCompatImpl)new InputContentInfoCompatApi25Impl(uri, clipDescription, uri2);
        }
        else {
            this.mImpl = (InputContentInfoCompatImpl)new InputContentInfoCompatBaseImpl(uri, clipDescription, uri2);
        }
    }
    
    private InputContentInfoCompat(final InputContentInfoCompatImpl mImpl) {
        this.mImpl = mImpl;
    }
    
    public static InputContentInfoCompat wrap(final Object o) {
        if (o == null) {
            return null;
        }
        if (Build$VERSION.SDK_INT < 25) {
            return null;
        }
        return new InputContentInfoCompat((InputContentInfoCompatImpl)new InputContentInfoCompatApi25Impl(o));
    }
    
    public Uri getContentUri() {
        return this.mImpl.getContentUri();
    }
    
    public ClipDescription getDescription() {
        return this.mImpl.getDescription();
    }
    
    public Uri getLinkUri() {
        return this.mImpl.getLinkUri();
    }
    
    public void releasePermission() {
        this.mImpl.releasePermission();
    }
    
    public void requestPermission() {
        this.mImpl.requestPermission();
    }
    
    public Object unwrap() {
        return this.mImpl.getInputContentInfo();
    }
    
    private static final class InputContentInfoCompatApi25Impl implements InputContentInfoCompatImpl
    {
        final InputContentInfo mObject;
        
        InputContentInfoCompatApi25Impl(final Uri uri, final ClipDescription clipDescription, final Uri uri2) {
            this.mObject = new InputContentInfo(uri, clipDescription, uri2);
        }
        
        InputContentInfoCompatApi25Impl(final Object o) {
            this.mObject = (InputContentInfo)o;
        }
        
        @Override
        public Uri getContentUri() {
            return this.mObject.getContentUri();
        }
        
        @Override
        public ClipDescription getDescription() {
            return this.mObject.getDescription();
        }
        
        @Override
        public Object getInputContentInfo() {
            return this.mObject;
        }
        
        @Override
        public Uri getLinkUri() {
            return this.mObject.getLinkUri();
        }
        
        @Override
        public void releasePermission() {
            this.mObject.releasePermission();
        }
        
        @Override
        public void requestPermission() {
            this.mObject.requestPermission();
        }
    }
    
    private static final class InputContentInfoCompatBaseImpl implements InputContentInfoCompatImpl
    {
        private final Uri mContentUri;
        private final ClipDescription mDescription;
        private final Uri mLinkUri;
        
        InputContentInfoCompatBaseImpl(final Uri mContentUri, final ClipDescription mDescription, final Uri mLinkUri) {
            this.mContentUri = mContentUri;
            this.mDescription = mDescription;
            this.mLinkUri = mLinkUri;
        }
        
        @Override
        public Uri getContentUri() {
            return this.mContentUri;
        }
        
        @Override
        public ClipDescription getDescription() {
            return this.mDescription;
        }
        
        @Override
        public Object getInputContentInfo() {
            return null;
        }
        
        @Override
        public Uri getLinkUri() {
            return this.mLinkUri;
        }
        
        @Override
        public void releasePermission() {
        }
        
        @Override
        public void requestPermission() {
        }
    }
    
    private interface InputContentInfoCompatImpl
    {
        Uri getContentUri();
        
        ClipDescription getDescription();
        
        Object getInputContentInfo();
        
        Uri getLinkUri();
        
        void releasePermission();
        
        void requestPermission();
    }
}
