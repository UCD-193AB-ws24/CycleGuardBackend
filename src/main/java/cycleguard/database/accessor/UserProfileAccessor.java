package cycleguard.database.accessor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Configuration;

import cycleguard.database.AbstractDatabaseUserEntry;
import cycleguard.database.accessor.UserProfileAccessor.UserProfile;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;

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
		var profile = new UserProfile();
		profile.setPack("");
		return profile;
	}

	public List<UserProfile> getAllUsers() {
		try {
			// Perform a scan operation on the DynamoDB table
			var users = tableInstance.scan(ScanEnhancedRequest.builder().build())
				.items()
				.stream()
				.collect(Collectors.toList());

			for (var user : users) {
				if (!user.getIsPublic()) {
					user.setBio("");
					user.setPack("");
					user.setIsNewAccount(false);
				}
			}

			return users;

		} catch (Exception e) {
			System.err.println("Error fetching users from DynamoDB: " + e.getMessage());
			return Collections.emptyList(); // Return empty list in case of failure
		}
	}

	/**
	 * {@link DynamoDbBean} linking a username to that user's profile information.
	 * <br>
	 *
	 * <ul>
	 *     <li>{@link UserProfile#displayName} - Custom display name of user: not necessarily username</li>
	 *     <li>{@link UserProfile#bio} - Custom biography/description of user</li>
	 *     <li>{@link UserProfile#pack} - User's current pack. getBlankEntry sets pack to empty string, but defaults to null.</li>
	 *     <li>{@link UserProfile#isPublic} - If the user's profile is publicly viewable.</li>
	 *     <li>{@link UserProfile#isNewAccount} - If the account is new and should display a tutorial on startup.</li>
	 *     <li>{@link UserProfile#profileIcon} - Name of profile icon used by user.</li>
	 */
	@DynamoDbBean
	public static final class UserProfile extends AbstractDatabaseUserEntry {
		private String displayName="", bio="", pack;
		private boolean isPublic, isNewAccount=true;
		private String profileIcon="";

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

		public String getPack() {
			return pack;
		}

		public void setPack(String pack) {
			this.pack = pack;
		}

		public boolean getIsNewAccount() {
			return isNewAccount;
		}

		public void setIsNewAccount(boolean newAccount) {
			isNewAccount = newAccount;
		}

		public String getProfileIcon() {
			return profileIcon;
		}

		public void setProfileIcon(String profileIcon) {
			this.profileIcon = profileIcon;
		}


	}
}
