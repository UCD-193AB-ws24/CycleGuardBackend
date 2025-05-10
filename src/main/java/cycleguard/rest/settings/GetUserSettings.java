package cycleguard.rest.settings;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.UserSettingsAccessor;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static cycleguard.database.accessor.UserSettingsAccessor.UserSettings;

@RestController
public final class GetUserSettings {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private UserSettingsAccessor userSettingsAccessor;

	/**
	 * Endpoint for retrieving a user's configured settings.
	 * @return Non-null {@link UserSettings}
	 */
	@GetMapping("/user/getSettings")
	public UserSettings getSettings(@RequestHeader("Token") String token, HttpServletResponse response) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		return userSettingsAccessor.getEntryOrDefaultBlank(username);
	}
}
