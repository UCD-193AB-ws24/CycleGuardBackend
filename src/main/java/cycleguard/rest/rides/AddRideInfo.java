package cycleguard.rest.rides;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.HealthInfoAccessor.HealthInfo;
import cycleguard.database.achievements.AchievementInfoService;
import cycleguard.database.rides.ProcessRideService;
import cycleguard.database.rides.ProcessRideService.RideInfo;
import cycleguard.database.stats.UserStatsService;
import cycleguard.util.StringDoubles;
import cycleguard.util.TimeUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public final class AddRideInfo {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private ProcessRideService processRideService;

	/**
	 * Endpoint handling processing of a ride.
	 * @param rideInfo Newly-completed ride to process
	 * @return Seconds since epoch, used for trip coordinates
	 */
	@PostMapping("/rides/addRide")
	public String getHealthInfo(@RequestHeader("Token") String token, HttpServletResponse response,
	                                 @RequestBody RideInfo rideInfo) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return "UNAUTHORIZED";
		}

		System.out.println(rideInfo);
		long timestamp = processRideService.processNewRide(username, rideInfo);

		return Long.toString(timestamp);
	}
}
