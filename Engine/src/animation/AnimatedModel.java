/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package animation;

import engine.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.ResourceLoader;

/**
 *
 * @author Andy
 */
public class AnimatedModel implements DisplayableEntity {

    Bone root;
    public static String defaultTexture = "test.png";

    public AnimatedModel(Bone root) {

        this.root = root;

    }

    public static AnimatedModel loadAnimation(String path) {

        Bone root = null;
        ArrayList verts = new ArrayList();
        ArrayList normals = new ArrayList();
        ArrayList faces = new ArrayList();
        ArrayList<Bone> bones = new ArrayList<Bone>();
        ArrayList boneNames = new ArrayList();
        ArrayList boneChildren = new ArrayList();
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    ResourceLoader.getResourceAsStream("res/" + path)));
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {

                //System.out.println(currentLine);
                if (currentLine.startsWith("v ")) {
                    float x = Float.valueOf(currentLine.split(" ")[1]);
                    float y = Float.valueOf(currentLine.split(" ")[2]);
                    float z = Float.valueOf(currentLine.split(" ")[3]);
                    verts.add(new Vector3f(x, y, z));
                    /*
                     * float dev = (float) Math.random() * 0.05f; colors.add(new
                     * Color(.725f - dev, .525f - dev, .325f - dev));
                     */

                } else if (currentLine.startsWith("vn ")) {
                    float x = Float.valueOf(currentLine.split(" ")[1]);
                    float y = Float.valueOf(currentLine.split(" ")[2]);
                    float z = Float.valueOf(currentLine.split(" ")[3]);
                    normals.add(new Vector3f(x, y, z));
                } else if (currentLine.startsWith("f ")) {
                    int[] vertexIndices = {Integer.parseInt(currentLine.split(" ")[1].split("/")[0]) - 1,
                        Integer.parseInt(currentLine.split(" ")[2].split("/")[0]) - 1,
                        Integer.parseInt(currentLine.split(" ")[3].split("/")[0]) - 1};
                    int[] normalIndices = null;
                    try {
                        normalIndices = new int[]{Integer.parseInt(currentLine.split(" ")[1].split("/")[2]) - 1,
                            Integer.parseInt(currentLine.split(" ")[2].split("/")[2]) - 1,
                            Integer.parseInt(currentLine.split(" ")[3].split("/")[2]) - 1};
                    } catch (ArrayIndexOutOfBoundsException ee) {
                        System.out.println(currentLine);
                    }

                    Vector3f[] faceVertexes = new Vector3f[vertexIndices.length];
                    for (int i = 0; i < faceVertexes.length; i++) {
                        faceVertexes[i] = (Vector3f) verts.get(vertexIndices[i]);
                    }
                    Vector3f[] faceNormals = new Vector3f[vertexIndices.length];
                    for (int i = 0; i < faceNormals.length; i++) {
                        faceNormals[i] = (Vector3f) normals.get(normalIndices[i]);
                    }
                    Vector2f[] textureCoordinates = new Vector2f[]{new Vector2f(0, 0), new Vector2f(0, 1), new Vector2f(1, 0)};
                    int textureIndice = AFace.defaultTextureIndice;
                    faces.add(new AFace(faceVertexes, faceNormals, textureCoordinates, textureIndice));

                } else if (currentLine.startsWith("Bone ")) {
                    boneNames.add(currentLine.split(" ")[1]);
                    System.out.println("Bone Name " + currentLine.split(" ")[1]);
                    Vector3f bone = null;
                    Quaternion quat = null;
                    float speed = 0;
                    float lower = 0, upper = 0;
                    AFace[] skin = null;
                    ArrayList<String> children = new ArrayList<String>();
                    while ((currentLine = reader.readLine()) != null && !currentLine.isEmpty()) {
                        System.out.println(currentLine);
                        if (currentLine.startsWith("vec ")) {
                            bone = new Vector3f(Float.parseFloat(currentLine.split(" ")[1]),
                                    Float.parseFloat(currentLine.split(" ")[2]),
                                    Float.parseFloat(currentLine.split(" ")[3]));
                        } else if (currentLine.startsWith("quat ")) {
                            quat = new Quaternion(Float.parseFloat(currentLine.split(" ")[1]),
                                    Float.parseFloat(currentLine.split(" ")[2]),
                                    Float.parseFloat(currentLine.split(" ")[3]),
                                    Float.parseFloat(currentLine.split(" ")[4]));
                        } else if (currentLine.startsWith("speed ")) {
                            speed = Float.parseFloat(currentLine.split(" ")[1]);
                        } else if (currentLine.startsWith("lower ")) {
                            lower = Float.parseFloat(currentLine.split(" ")[1]);
                        } else if (currentLine.startsWith("upper ")) {
                            upper = Float.parseFloat(currentLine.split(" ")[1]);
                        } else if (currentLine.startsWith("skin ")) {
                            String[] indices = currentLine.split(" ");
                            skin = new AFace[indices.length - 1];
                            for (int i = 0; i < skin.length; i++) {
                                skin[i] = (AFace) faces.get(Integer.parseInt(indices[i + 1]) - 1);
                            }
                        } else if (currentLine.startsWith("children ")) {
                            String[] names = currentLine.split(" ");
                            for (int i = 1; i < names.length; i++) {
                                children.add(names[i]);
                            }
                        }
                    }
                    Bone b = new Bone(bone, quat, speed, lower, upper, skin, null);
                    bones.add(b);
                    boneChildren.add(children);
                }

            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String>[] childrenArray = new ArrayList[boneChildren.size()];
        boneChildren.toArray(childrenArray);
        System.out.println("Bones " + boneNames);
        System.out.println("Bones " + bones);
        for (int i = 0; i < bones.size(); i++) {
            ArrayList<String> children = childrenArray[i];
            Bone[] boneArray = new Bone[children.size()];
            for (int j = 0; j < children.size(); j++) {
                String name = children.get(j);
                int index = boneNames.indexOf(name);
                System.out.println("Child Index" + index);
                if (index != -1) {
                    boneArray[j] = bones.get(index);
                }
            }
            bones.get(i).setChildren(boneArray);
        }
        for (int i = 0; i < boneNames.size(); i++) {
            if (boneNames.get(i).equals("Root")) {
                root = bones.get(i);
            }
        }
        Model.getTexture(defaultTexture);
        System.out.println(root);
        return new AnimatedModel(root);
    }

    @Override
    public void draw() {
        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        glColor3f(1, 0, 1);
        glTranslatef(0, 3, 0);
        root.draw();
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();
    }

    @Override
    public void update(int delta) {
        root.update(delta);
    }
}
