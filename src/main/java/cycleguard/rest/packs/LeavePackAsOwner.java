package cycleguard.rest.packs;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.HealthInfoAccessor.HealthInfo;
import cycleguard.database.packs.packData.PackDataService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint for a user to enter in health metrics.
 * Requires {@link HealthInfo} as body.
 */
@RestController
public final class LeavePackAsOwner {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private PackDataService packDataService;

	@PostMapping("/packs/leavePackAsOwner")
	public void leavePackAsOwner(@RequestHeader("Token") String token, HttpServletResponse response,
	                                   @RequestBody @NonNull NewOwner newOwner) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		String newOwnerUsername = newOwner.newOwner;

		int status = packDataService.leavePackAsOwner(username, newOwnerUsername);
		response.setStatus(status);
	}

	public static class NewOwner {
		public String newOwner;
	}
}
