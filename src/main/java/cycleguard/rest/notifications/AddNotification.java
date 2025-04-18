package cycleguard.rest.notifications;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.HealthInfoAccessor.HealthInfo;
import cycleguard.database.accessor.NotificationsAccessor;
import cycleguard.database.accessor.NotificationsAccessor.Notification;
import cycleguard.database.accessor.NotificationsAccessor.NotificationList;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoint for a user to enter in health metrics.
 * Requires {@link HealthInfo} as body.
 */
@RestController
public final class AddNotification {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private NotificationsAccessor notificationsAccessor;

	@PostMapping("/notifications/addNotification")
	public NotificationList addNotification(@RequestHeader("Token") String token, HttpServletResponse response,
	                                         @RequestBody @NonNull Notification notification) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		NotificationList notificationsListObject = notificationsAccessor.getEntryOrDefaultBlank(username);
		List<Notification> list = notificationsListObject.getNotifications();

		if (!list.contains(notification)) {
			list.add(notification);
			notificationsAccessor.setEntry(notificationsListObject);
		}

		return notificationsListObject;
	}
}