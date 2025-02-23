package cycleguard.database.rides;

import cycleguard.database.rides.WeekHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessRideService {
	@Autowired
	private WeekHistoryService weekHistoryService;

	public void processNewRide(String username, RideInfo rideInfo) {
		weekHistoryService.addDayHistory(username, rideInfo);
	}

	public static final class RideInfo {
		public double distance, calories, time;
	}
}
