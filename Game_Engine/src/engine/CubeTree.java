/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

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
            return true;
        }
        if (northeasttop.insert(object)) {
            return true;
        }
        if (southwesttop.insert(object)) {
            return true;
        }
        if (southeasttop.insert(object)) {
            return true;
        }

        if (northwestbottom.insert(object)) {
            return true;
        }
        if (northeastbottom.insert(object)) {
            return true;
        }
        if (southwestbottom.insert(object)) {
            return true;
        }
        if (southeastbottom.insert(object)) {
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

        objectsInRange.addAll(northwesttop.queryRange(range));
        objectsInRange.addAll(northeasttop.queryRange(range));
        objectsInRange.addAll(southwesttop.queryRange(range));
        objectsInRange.addAll(southeasttop.queryRange(range));

        objectsInRange.addAll(northwestbottom.queryRange(range));
        objectsInRange.addAll(northeastbottom.queryRange(range));
        objectsInRange.addAll(southwestbottom.queryRange(range));
        objectsInRange.addAll(southeastbottom.queryRange(range));

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
