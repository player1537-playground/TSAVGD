/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

/**
 *
 * @author Andy
 */
public class Face {

    public int[] verts;
    public int[] normals;
    public int[] texCoords;
    int textureIndice;
    int numOfVerts;

    public Face(int[] verts, int[] normals, int[] texCoords, int textureIndice) {

        this.verts = verts;
        this.normals = normals;
        this.texCoords = texCoords;
        this.textureIndice = textureIndice;
        this.numOfVerts = verts.length;
    }
}
