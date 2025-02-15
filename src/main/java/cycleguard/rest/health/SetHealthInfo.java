package cycleguard.rest.health;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.HealthInfoAccessor;
import cycleguard.database.accessor.HealthInfoAccessor.HealthInfo;
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
public final class SetHealthInfo {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private HealthInfoAccessor healthInfoAccessor;

	@PostMapping("/health/set")
	public String setHealthInfo(@RequestHeader("Token") String token, HttpServletResponse response,
	                                                     @RequestBody @NonNull HealthInfo healthInfo) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return "UNAUTHORIZED";
		}

		if (healthInfo.getAgeYears()<=0 || healthInfo.getHeightInches()<=0 || healthInfo.getWeightPounds()<=0) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return "NON POSITIVE VALUES";
		}

		healthInfo.setUsername(username);
		healthInfoAccessor.setEntry(healthInfo);

		return "OK";
	}
}
