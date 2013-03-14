package quests;

import events.PropertyManager;

public class CowQuest extends Quest {
    { 
	description = new String[] {
	    "NOTHING!  You've done them all!"
	};
       
    }
    
    public boolean testFinishCriteria() {
	return false;
    }

    public int percentageComplete() {
	return -33;
    }

    public void onFinish() {
	// Never gonna happen!
	EventTest.setQuest(new NullQuest());
    }
}