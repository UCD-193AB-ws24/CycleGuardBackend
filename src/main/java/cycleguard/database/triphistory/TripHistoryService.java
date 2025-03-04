package cycleguard.database.triphistory;

import cycleguard.database.SingleRideHistory;
import cycleguard.database.rides.ProcessRideService;
import cycleguard.database.tripcoordinates.TripCoordinates;
import cycleguard.database.tripcoordinates.TripCoordinatesService;
import cycleguard.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Service
public class TripHistoryService {
	public static final int MAX_HISTORY = 50;
	@Autowired
	private TripHistoryAccessor tripHistoryAccessor;

	@Autowired
	private TripCoordinatesService tripCoordinatesService;

	public TripHistory getTripHistory(String username) {
		return tripHistoryAccessor.getEntryOrDefaultBlank(username);
	}

	public void processNewRide(String username, ProcessRideService.RideInfo rideInfo, Instant now) {
		TripHistory stats = tripHistoryAccessor.getEntryOrDefaultBlank(username);

		long timestamp = TimeUtil.getCurrentSecond(now);
		SingleRideHistory rideHistory = new SingleRideHistory(rideInfo);

		Map<Long, SingleRideHistory> tripHistoryMap = stats.getTimestampTripHistoryMap();

		tripHistoryMap.put(timestamp, rideHistory);

		while (tripHistoryMap.size() > MAX_HISTORY) {
			Optional<Long> min = tripHistoryMap.keySet().stream().min(Long::compareTo);
			long key = min.get();
			tripHistoryMap.remove(key);

			tripCoordinatesService.deleteEntry(username, key);
		}

		tripHistoryAccessor.setEntry(stats);
	}
}
