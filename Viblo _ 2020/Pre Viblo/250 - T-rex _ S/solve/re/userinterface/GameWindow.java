// 
// Decompiled by Procyon v0.5.36
// 

package userinterface;

import java.security.NoSuchAlgorithmException;
import java.awt.Component;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

public class GameWindow extends JFrame
{
    public static final int SCREEN_WIDTH = 600;
    private GameScreen gameScreen;
    
    public GameWindow() {
        super("Java T-Rex game");
        this.setSize(600, 175);
        this.setLocation(400, 200);
        this.setDefaultCloseOperation(3);
        this.setResizable(false);
        this.addKeyListener(this.gameScreen = new GameScreen());
        this.add(this.gameScreen);
    }
    
    public void startGame() {
        this.setVisible(true);
        this.gameScreen.startGame();
    }
    
    public static void main(final String[] args) throws NoSuchAlgorithmException {
        new GameWindow().startGame();
    }
}
