package cycleguard.database.entry;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

/**
 * {@link DynamoDbBean} linking an authentication token into a username.
 *
 * <br>
 * <ul>
 *     <li>username - Username of user linked to authentication token.</li>
 * </ul>
 */
@DynamoDbBean
public final class AuthToken extends AbstractDatabaseEntry {
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
}
