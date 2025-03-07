package cycleguard.database.friendsList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendsListService {
	@Autowired
	private FriendsListAccessor friendsListAccessor;

	public FriendsList getFriendsList(String username) {
		return friendsListAccessor.getEntryOrDefaultBlank(username);
	}

	void addFriend(String username, String friendUsername) {
		FriendsList myFriendsList = getFriendsList(username);
		FriendsList otherFriendsList = getFriendsList(friendUsername);

		if (!myFriendsList.getFriends().contains(friendUsername))
			myFriendsList.getFriends().add(friendUsername);
		if (!otherFriendsList.getFriends().contains(username))
			otherFriendsList.getFriends().add(username);

		friendsListAccessor.setEntry(myFriendsList);
		friendsListAccessor.setEntry(otherFriendsList);
	}

	public void removeFriend(String username, String friendUsername) {
		FriendsList myFriendsList = getFriendsList(username);
		FriendsList otherFriendsList = getFriendsList(friendUsername);

		myFriendsList.getFriends().remove(friendUsername);
		otherFriendsList.getFriends().remove(username);

		friendsListAccessor.setEntry(myFriendsList);
		friendsListAccessor.setEntry(otherFriendsList);
	}
}
