package cycleguard.database.tripCoordinates;

import cycleguard.database.AbstractDatabaseEntry;
import cycleguard.database.rides.ProcessRideService;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.List;

/**
 * {@link DynamoDbBean} linking a username to that user's week history.
 *
 * <br>
 * <ul>
 *     <li>cycleCoins - number of CycleCoins the user has.</li>
 * </ul>
 */
@DynamoDbBean
public final class TripCoordinates extends AbstractDatabaseEntry {
	private String usernameTimestamp;
	private List<String> latitudes, longitudes;

	public TripCoordinates() {}
	public TripCoordinates(String key, ProcessRideService.RideInfo rideInfo) {
		this.usernameTimestamp = key;
		this.latitudes = rideInfo.latitudes;
		this.longitudes = rideInfo.longitudes;
	}

	@DynamoDbPartitionKey
	public String getUsernameTimestamp() {
		return usernameTimestamp;
	}

	public void setUsernameTimestamp(String usernameTimestamp) {
		this.usernameTimestamp = usernameTimestamp;
	}

	public List<String> getLatitudes() {
		return latitudes;
	}

	public void setLatitudes(List<String> latitudes) {
		this.latitudes = latitudes;
	}

	public List<String> getLongitudes() {
		return longitudes;
	}

	public void setLongitudes(List<String> longitudes) {
		this.longitudes = longitudes;
	}

	@Override
	public void setPrimaryKey(String key) {
		setUsernameTimestamp(key);
	}
}
