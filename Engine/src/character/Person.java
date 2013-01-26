/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package character;

import event.*;
import java.util.ArrayList;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector3f;
import sound.*;
import static org.lwjgl.openal.AL10.*;

/**
 *
 * @author Andy
 */
public class Person extends AbstractEntity {

    MovementPattern mp;
    //Conversation c;
    static String soundPath = "res/ding.wav";
    Sound s;

    public Person(Model m, MovementPattern mp) {
        super(m);
        this.mp = mp;
        this.fg.add(mp);
        s = SoundManager.createSound(soundPath);
    }

    public Person() {
        super(Model.loadModel("soldier_small.obj"));
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
                force.set((float) Math.random() - 0.5f, 0, (float) Math.random() - 0.5f);
                force.scale(20 * getMass() * 2.5f);
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
        s = SoundManager.createSound(soundPath);

    }

    @Override
    public void collide(ArrayList<Triangle> cols) {
        mp.collide(cols);
    }

    @Override
    public void collide(PhysicalEntity col) {
        mp.collide(col);
        s.setPosition(b.getCenter());
        s.play();
    }

    @Override
    public void update(int delta) {
        mp.update(delta);
    }
}
