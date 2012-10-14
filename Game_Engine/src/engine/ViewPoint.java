/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

/**
 *
 * @author Andy
 */
public class ViewPoint {
    private Vector3f position;
    //Angle around said axis
    //pitch, yaw, roll
    private float xAngle, yAngle, zAngle;
    private float fieldOfView;
    private float aspectRatio;
    private float zNear, zFar;

    public ViewPoint() {

        this(new Vector3f(), 0, 0, 0);

    }

    public ViewPoint(Vector3f position, float xAngle, float yAngle, float zAngle) {

        this(position, xAngle, yAngle, zAngle, 60, 800f / 600f, 0.1f, 1100f);

    }

    public ViewPoint(Vector3f position, float xAngle, float yAngle, float zAngle,
            float fov, float aspectRatio, float zNear, float zFar) {

        this.position = position;
        this.xAngle = xAngle;
        this.yAngle = yAngle;
        this.zAngle = zAngle;
        this.fieldOfView = fov;
        this.aspectRatio = aspectRatio;
        this.zNear = zNear;
        this.zFar = zFar;

    }

    public void moveFromWorld(Vector3f u) {
        
        position.translate(u.getX(), u.getY(), u.getZ());
        
    }
    public void moveFromWorld(float x, float y, float z) {
        
        position.translate(x, y, z);
        
    }
    
    public void rotate(float dXAngle, float dYAngle, float dZAngle) {
        
        //FPS  xAngle = Math.max(Math.min(xAngle + dXAngle, 80), -80);
        xAngle = (xAngle + dXAngle) % 360;
        yAngle = (yAngle + dYAngle) % 360;
        zAngle = (zAngle + dZAngle) % 360;
        
    }
    
    public void applyPerspectiveMatrix() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(fieldOfView, aspectRatio, zNear, zFar);
        glMatrixMode(GL_MODELVIEW);
    }

    public void adjustToView(boolean resetMatrix) {
        if (resetMatrix) glLoadIdentity();
        glRotatef(xAngle, -1, 0, 0);
        glRotatef(yAngle, 0, -1, 0);
        glRotatef(zAngle, 0, 0, -1);
        glTranslatef(-position.getX(), -position.getY(), -position.getZ());
    }
    
    public float getX() {
        
        return position.getX();
        
    }
    
    public float getY() {
        
        return position.getY();
        
    }
    
    public float getZ() {
        
        return position.getZ();
        
    }

    void setPosition(float x, float y, float z) {
        
        this.position = new Vector3f(x, y, z);
        
    }

    void setPosition(Vector3f v) {
        
        this.position = v;
        
    }
    
    void setAngle(float x, float y, float z) {
        
        this.xAngle = x;
        this.yAngle = y;
        this.zAngle = z;
        
    }
    
    Vector3f getPosition() {
        return position;
    }
}
