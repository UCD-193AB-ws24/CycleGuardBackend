package cycleguard.database.accessor;

import cycleguard.database.AbstractDatabaseEntry;
import cycleguard.database.AbstractDatabaseUserEntry;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

/**
 * Wrapper for DynamoDB database access.
 * Subclasses must implement <code>getTableInstance</code> to return a singleton instance of the DynamoDB table, along with
 * setting {@link EntryType} to the class to be stored.
 * <br>
 * See {@link UserInfoAccessor} for an example of extending this class.
 * <br><br>
 * When this class is extended and a new table is added, the AWS IAM policy must be updated to include that table
 * (ask Jason to update that).
 *
 *
 *
 * @param <EntryType> Object class to be stored within the DynamoDB table.
 *                   This class must implement {@link AbstractDatabaseEntry},
 *                   be annotated {@link DynamoDbBean},
 *                   and have a getter annotated with
 *                   {@link DynamoDbPartitionKey}.
 *                   <br>
 *                   The {@link AbstractDatabaseUserEntry} handles the partition key,
 *                   automatically setting it to the user ID.
 */
@Component
@Configuration
public abstract class AbstractDatabaseAccessor<EntryType extends AbstractDatabaseEntry> {
	private static final DynamoDbEnhancedClient client;

	static {
		DynamoDbClient ddb = DynamoDbClient.builder()
				.region(Region.US_WEST_1)
				.credentialsProvider(EnvironmentVariableCredentialsProvider.create())
				.build();
		client = DynamoDbEnhancedClient.builder().dynamoDbClient(ddb).build();
	}

	/**
	 * Gets the singleton instance of the {@link DynamoDbEnhancedClient}, which is used
	 * to access tables.
	 * @return Singleton {@link DynamoDbEnhancedClient}
	 */
	protected DynamoDbEnhancedClient getClient() {
		return client;
	}

	/**
	 * Returns the DynamoDB-specific key to a table, given a <code>long</code>
	 * {@link DynamoDbPartitionKey}.
	 *
	 * @param key Partition key of the object to access
	 * @return {@link Key} to access an object of the current table
	 */
	private Key getKey(long key) {
		return Key.builder().partitionValue(key).build();
	}

	/**
	 * Returns the DynamoDB-specific key to a table, given a {@link String}
	 * {@link DynamoDbPartitionKey}.
	 *
	 * @param key Partition key of the object to access
	 * @return {@link Key} to access an object of the current table
	 */
	private Key getKey(String key) {
		return Key.builder().partitionValue(key).build();
	}

	/**
	 * Returns the entry in a table, given a <code>long</code>
	 * {@link DynamoDbPartitionKey}.
	 *
	 * @param key Value of partition key of the object to access
	 * @return Object of type {@link EntryType}: database object with that partition key<br>
	 * <code>null</code> if the key does not exist in the table
	 */
	public EntryType getEntry(long key) {
		return getTableInstance().getItem(getKey(key));
	}

	/**
	 * Returns the entry in a table, given a <code>long</code>
	 * {@link DynamoDbPartitionKey}.
	 *
	 * @param key Value of partition key of the object to access
	 * @return Object of type {@link EntryType}: database object with that partition key<br>
	 * <code>null</code> if the key does not exist in the table
	 */
	public EntryType getEntry(String key) {
		return getTableInstance().getItem(getKey(key));
	}

	/**
	 * Returns if the <code>long</code> {@link DynamoDbPartitionKey} has an associated entry in the table.
	 *
	 * @param key Value of partition key of the object to access
	 * @return <code>true</code> if the key is found in the table, <code>false</code> otherwise
	 */
	public boolean hasEntry(long key) {
		return getTableInstance().getItem(getKey(key)) != null;
	}

	/**
	 * Returns if the {@link String} {@link DynamoDbPartitionKey} has an associated entry in the table.
	 *
	 * @param key Value of partition key of the object to access
	 * @return <code>true</code> if the key is found in the table, <code>false</code> otherwise
	 */
	public boolean hasEntry(String key) {
		return getTableInstance().getItem(getKey(key)) != null;
	}

	/**
	 * Adds the provided <code>entry</code> into the table.
	 * @param entry Object to put into the database. Must have a getter annotated with
	 *              {@link DynamoDbPartitionKey},
	 *              along with a matching setter.
	 */
	public void setEntry(EntryType entry) {
		getTableInstance().putItem(entry);
	}

	/**
	 * Deletes the item in the table with
	 * {@link DynamoDbPartitionKey}
	 * matching the provided <code>long</code> <code>key</code>. Does nothing if key is not found.
	 *
	 * @param key Value of partition key of the object to delete
	 */
	public void deleteEntry(long key) {
		getTableInstance().deleteItem(getKey(key));
	}

	/**
	 * Deletes the item in the table with
	 * {@link DynamoDbPartitionKey}
	 * matching the provided {@link String} <code>key</code>. Does nothing if key is not found.
	 *
	 * @param key Value of partition key of the object to delete
	 */
	public void deleteEntry(String key) {
		getTableInstance().deleteItem(getKey(key));
	}

	/**
	 * Must be implemented in subclasses. Returns a singleton instance of the {@link DynamoDbTable}.
	 * @return Singleton instance of the {@link DynamoDbTable}
	 */
	protected abstract DynamoDbTable<EntryType> getTableInstance();
}