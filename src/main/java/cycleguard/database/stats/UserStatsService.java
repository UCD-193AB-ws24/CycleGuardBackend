package cycleguard.database.stats;

import cycleguard.database.rides.ProcessRideService;
import cycleguard.util.StringDoubles;
import cycleguard.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static cycleguard.util.StringDoubles.fromDouble;
import static cycleguard.util.StringDoubles.toDouble;

@Service
public class UserStatsService {
	@Autowired
	private UserStatsAccessor userStatsAccessor;

	public UserStats getUserStats(String username) {
		return userStatsAccessor.getEntryOrDefaultBlank(username);
	}

	public void createUser(String username) {
		UserStats userStats = new UserStats();
		userStats.setUsername(username);
		userStats.setAccountCreationTime(Instant.now().getEpochSecond());
		userStatsAccessor.setEntry(userStats);
	}
	public void processNewRide(String username, ProcessRideService.RideInfo rideInfo) {
		UserStats stats = userStatsAccessor.getEntry(username);

		stats.setTotalDistance(fromDouble(toDouble(stats.getTotalDistance()) + rideInfo.distance));
		stats.setTotalTime(fromDouble(toDouble(stats.getTotalTime()) + rideInfo.time));

		long prevDay = stats.getLastRideDay();
		long curDay = TimeUtil.getCurrentDayTime();
		long streak = stats.getRideStreak();
		long daysBetween = TimeUtil.getDaysBetweenTimeAndNow(prevDay, curDay);

		// 0 days: same as prev day, don't change streak
		if (daysBetween == 1) streak++;
		else if (daysBetween > 1) streak = 1;

		stats.setRideStreak(streak);
		stats.setLastRideDay(curDay);

		userStatsAccessor.setEntry(stats);
	}
}
