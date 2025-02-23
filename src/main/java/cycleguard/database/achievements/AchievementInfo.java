package cycleguard.database.achievements;

import cycleguard.database.AbstractDatabaseUserEntry;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.Map;
import java.util.TreeMap;

/**
 * {@link DynamoDbBean} linking a username to that user's basic data.
 *
 * <br>
 * <ul>
 *     <li>cycleCoins - number of CycleCoins the user has.</li>
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

	@DynamoDbBean
	public static final class AchievementProgress implements Comparable<AchievementProgress> {
		private long currentProgress, goalProgress;

		public long getCurrentProgress() {
			return currentProgress;
		}

		public void setCurrentProgress(long currentProgress) {
			this.currentProgress = currentProgress;
		}

		public long getGoalProgress() {
			return goalProgress;
		}

		public void setGoalProgress(long goalProgress) {
			this.goalProgress = goalProgress;
		}

		@Override
		public int compareTo(AchievementProgress o) {
			long progress = currentProgress * o.goalProgress;
			long oProgress = o.currentProgress * goalProgress;
			return (int)(progress - oProgress);
		}
	}
}

