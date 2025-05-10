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
public final class SendInvite {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private PackDataService packDataService;
	@Autowired
	private PackInvitesAccessor packInvitesAccessor;

	/**
	 * Invites a user to the pack.
	 * @param usernameBody User being invited
	 * @return 200 on success or if already invited<br>
	 * 404 if users not existent
	 */
	@PostMapping("/packs/sendInvite")
	public void sendInvite(@RequestHeader("Token") String token, HttpServletResponse response,
	                                @RequestBody @NonNull Username usernameBody) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		int status = packDataService.inviteUser(username, usernameBody.username);
		response.setStatus(status);
	}
}