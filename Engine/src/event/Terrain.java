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

    TerrainModel display;
    int indice;
    CubeTree<Triangle> planes;
    Player player;

    public Terrain(TerrainModel display, Model navigation, Player player) {

        this.display = display;
        this.player = player;
        init(navigation);

    }

    public Terrain(Player p) {

        this(TerrainModel.loadModel("village_disp_fixed.obj"), Model.loadModel("village_col.obj"), p);

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

        Vector3f position = player.getMiddle();
        float angle = player.yAngle;
        display.draw(position.getX(), position.getZ(), angle);

    }

    public ArrayList<Triangle> getTriangles(BoundingBox volume) {

        return planes.queryRange(volume);

    }

    @Override
    public void update(int delta) {
    }
    
}
