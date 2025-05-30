package cycleguard.database.packs;

import cycleguard.database.accessor.UserProfileAccessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PackDataServiceTest {
	@Autowired
	private PackDataService packDataService;
	@Autowired
	private UserProfileAccessor userProfileAccessor;

	private final String user = "backendtests123", pack = "testpack123",
	user2 = "backendtests124";


	@Test
	void getPack() {
		assert packDataService.getPack(null) == null;
		assert packDataService.getPack("") == null;
		assert packDataService.getPack("oneirophages") == null;
		assert packDataService.getPack("Oneirophages") != null;
	}

	@Test
	void packExists() {
		assert !packDataService.packExists(null);
		assert !packDataService.packExists("nullpack123");
		assert packDataService.packExists("Oneirophages");
	}

	@Test
	void createPack() {
		packDataService.leavePack(user);
		packDataService.leavePack(user2);
		assert userProfileAccessor.getEntryOrDefaultBlank(user).getPack().isEmpty();
		assert packDataService.getPack(pack)==null;

		assert packDataService.createPack(user, null, "")!=200;
		assert packDataService.createPack(user, "", "")!=200;
		assert packDataService.createPack(user, pack, "")==200;

		assert packDataService.leavePackAsOwner(user, null)==200;
		assert packDataService.getPack(pack)==null;
	}

	@Test
	void joinLeavePack() {
		packDataService.leavePack(user);
		packDataService.leavePack(user2);
		assert userProfileAccessor.getEntryOrDefaultBlank(user).getPack().isEmpty();
		assert userProfileAccessor.getEntryOrDefaultBlank(user2).getPack().isEmpty();
		assert packDataService.getPack(pack)==null;

		assert packDataService.createPack(user, pack, "")==200;
		assert packDataService.joinPack(user2, pack, "123")!=200;
		assert packDataService.joinPack(user, pack, "")==200;
		assert packDataService.joinPack(user, "Oneirophages", "")!=200;
		assert packDataService.joinPack(user2, pack, "")==200;
		assert packDataService.joinPack(user2, "Oneirophages", "")!=200;

		assert packDataService.leavePackAsOwner(user, user2)==200;
		assert packDataService.leavePackAsOwner(user2, "")==200;

		assert packDataService.getPack(pack)==null;
	}

	@Test
	void leavePack() {
		packDataService.leavePack(user);
		packDataService.leavePack(user2);
		assert userProfileAccessor.getEntryOrDefaultBlank(user).getPack().isEmpty();
		assert packDataService.getPack(pack)==null;
		assert packDataService.createPack(user, pack, "")==200;
		assert packDataService.joinPack(user2, pack, "")==200;

		assert packDataService.getPack(pack).getOwner().equals(user);
		assert packDataService.getPack(pack).getMemberList().contains(user);

		assert packDataService.leavePack(user)!=200;
		assert packDataService.getPack(pack).getOwner().equals(user);
		assert packDataService.getPack(pack).getMemberList().contains(user);
		assert packDataService.leavePack(user2)==200;
		assert packDataService.leavePackAsOwner(user, "")==200;

		assert packDataService.getPack(pack)==null;
	}

	@Test
	void setGoal() {
		packDataService.leavePack(user);
		packDataService.leavePack(user2);
		assert userProfileAccessor.getEntryOrDefaultBlank(user).getPack().isEmpty();
		assert packDataService.getPack(pack)==null;
		assert packDataService.createPack(user, pack, "")==200;

		assert packDataService.setGoal(user, 0, "distance", 50)!=200;
		assert packDataService.setGoal(user, 3600, "", 50)!=200;
		assert packDataService.setGoal(user, 3600, "", 0)!=200;
		assert packDataService.setGoal(user, 3600, "distance", 50)==200;
		assert packDataService.setGoal(user, 3600, "distance", 50)==200;
		assert packDataService.cancelCurrentGoal(user)==200;
		assert packDataService.cancelCurrentGoal(user)==200;

		assert packDataService.leavePackAsOwner(user, "")==200;
		assert packDataService.getPack(pack)==null;
	}

	@Test
	void inviteUser() {
		packDataService.leavePack(user);
		packDataService.leavePack(user2);
		assert userProfileAccessor.getEntryOrDefaultBlank(user).getPack().isEmpty();
		assert packDataService.getPack(pack)==null;
		assert packDataService.createPack(user, pack, "")==200;

		assert packDataService.setGoal(user, 0, "distance", 50)!=200;
		assert packDataService.setGoal(user, 3600, "", 50)!=200;
		assert packDataService.setGoal(user, 3600, "", 0)!=200;
		assert packDataService.setGoal(user, 3600, "distance", 50)==200;
		assert packDataService.setGoal(user, 3600, "distance", 50)==200;
		assert packDataService.cancelCurrentGoal(user)==200;
		assert packDataService.cancelCurrentGoal(user)==200;

		assert packDataService.leavePackAsOwner(user, "")==200;
		assert packDataService.getPack(pack)==null;
	}

	@Test
	void acceptInvite() {
		packDataService.leavePack(user);
		packDataService.leavePack(user2);
		assert userProfileAccessor.getEntryOrDefaultBlank(user).getPack().isEmpty();
		assert packDataService.getPack(pack)==null;
		assert packDataService.createPack(user, pack, "")==200;

		assert packDataService.inviteUser(user, user2)==200;
		assert packDataService.inviteUser(user, user2)==200;
		assert packDataService.cancelInvite(user, user2)==200;
		assert packDataService.inviteUser(user, user2)==200;
		assert packDataService.declineInvite(user2, user)==200;
		assert packDataService.inviteUser(user, user2)==200;
		assert packDataService.acceptInvite(user2, pack)==200;

		assert packDataService.kickUser(user, user2)==200;
		assert packDataService.leavePackAsOwner(user, "")==200;
		assert packDataService.getPack(pack)==null;
	}
}