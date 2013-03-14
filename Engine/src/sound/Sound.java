package sound;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import static org.lwjgl.openal.AL10.*;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Andy
 */
public class Sound {

    int source;
    boolean playing;
    int delay;
    int counter;
    float volume;
    float mult;

    Sound(int buffer, int delay) {
        System.out.println("Buffer " + buffer);
        this.source = alGenSources();
        alSourcei(source, AL_BUFFER, buffer);
        this.playing = false;
        this.delay = delay;
        this.volume = 1f;
        this.mult = 1;
    }

    Sound(int buffer, Vector3f pos, int delay) {
        this(buffer, delay);
        setPosition(pos);
    }

    public void setPosition(Vector3f pos) {
        alSource3f(source, AL_POSITION, pos.getX() / 100, pos.getY() / 100, pos.getZ() / 100);
    }

    public int getSource() {
        return source;
    }

    public void play(boolean force) {
        if (!playing || force) {
            alSourcePlay(getSource());
            playing = true;
            counter = delay;
        }
    }

    public void play() {
        play(false);
    }

    public void stop() {
        alSourceStop(getSource());
        playing = false;
    }

    public void repeat() {
        alSourcei(source, AL_LOOPING, AL_TRUE);
    }

    public void setVolume(float volume) {
        this.volume = volume;
        alSourcef(source, AL_GAIN, volume * mult);
    }

    public void mute(boolean muted) {
        if (muted) {
            alSourcef(source, AL_GAIN, 0);
        } else {
            setVolume(volume);
        }
    }

    void update(int delta) {
        if (playing) {
            if ((counter -= delta) < 0) {
                playing = false;
            }
        }
    }

    void setMult(float mult) {
        this.mult = mult;
        setVolume(this.volume);
    }
}
