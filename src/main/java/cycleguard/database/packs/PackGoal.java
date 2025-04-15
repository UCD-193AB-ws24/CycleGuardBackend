package cycleguard.database.packs;

import cycleguard.database.AbstractDatabaseEntry;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.Map;
import java.util.TreeMap;

/**
 * {@link DynamoDbBean} linking a pack to its current goal.
 *
 * <br>
 * <ul>
 *     <li></li>
 * </ul>
 */
@DynamoDbBean
public final class PackGoal {
	private Map<String, String> contributionMap = new TreeMap<>();
	private boolean active = false;
	private String goalField="distance";
	private long endTime, goalAmount;

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
}