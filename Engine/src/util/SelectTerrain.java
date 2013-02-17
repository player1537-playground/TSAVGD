/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import event.*;
import java.util.ArrayList;
import org.lwjgl.util.vector.Vector3f;
/**
 *
 * @author Andy
 */
public class SelectTerrain extends Terrain {

    public SelectTerrain(TerrainModel display, Model navigation, Player player) {

        super();
        this.display = display;
        this.player = player;
        planes = new CubeTree<Triangle>(navigation.getBounds());
        for (Face face : navigation.faces) {

            Vector3f[] verts = new Vector3f[3];

            for (int i = 0; i < 3; i++) {
                verts[i] = navigation.verts[face.verts[i]];
            }

            planes.insert(new SelectTriangle(verts, navigation.normals[face.normals[0]]));
        }

    }

    public SelectTerrain(Player p) {

        this(TerrainModel.loadModel("village_disp_fixed.obj"), Model.loadModel("village_col.obj"), p);

    }
    
}
