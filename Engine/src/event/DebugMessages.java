/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import java.util.HashMap;
import java.util.Set;
import org.newdawn.slick.UnicodeFont;

/**
 *
 * @author Andy
 */
public class DebugMessages {

    static HashMap<String, String> messages = new HashMap<String, String>();
    static final UnicodeFont font = HudGraphic.loadFont("Courier New", 20);
    private static boolean show;

    public static void addMessage(String name, String text) {
        messages.put(name, text);
    }

    public static void removeMessage(String name) {
        messages.remove(name);
    }

    public static void clear() {
        messages.clear();
    }

    public static void setShow(boolean showing) {
        show = showing;
    }

    public static boolean getShow() {
        return show;
    }

    public static void draw() {
        if (show) {
            String[] names = new String[messages.size()];
            messages.keySet().toArray(names);
            for (int i = 0; i < names.length; i++) {
                String name = names[i];
                font.drawString(10, 10 + i * 10, name + ": " + messages.get(name));
            }
        }
    }
}
