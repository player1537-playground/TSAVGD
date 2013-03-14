/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sound;

/**
 *
 * @author Andy
 */
import event.Entity;
import event.EventTest;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import static org.lwjgl.openal.AL10.*;
import org.lwjgl.util.WaveData;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.util.ResourceLoader;

public class SoundManager {

    static HashMap<String, Integer> buffers;
    static HashMap<String, Integer> lengths;
    static ArrayList<Sound> sounds;
    private static boolean muted;

    public static void create() {
        try {
            AL.create();
            alListener(AL_ORIENTATION, (FloatBuffer) BufferUtils.createFloatBuffer(6).put(new float[]{0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f}).flip());
        } catch (LWJGLException e) {
            e.printStackTrace();
            AL.destroy();
        }
        buffers = new HashMap<String, Integer>();
        lengths = new HashMap<String, Integer>();
        sounds = new ArrayList<Sound>();

    }

    public static void destroy() {
        Iterator it = buffers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            alDeleteBuffers((Integer) pairs.getValue());
            it.remove();
        }
        AL.destroy();
    }

    public static void update(int delta) {
        Vector3f pPos = EventTest.p.getMiddle();
        alListener3f(AL_POSITION, pPos.getX() / 100, pPos.getY() / 100, pPos.getZ() / 100);
        for(Sound s : sounds) {
            s.update(delta);
        }
    }

    public static Sound createSound(int buffer, int delay) {
        Sound s = new Sound(buffer, delay);
        sounds.add(s);
        return s;
    }
    public static Sound createSound(String name) {
        Sound s = new Sound(bufferFromPath(name), lengthFromPath(name));
        sounds.add(s);
        return s;
    }
    
    public static void mute(boolean muted) {
        SoundManager.muted = muted;
        for(Sound s : sounds) {
            s.mute(muted);
        }
    }
    
    static private void loadSound(String path) {
	WaveData data = null;
        if (!buffers.containsKey(path)) {
	    data = WaveData.create(new BufferedInputStream(ResourceLoader.getResourceAsStream(path)));
            int buffer = alGenBuffers();
            alBufferData(buffer, data.format, data.data, data.samplerate);
            int length = (int)((float)data.data.capacity() / 
			       (data.format == AL_FORMAT_STEREO16 ? 4 : 2) /
			       data.samplerate * 1000);
            System.out.println("Sound " + path);
            lengths.put(path, length);
            data.dispose();
            buffers.put(path, buffer);
        }
    }
    
    public static int bufferFromPath(String path) {
        loadSound(path);
        return buffers.get(path);
    }

    private static int lengthFromPath(String path) {
        loadSound(path);
        return lengths.get(path);
    }

    public static boolean isMuted() {
        return muted;
    }

    public static void setVolume(float volume) {
        for(Sound s : sounds) {
            s.setMult(volume);
        }
    }
}
