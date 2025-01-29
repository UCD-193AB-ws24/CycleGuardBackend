package cycleguard.database;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Component
@Configuration
public abstract class DatabaseAccessor<EntryType extends AbstractDatabaseEntry> {
	protected DynamoDbEnhancedClient client;

	protected DatabaseAccessor(){
		DynamoDbClient ddb = DynamoDbClient.builder()
				.region(Region.US_WEST_1)
				.credentialsProvider(EnvironmentVariableCredentialsProvider.create())
				.build();
		client = DynamoDbEnhancedClient.builder().dynamoDbClient(ddb).build();
	}

	private Key getKey(long key) {
		return software.amazon.awssdk.enhanced.dynamodb.Key.builder().partitionValue(key).build();
	}

	public EntryType getEntry(long key) {
		return getTableInstance().getItem(getKey(key));
	}

	public void setEntry(EntryType entry) {
		getTableInstance().putItem(entry);
	}

	public void deleteEntry(long key) {
		getTableInstance().deleteItem(getKey(key));
	}

	protected abstract DynamoDbTable<EntryType> getTableInstance();
}