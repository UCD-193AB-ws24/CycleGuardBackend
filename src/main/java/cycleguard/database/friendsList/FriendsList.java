package cycleguard.database.friendsList;

import cycleguard.database.AbstractDatabaseEntry;
import cycleguard.database.AbstractDatabaseUserEntry;
import cycleguard.database.rides.ProcessRideService;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link DynamoDbBean} linking a username to that user's week history.
 *
 * <br>
 * <ul>
 *     <li>cycleCoins - number of CycleCoins the user has.</li>
 * </ul>
 */
@DynamoDbBean
public final class FriendsList extends AbstractDatabaseUserEntry {
	private List<String> friends = new ArrayList<>();

	public List<String> getFriends() {
		return friends;
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
	}
}
