// 
// Decompiled by Procyon v0.5.36
// 

package androidx.lifecycle;

import android.os.Bundle;
import android.app.FragmentManager;
import android.app.Activity;
import android.app.Fragment;

public class ReportFragment extends Fragment
{
    private static final String REPORT_FRAGMENT_TAG = "androidx.lifecycle.LifecycleDispatcher.report_fragment_tag";
    private ActivityInitializationListener mProcessListener;
    
    private void dispatch(final Lifecycle.Event event) {
        final Activity activity = this.getActivity();
        if (activity instanceof LifecycleRegistryOwner) {
            ((LifecycleRegistryOwner)activity).getLifecycle().handleLifecycleEvent(event);
            return;
        }
        if (activity instanceof LifecycleOwner) {
            final Lifecycle lifecycle = ((LifecycleRegistryOwner)activity).getLifecycle();
            if (lifecycle instanceof LifecycleRegistry) {
                ((LifecycleRegistry)lifecycle).handleLifecycleEvent(event);
            }
        }
    }
    
    private void dispatchCreate(final ActivityInitializationListener activityInitializationListener) {
        if (activityInitializationListener != null) {
            activityInitializationListener.onCreate();
        }
    }
    
    private void dispatchResume(final ActivityInitializationListener activityInitializationListener) {
        if (activityInitializationListener != null) {
            activityInitializationListener.onResume();
        }
    }
    
    private void dispatchStart(final ActivityInitializationListener activityInitializationListener) {
        if (activityInitializationListener != null) {
            activityInitializationListener.onStart();
        }
    }
    
    static ReportFragment get(final Activity activity) {
        return (ReportFragment)activity.getFragmentManager().findFragmentByTag("androidx.lifecycle.LifecycleDispatcher.report_fragment_tag");
    }
    
    public static void injectIfNeededIn(final Activity activity) {
        final FragmentManager fragmentManager = activity.getFragmentManager();
        if (fragmentManager.findFragmentByTag("androidx.lifecycle.LifecycleDispatcher.report_fragment_tag") == null) {
            fragmentManager.beginTransaction().add((Fragment)new ReportFragment(), "androidx.lifecycle.LifecycleDispatcher.report_fragment_tag").commit();
            fragmentManager.executePendingTransactions();
        }
    }
    
    public void onActivityCreated(final Bundle bundle) {
        super.onActivityCreated(bundle);
        this.dispatchCreate(this.mProcessListener);
        this.dispatch(Lifecycle.Event.ON_CREATE);
    }
    
    public void onDestroy() {
        super.onDestroy();
        this.dispatch(Lifecycle.Event.ON_DESTROY);
        this.mProcessListener = null;
    }
    
    public void onPause() {
        super.onPause();
        this.dispatch(Lifecycle.Event.ON_PAUSE);
    }
    
    public void onResume() {
        super.onResume();
        this.dispatchResume(this.mProcessListener);
        this.dispatch(Lifecycle.Event.ON_RESUME);
    }
    
    public void onStart() {
        super.onStart();
        this.dispatchStart(this.mProcessListener);
        this.dispatch(Lifecycle.Event.ON_START);
    }
    
    public void onStop() {
        super.onStop();
        this.dispatch(Lifecycle.Event.ON_STOP);
    }
    
    void setProcessListener(final ActivityInitializationListener mProcessListener) {
        this.mProcessListener = mProcessListener;
    }
    
    interface ActivityInitializationListener
    {
        void onCreate();
        
        void onResume();
        
        void onStart();
    }
}
