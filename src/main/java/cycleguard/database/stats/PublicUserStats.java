package cycleguard.database.stats;

import cycleguard.database.packs.PackGoal;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

/**
 * {@link DynamoDbBean} linking a username to that user's publicly viewable data fields.
 * <br>
 *
 * <ul>
 *     <li>{@link PublicUserStats#totalDistance} - Total distance traveled by the user</li>
 *     <li>{@link PublicUserStats#totalTime} - Total time spent biking</li>
 * </ul>
 */
public class PublicUserStats {
	private String totalDistance, totalTime;
	public PublicUserStats(){}
	public PublicUserStats(UserStats userStats) {
		this.totalDistance= userStats.getTotalDistance();
		this.totalTime = userStats.getTotalTime();
	}

	public String getTotalDistance() {
		return totalDistance;
	}

	public void setTotalDistance(String totalDistance) {
		this.totalDistance = totalDistance;
	}

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}
}
