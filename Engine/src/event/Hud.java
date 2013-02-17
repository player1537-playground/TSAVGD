/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import conversation.ConversationDisplay;
import java.io.IOException;
import java.util.ArrayList;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.font.effects.ColorEffect;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.SlickException;
import sound.*;

/**
 *
 * @author Andy
 */
public class Hud implements DisplayableEntity {

    public static float width, height;
    static ArrayList<DisplayableEntity> graphics = new ArrayList<DisplayableEntity>();
    static Menu pauseMenu;
    static Menu itemBar;
    static Menu messages;
    static Menu debug;
    static Menu conv;

    boolean debounce;

    public Hud(float width, float height) {
        this.width = width;
        this.height = height;
        HudGraphic menu = new HudGraphic(Hud.load("res/tree.png"), null);
        Texture menuItemTex = Hud.load("res/test.png");

        HudGraphic itemBarGraphic = new HudGraphic(Hud.load("res/item_bar_test.png"), null);
        HudGraphic item = new HudGraphic(Hud.load("res/bullet.png"), null);

        final Sound ding = SoundManager.createSound("res/ding.wav");

        Menu[] menuItems = new Menu[6];
        String[] subMenuText = {"Items", "Character", "Options", "Save", "Quit", "Continue"};
        for (int i = 0; i < menuItems.length; i++) {
            menuItems[i] = new Menu(64, 100 + 96 * i, false, new HudGraphic(menuItemTex, subMenuText[i]), true, null, new Event() {

                @Override
                public void execute() {
                    pauseMenu.hide();
                    Menu[] items = pauseMenu.sub;
                    for (Menu m : items) {
                        m.hide();
                    }

                    ding.play(true);
                }
            });
        }
        pauseMenu = new Menu(width - menu.getWidth(), 0, false, menu, true, menuItems, null);
        pauseMenu.hideBranch();

        Menu[] items = new Menu[7];
        for (int i = 0; i < items.length; i++) {
            items[i] = new Menu(16 + 72 * i, 10, false, item, true, null, null);
        }
        itemBar = new Menu((width - itemBarGraphic.getWidth()) / 2, height - itemBarGraphic.getHeight(), false, itemBarGraphic, true, items, null);

        MessageCenter.setAttributes(300, 100, 5);
        messages = new Menu(30, height - 110, 300, 100, false, new DisplayableEntity() {

            @Override
            public void draw() {
                MessageCenter.draw();
            }

            @Override
            public void update(int delta) {
                MessageCenter.update(delta);
            }
        }, true, null, null);
        
        debug = new Menu(0, 0, 400, 100, false, new DisplayableEntity() {

            @Override
            public void draw() {
                DebugMessages.draw();
            }

            @Override
            public void update(int delta) {
                DebugMessages.update(delta);
            }
        }, true, null, null);
        
        conv = new Menu(0, 0, 0, 0, false, new DisplayableEntity() {

            @Override
            public void draw() {
                ConversationDisplay.draw();
            }

            @Override
            public void update(int delta) {
                ConversationDisplay.update(delta);
            }
        }, false, null, null);

        debounce = true;

        graphics.add(pauseMenu);
        graphics.add(itemBar);
        graphics.add(messages);
        graphics.add(debug);
        graphics.add(conv);

    }

    public static void addGraphic(DisplayableEntity d) {
        graphics.add(d);
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

        for (DisplayableEntity d : graphics) {
            d.draw();
        }

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
        for(Entity e : graphics) {
            e.update(delta);
        }
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
        conv.setShowBranch(!paused);
        if (!paused) {
            resetMenu();
        }
    }

    private void resetMenu() {
        Menu[] items = pauseMenu.sub;
        for (Menu m : items) {
            m.hideChildren();
            m.show = true;
        }
    }
    
    public static void setShowConversation(boolean show) {
        conv.setShow(show);
    }
}
