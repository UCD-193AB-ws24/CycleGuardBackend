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
public final class LeavePackAsOwner {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private PackDataService packDataService;

	/**
	 * Leave a pack as the owner of the pack.
	 * @param newOwner Username of new owner
	 * @return 200 on success, or already not in pack<br>
	 * 400 on malformed pack name<br>
	 * 404 if pack not existent<br>
	 * 409 if user is pack owner
	 */
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
