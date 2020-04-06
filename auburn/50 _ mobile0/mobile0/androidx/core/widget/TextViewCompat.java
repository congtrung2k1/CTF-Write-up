// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.widget;

import android.view.ActionMode;
import android.view.MenuItem;
import java.lang.reflect.InvocationTargetException;
import android.view.Menu;
import android.text.Editable;
import java.util.Iterator;
import android.app.Activity;
import java.util.ArrayList;
import java.util.List;
import android.content.pm.PackageManager;
import android.content.Context;
import android.content.pm.ResolveInfo;
import android.content.Intent;
import java.lang.reflect.Method;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.graphics.Paint$FontMetricsInt;
import androidx.core.util.Preconditions;
import android.view.ActionMode$Callback;
import android.util.Log;
import android.graphics.Paint;
import android.text.TextPaint;
import androidx.core.text.PrecomputedTextCompat;
import android.icu.text.DecimalFormatSymbols;
import android.text.method.PasswordTransformationMethod;
import android.text.TextDirectionHeuristics;
import android.text.TextDirectionHeuristic;
import android.graphics.drawable.Drawable;
import android.os.Build$VERSION;
import android.widget.TextView;
import java.lang.reflect.Field;

public final class TextViewCompat
{
    public static final int AUTO_SIZE_TEXT_TYPE_NONE = 0;
    public static final int AUTO_SIZE_TEXT_TYPE_UNIFORM = 1;
    private static final int LINES = 1;
    private static final String LOG_TAG = "TextViewCompat";
    private static Field sMaxModeField;
    private static boolean sMaxModeFieldFetched;
    private static Field sMaximumField;
    private static boolean sMaximumFieldFetched;
    private static Field sMinModeField;
    private static boolean sMinModeFieldFetched;
    private static Field sMinimumField;
    private static boolean sMinimumFieldFetched;
    
    private TextViewCompat() {
    }
    
    public static int getAutoSizeMaxTextSize(final TextView textView) {
        if (Build$VERSION.SDK_INT >= 27) {
            return textView.getAutoSizeMaxTextSize();
        }
        if (textView instanceof AutoSizeableTextView) {
            return ((AutoSizeableTextView)textView).getAutoSizeMaxTextSize();
        }
        return -1;
    }
    
    public static int getAutoSizeMinTextSize(final TextView textView) {
        if (Build$VERSION.SDK_INT >= 27) {
            return textView.getAutoSizeMinTextSize();
        }
        if (textView instanceof AutoSizeableTextView) {
            return ((AutoSizeableTextView)textView).getAutoSizeMinTextSize();
        }
        return -1;
    }
    
    public static int getAutoSizeStepGranularity(final TextView textView) {
        if (Build$VERSION.SDK_INT >= 27) {
            return textView.getAutoSizeStepGranularity();
        }
        if (textView instanceof AutoSizeableTextView) {
            return ((AutoSizeableTextView)textView).getAutoSizeStepGranularity();
        }
        return -1;
    }
    
    public static int[] getAutoSizeTextAvailableSizes(final TextView textView) {
        if (Build$VERSION.SDK_INT >= 27) {
            return textView.getAutoSizeTextAvailableSizes();
        }
        if (textView instanceof AutoSizeableTextView) {
            return ((AutoSizeableTextView)textView).getAutoSizeTextAvailableSizes();
        }
        return new int[0];
    }
    
    public static int getAutoSizeTextType(final TextView textView) {
        if (Build$VERSION.SDK_INT >= 27) {
            return textView.getAutoSizeTextType();
        }
        if (textView instanceof AutoSizeableTextView) {
            return ((AutoSizeableTextView)textView).getAutoSizeTextType();
        }
        return 0;
    }
    
    public static Drawable[] getCompoundDrawablesRelative(final TextView textView) {
        if (Build$VERSION.SDK_INT >= 18) {
            return textView.getCompoundDrawablesRelative();
        }
        if (Build$VERSION.SDK_INT >= 17) {
            final int layoutDirection = textView.getLayoutDirection();
            boolean b = true;
            if (layoutDirection != 1) {
                b = false;
            }
            final Drawable[] compoundDrawables = textView.getCompoundDrawables();
            if (b) {
                final Drawable drawable = compoundDrawables[2];
                final Drawable drawable2 = compoundDrawables[0];
                compoundDrawables[0] = drawable;
                compoundDrawables[2] = drawable2;
            }
            return compoundDrawables;
        }
        return textView.getCompoundDrawables();
    }
    
    public static int getFirstBaselineToTopHeight(final TextView textView) {
        return textView.getPaddingTop() - textView.getPaint().getFontMetricsInt().top;
    }
    
    public static int getLastBaselineToBottomHeight(final TextView textView) {
        return textView.getPaddingBottom() + textView.getPaint().getFontMetricsInt().bottom;
    }
    
    public static int getMaxLines(final TextView textView) {
        if (Build$VERSION.SDK_INT >= 16) {
            return textView.getMaxLines();
        }
        if (!TextViewCompat.sMaxModeFieldFetched) {
            TextViewCompat.sMaxModeField = retrieveField("mMaxMode");
            TextViewCompat.sMaxModeFieldFetched = true;
        }
        final Field sMaxModeField = TextViewCompat.sMaxModeField;
        if (sMaxModeField != null && retrieveIntFromField(sMaxModeField, textView) == 1) {
            if (!TextViewCompat.sMaximumFieldFetched) {
                TextViewCompat.sMaximumField = retrieveField("mMaximum");
                TextViewCompat.sMaximumFieldFetched = true;
            }
            final Field sMaximumField = TextViewCompat.sMaximumField;
            if (sMaximumField != null) {
                return retrieveIntFromField(sMaximumField, textView);
            }
        }
        return -1;
    }
    
    public static int getMinLines(final TextView textView) {
        if (Build$VERSION.SDK_INT >= 16) {
            return textView.getMinLines();
        }
        if (!TextViewCompat.sMinModeFieldFetched) {
            TextViewCompat.sMinModeField = retrieveField("mMinMode");
            TextViewCompat.sMinModeFieldFetched = true;
        }
        final Field sMinModeField = TextViewCompat.sMinModeField;
        if (sMinModeField != null && retrieveIntFromField(sMinModeField, textView) == 1) {
            if (!TextViewCompat.sMinimumFieldFetched) {
                TextViewCompat.sMinimumField = retrieveField("mMinimum");
                TextViewCompat.sMinimumFieldFetched = true;
            }
            final Field sMinimumField = TextViewCompat.sMinimumField;
            if (sMinimumField != null) {
                return retrieveIntFromField(sMinimumField, textView);
            }
        }
        return -1;
    }
    
    private static int getTextDirection(final TextDirectionHeuristic textDirectionHeuristic) {
        if (textDirectionHeuristic == TextDirectionHeuristics.FIRSTSTRONG_RTL) {
            return 1;
        }
        if (textDirectionHeuristic == TextDirectionHeuristics.FIRSTSTRONG_LTR) {
            return 1;
        }
        if (textDirectionHeuristic == TextDirectionHeuristics.ANYRTL_LTR) {
            return 2;
        }
        if (textDirectionHeuristic == TextDirectionHeuristics.LTR) {
            return 3;
        }
        if (textDirectionHeuristic == TextDirectionHeuristics.RTL) {
            return 4;
        }
        if (textDirectionHeuristic == TextDirectionHeuristics.LOCALE) {
            return 5;
        }
        if (textDirectionHeuristic == TextDirectionHeuristics.FIRSTSTRONG_LTR) {
            return 6;
        }
        if (textDirectionHeuristic == TextDirectionHeuristics.FIRSTSTRONG_RTL) {
            return 7;
        }
        return 1;
    }
    
    private static TextDirectionHeuristic getTextDirectionHeuristic(final TextView textView) {
        if (textView.getTransformationMethod() instanceof PasswordTransformationMethod) {
            return TextDirectionHeuristics.LTR;
        }
        final int sdk_INT = Build$VERSION.SDK_INT;
        boolean b = false;
        if (sdk_INT >= 28 && (textView.getInputType() & 0xF) == 0x3) {
            final byte directionality = Character.getDirectionality(DecimalFormatSymbols.getInstance(textView.getTextLocale()).getDigitStrings()[0].codePointAt(0));
            if (directionality != 1 && directionality != 2) {
                return TextDirectionHeuristics.LTR;
            }
            return TextDirectionHeuristics.RTL;
        }
        else {
            if (textView.getLayoutDirection() == 1) {
                b = true;
            }
            switch (textView.getTextDirection()) {
                default: {
                    TextDirectionHeuristic textDirectionHeuristic;
                    if (b) {
                        textDirectionHeuristic = TextDirectionHeuristics.FIRSTSTRONG_RTL;
                    }
                    else {
                        textDirectionHeuristic = TextDirectionHeuristics.FIRSTSTRONG_LTR;
                    }
                    return textDirectionHeuristic;
                }
                case 7: {
                    return TextDirectionHeuristics.FIRSTSTRONG_RTL;
                }
                case 6: {
                    return TextDirectionHeuristics.FIRSTSTRONG_LTR;
                }
                case 5: {
                    return TextDirectionHeuristics.LOCALE;
                }
                case 4: {
                    return TextDirectionHeuristics.RTL;
                }
                case 3: {
                    return TextDirectionHeuristics.LTR;
                }
                case 2: {
                    return TextDirectionHeuristics.ANYRTL_LTR;
                }
            }
        }
    }
    
    public static PrecomputedTextCompat.Params getTextMetricsParams(final TextView textView) {
        if (Build$VERSION.SDK_INT >= 28) {
            return new PrecomputedTextCompat.Params(textView.getTextMetricsParams());
        }
        final PrecomputedTextCompat.Params.Builder builder = new PrecomputedTextCompat.Params.Builder(new TextPaint((Paint)textView.getPaint()));
        if (Build$VERSION.SDK_INT >= 23) {
            builder.setBreakStrategy(textView.getBreakStrategy());
            builder.setHyphenationFrequency(textView.getHyphenationFrequency());
        }
        if (Build$VERSION.SDK_INT >= 18) {
            builder.setTextDirection(getTextDirectionHeuristic(textView));
        }
        return builder.build();
    }
    
    private static Field retrieveField(final String s) {
        Field declaredField = null;
        try {
            final Field field = declaredField = TextView.class.getDeclaredField(s);
            field.setAccessible(true);
            declaredField = field;
        }
        catch (NoSuchFieldException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Could not retrieve ");
            sb.append(s);
            sb.append(" field.");
            Log.e("TextViewCompat", sb.toString());
        }
        return declaredField;
    }
    
    private static int retrieveIntFromField(final Field field, final TextView obj) {
        try {
            return field.getInt(obj);
        }
        catch (IllegalAccessException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Could not retrieve value of ");
            sb.append(field.getName());
            sb.append(" field.");
            Log.d("TextViewCompat", sb.toString());
            return -1;
        }
    }
    
    public static void setAutoSizeTextTypeUniformWithConfiguration(final TextView textView, final int n, final int n2, final int n3, final int n4) throws IllegalArgumentException {
        if (Build$VERSION.SDK_INT >= 27) {
            textView.setAutoSizeTextTypeUniformWithConfiguration(n, n2, n3, n4);
        }
        else if (textView instanceof AutoSizeableTextView) {
            ((AutoSizeableTextView)textView).setAutoSizeTextTypeUniformWithConfiguration(n, n2, n3, n4);
        }
    }
    
    public static void setAutoSizeTextTypeUniformWithPresetSizes(final TextView textView, final int[] array, final int n) throws IllegalArgumentException {
        if (Build$VERSION.SDK_INT >= 27) {
            textView.setAutoSizeTextTypeUniformWithPresetSizes(array, n);
        }
        else if (textView instanceof AutoSizeableTextView) {
            ((AutoSizeableTextView)textView).setAutoSizeTextTypeUniformWithPresetSizes(array, n);
        }
    }
    
    public static void setAutoSizeTextTypeWithDefaults(final TextView textView, final int n) {
        if (Build$VERSION.SDK_INT >= 27) {
            textView.setAutoSizeTextTypeWithDefaults(n);
        }
        else if (textView instanceof AutoSizeableTextView) {
            ((AutoSizeableTextView)textView).setAutoSizeTextTypeWithDefaults(n);
        }
    }
    
    public static void setCompoundDrawablesRelative(final TextView textView, Drawable drawable, final Drawable drawable2, final Drawable drawable3, final Drawable drawable4) {
        if (Build$VERSION.SDK_INT >= 18) {
            textView.setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
        }
        else if (Build$VERSION.SDK_INT >= 17) {
            final int layoutDirection = textView.getLayoutDirection();
            boolean b = true;
            if (layoutDirection != 1) {
                b = false;
            }
            Drawable drawable5;
            if (b) {
                drawable5 = drawable3;
            }
            else {
                drawable5 = drawable;
            }
            if (!b) {
                drawable = drawable3;
            }
            textView.setCompoundDrawables(drawable5, drawable2, drawable, drawable4);
        }
        else {
            textView.setCompoundDrawables(drawable, drawable2, drawable3, drawable4);
        }
    }
    
    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(final TextView textView, int n, final int n2, final int n3, final int n4) {
        if (Build$VERSION.SDK_INT >= 18) {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(n, n2, n3, n4);
        }
        else if (Build$VERSION.SDK_INT >= 17) {
            final int layoutDirection = textView.getLayoutDirection();
            boolean b = true;
            if (layoutDirection != 1) {
                b = false;
            }
            int n5;
            if (b) {
                n5 = n3;
            }
            else {
                n5 = n;
            }
            if (!b) {
                n = n3;
            }
            textView.setCompoundDrawablesWithIntrinsicBounds(n5, n2, n, n4);
        }
        else {
            textView.setCompoundDrawablesWithIntrinsicBounds(n, n2, n3, n4);
        }
    }
    
    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(final TextView textView, Drawable drawable, final Drawable drawable2, final Drawable drawable3, final Drawable drawable4) {
        if (Build$VERSION.SDK_INT >= 18) {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
        }
        else if (Build$VERSION.SDK_INT >= 17) {
            final int layoutDirection = textView.getLayoutDirection();
            boolean b = true;
            if (layoutDirection != 1) {
                b = false;
            }
            Drawable drawable5;
            if (b) {
                drawable5 = drawable3;
            }
            else {
                drawable5 = drawable;
            }
            if (!b) {
                drawable = drawable3;
            }
            textView.setCompoundDrawablesWithIntrinsicBounds(drawable5, drawable2, drawable, drawable4);
        }
        else {
            textView.setCompoundDrawablesWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
        }
    }
    
    public static void setCustomSelectionActionModeCallback(final TextView textView, final ActionMode$Callback actionMode$Callback) {
        textView.setCustomSelectionActionModeCallback(wrapCustomSelectionActionModeCallback(textView, actionMode$Callback));
    }
    
    public static void setFirstBaselineToTopHeight(final TextView textView, final int firstBaselineToTopHeight) {
        Preconditions.checkArgumentNonnegative(firstBaselineToTopHeight);
        if (Build$VERSION.SDK_INT >= 28) {
            textView.setFirstBaselineToTopHeight(firstBaselineToTopHeight);
            return;
        }
        final Paint$FontMetricsInt fontMetricsInt = textView.getPaint().getFontMetricsInt();
        int a;
        if (Build$VERSION.SDK_INT >= 16 && !textView.getIncludeFontPadding()) {
            a = fontMetricsInt.ascent;
        }
        else {
            a = fontMetricsInt.top;
        }
        if (firstBaselineToTopHeight > Math.abs(a)) {
            textView.setPadding(textView.getPaddingLeft(), firstBaselineToTopHeight - -a, textView.getPaddingRight(), textView.getPaddingBottom());
        }
    }
    
    public static void setLastBaselineToBottomHeight(final TextView textView, final int n) {
        Preconditions.checkArgumentNonnegative(n);
        final Paint$FontMetricsInt fontMetricsInt = textView.getPaint().getFontMetricsInt();
        int a;
        if (Build$VERSION.SDK_INT >= 16 && !textView.getIncludeFontPadding()) {
            a = fontMetricsInt.descent;
        }
        else {
            a = fontMetricsInt.bottom;
        }
        if (n > Math.abs(a)) {
            textView.setPadding(textView.getPaddingLeft(), textView.getPaddingTop(), textView.getPaddingRight(), n - a);
        }
    }
    
    public static void setLineHeight(final TextView textView, final int n) {
        Preconditions.checkArgumentNonnegative(n);
        final int fontMetricsInt = textView.getPaint().getFontMetricsInt((Paint$FontMetricsInt)null);
        if (n != fontMetricsInt) {
            textView.setLineSpacing((float)(n - fontMetricsInt), 1.0f);
        }
    }
    
    public static void setPrecomputedText(final TextView textView, final PrecomputedTextCompat text) {
        if (getTextMetricsParams(textView).equalsWithoutTextDirection(text.getParams())) {
            textView.setText((CharSequence)text);
            return;
        }
        throw new IllegalArgumentException("Given text can not be applied to TextView.");
    }
    
    public static void setTextAppearance(final TextView textView, final int textAppearance) {
        if (Build$VERSION.SDK_INT >= 23) {
            textView.setTextAppearance(textAppearance);
        }
        else {
            textView.setTextAppearance(textView.getContext(), textAppearance);
        }
    }
    
    public static void setTextMetricsParams(final TextView textView, final PrecomputedTextCompat.Params params) {
        if (Build$VERSION.SDK_INT >= 18) {
            textView.setTextDirection(getTextDirection(params.getTextDirection()));
        }
        if (Build$VERSION.SDK_INT < 23) {
            final float textScaleX = params.getTextPaint().getTextScaleX();
            textView.getPaint().set(params.getTextPaint());
            if (textScaleX == textView.getTextScaleX()) {
                textView.setTextScaleX(textScaleX / 2.0f + 1.0f);
            }
            textView.setTextScaleX(textScaleX);
        }
        else {
            textView.getPaint().set(params.getTextPaint());
            textView.setBreakStrategy(params.getBreakStrategy());
            textView.setHyphenationFrequency(params.getHyphenationFrequency());
        }
    }
    
    public static ActionMode$Callback wrapCustomSelectionActionModeCallback(final TextView textView, final ActionMode$Callback actionMode$Callback) {
        if (Build$VERSION.SDK_INT >= 26 && Build$VERSION.SDK_INT <= 27 && !(actionMode$Callback instanceof OreoCallback)) {
            return (ActionMode$Callback)new OreoCallback(actionMode$Callback, textView);
        }
        return actionMode$Callback;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface AutoSizeTextType {
    }
    
    private static class OreoCallback implements ActionMode$Callback
    {
        private static final int MENU_ITEM_ORDER_PROCESS_TEXT_INTENT_ACTIONS_START = 100;
        private final ActionMode$Callback mCallback;
        private boolean mCanUseMenuBuilderReferences;
        private boolean mInitializedMenuBuilderReferences;
        private Class mMenuBuilderClass;
        private Method mMenuBuilderRemoveItemAtMethod;
        private final TextView mTextView;
        
        OreoCallback(final ActionMode$Callback mCallback, final TextView mTextView) {
            this.mCallback = mCallback;
            this.mTextView = mTextView;
            this.mInitializedMenuBuilderReferences = false;
        }
        
        private Intent createProcessTextIntent() {
            return new Intent().setAction("android.intent.action.PROCESS_TEXT").setType("text/plain");
        }
        
        private Intent createProcessTextIntentForResolveInfo(final ResolveInfo resolveInfo, final TextView textView) {
            return this.createProcessTextIntent().putExtra("android.intent.extra.PROCESS_TEXT_READONLY", this.isEditable(textView) ^ true).setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
        }
        
        private List<ResolveInfo> getSupportedActivities(final Context context, final PackageManager packageManager) {
            final ArrayList<ResolveInfo> list = new ArrayList<ResolveInfo>();
            if (!(context instanceof Activity)) {
                return list;
            }
            for (final ResolveInfo resolveInfo : packageManager.queryIntentActivities(this.createProcessTextIntent(), 0)) {
                if (this.isSupportedActivity(resolveInfo, context)) {
                    list.add(resolveInfo);
                }
            }
            return list;
        }
        
        private boolean isEditable(final TextView textView) {
            return textView instanceof Editable && textView.onCheckIsTextEditor() && textView.isEnabled();
        }
        
        private boolean isSupportedActivity(final ResolveInfo resolveInfo, final Context context) {
            final boolean equals = context.getPackageName().equals(resolveInfo.activityInfo.packageName);
            boolean b = true;
            if (equals) {
                return true;
            }
            if (!resolveInfo.activityInfo.exported) {
                return false;
            }
            if (resolveInfo.activityInfo.permission != null) {
                if (context.checkSelfPermission(resolveInfo.activityInfo.permission) != 0) {
                    b = false;
                }
            }
            return b;
        }
        
        private void recomputeProcessTextMenuItems(final Menu obj) {
            final Context context = this.mTextView.getContext();
            final PackageManager packageManager = context.getPackageManager();
            Label_0089: {
                if (!this.mInitializedMenuBuilderReferences) {
                    this.mInitializedMenuBuilderReferences = true;
                    try {
                        final Class<?> forName = Class.forName("com.android.internal.view.menu.MenuBuilder");
                        this.mMenuBuilderClass = forName;
                        this.mMenuBuilderRemoveItemAtMethod = forName.getDeclaredMethod("removeItemAt", Integer.TYPE);
                        this.mCanUseMenuBuilderReferences = true;
                        break Label_0089;
                    }
                    catch (NoSuchMethodException ex) {}
                    catch (ClassNotFoundException ex2) {}
                    this.mMenuBuilderClass = null;
                    this.mMenuBuilderRemoveItemAtMethod = null;
                    this.mCanUseMenuBuilderReferences = false;
                }
                try {
                    Method method;
                    if (this.mCanUseMenuBuilderReferences && this.mMenuBuilderClass.isInstance(obj)) {
                        method = this.mMenuBuilderRemoveItemAtMethod;
                    }
                    else {
                        method = obj.getClass().getDeclaredMethod("removeItemAt", Integer.TYPE);
                    }
                    for (int i = obj.size() - 1; i >= 0; --i) {
                        final MenuItem item = obj.getItem(i);
                        if (item.getIntent() != null && "android.intent.action.PROCESS_TEXT".equals(item.getIntent().getAction())) {
                            method.invoke(obj, i);
                        }
                    }
                    final List<ResolveInfo> supportedActivities = this.getSupportedActivities(context, packageManager);
                    for (int j = 0; j < supportedActivities.size(); ++j) {
                        final ResolveInfo resolveInfo = supportedActivities.get(j);
                        obj.add(0, 0, j + 100, resolveInfo.loadLabel(packageManager)).setIntent(this.createProcessTextIntentForResolveInfo(resolveInfo, this.mTextView)).setShowAsAction(1);
                    }
                }
                catch (InvocationTargetException ex3) {}
                catch (IllegalAccessException ex4) {}
                catch (NoSuchMethodException ex5) {}
            }
        }
        
        public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
            return this.mCallback.onActionItemClicked(actionMode, menuItem);
        }
        
        public boolean onCreateActionMode(final ActionMode actionMode, final Menu menu) {
            return this.mCallback.onCreateActionMode(actionMode, menu);
        }
        
        public void onDestroyActionMode(final ActionMode actionMode) {
            this.mCallback.onDestroyActionMode(actionMode);
        }
        
        public boolean onPrepareActionMode(final ActionMode actionMode, final Menu menu) {
            this.recomputeProcessTextMenuItems(menu);
            return this.mCallback.onPrepareActionMode(actionMode, menu);
        }
    }
}
