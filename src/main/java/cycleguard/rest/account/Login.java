package cycleguard.rest.account;

import cycleguard.auth.AccessTokenManager;
import cycleguard.auth.AccountCredentials;
import cycleguard.auth.AccountService;
import cycleguard.database.accessor.UserCredentialsAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint for a user to log in.
 * Requires {@link AccountCredentials} as body.
 * <br>
 * Returns an authentication token that must be set in every request's header, with header key <code>Token</code>.
 */
@RestController
public final class Login {
	@Autowired
	private AccountService accountService;
	@Autowired
	private UserCredentialsAccessor userCredentialsAccessor;

	@Autowired
	private AccessTokenManager accessTokenManager;


	@PostMapping("/account/login")
	public String login(@RequestBody @NonNull AccountCredentials credentials) {
		if (!accountService.isValidLogin(credentials)) return "INVALID";

		String token = accessTokenManager.setRandomNewToken(credentials.getUsername());
		return token;
	}
}
