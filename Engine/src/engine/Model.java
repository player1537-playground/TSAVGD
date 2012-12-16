/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class Model {
    
    Vector3f[] verts;
    Vector3f[] normals;
    Vector3f[] textureCoords;
    Face[] faces;
    Texture[] tex;
    int indice;
    static String defaultTexture = "test.png";

    public Model(Vector3f[] verts, Vector3f[] normals, Face[] faces, Texture t) {

        this.verts = verts;
        this.normals = normals;
        this.faces = faces;
        this.textureCoords = new Vector3f[]{new Vector3f(0, 0, 0), new Vector3f(0, 1, 0), new Vector3f(1, 1, 0)};
        System.out.println(t.getTextureID());
        tex = new Texture[]{t};
        indice = glGenLists(1);
        glNewList(indice, GL_COMPILE);
        initDraw();
        glEndList();

    }

    public Model(Vector3f[] verts, Vector3f[] normals, Vector3f[] textureArray, Face[] faces, Texture[] tex) {

        this.verts = verts;
        this.normals = normals;
        this.textureCoords = textureArray;
        this.faces = faces;
        this.tex = tex;
        indice = glGenLists(1);
        glNewList(indice, GL_COMPILE);
        initDraw();
        glEndList();
    }

    public static Model loadModel(String path) {
        return Model.loadModel(path, defaultTexture);
    }
    
    public static Model loadModel(String mPath, String tPath) {

        ArrayList verts = new ArrayList(1000);
        ArrayList normals = new ArrayList(1000);
        ArrayList faces = new ArrayList(1000);
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    ResourceLoader.getResourceAsStream("res/" + mPath)));
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
		String[] splitLine = currentLine.split(" ");
                //System.out.println(currentLine);
                if (currentLine.startsWith("v ")) {
                    float x = Float.valueOf(splitLine[1]);
                    float y = Float.valueOf(splitLine[2]);
                    float z = Float.valueOf(splitLine[3]);
                    verts.add(new Vector3f(x, y, z));
                    /*
                     * float dev = (float) Math.random() * 0.05f; colors.add(new
                     * Color(.725f - dev, .525f - dev, .325f - dev));
                     */

                } else if (currentLine.startsWith("vn ")) {
                    float x = Float.valueOf(splitLine[1]);
                    float y = Float.valueOf(splitLine[2]);
                    float z = Float.valueOf(splitLine[3]);
                    normals.add(new Vector3f(x, y, z));
                } else if (currentLine.startsWith("f ")) {
		    String[][] splitSlash = { splitLine[1].split("/"),  splitLine[2].split("/"), splitLine[3].split("/") };
                    int[] vertexIndices = {Integer.parseInt(splitSlash[1][0]) - 1,
                        Integer.parseInt(splitSlash[2][0]) - 1,
                        Integer.parseInt(splitSlash[3][0]) - 1};
                    int[] normalIndices = null;
                    try {
                        normalIndices = new int[]{Integer.parseInt(splitSlash[1][2]) - 1,
                            Integer.parseInt(splitSlash[2][2]) - 1,
                            Integer.parseInt(splitSlash[3][2]) - 1};
                    } catch (ArrayIndexOutOfBoundsException ee) {
                        System.out.println(currentLine);
                    }

                    int[] textureCoordinates = {0, 1, 2};
                    int textureIndice = 0;
                    faces.add(new Face(vertexIndices, normalIndices, textureCoordinates, textureIndice));

                }

            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Vector3f[] vertArray = new Vector3f[verts.size()];
        verts.toArray(vertArray);
        Vector3f[] normalArray = new Vector3f[normals.size()];
        normals.toArray(normalArray);
        Face[] faceArray = new Face[faces.size()];
        faces.toArray(faceArray);
        return new Model(vertArray, normalArray, faceArray, getTexture(tPath));

    }

    public static Model loadModel(String path, boolean texture) {

        ArrayList verts = new ArrayList();
        ArrayList normals = new ArrayList();
        ArrayList textureCoords = new ArrayList();
        ArrayList faces = new ArrayList();
        ArrayList<Material> materials = new ArrayList<Material>();
        int textureIndex = 0;
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    ResourceLoader.getResourceAsStream("res/" + path)));
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
                    materials.add(new Material(name, currentPath));
                }
            }
            while ((currentLine = reader.readLine()) != null) {

                //System.out.println(currentLine);
                if (currentLine.startsWith("v ")) {
                    float x = Float.valueOf(currentLine.split(" ")[1]);
                    float y = Float.valueOf(currentLine.split(" ")[2]);
                    float z = Float.valueOf(currentLine.split(" ")[3]);
                    verts.add(new Vector3f(x, y, z));
                } else if (currentLine.startsWith("vt ")) {
                    float x = Float.valueOf(currentLine.split(" ")[1]);
                    float y = Float.valueOf(currentLine.split(" ")[2]);
                    textureCoords.add(new Vector3f(x, y, 0));
                } else if (currentLine.startsWith("vn ")) {
                    float x = Float.valueOf(currentLine.split(" ")[1]);
                    float y = Float.valueOf(currentLine.split(" ")[2]);
                    float z = Float.valueOf(currentLine.split(" ")[3]);
                    normals.add(new Vector3f(x, y, z));
                } else if (currentLine.startsWith("f ")) {
                    int[] vertexIndices = {Integer.parseInt(currentLine.split(" ")[1].split("/")[0]) - 1,
                        Integer.parseInt(currentLine.split(" ")[2].split("/")[0]) - 1,
                        Integer.parseInt(currentLine.split(" ")[3].split("/")[0]) - 1};
                    int[] textureCoordinates = {Integer.parseInt(currentLine.split(" ")[1].split("/")[1]) - 1,
                        Integer.parseInt(currentLine.split(" ")[2].split("/")[1]) - 1,
                        Integer.parseInt(currentLine.split(" ")[3].split("/")[1]) - 1};
                    int[] normalIndices = new int[]{Integer.parseInt(currentLine.split(" ")[1].split("/")[2]) - 1,
                        Integer.parseInt(currentLine.split(" ")[2].split("/")[2]) - 1,
                        Integer.parseInt(currentLine.split(" ")[3].split("/")[2]) - 1};

                    faces.add(new Face(vertexIndices, normalIndices, textureCoordinates, textureIndex));

                } else if (currentLine.startsWith("usemtl ")) {
                    for (Material m : materials) {
                        String currentName = currentLine.split(" ")[1];
                        if (m.getName().equals(currentName)) {
                            textureIndex = m.textureIndice;
                            break;
                        }
                    }
                }

            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Vector3f[] vertArray = new Vector3f[verts.size()];
        verts.toArray(vertArray);
        Vector3f[] normalArray = new Vector3f[normals.size()];
        normals.toArray(normalArray);
        Vector3f[] textureCArray = new Vector3f[textureCoords.size()];
        textureCoords.toArray(textureCArray);
        Face[] faceArray = new Face[faces.size()];
        faces.toArray(faceArray);
        Texture[] textureArray = new Texture[Material.textures.size()];
        Material.textures.toArray(textureArray);
        return new Model(vertArray, normalArray, textureCArray, faceArray, textureArray);

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
}