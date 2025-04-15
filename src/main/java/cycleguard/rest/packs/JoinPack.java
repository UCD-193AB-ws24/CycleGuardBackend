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
public final class JoinPack {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private PackDataService packDataService;

	@PostMapping("/packs/joinPack")
	public void joinPack(@RequestHeader("Token") String token, HttpServletResponse response,
	                                   @RequestBody @NonNull PackCredentials credentials) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		int status = packDataService.joinPack(username, credentials.packName, credentials.password);
		response.setStatus(status);
	}
}
