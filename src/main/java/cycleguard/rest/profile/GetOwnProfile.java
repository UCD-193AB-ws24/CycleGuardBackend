package cycleguard.rest.profile;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.HealthInfoAccessor.HealthInfo;
import cycleguard.database.accessor.UserProfileAccessor;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static cycleguard.database.accessor.UserProfileAccessor.*;
import static cycleguard.database.accessor.UserSettingsAccessor.UserSettings;

/**
 * Endpoint for a user to enter in health metrics.
 * Requires {@link HealthInfo} as body.
 */
@RestController
public final class GetOwnProfile {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private UserProfileAccessor userProfileAccessor;

	@GetMapping("/profile/getProfile")
	public UserProfile getProfile(@RequestHeader("Token") String token, HttpServletResponse response) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		return userProfileAccessor.getEntry(username);
	}
}
