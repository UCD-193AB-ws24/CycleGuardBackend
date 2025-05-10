package cycleguard.rest.notifications;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.NotificationsAccessor;
import cycleguard.database.accessor.NotificationsAccessor.Notification;
import cycleguard.database.accessor.NotificationsAccessor.NotificationList;
import cycleguard.database.globalLeaderboards.GlobalLeaderboards;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public final class AddNotification {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private NotificationsAccessor notificationsAccessor;

	/**
	 * Endpoint to add a custom notification. Ignores duplicate notifications.
	 * @param notification Notification to add
	 */
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