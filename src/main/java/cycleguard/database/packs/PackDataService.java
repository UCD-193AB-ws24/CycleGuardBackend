package cycleguard.database.packs;

import cycleguard.database.RideProcessable;
import cycleguard.database.accessor.UserCredentialsAccessor;
import cycleguard.database.accessor.UserProfileAccessor;
import cycleguard.database.accessor.UserProfileAccessor.UserProfile;
import cycleguard.database.rides.ProcessRideService.RideInfo;
import cycleguard.util.TimeUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static cycleguard.database.packs.PackInvitesAccessor.*;

/**
 * Service wrapper for retrieving and modifying {@link PackData}.
 */
@Service
public class PackDataService implements RideProcessable {
	@Autowired
	private UserProfileAccessor userProfileAccessor;
	@Autowired
	private PackDataAccessor packDataAccessor;
	@Autowired
	private PackGoalService packGoalService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Retrieves a pack from its pack name.
	 * @param packName Name of pack to retrieve
	 * @return {@link PackData}, null if doesn't exist
	 */
	public PackData getPack(String packName) {
		PackData packData = packDataAccessor.getEntry(packName);
		if (packData==null) return null;

		PackGoal packGoal = packDataAccessor.getEntry(packName).getPackGoal();
		if (packGoal.getEndTime() < TimeUtil.getCurrentSecond() && packGoal.getActive()) {
			packGoal.setActive(false);
			packDataAccessor.setEntry(packData);
		}
		return packData;
	}

	/**
	 * Return if or not a pack exists.
	 * @param packName Name of pack
	 * @return If the pack exists
	 */
	public boolean packExists(String packName) {
		return packDataAccessor.hasEntry(packName);
	}

	/**
	 * Create a new pack. Returns an HTTP status code, regarding status.
	 * @param username Username of pack creator
	 * @param packName Name of pack
	 * @param password Password of pack, to enter the pack
	 * @return 200 on success<br>
	 * 400 on malformed pack name<br>
	 * 409 on conflict with another pack name, or if user already in another pack
	 */
	public int createPack(String username, String packName, String password) {
		if (packName==null) return HttpServletResponse.SC_BAD_REQUEST;
		packName = packName.trim();
		if (packName.isEmpty()) return HttpServletResponse.SC_BAD_REQUEST;

		UserProfile userProfile = userProfileAccessor.getEntryOrDefaultBlank(username);
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

		userProfile.setPack(packName);
		userProfileAccessor.setEntry(userProfile);
		return HttpServletResponse.SC_OK;
	}

	/**
	 * Join a pack. Updates pack and profile information.
	 * @param username Username of joinee
	 * @param packName Name of pack
	 * @param password Password of pack, to enter the pack
	 * @return 200 on success, or already in pack<br>
	 * 400 on malformed pack name<br>
	 * 401 on password mismatch<br>
	 * 404 if pack not existent<br>
	 * 409 if user already in another pack
	 */
	public int joinPack(String username, String packName, String password) {
		UserProfile userProfile = userProfileAccessor.getEntryOrDefaultBlank(username);
		if (userProfile.getPack() != null && !userProfile.getPack().isEmpty()) {
			if (userProfile.getPack().equals(packName)) return HttpServletResponse.SC_OK;
			return HttpServletResponse.SC_CONFLICT;
		}


		PackData packData = packDataAccessor.getEntry(packName);
		if (packData==null) return HttpServletResponse.SC_NOT_FOUND;
		if (packData.getMemberList().contains(username)) return HttpServletResponse.SC_OK;

		if (!packData.getInvites().contains(username)) {
			String hashedPassword = packData.getHashedPassword();
			if (!passwordEncoder.matches(password, hashedPassword))
				return HttpServletResponse.SC_UNAUTHORIZED;
		}


		packData.getMemberList().add(username);
		packData.getMemberList().sort(String::compareTo);
		packData.getInvites().remove(username);
		packDataAccessor.setEntry(packData);


		userProfile.setPack(packName);
		userProfileAccessor.setEntry(userProfile);

		var invites = packInvitesAccessor.getEntryOrDefaultBlank(username);
		if (invites.getInvites().contains(packName)) {
			invites.getInvites().remove(packName);
			packInvitesAccessor.setEntry(invites);
		}

		return HttpServletResponse.SC_OK;
	}

	/**
	 * Leave a pack as a non-owner pack member.
	 * @param username Username of leaving user
	 * @return 200 on success, or already not in pack<br>
	 * 400 on malformed pack name<br>
	 * 404 if pack not existent<br>
	 * 409 if user is pack owner
	 */
	public int leavePack(String username) {
		UserProfile userProfile = userProfileAccessor.getEntry(username);
		if (userProfile.getPack() == null || userProfile.getPack().isEmpty())
			return HttpServletResponse.SC_OK;

		String packName = userProfile.getPack();
		PackData packData = packDataAccessor.getEntry(packName);
		if (packData==null) return HttpServletResponse.SC_NOT_FOUND;

		if (username.equals(packData.getOwner())) return HttpServletResponse.SC_CONFLICT;

		packData.getMemberList().remove(username);
		packData.getPackGoal().getContributionMap().remove(username);
		packDataAccessor.setEntry(packData);

		userProfile.setPack("");
		userProfileAccessor.setEntry(userProfile);
		return HttpServletResponse.SC_OK;
	}

	/**
	 * Leave a pack as the owner of the pack.
	 * @param username Username of leaving owner
	 * @param newOwner Username of new owner
	 * @return 200 on success, or already not in pack<br>
	 * 400 on malformed pack name<br>
	 * 404 if pack not existent<br>
	 * 409 if user is pack owner
	 */
	public int leavePackAsOwner(String username, String newOwner) {
		UserProfile userProfile = userProfileAccessor.getEntry(username);
		if (userProfile.getPack() == null || userProfile.getPack().isEmpty())
			return HttpServletResponse.SC_OK;

		if (username.equals(newOwner)) return HttpServletResponse.SC_CONFLICT;

		String packName = userProfile.getPack();
		PackData packData = packDataAccessor.getEntry(packName);
		if (packData==null) return HttpServletResponse.SC_NOT_FOUND;

		if (!packData.getOwner().equals(username)) return HttpServletResponse.SC_UNAUTHORIZED;

		if (packData.getMemberList().size() > 1 && !packData.getMemberList().contains(newOwner))
			return HttpServletResponse.SC_CONFLICT;

		if (packData.getMemberList().size() == 1) {
			packDataAccessor.deleteEntry(packName);
		} else {
			packData.getMemberList().remove(username);
			packData.setOwner(newOwner);
			packData.getPackGoal().getContributionMap().remove(username);
			packDataAccessor.setEntry(packData);
		}


		userProfile.setPack("");
		userProfileAccessor.setEntry(userProfile);
		return HttpServletResponse.SC_OK;
	}

	@Override
	public void processNewRide(String username, RideInfo rideInfo, Instant now) {
		UserProfile userProfile = userProfileAccessor.getEntryOrDefaultBlank(username);
		if (userProfile.getPack() == null || userProfile.getPack().isEmpty()) return;

		PackData packData = packDataAccessor.getEntry(userProfile.getPack());
	    packGoalService.updateContribution(username, packData, rideInfo);

		packDataAccessor.setEntry(packData);
	}

	/**
	 * Set the current pack goal.
	 * @param username User performing action
	 * @param durationSeconds Duration of new pack goal
	 * @param goalField Distance or time
	 * @param goalAmount Total amount to complete goal
	 * @return 200 on success<br>
	 * 400 on malformed request<br>
	 * 401 if user is not pack owner<br>
	 * 404 if pack not existent
	 */
	public int setGoal(String username, long durationSeconds, String goalField, long goalAmount) {
		UserProfile userProfile = userProfileAccessor.getEntryOrDefaultBlank(username);
		if (userProfile.getPack() == null || userProfile.getPack().isEmpty())
			return HttpServletResponse.SC_NOT_FOUND;

		PackData packData = packDataAccessor.getEntry(userProfile.getPack());
		if (!username.equals(packData.getOwner())) return HttpServletResponse.SC_UNAUTHORIZED;

		int res = packGoalService.setGoal(packData, durationSeconds, goalField, goalAmount);
		if (res == 200) packDataAccessor.setEntry(packData);
		return res;
	}

	/**
	 * Cancels the current pack goal.
	 * @param username User performing action
	 * @return 200 on success<br>
	 * 401 if user is not pack owner<br>
	 * 404 if pack not existent
	 */
	public int cancelCurrentGoal(String username) {
		UserProfile userProfile = userProfileAccessor.getEntryOrDefaultBlank(username);
		if (userProfile.getPack() == null || userProfile.getPack().isEmpty())
			return HttpServletResponse.SC_NOT_FOUND;

		PackData packData = packDataAccessor.getEntry(userProfile.getPack());

		if (!username.equals(packData.getOwner())) return HttpServletResponse.SC_UNAUTHORIZED;

		packGoalService.clearGoal(packData);

		packDataAccessor.setEntry(packData);

		return HttpServletResponse.SC_OK;
	}



	@Autowired
	private UserCredentialsAccessor userCredentialsAccessor;
	@Autowired
	private PackInvitesAccessor packInvitesAccessor;

	/**
	 * Invites a user to the pack.
	 * @param username User performing action
	 * @param userToInvite User being invited
	 * @return 200 on success or if already invited<br>
	 * 404 if users not existent
	 */
	public int inviteUser(String username, String userToInvite) {
		if (!userCredentialsAccessor.hasEntry(userToInvite))
			return HttpServletResponse.SC_NOT_FOUND;

		UserProfile userProfile = userProfileAccessor.getEntryOrDefaultBlank(username);
		PackData packData = packDataAccessor.getEntry(userProfile.getPack());

		if (packData.getMemberList().contains(userToInvite)) return HttpServletResponse.SC_OK;
		if (packData.getInvites().contains(userToInvite)) return HttpServletResponse.SC_OK;

		String packName = packData.getName();
		PackInvites packInvites = packInvitesAccessor.getEntryOrDefaultBlank(userToInvite);
		if (packInvites.getInvites().contains(packName)) return HttpServletResponse.SC_OK;

		{
			List<String> curInvites = packInvites.getInvites();
			curInvites.add(packName);
			curInvites.sort(String::compareTo);
			packInvites.setInvites(curInvites);
		}
		packInvitesAccessor.setEntry(packInvites);

		{
			List<String> curInvites = packData.getInvites();
			curInvites.add(userToInvite);
			curInvites.sort(String::compareTo);
			packData.setInvites(curInvites);
		}
		packDataAccessor.setEntry(packData);

		return HttpServletResponse.SC_OK;
	}

	/**
	 * Joins the pack as an invited user.
	 * @param username User accepting invite
	 * @param packName Pack to join
	 * @return 200 on success, or already in pack<br>
	 * 400 on malformed pack name<br>
	 * 401 on password mismatch<br>
	 * 404 if pack not existent<br>
	 * 409 if user already in another pack
	 */
	public int acceptInvite(String username, String packName) {
		return joinPack(username, packName, "");
	}

	/**
	 * Cancels a current pack invitation.
	 * @param username User performing action
	 * @param otherUser User with invitation
	 * @return 200 on success or already cancelled<br>
	 * 404 if pack not existent
	 */
	public int cancelInvite(String username, String otherUser) {
		UserProfile userProfile = userProfileAccessor.getEntryOrDefaultBlank(username);
		if (userProfile.getPack() == null || userProfile.getPack().isEmpty())
			return HttpServletResponse.SC_NOT_FOUND;

		PackData packData = packDataAccessor.getEntry(userProfile.getPack());
		String packName = packData.getName();

		return declineInvite(otherUser, packName);
	}

	/**
	 * Declines a current pack invitation.
	 * @param username User performing action
	 * @param packName Pack invitation to decline
	 * @return 200 on success or if already declined<br>
	 * 404 if pack not existent
	 */
	public int declineInvite(String username, String packName) {
		PackInvites packInvites = packInvitesAccessor.getEntryOrDefaultBlank(username);
		if (!packInvites.getInvites().contains(packName)) return HttpServletResponse.SC_OK;

		PackData packData = packDataAccessor.getEntry(packName);
		packData.getInvites().remove(username);
		packInvites.getInvites().remove(packName);

		packDataAccessor.setEntry(packData);
		packInvitesAccessor.setEntry(packInvites);

		return HttpServletResponse.SC_OK;
	}

	/**
	 * Changes the pack's owner.
	 * @param username Owner performing action
	 * @param newOwner New owner
	 * @return 200 on success<br>
	 * 401 if user is not the current owner
	 * 404 if pack not existent
	 * 409 if username equals new owner, or new owner not in members list
	 */
	public int changeOwner(String username, String newOwner) {
		UserProfile userProfile = userProfileAccessor.getEntry(username);
		if (userProfile.getPack() == null || userProfile.getPack().isEmpty())
			return HttpServletResponse.SC_OK;

		if (username.equals(newOwner)) return HttpServletResponse.SC_CONFLICT;

		String packName = userProfile.getPack();
		PackData packData = packDataAccessor.getEntry(packName);
		if (packData==null) return HttpServletResponse.SC_NOT_FOUND;

		if (!packData.getOwner().equals(username)) return HttpServletResponse.SC_UNAUTHORIZED;

		if (packData.getMemberList().size() > 1 && !packData.getMemberList().contains(newOwner))
			return HttpServletResponse.SC_CONFLICT;

		packData.setOwner(newOwner);
		packDataAccessor.setEntry(packData);

		return HttpServletResponse.SC_OK;
	}

	/**
	 * Removes a user from the pack.
	 * @param username Owner performing action
	 * @param user User to kick
	 * @return 200 on success, or if user already kicked<br>
	 * 401 if user is not the current owner
	 * 404 if pack not existent
	 * 409 if username equals user to kick, or user not in pack
	 */
	public int kickUser(String username, String user) {
		UserProfile userProfile = userProfileAccessor.getEntry(username);
		if (userProfile.getPack() == null || userProfile.getPack().isEmpty())
			return HttpServletResponse.SC_OK;

		if (username.equals(user)) return HttpServletResponse.SC_CONFLICT;

		String packName = userProfile.getPack();
		PackData packData = packDataAccessor.getEntry(packName);
		if (packData==null) return HttpServletResponse.SC_NOT_FOUND;

		if (!packData.getOwner().equals(username)) return HttpServletResponse.SC_UNAUTHORIZED;

		if (packData.getMemberList().size() > 1 && !packData.getMemberList().contains(user))
			return HttpServletResponse.SC_CONFLICT;

		packData.getMemberList().remove(user);
		packData.getPackGoal().getContributionMap().remove(user);
		packDataAccessor.setEntry(packData);


		UserProfile userToKick = userProfileAccessor.getEntry(user);
		userToKick.setPack("");
		userProfileAccessor.setEntry(userToKick);
		return HttpServletResponse.SC_OK;
	}
}
