// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.widget;

import android.content.res.TypedArray;
import android.view.ViewTreeObserver;
import android.widget.PopupWindow$OnDismissListener;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import androidx.core.view.ViewCompat;
import android.widget.AdapterView;
import android.widget.AdapterView$OnItemClickListener;
import android.database.DataSetObserver;
import android.widget.ThemedSpinnerAdapter;
import androidx.appcompat.content.res.AppCompatResources;
import android.widget.ListAdapter;
import android.widget.Adapter;
import android.view.MotionEvent;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.view.ViewGroup$LayoutParams;
import android.view.ViewGroup;
import android.view.View$MeasureSpec;
import android.graphics.drawable.Drawable;
import android.widget.ArrayAdapter;
import androidx.appcompat.view.menu.ShowableListMenu;
import android.os.Build$VERSION;
import androidx.appcompat.view.ContextThemeWrapper;
import android.view.View;
import android.content.res.Resources$Theme;
import androidx.appcompat.R;
import android.util.AttributeSet;
import android.graphics.Rect;
import android.widget.SpinnerAdapter;
import android.content.Context;
import androidx.core.view.TintableBackgroundView;
import android.widget.Spinner;

public class AppCompatSpinner extends Spinner implements TintableBackgroundView
{
    private static final int[] ATTRS_ANDROID_SPINNERMODE;
    private static final int MAX_ITEMS_MEASURED = 15;
    private static final int MODE_DIALOG = 0;
    private static final int MODE_DROPDOWN = 1;
    private static final int MODE_THEME = -1;
    private static final String TAG = "AppCompatSpinner";
    private final AppCompatBackgroundHelper mBackgroundTintHelper;
    int mDropDownWidth;
    private ForwardingListener mForwardingListener;
    DropdownPopup mPopup;
    private final Context mPopupContext;
    private final boolean mPopupSet;
    private SpinnerAdapter mTempAdapter;
    final Rect mTempRect;
    
    static {
        ATTRS_ANDROID_SPINNERMODE = new int[] { 16843505 };
    }
    
    public AppCompatSpinner(final Context context) {
        this(context, null);
    }
    
    public AppCompatSpinner(final Context context, final int n) {
        this(context, null, R.attr.spinnerStyle, n);
    }
    
    public AppCompatSpinner(final Context context, final AttributeSet set) {
        this(context, set, R.attr.spinnerStyle);
    }
    
    public AppCompatSpinner(final Context context, final AttributeSet set, final int n) {
        this(context, set, n, -1);
    }
    
    public AppCompatSpinner(final Context context, final AttributeSet set, final int n, final int n2) {
        this(context, set, n, n2, null);
    }
    
    public AppCompatSpinner(final Context context, final AttributeSet set, final int n, int n2, Resources$Theme resources$Theme) {
        super(context, set, n);
        this.mTempRect = new Rect();
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, set, R.styleable.Spinner, n, 0);
        this.mBackgroundTintHelper = new AppCompatBackgroundHelper((View)this);
        if (resources$Theme != null) {
            this.mPopupContext = (Context)new ContextThemeWrapper(context, resources$Theme);
        }
        else {
            final int resourceId = obtainStyledAttributes.getResourceId(R.styleable.Spinner_popupTheme, 0);
            if (resourceId != 0) {
                this.mPopupContext = (Context)new ContextThemeWrapper(context, resourceId);
            }
            else {
                Context mPopupContext;
                if (Build$VERSION.SDK_INT < 23) {
                    mPopupContext = context;
                }
                else {
                    mPopupContext = null;
                }
                this.mPopupContext = mPopupContext;
            }
        }
        if (this.mPopupContext != null) {
            int n3;
            if ((n3 = n2) == -1) {
                resources$Theme = null;
                Object o = null;
                try {
                    try {
                        final Object obtainStyledAttributes2 = context.obtainStyledAttributes(set, AppCompatSpinner.ATTRS_ANDROID_SPINNERMODE, n, 0);
                        int int1 = n2;
                        o = obtainStyledAttributes2;
                        resources$Theme = (Resources$Theme)obtainStyledAttributes2;
                        if (((TypedArray)obtainStyledAttributes2).hasValue(0)) {
                            o = obtainStyledAttributes2;
                            resources$Theme = (Resources$Theme)obtainStyledAttributes2;
                            int1 = ((TypedArray)obtainStyledAttributes2).getInt(0, 0);
                        }
                        n3 = int1;
                        if (obtainStyledAttributes2 != null) {
                            n2 = int1;
                            resources$Theme = (Resources$Theme)obtainStyledAttributes2;
                            ((TypedArray)resources$Theme).recycle();
                            n3 = n2;
                        }
                    }
                    finally {
                        if (o != null) {
                            ((TypedArray)o).recycle();
                        }
                    }
                }
                catch (Exception ex) {}
            }
            if (n3 == 1) {
                final DropdownPopup mPopup = new DropdownPopup(this.mPopupContext, set, n);
                final TintTypedArray obtainStyledAttributes3 = TintTypedArray.obtainStyledAttributes(this.mPopupContext, set, R.styleable.Spinner, n, 0);
                this.mDropDownWidth = obtainStyledAttributes3.getLayoutDimension(R.styleable.Spinner_android_dropDownWidth, -2);
                mPopup.setBackgroundDrawable(obtainStyledAttributes3.getDrawable(R.styleable.Spinner_android_popupBackground));
                mPopup.setPromptText(obtainStyledAttributes.getString(R.styleable.Spinner_android_prompt));
                obtainStyledAttributes3.recycle();
                this.mPopup = mPopup;
                this.mForwardingListener = new ForwardingListener(this) {
                    @Override
                    public ShowableListMenu getPopup() {
                        return mPopup;
                    }
                    
                    public boolean onForwardingStarted() {
                        if (!AppCompatSpinner.this.mPopup.isShowing()) {
                            AppCompatSpinner.this.mPopup.show();
                        }
                        return true;
                    }
                };
            }
        }
        final CharSequence[] textArray = obtainStyledAttributes.getTextArray(R.styleable.Spinner_android_entries);
        if (textArray != null) {
            final ArrayAdapter adapter = new ArrayAdapter(context, 17367048, (Object[])textArray);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            this.setAdapter((SpinnerAdapter)adapter);
        }
        obtainStyledAttributes.recycle();
        this.mPopupSet = true;
        final SpinnerAdapter mTempAdapter = this.mTempAdapter;
        if (mTempAdapter != null) {
            this.setAdapter(mTempAdapter);
            this.mTempAdapter = null;
        }
        this.mBackgroundTintHelper.loadFromAttributes(set, n);
    }
    
    int compatMeasureContentWidth(final SpinnerAdapter spinnerAdapter, final Drawable drawable) {
        if (spinnerAdapter == null) {
            return 0;
        }
        int max = 0;
        View view = null;
        int n = 0;
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 0);
        final int measureSpec2 = View$MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 0);
        final int max2 = Math.max(0, this.getSelectedItemPosition());
        int n2;
        for (int min = Math.min(spinnerAdapter.getCount(), max2 + 15), i = Math.max(0, max2 - (15 - (min - max2))); i < min; ++i, n = n2) {
            final int itemViewType = spinnerAdapter.getItemViewType(i);
            if (itemViewType != (n2 = n)) {
                n2 = itemViewType;
                view = null;
            }
            view = spinnerAdapter.getView(i, view, (ViewGroup)this);
            if (view.getLayoutParams() == null) {
                view.setLayoutParams(new ViewGroup$LayoutParams(-2, -2));
            }
            view.measure(measureSpec, measureSpec2);
            max = Math.max(max, view.getMeasuredWidth());
        }
        int n3 = max;
        if (drawable != null) {
            drawable.getPadding(this.mTempRect);
            n3 = max + (this.mTempRect.left + this.mTempRect.right);
        }
        return n3;
    }
    
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        final AppCompatBackgroundHelper mBackgroundTintHelper = this.mBackgroundTintHelper;
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.applySupportBackgroundTint();
        }
    }
    
    public int getDropDownHorizontalOffset() {
        final DropdownPopup mPopup = this.mPopup;
        if (mPopup != null) {
            return mPopup.getHorizontalOffset();
        }
        if (Build$VERSION.SDK_INT >= 16) {
            return super.getDropDownHorizontalOffset();
        }
        return 0;
    }
    
    public int getDropDownVerticalOffset() {
        final DropdownPopup mPopup = this.mPopup;
        if (mPopup != null) {
            return mPopup.getVerticalOffset();
        }
        if (Build$VERSION.SDK_INT >= 16) {
            return super.getDropDownVerticalOffset();
        }
        return 0;
    }
    
    public int getDropDownWidth() {
        if (this.mPopup != null) {
            return this.mDropDownWidth;
        }
        if (Build$VERSION.SDK_INT >= 16) {
            return super.getDropDownWidth();
        }
        return 0;
    }
    
    public Drawable getPopupBackground() {
        final DropdownPopup mPopup = this.mPopup;
        if (mPopup != null) {
            return mPopup.getBackground();
        }
        if (Build$VERSION.SDK_INT >= 16) {
            return super.getPopupBackground();
        }
        return null;
    }
    
    public Context getPopupContext() {
        if (this.mPopup != null) {
            return this.mPopupContext;
        }
        if (Build$VERSION.SDK_INT >= 23) {
            return super.getPopupContext();
        }
        return null;
    }
    
    public CharSequence getPrompt() {
        final DropdownPopup mPopup = this.mPopup;
        CharSequence charSequence;
        if (mPopup != null) {
            charSequence = mPopup.getHintText();
        }
        else {
            charSequence = super.getPrompt();
        }
        return charSequence;
    }
    
    public ColorStateList getSupportBackgroundTintList() {
        final AppCompatBackgroundHelper mBackgroundTintHelper = this.mBackgroundTintHelper;
        ColorStateList supportBackgroundTintList;
        if (mBackgroundTintHelper != null) {
            supportBackgroundTintList = mBackgroundTintHelper.getSupportBackgroundTintList();
        }
        else {
            supportBackgroundTintList = null;
        }
        return supportBackgroundTintList;
    }
    
    public PorterDuff$Mode getSupportBackgroundTintMode() {
        final AppCompatBackgroundHelper mBackgroundTintHelper = this.mBackgroundTintHelper;
        PorterDuff$Mode supportBackgroundTintMode;
        if (mBackgroundTintHelper != null) {
            supportBackgroundTintMode = mBackgroundTintHelper.getSupportBackgroundTintMode();
        }
        else {
            supportBackgroundTintMode = null;
        }
        return supportBackgroundTintMode;
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        final DropdownPopup mPopup = this.mPopup;
        if (mPopup != null && mPopup.isShowing()) {
            this.mPopup.dismiss();
        }
    }
    
    protected void onMeasure(final int n, int measuredWidth) {
        super.onMeasure(n, measuredWidth);
        if (this.mPopup != null && View$MeasureSpec.getMode(n) == Integer.MIN_VALUE) {
            measuredWidth = this.getMeasuredWidth();
            this.setMeasuredDimension(Math.min(Math.max(measuredWidth, this.compatMeasureContentWidth(this.getAdapter(), this.getBackground())), View$MeasureSpec.getSize(n)), this.getMeasuredHeight());
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final ForwardingListener mForwardingListener = this.mForwardingListener;
        return (mForwardingListener != null && mForwardingListener.onTouch((View)this, motionEvent)) || super.onTouchEvent(motionEvent);
    }
    
    public boolean performClick() {
        final DropdownPopup mPopup = this.mPopup;
        if (mPopup != null) {
            if (!mPopup.isShowing()) {
                this.mPopup.show();
            }
            return true;
        }
        return super.performClick();
    }
    
    public void setAdapter(final SpinnerAdapter spinnerAdapter) {
        if (!this.mPopupSet) {
            this.mTempAdapter = spinnerAdapter;
            return;
        }
        super.setAdapter(spinnerAdapter);
        if (this.mPopup != null) {
            Context context;
            if ((context = this.mPopupContext) == null) {
                context = this.getContext();
            }
            this.mPopup.setAdapter((ListAdapter)new DropDownAdapter(spinnerAdapter, context.getTheme()));
        }
    }
    
    public void setBackgroundDrawable(final Drawable backgroundDrawable) {
        super.setBackgroundDrawable(backgroundDrawable);
        final AppCompatBackgroundHelper mBackgroundTintHelper = this.mBackgroundTintHelper;
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.onSetBackgroundDrawable(backgroundDrawable);
        }
    }
    
    public void setBackgroundResource(final int backgroundResource) {
        super.setBackgroundResource(backgroundResource);
        final AppCompatBackgroundHelper mBackgroundTintHelper = this.mBackgroundTintHelper;
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.onSetBackgroundResource(backgroundResource);
        }
    }
    
    public void setDropDownHorizontalOffset(final int n) {
        final DropdownPopup mPopup = this.mPopup;
        if (mPopup != null) {
            mPopup.setHorizontalOffset(n);
        }
        else if (Build$VERSION.SDK_INT >= 16) {
            super.setDropDownHorizontalOffset(n);
        }
    }
    
    public void setDropDownVerticalOffset(final int n) {
        final DropdownPopup mPopup = this.mPopup;
        if (mPopup != null) {
            mPopup.setVerticalOffset(n);
        }
        else if (Build$VERSION.SDK_INT >= 16) {
            super.setDropDownVerticalOffset(n);
        }
    }
    
    public void setDropDownWidth(final int n) {
        if (this.mPopup != null) {
            this.mDropDownWidth = n;
        }
        else if (Build$VERSION.SDK_INT >= 16) {
            super.setDropDownWidth(n);
        }
    }
    
    public void setPopupBackgroundDrawable(final Drawable drawable) {
        final DropdownPopup mPopup = this.mPopup;
        if (mPopup != null) {
            mPopup.setBackgroundDrawable(drawable);
        }
        else if (Build$VERSION.SDK_INT >= 16) {
            super.setPopupBackgroundDrawable(drawable);
        }
    }
    
    public void setPopupBackgroundResource(final int n) {
        this.setPopupBackgroundDrawable(AppCompatResources.getDrawable(this.getPopupContext(), n));
    }
    
    public void setPrompt(final CharSequence charSequence) {
        final DropdownPopup mPopup = this.mPopup;
        if (mPopup != null) {
            mPopup.setPromptText(charSequence);
        }
        else {
            super.setPrompt(charSequence);
        }
    }
    
    public void setSupportBackgroundTintList(final ColorStateList supportBackgroundTintList) {
        final AppCompatBackgroundHelper mBackgroundTintHelper = this.mBackgroundTintHelper;
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.setSupportBackgroundTintList(supportBackgroundTintList);
        }
    }
    
    public void setSupportBackgroundTintMode(final PorterDuff$Mode supportBackgroundTintMode) {
        final AppCompatBackgroundHelper mBackgroundTintHelper = this.mBackgroundTintHelper;
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.setSupportBackgroundTintMode(supportBackgroundTintMode);
        }
    }
    
    private static class DropDownAdapter implements ListAdapter, SpinnerAdapter
    {
        private SpinnerAdapter mAdapter;
        private ListAdapter mListAdapter;
        
        public DropDownAdapter(final SpinnerAdapter mAdapter, final Resources$Theme resources$Theme) {
            this.mAdapter = mAdapter;
            if (mAdapter instanceof ListAdapter) {
                this.mListAdapter = (ListAdapter)mAdapter;
            }
            if (resources$Theme != null) {
                if (Build$VERSION.SDK_INT >= 23 && mAdapter instanceof ThemedSpinnerAdapter) {
                    final ThemedSpinnerAdapter themedSpinnerAdapter = (ThemedSpinnerAdapter)mAdapter;
                    if (themedSpinnerAdapter.getDropDownViewTheme() != resources$Theme) {
                        themedSpinnerAdapter.setDropDownViewTheme(resources$Theme);
                    }
                }
                else if (mAdapter instanceof androidx.appcompat.widget.ThemedSpinnerAdapter) {
                    final androidx.appcompat.widget.ThemedSpinnerAdapter themedSpinnerAdapter2 = (androidx.appcompat.widget.ThemedSpinnerAdapter)mAdapter;
                    if (themedSpinnerAdapter2.getDropDownViewTheme() == null) {
                        themedSpinnerAdapter2.setDropDownViewTheme(resources$Theme);
                    }
                }
            }
        }
        
        public boolean areAllItemsEnabled() {
            final ListAdapter mListAdapter = this.mListAdapter;
            return mListAdapter == null || mListAdapter.areAllItemsEnabled();
        }
        
        public int getCount() {
            final SpinnerAdapter mAdapter = this.mAdapter;
            int count;
            if (mAdapter == null) {
                count = 0;
            }
            else {
                count = mAdapter.getCount();
            }
            return count;
        }
        
        public View getDropDownView(final int n, View dropDownView, final ViewGroup viewGroup) {
            final SpinnerAdapter mAdapter = this.mAdapter;
            if (mAdapter == null) {
                dropDownView = null;
            }
            else {
                dropDownView = mAdapter.getDropDownView(n, dropDownView, viewGroup);
            }
            return dropDownView;
        }
        
        public Object getItem(final int n) {
            final SpinnerAdapter mAdapter = this.mAdapter;
            Object item;
            if (mAdapter == null) {
                item = null;
            }
            else {
                item = mAdapter.getItem(n);
            }
            return item;
        }
        
        public long getItemId(final int n) {
            final SpinnerAdapter mAdapter = this.mAdapter;
            long itemId;
            if (mAdapter == null) {
                itemId = -1L;
            }
            else {
                itemId = mAdapter.getItemId(n);
            }
            return itemId;
        }
        
        public int getItemViewType(final int n) {
            return 0;
        }
        
        public View getView(final int n, final View view, final ViewGroup viewGroup) {
            return this.getDropDownView(n, view, viewGroup);
        }
        
        public int getViewTypeCount() {
            return 1;
        }
        
        public boolean hasStableIds() {
            final SpinnerAdapter mAdapter = this.mAdapter;
            return mAdapter != null && mAdapter.hasStableIds();
        }
        
        public boolean isEmpty() {
            return this.getCount() == 0;
        }
        
        public boolean isEnabled(final int n) {
            final ListAdapter mListAdapter = this.mListAdapter;
            return mListAdapter == null || mListAdapter.isEnabled(n);
        }
        
        public void registerDataSetObserver(final DataSetObserver dataSetObserver) {
            final SpinnerAdapter mAdapter = this.mAdapter;
            if (mAdapter != null) {
                mAdapter.registerDataSetObserver(dataSetObserver);
            }
        }
        
        public void unregisterDataSetObserver(final DataSetObserver dataSetObserver) {
            final SpinnerAdapter mAdapter = this.mAdapter;
            if (mAdapter != null) {
                mAdapter.unregisterDataSetObserver(dataSetObserver);
            }
        }
    }
    
    private class DropdownPopup extends ListPopupWindow
    {
        ListAdapter mAdapter;
        private CharSequence mHintText;
        private final Rect mVisibleRect;
        final /* synthetic */ AppCompatSpinner this$0;
        
        public DropdownPopup(final Context context, final AttributeSet set, final int n) {
            super(context, set, n);
            this.mVisibleRect = new Rect();
            this.setAnchorView((View)AppCompatSpinner.this);
            this.setModal(true);
            this.setPromptPosition(0);
            this.setOnItemClickListener((AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
                public void onItemClick(final AdapterView<?> adapterView, final View view, final int selection, final long n) {
                    AppCompatSpinner.this.setSelection(selection);
                    if (AppCompatSpinner.this.getOnItemClickListener() != null) {
                        AppCompatSpinner.this.performItemClick(view, selection, DropdownPopup.this.mAdapter.getItemId(selection));
                    }
                    DropdownPopup.this.dismiss();
                }
            });
        }
        
        void computeContentWidth() {
            final Drawable background = this.getBackground();
            int right = 0;
            if (background != null) {
                background.getPadding(AppCompatSpinner.this.mTempRect);
                if (ViewUtils.isLayoutRtl((View)AppCompatSpinner.this)) {
                    right = AppCompatSpinner.this.mTempRect.right;
                }
                else {
                    right = -AppCompatSpinner.this.mTempRect.left;
                }
            }
            else {
                final Rect mTempRect = AppCompatSpinner.this.mTempRect;
                AppCompatSpinner.this.mTempRect.right = 0;
                mTempRect.left = 0;
            }
            final int paddingLeft = AppCompatSpinner.this.getPaddingLeft();
            final int paddingRight = AppCompatSpinner.this.getPaddingRight();
            final int width = AppCompatSpinner.this.getWidth();
            if (AppCompatSpinner.this.mDropDownWidth == -2) {
                final int compatMeasureContentWidth = AppCompatSpinner.this.compatMeasureContentWidth((SpinnerAdapter)this.mAdapter, this.getBackground());
                final int n = AppCompatSpinner.this.getContext().getResources().getDisplayMetrics().widthPixels - AppCompatSpinner.this.mTempRect.left - AppCompatSpinner.this.mTempRect.right;
                int a;
                if ((a = compatMeasureContentWidth) > n) {
                    a = n;
                }
                this.setContentWidth(Math.max(a, width - paddingLeft - paddingRight));
            }
            else if (AppCompatSpinner.this.mDropDownWidth == -1) {
                this.setContentWidth(width - paddingLeft - paddingRight);
            }
            else {
                this.setContentWidth(AppCompatSpinner.this.mDropDownWidth);
            }
            int horizontalOffset;
            if (ViewUtils.isLayoutRtl((View)AppCompatSpinner.this)) {
                horizontalOffset = right + (width - paddingRight - this.getWidth());
            }
            else {
                horizontalOffset = right + paddingLeft;
            }
            this.setHorizontalOffset(horizontalOffset);
        }
        
        public CharSequence getHintText() {
            return this.mHintText;
        }
        
        boolean isVisibleToUser(final View view) {
            return ViewCompat.isAttachedToWindow(view) && view.getGlobalVisibleRect(this.mVisibleRect);
        }
        
        @Override
        public void setAdapter(final ListAdapter listAdapter) {
            super.setAdapter(listAdapter);
            this.mAdapter = listAdapter;
        }
        
        public void setPromptText(final CharSequence mHintText) {
            this.mHintText = mHintText;
        }
        
        @Override
        public void show() {
            final boolean showing = this.isShowing();
            this.computeContentWidth();
            this.setInputMethodMode(2);
            super.show();
            this.getListView().setChoiceMode(1);
            this.setSelection(AppCompatSpinner.this.getSelectedItemPosition());
            if (showing) {
                return;
            }
            final ViewTreeObserver viewTreeObserver = AppCompatSpinner.this.getViewTreeObserver();
            if (viewTreeObserver != null) {
                final ViewTreeObserver$OnGlobalLayoutListener viewTreeObserver$OnGlobalLayoutListener = (ViewTreeObserver$OnGlobalLayoutListener)new ViewTreeObserver$OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        final DropdownPopup this$1 = DropdownPopup.this;
                        if (!this$1.isVisibleToUser((View)this$1.this$0)) {
                            DropdownPopup.this.dismiss();
                        }
                        else {
                            DropdownPopup.this.computeContentWidth();
                            ListPopupWindow.this.show();
                        }
                    }
                };
                viewTreeObserver.addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)viewTreeObserver$OnGlobalLayoutListener);
                this.setOnDismissListener((PopupWindow$OnDismissListener)new PopupWindow$OnDismissListener() {
                    public void onDismiss() {
                        final ViewTreeObserver viewTreeObserver = AppCompatSpinner.this.getViewTreeObserver();
                        if (viewTreeObserver != null) {
                            viewTreeObserver.removeGlobalOnLayoutListener(viewTreeObserver$OnGlobalLayoutListener);
                        }
                    }
                });
            }
        }
    }
}
