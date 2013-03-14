package quests;

public abstract class Quest {
    String[] description;
    String name;
    public void update() {
	if (testFinishCriteria()) {
	    onFinish();
	}
    }

    public String getName() {
	return this.name;
    }

    public String[] getDescription() {
	return this.description;
    }
    
    public abstract int getPercentage();
    public abstract boolean testFinishCriteria();
    public abstract void onFinish();
}
