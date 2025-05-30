package cycleguard.database.rides;

import cycleguard.database.tripCoordinates.TripCoordinatesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProcessRideServiceTest {
	@Autowired
	private ProcessRideService processRideService;

	@Autowired
	private TripCoordinatesService tripCoordinatesService;
	@Test
	void processNewRide() {
		var username = "javagod123";
		var rideinfo = new ProcessRideService.RideInfo();
		rideinfo.time = 5;
		rideinfo.calories = 50.5;
		rideinfo.distance = 1.2;
		rideinfo.latitudes = List.of("30", "30.001", "30.002");
		rideinfo.longitudes = List.of("-120", "-120.001", "-120.002");

		var timestamp = processRideService.processNewRide(username, rideinfo);
		tripCoordinatesService.getEntry(username, timestamp);
	}
}