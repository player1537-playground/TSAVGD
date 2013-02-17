/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import event.Activatable;
import static org.lwjgl.opengl.GL11.*;
import event.DisplayableEntity;
import event.EventTest;
import event.Triangle;
import java.util.ArrayList;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Andy
 */
public class SelectTriangle extends Triangle implements DisplayableEntity, Activatable{

    private final Vector3f[] verts;
    private final Vector3f norm;
    static ArrayList<SelectTriangle> selected = new ArrayList<SelectTriangle>();

    public SelectTriangle(Vector3f[] v, Vector3f n) {
        super(v, n);
        this.verts = v;
        this.norm = n;
    }

    public static void print() {
        System.out.println(selected);
    }
    
    public void draw() {
        glBegin(GL_TRIANGLES);

        for (int j = 0; j < 3; j++) {

            myNormal3f(norm);
            myVertex3f(verts[j]);

        }

        glEnd();
    }

    public void myNormal3f(Vector3f n) {

        glNormal3f(n.getX(), n.getY(), n.getZ());

    }

    public void myVertex3f(Vector3f v) {

        glVertex3f(v.getX(), v.getY(), v.getZ());

    }

    @Override
    public void update(int delta) {
    }

    @Override
    public void activate() {
        if (selected.contains(this)) {
            selected.remove(this);
            EventTest.removeDisplayableEntity(this);
        } else {
            selected.add(this);
            EventTest.addDisplayableEntity(this);
        }
    }
}
