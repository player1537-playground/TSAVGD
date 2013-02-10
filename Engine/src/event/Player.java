/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

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
    float maxSpeed = 16;
    boolean collision = false;
    int count = 0;
    boolean space = false;
    PhysicalEntity[] rayCast;
    int rayCastCounter;

    public Player() {

        super(new BoundingBox(new Vector3f(0, 5, 0), new Vector3f(0.6f, 2f, 0.6f)));
        v = new ViewPoint();
        fg.add(new ForceGenerator() {

            @Override
            public Vector3f getForce(PhysicalEntity e) {
                if (count > 0) {
                    count--;
                }

                float x = 0, y = 0, z = 0;

                boolean keyUp = Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W);
                boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S);
                boolean keyLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A);
                boolean keyRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D);
                boolean keySpace = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
                boolean keyShift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);

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
                    if (!space && keySpace && count == 0) {
                        count = 80;
                        y = 100;
                    }
                }
                if (keyShift) {
                    y = 10;
                }
                space = keySpace;
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
                        if(EventTest.isActivate()) {
                            ((Activatable)col).activate();
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
        //Mouse
        yAngle += (float) (-EventTest.getDx() / (Display.getWidth() / 360f) / 2);
        xAngle += (float) EventTest.getDy() / (Display.getHeight() / 360f) / 2;
        yAngle %= 360;
        if (yAngle < 0) {
            yAngle += 360;
        }
        if (xAngle > 85) {
            xAngle = 85f;
        }
        if (xAngle < -85) {
            xAngle = -85;
        }

        v.setAngle(xAngle, yAngle, 0);

        rayCast[rayCastCounter].setPosition(getMiddle().translate(0, b.getDimension().getY() / 2, 0));
        float rayCastSpeed = 60;
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

    public void collide(ArrayList<Triangle> collisions) {
        collision = false;
        for (Triangle col : collisions) {
            if (col.b > .71) {
                collision = true;
                break;
            }
        }
    }

    @Override
    public void collide(PhysicalEntity cols) {
    }
}
