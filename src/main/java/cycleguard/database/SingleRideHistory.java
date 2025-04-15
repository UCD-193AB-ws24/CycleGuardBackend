package cycleguard.database;

import cycleguard.database.rides.ProcessRideService;
import cycleguard.util.StringDoubles;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import static cycleguard.util.StringDoubles.toDouble;

@DynamoDbBean
public final class SingleRideHistory {
	public SingleRideHistory() {}
	public SingleRideHistory(ProcessRideService.RideInfo rideInfo) {
		distance = StringDoubles.toString(rideInfo.distance);
		calories = StringDoubles.toString(rideInfo.calories);
		time = StringDoubles.toString(rideInfo.time);
	}
	private String distance="0", calories="0", time="0";

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getCalories() {
		return calories;
	}

	public void setCalories(String calories) {
		this.calories = calories;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void addDistance(String distance) {
		this.distance = StringDoubles.toString(toDouble(this.distance) + toDouble(distance));
	}

	public void addCalories(String calories) {
		this.calories = StringDoubles.toString(toDouble(this.calories) + toDouble(calories));
	}

	public void addTime(String time) {
		this.time = StringDoubles.toString(toDouble(this.time) + toDouble(time));
	}
}
