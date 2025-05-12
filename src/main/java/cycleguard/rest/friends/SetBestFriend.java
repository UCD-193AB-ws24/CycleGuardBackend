package cycleguard.rest.friends;

import cycleguard.auth.AccessTokenManager;
import cycleguard.auth.AccountService;
import cycleguard.database.friendsList.FriendRequestService;
import cycleguard.database.friendsList.FriendsListService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class SetBestFriend {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private FriendsListService friendsListService;
	@Autowired
	private AccountService accountService;

	/**
	 * Endpoint to set a best friend. Friend must already be in friends list.
	 * @param singleUsername Username to set
	 * @return Error message on fail, or OK
	 */
	@PostMapping("/friends/setBestFriend")
	public String setBestFriend(@RequestHeader("Token") String token, HttpServletResponse response,
	                                   @RequestBody @NonNull SingleUsername singleUsername) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return "UNAUTHORIZED";
		}

		String friendUsername = singleUsername.username;
		if (username.equals(friendUsername)) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			return "CANNOT SEND SELF FRIEND REQUEST";
		}

		friendsListService.setBestFriend(username, friendUsername);
		return "OK";
	}
}


