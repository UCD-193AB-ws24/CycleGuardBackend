package cycleguard.rest.user;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.stats.UserStats;
import cycleguard.database.stats.UserStatsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public final class GetUserStats {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private UserStatsService userStatsService;

	/**
	 * Endpoint for retrieving a user's statistics.
	 * @return Non-null {@link UserStats}
	 */
	@GetMapping("/getStats")
	public UserStats getStats(@RequestHeader("Token") String token, HttpServletResponse response) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		return userStatsService.getUserStats(username);
	}
}
