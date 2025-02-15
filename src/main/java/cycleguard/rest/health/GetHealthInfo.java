package cycleguard.rest.health;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.HealthInfoAccessor;
import cycleguard.database.accessor.HealthInfoAccessor.HealthInfo;
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
public final class GetHealthInfo {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private HealthInfoAccessor healthInfoAccessor;

	@GetMapping("/health/get")
	public HealthInfo getHealthInfo(@RequestHeader("Token") String token, HttpServletResponse response) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		return healthInfoAccessor.getEntryOrDefaultBlank(username);
	}
}
