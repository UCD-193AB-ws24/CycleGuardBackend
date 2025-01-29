package cycleguard.database;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public final class UserInfo extends cycleguard.database.AbstractDatabaseEntry {
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
