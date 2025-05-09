package cycleguard.database.accessor;

import cycleguard.database.AbstractDatabaseUserEntry;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import static cycleguard.database.accessor.UserSettingsAccessor.*;

@Configuration
public class UserSettingsAccessor extends AbstractDatabaseAccessor<UserSettings> {
	private final DynamoDbTable<UserSettings> tableInstance;

	protected UserSettingsAccessor() {
		tableInstance = getClient().table("CycleGuard-UserSettings", TableSchema.fromBean(UserSettings.class));
	}

	@Override
	protected DynamoDbTable<UserSettings> getTableInstance() {
		return tableInstance;
	}

	@Override
	protected UserSettings getBlankEntry() {
		return new UserSettings();
	}


	/**
	 * {@link DynamoDbBean} linking a username to that user's customized settings.
	 * <br>
	 *
	 * <ul>
	 *     <li>{@link UserSettings#darkModeEnabled} - If the user is in dark mode</li>
	 *     <li>{@link UserSettings#currentTheme} - Name of user's currently used theme</li>
	 * </ul>
	 */
	@DynamoDbBean
	public static final class UserSettings extends AbstractDatabaseUserEntry {
		private boolean darkModeEnabled=false;
		private String currentTheme = "orange";

		public boolean isDarkModeEnabled() {
			return darkModeEnabled;
		}

		public void setDarkModeEnabled(boolean darkModeEnabled) {
			this.darkModeEnabled = darkModeEnabled;
		}

		public String getCurrentTheme() {
			return currentTheme;
		}

		public void setCurrentTheme(String currentTheme) {
			this.currentTheme = currentTheme;
		}
	}
}
