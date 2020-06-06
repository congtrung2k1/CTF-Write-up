// 
// Decompiled by Procyon v0.5.36
// 

package userinterface;

import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics;
import util.Resource;
import gameobject.Encryptor;
import java.awt.image.BufferedImage;
import gameobject.Clouds;
import gameobject.EnemiesManager;
import gameobject.MainCharacter;
import gameobject.Land;
import java.awt.event.KeyListener;
import javax.swing.JPanel;

public class GameScreen extends JPanel implements Runnable, KeyListener
{
    private static final int START_GAME_STATE = 0;
    private static final int GAME_PLAYING_STATE = 1;
    private static final int GAME_OVER_STATE = 2;
    private byte[] b;
    private Land land;
    private MainCharacter mainCharacter;
    private EnemiesManager enemiesManager;
    private Clouds clouds;
    private Thread thread;
    private String k;
    private String v;
    private String password;
    private String p;
    private Boolean flag;
    private boolean isKeyPressed;
    private int gameState;
    private BufferedImage replayButtonImage;
    private BufferedImage gameOverButtonImage;
    
    public GameScreen() {
        this.b = new byte[] { 76, 105, 76, 57, 82, 109, 55, 70, 90, 86, 104, 68, 114, 87, 68, 100, 121, 51, 66, 98, 122, 90, 57, 81, 43, 79, 120, 73, 75, 78, 98, 105, 55, 109, 47, 69, 87, 77, 70, 99, 117, 118, 86, 61 };
        this.k = new String(Encryptor.key);
        this.v = new String(Encryptor.initVector);
        this.password = "";
        this.p = "FHAFURYY\n";
        this.flag = false;
        this.gameState = 0;
        this.mainCharacter = new MainCharacter();
        this.land = new Land(600, this.mainCharacter);
        this.mainCharacter.setSpeedX(4);
        this.replayButtonImage = Resource.getResouceImage("data/replay_button.png");
        this.gameOverButtonImage = Resource.getResouceImage("data/gameover_text.png");
        this.enemiesManager = new EnemiesManager(this.mainCharacter);
        this.clouds = new Clouds(600, this.mainCharacter);
    }
    
    public void startGame() {
        (this.thread = new Thread(this)).start();
    }
    
    public void gameUpdate() {
        if (this.gameState == 1) {
            this.clouds.update();
            this.land.update();
            this.mainCharacter.update();
            this.enemiesManager.update();
            if (this.enemiesManager.isCollision()) {
                this.mainCharacter.playDeadSound();
                this.gameState = 2;
                this.mainCharacter.dead(true);
            }
        }
    }
    
    @Override
    public void paint(final Graphics g) {
        g.setColor(Color.decode("#f7f7f7"));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        if (this.flag) {
            this.p = "";
            g.setColor(Color.red);
            g.drawString(Encryptor.decrypt(this.k, this.v, Encryptor.r(new String(this.b), 13)), 70, 70);
            return;
        }
        switch (this.gameState) {
            case 0: {
                this.mainCharacter.draw(g);
                break;
            }
            case 1:
            case 2: {
                this.clouds.draw(g);
                this.land.draw(g);
                this.enemiesManager.draw(g);
                this.mainCharacter.draw(g);
                g.setColor(Color.BLACK);
                if (this.mainCharacter.score >= 20000) {
                    g.drawString(Encryptor.decrypt(this.k, this.v, Encryptor.r(new String(this.b), 13)), 70, 70);
                }
                g.drawString("HI " + this.mainCharacter.score, 500, 20);
                if (this.gameState == 2) {
                    g.drawImage(this.gameOverButtonImage, 200, 30, null);
                    g.drawImage(this.replayButtonImage, 283, 50, null);
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public void run() {
        final int fps = 50;
        final long msPerFrame = 10000000L;
        long lastTime = 0L;
        final long lag = 0L;
        while (true) {
            this.gameUpdate();
            this.repaint();
            final long endProcessGame = System.nanoTime();
            final long elapsed = lastTime + msPerFrame - System.nanoTime();
            final int msSleep = (int)(elapsed / 1000000L);
            final int nanoSleep = (int)(elapsed % 1000000L);
            if (msSleep <= 0) {
                lastTime = System.nanoTime();
            }
            else {
                try {
                    Thread.sleep(msSleep, nanoSleep);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lastTime = System.nanoTime();
            }
        }
    }
    
    @Override
    public void keyPressed(final KeyEvent e) {
        if (!this.isKeyPressed) {
            this.isKeyPressed = true;
            this.password += (char)e.getKeyCode();
            if (Encryptor.r(this.p, 13).equals(this.password)) {
                this.flag = true;
            }
            switch (this.gameState) {
                case 0: {
                    if (e.getKeyCode() == 32) {
                        this.gameState = 1;
                        break;
                    }
                    break;
                }
                case 1: {
                    if (e.getKeyCode() == 32) {
                        this.mainCharacter.jump();
                        break;
                    }
                    if (e.getKeyCode() == 40) {
                        this.mainCharacter.down(true);
                        break;
                    }
                    break;
                }
                case 2: {
                    if (e.getKeyCode() == 32) {
                        this.gameState = 1;
                        this.resetGame();
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    public void keyReleased(final KeyEvent e) {
        this.isKeyPressed = false;
        if (this.gameState == 1 && e.getKeyCode() == 40) {
            this.mainCharacter.down(false);
        }
    }
    
    @Override
    public void keyTyped(final KeyEvent e) {
    }
    
    private void resetGame() {
        this.enemiesManager.reset();
        this.mainCharacter.dead(false);
        this.mainCharacter.reset();
    }
}
