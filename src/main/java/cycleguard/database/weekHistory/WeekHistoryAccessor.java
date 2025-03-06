package cycleguard.database.weekHistory;

import cycleguard.database.accessor.AbstractDatabaseAccessor;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Configuration
class WeekHistoryAccessor extends AbstractDatabaseAccessor<WeekHistory> {
	private final DynamoDbTable<WeekHistory> tableInstance;

	protected WeekHistoryAccessor() {
		tableInstance = getClient().table("CycleGuard-WeekHistory", TableSchema.fromBean(WeekHistory.class));
	}

	@Override
	protected DynamoDbTable<WeekHistory> getTableInstance() {
		return tableInstance;
	}

	@Override
	protected WeekHistory getBlankEntry() {
		return new WeekHistory();
	}
}
