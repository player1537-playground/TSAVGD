package quests;

import event.EventTest;
import levels.PropertyManager;

public class BananaQuest extends Quest {
    int NUM_BANANAS = 5;
    { 
	name = "Bananas!";
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

    public int getPercentage() {
	return getBananas() * 100 / NUM_BANANAS;
    }

    public void onFinish() {
	EventTest.setQuest(new CowQuest());
    }
}