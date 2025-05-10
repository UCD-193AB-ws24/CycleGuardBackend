package cycleguard.rest.achievements;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.HealthInfoAccessor.HealthInfo;
import cycleguard.database.achievements.AchievementInfo;
import cycleguard.database.achievements.AchievementInfoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class GetAchievementInfo {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private AchievementInfoService achievementInfoService;

	/**
	 * Endpoint for a user to retrieve achievement information.
	 * @return {@link AchievementInfo} linked to user
	 */
	@GetMapping("/achievements/getAchievements")
	public AchievementInfo getAchievementInfo(@RequestHeader("Token") String token, HttpServletResponse response) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		return achievementInfoService.getAchievementInfo(username);
	}
}
