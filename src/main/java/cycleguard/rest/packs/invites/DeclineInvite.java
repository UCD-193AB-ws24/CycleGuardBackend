package cycleguard.rest.packs.invites;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.packs.PackDataService;
import cycleguard.database.packs.PackInvitesAccessor;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class DeclineInvite {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private PackDataService packDataService;
	@Autowired
	private PackInvitesAccessor packInvitesAccessor;

	/**
	 * Declines a current pack invitation.
	 * @param packNameBody Pack to decline
	 * @return 200 on success or if already declined<br>
	 * 404 if pack not existent
	 */
	@PostMapping("/packs/declineInvite")
	public void declineInvite(@RequestHeader("Token") String token, HttpServletResponse response,
	                                @RequestBody @NonNull PackName packNameBody) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		String packName = packNameBody.packName;

		response.setStatus(packDataService.declineInvite(username, packName));
	}
}