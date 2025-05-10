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
public final class AcceptInvite {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private PackDataService packDataService;
	@Autowired
	private PackInvitesAccessor packInvitesAccessor;

	/**
	 * Accept a pack invite.
	 * @param packNameBody Name of the pack
	 * @return 200 on success, or already in pack<br>
	 * 400 on malformed pack name<br>
	 * 401 on password mismatch<br>
	 * 404 if pack not existent<br>
	 * 409 if user already in another pack
	 */
	@PostMapping("/packs/acceptInvite")
	public void acceptInvite(@RequestHeader("Token") String token, HttpServletResponse response,
	                                @RequestBody @NonNull PackName packNameBody) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		String packName = packNameBody.packName;

		response.setStatus(packDataService.acceptInvite(username, packName));
	}
}