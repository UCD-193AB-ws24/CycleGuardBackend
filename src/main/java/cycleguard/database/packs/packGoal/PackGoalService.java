package cycleguard.database.packs.packGoal;

import cycleguard.database.packs.packData.PackData;
import cycleguard.database.stats.UserStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PackGoalService {
	@Autowired
	private PackGoalAccessor packGoalAccessor;

//	public PackData createPack(String packName, String password) {
//		UserStats userStats = new UserStats();
//		userStats.setUsername(username);
//		userStatsAccessor.setEntry(userStats);
//		return userStats;
//	}
}
