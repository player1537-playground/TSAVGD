/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Andy
 */
public class SkyDome implements DisplayableEntity {

    Sphere sphere;
    Texture t;

    public SkyDome() {

        this.sphere = new Sphere();
        sphere.setOrientation(GLU.GLU_INSIDE);
        sphere.setTextureFlag(true);
        try {
            this.t = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/wall.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void draw() {

        glPushMatrix();
        glLoadIdentity();
        (new Color(0.4f, 0.6f, 0.8f)).bind();
        glBindTexture(GL_TEXTURE_2D, t.getTextureID());
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_LIGHTING);
        sphere.draw(100, 10, 10);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_LIGHTING);
        glPopMatrix();

    }

    public void update(int delta) {
    }

}
