package cycleguard.database.friendsList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendRequestService {
	@Autowired
	private FriendsListService friendsListService;
	@Autowired
	private FriendRequestAccessor friendRequestAccessor;

	public FriendRequestList getFriendRequestList(String username) {
		return friendRequestAccessor.getEntryOrDefaultBlank(username);
	}

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

	public void cancelFriendRequest(String username, String friendUsername) {
		FriendRequestList myFriendRequests = friendRequestAccessor.getEntryOrDefaultBlank(username);
		FriendRequestList otherFriendRequests = friendRequestAccessor.getEntryOrDefaultBlank(friendUsername);

		myFriendRequests.getPendingFriendRequests().remove(friendUsername);
		otherFriendRequests.getReceivedFriendRequests().remove(username);

		friendRequestAccessor.setEntry(myFriendRequests);
		friendRequestAccessor.setEntry(otherFriendRequests);
	}



	public void acceptFriendRequest(String username, String friendUsername) {
		declineFriendRequest(username, friendUsername);
		friendsListService.addFriend(username, friendUsername);
	}

	public void declineFriendRequest(String username, String friendUsername) {
		cancelFriendRequest(friendUsername, username);
	}
}
