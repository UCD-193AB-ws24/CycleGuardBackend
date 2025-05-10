package cycleguard.rest.packs;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.HealthInfoAccessor.HealthInfo;
import cycleguard.database.packs.PackDataService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class SetPackGoal {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private PackDataService packDataService;

	/**
	 * Set the current pack goal.
	 * @param newPackGoal Data of the pack's new goal
	 * @return 200 on success<br>
	 * 400 on malformed request<br>
	 * 401 if user is not pack owner<br>
	 * 404 if pack not existent
	 */
	@PostMapping("/packs/setPackGoal")
	public void setPackGoal(@RequestHeader("Token") String token, HttpServletResponse response,
	                                   @RequestBody @NonNull NewPackGoal newPackGoal) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}


		int status = packDataService.setGoal(
				username, newPackGoal.durationSeconds,
				newPackGoal.goalField, newPackGoal.goalAmount);

		response.setStatus(status);
	}

	public static class NewPackGoal {
		public long durationSeconds;
		public String goalField;
		public long goalAmount;
	}
}
