package cycleguard.database.rides;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.rides.WeekHistory.DayHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

	public WeekHistory getWeekHistory(String username) {
		Instant now = Instant.now();
		WeekHistory weekHistory = weekHistoryAccessor.getEntryOrDefaultBlank(username);

		int startSize = weekHistory.getDayHistoryMap().size();
		removeOlderHistories(weekHistory, now);
		int endSize = weekHistory.getDayHistoryMap().size();

		if (startSize != endSize)
			weekHistoryAccessor.setEntry(weekHistory);

		return weekHistory;
	}

	public void addDayHistory(String username, ProcessRideService.RideInfo rideInfo) {
		DayHistory dayHistory = new DayHistory(rideInfo);

		Instant now = Instant.now();
		WeekHistory weekHistory = weekHistoryAccessor.getEntryOrDefaultBlank(username);
		removeOlderHistories(weekHistory, now);

		long curDayTime = getCurrentDayTime(now);
		Map<Long, DayHistory> dayHistoryMap = weekHistory.getDayHistoryMap();
		DayHistory prev = dayHistoryMap.getOrDefault(curDayTime, new DayHistory());

		dayHistory.addCalories(prev.getCalories());
		dayHistory.addTime(prev.getTime());
		dayHistory.addDistance(prev.getDistance());

		dayHistoryMap.put(curDayTime, dayHistory);

		weekHistoryAccessor.setEntry(weekHistory);
	}

	private void removeOlderHistories(WeekHistory weekHistory, Instant now) {
		Map<Long, DayHistory> dayHistoryMap = weekHistory.getDayHistoryMap();
		dayHistoryMap.entrySet().removeIf(e -> getDaysBetweenTimeAndNow(e.getKey(), now.getEpochSecond()) >= HISTORY_LENGTH);
	}
}
