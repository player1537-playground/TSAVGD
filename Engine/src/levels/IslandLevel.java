package levels;

import event.*;
import org.lwjgl.util.vector.Vector3f;

public class IslandLevel extends Level {

    public void init() {
        EventTest.init();
        TerrainModel terDisp = new TerrainModel("islandD", "village_disp_fixed.obj");
        Model terCol = new Model("islandP", "village_col.obj");
        Terrain t = new Terrain(terDisp, terCol, EventTest.p);
        character.Person p = new character.Person();
        p.b.setPosition(new Vector3f(20, 20, 20));
        addResource(terDisp);
        addResource(terCol);
        addResource(t);
        EventTest.addEntity(t);
        EventTest.addDisplayableEntity(t);
        EventTest.addAbstractEntity(p);
        EventTest.setTerrain(t);
    }
}
