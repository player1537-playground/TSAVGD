package event;

public class InventoryItem {
    String name;
    int count;

    public InventoryItem(String name, int count) {
	this.name = name;
	this.count = count;
    }

    public String getName() {
	return this.name;
    }

    public int getCount() {
	return this.count;
    }
}