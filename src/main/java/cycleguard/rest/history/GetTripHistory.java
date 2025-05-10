package cycleguard.rest.history;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.tripCoordinates.TripCoordinates;
import cycleguard.database.tripHistory.TripHistory;
import cycleguard.database.tripHistory.TripHistoryService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class GetTripHistory {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private TripHistoryService tripHistoryService;

	/**
	 * Endpoint to retrieve a user's full trip history.
	 * @return Non-null {@link TripHistory}
	 */
	@GetMapping("/history/getTripHistory")
	public TripHistory getWeekHistory(@RequestHeader("Token") String token, HttpServletResponse response) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		return tripHistoryService.getTripHistory(username);
	}
}
