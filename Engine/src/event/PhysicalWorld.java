 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import java.util.ArrayList;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Andy
 */
public class PhysicalWorld {

    Terrain t;
    ArrayList<PhysicalEntity> ents;
    static final float restitution = 0.2f;

    public PhysicalWorld(Terrain t, PhysicalEntity[] ents) {

        this.t = t;
        this.ents = new ArrayList<PhysicalEntity>();
        for (PhysicalEntity e : ents) {
            this.ents.add(e);
        }

    }

    public PhysicalWorld(Terrain t, ArrayList<PhysicalEntity> things) {
        this.t = t;
        this.ents = things;
    }

    public void update(int delta) {
        float time = delta / 1000f;

        for (PhysicalEntity e : ents) {

            e.setAwake(!e.canSleep());
            e.updateForces();
            if (e.isAwake) {
                e.integrate(time);
            }
            e.forceAccum = new Vector3f();

        }

        for (PhysicalEntity e : ents) {

            ArrayList<Triangle> collisions = t.getTriangles(e.b);
            if (e.isCollidable() && !collisions.isEmpty()) {
                Vector3f[] vertexes = e.getVertexes();
                for (Vector3f v : vertexes) {
                    float lowestVelocity = Float.MAX_VALUE;
                    Triangle tri = null;
                    for (Triangle t : collisions) {
                        float angle = (float) Math.toDegrees(Vector3f.angle(t.getNormal(), Vector3f.sub(e.getMiddle(), t.getMiddle(), null)));
                        if (angle > 90) {
                            continue;
                        }
                        angle = (float) Math.toDegrees(Vector3f.angle(t.getNormal(), Vector3f.sub(v, t.getMiddle(), null)));
                        if (angle < 90) {
                            continue;
                        }
                        float seperatingVelocity = Vector3f.dot(e.velocity, t.getNormal());
                        if (seperatingVelocity < lowestVelocity) {
                            lowestVelocity = seperatingVelocity;
                            tri = t;
                        }
                    }

                    if (lowestVelocity >= 0) {
                        continue;
                    }

                    float newVelocity = -lowestVelocity * restitution;

                    Vector3f accCausedVelocity = e.acceleration;
                    float accCausedSepVelocity = Vector3f.dot(accCausedVelocity, tri.getNormal()) * time;
                    if (accCausedSepVelocity < 0) {
                        newVelocity += restitution * accCausedSepVelocity;
                        if (newVelocity < 0) {
                            newVelocity = 0;
                        }
                    }

                    Vector3f impulseVec = (Vector3f) tri.getNormal().scale(-lowestVelocity);
                    Vector3f.add(e.velocity, impulseVec, e.velocity);

                    e.addForce((Vector3f) new Vector3f(e.velocity).normalise().scale(-1f * 20 / e.invMass * tri.getNormal().getY()));
                    //System.out.println((Vector3f) copy(e.velocity).normalise().scale(-1f * 50 / e.invMass));
                    impulseVec = (Vector3f) tri.getNormal().scale(newVelocity);
                    Vector3f.add(e.velocity, impulseVec, e.velocity);

                }
            }
            e.collide(collisions);
        }

        ArrayList<PhysicalEntity> temporary = new ArrayList<PhysicalEntity>(ents);

        for (PhysicalEntity e : ents) {

            for (int i = 0; i < temporary.size(); i++) {
                PhysicalEntity tempEnt = temporary.get(i);
                if (!e.equals(tempEnt)) {
                    if (e.b.intersects(tempEnt.b)) {
                        if (e.isCollidable() && tempEnt.isCollidable()) {
                            Vector3f bounce = new Vector3f();
                            Vector3f relativePosition = new Vector3f();
                            Vector3f.sub(e.getMiddle(), tempEnt.getMiddle(), relativePosition);
                            if (relativePosition.lengthSquared() < 0) {
                                continue;
                            }
                            if (relativePosition.lengthSquared() != 0) {
                                relativePosition.normalise();
                            }
                            float sepVelocity = Vector3f.dot(Vector3f.sub(e.velocity, tempEnt.velocity, null), relativePosition);
                            if (sepVelocity >= 0) {
                                continue;
                            }
                            float totalInvMass = e.invMass + tempEnt.invMass;
                            relativePosition.scale(sepVelocity * tempEnt.invMass / totalInvMass);
                            Vector3f.add(tempEnt.velocity, relativePosition, tempEnt.velocity);
                            relativePosition.normalise().scale(sepVelocity * e.invMass / totalInvMass);
                            Vector3f.add(e.velocity, relativePosition, e.velocity);
                            e.setAwake(true);
                            tempEnt.setAwake(true);
                        }
                        e.collide(tempEnt);
                        tempEnt.collide(e);
                    }
                }
            }
            temporary.remove(e);
        }
    }

    public boolean valueInRange(float value, float min, float max) {

        return (value >= min) && (value <= max);

    }

    public void remove(Entity e) {
        ents.remove(e);
    }

    void add(PhysicalEntity e) {
        ents.add(e);
    }
}