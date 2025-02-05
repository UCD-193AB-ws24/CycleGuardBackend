package cycleguard.database.accessor;

import cycleguard.database.entry.HashedUserCredentials;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Configuration
public class UserCredentialsAccessor extends AbstractDatabaseAccessor<HashedUserCredentials> {
	private final DynamoDbTable<HashedUserCredentials> tableInstance;

	protected UserCredentialsAccessor() {
		tableInstance = getClient().table("CycleGuard-UserCredentials", TableSchema.fromBean(HashedUserCredentials.class));
	}

	@Override
	protected DynamoDbTable<HashedUserCredentials> getTableInstance() {
		return tableInstance;
	}
}
