package cycleguard.database.stats;

public class PublicUserStats {
	private String totalDistance, totalTime;
	public PublicUserStats(){}
	public PublicUserStats(UserStats userStats) {
		this.totalDistance= userStats.getTotalDistance();
		this.totalTime = userStats.getTotalTime();
	}

	public String getTotalDistance() {
		return totalDistance;
	}

	public void setTotalDistance(String totalDistance) {
		this.totalDistance = totalDistance;
	}

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}
}
