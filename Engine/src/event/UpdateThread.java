package event;

import org.lwjgl.*;
import org.lwjgl.opengl.*;

class UpdateThread extends Thread {
    long lastTime;
    public UpdateThread() {
	lastTime = EventTest.getTime();
    }
    
    public void run() {
	while (EventTest.isRunning()) {
	    long deltaTime = EventTest.getTime() - lastTime;
	    EventTest.update((int)deltaTime);
	    lastTime = EventTest.getTime();
	    try { Thread.sleep(1000 / 30); } catch (Exception e) { }
	}
    }
}
