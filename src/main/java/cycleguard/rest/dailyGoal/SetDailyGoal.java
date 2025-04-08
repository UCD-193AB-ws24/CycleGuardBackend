package cycleguard.rest.dailyGoal;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.HealthInfoAccessor.HealthInfo;
import cycleguard.database.accessor.UserDailyGoalAccessor;
import cycleguard.database.accessor.UserDailyGoalAccessor.UserDailyGoal;
import cycleguard.util.StringDoubles;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * Endpoint for a user to enter in health metrics.
 * Requires {@link HealthInfo} as body.
 */
@RestController
public final class SetDailyGoal {
	@Autowired
	private UserDailyGoalAccessor userDailyGoalAccessor;
	@Autowired
	private AccessTokenManager accessTokenManager;

	@PostMapping("/daily/setDailyGoal")
	public void setDailyGoal(@RequestHeader("Token") String token, HttpServletResponse response,
	                                  @RequestBody @NonNull UserDailyGoalInput goal) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		double[] vals = {goal.distance, goal.time, goal.calories};
		double sum=0;
		for (var v : vals) {
			if (v < 0) return;
			sum += v;
		}
		if (sum==0) {
			userDailyGoalAccessor.deleteEntry(username);
			return;
		}

		UserDailyGoal userDailyGoal = new UserDailyGoal(goal.distance, goal.time, goal.calories);
		userDailyGoal.setUsername(username);

		userDailyGoalAccessor.setEntry(userDailyGoal);
	}

	static class UserDailyGoalInput {
		public double distance, time, calories;
	}
}
