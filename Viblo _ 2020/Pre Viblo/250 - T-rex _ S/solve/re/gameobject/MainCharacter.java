// 
// Decompiled by Procyon v0.5.36
// 

package gameobject;

import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.Graphics;
import java.net.MalformedURLException;
import java.applet.Applet;
import java.net.URL;
import util.Resource;
import java.applet.AudioClip;
import java.awt.image.BufferedImage;
import util.Animation;
import java.awt.Rectangle;

public class MainCharacter
{
    public static final int LAND_POSY = 80;
    public static final float GRAVITY = 0.4f;
    private static final int NORMAL_RUN = 0;
    private static final int JUMPING = 1;
    private static final int DOWN_RUN = 2;
    private static final int DEATH = 3;
    private float posY;
    private float posX;
    private float speedX;
    private float speedY;
    private Rectangle rectBound;
    public int score;
    private int state;
    private Animation normalRunAnim;
    private BufferedImage jumping;
    private Animation downRunAnim;
    private BufferedImage deathImage;
    private AudioClip jumpSound;
    private AudioClip deadSound;
    private AudioClip scoreUpSound;
    
    public MainCharacter() {
        this.score = 0;
        this.state = 0;
        this.posX = 50.0f;
        this.posY = 80.0f;
        this.rectBound = new Rectangle();
        (this.normalRunAnim = new Animation(90)).addFrame(Resource.getResouceImage("data/main-character1.png"));
        this.normalRunAnim.addFrame(Resource.getResouceImage("data/main-character2.png"));
        this.jumping = Resource.getResouceImage("data/main-character3.png");
        (this.downRunAnim = new Animation(90)).addFrame(Resource.getResouceImage("data/main-character5.png"));
        this.downRunAnim.addFrame(Resource.getResouceImage("data/main-character6.png"));
        this.deathImage = Resource.getResouceImage("data/main-character4.png");
        try {
            this.jumpSound = Applet.newAudioClip(new URL("file", "", "data/jump.wav"));
            this.deadSound = Applet.newAudioClip(new URL("file", "", "data/dead.wav"));
            this.scoreUpSound = Applet.newAudioClip(new URL("file", "", "data/scoreup.wav"));
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    
    public float getSpeedX() {
        return this.speedX;
    }
    
    public void setSpeedX(final int speedX) {
        this.speedX = (float)speedX;
    }
    
    public void draw(final Graphics g) {
        switch (this.state) {
            case 0: {
                g.drawImage(this.normalRunAnim.getFrame(), (int)this.posX, (int)this.posY, null);
                break;
            }
            case 1: {
                g.drawImage(this.jumping, (int)this.posX, (int)this.posY, null);
                break;
            }
            case 2: {
                g.drawImage(this.downRunAnim.getFrame(), (int)this.posX, (int)(this.posY + 20.0f), null);
                break;
            }
            case 3: {
                g.drawImage(this.deathImage, (int)this.posX, (int)this.posY, null);
                break;
            }
        }
    }
    
    public void update() {
        this.normalRunAnim.updateFrame();
        this.downRunAnim.updateFrame();
        if (this.posY >= 80.0f) {
            this.posY = 80.0f;
            if (this.state != 2) {
                this.state = 0;
            }
        }
        else {
            this.speedY += 0.4f;
            this.posY += this.speedY;
        }
    }
    
    public void jump() {
        if (this.posY >= 80.0f) {
            if (this.jumpSound != null) {
                this.jumpSound.play();
            }
            this.speedY = -7.5f;
            this.posY += this.speedY;
            this.state = 1;
        }
    }
    
    public void down(final boolean isDown) {
        if (this.state == 1) {
            return;
        }
        if (isDown) {
            this.state = 2;
        }
        else {
            this.state = 0;
        }
    }
    
    public Rectangle getBound() {
        this.rectBound = new Rectangle();
        if (this.state == 2) {
            this.rectBound.x = (int)this.posX + 5;
            this.rectBound.y = (int)this.posY + 20;
            this.rectBound.width = this.downRunAnim.getFrame().getWidth() - 10;
            this.rectBound.height = this.downRunAnim.getFrame().getHeight();
        }
        else {
            this.rectBound.x = (int)this.posX + 5;
            this.rectBound.y = (int)this.posY;
            this.rectBound.width = this.normalRunAnim.getFrame().getWidth() - 10;
            this.rectBound.height = this.normalRunAnim.getFrame().getHeight();
        }
        return this.rectBound;
    }
    
    public void dead(final boolean isDeath) {
        if (isDeath) {
            this.state = 3;
        }
        else {
            this.state = 0;
        }
    }
    
    public void reset() {
        this.posY = 80.0f;
    }
    
    public void playDeadSound() {
        this.deadSound.play();
    }
    
    public void upScore() {
        this.score += 10;
        if (this.score % 100 == 0) {
            this.scoreUpSound.play();
        }
    }
}
