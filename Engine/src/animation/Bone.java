/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package animation;

import engine.Face;
import java.util.ArrayList;
import org.lwjgl.util.vector.*;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;

/**
 *
 * @author Andy
 */
public class Bone {

    Vector3f bone;
    Quaternion q;
    AnimationUpdateScheme s;
    int index;
    Bone[] children;

    public Bone(Vector3f bone, Quaternion q, float k, float lower, float upper, AFace[] skin, Bone[] children) {
        this(bone, q, new OscillateScheme(k, lower, upper), skin, children);
    }
    
    

    public Bone(Vector3f bone, Quaternion q, AnimationUpdateScheme s, AFace[] skin, Bone[] children) {
        this.bone = bone;
        this.q = q;
        this.s = s;
        index = glGenLists(1);
        glNewList(index, GL_COMPILE);
        draw(skin);
        glEndList();
        this.children = children;
    }

    public void setChildren(Bone[] children) {
        this.children = children;
    }

    public void draw(AFace[] faces) {
        Color.white.bind();
        glBindTexture(GL_TEXTURE_2D, faces[0].textureIndice);
        int last = faces[0].textureIndice;
        glBegin(GL_TRIANGLES);
        for (AFace face : faces) {

            if (last != face.textureIndice) {
                glEnd();
                Color.white.bind();
                glBindTexture(GL_TEXTURE_2D, face.textureIndice);
                last = face.textureIndice;
                glBegin(GL_TRIANGLES);
            }

            for (int j = 0; j < 3; j++) {

                myTexture(face.texCoords[j]);
                myNormal3f(face.normals[j]);
                myVertex3f(face.verts[j]);

            }

        }
        glEnd();
    }

    public void draw() {
        glPushMatrix();
        glRotatef(q.getW(), q.getX(), q.getY(), q.getZ());
        glBegin(GL_LINES);
        glVertex3f(0, 0, 0);
        glVertex3f(bone.getX(), bone.getY(), bone.getZ());
        glEnd();
        glCallList(index);
        glTranslatef(bone.getX(), bone.getY(), bone.getZ());
        if (children != null) {
            for (Bone child : children) {
                if (child != null) {
                    child.draw();
                }
            }
        }
        glPopMatrix();
    }

    public void update(int delta) {
        s.updateOrientation(q, delta);
        if (children != null) {
            for (Bone child : children) {
                if (child != null) {
                    child.update(delta);
                }
            }
        }
    }

    private void glVector3f(Vector3f v) {
        glVertex3f(v.getX(), v.getY(), v.getZ());
    }

    public void myNormal3f(Vector3f n) {

        glNormal3f(n.getX(), n.getY(), n.getZ());

    }

    public void myVertex3f(Vector3f v) {

        glVertex3f(v.getX(), v.getY(), v.getZ());

    }

    private void myTexture(Vector2f v) {

        glTexCoord2f(v.getX(), v.getY());

    }
}
