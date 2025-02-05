package cycleguard.database.entry;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

/**
 * Abstract {@link DynamoDbBean} class to be saved within a DynamoDB table mapping username to some data.
 * Defines primary key as <code>username</code> with getter and setter.
 * <br>
 * Subclasses with a primary key other than <code>username</code> should extend {@link AbstractDatabaseEntry} instead.
 * <br><br>
 * <b>Must</b> implement getters and setters for each instance variable other than <code>username</code>.
 */
@DynamoDbBean
public abstract class AbstractDatabaseUserEntry extends AbstractDatabaseEntry {
	private String username;

	@DynamoDbPartitionKey
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
