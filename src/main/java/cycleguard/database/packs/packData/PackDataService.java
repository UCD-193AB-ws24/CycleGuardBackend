package cycleguard.database.packs.packData;

import cycleguard.database.stats.UserStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PackDataService {
	@Autowired
	private PackDataAccessor packDataAccessor;

//	public PackData createPack(String packName, String password) {
//		UserStats userStats = new UserStats();
//		userStats.setUsername(username);
//		userStatsAccessor.setEntry(userStats);
//		return userStats;
//	}
}
