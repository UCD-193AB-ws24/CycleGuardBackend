package cycleguard.database.triphistory;

import cycleguard.database.accessor.AbstractDatabaseAccessor;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Configuration
class TripHistoryAccessor extends AbstractDatabaseAccessor<TripHistory> {
	private final DynamoDbTable<TripHistory> tableInstance;

	protected TripHistoryAccessor() {
		tableInstance = getClient().table("CycleGuard-TripHistory", TableSchema.fromBean(TripHistory.class));
	}

	@Override
	protected DynamoDbTable<TripHistory> getTableInstance() {
		return tableInstance;
	}

	@Override
	protected TripHistory getBlankEntry() {
		return new TripHistory();
	}
}
