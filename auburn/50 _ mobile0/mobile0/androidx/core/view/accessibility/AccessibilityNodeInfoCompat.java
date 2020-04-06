// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.view.accessibility;

import android.view.accessibility.AccessibilityNodeInfo$RangeInfo;
import android.view.accessibility.AccessibilityNodeInfo$CollectionItemInfo;
import android.view.accessibility.AccessibilityNodeInfo$CollectionInfo;
import android.graphics.Rect;
import java.util.Iterator;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import android.view.accessibility.AccessibilityNodeInfo$AccessibilityAction;
import android.os.Build$VERSION;
import android.view.View;
import android.os.Bundle;
import android.view.accessibility.AccessibilityNodeInfo;

public class AccessibilityNodeInfoCompat
{
    public static final int ACTION_ACCESSIBILITY_FOCUS = 64;
    public static final String ACTION_ARGUMENT_COLUMN_INT = "android.view.accessibility.action.ARGUMENT_COLUMN_INT";
    public static final String ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN = "ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN";
    public static final String ACTION_ARGUMENT_HTML_ELEMENT_STRING = "ACTION_ARGUMENT_HTML_ELEMENT_STRING";
    public static final String ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT = "ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT";
    public static final String ACTION_ARGUMENT_MOVE_WINDOW_X = "ACTION_ARGUMENT_MOVE_WINDOW_X";
    public static final String ACTION_ARGUMENT_MOVE_WINDOW_Y = "ACTION_ARGUMENT_MOVE_WINDOW_Y";
    public static final String ACTION_ARGUMENT_PROGRESS_VALUE = "android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE";
    public static final String ACTION_ARGUMENT_ROW_INT = "android.view.accessibility.action.ARGUMENT_ROW_INT";
    public static final String ACTION_ARGUMENT_SELECTION_END_INT = "ACTION_ARGUMENT_SELECTION_END_INT";
    public static final String ACTION_ARGUMENT_SELECTION_START_INT = "ACTION_ARGUMENT_SELECTION_START_INT";
    public static final String ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE = "ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE";
    public static final int ACTION_CLEAR_ACCESSIBILITY_FOCUS = 128;
    public static final int ACTION_CLEAR_FOCUS = 2;
    public static final int ACTION_CLEAR_SELECTION = 8;
    public static final int ACTION_CLICK = 16;
    public static final int ACTION_COLLAPSE = 524288;
    public static final int ACTION_COPY = 16384;
    public static final int ACTION_CUT = 65536;
    public static final int ACTION_DISMISS = 1048576;
    public static final int ACTION_EXPAND = 262144;
    public static final int ACTION_FOCUS = 1;
    public static final int ACTION_LONG_CLICK = 32;
    public static final int ACTION_NEXT_AT_MOVEMENT_GRANULARITY = 256;
    public static final int ACTION_NEXT_HTML_ELEMENT = 1024;
    public static final int ACTION_PASTE = 32768;
    public static final int ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY = 512;
    public static final int ACTION_PREVIOUS_HTML_ELEMENT = 2048;
    public static final int ACTION_SCROLL_BACKWARD = 8192;
    public static final int ACTION_SCROLL_FORWARD = 4096;
    public static final int ACTION_SELECT = 4;
    public static final int ACTION_SET_SELECTION = 131072;
    public static final int ACTION_SET_TEXT = 2097152;
    private static final int BOOLEAN_PROPERTY_IS_HEADING = 2;
    private static final int BOOLEAN_PROPERTY_IS_SHOWING_HINT = 4;
    private static final String BOOLEAN_PROPERTY_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.BOOLEAN_PROPERTY_KEY";
    private static final int BOOLEAN_PROPERTY_SCREEN_READER_FOCUSABLE = 1;
    public static final int FOCUS_ACCESSIBILITY = 2;
    public static final int FOCUS_INPUT = 1;
    private static final String HINT_TEXT_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.HINT_TEXT_KEY";
    public static final int MOVEMENT_GRANULARITY_CHARACTER = 1;
    public static final int MOVEMENT_GRANULARITY_LINE = 4;
    public static final int MOVEMENT_GRANULARITY_PAGE = 16;
    public static final int MOVEMENT_GRANULARITY_PARAGRAPH = 8;
    public static final int MOVEMENT_GRANULARITY_WORD = 2;
    private static final String PANE_TITLE_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.PANE_TITLE_KEY";
    private static final String ROLE_DESCRIPTION_KEY = "AccessibilityNodeInfo.roleDescription";
    private static final String TOOLTIP_TEXT_KEY = "androidx.view.accessibility.AccessibilityNodeInfoCompat.TOOLTIP_TEXT_KEY";
    private final AccessibilityNodeInfo mInfo;
    public int mParentVirtualDescendantId;
    
    private AccessibilityNodeInfoCompat(final AccessibilityNodeInfo mInfo) {
        this.mParentVirtualDescendantId = -1;
        this.mInfo = mInfo;
    }
    
    @Deprecated
    public AccessibilityNodeInfoCompat(final Object o) {
        this.mParentVirtualDescendantId = -1;
        this.mInfo = (AccessibilityNodeInfo)o;
    }
    
    private static String getActionSymbolicName(final int n) {
        if (n == 1) {
            return "ACTION_FOCUS";
        }
        if (n == 2) {
            return "ACTION_CLEAR_FOCUS";
        }
        switch (n) {
            default: {
                return "ACTION_UNKNOWN";
            }
            case 131072: {
                return "ACTION_SET_SELECTION";
            }
            case 65536: {
                return "ACTION_CUT";
            }
            case 32768: {
                return "ACTION_PASTE";
            }
            case 16384: {
                return "ACTION_COPY";
            }
            case 8192: {
                return "ACTION_SCROLL_BACKWARD";
            }
            case 4096: {
                return "ACTION_SCROLL_FORWARD";
            }
            case 2048: {
                return "ACTION_PREVIOUS_HTML_ELEMENT";
            }
            case 1024: {
                return "ACTION_NEXT_HTML_ELEMENT";
            }
            case 512: {
                return "ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY";
            }
            case 256: {
                return "ACTION_NEXT_AT_MOVEMENT_GRANULARITY";
            }
            case 128: {
                return "ACTION_CLEAR_ACCESSIBILITY_FOCUS";
            }
            case 64: {
                return "ACTION_ACCESSIBILITY_FOCUS";
            }
            case 32: {
                return "ACTION_LONG_CLICK";
            }
            case 16: {
                return "ACTION_CLICK";
            }
            case 8: {
                return "ACTION_CLEAR_SELECTION";
            }
            case 4: {
                return "ACTION_SELECT";
            }
        }
    }
    
    private boolean getBooleanProperty(final int n) {
        final Bundle extras = this.getExtras();
        boolean b = false;
        if (extras == null) {
            return false;
        }
        if ((extras.getInt("androidx.view.accessibility.AccessibilityNodeInfoCompat.BOOLEAN_PROPERTY_KEY", 0) & n) == n) {
            b = true;
        }
        return b;
    }
    
    public static AccessibilityNodeInfoCompat obtain() {
        return wrap(AccessibilityNodeInfo.obtain());
    }
    
    public static AccessibilityNodeInfoCompat obtain(final View view) {
        return wrap(AccessibilityNodeInfo.obtain(view));
    }
    
    public static AccessibilityNodeInfoCompat obtain(final View view, final int n) {
        if (Build$VERSION.SDK_INT >= 16) {
            return wrapNonNullInstance(AccessibilityNodeInfo.obtain(view, n));
        }
        return null;
    }
    
    public static AccessibilityNodeInfoCompat obtain(final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        return wrap(AccessibilityNodeInfo.obtain(accessibilityNodeInfoCompat.mInfo));
    }
    
    private void setBooleanProperty(final int n, final boolean b) {
        final Bundle extras = this.getExtras();
        if (extras != null) {
            int n2 = 0;
            final int int1 = extras.getInt("androidx.view.accessibility.AccessibilityNodeInfoCompat.BOOLEAN_PROPERTY_KEY", 0);
            if (b) {
                n2 = n;
            }
            extras.putInt("androidx.view.accessibility.AccessibilityNodeInfoCompat.BOOLEAN_PROPERTY_KEY", n2 | (int1 & ~n));
        }
    }
    
    public static AccessibilityNodeInfoCompat wrap(final AccessibilityNodeInfo accessibilityNodeInfo) {
        return new AccessibilityNodeInfoCompat(accessibilityNodeInfo);
    }
    
    static AccessibilityNodeInfoCompat wrapNonNullInstance(final Object o) {
        if (o != null) {
            return new AccessibilityNodeInfoCompat(o);
        }
        return null;
    }
    
    public void addAction(final int n) {
        this.mInfo.addAction(n);
    }
    
    public void addAction(final AccessibilityActionCompat accessibilityActionCompat) {
        if (Build$VERSION.SDK_INT >= 21) {
            this.mInfo.addAction((AccessibilityNodeInfo$AccessibilityAction)accessibilityActionCompat.mAction);
        }
    }
    
    public void addChild(final View view) {
        this.mInfo.addChild(view);
    }
    
    public void addChild(final View view, final int n) {
        if (Build$VERSION.SDK_INT >= 16) {
            this.mInfo.addChild(view, n);
        }
    }
    
    public boolean canOpenPopup() {
        return Build$VERSION.SDK_INT >= 19 && this.mInfo.canOpenPopup();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = (AccessibilityNodeInfoCompat)o;
        final AccessibilityNodeInfo mInfo = this.mInfo;
        if (mInfo == null) {
            if (accessibilityNodeInfoCompat.mInfo != null) {
                return false;
            }
        }
        else if (!mInfo.equals((Object)accessibilityNodeInfoCompat.mInfo)) {
            return false;
        }
        return true;
    }
    
    public List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByText(final String s) {
        final ArrayList<AccessibilityNodeInfoCompat> list = new ArrayList<AccessibilityNodeInfoCompat>();
        final List accessibilityNodeInfosByText = this.mInfo.findAccessibilityNodeInfosByText(s);
        for (int size = accessibilityNodeInfosByText.size(), i = 0; i < size; ++i) {
            list.add(wrap(accessibilityNodeInfosByText.get(i)));
        }
        return list;
    }
    
    public List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId(final String s) {
        if (Build$VERSION.SDK_INT >= 18) {
            final List accessibilityNodeInfosByViewId = this.mInfo.findAccessibilityNodeInfosByViewId(s);
            final ArrayList<AccessibilityNodeInfoCompat> list = new ArrayList<AccessibilityNodeInfoCompat>();
            final Iterator<AccessibilityNodeInfo> iterator = accessibilityNodeInfosByViewId.iterator();
            while (iterator.hasNext()) {
                list.add(wrap(iterator.next()));
            }
            return list;
        }
        return Collections.emptyList();
    }
    
    public AccessibilityNodeInfoCompat findFocus(final int n) {
        if (Build$VERSION.SDK_INT >= 16) {
            return wrapNonNullInstance(this.mInfo.findFocus(n));
        }
        return null;
    }
    
    public AccessibilityNodeInfoCompat focusSearch(final int n) {
        if (Build$VERSION.SDK_INT >= 16) {
            return wrapNonNullInstance(this.mInfo.focusSearch(n));
        }
        return null;
    }
    
    public List<AccessibilityActionCompat> getActionList() {
        List<Object> actionList = null;
        if (Build$VERSION.SDK_INT >= 21) {
            actionList = (List<Object>)this.mInfo.getActionList();
        }
        if (actionList != null) {
            final ArrayList<AccessibilityActionCompat> list = new ArrayList<AccessibilityActionCompat>();
            for (int size = actionList.size(), i = 0; i < size; ++i) {
                list.add(new AccessibilityActionCompat(actionList.get(i)));
            }
            return list;
        }
        return Collections.emptyList();
    }
    
    public int getActions() {
        return this.mInfo.getActions();
    }
    
    public void getBoundsInParent(final Rect rect) {
        this.mInfo.getBoundsInParent(rect);
    }
    
    public void getBoundsInScreen(final Rect rect) {
        this.mInfo.getBoundsInScreen(rect);
    }
    
    public AccessibilityNodeInfoCompat getChild(final int n) {
        return wrapNonNullInstance(this.mInfo.getChild(n));
    }
    
    public int getChildCount() {
        return this.mInfo.getChildCount();
    }
    
    public CharSequence getClassName() {
        return this.mInfo.getClassName();
    }
    
    public CollectionInfoCompat getCollectionInfo() {
        if (Build$VERSION.SDK_INT >= 19) {
            final AccessibilityNodeInfo$CollectionInfo collectionInfo = this.mInfo.getCollectionInfo();
            if (collectionInfo != null) {
                return new CollectionInfoCompat(collectionInfo);
            }
        }
        return null;
    }
    
    public CollectionItemInfoCompat getCollectionItemInfo() {
        if (Build$VERSION.SDK_INT >= 19) {
            final AccessibilityNodeInfo$CollectionItemInfo collectionItemInfo = this.mInfo.getCollectionItemInfo();
            if (collectionItemInfo != null) {
                return new CollectionItemInfoCompat(collectionItemInfo);
            }
        }
        return null;
    }
    
    public CharSequence getContentDescription() {
        return this.mInfo.getContentDescription();
    }
    
    public int getDrawingOrder() {
        if (Build$VERSION.SDK_INT >= 24) {
            return this.mInfo.getDrawingOrder();
        }
        return 0;
    }
    
    public CharSequence getError() {
        if (Build$VERSION.SDK_INT >= 21) {
            return this.mInfo.getError();
        }
        return null;
    }
    
    public Bundle getExtras() {
        if (Build$VERSION.SDK_INT >= 19) {
            return this.mInfo.getExtras();
        }
        return new Bundle();
    }
    
    public CharSequence getHintText() {
        if (Build$VERSION.SDK_INT >= 26) {
            return this.mInfo.getHintText();
        }
        if (Build$VERSION.SDK_INT >= 19) {
            return this.mInfo.getExtras().getCharSequence("androidx.view.accessibility.AccessibilityNodeInfoCompat.HINT_TEXT_KEY");
        }
        return null;
    }
    
    @Deprecated
    public Object getInfo() {
        return this.mInfo;
    }
    
    public int getInputType() {
        if (Build$VERSION.SDK_INT >= 19) {
            return this.mInfo.getInputType();
        }
        return 0;
    }
    
    public AccessibilityNodeInfoCompat getLabelFor() {
        if (Build$VERSION.SDK_INT >= 17) {
            return wrapNonNullInstance(this.mInfo.getLabelFor());
        }
        return null;
    }
    
    public AccessibilityNodeInfoCompat getLabeledBy() {
        if (Build$VERSION.SDK_INT >= 17) {
            return wrapNonNullInstance(this.mInfo.getLabeledBy());
        }
        return null;
    }
    
    public int getLiveRegion() {
        if (Build$VERSION.SDK_INT >= 19) {
            return this.mInfo.getLiveRegion();
        }
        return 0;
    }
    
    public int getMaxTextLength() {
        if (Build$VERSION.SDK_INT >= 21) {
            return this.mInfo.getMaxTextLength();
        }
        return -1;
    }
    
    public int getMovementGranularities() {
        if (Build$VERSION.SDK_INT >= 16) {
            return this.mInfo.getMovementGranularities();
        }
        return 0;
    }
    
    public CharSequence getPackageName() {
        return this.mInfo.getPackageName();
    }
    
    public CharSequence getPaneTitle() {
        if (Build$VERSION.SDK_INT >= 28) {
            return this.mInfo.getPaneTitle();
        }
        if (Build$VERSION.SDK_INT >= 19) {
            return this.mInfo.getExtras().getCharSequence("androidx.view.accessibility.AccessibilityNodeInfoCompat.PANE_TITLE_KEY");
        }
        return null;
    }
    
    public AccessibilityNodeInfoCompat getParent() {
        return wrapNonNullInstance(this.mInfo.getParent());
    }
    
    public RangeInfoCompat getRangeInfo() {
        if (Build$VERSION.SDK_INT >= 19) {
            final AccessibilityNodeInfo$RangeInfo rangeInfo = this.mInfo.getRangeInfo();
            if (rangeInfo != null) {
                return new RangeInfoCompat(rangeInfo);
            }
        }
        return null;
    }
    
    public CharSequence getRoleDescription() {
        if (Build$VERSION.SDK_INT >= 19) {
            return this.mInfo.getExtras().getCharSequence("AccessibilityNodeInfo.roleDescription");
        }
        return null;
    }
    
    public CharSequence getText() {
        return this.mInfo.getText();
    }
    
    public int getTextSelectionEnd() {
        if (Build$VERSION.SDK_INT >= 18) {
            return this.mInfo.getTextSelectionEnd();
        }
        return -1;
    }
    
    public int getTextSelectionStart() {
        if (Build$VERSION.SDK_INT >= 18) {
            return this.mInfo.getTextSelectionStart();
        }
        return -1;
    }
    
    public CharSequence getTooltipText() {
        if (Build$VERSION.SDK_INT >= 28) {
            return this.mInfo.getTooltipText();
        }
        if (Build$VERSION.SDK_INT >= 19) {
            return this.mInfo.getExtras().getCharSequence("androidx.view.accessibility.AccessibilityNodeInfoCompat.TOOLTIP_TEXT_KEY");
        }
        return null;
    }
    
    public AccessibilityNodeInfoCompat getTraversalAfter() {
        if (Build$VERSION.SDK_INT >= 22) {
            return wrapNonNullInstance(this.mInfo.getTraversalAfter());
        }
        return null;
    }
    
    public AccessibilityNodeInfoCompat getTraversalBefore() {
        if (Build$VERSION.SDK_INT >= 22) {
            return wrapNonNullInstance(this.mInfo.getTraversalBefore());
        }
        return null;
    }
    
    public String getViewIdResourceName() {
        if (Build$VERSION.SDK_INT >= 18) {
            return this.mInfo.getViewIdResourceName();
        }
        return null;
    }
    
    public AccessibilityWindowInfoCompat getWindow() {
        if (Build$VERSION.SDK_INT >= 21) {
            return AccessibilityWindowInfoCompat.wrapNonNullInstance(this.mInfo.getWindow());
        }
        return null;
    }
    
    public int getWindowId() {
        return this.mInfo.getWindowId();
    }
    
    @Override
    public int hashCode() {
        final AccessibilityNodeInfo mInfo = this.mInfo;
        int hashCode;
        if (mInfo == null) {
            hashCode = 0;
        }
        else {
            hashCode = mInfo.hashCode();
        }
        return hashCode;
    }
    
    public boolean isAccessibilityFocused() {
        return Build$VERSION.SDK_INT >= 16 && this.mInfo.isAccessibilityFocused();
    }
    
    public boolean isCheckable() {
        return this.mInfo.isCheckable();
    }
    
    public boolean isChecked() {
        return this.mInfo.isChecked();
    }
    
    public boolean isClickable() {
        return this.mInfo.isClickable();
    }
    
    public boolean isContentInvalid() {
        return Build$VERSION.SDK_INT >= 19 && this.mInfo.isContentInvalid();
    }
    
    public boolean isContextClickable() {
        return Build$VERSION.SDK_INT >= 23 && this.mInfo.isContextClickable();
    }
    
    public boolean isDismissable() {
        return Build$VERSION.SDK_INT >= 19 && this.mInfo.isDismissable();
    }
    
    public boolean isEditable() {
        return Build$VERSION.SDK_INT >= 18 && this.mInfo.isEditable();
    }
    
    public boolean isEnabled() {
        return this.mInfo.isEnabled();
    }
    
    public boolean isFocusable() {
        return this.mInfo.isFocusable();
    }
    
    public boolean isFocused() {
        return this.mInfo.isFocused();
    }
    
    public boolean isHeading() {
        if (Build$VERSION.SDK_INT >= 28) {
            return this.mInfo.isHeading();
        }
        final boolean booleanProperty = this.getBooleanProperty(2);
        boolean b = true;
        if (booleanProperty) {
            return true;
        }
        final CollectionItemInfoCompat collectionItemInfo = this.getCollectionItemInfo();
        if (collectionItemInfo == null || !collectionItemInfo.isHeading()) {
            b = false;
        }
        return b;
    }
    
    public boolean isImportantForAccessibility() {
        return Build$VERSION.SDK_INT < 24 || this.mInfo.isImportantForAccessibility();
    }
    
    public boolean isLongClickable() {
        return this.mInfo.isLongClickable();
    }
    
    public boolean isMultiLine() {
        return Build$VERSION.SDK_INT >= 19 && this.mInfo.isMultiLine();
    }
    
    public boolean isPassword() {
        return this.mInfo.isPassword();
    }
    
    public boolean isScreenReaderFocusable() {
        if (Build$VERSION.SDK_INT >= 28) {
            return this.mInfo.isScreenReaderFocusable();
        }
        return this.getBooleanProperty(1);
    }
    
    public boolean isScrollable() {
        return this.mInfo.isScrollable();
    }
    
    public boolean isSelected() {
        return this.mInfo.isSelected();
    }
    
    public boolean isShowingHintText() {
        if (Build$VERSION.SDK_INT >= 26) {
            return this.mInfo.isShowingHintText();
        }
        return this.getBooleanProperty(4);
    }
    
    public boolean isVisibleToUser() {
        return Build$VERSION.SDK_INT >= 16 && this.mInfo.isVisibleToUser();
    }
    
    public boolean performAction(final int n) {
        return this.mInfo.performAction(n);
    }
    
    public boolean performAction(final int n, final Bundle bundle) {
        return Build$VERSION.SDK_INT >= 16 && this.mInfo.performAction(n, bundle);
    }
    
    public void recycle() {
        this.mInfo.recycle();
    }
    
    public boolean refresh() {
        return Build$VERSION.SDK_INT >= 18 && this.mInfo.refresh();
    }
    
    public boolean removeAction(final AccessibilityActionCompat accessibilityActionCompat) {
        return Build$VERSION.SDK_INT >= 21 && this.mInfo.removeAction((AccessibilityNodeInfo$AccessibilityAction)accessibilityActionCompat.mAction);
    }
    
    public boolean removeChild(final View view) {
        return Build$VERSION.SDK_INT >= 21 && this.mInfo.removeChild(view);
    }
    
    public boolean removeChild(final View view, final int n) {
        return Build$VERSION.SDK_INT >= 21 && this.mInfo.removeChild(view, n);
    }
    
    public void setAccessibilityFocused(final boolean accessibilityFocused) {
        if (Build$VERSION.SDK_INT >= 16) {
            this.mInfo.setAccessibilityFocused(accessibilityFocused);
        }
    }
    
    public void setBoundsInParent(final Rect boundsInParent) {
        this.mInfo.setBoundsInParent(boundsInParent);
    }
    
    public void setBoundsInScreen(final Rect boundsInScreen) {
        this.mInfo.setBoundsInScreen(boundsInScreen);
    }
    
    public void setCanOpenPopup(final boolean canOpenPopup) {
        if (Build$VERSION.SDK_INT >= 19) {
            this.mInfo.setCanOpenPopup(canOpenPopup);
        }
    }
    
    public void setCheckable(final boolean checkable) {
        this.mInfo.setCheckable(checkable);
    }
    
    public void setChecked(final boolean checked) {
        this.mInfo.setChecked(checked);
    }
    
    public void setClassName(final CharSequence className) {
        this.mInfo.setClassName(className);
    }
    
    public void setClickable(final boolean clickable) {
        this.mInfo.setClickable(clickable);
    }
    
    public void setCollectionInfo(final Object o) {
        if (Build$VERSION.SDK_INT >= 19) {
            final AccessibilityNodeInfo mInfo = this.mInfo;
            AccessibilityNodeInfo$CollectionInfo collectionInfo;
            if (o == null) {
                collectionInfo = null;
            }
            else {
                collectionInfo = (AccessibilityNodeInfo$CollectionInfo)((CollectionInfoCompat)o).mInfo;
            }
            mInfo.setCollectionInfo(collectionInfo);
        }
    }
    
    public void setCollectionItemInfo(final Object o) {
        if (Build$VERSION.SDK_INT >= 19) {
            final AccessibilityNodeInfo mInfo = this.mInfo;
            AccessibilityNodeInfo$CollectionItemInfo collectionItemInfo;
            if (o == null) {
                collectionItemInfo = null;
            }
            else {
                collectionItemInfo = (AccessibilityNodeInfo$CollectionItemInfo)((CollectionItemInfoCompat)o).mInfo;
            }
            mInfo.setCollectionItemInfo(collectionItemInfo);
        }
    }
    
    public void setContentDescription(final CharSequence contentDescription) {
        this.mInfo.setContentDescription(contentDescription);
    }
    
    public void setContentInvalid(final boolean contentInvalid) {
        if (Build$VERSION.SDK_INT >= 19) {
            this.mInfo.setContentInvalid(contentInvalid);
        }
    }
    
    public void setContextClickable(final boolean contextClickable) {
        if (Build$VERSION.SDK_INT >= 23) {
            this.mInfo.setContextClickable(contextClickable);
        }
    }
    
    public void setDismissable(final boolean dismissable) {
        if (Build$VERSION.SDK_INT >= 19) {
            this.mInfo.setDismissable(dismissable);
        }
    }
    
    public void setDrawingOrder(final int drawingOrder) {
        if (Build$VERSION.SDK_INT >= 24) {
            this.mInfo.setDrawingOrder(drawingOrder);
        }
    }
    
    public void setEditable(final boolean editable) {
        if (Build$VERSION.SDK_INT >= 18) {
            this.mInfo.setEditable(editable);
        }
    }
    
    public void setEnabled(final boolean enabled) {
        this.mInfo.setEnabled(enabled);
    }
    
    public void setError(final CharSequence error) {
        if (Build$VERSION.SDK_INT >= 21) {
            this.mInfo.setError(error);
        }
    }
    
    public void setFocusable(final boolean focusable) {
        this.mInfo.setFocusable(focusable);
    }
    
    public void setFocused(final boolean focused) {
        this.mInfo.setFocused(focused);
    }
    
    public void setHeading(final boolean heading) {
        if (Build$VERSION.SDK_INT >= 28) {
            this.mInfo.setHeading(heading);
        }
        else {
            this.setBooleanProperty(2, heading);
        }
    }
    
    public void setHintText(final CharSequence hintText) {
        if (Build$VERSION.SDK_INT >= 26) {
            this.mInfo.setHintText(hintText);
        }
        else if (Build$VERSION.SDK_INT >= 19) {
            this.mInfo.getExtras().putCharSequence("androidx.view.accessibility.AccessibilityNodeInfoCompat.HINT_TEXT_KEY", hintText);
        }
    }
    
    public void setImportantForAccessibility(final boolean importantForAccessibility) {
        if (Build$VERSION.SDK_INT >= 24) {
            this.mInfo.setImportantForAccessibility(importantForAccessibility);
        }
    }
    
    public void setInputType(final int inputType) {
        if (Build$VERSION.SDK_INT >= 19) {
            this.mInfo.setInputType(inputType);
        }
    }
    
    public void setLabelFor(final View labelFor) {
        if (Build$VERSION.SDK_INT >= 17) {
            this.mInfo.setLabelFor(labelFor);
        }
    }
    
    public void setLabelFor(final View view, final int n) {
        if (Build$VERSION.SDK_INT >= 17) {
            this.mInfo.setLabelFor(view, n);
        }
    }
    
    public void setLabeledBy(final View labeledBy) {
        if (Build$VERSION.SDK_INT >= 17) {
            this.mInfo.setLabeledBy(labeledBy);
        }
    }
    
    public void setLabeledBy(final View view, final int n) {
        if (Build$VERSION.SDK_INT >= 17) {
            this.mInfo.setLabeledBy(view, n);
        }
    }
    
    public void setLiveRegion(final int liveRegion) {
        if (Build$VERSION.SDK_INT >= 19) {
            this.mInfo.setLiveRegion(liveRegion);
        }
    }
    
    public void setLongClickable(final boolean longClickable) {
        this.mInfo.setLongClickable(longClickable);
    }
    
    public void setMaxTextLength(final int maxTextLength) {
        if (Build$VERSION.SDK_INT >= 21) {
            this.mInfo.setMaxTextLength(maxTextLength);
        }
    }
    
    public void setMovementGranularities(final int movementGranularities) {
        if (Build$VERSION.SDK_INT >= 16) {
            this.mInfo.setMovementGranularities(movementGranularities);
        }
    }
    
    public void setMultiLine(final boolean multiLine) {
        if (Build$VERSION.SDK_INT >= 19) {
            this.mInfo.setMultiLine(multiLine);
        }
    }
    
    public void setPackageName(final CharSequence packageName) {
        this.mInfo.setPackageName(packageName);
    }
    
    public void setPaneTitle(final CharSequence paneTitle) {
        if (Build$VERSION.SDK_INT >= 28) {
            this.mInfo.setPaneTitle(paneTitle);
        }
        else if (Build$VERSION.SDK_INT >= 19) {
            this.mInfo.getExtras().putCharSequence("androidx.view.accessibility.AccessibilityNodeInfoCompat.PANE_TITLE_KEY", paneTitle);
        }
    }
    
    public void setParent(final View parent) {
        this.mInfo.setParent(parent);
    }
    
    public void setParent(final View view, final int mParentVirtualDescendantId) {
        this.mParentVirtualDescendantId = mParentVirtualDescendantId;
        if (Build$VERSION.SDK_INT >= 16) {
            this.mInfo.setParent(view, mParentVirtualDescendantId);
        }
    }
    
    public void setPassword(final boolean password) {
        this.mInfo.setPassword(password);
    }
    
    public void setRangeInfo(final RangeInfoCompat rangeInfoCompat) {
        if (Build$VERSION.SDK_INT >= 19) {
            this.mInfo.setRangeInfo((AccessibilityNodeInfo$RangeInfo)rangeInfoCompat.mInfo);
        }
    }
    
    public void setRoleDescription(final CharSequence charSequence) {
        if (Build$VERSION.SDK_INT >= 19) {
            this.mInfo.getExtras().putCharSequence("AccessibilityNodeInfo.roleDescription", charSequence);
        }
    }
    
    public void setScreenReaderFocusable(final boolean screenReaderFocusable) {
        if (Build$VERSION.SDK_INT >= 28) {
            this.mInfo.setScreenReaderFocusable(screenReaderFocusable);
        }
        else {
            this.setBooleanProperty(1, screenReaderFocusable);
        }
    }
    
    public void setScrollable(final boolean scrollable) {
        this.mInfo.setScrollable(scrollable);
    }
    
    public void setSelected(final boolean selected) {
        this.mInfo.setSelected(selected);
    }
    
    public void setShowingHintText(final boolean showingHintText) {
        if (Build$VERSION.SDK_INT >= 26) {
            this.mInfo.setShowingHintText(showingHintText);
        }
        else {
            this.setBooleanProperty(4, showingHintText);
        }
    }
    
    public void setSource(final View source) {
        this.mInfo.setSource(source);
    }
    
    public void setSource(final View view, final int n) {
        if (Build$VERSION.SDK_INT >= 16) {
            this.mInfo.setSource(view, n);
        }
    }
    
    public void setText(final CharSequence text) {
        this.mInfo.setText(text);
    }
    
    public void setTextSelection(final int n, final int n2) {
        if (Build$VERSION.SDK_INT >= 18) {
            this.mInfo.setTextSelection(n, n2);
        }
    }
    
    public void setTooltipText(final CharSequence tooltipText) {
        if (Build$VERSION.SDK_INT >= 28) {
            this.mInfo.setTooltipText(tooltipText);
        }
        else if (Build$VERSION.SDK_INT >= 19) {
            this.mInfo.getExtras().putCharSequence("androidx.view.accessibility.AccessibilityNodeInfoCompat.TOOLTIP_TEXT_KEY", tooltipText);
        }
    }
    
    public void setTraversalAfter(final View traversalAfter) {
        if (Build$VERSION.SDK_INT >= 22) {
            this.mInfo.setTraversalAfter(traversalAfter);
        }
    }
    
    public void setTraversalAfter(final View view, final int n) {
        if (Build$VERSION.SDK_INT >= 22) {
            this.mInfo.setTraversalAfter(view, n);
        }
    }
    
    public void setTraversalBefore(final View traversalBefore) {
        if (Build$VERSION.SDK_INT >= 22) {
            this.mInfo.setTraversalBefore(traversalBefore);
        }
    }
    
    public void setTraversalBefore(final View view, final int n) {
        if (Build$VERSION.SDK_INT >= 22) {
            this.mInfo.setTraversalBefore(view, n);
        }
    }
    
    public void setViewIdResourceName(final String viewIdResourceName) {
        if (Build$VERSION.SDK_INT >= 18) {
            this.mInfo.setViewIdResourceName(viewIdResourceName);
        }
    }
    
    public void setVisibleToUser(final boolean visibleToUser) {
        if (Build$VERSION.SDK_INT >= 16) {
            this.mInfo.setVisibleToUser(visibleToUser);
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        final Rect rect = new Rect();
        this.getBoundsInParent(rect);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("; boundsInParent: ");
        sb2.append(rect);
        sb.append(sb2.toString());
        this.getBoundsInScreen(rect);
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("; boundsInScreen: ");
        sb3.append(rect);
        sb.append(sb3.toString());
        sb.append("; packageName: ");
        sb.append(this.getPackageName());
        sb.append("; className: ");
        sb.append(this.getClassName());
        sb.append("; text: ");
        sb.append(this.getText());
        sb.append("; contentDescription: ");
        sb.append(this.getContentDescription());
        sb.append("; viewId: ");
        sb.append(this.getViewIdResourceName());
        sb.append("; checkable: ");
        sb.append(this.isCheckable());
        sb.append("; checked: ");
        sb.append(this.isChecked());
        sb.append("; focusable: ");
        sb.append(this.isFocusable());
        sb.append("; focused: ");
        sb.append(this.isFocused());
        sb.append("; selected: ");
        sb.append(this.isSelected());
        sb.append("; clickable: ");
        sb.append(this.isClickable());
        sb.append("; longClickable: ");
        sb.append(this.isLongClickable());
        sb.append("; enabled: ");
        sb.append(this.isEnabled());
        sb.append("; password: ");
        sb.append(this.isPassword());
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("; scrollable: ");
        sb4.append(this.isScrollable());
        sb.append(sb4.toString());
        sb.append("; [");
        int i = this.getActions();
        while (i != 0) {
            final int n = 1 << Integer.numberOfTrailingZeros(i);
            i &= ~n;
            sb.append(getActionSymbolicName(n));
            if (i != 0) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    public AccessibilityNodeInfo unwrap() {
        return this.mInfo;
    }
    
    public static class AccessibilityActionCompat
    {
        public static final AccessibilityActionCompat ACTION_ACCESSIBILITY_FOCUS;
        public static final AccessibilityActionCompat ACTION_CLEAR_ACCESSIBILITY_FOCUS;
        public static final AccessibilityActionCompat ACTION_CLEAR_FOCUS;
        public static final AccessibilityActionCompat ACTION_CLEAR_SELECTION;
        public static final AccessibilityActionCompat ACTION_CLICK;
        public static final AccessibilityActionCompat ACTION_COLLAPSE;
        public static final AccessibilityActionCompat ACTION_CONTEXT_CLICK;
        public static final AccessibilityActionCompat ACTION_COPY;
        public static final AccessibilityActionCompat ACTION_CUT;
        public static final AccessibilityActionCompat ACTION_DISMISS;
        public static final AccessibilityActionCompat ACTION_EXPAND;
        public static final AccessibilityActionCompat ACTION_FOCUS;
        public static final AccessibilityActionCompat ACTION_HIDE_TOOLTIP;
        public static final AccessibilityActionCompat ACTION_LONG_CLICK;
        public static final AccessibilityActionCompat ACTION_MOVE_WINDOW;
        public static final AccessibilityActionCompat ACTION_NEXT_AT_MOVEMENT_GRANULARITY;
        public static final AccessibilityActionCompat ACTION_NEXT_HTML_ELEMENT;
        public static final AccessibilityActionCompat ACTION_PASTE;
        public static final AccessibilityActionCompat ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY;
        public static final AccessibilityActionCompat ACTION_PREVIOUS_HTML_ELEMENT;
        public static final AccessibilityActionCompat ACTION_SCROLL_BACKWARD;
        public static final AccessibilityActionCompat ACTION_SCROLL_DOWN;
        public static final AccessibilityActionCompat ACTION_SCROLL_FORWARD;
        public static final AccessibilityActionCompat ACTION_SCROLL_LEFT;
        public static final AccessibilityActionCompat ACTION_SCROLL_RIGHT;
        public static final AccessibilityActionCompat ACTION_SCROLL_TO_POSITION;
        public static final AccessibilityActionCompat ACTION_SCROLL_UP;
        public static final AccessibilityActionCompat ACTION_SELECT;
        public static final AccessibilityActionCompat ACTION_SET_PROGRESS;
        public static final AccessibilityActionCompat ACTION_SET_SELECTION;
        public static final AccessibilityActionCompat ACTION_SET_TEXT;
        public static final AccessibilityActionCompat ACTION_SHOW_ON_SCREEN;
        public static final AccessibilityActionCompat ACTION_SHOW_TOOLTIP;
        final Object mAction;
        
        static {
            final Object o = null;
            ACTION_FOCUS = new AccessibilityActionCompat(1, null);
            ACTION_CLEAR_FOCUS = new AccessibilityActionCompat(2, null);
            ACTION_SELECT = new AccessibilityActionCompat(4, null);
            ACTION_CLEAR_SELECTION = new AccessibilityActionCompat(8, null);
            ACTION_CLICK = new AccessibilityActionCompat(16, null);
            ACTION_LONG_CLICK = new AccessibilityActionCompat(32, null);
            ACTION_ACCESSIBILITY_FOCUS = new AccessibilityActionCompat(64, null);
            ACTION_CLEAR_ACCESSIBILITY_FOCUS = new AccessibilityActionCompat(128, null);
            ACTION_NEXT_AT_MOVEMENT_GRANULARITY = new AccessibilityActionCompat(256, null);
            ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY = new AccessibilityActionCompat(512, null);
            ACTION_NEXT_HTML_ELEMENT = new AccessibilityActionCompat(1024, null);
            ACTION_PREVIOUS_HTML_ELEMENT = new AccessibilityActionCompat(2048, null);
            ACTION_SCROLL_FORWARD = new AccessibilityActionCompat(4096, null);
            ACTION_SCROLL_BACKWARD = new AccessibilityActionCompat(8192, null);
            ACTION_COPY = new AccessibilityActionCompat(16384, null);
            ACTION_PASTE = new AccessibilityActionCompat(32768, null);
            ACTION_CUT = new AccessibilityActionCompat(65536, null);
            ACTION_SET_SELECTION = new AccessibilityActionCompat(131072, null);
            ACTION_EXPAND = new AccessibilityActionCompat(262144, null);
            ACTION_COLLAPSE = new AccessibilityActionCompat(524288, null);
            ACTION_DISMISS = new AccessibilityActionCompat(1048576, null);
            ACTION_SET_TEXT = new AccessibilityActionCompat(2097152, null);
            AccessibilityNodeInfo$AccessibilityAction action_SHOW_ON_SCREEN;
            if (Build$VERSION.SDK_INT >= 23) {
                action_SHOW_ON_SCREEN = AccessibilityNodeInfo$AccessibilityAction.ACTION_SHOW_ON_SCREEN;
            }
            else {
                action_SHOW_ON_SCREEN = null;
            }
            ACTION_SHOW_ON_SCREEN = new AccessibilityActionCompat(action_SHOW_ON_SCREEN);
            AccessibilityNodeInfo$AccessibilityAction action_SCROLL_TO_POSITION;
            if (Build$VERSION.SDK_INT >= 23) {
                action_SCROLL_TO_POSITION = AccessibilityNodeInfo$AccessibilityAction.ACTION_SCROLL_TO_POSITION;
            }
            else {
                action_SCROLL_TO_POSITION = null;
            }
            ACTION_SCROLL_TO_POSITION = new AccessibilityActionCompat(action_SCROLL_TO_POSITION);
            AccessibilityNodeInfo$AccessibilityAction action_SCROLL_UP;
            if (Build$VERSION.SDK_INT >= 23) {
                action_SCROLL_UP = AccessibilityNodeInfo$AccessibilityAction.ACTION_SCROLL_UP;
            }
            else {
                action_SCROLL_UP = null;
            }
            ACTION_SCROLL_UP = new AccessibilityActionCompat(action_SCROLL_UP);
            AccessibilityNodeInfo$AccessibilityAction action_SCROLL_LEFT;
            if (Build$VERSION.SDK_INT >= 23) {
                action_SCROLL_LEFT = AccessibilityNodeInfo$AccessibilityAction.ACTION_SCROLL_LEFT;
            }
            else {
                action_SCROLL_LEFT = null;
            }
            ACTION_SCROLL_LEFT = new AccessibilityActionCompat(action_SCROLL_LEFT);
            AccessibilityNodeInfo$AccessibilityAction action_SCROLL_DOWN;
            if (Build$VERSION.SDK_INT >= 23) {
                action_SCROLL_DOWN = AccessibilityNodeInfo$AccessibilityAction.ACTION_SCROLL_DOWN;
            }
            else {
                action_SCROLL_DOWN = null;
            }
            ACTION_SCROLL_DOWN = new AccessibilityActionCompat(action_SCROLL_DOWN);
            AccessibilityNodeInfo$AccessibilityAction action_SCROLL_RIGHT;
            if (Build$VERSION.SDK_INT >= 23) {
                action_SCROLL_RIGHT = AccessibilityNodeInfo$AccessibilityAction.ACTION_SCROLL_RIGHT;
            }
            else {
                action_SCROLL_RIGHT = null;
            }
            ACTION_SCROLL_RIGHT = new AccessibilityActionCompat(action_SCROLL_RIGHT);
            AccessibilityNodeInfo$AccessibilityAction action_CONTEXT_CLICK;
            if (Build$VERSION.SDK_INT >= 23) {
                action_CONTEXT_CLICK = AccessibilityNodeInfo$AccessibilityAction.ACTION_CONTEXT_CLICK;
            }
            else {
                action_CONTEXT_CLICK = null;
            }
            ACTION_CONTEXT_CLICK = new AccessibilityActionCompat(action_CONTEXT_CLICK);
            AccessibilityNodeInfo$AccessibilityAction action_SET_PROGRESS;
            if (Build$VERSION.SDK_INT >= 24) {
                action_SET_PROGRESS = AccessibilityNodeInfo$AccessibilityAction.ACTION_SET_PROGRESS;
            }
            else {
                action_SET_PROGRESS = null;
            }
            ACTION_SET_PROGRESS = new AccessibilityActionCompat(action_SET_PROGRESS);
            AccessibilityNodeInfo$AccessibilityAction action_MOVE_WINDOW;
            if (Build$VERSION.SDK_INT >= 26) {
                action_MOVE_WINDOW = AccessibilityNodeInfo$AccessibilityAction.ACTION_MOVE_WINDOW;
            }
            else {
                action_MOVE_WINDOW = null;
            }
            ACTION_MOVE_WINDOW = new AccessibilityActionCompat(action_MOVE_WINDOW);
            AccessibilityNodeInfo$AccessibilityAction action_SHOW_TOOLTIP;
            if (Build$VERSION.SDK_INT >= 28) {
                action_SHOW_TOOLTIP = AccessibilityNodeInfo$AccessibilityAction.ACTION_SHOW_TOOLTIP;
            }
            else {
                action_SHOW_TOOLTIP = null;
            }
            ACTION_SHOW_TOOLTIP = new AccessibilityActionCompat(action_SHOW_TOOLTIP);
            Object action_HIDE_TOOLTIP = o;
            if (Build$VERSION.SDK_INT >= 28) {
                action_HIDE_TOOLTIP = AccessibilityNodeInfo$AccessibilityAction.ACTION_HIDE_TOOLTIP;
            }
            ACTION_HIDE_TOOLTIP = new AccessibilityActionCompat(action_HIDE_TOOLTIP);
        }
        
        public AccessibilityActionCompat(final int n, final CharSequence charSequence) {
            AccessibilityNodeInfo$AccessibilityAction accessibilityNodeInfo$AccessibilityAction;
            if (Build$VERSION.SDK_INT >= 21) {
                accessibilityNodeInfo$AccessibilityAction = new AccessibilityNodeInfo$AccessibilityAction(n, charSequence);
            }
            else {
                accessibilityNodeInfo$AccessibilityAction = null;
            }
            this(accessibilityNodeInfo$AccessibilityAction);
        }
        
        AccessibilityActionCompat(final Object mAction) {
            this.mAction = mAction;
        }
        
        public int getId() {
            if (Build$VERSION.SDK_INT >= 21) {
                return ((AccessibilityNodeInfo$AccessibilityAction)this.mAction).getId();
            }
            return 0;
        }
        
        public CharSequence getLabel() {
            if (Build$VERSION.SDK_INT >= 21) {
                return ((AccessibilityNodeInfo$AccessibilityAction)this.mAction).getLabel();
            }
            return null;
        }
    }
    
    public static class CollectionInfoCompat
    {
        public static final int SELECTION_MODE_MULTIPLE = 2;
        public static final int SELECTION_MODE_NONE = 0;
        public static final int SELECTION_MODE_SINGLE = 1;
        final Object mInfo;
        
        CollectionInfoCompat(final Object mInfo) {
            this.mInfo = mInfo;
        }
        
        public static CollectionInfoCompat obtain(final int n, final int n2, final boolean b) {
            if (Build$VERSION.SDK_INT >= 19) {
                return new CollectionInfoCompat(AccessibilityNodeInfo$CollectionInfo.obtain(n, n2, b));
            }
            return new CollectionInfoCompat(null);
        }
        
        public static CollectionInfoCompat obtain(final int n, final int n2, final boolean b, final int n3) {
            if (Build$VERSION.SDK_INT >= 21) {
                return new CollectionInfoCompat(AccessibilityNodeInfo$CollectionInfo.obtain(n, n2, b, n3));
            }
            if (Build$VERSION.SDK_INT >= 19) {
                return new CollectionInfoCompat(AccessibilityNodeInfo$CollectionInfo.obtain(n, n2, b));
            }
            return new CollectionInfoCompat(null);
        }
        
        public int getColumnCount() {
            if (Build$VERSION.SDK_INT >= 19) {
                return ((AccessibilityNodeInfo$CollectionInfo)this.mInfo).getColumnCount();
            }
            return 0;
        }
        
        public int getRowCount() {
            if (Build$VERSION.SDK_INT >= 19) {
                return ((AccessibilityNodeInfo$CollectionInfo)this.mInfo).getRowCount();
            }
            return 0;
        }
        
        public int getSelectionMode() {
            if (Build$VERSION.SDK_INT >= 21) {
                return ((AccessibilityNodeInfo$CollectionInfo)this.mInfo).getSelectionMode();
            }
            return 0;
        }
        
        public boolean isHierarchical() {
            return Build$VERSION.SDK_INT >= 19 && ((AccessibilityNodeInfo$CollectionInfo)this.mInfo).isHierarchical();
        }
    }
    
    public static class CollectionItemInfoCompat
    {
        final Object mInfo;
        
        CollectionItemInfoCompat(final Object mInfo) {
            this.mInfo = mInfo;
        }
        
        public static CollectionItemInfoCompat obtain(final int n, final int n2, final int n3, final int n4, final boolean b) {
            if (Build$VERSION.SDK_INT >= 19) {
                return new CollectionItemInfoCompat(AccessibilityNodeInfo$CollectionItemInfo.obtain(n, n2, n3, n4, b));
            }
            return new CollectionItemInfoCompat(null);
        }
        
        public static CollectionItemInfoCompat obtain(final int n, final int n2, final int n3, final int n4, final boolean b, final boolean b2) {
            if (Build$VERSION.SDK_INT >= 21) {
                return new CollectionItemInfoCompat(AccessibilityNodeInfo$CollectionItemInfo.obtain(n, n2, n3, n4, b, b2));
            }
            if (Build$VERSION.SDK_INT >= 19) {
                return new CollectionItemInfoCompat(AccessibilityNodeInfo$CollectionItemInfo.obtain(n, n2, n3, n4, b));
            }
            return new CollectionItemInfoCompat(null);
        }
        
        public int getColumnIndex() {
            if (Build$VERSION.SDK_INT >= 19) {
                return ((AccessibilityNodeInfo$CollectionItemInfo)this.mInfo).getColumnIndex();
            }
            return 0;
        }
        
        public int getColumnSpan() {
            if (Build$VERSION.SDK_INT >= 19) {
                return ((AccessibilityNodeInfo$CollectionItemInfo)this.mInfo).getColumnSpan();
            }
            return 0;
        }
        
        public int getRowIndex() {
            if (Build$VERSION.SDK_INT >= 19) {
                return ((AccessibilityNodeInfo$CollectionItemInfo)this.mInfo).getRowIndex();
            }
            return 0;
        }
        
        public int getRowSpan() {
            if (Build$VERSION.SDK_INT >= 19) {
                return ((AccessibilityNodeInfo$CollectionItemInfo)this.mInfo).getRowSpan();
            }
            return 0;
        }
        
        public boolean isHeading() {
            return Build$VERSION.SDK_INT >= 19 && ((AccessibilityNodeInfo$CollectionItemInfo)this.mInfo).isHeading();
        }
        
        public boolean isSelected() {
            return Build$VERSION.SDK_INT >= 21 && ((AccessibilityNodeInfo$CollectionItemInfo)this.mInfo).isSelected();
        }
    }
    
    public static class RangeInfoCompat
    {
        public static final int RANGE_TYPE_FLOAT = 1;
        public static final int RANGE_TYPE_INT = 0;
        public static final int RANGE_TYPE_PERCENT = 2;
        final Object mInfo;
        
        RangeInfoCompat(final Object mInfo) {
            this.mInfo = mInfo;
        }
        
        public static RangeInfoCompat obtain(final int n, final float n2, final float n3, final float n4) {
            if (Build$VERSION.SDK_INT >= 19) {
                return new RangeInfoCompat(AccessibilityNodeInfo$RangeInfo.obtain(n, n2, n3, n4));
            }
            return new RangeInfoCompat(null);
        }
        
        public float getCurrent() {
            if (Build$VERSION.SDK_INT >= 19) {
                return ((AccessibilityNodeInfo$RangeInfo)this.mInfo).getCurrent();
            }
            return 0.0f;
        }
        
        public float getMax() {
            if (Build$VERSION.SDK_INT >= 19) {
                return ((AccessibilityNodeInfo$RangeInfo)this.mInfo).getMax();
            }
            return 0.0f;
        }
        
        public float getMin() {
            if (Build$VERSION.SDK_INT >= 19) {
                return ((AccessibilityNodeInfo$RangeInfo)this.mInfo).getMin();
            }
            return 0.0f;
        }
        
        public int getType() {
            if (Build$VERSION.SDK_INT >= 19) {
                return ((AccessibilityNodeInfo$RangeInfo)this.mInfo).getType();
            }
            return 0;
        }
    }
}
