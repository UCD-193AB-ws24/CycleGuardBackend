package cycleguard.rest.packs;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.HealthInfoAccessor.HealthInfo;
import cycleguard.database.packs.PackDataService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class KickUser {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private PackDataService packDataService;

	/**
	 * Removes a user from the pack.
	 * @param userToKick User to kick
	 * @return 200 on success, or if user already kicked<br>
	 * 401 if user is not the current owner
	 * 404 if pack not existent
	 * 409 if username equals user to kick, or user not in pack
	 */
	@PostMapping("/packs/kickUser")
	public void kickUser(@RequestHeader("Token") String token, HttpServletResponse response,
	                                   @RequestBody @NonNull UserToKick userToKick) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		String newOwnerUsername = userToKick.username;

		int status = packDataService.kickUser(username, newOwnerUsername);
		response.setStatus(status);
	}

	public static class UserToKick {
		public String username;
	}
}
