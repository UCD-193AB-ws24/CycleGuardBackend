package cycleguard.database.packs;

import cycleguard.database.AbstractDatabaseEntry;
import cycleguard.database.accessor.AuthTokenAccessor;
import cycleguard.util.StringDoubles;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.Map;
import java.util.TreeMap;

/**
 * {@link DynamoDbBean} linking a pack to its current goal.
 * <br>
 *
 * <ul>
 *     <li>{@link PackGoal#contributionMap} - Maps usernames to contribution, as a String</li>
 *     <li>{@link PackGoal#active} - If a goal is currently active or not</li>
 *     <li>{@link PackGoal#goalField} - Field to track, either <code>distance</code> or <code>time</code></li>
 *     <li>{@link PackGoal#startTime} - Start time of the goal, in seconds after epoch</li>
 *     <li>{@link PackGoal#endTime} - Start time of the goal, in seconds after epoch</li>
 *     <li>{@link PackGoal#goalAmount} - Goal field of the pack</li>
 * </ul>
 */
@DynamoDbBean
public final class PackGoal {
	private Map<String, String> contributionMap = new TreeMap<>();
	private boolean active = false;
	private String goalField="distance";
	private long startTime, endTime, goalAmount;

	public Map<String, String> getContributionMap() {
		return contributionMap;
	}

	public void setContributionMap(Map<String, String> contributionMap) {
		this.contributionMap = contributionMap;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getGoalField() {
		return goalField;
	}

	public void setGoalField(String goalField) {
		this.goalField = goalField;
	}

	public long getGoalAmount() {
		return goalAmount;
	}

	public void setGoalAmount(long goalAmount) {
		this.goalAmount = goalAmount;
	}

	public double getTotalContribution() {
		return contributionMap.values().stream()
				.mapToDouble(StringDoubles::toDouble).sum();
	}
}