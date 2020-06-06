.class public final Lno/tghack/gaiainvaders/GaiaInvadersView;
.super Landroid/view/SurfaceView;
.source "GaiaInvadersView.kt"

# interfaces
.implements Ljava/lang/Runnable;


# annotations
.annotation runtime Lkotlin/Metadata;
    bv = {
        0x1,
        0x0,
        0x3
    }
    d1 = {
        "\u0000\u0086\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0008\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\u0008\u0008\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0010\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0008\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\u0008\u0010.\u001a\u00020/H\u0002J\u0008\u00100\u001a\u00020/H\u0002J\u0010\u00101\u001a\u00020\"2\u0006\u00102\u001a\u000203H\u0016J\u0006\u00104\u001a\u00020/J\u0008\u00105\u001a\u00020/H\u0002J\u0006\u00106\u001a\u00020/J\u0008\u00107\u001a\u00020/H\u0016J\u0010\u00108\u001a\u00020/2\u0006\u00109\u001a\u00020\u0017H\u0002R\u001e\u0010\u0008\u001a\u0012\u0012\u0004\u0012\u00020\n0\tj\u0008\u0012\u0004\u0012\u00020\n`\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000c\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0012\u001a\u0012\u0012\u0004\u0012\u00020\u00130\tj\u0008\u0012\u0004\u0012\u00020\u0013`\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0014\u001a\u0012\u0012\u0004\u0012\u00020\u00150\tj\u0008\u0012\u0004\u0012\u00020\u0015`\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0011X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020 X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\"X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020%X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\"X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\'\u001a\u00020(X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020+X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\"X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006:"
    }
    d2 = {
        "Lno/tghack/gaiainvaders/GaiaInvadersView;",
        "Landroid/view/SurfaceView;",
        "Ljava/lang/Runnable;",
        "context",
        "Landroid/content/Context;",
        "size",
        "Landroid/graphics/Point;",
        "(Landroid/content/Context;Landroid/graphics/Point;)V",
        "bricks",
        "Ljava/util/ArrayList;",
        "Lno/tghack/gaiainvaders/DefenceBrick;",
        "Lkotlin/collections/ArrayList;",
        "canvas",
        "Landroid/graphics/Canvas;",
        "gameThread",
        "Ljava/lang/Thread;",
        "highScore",
        "",
        "invaders",
        "Lno/tghack/gaiainvaders/Invader;",
        "invadersBullets",
        "Lno/tghack/gaiainvaders/Bullet;",
        "lastMenaceTime",
        "",
        "lives",
        "magic",
        "maxInvaderBullets",
        "menaceInterval",
        "nextBullet",
        "numBricks",
        "numInvaders",
        "paint",
        "Landroid/graphics/Paint;",
        "paused",
        "",
        "playerBullet",
        "playerShip",
        "Lno/tghack/gaiainvaders/PlayerShip;",
        "playing",
        "prefs",
        "Landroid/content/SharedPreferences;",
        "score",
        "soundPlayer",
        "Lno/tghack/gaiainvaders/SoundPlayer;",
        "uhOrOh",
        "waves",
        "draw",
        "",
        "menacePlayer",
        "onTouchEvent",
        "motionEvent",
        "Landroid/view/MotionEvent;",
        "pause",
        "prepareLevel",
        "resume",
        "run",
        "update",
        "fps",
        "app_release"
    }
    k = 0x1
    mv = {
        0x1,
        0x1,
        0x10
    }
.end annotation


# instance fields
.field private _$_findViewCache:Ljava/util/HashMap;

.field private final bricks:Ljava/util/ArrayList;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Ljava/util/ArrayList<",
            "Lno/tghack/gaiainvaders/DefenceBrick;",
            ">;"
        }
    .end annotation
.end field

.field private canvas:Landroid/graphics/Canvas;

.field private final gameThread:Ljava/lang/Thread;

.field private highScore:I

.field private final invaders:Ljava/util/ArrayList;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Ljava/util/ArrayList<",
            "Lno/tghack/gaiainvaders/Invader;",
            ">;"
        }
    .end annotation
.end field

.field private final invadersBullets:Ljava/util/ArrayList;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Ljava/util/ArrayList<",
            "Lno/tghack/gaiainvaders/Bullet;",
            ">;"
        }
    .end annotation
.end field

.field private lastMenaceTime:J

.field private lives:I

.field private magic:J

.field private final maxInvaderBullets:I

.field private menaceInterval:J

.field private nextBullet:I

.field private numBricks:I

.field private numInvaders:I

.field private final paint:Landroid/graphics/Paint;

.field private paused:Z

.field private playerBullet:Lno/tghack/gaiainvaders/Bullet;

.field private playerShip:Lno/tghack/gaiainvaders/PlayerShip;

.field private playing:Z

.field private final prefs:Landroid/content/SharedPreferences;

.field private score:I

.field private final size:Landroid/graphics/Point;

.field private final soundPlayer:Lno/tghack/gaiainvaders/SoundPlayer;

.field private uhOrOh:Z

.field private waves:I


# direct methods
.method public constructor <init>(Landroid/content/Context;Landroid/graphics/Point;)V
    .locals 4

    const-string v0, "context"

    invoke-static {p1, v0}, Lkotlin/jvm/internal/Intrinsics;->checkParameterIsNotNull(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v0, "size"

    invoke-static {p2, v0}, Lkotlin/jvm/internal/Intrinsics;->checkParameterIsNotNull(Ljava/lang/Object;Ljava/lang/String;)V

    .line 11
    invoke-direct {p0, p1}, Landroid/view/SurfaceView;-><init>(Landroid/content/Context;)V

    iput-object p2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->size:Landroid/graphics/Point;

    .line 12
    new-instance p2, Lno/tghack/gaiainvaders/SoundPlayer;

    invoke-direct {p2, p1}, Lno/tghack/gaiainvaders/SoundPlayer;-><init>(Landroid/content/Context;)V

    iput-object p2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->soundPlayer:Lno/tghack/gaiainvaders/SoundPlayer;

    .line 13
    new-instance p2, Ljava/lang/Thread;

    move-object v0, p0

    check-cast v0, Ljava/lang/Runnable;

    invoke-direct {p2, v0}, Ljava/lang/Thread;-><init>(Ljava/lang/Runnable;)V

    iput-object p2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->gameThread:Ljava/lang/Thread;

    const/4 p2, 0x1

    .line 15
    iput-boolean p2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->paused:Z

    .line 16
    new-instance v0, Landroid/graphics/Canvas;

    invoke-direct {v0}, Landroid/graphics/Canvas;-><init>()V

    iput-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->canvas:Landroid/graphics/Canvas;

    .line 17
    new-instance v0, Landroid/graphics/Paint;

    invoke-direct {v0}, Landroid/graphics/Paint;-><init>()V

    iput-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->paint:Landroid/graphics/Paint;

    .line 19
    new-instance v0, Lno/tghack/gaiainvaders/PlayerShip;

    iget-object v1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->size:Landroid/graphics/Point;

    iget v1, v1, Landroid/graphics/Point;->x:I

    iget-object v2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->size:Landroid/graphics/Point;

    iget v2, v2, Landroid/graphics/Point;->y:I

    invoke-direct {v0, p1, v1, v2}, Lno/tghack/gaiainvaders/PlayerShip;-><init>(Landroid/content/Context;II)V

    iput-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerShip:Lno/tghack/gaiainvaders/PlayerShip;

    .line 20
    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    iput-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->invaders:Ljava/util/ArrayList;

    .line 22
    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    iput-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->bricks:Ljava/util/ArrayList;

    .line 28
    new-instance v0, Lno/tghack/gaiainvaders/Bullet;

    iget-object v1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->size:Landroid/graphics/Point;

    iget v1, v1, Landroid/graphics/Point;->y:I

    const/high16 v2, 0x44960000    # 1200.0f

    const/high16 v3, 0x42200000    # 40.0f

    invoke-direct {v0, v1, v2, v3}, Lno/tghack/gaiainvaders/Bullet;-><init>(IFF)V

    iput-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerBullet:Lno/tghack/gaiainvaders/Bullet;

    .line 31
    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    iput-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->invadersBullets:Ljava/util/ArrayList;

    const/16 v0, 0xa

    .line 33
    iput v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->maxInvaderBullets:I

    .line 36
    iput p2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->waves:I

    const/4 p2, 0x3

    .line 37
    iput p2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->lives:I

    const-wide/16 v0, 0x1337

    .line 38
    iput-wide v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->magic:J

    const/4 p2, 0x0

    const-string v0, "Gaia Invaders"

    .line 41
    invoke-virtual {p1, v0, p2}, Landroid/content/Context;->getSharedPreferences(Ljava/lang/String;I)Landroid/content/SharedPreferences;

    move-result-object p1

    const-string v0, "context.getSharedPrefere\u2026    Context.MODE_PRIVATE)"

    invoke-static {p1, v0}, Lkotlin/jvm/internal/Intrinsics;->checkExpressionValueIsNotNull(Ljava/lang/Object;Ljava/lang/String;)V

    iput-object p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->prefs:Landroid/content/SharedPreferences;

    .line 45
    iget-object p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->prefs:Landroid/content/SharedPreferences;

    const-string v0, "highScore"

    invoke-interface {p1, v0, p2}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    move-result p1

    iput p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->highScore:I

    const-wide/16 p1, 0x3e8

    .line 46
    iput-wide p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->menaceInterval:J

    .line 48
    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide p1

    iput-wide p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->lastMenaceTime:J

    return-void
.end method

.method private final draw()V
    .locals 7

    .line 361
    invoke-virtual {p0}, Lno/tghack/gaiainvaders/GaiaInvadersView;->getHolder()Landroid/view/SurfaceHolder;

    move-result-object v0

    const-string v1, "holder"

    invoke-static {v0, v1}, Lkotlin/jvm/internal/Intrinsics;->checkExpressionValueIsNotNull(Ljava/lang/Object;Ljava/lang/String;)V

    invoke-interface {v0}, Landroid/view/SurfaceHolder;->getSurface()Landroid/view/Surface;

    move-result-object v0

    const-string v1, "holder.surface"

    invoke-static {v0, v1}, Lkotlin/jvm/internal/Intrinsics;->checkExpressionValueIsNotNull(Ljava/lang/Object;Ljava/lang/String;)V

    invoke-virtual {v0}, Landroid/view/Surface;->isValid()Z

    move-result v0

    if-eqz v0, :cond_a

    .line 363
    invoke-virtual {p0}, Lno/tghack/gaiainvaders/GaiaInvadersView;->getHolder()Landroid/view/SurfaceHolder;

    move-result-object v0

    invoke-interface {v0}, Landroid/view/SurfaceHolder;->lockCanvas()Landroid/graphics/Canvas;

    move-result-object v0

    const-string v1, "holder.lockCanvas()"

    invoke-static {v0, v1}, Lkotlin/jvm/internal/Intrinsics;->checkExpressionValueIsNotNull(Ljava/lang/Object;Ljava/lang/String;)V

    iput-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->canvas:Landroid/graphics/Canvas;

    .line 366
    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->canvas:Landroid/graphics/Canvas;

    invoke-virtual {p0}, Lno/tghack/gaiainvaders/GaiaInvadersView;->getContext()Landroid/content/Context;

    move-result-object v1

    const v2, 0x7f05002a

    invoke-static {v1, v2}, Landroidx/core/content/ContextCompat;->getColor(Landroid/content/Context;I)I

    move-result v1

    invoke-virtual {v0, v1}, Landroid/graphics/Canvas;->drawColor(I)V

    .line 369
    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->paint:Landroid/graphics/Paint;

    invoke-virtual {p0}, Lno/tghack/gaiainvaders/GaiaInvadersView;->getContext()Landroid/content/Context;

    move-result-object v1

    const v2, 0x7f050027

    invoke-static {v1, v2}, Landroidx/core/content/ContextCompat;->getColor(Landroid/content/Context;I)I

    move-result v1

    invoke-virtual {v0, v1}, Landroid/graphics/Paint;->setColor(I)V

    .line 373
    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->canvas:Landroid/graphics/Canvas;

    iget-object v1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerShip:Lno/tghack/gaiainvaders/PlayerShip;

    invoke-virtual {v1}, Lno/tghack/gaiainvaders/PlayerShip;->getBitmap()Landroid/graphics/Bitmap;

    move-result-object v1

    iget-object v3, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerShip:Lno/tghack/gaiainvaders/PlayerShip;

    invoke-virtual {v3}, Lno/tghack/gaiainvaders/PlayerShip;->getPosition()Landroid/graphics/RectF;

    move-result-object v3

    iget v3, v3, Landroid/graphics/RectF;->left:F

    .line 374
    iget-object v4, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerShip:Lno/tghack/gaiainvaders/PlayerShip;

    invoke-virtual {v4}, Lno/tghack/gaiainvaders/PlayerShip;->getPosition()Landroid/graphics/RectF;

    move-result-object v4

    iget v4, v4, Landroid/graphics/RectF;->top:F

    .line 375
    iget-object v5, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->paint:Landroid/graphics/Paint;

    .line 373
    invoke-virtual {v0, v1, v3, v4, v5}, Landroid/graphics/Canvas;->drawBitmap(Landroid/graphics/Bitmap;FFLandroid/graphics/Paint;)V

    .line 378
    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->invaders:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;

    move-result-object v0

    :cond_0
    :goto_0
    invoke-interface {v0}, Ljava/util/Iterator;->hasNext()Z

    move-result v1

    if-eqz v1, :cond_4

    invoke-interface {v0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lno/tghack/gaiainvaders/Invader;

    .line 379
    invoke-virtual {v1}, Lno/tghack/gaiainvaders/Invader;->isVisible()Z

    move-result v3

    if-eqz v3, :cond_0

    .line 380
    iget-boolean v3, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->uhOrOh:Z

    if-eqz v3, :cond_2

    .line 381
    iget-object v3, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->canvas:Landroid/graphics/Canvas;

    .line 382
    sget-object v4, Lno/tghack/gaiainvaders/Invader;->Companion:Lno/tghack/gaiainvaders/Invader$Companion;

    invoke-virtual {v4}, Lno/tghack/gaiainvaders/Invader$Companion;->getBitmap1()Landroid/graphics/Bitmap;

    move-result-object v4

    if-nez v4, :cond_1

    invoke-static {}, Lkotlin/jvm/internal/Intrinsics;->throwNpe()V

    .line 383
    :cond_1
    invoke-virtual {v1}, Lno/tghack/gaiainvaders/Invader;->getPosition()Landroid/graphics/RectF;

    move-result-object v5

    iget v5, v5, Landroid/graphics/RectF;->left:F

    .line 384
    invoke-virtual {v1}, Lno/tghack/gaiainvaders/Invader;->getPosition()Landroid/graphics/RectF;

    move-result-object v1

    iget v1, v1, Landroid/graphics/RectF;->top:F

    .line 385
    iget-object v6, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->paint:Landroid/graphics/Paint;

    .line 381
    invoke-virtual {v3, v4, v5, v1, v6}, Landroid/graphics/Canvas;->drawBitmap(Landroid/graphics/Bitmap;FFLandroid/graphics/Paint;)V

    goto :goto_0

    .line 387
    :cond_2
    iget-object v3, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->canvas:Landroid/graphics/Canvas;

    .line 388
    sget-object v4, Lno/tghack/gaiainvaders/Invader;->Companion:Lno/tghack/gaiainvaders/Invader$Companion;

    invoke-virtual {v4}, Lno/tghack/gaiainvaders/Invader$Companion;->getBitmap2()Landroid/graphics/Bitmap;

    move-result-object v4

    if-nez v4, :cond_3

    invoke-static {}, Lkotlin/jvm/internal/Intrinsics;->throwNpe()V

    .line 389
    :cond_3
    invoke-virtual {v1}, Lno/tghack/gaiainvaders/Invader;->getPosition()Landroid/graphics/RectF;

    move-result-object v5

    iget v5, v5, Landroid/graphics/RectF;->left:F

    .line 390
    invoke-virtual {v1}, Lno/tghack/gaiainvaders/Invader;->getPosition()Landroid/graphics/RectF;

    move-result-object v1

    iget v1, v1, Landroid/graphics/RectF;->top:F

    .line 391
    iget-object v6, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->paint:Landroid/graphics/Paint;

    .line 387
    invoke-virtual {v3, v4, v5, v1, v6}, Landroid/graphics/Canvas;->drawBitmap(Landroid/graphics/Bitmap;FFLandroid/graphics/Paint;)V

    goto :goto_0

    .line 397
    :cond_4
    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->bricks:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;

    move-result-object v0

    :cond_5
    :goto_1
    invoke-interface {v0}, Ljava/util/Iterator;->hasNext()Z

    move-result v1

    if-eqz v1, :cond_6

    invoke-interface {v0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lno/tghack/gaiainvaders/DefenceBrick;

    .line 398
    invoke-virtual {v1}, Lno/tghack/gaiainvaders/DefenceBrick;->isVisible()Z

    move-result v3

    if-eqz v3, :cond_5

    .line 399
    iget-object v3, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->canvas:Landroid/graphics/Canvas;

    invoke-virtual {v1}, Lno/tghack/gaiainvaders/DefenceBrick;->getPosition()Landroid/graphics/RectF;

    move-result-object v1

    iget-object v4, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->paint:Landroid/graphics/Paint;

    invoke-virtual {v3, v1, v4}, Landroid/graphics/Canvas;->drawRect(Landroid/graphics/RectF;Landroid/graphics/Paint;)V

    goto :goto_1

    .line 404
    :cond_6
    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerBullet:Lno/tghack/gaiainvaders/Bullet;

    invoke-virtual {v0}, Lno/tghack/gaiainvaders/Bullet;->isActive()Z

    move-result v0

    if-eqz v0, :cond_7

    .line 405
    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->canvas:Landroid/graphics/Canvas;

    iget-object v1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerBullet:Lno/tghack/gaiainvaders/Bullet;

    invoke-virtual {v1}, Lno/tghack/gaiainvaders/Bullet;->getPosition()Landroid/graphics/RectF;

    move-result-object v1

    iget-object v3, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->paint:Landroid/graphics/Paint;

    invoke-virtual {v0, v1, v3}, Landroid/graphics/Canvas;->drawRect(Landroid/graphics/RectF;Landroid/graphics/Paint;)V

    .line 409
    :cond_7
    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->invadersBullets:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;

    move-result-object v0

    :cond_8
    :goto_2
    invoke-interface {v0}, Ljava/util/Iterator;->hasNext()Z

    move-result v1

    if-eqz v1, :cond_9

    invoke-interface {v0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lno/tghack/gaiainvaders/Bullet;

    .line 410
    invoke-virtual {v1}, Lno/tghack/gaiainvaders/Bullet;->isActive()Z

    move-result v3

    if-eqz v3, :cond_8

    .line 411
    iget-object v3, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->canvas:Landroid/graphics/Canvas;

    invoke-virtual {v1}, Lno/tghack/gaiainvaders/Bullet;->getPosition()Landroid/graphics/RectF;

    move-result-object v1

    iget-object v4, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->paint:Landroid/graphics/Paint;

    invoke-virtual {v3, v1, v4}, Landroid/graphics/Canvas;->drawRect(Landroid/graphics/RectF;Landroid/graphics/Paint;)V

    goto :goto_2

    .line 417
    :cond_9
    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->paint:Landroid/graphics/Paint;

    invoke-virtual {p0}, Lno/tghack/gaiainvaders/GaiaInvadersView;->getContext()Landroid/content/Context;

    move-result-object v1

    invoke-static {v1, v2}, Landroidx/core/content/ContextCompat;->getColor(Landroid/content/Context;I)I

    move-result v1

    invoke-virtual {v0, v1}, Landroid/graphics/Paint;->setColor(I)V

    .line 418
    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->paint:Landroid/graphics/Paint;

    const/high16 v1, 0x42480000    # 50.0f

    invoke-virtual {v0, v1}, Landroid/graphics/Paint;->setTextSize(F)V

    .line 419
    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->canvas:Landroid/graphics/Canvas;

    invoke-virtual {v0}, Landroid/graphics/Canvas;->getWidth()I

    move-result v0

    int-to-float v0, v0

    const/high16 v1, 0x40000000    # 2.0f

    div-float/2addr v0, v1

    .line 420
    iget-object v1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->paint:Landroid/graphics/Paint;

    sget-object v2, Landroid/graphics/Paint$Align;->CENTER:Landroid/graphics/Paint$Align;

    invoke-virtual {v1, v2}, Landroid/graphics/Paint;->setTextAlign(Landroid/graphics/Paint$Align;)V

    .line 421
    iget-object v1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->canvas:Landroid/graphics/Canvas;

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "Score: "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v3, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->score:I

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v3, "      Lives: "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v3, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->lives:I

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v3, "       "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, "Wave: "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    .line 422
    iget v3, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->waves:I

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v3, "       HI: "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v3, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->highScore:I

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    const/high16 v3, 0x42960000    # 75.0f

    iget-object v4, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->paint:Landroid/graphics/Paint;

    .line 421
    invoke-virtual {v1, v2, v0, v3, v4}, Landroid/graphics/Canvas;->drawText(Ljava/lang/String;FFLandroid/graphics/Paint;)V

    .line 425
    invoke-virtual {p0}, Lno/tghack/gaiainvaders/GaiaInvadersView;->getHolder()Landroid/view/SurfaceHolder;

    move-result-object v0

    iget-object v1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->canvas:Landroid/graphics/Canvas;

    invoke-interface {v0, v1}, Landroid/view/SurfaceHolder;->unlockCanvasAndPost(Landroid/graphics/Canvas;)V

    :cond_a
    return-void
.end method

.method private final menacePlayer()V
    .locals 2

    .line 127
    iget-boolean v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->uhOrOh:Z

    if-eqz v0, :cond_0

    .line 129
    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->soundPlayer:Lno/tghack/gaiainvaders/SoundPlayer;

    sget-object v1, Lno/tghack/gaiainvaders/SoundPlayer;->Companion:Lno/tghack/gaiainvaders/SoundPlayer$Companion;

    invoke-virtual {v1}, Lno/tghack/gaiainvaders/SoundPlayer$Companion;->getUhID()I

    move-result v1

    invoke-virtual {v0, v1}, Lno/tghack/gaiainvaders/SoundPlayer;->playSound(I)V

    goto :goto_0

    .line 133
    :cond_0
    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->soundPlayer:Lno/tghack/gaiainvaders/SoundPlayer;

    sget-object v1, Lno/tghack/gaiainvaders/SoundPlayer;->Companion:Lno/tghack/gaiainvaders/SoundPlayer$Companion;

    invoke-virtual {v1}, Lno/tghack/gaiainvaders/SoundPlayer$Companion;->getOhID()I

    move-result v1

    invoke-virtual {v0, v1}, Lno/tghack/gaiainvaders/SoundPlayer;->playSound(I)V

    .line 137
    :goto_0
    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v0

    iput-wide v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->lastMenaceTime:J

    .line 139
    iget-boolean v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->uhOrOh:Z

    xor-int/lit8 v0, v0, 0x1

    iput-boolean v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->uhOrOh:Z

    return-void
.end method

.method private final prepareLevel()V
    .locals 15

    .line 53
    sget-object v0, Lno/tghack/gaiainvaders/Invader;->Companion:Lno/tghack/gaiainvaders/Invader$Companion;

    const/4 v1, 0x0

    invoke-virtual {v0, v1}, Lno/tghack/gaiainvaders/Invader$Companion;->setNumberOfInvaders(I)V

    .line 54
    iput v1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->numInvaders:I

    const/4 v0, 0x0

    :goto_0
    const/16 v2, 0xa

    if-gt v0, v2, :cond_1

    const/4 v8, 0x5

    const/4 v9, 0x0

    :goto_1
    if-gt v9, v8, :cond_0

    .line 57
    iget-object v10, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->invaders:Ljava/util/ArrayList;

    .line 58
    new-instance v11, Lno/tghack/gaiainvaders/Invader;

    .line 59
    invoke-virtual {p0}, Lno/tghack/gaiainvaders/GaiaInvadersView;->getContext()Landroid/content/Context;

    move-result-object v3

    const-string v2, "context"

    invoke-static {v3, v2}, Lkotlin/jvm/internal/Intrinsics;->checkExpressionValueIsNotNull(Ljava/lang/Object;Ljava/lang/String;)V

    .line 67
    iget v2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->numInvaders:I

    add-int/lit8 v2, v2, 0x1

    iput v2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->numInvaders:I

    add-int/lit8 v9, v9, 0x1

    goto :goto_1

    :cond_0
    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    .line 72
    :cond_1
    iput v1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->numBricks:I

    const/4 v0, 0x4

    const/4 v8, 0x0

    :goto_2
    if-gt v8, v0, :cond_4

    const/16 v9, 0xe

    const/4 v10, 0x0

    :goto_3
    if-gt v10, v9, :cond_3

    const/16 v11, 0x8

    const/4 v12, 0x0

    :goto_4
    if-gt v12, v11, :cond_2

    .line 76
    iget-object v13, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->bricks:Ljava/util/ArrayList;

    .line 77
    new-instance v14, Lno/tghack/gaiainvaders/DefenceBrick;

    .line 81
    iget-object v2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->size:Landroid/graphics/Point;

    iget v6, v2, Landroid/graphics/Point;->x:I

    .line 82
    iget-object v2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->size:Landroid/graphics/Point;

    iget v7, v2, Landroid/graphics/Point;->y:I

    move-object v2, v14

    move v3, v12

    move v4, v10

    move v5, v8

    .line 77
    invoke-direct/range {v2 .. v7}, Lno/tghack/gaiainvaders/DefenceBrick;-><init>(IIIII)V

    .line 76
    invoke-virtual {v13, v14}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    .line 86
    iget v2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->numBricks:I

    add-int/lit8 v2, v2, 0x1

    iput v2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->numBricks:I

    add-int/lit8 v12, v12, 0x1

    goto :goto_4

    :cond_2
    add-int/lit8 v10, v10, 0x1

    goto :goto_3

    :cond_3
    add-int/lit8 v8, v8, 0x1

    goto :goto_2

    .line 92
    :cond_4
    iget v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->maxInvaderBullets:I

    :goto_5
    if-ge v1, v0, :cond_5

    .line 93
    iget-object v2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->invadersBullets:Ljava/util/ArrayList;

    new-instance v9, Lno/tghack/gaiainvaders/Bullet;

    iget-object v3, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->size:Landroid/graphics/Point;

    iget v4, v3, Landroid/graphics/Point;->y:I

    const/4 v5, 0x0

    const/4 v6, 0x0

    const/4 v7, 0x6

    const/4 v8, 0x0

    move-object v3, v9

    invoke-direct/range {v3 .. v8}, Lno/tghack/gaiainvaders/Bullet;-><init>(IFFILkotlin/jvm/internal/DefaultConstructorMarker;)V

    invoke-virtual {v2, v9}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    add-int/lit8 v1, v1, 0x1

    goto :goto_5

    :cond_5
    return-void
.end method

.method private final update(J)V
    .locals 12

    .line 147
    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerShip:Lno/tghack/gaiainvaders/PlayerShip;

    invoke-virtual {v0, p1, p2}, Lno/tghack/gaiainvaders/PlayerShip;->update(J)V

    .line 154
    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->invaders:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;

    move-result-object v0

    const/4 v1, 0x0

    const/4 v2, 0x0

    :cond_0
    :goto_0
    invoke-interface {v0}, Ljava/util/Iterator;->hasNext()Z

    move-result v3

    const/4 v4, 0x2

    const/4 v5, 0x1

    if-eqz v3, :cond_3

    invoke-interface {v0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Lno/tghack/gaiainvaders/Invader;

    .line 156
    invoke-virtual {v3}, Lno/tghack/gaiainvaders/Invader;->isVisible()Z

    move-result v6

    if-eqz v6, :cond_0

    .line 158
    invoke-virtual {v3, p1, p2}, Lno/tghack/gaiainvaders/Invader;->update(J)V

    .line 161
    iget-object v6, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerShip:Lno/tghack/gaiainvaders/PlayerShip;

    invoke-virtual {v6}, Lno/tghack/gaiainvaders/PlayerShip;->getPosition()Landroid/graphics/RectF;

    move-result-object v6

    iget v6, v6, Landroid/graphics/RectF;->left:F

    .line 162
    iget-object v7, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerShip:Lno/tghack/gaiainvaders/PlayerShip;

    invoke-virtual {v7}, Lno/tghack/gaiainvaders/PlayerShip;->getWidth()F

    move-result v7

    .line 163
    iget v8, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->waves:I

    .line 161
    invoke-virtual {v3, v6, v7, v8}, Lno/tghack/gaiainvaders/Invader;->takeAim(FFI)Z

    move-result v6

    if-eqz v6, :cond_1

    .line 166
    iget-object v6, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->invadersBullets:Ljava/util/ArrayList;

    iget v7, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->nextBullet:I

    invoke-virtual {v6, v7}, Ljava/util/ArrayList;->get(I)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Lno/tghack/gaiainvaders/Bullet;

    invoke-virtual {v3}, Lno/tghack/gaiainvaders/Invader;->getPosition()Landroid/graphics/RectF;

    move-result-object v7

    iget v7, v7, Landroid/graphics/RectF;->left:F

    .line 167
    invoke-virtual {v3}, Lno/tghack/gaiainvaders/Invader;->getWidth()F

    move-result v8

    int-to-float v4, v4

    div-float/2addr v8, v4

    add-float/2addr v7, v8

    .line 168
    invoke-virtual {v3}, Lno/tghack/gaiainvaders/Invader;->getPosition()Landroid/graphics/RectF;

    move-result-object v4

    iget v4, v4, Landroid/graphics/RectF;->top:F

    iget-object v8, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerBullet:Lno/tghack/gaiainvaders/Bullet;

    invoke-virtual {v8}, Lno/tghack/gaiainvaders/Bullet;->getDown()I

    move-result v8

    .line 166
    invoke-virtual {v6, v7, v4, v8}, Lno/tghack/gaiainvaders/Bullet;->shoot(FFI)Z

    move-result v4

    if-eqz v4, :cond_1

    .line 172
    iget v4, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->nextBullet:I

    add-int/2addr v4, v5

    iput v4, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->nextBullet:I

    .line 175
    iget v4, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->nextBullet:I

    iget v6, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->maxInvaderBullets:I

    if-ne v4, v6, :cond_1

    .line 180
    iput v1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->nextBullet:I

    .line 188
    :cond_1
    invoke-virtual {v3}, Lno/tghack/gaiainvaders/Invader;->getPosition()Landroid/graphics/RectF;

    move-result-object v4

    iget v4, v4, Landroid/graphics/RectF;->left:F

    .line 187
    iget-object v6, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->size:Landroid/graphics/Point;

    iget v6, v6, Landroid/graphics/Point;->x:I

    int-to-float v6, v6

    invoke-virtual {v3}, Lno/tghack/gaiainvaders/Invader;->getWidth()F

    move-result v7

    sub-float/2addr v6, v7

    cmpl-float v4, v4, v6

    if-gtz v4, :cond_2

    invoke-virtual {v3}, Lno/tghack/gaiainvaders/Invader;->getPosition()Landroid/graphics/RectF;

    move-result-object v3

    iget v3, v3, Landroid/graphics/RectF;->left:F

    int-to-float v4, v1

    cmpg-float v3, v3, v4

    if-gez v3, :cond_0

    :cond_2
    const/4 v2, 0x1

    goto/16 :goto_0

    .line 196
    :cond_3
    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerBullet:Lno/tghack/gaiainvaders/Bullet;

    invoke-virtual {v0}, Lno/tghack/gaiainvaders/Bullet;->isActive()Z

    move-result v0

    if-eqz v0, :cond_4

    .line 197
    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerBullet:Lno/tghack/gaiainvaders/Bullet;

    invoke-virtual {v0, p1, p2}, Lno/tghack/gaiainvaders/Bullet;->update(J)V

    .line 202
    :cond_4
    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->invadersBullets:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;

    move-result-object v0

    :cond_5
    :goto_1
    invoke-interface {v0}, Ljava/util/Iterator;->hasNext()Z

    move-result v3

    if-eqz v3, :cond_6

    invoke-interface {v0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Lno/tghack/gaiainvaders/Bullet;

    .line 203
    invoke-virtual {v3}, Lno/tghack/gaiainvaders/Bullet;->isActive()Z

    move-result v6

    if-eqz v6, :cond_5

    .line 204
    invoke-virtual {v3, p1, p2}, Lno/tghack/gaiainvaders/Bullet;->update(J)V

    goto :goto_1

    :cond_6
    if-eqz v2, :cond_8

    .line 212
    iget-object p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->invaders:Ljava/util/ArrayList;

    invoke-virtual {p1}, Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;

    move-result-object p1

    const/4 p2, 0x0

    :cond_7
    :goto_2
    invoke-interface {p1}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_9

    invoke-interface {p1}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lno/tghack/gaiainvaders/Invader;

    .line 213
    iget v2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->waves:I

    invoke-virtual {v0, v2}, Lno/tghack/gaiainvaders/Invader;->dropDownAndReverse(I)V

    .line 215
    invoke-virtual {v0}, Lno/tghack/gaiainvaders/Invader;->getPosition()Landroid/graphics/RectF;

    move-result-object v2

    iget v2, v2, Landroid/graphics/RectF;->bottom:F

    iget-object v3, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->size:Landroid/graphics/Point;

    iget v3, v3, Landroid/graphics/Point;->y:I

    int-to-float v3, v3

    cmpl-float v2, v2, v3

    if-ltz v2, :cond_7

    invoke-virtual {v0}, Lno/tghack/gaiainvaders/Invader;->isVisible()Z

    move-result v0

    if-eqz v0, :cond_7

    const/4 p2, 0x1

    goto :goto_2

    :cond_8
    const/4 p2, 0x0

    .line 222
    :cond_9
    iget-object p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerBullet:Lno/tghack/gaiainvaders/Bullet;

    invoke-virtual {p1}, Lno/tghack/gaiainvaders/Bullet;->getPosition()Landroid/graphics/RectF;

    move-result-object p1

    iget p1, p1, Landroid/graphics/RectF;->bottom:F

    int-to-float v0, v1

    cmpg-float p1, p1, v0

    if-gez p1, :cond_a

    .line 223
    iget-object p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerBullet:Lno/tghack/gaiainvaders/Bullet;

    invoke-virtual {p1, v1}, Lno/tghack/gaiainvaders/Bullet;->setActive(Z)V

    .line 227
    :cond_a
    iget-object p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->invadersBullets:Ljava/util/ArrayList;

    invoke-virtual {p1}, Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;

    move-result-object p1

    :cond_b
    :goto_3
    invoke-interface {p1}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_c

    invoke-interface {p1}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lno/tghack/gaiainvaders/Bullet;

    .line 228
    invoke-virtual {v0}, Lno/tghack/gaiainvaders/Bullet;->getPosition()Landroid/graphics/RectF;

    move-result-object v2

    iget v2, v2, Landroid/graphics/RectF;->top:F

    iget-object v3, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->size:Landroid/graphics/Point;

    iget v3, v3, Landroid/graphics/Point;->y:I

    int-to-float v3, v3

    cmpl-float v2, v2, v3

    if-lez v2, :cond_b

    .line 229
    invoke-virtual {v0, v1}, Lno/tghack/gaiainvaders/Bullet;->setActive(Z)V

    goto :goto_3

    .line 234
    :cond_c
    iget-object p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerBullet:Lno/tghack/gaiainvaders/Bullet;

    invoke-virtual {p1}, Lno/tghack/gaiainvaders/Bullet;->isActive()Z

    move-result p1

    if-eqz p1, :cond_e

    .line 235
    iget-object p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->invaders:Ljava/util/ArrayList;

    invoke-virtual {p1}, Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;

    move-result-object p1

    :cond_d
    invoke-interface {p1}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_e

    invoke-interface {p1}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lno/tghack/gaiainvaders/Invader;

    .line 236
    invoke-virtual {v0}, Lno/tghack/gaiainvaders/Invader;->isVisible()Z

    move-result v2

    if-eqz v2, :cond_d

    .line 237
    iget-object v2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerBullet:Lno/tghack/gaiainvaders/Bullet;

    invoke-virtual {v2}, Lno/tghack/gaiainvaders/Bullet;->getPosition()Landroid/graphics/RectF;

    move-result-object v2

    invoke-virtual {v0}, Lno/tghack/gaiainvaders/Invader;->getPosition()Landroid/graphics/RectF;

    move-result-object v3

    invoke-static {v2, v3}, Landroid/graphics/RectF;->intersects(Landroid/graphics/RectF;Landroid/graphics/RectF;)Z

    move-result v2

    if-eqz v2, :cond_d

    .line 238
    invoke-virtual {v0, v1}, Lno/tghack/gaiainvaders/Invader;->setVisible(Z)V

    .line 240
    iget-object p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->soundPlayer:Lno/tghack/gaiainvaders/SoundPlayer;

    sget-object v0, Lno/tghack/gaiainvaders/SoundPlayer;->Companion:Lno/tghack/gaiainvaders/SoundPlayer$Companion;

    invoke-virtual {v0}, Lno/tghack/gaiainvaders/SoundPlayer$Companion;->getInvaderExplodeID()I

    move-result v0

    invoke-virtual {p1, v0}, Lno/tghack/gaiainvaders/SoundPlayer;->playSound(I)V

    .line 241
    iget-object p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerBullet:Lno/tghack/gaiainvaders/Bullet;

    invoke-virtual {p1, v1}, Lno/tghack/gaiainvaders/Bullet;->setActive(Z)V

    .line 242
    sget-object p1, Lno/tghack/gaiainvaders/Invader;->Companion:Lno/tghack/gaiainvaders/Invader$Companion;

    invoke-virtual {p1}, Lno/tghack/gaiainvaders/Invader$Companion;->getNumberOfInvaders()I

    move-result v0

    add-int/lit8 v0, v0, -0x0

    invoke-virtual {p1, v0}, Lno/tghack/gaiainvaders/Invader$Companion;->setNumberOfInvaders(I)V

    .line 243
    iget p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->score:I

    add-int/lit8 p1, p1, 0xa

    iput p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->score:I

    .line 244
    iget p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->score:I

    iget v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->highScore:I

    if-le p1, v0, :cond_e

    .line 245
    iput p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->highScore:I

    .line 257
    :cond_e
    sget-object p1, Lno/tghack/gaiainvaders/Invader;->Companion:Lno/tghack/gaiainvaders/Invader$Companion;

    invoke-virtual {p1}, Lno/tghack/gaiainvaders/Invader$Companion;->getNumberOfInvaders()I

    move-result p1

    if-nez p1, :cond_10

    .line 258
    iput-boolean v5, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->paused:Z

    .line 259
    iget p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->lives:I

    add-int/2addr p1, v5

    iput p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->lives:I

    .line 260
    iget-object p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->invaders:Ljava/util/ArrayList;

    invoke-virtual {p1}, Ljava/util/ArrayList;->clear()V

    .line 261
    iget-object p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->bricks:Ljava/util/ArrayList;

    invoke-virtual {p1}, Ljava/util/ArrayList;->clear()V

    .line 262
    iget-object p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->invadersBullets:Ljava/util/ArrayList;

    invoke-virtual {p1}, Ljava/util/ArrayList;->clear()V

    .line 265
    iget p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->waves:I

    const/16 v0, 0x3e8

    if-ne p1, v0, :cond_f

    .line 266
    iput-boolean v5, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->paused:Z

    .line 268
    invoke-virtual {p0}, Lno/tghack/gaiainvaders/GaiaInvadersView;->getResources()Landroid/content/res/Resources;

    move-result-object p1

    const v0, 0x7f070067

    invoke-static {p1, v0}, Landroid/graphics/BitmapFactory;->decodeResource(Landroid/content/res/Resources;I)Landroid/graphics/Bitmap;

    move-result-object p1

    .line 269
    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->size:Landroid/graphics/Point;

    iget v0, v0, Landroid/graphics/Point;->x:I

    iget-object v2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->size:Landroid/graphics/Point;

    iget v2, v2, Landroid/graphics/Point;->y:I

    sget-object v3, Landroid/graphics/Bitmap$Config;->ARGB_8888:Landroid/graphics/Bitmap$Config;

    invoke-static {v0, v2, v3}, Landroid/graphics/Bitmap;->createBitmap(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;

    move-result-object v0

    .line 270
    new-instance v2, Landroid/graphics/Canvas;

    invoke-direct {v2, v0}, Landroid/graphics/Canvas;-><init>(Landroid/graphics/Bitmap;)V

    iput-object v2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->canvas:Landroid/graphics/Canvas;

    .line 271
    invoke-virtual {p0}, Lno/tghack/gaiainvaders/GaiaInvadersView;->getHolder()Landroid/view/SurfaceHolder;

    move-result-object v0

    invoke-interface {v0}, Landroid/view/SurfaceHolder;->lockCanvas()Landroid/graphics/Canvas;

    move-result-object v0

    const-string v2, "holder.lockCanvas()"

    invoke-static {v0, v2}, Lkotlin/jvm/internal/Intrinsics;->checkExpressionValueIsNotNull(Ljava/lang/Object;Ljava/lang/String;)V

    iput-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->canvas:Landroid/graphics/Canvas;

    .line 273
    new-instance v0, Landroid/graphics/RectF;

    const/high16 v2, 0x42c80000    # 100.0f

    iget-object v3, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->size:Landroid/graphics/Point;

    iget v3, v3, Landroid/graphics/Point;->x:I

    int-to-float v3, v3

    const/high16 v6, 0x428c0000    # 70.0f

    sub-float/2addr v3, v6

    iget-object v7, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->size:Landroid/graphics/Point;

    iget v7, v7, Landroid/graphics/Point;->y:I

    div-int/2addr v7, v4

    int-to-float v7, v7

    const/high16 v8, 0x43200000    # 160.0f

    sub-float/2addr v7, v8

    invoke-direct {v0, v6, v2, v3, v7}, Landroid/graphics/RectF;-><init>(FFFF)V

    .line 274
    iget-object v2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->canvas:Landroid/graphics/Canvas;

    const/4 v3, 0x0

    invoke-virtual {v2, p1, v3, v0, v3}, Landroid/graphics/Canvas;->drawBitmap(Landroid/graphics/Bitmap;Landroid/graphics/Rect;Landroid/graphics/RectF;Landroid/graphics/Paint;)V

    .line 276
    iget-object p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->paint:Landroid/graphics/Paint;

    invoke-virtual {p0}, Lno/tghack/gaiainvaders/GaiaInvadersView;->getContext()Landroid/content/Context;

    move-result-object v0

    const v2, 0x7f050028

    invoke-static {v0, v2}, Landroidx/core/content/ContextCompat;->getColor(Landroid/content/Context;I)I

    move-result v0

    invoke-virtual {p1, v0}, Landroid/graphics/Paint;->setColor(I)V

    .line 277
    iget-object p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->paint:Landroid/graphics/Paint;

    const/high16 v0, 0x42a00000    # 80.0f

    invoke-virtual {p1, v0}, Landroid/graphics/Paint;->setTextSize(F)V

    .line 278
    iget-object p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->canvas:Landroid/graphics/Canvas;

    invoke-virtual {p1}, Landroid/graphics/Canvas;->getWidth()I

    move-result p1

    int-to-float p1, p1

    const/high16 v0, 0x40000000    # 2.0f

    div-float/2addr p1, v0

    .line 279
    iget-object v2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->canvas:Landroid/graphics/Canvas;

    invoke-virtual {v2}, Landroid/graphics/Canvas;->getHeight()I

    move-result v2

    int-to-float v2, v2

    div-float/2addr v2, v0

    iget-object v3, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->paint:Landroid/graphics/Paint;

    invoke-virtual {v3}, Landroid/graphics/Paint;->descent()F

    move-result v3

    iget-object v6, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->paint:Landroid/graphics/Paint;

    invoke-virtual {v6}, Landroid/graphics/Paint;->ascent()F

    move-result v6

    add-float/2addr v3, v6

    div-float/2addr v3, v0

    sub-float/2addr v2, v3

    .line 280
    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->paint:Landroid/graphics/Paint;

    sget-object v3, Landroid/graphics/Paint$Align;->CENTER:Landroid/graphics/Paint$Align;

    invoke-virtual {v0, v3}, Landroid/graphics/Paint;->setTextAlign(Landroid/graphics/Paint$Align;)V

    .line 283
    iget-wide v6, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->magic:J

    invoke-static {v6, v7}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v0

    check-cast v0, Ljava/lang/Number;

    const-string v3, "M6GzLMKMB7TO7qCwYIE6tWjstepv0Fa6B3yyCrRtFbNRb2+VVVCqDuDO6UWY14LEu9Ac3A2sVaKG4Thk1s1j0g=="

    const-string v6, "ClcYYh9brZTGyTFG4kge7w=="

    invoke-static {v3, v0, v6}, Lno/tghack/gaiainvaders/EncryptionKt;->decrypt(Ljava/lang/String;Ljava/lang/Number;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    .line 284
    iget-object v3, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->canvas:Landroid/graphics/Canvas;

    iget-object v6, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->paint:Landroid/graphics/Paint;

    const-string v7, "Congrats!!!"

    invoke-virtual {v3, v7, p1, v2, v6}, Landroid/graphics/Canvas;->drawText(Ljava/lang/String;FFLandroid/graphics/Paint;)V

    .line 285
    iget-object v3, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->paint:Landroid/graphics/Paint;

    const/high16 v6, 0x42200000    # 40.0f

    invoke-virtual {v3, v6}, Landroid/graphics/Paint;->setTextSize(F)V

    .line 286
    iget-object v3, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->canvas:Landroid/graphics/Canvas;

    invoke-static {v0}, Ljava/lang/String;->valueOf(Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v0

    const/16 v6, 0x64

    int-to-float v6, v6

    add-float/2addr v2, v6

    iget-object v6, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->paint:Landroid/graphics/Paint;

    invoke-virtual {v3, v0, p1, v2, v6}, Landroid/graphics/Canvas;->drawText(Ljava/lang/String;FFLandroid/graphics/Paint;)V

    .line 287
    invoke-virtual {p0}, Lno/tghack/gaiainvaders/GaiaInvadersView;->getHolder()Landroid/view/SurfaceHolder;

    move-result-object p1

    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->canvas:Landroid/graphics/Canvas;

    invoke-interface {p1, v0}, Landroid/view/SurfaceHolder;->unlockCanvasAndPost(Landroid/graphics/Canvas;)V

    .line 289
    invoke-virtual {p0}, Lno/tghack/gaiainvaders/GaiaInvadersView;->pause()V

    .line 292
    :cond_f
    iget-wide v2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->magic:J

    iget p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->waves:I

    mul-int/lit8 p1, p1, 0x2a

    div-int/2addr p1, v4

    int-to-long v6, p1

    const-wide v8, 0xdeadbeefL

    iget p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->lives:I

    int-to-long v10, p1

    mul-long v10, v10, v8

    add-long/2addr v6, v10

    add-long/2addr v2, v6

    iput-wide v2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->magic:J

    .line 294
    invoke-direct {p0}, Lno/tghack/gaiainvaders/GaiaInvadersView;->prepareLevel()V

    .line 295
    iget p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->waves:I

    add-int/2addr p1, v5

    iput p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->waves:I

    .line 300
    :cond_10
    iget-object p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->invadersBullets:Ljava/util/ArrayList;

    invoke-virtual {p1}, Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;

    move-result-object p1

    :cond_11
    invoke-interface {p1}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_13

    invoke-interface {p1}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lno/tghack/gaiainvaders/Bullet;

    .line 301
    invoke-virtual {v0}, Lno/tghack/gaiainvaders/Bullet;->isActive()Z

    move-result v2

    if-eqz v2, :cond_11

    .line 302
    iget-object v2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->bricks:Ljava/util/ArrayList;

    invoke-virtual {v2}, Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;

    move-result-object v2

    :cond_12
    :goto_4
    invoke-interface {v2}, Ljava/util/Iterator;->hasNext()Z

    move-result v3

    if-eqz v3, :cond_11

    invoke-interface {v2}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Lno/tghack/gaiainvaders/DefenceBrick;

    .line 303
    invoke-virtual {v3}, Lno/tghack/gaiainvaders/DefenceBrick;->isVisible()Z

    move-result v4

    if-eqz v4, :cond_12

    .line 304
    invoke-virtual {v0}, Lno/tghack/gaiainvaders/Bullet;->getPosition()Landroid/graphics/RectF;

    move-result-object v4

    invoke-virtual {v3}, Lno/tghack/gaiainvaders/DefenceBrick;->getPosition()Landroid/graphics/RectF;

    move-result-object v6

    invoke-static {v4, v6}, Landroid/graphics/RectF;->intersects(Landroid/graphics/RectF;Landroid/graphics/RectF;)Z

    move-result v4

    if-eqz v4, :cond_12

    .line 306
    invoke-virtual {v0, v1}, Lno/tghack/gaiainvaders/Bullet;->setActive(Z)V

    .line 307
    invoke-virtual {v3, v1}, Lno/tghack/gaiainvaders/DefenceBrick;->setVisible(Z)V

    .line 308
    iget-object v3, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->soundPlayer:Lno/tghack/gaiainvaders/SoundPlayer;

    sget-object v4, Lno/tghack/gaiainvaders/SoundPlayer;->Companion:Lno/tghack/gaiainvaders/SoundPlayer$Companion;

    invoke-virtual {v4}, Lno/tghack/gaiainvaders/SoundPlayer$Companion;->getDamageShelterID()I

    move-result v4

    invoke-virtual {v3, v4}, Lno/tghack/gaiainvaders/SoundPlayer;->playSound(I)V

    goto :goto_4

    .line 317
    :cond_13
    iget-object p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerBullet:Lno/tghack/gaiainvaders/Bullet;

    invoke-virtual {p1}, Lno/tghack/gaiainvaders/Bullet;->isActive()Z

    move-result p1

    if-eqz p1, :cond_15

    .line 318
    iget-object p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->bricks:Ljava/util/ArrayList;

    invoke-virtual {p1}, Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;

    move-result-object p1

    :cond_14
    :goto_5
    invoke-interface {p1}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_15

    invoke-interface {p1}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lno/tghack/gaiainvaders/DefenceBrick;

    .line 319
    invoke-virtual {v0}, Lno/tghack/gaiainvaders/DefenceBrick;->isVisible()Z

    move-result v2

    if-eqz v2, :cond_14

    .line 320
    iget-object v2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerBullet:Lno/tghack/gaiainvaders/Bullet;

    invoke-virtual {v2}, Lno/tghack/gaiainvaders/Bullet;->getPosition()Landroid/graphics/RectF;

    move-result-object v2

    invoke-virtual {v0}, Lno/tghack/gaiainvaders/DefenceBrick;->getPosition()Landroid/graphics/RectF;

    move-result-object v3

    invoke-static {v2, v3}, Landroid/graphics/RectF;->intersects(Landroid/graphics/RectF;Landroid/graphics/RectF;)Z

    move-result v2

    if-eqz v2, :cond_14

    .line 322
    iget-object v2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerBullet:Lno/tghack/gaiainvaders/Bullet;

    invoke-virtual {v2, v1}, Lno/tghack/gaiainvaders/Bullet;->setActive(Z)V

    .line 323
    invoke-virtual {v0, v1}, Lno/tghack/gaiainvaders/DefenceBrick;->setVisible(Z)V

    .line 324
    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->soundPlayer:Lno/tghack/gaiainvaders/SoundPlayer;

    sget-object v2, Lno/tghack/gaiainvaders/SoundPlayer;->Companion:Lno/tghack/gaiainvaders/SoundPlayer$Companion;

    invoke-virtual {v2}, Lno/tghack/gaiainvaders/SoundPlayer$Companion;->getDamageShelterID()I

    move-result v2

    invoke-virtual {v0, v2}, Lno/tghack/gaiainvaders/SoundPlayer;->playSound(I)V

    goto :goto_5

    .line 331
    :cond_15
    iget-object p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->invadersBullets:Ljava/util/ArrayList;

    invoke-virtual {p1}, Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;

    move-result-object p1

    :cond_16
    invoke-interface {p1}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_17

    invoke-interface {p1}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lno/tghack/gaiainvaders/Bullet;

    .line 332
    invoke-virtual {v0}, Lno/tghack/gaiainvaders/Bullet;->isActive()Z

    move-result v2

    if-eqz v2, :cond_16

    .line 333
    iget-object v2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerShip:Lno/tghack/gaiainvaders/PlayerShip;

    invoke-virtual {v2}, Lno/tghack/gaiainvaders/PlayerShip;->getPosition()Landroid/graphics/RectF;

    move-result-object v2

    invoke-virtual {v0}, Lno/tghack/gaiainvaders/Bullet;->getPosition()Landroid/graphics/RectF;

    move-result-object v3

    invoke-static {v2, v3}, Landroid/graphics/RectF;->intersects(Landroid/graphics/RectF;Landroid/graphics/RectF;)Z

    move-result v2

    if-eqz v2, :cond_16

    .line 334
    invoke-virtual {v0, v1}, Lno/tghack/gaiainvaders/Bullet;->setActive(Z)V

    .line 335
    iget v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->lives:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->lives:I

    .line 336
    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->soundPlayer:Lno/tghack/gaiainvaders/SoundPlayer;

    sget-object v2, Lno/tghack/gaiainvaders/SoundPlayer;->Companion:Lno/tghack/gaiainvaders/SoundPlayer$Companion;

    invoke-virtual {v2}, Lno/tghack/gaiainvaders/SoundPlayer$Companion;->getPlayerExplodeID()I

    move-result v2

    invoke-virtual {v0, v2}, Lno/tghack/gaiainvaders/SoundPlayer;->playSound(I)V

    .line 339
    iget v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->lives:I

    if-nez v0, :cond_16

    const/4 p2, 0x1

    :cond_17
    if-eqz p2, :cond_18

    .line 348
    iput-boolean v5, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->paused:Z

    const/4 p1, 0x3

    .line 349
    iput p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->lives:I

    .line 350
    iput v1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->score:I

    .line 351
    iput v5, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->waves:I

    .line 352
    iget-object p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->invaders:Ljava/util/ArrayList;

    invoke-virtual {p1}, Ljava/util/ArrayList;->clear()V

    .line 353
    iget-object p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->bricks:Ljava/util/ArrayList;

    invoke-virtual {p1}, Ljava/util/ArrayList;->clear()V

    .line 354
    iget-object p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->invadersBullets:Ljava/util/ArrayList;

    invoke-virtual {p1}, Ljava/util/ArrayList;->clear()V

    .line 355
    invoke-direct {p0}, Lno/tghack/gaiainvaders/GaiaInvadersView;->prepareLevel()V

    :cond_18
    return-void
.end method


# virtual methods
.method public _$_clearFindViewByIdCache()V
    .locals 1

    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->_$_findViewCache:Ljava/util/HashMap;

    if-eqz v0, :cond_0

    invoke-virtual {v0}, Ljava/util/HashMap;->clear()V

    :cond_0
    return-void
.end method

.method public _$_findCachedViewById(I)Landroid/view/View;
    .locals 2

    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->_$_findViewCache:Ljava/util/HashMap;

    if-nez v0, :cond_0

    new-instance v0, Ljava/util/HashMap;

    invoke-direct {v0}, Ljava/util/HashMap;-><init>()V

    iput-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->_$_findViewCache:Ljava/util/HashMap;

    :cond_0
    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->_$_findViewCache:Ljava/util/HashMap;

    invoke-static {p1}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/View;

    if-nez v0, :cond_1

    invoke-virtual {p0, p1}, Landroid/view/View;->findViewById(I)Landroid/view/View;

    move-result-object v0

    iget-object v1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->_$_findViewCache:Ljava/util/HashMap;

    invoke-static {p1}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object p1

    invoke-virtual {v1, p1, v0}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    :cond_1
    return-object v0
.end method

.method public onTouchEvent(Landroid/view/MotionEvent;)Z
    .locals 5

    const-string v0, "motionEvent"

    invoke-static {p1, v0}, Lkotlin/jvm/internal/Intrinsics;->checkParameterIsNotNull(Ljava/lang/Object;Ljava/lang/String;)V

    .line 466
    invoke-virtual {p1}, Landroid/view/MotionEvent;->getAction()I

    move-result v0

    and-int/lit16 v0, v0, 0xff

    const/4 v1, 0x0

    const/4 v2, 0x2

    const/4 v3, 0x1

    if-eqz v0, :cond_1

    if-eq v0, v3, :cond_0

    if-eq v0, v2, :cond_1

    const/4 v4, 0x5

    if-eq v0, v4, :cond_1

    const/4 v2, 0x6

    if-eq v0, v2, :cond_0

    goto/16 :goto_1

    .line 499
    :cond_0
    invoke-virtual {p1}, Landroid/view/MotionEvent;->getY()F

    move-result p1

    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->size:Landroid/graphics/Point;

    iget v0, v0, Landroid/graphics/Point;->y:I

    iget-object v2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->size:Landroid/graphics/Point;

    iget v2, v2, Landroid/graphics/Point;->y:I

    div-int/lit8 v2, v2, 0xa

    sub-int/2addr v0, v2

    int-to-float v0, v0

    cmpl-float p1, p1, v0

    if-lez p1, :cond_4

    .line 500
    iget-object p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerShip:Lno/tghack/gaiainvaders/PlayerShip;

    invoke-virtual {p1, v1}, Lno/tghack/gaiainvaders/PlayerShip;->setMoving(I)V

    goto :goto_1

    .line 473
    :cond_1
    iput-boolean v1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->paused:Z

    .line 475
    invoke-virtual {p1}, Landroid/view/MotionEvent;->getY()F

    move-result v0

    iget-object v1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->size:Landroid/graphics/Point;

    iget v1, v1, Landroid/graphics/Point;->y:I

    iget-object v4, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->size:Landroid/graphics/Point;

    iget v4, v4, Landroid/graphics/Point;->y:I

    div-int/lit8 v4, v4, 0x8

    sub-int/2addr v1, v4

    int-to-float v1, v1

    cmpl-float v0, v0, v1

    if-lez v0, :cond_3

    .line 476
    invoke-virtual {p1}, Landroid/view/MotionEvent;->getX()F

    move-result v0

    iget-object v1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->size:Landroid/graphics/Point;

    iget v1, v1, Landroid/graphics/Point;->x:I

    div-int/2addr v1, v2

    int-to-float v1, v1

    cmpl-float v0, v0, v1

    if-lez v0, :cond_2

    .line 477
    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerShip:Lno/tghack/gaiainvaders/PlayerShip;

    invoke-virtual {v0, v2}, Lno/tghack/gaiainvaders/PlayerShip;->setMoving(I)V

    goto :goto_0

    .line 479
    :cond_2
    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerShip:Lno/tghack/gaiainvaders/PlayerShip;

    invoke-virtual {v0, v3}, Lno/tghack/gaiainvaders/PlayerShip;->setMoving(I)V

    .line 484
    :cond_3
    :goto_0
    invoke-virtual {p1}, Landroid/view/MotionEvent;->getY()F

    move-result p1

    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->size:Landroid/graphics/Point;

    iget v0, v0, Landroid/graphics/Point;->y:I

    iget-object v1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->size:Landroid/graphics/Point;

    iget v1, v1, Landroid/graphics/Point;->y:I

    div-int/lit8 v1, v1, 0x8

    sub-int/2addr v0, v1

    int-to-float v0, v0

    cmpg-float p1, p1, v0

    if-gez p1, :cond_4

    .line 486
    iget-object p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerBullet:Lno/tghack/gaiainvaders/Bullet;

    .line 487
    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerShip:Lno/tghack/gaiainvaders/PlayerShip;

    invoke-virtual {v0}, Lno/tghack/gaiainvaders/PlayerShip;->getPosition()Landroid/graphics/RectF;

    move-result-object v0

    iget v0, v0, Landroid/graphics/RectF;->left:F

    iget-object v1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerShip:Lno/tghack/gaiainvaders/PlayerShip;

    invoke-virtual {v1}, Lno/tghack/gaiainvaders/PlayerShip;->getWidth()F

    move-result v1

    const/high16 v2, 0x40000000    # 2.0f

    div-float/2addr v1, v2

    add-float/2addr v0, v1

    .line 488
    iget-object v1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerShip:Lno/tghack/gaiainvaders/PlayerShip;

    invoke-virtual {v1}, Lno/tghack/gaiainvaders/PlayerShip;->getPosition()Landroid/graphics/RectF;

    move-result-object v1

    iget v1, v1, Landroid/graphics/RectF;->top:F

    .line 489
    iget-object v2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playerBullet:Lno/tghack/gaiainvaders/Bullet;

    invoke-virtual {v2}, Lno/tghack/gaiainvaders/Bullet;->getUp()I

    move-result v2

    .line 486
    invoke-virtual {p1, v0, v1, v2}, Lno/tghack/gaiainvaders/Bullet;->shoot(FFI)Z

    move-result p1

    if-eqz p1, :cond_4

    .line 491
    iget-object p1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->soundPlayer:Lno/tghack/gaiainvaders/SoundPlayer;

    sget-object v0, Lno/tghack/gaiainvaders/SoundPlayer;->Companion:Lno/tghack/gaiainvaders/SoundPlayer$Companion;

    invoke-virtual {v0}, Lno/tghack/gaiainvaders/SoundPlayer$Companion;->getShootID()I

    move-result v0

    invoke-virtual {p1, v0}, Lno/tghack/gaiainvaders/SoundPlayer;->playSound(I)V

    :cond_4
    :goto_1
    return v3
.end method

.method public final pause()V
    .locals 4

    const/4 v0, 0x0

    .line 432
    iput-boolean v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playing:Z

    .line 434
    :try_start_0
    iget-object v1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->gameThread:Ljava/lang/Thread;

    invoke-virtual {v1}, Ljava/lang/Thread;->join()V
    :try_end_0
    .catch Ljava/lang/InterruptedException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    :catch_0
    const-string v1, "Error:"

    const-string v2, "joining thread"

    .line 436
    invoke-static {v1, v2}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    .line 439
    :goto_0
    invoke-virtual {p0}, Lno/tghack/gaiainvaders/GaiaInvadersView;->getContext()Landroid/content/Context;

    move-result-object v1

    const-string v2, "Gaia Invaders"

    invoke-virtual {v1, v2, v0}, Landroid/content/Context;->getSharedPreferences(Ljava/lang/String;I)Landroid/content/SharedPreferences;

    move-result-object v1

    const-string v2, "highScore"

    .line 443
    invoke-interface {v1, v2, v0}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    move-result v0

    .line 445
    iget v3, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->highScore:I

    if-le v3, v0, :cond_0

    .line 446
    invoke-interface {v1}, Landroid/content/SharedPreferences;->edit()Landroid/content/SharedPreferences$Editor;

    move-result-object v0

    .line 449
    iget v1, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->highScore:I

    .line 448
    invoke-interface {v0, v2, v1}, Landroid/content/SharedPreferences$Editor;->putInt(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;

    .line 451
    invoke-interface {v0}, Landroid/content/SharedPreferences$Editor;->apply()V

    :cond_0
    return-void
.end method

.method public final resume()V
    .locals 1

    const/4 v0, 0x1

    .line 458
    iput-boolean v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playing:Z

    .line 459
    invoke-direct {p0}, Lno/tghack/gaiainvaders/GaiaInvadersView;->prepareLevel()V

    .line 460
    iget-object v0, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->gameThread:Ljava/lang/Thread;

    invoke-virtual {v0}, Ljava/lang/Thread;->start()V

    return-void
.end method

.method public run()V
    .locals 9

    const-wide/16 v0, 0x0

    .line 101
    :cond_0
    :goto_0
    iget-boolean v2, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->playing:Z

    if-eqz v2, :cond_3

    .line 104
    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v2

    .line 107
    iget-boolean v4, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->paused:Z

    if-nez v4, :cond_1

    .line 108
    invoke-direct {p0, v0, v1}, Lno/tghack/gaiainvaders/GaiaInvadersView;->update(J)V

    .line 112
    :cond_1
    invoke-direct {p0}, Lno/tghack/gaiainvaders/GaiaInvadersView;->draw()V

    .line 115
    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v4

    sub-long/2addr v4, v2

    const-wide/16 v6, 0x1

    cmp-long v8, v4, v6

    if-ltz v8, :cond_2

    const/16 v0, 0x3e8

    int-to-long v0, v0

    .line 117
    div-long/2addr v0, v4

    .line 121
    :cond_2
    iget-boolean v4, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->paused:Z

    if-nez v4, :cond_0

    iget-wide v4, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->lastMenaceTime:J

    sub-long/2addr v2, v4

    iget-wide v4, p0, Lno/tghack/gaiainvaders/GaiaInvadersView;->menaceInterval:J

    cmp-long v6, v2, v4

    if-lez v6, :cond_0

    .line 122
    invoke-direct {p0}, Lno/tghack/gaiainvaders/GaiaInvadersView;->menacePlayer()V

    goto :goto_0

    :cond_3
    return-void
.end method
