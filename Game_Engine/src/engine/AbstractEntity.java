/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import java.util.ArrayList;
import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Andy
 */
public abstract class AbstractEntity extends PhysicalEntity implements DisplayableEntity {

    Model m;
    Vector3f origin;
    float angle;

    public AbstractEntity(Model m) {

        super(m.getBounds());
        this.m = m;
        Vector3f myMin = b.getMin();
        this.origin = new Vector3f(-myMin.getX(), -myMin.getY(), -myMin.getZ());
        this.angle = 0;

    }
    
    @Override
    public void draw() {

        if (m != null) {
            Vector3f min = b.getMin();
            Vector3f max = b.getMax();
            glPushMatrix();
            glTranslatef(origin.getX() + min.x, origin.getY() + min.y, origin.getZ() + min.z);
            glRotatef(angle, 0, 1, 0);
            m.draw();
            glPopMatrix();
            glDisable(GL_CULL_FACE);
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            glBegin(GL_QUADS);

            glVertex3f(min.getX(), min.getY(), min.getZ());
            glVertex3f(max.getX(), min.getY(), min.getZ());
            glVertex3f(max.getX(), max.getY(), min.getZ());
            glVertex3f(min.getX(), max.getY(), min.getZ());

            glVertex3f(min.getX(), min.getY(), max.getZ());
            glVertex3f(max.getX(), min.getY(), max.getZ());
            glVertex3f(max.getX(), max.getY(), max.getZ());
            glVertex3f(min.getX(), max.getY(), max.getZ());

            glVertex3f(min.getX(), min.getY(), min.getZ());
            glVertex3f(min.getX(), min.getY(), max.getZ());
            glVertex3f(min.getX(), max.getY(), max.getZ());
            glVertex3f(min.getX(), max.getY(), min.getZ());

            glVertex3f(max.getX(), min.getY(), min.getZ());
            glVertex3f(max.getX(), min.getY(), max.getZ());
            glVertex3f(max.getX(), max.getY(), max.getZ());
            glVertex3f(max.getX(), max.getY(), min.getZ());

            glVertex3f(max.getX(), min.getY(), min.getZ());
            glVertex3f(min.getX(), min.getY(), min.getZ());
            glVertex3f(min.getX(), min.getY(), max.getZ());
            glVertex3f(max.getX(), min.getY(), max.getZ());

            glVertex3f(max.getX(), max.getY(), min.getZ());
            glVertex3f(min.getX(), max.getY(), min.getZ());
            glVertex3f(min.getX(), max.getY(), max.getZ());
            glVertex3f(max.getX(), max.getY(), max.getZ());

            glEnd();
            glEnable(GL_CULL_FACE);
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        }

    }
    
    public void setAngle(float angle) {
        this.angle = angle;
    }
    
    public void rotate(float angle) {
        this.angle += angle;
    }
    
    
}
