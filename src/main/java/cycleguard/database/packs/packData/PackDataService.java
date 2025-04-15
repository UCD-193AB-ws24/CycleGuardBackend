package cycleguard.database.packs.packData;

import cycleguard.database.accessor.UserProfileAccessor;
import cycleguard.database.accessor.UserProfileAccessor.UserProfile;
import cycleguard.database.packs.packGoal.PackGoalService;
import cycleguard.database.stats.UserStats;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PackDataService {
	@Autowired
	private UserProfileAccessor userProfileAccessor;
	@Autowired
	private PackDataAccessor packDataAccessor;
	@Autowired
	private PackGoalService packGoalService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public PackData getPack(String packName) {
		return packDataAccessor.getEntryOrDefaultBlank(packName);
	}

	public boolean packExists(String packName) {
		return packDataAccessor.hasEntry(packName);
	}
	public int createPack(String username, String packName, String password) {
		packName = packName.trim();
		if (packName.isEmpty()) return HttpServletResponse.SC_BAD_REQUEST;

		UserProfile userProfile = userProfileAccessor.getEntry(username);
		if (userProfile.getPack() != null && !userProfile.getPack().isEmpty())
			return HttpServletResponse.SC_CONFLICT;
		if (packExists(packName)) return HttpServletResponse.SC_CONFLICT;

		String hashedPassword = passwordEncoder.encode(password);

		PackData packData = new PackData();
		packData.setOwner(username);
		packData.setName(packName);
		packData.setHashedPassword(hashedPassword);
		packData.getMemberList().add(username);

		packDataAccessor.setEntry(packData);
		packGoalService.createEntry(packName);

		userProfile.setPack(packName);
		userProfileAccessor.setEntry(userProfile);
		return HttpServletResponse.SC_OK;
	}

//	Delete pack?

	public int joinPack(String username, String packName, String password) {
		UserProfile userProfile = userProfileAccessor.getEntry(username);
		if (userProfile.getPack() != null && !userProfile.getPack().isEmpty())
			return HttpServletResponse.SC_CONFLICT;

		PackData packData = packDataAccessor.getEntry(packName);
		if (packData==null) return HttpServletResponse.SC_NOT_FOUND;
		if (packData.getMemberList().contains(username)) return HttpServletResponse.SC_OK;

		String hashedPassword = packData.getHashedPassword();
		if (!passwordEncoder.matches(password, hashedPassword))
			return HttpServletResponse.SC_UNAUTHORIZED;


		packData.getMemberList().add(username);
		packData.getMemberList().sort(String::compareTo);
		packDataAccessor.setEntry(packData);


		userProfile.setPack(packName);
		userProfileAccessor.setEntry(userProfile);
		return HttpServletResponse.SC_OK;
	}

	public int leavePack(String username) {
		UserProfile userProfile = userProfileAccessor.getEntry(username);
		if (userProfile.getPack() == null || userProfile.getPack().isEmpty())
			return HttpServletResponse.SC_OK;

		String packName = userProfile.getPack();
		PackData packData = packDataAccessor.getEntry(packName);
		if (packData==null) return HttpServletResponse.SC_NOT_FOUND;

		if (username.equals(packData.getOwner())) return HttpServletResponse.SC_CONFLICT;

		packData.getMemberList().remove(username);
		packDataAccessor.setEntry(packData);

		packGoalService.removeUser(packName, username);

		userProfile.setPack("");
		userProfileAccessor.setEntry(userProfile);
		return HttpServletResponse.SC_OK;
	}

	public int leavePackAsOwner(String username, String newOwner) {
		UserProfile userProfile = userProfileAccessor.getEntry(username);
		if (userProfile.getPack() == null || userProfile.getPack().isEmpty())
			return HttpServletResponse.SC_OK;

		if (username.equals(newOwner)) return HttpServletResponse.SC_CONFLICT;

		String packName = userProfile.getPack();
		PackData packData = packDataAccessor.getEntry(packName);
		if (packData==null) return HttpServletResponse.SC_NOT_FOUND;

		if (packData.getMemberList().size() > 1 && !packData.getMemberList().contains(newOwner))
			return HttpServletResponse.SC_CONFLICT;

		if (packData.getMemberList().size() == 1) {
			packDataAccessor.deleteEntry(packName);
			packGoalService.deleteEntry(packName);
			return HttpServletResponse.SC_OK;
		}

		packData.getMemberList().remove(username);
		packData.setOwner(newOwner);
		packDataAccessor.setEntry(packData);

		packGoalService.removeUser(packName, username);

		userProfile.setPack("");
		userProfileAccessor.setEntry(userProfile);
		return HttpServletResponse.SC_OK;
	}

}
