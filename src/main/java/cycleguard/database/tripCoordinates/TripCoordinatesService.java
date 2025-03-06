package cycleguard.database.tripCoordinates;

import cycleguard.database.rides.ProcessRideService;
import cycleguard.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TripCoordinatesService {
	@Autowired
	private TripCoordinatesAccessor tripCoordinatesAccessor;

	private String getKey(String username, long timestamp) {
		return String.format("%s_%d", username, timestamp);
	}

	public void deleteEntry(String username, long timestamp) {
		String key = getKey(username, timestamp);
		tripCoordinatesAccessor.deleteEntry(key);
	}

	public void processNewRide(String username, ProcessRideService.RideInfo rideInfo, Instant now) {
		String key = getKey(username, TimeUtil.getCurrentSecond(now));
		TripCoordinates coordinates = new TripCoordinates(key, rideInfo);

		tripCoordinatesAccessor.setEntry(coordinates);
	}

	public TripCoordinates getEntry(String username, long timestamp) {
		String key = getKey(username, timestamp);
		return tripCoordinatesAccessor.getEntry(key);
	}
}
