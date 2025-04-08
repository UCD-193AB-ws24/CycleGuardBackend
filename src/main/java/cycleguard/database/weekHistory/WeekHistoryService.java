package cycleguard.database.weekHistory;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.PurchaseInfoAccessor;
import cycleguard.database.rides.ProcessRideService;
import cycleguard.database.weekHistory.WeekHistory.SingleRideHistory;
import cycleguard.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

import static cycleguard.util.TimeUtil.getCurrentDayTime;
import static cycleguard.util.TimeUtil.getDaysBetweenTimeAndNow;

@Service
public class WeekHistoryService {
	private static final int HISTORY_LENGTH = 7;
	@Autowired
	private WeekHistoryAccessor weekHistoryAccessor;

	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private PurchaseInfoAccessor purchaseInfoAccessor;

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

		if (singleRideHistory.getDistanceDouble() >= 5 && !singleRideHistory.isOverFiveMiles()) {
			singleRideHistory.setOverFiveMiles(true);

			var purchaseInfo = purchaseInfoAccessor.getEntryOrDefaultBlank(username);
			purchaseInfo.setCycleCoins(purchaseInfo.getCycleCoins()+5);
			purchaseInfoAccessor.setEntry(purchaseInfo);
		}

		dayHistoryMap.put(curDayTime, singleRideHistory);

		weekHistoryAccessor.setEntry(weekHistory);
	}

	private void removeOlderHistories(WeekHistory weekHistory, Instant now) {
		Map<Long, SingleRideHistory> dayHistoryMap = weekHistory.getDayHistoryMap();
		dayHistoryMap.entrySet().removeIf(e -> getDaysBetweenTimeAndNow(e.getKey(), now.getEpochSecond()) >= HISTORY_LENGTH);
	}
}
