package cycleguard.rest.friends;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.friendsList.FriendsList;
import cycleguard.database.friendsList.FriendsListService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class GetFriendsList {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private FriendsListService friendsListService;

	/**
	 * Endpoint to retrieve a user's friends list.
	 * @return Non-null {@link FriendsList}
	 */
	@GetMapping("/friends/getFriendsList")
	public FriendsList getFriendsList(@RequestHeader("Token") String token, HttpServletResponse response) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		return friendsListService.getFriendsList(username);
	}
}
