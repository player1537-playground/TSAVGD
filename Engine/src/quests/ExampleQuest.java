package quests;

import events.PropertyManager;

public class BananaQuest extends Quest {
    boolean testFinishCriteria() {
	return (PropertyManager.utilGetInventoryByName("bananas") > 5);
    }

    void onFinish() {
	EventTest.setQuest(new ());
    }
}