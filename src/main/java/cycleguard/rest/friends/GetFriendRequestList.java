package cycleguard.rest.friends;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.friendsList.FriendRequestList;
import cycleguard.database.friendsList.FriendRequestService;
import cycleguard.database.friendsList.FriendsList;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class GetFriendRequestList {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private FriendRequestService friendRequestService;

	/**
	 * Endpoint to retrieve a user's friend requests list.
	 * @return Non-null {@link FriendRequestList}
	 */
	@GetMapping("/friends/getFriendRequestList")
	public FriendRequestList getFriendsList(@RequestHeader("Token") String token, HttpServletResponse response) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		return friendRequestService.getFriendRequestList(username);
	}
}
