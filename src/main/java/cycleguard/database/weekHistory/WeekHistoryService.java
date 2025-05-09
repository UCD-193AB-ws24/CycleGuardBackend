package cycleguard.database.weekHistory;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.RideProcessable;
import cycleguard.database.accessor.PurchaseInfoAccessor;
import cycleguard.database.achievements.AchievementInfo;
import cycleguard.database.rides.ProcessRideService;
import cycleguard.database.weekHistory.WeekHistory.SingleRideHistory;
import cycleguard.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

import static cycleguard.util.TimeUtil.getCurrentDayTime;
import static cycleguard.util.TimeUtil.getDaysBetweenTimeAndNow;

/**
 * Service wrapper for retrieving and modifying {@link WeekHistory}.
 */
@Service
public class WeekHistoryService implements RideProcessable {
	private static final int HISTORY_LENGTH = 7;
	@Autowired
	private WeekHistoryAccessor weekHistoryAccessor;
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private PurchaseInfoAccessor purchaseInfoAccessor;

	/**
	 * Get the week history for a user. Guaranteed 7 or fewer entries.
	 * @param username Username to retrieve history of
	 * @return {@link WeekHistory} with length <= 7
	 */
	public WeekHistory getWeekHistory(String username) {
		Instant now = TimeUtil.getAdjustedInstant(Instant.now());
		WeekHistory weekHistory = weekHistoryAccessor.getEntryOrDefaultBlank(username);

		int startSize = weekHistory.getDayHistoryMap().size();
		removeOlderHistories(weekHistory, now);
		int endSize = weekHistory.getDayHistoryMap().size();

		if (startSize != endSize)
			weekHistoryAccessor.setEntry(weekHistory);

		return weekHistory;
	}

	/**
	 * Updates the user's {@link WeekHistory} by
	 * adding a new entry or accumulating an existing entry for the current day.<br>
	 * Also, give CycleCoins if biked over five miles.
	 * @param username User who completed ride
	 * @param rideInfo Stats of completed ride
	 * @param now Seconds since epoch when ride was completed
	 */
	@Override
	public void processNewRide(String username, ProcessRideService.RideInfo rideInfo, Instant now) {
		now = TimeUtil.getAdjustedInstant(now);

		SingleRideHistory singleRideHistory = new SingleRideHistory(rideInfo);

		WeekHistory weekHistory = weekHistoryAccessor.getEntryOrDefaultBlank(username);
		removeOlderHistories(weekHistory, now);

		long curDayTime = getCurrentDayTime(now);
		Map<Long, SingleRideHistory> dayHistoryMap = weekHistory.getDayHistoryMap();
		SingleRideHistory prev = dayHistoryMap.getOrDefault(curDayTime, new SingleRideHistory());

		singleRideHistory.addCalories(prev.getCalories());
		singleRideHistory.addTime(prev.getTime());
		singleRideHistory.addDistance(prev.getDistance());
		singleRideHistory.setOverFiveMiles(prev.isOverFiveMiles());

		boolean overFiveMiles = singleRideHistory.getDistanceDouble() >= 5 && !singleRideHistory.isOverFiveMiles();
		int earnedCycleCoins = (int)rideInfo.distance;

		if (overFiveMiles || earnedCycleCoins>0) {
			var purchaseInfo = purchaseInfoAccessor.getEntryOrDefaultBlank(username);
			if (overFiveMiles) {
				singleRideHistory.setOverFiveMiles(true);
				earnedCycleCoins += 5;
			}
			purchaseInfo.setCycleCoins(purchaseInfo.getCycleCoins()+earnedCycleCoins);
			purchaseInfoAccessor.setEntry(purchaseInfo);
		}

		if (singleRideHistory.getDistanceDouble() >= 5 && !singleRideHistory.isOverFiveMiles()) {
			singleRideHistory.setOverFiveMiles(true);

			var purchaseInfo = purchaseInfoAccessor.getEntryOrDefaultBlank(username);
			purchaseInfo.setCycleCoins(purchaseInfo.getCycleCoins()+5);
			purchaseInfoAccessor.setEntry(purchaseInfo);
		}

		dayHistoryMap.put(curDayTime, singleRideHistory);

		weekHistoryAccessor.setEntry(weekHistory);
	}

	/**
	 * Remove all histories from a given {@link WeekHistory} older than the current timestamp by 7 days.
	 * @param weekHistory {@link WeekHistory} to remove
	 * @param now Current system timestamp
	 */
	private void removeOlderHistories(WeekHistory weekHistory, Instant now) {
		Map<Long, SingleRideHistory> dayHistoryMap = weekHistory.getDayHistoryMap();
		dayHistoryMap.entrySet().removeIf(e -> getDaysBetweenTimeAndNow(e.getKey(), now.getEpochSecond()) >= HISTORY_LENGTH);
	}
}
