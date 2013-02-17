package event;

import java.util.HashMap;
import org.lwjgl.input.*;

class KeyStatus {
    boolean pressed, oldpressed;
    public KeyStatus(boolean pressed) {
	this.oldpressed = false;
	this.pressed = pressed;
    }

    public boolean isUp() {
	return !this.pressed;
    }
    
    public boolean isDown() {
	return this.pressed;
    }

    public boolean isPressed() {
	return this.pressed && !this.oldpressed;
    }
    
    public void update(boolean pressed) {
	this.oldpressed = this.pressed;
	this.pressed = pressed;
    }

    public String toString() {
	return "{" + this.oldpressed + ", " + this.pressed + "}";
    }
}

class KeyboardWrapper {
    static HashMap<Integer, KeyStatus> map = new HashMap<Integer, KeyStatus>();
    static public KeyStatus put(int keyValue) {
	Integer key = new Integer(keyValue);
	if (map.containsKey(key)) {
	    map.get(key).update(Keyboard.isKeyDown(keyValue));
	} else {
	    map.put(key, new KeyStatus(Keyboard.isKeyDown(keyValue)));
	}
	return map.get(key);
    }

    static public KeyStatus get(int keyValue) {
	Integer key = new Integer(keyValue);
	if (map.containsKey(key)) {
	    return map.get(key);
	} else {
	    //System.err.println(key + " is not found in the keymap");
	    return new KeyStatus(false);
	}
    }
    public static String toAString() {
	return map.toString();
    }
}