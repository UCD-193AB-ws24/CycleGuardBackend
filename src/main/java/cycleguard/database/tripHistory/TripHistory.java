package cycleguard.database.tripHistory;

import cycleguard.database.AbstractDatabaseUserEntry;
import cycleguard.database.SingleRideHistory;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.Map;
import java.util.TreeMap;

/**
 * {@link DynamoDbBean} linking a username to that user's trip history.
 * <br>
 *
 * <ul>
 *     <li>{@link TripHistory#timestampTripHistoryMap} - Maps ride timestamps to {@link SingleRideHistory} entries</li>
 * </ul>
 */
@DynamoDbBean
public final class TripHistory extends AbstractDatabaseUserEntry {
	private Map<Long, SingleRideHistory> timestampTripHistoryMap = new TreeMap<>();
	public Map<Long, SingleRideHistory> getTimestampTripHistoryMap() {
		return timestampTripHistoryMap;
	}

	public void setTimestampTripHistoryMap(Map<Long, SingleRideHistory> timestampTripHistoryMap) {
		this.timestampTripHistoryMap = timestampTripHistoryMap;
	}
}
