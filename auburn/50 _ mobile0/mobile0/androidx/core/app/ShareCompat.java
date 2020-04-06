// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.app;

import android.text.Spanned;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager$NameNotFoundException;
import android.util.Log;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.os.Parcelable;
import android.net.Uri;
import android.content.Intent;
import java.util.ArrayList;
import android.content.ComponentName;
import android.app.Activity;
import android.os.Build$VERSION;
import android.view.ActionProvider;
import android.content.Context;
import android.widget.ShareActionProvider;
import android.view.MenuItem;
import android.view.Menu;

public final class ShareCompat
{
    public static final String EXTRA_CALLING_ACTIVITY = "androidx.core.app.EXTRA_CALLING_ACTIVITY";
    public static final String EXTRA_CALLING_PACKAGE = "androidx.core.app.EXTRA_CALLING_PACKAGE";
    private static final String HISTORY_FILENAME_PREFIX = ".sharecompat_";
    
    private ShareCompat() {
    }
    
    public static void configureMenuItem(final Menu menu, final int i, final IntentBuilder intentBuilder) {
        final MenuItem item = menu.findItem(i);
        if (item != null) {
            configureMenuItem(item, intentBuilder);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Could not find menu item with id ");
        sb.append(i);
        sb.append(" in the supplied menu");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static void configureMenuItem(final MenuItem menuItem, final IntentBuilder intentBuilder) {
        final ActionProvider actionProvider = menuItem.getActionProvider();
        ShareActionProvider actionProvider2;
        if (!(actionProvider instanceof ShareActionProvider)) {
            actionProvider2 = new ShareActionProvider((Context)intentBuilder.getActivity());
        }
        else {
            actionProvider2 = (ShareActionProvider)actionProvider;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(".sharecompat_");
        sb.append(intentBuilder.getActivity().getClass().getName());
        actionProvider2.setShareHistoryFileName(sb.toString());
        actionProvider2.setShareIntent(intentBuilder.getIntent());
        menuItem.setActionProvider((ActionProvider)actionProvider2);
        if (Build$VERSION.SDK_INT < 16 && !menuItem.hasSubMenu()) {
            menuItem.setIntent(intentBuilder.createChooserIntent());
        }
    }
    
    public static ComponentName getCallingActivity(final Activity activity) {
        ComponentName callingActivity;
        if ((callingActivity = activity.getCallingActivity()) == null) {
            callingActivity = (ComponentName)activity.getIntent().getParcelableExtra("androidx.core.app.EXTRA_CALLING_ACTIVITY");
        }
        return callingActivity;
    }
    
    public static String getCallingPackage(final Activity activity) {
        String s;
        if ((s = activity.getCallingPackage()) == null) {
            s = activity.getIntent().getStringExtra("androidx.core.app.EXTRA_CALLING_PACKAGE");
        }
        return s;
    }
    
    public static class IntentBuilder
    {
        private Activity mActivity;
        private ArrayList<String> mBccAddresses;
        private ArrayList<String> mCcAddresses;
        private CharSequence mChooserTitle;
        private Intent mIntent;
        private ArrayList<Uri> mStreams;
        private ArrayList<String> mToAddresses;
        
        private IntentBuilder(final Activity mActivity) {
            this.mActivity = mActivity;
            (this.mIntent = new Intent().setAction("android.intent.action.SEND")).putExtra("androidx.core.app.EXTRA_CALLING_PACKAGE", mActivity.getPackageName());
            this.mIntent.putExtra("androidx.core.app.EXTRA_CALLING_ACTIVITY", (Parcelable)mActivity.getComponentName());
            this.mIntent.addFlags(524288);
        }
        
        private void combineArrayExtra(final String s, final ArrayList<String> list) {
            final String[] stringArrayExtra = this.mIntent.getStringArrayExtra(s);
            int length;
            if (stringArrayExtra != null) {
                length = stringArrayExtra.length;
            }
            else {
                length = 0;
            }
            final String[] a = new String[list.size() + length];
            list.toArray(a);
            if (stringArrayExtra != null) {
                System.arraycopy(stringArrayExtra, 0, a, list.size(), length);
            }
            this.mIntent.putExtra(s, a);
        }
        
        private void combineArrayExtra(final String s, final String[] array) {
            final Intent intent = this.getIntent();
            final String[] stringArrayExtra = intent.getStringArrayExtra(s);
            int length;
            if (stringArrayExtra != null) {
                length = stringArrayExtra.length;
            }
            else {
                length = 0;
            }
            final String[] array2 = new String[array.length + length];
            if (stringArrayExtra != null) {
                System.arraycopy(stringArrayExtra, 0, array2, 0, length);
            }
            System.arraycopy(array, 0, array2, length, array.length);
            intent.putExtra(s, array2);
        }
        
        public static IntentBuilder from(final Activity activity) {
            return new IntentBuilder(activity);
        }
        
        public IntentBuilder addEmailBcc(final String e) {
            if (this.mBccAddresses == null) {
                this.mBccAddresses = new ArrayList<String>();
            }
            this.mBccAddresses.add(e);
            return this;
        }
        
        public IntentBuilder addEmailBcc(final String[] array) {
            this.combineArrayExtra("android.intent.extra.BCC", array);
            return this;
        }
        
        public IntentBuilder addEmailCc(final String e) {
            if (this.mCcAddresses == null) {
                this.mCcAddresses = new ArrayList<String>();
            }
            this.mCcAddresses.add(e);
            return this;
        }
        
        public IntentBuilder addEmailCc(final String[] array) {
            this.combineArrayExtra("android.intent.extra.CC", array);
            return this;
        }
        
        public IntentBuilder addEmailTo(final String e) {
            if (this.mToAddresses == null) {
                this.mToAddresses = new ArrayList<String>();
            }
            this.mToAddresses.add(e);
            return this;
        }
        
        public IntentBuilder addEmailTo(final String[] array) {
            this.combineArrayExtra("android.intent.extra.EMAIL", array);
            return this;
        }
        
        public IntentBuilder addStream(final Uri uri) {
            final Uri e = (Uri)this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
            if (this.mStreams == null && e == null) {
                return this.setStream(uri);
            }
            if (this.mStreams == null) {
                this.mStreams = new ArrayList<Uri>();
            }
            if (e != null) {
                this.mIntent.removeExtra("android.intent.extra.STREAM");
                this.mStreams.add(e);
            }
            this.mStreams.add(uri);
            return this;
        }
        
        public Intent createChooserIntent() {
            return Intent.createChooser(this.getIntent(), this.mChooserTitle);
        }
        
        Activity getActivity() {
            return this.mActivity;
        }
        
        public Intent getIntent() {
            final ArrayList<String> mToAddresses = this.mToAddresses;
            if (mToAddresses != null) {
                this.combineArrayExtra("android.intent.extra.EMAIL", mToAddresses);
                this.mToAddresses = null;
            }
            final ArrayList<String> mCcAddresses = this.mCcAddresses;
            if (mCcAddresses != null) {
                this.combineArrayExtra("android.intent.extra.CC", mCcAddresses);
                this.mCcAddresses = null;
            }
            final ArrayList<String> mBccAddresses = this.mBccAddresses;
            if (mBccAddresses != null) {
                this.combineArrayExtra("android.intent.extra.BCC", mBccAddresses);
                this.mBccAddresses = null;
            }
            final ArrayList<Uri> mStreams = this.mStreams;
            boolean b = true;
            if (mStreams == null || mStreams.size() <= 1) {
                b = false;
            }
            final boolean equals = this.mIntent.getAction().equals("android.intent.action.SEND_MULTIPLE");
            if (!b && equals) {
                this.mIntent.setAction("android.intent.action.SEND");
                final ArrayList<Uri> mStreams2 = this.mStreams;
                if (mStreams2 != null && !mStreams2.isEmpty()) {
                    this.mIntent.putExtra("android.intent.extra.STREAM", (Parcelable)this.mStreams.get(0));
                }
                else {
                    this.mIntent.removeExtra("android.intent.extra.STREAM");
                }
                this.mStreams = null;
            }
            if (b && !equals) {
                this.mIntent.setAction("android.intent.action.SEND_MULTIPLE");
                final ArrayList<Uri> mStreams3 = this.mStreams;
                if (mStreams3 != null && !mStreams3.isEmpty()) {
                    this.mIntent.putParcelableArrayListExtra("android.intent.extra.STREAM", (ArrayList)this.mStreams);
                }
                else {
                    this.mIntent.removeExtra("android.intent.extra.STREAM");
                }
            }
            return this.mIntent;
        }
        
        public IntentBuilder setChooserTitle(final int n) {
            return this.setChooserTitle(this.mActivity.getText(n));
        }
        
        public IntentBuilder setChooserTitle(final CharSequence mChooserTitle) {
            this.mChooserTitle = mChooserTitle;
            return this;
        }
        
        public IntentBuilder setEmailBcc(final String[] array) {
            this.mIntent.putExtra("android.intent.extra.BCC", array);
            return this;
        }
        
        public IntentBuilder setEmailCc(final String[] array) {
            this.mIntent.putExtra("android.intent.extra.CC", array);
            return this;
        }
        
        public IntentBuilder setEmailTo(final String[] array) {
            if (this.mToAddresses != null) {
                this.mToAddresses = null;
            }
            this.mIntent.putExtra("android.intent.extra.EMAIL", array);
            return this;
        }
        
        public IntentBuilder setHtmlText(final String s) {
            this.mIntent.putExtra("android.intent.extra.HTML_TEXT", s);
            if (!this.mIntent.hasExtra("android.intent.extra.TEXT")) {
                this.setText((CharSequence)Html.fromHtml(s));
            }
            return this;
        }
        
        public IntentBuilder setStream(final Uri uri) {
            if (!this.mIntent.getAction().equals("android.intent.action.SEND")) {
                this.mIntent.setAction("android.intent.action.SEND");
            }
            this.mStreams = null;
            this.mIntent.putExtra("android.intent.extra.STREAM", (Parcelable)uri);
            return this;
        }
        
        public IntentBuilder setSubject(final String s) {
            this.mIntent.putExtra("android.intent.extra.SUBJECT", s);
            return this;
        }
        
        public IntentBuilder setText(final CharSequence charSequence) {
            this.mIntent.putExtra("android.intent.extra.TEXT", charSequence);
            return this;
        }
        
        public IntentBuilder setType(final String type) {
            this.mIntent.setType(type);
            return this;
        }
        
        public void startChooser() {
            this.mActivity.startActivity(this.createChooserIntent());
        }
    }
    
    public static class IntentReader
    {
        private static final String TAG = "IntentReader";
        private Activity mActivity;
        private ComponentName mCallingActivity;
        private String mCallingPackage;
        private Intent mIntent;
        private ArrayList<Uri> mStreams;
        
        private IntentReader(final Activity mActivity) {
            this.mActivity = mActivity;
            this.mIntent = mActivity.getIntent();
            this.mCallingPackage = ShareCompat.getCallingPackage(mActivity);
            this.mCallingActivity = ShareCompat.getCallingActivity(mActivity);
        }
        
        public static IntentReader from(final Activity activity) {
            return new IntentReader(activity);
        }
        
        private static void withinStyle(final StringBuilder sb, final CharSequence charSequence, int i, final int n) {
            while (i < n) {
                final char char1 = charSequence.charAt(i);
                if (char1 == '<') {
                    sb.append("&lt;");
                }
                else if (char1 == '>') {
                    sb.append("&gt;");
                }
                else if (char1 == '&') {
                    sb.append("&amp;");
                }
                else if (char1 <= '~' && char1 >= ' ') {
                    if (char1 == ' ') {
                        while (i + 1 < n && charSequence.charAt(i + 1) == ' ') {
                            sb.append("&nbsp;");
                            ++i;
                        }
                        sb.append(' ');
                    }
                    else {
                        sb.append(char1);
                    }
                }
                else {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("&#");
                    sb2.append((int)char1);
                    sb2.append(";");
                    sb.append(sb2.toString());
                }
                ++i;
            }
        }
        
        public ComponentName getCallingActivity() {
            return this.mCallingActivity;
        }
        
        public Drawable getCallingActivityIcon() {
            if (this.mCallingActivity == null) {
                return null;
            }
            final PackageManager packageManager = this.mActivity.getPackageManager();
            try {
                return packageManager.getActivityIcon(this.mCallingActivity);
            }
            catch (PackageManager$NameNotFoundException ex) {
                Log.e("IntentReader", "Could not retrieve icon for calling activity", (Throwable)ex);
                return null;
            }
        }
        
        public Drawable getCallingApplicationIcon() {
            if (this.mCallingPackage == null) {
                return null;
            }
            final PackageManager packageManager = this.mActivity.getPackageManager();
            try {
                return packageManager.getApplicationIcon(this.mCallingPackage);
            }
            catch (PackageManager$NameNotFoundException ex) {
                Log.e("IntentReader", "Could not retrieve icon for calling application", (Throwable)ex);
                return null;
            }
        }
        
        public CharSequence getCallingApplicationLabel() {
            if (this.mCallingPackage == null) {
                return null;
            }
            final PackageManager packageManager = this.mActivity.getPackageManager();
            try {
                return packageManager.getApplicationLabel(packageManager.getApplicationInfo(this.mCallingPackage, 0));
            }
            catch (PackageManager$NameNotFoundException ex) {
                Log.e("IntentReader", "Could not retrieve label for calling application", (Throwable)ex);
                return null;
            }
        }
        
        public String getCallingPackage() {
            return this.mCallingPackage;
        }
        
        public String[] getEmailBcc() {
            return this.mIntent.getStringArrayExtra("android.intent.extra.BCC");
        }
        
        public String[] getEmailCc() {
            return this.mIntent.getStringArrayExtra("android.intent.extra.CC");
        }
        
        public String[] getEmailTo() {
            return this.mIntent.getStringArrayExtra("android.intent.extra.EMAIL");
        }
        
        public String getHtmlText() {
            String s2;
            final String s = s2 = this.mIntent.getStringExtra("android.intent.extra.HTML_TEXT");
            if (s == null) {
                final CharSequence text = this.getText();
                if (text instanceof Spanned) {
                    s2 = Html.toHtml((Spanned)text);
                }
                else {
                    s2 = s;
                    if (text != null) {
                        if (Build$VERSION.SDK_INT >= 16) {
                            s2 = Html.escapeHtml(text);
                        }
                        else {
                            final StringBuilder sb = new StringBuilder();
                            withinStyle(sb, text, 0, text.length());
                            s2 = sb.toString();
                        }
                    }
                }
            }
            return s2;
        }
        
        public Uri getStream() {
            return (Uri)this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
        }
        
        public Uri getStream(final int n) {
            if (this.mStreams == null && this.isMultipleShare()) {
                this.mStreams = (ArrayList<Uri>)this.mIntent.getParcelableArrayListExtra("android.intent.extra.STREAM");
            }
            final ArrayList<Uri> mStreams = this.mStreams;
            if (mStreams != null) {
                return mStreams.get(n);
            }
            if (n == 0) {
                return (Uri)this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Stream items available: ");
            sb.append(this.getStreamCount());
            sb.append(" index requested: ");
            sb.append(n);
            throw new IndexOutOfBoundsException(sb.toString());
        }
        
        public int getStreamCount() {
            if (this.mStreams == null && this.isMultipleShare()) {
                this.mStreams = (ArrayList<Uri>)this.mIntent.getParcelableArrayListExtra("android.intent.extra.STREAM");
            }
            final ArrayList<Uri> mStreams = this.mStreams;
            if (mStreams != null) {
                return mStreams.size();
            }
            return this.mIntent.hasExtra("android.intent.extra.STREAM") ? 1 : 0;
        }
        
        public String getSubject() {
            return this.mIntent.getStringExtra("android.intent.extra.SUBJECT");
        }
        
        public CharSequence getText() {
            return this.mIntent.getCharSequenceExtra("android.intent.extra.TEXT");
        }
        
        public String getType() {
            return this.mIntent.getType();
        }
        
        public boolean isMultipleShare() {
            return "android.intent.action.SEND_MULTIPLE".equals(this.mIntent.getAction());
        }
        
        public boolean isShareIntent() {
            final String action = this.mIntent.getAction();
            return "android.intent.action.SEND".equals(action) || "android.intent.action.SEND_MULTIPLE".equals(action);
        }
        
        public boolean isSingleShare() {
            return "android.intent.action.SEND".equals(this.mIntent.getAction());
        }
    }
}
