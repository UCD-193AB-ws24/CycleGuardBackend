package cycleguard.database.stats;

import cycleguard.database.RideProcessable;
import cycleguard.database.achievements.AchievementInfo;
import cycleguard.database.rides.ProcessRideService;
import cycleguard.util.StringDoubles;
import cycleguard.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static cycleguard.util.StringDoubles.toDouble;

/**
 * Service wrapper for retrieving and modifying {@link UserStats}.
 */
@Service
public class UserStatsService implements RideProcessable {
	@Autowired
	private UserStatsAccessor userStatsAccessor;

	public UserStats getUserStats(String username) {
		return userStatsAccessor.getEntryOrDefaultBlank(username);
	}

	/**
	 * Creates a new user and adds to database.
	 * @param username Username of user
	 * @return New {@link UserStats}
	 */
	public UserStats createUser(String username) {
		UserStats userStats = new UserStats();
		userStats.setUsername(username);
		userStatsAccessor.setEntry(userStats);
		return userStats;
	}

	/**
	 * Sets ride streak and total distance and time of a user's stats.
	 * @param username User who completed ride
	 * @param rideInfo Stats of completed ride
	 * @param now Seconds since epoch when ride was completed
	 */
	@Override
	public void processNewRide(String username, ProcessRideService.RideInfo rideInfo, Instant now) {
		now = TimeUtil.getAdjustedInstant(now);

		UserStats stats = getUserStats(username);

		stats.setTotalDistance(StringDoubles.toString(toDouble(stats.getTotalDistance()) + rideInfo.distance));
		stats.setTotalTime(StringDoubles.toString(toDouble(stats.getTotalTime()) + rideInfo.time));

		long prevDay = stats.getLastRideDay();
		long curDay = TimeUtil.getCurrentDayTime(now);
		long streak = stats.getRideStreak();
		long daysBetween = TimeUtil.getDaysBetweenTimeAndNow(prevDay, curDay);

		// 0 days: same as prev day, don't change streak
		if (daysBetween == 1) streak++;
		else if (daysBetween > 1) streak = 1;

		stats.setRideStreak(streak);
		stats.setLastRideDay(curDay);

		stats.setBestStreak(Math.max(stats.getBestStreak(), streak));

		userStatsAccessor.setEntry(stats);
	}

	/**
	 * Saves a {@link UserStats} to database.
	 * @param userStats Object to save to database
	 */
	public void setEntry(UserStats userStats) {
		userStatsAccessor.setEntry(userStats);
	}
}
