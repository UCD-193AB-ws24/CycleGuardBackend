package cycleguard.database;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

/**
 * Abstract {@link DynamoDbBean} class to be saved within a DynamoDB table.
 * <br>
 * Subclasses with a primary key of <code>username</code> should extend {@link AbstractDatabaseUserEntry} instead.
 * <br><br>
 * <b>Must</b> implement getters and setters for each instance variable, and define a primary key
 * using {@link DynamoDbPartitionKey}.
 */
@DynamoDbBean
public abstract class AbstractDatabaseEntry {
	public abstract void setPrimaryKey(String key);
	public abstract String getPrimaryKey();
}
