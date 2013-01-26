/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.opengl.Texture;

/**
 *
 * @author Andy
 */
public class HudItem implements DisplayableEntity {

    int width, height;
    String label;
    Texture texture;
    TextField t;
    static TrueTypeFont f;
    
    @Override
    public void draw() {
        
    }

    @Override
    public void update(int delta) {
    }

    
}
