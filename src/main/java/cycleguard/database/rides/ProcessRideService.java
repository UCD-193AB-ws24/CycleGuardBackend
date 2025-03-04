package cycleguard.database.rides;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import cycleguard.database.achievements.AchievementInfoService;
import cycleguard.database.stats.UserStatsService;
import cycleguard.database.triphistory.TripHistory;
import cycleguard.database.triphistory.TripHistoryService;
import cycleguard.database.weekhistory.WeekHistoryService;
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

	public void processNewRide(String username, RideInfo rideInfo) {
		Instant now = Instant.now();
		weekHistoryService.addDayHistory(username, rideInfo, now);
		userStatsService.processNewRide(username, rideInfo, now);
		achievementInfoService.processNewRide(username, rideInfo, now);
		tripHistoryService.processNewRide(username, rideInfo, now);
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
