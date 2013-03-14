package quests;

import event.EventTest;

public class NullQuest extends Quest {
    { 
	name = "End Quest";
	description = new String[] {
	    "NOTHING!  You've done them all!"
	};
       
    }
    
    public boolean testFinishCriteria() {
	return false;
    }

    public int getPercentage() {
	return -33;
    }

    public void onFinish() {
	// Never gonna happen!
	EventTest.setQuest(new NullQuest());
    }
}