package cycleguard.database.globalLeaderboards;

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
	private void updateEntry(GlobalLeaderboards leaderboard, LeaderboardEntry entry) {
		List<LeaderboardEntry> entries = leaderboard.getEntries();
		boolean found = false;
		for (LeaderboardEntry curEntry : entries) {
			if (curEntry.username.equals(entry.getUsername())) {
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

		updateEntry(distanceLeaderboards, distanceEntry);
		updateEntry(timeLeaderboards, timeEntry);
	}
}
