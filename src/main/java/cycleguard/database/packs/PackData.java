package cycleguard.database.packs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import cycleguard.database.AbstractDatabaseEntry;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link DynamoDbBean} linking a pack to its data.
 *
 * <br>
 * <ul>
 *     <li></li>
 * </ul>
 */
@JsonIgnoreProperties("hashedPassword")
@DynamoDbBean
public final class PackData extends AbstractDatabaseEntry {
	private String name="", hashedPassword="", owner="";
	private List<String> memberList = new ArrayList<>();
	private List<String> invites = new ArrayList<>();
	private PackGoal packGoal = new PackGoal();

	@DynamoDbPartitionKey
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public List<String> getMemberList() {
		return memberList;
	}

	public void setMemberList(List<String> memberList) {
		this.memberList = memberList;
	}

	@Override
	public void setPrimaryKey(String key) {
		setName(key);
	}

	public int getMemberCount() {
		return memberList.size();
	}

	public PackGoal getPackGoal() {
		return packGoal;
	}

	public void setPackGoal(PackGoal packGoal) {
		this.packGoal = packGoal;
	}

	public List<String> getInvites() {
		return invites;
	}

	public void setInvites(List<String> invites) {
		this.invites = invites;
	}
}