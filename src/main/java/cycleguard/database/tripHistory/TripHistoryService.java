package cycleguard.database.tripHistory;

import cycleguard.database.RideProcessable;
import cycleguard.database.SingleRideHistory;
import cycleguard.database.achievements.AchievementInfo;
import cycleguard.database.rides.ProcessRideService;
import cycleguard.database.tripCoordinates.TripCoordinatesService;
import cycleguard.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

/**
 * Service wrapper for retrieving and modifying {@link TripHistory}.
 */
@Service
public class TripHistoryService implements RideProcessable {
	public static final int MAX_HISTORY = 500;
	@Autowired
	private TripHistoryAccessor tripHistoryAccessor;

	@Autowired
	private TripCoordinatesService tripCoordinatesService;

	/**
	 * Returns an entry of {@link TripHistory}, or a blank entry if not existing.
	 * @param username Username to retrieve
	 * @return Non-null {@link TripHistory}
	 */
	public TripHistory getTripHistory(String username) {
		return tripHistoryAccessor.getEntryOrDefaultBlank(username);
	}

	/**
	 * Adds the ride information to {@link TripHistory}.
	 * @param username User who completed ride
	 * @param rideInfo Stats of completed ride
	 * @param now Seconds since epoch when ride was completed
	 */
	@Override
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
