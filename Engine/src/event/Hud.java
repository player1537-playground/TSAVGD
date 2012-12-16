/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import java.io.IOException;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.SlickException;
    
/**
 *
 * @author Andy
 */
public class Hud implements DisplayableEntity {

    static float width, height;
    static HudItem[] items;
    static UnicodeFont font;
    
    public Hud(float width, float height) {
        this.width = width;
        this.height = height;
        Texture menu = Hud.load("res/tree.png");
        Texture itemBar = Hud.load("res/item_bar_test.png");
        //setUpFonts();
        items = new HudItem[] {
            new HudItem(menu, width - menu.getImageWidth(), 0, menu.getImageWidth(), height,
                (float)menu.getImageWidth() / menu.getTextureWidth(), (float)menu.getImageHeight() / menu.getTextureHeight(), HudItem.Behavior.paused),
            new HudItem(itemBar, (width - itemBar.getImageWidth()) / 2, height - itemBar.getImageHeight(), itemBar.getImageWidth(), itemBar.getImageHeight(),
                (float)menu.getImageWidth() / menu.getTextureWidth(), (float)itemBar.getImageHeight() / itemBar.getTextureHeight())
        };
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
        
        for(HudItem i : items) {
            i.draw();
        }
        //font.drawString(100, 100, "FONT TEST");
        
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
    }
    
    public static Texture load(String path) {
        try {
            return TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(path));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    void setPause(boolean paused) {
        for(HudItem i : items) {
            i.setPaused(paused);
        }
    }
    
    private static void setUpFonts() {
        java.awt.Font awtFont = new java.awt.Font("Times New Roman", java.awt.Font.BOLD, 18);
        font = new UnicodeFont(awtFont);
        font.getEffects().add(new ColorEffect(java.awt.Color.black));
        font.addAsciiGlyphs();
        try {
            font.loadGlyphs();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
    
}
