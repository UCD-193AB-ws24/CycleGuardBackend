package cycleguard.database.accessor;

import cycleguard.database.AbstractDatabaseUserEntry;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import static cycleguard.database.accessor.UserProfileAccessor.*;
import static cycleguard.database.accessor.UserSettingsAccessor.UserSettings;

@Configuration
public class UserProfileAccessor extends AbstractDatabaseAccessor<UserProfile> {
	private final DynamoDbTable<UserProfile> tableInstance;

	protected UserProfileAccessor() {
		tableInstance = getClient().table("CycleGuard-UserProfile", TableSchema.fromBean(UserProfile.class));
	}

	@Override
	protected DynamoDbTable<UserProfile> getTableInstance() {
		return tableInstance;
	}

	@Override
	protected UserProfile getBlankEntry() {
		return new UserProfile();
	}


	/**
	 * {@link DynamoDbBean} linking a username to that user's profile.
	 *
	 * <br>
	 * <ul>
	 *     <li>{@link UserProfile#displayName} - Height of user, in inches.</li>
	 *     <li>{@link UserProfile#bio} - Height of user, in inches.</li>
	 * </ul>
	 */
	@DynamoDbBean
	public static final class UserProfile extends AbstractDatabaseUserEntry {
		private String displayName="", bio="";
		private boolean isPublic;

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		public String getBio() {
			return bio;
		}

		public void setBio(String bio) {
			this.bio = bio;
		}

		public boolean getIsPublic() {
			return isPublic;
		}

		public void setIsPublic(boolean aPublic) {
			isPublic = aPublic;
		}
	}
}
