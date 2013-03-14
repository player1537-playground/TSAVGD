/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import levels.ConversationDisplay;
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
import org.newdawn.slick.UnicodeFont;
import sound.*;

/**
 *
 * @author Andy
 */
public class Hud implements DisplayableEntity {

    public static float width, height;
    static ArrayList<DisplayableEntity> graphics = new ArrayList<DisplayableEntity>();
    static Menu pauseMenu;
    static Menu questMenu;
    static Menu optionsMenu;
    static Menu itemBar;
    static Menu messages;
    static Menu debug;
    static Menu conv;
    boolean debounce;

    public Hud(float width, float height) {
        this.width = width;
        this.height = height;
        UnicodeFont uf = HudGraphic.loadFont("Comic Sans", 16);
        HudGraphic menu = new HudGraphic(Hud.load("res/pause-menu.png"), null);
        Texture menuItemTex = Hud.load("res/pause-menu-button.png");

        HudGraphic itemBarGraphic = new HudGraphic(Hud.load("res/item_bar_test.png"), null);
        HudGraphic item = new HudGraphic(null, null);

        final Sound ding = SoundManager.createSound("res/ding.wav");

        String[] subMenuText = {"Quests", "Options", "Quit", "Continue"};
        Event[] subMenuEvent = {new Event() {

        @Override
        public void execute() {
            ding.play(true);
            setShowQuestMenu(true);
        }
    }, new Event() {

        @Override
        public void execute() {
            ding.play(true);
            setShowOptionsMenu(true);
        }
    }, new Event() {

        @Override
        public void execute() {
            ding.play(true);
            EventTest.quit();
        }
    }, new Event() {

        @Override
        public void execute() {
            ding.play(true);
            setShowPauseMenu(false);
            EventTest.pause(false);
        }
    }
        };
        Menu[] menuItems = new Menu[subMenuText.length];
        for (int i = 0; i < menuItems.length; i++) {
            menuItems[i] = new Menu(24, 110 + (menuItemTex.getImageHeight() + 24) * i, false, new HudGraphic(menuItemTex, subMenuText[i]), true, null, subMenuEvent[i]);
        }
        pauseMenu = new Menu(width - menu.getWidth(), 0, false, menu, true, menuItems, null);
        pauseMenu.hideBranch();

        Menu[] items = new Menu[7];
        for (int i = 0; i < items.length; i++) {
            items[i] = new Menu(86 + 72 * i, 16, false, item, true, null, null);
        }
        itemBar = new Menu(20, height - itemBarGraphic.getHeight(), false, itemBarGraphic, true, items, null);

        MessageCenter.setAttributes(300, 100, 5);
        messages = new Menu(30, height - 190, 300, 100, false, new DisplayableEntity() {

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

        UnicodeFont menuTitle = HudGraphic.loadFont("Arial", 64);
        HudGraphic quest = new HudGraphic(Hud.load("res/quest-menu.png"), "Quests", menuTitle, (int) (width * 3f / 4 - 120), 80);
        questMenu = new Menu(0, 0, false, quest, false, null, null);
        questMenu.hideBranch();
        
        Menu[] volumeTicks = new Menu[11];
        Texture volumeTickTex = Hud.load("res/tick.png");
        for(int i = 0; i < volumeTicks.length; i++) {
            final int index = i;
            volumeTicks[i] = new Menu(23 + 25 * i, 26, false, new HudGraphic(volumeTickTex, "" + i, uf, -2, -19), true, null, new Event() {
                @Override
                public void execute() {
                    SoundManager.setVolume(index / 10f);
                }
            });
        }
        Texture volumeBackground = Hud.load("res/volume.png");
        Menu volumeChange = new Menu(350, 325, false, new HudGraphic(volumeBackground, "Change Volume", HudGraphic.fonts.get(0), 20, -100), true, volumeTicks, null);
        
        String[] qualityStrings = {"   Low", "Medium", "  High"};
        Menu[] qualityOptions = new Menu[qualityStrings.length];
        for(int i = 0; i < qualityOptions.length; i++) {
            qualityOptions[i] = new Menu(10 + i * 290, 100, false, new HudGraphic(menuItemTex, qualityStrings[i], HudGraphic.fonts.get(0), 70, 30), true, null, null);
        }
        Menu quality = new Menu(60, 500, false, new HudGraphic(null, "Quality", HudGraphic.fonts.get(0), 380, 20), true, qualityOptions, null);
        
        Menu[] optionSubMenus = {
            volumeChange,
            quality
        };
        
        HudGraphic options = new HudGraphic(Hud.load("res/options.png"), "Options", menuTitle, (int) (width / 2 - 120), 80);
        optionsMenu = new Menu(0, 0, false, options, false, optionSubMenus, null);
        optionsMenu.hideBranch();
        
        debounce = true;

        graphics.add(itemBar);
        graphics.add(messages);
        graphics.add(conv);
        graphics.add(pauseMenu);
        graphics.add(questMenu);
        graphics.add(optionsMenu);
        graphics.add(debug);

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
        for (Entity e : graphics) {
            e.update(delta);
        }
        boolean pressed = Mouse.isButtonDown(0);
        if (pressed && debounce) {
            pauseMenu.mouseClick(Mouse.getX(), (int) (height - Mouse.getY()));
            questMenu.mouseClick(Mouse.getX(), (int) (height - Mouse.getY()));
            optionsMenu.mouseClick(Mouse.getX(), (int) (height - Mouse.getY()));
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

    public static void setPause(boolean paused) {
        setShowPauseMenu(paused);
    }

    public static void setShowConversation(boolean show) {
        conv.setShow(show);
    }

    public static void setShowQuestMenu(boolean show) {
        pauseMenu.setShowBranch(!show);
        itemBar.setShowBranch(!show);
        questMenu.setShowBranch(show);
    }

    public static void setShowOptionsMenu(boolean show) {
        pauseMenu.setShowBranch(!show);
        itemBar.setShowBranch(!show);
        optionsMenu.setShowBranch(show);
    }

    public static void setShowPauseMenu(boolean show) {
        pauseMenu.setShowBranch(show);
    }

    public static void alternateMenu() {
        if (questMenu.show) {
            setShowQuestMenu(false);
        } else if (optionsMenu.show) {
            setShowOptionsMenu(false);
        } else if (pauseMenu.show) {
            setShowPauseMenu(false);
        } else {
            setShowPauseMenu(true);
        }
    }
}
