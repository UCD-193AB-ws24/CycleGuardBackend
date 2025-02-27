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
			AchievementProgress dist1 = progressMap.getOrDefault(2, new AchievementProgress());
			AchievementProgress dist2 = progressMap.getOrDefault(3, new AchievementProgress());
			AchievementProgress dist3 = progressMap.getOrDefault(4, new AchievementProgress());

			dist1.setCurrentProgress(Math.min(distance, 100));
			dist2.setCurrentProgress(Math.min(distance, 1000));
			dist3.setCurrentProgress(Math.min(distance, 10000));

			dist1.setComplete(dist1.getCurrentProgress() == 100);
			dist2.setComplete(dist2.getCurrentProgress() == 1000);
			dist3.setComplete(dist3.getCurrentProgress() == 10000);

			progressMap.put(2, dist1);
			progressMap.put(3, dist2);
			progressMap.put(4, dist3);
		}

		long time = (long)StringDoubles.toDouble(userStats.getTotalTime());
		{
			AchievementProgress time4 = progressMap.getOrDefault(5, new AchievementProgress());
			AchievementProgress time5 = progressMap.getOrDefault(6, new AchievementProgress());
			AchievementProgress time6 = progressMap.getOrDefault(7, new AchievementProgress());

			time4.setCurrentProgress(Math.min(time, 600));
			time5.setCurrentProgress(Math.min(time, 6000));
			time6.setCurrentProgress(Math.min(time, 60000));

			time4.setComplete(time4.getCurrentProgress() == 600);
			time5.setComplete(time5.getCurrentProgress() == 6000);
			time6.setComplete(time6.getCurrentProgress() == 60000);

			progressMap.put(5, time4);
			progressMap.put(6, time5);
			progressMap.put(7, time6);
		}

		long streak = userStats.getRideStreak();
		{
			AchievementProgress strk7 = progressMap.getOrDefault(8, new AchievementProgress());
			AchievementProgress strk8 = progressMap.getOrDefault(9, new AchievementProgress());
			AchievementProgress strk9 = progressMap.getOrDefault(10, new AchievementProgress());

			strk7.setCurrentProgress(Math.min(streak, 7));
			strk8.setCurrentProgress(Math.min(streak, 30));
			strk9.setCurrentProgress(Math.min(streak, 365));

			strk7.setComplete(strk7.getCurrentProgress() == 7);
			strk8.setComplete(strk8.getCurrentProgress() == 30);
			strk9.setComplete(strk9.getCurrentProgress() == 365);

			progressMap.put(8, strk7);
			progressMap.put(9, strk8);
			progressMap.put(10, strk9);
		}

		{
			AchievementProgress all = progressMap.getOrDefault(1, new AchievementProgress());
			if (distance>=10000 && time>=60000 && streak>=365) {
				all.setComplete(true);
				all.setCurrentProgress(1);
			}
			progressMap.put(1, all);
		}

		achievementInfoAccessor.setEntry(achievementInfo);
	}
}
