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
public class Person extends AbstractEntity implements Activatable {

    MovementPattern mp;
    //Conversation c;
    static String soundPath = "res/ding.wav";
    Sound s;
    boolean conversation = false;

    public Person(Model m, MovementPattern mp) {
        super(m);
        this.mp = mp;
        this.fg.add(mp);
        s = SoundManager.createSound(soundPath);
    }

    public Person() {
        super(Model.loadModel("soldier_small.obj"));
        this.mp = new PersonMovement(this);
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
        //s.setPosition(b.getCenter());
        //s.play();
    }

    @Override
    public void update(int delta) {
        mp.update(delta);
        if (conversation) {
            velocity.set(0, 0, 0);
            Vector3f diff = Vector3f.sub(EventTest.p.getMiddle(), getMiddle(), null);
            if (diff.lengthSquared() > 64) {
                ConversationDisplay.finish();
            }
            setAngle((float) Math.toDegrees(Math.atan2(diff.x, diff.z)) - 90);
        } else {
        }
    }

    @Override
    public void activate() {
        startConversation();
    }

    public void startConversation() {
        ConversationDisplay.startConversation(this, "HEY WASSUPP DAWG");
    }

    public void setConversation(boolean conv) {
        this.conversation = conv;
    }

    public boolean isInConversation() {
        return conversation;
    }
}

class PersonMovement extends MovementPattern {

    Vector3f force;
    Vector3f zero = new Vector3f();
    float maxSpeed = 10;
    final Person person;

    public PersonMovement(final Person person) {
        this.person = person;
        this.force = new Vector3f();
        this.current = new ForceGenerator() {

            @Override
            public Vector3f getForce(PhysicalEntity p) {
                if(!person.isInConversation()) {
                    p.setAwake(true);
                }
                if (person.velocity.lengthSquared() > maxSpeed * maxSpeed) {
                    return zero;
                } else {
                    return force;
                }
            }
        };
    }

    @Override
    public void changeState() {
        if (!person.isInConversation()) {
            float x = (float) Math.random() - 0.5f;
            float z = (float) Math.random() - 0.5f;
            float newAngle = (float) Math.atan2(x, z);
            person.setAngle((float) (newAngle * 180 / Math.PI) - 90);
            force.set(x, 0, z);
            force.normalise();
            force.scale(20 * person.getMass() * 2.5f);
        } else  {
            force.set(zero);
        }
        counter = 1000;
    }

    @Override
    public void collide(PhysicalEntity p) {
        if (p.collidable) {
            changeState();
        }
    }

    @Override
    public void collide(ArrayList<Triangle> collisions) {
        for (Triangle col : collisions) {
            if (col.b < .71) {
                changeState();
            }
        }
    }
}
