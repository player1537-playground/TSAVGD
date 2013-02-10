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
    DisplayableEntity de;
    Menu[] sub;
    boolean absolute;
    boolean show;
    boolean showChildren;
    Event event;

    public Menu(float x, float y, boolean absolute, boolean show, Menu[] sub) {
        
        this(x, y, 0, 0, absolute, null, show, sub, null);
        
    }
    
    public Menu(float x, float y, boolean absolute, HudGraphic hg, boolean show, Menu[] sub, Event event) {

        this(x, y, hg.getWidth(), hg.getHeight(), absolute, hg, show, sub, event);

    }

    public Menu(float x, float y, float width, float height, boolean absolute, DisplayableEntity de, boolean show, Menu[] sub, Event event) {

        this.width = width;
        this.height = height;
        this.de = de;
        this.show = show;
        this.sub = sub;
        this.absolute = absolute;
        if(absolute) {
            this.x = x;
            this.y = y;
        }
        translate(x, y);
        this.event = event;
        this.showChildren = true;

    }

    @Override
    public void draw() {
        if (show && de != null) {
            glPushMatrix();
            glTranslatef(x, y, 0);
            de.draw();
            glPopMatrix();
        }
        if (showChildren) {
            if (sub != null) {
                for (Menu m : sub) {
                    m.draw();
                }
            }
        }
    }

    @Override
    public void update(int delta) {
        de.update(delta);
    }

    public void mouseClick(int mx, int my) {
        if (event != null && show && inArea(mx, my)) {
            event.execute();
        }
        if (showChildren && sub != null) {
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

    void hideChildren() {
        showChildren = false;
    }

    void showChildren() {
        showChildren = true;
    }

    void hideBranch() {
        hide();
        hideChildren();
    }

    void showBranch() {
        show();
        showChildren();
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

    void setShowChildren(boolean showChildren) {
        this.showChildren = showChildren;
    }

    void setShowBranch(boolean showBranch) {
        setShow(showBranch);
        setShowChildren(showBranch);
    }
}
