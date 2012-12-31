/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package character;

import event.*;
import java.util.ArrayList;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Andy
 */
public class Person extends AbstractEntity {

    MovementPattern mp;
    //Conversation c;

    public Person(Model m, MovementPattern mp) {
        super(m);
        this.mp = mp;
        this.fg.add(mp);
    }

    public Person() {
        super(Model.loadModel("soldier.obj"));
        this.mp = new MovementPattern() {

            Vector3f force;
            Vector3f zero = new Vector3f();
            float maxSpeed = 10;

            {
                force = new Vector3f();
                current = new ForceGenerator() {

                    @Override
                    public Vector3f getForce(PhysicalEntity p) {
                        if (velocity.lengthSquared() > maxSpeed * maxSpeed) {
                            return zero;
                        } else {
                            return force;
                        }
                    }
                };
            }

            @Override
            public void changeState() {
                force.set((float) Math.random(), (float) Math.random(), (float) Math.random());
                force.scale(20 * getMass() * 1.5f);
                counter = 1000;
            }

            @Override
            public void collide(PhysicalEntity p) {
                changeState();
            }

            @Override
            public void collide(ArrayList<Triangle> collisions) {
                for (Triangle col : collisions) {
                    if (col.b < .71) {
                        changeState();
                    }
                }
            }
        };
        this.fg.add(mp);

    }

    @Override
    public void collide(ArrayList<Triangle> cols) {
        mp.collide(cols);
    }

    @Override
    public void collide(PhysicalEntity col) {
        mp.collide(col);
    }

    @Override
    public void update(int delta) {
        mp.update(delta);
    }
}
