/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

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
    float maxSpeed = 15;
    boolean collision = false;
    int count = 0;
    boolean space = false;

    public Player() {

        super(new BoundingBox(new Vector3f(0, 10, 0), new Vector3f(0.5f, 1.5f, 0.5f)));
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
                        y = 170;
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

    }

    public void draw() {
    }

    public void init() {

        v.applyPerspectiveMatrix();

    }

    @Override
    public void update(int delta) {

        //Mouse
        yAngle += (float) (-Mouse.getDX() / (Display.getWidth() / 360f) / 2) % 360;
        xAngle += (float) Mouse.getDY() / (Display.getHeight() / 360f) / 2;

        if (xAngle > 85) {
            xAngle = 85f;
        }
        if (xAngle < -85) {
            xAngle = -85;
        }

        v.setAngle(xAngle, yAngle, 0);

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
