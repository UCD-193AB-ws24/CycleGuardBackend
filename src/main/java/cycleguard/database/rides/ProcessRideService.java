package cycleguard.database.rides;

import cycleguard.database.achievements.AchievementInfoService;
import cycleguard.database.globalLeaderboards.GlobalLeaderboardsService;
import cycleguard.database.packs.PackDataService;
import cycleguard.database.stats.UserStatsService;
import cycleguard.database.tripCoordinates.TripCoordinatesService;
import cycleguard.database.tripHistory.TripHistoryService;
import cycleguard.database.weekHistory.WeekHistoryService;
import cycleguard.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Service wrapper for processing a new submitted ride.
 * Call {@link ProcessRideService#processNewRide} to fully process a new ride,
 * updating every related database entry.
 */
@Service
public class ProcessRideService {
	@Autowired
	private WeekHistoryService weekHistoryService;
	@Autowired
	private UserStatsService userStatsService;
	@Autowired
	private AchievementInfoService achievementInfoService;
	@Autowired
	private TripHistoryService tripHistoryService;
	@Autowired
	private TripCoordinatesService tripCoordinatesService;
	@Autowired
	private GlobalLeaderboardsService globalLeaderboardsService;
	@Autowired
	private PackDataService packDataService;

	/**
	 * Fully processes a single ride, updating all related database entries.
	 * @param username User who took the ride
	 * @param rideInfo Information of the ride
	 * @return Seconds since epoch that ride was taken - used for {@link TripCoordinatesService}
	 */
	public long processNewRide(String username, RideInfo rideInfo) {
		Instant now = Instant.now();
		weekHistoryService.processNewRide(username, rideInfo, now);
		userStatsService.processNewRide(username, rideInfo, now);
		achievementInfoService.processNewRide(username, rideInfo, now);
		tripHistoryService.processNewRide(username, rideInfo, now);
		tripCoordinatesService.processNewRide(username, rideInfo, now);
		globalLeaderboardsService.processNewRide(username, rideInfo, now);
		packDataService.processNewRide(username, rideInfo, now);

		return TimeUtil.getCurrentSecond(now);
	}

	/**
	 * Information for a single ride.
	 * <ul>
	 *     <li>{@link RideInfo#distance} - Distance traveled during ride</li>
	 *     <li>{@link RideInfo#calories} - Calories burned during ride</li>
	 *     <li>{@link RideInfo#time} - Time spent riding</li>
	 *     <li>{@link RideInfo#climb} - Accumulation of positive altitude changes during ride</li>
	 *     <li>{@link RideInfo#averageAltitude} - Average elevation during ride</li>
	 * </ul>
	 */
	public static final class RideInfo {
		public double distance, calories, time, climb, averageAltitude;
		public List<String> latitudes=new ArrayList<>(), longitudes=new ArrayList<>();

		@Override
		public String toString() {
			return "RideInfo{" +
					"distance=" + distance +
					", calories=" + calories +
					", time=" + time +
					", climb=" + climb +
					", averageAltitude=" + averageAltitude +
					", latitudes=" + latitudes +
					", longitudes=" + longitudes +
					'}';
		}
	}
}
