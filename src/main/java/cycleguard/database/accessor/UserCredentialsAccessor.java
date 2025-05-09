package cycleguard.database.accessor;

import cycleguard.database.AbstractDatabaseUserEntry;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import static cycleguard.database.accessor.UserCredentialsAccessor.HashedUserCredentials;

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

	@Override
	protected HashedUserCredentials getBlankEntry() {
		return new HashedUserCredentials();
	}

	/**
	 * {@link DynamoDbBean} linking a username to that user's encrypted password.<br>
	 * Passwords are encrypted within {@link cycleguard.auth.AccessTokenManager}.
	 * <br>
	 *
	 * <ul>
	 *     <li>{@link HashedUserCredentials#hashedPassword} - Hashed password of the user</li>
	 * </ul>
	 * @see cycleguard.auth.AccessTokenManager
	 */
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
