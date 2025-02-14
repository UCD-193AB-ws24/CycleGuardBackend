package cycleguard.rest.purchases;

import java.util.TreeMap;

public class ItemInfo {
	private TreeMap<String, Integer> itemCosts;

	public TreeMap<String, Integer> getItemCosts() {
		return itemCosts;
	}

	public void setItemCosts(TreeMap<String, Integer> itemCosts) {
		this.itemCosts = itemCosts;
	}

	@Override
	public String toString() {
		return itemCosts.toString();
	}
}