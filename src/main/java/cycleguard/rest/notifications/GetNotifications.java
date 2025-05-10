package cycleguard.rest.notifications;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.NotificationsAccessor;
import cycleguard.database.accessor.NotificationsAccessor.NotificationList;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class GetNotifications {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private NotificationsAccessor notificationsAccessor;

	/**
	 * Endpoint to get all custom notifications.
	 * @return Non-null {@link NotificationList}
	 */
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