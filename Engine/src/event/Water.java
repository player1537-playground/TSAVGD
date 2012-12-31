/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 *
 * @author Andy
 */
public class Water implements DisplayableEntity {

    Vector3f min;
    float height;
    float tranx, tranz;
    float[][] heightMap;
    Texture tex;

    public Water(Vector3f min, float length, float width, float height, int sectionx, int sectionz) {
        this.min = min;
        this.height = height;
        this.tranx = length / sectionx;
        this.tranz = width / sectionz;
        heightMap = new float[sectionx][sectionz];
        for (int i = 0; i < heightMap.length; i++) {
            for (int j = 0; j < heightMap[i].length; j++) {
                heightMap[i][j] = (float) (.5 * Math.sin((float)i / sectionx * Math.PI * 20) + .25 * Math.cos((float)j / sectionz * Math.PI * 31) + 3 * Math.cos((float)(i * j) / sectionx / sectionz * Math.PI * 5)) * height / 4;
            }
        }
        try {
            this.tex = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/wall.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void draw() {
        glPushMatrix();
        glTranslatef(min.getX(), min.getY(), min.getZ());
        glBindTexture(GL_TEXTURE_2D, tex.getTextureID());
        System.out.println(tex.getTextureID());
        glDisable(GL_CULL_FACE);
        glBegin(GL_TRIANGLES);
        for (int i = 0; i < heightMap.length - 1; i++) {
            for (int j = 0; j < heightMap[i].length - 1; j++) {
                
                glNormal3f(0, 1, 0);
                glTexCoord2f(0, 0);
                glVertex3f(i * tranx, heightMap[i][j], j * tranz);
                glNormal3f(0, 1, 0);
                glTexCoord2f(1, 0);
                glVertex3f((i + 1) * tranx, heightMap[i + 1][j], j * tranz);
                glNormal3f(0, 1, 0);
                glTexCoord2f(1, 1);
                glVertex3f((i + 1) * tranx, heightMap[i + 1][j + 1], (j + 1) * tranz);
                
                glNormal3f(0, 1, 0);
                glTexCoord2f(0, 0);
                glVertex3f(i * tranx, heightMap[i][j], j * tranz);
                glNormal3f(0, 1, 0);
                glTexCoord2f(0, 1);
                glVertex3f(i * tranx, heightMap[i][j + 1], (j + 1) * tranz);
                glNormal3f(0, 1, 0);
                glTexCoord2f(1, 1);
                glVertex3f((i + 1) * tranx, heightMap[i + 1][j + 1], (j + 1) * tranz);
            }
        }
        glEnd();
        glEnable(GL_CULL_FACE);
        glPopMatrix();
    }

    @Override
    public void update(int delta) {
        
    }
}
