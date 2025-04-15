package cycleguard.database.packs.packGoal;

import cycleguard.database.accessor.UserProfileAccessor;
import cycleguard.database.packs.packData.PackData;
import cycleguard.database.stats.UserStatsService;
import cycleguard.util.StringDoubles;
import cycleguard.util.TimeUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static cycleguard.database.rides.ProcessRideService.*;

@Service
public class PackGoalService {
	private static final String GOAL_DISTANCE = "distance", GOAL_TIME = "time";
	@Autowired
	private PackGoalAccessor packGoalAccessor;
	@Autowired
	private UserStatsService userStatsService;
	@Autowired
	private UserProfileAccessor userProfileAccessor;

	private static final List<String> validFields = List.of(
			"distance",
			"time"
	);

	public PackGoal getEntry(String packName) {
		PackGoal packGoal = packGoalAccessor.getEntry(packName);
		if (packGoal.getEndTime() > TimeUtil.getCurrentSecond() && packGoal.isActive()) {
			packGoal.setActive(false);
			packGoalAccessor.setEntry(packGoal);
		}
		return packGoal;
	}

	public void createEntry(String packName) {
		PackGoal packGoal = new PackGoal();
		packGoal.setName(packName);
		packGoalAccessor.setEntry(packGoal);
	}
	public void deleteEntry(String packName) {
		packGoalAccessor.deleteEntry(packName);
	}
	public void removeUser(String packName, String username) {
		PackGoal packGoal = packGoalAccessor.getEntry(packName);
		if (packGoal == null) return;
		if (!packGoal.getContributionMap().containsKey(username)) return;

		packGoal.getContributionMap().remove(username);
		packGoalAccessor.setEntry(packGoal);
	}

	public void updateContribution(String username, PackData packData, RideInfo rideInfo) {
		String packName = packData.getName();
		PackGoal packGoal = packGoalAccessor.getEntry(packName);
		if (packGoal==null) return;
		if (!packGoal.isActive()) return;

		Map<String, String> contributionMap = packGoal.getContributionMap();

		double curVal = StringDoubles.toDouble(contributionMap.getOrDefault(username, "0"));

		if (packGoal.getGoalField().equals(GOAL_DISTANCE))
			curVal += rideInfo.distance;
		else
			curVal += rideInfo.time;

		contributionMap.put(username, StringDoubles.toString(curVal));

		packGoalAccessor.setEntry(packGoal);
	}

	public int setGoal(String packName, long durationSeconds, String goalField, long goalAmount) {
		PackGoal packGoal = packGoalAccessor.getEntry(packName);
		if (packGoal ==null) return HttpServletResponse.SC_NOT_FOUND;

		if (durationSeconds < 1) return HttpServletResponse.SC_BAD_REQUEST;
		if (!validFields.contains(goalField)) return HttpServletResponse.SC_BAD_REQUEST;
		if (goalAmount<1) return HttpServletResponse.SC_BAD_REQUEST;

		packGoal.getContributionMap().clear();

		packGoalAccessor.setEntry(packGoal);

		return HttpServletResponse.SC_OK;
	}

	public double getProgress(String packName) {
		PackGoal packGoal = packGoalAccessor.getEntry(packName);
		if (packGoal ==null) return 0;

		return packGoal.getContributionMap().values().stream()
				.mapToDouble(StringDoubles::toDouble).sum();
	}

//	Update user’s contribution, set new goal

//	public PackData createPack(String packName, String password) {
//		UserStats userStats = new UserStats();
//		userStats.setUsername(username);
//		userStatsAccessor.setEntry(userStats);
//		return userStats;
//	}
}
