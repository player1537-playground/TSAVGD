package quests;

import levels.PropertyManager;
import event.EventTest;

public class CowQuest extends Quest { 
    private int NUM_MILK_JUGS = 3;
    { 
	name = "Cows!";
	description = new String[] {
	    "Gather " + NUM_MILK_JUGS + " milk jugs for the festival god!"
	};
       
    }
    
    private int getMilkJugs() {
	return PropertyManager.utilGetInventoryByName("milk-jugs");
    }

    public boolean testFinishCriteria() {
	return getMilkJugs() > NUM_MILK_JUGS;
    }

    public int getPercentage() {
	return getMilkJugs() * 100 / NUM_MILK_JUGS;
    }

    public void onFinish() {
	EventTest.setQuest(new NullQuest());
    }
}