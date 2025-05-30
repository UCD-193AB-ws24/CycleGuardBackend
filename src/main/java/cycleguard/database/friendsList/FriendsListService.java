package cycleguard.database.friendsList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service wrapper for retrieving and modifying {@link FriendsList}.
 */
@Service
public class FriendsListService {
	@Autowired
	private FriendsListAccessor friendsListAccessor;

	/**
	 * Return the friends list.
	 * @param username Username of querying user
	 * @return {@link FriendsList} of user
	 */
	public FriendsList getFriendsList(String username) {
		return friendsListAccessor.getEntryOrDefaultBlank(username);
	}

	/**
	 * Befriend a pair of users.
	 * @param username First user
	 * @param friendUsername Second user
	 */
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

	/**
	 * Unfriend a pair of users.
	 * @param username First user
	 * @param friendUsername Second user
	 */
	public void removeFriend(String username, String friendUsername) {
		FriendsList myFriendsList = getFriendsList(username);
		FriendsList otherFriendsList = getFriendsList(friendUsername);

		myFriendsList.getFriends().remove(friendUsername);
		otherFriendsList.getFriends().remove(username);

		friendsListAccessor.setEntry(myFriendsList);
		friendsListAccessor.setEntry(otherFriendsList);
	}


	public void setBestFriend(String username, String friendUsername) {
//		System.out.println("Setting best friend: "+friendUsername);
		FriendsList myFriendsList = getFriendsList(username);

		if (myFriendsList.getBestFriend()==null && friendUsername==null) return;
		if (friendUsername==null) {
			myFriendsList.setBestFriend(null);
			friendsListAccessor.setEntry(myFriendsList);
		}

		if (!myFriendsList.getFriends().contains(friendUsername)) return;
		myFriendsList.setBestFriend(friendUsername);
		friendsListAccessor.setEntry(myFriendsList);
	}
}
