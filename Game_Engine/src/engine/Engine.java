/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import com.sun.org.apache.bcel.internal.generic.SIPUSH;
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
public class Engine {

    static final float width = 1200;
    static final float height = 900;
    static long lastFrame = 0;
    Player p;
    Terrain ter;
    ArrayList<Entity> e;
    ArrayList<DisplayableEntity> de;
    ArrayList<PhysicalEntity> pe;
    PhysicalWorld w;

    public static void main(String[] argv) {

        Engine p = new Engine();

    }
    
    public Engine() {

        init();
        run();
        destroy();

    }

    private void init() {

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
            glFogf(GL_FOG_START, 500f);             // Fog Start Depth
            glFogf(GL_FOG_END, 900f);               // Fog End Depth
            glEnable(GL_FOG);                   // Enables GL_FOG

            //Timer
            getDelta();

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
            
            e = new ArrayList<Entity>();
            e.add(sky);
            e.add(ter);
            e.add(p);

            de = new ArrayList<DisplayableEntity>();
            de.add(sky);
            de.add(ter);
            
            pe = new ArrayList<PhysicalEntity>();
            pe.add(p);

            w = new PhysicalWorld(ter, pe);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    private void run() {

        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            int delta = getDelta();
            System.out.println(1000f / delta);
            processInputs();

            for (int i = 0; i < 3; i++) {
                update(delta / 3);
            }

            render();

            Display.update();
            Display.sync(60);

        }

    }

    private void destroy() {

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
        //System.out.println(1000f / delta);
        return delta;

    }

    private void processInputs() {

        processKeyboard();
        processMouse();

    }
    
    private void processKeyboard() {
    }

    private void processMouse() {
    }

    private void update(int delta) {

        for (Entity ee : e) {
            ee.update(delta);
        }
        w.update(delta);

    }
    
    private void render() {

        //renderCode
        p.adjust();
        for (DisplayableEntity ent : de) {
            ent.draw();
        }

        /*
         * Code to draw 2D UI on display glMatrixMode(GL_PROJECTION);
         * glPushMatrix(); glLoadIdentity(); glOrtho(0, width, height, 0, -1,
         * 1); glMatrixMode(GL_MODELVIEW); glLoadIdentity();
         * glDisable(GL_CULL_FACE);
         *
         * glClear(GL_DEPTH_BUFFER_BIT); glDisable(GL_TEXTURE_2D);
         *
         * glColor3f(1, 1, 1); glBegin(GL_QUADS); glVertex2f(1, 1);
         * glVertex2f(100, 1); glVertex2f(100, 100); glVertex2f(1, 100);
         * glEnd(); glEnable(GL_TEXTURE_2D); glEnable(GL_CULL_FACE);
         * glMatrixMode(GL_PROJECTION); glPopMatrix();
         * glMatrixMode(GL_MODELVIEW);
         */

    }

    private static FloatBuffer asFloatBuffer(float... values) {

        FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);
        buffer.put(values);
        buffer.flip();
        return buffer;

    }
    
}