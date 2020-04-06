// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.view.accessibility;

import android.os.Build$VERSION;
import android.view.accessibility.AccessibilityRecord;
import android.view.accessibility.AccessibilityEvent;

public final class AccessibilityEventCompat
{
    public static final int CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION = 4;
    public static final int CONTENT_CHANGE_TYPE_SUBTREE = 1;
    public static final int CONTENT_CHANGE_TYPE_TEXT = 2;
    public static final int CONTENT_CHANGE_TYPE_UNDEFINED = 0;
    public static final int TYPES_ALL_MASK = -1;
    public static final int TYPE_ANNOUNCEMENT = 16384;
    public static final int TYPE_ASSIST_READING_CONTEXT = 16777216;
    public static final int TYPE_GESTURE_DETECTION_END = 524288;
    public static final int TYPE_GESTURE_DETECTION_START = 262144;
    @Deprecated
    public static final int TYPE_TOUCH_EXPLORATION_GESTURE_END = 1024;
    @Deprecated
    public static final int TYPE_TOUCH_EXPLORATION_GESTURE_START = 512;
    public static final int TYPE_TOUCH_INTERACTION_END = 2097152;
    public static final int TYPE_TOUCH_INTERACTION_START = 1048576;
    public static final int TYPE_VIEW_ACCESSIBILITY_FOCUSED = 32768;
    public static final int TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED = 65536;
    public static final int TYPE_VIEW_CONTEXT_CLICKED = 8388608;
    @Deprecated
    public static final int TYPE_VIEW_HOVER_ENTER = 128;
    @Deprecated
    public static final int TYPE_VIEW_HOVER_EXIT = 256;
    @Deprecated
    public static final int TYPE_VIEW_SCROLLED = 4096;
    @Deprecated
    public static final int TYPE_VIEW_TEXT_SELECTION_CHANGED = 8192;
    public static final int TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY = 131072;
    public static final int TYPE_WINDOWS_CHANGED = 4194304;
    @Deprecated
    public static final int TYPE_WINDOW_CONTENT_CHANGED = 2048;
    
    private AccessibilityEventCompat() {
    }
    
    @Deprecated
    public static void appendRecord(final AccessibilityEvent accessibilityEvent, final AccessibilityRecordCompat accessibilityRecordCompat) {
        accessibilityEvent.appendRecord((AccessibilityRecord)accessibilityRecordCompat.getImpl());
    }
    
    @Deprecated
    public static AccessibilityRecordCompat asRecord(final AccessibilityEvent accessibilityEvent) {
        return new AccessibilityRecordCompat(accessibilityEvent);
    }
    
    public static int getAction(final AccessibilityEvent accessibilityEvent) {
        if (Build$VERSION.SDK_INT >= 16) {
            return accessibilityEvent.getAction();
        }
        return 0;
    }
    
    public static int getContentChangeTypes(final AccessibilityEvent accessibilityEvent) {
        if (Build$VERSION.SDK_INT >= 19) {
            return accessibilityEvent.getContentChangeTypes();
        }
        return 0;
    }
    
    public static int getMovementGranularity(final AccessibilityEvent accessibilityEvent) {
        if (Build$VERSION.SDK_INT >= 16) {
            return accessibilityEvent.getMovementGranularity();
        }
        return 0;
    }
    
    @Deprecated
    public static AccessibilityRecordCompat getRecord(final AccessibilityEvent accessibilityEvent, final int n) {
        return new AccessibilityRecordCompat(accessibilityEvent.getRecord(n));
    }
    
    @Deprecated
    public static int getRecordCount(final AccessibilityEvent accessibilityEvent) {
        return accessibilityEvent.getRecordCount();
    }
    
    public static void setAction(final AccessibilityEvent accessibilityEvent, final int action) {
        if (Build$VERSION.SDK_INT >= 16) {
            accessibilityEvent.setAction(action);
        }
    }
    
    public static void setContentChangeTypes(final AccessibilityEvent accessibilityEvent, final int contentChangeTypes) {
        if (Build$VERSION.SDK_INT >= 19) {
            accessibilityEvent.setContentChangeTypes(contentChangeTypes);
        }
    }
    
    public static void setMovementGranularity(final AccessibilityEvent accessibilityEvent, final int movementGranularity) {
        if (Build$VERSION.SDK_INT >= 16) {
            accessibilityEvent.setMovementGranularity(movementGranularity);
        }
    }
}
