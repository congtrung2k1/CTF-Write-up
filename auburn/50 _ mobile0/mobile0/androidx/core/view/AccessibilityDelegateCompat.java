// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.view;

import android.view.accessibility.AccessibilityNodeInfo;
import android.os.Bundle;
import android.view.ViewGroup;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.accessibility.AccessibilityNodeProvider;
import android.os.Build$VERSION;
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat;
import android.view.accessibility.AccessibilityEvent;
import android.view.View;
import android.view.View$AccessibilityDelegate;

public class AccessibilityDelegateCompat
{
    private static final View$AccessibilityDelegate DEFAULT_DELEGATE;
    private final View$AccessibilityDelegate mBridge;
    
    static {
        DEFAULT_DELEGATE = new View$AccessibilityDelegate();
    }
    
    public AccessibilityDelegateCompat() {
        this.mBridge = new AccessibilityDelegateAdapter(this);
    }
    
    public boolean dispatchPopulateAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
        return AccessibilityDelegateCompat.DEFAULT_DELEGATE.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
    }
    
    public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(final View view) {
        if (Build$VERSION.SDK_INT >= 16) {
            final AccessibilityNodeProvider accessibilityNodeProvider = AccessibilityDelegateCompat.DEFAULT_DELEGATE.getAccessibilityNodeProvider(view);
            if (accessibilityNodeProvider != null) {
                return new AccessibilityNodeProviderCompat(accessibilityNodeProvider);
            }
        }
        return null;
    }
    
    View$AccessibilityDelegate getBridge() {
        return this.mBridge;
    }
    
    public void onInitializeAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
        AccessibilityDelegateCompat.DEFAULT_DELEGATE.onInitializeAccessibilityEvent(view, accessibilityEvent);
    }
    
    public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        AccessibilityDelegateCompat.DEFAULT_DELEGATE.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat.unwrap());
    }
    
    public void onPopulateAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
        AccessibilityDelegateCompat.DEFAULT_DELEGATE.onPopulateAccessibilityEvent(view, accessibilityEvent);
    }
    
    public boolean onRequestSendAccessibilityEvent(final ViewGroup viewGroup, final View view, final AccessibilityEvent accessibilityEvent) {
        return AccessibilityDelegateCompat.DEFAULT_DELEGATE.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
    }
    
    public boolean performAccessibilityAction(final View view, final int n, final Bundle bundle) {
        return Build$VERSION.SDK_INT >= 16 && AccessibilityDelegateCompat.DEFAULT_DELEGATE.performAccessibilityAction(view, n, bundle);
    }
    
    public void sendAccessibilityEvent(final View view, final int n) {
        AccessibilityDelegateCompat.DEFAULT_DELEGATE.sendAccessibilityEvent(view, n);
    }
    
    public void sendAccessibilityEventUnchecked(final View view, final AccessibilityEvent accessibilityEvent) {
        AccessibilityDelegateCompat.DEFAULT_DELEGATE.sendAccessibilityEventUnchecked(view, accessibilityEvent);
    }
    
    private static final class AccessibilityDelegateAdapter extends View$AccessibilityDelegate
    {
        private final AccessibilityDelegateCompat mCompat;
        
        AccessibilityDelegateAdapter(final AccessibilityDelegateCompat mCompat) {
            this.mCompat = mCompat;
        }
        
        public boolean dispatchPopulateAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
            return this.mCompat.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
        }
        
        public AccessibilityNodeProvider getAccessibilityNodeProvider(final View view) {
            final AccessibilityNodeProviderCompat accessibilityNodeProvider = this.mCompat.getAccessibilityNodeProvider(view);
            AccessibilityNodeProvider accessibilityNodeProvider2;
            if (accessibilityNodeProvider != null) {
                accessibilityNodeProvider2 = (AccessibilityNodeProvider)accessibilityNodeProvider.getProvider();
            }
            else {
                accessibilityNodeProvider2 = null;
            }
            return accessibilityNodeProvider2;
        }
        
        public void onInitializeAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
            this.mCompat.onInitializeAccessibilityEvent(view, accessibilityEvent);
        }
        
        public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfo accessibilityNodeInfo) {
            this.mCompat.onInitializeAccessibilityNodeInfo(view, AccessibilityNodeInfoCompat.wrap(accessibilityNodeInfo));
        }
        
        public void onPopulateAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
            this.mCompat.onPopulateAccessibilityEvent(view, accessibilityEvent);
        }
        
        public boolean onRequestSendAccessibilityEvent(final ViewGroup viewGroup, final View view, final AccessibilityEvent accessibilityEvent) {
            return this.mCompat.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
        }
        
        public boolean performAccessibilityAction(final View view, final int n, final Bundle bundle) {
            return this.mCompat.performAccessibilityAction(view, n, bundle);
        }
        
        public void sendAccessibilityEvent(final View view, final int n) {
            this.mCompat.sendAccessibilityEvent(view, n);
        }
        
        public void sendAccessibilityEventUnchecked(final View view, final AccessibilityEvent accessibilityEvent) {
            this.mCompat.sendAccessibilityEventUnchecked(view, accessibilityEvent);
        }
    }
}
