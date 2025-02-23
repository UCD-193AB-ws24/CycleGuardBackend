package cycleguard.database.achievements;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.UserInfoAccessor;
import cycleguard.database.achievements.AchievementInfo.AchievementProgress;
import cycleguard.database.rides.ProcessRideService;
import cycleguard.database.rides.WeekHistory;
import cycleguard.database.rides.WeekHistory.DayHistory;
import cycleguard.database.stats.UserStats;
import cycleguard.database.stats.UserStatsService;
import cycleguard.util.StringDoubles;
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
	@Autowired
	private UserStatsService userStatsService;

	public AchievementInfo getAchievementInfo(String username) {
		AchievementInfo achievementInfo = achievementInfoAccessor.getEntryOrDefaultBlank(username);
		return achievementInfo;
	}

	/*
	Achievement list
	0: First ride
	1: Bike 10 miles
	2: Bike 50 miles
	3: Bike 250 miles
	4: Bike 1 hour
	5: Bike 10 hours
	6: Bike 100 hours
	7: 5 day streak
	8: 20 day streak
	9: 50 day streak
	10: Get all achievements
	 */
	public void processNewRide(String username, ProcessRideService.RideInfo rideInfo) {
		AchievementInfo achievementInfo = achievementInfoAccessor.getEntryOrDefaultBlank(username);
		UserStats userStats = userStatsService.getUserStats(username);

		Map<Integer, AchievementProgress> progressMap = achievementInfo.getAchievementProgressMap();

//		TODO Make this better!!!!!!!!

		{
			AchievementProgress firstRide = progressMap.getOrDefault(0, new AchievementProgress());
			firstRide.setComplete(true);
			firstRide.setCurrentProgress(1);
			progressMap.put(0, firstRide);
		}


		long distance = (long)(StringDoubles.toDouble(userStats.getTotalDistance()));
		{
			AchievementProgress dist1 = progressMap.getOrDefault(1, new AchievementProgress());
			AchievementProgress dist2 = progressMap.getOrDefault(2, new AchievementProgress());
			AchievementProgress dist3 = progressMap.getOrDefault(3, new AchievementProgress());

			dist1.setCurrentProgress(Math.min(distance, 10L));
			dist2.setCurrentProgress(Math.min(distance, 50L));
			dist3.setCurrentProgress(Math.min(distance, 250L));

			dist1.setComplete(dist1.getCurrentProgress() == 10L);
			dist2.setComplete(dist2.getCurrentProgress() == 50L);
			dist3.setComplete(dist3.getCurrentProgress() == 250L);

			progressMap.put(1, dist1);
			progressMap.put(2, dist2);
			progressMap.put(3, dist3);
		}

		long time = (long)StringDoubles.toDouble(userStats.getTotalTime());
		{
			AchievementProgress time4 = progressMap.getOrDefault(4, new AchievementProgress());
			AchievementProgress time5 = progressMap.getOrDefault(5, new AchievementProgress());
			AchievementProgress time6 = progressMap.getOrDefault(6, new AchievementProgress());

			time4.setCurrentProgress(Math.min(time, 1L));
			time5.setCurrentProgress(Math.min(time, 10L));
			time6.setCurrentProgress(Math.min(time, 100L));

			time4.setComplete(time4.getCurrentProgress() == 1L);
			time5.setComplete(time5.getCurrentProgress() == 10L);
			time6.setComplete(time6.getCurrentProgress() == 100L);

			progressMap.put(4, time4);
			progressMap.put(5, time5);
			progressMap.put(6, time6);
		}

		long streak = userStats.getRideStreak();
		{
			AchievementProgress strk7 = progressMap.getOrDefault(7, new AchievementProgress());
			AchievementProgress strk8 = progressMap.getOrDefault(8, new AchievementProgress());
			AchievementProgress strk9 = progressMap.getOrDefault(9, new AchievementProgress());

			strk7.setCurrentProgress(Math.min(streak, 5L));
			strk8.setCurrentProgress(Math.min(streak, 20L));
			strk9.setCurrentProgress(Math.min(streak, 50L));

			strk7.setComplete(strk7.getCurrentProgress() == 5L);
			strk8.setComplete(strk8.getCurrentProgress() == 20L);
			strk9.setComplete(strk9.getCurrentProgress() == 50L);

			progressMap.put(7, strk7);
			progressMap.put(8, strk8);
			progressMap.put(9, strk9);
		}

		{
			AchievementProgress all = progressMap.getOrDefault(10, new AchievementProgress());
			if (distance>=250 && time>=100 && streak>=50) {
				all.setComplete(true);
				all.setCurrentProgress(1);
			}
			progressMap.put(10, all);
		}

		achievementInfoAccessor.setEntry(achievementInfo);
	}
}
