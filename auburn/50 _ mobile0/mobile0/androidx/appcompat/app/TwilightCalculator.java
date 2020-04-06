// 
// Decompiled by Procyon v0.5.36
// 

package androidx.appcompat.app;

class TwilightCalculator
{
    private static final float ALTIDUTE_CORRECTION_CIVIL_TWILIGHT = -0.10471976f;
    private static final float C1 = 0.0334196f;
    private static final float C2 = 3.49066E-4f;
    private static final float C3 = 5.236E-6f;
    public static final int DAY = 0;
    private static final float DEGREES_TO_RADIANS = 0.017453292f;
    private static final float J0 = 9.0E-4f;
    public static final int NIGHT = 1;
    private static final float OBLIQUITY = 0.4092797f;
    private static final long UTC_2000 = 946728000000L;
    private static TwilightCalculator sInstance;
    public int state;
    public long sunrise;
    public long sunset;
    
    static TwilightCalculator getInstance() {
        if (TwilightCalculator.sInstance == null) {
            TwilightCalculator.sInstance = new TwilightCalculator();
        }
        return TwilightCalculator.sInstance;
    }
    
    public void calculateTwilight(final long n, double v, double n2) {
        final float n3 = (n - 946728000000L) / 8.64E7f;
        final float n4 = 0.01720197f * n3 + 6.24006f;
        final double v2 = n4;
        final double sin = Math.sin(n4);
        Double.isNaN(v2);
        final double a = 1.796593063 + (v2 + sin * 0.03341960161924362 + Math.sin(2.0f * n4) * 3.4906598739326E-4 + Math.sin(3.0f * n4) * 5.236000106378924E-6) + 3.141592653589793;
        n2 = -n2 / 360.0;
        final double v3 = n3 - 9.0E-4f;
        Double.isNaN(v3);
        final double v4 = 9.0E-4f + Math.round(v3 - n2);
        Double.isNaN(v4);
        n2 = v4 + n2 + Math.sin(n4) * 0.0053 + Math.sin(2.0 * a) * -0.0069;
        final double asin = Math.asin(Math.sin(a) * Math.sin(0.4092797040939331));
        v *= 0.01745329238474369;
        v = (Math.sin(-0.10471975803375244) - Math.sin(v) * Math.sin(asin)) / (Math.cos(v) * Math.cos(asin));
        if (v >= 1.0) {
            this.state = 1;
            this.sunset = -1L;
            this.sunrise = -1L;
            return;
        }
        if (v <= -1.0) {
            this.state = 0;
            this.sunset = -1L;
            this.sunrise = -1L;
            return;
        }
        final float n5 = (float)(Math.acos(v) / 6.283185307179586);
        v = n5;
        Double.isNaN(v);
        this.sunset = Math.round((v + n2) * 8.64E7) + 946728000000L;
        v = n5;
        Double.isNaN(v);
        final long sunrise = Math.round((n2 - v) * 8.64E7) + 946728000000L;
        this.sunrise = sunrise;
        if (sunrise < n && this.sunset > n) {
            this.state = 0;
        }
        else {
            this.state = 1;
        }
    }
}
