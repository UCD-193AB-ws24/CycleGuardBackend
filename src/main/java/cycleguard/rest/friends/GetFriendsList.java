package cycleguard.rest.friends;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.HealthInfoAccessor.HealthInfo;
import cycleguard.database.friendsList.FriendsList;
import cycleguard.database.friendsList.FriendsListService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint for a user to enter in health metrics.
 * Requires {@link HealthInfo} as body.
 */
@RestController
public final class GetFriendsList {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private FriendsListService friendsListService;

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
