package cycleguard.rest.leaderboards;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.globalLeaderboards.GlobalLeaderboards;
import cycleguard.database.globalLeaderboards.GlobalLeaderboardsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class GetTimeLeaderboards {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private GlobalLeaderboardsService globalLeaderboardsService;

	/**
	 * Endpoint to retrieve the global time leaderboards.
	 * @return Non-null {@link GlobalLeaderboards}
	 */
	@GetMapping("/leaderboards/getTimeLeaderboards")
	public GlobalLeaderboards getWeekHistory(@RequestHeader("Token") String token, HttpServletResponse response) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		return globalLeaderboardsService.getTimeLeaderboards();
	}
}
