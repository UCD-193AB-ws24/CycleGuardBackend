package cycleguard.database.triphistory;

import cycleguard.database.AbstractDatabaseUserEntry;
import cycleguard.database.SingleRideHistory;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.Map;
import java.util.TreeMap;

/**
 * {@link DynamoDbBean} linking a username to that user's week history.
 *
 * <br>
 * <ul>
 *     <li>cycleCoins - number of CycleCoins the user has.</li>
 * </ul>
 */
@DynamoDbBean
public final class TripHistory extends AbstractDatabaseUserEntry {
	private Map<Long, SingleRideHistory> tripHistoryMap = new TreeMap<>();

	public Map<Long, SingleRideHistory> getTripHistoryMap() {
		return tripHistoryMap;
	}

	public void setTripHistoryMap(Map<Long, SingleRideHistory> tripHistoryMap) {
		this.tripHistoryMap = tripHistoryMap;
	}
}
