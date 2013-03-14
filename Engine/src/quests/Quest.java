package quests;

public abstract class Quest {
    private String[] description;
    public void update() {
	if (testFinishCriteria()) {
	    onFinish();
	}
    }

    public String[] getDescription() {
	return this.description;
    }
    
    public abstract int getPercentage();
    public abstract boolean testFinishCriteria();
    public abstract void onFinish();
}
