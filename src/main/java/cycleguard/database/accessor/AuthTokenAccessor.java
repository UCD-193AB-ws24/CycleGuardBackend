package cycleguard.database.accessor;

import cycleguard.database.entry.AuthToken;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Configuration
public class AuthTokenAccessor extends AbstractDatabaseAccessor<AuthToken> {
	private final DynamoDbTable<AuthToken> tableInstance;

	protected AuthTokenAccessor() {
		tableInstance = getClient().table("CycleGuard-AuthToken", TableSchema.fromBean(AuthToken.class));
	}

	@Override
	protected DynamoDbTable<AuthToken> getTableInstance() {
		return tableInstance;
	}
}
