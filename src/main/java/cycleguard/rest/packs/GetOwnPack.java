package cycleguard.rest.packs;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.HealthInfoAccessor.HealthInfo;
import cycleguard.database.accessor.UserProfileAccessor;
import cycleguard.database.packs.packData.PackData;
import cycleguard.database.packs.packData.PackDataService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static cycleguard.database.accessor.UserProfileAccessor.UserProfile;

/**
 * Endpoint for a user to enter in health metrics.
 * Requires {@link HealthInfo} as body.
 */
@RestController
public final class GetOwnPack {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private PackDataService packDataService;
	@Autowired
	private UserProfileAccessor userProfileAccessor;

	@GetMapping("/packs/getPack")
	public PackData getPack(@RequestHeader("Token") String token, HttpServletResponse response) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		String packName = userProfileAccessor.getEntry(username).getPack();
		if (packName==null || packName.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		return packDataService.getPack(packName);
	}
}