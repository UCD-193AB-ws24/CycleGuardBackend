package cycleguard.database.accessor;

import cycleguard.database.AbstractDatabaseUserEntry;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@Configuration
public class UserInfoAccessor extends AbstractDatabaseAccessor<UserInfoAccessor.UserInfo> {
	private final DynamoDbTable<UserInfo> tableInstance;

	protected UserInfoAccessor() {
		tableInstance = getClient().table("CycleGuard-UserInfo", TableSchema.fromBean(UserInfo.class));
	}

	@Override
	protected DynamoDbTable<UserInfo> getTableInstance() {
		return tableInstance;
	}


	/**
	 * {@link DynamoDbBean} linking a username to that user's basic data.
	 *
	 * <br>
	 * <ul>
	 *     <li>cycleCoins - number of CycleCoins the user has.</li>
	 * </ul>
	 */
	@DynamoDbBean
	public static final class UserInfo extends AbstractDatabaseUserEntry {
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
}
