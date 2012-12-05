/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Andy
 */
public class EventTest {

    static final float width = 1024;
    static final float height = 768;
    static long lastFrame = 0;
    static long lastFrameBottleNeck = 0;
    static Hud h;
    static Player p;
    static Terrain ter;
    static ArrayList<Entity> e;
    static ArrayList<DisplayableEntity> de;
    static ArrayList<PhysicalEntity> pe;
    static PhysicalWorld w;
    static boolean paused = false;
    static boolean debounce = true;
    static float dx, dy;

    public static void main(String[] argv) {

        init();
        run();
        destroy();

    }

    private static void init() {

        try {

            //Display
            Display.setDisplayMode(new DisplayMode((int) width, (int) height));
            Display.setVSyncEnabled(true);
            Display.setTitle("Engine");
            Display.create();

            //Keyboard
            Keyboard.create();

            //Mouse
            Mouse.setGrabbed(true);
            Mouse.create();

            //OpenGL
            glClearColor(0f, 0f, 0f, 0f);

            glEnable(GL_DEPTH_TEST);

            glEnable(GL_TEXTURE_2D);

            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

            glEnable(GL_CULL_FACE);

            glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_FASTEST);

            glShadeModel(GL_SMOOTH);

            glDisable(GL_DITHER);

            glEnable(GL_LIGHTING);
            glEnable(GL_LIGHT0);
            glLightModel(GL_LIGHT_MODEL_AMBIENT, asFloatBuffer(new float[]{0.4f, 0.4f, 0.3f, 1f}));
            glLight(GL_LIGHT0, GL_DIFFUSE, asFloatBuffer(new float[]{.8f, .8f, .8f, 1}));
            glLight(GL_LIGHT0, GL_POSITION, asFloatBuffer(new float[]{0f, 200f, 0f, 1}));
            glEnable(GL_COLOR_MATERIAL);
            glColorMaterial(GL_FRONT, GL_DIFFUSE);
            //float specReflection[] = {0.3f, 0.3f, 0.3f, 1f};
            //glMaterial(GL_FRONT, GL_SPECULAR, asFloatBuffer(specReflection));
            //glMateriali(GL_FRONT, GL_SHININESS, 30);

            glFogi(GL_FOG_MODE, GL_LINEAR);        // Fog Mode
            glFog(GL_FOG_COLOR, asFloatBuffer(new float[]{0.5f, 0.5f, 0.4f, 1f}));            // Set Fog Color
            glFogf(GL_FOG_DENSITY, 0.35f);              // How Dense Will The Fog Be
            glHint(GL_FOG_HINT, GL_DONT_CARE);          // Fog Hint Value
            glFogf(GL_FOG_START, 150f);             // Fog Start Depth
            glFogf(GL_FOG_END, 300f);               // Fog End Depth
            glEnable(GL_FOG);                   // Enables GL_FOG

            //Misc
            p = new Player();
            p.init();
            p.setPosition(new Vector3f(0, 100, 0));
            p.fg.add(new ForceGenerator() {

                @Override
                public Vector3f getForce(PhysicalEntity e) {
                    return new Vector3f(0, -e.getMass() * 20, 0);
                }
            });
            ter = new Terrain();
            SkyDome sky = new SkyDome();
            h = new Hud(width, height);
            e = new ArrayList<Entity>();
            e.add(sky);
            e.add(ter);
            e.add(p);
            e.add(h);

            de = new ArrayList<DisplayableEntity>();
            de.add(sky);
            de.add(ter);
            de.add(h);

            pe = new ArrayList<PhysicalEntity>();
            pe.add(p);

            w = new PhysicalWorld(ter, pe);

            //Timer
            getDelta();
            getDeltaBottleNeck();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    private static void run() {

        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {

            getDeltaBottleNeck();
            
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            int delta = getDelta();
            System.out.println("FPS " + 1000f / delta);

            processInputs();
            
            for (int i = 0; i < 3; i++) {
                update(delta / 3);
            }

            getDeltaBottleNeck();
            render();
            
            Display.update();
            Display.sync(60);

        }

    }

    private static void destroy() {

        Display.destroy();
        Keyboard.destroy();
        Mouse.destroy();
        System.exit(0);

    }

    public static long getTime() {

        return (Sys.getTime() * 1000) / Sys.getTimerResolution();

    }

    private static int getDelta() {

        long currentTime = getTime();
        int delta = (int) (currentTime - lastFrame);
        lastFrame = getTime();
        return delta;

    }

    public static int getDeltaBottleNeck() {

        long currentTime = getTime();
        int delta = (int) (currentTime - lastFrameBottleNeck);
        lastFrameBottleNeck = getTime();
        return delta;

    }

    private static void processInputs() {

        processKeyboard();
        processMouse();

    }

    private static void processKeyboard() {
        boolean keyP = Keyboard.isKeyDown(Keyboard.KEY_P);
        if (!debounce && keyP) {
            paused = !paused;
            h.setPause(paused);
        }
        debounce = keyP;
    }

    private static void processMouse() {
        dx = Mouse.getDX();
        dy = Mouse.getDY();
    }

    private static void update(int delta) {

        if (paused) {
            h.update(delta);
        } else {
            for (Entity ee : e) {
                ee.update(delta);
            }
            w.update(delta);
        }

    }

    private static void render() {

        //renderCode
        p.adjust();
        for (DisplayableEntity ent : de) {
            ent.draw();
        }
    }

    public static FloatBuffer asFloatBuffer(float... values) {

        FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);
        buffer.put(values);
        buffer.flip();
        return buffer;

    }

    public static float getDx() {
        return dx;
    }

    public static float getDy() {
        return dy;
    }
}