// 
// Decompiled by Procyon v0.5.36
// 

package androidx.lifecycle;

import java.lang.reflect.InvocationTargetException;
import android.app.Application;

public class ViewModelProvider
{
    private static final String DEFAULT_KEY = "androidx.lifecycle.ViewModelProvider.DefaultKey";
    private final Factory mFactory;
    private final ViewModelStore mViewModelStore;
    
    public ViewModelProvider(final ViewModelStore mViewModelStore, final Factory mFactory) {
        this.mFactory = mFactory;
        this.mViewModelStore = mViewModelStore;
    }
    
    public ViewModelProvider(final ViewModelStoreOwner viewModelStoreOwner, final Factory factory) {
        this(viewModelStoreOwner.getViewModelStore(), factory);
    }
    
    public <T extends ViewModel> T get(final Class<T> clazz) {
        final String canonicalName = clazz.getCanonicalName();
        if (canonicalName != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("androidx.lifecycle.ViewModelProvider.DefaultKey:");
            sb.append(canonicalName);
            return this.get(sb.toString(), clazz);
        }
        throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
    }
    
    public <T extends ViewModel> T get(final String s, final Class<T> clazz) {
        final ViewModel value = this.mViewModelStore.get(s);
        if (clazz.isInstance(value)) {
            return (T)value;
        }
        final ViewModel create = this.mFactory.create(clazz);
        this.mViewModelStore.put(s, create);
        return (T)create;
    }
    
    public static class AndroidViewModelFactory extends NewInstanceFactory
    {
        private static AndroidViewModelFactory sInstance;
        private Application mApplication;
        
        public AndroidViewModelFactory(final Application mApplication) {
            this.mApplication = mApplication;
        }
        
        public static AndroidViewModelFactory getInstance(final Application application) {
            if (AndroidViewModelFactory.sInstance == null) {
                AndroidViewModelFactory.sInstance = new AndroidViewModelFactory(application);
            }
            return AndroidViewModelFactory.sInstance;
        }
        
        @Override
        public <T extends ViewModel> T create(final Class<T> clazz) {
            if (AndroidViewModel.class.isAssignableFrom(clazz)) {
                try {
                    return clazz.getConstructor(Application.class).newInstance(this.mApplication);
                }
                catch (InvocationTargetException cause) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Cannot create an instance of ");
                    sb.append(clazz);
                    throw new RuntimeException(sb.toString(), cause);
                }
                catch (InstantiationException cause2) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Cannot create an instance of ");
                    sb2.append(clazz);
                    throw new RuntimeException(sb2.toString(), cause2);
                }
                catch (IllegalAccessException cause3) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Cannot create an instance of ");
                    sb3.append(clazz);
                    throw new RuntimeException(sb3.toString(), cause3);
                }
                catch (NoSuchMethodException cause4) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("Cannot create an instance of ");
                    sb4.append(clazz);
                    throw new RuntimeException(sb4.toString(), cause4);
                }
            }
            return super.create(clazz);
        }
    }
    
    public interface Factory
    {
         <T extends ViewModel> T create(final Class<T> p0);
    }
    
    public static class NewInstanceFactory implements Factory
    {
        @Override
        public <T extends ViewModel> T create(final Class<T> clazz) {
            try {
                return clazz.newInstance();
            }
            catch (IllegalAccessException cause) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Cannot create an instance of ");
                sb.append(clazz);
                throw new RuntimeException(sb.toString(), cause);
            }
            catch (InstantiationException cause2) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Cannot create an instance of ");
                sb2.append(clazz);
                throw new RuntimeException(sb2.toString(), cause2);
            }
        }
    }
}
