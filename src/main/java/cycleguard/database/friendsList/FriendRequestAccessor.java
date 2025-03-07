package cycleguard.database.friendsList;

import cycleguard.database.accessor.AbstractDatabaseAccessor;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Configuration
class FriendRequestAccessor extends AbstractDatabaseAccessor<FriendRequestList> {
	private final DynamoDbTable<FriendRequestList> tableInstance;

	protected FriendRequestAccessor() {
		tableInstance = getClient().table("CycleGuard-FriendRequestList", TableSchema.fromBean(FriendRequestList.class));
	}

	@Override
	protected DynamoDbTable<FriendRequestList> getTableInstance() {
		return tableInstance;
	}

	@Override
	protected FriendRequestList getBlankEntry() {
		return new FriendRequestList();
	}
}
