package cycleguard.rest.dailyGoal;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.UserDailyGoalAccessor;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class DeleteDailyGoal {
	@Autowired
	private UserDailyGoalAccessor userDailyGoalAccessor;
	@Autowired
	private AccessTokenManager accessTokenManager;

	/**
	 * Endpoint for a user to delete a daily goal.
	 */
	@PostMapping("/daily/deleteDailyGoal")
	public void setDailyGoal(@RequestHeader("Token") String token, HttpServletResponse response) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		userDailyGoalAccessor.deleteEntry(username);
	}
}
