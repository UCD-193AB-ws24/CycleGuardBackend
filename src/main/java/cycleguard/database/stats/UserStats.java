package cycleguard.database.stats;

import cycleguard.database.AbstractDatabaseUserEntry;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

/**
 * {@link DynamoDbBean} linking a username to that user's stats.
 *
 * <br>
 * <ul>
 *     <li>cycleCoins - number of CycleCoins the user has.</li>
 * </ul>
 */
@DynamoDbBean
public final class UserStats extends AbstractDatabaseUserEntry {
	private long accountCreationTime;
	private String totalDistance, totalTime;
	private long lastRideDay;
	private long rideStreak;

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
}