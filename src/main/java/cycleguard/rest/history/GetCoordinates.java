package cycleguard.rest.history;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.tripCoordinates.TripCoordinates;
import cycleguard.database.tripCoordinates.TripCoordinatesService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class GetCoordinates {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private TripCoordinatesService tripCoordinatesService;

	/**
	 * Endpoint to retrieve the coordinates of a previous ride.
	 * @return {@link TripCoordinates} or null if non-existent
	 */
	@GetMapping("/history/getCoordinates/{timestamp}")
	public TripCoordinates getWeekHistory(@RequestHeader("Token") String token, HttpServletResponse response,
	                                      @NonNull @PathVariable("timestamp") long timestamp) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		return tripCoordinatesService.getEntry(username, timestamp);
	}
}
