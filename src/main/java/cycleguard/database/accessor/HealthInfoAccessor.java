package cycleguard.database.accessor;

import cycleguard.database.AbstractDatabaseUserEntry;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import cycleguard.database.accessor.HealthInfoAccessor.HealthInfo;

@Configuration
public class HealthInfoAccessor extends AbstractDatabaseAccessor<HealthInfo> {
	private final DynamoDbTable<HealthInfo> tableInstance;

	protected HealthInfoAccessor() {
		tableInstance = getClient().table("CycleGuard-HealthInfo", TableSchema.fromBean(HealthInfo.class));
	}

	@Override
	protected DynamoDbTable<HealthInfo> getTableInstance() {
		return tableInstance;
	}

	@Override
	protected HealthInfo getBlankEntry() {
		return new HealthInfo();
	}


	/**
	 * {@link DynamoDbBean} linking a username to that user's health data.
	 *
	 * <br>
	 * <ul>
	 *     <li>{@link HealthInfo#heightInches} - Height of user, in inches.</li>
	 *     <li>{@link HealthInfo#weightPounds} - Weight of user, in pounds.</li>
	 *     <li>{@link HealthInfo#ageYears} - Age of user, in years.</li>
	 * </ul>
	 */
	@DynamoDbBean
	public static final class HealthInfo extends AbstractDatabaseUserEntry {
		private long heightInches, weightPounds, ageYears;

		public long getHeightInches() {
			return heightInches;
		}

		public void setHeightInches(long heightInches) {
			this.heightInches = heightInches;
		}

		public long getWeightPounds() {
			return weightPounds;
		}

		public void setWeightPounds(long weightPounds) {
			this.weightPounds = weightPounds;
		}

		public long getAgeYears() {
			return ageYears;
		}

		public void setAgeYears(long ageYears) {
			this.ageYears = ageYears;
		}
	}
}
