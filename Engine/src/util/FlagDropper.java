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
public class FlagDropper {

    static Model flagModel;
    static ArrayList<Flag> flags;

    public static void init() {
        flagModel = Model.loadModel("flag.obj");
        flags = new ArrayList<Flag>();
    }
    
    public static void update(int delta) {
        if (EventTest.isActivate()) {
            Flag f = new Flag();
            flags.add(f);
            EventTest.addAbstractEntity(f);
        }
    }
    
    public static void destroy() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < flags.size(); i++) {
            Vector3f p = flags.get(i).b.getMin();
            sb.append("" + i + ": X- " + p.getX() + " Y- " + p.getY() + " Z- " + p.getZ() + "\n");
        }
        System.out.println(sb);
    }
}

class Flag extends AbstractEntity {

    public Flag() {
        super("FLAG");
        setModel(FlagDropper.flagModel);
        this.collidable = false;
        this.load();
        this.setPosition(new Vector3f(EventTest.p.b.getMin()));
    }
    @Override
    public void collide(ArrayList<Triangle> cols) {
    }

    @Override
    public void collide(PhysicalEntity col) {
    }

    @Override
    public void update(int delta) {
    }
    
}
