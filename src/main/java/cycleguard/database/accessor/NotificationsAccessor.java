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
	 * {@link DynamoDbBean} linking a username to that user's configured notifications.<br>
	 * Stores only unique notifications - duplicates are ignored.
	 * <br>
	 *
	 * <ul>
	 *     <li>{@link NotificationList#notifications} - List of set up {@link Notification}s</li>
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

	/**
	 * {@link DynamoDbBean} of one single user-configured notification, stored within {@link NotificationList}.<br>
	 * Two {@link Notification}s are equal if they are equivalent in all fields.
	 * <br>
	 *
	 *
	 * <ul>
	 *     <li>{@link Notification#title} - Name of notification</li>
	 *     <li>{@link Notification#body} - Description of notification</li>
	 *     <li>{@link Notification#hour} - Hour of day to show notification</li>
	 *     <li>{@link Notification#minute} - Minute of hour to show notification</li>
	 *     <li>{@link Notification#frequency} - Frequency of notification.
	 *          Values are: 0-2 --> daily/weekly/one time</li>
	 *     <li>{@link Notification#dayOfWeek} - Day of week to show notification. Values are: 0-6</li>
	 *     <li>{@link Notification#month} - Month to show notifications, for one time notifications</li>
	 * </ul>
	 */
	@DynamoDbBean
	public static final class Notification {
		private String title="", body="";
		private int hour, minute, frequency, dayOfWeek, month;

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

		public int getFrequency() {
			return frequency;
		}

		public void setFrequency(int frequency) {
			this.frequency = frequency;
		}

		public int getDayOfWeek() {
			return dayOfWeek;
		}

		public void setDayOfWeek(int dayOfWeek) {
			this.dayOfWeek = dayOfWeek;
		}

		public int getMonth() {
			return month;
		}

		public void setMonth(int month) {
			this.month = month;
		}

		@Override
		public boolean equals(Object object) {
			if (this == object) return true;
			if (object == null || getClass() != object.getClass()) return false;
			Notification that = (Notification) object;
			return hour == that.hour && minute == that.minute && frequency == that.frequency && dayOfWeek == that.dayOfWeek && month == that.month && Objects.equals(title, that.title) && Objects.equals(body, that.body);
		}

		@Override
		public int hashCode() {
			return Objects.hash(title, body, hour, minute, frequency, dayOfWeek, month);
		}

		@Override
		public String toString() {
			return "Notification{" +
					"title='" + title + '\'' +
					", body='" + body + '\'' +
					", hour=" + hour +
					", minute=" + minute +
					", frequency=" + frequency +
					", dayOfWeek=" + dayOfWeek +
					", month=" + month +
					'}';
		}
	}
}
