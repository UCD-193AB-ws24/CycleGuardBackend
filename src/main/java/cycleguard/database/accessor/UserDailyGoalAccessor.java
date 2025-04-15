package cycleguard.database.accessor;

import cycleguard.database.AbstractDatabaseUserEntry;
import cycleguard.util.StringDoubles;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@Configuration
public class UserDailyGoalAccessor extends AbstractDatabaseAccessor<UserDailyGoalAccessor.UserDailyGoal> {
	private final DynamoDbTable<UserDailyGoal> tableInstance;

	protected UserDailyGoalAccessor() {
		tableInstance = getClient().table("CycleGuard-UserDailyGoal", TableSchema.fromBean(UserDailyGoal.class));
	}

	@Override
	protected DynamoDbTable<UserDailyGoal> getTableInstance() {
		return tableInstance;
	}

	@Override
	protected UserDailyGoal getBlankEntry() {
		return new UserDailyGoal();
	}


	/**
	 * {@link DynamoDbBean} linking a username to that user's basic data.
	 *
	 * <br>
	 * <ul>
	 *     <li>cycleCoins - number of CycleCoins the user has.</li>
	 * </ul>
	 */
	@DynamoDbBean
	public static final class UserDailyGoal extends AbstractDatabaseUserEntry {
		public UserDailyGoal() {
		}

		public UserDailyGoal(String distance, String time, String calories) {
			this.distance = distance;
			this.time = time;
			this.calories = calories;
		}
		public UserDailyGoal(double distance, double time, double calories) {
			this(
					StringDoubles.toString(distance),
					StringDoubles.toString(time),
					StringDoubles.toString(calories)
			);
		}

		private String distance="0", time="0", calories="0";

		public String getDistance() {
			return distance;
		}

		public void setDistance(String distance) {
			this.distance = distance;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getCalories() {
			return calories;
		}

		public void setCalories(String calories) {
			this.calories = calories;
		}
	}
}
