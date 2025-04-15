package cycleguard.database.packs;

import cycleguard.database.accessor.UserProfileAccessor;
import cycleguard.database.stats.UserStatsService;
import cycleguard.util.StringDoubles;
import cycleguard.util.TimeUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static cycleguard.database.rides.ProcessRideService.RideInfo;

@Service
class PackGoalService {
	private static final String GOAL_DISTANCE = "distance", GOAL_TIME = "time";
	@Autowired
	private UserStatsService userStatsService;
	@Autowired
	private UserProfileAccessor userProfileAccessor;

	void updateContribution(String username, PackData packData, RideInfo rideInfo) {
		PackGoal packGoal = packData.getPackGoal();
		if (!packGoal.isActive()) return;

		Map<String, String> contributionMap = packGoal.getContributionMap();

		double curVal = StringDoubles.toDouble(contributionMap.getOrDefault(username, "0"));

		if (packGoal.getGoalField().equals(GOAL_DISTANCE))
			curVal += rideInfo.distance;
		else
			curVal += rideInfo.time;

		contributionMap.put(username, StringDoubles.toString(curVal));
	}

	int setGoal(PackData packData, long durationSeconds, String goalField, long goalAmount) {
		PackGoal packGoal = packData.getPackGoal();

		if (durationSeconds < 1) return HttpServletResponse.SC_BAD_REQUEST;
		if (!(goalField.equals(GOAL_DISTANCE) || goalField.equals(GOAL_TIME)))
			return HttpServletResponse.SC_BAD_REQUEST;
		if (goalAmount<1) return HttpServletResponse.SC_BAD_REQUEST;

		packGoal.getContributionMap().clear();
		packGoal.setActive(true);
		packGoal.setGoalAmount(goalAmount);
		packGoal.setGoalField(goalField);

		long currentSecond = TimeUtil.getCurrentSecond();
		packGoal.setStartTime(currentSecond);
		packGoal.setEndTime(currentSecond + durationSeconds);

		return HttpServletResponse.SC_OK;
	}

	void clearGoal(PackData packData) {
		packData.setPackGoal(new PackGoal());

//		PackGoal packGoal = packData.getPackGoal();
//		packGoal.getContributionMap().clear();
//		packGoal.setActive(false);
//		packGoal.setGoalAmount(0);
//		packGoal.setGoalField("");
//
//		packGoal.setStartTime(0);
//		packGoal.setEndTime(0);
	}
}