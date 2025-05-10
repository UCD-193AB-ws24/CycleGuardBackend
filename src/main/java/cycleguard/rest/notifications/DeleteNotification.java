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

@RestController
public final class DeleteNotification {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private NotificationsAccessor notificationsAccessor;

	/**
	 * Endpoint to delete a custom notification.
	 * @param notification Notification to delete
	 */
	@PostMapping("/notifications/deleteNotification")
	public NotificationList deleteNotification(@RequestHeader("Token") String token, HttpServletResponse response,
	                                           @RequestBody @NonNull Notification notification) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		NotificationList notificationsListObject = notificationsAccessor.getEntryOrDefaultBlank(username);
		List<Notification> list = notificationsListObject.getNotifications();

		if (list.contains(notification)) {
			list.remove(notification);
			notificationsAccessor.setEntry(notificationsListObject);
		}

		return notificationsListObject;
	}
}