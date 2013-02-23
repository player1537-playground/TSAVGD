/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package character;

import levels.ConversationDisplay;
import event.*;
import java.util.ArrayList;
import levels.Resource;
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
    private boolean collision;

    public Person(String name) {
        super(name);
        fg.add(new ForceGenerator() {

            @Override
            public Vector3f getForce(PhysicalEntity e) {
                if (!((character.Person) e).isCollision()) {
                    e.setAwake(true);
                }
                return new Vector3f(0, -e.getMass() * EventTest.getGravity(), 0);
            }
        });
        setMovementPattern(new PersonMovement(this));
    }

    @Override
    public void collide(ArrayList<Triangle> cols) {
        collision = cols.size() > 0;
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

    public boolean isCollision() {
        return collision;
    }

    @Override
    public void load() {
        super.load();
        s = SoundManager.createSound(soundPath);
    }

    public void setModel(Model m) {
        super.setModel(m);
    }

    public void setMovementPattern(MovementPattern mp) {
        this.mp = mp;
        fg.add(mp);
    }
}

class PersonMovement extends MovementPattern {

    Vector3f force;
    Vector3f zero = new Vector3f();
    float maxSpeed = 8;
    final Person person;
    static int reset = 500;

    public PersonMovement(final Person person) {
        this.person = person;
        this.force = new Vector3f();
        this.current = new ForceGenerator() {

            @Override
            public Vector3f getForce(PhysicalEntity p) {
                if (!person.isInConversation()) {
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
        if (counter < reset * .95) {
            if (!person.isInConversation()) {
                float x = (float) Math.random() - 0.5f;
                float z = (float) Math.random() - 0.5f;
                float newAngle = (float) Math.atan2(x, z);
                person.setAngle((float) (newAngle * 180 / Math.PI) - 90);
                force.set(x, 0, z);
                force.normalise();
                force.scale(20 * person.getMass() * 2.0f);
            } else {
                force.set(zero);
            }
            counter = reset;
        }
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
                MessageCenter.addMessage("CHANGE");
            }
        }
    }
}
