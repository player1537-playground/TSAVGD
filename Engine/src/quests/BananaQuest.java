package quests;

import events.PropertyManager;

public class BananaQuest extends Quest {
    { 
	private int NUM_BANANAS = 5;
	description = new String[] {
	    "Gather " + NUM_BANANAS + " bananas to please the banananananaana god!"
	};
       
    }
    
    private int getBananas() {
	return PropertyManager.utilGetInventoryByName("banana");
    }

    public boolean testFinishCriteria() {
	return getBananas() > NUM_BANANAS;
    }

    public int percentageComplete() {
	return getBananas * 100 / NUM_BANANAS;
    }

    public void onFinish() {
	EventTest.setQuest(new CowQuest());
    }
}