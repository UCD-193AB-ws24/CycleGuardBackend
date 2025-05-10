package cycleguard.rest.packs;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.HealthInfoAccessor.HealthInfo;
import cycleguard.database.accessor.UserProfileAccessor;
import cycleguard.database.packs.PackData;
import cycleguard.database.packs.PackDataService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class GetOwnPack {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private PackDataService packDataService;
	@Autowired
	private UserProfileAccessor userProfileAccessor;

	/**
	 * Retrieves user's current pack.
	 * @return {@link PackData}, null if doesn't exist
	 */
	@GetMapping("/packs/getPack")
	public PackData getPack(@RequestHeader("Token") String token, HttpServletResponse response) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		String packName = userProfileAccessor.getEntryOrDefaultBlank(username).getPack();
		if (packName==null || packName.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		return packDataService.getPack(packName);
	}
}