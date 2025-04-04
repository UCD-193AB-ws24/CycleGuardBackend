package cycleguard.database.achievements;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.PurchaseInfoAccessor;
import cycleguard.database.achievements.AchievementInfo.AchievementProgress;
import cycleguard.database.rides.ProcessRideService;
import cycleguard.database.stats.UserStats;
import cycleguard.database.stats.UserStatsService;
import cycleguard.util.StringDoubles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.function.BooleanSupplier;

@Service
public class AchievementInfoService {
	@Autowired
	private AchievementInfoAccessor achievementInfoAccessor;
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private UserStatsService userStatsService;
	@Autowired
	private PurchaseInfoAccessor purchaseInfoAccessor;
	private static final int NUM_ACHIEVEMENTS = 12;

	public AchievementInfo getAchievementInfo(String username) {
		AchievementInfo achievementInfo = achievementInfoAccessor.getEntryOrDefaultBlank(username);
		return achievementInfo;
	}


	private void setAchievement(Map<Integer, AchievementProgress> progressMap,
	                            int idx, long progress, long goal) {
		AchievementProgress achievementProgress = progressMap.getOrDefault(idx, new AchievementProgress());
		boolean complete = progress >= goal;

		achievementProgress.setComplete(complete);
		achievementProgress.setCurrentProgress(Math.min(progress, goal));
		
		progressMap.put(idx, achievementProgress);
	}

	public int completedAchievements(Map<Integer, AchievementProgress> progressMap) {
		return (int)progressMap.values().stream()
				.filter(AchievementProgress::isComplete)
				.count();
	}

	public void processAchievements(String username) {
		AchievementInfo achievementInfo = achievementInfoAccessor.getEntryOrDefaultBlank(username);
		UserStats userStats = userStatsService.getUserStats(username);


		Map<Integer, AchievementProgress> progressMap = achievementInfo.getAchievementProgressMap();

//		TODO Make this better!!!!!!!!

//		0: First ride
		setAchievement(progressMap, 0, 1, 1);
//		1: Rocket boost
		{
			var purchases = purchaseInfoAccessor.getEntryOrDefaultBlank(username);
			int progress = purchases.getThemesOwned().contains("Rocket Boost")?1:0;
			setAchievement(progressMap, 1, progress, 1);
		}


//		3-5: Distance
		{
			long distance = (long)(StringDoubles.toDouble(userStats.getTotalDistance()));
			setAchievement(progressMap, 3, distance, 100);
			setAchievement(progressMap, 4, distance, 1000);
			setAchievement(progressMap, 5, distance, 10000);
		}
//		6-8: Time
		{
			long time = (long)StringDoubles.toDouble(userStats.getTotalTime());
			setAchievement(progressMap, 6, time, 600);
			setAchievement(progressMap, 7, time, 6000);
			setAchievement(progressMap, 8, time, 60000);
		}
//		9-11: Streak
		{
			long streak = userStats.getBestStreak();
			setAchievement(progressMap, 9, streak, 7);
			setAchievement(progressMap, 10, streak, 30);
			setAchievement(progressMap, 11, streak, 365);
		}

//		2: Get all achievements
		setAchievement(progressMap, 2, completedAchievements(progressMap), NUM_ACHIEVEMENTS-1);

		achievementInfoAccessor.setEntry(achievementInfo);
	}

	public void processNewRide(String username, ProcessRideService.RideInfo rideInfo, Instant now) {
		processAchievements(username);
	}
}
