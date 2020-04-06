// 
// Decompiled by Procyon v0.5.36
// 

package androidx.vectordrawable.graphics.drawable;

import android.content.res.XmlResourceParser;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import android.content.res.Resources$NotFoundException;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import android.view.animation.AnimationUtils;
import android.os.Build$VERSION;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import android.util.AttributeSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.util.Xml;
import android.view.animation.Interpolator;
import org.xmlpull.v1.XmlPullParser;
import android.content.res.Resources$Theme;
import android.content.res.Resources;
import android.content.Context;

public class AnimationUtilsCompat
{
    private AnimationUtilsCompat() {
    }
    
    private static Interpolator createInterpolatorFromXml(final Context context, final Resources resources, final Resources$Theme resources$Theme, final XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        Object o = null;
        final int depth = xmlPullParser.getDepth();
        while (true) {
            final int next = xmlPullParser.next();
            if ((next == 3 && xmlPullParser.getDepth() <= depth) || next == 1) {
                return (Interpolator)o;
            }
            if (next != 2) {
                continue;
            }
            final AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
            final String name = xmlPullParser.getName();
            if (name.equals("linearInterpolator")) {
                o = new LinearInterpolator();
            }
            else if (name.equals("accelerateInterpolator")) {
                o = new AccelerateInterpolator(context, attributeSet);
            }
            else if (name.equals("decelerateInterpolator")) {
                o = new DecelerateInterpolator(context, attributeSet);
            }
            else if (name.equals("accelerateDecelerateInterpolator")) {
                o = new AccelerateDecelerateInterpolator();
            }
            else if (name.equals("cycleInterpolator")) {
                o = new CycleInterpolator(context, attributeSet);
            }
            else if (name.equals("anticipateInterpolator")) {
                o = new AnticipateInterpolator(context, attributeSet);
            }
            else if (name.equals("overshootInterpolator")) {
                o = new OvershootInterpolator(context, attributeSet);
            }
            else if (name.equals("anticipateOvershootInterpolator")) {
                o = new AnticipateOvershootInterpolator(context, attributeSet);
            }
            else if (name.equals("bounceInterpolator")) {
                o = new BounceInterpolator();
            }
            else {
                if (!name.equals("pathInterpolator")) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unknown interpolator name: ");
                    sb.append(xmlPullParser.getName());
                    throw new RuntimeException(sb.toString());
                }
                o = new PathInterpolatorCompat(context, attributeSet, xmlPullParser);
            }
        }
    }
    
    public static Interpolator loadInterpolator(final Context context, final int i) throws Resources$NotFoundException {
        if (Build$VERSION.SDK_INT >= 21) {
            return AnimationUtils.loadInterpolator(context, i);
        }
        final XmlResourceParser xmlResourceParser = null;
        Label_0062: {
            if (i != 17563663) {
                break Label_0062;
            }
            try {
                try {
                    final FastOutLinearInInterpolator fastOutLinearInInterpolator = new FastOutLinearInInterpolator();
                    if (false) {
                        throw new NullPointerException();
                    }
                    return (Interpolator)fastOutLinearInInterpolator;
                }
                finally {
                    if (xmlResourceParser != null) {
                        xmlResourceParser.close();
                    }
                    Label_0116: {
                        final LinearOutSlowInInterpolator linearOutSlowInInterpolator;
                        return (Interpolator)linearOutSlowInInterpolator;
                    }
                    // iftrue(Label_0116:, !false)
                    // iftrue(Label_0088:, !false)
                    // iftrue(Label_0090:, i != 17563661)
                    // iftrue(Label_0165:, animation == null)
                Block_9_Outer:
                    while (true) {
                        final XmlResourceParser animation;
                        animation.close();
                        return;
                        final LinearOutSlowInInterpolator linearOutSlowInInterpolator = new LinearOutSlowInInterpolator();
                        throw new NullPointerException();
                        FastOutSlowInInterpolator fastOutSlowInInterpolator = null;
                        Label_0088:
                        return (Interpolator)fastOutSlowInInterpolator;
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Can't load animation resource ID #0x");
                        sb.append(Integer.toHexString(i));
                        final Resources$NotFoundException ex = new Resources$NotFoundException(sb.toString());
                        final IOException ex2;
                        ex.initCause((Throwable)ex2);
                        throw ex;
                        Label_0165:
                        return interpolatorFromXml;
                        while (true) {
                            fastOutSlowInInterpolator = new FastOutSlowInInterpolator();
                            throw new NullPointerException();
                            continue;
                        }
                        Label_0118:
                        animation = context.getResources().getAnimation(i);
                        interpolatorFromXml = createInterpolatorFromXml(context, context.getResources(), context.getTheme(), (XmlPullParser)animation);
                        continue Block_9_Outer;
                    }
                    Label_0090:;
                }
                // iftrue(Label_0118:, i != 17563662)
            }
            catch (IOException ex3) {}
            catch (XmlPullParserException ex4) {}
        }
    }
}
