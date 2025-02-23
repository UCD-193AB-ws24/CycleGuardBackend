package cycleguard.database.achievements;

import cycleguard.database.accessor.AbstractDatabaseAccessor;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Configuration
class AchievementInfoAccessor extends AbstractDatabaseAccessor<AchievementInfo> {
	private final DynamoDbTable<AchievementInfo> tableInstance;

	protected AchievementInfoAccessor() {
		tableInstance = getClient().table("CycleGuard-AchievementInfo", TableSchema.fromBean(AchievementInfo.class));
	}

	@Override
	protected DynamoDbTable<AchievementInfo> getTableInstance() {
		return tableInstance;
	}

	@Override
	protected AchievementInfo getBlankEntry() {
		return new AchievementInfo();
	}
}
