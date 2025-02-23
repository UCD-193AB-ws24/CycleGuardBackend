package cycleguard.rest.rides;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.HealthInfoAccessor.HealthInfo;
import cycleguard.database.achievements.AchievementInfoService;
import cycleguard.database.rides.ProcessRideService;
import cycleguard.database.rides.ProcessRideService.RideInfo;
import cycleguard.database.stats.UserStatsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoint for a user to enter in health metrics.
 * Requires {@link HealthInfo} as body.
 */
@RestController
public final class AddRideInfo {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private ProcessRideService processRideService;
	@Autowired
	private UserStatsService userStatsService;
	@Autowired
	private AchievementInfoService achievementInfoService;

	@PostMapping("/rides/addRide")
	public String getHealthInfo(@RequestHeader("Token") String token, HttpServletResponse response,
	                                 @RequestBody RideInfo rideInfo) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return "UNAUTHORIZED";
		}

		processRideService.processNewRide(username, rideInfo);
		userStatsService.processNewRide(username, rideInfo);
		achievementInfoService.processNewRide(username, rideInfo);

		return "OK";
	}
}
