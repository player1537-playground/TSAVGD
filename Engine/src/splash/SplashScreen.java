/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package splash;

import event.*;
import java.util.ArrayList;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import sound.Sound;
import sound.SoundManager;

/**
 *
 * @author Andy
 */
public class SplashScreen {

    static boolean readyPlay = false;
    static int resIndex = 0;
    static DisplayMode dm;
    static HudGraphic res;
    private static boolean debounce;
    static int gameHeight = 768;
    static int gameWidth = 1024;
    public static boolean fullscreen = true;
    public static String[] options = {"Full Screen", "1024x768"};

    public static void main(String[] args) {
        int width = 640, height = 480;
        try {
            DisplayMode[] yo = Display.getAvailableDisplayModes();
            for (DisplayMode y : yo) {
                if (y.isFullscreenCapable() && y.getHeight() == gameHeight && y.getWidth() == gameWidth) {
                    dm = y;
                }
            }

            Display.setDisplayMode(new DisplayMode(width, height));
            Display.setTitle("Voyager");
            Display.create();
            Mouse.create();
            HudGraphic.init();
        } catch (LWJGLException e) {
            e.printStackTrace();
            Display.destroy();
            System.exit(1);
        }
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glMatrixMode(GL_PROJECTION);
        glOrtho(0, width, height, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);

        Texture button = Hud.load("res/play.png");
        HudGraphic play = new HudGraphic(button, "Play", HudGraphic.fonts.get(0), 65, 7);
        res = new HudGraphic(button, options[0], HudGraphic.loadFont("Times New Roman", 28), 16, 14);
        Menu playButton = new Menu(0, 0, false, play, true, null, new Event() {

            public void execute() {
                readyPlay = true;
            }
        });
        Menu resButton = new Menu(0, 80, false, res, true, null, new Event() {

            public void execute() {
                toggleFull();
            }
        });
        Menu[] menuWrapper = {new Menu((width - play.getWidth()) / 2, 200, false, true, new Menu[]{playButton, resButton})};
        Menu m = new Menu(0, 0, true, new HudGraphic(Hud.load("res/splash_background.png"), null), true, menuWrapper, null);

        SoundManager.create();
        Sound openingMusic = SoundManager.createSound("res/opening.wav");
        openingMusic.repeat();
        openingMusic.play();

        while (!Display.isCloseRequested() && !readyPlay) {
            glClear(GL_COLOR_BUFFER_BIT);
            m.draw();
            Display.update();
            Display.sync(60);
            boolean pressed = Mouse.isButtonDown(0);
            if (pressed && debounce) {
                m.mouseClick(Mouse.getX(), (int) (height - Mouse.getY()));
            }
            debounce = !pressed;
        }

        SoundManager.destroy();
        Display.destroy();
        Mouse.destroy();
        if (readyPlay) {
            if (fullscreen) {
                EventTest.main(dm);
            } else {
                EventTest.main(new DisplayMode(gameWidth, gameHeight));
            }
        }
    }

    public static void toggleFull() {
        fullscreen = !fullscreen;
        res.setMessage(options[fullscreen ? 0 : 1]);
    }
}
