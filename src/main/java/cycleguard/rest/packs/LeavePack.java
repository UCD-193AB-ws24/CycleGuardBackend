package cycleguard.rest.packs;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.HealthInfoAccessor.HealthInfo;
import cycleguard.database.packs.PackDataService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class LeavePack {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private PackDataService packDataService;

	/**
	 * Leave a pack as a non-owner pack member.
	 * @return 200 on success, or already not in pack<br>
	 * 400 on malformed pack name<br>
	 * 404 if pack not existent<br>
	 * 409 if user is pack owner
	 */
	@PostMapping("/packs/leavePack")
	public void leavePack(@RequestHeader("Token") String token, HttpServletResponse response) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		int status = packDataService.leavePack(username);
		response.setStatus(status);
	}
}