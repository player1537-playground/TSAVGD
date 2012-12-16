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
public interface AnimationUpdateScheme {
    
    public void updateOrientation(Quaternion q, float delta);
    
}
