/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import java.util.ArrayList;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Andy
 */
public class CubeTree<T extends Boundable> {

    ArrayList<T> objects;
    CubeTree northwesttop;
    CubeTree northeasttop;
    CubeTree southwesttop;
    CubeTree southeasttop;
    CubeTree northwestbottom;
    CubeTree northeastbottom;
    CubeTree southwestbottom;
    CubeTree southeastbottom;
    BoundingBox boundary;
    int numChildren = 0;
    // TODO: Delete
    int max = -1;

    public CubeTree(T[] objects, BoundingBox boundary) {

        this(boundary);
        for (T object : objects) {

            insert(object);

        }

    }

    public CubeTree(BoundingBox boundary) {

        this.boundary = boundary;
        this.objects = new ArrayList<T>();

    }

    public boolean insert(T object) {

        if (!boundary.contains(object.getBounds())) {
            return false;
        }

        if (northwesttop == null) {
            subdivide();
        }

        if (northwesttop.insert(object)) {
            numChildren++;
            return true;
        }
        if (northeasttop.insert(object)) {
            numChildren++;
            return true;
        }
        if (southwesttop.insert(object)) {
            numChildren++;
            return true;
        }
        if (southeasttop.insert(object)) {
            numChildren++;
            return true;
        }

        if (northwestbottom.insert(object)) {
            numChildren++;
            return true;
        }
        if (northeastbottom.insert(object)) {
            numChildren++;
            return true;
        }
        if (southwestbottom.insert(object)) {
            numChildren++;
            return true;
        }
        if (southeastbottom.insert(object)) {
            numChildren++;
            return true;
        }

        this.objects.add(object);

        return true;

    }

    public ArrayList<T> queryRange(BoundingBox range) {

        ArrayList<T> objectsInRange = new ArrayList<T>();

        if (!boundary.intersects(range)) {

            return objectsInRange;

        }

        for (int i = 0; i < objects.size(); i++) {

            if (range.intersects(objects.get(i).getBounds())) {

                objectsInRange.add(objects.get(i));

            }

        }

        if (northwesttop == null) {

            return objectsInRange;

        }

        if (northwesttop.numChildren != 0) {
            objectsInRange.addAll(northwesttop.queryRange(range));
        }
        if (northeasttop.numChildren != 0) {
            objectsInRange.addAll(northeasttop.queryRange(range));
        }
        if (southwesttop.numChildren != 0) {
            objectsInRange.addAll(southwesttop.queryRange(range));
        }
        if (southeasttop.numChildren != 0) {
            objectsInRange.addAll(southeasttop.queryRange(range));
        }
        if (northwestbottom.numChildren != 0) {
            objectsInRange.addAll(northwestbottom.queryRange(range));
        }
        if (northeastbottom.numChildren != 0) {
            objectsInRange.addAll(northeastbottom.queryRange(range));
        }
        if (southwestbottom.numChildren != 0) {
            objectsInRange.addAll(southwestbottom.queryRange(range));
        }
        if (southeastbottom.numChildren != 0) {
            objectsInRange.addAll(southeastbottom.queryRange(range));
        }

        if (objectsInRange.size() > max) {
            max = objectsInRange.size();
            System.out.println("Max: " + max);
        }
        return objectsInRange;

    }

    private void subdivide() {
        Vector3f min = boundary.getMin();
        Vector3f max = boundary.getDimension();
        Vector3f half = (Vector3f) new Vector3f(max).scale(0.5f);

        northwestbottom = new CubeTree(new BoundingBox(min,
                new Vector3f(half.getX(), half.getY(), half.getZ())));
        northeastbottom = new CubeTree(new BoundingBox(new Vector3f(half.getX(), min.getY(), min.getZ()),
                new Vector3f(max.getX(), half.getY(), half.getZ())));
        southwestbottom = new CubeTree(new BoundingBox(new Vector3f(min.getX(), min.getY(), half.getZ()),
                new Vector3f(half.getX(), half.getY(), max.getZ())));
        southeastbottom = new CubeTree(new BoundingBox(new Vector3f(half.getX(), min.getY(), half.getZ()),
                new Vector3f(max.getX(), half.getY(), max.getZ())));

        northwesttop = new CubeTree(new BoundingBox(new Vector3f(min.getX(), half.getY(), min.getZ()),
                new Vector3f(half.getX(), max.getY(), half.getZ())));
        northeasttop = new CubeTree(new BoundingBox(new Vector3f(half.getX(), half.getY(), min.getZ()),
                new Vector3f(max.getX(), max.getY(), half.getZ())));
        southwesttop = new CubeTree(new BoundingBox(new Vector3f(min.getX(), half.getY(), half.getZ()),
                new Vector3f(half.getX(), max.getY(), max.getZ())));
        southeasttop = new CubeTree(new BoundingBox(new Vector3f(half.getX(), half.getY(), half.getZ()),
                new Vector3f(max.getX(), max.getY(), max.getZ())));

    }
}
