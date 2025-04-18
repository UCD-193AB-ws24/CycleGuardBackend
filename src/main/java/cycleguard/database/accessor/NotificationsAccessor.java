package cycleguard.database.accessor;

import cycleguard.database.AbstractDatabaseUserEntry;
import cycleguard.database.accessor.NotificationsAccessor.NotificationList;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Configuration
public class NotificationsAccessor extends AbstractDatabaseAccessor<NotificationList> {
	private final DynamoDbTable<NotificationList> tableInstance;

	protected NotificationsAccessor() {
		tableInstance = getClient().table("CycleGuard-NotificationList", TableSchema.fromBean(NotificationList.class));
	}

	@Override
	protected DynamoDbTable<NotificationList> getTableInstance() {
		return tableInstance;
	}

	@Override
	protected NotificationList getBlankEntry() {
		return new NotificationList();
	}

	/**
	 * {@link DynamoDbBean} linking a username to that user's profile.
	 *
	 * <br>
	 * <ul>
	 *     <li>{@link NotificationList
	 *    #displayName} - Height of user, in inches.</li>
	 *     <li>{@link NotificationList
	 *    #bio} - Height of user, in inches.</li>
	 * </ul>
	 */
	@DynamoDbBean
	public static final class NotificationList extends AbstractDatabaseUserEntry {
		private List<Notification> notifications = new ArrayList<>();

		public List<Notification> getNotifications() {
			return notifications;
		}

		public void setNotifications(List<Notification> notifications) {
			this.notifications = notifications;
		}

		@Override
		public String toString() {
			return "NotificationList{" +
					"notifications=" + notifications +
					'}';
		}
	}

	@DynamoDbBean
	public static final class Notification {
		private String title="", body="";
		private int hour, minute;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getBody() {
			return body;
		}

		public void setBody(String body) {
			this.body = body;
		}

		public int getHour() {
			return hour;
		}

		public void setHour(int hour) {
			this.hour = hour;
		}

		public int getMinute() {
			return minute;
		}

		public void setMinute(int minute) {
			this.minute = minute;
		}

		@Override
		public boolean equals(Object object) {
			if (this == object) return true;
			if (object == null || getClass() != object.getClass()) return false;
			Notification that = (Notification) object;
			return hour == that.hour && minute == that.minute &&
					Objects.equals(title, that.title) && Objects.equals(body, that.body);
		}

		@Override
		public int hashCode() {
			return Objects.hash(title, body, hour, minute);
		}

		@Override
		public String toString() {
			return "Notification{" +
					"title='" + title + '\'' +
					", body='" + body + '\'' +
					", hour=" + hour +
					", minute=" + minute +
					'}';
		}
	}
}
