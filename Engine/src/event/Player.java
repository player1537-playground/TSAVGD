/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import character.Person;
import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Andy
 */
public class Player extends PhysicalEntity {

    ViewPoint v;
    float xAngle = 0, yAngle = 0;
    float maxSpeed = 14;
    boolean collision = false;
    boolean verticalCollision = false;
    int count = 0;
    boolean space = false;
    PhysicalEntity[] rayCast;
    int rayCastCounter;
    boolean conversation;
    private float dXAngle, dYAngle, dZAngle;
    private boolean transitioning;
    private float duration;
    private float desiredXAngle, desiredYAngle;

    public Player() {

        super(new BoundingBox(new Vector3f(0, 5, 0), new Vector3f(0.6f, 2f, 0.6f)));
        v = new ViewPoint();
        fg.add(new ForceGenerator() {

            @Override
            public Vector3f getForce(PhysicalEntity e) {
                if (!conversation) {
                    if (count > 0) {
                        count--;
                    }

                    float x = 0, y = 0, z = 0;

                    boolean keyUp = KeyboardWrapper.get(Keyboard.KEY_UP).isDown() || KeyboardWrapper.get(Keyboard.KEY_W).isDown();
                    boolean keyDown = KeyboardWrapper.get(Keyboard.KEY_DOWN).isDown() || KeyboardWrapper.get(Keyboard.KEY_S).isDown();
                    boolean keyLeft = KeyboardWrapper.get(Keyboard.KEY_LEFT).isDown() || KeyboardWrapper.get(Keyboard.KEY_A).isDown();
                    boolean keyRight = KeyboardWrapper.get(Keyboard.KEY_RIGHT).isDown() || KeyboardWrapper.get(Keyboard.KEY_D).isDown();
                    boolean keyShift = KeyboardWrapper.get(Keyboard.KEY_LSHIFT).isDown();


                    if (keyUp) {
                        z += 1;
                    }
                    if (keyDown) {
                        z -= 1;
                    }
                    if (keyLeft) {
                        x -= 1;
                    }
                    if (keyRight) {
                        x += 1;
                    }
                    if (Math.abs(z) + Math.abs(x) == 2) {
                        x *= .7f;
                        z *= .7f;
                    }
                    if (collision) {
                        x *= 3;
                        z *= 3;
                        if (KeyboardWrapper.get(Keyboard.KEY_SPACE).isPressed() && count == 0) {
                            count = 80;
                            y = 100;
                        }
                    }
                    if (keyShift) {
                        y = 10;
                    }
                    float temp = yAngle + 90;
                    float newX = (float) (x * Math.sin(Math.toRadians(temp)) + z * Math.cos(Math.toRadians(temp)));
                    float newZ = -(float) (-x * Math.cos(Math.toRadians(temp)) + z * Math.sin(Math.toRadians(temp)));
                    Vector3f force = new Vector3f();
                    if (velocity.lengthSquared() > maxSpeed * maxSpeed) {
                        float scale = (float) (Vector2f.angle(new Vector2f(newX, newZ), (Vector2f) new Vector2f(velocity.x, velocity.z)) / Math.PI);
                        if (!Float.isNaN(scale)) {
                            force = new Vector3f(newX * scale, 0, newZ * scale);
                        }
                    } else {
                        force = (Vector3f) new Vector3f(newX, 0, newZ);
                    }
                    force.setY(y);
                    //force.scale(2000);
                    force.scale(100);
                    if (force.lengthSquared() != 0) {
                        e.setAwake(true);
                    }
                    return force;
                }
                return new Vector3f();
            }
        });

        rayCast = new PhysicalEntity[8];
        for (int i = 0; i < rayCast.length; i++) {
            rayCast[i] = new PhysicalEntity(new BoundingBox(new Vector3f(), new Vector3f(.1f, .1f, .1f))) {

                {
                    invMass = 1000;
                    collidable = false;
                }

                @Override
                public void collide(ArrayList<Triangle> cols) {
                }

                @Override
                public void collide(PhysicalEntity col) {
                    if (col instanceof Activatable) {
                        if (!MessageCenter.contains("Press E")) {
                            MessageCenter.addMessage("Press E", 300);
                        }
                        if (EventTest.isActivate()) {
                            ((Activatable) col).activate();
                        }
                    }
                }

                @Override
                public void update(int delta) {
                }
            };

            EventTest.addEntity(rayCast[i]);
            EventTest.addPhysicalEntity(rayCast[i]);
        }

    }

    public void draw() {
    }

    public void init() {

        v.applyPerspectiveMatrix();

    }

    @Override
    public void update(int delta) {

        if (transitioning) {
            setAngle(xAngle + dXAngle * delta, yAngle + dYAngle * delta);
            if ((duration -= delta) < 0) {
                stopTransition();
            }
        } else {
            if (!conversation) {
                yAngle += (float) (-EventTest.getDx() / (Display.getWidth() / 360f) / 2);
                xAngle += (float) EventTest.getDy() / (Display.getHeight() / 360f) / 2;
            } else {
                float dx = (float) EventTest.getDy() / (Display.getHeight() / 360f) / 6;
                float dy = (float) (-EventTest.getDx() / (Display.getWidth() / 360f) / 6);
                if(desiredXAngle - xAngle < 30 && dx < 0) {
                    xAngle += dx;
                }
                if(desiredXAngle - xAngle > -15 && dx > 0) {
                    xAngle += dx;
                }
                if(desiredYAngle - yAngle < 30 && dy < 0) {
                    yAngle += dy;
                }
                if(desiredYAngle - yAngle > -30 && dy > 0) {
                    yAngle += dy;
                }
            }
            yAngle %= 360;
            xAngle %= 360;
            
            if (yAngle < 0) {
                yAngle += 360;
            }
            if (xAngle > 85) {
                xAngle = 85f;
            }
            if (xAngle < -85) {
                xAngle = -85;
            }

            setAngle(xAngle, yAngle);

            if (!conversation) {
                rayCast[rayCastCounter].setPosition(getMiddle().translate(0, b.getDimension().getY() / 2, 0));
                float rayCastSpeed = 20;
                double radX = Math.toRadians(xAngle);
                double radY = Math.toRadians(yAngle);
                double horLength = Math.cos(radX);
                Vector3f rayCastVel = (Vector3f) (new Vector3f((float) (horLength * -Math.sin(radY)),
                        (float) Math.sin(radX), (float) (horLength * -Math.cos(radY)))).scale(rayCastSpeed);
                Vector3f.add(rayCastVel, velocity, rayCastVel);
                rayCast[rayCastCounter].setVelocity(rayCastVel);
                rayCast[rayCastCounter].integrate(delta / 1000f);
                if (++rayCastCounter == rayCast.length) {
                    rayCastCounter = 0;
                }

            }
        }

    }

    public void adjust() {

        Vector3f newPosition = b.getCenter();
        newPosition.translate(0, b.getHeight() / 2, 0);
        v.setPosition(newPosition);
        v.adjustToView(true);

    }

    @Override
    public void setPosition(Vector3f ve) {

        super.setPosition(ve);
        v.setPosition(ve.getX() + b.getWidth() / 2, ve.getY() + b.getHeight(), ve.getZ() + b.getDepth() / 2);

    }

    @Override
    public void collide(ArrayList<Triangle> cols) {
        collision = false;
        verticalCollision = false;
        for (Triangle col : cols) {
            collision = true;
            if (col.b < .71) {
                collision = true;
            }
        }
    }

    @Override
    public void collide(PhysicalEntity col) {
    }

    public void startConversation(Person pe) {

        conversation = true;
        velocity = new Vector3f();
        Vector3f pePosition = pe.b.getCenter().translate(0, b.getDimension().getY() / 2.5f, 0);
        Vector3f myPosition = getMiddle().translate(0, b.getDimension().getY() / 2, 0);
        Vector3f.sub(pePosition, myPosition, pePosition);
        float hypot = (float) Math.pow(Math.pow(pePosition.getX(), 2) + Math.pow(pePosition.getZ(), 2), .5f);
        float xAngle = (float) Math.toDegrees(Math.atan2(pePosition.getY(), hypot));
        float yAngle = (float) Math.toDegrees(Math.atan2(-pePosition.getX(), -pePosition.getZ()));
        if(yAngle < 0) {
            yAngle+=360;
        }
        transition(xAngle, yAngle, 1000);

    }

    void endConversation() {
        conversation = false;
    }

    public void transition(float desiredXAngle, float desiredYAngle, float duration) {
        this.transitioning = true;
        this.desiredXAngle = desiredXAngle;
        this.desiredYAngle = desiredYAngle;
        this.dXAngle = (desiredXAngle - xAngle) / duration;
        this.dYAngle = (desiredYAngle - yAngle) / duration;
        this.duration = duration;
    }

    public void stopTransition() {
        this.transitioning = false;
    }

    private void setAngle(float xAngle, float yAngle) {
        this.xAngle = xAngle;
        this.yAngle = yAngle;
        v.setAngle(xAngle, yAngle, 0);
    }
}
