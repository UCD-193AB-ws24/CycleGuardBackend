package cycleguard.database.packs;

import cycleguard.database.accessor.AbstractDatabaseAccessor;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Configuration
class PackDataAccessor extends AbstractDatabaseAccessor<PackData> {
	private final DynamoDbTable<PackData> tableInstance;

	protected PackDataAccessor() {
		tableInstance = getClient().table("CycleGuard-PackData", TableSchema.fromBean(PackData.class));
	}

	@Override
	protected DynamoDbTable<PackData> getTableInstance() {
		return tableInstance;
	}

	@Override
	protected PackData getBlankEntry() {
		return new PackData();
	}
}
