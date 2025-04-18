package cycleguard.rest.notifications;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.HealthInfoAccessor.HealthInfo;
import cycleguard.database.accessor.NotificationsAccessor;
import cycleguard.database.accessor.NotificationsAccessor.NotificationList;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint for a user to enter in health metrics.
 * Requires {@link HealthInfo} as body.
 */
@RestController
public final class GetNotifications {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private NotificationsAccessor notificationsAccessor;

	@GetMapping("/notifications/getNotifications")
	public NotificationList getNotifications(@RequestHeader("Token") String token, HttpServletResponse response) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		return notificationsAccessor.getEntryOrDefaultBlank(username);
	}
}