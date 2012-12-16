/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

/**
 *
 * @author Andy
 */
public class Face {

    int[] verts;
    int[] normals;
    int[] texCoords;
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
