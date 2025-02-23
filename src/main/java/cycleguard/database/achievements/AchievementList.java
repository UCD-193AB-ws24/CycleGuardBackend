package cycleguard.database.achievements;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AchievementList {
//	Maps name : data
	private static final Map<String, AchievementData> ACHIEVEMENT_MAP = Map.of(
//			1,
	);

	public static final class AchievementData {
		public String description, category;
	}
}
