/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package animation;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Andy
 */
public class AFace {

    Vector3f[] verts;
    Vector3f[] normals;
    Vector2f[] texCoords;
    int textureIndice;
    int numOfVerts;
    static int defaultTextureIndice = 1;

    public AFace(Vector3f[] verts, Vector3f[] normals, Vector2f[] texCoords, int textureIndice) {

        this.verts = verts;
        this.normals = normals;
        this.texCoords = texCoords;
        this.textureIndice = textureIndice;
        this.numOfVerts = verts.length;
    }
}