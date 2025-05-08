package cycleguard.database.accessor;

import cycleguard.database.AbstractDatabaseEntry;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Configuration
public class AuthTokenAccessor extends AbstractDatabaseAccessor<AuthTokenAccessor.AuthToken> {
	private final DynamoDbTable<AuthToken> tableInstance;

	protected AuthTokenAccessor() {
		tableInstance = getClient().table("CycleGuard-AuthToken", TableSchema.fromBean(AuthToken.class));
	}

	@Override
	protected AuthToken getBlankEntry() {
		return new AuthToken();
	}

	@Override
	protected DynamoDbTable<AuthToken> getTableInstance() {
		return tableInstance;
	}



	/**
	 * {@link DynamoDbBean} linking an authentication token into a username.
	 *
	 * <br>
	 * <ul>
	 *     <li>username - Username of user linked to authentication token.</li>
	 * </ul>
	 */
	@DynamoDbBean
	public static final class AuthToken extends AbstractDatabaseEntry {
		private String token;
		private String username;

		@DynamoDbPartitionKey
		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		@Override
		public void setPrimaryKey(String key) {
			setToken(username);
		}

		@Override
		public String getPrimaryKey() {
			return getToken();
		}
	}

}
