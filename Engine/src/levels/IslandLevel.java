package levels;

import character.Person;
import event.*;
import org.lwjgl.util.vector.Vector3f;

public class IslandLevel extends Level {

    public void init() {
        EventTest.init();
        EventTest.setGravity(20);

        TerrainModel terDisp = new TerrainModel("islandD", "village_disp_fixed.obj");
        addResource(terDisp);
        Model terCol = new Model("islandP", "village_col.obj");
        addResource(terCol);
        Terrain t = new Terrain("Island");
        addResource(t);
        t.setDisplay(terDisp);
        t.setNavigation(terCol);
        EventTest.addEntity(t);
        EventTest.addDisplayableEntity(t);
        EventTest.setTerrain(t);

        Player player = new Player();
        player.b.setPosition(new Vector3f(0, 10, 0));
        EventTest.addEntity(player);
        EventTest.addPhysicalEntity(player);
        EventTest.setPlayer(player);

        Model soldierModel = new Model("Martha's Model", "shark.obj");
        addResource(soldierModel);

        for (int i = 0; i < 4; i++) {
            Person per = new Person("Martha" + i);
            per.b.setPosition(new Vector3f(0, 5 * i + 10, 20));
            per.setModel(soldierModel);
            addResource(per);
            EventTest.addAbstractEntity(per);
        }

	

    }
}
