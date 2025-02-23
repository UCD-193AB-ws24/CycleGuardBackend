package cycleguard.database.stats;

import cycleguard.database.accessor.AbstractDatabaseAccessor;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Configuration
class UserStatsAccessor extends AbstractDatabaseAccessor<UserStats> {
	private final DynamoDbTable<UserStats> tableInstance;

	protected UserStatsAccessor() {
		tableInstance = getClient().table("CycleGuard-UserStats", TableSchema.fromBean(UserStats.class));
	}

	@Override
	protected DynamoDbTable<UserStats> getTableInstance() {
		return tableInstance;
	}

	@Override
	protected UserStats getBlankEntry() {
		return new UserStats();
	}
}
