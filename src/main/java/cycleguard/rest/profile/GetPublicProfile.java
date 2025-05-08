package cycleguard.rest.profile;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.HealthInfoAccessor.HealthInfo;
import cycleguard.database.accessor.UserProfileAccessor;
import cycleguard.database.friendsList.FriendsListService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static cycleguard.database.accessor.UserProfileAccessor.UserProfile;

/**
 * Endpoint for a user to enter in health metrics.
 * Requires {@link HealthInfo} as body.
 */
@RestController
public final class GetPublicProfile {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private UserProfileAccessor userProfileAccessor;
	@Autowired
	private FriendsListService friendsListService;

	@GetMapping("/profile/getPublicProfile/{username}")
	public UserProfile getProfile(@RequestHeader("Token") String token, HttpServletResponse response,
	                              @NonNull @PathVariable("username") String username) {
		String curUsername = accessTokenManager.getUsernameFromToken(token);

		UserProfile profile = userProfileAccessor.getEntryOrDefaultBlank(username);
		if (profile.getIsPublic() || username.equals(curUsername)) {
			return profile;
		}

		if (friendsListService.getFriendsList(curUsername).getFriends().contains(username)) {
			return profile;
		}

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		return null;
	}
}
