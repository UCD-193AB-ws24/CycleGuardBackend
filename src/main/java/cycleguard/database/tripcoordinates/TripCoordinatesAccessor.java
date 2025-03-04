package cycleguard.database.tripcoordinates;

import cycleguard.database.accessor.AbstractDatabaseAccessor;
import cycleguard.database.triphistory.TripHistory;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Configuration
class TripCoordinatesAccessor extends AbstractDatabaseAccessor<TripCoordinates> {
	private final DynamoDbTable<TripCoordinates> tableInstance;

	protected TripCoordinatesAccessor() {
		tableInstance = getClient().table("CycleGuard-TripCoordinates", TableSchema.fromBean(TripCoordinates.class));
	}

	@Override
	protected DynamoDbTable<TripCoordinates> getTableInstance() {
		return tableInstance;
	}

	@Override
	protected TripCoordinates getBlankEntry() {
		return new TripCoordinates();
	}
}
