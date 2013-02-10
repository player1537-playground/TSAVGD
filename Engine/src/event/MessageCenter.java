/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Andy
 */
public class MessageCenter {

    private static ArrayList<Message> messages = new ArrayList<Message>();
    private static int width, height;
    private static int maxMessages;
    private static int messageHeight;
    private static int defautTimeout = 3000;

    public static void setAttributes(int width, int height, int maxMessages) {
        MessageCenter.width = width;
        MessageCenter.height = height;
        MessageCenter.maxMessages = maxMessages;
        MessageCenter.messageHeight = height / maxMessages;
    }

    public static void addMessage(String text) {
        addMessage(text, defautTimeout);
    }

    public static void addMessage(String text, int timeout) {
        if (messages.size() >= maxMessages) {
            messages.remove(0);
        }
        messages.add(new Message(text, timeout));
    }

    public static void clear() {
        messages.clear();
    }

    public static void draw() {
        glPushMatrix();
        glTranslatef(5, height - messages.size() * messageHeight, 0);
        for (int i = 0; i < messages.size(); i++) {
            messages.get(i).draw();
            glTranslatef(0, messageHeight, 0);
        }
        glPopMatrix();
    }

    public static void update(int delta) {
        for (int i = 0; i < messages.size(); i++) {
            Message m = messages.get(i);
            m.update(delta);
            if (m.timeout < 0) {
                messages.remove(i);
            }
        }
    }

    static boolean contains(String text) {
        for (Message m : messages) {
            if (m.text.equals(text)) {
                return true;
            }
        }
        return false;
    }

    static void remove(String text) {
        for (int i = 0; i < messages.size(); i++) {
            if(messages.get(i).text.equals(text)) {
                messages.remove(i);
            }
        }
    }
}

class Message implements DisplayableEntity {

    String text;
    int timeout;
    HudGraphic hg;
    private static UnicodeFont uf = HudGraphic.loadFont("Comic Sans", 16);

    public Message(String text, int timeout) {
        this.text = text;
        this.timeout = timeout;
        hg = new HudGraphic(null, text, uf, 0, 0);
    }

    @Override
    public void draw() {
        hg.draw();
    }

    @Override
    public void update(int delta) {
        timeout -= delta;
    }
}