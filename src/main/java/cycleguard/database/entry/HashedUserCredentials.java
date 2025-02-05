package cycleguard.database.entry;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

/**
 * {@link DynamoDbBean} linking a username to a hashed password, for login purposes.
 *
 * <br>
 * <ul>
 *     <li>hashedPassword - salted hash of the user's chosen password.</li>
 * </ul>
 */
@DynamoDbBean
public final class HashedUserCredentials extends AbstractDatabaseUserEntry{
	private String hashedPassword;

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}
}
