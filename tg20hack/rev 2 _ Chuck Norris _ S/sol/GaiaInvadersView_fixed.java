// 
// Decompiled by Procyon v0.5.36
// 

package no.tghack.gaiainvaders;

import android.content.SharedPreferences$Editor;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Rect;
import android.graphics.Bitmap$Config;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.graphics.Bitmap;
import java.util.Iterator;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.graphics.Paint$Align;
import androidx.core.content.ContextCompat;
import kotlin.jvm.internal.Intrinsics;
import android.content.Context;
import android.graphics.Point;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Canvas;
import java.util.ArrayList;
import java.util.HashMap;
import kotlin.Metadata;
import android.view.SurfaceView;

@Metadata(bv = { 1, 0, 3 }, d1 = { "\u0000\u0086\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\b\u0010.\u001a\u00020/H\u0002J\b\u00100\u001a\u00020/H\u0002J\u0010\u00101\u001a\u00020\"2\u0006\u00102\u001a\u000203H\u0016J\u0006\u00104\u001a\u00020/J\b\u00105\u001a\u00020/H\u0002J\u0006\u00106\u001a\u00020/J\b\u00107\u001a\u00020/H\u0016J\u0010\u00108\u001a\u00020/2\u0006\u00109\u001a\u00020\u0017H\u0002R\u001e\u0010\b\u001a\u0012\u0012\u0004\u0012\u00020\n0\tj\b\u0012\u0004\u0012\u00020\n`\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\u0012\u001a\u0012\u0012\u0004\u0012\u00020\u00130\tj\b\u0012\u0004\u0012\u00020\u0013`\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u0014\u001a\u0012\u0012\u0004\u0012\u00020\u00150\tj\b\u0012\u0004\u0012\u00020\u0015`\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0017X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0011X\u0082D¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0017X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020 X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\"X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0015X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020%X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\"X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020(X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020+X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\"X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006:" }, d2 = { "Lno/tghack/gaiainvaders/GaiaInvadersView;", "Landroid/view/SurfaceView;", "Ljava/lang/Runnable;", "context", "Landroid/content/Context;", "size", "Landroid/graphics/Point;", "(Landroid/content/Context;Landroid/graphics/Point;)V", "bricks", "Ljava/util/ArrayList;", "Lno/tghack/gaiainvaders/DefenceBrick;", "Lkotlin/collections/ArrayList;", "canvas", "Landroid/graphics/Canvas;", "gameThread", "Ljava/lang/Thread;", "highScore", "", "invaders", "Lno/tghack/gaiainvaders/Invader;", "invadersBullets", "Lno/tghack/gaiainvaders/Bullet;", "lastMenaceTime", "", "lives", "magic", "maxInvaderBullets", "menaceInterval", "nextBullet", "numBricks", "numInvaders", "paint", "Landroid/graphics/Paint;", "paused", "", "playerBullet", "playerShip", "Lno/tghack/gaiainvaders/PlayerShip;", "playing", "prefs", "Landroid/content/SharedPreferences;", "score", "soundPlayer", "Lno/tghack/gaiainvaders/SoundPlayer;", "uhOrOh", "waves", "draw", "", "menacePlayer", "onTouchEvent", "motionEvent", "Landroid/view/MotionEvent;", "pause", "prepareLevel", "resume", "run", "update", "fps", "app_release" }, k = 1, mv = { 1, 1, 16 })
public final class GaiaInvadersView extends SurfaceView implements Runnable
{
    private HashMap _$_findViewCache;
    private final ArrayList<DefenceBrick> bricks;
    private Canvas canvas;
    private final Thread gameThread;
    private int highScore;
    private final ArrayList<Invader> invaders;
    private final ArrayList<Bullet> invadersBullets;
    private long lastMenaceTime;
    private int lives;
    private long magic;
    private final int maxInvaderBullets;
    private long menaceInterval;
    private int nextBullet;
    private int numBricks;
    private int numInvaders;
    private final Paint paint;
    private boolean paused;
    private Bullet playerBullet;
    private PlayerShip playerShip;
    private boolean playing;
    private final SharedPreferences prefs;
    private int score;
    private final Point size;
    private final SoundPlayer soundPlayer;
    private boolean uhOrOh;
    private int waves;
    
    public GaiaInvadersView(final Context context, final Point size) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(size, "size");
        super(context);
        this.size = size;
        this.soundPlayer = new SoundPlayer(context);
        this.gameThread = new Thread(this);
        this.paused = true;
        this.canvas = new Canvas();
        this.paint = new Paint();
        this.playerShip = new PlayerShip(context, this.size.x, this.size.y);
        this.invaders = new ArrayList<Invader>();
        this.bricks = new ArrayList<DefenceBrick>();
        this.playerBullet = new Bullet(this.size.y, 1200.0f, 40.0f);
        this.invadersBullets = new ArrayList<Bullet>();
        this.maxInvaderBullets = 10;
        this.waves = 1;
        this.lives = 3;
        this.magic = 4919L;
        final SharedPreferences sharedPreferences = context.getSharedPreferences("Gaia Invaders", 0);
        Intrinsics.checkExpressionValueIsNotNull(sharedPreferences, "context.getSharedPrefere\u2026    Context.MODE_PRIVATE)");
        this.prefs = sharedPreferences;
        this.highScore = this.prefs.getInt("highScore", 0);
        this.menaceInterval = 1000L;
        this.lastMenaceTime = System.currentTimeMillis();
    }
    
    private final void draw() {
        final SurfaceHolder holder = this.getHolder();
        Intrinsics.checkExpressionValueIsNotNull(holder, "holder");
        final Surface surface = holder.getSurface();
        Intrinsics.checkExpressionValueIsNotNull(surface, "holder.surface");
        if (surface.isValid()) {
            final Canvas lockCanvas = this.getHolder().lockCanvas();
            Intrinsics.checkExpressionValueIsNotNull(lockCanvas, "holder.lockCanvas()");
            (this.canvas = lockCanvas).drawColor(ContextCompat.getColor(this.getContext(), 2131034154));
            this.paint.setColor(ContextCompat.getColor(this.getContext(), 2131034151));
            this.canvas.drawBitmap(this.playerShip.getBitmap(), this.playerShip.getPosition().left, this.playerShip.getPosition().top, this.paint);
            for (final Invader invader : this.invaders) {
                if (invader.isVisible()) {
                    if (this.uhOrOh) {
                        final Canvas canvas = this.canvas;
                        final Bitmap bitmap1 = Invader.Companion.getBitmap1();
                        if (bitmap1 == null) {
                            Intrinsics.throwNpe();
                        }
                        canvas.drawBitmap(bitmap1, invader.getPosition().left, invader.getPosition().top, this.paint);
                    }
                    else {
                        final Canvas canvas2 = this.canvas;
                        final Bitmap bitmap2 = Invader.Companion.getBitmap2();
                        if (bitmap2 == null) {
                            Intrinsics.throwNpe();
                        }
                        canvas2.drawBitmap(bitmap2, invader.getPosition().left, invader.getPosition().top, this.paint);
                    }
                }
            }
            for (final DefenceBrick defenceBrick : this.bricks) {
                if (defenceBrick.isVisible()) {
                    this.canvas.drawRect(defenceBrick.getPosition(), this.paint);
                }
            }
            if (this.playerBullet.isActive()) {
                this.canvas.drawRect(this.playerBullet.getPosition(), this.paint);
            }
            for (final Bullet bullet : this.invadersBullets) {
                if (bullet.isActive()) {
                    this.canvas.drawRect(bullet.getPosition(), this.paint);
                }
            }
            this.paint.setColor(ContextCompat.getColor(this.getContext(), 2131034151));
            this.paint.setTextSize(50.0f);
            final float n = this.canvas.getWidth() / 2.0f;
            this.paint.setTextAlign(Paint$Align.CENTER);
            final Canvas canvas3 = this.canvas;
            final StringBuilder sb = new StringBuilder();
            sb.append("Score: ");
            sb.append(this.score);
            sb.append("      Lives: ");
            sb.append(this.lives);
            sb.append("       ");
            sb.append("Wave: ");
            sb.append(this.waves);
            sb.append("       HI: ");
            sb.append(this.highScore);
            canvas3.drawText(sb.toString(), n, 75.0f, this.paint);
            this.getHolder().unlockCanvasAndPost(this.canvas);
        }
    }
    
    private final void menacePlayer() {
        if (this.uhOrOh) {
            this.soundPlayer.playSound(SoundPlayer.Companion.getUhID());
        }
        else {
            this.soundPlayer.playSound(SoundPlayer.Companion.getOhID());
        }
        this.lastMenaceTime = System.currentTimeMillis();
        this.uhOrOh ^= true;
    }
    
    private final void prepareLevel() {
        final Invader.Companion companion = Invader.Companion;
        final int n = 0;
        companion.setNumberOfInvaders(0);
        this.numInvaders = 0;
        for (int i = 0; i <= 10; ++i) {
            for (int j = 0; j <= 5; ++j) {
                final ArrayList<Invader> invaders = this.invaders;
                final Context context = this.getContext();
                Intrinsics.checkExpressionValueIsNotNull(context, "context");
                ++this.numInvaders;
            }
        }
        this.numBricks = 0;
        for (int k = 0; k <= 4; ++k) {
            for (int l = 0; l <= 14; ++l) {
                for (int n2 = 0; n2 <= 8; ++n2) {
                    this.bricks.add(new DefenceBrick(n2, l, k, this.size.x, this.size.y));
                    ++this.numBricks;
                }
            }
        }
        for (int maxInvaderBullets = this.maxInvaderBullets, n3 = n; n3 < maxInvaderBullets; ++n3) {
            this.invadersBullets.add(new Bullet(this.size.y, 0.0f, 0.0f, 6, null));
        }
    }
    
    private final void update(final long n) {
        this.playerShip.update(n);
        final Iterator<Invader> iterator = this.invaders.iterator();
        boolean b = false;
        while (iterator.hasNext()) {
            final Invader invader = iterator.next();
            if (invader.isVisible()) {
                invader.update(n);
                if (invader.takeAim(this.playerShip.getPosition().left, this.playerShip.getWidth(), this.waves) && this.invadersBullets.get(this.nextBullet).shoot(invader.getPosition().left + invader.getWidth() / 2, invader.getPosition().top, this.playerBullet.getDown())) {
                    ++this.nextBullet;
                    if (this.nextBullet == this.maxInvaderBullets) {
                        this.nextBullet = 0;
                    }
                }
                if (invader.getPosition().left <= this.size.x - invader.getWidth() && invader.getPosition().left >= 0) {
                    continue;
                }
                b = true;
            }
        }
        if (this.playerBullet.isActive()) {
            this.playerBullet.update(n);
        }
        for (final Bullet bullet : this.invadersBullets) {
            if (bullet.isActive()) {
                bullet.update(n);
            }
        }
        int n3;
        if (b) {
            final Iterator<Invader> iterator3 = this.invaders.iterator();
            int n2 = 0;
            while (true) {
                n3 = n2;
                if (!iterator3.hasNext()) {
                    break;
                }
                final Invader invader2 = iterator3.next();
                invader2.dropDownAndReverse(this.waves);
                if (invader2.getPosition().bottom < this.size.y || !invader2.isVisible()) {
                    continue;
                }
                n2 = 1;
            }
        }
        else {
            n3 = 0;
        }
        if (this.playerBullet.getPosition().bottom < 0) {
            this.playerBullet.setActive(false);
        }
        for (final Bullet bullet2 : this.invadersBullets) {
            if (bullet2.getPosition().top > this.size.y) {
                bullet2.setActive(false);
            }
        }
        if (this.playerBullet.isActive()) {
            for (final Invader invader3 : this.invaders) {
                if (invader3.isVisible() && RectF.intersects(this.playerBullet.getPosition(), invader3.getPosition())) {
                    invader3.setVisible(false);
                    this.soundPlayer.playSound(SoundPlayer.Companion.getInvaderExplodeID());
                    this.playerBullet.setActive(false);
                    final Invader.Companion companion = Invader.Companion;
                    companion.setNumberOfInvaders(companion.getNumberOfInvaders() - 1);
                    this.score += 10;
                    final int score = this.score;
                    if (score > this.highScore) {
                        this.highScore = score;
                        break;
                    }
                    break;
                }
            }
        }
        if (Invader.Companion.getNumberOfInvaders() == 0) {
            this.paused = true;
            ++this.lives;
            this.invaders.clear();
            this.bricks.clear();
            this.invadersBullets.clear();
            if (this.waves == 1000) {
                this.paused = true;
                final Bitmap decodeResource = BitmapFactory.decodeResource(this.getResources(), 2131165287);
                this.canvas = new Canvas(Bitmap.createBitmap(this.size.x, this.size.y, Bitmap$Config.ARGB_8888));
                final Canvas lockCanvas = this.getHolder().lockCanvas();
                Intrinsics.checkExpressionValueIsNotNull(lockCanvas, "holder.lockCanvas()");
                (this.canvas = lockCanvas).drawBitmap(decodeResource, (Rect)null, new RectF(70.0f, 100.0f, this.size.x - 70.0f, this.size.y / 2 - 160.0f), (Paint)null);
                this.paint.setColor(ContextCompat.getColor(this.getContext(), 2131034152));
                this.paint.setTextSize(80.0f);
                final float n4 = this.canvas.getWidth() / 2.0f;
                final float n5 = this.canvas.getHeight() / 2.0f - (this.paint.descent() + this.paint.ascent()) / 2.0f;
                this.paint.setTextAlign(Paint$Align.CENTER);
                final String decrypt = EncryptionKt.decrypt("M6GzLMKMB7TO7qCwYIE6tWjstepv0Fa6B3yyCrRtFbNRb2+VVVCqDuDO6UWY14LEu9Ac3A2sVaKG4Thk1s1j0g==", this.magic, "ClcYYh9brZTGyTFG4kge7w==");
                this.canvas.drawText("Congrats!!!", n4, n5, this.paint);
                this.paint.setTextSize(40.0f);
                this.canvas.drawText(String.valueOf(decrypt), n4, n5 + 100, this.paint);
                this.getHolder().unlockCanvasAndPost(this.canvas);
                this.pause();
            }
            this.magic += this.waves * 42 / 2 + this.lives * 0xDEADBEEFL;
            this.prepareLevel();
            ++this.waves;
        }
        for (final Bullet bullet3 : this.invadersBullets) {
            if (bullet3.isActive()) {
                for (final DefenceBrick defenceBrick : this.bricks) {
                    if (defenceBrick.isVisible() && RectF.intersects(bullet3.getPosition(), defenceBrick.getPosition())) {
                        bullet3.setActive(false);
                        defenceBrick.setVisible(false);
                        this.soundPlayer.playSound(SoundPlayer.Companion.getDamageShelterID());
                    }
                }
            }
        }
        if (this.playerBullet.isActive()) {
            for (final DefenceBrick defenceBrick2 : this.bricks) {
                if (defenceBrick2.isVisible() && RectF.intersects(this.playerBullet.getPosition(), defenceBrick2.getPosition())) {
                    this.playerBullet.setActive(false);
                    defenceBrick2.setVisible(false);
                    this.soundPlayer.playSound(SoundPlayer.Companion.getDamageShelterID());
                }
            }
        }
        final Iterator<Bullet> iterator9 = this.invadersBullets.iterator();
        int n6;
        while (true) {
            n6 = n3;
            if (!iterator9.hasNext()) {
                break;
            }
            final Bullet bullet4 = iterator9.next();
            if (!bullet4.isActive() || !RectF.intersects(this.playerShip.getPosition(), bullet4.getPosition())) {
                continue;
            }
            bullet4.setActive(false);
            --this.lives;
            this.soundPlayer.playSound(SoundPlayer.Companion.getPlayerExplodeID());
            if (this.lives == 0) {
                n6 = 1;
                break;
            }
        }
        if (n6 != 0) {
            this.paused = true;
            this.lives = 3;
            this.score = 0;
            this.waves = 1;
            this.invaders.clear();
            this.bricks.clear();
            this.invadersBullets.clear();
            this.prepareLevel();
        }
    }
    
    public void _$_clearFindViewByIdCache() {
        final HashMap $_findViewCache = this._$_findViewCache;
        if ($_findViewCache != null) {
            $_findViewCache.clear();
        }
    }
    
    public View _$_findCachedViewById(final int n) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View viewById;
        if ((viewById = this._$_findViewCache.get(n)) == null) {
            viewById = ((View)this).findViewById(n);
            this._$_findViewCache.put(n, viewById);
        }
        return viewById;
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        Intrinsics.checkParameterIsNotNull(motionEvent, "motionEvent");
        final int n = motionEvent.getAction() & 0xFF;
        Label_0082: {
            if (n != 0) {
                if (n != 1) {
                    if (n == 2 || n == 5) {
                        break Label_0082;
                    }
                    if (n != 6) {
                        return true;
                    }
                }
                if (motionEvent.getY() > this.size.y - this.size.y / 10) {
                    this.playerShip.setMoving(0);
                    return true;
                }
                return true;
            }
        }
        this.paused = false;
        if (motionEvent.getY() > this.size.y - this.size.y / 8) {
            if (motionEvent.getX() > this.size.x / 2) {
                this.playerShip.setMoving(2);
            }
            else {
                this.playerShip.setMoving(1);
            }
        }
        if (motionEvent.getY() < this.size.y - this.size.y / 8 && this.playerBullet.shoot(this.playerShip.getPosition().left + this.playerShip.getWidth() / 2.0f, this.playerShip.getPosition().top, this.playerBullet.getUp())) {
            this.soundPlayer.playSound(SoundPlayer.Companion.getShootID());
        }
        return true;
    }
    
    public final void pause() {
        this.playing = false;
        try {
            this.gameThread.join();
        }
        catch (InterruptedException ex) {
            Log.e("Error:", "joining thread");
        }
        final SharedPreferences sharedPreferences = this.getContext().getSharedPreferences("Gaia Invaders", 0);
        if (this.highScore > sharedPreferences.getInt("highScore", 0)) {
            final SharedPreferences$Editor edit = sharedPreferences.edit();
            edit.putInt("highScore", this.highScore);
            edit.apply();
        }
    }
    
    public final void resume() {
        this.playing = true;
        this.prepareLevel();
        this.gameThread.start();
    }
    
    public void run() {
        long n = 0L;
        while (this.playing) {
            final long currentTimeMillis = System.currentTimeMillis();
            if (!this.paused) {
                this.update(n);
            }
            this.draw();
            final long n2 = System.currentTimeMillis() - currentTimeMillis;
            long n3 = n;
            if (n2 >= 1L) {
                n3 = 1000 / n2;
            }
            n = n3;
            if (!this.paused) {
                n = n3;
                if (currentTimeMillis - this.lastMenaceTime <= this.menaceInterval) {
                    continue;
                }
                this.menacePlayer();
                n = n3;
            }
        }
    }
}
