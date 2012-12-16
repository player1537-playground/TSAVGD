import java.util.*;

class PropertyManager {
    static HashMap<String, String> map = new HashMap<String, String>();
    private static String getRandomValue() {
	switch ((int)(Math.random()*3)) {
	case 0: return null;
	case 1: return "";
	default: return "rawr";
	}
    }
    public static String getValue(String s) {
	System.out.println("Accessing property " + s);
	if (PropertyManager.map.containsKey(s)) {
	    return PropertyManager.map.get(s);
	} else {
	    String ret = getRandomValue();
	    PropertyManager.map.put(s, ret);
	    return ret;
	}
    }
    public static void setValue(String s, String val) {
	PropertyManager.map.put(s, val);
    }
    public static void setValue(String s) {
	setValue(s, "true");
    }
    public static void printMap() {
	System.out.println(PropertyManager.map);
    }
}
