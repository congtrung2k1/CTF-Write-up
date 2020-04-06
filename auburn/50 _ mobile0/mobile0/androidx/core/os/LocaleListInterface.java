// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.os;

import java.util.Locale;

interface LocaleListInterface
{
    boolean equals(final Object p0);
    
    Locale get(final int p0);
    
    Locale getFirstMatch(final String[] p0);
    
    Object getLocaleList();
    
    int hashCode();
    
    int indexOf(final Locale p0);
    
    boolean isEmpty();
    
    void setLocaleList(final Locale... p0);
    
    int size();
    
    String toLanguageTags();
    
    String toString();
}
