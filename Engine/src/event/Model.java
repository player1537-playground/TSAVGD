/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import levels.Resource;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import org.lwjgl.util.glu.Util.*;

/**
 *
 * @author Andy
 */
public class Model implements Resource {

    public Vector3f[] verts;
    public Vector3f[] normals;
    public Vector3f[] textureCoords;
    public Face[] faces;
    public Texture[] tex;
    private int indice;
    static String defaultTexture = "test.png";
    private final String name;
    private final String mpath, tpath;

    public Model(String name, String path) {
        this.name = name;
        this.mpath = path;
        this.tpath = defaultTexture;
    }

    public void load() {
        ArrayList vertexList = new ArrayList(1000);
        ArrayList normalList = new ArrayList(1000);
        ArrayList faceList = new ArrayList(1000);

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    ResourceLoader.getResourceAsStream("res/" + mpath)));
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] splitLine = currentLine.split(" ");
                //System.out.println(currentLine);
                if (currentLine.startsWith("v ")) {
                    float x = Float.valueOf(splitLine[1]);
                    float y = Float.valueOf(splitLine[2]);
                    float z = Float.valueOf(splitLine[3]);
                    vertexList.add(new Vector3f(x, y, z));
                    /*
                     * float dev = (float) Math.random() * 0.05f; colors.add(new
                     * Color(.725f - dev, .525f - dev, .325f - dev));
                     */

                } else if (currentLine.startsWith("vn ")) {
                    float x = Float.valueOf(splitLine[1]);
                    float y = Float.valueOf(splitLine[2]);
                    float z = Float.valueOf(splitLine[3]);
                    normalList.add(new Vector3f(x, y, z));
                } else if (currentLine.startsWith("f ")) {
                    String[][] splitSlash = {splitLine[1].split("/"), splitLine[2].split("/"), splitLine[3].split("/")};
                    int[] vertexIndices = {Integer.parseInt(splitSlash[0][0]) - 1,
                        Integer.parseInt(splitSlash[1][0]) - 1,
                        Integer.parseInt(splitSlash[2][0]) - 1};
                    int[] normalIndices = null;
                    try {
                        normalIndices = new int[]{Integer.parseInt(splitSlash[0][2]) - 1,
                            Integer.parseInt(splitSlash[1][2]) - 1,
                            Integer.parseInt(splitSlash[2][2]) - 1};
                    } catch (ArrayIndexOutOfBoundsException ee) {
                        System.out.println(currentLine);
                    }

                    int[] textureCoordinates = {0, 1, 2};
                    int textureIndice = 0;
                    faceList.add(new Face(vertexIndices, normalIndices, textureCoordinates, textureIndice));

                }

            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.verts = new Vector3f[vertexList.size()];
        vertexList.toArray(this.verts);
        this.normals = new Vector3f[normalList.size()];
        normalList.toArray(this.normals);
        this.faces = new Face[faceList.size()];
        faceList.toArray(faces);

        Texture t = getTexture(tpath);
        this.textureCoords = new Vector3f[]{new Vector3f(0, 0, 0), new Vector3f(0, 1, 0), new Vector3f(1, 1, 0)};
        System.out.println("Model loaded " + t.getTextureID());
        tex = new Texture[]{t};
        indice = glGenLists(1);
        glNewList(indice, GL_COMPILE);
        initDraw();
        glEndList();

    }

    public static Model loadModel(String path) {
        Model m = new Model(path, path);
        m.load();
        return m;
    }

    public void loadWithTex() {

        ArrayList verts = new ArrayList();
        ArrayList normals = new ArrayList();
        ArrayList textureCoords = new ArrayList();
        ArrayList faces = new ArrayList();
        HashMap<String, Material> materialHash = new HashMap<String, Material>();
        //ArrayList<Material> materialHash = new HashMap<String, Material>();
        int textureIndex = 0;
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    ResourceLoader.getResourceAsStream("res/" + mpath)));
            String currentLine;
            while (!(currentLine = reader.readLine()).startsWith("mtllib ")) {
            }
            String materialPath = currentLine.split(" ")[1];
            BufferedReader matReader = new BufferedReader(new InputStreamReader(
                    ResourceLoader.getResourceAsStream("res/" + materialPath)));
            String name = "";
            while ((currentLine = matReader.readLine()) != null) {
                if (currentLine.startsWith("newmtl ")) {
                    name = currentLine.split(" ")[1];
                } else if (currentLine.startsWith("map_Kd ")) {
                    String currentPath = currentLine.split(" ")[1];
                    materialHash.put(name, new Material(name, currentPath));
                }
            }
            while ((currentLine = reader.readLine()) != null) {
                String[] spaceSplit = currentLine.split(" ");
                //System.out.println(currentLine);
                if (currentLine.startsWith("v ")) {
                    float x = Float.valueOf(spaceSplit[1]);
                    float y = Float.valueOf(spaceSplit[2]);
                    float z = Float.valueOf(spaceSplit[3]);
                    verts.add(new Vector3f(x, y, z));
                } else if (currentLine.startsWith("vt ")) {
                    float x = Float.valueOf(spaceSplit[1]);
                    float y = Float.valueOf(spaceSplit[2]);
                    textureCoords.add(new Vector3f(x, y, 0));
                } else if (currentLine.startsWith("vn ")) {
                    float x = Float.valueOf(spaceSplit[1]);
                    float y = Float.valueOf(spaceSplit[2]);
                    float z = Float.valueOf(spaceSplit[3]);
                    normals.add(new Vector3f(x, y, z));
                } else if (currentLine.startsWith("f ")) {
                    String[][] slashSplit = new String[][]{
                        spaceSplit[1].split("/"),
                        spaceSplit[2].split("/"),
                        spaceSplit[3].split("/")
                    };
                    
                    int[] vertexIndices = {Integer.parseInt(slashSplit[0][0]) - 1,
                        Integer.parseInt(slashSplit[1][0]) - 1,
                        Integer.parseInt(slashSplit[2][0]) - 1};
                    int[] textureCoordinates = {Integer.parseInt(slashSplit[0][1]) - 1,
                        Integer.parseInt(slashSplit[1][1]) - 1,
                        Integer.parseInt(slashSplit[2][1]) - 1};
                    int[] normalIndices = new int[]{Integer.parseInt(slashSplit[0][2]) - 1,
                        Integer.parseInt(slashSplit[1][2]) - 1,
                        Integer.parseInt(slashSplit[2][2]) - 1};

                    faces.add(new Face(vertexIndices, normalIndices, textureCoordinates, textureIndex));

                } else if (currentLine.startsWith("usemtl ")) {
                    String currentName = spaceSplit[1];
                    textureIndex = materialHash.get(currentName).textureIndice;
                }

            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.verts = new Vector3f[verts.size()];
        verts.toArray(this.verts);
        this.normals = new Vector3f[normals.size()];
        normals.toArray(this.normals);
        this.textureCoords = new Vector3f[textureCoords.size()];
        textureCoords.toArray(this.textureCoords);
        this.faces = new Face[faces.size()];
        faces.toArray(this.faces);
        tex = new Texture[Material.textures.size()];
        Material.textures.toArray(tex);
        
        indice = glGenLists(1);
        glNewList(indice, GL_COMPILE);
        initDraw();
        glEndList();
    }

    public void initDraw() {

        Color.white.bind();
        glBindTexture(GL_TEXTURE_2D, tex[faces[0].textureIndice].getTextureID());
        int last = faces[0].textureIndice;
        glBegin(GL_TRIANGLES);
        for (Face face : faces) {

            if (last != face.textureIndice) {
                glEnd();
                Color.white.bind();
                tex[face.textureIndice].bind();
                last = face.textureIndice;
                glBegin(GL_TRIANGLES);
            }

            for (int j = 0; j < 3; j++) {

                myTexture(textureCoords[face.texCoords[j]]);
                myNormal3f(normals[face.normals[j]]);
                myVertex3f(verts[face.verts[j]]);

            }

        }
        glEnd();
    }

    public void draw() {

        glCallList(indice);

    }

    public void myNormal3f(Vector3f n) {

        glNormal3f(n.getX(), n.getY(), n.getZ());

    }

    public void myVertex3f(Vector3f v) {

        glVertex3f(v.getX(), v.getY(), v.getZ());

    }

    private void myTexture(Vector3f v) {

        glTexCoord2f(v.getX(), v.getY());

    }

    public BoundingBox getBounds() {

        return BoundingBox.create(verts);

    }

    public static Texture getTexture(String path) {

        Texture t = null;
        try {
            t = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/" + path));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return t;

    }

    @Override
    public String getName() {
        return name;
    }
}