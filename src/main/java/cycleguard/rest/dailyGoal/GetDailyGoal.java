package cycleguard.rest.dailyGoal;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.UserDailyGoalAccessor;
import cycleguard.database.accessor.UserDailyGoalAccessor.UserDailyGoal;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class GetDailyGoal {
	@Autowired
	private UserDailyGoalAccessor userDailyGoalAccessor;
	@Autowired
	private AccessTokenManager accessTokenManager;

	/**
	 * Endpoint for a user to retrieve a daily goal.
	 * @return {@link UserDailyGoal} linked to user
	 */
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
