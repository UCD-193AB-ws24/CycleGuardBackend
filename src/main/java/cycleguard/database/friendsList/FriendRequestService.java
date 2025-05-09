package cycleguard.database.friendsList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service wrapper for retrieving and modifying {@link FriendRequestList}.
 */
@Service
public class FriendRequestService {
	@Autowired
	private FriendsListService friendsListService;
	@Autowired
	private FriendRequestAccessor friendRequestAccessor;

	public FriendRequestList getFriendRequestList(String username) {
		return friendRequestAccessor.getEntryOrDefaultBlank(username);
	}

	/**
	 * Send a friend request to another user.
	 * @param username User sending request
	 * @param friendUsername User receiving request
	 */
	public void sendFriendRequest(String username, String friendUsername) {
		FriendsList myFriendsList = friendsListService.getFriendsList(username);
		if (myFriendsList.getFriends().contains(friendUsername)) return;

		FriendRequestList myFriendRequests = friendRequestAccessor.getEntryOrDefaultBlank(username);
		FriendRequestList otherFriendRequests = friendRequestAccessor.getEntryOrDefaultBlank(friendUsername);

		if (!myFriendRequests.getPendingFriendRequests().contains(friendUsername))
			myFriendRequests.getPendingFriendRequests().add(friendUsername);
		if (!otherFriendRequests.getReceivedFriendRequests().contains(username))
			otherFriendRequests.getReceivedFriendRequests().add(username);

		friendRequestAccessor.setEntry(myFriendRequests);
		friendRequestAccessor.setEntry(otherFriendRequests);
	}

	/**
	 * Cancel a friend request to another user.
	 * @param username User sending request
	 * @param friendUsername User receiving request
	 */
	public void cancelFriendRequest(String username, String friendUsername) {
		FriendRequestList myFriendRequests = friendRequestAccessor.getEntryOrDefaultBlank(username);
		FriendRequestList otherFriendRequests = friendRequestAccessor.getEntryOrDefaultBlank(friendUsername);

		myFriendRequests.getPendingFriendRequests().remove(friendUsername);
		otherFriendRequests.getReceivedFriendRequests().remove(username);

		friendRequestAccessor.setEntry(myFriendRequests);
		friendRequestAccessor.setEntry(otherFriendRequests);
	}

	/**
	 * Accept a friend request from another user.
	 * @param username User receiving request
	 * @param friendUsername User sending request
	 */
	public void acceptFriendRequest(String username, String friendUsername) {
		declineFriendRequest(username, friendUsername);
		friendsListService.addFriend(username, friendUsername);
	}

	/**
	 * Decline a friend request from another user.
	 * @param username User receiving request
	 * @param friendUsername User sending request
	 */
	public void declineFriendRequest(String username, String friendUsername) {
		cancelFriendRequest(friendUsername, username);
	}
}
