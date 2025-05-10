package cycleguard.database.packs;

import cycleguard.database.AbstractDatabaseUserEntry;
import cycleguard.database.accessor.AbstractDatabaseAccessor;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.ArrayList;
import java.util.List;

import static cycleguard.database.packs.PackInvitesAccessor.PackInvites;

@Configuration
public class PackInvitesAccessor extends AbstractDatabaseAccessor<PackInvites> {
	private final DynamoDbTable<PackInvites> tableInstance;

	protected PackInvitesAccessor() {
		tableInstance = getClient().table("CycleGuard-PackInvites", TableSchema.fromBean(PackInvites.class));
	}

	@Override
	protected DynamoDbTable<PackInvites> getTableInstance() {
		return tableInstance;
	}

	@Override
	protected PackInvites getBlankEntry() {
		return new PackInvites();
	}


	/**
	 * {@link DynamoDbBean} linking a username to that user's pack invites.
	 *
	 * <br>
	 * <ul>
	 *     <li>{@link PackInvites#invites} - List of packs the user has been invited to</li>
	 * </ul>
	 */
	@DynamoDbBean
	public static final class PackInvites extends AbstractDatabaseUserEntry {
		private List<String> invites = new ArrayList<>();

		public List<String> getInvites() {
			return invites;
		}

		public void setInvites(List<String> invites) {
			this.invites = invites;
		}
	}
}
