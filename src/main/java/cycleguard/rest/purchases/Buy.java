package cycleguard.rest.purchases;

import cycleguard.auth.AccessTokenManager;
import cycleguard.auth.AccountCredentials;
import cycleguard.auth.AuthHeader;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoint for a user to create an account.
 * Requires {@link AccountCredentials} as body.
 */
@RestController
public final class Buy {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@PostMapping("/purchaseInfo/buy")
	public HttpStatus buy(@RequestHeader("Token") String token, HttpServletResponse response,
	                      @RequestBody @NonNull ItemToBuy itemToBuy) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return HttpStatus.UNAUTHORIZED;
		}
		System.out.println(token);
		System.out.println(itemToBuy.item);
		return HttpStatus.OK;
	}

	private static final class ItemToBuy {
		private String item;

		public String getItem() {
			return item;
		}

		public void setItem(String item) {
			this.item = item;
		}
	}
}
