package cycleguard.database.entry;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

/**
 * Abstract class
 */
@DynamoDbBean
public abstract class AbstractDatabaseUserEntry extends AbstractDatabaseEntry {
	private long userId;

	@DynamoDbPartitionKey
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
