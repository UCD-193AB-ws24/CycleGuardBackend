package cycleguard.database.globalLeaderboards;

import cycleguard.database.AbstractDatabaseEntry;
import cycleguard.database.AbstractDatabaseUserEntry;
import cycleguard.database.friendsList.FriendRequestList;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * {@link DynamoDbBean} linking a username to that user's received and pending friend requests.
 * <br>
 *
 * <ul>
 *     <li>{@link GlobalLeaderboards#leaderboardName} - Name of leaderboard: <code>distance</code> or <code>time</code></li>
 *     <li>{@link GlobalLeaderboards#entries} - List of {@link LeaderboardEntry} on the current leaderboard</li>
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