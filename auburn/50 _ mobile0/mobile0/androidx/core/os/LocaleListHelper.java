// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.os;

import java.util.Arrays;
import android.os.Build$VERSION;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

final class LocaleListHelper
{
    private static final Locale EN_LATN;
    private static final Locale LOCALE_AR_XB;
    private static final Locale LOCALE_EN_XA;
    private static final int NUM_PSEUDO_LOCALES = 2;
    private static final String STRING_AR_XB = "ar-XB";
    private static final String STRING_EN_XA = "en-XA";
    private static LocaleListHelper sDefaultAdjustedLocaleList;
    private static LocaleListHelper sDefaultLocaleList;
    private static final Locale[] sEmptyList;
    private static final LocaleListHelper sEmptyLocaleList;
    private static Locale sLastDefaultLocale;
    private static LocaleListHelper sLastExplicitlySetLocaleList;
    private static final Object sLock;
    private final Locale[] mList;
    private final String mStringRepresentation;
    
    static {
        sEmptyList = new Locale[0];
        sEmptyLocaleList = new LocaleListHelper(new Locale[0]);
        LOCALE_EN_XA = new Locale("en", "XA");
        LOCALE_AR_XB = new Locale("ar", "XB");
        EN_LATN = LocaleHelper.forLanguageTag("en-Latn");
        sLock = new Object();
        LocaleListHelper.sLastExplicitlySetLocaleList = null;
        LocaleListHelper.sDefaultLocaleList = null;
        LocaleListHelper.sDefaultAdjustedLocaleList = null;
        LocaleListHelper.sLastDefaultLocale = null;
    }
    
    LocaleListHelper(final Locale locale, final LocaleListHelper localeListHelper) {
        if (locale != null) {
            int length;
            if (localeListHelper == null) {
                length = 0;
            }
            else {
                length = localeListHelper.mList.length;
            }
            final int n = -1;
            int n2 = 0;
            int n3;
            while (true) {
                n3 = n;
                if (n2 >= length) {
                    break;
                }
                if (locale.equals(localeListHelper.mList[n2])) {
                    n3 = n2;
                    break;
                }
                ++n2;
            }
            int n4;
            if (n3 == -1) {
                n4 = 1;
            }
            else {
                n4 = 0;
            }
            final int n5 = n4 + length;
            final Locale[] mList = new Locale[n5];
            mList[0] = (Locale)locale.clone();
            if (n3 == -1) {
                for (int i = 0; i < length; ++i) {
                    mList[i + 1] = (Locale)localeListHelper.mList[i].clone();
                }
            }
            else {
                for (int j = 0; j < n3; ++j) {
                    mList[j + 1] = (Locale)localeListHelper.mList[j].clone();
                }
                for (int k = n3 + 1; k < length; ++k) {
                    mList[k] = (Locale)localeListHelper.mList[k].clone();
                }
            }
            final StringBuilder sb = new StringBuilder();
            for (int l = 0; l < n5; ++l) {
                sb.append(LocaleHelper.toLanguageTag(mList[l]));
                if (l < n5 - 1) {
                    sb.append(',');
                }
            }
            this.mList = mList;
            this.mStringRepresentation = sb.toString();
            return;
        }
        throw new NullPointerException("topLocale is null");
    }
    
    LocaleListHelper(final Locale... array) {
        if (array.length == 0) {
            this.mList = LocaleListHelper.sEmptyList;
            this.mStringRepresentation = "";
        }
        else {
            final Locale[] mList = new Locale[array.length];
            final HashSet<Locale> set = new HashSet<Locale>();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.length; ++i) {
                final Locale o = array[i];
                if (o == null) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("list[");
                    sb2.append(i);
                    sb2.append("] is null");
                    throw new NullPointerException(sb2.toString());
                }
                if (set.contains(o)) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("list[");
                    sb3.append(i);
                    sb3.append("] is a repetition");
                    throw new IllegalArgumentException(sb3.toString());
                }
                final Locale e = (Locale)o.clone();
                mList[i] = e;
                sb.append(LocaleHelper.toLanguageTag(e));
                if (i < array.length - 1) {
                    sb.append(',');
                }
                set.add(e);
            }
            this.mList = mList;
            this.mStringRepresentation = sb.toString();
        }
    }
    
    private Locale computeFirstMatch(final Collection<String> collection, final boolean b) {
        final int computeFirstMatchIndex = this.computeFirstMatchIndex(collection, b);
        Locale locale;
        if (computeFirstMatchIndex == -1) {
            locale = null;
        }
        else {
            locale = this.mList[computeFirstMatchIndex];
        }
        return locale;
    }
    
    private int computeFirstMatchIndex(final Collection<String> collection, final boolean b) {
        final Locale[] mList = this.mList;
        if (mList.length == 1) {
            return 0;
        }
        if (mList.length == 0) {
            return -1;
        }
        int n2;
        final int n = n2 = Integer.MAX_VALUE;
        if (b) {
            final int firstMatchIndex = this.findFirstMatchIndex(LocaleListHelper.EN_LATN);
            if (firstMatchIndex == 0) {
                return 0;
            }
            n2 = n;
            if (firstMatchIndex < Integer.MAX_VALUE) {
                n2 = firstMatchIndex;
            }
        }
        final Iterator<String> iterator = collection.iterator();
        while (iterator.hasNext()) {
            final int firstMatchIndex2 = this.findFirstMatchIndex(LocaleHelper.forLanguageTag(iterator.next()));
            if (firstMatchIndex2 == 0) {
                return 0;
            }
            int n3;
            if (firstMatchIndex2 < (n3 = n2)) {
                n3 = firstMatchIndex2;
            }
            n2 = n3;
        }
        if (n2 == Integer.MAX_VALUE) {
            return 0;
        }
        return n2;
    }
    
    private int findFirstMatchIndex(final Locale locale) {
        int n = 0;
        while (true) {
            final Locale[] mList = this.mList;
            if (n >= mList.length) {
                return Integer.MAX_VALUE;
            }
            if (matchScore(locale, mList[n]) > 0) {
                return n;
            }
            ++n;
        }
    }
    
    static LocaleListHelper forLanguageTags(final String s) {
        if (s != null && !s.isEmpty()) {
            final String[] split = s.split(",", -1);
            final Locale[] array = new Locale[split.length];
            for (int i = 0; i < array.length; ++i) {
                array[i] = LocaleHelper.forLanguageTag(split[i]);
            }
            return new LocaleListHelper(array);
        }
        return getEmptyLocaleList();
    }
    
    static LocaleListHelper getAdjustedDefault() {
        getDefault();
        synchronized (LocaleListHelper.sLock) {
            return LocaleListHelper.sDefaultAdjustedLocaleList;
        }
    }
    
    static LocaleListHelper getDefault() {
        final Locale default1 = Locale.getDefault();
        synchronized (LocaleListHelper.sLock) {
            if (!default1.equals(LocaleListHelper.sLastDefaultLocale)) {
                LocaleListHelper.sLastDefaultLocale = default1;
                if (LocaleListHelper.sDefaultLocaleList != null && default1.equals(LocaleListHelper.sDefaultLocaleList.get(0))) {
                    return LocaleListHelper.sDefaultLocaleList;
                }
                LocaleListHelper.sDefaultAdjustedLocaleList = (LocaleListHelper.sDefaultLocaleList = new LocaleListHelper(default1, LocaleListHelper.sLastExplicitlySetLocaleList));
            }
            return LocaleListHelper.sDefaultLocaleList;
        }
    }
    
    static LocaleListHelper getEmptyLocaleList() {
        return LocaleListHelper.sEmptyLocaleList;
    }
    
    private static String getLikelyScript(final Locale locale) {
        if (Build$VERSION.SDK_INT < 21) {
            return "";
        }
        final String script = locale.getScript();
        if (!script.isEmpty()) {
            return script;
        }
        return "";
    }
    
    private static boolean isPseudoLocale(final String s) {
        return "en-XA".equals(s) || "ar-XB".equals(s);
    }
    
    private static boolean isPseudoLocale(final Locale locale) {
        return LocaleListHelper.LOCALE_EN_XA.equals(locale) || LocaleListHelper.LOCALE_AR_XB.equals(locale);
    }
    
    static boolean isPseudoLocalesOnly(final String[] array) {
        if (array == null) {
            return true;
        }
        if (array.length > 3) {
            return false;
        }
        for (final String s : array) {
            if (!s.isEmpty() && !isPseudoLocale(s)) {
                return false;
            }
        }
        return true;
    }
    
    private static int matchScore(final Locale locale, final Locale obj) {
        final boolean equals = locale.equals(obj);
        final boolean b = true;
        if (equals) {
            return 1;
        }
        if (!locale.getLanguage().equals(obj.getLanguage())) {
            return 0;
        }
        if (isPseudoLocale(locale) || isPseudoLocale(obj)) {
            return 0;
        }
        final String likelyScript = getLikelyScript(locale);
        if (likelyScript.isEmpty()) {
            final String country = locale.getCountry();
            int n = b ? 1 : 0;
            if (!country.isEmpty()) {
                if (country.equals(obj.getCountry())) {
                    n = (b ? 1 : 0);
                }
                else {
                    n = 0;
                }
            }
            return n;
        }
        return likelyScript.equals(getLikelyScript(obj)) ? 1 : 0;
    }
    
    static void setDefault(final LocaleListHelper localeListHelper) {
        setDefault(localeListHelper, 0);
    }
    
    static void setDefault(final LocaleListHelper sDefaultAdjustedLocaleList, final int n) {
        if (sDefaultAdjustedLocaleList != null) {
            if (!sDefaultAdjustedLocaleList.isEmpty()) {
                synchronized (LocaleListHelper.sLock) {
                    Locale.setDefault(LocaleListHelper.sLastDefaultLocale = sDefaultAdjustedLocaleList.get(n));
                    LocaleListHelper.sLastExplicitlySetLocaleList = sDefaultAdjustedLocaleList;
                    LocaleListHelper.sDefaultLocaleList = sDefaultAdjustedLocaleList;
                    if (n == 0) {
                        LocaleListHelper.sDefaultAdjustedLocaleList = sDefaultAdjustedLocaleList;
                    }
                    else {
                        LocaleListHelper.sDefaultAdjustedLocaleList = new LocaleListHelper(LocaleListHelper.sLastDefaultLocale, LocaleListHelper.sDefaultLocaleList);
                    }
                    return;
                }
            }
            throw new IllegalArgumentException("locales is empty");
        }
        throw new NullPointerException("locales is null");
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LocaleListHelper)) {
            return false;
        }
        final Locale[] mList = ((LocaleListHelper)o).mList;
        if (this.mList.length != mList.length) {
            return false;
        }
        int n = 0;
        while (true) {
            final Locale[] mList2 = this.mList;
            if (n >= mList2.length) {
                return true;
            }
            if (!mList2[n].equals(mList[n])) {
                return false;
            }
            ++n;
        }
    }
    
    Locale get(final int n) {
        if (n >= 0) {
            final Locale[] mList = this.mList;
            if (n < mList.length) {
                return mList[n];
            }
        }
        return null;
    }
    
    Locale getFirstMatch(final String[] a) {
        return this.computeFirstMatch(Arrays.asList(a), false);
    }
    
    int getFirstMatchIndex(final String[] a) {
        return this.computeFirstMatchIndex(Arrays.asList(a), false);
    }
    
    int getFirstMatchIndexWithEnglishSupported(final Collection<String> collection) {
        return this.computeFirstMatchIndex(collection, true);
    }
    
    int getFirstMatchIndexWithEnglishSupported(final String[] a) {
        return this.getFirstMatchIndexWithEnglishSupported(Arrays.asList(a));
    }
    
    Locale getFirstMatchWithEnglishSupported(final String[] a) {
        return this.computeFirstMatch(Arrays.asList(a), true);
    }
    
    @Override
    public int hashCode() {
        int n = 1;
        int n2 = 0;
        while (true) {
            final Locale[] mList = this.mList;
            if (n2 >= mList.length) {
                break;
            }
            n = n * 31 + mList[n2].hashCode();
            ++n2;
        }
        return n;
    }
    
    int indexOf(final Locale obj) {
        int n = 0;
        while (true) {
            final Locale[] mList = this.mList;
            if (n >= mList.length) {
                return -1;
            }
            if (mList[n].equals(obj)) {
                return n;
            }
            ++n;
        }
    }
    
    boolean isEmpty() {
        return this.mList.length == 0;
    }
    
    int size() {
        return this.mList.length;
    }
    
    String toLanguageTags() {
        return this.mStringRepresentation;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        int n = 0;
        while (true) {
            final Locale[] mList = this.mList;
            if (n >= mList.length) {
                break;
            }
            sb.append(mList[n]);
            if (n < this.mList.length - 1) {
                sb.append(',');
            }
            ++n;
        }
        sb.append("]");
        return sb.toString();
    }
}
