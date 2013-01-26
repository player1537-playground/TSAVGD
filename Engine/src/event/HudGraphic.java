/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import java.awt.Font;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.opengl.Texture;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.font.effects.ColorEffect;

/**
 *
 * @author Andy
 */
public class HudGraphic implements DisplayableEntity {

    Texture texture;
    int width, height;
    String text;
    UnicodeFont font;
    int textx, texty;
    public static ArrayList<UnicodeFont> fonts;

    static {
        fonts = new ArrayList<UnicodeFont>();
        fonts.add(loadFont("Times New Roman", 40));
    }
    
    public static UnicodeFont loadFont(String name, float size) {
        
        Font awtFont = new Font(name, Font.BOLD, 24);
        UnicodeFont f = new UnicodeFont(awtFont.deriveFont(0, size));
        f.addAsciiGlyphs();
        f.getEffects().add(new ColorEffect(java.awt.Color.BLACK));
        try {
            f.loadGlyphs();
        } catch (SlickException ex) {
            ex.printStackTrace();
        }
        fonts.add(f);
        return f;
    }

    public HudGraphic(Texture texture, String text) {
        this(texture, text, fonts.get(0), 0, 0);
    }

    public HudGraphic(Texture texture, String text, UnicodeFont font, int textx, int texty) {
        if (texture != null) {
            this.texture = texture;
            this.width = texture.getImageWidth();
            this.height = texture.getImageHeight();
        } else {
            this.width = 0;
            this.height = 0;
        }

        if (text != null) {
            this.text = text;
            if (!this.fonts.contains(font)) {
                this.fonts.add(font);
            }
            this.font = font;
            this.textx = textx;
            this.texty = texty;
        }
    }

    @Override
    public void draw() {
        if (texture != null) {
            glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
            glBegin(GL_QUADS);
            glTexCoord2f(0, 0);
            glVertex2f(0, 0);
            glTexCoord2f(texture.getWidth(), 0);
            glVertex2f(width, 0);
            glTexCoord2f(texture.getWidth(), texture.getHeight());
            glVertex2f(width, height);
            glTexCoord2f(0, texture.getHeight());
            glVertex2f(0, height);
            glEnd();
        }

        if (text != null) {
            font.drawString(textx, texty, text);
        }
    }

    @Override
    public void update(int delta) {
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
