package cycleguard.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Configuration
public class UserInfoAccessor extends DatabaseAccessor<UserInfo> {
	private final DynamoDbTable<UserInfo> tableInstance;

	protected UserInfoAccessor() {
		super();
		tableInstance = client.table("CycleGuard-UserInfo", TableSchema.fromBean(UserInfo.class));
	}

	@Override
	protected DynamoDbTable<UserInfo> getTableInstance() {
		return tableInstance;
	}
}
