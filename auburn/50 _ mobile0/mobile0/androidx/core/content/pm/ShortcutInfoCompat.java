// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.content.pm;

import android.text.TextUtils;
import android.content.pm.ShortcutInfo$Builder;
import android.content.pm.ShortcutInfo;
import java.util.Arrays;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.content.pm.PackageManager$NameNotFoundException;
import android.os.Parcelable;
import android.content.Intent;
import androidx.core.graphics.drawable.IconCompat;
import android.content.Context;
import android.content.ComponentName;

public class ShortcutInfoCompat
{
    ComponentName mActivity;
    Context mContext;
    CharSequence mDisabledMessage;
    IconCompat mIcon;
    String mId;
    Intent[] mIntents;
    boolean mIsAlwaysBadged;
    CharSequence mLabel;
    CharSequence mLongLabel;
    
    ShortcutInfoCompat() {
    }
    
    Intent addToIntent(final Intent intent) {
        final Intent[] mIntents = this.mIntents;
        intent.putExtra("android.intent.extra.shortcut.INTENT", (Parcelable)mIntents[mIntents.length - 1]).putExtra("android.intent.extra.shortcut.NAME", this.mLabel.toString());
        if (this.mIcon != null) {
            Drawable loadIcon = null;
            final Drawable drawable = null;
            if (this.mIsAlwaysBadged) {
                final PackageManager packageManager = this.mContext.getPackageManager();
                final ComponentName mActivity = this.mActivity;
                Drawable activityIcon = drawable;
                if (mActivity != null) {
                    try {
                        activityIcon = packageManager.getActivityIcon(mActivity);
                    }
                    catch (PackageManager$NameNotFoundException ex) {
                        activityIcon = drawable;
                    }
                }
                if ((loadIcon = activityIcon) == null) {
                    loadIcon = this.mContext.getApplicationInfo().loadIcon(packageManager);
                }
            }
            this.mIcon.addToShortcutIntent(intent, loadIcon, this.mContext);
        }
        return intent;
    }
    
    public ComponentName getActivity() {
        return this.mActivity;
    }
    
    public CharSequence getDisabledMessage() {
        return this.mDisabledMessage;
    }
    
    public String getId() {
        return this.mId;
    }
    
    public Intent getIntent() {
        final Intent[] mIntents = this.mIntents;
        return mIntents[mIntents.length - 1];
    }
    
    public Intent[] getIntents() {
        final Intent[] mIntents = this.mIntents;
        return Arrays.copyOf(mIntents, mIntents.length);
    }
    
    public CharSequence getLongLabel() {
        return this.mLongLabel;
    }
    
    public CharSequence getShortLabel() {
        return this.mLabel;
    }
    
    public ShortcutInfo toShortcutInfo() {
        final ShortcutInfo$Builder setIntents = new ShortcutInfo$Builder(this.mContext, this.mId).setShortLabel(this.mLabel).setIntents(this.mIntents);
        final IconCompat mIcon = this.mIcon;
        if (mIcon != null) {
            setIntents.setIcon(mIcon.toIcon());
        }
        if (!TextUtils.isEmpty(this.mLongLabel)) {
            setIntents.setLongLabel(this.mLongLabel);
        }
        if (!TextUtils.isEmpty(this.mDisabledMessage)) {
            setIntents.setDisabledMessage(this.mDisabledMessage);
        }
        final ComponentName mActivity = this.mActivity;
        if (mActivity != null) {
            setIntents.setActivity(mActivity);
        }
        return setIntents.build();
    }
    
    public static class Builder
    {
        private final ShortcutInfoCompat mInfo;
        
        public Builder(final Context mContext, final String mId) {
            final ShortcutInfoCompat mInfo = new ShortcutInfoCompat();
            this.mInfo = mInfo;
            mInfo.mContext = mContext;
            this.mInfo.mId = mId;
        }
        
        public ShortcutInfoCompat build() {
            if (TextUtils.isEmpty(this.mInfo.mLabel)) {
                throw new IllegalArgumentException("Shortcut must have a non-empty label");
            }
            if (this.mInfo.mIntents != null && this.mInfo.mIntents.length != 0) {
                return this.mInfo;
            }
            throw new IllegalArgumentException("Shortcut must have an intent");
        }
        
        public Builder setActivity(final ComponentName mActivity) {
            this.mInfo.mActivity = mActivity;
            return this;
        }
        
        public Builder setAlwaysBadged() {
            this.mInfo.mIsAlwaysBadged = true;
            return this;
        }
        
        public Builder setDisabledMessage(final CharSequence mDisabledMessage) {
            this.mInfo.mDisabledMessage = mDisabledMessage;
            return this;
        }
        
        public Builder setIcon(final IconCompat mIcon) {
            this.mInfo.mIcon = mIcon;
            return this;
        }
        
        public Builder setIntent(final Intent intent) {
            return this.setIntents(new Intent[] { intent });
        }
        
        public Builder setIntents(final Intent[] mIntents) {
            this.mInfo.mIntents = mIntents;
            return this;
        }
        
        public Builder setLongLabel(final CharSequence mLongLabel) {
            this.mInfo.mLongLabel = mLongLabel;
            return this;
        }
        
        public Builder setShortLabel(final CharSequence mLabel) {
            this.mInfo.mLabel = mLabel;
            return this;
        }
    }
}
