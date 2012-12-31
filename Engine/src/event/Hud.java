/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import java.io.IOException;
import java.util.ArrayList;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Andy
 */
public class Hud implements DisplayableEntity {

    static float width, height;
    Menu pauseMenu;
    Menu itemBar;
    boolean debounce;

    public Hud(float width, float height) {
        this.width = width;
        this.height = height;
        Texture menuTex = Hud.load("res/tree.png");
        Texture menuItemTex = Hud.load("res/test.png");
        Texture itemBarTex = Hud.load("res/item_bar_test.png");
        Texture itemTex = Hud.load("res/bullet.png");

        Menu[] menuItems = new Menu[6];
        for (int i = 0; i < menuItems.length; i++) {
            menuItems[i] = new Menu(64, 100 + 96 * i, false, menuItemTex, true, null, new Event() {

                @Override
                public void execute() {
                    pauseMenu.hide();
                }
            });
        }
        pauseMenu = new Menu(width - menuTex.getImageWidth(), 0, false, menuTex, true, menuItems, null);
        pauseMenu.hideBranch();

        Menu[] items = new Menu[7];
        for (int i = 0; i < items.length; i++) {
            items[i] = new Menu(16 + 72 * i, 10, false, itemTex, true, null, null);
        }
        itemBar = new Menu((width - itemBarTex.getImageWidth()) / 2, height - itemBarTex.getImageHeight(), false, itemBarTex, true, items, null);
        
        debounce = true;

    }

    @Override
    public void draw() {
        glPushMatrix();
        glLoadIdentity();
        glDisable(GL_CULL_FACE);
        glDisable(GL_LIGHTING);
        glDisable(GL_DEPTH_TEST);
        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        glOrtho(0, width, height, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);

        pauseMenu.draw();
        itemBar.draw();

        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_CULL_FACE);
        glEnable(GL_LIGHTING);
        glEnable(GL_DEPTH_TEST);
        glPopMatrix();
    }

    @Override
    public void update(int delta) {
        boolean pressed = Mouse.isButtonDown(0);
        if (pressed && debounce) {
            pauseMenu.mouseClick(Mouse.getX(), (int) (height - Mouse.getY()));
        }
        debounce = !pressed;
    }

    public static Texture load(String path) {
        try {
            return TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(path));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void setPause(boolean paused) {
        pauseMenu.setShowBranch(paused);
        itemBar.setShowBranch(!paused);
    }
}
