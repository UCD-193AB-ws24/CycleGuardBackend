package cycleguard.database.entry;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
public final class UserInfo extends AbstractDatabaseUserEntry {
	private String name;
	private long cycleCoins;

	public long getCycleCoins() {
		return cycleCoins;
	}

	public void setCycleCoins(long cycleCoins) {
		this.cycleCoins = cycleCoins;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
