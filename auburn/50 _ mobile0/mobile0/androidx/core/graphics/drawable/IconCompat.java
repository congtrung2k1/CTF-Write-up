// 
// Decompiled by Procyon v0.5.36
// 

package androidx.core.graphics.drawable;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import java.io.OutputStream;
import android.graphics.Bitmap$CompressFormat;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import android.app.ActivityManager;
import androidx.core.content.ContextCompat;
import android.content.Intent$ShortcutIconResource;
import android.content.Intent;
import java.io.InputStream;
import androidx.core.content.res.ResourcesCompat;
import android.text.TextUtils;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.File;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager$NameNotFoundException;
import java.lang.reflect.InvocationTargetException;
import android.os.Build$VERSION;
import android.net.Uri;
import android.graphics.Shader;
import android.graphics.Matrix;
import android.graphics.BitmapShader;
import android.graphics.Shader$TileMode;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Bitmap$Config;
import android.graphics.Bitmap;
import android.content.res.Resources;
import android.content.res.Resources$NotFoundException;
import androidx.core.util.Preconditions;
import android.graphics.drawable.Icon;
import android.content.Context;
import android.util.Log;
import android.os.Bundle;
import android.content.res.ColorStateList;
import android.os.Parcelable;
import android.graphics.PorterDuff$Mode;
import androidx.versionedparcelable.CustomVersionedParcelable;

public class IconCompat extends CustomVersionedParcelable
{
    private static final float ADAPTIVE_ICON_INSET_FACTOR = 0.25f;
    private static final int AMBIENT_SHADOW_ALPHA = 30;
    private static final float BLUR_FACTOR = 0.010416667f;
    static final PorterDuff$Mode DEFAULT_TINT_MODE;
    private static final float DEFAULT_VIEW_PORT_SCALE = 0.6666667f;
    private static final String EXTRA_INT1 = "int1";
    private static final String EXTRA_INT2 = "int2";
    private static final String EXTRA_OBJ = "obj";
    private static final String EXTRA_TINT_LIST = "tint_list";
    private static final String EXTRA_TINT_MODE = "tint_mode";
    private static final String EXTRA_TYPE = "type";
    private static final float ICON_DIAMETER_FACTOR = 0.9166667f;
    private static final int KEY_SHADOW_ALPHA = 61;
    private static final float KEY_SHADOW_OFFSET_FACTOR = 0.020833334f;
    private static final String TAG = "IconCompat";
    public static final int TYPE_UNKNOWN = -1;
    public byte[] mData;
    public int mInt1;
    public int mInt2;
    Object mObj1;
    public Parcelable mParcelable;
    public ColorStateList mTintList;
    PorterDuff$Mode mTintMode;
    public String mTintModeStr;
    public int mType;
    
    static {
        DEFAULT_TINT_MODE = PorterDuff$Mode.SRC_IN;
    }
    
    public IconCompat() {
        this.mTintList = null;
        this.mTintMode = IconCompat.DEFAULT_TINT_MODE;
    }
    
    private IconCompat(final int mType) {
        this.mTintList = null;
        this.mTintMode = IconCompat.DEFAULT_TINT_MODE;
        this.mType = mType;
    }
    
    public static IconCompat createFromBundle(final Bundle bundle) {
        final int int1 = bundle.getInt("type");
        final IconCompat iconCompat = new IconCompat(int1);
        iconCompat.mInt1 = bundle.getInt("int1");
        iconCompat.mInt2 = bundle.getInt("int2");
        if (bundle.containsKey("tint_list")) {
            iconCompat.mTintList = (ColorStateList)bundle.getParcelable("tint_list");
        }
        if (bundle.containsKey("tint_mode")) {
            iconCompat.mTintMode = PorterDuff$Mode.valueOf(bundle.getString("tint_mode"));
        }
        Label_0169: {
            if (int1 != -1 && int1 != 1) {
                if (int1 != 2) {
                    if (int1 == 3) {
                        iconCompat.mObj1 = bundle.getByteArray("obj");
                        return iconCompat;
                    }
                    if (int1 != 4) {
                        if (int1 != 5) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Unknown type ");
                            sb.append(int1);
                            Log.w("IconCompat", sb.toString());
                            return null;
                        }
                        break Label_0169;
                    }
                }
                iconCompat.mObj1 = bundle.getString("obj");
                return iconCompat;
            }
        }
        iconCompat.mObj1 = bundle.getParcelable("obj");
        return iconCompat;
    }
    
    public static IconCompat createFromIcon(final Context context, final Icon mObj1) {
        Preconditions.checkNotNull(mObj1);
        final int type = getType(mObj1);
        if (type != 2) {
            if (type != 4) {
                final IconCompat iconCompat = new IconCompat(-1);
                iconCompat.mObj1 = mObj1;
                return iconCompat;
            }
            return createWithContentUri(getUri(mObj1));
        }
        else {
            final String resPackage = getResPackage(mObj1);
            try {
                return createWithResource(getResources(context, resPackage), resPackage, getResId(mObj1));
            }
            catch (Resources$NotFoundException ex) {
                throw new IllegalArgumentException("Icon resource cannot be found");
            }
        }
    }
    
    public static IconCompat createFromIcon(final Icon mObj1) {
        Preconditions.checkNotNull(mObj1);
        final int type = getType(mObj1);
        if (type == 2) {
            return createWithResource(null, getResPackage(mObj1), getResId(mObj1));
        }
        if (type != 4) {
            final IconCompat iconCompat = new IconCompat(-1);
            iconCompat.mObj1 = mObj1;
            return iconCompat;
        }
        return createWithContentUri(getUri(mObj1));
    }
    
    static Bitmap createLegacyIconFromAdaptiveIcon(final Bitmap bitmap, final boolean b) {
        final int n = (int)(Math.min(bitmap.getWidth(), bitmap.getHeight()) * 0.6666667f);
        final Bitmap bitmap2 = Bitmap.createBitmap(n, n, Bitmap$Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap2);
        final Paint paint = new Paint(3);
        final float n2 = n * 0.5f;
        final float n3 = 0.9166667f * n2;
        if (b) {
            final float n4 = n * 0.010416667f;
            paint.setColor(0);
            paint.setShadowLayer(n4, 0.0f, n * 0.020833334f, 1023410176);
            canvas.drawCircle(n2, n2, n3, paint);
            paint.setShadowLayer(n4, 0.0f, 0.0f, 503316480);
            canvas.drawCircle(n2, n2, n3, paint);
            paint.clearShadowLayer();
        }
        paint.setColor(-16777216);
        final BitmapShader shader = new BitmapShader(bitmap, Shader$TileMode.CLAMP, Shader$TileMode.CLAMP);
        final Matrix localMatrix = new Matrix();
        localMatrix.setTranslate((float)(-(bitmap.getWidth() - n) / 2), (float)(-(bitmap.getHeight() - n) / 2));
        shader.setLocalMatrix(localMatrix);
        paint.setShader((Shader)shader);
        canvas.drawCircle(n2, n2, n3, paint);
        canvas.setBitmap((Bitmap)null);
        return bitmap2;
    }
    
    public static IconCompat createWithAdaptiveBitmap(final Bitmap mObj1) {
        if (mObj1 != null) {
            final IconCompat iconCompat = new IconCompat(5);
            iconCompat.mObj1 = mObj1;
            return iconCompat;
        }
        throw new IllegalArgumentException("Bitmap must not be null.");
    }
    
    public static IconCompat createWithBitmap(final Bitmap mObj1) {
        if (mObj1 != null) {
            final IconCompat iconCompat = new IconCompat(1);
            iconCompat.mObj1 = mObj1;
            return iconCompat;
        }
        throw new IllegalArgumentException("Bitmap must not be null.");
    }
    
    public static IconCompat createWithContentUri(final Uri uri) {
        if (uri != null) {
            return createWithContentUri(uri.toString());
        }
        throw new IllegalArgumentException("Uri must not be null.");
    }
    
    public static IconCompat createWithContentUri(final String mObj1) {
        if (mObj1 != null) {
            final IconCompat iconCompat = new IconCompat(4);
            iconCompat.mObj1 = mObj1;
            return iconCompat;
        }
        throw new IllegalArgumentException("Uri must not be null.");
    }
    
    public static IconCompat createWithData(final byte[] mObj1, final int mInt1, final int mInt2) {
        if (mObj1 != null) {
            final IconCompat iconCompat = new IconCompat(3);
            iconCompat.mObj1 = mObj1;
            iconCompat.mInt1 = mInt1;
            iconCompat.mInt2 = mInt2;
            return iconCompat;
        }
        throw new IllegalArgumentException("Data must not be null.");
    }
    
    public static IconCompat createWithResource(final Context context, final int n) {
        if (context != null) {
            return createWithResource(context.getResources(), context.getPackageName(), n);
        }
        throw new IllegalArgumentException("Context must not be null.");
    }
    
    public static IconCompat createWithResource(final Resources resources, final String mObj1, final int mInt1) {
        if (mObj1 == null) {
            throw new IllegalArgumentException("Package must not be null.");
        }
        if (mInt1 != 0) {
            final IconCompat iconCompat = new IconCompat(2);
            iconCompat.mInt1 = mInt1;
            if (resources != null) {
                try {
                    iconCompat.mObj1 = resources.getResourceName(mInt1);
                    return iconCompat;
                }
                catch (Resources$NotFoundException ex) {
                    throw new IllegalArgumentException("Icon resource cannot be found");
                }
            }
            iconCompat.mObj1 = mObj1;
            return iconCompat;
        }
        throw new IllegalArgumentException("Drawable resource ID must not be 0");
    }
    
    private static int getResId(final Icon obj) {
        if (Build$VERSION.SDK_INT >= 28) {
            return obj.getResId();
        }
        try {
            return (int)obj.getClass().getMethod("getResId", (Class<?>[])new Class[0]).invoke(obj, new Object[0]);
        }
        catch (NoSuchMethodException ex) {
            Log.e("IconCompat", "Unable to get icon resource", (Throwable)ex);
            return 0;
        }
        catch (InvocationTargetException ex2) {
            Log.e("IconCompat", "Unable to get icon resource", (Throwable)ex2);
            return 0;
        }
        catch (IllegalAccessException ex3) {
            Log.e("IconCompat", "Unable to get icon resource", (Throwable)ex3);
            return 0;
        }
    }
    
    private static String getResPackage(final Icon obj) {
        if (Build$VERSION.SDK_INT >= 28) {
            return obj.getResPackage();
        }
        try {
            return (String)obj.getClass().getMethod("getResPackage", (Class<?>[])new Class[0]).invoke(obj, new Object[0]);
        }
        catch (NoSuchMethodException ex) {
            Log.e("IconCompat", "Unable to get icon package", (Throwable)ex);
            return null;
        }
        catch (InvocationTargetException ex2) {
            Log.e("IconCompat", "Unable to get icon package", (Throwable)ex2);
            return null;
        }
        catch (IllegalAccessException ex3) {
            Log.e("IconCompat", "Unable to get icon package", (Throwable)ex3);
            return null;
        }
    }
    
    private static Resources getResources(final Context context, final String anObject) {
        if ("android".equals(anObject)) {
            return Resources.getSystem();
        }
        final PackageManager packageManager = context.getPackageManager();
        try {
            final ApplicationInfo applicationInfo = packageManager.getApplicationInfo(anObject, 8192);
            if (applicationInfo != null) {
                return packageManager.getResourcesForApplication(applicationInfo);
            }
            return null;
        }
        catch (PackageManager$NameNotFoundException ex) {
            Log.e("IconCompat", String.format("Unable to find pkg=%s for icon", anObject), (Throwable)ex);
            return null;
        }
    }
    
    private static int getType(final Icon icon) {
        if (Build$VERSION.SDK_INT >= 28) {
            return icon.getType();
        }
        try {
            return (int)icon.getClass().getMethod("getType", (Class<?>[])new Class[0]).invoke(icon, new Object[0]);
        }
        catch (NoSuchMethodException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unable to get icon type ");
            sb.append(icon);
            Log.e("IconCompat", sb.toString(), (Throwable)ex);
            return -1;
        }
        catch (InvocationTargetException ex2) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Unable to get icon type ");
            sb2.append(icon);
            Log.e("IconCompat", sb2.toString(), (Throwable)ex2);
            return -1;
        }
        catch (IllegalAccessException ex3) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Unable to get icon type ");
            sb3.append(icon);
            Log.e("IconCompat", sb3.toString(), (Throwable)ex3);
            return -1;
        }
    }
    
    private static Uri getUri(final Icon obj) {
        if (Build$VERSION.SDK_INT >= 28) {
            return obj.getUri();
        }
        try {
            return (Uri)obj.getClass().getMethod("getUri", (Class<?>[])new Class[0]).invoke(obj, new Object[0]);
        }
        catch (NoSuchMethodException ex) {
            Log.e("IconCompat", "Unable to get icon uri", (Throwable)ex);
            return null;
        }
        catch (InvocationTargetException ex2) {
            Log.e("IconCompat", "Unable to get icon uri", (Throwable)ex2);
            return null;
        }
        catch (IllegalAccessException ex3) {
            Log.e("IconCompat", "Unable to get icon uri", (Throwable)ex3);
            return null;
        }
    }
    
    private Drawable loadDrawableInner(final Context context) {
        final int mType = this.mType;
        if (mType != 1) {
            if (mType != 2) {
                if (mType == 3) {
                    return (Drawable)new BitmapDrawable(context.getResources(), BitmapFactory.decodeByteArray((byte[])this.mObj1, this.mInt1, this.mInt2));
                }
                if (mType != 4) {
                    if (mType == 5) {
                        return (Drawable)new BitmapDrawable(context.getResources(), createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, false));
                    }
                }
                else {
                    final Uri parse = Uri.parse((String)this.mObj1);
                    final String scheme = parse.getScheme();
                    InputStream openInputStream = null;
                    final InputStream inputStream = null;
                    Label_0243: {
                        if (!"content".equals(scheme)) {
                            if (!"file".equals(scheme)) {
                                try {
                                    openInputStream = new FileInputStream(new File((String)this.mObj1));
                                }
                                catch (FileNotFoundException ex) {
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("Unable to load image from path: ");
                                    sb.append(parse);
                                    Log.w("IconCompat", sb.toString(), (Throwable)ex);
                                }
                                break Label_0243;
                            }
                        }
                        try {
                            openInputStream = context.getContentResolver().openInputStream(parse);
                        }
                        catch (Exception ex2) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("Unable to load image from URI: ");
                            sb2.append(parse);
                            Log.w("IconCompat", sb2.toString(), (Throwable)ex2);
                            openInputStream = inputStream;
                        }
                    }
                    if (openInputStream != null) {
                        return (Drawable)new BitmapDrawable(context.getResources(), BitmapFactory.decodeStream(openInputStream));
                    }
                }
            }
            else {
                String s;
                if (TextUtils.isEmpty((CharSequence)(s = this.getResPackage()))) {
                    s = context.getPackageName();
                }
                final Resources resources = getResources(context, s);
                try {
                    return ResourcesCompat.getDrawable(resources, this.mInt1, context.getTheme());
                }
                catch (RuntimeException ex3) {
                    Log.e("IconCompat", String.format("Unable to load resource 0x%08x from pkg=%s", this.mInt1, this.mObj1), (Throwable)ex3);
                }
            }
            return null;
        }
        return (Drawable)new BitmapDrawable(context.getResources(), (Bitmap)this.mObj1);
    }
    
    private static String typeToString(final int n) {
        if (n == 1) {
            return "BITMAP";
        }
        if (n == 2) {
            return "RESOURCE";
        }
        if (n == 3) {
            return "DATA";
        }
        if (n == 4) {
            return "URI";
        }
        if (n != 5) {
            return "UNKNOWN";
        }
        return "BITMAP_MASKABLE";
    }
    
    public void addToShortcutIntent(final Intent intent, final Drawable drawable, Context packageContext) {
        this.checkResource(packageContext);
        final int mType = this.mType;
        Bitmap bitmap = null;
        Label_0264: {
            if (mType != 1) {
                if (mType != 2) {
                    if (mType == 5) {
                        bitmap = createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, true);
                        break Label_0264;
                    }
                    throw new IllegalArgumentException("Icon type not supported for intent shortcuts");
                }
                else {
                    try {
                        packageContext = packageContext.createPackageContext(this.getResPackage(), 0);
                        if (drawable == null) {
                            intent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", (Parcelable)Intent$ShortcutIconResource.fromContext(packageContext, this.mInt1));
                            return;
                        }
                        final Drawable drawable2 = ContextCompat.getDrawable(packageContext, this.mInt1);
                        if (drawable2.getIntrinsicWidth() > 0 && drawable2.getIntrinsicHeight() > 0) {
                            bitmap = Bitmap.createBitmap(drawable2.getIntrinsicWidth(), drawable2.getIntrinsicHeight(), Bitmap$Config.ARGB_8888);
                        }
                        else {
                            final int launcherLargeIconSize = ((ActivityManager)packageContext.getSystemService("activity")).getLauncherLargeIconSize();
                            bitmap = Bitmap.createBitmap(launcherLargeIconSize, launcherLargeIconSize, Bitmap$Config.ARGB_8888);
                        }
                        drawable2.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                        drawable2.draw(new Canvas(bitmap));
                        break Label_0264;
                    }
                    catch (PackageManager$NameNotFoundException cause) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Can't find package ");
                        sb.append(this.mObj1);
                        throw new IllegalArgumentException(sb.toString(), (Throwable)cause);
                    }
                }
            }
            final Bitmap bitmap2 = bitmap = (Bitmap)this.mObj1;
            if (drawable != null) {
                bitmap = bitmap2.copy(bitmap2.getConfig(), true);
            }
        }
        if (drawable != null) {
            final int width = bitmap.getWidth();
            final int height = bitmap.getHeight();
            drawable.setBounds(width / 2, height / 2, width, height);
            drawable.draw(new Canvas(bitmap));
        }
        intent.putExtra("android.intent.extra.shortcut.ICON", (Parcelable)bitmap);
    }
    
    public void checkResource(final Context context) {
        if (this.mType == 2) {
            final String s = (String)this.mObj1;
            if (!s.contains(":")) {
                return;
            }
            final String s2 = s.split(":", -1)[1];
            final String s3 = s2.split("/", -1)[0];
            final String str = s2.split("/", -1)[1];
            final String str2 = s.split(":", -1)[0];
            final int identifier = getResources(context, str2).getIdentifier(str, s3, str2);
            if (this.mInt1 != identifier) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Id has changed for ");
                sb.append(str2);
                sb.append("/");
                sb.append(str);
                Log.i("IconCompat", sb.toString());
                this.mInt1 = identifier;
            }
        }
    }
    
    public int getResId() {
        if (this.mType == -1 && Build$VERSION.SDK_INT >= 23) {
            return getResId((Icon)this.mObj1);
        }
        if (this.mType == 2) {
            return this.mInt1;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("called getResId() on ");
        sb.append(this);
        throw new IllegalStateException(sb.toString());
    }
    
    public String getResPackage() {
        if (this.mType == -1 && Build$VERSION.SDK_INT >= 23) {
            return getResPackage((Icon)this.mObj1);
        }
        if (this.mType == 2) {
            return ((String)this.mObj1).split(":", -1)[0];
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("called getResPackage() on ");
        sb.append(this);
        throw new IllegalStateException(sb.toString());
    }
    
    public int getType() {
        if (this.mType == -1 && Build$VERSION.SDK_INT >= 23) {
            return getType((Icon)this.mObj1);
        }
        return this.mType;
    }
    
    public Uri getUri() {
        if (this.mType == -1 && Build$VERSION.SDK_INT >= 23) {
            return getUri((Icon)this.mObj1);
        }
        return Uri.parse((String)this.mObj1);
    }
    
    public Drawable loadDrawable(final Context context) {
        this.checkResource(context);
        if (Build$VERSION.SDK_INT >= 23) {
            return this.toIcon().loadDrawable(context);
        }
        final Drawable loadDrawableInner = this.loadDrawableInner(context);
        if (loadDrawableInner != null && (this.mTintList != null || this.mTintMode != IconCompat.DEFAULT_TINT_MODE)) {
            loadDrawableInner.mutate();
            DrawableCompat.setTintList(loadDrawableInner, this.mTintList);
            DrawableCompat.setTintMode(loadDrawableInner, this.mTintMode);
        }
        return loadDrawableInner;
    }
    
    @Override
    public void onPostParceling() {
        this.mTintMode = PorterDuff$Mode.valueOf(this.mTintModeStr);
        final int mType = this.mType;
        if (mType != -1) {
            Label_0084: {
                if (mType != 1) {
                    if (mType != 2) {
                        if (mType == 3) {
                            this.mObj1 = this.mData;
                            return;
                        }
                        if (mType != 4) {
                            if (mType != 5) {
                                return;
                            }
                            break Label_0084;
                        }
                    }
                    this.mObj1 = new String(this.mData, Charset.forName("UTF-16"));
                    return;
                }
            }
            final Parcelable mParcelable = this.mParcelable;
            if (mParcelable != null) {
                this.mObj1 = mParcelable;
            }
            else {
                final byte[] mData = this.mData;
                this.mObj1 = mData;
                this.mType = 3;
                this.mInt1 = 0;
                this.mInt2 = mData.length;
            }
        }
        else {
            final Parcelable mParcelable2 = this.mParcelable;
            if (mParcelable2 == null) {
                throw new IllegalArgumentException("Invalid icon");
            }
            this.mObj1 = mParcelable2;
        }
    }
    
    @Override
    public void onPreParceling(final boolean b) {
        this.mTintModeStr = this.mTintMode.name();
        final int mType = this.mType;
        if (mType != -1) {
            if (mType != 1) {
                if (mType == 2) {
                    this.mData = ((String)this.mObj1).getBytes(Charset.forName("UTF-16"));
                    return;
                }
                if (mType == 3) {
                    this.mData = (byte[])this.mObj1;
                    return;
                }
                if (mType == 4) {
                    this.mData = this.mObj1.toString().getBytes(Charset.forName("UTF-16"));
                    return;
                }
                if (mType != 5) {
                    return;
                }
            }
            if (b) {
                final Bitmap bitmap = (Bitmap)this.mObj1;
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap$CompressFormat.PNG, 90, (OutputStream)byteArrayOutputStream);
                this.mData = byteArrayOutputStream.toByteArray();
            }
            else {
                this.mParcelable = (Parcelable)this.mObj1;
            }
        }
        else {
            if (b) {
                throw new IllegalArgumentException("Can't serialize Icon created with IconCompat#createFromIcon");
            }
            this.mParcelable = (Parcelable)this.mObj1;
        }
    }
    
    public IconCompat setTint(final int n) {
        return this.setTintList(ColorStateList.valueOf(n));
    }
    
    public IconCompat setTintList(final ColorStateList mTintList) {
        this.mTintList = mTintList;
        return this;
    }
    
    public IconCompat setTintMode(final PorterDuff$Mode mTintMode) {
        this.mTintMode = mTintMode;
        return this;
    }
    
    public Bundle toBundle() {
        final Bundle bundle = new Bundle();
        final int mType = this.mType;
        Label_0118: {
            if (mType != -1) {
                Label_0089: {
                    if (mType != 1) {
                        if (mType != 2) {
                            if (mType == 3) {
                                bundle.putByteArray("obj", (byte[])this.mObj1);
                                break Label_0118;
                            }
                            if (mType != 4) {
                                if (mType == 5) {
                                    break Label_0089;
                                }
                                throw new IllegalArgumentException("Invalid icon");
                            }
                        }
                        bundle.putString("obj", (String)this.mObj1);
                        break Label_0118;
                    }
                }
                bundle.putParcelable("obj", (Parcelable)this.mObj1);
            }
            else {
                bundle.putParcelable("obj", (Parcelable)this.mObj1);
            }
        }
        bundle.putInt("type", this.mType);
        bundle.putInt("int1", this.mInt1);
        bundle.putInt("int2", this.mInt2);
        final ColorStateList mTintList = this.mTintList;
        if (mTintList != null) {
            bundle.putParcelable("tint_list", (Parcelable)mTintList);
        }
        final PorterDuff$Mode mTintMode = this.mTintMode;
        if (mTintMode != IconCompat.DEFAULT_TINT_MODE) {
            bundle.putString("tint_mode", mTintMode.name());
        }
        return bundle;
    }
    
    public Icon toIcon() {
        final int mType = this.mType;
        if (mType != -1) {
            Icon icon;
            if (mType != 1) {
                if (mType != 2) {
                    if (mType != 3) {
                        if (mType != 4) {
                            if (mType != 5) {
                                throw new IllegalArgumentException("Unknown type");
                            }
                            if (Build$VERSION.SDK_INT >= 26) {
                                icon = Icon.createWithAdaptiveBitmap((Bitmap)this.mObj1);
                            }
                            else {
                                icon = Icon.createWithBitmap(createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, false));
                            }
                        }
                        else {
                            icon = Icon.createWithContentUri((String)this.mObj1);
                        }
                    }
                    else {
                        icon = Icon.createWithData((byte[])this.mObj1, this.mInt1, this.mInt2);
                    }
                }
                else {
                    icon = Icon.createWithResource(this.getResPackage(), this.mInt1);
                }
            }
            else {
                icon = Icon.createWithBitmap((Bitmap)this.mObj1);
            }
            final ColorStateList mTintList = this.mTintList;
            if (mTintList != null) {
                icon.setTintList(mTintList);
            }
            final PorterDuff$Mode mTintMode = this.mTintMode;
            if (mTintMode != IconCompat.DEFAULT_TINT_MODE) {
                icon.setTintMode(mTintMode);
            }
            return icon;
        }
        return (Icon)this.mObj1;
    }
    
    @Override
    public String toString() {
        if (this.mType == -1) {
            return String.valueOf(this.mObj1);
        }
        final StringBuilder append = new StringBuilder("Icon(typ=").append(typeToString(this.mType));
        final int mType = this.mType;
        Label_0233: {
            if (mType != 1) {
                if (mType == 2) {
                    append.append(" pkg=");
                    append.append(this.getResPackage());
                    append.append(" id=");
                    append.append(String.format("0x%08x", this.getResId()));
                    break Label_0233;
                }
                if (mType != 3) {
                    if (mType == 4) {
                        append.append(" uri=");
                        append.append(this.mObj1);
                        break Label_0233;
                    }
                    if (mType != 5) {
                        break Label_0233;
                    }
                }
                else {
                    append.append(" len=");
                    append.append(this.mInt1);
                    if (this.mInt2 != 0) {
                        append.append(" off=");
                        append.append(this.mInt2);
                    }
                    break Label_0233;
                }
            }
            append.append(" size=");
            append.append(((Bitmap)this.mObj1).getWidth());
            append.append("x");
            append.append(((Bitmap)this.mObj1).getHeight());
        }
        if (this.mTintList != null) {
            append.append(" tint=");
            append.append(this.mTintList);
        }
        if (this.mTintMode != IconCompat.DEFAULT_TINT_MODE) {
            append.append(" mode=");
            append.append(this.mTintMode);
        }
        append.append(")");
        return append.toString();
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface IconType {
    }
}
