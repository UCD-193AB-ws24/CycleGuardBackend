package cycleguard.database.friendsList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FriendRequestServiceTest {
	@Autowired
	private FriendRequestService friendRequestService;
	@Autowired
	private FriendsListService friendsListService;

	private final String user = "javagod123";
	private final String friend = "dreamwarrior";

	@Test
	void getFriendRequestList() {
		friendRequestService.getFriendRequestList("javagod123");
	}

	@Test
	void sendAndDecline() {
		assert !friendsListService.getFriendsList(user).getFriends().contains(friend);
		friendRequestService.sendFriendRequest(user, friend);
		friendRequestService.declineFriendRequest(friend, user);
		assert !friendsListService.getFriendsList(user).getFriends().contains(friend);
	}

	@Test
	void sendAndCancel() {
		assert !friendsListService.getFriendsList(user).getFriends().contains(friend);
		friendRequestService.sendFriendRequest(user, friend);
		friendRequestService.cancelFriendRequest(user, friend);
		assert !friendsListService.getFriendsList(user).getFriends().contains(friend);
	}

	@Test
	void sendAndAccept() {
		assert !friendsListService.getFriendsList(user).getFriends().contains(friend);
		friendRequestService.sendFriendRequest(user, friend);
		friendRequestService.acceptFriendRequest(user, friend);
		assert friendsListService.getFriendsList(user).getFriends().contains(friend);
		friendsListService.removeFriend(user, friend);
		assert !friendsListService.getFriendsList(user).getFriends().contains(friend);
	}
}