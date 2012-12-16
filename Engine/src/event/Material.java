/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import java.io.IOException;
import java.util.ArrayList;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 *
 * @author Andy
 */
public class Material {

    int textureIndice;
    String name;
    static ArrayList<String> paths = new ArrayList<String>();
    static ArrayList<Texture> textures = new ArrayList<Texture>();

    public Material(String name, String path) {
        this.name = name;
        if (!paths.contains(path)) {
            try {
                this.textureIndice = textures.size();
                textures.add(TextureLoader.getTexture("JPG",
                        ResourceLoader.getResourceAsStream(path)));
                paths.add(path);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            this.textureIndice = paths.indexOf(path);
        }
    }
    
    public Texture getTexture(int i) {
        return textures.get(i);
    }
    
    public String getName() {
        return this.name;
    }
    
}