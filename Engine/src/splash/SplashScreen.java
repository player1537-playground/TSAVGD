/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package splash;

import event.Event;
import event.EventTest;
import event.Menu;
import event.Hud;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.opengl.Texture;

/**
 *
 * @author Andy
 */
public class SplashScreen {

    static boolean readyPlay = false;

    public static void main(String[] args) {
        int width = 640, height = 480;
        try {
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.setTitle("Voyager");
            Display.create();
            //Mouse.create();
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
        Texture play = Hud.load("play.png");
        Menu[] menuItems = {new Menu((width - play.getImageWidth()) / 2, 200, false, play, true, null, new Event() {

        public void execute() {
            readyPlay = true;
        }
    })};
        Menu m = new Menu(0, 0, true, Hud.load("splash_background.png"), true, menuItems, null);
        while (!Display.isCloseRequested() && !readyPlay) {
            glClear(GL_COLOR_BUFFER_BIT);
            m.draw();
            Display.update();
            Display.sync(60);
            if (Mouse.isButtonDown(0)) {
                m.mouseClick(Mouse.getX(), height - Mouse.getY());
            }
        }
        Display.destroy();
        Mouse.destroy();
        EventTest.main(null);
    }
}
