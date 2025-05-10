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
public final class CancelInvite {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private PackDataService packDataService;
	@Autowired
	private PackInvitesAccessor packInvitesAccessor;

	/**
	 * Cancel a pack invite to a user.
	 * @param usernameBody Username to cancel invite
	 * @return 200 on success or already cancelled<br>
	 * 404 if pack not existent
	 */
	@PostMapping("/packs/cancelInvite")
	public void cancelInvite(@RequestHeader("Token") String token, HttpServletResponse response,
	                                @RequestBody @NonNull Username usernameBody) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		response.setStatus(packDataService.cancelInvite(username, usernameBody.username));
	}
}