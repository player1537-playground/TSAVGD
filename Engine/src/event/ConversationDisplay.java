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
    static  Texture back;
    static  Texture circle;
    static HudGraphic textDisp;
    static Menu disp;
    private static boolean finished;

    public static void init() {
	back = Hud.load("res/conversation.png");
	circle = Hud.load("res/circle.png");
	textDisp = new HudGraphic(back, null, HudGraphic.loadFont("Comic Sans", 18, Color.white), 90, 36);
	disp = new Menu(0, Hud.height - 350, false, textDisp, true, new Menu[]{
                new Menu(Hud.width / 2 - 32, 220, false, new HudGraphic(circle, null), true, null, null)
            }, null);
	finished = true;
    }

    public static void startConversation(Person author, String text) {
        EventTest.p.velocity.set(0, 0, 0);
        finished = false;
        ConversationDisplay.author = author;
        author.setConversation(true);
        EventTest.p.startConversation(author);
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
        EventTest.p.endConversation();
    }

    public static boolean isFinished() {
        return finished;
    }
}
