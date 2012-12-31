/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package character;

import event.*;
import java.util.ArrayList;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Andy
 */
public abstract class MovementPattern implements ForceGenerator {
    
    int counter;
    ForceGenerator current;
    
    public MovementPattern() {
        this.counter = 0;
    }
    
    public void update(int delta) {
        counter--;
        if(counter < 0) {
            changeState();
        }
    }
    public Vector3f getForce(PhysicalEntity p) {
        return current.getForce(p);
    }
    
    public abstract void changeState();
    public abstract void collide(PhysicalEntity p);
    public abstract void collide(ArrayList<Triangle> p);
    
}
