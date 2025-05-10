package cycleguard.rest.packs;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.packs.PackDataService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class JoinPack {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private PackDataService packDataService;

	/**
	 * Join a pack. Updates pack and profile information.
	 * @param credentials Name and password of pack
	 * @return 200 on success, or already in pack<br>
	 * 400 on malformed pack name<br>
	 * 401 on password mismatch<br>
	 * 404 if pack not existent<br>
	 * 409 if user already in another pack
	 */
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
