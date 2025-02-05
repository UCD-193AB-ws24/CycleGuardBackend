package cycleguard.database.entry;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

/**
 * {@link DynamoDbBean} linking a username to that user's basic data.
 *
 * <br>
 * <ul>
 *     <li>cycleCoins - number of CycleCoins the user has.</li>
 * </ul>
 */
@DynamoDbBean
public final class UserInfo extends AbstractDatabaseUserEntry {
	private long cycleCoins;
	private String email;

	public long getCycleCoins() {
		return cycleCoins;
	}

	public void setCycleCoins(long cycleCoins) {
		this.cycleCoins = cycleCoins;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
