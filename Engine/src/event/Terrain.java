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
public class Terrain implements DisplayableEntity {

    Model display;
    int indice;
    CubeTree<Triangle> planes;

    public Terrain(Model display, Model navigation) {

        this.display = display;
        init(navigation);

    }

    public Terrain() {

        this(Model.loadModel("village_disp.obj"), Model.loadModel("village_col.obj"));

    }

    public void init(Model navigation) {

        planes = new CubeTree<Triangle>(navigation.getBounds());
        for (Face face : navigation.faces) {

            Vector3f[] verts = new Vector3f[3];

            for (int i = 0; i < 3; i++) {
                verts[i] = navigation.verts[face.verts[i]];
            }

            planes.insert(new Triangle(verts, navigation.normals[face.normals[0]]));
        }

    }

    @Override
    public void draw() {

        display.draw();

    }

    public ArrayList<Triangle> getTriangles(BoundingBox volume) {

        return planes.queryRange(volume);

    }

    @Override
    public void update(int delta) {
    }
    
}
