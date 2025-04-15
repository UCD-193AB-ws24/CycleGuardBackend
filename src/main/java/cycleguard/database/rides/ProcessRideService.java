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

	public static final class RideInfo {
		public double distance, calories, time;
		public List<String> latitudes=new ArrayList<>(), longitudes=new ArrayList<>();

		@Override
		public String toString() {
			return "RideInfo{" +
					"distance=" + distance +
					", calories=" + calories +
					", time=" + time +
					", latitudes=" + latitudes +
					", longitudes=" + longitudes +
					'}';
		}
	}
}
