package cycleguard.rest.friends;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.friendsList.FriendsList;
import cycleguard.database.friendsList.FriendsListService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class RemoveFriend {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private FriendsListService friendsListService;

	/**
	 * Endpoint to remove a friend.
	 * @param singleUsername Username to remove
	 * @return Error message on fail, or OK
	 */
	@PostMapping("/friends/removeFriend")
	public String removeFriend(@RequestHeader("Token") String token, HttpServletResponse response,
	                                   @RequestBody @NonNull SingleUsername singleUsername) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return "UNAUTHORIZED";
		}

		String friendUsername = singleUsername.username;
		if (username.equals(friendUsername)) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return "CANNOT REMOVE SELF";
		}


		friendsListService.removeFriend(username, friendUsername);
		return "OK";
	}
}


