package quests;

import events.PropertyManager;

public class CowQuest extends Quest {
    { 
	private int NUM_MILK_JUGS = 3;
	description = new String[] {
	    "Gather " + NUM_BANANAS + " milk jugs for the festival god!"
	};
       
    }
    
    private int getMilkJugs() {
	return PropertyManager.utilGetInventoryByName("milk-jugs");
    }

    public boolean testFinishCriteria() {
	return getMilkJugs() > NUM_MILK_JUGS;
    }

    public int percentageComplete() {
	return getMilkJugs * 100 / NUM_MILK_JUGS;
    }

    public void onFinish() {
	EventTest.setQuest(new NullQuest());
    }
}