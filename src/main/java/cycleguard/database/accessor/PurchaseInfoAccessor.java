package cycleguard.database.accessor;

import cycleguard.database.AbstractDatabaseUserEntry;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Configuration
public class PurchaseInfoAccessor extends AbstractDatabaseAccessor<PurchaseInfoAccessor.PurchaseInfo> {
	private final DynamoDbTable<PurchaseInfo> tableInstance;

	protected PurchaseInfoAccessor() {
		tableInstance = getClient().table("CycleGuard-PurchaseInfo", TableSchema.fromBean(PurchaseInfo.class));
	}

	@Override
	protected DynamoDbTable<PurchaseInfo> getTableInstance() {
		return tableInstance;
	}

	@Override
	protected PurchaseInfo getBlankEntry() {
		return new PurchaseInfo();
	}


	/**
	 * {@link DynamoDbBean} linking a username to that user's shop-related data, and owned items.
	 * <br>
	 *
	 * <ul>
	 *     <li>{@link PurchaseInfo#cycleCoins} - Number of owned CycleCoins</li>
	 *     <li>{@link PurchaseInfo#themesOwned} - List of owned themes, as {@link String}s</li>
	 *     <li>{@link PurchaseInfo#miscOwned} - List of owned miscellaneous items, as {@link String}s</li>
	 *     <li>{@link PurchaseInfo#iconsOwned} - List of owned user icon, as {@link String}s</li>
	 * </ul>
	 */
	@DynamoDbBean
	public static final class PurchaseInfo extends AbstractDatabaseUserEntry {
		private long cycleCoins;
		private List<String> themesOwned = new ArrayList<>();
		private List<String> miscOwned = new ArrayList<>();
		private List<String> iconsOwned = new ArrayList<>();

		public long getCycleCoins() {
			return cycleCoins;
		}

		public void setCycleCoins(long cycleCoins) {
			this.cycleCoins = cycleCoins;
		}

		public List<String> getThemesOwned() {
			return themesOwned;
		}

		public void setThemesOwned(List<String> themesOwned) {
			this.themesOwned = themesOwned;
		}

		public List<String> getMiscOwned() {
			return miscOwned;
		}

		public void setMiscOwned(List<String> miscOwned) {
			this.miscOwned = miscOwned;
		}

		public List<String> getIconsOwned() {
			return iconsOwned;
		}

		public void setIconsOwned(List<String> iconsOwned) {
			this.iconsOwned = iconsOwned;
		}
	}
}
