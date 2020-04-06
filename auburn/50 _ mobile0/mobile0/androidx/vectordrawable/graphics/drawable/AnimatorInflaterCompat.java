// 
// Decompiled by Procyon v0.5.36
// 

package androidx.vectordrawable.graphics.drawable;

import android.graphics.PathMeasure;
import android.graphics.Path;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.res.XmlResourceParser;
import android.content.res.Resources$NotFoundException;
import android.animation.AnimatorInflater;
import android.os.Build$VERSION;
import android.util.TypedValue;
import android.animation.TypeEvaluator;
import android.view.InflateException;
import androidx.core.graphics.PathParser;
import android.util.Log;
import android.animation.Keyframe;
import java.util.Iterator;
import android.animation.PropertyValuesHolder;
import android.content.res.TypedArray;
import java.util.ArrayList;
import androidx.core.content.res.TypedArrayUtils;
import android.animation.ValueAnimator;
import android.util.AttributeSet;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import android.animation.AnimatorSet;
import android.util.Xml;
import android.animation.Animator;
import org.xmlpull.v1.XmlPullParser;
import android.content.res.Resources$Theme;
import android.content.res.Resources;
import android.content.Context;

public class AnimatorInflaterCompat
{
    private static final boolean DBG_ANIMATOR_INFLATER = false;
    private static final int MAX_NUM_POINTS = 100;
    private static final String TAG = "AnimatorInflater";
    private static final int TOGETHER = 0;
    private static final int VALUE_TYPE_COLOR = 3;
    private static final int VALUE_TYPE_FLOAT = 0;
    private static final int VALUE_TYPE_INT = 1;
    private static final int VALUE_TYPE_PATH = 2;
    private static final int VALUE_TYPE_UNDEFINED = 4;
    
    private AnimatorInflaterCompat() {
    }
    
    private static Animator createAnimatorFromXml(final Context context, final Resources resources, final Resources$Theme resources$Theme, final XmlPullParser xmlPullParser, final float n) throws XmlPullParserException, IOException {
        return createAnimatorFromXml(context, resources, resources$Theme, xmlPullParser, Xml.asAttributeSet(xmlPullParser), null, 0, n);
    }
    
    private static Animator createAnimatorFromXml(final Context context, final Resources resources, final Resources$Theme resources$Theme, final XmlPullParser xmlPullParser, final AttributeSet set, final AnimatorSet set2, final int n, final float n2) throws XmlPullParserException, IOException {
        final int depth = xmlPullParser.getDepth();
        Object e = null;
        ArrayList<ValueAnimator> list = null;
        while (true) {
            final int next = xmlPullParser.next();
            if (next == 3 && xmlPullParser.getDepth() <= depth) {
                break;
            }
            if (next == 1) {
                break;
            }
            if (next != 2) {
                continue;
            }
            final String name = xmlPullParser.getName();
            boolean b = false;
            if (name.equals("objectAnimator")) {
                e = loadObjectAnimator(context, resources, resources$Theme, set, n2, xmlPullParser);
            }
            else if (name.equals("animator")) {
                e = loadAnimator(context, resources, resources$Theme, set, null, n2, xmlPullParser);
            }
            else if (name.equals("set")) {
                e = new AnimatorSet();
                final TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, AndroidResources.STYLEABLE_ANIMATOR_SET);
                createAnimatorFromXml(context, resources, resources$Theme, xmlPullParser, set, (AnimatorSet)e, TypedArrayUtils.getNamedInt(obtainAttributes, xmlPullParser, "ordering", 0, 0), n2);
                obtainAttributes.recycle();
            }
            else {
                if (!name.equals("propertyValuesHolder")) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unknown animator name: ");
                    sb.append(xmlPullParser.getName());
                    throw new RuntimeException(sb.toString());
                }
                final PropertyValuesHolder[] loadValues = loadValues(context, resources, resources$Theme, xmlPullParser, Xml.asAttributeSet(xmlPullParser));
                if (loadValues != null && e != null && e instanceof ValueAnimator) {
                    ((ValueAnimator)e).setValues(loadValues);
                }
                b = true;
            }
            ArrayList<ValueAnimator> list2 = list;
            if (set2 != null) {
                list2 = list;
                if (!b) {
                    if ((list2 = list) == null) {
                        list2 = new ArrayList<ValueAnimator>();
                    }
                    list2.add((ValueAnimator)e);
                }
            }
            list = list2;
        }
        if (set2 != null && list != null) {
            final Animator[] array = new Animator[list.size()];
            int n3 = 0;
            final Iterator<ValueAnimator> iterator = list.iterator();
            while (iterator.hasNext()) {
                array[n3] = (Animator)iterator.next();
                ++n3;
            }
            if (n == 0) {
                set2.playTogether(array);
            }
            else {
                set2.playSequentially(array);
            }
        }
        return (Animator)e;
    }
    
    private static Keyframe createNewKeyframe(Keyframe keyframe, final float n) {
        if (keyframe.getType() == Float.TYPE) {
            keyframe = Keyframe.ofFloat(n);
        }
        else if (keyframe.getType() == Integer.TYPE) {
            keyframe = Keyframe.ofInt(n);
        }
        else {
            keyframe = Keyframe.ofObject(n);
        }
        return keyframe;
    }
    
    private static void distributeKeyframes(final Keyframe[] array, float n, int i, final int n2) {
        n /= n2 - i + 2;
        while (i <= n2) {
            array[i].setFraction(array[i - 1].getFraction() + n);
            ++i;
        }
    }
    
    private static void dumpKeyframes(final Object[] array, String s) {
        if (array != null && array.length != 0) {
            Log.d("AnimatorInflater", s);
            for (int length = array.length, i = 0; i < length; ++i) {
                final Keyframe keyframe = (Keyframe)array[i];
                final StringBuilder sb = new StringBuilder();
                sb.append("Keyframe ");
                sb.append(i);
                sb.append(": fraction ");
                final float fraction = keyframe.getFraction();
                final String s2 = "null";
                if (fraction < 0.0f) {
                    s = "null";
                }
                else {
                    s = (String)Float.valueOf(keyframe.getFraction());
                }
                sb.append((Object)s);
                sb.append(", ");
                sb.append(", value : ");
                s = s2;
                if (keyframe.hasValue()) {
                    s = (String)keyframe.getValue();
                }
                sb.append((Object)s);
                Log.d("AnimatorInflater", sb.toString());
            }
        }
    }
    
    private static PropertyValuesHolder getPVH(final TypedArray typedArray, int n, int n2, final int n3, final String s) {
        final TypedValue peekValue = typedArray.peekValue(n2);
        final boolean b = peekValue != null;
        int type;
        if (b) {
            type = peekValue.type;
        }
        else {
            type = 0;
        }
        final TypedValue peekValue2 = typedArray.peekValue(n3);
        final boolean b2 = peekValue2 != null;
        int type2;
        if (b2) {
            type2 = peekValue2.type;
        }
        else {
            type2 = 0;
        }
        if (n == 4) {
            if ((b && isColorType(type)) || (b2 && isColorType(type2))) {
                n = 3;
            }
            else {
                n = 0;
            }
        }
        final boolean b3 = n == 0;
        PropertyValuesHolder propertyValuesHolder2;
        if (n == 2) {
            final String string = typedArray.getString(n2);
            final String string2 = typedArray.getString(n3);
            final PathParser.PathDataNode[] nodesFromPathData = PathParser.createNodesFromPathData(string);
            final PathParser.PathDataNode[] nodesFromPathData2 = PathParser.createNodesFromPathData(string2);
            PropertyValuesHolder propertyValuesHolder = null;
            Label_0340: {
                if (nodesFromPathData != null || nodesFromPathData2 != null) {
                    if (nodesFromPathData != null) {
                        final PathDataEvaluator pathDataEvaluator = new PathDataEvaluator();
                        if (nodesFromPathData2 != null) {
                            if (!PathParser.canMorph(nodesFromPathData, nodesFromPathData2)) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append(" Can't morph from ");
                                sb.append(string);
                                sb.append(" to ");
                                sb.append(string2);
                                throw new InflateException(sb.toString());
                            }
                            propertyValuesHolder = PropertyValuesHolder.ofObject(s, (TypeEvaluator)pathDataEvaluator, new Object[] { nodesFromPathData, nodesFromPathData2 });
                        }
                        else {
                            propertyValuesHolder = PropertyValuesHolder.ofObject(s, (TypeEvaluator)pathDataEvaluator, new Object[] { nodesFromPathData });
                        }
                        break Label_0340;
                    }
                    if (nodesFromPathData2 != null) {
                        propertyValuesHolder = PropertyValuesHolder.ofObject(s, (TypeEvaluator)new PathDataEvaluator(), new Object[] { nodesFromPathData2 });
                        break Label_0340;
                    }
                }
                propertyValuesHolder = null;
            }
            propertyValuesHolder2 = propertyValuesHolder;
        }
        else {
            Object instance = null;
            if (n == 3) {
                instance = ArgbEvaluator.getInstance();
            }
            PropertyValuesHolder propertyValuesHolder3;
            if (b3) {
                if (b) {
                    float n4;
                    if (type == 5) {
                        n4 = typedArray.getDimension(n2, 0.0f);
                    }
                    else {
                        n4 = typedArray.getFloat(n2, 0.0f);
                    }
                    if (b2) {
                        float n5;
                        if (type2 == 5) {
                            n5 = typedArray.getDimension(n3, 0.0f);
                        }
                        else {
                            n5 = typedArray.getFloat(n3, 0.0f);
                        }
                        propertyValuesHolder3 = PropertyValuesHolder.ofFloat(s, new float[] { n4, n5 });
                    }
                    else {
                        propertyValuesHolder3 = PropertyValuesHolder.ofFloat(s, new float[] { n4 });
                    }
                }
                else {
                    float n6;
                    if (type2 == 5) {
                        n6 = typedArray.getDimension(n3, 0.0f);
                    }
                    else {
                        n6 = typedArray.getFloat(n3, 0.0f);
                    }
                    propertyValuesHolder3 = PropertyValuesHolder.ofFloat(s, new float[] { n6 });
                }
            }
            else if (b) {
                if (type == 5) {
                    n = (int)typedArray.getDimension(n2, 0.0f);
                }
                else if (isColorType(type)) {
                    n = typedArray.getColor(n2, 0);
                }
                else {
                    n = typedArray.getInt(n2, 0);
                }
                if (b2) {
                    if (type2 == 5) {
                        n2 = (int)typedArray.getDimension(n3, 0.0f);
                    }
                    else if (isColorType(type2)) {
                        n2 = typedArray.getColor(n3, 0);
                    }
                    else {
                        n2 = typedArray.getInt(n3, 0);
                    }
                    propertyValuesHolder3 = PropertyValuesHolder.ofInt(s, new int[] { n, n2 });
                }
                else {
                    propertyValuesHolder3 = PropertyValuesHolder.ofInt(s, new int[] { n });
                }
            }
            else if (b2) {
                if (type2 == 5) {
                    n = (int)typedArray.getDimension(n3, 0.0f);
                }
                else if (isColorType(type2)) {
                    n = typedArray.getColor(n3, 0);
                }
                else {
                    n = typedArray.getInt(n3, 0);
                }
                propertyValuesHolder3 = PropertyValuesHolder.ofInt(s, new int[] { n });
            }
            else {
                propertyValuesHolder3 = null;
            }
            propertyValuesHolder2 = propertyValuesHolder3;
            if (propertyValuesHolder3 != null) {
                propertyValuesHolder2 = propertyValuesHolder3;
                if (instance != null) {
                    propertyValuesHolder3.setEvaluator((TypeEvaluator)instance);
                    propertyValuesHolder2 = propertyValuesHolder3;
                }
            }
        }
        return propertyValuesHolder2;
    }
    
    private static int inferValueTypeFromValues(final TypedArray typedArray, int n, int n2) {
        final TypedValue peekValue = typedArray.peekValue(n);
        final int n3 = 1;
        int type = 0;
        if (peekValue != null) {
            n = 1;
        }
        else {
            n = 0;
        }
        int type2;
        if (n != 0) {
            type2 = peekValue.type;
        }
        else {
            type2 = 0;
        }
        final TypedValue peekValue2 = typedArray.peekValue(n2);
        if (peekValue2 != null) {
            n2 = n3;
        }
        else {
            n2 = 0;
        }
        if (n2 != 0) {
            type = peekValue2.type;
        }
        if ((n != 0 && isColorType(type2)) || (n2 != 0 && isColorType(type))) {
            n = 3;
        }
        else {
            n = 0;
        }
        return n;
    }
    
    private static int inferValueTypeOfKeyframe(final Resources resources, final Resources$Theme resources$Theme, final AttributeSet set, final XmlPullParser xmlPullParser) {
        final TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, AndroidResources.STYLEABLE_KEYFRAME);
        boolean b = false;
        final TypedValue peekNamedValue = TypedArrayUtils.peekNamedValue(obtainAttributes, xmlPullParser, "value", 0);
        if (peekNamedValue != null) {
            b = true;
        }
        int n;
        if (b && isColorType(peekNamedValue.type)) {
            n = 3;
        }
        else {
            n = 0;
        }
        obtainAttributes.recycle();
        return n;
    }
    
    private static boolean isColorType(final int n) {
        return n >= 28 && n <= 31;
    }
    
    public static Animator loadAnimator(final Context context, final int n) throws Resources$NotFoundException {
        Animator animator;
        if (Build$VERSION.SDK_INT >= 24) {
            animator = AnimatorInflater.loadAnimator(context, n);
        }
        else {
            animator = loadAnimator(context, context.getResources(), context.getTheme(), n);
        }
        return animator;
    }
    
    public static Animator loadAnimator(final Context context, final Resources resources, final Resources$Theme resources$Theme, final int n) throws Resources$NotFoundException {
        return loadAnimator(context, resources, resources$Theme, n, 1.0f);
    }
    
    public static Animator loadAnimator(final Context context, final Resources resources, final Resources$Theme resources$Theme, final int n, final float n2) throws Resources$NotFoundException {
        XmlResourceParser animation = null;
        try {
            try {
                final XmlResourceParser xmlResourceParser = animation = resources.getAnimation(n);
                final Animator animatorFromXml = createAnimatorFromXml(context, resources, resources$Theme, (XmlPullParser)xmlResourceParser, n2);
                if (xmlResourceParser != null) {
                    xmlResourceParser.close();
                }
                return animatorFromXml;
            }
            finally {
                if (animation != null) {
                    animation.close();
                }
            }
        }
        catch (IOException ex) {}
        catch (XmlPullParserException ex2) {}
    }
    
    private static ValueAnimator loadAnimator(final Context context, final Resources resources, final Resources$Theme resources$Theme, final AttributeSet set, final ValueAnimator valueAnimator, final float n, final XmlPullParser xmlPullParser) throws Resources$NotFoundException {
        final TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, AndroidResources.STYLEABLE_ANIMATOR);
        final TypedArray obtainAttributes2 = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, AndroidResources.STYLEABLE_PROPERTY_ANIMATOR);
        ValueAnimator valueAnimator2 = valueAnimator;
        if (valueAnimator == null) {
            valueAnimator2 = new ValueAnimator();
        }
        parseAnimatorFromTypeArray(valueAnimator2, obtainAttributes, obtainAttributes2, n, xmlPullParser);
        final int namedResourceId = TypedArrayUtils.getNamedResourceId(obtainAttributes, xmlPullParser, "interpolator", 0, 0);
        if (namedResourceId > 0) {
            valueAnimator2.setInterpolator((TimeInterpolator)AnimationUtilsCompat.loadInterpolator(context, namedResourceId));
        }
        obtainAttributes.recycle();
        if (obtainAttributes2 != null) {
            obtainAttributes2.recycle();
        }
        return valueAnimator2;
    }
    
    private static Keyframe loadKeyframe(final Context context, final Resources resources, final Resources$Theme resources$Theme, final AttributeSet set, int namedResourceId, final XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        final TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, AndroidResources.STYLEABLE_KEYFRAME);
        Keyframe keyframe = null;
        final float namedFloat = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser, "fraction", 3, -1.0f);
        final TypedValue peekNamedValue = TypedArrayUtils.peekNamedValue(obtainAttributes, xmlPullParser, "value", 0);
        final boolean b = peekNamedValue != null;
        int n = namedResourceId;
        if (namedResourceId == 4) {
            if (b && isColorType(peekNamedValue.type)) {
                n = 3;
            }
            else {
                n = 0;
            }
        }
        if (b) {
            if (n != 0) {
                if (n == 1 || n == 3) {
                    keyframe = Keyframe.ofInt(namedFloat, TypedArrayUtils.getNamedInt(obtainAttributes, xmlPullParser, "value", 0, 0));
                }
            }
            else {
                keyframe = Keyframe.ofFloat(namedFloat, TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser, "value", 0, 0.0f));
            }
        }
        else if (n == 0) {
            keyframe = Keyframe.ofFloat(namedFloat);
        }
        else {
            keyframe = Keyframe.ofInt(namedFloat);
        }
        namedResourceId = TypedArrayUtils.getNamedResourceId(obtainAttributes, xmlPullParser, "interpolator", 1, 0);
        if (namedResourceId > 0) {
            keyframe.setInterpolator((TimeInterpolator)AnimationUtilsCompat.loadInterpolator(context, namedResourceId));
        }
        obtainAttributes.recycle();
        return keyframe;
    }
    
    private static ObjectAnimator loadObjectAnimator(final Context context, final Resources resources, final Resources$Theme resources$Theme, final AttributeSet set, final float n, final XmlPullParser xmlPullParser) throws Resources$NotFoundException {
        final ObjectAnimator objectAnimator = new ObjectAnimator();
        loadAnimator(context, resources, resources$Theme, set, (ValueAnimator)objectAnimator, n, xmlPullParser);
        return objectAnimator;
    }
    
    private static PropertyValuesHolder loadPvh(final Context context, final Resources resources, final Resources$Theme resources$Theme, final XmlPullParser xmlPullParser, final String s, int size) throws XmlPullParserException, IOException {
        ArrayList<Keyframe> list = null;
        int inferValueTypeOfKeyframe = size;
        int next;
        while (true) {
            size = (next = xmlPullParser.next());
            if (size == 3 || next == 1) {
                break;
            }
            if (!xmlPullParser.getName().equals("keyframe")) {
                continue;
            }
            if (inferValueTypeOfKeyframe == 4) {
                inferValueTypeOfKeyframe = inferValueTypeOfKeyframe(resources, resources$Theme, Xml.asAttributeSet(xmlPullParser), xmlPullParser);
            }
            final Keyframe loadKeyframe = loadKeyframe(context, resources, resources$Theme, Xml.asAttributeSet(xmlPullParser), inferValueTypeOfKeyframe, xmlPullParser);
            ArrayList<Keyframe> list2 = list;
            if (loadKeyframe != null) {
                if ((list2 = list) == null) {
                    list2 = new ArrayList<Keyframe>();
                }
                list2.add(loadKeyframe);
            }
            xmlPullParser.next();
            list = list2;
        }
        if (list != null) {
            size = list.size();
            final int n;
            if ((n = size) > 0) {
                final Keyframe keyframe = list.get(0);
                final Keyframe keyframe2 = list.get(n - 1);
                final float fraction = keyframe2.getFraction();
                size = n;
                if (fraction < 1.0f) {
                    if (fraction < 0.0f) {
                        keyframe2.setFraction(1.0f);
                        size = n;
                    }
                    else {
                        list.add(list.size(), createNewKeyframe(keyframe2, 1.0f));
                        size = n + 1;
                    }
                }
                final float fraction2 = keyframe.getFraction();
                int n2 = size;
                if (fraction2 != 0.0f) {
                    if (fraction2 < 0.0f) {
                        keyframe.setFraction(0.0f);
                        n2 = size;
                    }
                    else {
                        list.add(0, createNewKeyframe(keyframe, 0.0f));
                        n2 = size + 1;
                    }
                }
                final Keyframe[] a = new Keyframe[n2];
                list.toArray(a);
                int i = 0;
                size = next;
                while (i < n2) {
                    final Keyframe keyframe3 = a[i];
                    if (keyframe3.getFraction() < 0.0f) {
                        if (i == 0) {
                            keyframe3.setFraction(0.0f);
                        }
                        else if (i == n2 - 1) {
                            keyframe3.setFraction(1.0f);
                        }
                        else {
                            final int n3 = i + 1;
                            int n4 = i;
                            final int n5 = size;
                            for (size = n3; size < n2 - 1 && a[size].getFraction() < 0.0f; ++size) {
                                n4 = size;
                            }
                            distributeKeyframes(a, a[n4 + 1].getFraction() - a[i - 1].getFraction(), i, n4);
                            size = n5;
                        }
                    }
                    ++i;
                }
                PropertyValuesHolder ofKeyframe;
                final PropertyValuesHolder propertyValuesHolder = ofKeyframe = PropertyValuesHolder.ofKeyframe(s, a);
                if (inferValueTypeOfKeyframe == 3) {
                    propertyValuesHolder.setEvaluator((TypeEvaluator)ArgbEvaluator.getInstance());
                    ofKeyframe = propertyValuesHolder;
                    return ofKeyframe;
                }
                return ofKeyframe;
            }
        }
        return null;
    }
    
    private static PropertyValuesHolder[] loadValues(final Context context, final Resources resources, final Resources$Theme resources$Theme, final XmlPullParser xmlPullParser, final AttributeSet set) throws XmlPullParserException, IOException {
        ArrayList<PropertyValuesHolder> list = null;
        while (true) {
            final int eventType = xmlPullParser.getEventType();
            if (eventType == 3 || eventType == 1) {
                break;
            }
            if (eventType != 2) {
                xmlPullParser.next();
            }
            else {
                if (xmlPullParser.getName().equals("propertyValuesHolder")) {
                    final TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, AndroidResources.STYLEABLE_PROPERTY_VALUES_HOLDER);
                    final String namedString = TypedArrayUtils.getNamedString(obtainAttributes, xmlPullParser, "propertyName", 3);
                    final int namedInt = TypedArrayUtils.getNamedInt(obtainAttributes, xmlPullParser, "valueType", 2, 4);
                    PropertyValuesHolder e = loadPvh(context, resources, resources$Theme, xmlPullParser, namedString, namedInt);
                    if (e == null) {
                        e = getPVH(obtainAttributes, namedInt, 0, 1, namedString);
                    }
                    ArrayList<PropertyValuesHolder> list2 = list;
                    if (e != null) {
                        if ((list2 = list) == null) {
                            list2 = new ArrayList<PropertyValuesHolder>();
                        }
                        list2.add(e);
                    }
                    obtainAttributes.recycle();
                    list = list2;
                }
                xmlPullParser.next();
            }
        }
        PropertyValuesHolder[] array = null;
        if (list != null) {
            final int size = list.size();
            final PropertyValuesHolder[] array2 = new PropertyValuesHolder[size];
            int index = 0;
            while (true) {
                array = array2;
                if (index >= size) {
                    break;
                }
                array2[index] = list.get(index);
                ++index;
            }
        }
        return array;
    }
    
    private static void parseAnimatorFromTypeArray(final ValueAnimator valueAnimator, final TypedArray typedArray, final TypedArray typedArray2, final float n, final XmlPullParser xmlPullParser) {
        final long duration = TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "duration", 1, 300);
        final long startDelay = TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "startOffset", 2, 0);
        int namedInt;
        final int n2 = namedInt = TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "valueType", 7, 4);
        if (TypedArrayUtils.hasAttribute(xmlPullParser, "valueFrom")) {
            namedInt = n2;
            if (TypedArrayUtils.hasAttribute(xmlPullParser, "valueTo")) {
                int inferValueTypeFromValues;
                if ((inferValueTypeFromValues = n2) == 4) {
                    inferValueTypeFromValues = inferValueTypeFromValues(typedArray, 5, 6);
                }
                final PropertyValuesHolder pvh = getPVH(typedArray, inferValueTypeFromValues, 5, 6, "");
                namedInt = inferValueTypeFromValues;
                if (pvh != null) {
                    valueAnimator.setValues(new PropertyValuesHolder[] { pvh });
                    namedInt = inferValueTypeFromValues;
                }
            }
        }
        valueAnimator.setDuration(duration);
        valueAnimator.setStartDelay(startDelay);
        valueAnimator.setRepeatCount(TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "repeatCount", 3, 0));
        valueAnimator.setRepeatMode(TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "repeatMode", 4, 1));
        if (typedArray2 != null) {
            setupObjectAnimator(valueAnimator, typedArray2, namedInt, n, xmlPullParser);
        }
    }
    
    private static void setupObjectAnimator(final ValueAnimator valueAnimator, final TypedArray typedArray, final int n, final float n2, final XmlPullParser xmlPullParser) {
        final ObjectAnimator objectAnimator = (ObjectAnimator)valueAnimator;
        final String namedString = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "pathData", 1);
        if (namedString != null) {
            final String namedString2 = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "propertyXName", 2);
            final String namedString3 = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "propertyYName", 3);
            if (n == 2 || n == 4) {}
            if (namedString2 == null && namedString3 == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append(typedArray.getPositionDescription());
                sb.append(" propertyXName or propertyYName is needed for PathData");
                throw new InflateException(sb.toString());
            }
            setupPathMotion(PathParser.createPathFromPathData(namedString), objectAnimator, 0.5f * n2, namedString2, namedString3);
        }
        else {
            objectAnimator.setPropertyName(TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "propertyName", 0));
        }
    }
    
    private static void setupPathMotion(final Path path, final ObjectAnimator objectAnimator, float n, final String s, final String s2) {
        final PathMeasure pathMeasure = new PathMeasure(path, false);
        float f = 0.0f;
        final ArrayList<Float> list = new ArrayList<Float>();
        list.add(0.0f);
        do {
            f += pathMeasure.getLength();
            list.add(f);
        } while (pathMeasure.nextContour());
        final PathMeasure pathMeasure2 = new PathMeasure(path, false);
        final int min = Math.min(100, (int)(f / n) + 1);
        final float[] array = new float[min];
        final float[] array2 = new float[min];
        final float[] array3 = new float[2];
        int index = 0;
        final float n2 = f / (min - 1);
        n = 0.0f;
        int n3;
        for (int i = 0; i < min; ++i, index = n3) {
            pathMeasure2.getPosTan(n - list.get(index), array3, (float[])null);
            array[i] = array3[0];
            array2[i] = array3[1];
            n += n2;
            n3 = index;
            if (index + 1 < list.size()) {
                n3 = index;
                if (n > list.get(index + 1)) {
                    n3 = index + 1;
                    pathMeasure2.nextContour();
                }
            }
        }
        PropertyValuesHolder ofFloat = null;
        final PropertyValuesHolder propertyValuesHolder = null;
        if (s != null) {
            ofFloat = PropertyValuesHolder.ofFloat(s, array);
        }
        PropertyValuesHolder ofFloat2 = propertyValuesHolder;
        if (s2 != null) {
            ofFloat2 = PropertyValuesHolder.ofFloat(s2, array2);
        }
        if (ofFloat == null) {
            objectAnimator.setValues(new PropertyValuesHolder[] { ofFloat2 });
        }
        else if (ofFloat2 == null) {
            objectAnimator.setValues(new PropertyValuesHolder[] { ofFloat });
        }
        else {
            objectAnimator.setValues(new PropertyValuesHolder[] { ofFloat, ofFloat2 });
        }
    }
    
    private static class PathDataEvaluator implements TypeEvaluator<PathParser.PathDataNode[]>
    {
        private PathParser.PathDataNode[] mNodeArray;
        
        PathDataEvaluator() {
        }
        
        PathDataEvaluator(final PathParser.PathDataNode[] mNodeArray) {
            this.mNodeArray = mNodeArray;
        }
        
        public PathParser.PathDataNode[] evaluate(final float n, final PathParser.PathDataNode[] array, final PathParser.PathDataNode[] array2) {
            if (PathParser.canMorph(array, array2)) {
                final PathParser.PathDataNode[] mNodeArray = this.mNodeArray;
                if (mNodeArray == null || !PathParser.canMorph(mNodeArray, array)) {
                    this.mNodeArray = PathParser.deepCopyNodes(array);
                }
                for (int i = 0; i < array.length; ++i) {
                    this.mNodeArray[i].interpolatePathDataNode(array[i], array2[i], n);
                }
                return this.mNodeArray;
            }
            throw new IllegalArgumentException("Can't interpolate between two incompatible pathData");
        }
    }
}
