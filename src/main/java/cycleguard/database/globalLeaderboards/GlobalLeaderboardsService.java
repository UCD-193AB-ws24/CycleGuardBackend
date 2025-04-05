package cycleguard.database.globalLeaderboards;

import cycleguard.database.accessor.UserInfoAccessor;
import cycleguard.database.accessor.UserProfileAccessor;
import cycleguard.database.rides.ProcessRideService;
import cycleguard.database.stats.UserStats;
import cycleguard.database.stats.UserStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Service
public class GlobalLeaderboardsService {
	@Autowired
	private GlobalLeaderboardsAccessor globalLeaderboardsAccessor;
	@Autowired
	private UserStatsService userStatsService;
	@Autowired
	private UserProfileAccessor userProfileAccessor;

	private GlobalLeaderboards distanceLeaderboards=null;
	private GlobalLeaderboards timeLeaderboards=null;
	public GlobalLeaderboards getDistanceLeaderboards() {
		if (distanceLeaderboards==null) distanceLeaderboards = globalLeaderboardsAccessor.getEntryOrDefaultBlank("distance");
		return distanceLeaderboards;
	}
	public GlobalLeaderboards getTimeLeaderboards() {
		if (timeLeaderboards==null) timeLeaderboards = globalLeaderboardsAccessor.getEntryOrDefaultBlank("time");
		return timeLeaderboards;
	}
	private void updateEntry(GlobalLeaderboards leaderboard, LeaderboardEntry entry, boolean isPublic) {
		List<LeaderboardEntry> entries = leaderboard.getEntries();
		boolean found = false;
		for (LeaderboardEntry curEntry : entries) {
			if (curEntry.username.equals(entry.getUsername())) {
				if (!isPublic) {
					entries.remove(curEntry);
					globalLeaderboardsAccessor.setEntry(leaderboard);
					return;
				}
				curEntry.value = entry.value;
				found = true;
				break;
			}
		}

		if (!found) entries.add(entry);

		Collections.sort(entries);

		globalLeaderboardsAccessor.setEntry(leaderboard);
	}


	public void processNewRide(String username, ProcessRideService.RideInfo rideInfo, Instant now) {
		UserStats stats = userStatsService.getUserStats(username);

		GlobalLeaderboards distanceLeaderboards = getDistanceLeaderboards();
		GlobalLeaderboards timeLeaderboards = getTimeLeaderboards();

		LeaderboardEntry distanceEntry = new LeaderboardEntry(username, stats.getTotalDistance());
		LeaderboardEntry timeEntry = new LeaderboardEntry(username, stats.getTotalTime());

		boolean isPublic = userProfileAccessor.getEntry(username).getIsPublic();

		updateEntry(distanceLeaderboards, distanceEntry, isPublic);
		updateEntry(timeLeaderboards, timeEntry, isPublic);
	}
}
