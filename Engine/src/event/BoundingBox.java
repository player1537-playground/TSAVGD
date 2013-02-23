/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Andy
 */
public class BoundingBox {

    private Vector3f min, max;
    static BoundingBox defaultBox = new BoundingBox(new Vector3f(), new Vector3f());

    public BoundingBox() {
        set(defaultBox);
    }
    
    public BoundingBox(Vector3f min, Vector3f max) {

        this.min = min;
        this.max = max;

    }

    public static BoundingBox create(Vector3f[] verts) {

        float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, minZ = Float.MAX_VALUE;
        float maxX = -Float.MAX_VALUE, maxY = -Float.MAX_VALUE, maxZ = -Float.MAX_VALUE;
        for (Vector3f v : verts) {

            if (v.getX() < minX) {
                minX = v.getX();
            }
            if (v.getY() < minY) {
                minY = v.getY();
            }
            if (v.getZ() < minZ) {
                minZ = v.getZ();
            }

            if (v.getX() > maxX) {
                maxX = v.getX();
            }
            if (v.getY() > maxY) {
                maxY = v.getY();
            }
            if (v.getZ() > maxZ) {
                maxZ = v.getZ();
            }

        }
        
        maxX -= minX;
        maxY -= minY;
        maxZ -= minZ;
        return new BoundingBox(new Vector3f(minX, minY, minZ), new Vector3f(maxX, maxY, maxZ));

    }

    public void setPosition(Vector3f p) {

        this.min = p;

    }

    public void translate(float x, float y, float z) {

        min.translate(x, y, z);

    }

    public void translate(Vector3f v) {

        min.translate(v.getX(), v.getY(), v.getZ());

    }

    public Vector3f[] getVertexes() {

        Vector3f min = getMin();
        Vector3f max = getMax();
        return new Vector3f[]{
                    min,
                    new Vector3f(min.getX(), min.getY(), max.getZ()),
                    new Vector3f(min.getX(), max.getY(), max.getZ()),
                    new Vector3f(min.getX(), max.getY(), min.getZ()),
                    new Vector3f(max.getX(), min.getY(), max.getZ()),
                    new Vector3f(max.getX(), min.getY(), min.getZ()),
                    new Vector3f(max.getX(), max.getY(), min.getZ()),
                    max
                };

    }

    public Vector3f getCenter() {
        return new Vector3f(min.getX() + max.getX() / 2, min.getY() + max.getY() / 2, min.getZ() + max.getZ() / 2);
    }

    public boolean contains(Vector3f v) {
        Vector3f min = getMin();
        Vector3f max = getMax();
        if (valueInRange(v.getX(), min.getX(), max.getX())
                && valueInRange(v.getY(), min.getY(), max.getY())
                && valueInRange(v.getZ(), min.getZ(), max.getZ())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean contains(BoundingBox b) {
        Vector3f min = getMin();
        Vector3f max = getMax();
        Vector3f bMin = b.getMin();
        //Vector3f bMax = b.getMax();
        //System.out.println("Test Min " + bMin.getX() + " Max " + bMax.getX());
        //System.out.println("BoBo Min " + min.getX() + " Max " + max.getX());
        if (valueInRange(bMin.getX(), min.getX(), max.getX() - b.getWidth() )
                && valueInRange(bMin.getY(), min.getY(), max.getY() - b.getHeight())
                && valueInRange(bMin.getZ(), min.getZ(), max.getZ() - b.getDepth())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean intersects(BoundingBox b) {
        Vector3f min = getMin();
        Vector3f max = getMax();
        Vector3f bMin = b.getMin();
        Vector3f bMax = b.getMax();
        boolean xOverlap = valueInRange(min.getX(), bMin.getX(), bMax.getX())
                || valueInRange(bMin.getX(), min.getX(), max.getX());
        boolean yOverlap = valueInRange(min.getY(), bMin.getY(), bMax.getY())
                || valueInRange(bMin.getY(), min.getY(), max.getY());
        boolean zOverlap = valueInRange(min.getZ(), bMin.getZ(), bMax.getZ())
                || valueInRange(bMin.getZ(), min.getZ(), max.getZ());

        return xOverlap && yOverlap && zOverlap;
    }

    public boolean valueInRange(float value, float min, float max) {
        return (value >= min) && (value <= max);
    }

    public String toString() {
        return getMin().toString() + getMax().toString();
    }

    public Vector3f getMax() {
        return Vector3f.add(min, max, null);
    }

    public Vector3f getMin() {
        return min;
    }

    public float getWidth() {
        return max.x;
    }

    public float getHeight() {
        return max.y;
    }

    public float getDepth() {
        return max.z;
    }

    Vector3f getDimension() {
        return max;
    }

    public void set(BoundingBox box) {
        this.min = box.getMin();
        this.max = box.getMax();
    }
}
