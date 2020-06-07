// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.app;

import java.util.HashSet;
import java.util.HashMap;
import android.content.ClipDescription;
import android.app.RemoteInput$Builder;
import java.util.Iterator;
import android.util.Log;
import android.content.ClipData;
import android.os.Build$VERSION;
import android.net.Uri;
import java.util.Map;
import android.content.Intent;
import android.os.Bundle;
import java.util.Set;

public final class RemoteInput
{
    private static final String EXTRA_DATA_TYPE_RESULTS_DATA = "android.remoteinput.dataTypeResultsData";
    public static final String EXTRA_RESULTS_DATA = "android.remoteinput.resultsData";
    public static final String RESULTS_CLIP_LABEL = "android.remoteinput.results";
    private static final String TAG = "RemoteInput";
    private final boolean mAllowFreeFormTextInput;
    private final Set<String> mAllowedDataTypes;
    private final CharSequence[] mChoices;
    private final Bundle mExtras;
    private final CharSequence mLabel;
    private final String mResultKey;
    
    RemoteInput(final String mResultKey, final CharSequence mLabel, final CharSequence[] mChoices, final boolean mAllowFreeFormTextInput, final Bundle mExtras, final Set<String> mAllowedDataTypes) {
        this.mResultKey = mResultKey;
        this.mLabel = mLabel;
        this.mChoices = mChoices;
        this.mAllowFreeFormTextInput = mAllowFreeFormTextInput;
        this.mExtras = mExtras;
        this.mAllowedDataTypes = mAllowedDataTypes;
    }
    
    public static void addDataResultToIntent(final RemoteInput remoteInput, final Intent intent, final Map<String, Uri> map) {
        if (Build$VERSION.SDK_INT >= 26) {
            android.app.RemoteInput.addDataResultToIntent(fromCompat(remoteInput), intent, (Map)map);
        }
        else if (Build$VERSION.SDK_INT >= 16) {
            Intent clipDataIntentFromIntent;
            if ((clipDataIntentFromIntent = getClipDataIntentFromIntent(intent)) == null) {
                clipDataIntentFromIntent = new Intent();
            }
            for (final Map.Entry<String, Uri> entry : map.entrySet()) {
                final String s = entry.getKey();
                final Uri uri = entry.getValue();
                if (s == null) {
                    continue;
                }
                Bundle bundleExtra;
                if ((bundleExtra = clipDataIntentFromIntent.getBundleExtra(getExtraResultsKeyForData(s))) == null) {
                    bundleExtra = new Bundle();
                }
                bundleExtra.putString(remoteInput.getResultKey(), uri.toString());
                clipDataIntentFromIntent.putExtra(getExtraResultsKeyForData(s), bundleExtra);
            }
            intent.setClipData(ClipData.newIntent((CharSequence)"android.remoteinput.results", clipDataIntentFromIntent));
        }
        else {
            Log.w("RemoteInput", "RemoteInput is only supported from API Level 16");
        }
    }
    
    public static void addResultsToIntent(final RemoteInput[] array, final Intent intent, Bundle bundle) {
        if (Build$VERSION.SDK_INT >= 26) {
            android.app.RemoteInput.addResultsToIntent(fromCompat(array), intent, bundle);
        }
        else {
            final int sdk_INT = Build$VERSION.SDK_INT;
            int i = 0;
            if (sdk_INT >= 20) {
                final Bundle resultsFromIntent = getResultsFromIntent(intent);
                if (resultsFromIntent != null) {
                    resultsFromIntent.putAll(bundle);
                    bundle = resultsFromIntent;
                }
                for (final RemoteInput remoteInput : array) {
                    final Map<String, Uri> dataResultsFromIntent = getDataResultsFromIntent(intent, remoteInput.getResultKey());
                    android.app.RemoteInput.addResultsToIntent(fromCompat(new RemoteInput[] { remoteInput }), intent, bundle);
                    if (dataResultsFromIntent != null) {
                        addDataResultToIntent(remoteInput, intent, dataResultsFromIntent);
                    }
                }
            }
            else if (Build$VERSION.SDK_INT >= 16) {
                Intent clipDataIntentFromIntent;
                if ((clipDataIntentFromIntent = getClipDataIntentFromIntent(intent)) == null) {
                    clipDataIntentFromIntent = new Intent();
                }
                Bundle bundleExtra;
                if ((bundleExtra = clipDataIntentFromIntent.getBundleExtra("android.remoteinput.resultsData")) == null) {
                    bundleExtra = new Bundle();
                }
                while (i < array.length) {
                    final RemoteInput remoteInput2 = array[i];
                    final Object value = bundle.get(remoteInput2.getResultKey());
                    if (value instanceof CharSequence) {
                        bundleExtra.putCharSequence(remoteInput2.getResultKey(), (CharSequence)value);
                    }
                    ++i;
                }
                clipDataIntentFromIntent.putExtra("android.remoteinput.resultsData", bundleExtra);
                intent.setClipData(ClipData.newIntent((CharSequence)"android.remoteinput.results", clipDataIntentFromIntent));
            }
            else {
                Log.w("RemoteInput", "RemoteInput is only supported from API Level 16");
            }
        }
    }
    
    static android.app.RemoteInput fromCompat(final RemoteInput remoteInput) {
        return new RemoteInput$Builder(remoteInput.getResultKey()).setLabel(remoteInput.getLabel()).setChoices(remoteInput.getChoices()).setAllowFreeFormInput(remoteInput.getAllowFreeFormInput()).addExtras(remoteInput.getExtras()).build();
    }
    
    static android.app.RemoteInput[] fromCompat(final RemoteInput[] array) {
        if (array == null) {
            return null;
        }
        final android.app.RemoteInput[] array2 = new android.app.RemoteInput[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = fromCompat(array[i]);
        }
        return array2;
    }
    
    private static Intent getClipDataIntentFromIntent(final Intent intent) {
        final ClipData clipData = intent.getClipData();
        if (clipData == null) {
            return null;
        }
        final ClipDescription description = clipData.getDescription();
        if (!description.hasMimeType("text/vnd.android.intent")) {
            return null;
        }
        if (!description.getLabel().equals("android.remoteinput.results")) {
            return null;
        }
        return clipData.getItemAt(0).getIntent();
    }
    
    public static Map<String, Uri> getDataResultsFromIntent(final Intent intent, final String s) {
        if (Build$VERSION.SDK_INT >= 26) {
            return (Map<String, Uri>)android.app.RemoteInput.getDataResultsFromIntent(intent, s);
        }
        final int sdk_INT = Build$VERSION.SDK_INT;
        final HashMap<String, Uri> hashMap = null;
        if (sdk_INT < 16) {
            Log.w("RemoteInput", "RemoteInput is only supported from API Level 16");
            return null;
        }
        final Intent clipDataIntentFromIntent = getClipDataIntentFromIntent(intent);
        if (clipDataIntentFromIntent == null) {
            return null;
        }
        HashMap<String, Uri> hashMap2 = new HashMap<String, Uri>();
        for (final String s2 : clipDataIntentFromIntent.getExtras().keySet()) {
            if (s2.startsWith("android.remoteinput.dataTypeResultsData")) {
                final String substring = s2.substring("android.remoteinput.dataTypeResultsData".length());
                if (substring.isEmpty()) {
                    continue;
                }
                final String string = clipDataIntentFromIntent.getBundleExtra(s2).getString(s);
                if (string == null) {
                    continue;
                }
                if (string.isEmpty()) {
                    continue;
                }
                hashMap2.put(substring, Uri.parse(string));
            }
        }
        if (hashMap2.isEmpty()) {
            hashMap2 = hashMap;
        }
        return hashMap2;
    }
    
    private static String getExtraResultsKeyForData(final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append("android.remoteinput.dataTypeResultsData");
        sb.append(str);
        return sb.toString();
    }
    
    public static Bundle getResultsFromIntent(Intent clipDataIntentFromIntent) {
        if (Build$VERSION.SDK_INT >= 20) {
            return android.app.RemoteInput.getResultsFromIntent(clipDataIntentFromIntent);
        }
        if (Build$VERSION.SDK_INT < 16) {
            Log.w("RemoteInput", "RemoteInput is only supported from API Level 16");
            return null;
        }
        clipDataIntentFromIntent = getClipDataIntentFromIntent(clipDataIntentFromIntent);
        if (clipDataIntentFromIntent == null) {
            return null;
        }
        return (Bundle)clipDataIntentFromIntent.getExtras().getParcelable("android.remoteinput.resultsData");
    }
    
    public boolean getAllowFreeFormInput() {
        return this.mAllowFreeFormTextInput;
    }
    
    public Set<String> getAllowedDataTypes() {
        return this.mAllowedDataTypes;
    }
    
    public CharSequence[] getChoices() {
        return this.mChoices;
    }
    
    public Bundle getExtras() {
        return this.mExtras;
    }
    
    public CharSequence getLabel() {
        return this.mLabel;
    }
    
    public String getResultKey() {
        return this.mResultKey;
    }
    
    public boolean isDataOnly() {
        return !this.getAllowFreeFormInput() && (this.getChoices() == null || this.getChoices().length == 0) && this.getAllowedDataTypes() != null && !this.getAllowedDataTypes().isEmpty();
    }
    
    public static final class Builder
    {
        private boolean mAllowFreeFormTextInput;
        private final Set<String> mAllowedDataTypes;
        private CharSequence[] mChoices;
        private final Bundle mExtras;
        private CharSequence mLabel;
        private final String mResultKey;
        
        public Builder(final String mResultKey) {
            this.mAllowedDataTypes = new HashSet<String>();
            this.mExtras = new Bundle();
            this.mAllowFreeFormTextInput = true;
            if (mResultKey != null) {
                this.mResultKey = mResultKey;
                return;
            }
            throw new IllegalArgumentException("Result key can't be null");
        }
        
        public Builder addExtras(final Bundle bundle) {
            if (bundle != null) {
                this.mExtras.putAll(bundle);
            }
            return this;
        }
        
        public RemoteInput build() {
            return new RemoteInput(this.mResultKey, this.mLabel, this.mChoices, this.mAllowFreeFormTextInput, this.mExtras, this.mAllowedDataTypes);
        }
        
        public Bundle getExtras() {
            return this.mExtras;
        }
        
        public Builder setAllowDataType(final String s, final boolean b) {
            if (b) {
                this.mAllowedDataTypes.add(s);
            }
            else {
                this.mAllowedDataTypes.remove(s);
            }
            return this;
        }
        
        public Builder setAllowFreeFormInput(final boolean mAllowFreeFormTextInput) {
            this.mAllowFreeFormTextInput = mAllowFreeFormTextInput;
            return this;
        }
        
        public Builder setChoices(final CharSequence[] mChoices) {
            this.mChoices = mChoices;
            return this;
        }
        
        public Builder setLabel(final CharSequence mLabel) {
            this.mLabel = mLabel;
            return this;
        }
    }
}