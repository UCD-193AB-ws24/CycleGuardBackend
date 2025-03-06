package cycleguard.database.globalLeaderboards;

import cycleguard.database.accessor.AbstractDatabaseAccessor;
import cycleguard.database.stats.UserStats;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Configuration
class GlobalLeaderboardsAccessor extends AbstractDatabaseAccessor<GlobalLeaderboards> {
	private final DynamoDbTable<GlobalLeaderboards> tableInstance;

	protected GlobalLeaderboardsAccessor() {
		tableInstance = getClient().table("CycleGuard-GlobalLeaderboards", TableSchema.fromBean(GlobalLeaderboards.class));
	}

	@Override
	protected DynamoDbTable<GlobalLeaderboards> getTableInstance() {
		return tableInstance;
	}

	@Override
	protected GlobalLeaderboards getBlankEntry() {
		return new GlobalLeaderboards();
	}
}
