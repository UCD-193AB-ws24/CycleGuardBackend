package cycleguard.database.accessor;

import cycleguard.database.AbstractDatabaseUserEntry;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@Configuration
public class UserCredentialsAccessor extends AbstractDatabaseAccessor<UserCredentialsAccessor.HashedUserCredentials> {
	private final DynamoDbTable<HashedUserCredentials> tableInstance;

	protected UserCredentialsAccessor() {
		tableInstance = getClient().table("CycleGuard-UserCredentials", TableSchema.fromBean(HashedUserCredentials.class));
	}

	@Override
	protected DynamoDbTable<HashedUserCredentials> getTableInstance() {
		return tableInstance;
	}

	@DynamoDbBean
	public static final class HashedUserCredentials extends AbstractDatabaseUserEntry {
		private String hashedPassword;

		public String getHashedPassword() {
			return hashedPassword;
		}

		public void setHashedPassword(String hashedPassword) {
			this.hashedPassword = hashedPassword;
		}
	}
}
