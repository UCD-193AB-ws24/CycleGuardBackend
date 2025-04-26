package cycleguard.rest.packs.invites;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.HealthInfoAccessor.HealthInfo;
import cycleguard.database.packs.PackData;
import cycleguard.database.packs.PackDataService;
import cycleguard.database.packs.PackInvitesAccessor;
import cycleguard.database.packs.PackInvitesAccessor.PackInvites;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoint for a user to enter in health metrics.
 * Requires {@link HealthInfo} as body.
 */
@RestController
public final class AcceptInvite {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private PackDataService packDataService;
	@Autowired
	private PackInvitesAccessor packInvitesAccessor;

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