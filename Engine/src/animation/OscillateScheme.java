/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package animation;

import org.lwjgl.util.vector.Quaternion;

/**
 *
 * @author Andy
 */
public class OscillateScheme implements AnimationUpdateScheme{
    
    float speed;
    float lower, upper;
    
    public OscillateScheme(float speed, float lower, float upper) {
        this.speed = speed;
        this.lower = lower;
        this.upper = upper;
    }
    
    @Override
    public void updateOrientation(Quaternion q, float delta) {
        float angle = q.getW() + speed * delta;
        if(angle < lower) {
            angle = lower;
            speed *= -1;
        }
        if(angle > upper) {
            angle = upper;
            speed *= -1;
        }
        q.setW(angle);
    }
    
}
