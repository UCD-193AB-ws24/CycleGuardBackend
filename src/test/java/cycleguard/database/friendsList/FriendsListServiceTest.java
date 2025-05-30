package cycleguard.database.friendsList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FriendsListServiceTest {
	@Autowired
	private FriendsListService friendsListService;

	private final String user = "javagod123";
	private final String friend = "dreamwarrior";

	@Test
	void getFriendsList() {
		friendsListService.getFriendsList(user);
	}

	@Test
	void setBestFriend() {
		assert friendsListService.getFriendsList(user).getBestFriend()==null;
		friendsListService.addFriend(user, friend);
		assert friendsListService.getFriendsList(user).getFriends().contains(friend);
		assert friendsListService.getFriendsList(friend).getFriends().contains(user);

		friendsListService.setBestFriend(user, friend);
		assert friendsListService.getFriendsList(user).getBestFriend().equals(friend);
		friendsListService.setBestFriend(user, null);
		assert friendsListService.getFriendsList(user).getBestFriend()==null;

		friendsListService.removeFriend(friend, user);
		assert !friendsListService.getFriendsList(user).getFriends().contains(friend);
		assert !friendsListService.getFriendsList(friend).getFriends().contains(user);
	}
}