/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import java.util.ArrayList;
import levels.Resource;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Andy
 */
public class Terrain implements DisplayableEntity, Resource {

    public TerrainModel display;
    public Model navigation;
    public CubeTree<Triangle> planes;
    public Player player;

    public Terrain() {
    }

    public Terrain(TerrainModel display, Model navigation, Player player) {

        this.display = display;
        this.navigation = navigation;
        this.player = player;

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

    @Override
    public String getName() {
        return "retean;";
    }

    @Override
    public void load() {
        init(navigation);
    }
}
