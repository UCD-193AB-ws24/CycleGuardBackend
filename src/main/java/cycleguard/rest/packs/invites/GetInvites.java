package cycleguard.rest.packs.invites;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.HealthInfoAccessor.HealthInfo;
import cycleguard.database.accessor.UserProfileAccessor;
import cycleguard.database.packs.PackData;
import cycleguard.database.packs.PackDataService;
import cycleguard.database.packs.PackInvitesAccessor;
import cycleguard.database.packs.PackInvitesAccessor.PackInvites;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint for a user to enter in health metrics.
 * Requires {@link HealthInfo} as body.
 */
@RestController
public final class GetInvites {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private PackInvitesAccessor packInvitesAccessor;

	/**
	 * Retrieve the packs a user has been invited to.
	 * @return Non-null {@link PackInvites}
	 */
	@GetMapping("/packs/getInvites")
	public PackInvites getInvites(@RequestHeader("Token") String token, HttpServletResponse response) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		return packInvitesAccessor.getEntryOrDefaultBlank(username);
	}
}