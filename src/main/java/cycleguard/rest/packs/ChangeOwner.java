package cycleguard.rest.packs;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.HealthInfoAccessor.HealthInfo;
import cycleguard.database.packs.PackDataService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class ChangeOwner {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private PackDataService packDataService;

	/**
	 * Changes the pack's owner.
	 * @param newOwner New owner
	 * @return 200 on success<br>
	 * 401 if user is not the current owner
	 * 404 if pack not existent
	 * 409 if username equals new owner, or new owner not in members list
	 */
	@PostMapping("/packs/changeOwner")
	public void changeOwner(@RequestHeader("Token") String token, HttpServletResponse response,
	                                   @RequestBody @NonNull NewOwner newOwner) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		String newOwnerUsername = newOwner.newOwner;

		int status = packDataService.changeOwner(username, newOwnerUsername);
		response.setStatus(status);
	}

	public static class NewOwner {
		public String newOwner;
	}
}
