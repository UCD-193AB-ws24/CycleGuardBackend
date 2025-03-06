package cycleguard.rest.settings;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.HealthInfoAccessor.HealthInfo;
import cycleguard.database.accessor.UserSettingsAccessor;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import static cycleguard.database.accessor.UserSettingsAccessor.UserSettings;

/**
 * Endpoint for a user to enter in health metrics.
 * Requires {@link HealthInfo} as body.
 */
@RestController
public final class UpdateUserSettings {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private UserSettingsAccessor userSettingsAccessor;

	@PostMapping("/user/updateSettings")
	public String updateSettings(@RequestHeader("Token") String token, HttpServletResponse response,
	                                   @RequestBody @NonNull UserSettings settings) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return "UNAUTHORIZED";
		}
		settings.setUsername(username);

		userSettingsAccessor.setEntry(settings);
		return "OK";
	}
}
