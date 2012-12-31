/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
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
public class TerrainModel {

    ArrayList verts;
    ArrayList normals;
    ArrayList textureCoords;
    ArrayList[][] faces;
    Texture[] tex;
    int indices;
    int sectionx, sectionz;
    Vector3f min, max;
    static String defaultTexture = "terrain.png";
    private float multx, multz;

    public enum Section {

        TOP, LEFT, BOTTOM, RIGHT, ALL
    }

    public TerrainModel(ArrayList verts, ArrayList normals, ArrayList[][] faces, Texture t, Vector3f min, Vector3f max) {

        this.verts = verts;
        this.normals = normals;
        this.faces = faces;
        this.textureCoords = new ArrayList() {

            {
                add(new Vector3f(0, 0, 0));
                add(new Vector3f(0, 1, 0));
                add(new Vector3f(1, 1, 0));
            }
        };
        System.out.println(t.getTextureID());
        tex = new Texture[]{t};
        indices = glGenLists(faces.length * faces[0].length);
        sectionx = faces.length;
        sectionz = faces[0].length;
        multx = sectionx / (max.getX() - min.getX());
        multz = sectionz / (max.getZ() - min.getZ());
        Vector3f.sub(max, min, max);
        this.min = min;
        this.max = max;
        for (int i = 0; i < faces.length; i++) {
            for (int j = 0; j < faces[i].length; j++) {
                glNewList(indices + i * faces[i].length + j, GL_COMPILE);
                initDraw(i, j);
                glEndList();
            }
        }

    }

    public static TerrainModel loadModel(String path) {
        return TerrainModel.loadModel(path, defaultTexture);
    }

    public static TerrainModel loadModel(String mPath, String tPath) {
        int gridx = 0, gridz = 0;
        Vector3f min = null;
        Vector3f max = null;
        int sectionx = 1, sectionz = 1;
        int vertCount = 0, normCount = 0, textCount = 0, faceCount = 0;
        int realvertCount = 0;
        ArrayList verts = null, normals = null;
        ArrayList[][] faces = null;
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    ResourceLoader.getResourceAsStream("res/" + mPath)));
            String currentLine;
            while ((currentLine = reader.readLine()).startsWith("#")) {
                System.out.println(currentLine);
                String[] splitLine = currentLine.split(" ");
                if (currentLine.startsWith("#min ")) {
                    min = new Vector3f(
                            Float.valueOf(splitLine[1]),
                            Float.valueOf(splitLine[2]),
                            Float.valueOf(splitLine[3]));
                }
                if (currentLine.startsWith("#max ")) {
                    max = new Vector3f(
                            Float.valueOf(splitLine[1]),
                            Float.valueOf(splitLine[2]),
                            Float.valueOf(splitLine[3]));
                }
                if (currentLine.startsWith("#gridsize ")) {
                    sectionx = Integer.parseInt(splitLine[1]);
                    sectionz = Integer.parseInt(splitLine[3]);
                }
                if (currentLine.startsWith("#vertexCount ")) {
                    vertCount = Integer.parseInt(splitLine[1]);
                }
                if (currentLine.startsWith("#normalCount ")) {
                    normCount = Integer.parseInt(splitLine[1]);
                }
                if (currentLine.startsWith("#textureCount ")) {
                    textCount = Integer.parseInt(splitLine[1]);
                }
                if (currentLine.startsWith("#faceCount ")) {
                    faceCount = Integer.parseInt(splitLine[1]);
                }
            }

            verts = new ArrayList(vertCount);
            normals = new ArrayList(normCount);
            faces = new ArrayList[sectionx][sectionz];

            for (int i = 0; i < faces.length; i++) {
                for (int j = 0; j < faces[i].length; j++) {
                    faces[i][j] = new ArrayList();
                }
            }

            do {
                String[] splitLine = currentLine.split(" ");
                //System.out.println(currentLine);
                if (currentLine.startsWith("v ")) {
                    realvertCount++;
                    float x = Float.valueOf(splitLine[1]);
                    float y = Float.valueOf(splitLine[2]);
                    float z = Float.valueOf(splitLine[3]);
                    verts.add(new Vector3f(x, y, z));

                } else if (currentLine.startsWith("vn ")) {
                    float x = Float.valueOf(splitLine[1]);
                    float y = Float.valueOf(splitLine[2]);
                    float z = Float.valueOf(splitLine[3]);
                    normals.add(new Vector3f(x, y, z));
                } else if (currentLine.startsWith("f ")) {
                    String[][] splitSlash = {splitLine[1].split("/"), splitLine[2].split("/"), splitLine[3].split("/")};
                    int[] vertexIndices = null;
                    int[] normalIndices = null;
                    try {
                        vertexIndices = new int[]{
                            Integer.parseInt(splitSlash[0][0]) - 1,
                            Integer.parseInt(splitSlash[1][0]) - 1,
                            Integer.parseInt(splitSlash[2][0]) - 1
                        };
                        normalIndices = new int[]{Integer.parseInt(splitSlash[0][2]) - 1,
                            Integer.parseInt(splitSlash[1][2]) - 1,
                            Integer.parseInt(splitSlash[2][2]) - 1};
                    } catch (ArrayIndexOutOfBoundsException ee) {
                        System.out.println(currentLine);
                        for (String[] a : splitSlash) {
                            System.out.println("length " + a.length);
                            for (String s : a) {
                                System.out.println(s);
                            }
                        }
                        ee.printStackTrace();
                    }

                    int[] textureCoordinates = {0, 1, 2};
                    int textureIndice = 0;
                    faces[gridx][gridz].add(new Face(vertexIndices, normalIndices, textureCoordinates, textureIndice));

                } else if (currentLine.startsWith("#grid ")) {
                    String[] coord = currentLine.split(" ");
                    gridx = Integer.parseInt(coord[1]);
                    gridz = Integer.parseInt(coord[2]);
                }

            } while ((currentLine = reader.readLine()) != null);

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(realvertCount);
        return new TerrainModel(verts, normals, faces, getTexture(tPath), min, max);

    }

    public void initDraw(int x, int z) {

        Color.white.bind();
        glBindTexture(GL_TEXTURE_2D, tex[((Face) faces[x][z].get(0)).textureIndice].getTextureID());
        int last = ((Face) faces[x][z].get(0)).textureIndice;
        glBegin(GL_TRIANGLES);
        for (Iterator it = faces[x][z].iterator(); it.hasNext();) {
            Face face = (Face) it.next();
            if (last != face.textureIndice) {
                glEnd();
                Color.white.bind();
                tex[face.textureIndice].bind();
                last = face.textureIndice;
                glBegin(GL_TRIANGLES);
            }
            for (int j = 0; j < 3; j++) {

                myTexture((Vector3f) textureCoords.get(face.texCoords[j]));
                myNormal3f((Vector3f) normals.get(face.normals[j]));
                myVertex3f((Vector3f) verts.get(face.verts[j]));

            }
        }
        glEnd();
    }

    public void draw(int x, int z, Section s) {

        if (s.equals(Section.TOP)) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    glCallList(indices + (x + i) * sectionz + (z - j));
                    glCallList(indices + (x - i) * sectionz + (z - j));
                }
            }
            glCallList(indices + (x - 1) * sectionz + (z + 1));
            glCallList(indices + (x) * sectionz + (z + 1));
            glCallList(indices + (x + 1) * sectionz + (z + 1));
        }
        if (s.equals(Section.LEFT)) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    glCallList(indices + (x - i) * sectionz + (z - j));
                    glCallList(indices + (x - i) * sectionz + (z + j));
                }
            }
            glCallList(indices + (x + 1) * sectionz + (z - 1));
            glCallList(indices + (x + 1) * sectionz + (z));
            glCallList(indices + (x + 1) * sectionz + (z + 1));
        }
        if (s.equals(Section.BOTTOM)) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    glCallList(indices + (x + i) * sectionz + (z + j));
                    glCallList(indices + (x - i) * sectionz + (z + j));
                }
            }
            glCallList(indices + (x - 1) * sectionz + (z - 1));
            glCallList(indices + (x) * sectionz + (z - 1));
            glCallList(indices + (x + 1) * sectionz + (z - 1));
        }
        if (s.equals(Section.RIGHT)) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    glCallList(indices + (x + i) * sectionz + (z - j));
                    glCallList(indices + (x + i) * sectionz + (z + j));
                }
            }
            glCallList(indices + (x - 1) * sectionz + (z - 1));
            glCallList(indices + (x - 1) * sectionz + (z));
            glCallList(indices + (x - 1) * sectionz + (z + 1));
        }

    }

    public void draw(float x, float z, float angle) {

        Section s = Section.ALL;
        if (inBounds(angle, 45, 135)) {
            s = Section.LEFT;
        }
        if (inBounds(angle, 135, 225)) {
            s = Section.BOTTOM;
        }
        if (inBounds(angle, 225, 315)) {
            s = Section.RIGHT;
        }
        if (inBounds(angle, 315, 360) || inBounds(angle, 0, 45)) {
            s = Section.TOP;
        }
        draw((int) ((x - min.getX()) * multx), (int) ((z - min.getZ()) * multz), s);

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

        return new BoundingBox(min, max);

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

    private boolean inBounds(float angle, float lower, float upper) {
        return angle < upper && angle >= lower;
    }
}
