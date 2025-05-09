package cycleguard.database.achievements;

import cycleguard.database.AbstractDatabaseUserEntry;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.Map;
import java.util.TreeMap;

/**
 * {@link DynamoDbBean} linking a username to that user's achievement progress.
 * <br>
 *
 * <ul>
 *     <li>{@link AchievementInfo#achievementProgressMap} - Maps achievement ID to {@link AchievementProgress}</li>
 * </ul>
 */
@DynamoDbBean
public final class AchievementInfo extends AbstractDatabaseUserEntry {
	private Map<Integer, AchievementProgress> achievementProgressMap = new TreeMap<>();

	public Map<Integer, AchievementProgress> getAchievementProgressMap() {
		return achievementProgressMap;
	}

	public void setAchievementProgressMap(Map<Integer, AchievementProgress> achievementProgressMap) {
		this.achievementProgressMap = achievementProgressMap;
	}

	/**
	 * {@link DynamoDbBean} of a user's progress on one specific achievement.
	 * <br>
	 *
	 * <ul>
	 *     <li>{@link AchievementProgress#currentProgress} - Integer value of user's current progress towards a goal</li>
	 *     <li>{@link AchievementProgress#complete} - If the achievement is complete or not</li>
	 * </ul>
	 */
	@DynamoDbBean
	public static final class AchievementProgress {
		private long currentProgress;
		private boolean complete;

		public boolean isComplete() {
			return complete;
		}

		public void setComplete(boolean complete) {
			this.complete = complete;
		}

		public long getCurrentProgress() {
			return currentProgress;
		}

		public void setCurrentProgress(long currentProgress) {
			this.currentProgress = currentProgress;
		}
	}
}

