package cycleguard.rest.friends;

import cycleguard.auth.AccessTokenManager;
import cycleguard.auth.AccountService;
import cycleguard.database.accessor.HealthInfoAccessor.HealthInfo;
import cycleguard.database.accessor.UserCredentialsAccessor;
import cycleguard.database.friendsList.FriendRequestService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint for a user to enter in health metrics.
 * Requires {@link HealthInfo} as body.
 */
@RestController
public final class SendFriendRequest {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private FriendRequestService friendRequestService;
	@Autowired
	private AccountService accountService;

	@PostMapping("/friends/sendFriendRequest")
	public String updateProfile(@RequestHeader("Token") String token, HttpServletResponse response,
	                                   @RequestBody @NonNull SingleUsername singleUsername) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return "UNAUTHORIZED";
		}

		String friendUsername = singleUsername.username;

		if (username.equals(friendUsername)) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return "CANNOT SEND SELF FRIEND REQUEST";
		}
		if (!accountService.accountExists(friendUsername)) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return "NO EXISTING ACCOUNT";
		}

		friendRequestService.sendFriendRequest(username, friendUsername);
		return "OK";
	}
}


