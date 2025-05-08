package cycleguard.database.globalLeaderboards;

import cycleguard.database.AbstractDatabaseEntry;
import cycleguard.database.AbstractDatabaseUserEntry;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * {@link DynamoDbBean} linking a username to that user's stats.
 *
 * <br>
 * <ul>
 *     <li>cycleCoins - number of CycleCoins the user has.</li>
 * </ul>
 */
@DynamoDbBean
public final class GlobalLeaderboards extends AbstractDatabaseEntry {
	private String leaderboardName="";
	private List<LeaderboardEntry> entries = new ArrayList<>();

	@DynamoDbPartitionKey
	public String getLeaderboardName() {
		return leaderboardName;
	}

	public void setLeaderboardName(String leaderboardName) {
		this.leaderboardName = leaderboardName;
	}

	public List<LeaderboardEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<LeaderboardEntry> entries) {
		this.entries = entries;
	}

	@Override
	public void setPrimaryKey(String key) {
		leaderboardName = key;
	}

	@Override
	public String getPrimaryKey() {
		return getLeaderboardName();
	}
}