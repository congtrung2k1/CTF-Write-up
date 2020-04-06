// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.app;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.app.Application;
import java.lang.reflect.InvocationTargetException;
import android.app.Activity;
import android.content.Intent;

public class AppComponentFactory extends android.app.AppComponentFactory
{
    public final Activity instantiateActivity(final ClassLoader classLoader, final String s, final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateActivityCompat(classLoader, s, intent));
    }
    
    public Activity instantiateActivityCompat(ClassLoader cause, final String name, final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            cause = (NoSuchMethodException)((ClassLoader)cause).loadClass(name).getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
            return (Activity)cause;
        }
        catch (NoSuchMethodException cause) {}
        catch (InvocationTargetException ex) {}
        throw new RuntimeException("Couldn't call constructor", cause);
    }
    
    public final Application instantiateApplication(final ClassLoader classLoader, final String s) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateApplicationCompat(classLoader, s));
    }
    
    public Application instantiateApplicationCompat(ClassLoader cause, final String name) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            cause = (NoSuchMethodException)((ClassLoader)cause).loadClass(name).getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
            return (Application)cause;
        }
        catch (NoSuchMethodException cause) {}
        catch (InvocationTargetException ex) {}
        throw new RuntimeException("Couldn't call constructor", cause);
    }
    
    public final ContentProvider instantiateProvider(final ClassLoader classLoader, final String s) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateProviderCompat(classLoader, s));
    }
    
    public ContentProvider instantiateProviderCompat(ClassLoader cause, final String name) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            cause = (NoSuchMethodException)((ClassLoader)cause).loadClass(name).getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
            return (ContentProvider)cause;
        }
        catch (NoSuchMethodException cause) {}
        catch (InvocationTargetException ex) {}
        throw new RuntimeException("Couldn't call constructor", cause);
    }
    
    public final BroadcastReceiver instantiateReceiver(final ClassLoader classLoader, final String s, final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateReceiverCompat(classLoader, s, intent));
    }
    
    public BroadcastReceiver instantiateReceiverCompat(ClassLoader cause, final String name, final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            cause = (NoSuchMethodException)((ClassLoader)cause).loadClass(name).getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
            return (BroadcastReceiver)cause;
        }
        catch (NoSuchMethodException cause) {}
        catch (InvocationTargetException ex) {}
        throw new RuntimeException("Couldn't call constructor", cause);
    }
    
    public final Service instantiateService(final ClassLoader classLoader, final String s, final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateServiceCompat(classLoader, s, intent));
    }
    
    public Service instantiateServiceCompat(ClassLoader cause, final String name, final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            cause = (NoSuchMethodException)((ClassLoader)cause).loadClass(name).getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
            return (Service)cause;
        }
        catch (NoSuchMethodException cause) {}
        catch (InvocationTargetException ex) {}
        throw new RuntimeException("Couldn't call constructor", cause);
    }
}
