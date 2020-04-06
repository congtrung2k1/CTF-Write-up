// 
// Decompiled by Procyon v0.5.36
// 

package androidx.lifecycle;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;

public class Lifecycling
{
    private static final int GENERATED_CALLBACK = 2;
    private static final int REFLECTIVE_CALLBACK = 1;
    private static Map<Class, Integer> sCallbackCache;
    private static Map<Class, List<Constructor<? extends GeneratedAdapter>>> sClassToAdapters;
    
    static {
        Lifecycling.sCallbackCache = new HashMap<Class, Integer>();
        Lifecycling.sClassToAdapters = new HashMap<Class, List<Constructor<? extends GeneratedAdapter>>>();
    }
    
    private Lifecycling() {
    }
    
    private static GeneratedAdapter createGeneratedAdapter(final Constructor<? extends GeneratedAdapter> constructor, final Object o) {
        try {
            return (GeneratedAdapter)constructor.newInstance(o);
        }
        catch (InvocationTargetException cause) {
            throw new RuntimeException(cause);
        }
        catch (InstantiationException cause2) {
            throw new RuntimeException(cause2);
        }
        catch (IllegalAccessException cause3) {
            throw new RuntimeException(cause3);
        }
    }
    
    private static Constructor<? extends GeneratedAdapter> generatedConstructor(final Class<?> clazz) {
        try {
            final Package package1 = clazz.getPackage();
            String s = clazz.getCanonicalName();
            String name;
            if (package1 != null) {
                name = package1.getName();
            }
            else {
                name = "";
            }
            if (!name.isEmpty()) {
                s = s.substring(name.length() + 1);
            }
            final String adapterName = getAdapterName(s);
            String string;
            if (name.isEmpty()) {
                string = adapterName;
            }
            else {
                final StringBuilder sb = new StringBuilder();
                sb.append(name);
                sb.append(".");
                sb.append(adapterName);
                string = sb.toString();
            }
            final Constructor<?> declaredConstructor = Class.forName(string).getDeclaredConstructor(clazz);
            if (!declaredConstructor.isAccessible()) {
                declaredConstructor.setAccessible(true);
            }
            return (Constructor<? extends GeneratedAdapter>)declaredConstructor;
        }
        catch (NoSuchMethodException cause) {
            throw new RuntimeException(cause);
        }
        catch (ClassNotFoundException ex) {
            return null;
        }
    }
    
    public static String getAdapterName(final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s.replace(".", "_"));
        sb.append("_LifecycleAdapter");
        return sb.toString();
    }
    
    static GenericLifecycleObserver getCallback(final Object o) {
        if (o instanceof FullLifecycleObserver) {
            return new FullLifecycleObserverAdapter((FullLifecycleObserver)o);
        }
        if (o instanceof GenericLifecycleObserver) {
            return (GenericLifecycleObserver)o;
        }
        final Class<?> class1 = o.getClass();
        if (getObserverConstructorType(class1) != 2) {
            return new ReflectiveGenericLifecycleObserver(o);
        }
        final List<Constructor<? extends GeneratedAdapter>> list = Lifecycling.sClassToAdapters.get(class1);
        if (list.size() == 1) {
            return new SingleGeneratedAdapterObserver(createGeneratedAdapter(list.get(0), o));
        }
        final GeneratedAdapter[] array = new GeneratedAdapter[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            array[i] = createGeneratedAdapter(list.get(i), o);
        }
        return new CompositeGeneratedAdaptersObserver(array);
    }
    
    private static int getObserverConstructorType(final Class<?> clazz) {
        if (Lifecycling.sCallbackCache.containsKey(clazz)) {
            return Lifecycling.sCallbackCache.get(clazz);
        }
        final int resolveObserverCallbackType = resolveObserverCallbackType(clazz);
        Lifecycling.sCallbackCache.put(clazz, resolveObserverCallbackType);
        return resolveObserverCallbackType;
    }
    
    private static boolean isLifecycleParent(final Class<?> clazz) {
        return clazz != null && LifecycleObserver.class.isAssignableFrom(clazz);
    }
    
    private static int resolveObserverCallbackType(final Class<?> clazz) {
        if (clazz.getCanonicalName() == null) {
            return 1;
        }
        final Constructor<? extends GeneratedAdapter> generatedConstructor = generatedConstructor(clazz);
        if (generatedConstructor != null) {
            Lifecycling.sClassToAdapters.put(clazz, Collections.singletonList(generatedConstructor));
            return 2;
        }
        if (ClassesInfoCache.sInstance.hasLifecycleMethods(clazz)) {
            return 1;
        }
        final Class<?> superclass = clazz.getSuperclass();
        List<Constructor<? extends GeneratedAdapter>> list = null;
        if (isLifecycleParent(superclass)) {
            if (getObserverConstructorType(superclass) == 1) {
                return 1;
            }
            list = new ArrayList<Constructor<? extends GeneratedAdapter>>(Lifecycling.sClassToAdapters.get(superclass));
        }
        for (final Class<?> clazz2 : clazz.getInterfaces()) {
            if (isLifecycleParent(clazz2)) {
                if (getObserverConstructorType(clazz2) == 1) {
                    return 1;
                }
                List<Constructor<? extends GeneratedAdapter>> list2;
                if ((list2 = list) == null) {
                    list2 = new ArrayList<Constructor<? extends GeneratedAdapter>>();
                }
                list2.addAll(Lifecycling.sClassToAdapters.get(clazz2));
                list = list2;
            }
        }
        if (list != null) {
            Lifecycling.sClassToAdapters.put(clazz, list);
            return 2;
        }
        return 1;
    }
}
