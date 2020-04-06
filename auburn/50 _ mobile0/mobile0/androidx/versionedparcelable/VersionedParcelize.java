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
@Target({ ElementType.TYPE })
public @interface VersionedParcelize {
    boolean allowSerialization() default false;
    
    int[] deprecatedIds() default {};
    
    boolean ignoreParcelables() default false;
    
    boolean isCustom() default false;
    
    String jetifyAs() default "";
}
