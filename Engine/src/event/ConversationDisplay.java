/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import character.Person;
import java.awt.Color;
import org.newdawn.slick.opengl.Texture;

/**
 *
 * @author Andy
 */
public class ConversationDisplay {

    static Person author;
    static String text;
    static final Texture back = Hud.load("conversation.png");
    static final Texture circle = Hud.load("circle.png");
    static HudGraphic textDisp = new HudGraphic(back, null, HudGraphic.loadFont("Comic Sans", 18, Color.white), 90, 36);
    static Menu disp = new Menu(0, Hud.height - 350, false, textDisp, true, new Menu[]{
                new Menu(Hud.width / 2 - 32, 220, false, new HudGraphic(circle, null), true, null, null)
            }, null);
    private static boolean finished = true;

    public static void startConversation(Person author, String text) {
        finished = false;
        ConversationDisplay.author = author;
        author.setConversation(true);
        ConversationDisplay.text = text;
        textDisp.setMessage(author.toString() + " - \n         " + text);
    }

    public static void update(int delta) {
    }

    public static void draw() {
        disp.draw();
    }

    public static void advance() {
        finish();
    }

    public static void finish() {
        finished = true;
        author.setConversation(false);
    }

    public static boolean isFinished() {
        return finished;
    }
}
