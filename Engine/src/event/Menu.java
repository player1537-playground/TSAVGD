/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import org.newdawn.slick.opengl.Texture;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;

/**
 *
 * @author Andy
 */
public class Menu implements DisplayableEntity {

    float x, y;
    float width, height;
    Texture t;
    Menu[] sub;
    boolean absolute;
    boolean show;
    boolean showBranch;
    Event event;

    public Menu(float x, float y, boolean absolute, Texture t, boolean show, Menu[] sub, Event event) {

        this(x, y, t.getImageWidth(), t.getImageHeight(), absolute, t, show, sub, event);

    }

    public Menu(float x, float y, float width, float height, boolean absolute, Texture t, boolean show, Menu[] sub, Event event) {

        this.width = width;
        this.height = height;
        this.t = t;
        this.show = show;
        this.sub = sub;
        this.absolute = absolute;
        if(absolute) {
            this.x = x;
            this.y = y;
        }
        translate(x, y);
        this.event = event;
        this.showBranch = true;

    }

    @Override
    public void draw() {
        if (show && showBranch) {
            glBindTexture(GL_TEXTURE_2D, t.getTextureID());
            glBegin(GL_QUADS);
            glTexCoord2f(0, 0);
            glVertex2f(x, y);
            glTexCoord2f(t.getWidth(), 0);
            glVertex2f(x + width, y);
            glTexCoord2f(t.getWidth(), t.getHeight());
            glVertex2f(x + width, y + height);
            glTexCoord2f(0, t.getHeight());
            glVertex2f(x, y + height);
            glEnd();
        }
        if (showBranch) {
            if (sub != null) {
                for (Menu m : sub) {
                    m.draw();
                }
            }
        }
    }

    @Override
    public void update(int delta) {
    }

    public void mouseClick(int mx, int my) {
        if (event != null && show && showBranch && inArea(mx, my)) {
            event.execute();
        }
        if (showBranch && sub != null) {
            for (Menu m : sub) {
                m.mouseClick(mx, my);
            }
        }
    }

    void hide() {
        show = false;
    }

    void show() {
        show = true;
    }

    void hideBranch() {
        showBranch = false;
    }

    void showBranch() {
        showBranch = true;
    }

    private boolean inArea(int mx, int my) {
        return x <= mx && mx <= x + width && y <= my && my <= y + height;
    }

    private void translate(float x, float y) {
        if (!absolute) {
            this.x += x;
            this.y += y;
        }
        if (sub != null) {
            for (Menu m : sub) {
                m.translate(x, y);
            }
        }
    }

    void setShow(boolean show) {
        this.show = show;
    }

    void setShowBranch(boolean showBranch) {
        this.showBranch = showBranch;
    }
}
