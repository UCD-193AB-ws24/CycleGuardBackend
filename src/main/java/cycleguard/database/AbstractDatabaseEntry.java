package cycleguard.database;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

// This abstract class stuff is the only useful thing I learned from Posnett ECS160
@DynamoDbBean
public abstract class AbstractDatabaseEntry {
	private long userId;

	@DynamoDbPartitionKey
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
