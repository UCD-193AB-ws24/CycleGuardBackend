package cycleguard.database;

import cycleguard.database.rides.ProcessRideService;

import java.time.Instant;

public interface RideProcessable {
	/**
	 * Process the information from a new ride.
	 * @param username User who completed ride
	 * @param rideInfo Stats of completed ride
	 * @param now Seconds since epoch when ride was completed
	 */
	void processNewRide(String username, ProcessRideService.RideInfo rideInfo, Instant now);
}
