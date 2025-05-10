package cycleguard.rest.packs;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.packs.PackDataService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class CancelPackGoal {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private PackDataService packDataService;

	/**
	 * Cancels a pack's current goal.
	 * @return 200 on success<br>
	 * 401 if user is not pack owner<br>
	 * 404 if pack not existent
	 */
	@PostMapping("/packs/cancelGoal")
	public void leavePackAsOwner(@RequestHeader("Token") String token, HttpServletResponse response) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		int status = packDataService.cancelCurrentGoal(username);
		response.setStatus(status);
	}
}