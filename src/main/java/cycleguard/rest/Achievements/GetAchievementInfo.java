package cycleguard.rest.Achievements;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.HealthInfoAccessor.HealthInfo;
import cycleguard.database.rides.WeekHistory;
import cycleguard.database.rides.WeekHistoryService;
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
public final class GetAchievementInfo {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private WeekHistoryService weekHistoryService;

	@GetMapping("/rides/getWeekHistory")
	public WeekHistory getHealthInfo(@RequestHeader("Token") String token, HttpServletResponse response) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		return weekHistoryService.getWeekHistory(username);
	}
}
