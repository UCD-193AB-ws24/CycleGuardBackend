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
public final class CreatePack {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private PackDataService packDataService;

	/**
	 * Create a new pack. Returns an HTTP status code, regarding status.
	 * @param credentials Name and password of pack
	 * @return 200 on success<br>
	 * 400 on malformed pack name<br>
	 * 409 on conflict with another pack name, or if user already in another pack
	 */
	@PostMapping("/packs/createPack")
	public void createPack(@RequestHeader("Token") String token, HttpServletResponse response,
	                                   @RequestBody @NonNull PackCredentials credentials) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		int status = packDataService.createPack(username, credentials.packName, credentials.password);
		response.setStatus(status);
	}
}
