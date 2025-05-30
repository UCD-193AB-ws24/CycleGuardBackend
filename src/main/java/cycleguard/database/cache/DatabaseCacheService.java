package cycleguard.database.cache;

import cycleguard.database.accessor.AbstractDatabaseAccessor;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseCacheService {
	public static final int CACHE_LIFETIME_MILLIS = 60_000;
	public static final int BLANK_LIFETIME_MILLIS = 180_000;
	public static final int MAX_CACHE_WRITES = 3;
	public static final Duration CLEAR_DELAY = Duration.of(1, ChronoUnit.MINUTES);
	@Autowired
	private TaskScheduler executor;

	@PostConstruct
	public void postConstruct() {
		executor.scheduleWithFixedDelay(new CacheServiceRunnable(), CLEAR_DELAY);
	}

	private final List<AbstractDatabaseAccessor> subscriptions = new ArrayList<>();
	public void subscribe(AbstractDatabaseAccessor accessor) {
		subscriptions.add(accessor);
	}

	private class CacheServiceRunnable implements Runnable {
		@Override
		public void run() {
			long systemTime = System.currentTimeMillis();
			for (AbstractDatabaseAccessor subscription : subscriptions) {
				subscription.clearOldCacheEntries(systemTime);
			}
		}
	}
}
