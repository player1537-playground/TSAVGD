package levels;

import java.util.*;
import org.lwjgl.*;
import java.io.*;
import event.*;

public class PropertyManager {
    private static String filename = null;
    private static Properties prop = null;
    private static File file = null;

    private static void createFileUnlessExists() {
	if (filename == null) {
	    // Get the filename then create the file
	    String homedir = System.getProperty("user.home");
	    System.err.println(homedir);
	    int platform = LWJGLUtil.getPlatform();
	    String gamedir;
	    switch (platform) {
	    case LWJGLUtil.PLATFORM_WINDOWS:
		gamedir = homedir + "\\VoyagerGame";
		break;
	    case LWJGLUtil.PLATFORM_LINUX:
	    case LWJGLUtil.PLATFORM_MACOSX:
		gamedir = homedir + "/.voyager-game";
		break;
	    default:
		// Maybe solaris?
		System.err.println("Platform: " + platform);
		gamedir = homedir;
	    }
	    File gamedirFile = new File(gamedir);
	    if (!gamedirFile.exists()) {
		gamedirFile.mkdir();
	    }
	    filename = gamedir + "/event.inf";
	    file = new File(filename);	
	    prop = new Properties();
	    if (!file.exists()) {
		saveProperties();
	    } else {
		try {
		    prop.load(new FileInputStream(file));
		} catch (FileNotFoundException e) {
		    System.err.println("You _should_ exist...");
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    private static void saveProperties() {
	try {
	    prop.store(new FileOutputStream(file), "This file keeps track of events in Voyager");
	} catch (FileNotFoundException e) {
	    System.err.println("That's the point... you don't exist yet, I'm creating you.");
	} catch (IOException e) {
	    e.printStackTrace();
	}	    
    }
	
    /* Call this either: in the constructor (bad idea, I think)
     *   or in the beginning of getValue(String) and setValue(String, String)
     *   only if it doesn't exist.  You could call it createUnlessExist(), and call
     *   it no matter what (in other words, moving the check inside of that function)
     */
    // null if not found
    public static String getString(String key) {
	return getString(key, "");
    }

    public static String getString(String key, String def) {
	createFileUnlessExists();
	return prop.getProperty(key, def);
    }

    public static int getInteger(String key) {
	return getInteger(key, 0);
    }
    
    public static int getInteger(String key, int def) {
	return Integer.parseInt(getString(key, "" + def));
    }

    public static boolean getBoolean(String key) {
	return getBoolean(key, false);
    }
    
    public static boolean getBoolean(String key, boolean def) {
	return Boolean.parseBoolean(getString(key, "" + def));
    }

    public static void setValue(String key, String value) {
	createFileUnlessExists();
	prop.setProperty(key, value);
	saveProperties();
    }

    public static void setValue(String key, boolean value) {
	setValue(key, "" + value);
    }

    public static void setValue(String key, int value) {
	setValue(key, "" + value);
    }

    public static void setValue(String key) {
	setValue(key, true);
    }

    /* Special functions */
    private static int NUM_ITEMS = 9;
    private static String utilInvName(int slot) {
	return "inv" + slot;
    }

    private static String utilInvCount(int slot) {
	return utilInvName(slot) + "-count";
    }

    public static ArrayList<InventoryItem> utilGetInventoryItems() {
	ArrayList<InventoryItem> toReturn = new ArrayList<InventoryItem>();
	for (int i=1; i<=NUM_ITEMS; i++) {
	    String item = getString(utilInvName(i), null);
	    if (item != null) {
		int count = getInteger(utilInvCount(i));
		toReturn.add(new InventoryItem(item, count));
	    }
	}
	return toReturn;
    }
}