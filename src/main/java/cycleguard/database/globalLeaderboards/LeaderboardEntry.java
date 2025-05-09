package cycleguard.database.globalLeaderboards;

import cycleguard.util.StringDoubles;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

/**
 * {@link DynamoDbBean} of an entry on a leaderboard. No primary key.
 * <br>
 *
 * <ul>
 *     <li>{@link LeaderboardEntry#username} - User which entry belongs to</li>
 *     <li>{@link LeaderboardEntry#value} - Distance or time value of user, depending on {@link GlobalLeaderboards field}</li>
 * </ul>
 */
@DynamoDbBean
public class LeaderboardEntry implements Comparable<LeaderboardEntry> {
	public String username;
	public String value;

	public LeaderboardEntry(){}
	public LeaderboardEntry(String username, String value) {
		this.username = username;
		this.value = value;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int compareTo(LeaderboardEntry o) {
		double diff = StringDoubles.toDouble(this.value) - StringDoubles.toDouble(o.value);
		if (diff<0) return 1;
		else if (diff>0) return -1;
		else return this.username.compareTo(o.username);
	}
}
