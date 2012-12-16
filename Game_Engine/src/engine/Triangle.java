/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Andy
 */
public class Triangle implements Boundable {
    BoundingBox bounds;
    float a, b, c, d;

    public Triangle(Vector3f[] v, Vector3f n) {

        bounds = BoundingBox.create(v);
        a = n.getX();
        b = n.getY();
        c = n.getZ();
        d = a * v[0].getX() + b * v[0].getY() + c * v[0].getZ();

    }

    public BoundingBox getBounds() {

        return bounds;

    }

    public String toString() {

        return bounds.toString();

    }
    
    public Vector3f getNormal() {
        return new Vector3f(a, b, c);
    }
    
    public Vector3f getMiddle() {
        return bounds.getCenter();
    }
    
}
