package cycleguard.database.friendsList;

import cycleguard.database.AbstractDatabaseUserEntry;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link DynamoDbBean} linking a username to that user's friends list and best friend.
 * <br>
 *
 * <ul>
 *     <li>{@link FriendsList#friends} - List of usernames who are friends</li>
 *     <li>{@link FriendsList#bestFriend} - Current best friend</li>
 * </ul>
 */
@DynamoDbBean
public final class FriendsList extends AbstractDatabaseUserEntry {
	private List<String> friends = new ArrayList<>();
	private String bestFriend = null;

	public List<String> getFriends() {
		return friends;
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
	}

	public String getBestFriend() {
		return bestFriend;
	}

	public void setBestFriend(String bestFriend) {
		this.bestFriend = bestFriend;
	}
}
