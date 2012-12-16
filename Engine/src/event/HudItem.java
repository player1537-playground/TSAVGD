/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

/**
 *
 * @author Andy
 */
public class HudItem implements DisplayableEntity {

    Texture t;
    float x, y;
    float width, height;
    float tx, ty;
    Behavior b;
    private boolean show;

    public enum Behavior {

        always, paused
    }

    public HudItem(Texture t, float x, float y, float width, float height) {
        this(t, x, y, width, height, 1, 1f);
    }

    public HudItem(Texture t, float x, float y, float width, float height, float tx, float ty) {
        this(t, x, y, width, height, tx, ty, Behavior.always);
        show = true;
    }

    public HudItem(Texture t, float x, float y, float width, float height, float tx, float ty, Behavior b) {
        this.t = t;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.tx = tx;
        this.ty = ty;
        this.b = b;
    }

    @Override
    public void draw() {

        if (show) {
            Color.white.bind();
            glBindTexture(GL_TEXTURE_2D, t.getTextureID());
            glBegin(GL_QUADS);
            glTexCoord2f(0, 0);
            glVertex2f(x, y);
            glTexCoord2f(tx, 0);
            glVertex2f(x + width, y);
            glTexCoord2f(tx, ty);
            glVertex2f(x + width, y + height);
            glTexCoord2f(0, ty);
            glVertex2f(x, y + height);
            glEnd();
        }

    }

    @Override
    public void update(int delta) {
    }

    void setPaused(boolean paused) {
        if (b.equals(Behavior.paused)) {
            this.show = paused;
        }
    }
}
