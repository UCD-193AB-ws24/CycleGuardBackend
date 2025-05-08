package cycleguard.database.accessor;

import cycleguard.database.AbstractDatabaseEntry;
import cycleguard.database.AbstractDatabaseUserEntry;
import cycleguard.database.cache.CacheTimeToDelete;
import cycleguard.database.cache.DatabaseCacheService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Wrapper for DynamoDB database access.
 * Subclasses must implement <code>getTableInstance</code> to return a singleton instance of the DynamoDB table, along with
 * setting {@link EntryType} to the class to be stored.
 * <br>
 * See {@link UserProfileAccessor} for an example of extending this class.
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

	@Autowired
	private DatabaseCacheService databaseCacheService;
	private TreeMap<String, CacheTimeToDelete<EntryType>> cache = new TreeMap<>();
	private TreeMap<String, Long> blankEntries = new TreeMap<>();
	public void clearOldCacheEntries(long curSysTime) {
		for (var entry : new ArrayList<>(cache.entrySet())) {
			String key = entry.getKey();
			CacheTimeToDelete<EntryType> cached = entry.getValue();
			if (cached.getTimeToDelete() <= curSysTime) {
				if (cached.isDirty())
					getTableInstance().putItem(cached.getEntry());
				cache.remove(key);
			}
		}

		for (var entry : new ArrayList<>(blankEntries.entrySet())) {
			String key = entry.getKey();
			long timeToDelete = entry.getValue();
			if (timeToDelete <= curSysTime)
				blankEntries.remove(key);
		}
	}
	@PostConstruct
	public void subscribeToCache() {
		databaseCacheService.subscribe(this);
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
	 * Returns the entry in a table, given a {@link String}
	 * {@link DynamoDbPartitionKey}.
	 *
	 * @param key Value of partition key of the object to access
	 * @return Object of type {@link EntryType}: database object with that partition key<br>
	 * <code>null</code> if the key does not exist in the table
	 */
	public EntryType getEntry(String key) {
		long timeToDelete = System.currentTimeMillis() + DatabaseCacheService.CACHE_LIFETIME_MILLIS;
		CacheTimeToDelete<EntryType> cacheEntry = cache.getOrDefault(key, null);
		if (cacheEntry != null) {
			cacheEntry.setTimeToDelete(timeToDelete);
			return cacheEntry.getEntry();
		}

		EntryType entry = getTableInstance().getItem(getKey(key));
		cache.put(key, new CacheTimeToDelete<>(timeToDelete, entry, false));
		return getTableInstance().getItem(getKey(key));
	}

	/**
	 * Returns the entry in a table, given a {@link String}
	 * {@link DynamoDbPartitionKey}. If the entry doesn't exist in the database,
	 * return a blank entry instead.
	 *
	 * @param key Value of partition key of the object to access
	 * @return Object of type {@link EntryType}: database object with that partition key<br>
	 * Guaranteed to not return <code>null</code>
	 */
	@NonNull
	public EntryType getEntryOrDefaultBlank(String key) {
		long timeToDelete = System.currentTimeMillis() + DatabaseCacheService.CACHE_LIFETIME_MILLIS;
		if (blankEntries.containsKey(key)) {
			EntryType entry = getBlankEntry();
			entry.setPrimaryKey(key);
			blankEntries.put(key, timeToDelete);
			return entry;
		}
		EntryType entry = getEntry(key);
		if (entry == null) {
			entry = getBlankEntry();
			entry.setPrimaryKey(key);

			blankEntries.put(key, timeToDelete);
		}
		return entry;
	}

	/**
	 * Returns if the {@link String} {@link DynamoDbPartitionKey} has an associated entry in the table.
	 *
	 * @param key Value of partition key of the object to access
	 * @return <code>true</code> if the key is found in the table, <code>false</code> otherwise
	 */
	public boolean hasEntry(String key) {
		return cache.containsKey(key) ||
				getTableInstance().getItem(getKey(key)) != null;
	}

	/**
	 * Adds the provided <code>entry</code> into the table.
	 * @param entry Object to put into the database. Must have a getter annotated with
	 *              {@link DynamoDbPartitionKey},
	 *              along with a matching setter.
	 */
	public void setEntry(EntryType entry) {
		String key = entry.getPrimaryKey();
		var cached = cache.getOrDefault(key, null);
		long timeToDelete = System.currentTimeMillis() + DatabaseCacheService.CACHE_LIFETIME_MILLIS;

		blankEntries.remove(key);

		if (cached != null) {
			cached.setTimeToDelete(timeToDelete);
			cached.setEntry(entry);
			cached.setDirty(true);
			cached.incrementTimesWritten();

			if (cached.doWriteOverride()) {
				getTableInstance().putItem(cached.getEntry());
				cached.resetTimesWritten();
			}
		} else {
			cached = new CacheTimeToDelete<>(timeToDelete, entry, true);
			cache.put(key, cached);
		}
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
		blankEntries.remove(key);
		cache.remove(key);
	}

	/**
	 * Returns a blank instance of the database entry,
	 * of type {@link EntryType} extending {@link AbstractDatabaseEntry}.
	 *
	 * @return Blank {@link EntryType} of extending subclass
	 */
	protected abstract EntryType getBlankEntry();

	/**
	 * Must be implemented in subclasses. Returns a singleton instance of the {@link DynamoDbTable}.
	 * @return Singleton instance of the {@link DynamoDbTable}
	 */
	protected abstract DynamoDbTable<EntryType> getTableInstance();
}