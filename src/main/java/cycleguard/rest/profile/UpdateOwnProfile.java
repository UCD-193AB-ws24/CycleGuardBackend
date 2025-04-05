package cycleguard.rest.profile;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.HealthInfoAccessor.HealthInfo;
import cycleguard.database.accessor.UserProfileAccessor;
import cycleguard.database.accessor.UserSettingsAccessor;
import cycleguard.database.globalLeaderboards.GlobalLeaderboardsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static cycleguard.database.accessor.UserProfileAccessor.*;
import static cycleguard.database.accessor.UserSettingsAccessor.UserSettings;

/**
 * Endpoint for a user to enter in health metrics.
 * Requires {@link HealthInfo} as body.
 */
@RestController
public final class UpdateOwnProfile {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private UserProfileAccessor userProfileAccessor;
	@Autowired
	private GlobalLeaderboardsService globalLeaderboardsService;

	@PostMapping("/profile/updateProfile")
	public String updateProfile(@RequestHeader("Token") String token, HttpServletResponse response,
	                                   @RequestBody @NonNull UserProfile userProfile) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return "UNAUTHORIZED";
		}
		userProfile.setUsername(username);

		userProfileAccessor.setEntry(userProfile);

		globalLeaderboardsService.processNewRide(username, null, null);
		return "OK";
	}
}
