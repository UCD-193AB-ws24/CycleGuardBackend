package cycleguard.database;

import cycleguard.database.rides.ProcessRideService;

import java.time.Instant;

public interface RideProcessable {
	void processNewRide(String username, ProcessRideService.RideInfo rideInfo, Instant now);
}
