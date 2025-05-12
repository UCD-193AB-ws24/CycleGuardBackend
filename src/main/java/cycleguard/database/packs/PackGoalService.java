package cycleguard.database.packs;

import cycleguard.database.accessor.UserProfileAccessor;
import cycleguard.database.achievements.AchievementInfoService;
import cycleguard.database.stats.UserStatsService;
import cycleguard.util.StringDoubles;
import cycleguard.util.TimeUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static cycleguard.database.rides.ProcessRideService.RideInfo;

/**
 * Service wrapper for retrieving and modifying {@link PackGoal}.
 */
@Service
class PackGoalService {
	private static final String GOAL_DISTANCE = "distance", GOAL_TIME = "time";
	@Autowired
	private UserStatsService userStatsService;
	@Autowired
	private UserProfileAccessor userProfileAccessor;
	@Autowired
	private AchievementInfoService achievementInfoService;

	/**
	 * Updates contributions from a user towards the pack's current goal.
	 * @param username Username to update
	 * @param packData Data of current pack
	 * @param rideInfo Current ride information
	 */
	void updateContribution(String username, PackData packData, RideInfo rideInfo) {
		PackGoal packGoal = packData.getPackGoal();
		if (!packGoal.getActive()) return;

		Map<String, String> contributionMap = packGoal.getContributionMap();

		double prevContribution = packGoal.getTotalContribution();
		if (prevContribution>=packGoal.getGoalAmount()) return;

		double curVal = StringDoubles.toDouble(contributionMap.getOrDefault(username, "0"));
		double toAdd;
		if (packGoal.getGoalField().equals(GOAL_DISTANCE))
			toAdd = rideInfo.distance;
		else
			toAdd = rideInfo.time;
		toAdd = Math.min(packGoal.getGoalAmount()-prevContribution, toAdd);

		curVal += toAdd;
		contributionMap.put(username, StringDoubles.toString(curVal));

		achievementInfoService.processPackGoalProgress(packData);
	}

	/**
	 * Sets the current goal of the pack. Does not write back to database.
	 * @param packData Data of pack
	 * @param durationSeconds Duration of new pack goal
	 * @param goalField Distance or time
	 * @param goalAmount Total amount to complete goal
	 * @return 200 on success<br>
	 * 400 on malformed request<br>
	 * 401 if user is not pack owner<br>
	 * 404 if pack not existent
	 */
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

	/**
	 * Clear the pack's current goal. Does not write back to database.
	 * @param packData Data of pack
	 */
	void clearGoal(PackData packData) {
		packData.setPackGoal(new PackGoal());
	}
}