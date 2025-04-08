package cycleguard.rest.dailyGoal;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.HealthInfoAccessor.HealthInfo;
import cycleguard.database.accessor.UserDailyGoalAccessor;
import cycleguard.database.accessor.UserDailyGoalAccessor.UserDailyGoal;
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
public final class GetDailyGoal {
	@Autowired
	private UserDailyGoalAccessor userDailyGoalAccessor;
	@Autowired
	private AccessTokenManager accessTokenManager;

	@GetMapping("/daily/getDailyGoal")
	public UserDailyGoal getDailyGoal(@RequestHeader("Token") String token, HttpServletResponse response) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		return userDailyGoalAccessor.getEntry(username);
	}
}
