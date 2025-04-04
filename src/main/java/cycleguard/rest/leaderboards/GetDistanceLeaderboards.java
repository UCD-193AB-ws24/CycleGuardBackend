package cycleguard.rest.leaderboards;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.HealthInfoAccessor.HealthInfo;
import cycleguard.database.globalLeaderboards.GlobalLeaderboards;
import cycleguard.database.globalLeaderboards.GlobalLeaderboardsService;
import cycleguard.database.tripHistory.TripHistory;
import cycleguard.database.tripHistory.TripHistoryService;
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
public final class GetDistanceLeaderboards {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private GlobalLeaderboardsService globalLeaderboardsService;

	@GetMapping("/leaderboards/getDistanceLeaderboards")
	public GlobalLeaderboards getWeekHistory(@RequestHeader("Token") String token, HttpServletResponse response) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		return globalLeaderboardsService.getDistanceLeaderboards();
	}
}
