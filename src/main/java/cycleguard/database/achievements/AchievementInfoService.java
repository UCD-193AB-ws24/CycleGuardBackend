package cycleguard.database.achievements;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.rides.ProcessRideService;
import cycleguard.database.rides.WeekHistory;
import cycleguard.database.rides.WeekHistory.DayHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Service
public class AchievementInfoService {
	@Autowired
	private AchievementInfoAccessor achievementInfoAccessor;

	@Autowired
	private AccessTokenManager accessTokenManager;

	public AchievementInfo getAchievementInfo(String username) {
		AchievementInfo achievementInfo = achievementInfoAccessor.getEntryOrDefaultBlank(username);
		return achievementInfo;
	}
	public void processNewRide(String username, ProcessRideService.RideInfo rideInfo) {

	}
}
