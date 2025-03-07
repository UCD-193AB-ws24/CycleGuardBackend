package cycleguard.database.friendsList;

import cycleguard.database.accessor.AbstractDatabaseAccessor;
import cycleguard.database.tripCoordinates.TripCoordinates;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Configuration
class FriendsListAccessor extends AbstractDatabaseAccessor<FriendsList> {
	private final DynamoDbTable<FriendsList> tableInstance;

	protected FriendsListAccessor() {
		tableInstance = getClient().table("CycleGuard-FriendsList", TableSchema.fromBean(FriendsList.class));
	}

	@Override
	protected DynamoDbTable<FriendsList> getTableInstance() {
		return tableInstance;
	}

	@Override
	protected FriendsList getBlankEntry() {
		return new FriendsList();
	}
}
