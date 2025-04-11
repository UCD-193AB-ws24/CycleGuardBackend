package cycleguard.database.packs.packGoal;

import cycleguard.database.accessor.AbstractDatabaseAccessor;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Configuration
class PackGoalAccessor extends AbstractDatabaseAccessor<PackGoal> {
	private final DynamoDbTable<PackGoal> tableInstance;

	protected PackGoalAccessor() {
		tableInstance = getClient().table("CycleGuard-PackGoal", TableSchema.fromBean(PackGoal.class));
	}

	@Override
	protected DynamoDbTable<PackGoal> getTableInstance() {
		return tableInstance;
	}

	@Override
	protected PackGoal getBlankEntry() {
		return new PackGoal();
	}
}
