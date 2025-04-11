package cycleguard.database.packs.packData;

import cycleguard.database.AbstractDatabaseEntry;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.Instant;
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
@DynamoDbBean
public final class PackData extends AbstractDatabaseEntry {
	private String name="", password="", owner="";
	private List<String> memberList=new ArrayList<>();

	@DynamoDbPartitionKey
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
}