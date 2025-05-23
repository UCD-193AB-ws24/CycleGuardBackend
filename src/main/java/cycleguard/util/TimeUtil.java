package cycleguard.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TimeUtil {
	private static final int HOUR_OFFSET = -7;
	public static long getDaysBetweenTimeAndNow(long startSeconds, long endSeconds) {
		Instant start = Instant.ofEpochSecond(startSeconds);
		Instant end = Instant.ofEpochSecond(endSeconds);
		return ChronoUnit.DAYS.between(start, end);
	}



	public static long getCurrentDayTime() {
		return getCurrentDayTime(Instant.now());
	}

	public static long getCurrentDayTime(Instant now) {
		long daysSinceEpoch = getDaysBetweenTimeAndNow(0, now.getEpochSecond());

		Instant truncated = ChronoUnit.DAYS.addTo(Instant.EPOCH, daysSinceEpoch);
		return truncated.getEpochSecond();
	}

	public static long getCurrentSecond(Instant now) {
		return now.getEpochSecond();
	}

	public static long getCurrentSecond() {
		return getCurrentSecond(Instant.now());
	}

	public static Instant getAdjustedInstant(Instant now) {
		Instant res = ChronoUnit.HOURS.addTo(now, HOUR_OFFSET);
		return res;
	}
}
