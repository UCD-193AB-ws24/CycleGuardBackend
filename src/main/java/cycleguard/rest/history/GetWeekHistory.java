package cycleguard.rest.history;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.HealthInfoAccessor.HealthInfo;
import cycleguard.database.accessor.UserProfileAccessor;
import cycleguard.database.friendsList.FriendsListService;
import cycleguard.database.weekHistory.WeekHistoryService;
import cycleguard.database.weekHistory.WeekHistory;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint for a user to enter in health metrics.
 * Requires {@link HealthInfo} as body.
 */
@RestController
public final class GetWeekHistory {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private WeekHistoryService weekHistoryService;
	@Autowired
	private FriendsListService friendsListService;
	@Autowired
	private UserProfileAccessor userProfileAccessor;

	@GetMapping({"/history/getWeekHistory", "/history/getWeekHistory/{user}"})
	public WeekHistory getWeekHistory(@RequestHeader("Token") String token, HttpServletResponse response,
	                                  @PathVariable(value = "user", required = false) String user) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		if (user==null) return weekHistoryService.getWeekHistory(username);

		boolean isPublic = userProfileAccessor.getEntryOrDefaultBlank(user).getIsPublic();
		if (isPublic) return weekHistoryService.getWeekHistory(user);

		boolean isFriend = friendsListService.getFriendsList(username).getFriends().contains(user);
		if (isFriend) return weekHistoryService.getWeekHistory(user);

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		return null;
	}
}
