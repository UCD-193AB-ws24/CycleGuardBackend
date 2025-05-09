package cycleguard.database.friendsList;

import cycleguard.database.AbstractDatabaseUserEntry;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link DynamoDbBean} linking a username to that user's received and pending friend requests.
 * <br>
 *
 * <ul>
 *     <li>{@link FriendRequestList#receivedFriendRequests} - List of usernames who sent a request</li>
 *     <li>{@link FriendRequestList#receivedFriendRequests} - List of usernames pending a request response</li>
 * </ul>
 */
@DynamoDbBean
public final class FriendRequestList extends AbstractDatabaseUserEntry {
	private List<String> receivedFriendRequests = new ArrayList<>();
	private List<String> pendingFriendRequests = new ArrayList<>();

	public List<String> getReceivedFriendRequests() {
		return receivedFriendRequests;
	}

	public void setReceivedFriendRequests(List<String> receivedFriendRequests) {
		this.receivedFriendRequests = receivedFriendRequests;
	}

	public List<String> getPendingFriendRequests() {
		return pendingFriendRequests;
	}

	public void setPendingFriendRequests(List<String> pendingFriendRequests) {
		this.pendingFriendRequests = pendingFriendRequests;
	}
}
