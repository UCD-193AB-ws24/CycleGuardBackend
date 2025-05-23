package cycleguard.database.stats;

import cycleguard.database.AbstractDatabaseUserEntry;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.time.Instant;

/**
 * {@link DynamoDbBean} linking a username to that user's full statistics.
 * <br>
 *
 * <ul>
 *     <li>{@link UserStats#accountCreationTime} - Seconds after epoch when account was created</li>
 *     <li>{@link UserStats#totalDistance} - Total distance traveled by the user</li>
 *     <li>{@link UserStats#totalTime} - Total time user spent biking</li>
 *     <li>{@link UserStats#bestPackGoalProgress} - Best pack goal progress achieved by any of user's previous or current packs</li>
 *     <li>{@link UserStats#lastRideDay} - Highest seconds after epoch when user took a ride</li>
 *     <li>{@link UserStats#rideStreak} - Current daily ride streak of user</li>
 *     <li>{@link UserStats#bestStreak} - Best daily ride streak of user</li>
 * </ul>
 */
@DynamoDbBean
public final class UserStats extends AbstractDatabaseUserEntry {
	private long accountCreationTime;
	private String totalDistance, totalTime;
	private long bestPackGoalProgress;
	private long lastRideDay;
	private long rideStreak, bestStreak;

	public UserStats() {
		accountCreationTime = Instant.now().getEpochSecond();
		totalDistance="0";
		totalTime="0";
	}

	public long getAccountCreationTime() {
		return accountCreationTime;
	}

	public void setAccountCreationTime(long accountCreationTime) {
		this.accountCreationTime = accountCreationTime;
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

	public long getBestPackGoalProgress() {
		return bestPackGoalProgress;
	}

	public void setBestPackGoalProgress(long bestPackGoalProgress) {
		this.bestPackGoalProgress = bestPackGoalProgress;
	}

	public long getLastRideDay() {
		return lastRideDay;
	}

	public void setLastRideDay(long lastRideDay) {
		this.lastRideDay = lastRideDay;
	}

	public long getRideStreak() {
		return rideStreak;
	}

	public void setRideStreak(long rideStreak) {
		this.rideStreak = rideStreak;
	}

	public long getBestStreak() {
		return bestStreak;
	}

	public void setBestStreak(long bestStreak) {
		this.bestStreak = bestStreak;
	}
}