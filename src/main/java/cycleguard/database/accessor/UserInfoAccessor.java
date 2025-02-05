package cycleguard.database.accessor;

import cycleguard.database.entry.UserInfo;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Configuration
public class UserInfoAccessor extends AbstractDatabaseAccessor<UserInfo> {
	private final DynamoDbTable<UserInfo> tableInstance;

	protected UserInfoAccessor() {
		tableInstance = getClient().table("CycleGuard-UserInfo", TableSchema.fromBean(UserInfo.class));
	}

	@Override
	protected DynamoDbTable<UserInfo> getTableInstance() {
		return tableInstance;
	}
}
