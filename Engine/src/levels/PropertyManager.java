package levels;

import java.util.*;
import org.lwjgl.*;

public class PropertyManager {
    private static String filename = null;
    private static Properties prop = null;

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
	    (new File(gamedir)).mkdir();
	    filename = gamedir + "/event.inf";
	    
	}
    }
	    
	
    /* Call this either: in the constructor (bad idea, I think)
     *   or in the beginning of getValue(String) and setValue(String, String)
     *   only if it doesn't exist.  You could call it createUnlessExist(), and call
     *   it no matter what (in other words, moving the check inside of that function)
     */
    // null if not found
    public static String getString(String key) {
	createFileUnlessExists();
	return "rawr";
    }

    public static int getInteger(String key) {
	return Integer.parseInt(getString(key));
    }

    public static boolean getBoolean(String key) {
	return Boolean.parseBoolean(getString(key));
    }

    public static void setValue(String key, String value) {
	createFileUnlessExists();
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
}