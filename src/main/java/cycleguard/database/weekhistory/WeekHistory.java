package cycleguard.database.weekhistory;

import cycleguard.database.AbstractDatabaseUserEntry;
import cycleguard.database.rides.ProcessRideService;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.Map;
import java.util.TreeMap;

import static cycleguard.util.StringDoubles.fromDouble;
import static cycleguard.util.StringDoubles.toDouble;

/**
 * {@link DynamoDbBean} linking a username to that user's week history.
 *
 * <br>
 * <ul>
 *     <li>cycleCoins - number of CycleCoins the user has.</li>
 * </ul>
 */
@DynamoDbBean
public final class WeekHistory extends AbstractDatabaseUserEntry {
	private Map<Long, SingleRideHistory> dayHistoryMap = new TreeMap<>();

	public Map<Long, SingleRideHistory> getDayHistoryMap() {
		return dayHistoryMap;
	}

	public void setDayHistoryMap(Map<Long, SingleRideHistory> dayHistoryMap) {
		this.dayHistoryMap = dayHistoryMap;
	}


	@DynamoDbBean
	public static final class SingleRideHistory {
		public SingleRideHistory() {}
		public SingleRideHistory(ProcessRideService.RideInfo rideInfo) {
			distance = fromDouble(rideInfo.distance);
			calories = fromDouble(rideInfo.calories);
			time = fromDouble(rideInfo.time);
		}
		private String distance="0", calories="0", time="0";

		public String getDistance() {
			return distance;
		}

		public void setDistance(String distance) {
			this.distance = distance;
		}

		public String getCalories() {
			return calories;
		}

		public void setCalories(String calories) {
			this.calories = calories;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public void addDistance(String distance) {
			this.distance = fromDouble(toDouble(this.distance) + toDouble(distance));
		}

		public void addCalories(String calories) {
			this.calories = fromDouble(toDouble(this.calories) + toDouble(calories));
		}

		public void addTime(String time) {
			this.time = fromDouble(toDouble(this.time) + toDouble(time));
		}
	}
}
