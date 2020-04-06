// 
// Decompiled by Procyon v0.5.36
// 

package androidx.versionedparcelable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.FIELD })
public @interface ParcelField {
    int value();
}
