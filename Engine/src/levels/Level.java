package levels;

import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.opengl.Display;

public abstract class Level {

    ArrayList<Resource> resourcesToLoad = new ArrayList<Resource>();
    HashMap<String, Resource> resources = new HashMap<String, Resource>();

    public abstract void init();

    public void load() {
        for (Resource r : resourcesToLoad) {
            r.load();
            resources.put(r.getName(), r);
        System.out.println("Resource " + r.getName() + " loaded.");
            Display.sync(30);
        }
    }

    public void addResource(Resource r) {
        resourcesToLoad.add(r);
    }
}